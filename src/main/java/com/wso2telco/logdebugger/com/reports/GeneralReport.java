package com.wso2telco.logdebugger.com.reports;

import java.io.PrintWriter;

public class GeneralReport implements RobiReports {


    @Override
    public void writeToReport(StringBuilder record, PrintWriter report) {

        //String filename = this.getClass().getName();
        report.println(record.toString());
    }
}
