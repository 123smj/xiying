/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Component
 */
package com.trade.timer;

import com.account.service.TradeMchtAccountService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DaifuResultTask {
    @Autowired
    private TradeMchtAccountService tradeMchtAccountServiceImpl;
    private static Logger log = Logger.getLogger(DaifuResultTask.class);

    public void queryDfResult() {
        this.tradeMchtAccountServiceImpl.queryUnSuccessDaifuResult();
    }
}
