## log-extractor
Used to Extract Reports from failed-notify-events.log

## How to build
mvn clean compile assembly:single

## How to execute
java -jar com.wso2telco.logdebugger.com-1.3.1.jar $(time difference between jar executing server and production server in hours) failed-notify-events.log_path path_that_reports_should_be_saved