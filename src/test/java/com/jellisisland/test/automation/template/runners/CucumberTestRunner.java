package com.jellisisland.test.automation.template.runners;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, 
    value = "io.cucumber.core.plugin.SerenityReporterParallel,pretty,timeline:build/test-results")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, 
    value = "com.jellisisland.test.automation.template.stepdefinitions")
@ConfigurationParameter(key = Constants.FILTER_TAGS_PROPERTY_NAME, value = "")
@ConfigurationParameter(key = Constants.FEATURES_PROPERTY_NAME, value = "src/test/resources/features")
public class CucumberTestRunner {
}