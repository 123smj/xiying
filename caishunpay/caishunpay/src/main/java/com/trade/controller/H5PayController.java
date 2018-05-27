package com.trade.controller;

import com.gy.system.SysParamUtil;
import com.gy.util.StringUtil;
import com.trade.bean.ThirdPartyPayDetail;
import com.trade.bean.own.MerchantInf;
import com.trade.bean.own.PayChannelInf;
import com.trade.bean.response.ThirdPartyPayResponse;
import com.trade.dao.ThirdPartyPayDetailDao;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeStateEnum;
import com.trade.service.MerchantInfService;
import com.trade.service.WappayService;
import com.trade.service.impl.ThirdPartyPayDispatcherService;
import com.trade.util.AesUtil;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Controller
public class H5PayController extends PayController {
    private static String AES_KEY = SysParamUtil.getParam("AES_KEY");
    private static String QRCODE_URL = SysParamUtil.getParam("QRCODE_URL");
    //追踪类信息
    private static Logger log = LoggerFactory.getLogger(H5PayController.class);

    @Autowired
    ThirdPartyPayDispatcherService thirdPartyPayDispatcherService;

    @Autowired
    private MerchantInfService merchantInfServiceImpl;

    @Autowired
    private ThirdPartyPayDetailDao thirdPartyPayDetailDao;

    /**
     * 此方法反会客户端支付后应得到的视图
     * @param request
     * @param cipher 表示用户参数
     * @return errorNoPermission表示用户权限不够无法拉起支付或是此通道尚未开通
     * @return payok表示用户成功拉起微信支付且支付成功
     * @return h5jum表示用户成功拉起安全支付且支付成功
     * @throws Exception
     */
    @RequestMapping("/h5/jump")
    public ModelAndView acceptJump(HttpServletRequest request, @RequestParam String cipher) throws Exception {
        Map<String, Object> objectMap = new HashMap<>();
        try {
            //@userAgent:此变量值为http协议中的消息头,全小写形势
            String userAgent = request.getHeader("User-Agent").toLowerCase();
            //@此变量是一个boolean值 他判断是是userAgent是否包含如下两个值
            Boolean isInside = userAgent.contains("micromessenger") ||
                    userAgent.contains("alipay");
            String outTransactionNo = decodeCipher(cipher);
            ThirdPartyPayDetail payDetail = thirdPartyPayDetailDao.getById(outTransactionNo);
            String channelId = payDetail.getChannel_id();
            String channelMerchantNo = payDetail.getMch_id();
            WappayService wappayService = thirdPartyPayDispatcherService.switchWapService(channelId);
            PayChannelInf channelInf = this.merchantInfServiceImpl.getChannelInf(channelId, channelMerchantNo);
            MerchantInf qrcodeMcht = this.merchantInfServiceImpl.getMchtInfo(payDetail.getMerchantId());
            if (payDetail.getTrade_state().equals(TradeStateEnum.SUCCESS.getCode())) {
                return new ModelAndView("/page/payok");
            }
            ThirdPartyPayResponse thirdPartyPayResponse = wappayService.doWapTrade(request, payDetail, qrcodeMcht, channelInf);
            if (thirdPartyPayResponse.getResultCode().equals(ResponseEnum.SUCCESS.getCode())) {
                objectMap.put("url", thirdPartyPayResponse.getPay_url());
                objectMap.put("isInside", isInside);
            } else {
                return new ModelAndView("/page/errorNoPermission");
            }
            return new ModelAndView("/trade/h5jump", objectMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelAndView("/page/errorNoPermission");
        }
    }
    /**
     * 功能未知
     * @param cipher
     * @param response
     * @throws Exception
     */
    @RequestMapping("/h5/qrcode")
    public void qrCode(@RequestParam String cipher, HttpServletResponse response) throws Exception {
        String qrCodeUrl = decodeCipher(cipher);
        ByteArrayOutputStream bufferedImage = QRCode.from(qrCodeUrl).to(ImageType.JPG).withSize(500, 500).stream();
        response.setContentType("image/jpg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Expires", "0");
        response.setDateHeader("Expires", 0L);
        BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(bufferedImage.toByteArray());
        outputStream.close();
    }
    /**
     * 此方法暂定为解码
     * @param cipher 需要解码的参数
     * @return 将传入的参数解码翻译返回
     * @throws Exception 无法解码
     */
    private static String decodeCipher(String cipher) throws Exception {
        return new String(AesUtil.decodeAES(AES_KEY.getBytes(), StringUtil.hex2byte(cipher.getBytes())));
    }
    /**
     * 功能未知
     * @param view
     * @param string
     * @return
     */
    public static String makeJump(String view, String string){
        try {
            byte[] cipher = AesUtil.encodeAES(AES_KEY.getBytes(), string.getBytes());
            String cipherStr = StringUtil.byte2hex(cipher);
            return view + "?cipher=" + cipherStr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String makeJumpView(String jumpUrl, String transactionNo) {
        return makeJump(jumpUrl, transactionNo);
    }

    public static String makeQrCodeUrl(String url){
        return makeJump(QRCODE_URL, url);

    }
}
