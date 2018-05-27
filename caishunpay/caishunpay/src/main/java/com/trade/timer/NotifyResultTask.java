/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Component
 */
package com.trade.timer;

import com.trade.service.impl.NotifyProcessingService;
import com.trade.service.netpayimpl.BankCardPayDispatcherService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotifyResultTask {
    @Autowired
    private NotifyProcessingService notifyProcessingService;
    @Autowired
    private BankCardPayDispatcherService bankCardPayDispatcherService;
    private static Logger log = Logger.getLogger(NotifyResultTask.class);

    public void notifyResult() {
        try {
            this.notifyProcessingService.thirdPartyDetailNotifyTask();
        } catch (Exception e) {
            log.error((Object) ("\u5b9a\u65f6\u4efb\u52a1\u5f02\u5e38:notifyResult" + e.getMessage()));
        }
    }

    public void notifyNetpayResult() {
        try {
            this.notifyProcessingService.bankCardPayDetailNotifyTask();
        } catch (Exception e) {
            log.error((Object) ("\u5b9a\u65f6\u4efb\u52a1\u5f02\u5e38:notifyNetpayResult" + e.getMessage()));
        }
    }
}
