package com.proapps.common.support;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.google.inject.Inject;
import com.proapps.api.support.CustomLogFilter;
import com.proapps.mobile.support.World;

import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.Scenario;
import io.restassured.specification.RequestSpecification;

@ScenarioScoped
public class ScenarioUtils {
	
	@Inject
	private World world;
	
	public Scenario scenario;
	
	public void embedScreenshot(String... screenshotName)
	{
		String screenshot = screenshotName.length > 0 ? screenshotName[0] : "Mobile Screenshot";
		scenario.embed(((TakesScreenshot)world.driver).getScreenshotAs(OutputType.BYTES), "image/png", screenshot);
	}
	
	public void write(String text)
	{
		scenario.write(text);
	}
	
	public void writeRequestResponse(RequestSpecification request, CustomLogFilter logFilter) {
		write("API Request: " +logFilter.getRequestBuilder() + "\nAPI Response: " + logFilter.getResponseBuilder() );
	}
	
}