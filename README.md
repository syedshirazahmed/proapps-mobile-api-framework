# proapps-mobile-api-framework

The framework is designed in such a way that the mobile features and api features will run in parallel to each other when mvn clean install is run from the console.

If you wish to execute only UI (mobile) related scenarios via console, you can type the following command:
mvn clean install -Dcucumber.filter.tags="@ui" 
PS : While running the above command, please make sure that your feature has a tag called @ui. [WorkshopMeeting.feature has the tag as @ui]

If you wish to execute only API related scenarios via console, you can type the following command:
mvn clean install -Dcucumber.filter.tags="@api" 
PS : While running the above command, please make sure that your feature has a tag called @api. [SurfingChoice.feature has the tag as @api]

Incase, you want to directly run the API related features from your IDE, kindly go to src/test/java/com/proapps/api/support/APIRunnerTest.java and run this java class as a TestNG Test.
Similarly if you want to directly run the UI related scenarios from your IDE, kindly to to src/test/java/com/proapps/mobile/support/MobileRunnerTest.java and tun this java class as a TestNG Test.

NOTE: Reports will be generated only if the scenarios are run from console. The reports HTML that will be made is /target/cucumber-html-reports/cucumber-html-reports/overview-features.html

HAPPY AUTOMATING.!!
