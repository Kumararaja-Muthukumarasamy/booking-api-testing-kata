package com.booking.runner;

import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(
        key = GLUE_PROPERTY_NAME,
        value = "com.booking"
)
@ConfigurationParameter(
        key = PLUGIN_PROPERTY_NAME,
        value =
                "pretty," +
                        "html:target/reports/cucumber.html," +
                        "json:target/reports/cucumber.json," +
                        "rerun:target/rerun.txt"
)
@ConfigurationParameter(
        key = "cucumber.filter.tags",
        value = "not @patch"
)

public class TestRunner {
}