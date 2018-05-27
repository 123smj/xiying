/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Logger
 *  org.springframework.context.ApplicationContext
 */
package com.gy.util;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

public class ContextUtil {
    private static ApplicationContext context;
    private static Logger log;

    static {
        log = Logger.getLogger(ContextUtil.class);
    }

    public static void setContext(ApplicationContext ctx) {
        context = ctx;
    }

    public static Object getBean(String id) {
        Object obj = context.getBean(id);
        if (obj == null) {
            log.info((Object) ("bean id [ " + id + " ] not found in context path"));
        }
        return obj;
    }
}
