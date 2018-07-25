package com.wso2telco.logdebugger.com.reports;

import com.wso2telco.logdebugger.com.others.MutableInt;
import com.wso2telco.logdebugger.com.records.*;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ReportFactory  {

    private ADCS adcs;
    private ADMS adms;
    private BIO bio;
    private CBS cbs;
    private EBAL ebal;
    private FNF fnf;
    private LMS lms;
    private MMoney mMoney;
    private OCS ocs;
    private Payment payment;
    private Payment paymentError;
    private SDP sdp;
    private SMS sms;
    private SMS smsError;
    private XRBT xrbt;
    private APICount apiCountR;

    private Map<String, MutableInt> apiCount = new HashMap<String, MutableInt>();
    private Iterator apiIterator;

    public ReportFactory(String[] args){

        String fileLoc = "";
        try{
            fileLoc = args[2];
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Save to jar running location");
        }
        try{
            StringBuilder fileName = new StringBuilder();
            this.adcs = new ADCS(fileName.append(fileLoc).append(args[1].split("\\.log\\.")[1]).append("-robi-com.wso2telco.logdebugger.com.reports.ADCS.csv").toString());
            fileName = new StringBuilder();
            this.adms = new ADMS(fileName.append(fileLoc).append(args[1].split("\\.log\\.")[1]).append("-robi-adms.csv").toString());
            fileName = new StringBuilder();
            this.bio = new BIO(fileName.append(fileLoc).append(args[1].split("\\.log\\.")[1]).append("-robi-bio.csv").toString());
            fileName = new StringBuilder();
            this.cbs = new CBS(fileName.append(fileLoc).append(args[1].split("\\.log\\.")[1]).append("-robi-cbs.csv").toString());
            fileName = new StringBuilder();
            this.ebal = new EBAL(fileName.append(fileLoc).append(args[1].split("\\.log\\.")[1]).append("-robi-ebal.csv").toString());
            fileName = new StringBuilder();
            this.fnf = new FNF(fileName.append(fileLoc).append(args[1].split("\\.log\\.")[1]).append("-robi-fnf.csv").toString());
            fileName = new StringBuilder();
            this.lms = new LMS(fileName.append(fileLoc).append(args[1].split("\\.log\\.")[1]).append("-robi-com.wso2telco.logdebugger.com.reports.LMS.csv").toString());
            fileName = new StringBuilder();
            this.mMoney = new MMoney(fileName.append(fileLoc).append(args[1].split("\\.log\\.")[1]).append("-robi-mMoney.csv").toString());
            fileName = new StringBuilder();
            this.ocs = new OCS(fileName.append(fileLoc).append(args[1].split("\\.log\\.")[1]).append("-robi-ocs.csv").toString());
            fileName = new StringBuilder();
            this.payment = new Payment(fileName.append(fileLoc).append(args[1].split("\\.log\\.")[1]).append("-robi-payment.csv").toString());
            fileName = new StringBuilder();
            this.paymentError = new Payment(fileName.append(fileLoc).append("payment-errorReport.csv").toString());
            fileName = new StringBuilder();
            this.sdp = new SDP(fileName.append(fileLoc).append(args[1].split("\\.log\\.")[1]).append("-robi-sdp.csv").toString());
            fileName = new StringBuilder();
            this.sms = new SMS(fileName.append(fileLoc).append(args[1].split("\\.log\\.")[1]).append("-robi-sms.csv").toString());
            fileName = new StringBuilder();
            this.smsError = new SMS(fileName.append(fileLoc).append("sms-errorReport.csv").toString());
            fileName = new StringBuilder();
            this.xrbt = new XRBT(fileName.append(fileLoc).append(args[1].split("\\.log\\.")[1]).append("-robi-xrbt.csv").toString());
            fileName = new StringBuilder();
            this.apiCountR = new APICount(fileName.append(fileLoc).append(args[1].split("\\.log\\.")[1]).append("-robi-api-count.csv").toString());
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initializeReports(){
        StringBuilder record = new StringBuilder();
        record.append("API,MSISDN,Date Time,Service Provider,Application Name,Response Code");
        this.adcs.writeToReport(record, adcs.getReport());
        this.adms.writeToReport(record,adms.getReport());
        this.bio.writeToReport(record,bio.getReport());
        this.cbs.writeToReport(record,cbs.getReport());
        this.ebal.writeToReport(record,ebal.getReport());
        this.fnf.writeToReport(record,fnf.getReport());
        this.lms.writeToReport(record,lms.getReport());
        this.mMoney.writeToReport(record,mMoney.getReport());
        this.ocs.writeToReport(record,ocs.getReport());
        this.sdp.writeToReport(record,sdp.getReport());
        this.xrbt.writeToReport(record,xrbt.getReport());
        record.setLength(0);
        record.append("Api,Couunt");
        this.apiCountR.writeToReport(record,apiCountR.getReport());
        record.setLength(0);
        record.append("Date,Provider,Application,MSISDN,Amount,Description,Transaction Operation Status");
        this.payment.writeToReport(record,payment.getReport());
        record.setLength(0);
        record.append("Api,Date,Service Time,Provider,Api Publisher,Application,Response Code,MSISDN,Operation/Event,Total Amount Charged/Refunded,On Behalf Of,Purchase Category,Tax Amount,Channel,Amount,Currency,Description,Server Reference Code,Client Correlator,Transaction Operation Status,Reference Code,Resource URL");
        this.paymentError.writeToReport(record,paymentError.getReport());
        record.setLength(0);
        record.append("Api,Resource Path,Response Time,Service Time,Provider,Api Publisher,Application,Reference Code,MSISDN,Operation/Event,Client Correlator,Sender,Destination,status,Message,count,Message Id,Filter Criteria,Error Response");
        this.sms.writeToReport(record,sms.getReport());
        this.smsError.writeToReport(record,smsError.getReport());
    }

    public void generateReports(String strLine, long offset){
        String[] payload = strLine.split(", ");
        StringBuilder record;

        MutableInt count = apiCount.get(payload[3]);
        if (count == null) {
            apiCount.put(payload[3], new MutableInt());
        }
        else {
            count.increment();
        }
        if (payload[3].equals("payment")) {
            PaymentRecord paymentRecord = new PaymentRecord();
            record = paymentRecord.setPayload(payload, offset);
            if (paymentRecord.getError() && record != null)
                paymentError.writeToReport(record, paymentError.getReport());
            else if (!paymentRecord.getError()){
                payment.writeToReport(record, payment.getReport());
            }
        } else if (payload[3].equals("smsmessaging")) {
            SMSRecord smsRecord = new SMSRecord();
            record = smsRecord.setPayload(payload, offset);
            if (smsRecord.getError())
                smsError.writeToReport(record, smsError.getReport());
            else
                sms.writeToReport(record, sms.getReport());
        } else if (payload[3].equals("com.wso2telco.logdebugger.com.reports.ADCS")) {
            ADCSRecord adcsRecord = new ADCSRecord();
            record = adcsRecord.setPayload(payload, offset);
            adcs.writeToReport(record, adcs.getReport());
        } else if (payload[3].equals("adms")) {
            ADMSRecord admsRecord = new ADMSRecord();
            record = admsRecord.setPayload(payload, offset);
            adms.writeToReport(record, adms.getReport());
        } else if (payload[3].equals("bio")) {
            BIORecord bioRecord = new BIORecord();
            record = bioRecord.setPayload(payload, offset);
            bio.writeToReport(record, bio.getReport());
        } else if (payload[3].equals("cbs")) {
            CBSRecord cbsRecord = new CBSRecord();
            record = cbsRecord.setPayload(payload, offset);
            cbs.writeToReport(record, cbs.getReport());
        } else if (payload[3].equals("ebal")){
            EBALRecord ebalRecord = new EBALRecord();
            record = ebalRecord.setPayload(payload, offset);
            ebal.writeToReport(record, ebal.getReport());
        } else if (payload[3].equals("fnf")){
            FNFRecord fnfRecord = new FNFRecord();
            record = fnfRecord.setPayload(payload, offset);
            fnf.writeToReport(record, fnf.getReport());
        } else if (payload[3].equals("com.wso2telco.logdebugger.com.reports.LMS")){
            LMSRecord lmsRecord = new LMSRecord();
            record = lmsRecord.setPayload(payload, offset);
            lms.writeToReport(record,lms.getReport());
        } else if (payload[3].equals("com.wso2telco.logdebugger.com.reports.MMoney")){
            MMoneyRecord mMoneyRecord = new MMoneyRecord();
            record = mMoneyRecord.setPayload(payload, offset);
            mMoney.writeToReport(record, mMoney.getReport());
        } else if (payload[3].equals("ocs")){
            OCSRecord ocsRecord = new OCSRecord();
            record = ocsRecord.setPayload(payload, offset);
            ocs.writeToReport(record, ocs.getReport());
        } else if (payload[3].equals("sdp")){
            SDPRecords sdpRecords = new SDPRecords();
            record = sdpRecords.setPayload(payload, offset);
            sdp.writeToReport(record, sdp.getReport());
        } else if (payload[3].equals("xrbt")){
            XRBTRecord xrbtRecord = new XRBTRecord();
            record = xrbtRecord.setPayload(payload, offset);
            xrbt.writeToReport(record, xrbt.getReport());
        }

    }

    public void closeReports(){
        adcs.closeReport();
        adms.closeReport();
        bio.closeReport();
        cbs.closeReport();
        ebal.closeReport();
        fnf.closeReport();
        lms.closeReport();
        mMoney.closeReport();
        ocs.closeReport();
        payment.closeReport();
        paymentError.closeReport();
        sdp.closeReport();
        sms.closeReport();
        smsError.closeReport();
        xrbt.closeReport();
    }

    public void apiCountReport(){
        apiIterator= apiCount.entrySet().iterator();
        MutableInt tmp;
        while (apiIterator.hasNext()){
            StringBuilder record = new StringBuilder();
            Map.Entry pair = (Map.Entry)apiIterator.next();
            tmp = (MutableInt) pair.getValue();
            record.append(pair.getKey().toString()).append(",").append(tmp.get());
            apiCountR.writeToReport(record, apiCountR.getReport());
            System.out.println(record.toString());
        }
        apiCountR.closeReport();
    }

//Close the input stream

}
