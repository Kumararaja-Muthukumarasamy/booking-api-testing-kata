package com.booking;

import org.junit.platform.suite.api.*;

import static io.cucumber.core.options.Constants.*;

@Suite
@IncludeEngines("cucumber")
@SelectPackages("com.booking")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.booking")
@ConfigurationParameter(
        key = PLUGIN_PROPERTY_NAME,
        value = "pretty," +
                "html:target/cucumber-reports.html," +
                "json:target/cucumber.json," +
                "rerun:target/rerun.txt"
)
public class TestRunner {
}