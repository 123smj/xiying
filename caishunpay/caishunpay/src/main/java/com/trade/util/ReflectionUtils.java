package com.trade.util;

import com.trade.annotations.PayChannelImplement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;

import java.util.Map;

public class ReflectionUtils {
    private static Logger log = LoggerFactory.getLogger(ReflectionUtils.class);

    public static <T> void  putServiceTo(T service , Map<String,T> map){
        Class<?> clz = AopProxyUtils.ultimateTargetClass(service);
        PayChannelImplement id = clz.getAnnotation(PayChannelImplement.class);
        if(id == null){
            log.error("cannot read channelid annotation from service :" +clz.getName() + ",please check");
            throw new RuntimeException("cannot read channelid annotation from "+clz.getName());
        } else {
            log.info("loaded impl service :" + clz.getName() + " for channel : " + id.channelId());
            map.put(id.channelId(), service);
        }
    }
}
