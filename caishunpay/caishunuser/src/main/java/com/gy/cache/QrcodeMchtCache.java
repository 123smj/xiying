/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.cache.annotation.Cacheable
 *  org.springframework.stereotype.Service
 */
package com.gy.cache;

import com.trade.bean.own.JumpListBean;
import com.trade.dao.JumpListDao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class QrcodeMchtCache {
    private static Logger log = Logger.getLogger(QrcodeMchtCache.class);
    @Autowired
    private JumpListDao jumpListDaoImpl;

    //    @Cacheable(value = {"qrcodeMchtCache"})
    public List<JumpListBean> getJumpList(String jumpGroup, String tradeSource) {
        log.info((Object) "\u52a0\u8f7d\u7f13\u5b58\u8df3\u7801\u7ec4\u4fe1\u606f");
        List<JumpListBean> list = this.jumpListDaoImpl.getJumpList(jumpGroup, tradeSource);
        if (list == null || list.size() == 0) {
            return list;
        }
        ArrayList<JumpListBean> channelList = new ArrayList<JumpListBean>();
        for (JumpListBean jumpBean : list) {
            int i = 0;
            while (i < jumpBean.getWeight()) {
                JumpListBean channelInfo = new JumpListBean();
                channelInfo.setChannel_id(jumpBean.getChannel_id());
                channelInfo.setChannel_mcht_no(jumpBean.getChannel_mcht_no());
                channelList.add(channelInfo);
                ++i;
            }
        }
        return channelList;
    }
}
