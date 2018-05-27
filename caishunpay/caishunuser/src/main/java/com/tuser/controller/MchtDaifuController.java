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

import com.common.message.TradeMchtYzm;
import com.common.message.service.MessageService;
import com.gy.system.Environment;
import com.gy.system.SysParamUtil;
import com.gy.util.HttpUtility;
import com.gy.util.StringUtil;
import com.gy.util.UUIDGenerator;
import com.trade.bean.own.QrcodeMchtInfo;
import com.trade.enums.ResponseEnum;
import com.trade.service.QrcodeMchtInfoService;
import com.trade.util.GuangdaUtil;
import com.trade.util.JsonUtil;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = {"/tuser"})
public class MchtDaifuController {
    private static Logger log = Logger.getLogger(MchtDaifuController.class);
    @Autowired
    private QrcodeMchtInfoService qrcodeMchtInfoServiceImpl;
    @Autowired
    private MessageService messageServiceImpl;

    @RequestMapping(value = {"/mchtSinglePay"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public String mchtSinglePay(HttpServletRequest request, String bank_name, String bank_card_no, String card_name, Double dfAmount) {
        QrcodeMchtInfo mchtInfo;
        QrcodeMchtInfo mchtBaseInf;
        String response;
        block12:
        {
            TradeMchtYzm mchtYzm;
            block11:
            {
                String msgKey;
                String yzm;
                block10:
                {
                    mchtInfo = (QrcodeMchtInfo) request.getSession().getAttribute("mchtInfo");
                    yzm = request.getParameter("yzm");
                    msgKey = request.getParameter("msgKey");
                    response = ResponseEnum.FAIL_SYSTEM.getCode();
                    if (StringUtil.isEmpty(bank_name)) {
                        return "\u6536\u6b3e\u94f6\u884c\u4e0d\u80fd\u4e3a\u7a7a";
                    }
                    if (StringUtil.isEmpty(bank_card_no)) {
                        return "\u6536\u6b3e\u5361\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
                    }
                    if (StringUtil.isEmpty(card_name)) {
                        return "\u6536\u6b3e\u4eba\u4e0d\u80fd\u4e3a\u7a7a";
                    }
                    if (dfAmount == null) {
                        return "\u4ee3\u4ed8\u91d1\u989d\u4e0d\u80fd\u4e3a\u7a7a";
                    }
                    if (StringUtil.isEmpty(yzm)) {
                        return "\u9a8c\u8bc1\u7801\u4e0d\u80fd\u4e3a\u7a7a";
                    }
                    mchtBaseInf = this.qrcodeMchtInfoServiceImpl.getMchtInfo(mchtInfo.getMchtNo());
                    if (mchtBaseInf != null) break block10;
                    return "\u5546\u6237\u4e0d\u5b58\u5728";
                }
                mchtYzm = this.messageServiceImpl.getYzm(msgKey);
                if (!StringUtil.isEmpty(yzm) && mchtYzm != null && yzm.equals(mchtYzm.getYzm()))
                    break block11;
                return "\u9a8c\u8bc1\u7801\u6709\u8bef";
            }
            if (this.messageServiceImpl.checkYzmTime(mchtYzm)) break block12;
            return "\u65e0\u6548\u9a8c\u8bc1\u7801";
        }
        try {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("gymchtId", mchtBaseInf.getMchtNo());
            param.put("dfSn", UUIDGenerator.getOrderIdByUUId("sp"));
            param.put("receiptAmount", String.valueOf(StringUtil.changeY2F(dfAmount)));
            param.put("curType", "1");
            param.put("payType", "1");
            param.put("receiptName", StringUtil.trim(card_name));
            param.put("receiptPan", StringUtil.trim(bank_card_no));
            param.put("receiptBankNm", StringUtil.trim(bank_name));
            param.put("acctType", "0");
            param.put("mobile", mchtBaseInf.getPhone());
            param.put("nonce", StringUtil.getRandom(16));
            param.put("sign", GuangdaUtil.getMd5SignByMap(param, mchtBaseInf.getSecretKey(), "utf-8"));
            String keyValues = GuangdaUtil.map2HttpParam(param);
            Environment evn = Environment.createEnvironment(SysParamUtil.getParam("daifu_path"), "utf-8", "application/x-www-form-urlencoded");
            String jsonResult = HttpUtility.postData(evn, keyValues);
            Map resultMap = (Map) JsonUtil.parseJson(jsonResult);
            response = resultMap != null ? ("00000".equals(resultMap.get("resultCode")) ? ResponseEnum.SUCCESS.getCode() : String.valueOf((String) resultMap.get("resultCode")) + "---" + (String) resultMap.get("message")) : ResponseEnum.BACK_EXCEPTION.getCode();
        } catch (Exception e) {
            response = "\u4ee3\u4ed8\u5f02\u5e38";
            e.printStackTrace();
            log.error((Object) ("exception-----opr_id:" + mchtInfo.getMchtNo() + e));
        }
        return response;
    }
}
