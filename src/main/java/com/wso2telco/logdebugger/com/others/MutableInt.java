package com.wso2telco.logdebugger.com.others;

public class MutableInt {
    private int value = 1; // note that we start at 1 since we're counting
    public void increment () { ++value;      }
    public int  get ()       { return value; }
}
