package com.proapps.api.steps;

import java.security.InvalidParameterException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

import com.google.inject.Inject;
import com.proapps.api.pojos.Datum;
import com.proapps.api.pojos.Forecast;
import com.proapps.api.support.CustomLogFilter;
import com.proapps.common.support.ScenarioUtils;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class SurfingSteps {
	
	@Inject
	private ScenarioUtils sUtils;
	
	@Inject
	private CustomLogFilter logFilter;

	private static final String BASE_URL = "https://api.weatherbit.io/v2.0/forecast";
	private static final String API_KEY = "1fc4e6e289474ffe961db07620c53281";
	
	@SuppressWarnings("unused")
	private static String city;
	private static String country;
	private static List<String> daysArrayToSurf = new ArrayList<String>();
	private static List<String> datesToSurfBasedOnDays = new ArrayList<String>();
	private static int forecastUntilDays;
	private static List<Forecast> forecasts = new ArrayList<Forecast>();

	@Given("^I only like to surf on \"([^\"]*)\" in the next \"([^\"]*)\"$")
	public void iOnlyLikeToSurfOnInTheNext(String daysToSurf, String forecastUntil) {
		String[] splitDays = daysToSurf.split("&");
		for (String splitDay : splitDays) {
			daysArrayToSurf.add(splitDay.trim().toUpperCase());
		}
		if (forecastUntil.contains("months"))
			forecastUntilDays = Integer.parseInt(forecastUntil.split(" ")[0]) * 30;
		else if (forecastUntil.contains("days"))
			forecastUntilDays = Integer.parseInt(forecastUntil.split(" ")[0]);
		else
			throw new InvalidParameterException("The Parameter Passed is Incorrect");
		LocalDate currentDate = LocalDate.now();
		for (int i = 0 ; i < forecastUntilDays ; i++) {
			DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
			if (daysArrayToSurf.contains(dayOfWeek.toString())) {
				datesToSurfBasedOnDays.add(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			}
			currentDate = currentDate.plusDays(1);
		}
		datesToSurfBasedOnDays.stream().forEach(System.out::println);
	}

	@SuppressWarnings("static-access")
	@Given("^I like to surf in the city \"([^\"]*)\" in country \"([^\"]*)\"$")
	public void iLikeToSurfInTheCityInCountry(String city, String country) {
		this.city = city;
		this.country = country;
	}

	@When("^I look up the the weather forecast for the next required time with POSTAL CODES \"([^\"]*)\"$")
	public void iLookUpTheTheWeatherForecastForTheNextRequiredTimeWithPOSTALCODES(String postalCodes) {
		RestAssured.baseURI = BASE_URL;
		for (String postalCode : postalCodes.split(",")) {
			RequestSpecification request = RestAssured.given().filter(logFilter);
			Response jSONResponse = request.queryParam("key", API_KEY)
			.queryParam("postal_code", postalCode)
			.queryParam("country", country)
			.queryParam("days", forecastUntilDays)
			.get("/daily");
			sUtils.writeRequestResponse(request, logFilter);
			Assert.assertTrue(jSONResponse.getStatusCode() == 200, "The Response Code of the API is not 200");
			Forecast forecast = jSONResponse.as(Forecast.class);
			forecasts.add(forecast);
		}
		for (Forecast forecast : forecasts) {
			List<Datum> valuesToRemove = new ArrayList<Datum>();
			for (Datum datum : forecast.getData()) {
				if (!datesToSurfBasedOnDays.contains(datum.getValidDate())) {
					valuesToRemove.add(datum);
				}
			}
			forecast.getData().removeAll(valuesToRemove);
		}
	}

	@Then("I check to see if the temparature is between {double} and {double} degrees")
	public void iCheckToSeeIfTheTemparatureIsBetweenAndDegrees(double minTemp, double maxTemp) {
		for (Forecast forecast : forecasts) {
			List<Datum> valuesToRemove = new ArrayList<Datum>();
			for (Datum datum : forecast.getData()) {
				if (!((datum.getTemp() >= minTemp) && (datum.getTemp() <= maxTemp))) {
					valuesToRemove.add(datum);
				}
			}
			forecast.getData().removeAll(valuesToRemove);
		}
	}

	@And("I check the wind speed to be between {double} and {double}")
	public void iCheckTheWindSpeedToBeBetweenAnd(double minSpeed, double maxSpeed) {
		for (Forecast forecast : forecasts) {
			List<Datum> valuesToRemove = new ArrayList<Datum>();
			for (Datum datum : forecast.getData()) {
				if (!((datum.getWindSpd() >= minSpeed) && (datum.getWindSpd() <= maxSpeed))) {
					valuesToRemove.add(datum);
				}
			}
			forecast.getData().removeAll(valuesToRemove);
		}
	}

	@And("I check if the UV index is <= {double}")
	public void iCheckIfTheUVIndexIs(double maxUV) {
		for (Forecast forecast : forecasts) {
			List<Datum> valuesToRemove = new ArrayList<Datum>();
			for (Datum datum : forecast.getData()) {
				if (!(datum.getUv() <= maxUV)) {
					valuesToRemove.add(datum);
				}
			}
			forecast.getData().removeAll(valuesToRemove);
		}
	}

	@And("^I pickup the best spots and dates to surf on$")
	public void iPickupTheBestSpotsAndDatesToSurfOn() {
		sUtils.write("The following are the best spots and dates to surf on:");
		for (Forecast forecast : forecasts) {
			for (Datum datum : forecast.getData()) {
				sUtils.write("Date: "+datum.getValidDate()+ ", Longitude: "+forecast.getLon()+", Latitude: "+forecast.getLat());
			}
		}
	}
	
	
	
	
	
	
}
