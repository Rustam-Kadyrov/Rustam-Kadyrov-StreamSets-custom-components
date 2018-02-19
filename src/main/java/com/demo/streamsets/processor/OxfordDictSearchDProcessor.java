package com.demo.streamsets.processor;

import com.demo.streamsets.util.Groups;
import com.streamsets.pipeline.api.ConfigDef;
import com.streamsets.pipeline.api.ConfigGroups;
import com.streamsets.pipeline.api.GenerateResourceBundle;
import com.streamsets.pipeline.api.StageDef;

@StageDef(
        version = 1,
        label = "Oxford Dict Search",
        description = "Searches in Oxford dict",
        icon = "default.png",
        onlineHelpRefUrl = ""
)
@ConfigGroups(value = Groups.OxfordProcessor.class)
@GenerateResourceBundle
public class OxfordDictSearchDProcessor extends OxfordDictSearchProcessor {

    @ConfigDef(
            required = true,
            type = ConfigDef.Type.STRING,
            defaultValue = "/",
            label = "Input field path",
            displayPosition = 10,
            group = "OXFORD",
            description = "Input field path")
    public String inputFieldPath;

    @ConfigDef(
            required = true,
            type = ConfigDef.Type.STRING,
            defaultValue = "/response",
            label = "Output field path",
            displayPosition = 20,
            group = "OXFORD",
            description = "Output field path")
    public String outputField;

    @ConfigDef(
            required = true,
            type = ConfigDef.Type.NUMBER,
            defaultValue = "10",
            label = "Limit results",
            displayPosition = 30,
            group = "OXFORD",
            description = "Limit results")
    public Integer limit;

    @Override
    public String getInputFieldPath() {
        return inputFieldPath;
    }

    @Override
    public String getOutputField() {
        return outputField;
    }

    @Override
    public Integer getLimit() {
        return limit;
    }
}
