package com.trade.controller;

import com.gy.system.SysParamUtil;
import com.gy.util.StringUtil;
import com.manage.bean.RedirectInfo;
import com.trade.util.AesUtil;
import com.trade.util.JsonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/payredirect")
public class RedirectController {
    private static String AES_KEY = SysParamUtil.getParam("AES_KEY");
    private static String REDIRECT_URL = SysParamUtil.getParam("ROOT_URL") + "payredirect/jump";

    @RequestMapping("/jump")
    public ModelAndView acceptJump(HttpServletRequest request, @RequestParam String cipher) throws Exception {
        RedirectInfo info = JsonUtil.parseJson(decodeCipher(cipher), RedirectInfo.class);
        System.out.println(JsonUtil.buildJson(info));
        Map<String, Object> params = new HashMap<>();
        params.put("methodName", info.getMethodName());
        params.put("redirectUrl", info.getRedirectUrl());
        params.put("mapBean", info.getParamters());
        return new ModelAndView("/trade/redirect_jump", params);
    }

    public static String makeJump(Map<String, String> parameters, String methodName, String redirectUrl, String srcUrl) {
        try {
            RedirectInfo info = new RedirectInfo();
            info.setMethodName(methodName);
            info.setParamters(parameters);
            info.setRedirectUrl(redirectUrl);
            String string = JsonUtil.buildJson(info);
            byte[] cipher = AesUtil.encodeAES(AES_KEY.getBytes(), string.getBytes());
            String cipherStr = StringUtil.byte2hex(cipher);
            return srcUrl + "?cipher=" + cipherStr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String decodeCipher(String cipher) throws Exception {
        return new String(AesUtil.decodeAES(AES_KEY.getBytes(), StringUtil.hex2byte(cipher.getBytes())));
    }
}
