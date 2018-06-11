## log-extractor
Used to Extract Reports from failed-notify-events.log

## How to build
mvn clean compile assembly:single

## How to execute
java -jar com.wso2telco.logdebugger.com-1.0.jar $failed-notify-events.log_path $payment-api-report-location-to-save $sms-api-report-location-to-save $api-count-report-location-to-save
