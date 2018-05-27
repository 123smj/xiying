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
 *  org.springframework.web.multipart.MultipartFile
 *  org.springframework.web.multipart.MultipartHttpServletRequest
 *  org.springframework.web.servlet.ModelAndView
 */
package com.manage.controller;

import com.common.model.Response;
import com.gy.system.SysParamUtil;
import com.gy.util.DateUtil;
import com.gy.util.FileUtil;
import com.gy.util.StringUtil;
import com.gy.util.UUIDGenerator;
import com.manage.bean.ChannelRegisterBean;
import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import com.manage.service.ChannelRegisterService;
import com.trade.bean.own.QrcodeChannelInf;
import com.trade.bean.own.QrcodeMchtInfo;
import com.trade.enums.ChannelStatusEnum;
import com.trade.enums.MchtStatusEnum;
import com.trade.enums.ResponseEnum;
import com.trade.service.QrcodeMchtInfoService;
import com.trade.service.WxpayService;
import com.trade.util.BeanUtil;

import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = {"/manage"})
public class ChannelInfController {
    private static Logger log = Logger.getLogger(ChannelInfController.class);
    @Autowired
    private QrcodeMchtInfoService qrcodeMchtInfoServiceImpl;
    @Autowired
    private ChannelRegisterService hfbankServiceImpl;
    @Autowired
    private WxpayService wxpayServiceImpl;

    @RequestMapping(value = {"/channelQuery"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public PageModle<QrcodeChannelInf> channelQuery(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
        PageModle<QrcodeChannelInf> pageModle = null;
        try {
            int pageNum = 1;
            Map param = this.buildRequestParam(request);
            if (!StringUtil.isEmpty((String) param.get("pageNum"))) {
                pageNum = StringUtil.parseInt((String) param.get("pageNum"));
            }
            System.out.println(param);
            pageModle = this.qrcodeMchtInfoServiceImpl.listChannelMchtInfoByPage(param, pageNum, 10);
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) ("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return pageModle;
    }

    @RequestMapping(value = {"/channelQuerySingle"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public ModelAndView channelQuerySingle(HttpServletRequest request, String mchtNo) {
        OprInfo oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
        ModelAndView view = new ModelAndView("/manage/mcht_update");
        try {
            QrcodeMchtInfo qrcodeChannelInf = this.qrcodeMchtInfoServiceImpl.getMchtInfo(mchtNo);
            request.setAttribute("qrcodeChannelInf", (Object) qrcodeChannelInf);
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) ("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return view;
    }

    @RequestMapping(value = {"/channelMchtAdd"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public String mchtAdd(HttpServletRequest request, QrcodeChannelInf qrcodeChannelInf) {
        OprInfo oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
        try {
            String checkResult = this.checkChannel(qrcodeChannelInf);
            if (!"00".equals(checkResult)) {
                return checkResult;
            }
            qrcodeChannelInf.setT0Flag("0");
            qrcodeChannelInf.setStatus(MchtStatusEnum.NORMAL.getCode());
            qrcodeChannelInf.setUpdate_time(DateUtil.getCurrTime());
            qrcodeChannelInf.setCrt_time(DateUtil.getCurrTime());
            qrcodeChannelInf.setUpdate_opr(oprInfo.getOpr_id());
            this.qrcodeMchtInfoServiceImpl.saveChannelInf(qrcodeChannelInf);
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) ("exception-----opr_id:" + oprInfo.getOpr_id() + e));
            return ResponseEnum.FAIL_SYSTEM.getMemo();
        }
        return ResponseEnum.SUCCESS.getCode();
    }

    @RequestMapping(value = {"/channelMchtUpdate"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public String channelMchtUpdate(HttpServletRequest request, QrcodeChannelInf qrcodeChannelInf) {
        QrcodeChannelInf channelInfoOld;
        OprInfo oprInfo;
        block4:
        {
            oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
            try {
                String checkResult = this.checkChannel(qrcodeChannelInf);
                if (!"00".equals(checkResult)) {
                    return checkResult;
                }
                channelInfoOld = this.qrcodeMchtInfoServiceImpl.getChannelInf(qrcodeChannelInf.getChannel_id(), qrcodeChannelInf.getChannel_mcht_no());
                if (channelInfoOld != null) break block4;
                return "\u901a\u9053\u5546\u6237\u4e0d\u5b58\u5728\uff0c\u4fee\u6539\u6709\u8bef";
            } catch (Exception e) {
                e.printStackTrace();
                log.error((Object) ("exception-----opr_id:" + oprInfo.getOpr_id() + e));
                return ResponseEnum.FAIL_SYSTEM.getMemo();
            }
        }
        BeanUtil.copyPropertiesNotNull(qrcodeChannelInf, channelInfoOld, null, "secretKey");
        channelInfoOld.setUpdate_time(DateUtil.getCurrTime());
        channelInfoOld.setUpdate_opr(oprInfo.getOpr_id());
        this.qrcodeMchtInfoServiceImpl.updateChannelInf(channelInfoOld);
        return ResponseEnum.SUCCESS.getCode();
    }

    @RequestMapping(value = {"/channelMchtFreeze"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public String channelMchtFreeze(HttpServletRequest request, String channel_mcht_no, String channel_id) {
        QrcodeChannelInf qrcodeChannelInf;
        OprInfo oprInfo;
        block5:
        {
            block4:
            {
                oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
                try {
                    qrcodeChannelInf = this.qrcodeMchtInfoServiceImpl.getChannelInf(channel_id, channel_mcht_no);
                    if (qrcodeChannelInf != null) break block4;
                    return "\u5546\u6237\u4e0d\u5b58\u5728";
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error((Object) ("exception-----opr_id:" + oprInfo.getOpr_id() + e));
                    return ResponseEnum.FAIL_SYSTEM.getMemo();
                }
            }
            if (!MchtStatusEnum.FREEZE.getCode().equals(qrcodeChannelInf.getStatus())) break block5;
            return "\u5546\u6237\u5df2\u88ab\u51bb\u7ed3";
        }
        qrcodeChannelInf.setStatus(MchtStatusEnum.FREEZE.getCode());
        qrcodeChannelInf.setUpdate_time(DateUtil.getCurrTime());
        qrcodeChannelInf.setUpdate_opr(oprInfo.getOpr_id());
        this.qrcodeMchtInfoServiceImpl.updateChannelInf(qrcodeChannelInf);
        return ResponseEnum.SUCCESS.getCode();
    }

    @RequestMapping(value = {"/channelMchtRecover"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public String channelMchtRecover(HttpServletRequest request, String channel_mcht_no, String channel_id) {
        QrcodeChannelInf qrcodeChannelInf;
        OprInfo oprInfo;
        block5:
        {
            block4:
            {
                oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
                try {
                    qrcodeChannelInf = this.qrcodeMchtInfoServiceImpl.getChannelInf(channel_id, channel_mcht_no);
                    if (qrcodeChannelInf != null) break block4;
                    return "\u5546\u6237\u4e0d\u5b58\u5728";
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error((Object) ("exception-----opr_id:" + oprInfo.getOpr_id() + e));
                    return ResponseEnum.FAIL_SYSTEM.getMemo();
                }
            }
            if (!MchtStatusEnum.NORMAL.getCode().equals(qrcodeChannelInf.getStatus())) break block5;
            return "\u5546\u6237\u5df2\u6062\u590d";
        }
        qrcodeChannelInf.setStatus(MchtStatusEnum.NORMAL.getCode());
        qrcodeChannelInf.setUpdate_time(DateUtil.getCurrTime());
        qrcodeChannelInf.setUpdate_opr(oprInfo.getOpr_id());
        this.qrcodeMchtInfoServiceImpl.updateChannelInf(qrcodeChannelInf);
        return ResponseEnum.SUCCESS.getCode();
    }

    @RequestMapping(value = {"/channelMchtRegister"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public String mchtRegister(HttpServletRequest request, QrcodeChannelInf qrcodeChannelInf) {
        String result;
        OprInfo oprInfo;
        block9:
        {
            block8:
            {
                block7:
                {
                    block6:
                    {
                        oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
                        result = ResponseEnum.FAIL_SYSTEM.getMemo();
                        try {
                            if (!StringUtil.isEmpty(qrcodeChannelInf.getAgtId())) break block6;
                            return "\u673a\u6784\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error((Object) ("exception-----opr_id:" + oprInfo.getOpr_id() + e));
                            return ResponseEnum.FAIL_SYSTEM.getMemo();
                        }
                    }
                    if (!StringUtil.isEmpty(qrcodeChannelInf.getChannel_mcht_no())) break block7;
                    return "\u901a\u9053\u5546\u6237\u7f16\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
                }
                if (qrcodeChannelInf.getChannel_mcht_no().length() == 11) break block8;
                return "\u901a\u9053\u5546\u6237\u7f16\u53f7\u957f\u5ea6\u5fc5\u987b\u4e3a11";
            }
            if (!StringUtil.isEmpty(qrcodeChannelInf.getPass_word())) break block9;
            return "\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a";
        }
        qrcodeChannelInf.setT0Flag("0");
        qrcodeChannelInf.setStatus(ChannelStatusEnum.ADD.getCode());
        qrcodeChannelInf.setUpdate_time(DateUtil.getCurrTime());
        qrcodeChannelInf.setCrt_time(DateUtil.getCurrTime());
        qrcodeChannelInf.setUpdate_opr(oprInfo.getOpr_id());
        result = this.hfbankServiceImpl.registerChannelMcht(qrcodeChannelInf);
        return result;
    }

    @RequestMapping(value = {"/channelMchtVerify"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public ModelAndView channelMchtVerify(HttpServletRequest request, ChannelRegisterBean channelRegisterBean) {
        OprInfo oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
        String result = ResponseEnum.SUCCESS.getMemo();
        ModelAndView view = new ModelAndView("/channel/message");
        try {
            if (StringUtil.isEmpty(channelRegisterBean.getAccount())) {
                request.setAttribute("result", (Object) "\u901a\u9053\u5546\u6237\u53f7\u4e0d\u80fd\u4e3a\u7a7a");
                return view;
            }
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile cert_correct_file = multipartRequest.getFile("cert_correct");
            MultipartFile cert_opposite_file = multipartRequest.getFile("cert_opposite");
            MultipartFile cert_meet_file = multipartRequest.getFile("cert_meet");
            MultipartFile card_correct_file = multipartRequest.getFile("card_correct");
            MultipartFile card_opposite_file = multipartRequest.getFile("card_opposite");
            MultipartFile bl_img_file = multipartRequest.getFile("bl_img");
            MultipartFile door_img_file = multipartRequest.getFile("door_img");
            MultipartFile cashier_img_file = multipartRequest.getFile("cashier_img");
            String checkResult = this.checkChannelVerify(channelRegisterBean);
            if (!"00".equals(checkResult)) {
                request.setAttribute("result", (Object) checkResult);
                return view;
            }
            if (cert_correct_file == null || cert_correct_file.isEmpty()) {
                request.setAttribute("result", (Object) "\u8eab\u4efd\u8bc1\u6b63\u9762\u4e0d\u80fd\u4e3a\u7a7a");
                return view;
            }
            if (cert_opposite_file == null || cert_opposite_file.isEmpty()) {
                request.setAttribute("result", (Object) "\u8eab\u4efd\u8bc1\u80cc\u9762\u4e0d\u80fd\u4e3a\u7a7a");
                return view;
            }
            if (cert_meet_file == null || cert_meet_file.isEmpty()) {
                request.setAttribute("result", (Object) "\u8eab\u4efd\u8bc1\u80cc\u9762\u4e0d\u80fd\u4e3a\u7a7a");
                return view;
            }
            if (card_correct_file == null || card_correct_file.isEmpty()) {
                request.setAttribute("result", (Object) "\u94f6\u884c\u5361\u6b63\u9762\u4e0d\u80fd\u4e3a\u7a7a");
                return view;
            }
            if (card_opposite_file == null || card_opposite_file.isEmpty()) {
                request.setAttribute("result", (Object) "\u94f6\u884c\u5361\u80cc\u9762\u4e0d\u80fd\u4e3a\u7a7a");
                return view;
            }
            if (bl_img_file == null || bl_img_file.isEmpty()) {
                request.setAttribute("result", (Object) "\u8425\u4e1a\u6267\u7167\u4e0d\u80fd\u4e3a\u7a7a");
                return view;
            }
            if (door_img_file == null || door_img_file.isEmpty()) {
                request.setAttribute("result", (Object) "\u95e8\u5934\u7167\u4e0d\u80fd\u4e3a\u7a7a");
                return view;
            }
            if (cashier_img_file == null || cashier_img_file.isEmpty()) {
                request.setAttribute("result", (Object) "\u6536\u94f6\u53f0\u7167\u4e0d\u80fd\u4e3a\u7a7a");
                return view;
            }
            if (cert_correct_file.getSize() / 1024L > 150L || cert_opposite_file.getSize() / 1024L > 150L || cert_meet_file.getSize() / 1024L > 150L || card_correct_file.getSize() / 1024L > 150L || card_opposite_file.getSize() / 1024L > 150L || bl_img_file.getSize() / 1024L > 150L || door_img_file.getSize() / 1024L > 150L || cashier_img_file.getSize() / 1024L > 150L) {
                request.setAttribute("result", (Object) "\u56fe\u7247\u5927\u5c0f\u4e0d\u80fd\u8d85\u8fc7150kb");
                return view;
            }
            String rootPath = String.valueOf(SysParamUtil.getParam("CHANNEL_UPLOAD_PATH")) + channelRegisterBean.getAccount();
            channelRegisterBean.setCert_correct_path(FileUtil.multiUpload(cert_correct_file, rootPath));
            channelRegisterBean.setCert_opposite_path(FileUtil.multiUpload(cert_opposite_file, rootPath));
            channelRegisterBean.setCert_meet_path(FileUtil.multiUpload(cert_meet_file, rootPath));
            channelRegisterBean.setCard_correct_path(FileUtil.multiUpload(card_correct_file, rootPath));
            channelRegisterBean.setCard_opposite_path(FileUtil.multiUpload(card_opposite_file, rootPath));
            channelRegisterBean.setBl_img_path(FileUtil.multiUpload(bl_img_file, rootPath));
            channelRegisterBean.setDoor_img_path(FileUtil.multiUpload(door_img_file, rootPath));
            channelRegisterBean.setCashier_img_path(FileUtil.multiUpload(cashier_img_file, rootPath));
            System.out.println(channelRegisterBean);
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) ("exception-----opr_id:" + oprInfo.getOpr_id() + e));
            request.setAttribute("result", (Object) ResponseEnum.FAIL_SYSTEM.getMemo());
            return view;
        }
        request.setAttribute("result", (Object) result);
        return view;
    }

    @RequestMapping(value = {"/channelFileUpload"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public Response<String> channelFileUpload(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
        Response<String> response = new Response<String>();
        String tempMchtNo = request.getParameter("tempMchtNo");
        if (StringUtil.isEmpty(tempMchtNo)) {
            tempMchtNo = UUIDGenerator.create15();
        }
        System.out.println(tempMchtNo);
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> files = multipartRequest.getFiles("mchtFile");
            if (files == null) {
                response.setCode("00001");
                response.setMessage("\u4e0a\u4f20\u6587\u4ef6\u4e0d\u80fd\u4e3a\u7a7a");
                return response;
            }
            String rootPath = SysParamUtil.getParam("CHANNEL_UPLOAD_PATH");
            String crtTime = DateUtil.getCurrTime();
            File mchtPath = new File(String.valueOf(rootPath) + tempMchtNo);
            if (!mchtPath.exists()) {
                mchtPath.mkdirs();
            }
            for (MultipartFile file : files) {
                file.transferTo(new File(String.valueOf(rootPath) + tempMchtNo + "/" + file.getOriginalFilename()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) ("exception-----opr_id:" + oprInfo.getOpr_id() + e));
            response.setCode(ResponseEnum.FAIL_SYSTEM.getCode());
            response.setMessage(ResponseEnum.FAIL_SYSTEM.getMemo());
            return response;
        }
        response.setCode(ResponseEnum.SUCCESS.getCode());
        response.setMessage(ResponseEnum.SUCCESS.getMemo());
        return response;
    }

    private Map buildRequestParam(HttpServletRequest request) {
        Map properties = request.getParameterMap();
        HashMap<String, String> returnMap = new HashMap<String, String>();
        Iterator entries = properties.entrySet().iterator();
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            value = null;
            if (valueObj == null) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                int i = 0;
                while (i < values.length) {
                    value = value == null ? (values[i] == null ? "" : values[i]) : String.valueOf(value) + "," + (values[i] == null ? "" : values[i]);
                    ++i;
                }
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }

    private String checkChannelVerify(ChannelRegisterBean channelRegisterBean) {
        if (channelRegisterBean == null) {
            return "\u53c2\u6570\u6709\u8bef";
        }
        if (StringUtil.isEmpty(channelRegisterBean.getUserid())) {
            return "\u901a\u9053\u673a\u6784\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(channelRegisterBean.getAccount())) {
            return "\u901a\u9053\u5546\u6237\u7f16\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(channelRegisterBean.getReal_name())) {
            return "\u5f00\u6237\u4eba\u540d\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(channelRegisterBean.getCmer())) {
            return "\u5546\u6237\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(channelRegisterBean.getCmer_sort())) {
            return "\u5546\u6237\u7b80\u79f0\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(channelRegisterBean.getLocation())) {
            return "\u5f00\u6237\u57ce\u5e02\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(channelRegisterBean.getCard_no())) {
            return "\u5361\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(channelRegisterBean.getCert_no())) {
            return "\u8eab\u4efd\u8bc1\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(channelRegisterBean.getMobile()) || StringUtil.isEmpty(channelRegisterBean.getPhone())) {
            return "\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(channelRegisterBean.getRegionCode())) {
            return "\u533a\u7f16\u7801\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(channelRegisterBean.getAddress())) {
            return "\u8be6\u7ec6\u5730\u5740\u4e0d\u80fd\u4e3a\u7a7a";
        }
        return "00";
    }

    private String checkChannel(QrcodeChannelInf qrcodeChannelInf) {
        if (qrcodeChannelInf == null) {
            return "\u53c2\u6570\u6709\u8bef";
        }
        if (StringUtil.isEmpty(qrcodeChannelInf.getChannel_id())) {
            return "\u901a\u9053\u7f16\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(qrcodeChannelInf.getChannel_name())) {
            return "\u901a\u9053\u5546\u6237\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(qrcodeChannelInf.getChannel_mcht_no())) {
            return "\u901a\u9053\u5546\u6237\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(qrcodeChannelInf.getSecret_key())) {
            return "\u5bc6\u94a5\u4e0d\u80fd\u4e3a\u7a7a";
        }
        return "00";
    }
}
