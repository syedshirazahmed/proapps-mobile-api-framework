package com.proapps.api.support;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

//Features is the folder that contains your feature files
//Glue is the package that contains your step definition classes
//Pretty is the report format, you can add or edit
@CucumberOptions(
      strict = true,
      plugin = {"pretty", "html:target/api/cucumber", "json:target/api/cucumber.json"},
      features = "src/test/resources/features/api",
      glue = {"com.proapps.api.steps", "com.proapps.api.support", "com.proapps.common.support"}
)

public class APIRunnerTest extends AbstractTestNGCucumberTests {

}
