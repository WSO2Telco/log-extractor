import org.json.JSONObject;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.sql.Timestamp;

class ReportGenerator
{
    public static void main(String[] args)
    {
        try{
            // Open the file that is the first
            // command line parameter
            FileInputStream fstream = new FileInputStream(args[0]);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine,payload[],response1[],response2[];
            JSONObject response,subresponse;
            Path file = Paths.get(args[1]);
            PrintWriter writer = new PrintWriter(args[1], "UTF-8");
            writer.println("Api,Date,Service Time,Provider,Api Publisher,Application,Response Code,MSISDN,Operation/Event,Total Amount Charged/Refunded,On Behalf Of,Purchase Category,Tax Amount,Channel,Amount,Currency,Description,Server Reference Code,Client Correlator,Transaction Operation Status,Reference Code,Resource URL");


            //Read File Line By Line
            while ((strLine = br.readLine()) != null)   {
                // Print the content on the console
                if (strLine.contains("payloadData")){
                    payload = strLine.split(",");
                    response1 = strLine.split("north-bound,");
                    response2 = response1[1].split("}},");
                    response = new JSONObject(response2[0]+"}}");
                    response.getJSONObject("amountTransaction").getJSONObject("paymentAmount").get("totalAmountCharged");
                    writer.println (payload[3].replaceAll("\\s+","")+',' //API
                            + (Date)new Timestamp(Long.parseLong(payload[7].replaceAll("\\s+",""))) //Date
                            +",1970 01 01 06:00:00,"
                            +payload[9].replaceAll("\\s+","")+','
                            +payload[12].replaceAll("\\s+","")+','
                            +payload[13].replaceAll("\\s+","")+','
                            +payload[16].replaceAll("\\s+","")+','
                            +payload[4].split("/")[1].replaceAll("%3A%2B",":+")+','
                            +"Charge"+','
                            +validateJSON(response.getJSONObject("amountTransaction").getJSONObject("paymentAmount"),"totalAmountCharged")+','
                            +validateJSON(response.getJSONObject("amountTransaction").getJSONObject("paymentAmount").getJSONObject("chargingMetaData"), "onBehalfOf")+','
                            +validateJSON(response.getJSONObject("amountTransaction").getJSONObject("paymentAmount").getJSONObject("chargingMetaData"), "purchaseCategoryCode")+','
                            +validateJSON(response.getJSONObject("amountTransaction").getJSONObject("paymentAmount").getJSONObject("chargingMetaData"), "taxAmount")+','
                            +validateJSON(response.getJSONObject("amountTransaction").getJSONObject("paymentAmount").getJSONObject("chargingMetaData"), "channel")+','
                            +validateJSON(response.getJSONObject("amountTransaction").getJSONObject("paymentAmount").getJSONObject("chargingInformation"), "amount")+','
                            +validateJSON(response.getJSONObject("amountTransaction").getJSONObject("paymentAmount").getJSONObject("chargingInformation"), "currency")+','
                            +validateJSON(response.getJSONObject("amountTransaction").getJSONObject("paymentAmount").getJSONObject("chargingInformation"), "description")+','
                            +validateJSON(response.getJSONObject("amountTransaction"), "serverReferenceCode")+','
                            +validateJSON(response.getJSONObject("amountTransaction"), "clientCorrelator")+','
                            +validateJSON(response.getJSONObject("amountTransaction"), "transactionOperationStatus")+','
                            +validateJSON(response.getJSONObject("amountTransaction"), "referenceCode")+','
                            +validateJSON(response.getJSONObject("amountTransaction"), "resourceURL")

                    );

                }
            }
            //Close the input stream
            in.close();
            writer.close();
        }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }

    }
    public static String validateJSON(JSONObject response, String key){
        if(response.has(key)){
            return response.get(key).toString();
        }
        return "";

    }
}
