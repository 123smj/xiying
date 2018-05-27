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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoQueryTradeTask {
    @Autowired
    private NotifyProcessingService notifyProcessingService;
    private static Logger log = Logger.getLogger(AutoQueryTradeTask.class);

    public void notifyResult() {
    }
}
