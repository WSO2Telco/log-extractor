package com.wso2telco.logdebugger.com.main;

import com.wso2telco.logdebugger.com.reports.ReportFactory;

import java.io.*;

class ReportGenerator
{
    public static void main(String[] args)
    {
        String strLine="";
        int count = 0;
        try{
            // Open the file that is the first
            // command line parameter
            FileInputStream fstream = new FileInputStream(args[1]);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            //Path file1 = Paths.get(args[1]);
            //Path file2 = Paths.get(args[2]);
            ReportFactory reportFactory = new ReportFactory(args);
            long offset = 60000L * 60L * (long)(Float.parseFloat(args[0])*2) / 2L;
            reportFactory.initializeReports();
            
            //Read File Line By Line
            count++;
            while ((strLine = br.readLine()) != null)   {
                // Print the conten t on the console
                if (strLine.contains("payloadData")) {
                    reportFactory.generateReports(strLine, offset);

                }
            }
            //Close the input stream
//            */
            reportFactory.apiCountReport();
            in.close();
            reportFactory.closeReports();


        }catch (Exception e){//Catch exception if any
            e.printStackTrace();
            System.err.println("Error: " + count );
            System.err.println("Error: " + strLine );
        }

    }
}


