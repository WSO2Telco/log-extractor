package com.wso2telco.logdebugger.com.records;

import java.sql.Timestamp;
import java.util.Date;

public abstract class GeneralRecord implements RobiRecords {
    
    public StringBuilder setRecord(String payload[], long offset){
        StringBuilder record = new StringBuilder();
        record.append(payload[3]).append(",");
        try {
            record.append(payload[4].split("=")[1].split("&")[0]).append(",");
        }catch(ArrayIndexOutOfBoundsException e){
            record.append(",");
        }
        record.append((Date) new Timestamp(Long.parseLong(payload[7])+ offset)).append(",");
        record.append(payload[9]).append(",");
        record.append(payload[13]).append(",");
        record.append(payload[16]);

        return record;
    }
}
