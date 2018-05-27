
package com.trade.controller;

import com.gy.util.HttpUtility;
import com.gy.util.MD5Encrypt;
import com.trade.service.impl.NotifyProcessingService;
import com.trade.util.*;
import com.trade.util.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.*;

/***
 * 支付结果通知接口
 */
@Controller
@RequestMapping(value = {"/notify"})
public class NotifyController {
    private static Logger log = Logger.getLogger(NotifyController.class);

    @Autowired
    private NotifyProcessingService service;

    @ResponseBody
    @RequestMapping(value = {"/notifyHaibeiNetpay.do"})
    public String notifyHaibeiNetpay(HttpServletRequest request) {
        String response;
        String requestValue;
        try {
            requestValue = StreamUtil.getString((InputStream) request.getInputStream(), (String) "UTF-8");
            if (requestValue == null)
                return "fail";
            log.info("海贝网银交易结果通知" + requestValue);
            requestValue = URLDecoder.decode(requestValue, "UTF-8");
            Map resultMap = MD5Util.httpParam2Map((String) requestValue);
            this.service.acceptThirdPartyNotify("haibei", resultMap);
            return "OK";
        } catch (Exception e) {
            response = "fail";
            e.printStackTrace();
        }
        return response;
    }

    @ResponseBody
    @RequestMapping(value = {"/notifyHaibei.do"})
    public String notifyHaibei(HttpServletRequest request) {
        String response;
        String requestValue;
        try {
            requestValue = StreamUtil.getString((InputStream) request.getInputStream(), (String) "UTF-8");
            if (requestValue == null)
                return "fail";
            log.info("海贝三方交易结果通知" + requestValue);
            requestValue = URLDecoder.decode(requestValue, "UTF-8");
            Map resultMap = MD5Util.httpParam2Map((String) requestValue);
            this.service.acceptBankCardNotify("haibei", resultMap);
            return "OK";
        } catch (Exception e) {
            response = "fail";
            e.printStackTrace();
        }
        return response;
    }

    @ResponseBody
    @RequestMapping(value = {"/notifyHengfutong.do"})
    public String notifyHengfutong(HttpServletRequest request) {
        String response;
        try {
            request.setCharacterEncoding("utf-8");
            Map<String, String> resultMap = new HashMap<>();
            Enumeration e = request.getParameterNames();
            while (e.hasMoreElements()) {
                String paramName = (String) e.nextElement();
                String paramValue = request.getParameter(paramName);
                resultMap.put(paramName, paramValue);
            }
            this.service.acceptThirdPartyNotify("hengfutong", resultMap);
            return "ISRESPONSION";
        } catch (Exception e) {
            response = "fail";
            e.printStackTrace();
            log.error("恒付通交易结果回传时出错:", e);
        }
        log.info("恒付通交易结果回传:" + response);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = {"/notifyWanzhongyunfu.do"})
    public String notifyWanzhongyunfu(HttpServletRequest request, @RequestBody String json) {
        String response;
        try {
            request.setCharacterEncoding("utf-8");
            log.info("get wzyf result string:" + json);
            Map resultMap = JsonUtil.gsonParseJson(json);
            this.service.acceptThirdPartyNotify("wanzhongyunfu", resultMap);
            return "success";
        } catch (Exception e) {
            response = "fail";
            e.printStackTrace();
            log.error("万众云付交易结果回传时出错:", e);
        }
        log.info("万众云付交易结果回传:" + response);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = {"/notifyZhihuifu.do"})
    public String notifyZhihuifu(HttpServletRequest request, @RequestParam Map<String, String> params) {
        String response;
        try {
            this.service.acceptThirdPartyNotify("zhihuifu", params);
            return "success";
        } catch (Exception e) {
            response = "fail";
            e.printStackTrace();
            log.error("智慧付回传时出错:", e);
        }
        log.info("万众云付交易结果回传:" + response);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = {"/notifyYinshengbaoQP.do"})
    public String notifyYinshengbaoQP(HttpServletRequest request, @RequestParam Map<String, String> params) {
        String response;
        try {
            log.info("get ysb result string:" + JsonUtil.buildJson(params));
            params.put("__TYPE", "quickpay");
            this.service.acceptThirdPartyNotify("yinshengbao", params);
            return "success";
        } catch (Exception e) {
            response = "fail";
            e.printStackTrace();
            log.error("银生宝QP回传时出错:", e);
        }
        log.info("银生宝QP结果回传:" + response);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = {"/notifyYinshengbaoNP.do"})
    public String notifyYinshengbaoNP(HttpServletRequest request, @RequestParam Map<String, String> params) {
        String response;
        try {
            log.info("get ysb result string:" + JsonUtil.buildJson(params));
            params.put("__TYPE", "netpay");
            this.service.acceptThirdPartyNotify("yinshengbao", params);
            return "success";
        } catch (Exception e) {
            response = "fail";
            e.printStackTrace();
            log.error("银生宝NP回传时出错:", e);
        }
        log.info("银生宝NP交易结果回传:" + response);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = {"/notifyHuanxunNP.do"})
    public String notifyHuanxunNP(HttpServletRequest request, @RequestParam Map<String, String> params) {
        String response;
        try {
            log.info("get huanxun result string:" + JsonUtil.buildJson(params));
            params.put("__TYPE", "netpay");
            this.service.acceptThirdPartyNotify("huanxun", params);
            return "success";
        } catch (Exception e) {
            response = "fail";
            e.printStackTrace();
            log.error("银生宝NP回传时出错:", e);
        }
        log.info("银生宝NP交易结果回传:" + response);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = {"/notifyZhiwukeji.do"})
    public String notifyZhiwukeji(HttpServletRequest request, @RequestBody String json) {
        String response;
        try {
            request.setCharacterEncoding("utf-8");
            log.info("get zhiwukeji result string:" + json);
            Map resultMap = JsonUtil.gsonParseJson(json);
            this.service.acceptThirdPartyNotify("zhiwukeji", resultMap);
            return "success";
        } catch (Exception e) {
            response = "fail";
            e.printStackTrace();
            log.error("志武科技回传时出错:", e);
        }
        log.info("志武科技交易结果回传:" + response);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = {"/notifyYinshengbao.do"})
    public String notifyYinshengbao(HttpServletRequest request, @RequestBody String json) {
        String response;
        try {
            request.setCharacterEncoding("utf-8");
            log.info("get yinshengbao result string:" + json);
            Map resultMap = JsonUtil.gsonParseJson(json);
            this.service.acceptThirdPartyNotify("yinshengbao", resultMap);
            return "success";
        } catch (Exception e) {
            response = "fail";
            e.printStackTrace();
            log.error("yinshengbao回传时出错:", e);
        }
        log.info("yinshengbao交易结果回传:" + response);
        return response;
    }



    @ResponseBody
    @RequestMapping(value = {"/notifyShiyuntong.do"})
    public String notifyShiyuntong(HttpServletRequest request, @RequestBody String body) {
        String response;
        try {
            request.setCharacterEncoding("utf-8");
            log.info("get shiyuntong result string:" + body);
            body = URLDecoder.decode(body, "UTF-8");
            String sign = body.split("\\|")[0];
            String json = body.split("\\|")[1].replace("=", "");
            Map root = JsonUtil.gsonParseJson(json);
            Map data = (Map) JsonUtil.gsonParseJson(json).remove("data");
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("rawJson", json);
            resultMap.put("sign", sign);
            resultMap.put("orderId", data.get("orderId").toString());
//            resultMap.put("orderAmount", data.get("orderAmount").toString());
            resultMap.put("code", root.get("code").toString());
            resultMap.put("message", root.get("message").toString());
            this.service.acceptThirdPartyNotify("shiyuntong", resultMap);
            return "success";
        } catch (Exception e) {
            response = "fail";
            e.printStackTrace();
            log.error("回传时出错:", e);
        }
        log.info("交易结果回传:" + response);
        return response;
    }
    @ResponseBody
    @RequestMapping(value = {"/notifyCS.do"})
    public String  notifyCS(HttpServletRequest request, @RequestBody String json){
        String response;
        try {
            request.setCharacterEncoding("utf-8");
            log.info("get cs result string:" + json);
            Map resultMap = JsonUtil.gsonParseJson(json);
            this.service.acceptThirdPartyNotify("cs", resultMap);
            return "success";
        } catch (Exception e) {
            response = "fail";
            e.printStackTrace();
            log.error("cs付交易结果回传时出错:", e);
        }
        log.info("cs付交易结果回传:" + response);
        return response;
    }

}
