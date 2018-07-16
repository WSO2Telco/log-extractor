import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class ReportGenerator
{
    public static void main(String[] args)
    {
        String strLine="",payload[],fileName;
        JSONObject response = null;
        int paymet=0,adcs=0;
        try{
            // Open the file that is the first
            // command line parameter
            FileInputStream fstream = new FileInputStream(args[1]);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            //Path file1 = Paths.get(args[1]);
            //Path file2 = Paths.get(args[2]);
            try{
                fileName=args[2];
            }catch (Exception e){
                fileName="";
            }
            long offset = 60000L * 60L * (long)(Float.parseFloat(args[0])*2) / 2L;

            PrintWriter paymentReport = new PrintWriter(fileName + args[1].split("\\.log\\.")[1] + "-robi-payment.csv", "UTF-8");
            PrintWriter paymentErrorReport = new PrintWriter(fileName + "payment-errorReport.csv", "UTF-8");
            PrintWriter smsReport = new PrintWriter(fileName + args[1].split("\\.log\\.")[1] + "-robi-sms.csv", "UTF-8");
            PrintWriter smsErrorReport = new PrintWriter(fileName  + "sms-errorReport.csv", "UTF-8");
            PrintWriter admsReport = new PrintWriter(fileName + args[1].split("\\.log\\.")[1] + "-robi-adms.csv", "UTF-8");
            PrintWriter lmsReport = new PrintWriter(fileName + args[1].split("\\.log\\.")[1] + "-robi-LMS.csv", "UTF-8");
            PrintWriter xrbtReport = new PrintWriter(fileName + args[1].split("\\.log\\.")[1] + "-robi-xrbt.csv", "UTF-8");
            PrintWriter bioReport = new PrintWriter(fileName + args[1].split("\\.log\\.")[1] + "-robi-bio.csv", "UTF-8");
            PrintWriter adcsReport = new PrintWriter(fileName + args[1].split("\\.log\\.")[1] + "-robi-ADCS.csv", "UTF-8");
            PrintWriter ocsReport = new PrintWriter(fileName + args[1].split("\\.log\\.")[1] + "-robi-ocs.csv", "UTF-8");
            PrintWriter ebalReport = new PrintWriter(fileName + args[1].split("\\.log\\.")[1] + "-robi-ebal.csv", "UTF-8");
            PrintWriter sdpReport = new PrintWriter(fileName + args[1].split("\\.log\\.")[1] + "-robi-sdp.csv", "UTF-8");
            PrintWriter fnfReport = new PrintWriter(fileName + args[1].split("\\.log\\.")[1] + "-robi-fnf.csv", "UTF-8");
            PrintWriter cbsReport = new PrintWriter(fileName + args[1].split("\\.log\\.")[1] + "-robi-cbs.csv", "UTF-8");
            PrintWriter mMoneyReport = new PrintWriter(fileName + args[1].split("\\.log\\.")[1] + "-robi-MMoney.csv", "UTF-8");
            PrintWriter apiSummery = new PrintWriter(fileName + args[1].split("\\.log\\.")[1] + "-robi-api-count.csv", "UTF-8");

            Map<String, MutableInt> apiCount = new HashMap<String, MutableInt>();
            Iterator apiIterator;
            /*
                Date, Provider, Application, MSISDN, Amount, Description, and Transaction Operation Status
                2,4,6,8,15,17,20
             */
            paymentReport.println("Date,Provider,Application,MSISDN,Amount,Description,Transaction Operation Status");
            paymentErrorReport.println("Date,Provider,Application,MSISDN,Amount,Description,Transaction Operation Status");
            smsReport.println("Api,Resource Path,Response Time,Service Time,Provider,Api Publisher,Application,Reference Code,MSISDN,Operation/Event,Client Correlator,Sender,Destination,status,Message,count,Message Id,Filter Criteria,Error Response");
            smsErrorReport.println("Api,Resource Path,Response Time,Service Time,Provider,Api Publisher,Application,Reference Code,MSISDN,Operation/Event,Client Correlator,Sender,Destination,status,Message,count,Message Id,Filter Criteria,Error Response");
            admsReport.println("API,MSISDN,Date Time,Service Provider,Application Name,Response Code");
            lmsReport.println("API,MSISDN,Date Time,Service Provider,Application Name,Response Code");
            xrbtReport.println("API,MSISDN,Date Time,Service Provider,Application Name,Response Code");
            bioReport.println("API,MSISDN,Date Time,Service Provider,Application Name,Response Code");
            adcsReport.println("API,MSISDN,Date Time,Service Provider,Application Name,Response Code");
            ocsReport.println("API,MSISDN,Date Time,Service Provider,Application Name,Response Code");
            ebalReport.println("API,MSISDN,Date Time,Service Provider,Application Name,Response Code");
            sdpReport.println("API,MSISDN,Date Time,Service Provider,Application Name,Response Code");
            fnfReport.println("API,MSISDN,Date Time,Service Provider,Application Name,Response Code");
            cbsReport.println("API,MSISDN,Date Time,Service Provider,Application Name,Response Code");
            mMoneyReport.println("API,MSISDN,Date Time,Service Provider,Application Name,Response Code");
            apiSummery.println("Api,Couunt");
/*
payloadData=[9cueliEeoMxkfegVFbDLw2wPiLMa, /smsmessaging/v1, v1, smsmessaging, /outbound/tel%3A%2B2128304/requests, POST, v1, 1528395284239, 21, VUMobile@carbon.super, , mife-gateway.robi.com.bd, admin, BDTube, , 1, 201, , north-bound, {"outboundSMSMessageRequest":{"address":["tel::+8801646095543"],"deliveryInfoList":{"deliveryInfo":[{"address":"tel:+8801646095543","deliveryStatus":"MessageWaiting"}],"resourceURL":"https://api.robi.com.bd/smsmessaging/v1/outbound/tel%3A%2B2128304/requests/1528395284224SM113026503923/deliveryInfos"},"senderAddress":"tel:2128304","outboundSMSTextMessage":{"message":"Subscribed on BDTube@TK2.44/day(AutoRenew).Data charge applicable.To unsubscribe visit http://bdtube.mobi or type STOP & send SMS to 2128304. Help:01674985965"},"clientCorrelator":"201806080016465000946dc57dc10cab4a5d9edda81bbbd4c15f","receiptRequest":{"notifyURL":"","callbackData":""},"senderName":"","resourceURL":"https://api.robi.com.bd/smsmessaging/v1/outbound/tel%3A%2B2128304/requests/1528395284224SM113026503923"}}, 14, VUMobile, 9cueliEeoMxkfegVFbDLw2wPiLMa, ROBI, 1, 3, , 113],

 */
            
            //Read File Line By Line
            while ((strLine = br.readLine()) != null)   {
                // Print the content on the console
                if (strLine.contains("payloadData")) {
                    payload = strLine.split(", ");
                    MutableInt count = apiCount.get(payload[3]);
                    if (count == null) {
                        apiCount.put(payload[3], new MutableInt());
                    }
                    else {
                        count.increment();
                    }
                    try {
                        if(payload[3].equals("adms")){
                            try{
                                admsReport.println(payload[3] + ',' //API
                                        + payload[4].split("=")[1].split("&")[0] + ','
                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) + ',' //Date
                                        + payload[9] + ',' //Provider
                                        + payload[13] + ',' //Application
                                        + payload[16]
                                );
                            }catch (Exception e){
                                admsReport.println(payload[3] + ',' //API
                                        + ','
                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) + ',' //Date
                                        + payload[9] + ',' //Provider
                                        + payload[13] + ',' //Application
                                        + payload[16]
                                );
                            }
                        }else if(payload[3].equals("LMS")){
                            try{
                                lmsReport.println(payload[3] + ',' //API
                                        + payload[4].split("=")[1].split("&")[0] + ','
                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) + ',' //Date
                                        + payload[9] + ',' //Provider
                                        + payload[13] + ',' //Application
                                        + payload[16]
                                );
                            }catch (Exception e){
                                lmsReport.println(payload[3] + ',' //API
                                        + ','
                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) + ',' //Date
                                        + payload[9] + ',' //Provider
                                        + payload[13] + ',' //Application
                                        + payload[16]
                                );
                            }
                        }else if(payload[3].equals("xrbt")){
                            try{
                                xrbtReport.println(payload[3] + ',' //API
                                        + payload[4].split("=")[1].split("&")[0] + ','
                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) + ',' //Date
                                        + payload[9] + ',' //Provider
                                        + payload[13] + ',' //Application
                                        + payload[16]
                                );
                            }catch (Exception e){
                                xrbtReport.println(payload[3] + ',' //API
                                        + ','
                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) + ',' //Date
                                        + payload[9] + ',' //Provider
                                        + payload[13] + ',' //Application
                                        + payload[16]
                                );
                            }
                        }else if(payload[3].equals("bio")){
                            try{
                                bioReport.println(payload[3] + ',' //API
                                        + ','
                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) + ',' //Date
                                        + payload[9] + ',' //Provider
                                        + payload[13] + ',' //Application
                                        + payload[16]
                                );
                            }catch (Exception e){
                                bioReport.println(payload[3] + ',' //API
                                        + ','
                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) + ',' //Date +
                                        + payload[9] + ',' //Provider
                                        + payload[13] + ',' //Application
                                        + payload[16]
                                );
                            }
                        }else if(payload[3].equals("ADCS")){
                            try{
                                adcsReport.println(payload[3] + ',' //API
                                        + payload[4].split("=")[1].split("&")[0] + ','
                                        + (Date) new Timestamp(Long.parseLong(payload[7])) + ',' //Date
                                        + payload[9] + ',' //Provider
                                        + payload[13] + ',' //Application
                                        + payload[16]
                                );
                            }catch (Exception e){
                                adcsReport.println(payload[3] + ',' //API
                                        + ','
                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) + ',' //Date
                                        + payload[9] + ',' //Provider
                                        + payload[13] + ',' //Application
                                        + payload[16]
                                );
                            }
                        }else if(payload[3].equals("ocs") || payload[3].equals("OCS") ){
                            try{
                                ocsReport.println(payload[3] + ',' //API
                                        + payload[4].split("=")[1].split("&")[0] + ','
                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) + ',' //Date
                                        + payload[9] + ',' //Provider
                                        + payload[13] + ',' //Application
                                        + payload[16]
                                );
                            }catch (Exception e){
                                ocsReport.println(payload[3] + ',' //API
                                        + ','
                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) + ',' //Date
                                        + payload[9] + ',' //Provider
                                        + payload[13] + ',' //Application
                                        + payload[16]
                                );
                            }
                        }else if(payload[3].equals("ebal")){
                            try{
                                ebalReport.println(payload[3] + ',' //API
                                        + payload[4].split("=")[1].split("&")[0] + ','
                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) + ',' //Date
                                        + payload[9] + ',' //Provider
                                        + payload[13] + ',' //Application
                                        + payload[16]
                                );
                            }catch (Exception e){
                                ebalReport.println(payload[3] + ',' //API
                                        + ','
                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) + ',' //Date
                                        + payload[9] + ',' //Provider
                                        + payload[13] + ',' //Application
                                        + payload[16]
                                );
                            }
                        }else if(payload[3].equals("sdp")){
                            try{
                                sdpReport.println(payload[3] + ',' //API
                                        + payload[4].split("=")[1].split("&")[0] + ','
                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) + ',' //Date
                                        + payload[9] + ',' //Provider
                                        + payload[13] + ',' //Application
                                        + payload[16]
                                );
                            }catch (Exception e){
                                sdpReport.println(payload[3] + ',' //API
                                        + ','
                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset ) + ',' //Date
                                        + payload[9] + ',' //Provider
                                        + payload[13] + ',' //Application
                                        + payload[16]
                                );
                            }
                        }else if(payload[3].equals("fnf")){
                            try{
                                fnfReport.println(payload[3] + ',' //API
                                        + payload[4].split("=")[1].split("&")[0] + ','
                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) + ',' //Date
                                        + payload[9] + ',' //Provider
                                        + payload[13] + ',' //Application
                                        + payload[16]
                                );
                            }catch (Exception e){
                                fnfReport.println(payload[3] + ',' //API
                                        + ','
                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) + ',' //Date
                                        + payload[9] + ',' //Provider
                                        + payload[13] + ',' //Application
                                        + payload[16]
                                );
                            }
                        }else if(payload[3].equals("cbs")){
                            try{
                                cbsReport.println(payload[3] + ',' //API
                                        + payload[4].split("=")[1].split("&")[0] + ','
                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) + ',' //Date
                                        + payload[9] + ',' //Provider
                                        + payload[13] + ',' //Application
                                        + payload[16]
                                );
                            }catch (Exception e){
                                cbsReport.println(payload[3] + ',' //API
                                        + ','
                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) + ',' //Date
                                        + payload[9] + ',' //Provider
                                        + payload[13] + ',' //Application
                                        + payload[16]
                                );
                            }
                        }else if(payload[3].equals("MMoney")){
                            try{
                                mMoneyReport.println(payload[3] + ',' //API
                                        + payload[4].split("=")[1].split("&")[0] + ','
                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) + ',' //Date
                                        + payload[9] + ',' //Provider
                                        + payload[13] + ',' //Application
                                        + payload[16]
                                );
                            }catch (Exception e){
                                mMoneyReport.println(payload[3] + ',' //API
                                        + ','
                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) + ',' //Date
                                        + payload[9] + ',' //Provider
                                        + payload[13] + ',' //Application
                                        + payload[16]
                                );
                            }
                        }
                        response = new JSONObject(payload[19]);
                    } catch (Exception e) {
                        if (payload[3].equals("payment")) {
                            paymentErrorReport.println(payload[3] + ',' //API
                                    + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) //Date
                                    + ",1970 01 01 06:00:00,"
                                    + payload[9] + ','
                                    + payload[12] + ','
                                    + payload[13] + ','
                                    + payload[16] + ','
                                    + payload[4].split("/")[1].replaceAll("%3A%2B", ":+") + ','
                                    + "Charge" + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + payload[19]+", "+payload[20] +','
                            );
                        } else if (payload[3].equals("smsmessaging")) {
                            smsErrorReport.println(payload[3] + ',' //API
                                    + payload[4] + ',' //Resource Path
                                    + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) //Date
                                    + ",1970 01 01 06:00:00,"
                                    + payload[9] + ',' //Provider
                                    + payload[12] + ',' //Publisher
                                    + payload[13] + ',' //Application
                                    + "null" + ','
                                    + payload[4].split("/")[2].replaceAll("%3A%2B", ":") + ','
                                    + "sendSMS" + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + payload[19]+", "+payload[20] +','
                            );
                        }
//                        else if(payload[3].equals("adms")){
//                            admsErrorReport.println(payload[3] + ',' //API
//                                    + payload[4].split("=")[1].split("&")[0] + ','
//                                    + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) //Date
//                                    + payload[9] + ',' //Provider
//                                    + payload[13] + ',' //Application
//                                    + payload[16]
//                            );
//                        }else if(payload[3].equals("LMS")){
////                            System.out.println(strLine);
//                            lmsErrorReport.println(payload[3] + ',' //API
//                                    + payload[4].split("=")[1].split("&")[0] + ','
//                                    + (Date) new Timestamp(Long.parseLong(payload[7])+ offset)  + ','//Date
//                                    + payload[9] + ',' //Provider
//                                    + payload[13] + ',' //Application
//                                    + payload[16]
//                            );
//                        }else if(payload[3].equals("xrbt")){
//                            xrbtErrorReport.println(payload[3] + ',' //API
//                                    + payload[4].split("=")[1].split("&")[0] + ','
//                                    + (Date) new Timestamp(Long.parseLong(payload[7])+ offset)  + ','//Date
//                                    + payload[9] + ',' //Provider
//                                    + payload[13] + ',' //Application
//                                    + payload[16]
//                            );
//                        }else if(payload[3].equals("bio")){
//                            try{
//                                response = new JSONObject(payload[19]+", "+payload[20]+", "+payload[21]+", "+payload[22]+", "+payload[23]);
//                            }catch (Exception ex){
//                                bioErrorReport.println(payload[3] + ',' //API
//                                        + payload[4].split("=")[1].split("&")[0] + ','
//                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset)  + ','//Date
//                                        + payload[9] + ',' //Provider
//                                        + payload[13] + ',' //Application
//                                        + payload[16]
//                                );
//                            }
//
//                            try {
//                                response.get("IsSuccess");
//                            } catch (Exception ex) {
//                                bioErrorReport.println(payload[3] + ',' //API
//                                        + payload[4].split("=")[1].split("&")[0] + ','
//                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) + ',' //Date
//                                        + payload[9] + ',' //Provider
//                                        + payload[13] + ',' //Application
//                                        + payload[16]
//                                );
//                                continue;
//                            }
//                            try{
//                                bioReport.println(payload[3] + ',' //API
//                                        + ','
//                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) + ',' //Date
//                                        + payload[9] + ',' //Provider
//                                        + payload[13] + ',' //Application
//                                        + payload[16]
//                                );
//                            }catch (Exception ex){
//                                bioReport.println(payload[3] + ',' //API
//                                        + ','
//                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) + ',' //Date
//                                        + payload[9] + ',' //Provider
//                                        + payload[13] + ',' //Application
//                                        + payload[16]
//                                );
//                            }
//
//
//                        }else if(payload[3].equals("ADCS")){
//                            try{
//                                adcsErrorReport.println(payload[3] + ',' //API
//                                        + payload[4].split("=")[1].split("&")[0] + ','
//                                        + (Date) new Timestamp(Long.parseLong(payload[7])+ offset)  + ','//Date
//                                        + payload[9] + ',' //Provider
//                                        + payload[13] + ',' //Application
//                                        + payload[16]
//                                );
//                            }catch (Exception ex){
//                                if(Integer.parseInt(payload[16]) < 227 && Integer.parseInt(payload[16]) >= 200)
//                                    adcsReport.println(payload[3] + ',' //API
//                                            +  ','
//                                            + (Date) new Timestamp(Long.parseLong(payload[7])+ offset)  + ','//Date
//                                            + payload[9] + ',' //Provider
//                                            + payload[13] + ',' //Application
//                                            + payload[16]
//                                    );
//                                else
//                                    adcsErrorReport.println(payload[3] + ',' //API
//                                            +  ','
//                                            + (Date) new Timestamp(Long.parseLong(payload[7])+ offset)  + ','//Date
//                                            + payload[9] + ',' //Provider
//                                            + payload[13] + ',' //Application
//                                            + payload[16]
//                                    );
//                            }
//                        }

                        continue;
                    }
                    if (payload[3].equals("payment")) {
                        try {
                            response.getJSONObject("amountTransaction");
                        } catch (Exception e) {
                            paymentErrorReport.println(payload[3] + ',' //API
                                    + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) //Date
                                    + ",1970 01 01 06:00:00,"
                                    + payload[9] + ','
                                    + payload[12] + ','
                                    + payload[13] + ','
                                    + payload[16] + ','
                                    + payload[4].split("/")[1].replaceAll("%3A%2B", ":+") + ','
                                    + "Charge" + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + "\"" + payload[19] + "}}\"" + ','
                            );
                            continue;
                        }
                        //2,4,6,8,15,17,20
                        if (validateJSON(response.getJSONObject("amountTransaction"), "transactionOperationStatus").equals("Charged")){
                            paymet++;
                            paymentReport.println((Date) new Timestamp(Long.parseLong(payload[7])+ offset) //Date
                                    + payload[9] + ','
                                    + payload[13] + ','
                                    + payload[4].split("/")[1].replaceAll("%3A%2B", ":+") + ','
                                    + validateJSON(response.getJSONObject("amountTransaction").getJSONObject("paymentAmount").getJSONObject("chargingInformation"), "amount") + ','
                                    + validateJSON(response.getJSONObject("amountTransaction").getJSONObject("paymentAmount").getJSONObject("chargingInformation"), "description") + ','
                                    + validateJSON(response.getJSONObject("amountTransaction"), "transactionOperationStatus") + ','

                            );
                        }
                    } else if (payload[3].equals("smsmessaging")) {
                        try {
                            response.getJSONObject("outboundSMSMessageRequest");
                        } catch (Exception e) {
                            smsErrorReport.println(payload[3] + ',' //API
                                    + payload[4] + ',' //Resource Path
                                    + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) //Date
                                    + ",1970 01 01 06:00:00,"
                                    + payload[9] + ',' //Provider
                                    + payload[12] + ',' //Publisher
                                    + payload[13] + ',' //Application
                                    + "null" + ','
                                    + payload[4].split("/")[2].replaceAll("%3A%2B", ":") + ','
                                    + "sendSMS" + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + ','
                                    + "\"" + payload[19] + "\"" +','
                            );
                            continue;
                        }
                        smsReport.println(payload[3] + ',' //API
                                + payload[4] + ',' //Resource Path
                                + (Date) new Timestamp(Long.parseLong(payload[7])+ offset) //Date
                                + ",1970 01 01 06:00:00,"
                                + payload[9] + ',' //Provider
                                + payload[12] + ',' //Publisher
                                + payload[13] + ',' //Application
                                + "null" + ','
                                + payload[4].split("/")[2].replaceAll("%3A%2B", ":") + ','
                                + "sendSMS" + ','
                                + validateJSON(response.getJSONObject("outboundSMSMessageRequest"), "clientCorrelator") + ','  //Client Correlator
                                + validateJSON(response.getJSONObject("outboundSMSMessageRequest"), "senderAddress") + ',' //Sender
                                + validateJSONArray(response.getJSONObject("outboundSMSMessageRequest").getJSONObject("deliveryInfoList").getJSONArray("deliveryInfo"), "address") + ',' //Destination
                                + validateJSONArray(response.getJSONObject("outboundSMSMessageRequest").getJSONObject("deliveryInfoList").getJSONArray("deliveryInfo"), "deliveryStatus") + ',' //status
                                + "\"" + validateJSON(response.getJSONObject("outboundSMSMessageRequest").getJSONObject("outboundSMSTextMessage"), "message") + "\"" + ',' //Message
                                + getJSONArraySize(response.getJSONObject("outboundSMSMessageRequest").getJSONObject("deliveryInfoList").getJSONArray("deliveryInfo")) + ',' //count
                                + ','

                        );

                    }
                }
            }
            //Close the input stream
//            */

            in.close();
            paymentReport.close();
            smsReport.close();
            paymentErrorReport.close();
            smsErrorReport.close();
            admsReport.close();
            lmsReport.close();
            xrbtReport.close();
            bioReport.close();
            ocsReport.close();
            ebalReport.close();
            sdpReport.close();
            fnfReport.close();
            adcsReport.close();
            cbsReport.close();
            mMoneyReport.close();

            apiIterator= apiCount.entrySet().iterator();
            MutableInt tmp;
            while (apiIterator.hasNext()){
                Map.Entry pair = (Map.Entry)apiIterator.next();
                tmp = (MutableInt) pair.getValue();
                apiSummery.println(pair.getKey() + "," + tmp.get());
                System.out.println(pair.getKey() + " = " + tmp.get());
            }
            System.out.println(paymet +" " + adcs);
            apiSummery.close();
        }catch (Exception e){//Catch exception if any

            System.err.println("Error: " + strLine );
        }

    }
    static String validateJSON(JSONObject response, String key){
        if(response.has(key)){
            return response.get(key).toString();
        }
        return "";

    }
    static String validateJSONArray(JSONArray response, String key){
        if(response.length() > 0){
            String outputString = "\""+response.getJSONObject(0).get(key);
            for (int i=1; i < response.length(); i++){
                outputString+= ","+response.getJSONObject(i).get(key);
            }
            outputString+="\"";
            return outputString;
        }
        return "";

    }
    static  int getJSONArraySize(JSONArray respose){
        return respose.length();
    }

    static String getErrorReportName(String reportName){
        return (reportName.split(".csv")[0] + "-errorReport.csv");
    }


}

class MutableInt {
    int value = 1; // note that we start at 1 since we're counting
    public void increment () { ++value;      }
    public int  get ()       { return value; }
}
