/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  javax.servlet.ServletContext
 *  javax.servlet.ServletContextEvent
 *  javax.servlet.ServletContextListener
 *  org.springframework.context.ApplicationContext
 *  org.springframework.web.context.WebApplicationContext
 *  org.springframework.web.context.support.WebApplicationContextUtils
 */
package com.gy.util;

import com.gy.util.ContextUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ContextLoaderListener
        implements ServletContextListener {
    public void contextDestroyed(ServletContextEvent arg0) {
    }

    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext((ServletContext) context);
        ContextUtil.setContext((ApplicationContext) ctx);
    }
}
