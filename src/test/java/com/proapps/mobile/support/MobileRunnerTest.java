package com.proapps.mobile.support;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

//Features is the folder that contains your feature files
//Glue is the package that contains your step definition classes
//Pretty is the report format, you can add or edit
@CucumberOptions(
      strict = true,
      plugin = {"pretty", "html:target/mobile/cucumber", "json:target/mobile/cucumber.json"},
      features = "src/test/resources/features/mobile",
      glue = {"com.proapps.mobile.steps", "com.proapps.mobile.support", "com.proapps.common.support"}
)

public class MobileRunnerTest extends AbstractTestNGCucumberTests {

}
