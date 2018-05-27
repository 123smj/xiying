/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpSession
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Controller
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.ResponseBody
 */
package com.tuser.controller;

import com.common.message.service.MessageService;
import com.common.model.Response;
import com.gy.util.StringUtil;
import com.trade.bean.own.QrcodeMchtInfo;
import com.trade.service.QrcodeMchtInfoService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = {"/yzm"})
public class YzmController {
    private static Logger log = Logger.getLogger(YzmController.class);
    @Autowired
    private QrcodeMchtInfoService qrcodeMchtInfoServiceImpl;
    @Autowired
    private MessageService messageServiceImpl;

    @RequestMapping(value = {"/sendYzm"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public Response<String> accountAdd(HttpServletRequest request) {
        QrcodeMchtInfo mchtInfo = (QrcodeMchtInfo) request.getSession().getAttribute("mchtInfo");
        String bank_card_no = request.getParameter("bank_card_no");
        String dfAmount = request.getParameter("dfAmount");
        Response<String> response = new Response<String>();
        try {
            if (StringUtil.isEmpty(dfAmount)) {
                response.setCode("02");
                response.setMessage("\u4ee3\u4ed8\u91d1\u989d\u4e0d\u80fd\u4e3a\u7a7a");
                return response;
            }
            if (StringUtil.isEmpty(bank_card_no) || bank_card_no.length() < 4) {
                response.setCode("03");
                response.setMessage("\u4ee3\u4ed8\u5361\u53f7\u6709\u8bef");
                return response;
            }
            QrcodeMchtInfo mchtBaseInf = this.qrcodeMchtInfoServiceImpl.getMchtInfo(mchtInfo.getMchtNo());
            if (mchtBaseInf == null || StringUtil.isEmpty(mchtBaseInf.getPhone())) {
                response.setCode("01");
                response.setMessage("\u624b\u673a\u53f7\u4e3a\u7a7a");
                return response;
            }
            String randomYzm = StringUtil.getRandNum(6);
            response = this.messageServiceImpl.sendSingleMessage(mchtBaseInf.getMchtNo(), mchtBaseInf.getPhone(), bank_card_no.substring(bank_card_no.length() - 4), dfAmount, randomYzm);
        } catch (Exception e) {
            response.setCode("09");
            response.setMessage("\u83b7\u53d6\u9a8c\u8bc1\u7801\u5f02\u5e38");
            e.printStackTrace();
            log.error((Object) ("exception-----opr_id:" + mchtInfo.getMchtNo() + e));
        }
        return response;
    }
}
