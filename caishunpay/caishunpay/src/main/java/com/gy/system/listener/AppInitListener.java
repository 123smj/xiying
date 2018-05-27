/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.context.ApplicationContext
 *  org.springframework.context.ApplicationEvent
 *  org.springframework.context.ApplicationListener
 *  org.springframework.context.annotation.Scope
 *  org.springframework.context.event.ContextRefreshedEvent
 *  org.springframework.stereotype.Component
 */
package com.gy.system.listener;

import com.common.dwr.SelectOptionUnit;
import com.gy.system.DictParamUtil;
import com.gy.system.bean.IpWhiteList;
import com.gy.system.dao.impl.IpWhiteListDaoImpl;
import java.io.PrintStream;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component(value="BeanDefineConfigue")
@Scope(value="singleton")
public class AppInitListener
implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private IpWhiteListDaoImpl ipWhiteListDaoImpl;
    private static Logger log = Logger.getLogger(AppInitListener.class);

    //Remove temply
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info((Object)("event.getApplicationContext().getParent(): " + (Object)event.getApplicationContext().getParent()));
        if (event.getApplicationContext().getParent() == null) {
            try {
            SelectOptionUnit.initSelectOptions(event.getApplicationContext());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
//            log.info((Object)"init white ip list...");
//            List<IpWhiteList> list = this.ipWhiteListDaoImpl.listIpAll();
//            for (IpWhiteList ipWhiteList : list) {
//                System.out.println(String.valueOf(ipWhiteList.getIp()) + "---" + ipWhiteList.getFlag() + "---" + ipWhiteList.getAddDate());
//                DictParamUtil.whiteIpList.add(ipWhiteList.getIp());
//            }
//            log.info((Object)("white ip list: " + DictParamUtil.whiteIpList));
        }
    }
}
