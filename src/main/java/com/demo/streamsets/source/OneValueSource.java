/*
 * Copyright 2017 StreamSets Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.demo.streamsets.source;

import com.demo.streamsets.util.Errors;
import com.demo.streamsets.util.Groups;
import com.streamsets.pipeline.api.BatchMaker;
import com.streamsets.pipeline.api.Field;
import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.api.StageException;
import com.streamsets.pipeline.api.base.BaseSource;

import java.util.*;

/**
 * This source generates one value
 */
public abstract class OneValueSource extends BaseSource {

    private static final String END_MARKER = "END_MARKER";

    /**
     * Gives access to the UI configuration of the stage provided by the {@link OneValueDSource} class.
     */
    public abstract String getValue();

    public abstract String getOutputFieldName();

    public abstract Integer getTimeoutBeforeFinish();

    private long startTime;

    @Override
    protected List<ConfigIssue> init() {
        // Validate configuration values and open any required resources.
        List<ConfigIssue> issues = super.init();

        if (Objects.isNull(getValue()) || getValue().length() == 0) {
            issues.add(
                    getContext().createConfigIssue(
                            Groups.OneValueSource.ONE_VALUE.name(), "config", Errors.ONE_VALUE_00, "Value is empty"
                    )
            );
        }

        // If issues is not empty, the UI will inform the user of each configuration issue in the list.
        return issues;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        // Clean up any open resources.
        super.destroy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String produce(String lastSourceOffset, int maxBatchSize, BatchMaker batchMaker) throws StageException {

        if (END_MARKER.equals(lastSourceOffset)) {
            if (System.currentTimeMillis() - startTime > getTimeoutBeforeFinish()) {
                return null;
            }
        } else {
            startTime = System.currentTimeMillis();

            Record record = getContext().createRecord(UUID.randomUUID().toString());
            Map<String, Field> map = new HashMap<>();
            map.put(getOutputFieldName(), Field.create(getValue()));
            record.set(Field.create(map));
            batchMaker.addRecord(record);
        }

        return END_MARKER;
    }
}
