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
import com.gy.system.Environment;
import com.gy.system.SysParamUtil;
import com.gy.util.CommonFunction;
import com.gy.util.HttpUtility;
import com.gy.util.StringUtil;
import com.gy.util.UUIDGenerator;
import com.trade.bean.own.QrcodeMchtInfo;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeSourceEnum;
import com.trade.service.QrcodeMchtInfoService;
import com.trade.util.GuangdaUtil;
import com.trade.util.JsonUtil;

import java.math.BigDecimal;
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
public class MchtDaikouController {
    private static Logger log = Logger.getLogger(MchtDaikouController.class);
    @Autowired
    private QrcodeMchtInfoService qrcodeMchtInfoServiceImpl;
    @Autowired
    private MessageService messageServiceImpl;

    @RequestMapping(value = {"/mchtXieyi"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public String mchtXieyi(HttpServletRequest request) {
        QrcodeMchtInfo mchtInfo = (QrcodeMchtInfo) request.getSession().getAttribute("mchtInfo");
        if (!CommonFunction.isTradeSourceOpen((mchtInfo = this.qrcodeMchtInfoServiceImpl.getMchtInfo(mchtInfo.getMchtNo())).getTrade_source_list(), TradeSourceEnum.DAIKOU)) {
            return "02--\u60a8\u6682\u672a\u5f00\u901a\u4ee3\u6263\u529f\u80fd\uff01";
        }
        String orderNo = UUIDGenerator.getOrderIdByUUId("DK");
        String mcht_no = mchtInfo.getMchtNo();
        String card_name = request.getParameter("card_name");
        String cer_number = request.getParameter("cer_number");
        String bank_card_no = request.getParameter("bank_card_no");
        String mobile_number = request.getParameter("mobile_number");
        String dfAmount = request.getParameter("dfAmount");
        String money = String.valueOf(BigDecimal.valueOf(Double.parseDouble(dfAmount)).multiply(new BigDecimal(100)).intValue());
        String response = ResponseEnum.FAIL_SYSTEM.getCode();
        if (StringUtil.isEmpty(bank_card_no)) {
            return "01--\u5361\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(card_name)) {
            return "01--\u59d3\u540d\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(dfAmount)) {
            return "01--\u91d1\u989d\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(cer_number)) {
            return "01--\u8eab\u4efd\u8bc1\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(mobile_number)) {
            return "01--\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        try {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("gymchtId", mcht_no);
            param.put("tradeSn", orderNo);
            param.put("orderAmount", money);
            param.put("cardHolderName", card_name);
            param.put("cardNo", bank_card_no);
            param.put("cardType", "01");
            param.put("expireDate", "9999");
            param.put("cvv", "999");
            param.put("bankName", "\u94f6\u884c");
            param.put("cerType", "01");
            param.put("cerNumber", cer_number);
            param.put("nonce", StringUtil.getRandom(16));
            param.put("mobileNum", mobile_number);
            param.put("sign", GuangdaUtil.getMd5SignByMap(param, mchtInfo.getSecretKey(), "utf-8"));
            String keyValues = GuangdaUtil.map2HttpParam(param);
            Environment evn = Environment.createEnvironment(SysParamUtil.getParam("daikou_pre_path"), "utf-8", "application/x-www-form-urlencoded");
            String jsonResult = HttpUtility.postData(evn, keyValues);
            Map resultMap = (Map) JsonUtil.parseJson(jsonResult);
            response = resultMap != null ? ("00000".equals(resultMap.get("resultCode")) ? "00--\u5b50\u534f\u8bae\u83b7\u53d6\u6210\u529f--" + (String) resultMap.get("yzm") + "--" + (String) resultMap.get("transaction_id") + "--" + orderNo : String.valueOf((String) resultMap.get("resultCode")) + "--" + (String) resultMap.get("message")) : ResponseEnum.BACK_EXCEPTION.getCode();
        } catch (Exception e) {
            response = "\u4ee3\u6263\u5f02\u5e38";
            e.printStackTrace();
            log.error((Object) ("exception-----opr_id:" + mchtInfo.getMchtNo() + e));
        }
        return response;
    }

    @RequestMapping(value = {"/mchtDaikou"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public String mchtDaikou(HttpServletRequest request) {
        QrcodeMchtInfo mchtInfo = (QrcodeMchtInfo) request.getSession().getAttribute("mchtInfo");
        if (!CommonFunction.isTradeSourceOpen((mchtInfo = this.qrcodeMchtInfoServiceImpl.getMchtInfo(mchtInfo.getMchtNo())).getTrade_source_list(), TradeSourceEnum.DAIKOU)) {
            return "02--\u60a8\u6682\u672a\u5f00\u901a\u4ee3\u6263\u529f\u80fd\uff01";
        }
        String orderNo = request.getParameter("orderNo");
        String transaction_id = request.getParameter("transaction_id");
        String yzm = request.getParameter("yzm");
        String mcht_no = mchtInfo.getMchtNo();
        String card_name = request.getParameter("card_name");
        String cer_number = request.getParameter("cer_number");
        String bank_card_no = request.getParameter("bank_card_no");
        String mobile_number = request.getParameter("mobile_number");
        String dfAmount = request.getParameter("dfAmount");
        String money = String.valueOf(BigDecimal.valueOf(Double.parseDouble(dfAmount)).multiply(new BigDecimal(100)).intValue());
        String response = ResponseEnum.FAIL_SYSTEM.getCode();
        if (StringUtil.isEmpty(bank_card_no)) {
            return "01--\u5361\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(card_name)) {
            return "01--\u59d3\u540d\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(dfAmount)) {
            return "01--\u91d1\u989d\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(cer_number)) {
            return "01--\u8eab\u4efd\u8bc1\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(mobile_number)) {
            return "01--\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        try {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("gymchtId", mcht_no);
            param.put("tradeSn", orderNo);
            param.put("orderAmount", money);
            param.put("cardHolderName", card_name);
            param.put("cardNo", bank_card_no);
            param.put("cardType", "01");
            param.put("expireDate", "9999");
            param.put("cvv", "999");
            param.put("bankName", "\u94f6\u884c");
            param.put("cerType", "01");
            param.put("cerNumber", cer_number);
            param.put("nonce", StringUtil.getRandom(16));
            param.put("mobileNum", mobile_number);
            param.put("transaction_id", transaction_id);
            param.put("yzm", yzm);
            param.put("sign", GuangdaUtil.getMd5SignByMap(param, mchtInfo.getSecretKey(), "utf-8"));
            String keyValues = GuangdaUtil.map2HttpParam(param);
            Environment evn = Environment.createEnvironment(SysParamUtil.getParam("daikou_check_path"), "utf-8", "application/x-www-form-urlencoded");
            String jsonResult = HttpUtility.postData(evn, keyValues);
            Map resultMap = (Map) JsonUtil.parseJson(jsonResult);
            if (resultMap != null) {
                if ("00000".equals(resultMap.get("resultCode"))) {
                    String res = resultMap.get("tradeState") == null || !"SUCCESS".equals(resultMap.get("tradeState")) ? "\u4ee3\u6263\u53d1\u8d77\u6210\u529f\uff0c\u7b49\u5f85\u5ba1\u6838" : "\u4ee3\u6263\u6210\u529f";
                    response = "00--" + res;
                } else {
                    response = String.valueOf((String) resultMap.get("resultCode")) + "--" + (String) resultMap.get("message");
                }
            } else {
                response = ResponseEnum.BACK_EXCEPTION.getCode();
            }
        } catch (Exception e) {
            response = "\u4ee3\u6263\u5f02\u5e38";
            e.printStackTrace();
            log.error((Object) ("exception-----opr_id:" + mchtInfo.getMchtNo() + e));
        }
        return response;
    }
}
