package com.wso2telco.logdebugger.com.records;

import com.wso2telco.logdebugger.com.records.GeneralRecord;

public class XRBTRecord extends GeneralRecord {

    @Override
    public StringBuilder setPayload(String[] payload, long offset) {
        return  super.setRecord(payload,offset);
    }
}
