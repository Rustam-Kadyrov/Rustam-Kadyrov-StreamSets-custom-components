package com.demo.streamsets.processor;

import com.demo.streamsets.util.Errors;
import com.streamsets.pipeline.api.Field;
import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.api.StageException;
import com.streamsets.pipeline.api.base.SingleLaneRecordProcessor;
import com.streamsets.pipeline.api.impl.Utils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

import static java.util.Objects.nonNull;

public abstract class OxfordDictSearchProcessor extends SingleLaneRecordProcessor {

    public abstract String getInputFieldPath();

    @Override
    protected void process(Record record, SingleLaneBatchMaker batchMaker) throws StageException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            String value = record.get(getInputFieldPath()).getValueAsString();

            HttpHost host = new HttpHost("od-api.oxforddictionaries.com", 443, "https");
            HttpGet request = new HttpGet("/api/v1/search/en?q=" + value + "&prefix=false&limit=5");
            request.addHeader("Accept", "application/json");
            request.addHeader("app_id", System.getenv("OXFORD_APP_ID"));
            request.addHeader("app_key", System.getenv("OXFORD_APP_KEY"));

            CloseableHttpResponse response = httpClient.execute(host, request);
            HttpEntity entity = response.getEntity();
            Record recordOut = getContext().cloneRecord(record);

            String result;
            if (nonNull(entity)) {
                result = EntityUtils.toString(entity);
            } else {
                result = "NOT FOUND";
            }
            recordOut.set("/response", Field.create(result));
            batchMaker.addRecord(recordOut);
        } catch (Exception e) {
            switch (getContext().getOnErrorRecord()) {
                case DISCARD:
                    break;
                case TO_ERROR:
                    getContext().toError(record, e);
                    break;
                case STOP_PIPELINE:
                    throw new StageException(Errors.STAGE, e.toString());
                default:
                    throw new IllegalStateException(Utils.format("It should never happen. OnError '{}'",
                            getContext().getOnErrorRecord(), e));
            }
        }
//        finally {
//            try {
//                httpClient.close();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }
}
