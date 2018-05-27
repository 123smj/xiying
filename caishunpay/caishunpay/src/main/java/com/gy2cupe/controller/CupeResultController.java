/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Controller
 *  org.springframework.ui.Model
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.ResponseBody
 */
package com.gy2cupe.controller;

import com.gy.system.Environment;
import com.gy.util.HttpUtility;
import com.gy.util.StringUtil;
import com.gy2cupe.service.CupeResultService;
import com.trade.util.MD5Util;
import com.trade.util.JsonUtil;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value={"/cupeResult"})
public class CupeResultController {
    private static Logger log = Logger.getLogger(CupeResultController.class);
    @Autowired
    private CupeResultService cupeResultService;

    @ResponseBody
    @RequestMapping(value={"/notifyResult.do"}, produces={"text/plain;charset=utf-8"})
    public String notifyResult(HttpServletRequest request, Model model) {
        HashMap<String, String> param;
        String result;
        block11 : {
            List resultList;
            block10 : {
                log.info((Object)("\u652f\u4ed8\u7ed3\u679c\u901a\u77e5begin------\u5546\u6237\u4ea4\u6613\u53f7\uff1a" + request.getParameter("p7")));
                result = "FAIL";
                param = new HashMap<String, String>();
                param.put("c", request.getParameter("c"));
                param.put("t", request.getParameter("t"));
                param.put("r", request.getParameter("r"));
                param.put("p0", request.getParameter("p0"));
                param.put("p1", request.getParameter("p1"));
                param.put("p2", request.getParameter("p2"));
                param.put("p3", request.getParameter("p3"));
                param.put("p4", request.getParameter("p4"));
                param.put("p5", request.getParameter("p5"));
                param.put("p6", request.getParameter("p6"));
                param.put("p7", request.getParameter("p7"));
                if (request.getParameter("p7") == null || request.getParameter("p6") == null || request.getParameter("p5") == null || request.getParameter("p3") == null || request.getParameter("p2") == null || request.getParameter("p1") == null || request.getParameter("p0") == null || request.getParameter("c") == null) {
                    log.info((Object)"\u53c2\u6570\u6709\u8bef");
                    return "\u53c2\u6570\u6709\u8bef";
                }
                log.info(param);
                if (!this.cupeResultService.checkSign(param, request.getParameter("s"))) {
                    log.info((Object)"\u7b7e\u540d\u9519\u8bef");
                    return "\u7b7e\u540d\u9519\u8bef";
                }
                model.addAttribute("result", (Object)result);
                resultList = this.cupeResultService.getTradeStatus(param.get("p7"));
                if (resultList != null && resultList.size() != 0) break block10;
                log.info((Object)"\u672a\u627e\u5230\u5bf9\u5e94\u4ea4\u6613");
                return "\u672a\u627e\u5230\u5bf9\u5e94\u4ea4\u6613";
            }
            Object[] rst = (Object[])resultList.get(0);
            String payStatus = StringUtil.trans2Str(rst[0]);
            String notifyUrl = StringUtil.trans2Str(rst[1]);
            if (!StringUtil.isEmpty(notifyUrl)) {
                Environment evn = Environment.createEnvironment(notifyUrl, "utf-8", "application/json");
                HashMap<String, String> notifyMap = new HashMap<String, String>();
                notifyMap.put("payApp", param.get("p1"));
                notifyMap.put("channelTradeSn", param.get("p0"));
                notifyMap.put("equipCode", param.get("p6"));
                notifyMap.put("gymchtId", "");
                notifyMap.put("tradeSn", param.get("p7"));
                notifyMap.put("orderAmount", param.get("p3"));
                notifyMap.put("coupon_fee", param.get("p4"));
                notifyMap.put("bankType", "");
                notifyMap.put("t0Flag", "0");
                if ("0".equals(param.get("c"))) {
                    notifyMap.put("pay_result", "0");
                    notifyMap.put("timeEnd", param.get("p2"));
                } else {
                    notifyMap.put("pay_result", param.get("c"));
                    notifyMap.put("pay_info", "");
                }
                notifyMap.put("sign", MD5Util.getMd5SignByMap(notifyMap, "C0D26A23A03343E695D480D254C932FB", new String[0]));
                String jsonStr = JsonUtil.buildJson(notifyMap);
                log.info((Object)("\u56de\u8c03\u63a5\u5165\u65b9:" + notifyUrl + "----" + jsonStr));
                String notifyBack = HttpUtility.postData(evn, jsonStr);
                log.info((Object)("\u56de\u8c03\u63a5\u5165\u65b9\u8fd4\u56de:" + notifyBack));
            }
            if ("0".equals(payStatus)) break block11;
            log.info((Object)"\u652f\u4ed8\u7ed3\u679c\u5df2\u5b58\u5728");
            return "\u652f\u4ed8\u7ed3\u679c\u5df2\u5b58\u5728";
        }
        try {
            int saveRst = this.cupeResultService.saveBackResult(param);
            if (saveRst > 0) {
                result = "SUCCESS";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            result = "\u7a0b\u5e8f\u5f02\u5e38";
            log.info((Object)e.getMessage());
        }
        log.info((Object)"----\u652f\u4ed8\u7ed3\u679c\u901a\u77e5end------");
        return result;
    }
}
