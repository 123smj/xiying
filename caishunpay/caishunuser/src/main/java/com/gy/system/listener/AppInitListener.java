/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Logger
 *  org.springframework.context.ApplicationContext
 *  org.springframework.context.ApplicationEvent
 *  org.springframework.context.ApplicationListener
 *  org.springframework.context.annotation.Scope
 *  org.springframework.context.event.ContextRefreshedEvent
 *  org.springframework.stereotype.Component
 */
package com.gy.system.listener;

import com.common.dwr.SelectOptionUnit;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component(value = "BeanDefineConfigue")
@Scope(value = "singleton")
public class AppInitListener
        implements ApplicationListener<ContextRefreshedEvent> {
    private static Logger log = Logger.getLogger(AppInitListener.class);

    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info((Object) ("event.getApplicationContext().getParent(): " + (Object) event.getApplicationContext().getParent()));
        if (event.getApplicationContext().getParent() == null) {
            try {
                SelectOptionUnit.initSelectOptions(event.getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
