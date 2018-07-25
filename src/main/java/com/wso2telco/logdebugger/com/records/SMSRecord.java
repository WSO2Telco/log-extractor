package com.wso2telco.logdebugger.com.records;

import com.wso2telco.logdebugger.com.records.GeneralRecord;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Date;

public class SMSRecord extends GeneralRecord {

    private JSONObject response = null;
    private boolean error = false;
    private StringBuilder record = new StringBuilder();
    @Override
    public StringBuilder setPayload(String[] payload, long offset) {

        try {
            response = new JSONObject(payload[19]);
        }catch (JSONException e){
            setError(true);
            record.append(payload[3]).append(",");
            record.append(payload[4]).append(",");
            record.append((Date) new Timestamp(Long.parseLong(payload[7])+ offset)).append(",");
            record.append("1970 01 01 06:00:00,");
            record.append(payload[9]).append(",");
            record.append(payload[12]).append(",");
            record.append(payload[13]).append(",");
            record.append("null,");
            record.append(payload[4].split("/")[2].replaceAll("%3A%2B", ":")).append(",");
            record.append("sendSMS,");
            record.append(",");
            record.append(",");
            record.append(",");
            record.append(",");
            record.append(",");
            record.append(",");
            record.append(",");
            record.append(",");
            record.append(payload[19]).append(", ").append(payload[20]).append(",");
            return record;
        }
        try {
            response.getJSONObject("outboundSMSMessageRequest");
        } catch (JSONException e) {
            setError(true);
            record.append(payload[3]).append(",");
            record.append(payload[4]).append(",");
            record.append((Date) new Timestamp(Long.parseLong(payload[7])+ offset)).append(",");
            record.append("1970 01 01 06:00:00,");
            record.append(payload[9]).append(",");
            record.append(payload[12]).append(",");
            record.append(payload[13]).append(",");
            record.append("null,");
            record.append(payload[4].split("/")[2].replaceAll("%3A%2B", ":")).append(",");
            record.append("sendSMS,");
            record.append(",");
            record.append(",");
            record.append(",");
            record.append(",");
            record.append(",");
            record.append(",");
            record.append(",");
            record.append(",");
            record.append("\"").append(payload[19]).append("\"").append(",");
            return record;
        }
        record.append(payload[3]).append(",");
        record.append(payload[4]).append(",");
        record.append((Date) new Timestamp(Long.parseLong(payload[7])+ offset)).append(",");
        record.append("1970 01 01 06:00:00,");
        record.append(payload[9]).append(",");
        record.append(payload[12]).append(",");
        record.append(payload[13]).append(",");
        record.append("null,");
        record.append(payload[4].split("/")[2].replaceAll("%3A%2B", ":")).append(",");
        record.append("sendSMS,");
        record.append(validateJSON(response.getJSONObject("outboundSMSMessageRequest"), "clientCorrelator")).append(",");
        record.append(validateJSON(response.getJSONObject("outboundSMSMessageRequest"), "senderAddress")).append(",");
        record.append(validateJSONArray(response.getJSONObject("outboundSMSMessageRequest").getJSONObject("deliveryInfoList").getJSONArray("deliveryInfo"), "address")).append(",");
        record.append(validateJSONArray(response.getJSONObject("outboundSMSMessageRequest").getJSONObject("deliveryInfoList").getJSONArray("deliveryInfo"), "deliveryStatus")).append(",");
        record.append("\"").append(validateJSON(response.getJSONObject("outboundSMSMessageRequest").getJSONObject("outboundSMSTextMessage"), "message")).append("\"").append(",");
        record.append(getJSONArraySize(response.getJSONObject("outboundSMSMessageRequest").getJSONObject("deliveryInfoList").getJSONArray("deliveryInfo"))).append(",");
        record.append(",");

        return record;
    }

    private void setError(boolean error){
        this.error = error;
    }

    public boolean getError(){
        return error;
    }

    public String validateJSON(JSONObject response, String key){
        if(response.has(key)){
            return response.get(key).toString();
        }
        return "";

    }
    public String validateJSONArray(JSONArray response, String key){
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
    public   int getJSONArraySize(JSONArray respose){
        return respose.length();
    }
}
