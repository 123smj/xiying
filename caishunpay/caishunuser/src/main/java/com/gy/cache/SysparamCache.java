/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Logger
 *  org.springframework.cache.annotation.Cacheable
 *  org.springframework.stereotype.Service
 */
package com.gy.cache;

import com.gy.system.SysParamUtil;
import com.gy.util.StringUtil;

import java.io.PrintStream;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class SysparamCache {
    private static Logger log = Logger.getLogger(SysparamCache.class);

    @Cacheable(value = {"alipayTimeLimitCache"})
    public ArrayList<String> getAlipayTimeLimit() {
        System.out.println("\u52a0\u8f7d\u652f\u4ed8\u5b9d\u652f\u4ed8\u9650\u5236\u65f6\u95f4");
        ArrayList<String> timeLimit = new ArrayList<String>();
        SysParamUtil.reloadProps();
        String timeBegin = SysParamUtil.getParam("alipay_time_begin");
        String timeEnd = SysParamUtil.getParam("alipay_time_end");
        timeLimit.add(StringUtil.isEmpty(timeBegin) ? "080000" : timeBegin);
        timeLimit.add(StringUtil.isEmpty(timeEnd) ? "230059" : timeEnd);
        return timeLimit;
    }
}
