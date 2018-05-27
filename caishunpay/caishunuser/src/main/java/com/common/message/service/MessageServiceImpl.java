/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.common.message.service;

import com.common.message.TradeMchtYzm;
import com.common.message.dao.TradeMchtYzmDao;
import com.common.message.service.CloudMessageServiceImpl;
import com.common.message.service.MessageService;
import com.common.model.Response;
import com.gy.util.DateUtil;
import com.gy.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl
        implements MessageService {
    public static final String YZM_STATUS_NEW = "01";
    public static final String YZM_STATUS_ERROR = "02";
    public static final String YZM_STATUS_USED = "00";
    @Autowired
    private TradeMchtYzmDao tradeMchtYzmDaoImpl;
    @Autowired
    private CloudMessageServiceImpl cloudMessageServiceImpl;

    @Override
    public TradeMchtYzm getYzm(String id) {
        return this.tradeMchtYzmDaoImpl.getYzm(id);
    }

    @Override
    public void save(TradeMchtYzm mchtYzm) {
        this.tradeMchtYzmDaoImpl.save(mchtYzm);
    }

    @Override
    public void update(TradeMchtYzm mchtYzm) {
        this.tradeMchtYzmDaoImpl.update(mchtYzm);
    }

    @Override
    public /* varargs */ Response<String> sendSingleMessage(String userNo, String phoneNum, String... msgContent) {
        Response<String> response = new Response<String>();
        String msgKey = UUIDGenerator.getOrderIdByUUId("sm");
        TradeMchtYzm tradeMchtYzm = new TradeMchtYzm();
        tradeMchtYzm.setId(msgKey);
        tradeMchtYzm.setMchtNo(userNo);
        tradeMchtYzm.setTelphone(phoneNum);
        tradeMchtYzm.setYzmType("df");
        tradeMchtYzm.setYzm(msgContent[2]);
        tradeMchtYzm.setInsertTime(DateUtil.getCurrMilliTime());
        String sendResult = this.cloudMessageServiceImpl.sendSingleMessage(phoneNum, "127126", msgContent);
        if ("000000".equals(sendResult)) {
            tradeMchtYzm.setStatus("01");
            response.setCode("00");
            response.setData(msgKey);
            response.setMessage("\u9a8c\u8bc1\u7801\u53d1\u9001\u6210\u529f");
        } else {
            tradeMchtYzm.setStatus("02");
            response.setCode("02");
            response.setMessage("\u83b7\u53d6\u9a8c\u8bc1\u7801\u5931\u8d25:" + sendResult);
        }
        tradeMchtYzm.setMsg_result(sendResult);
        this.tradeMchtYzmDaoImpl.save(tradeMchtYzm);
        return response;
    }

    @Override
    public boolean checkYzmTime(TradeMchtYzm mchtYzm) {
        if (!"01".equals(mchtYzm.getStatus())) {
            return false;
        }
        String expiredDate = DateUtil.getDateBeforeSeconds(180, "yyyyMMddHHmmssSSS");
        if (mchtYzm.getInsertTime().compareTo(expiredDate) < 0) {
            return false;
        }
        mchtYzm.setStatus("00");
        this.tradeMchtYzmDaoImpl.update(mchtYzm);
        return true;
    }
}
