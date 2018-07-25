package com.wso2telco.logdebugger.com.records;

public class MMoneyRecord extends GeneralRecord {

    @Override
    public StringBuilder setPayload(String[] payload, long offset) {
        return  super.setRecord(payload,offset);
    }
}
