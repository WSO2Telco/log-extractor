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
        try{
            // Open the file that is the first
            // command line parameter
            int errorCount = 0;
            FileInputStream fstream = new FileInputStream(args[0]);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine,payload[],response1[],response2[];
            JSONObject response;
            //Path file1 = Paths.get(args[1]);
            //Path file2 = Paths.get(args[2]);
            PrintWriter paymentReport = new PrintWriter(args[1], "UTF-8");
            PrintWriter smsReport = new PrintWriter(args[2], "UTF-8");
            PrintWriter apiSummery = new PrintWriter(args[3], "UTF-8");
            PrintWriter errorResponse = new PrintWriter("errorResponse.log", "UTF-8");
            Map<String, MutableInt> apiCount = new HashMap<String, MutableInt>();
            Iterator apiIterator;
            paymentReport.println("Api,Date,Service Time,Provider,Api Publisher,Application,Response Code,MSISDN,Operation/Event,Total Amount Charged/Refunded,On Behalf Of,Purchase Category,Tax Amount,Channel,Amount,Currency,Description,Server Reference Code,Client Correlator,Transaction Operation Status,Reference Code,Resource URL,Error Response");
            smsReport.println("Api,Resource Path,Response Time,Service Time,Provider,Api Publisher,Application,Reference Code,MSISDN,Operation/Event,Client Correlator,Sender,Destination,status,Message,count,Message Id,Filter Criteria,Error Response");
            apiSummery.println("Api,Couunt");
/*
payloadData=[9cueliEeoMxkfegVFbDLw2wPiLMa, /smsmessaging/v1, v1, smsmessaging, /outbound/tel%3A%2B2128304/requests, POST, v1, 1528395284239, 21, VUMobile@carbon.super, , mife-gateway.robi.com.bd, admin, BDTube, , 1, 201, , north-bound, {"outboundSMSMessageRequest":{"address":["tel::+8801646095543"],"deliveryInfoList":{"deliveryInfo":[{"address":"tel:+8801646095543","deliveryStatus":"MessageWaiting"}],"resourceURL":"https://api.robi.com.bd/smsmessaging/v1/outbound/tel%3A%2B2128304/requests/1528395284224SM113026503923/deliveryInfos"},"senderAddress":"tel:2128304","outboundSMSTextMessage":{"message":"Subscribed on BDTube@TK2.44/day(AutoRenew).Data charge applicable.To unsubscribe visit http://bdtube.mobi or type STOP & send SMS to 2128304. Help:01674985965"},"clientCorrelator":"201806080016465000946dc57dc10cab4a5d9edda81bbbd4c15f","receiptRequest":{"notifyURL":"","callbackData":""},"senderName":"","resourceURL":"https://api.robi.com.bd/smsmessaging/v1/outbound/tel%3A%2B2128304/requests/1528395284224SM113026503923"}}, 14, VUMobile, 9cueliEeoMxkfegVFbDLw2wPiLMa, ROBI, 1, 3, , 113],

 */


            //Read File Line By Line
            while ((strLine = br.readLine()) != null)   {
                // Print the content on the console

                if (strLine.contains("payloadData")) {
                    payload = strLine.split(",");
                    response1 = strLine.split("north-bound,");
                    if(payload[3].equals(" adms")){
                        response2 = response1[1].split("]}},");
                    }else{
                        response2 = response1[1].split("}},");
                    }
                    MutableInt count = apiCount.get(payload[3].replaceAll("\\s+", ""));
                    if (count == null) {
                        apiCount.put(payload[3].replaceAll("\\s+", ""), new MutableInt());
                    }
                    else {
                        count.increment();
                    }
                    try {
                        if(payload[3].equals(" adms")){
                            response = new JSONObject(response2[0] + "]}}");
                        }else{
                            response = new JSONObject(response2[0] + "}}");
                        }

                    } catch (Exception e) {
                        if (payload[3].equals(" payment")) {
                            paymentReport.println(payload[3].replaceAll("\\s+", "") + ',' //API
                                    + (Date) new Timestamp(Long.parseLong(payload[7].replaceAll("\\s+", ""))) //Date
                                    + ",1970 01 01 06:00:00,"
                                    + payload[9].replaceAll("\\s+", "") + ','
                                    + payload[12].replaceAll("\\s+", "") + ','
                                    + payload[13].replaceAll("\\s+", "") + ','
                                    + payload[16].replaceAll("\\s+", "") + ','
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
                                    + "400 Bad Request" +','
                            );
                        } else if (payload[3].equals(" smsmessaging")) {
                            smsReport.println(payload[3].replaceAll("\\s+", "") + ',' //API
                                    + payload[4].replaceAll("\\s+", "") + ',' //Resource Path
                                    + (Date) new Timestamp(Long.parseLong(payload[7].replaceAll("\\s+", ""))) //Date
                                    + ",1970 01 01 06:00:00,"
                                    + payload[9].replaceAll("\\s+", "") + ',' //Provider
                                    + payload[12].replaceAll("\\s+", "") + ',' //Publisher
                                    + payload[13].replaceAll("\\s+", "") + ',' //Application
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
                                    + "400 Bad Request" +','
                            );
                        }

                        continue;
                    }
                    if (payload[3].equals(" payment")) {
                        try {
                            response.getJSONObject("amountTransaction");
                        } catch (Exception e) {
                            paymentReport.println(payload[3].replaceAll("\\s+", "") + ',' //API
                                    + (Date) new Timestamp(Long.parseLong(payload[7].replaceAll("\\s+", ""))) //Date
                                    + ",1970 01 01 06:00:00,"
                                    + payload[9].replaceAll("\\s+", "") + ','
                                    + payload[12].replaceAll("\\s+", "") + ','
                                    + payload[13].replaceAll("\\s+", "") + ','
                                    + payload[16].replaceAll("\\s+", "") + ','
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
                                    + "\"" + response2[0] + "}}\"" +','
                            );
                            continue;
                        }
                        paymentReport.println(payload[3].replaceAll("\\s+", "") + ',' //API
                                + (Date) new Timestamp(Long.parseLong(payload[7].replaceAll("\\s+", ""))) //Date
                                + ",1970 01 01 06:00:00,"
                                + payload[9].replaceAll("\\s+", "") + ','
                                + payload[12].replaceAll("\\s+", "") + ','
                                + payload[13].replaceAll("\\s+", "") + ','
                                + payload[16].replaceAll("\\s+", "") + ','
                                + payload[4].split("/")[1].replaceAll("%3A%2B", ":+") + ','
                                + "Charge" + ','
                                + validateJSON(response.getJSONObject("amountTransaction").getJSONObject("paymentAmount"), "totalAmountCharged") + ','
                                + validateJSON(response.getJSONObject("amountTransaction").getJSONObject("paymentAmount").getJSONObject("chargingMetaData"), "onBehalfOf") + ','
                                + validateJSON(response.getJSONObject("amountTransaction").getJSONObject("paymentAmount").getJSONObject("chargingMetaData"), "purchaseCategoryCode") + ','
                                + validateJSON(response.getJSONObject("amountTransaction").getJSONObject("paymentAmount").getJSONObject("chargingMetaData"), "taxAmount") + ','
                                + validateJSON(response.getJSONObject("amountTransaction").getJSONObject("paymentAmount").getJSONObject("chargingMetaData"), "channel") + ','
                                + validateJSON(response.getJSONObject("amountTransaction").getJSONObject("paymentAmount").getJSONObject("chargingInformation"), "amount") + ','
                                + validateJSON(response.getJSONObject("amountTransaction").getJSONObject("paymentAmount").getJSONObject("chargingInformation"), "currency") + ','
                                + validateJSON(response.getJSONObject("amountTransaction").getJSONObject("paymentAmount").getJSONObject("chargingInformation"), "description") + ','
                                + validateJSON(response.getJSONObject("amountTransaction"), "serverReferenceCode") + ','
                                + validateJSON(response.getJSONObject("amountTransaction"), "clientCorrelator") + ','
                                + validateJSON(response.getJSONObject("amountTransaction"), "transactionOperationStatus") + ','
                                + validateJSON(response.getJSONObject("amountTransaction"), "referenceCode") + ','
                                + validateJSON(response.getJSONObject("amountTransaction"), "resourceURL")

                        );
                    } else if (payload[3].equals(" smsmessaging")) {
                        try {
                            response.getJSONObject("outboundSMSMessageRequest");
                        } catch (Exception e) {
                            smsReport.println(payload[3].replaceAll("\\s+", "") + ',' //API
                                    + payload[4].replaceAll("\\s+", "") + ',' //Resource Path
                                    + (Date) new Timestamp(Long.parseLong(payload[7].replaceAll("\\s+", ""))) //Date
                                    + ",1970 01 01 06:00:00,"
                                    + payload[9].replaceAll("\\s+", "") + ',' //Provider
                                    + payload[12].replaceAll("\\s+", "") + ',' //Publisher
                                    + payload[13].replaceAll("\\s+", "") + ',' //Application
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
                                    + "\"" + response2[0] + "}}\"" +','
                            );
                            continue;
                        }
                        smsReport.println(payload[3].replaceAll("\\s+", "") + ',' //API
                                + payload[4].replaceAll("\\s+", "") + ',' //Resource Path
                                + (Date) new Timestamp(Long.parseLong(payload[7].replaceAll("\\s+", ""))) //Date
                                + ",1970 01 01 06:00:00,"
                                + payload[9].replaceAll("\\s+", "") + ',' //Provider
                                + payload[12].replaceAll("\\s+", "") + ',' //Publisher
                                + payload[13].replaceAll("\\s+", "") + ',' //Application
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
            errorResponse.close();

            apiIterator= apiCount.entrySet().iterator();
            MutableInt tmp;
            while (apiIterator.hasNext()){
                Map.Entry pair = (Map.Entry)apiIterator.next();
                tmp = (MutableInt) pair.getValue();
                apiSummery.println(pair.getKey() + "," + tmp.get());
                System.out.println(pair.getKey() + " = " + tmp.get());
            }
            apiSummery.close();
        }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
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


}

class MutableInt {
    int value = 1; // note that we start at 1 since we're counting
    public void increment () { ++value;      }
    public int  get ()       { return value; }
}
