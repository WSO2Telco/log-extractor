package com.wso2telco.logdebugger.com.reports;

import java.io.PrintWriter;

public interface RobiReports {
    public void writeToReport(StringBuilder record, PrintWriter report);
}
