///*
// * Decompiled with CFR 0_124.
// *
// * Could not load the following classes:
// *  org.apache.log4j.Logger
// *  org.springframework.beans.factory.annotation.Autowired
// *  org.springframework.stereotype.Component
// */
//package com.trade.timer;
//
//import com.trade.service.impl.CommonService;
//import com.trade.service.netpayimpl.NetpayHandlerService;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class NotifyResultTask {
//    @Autowired
//    private CommonService commonService;
//    @Autowired
//    private NetpayHandlerService netpayHandlerService;
//    private static Logger log = Logger.getLogger(NotifyResultTask.class);
//
//    public void notifyResult() {
//        this.commonService.notifyUnsuccessTrade();
//    }
//
//    public void notifyNetpayResult() {
//        this.netpayHandlerService.notifyUnsuccessTrade();
//    }
//}
