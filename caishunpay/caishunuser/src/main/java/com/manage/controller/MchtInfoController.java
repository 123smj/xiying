///*
// * Decompiled with CFR 0_124.
// *
// * Could not load the following classes:
// *  javax.mail.MessagingException
// *  javax.servlet.http.HttpServletRequest
// *  javax.servlet.http.HttpSession
// *  org.apache.log4j.Logger
// *  org.springframework.beans.factory.annotation.Autowired
// *  org.springframework.stereotype.Controller
// *  org.springframework.web.bind.annotation.RequestMapping
// *  org.springframework.web.bind.annotation.ResponseBody
// *  org.springframework.web.servlet.ModelAndView
// */
//package com.manage.controller;
//
//import com.account.bean.TradeMchtAccount;
//import com.gy.system.SysParamUtil;
//import com.gy.util.CommonFunction;
//import com.gy.util.DateUtil;
//import com.gy.util.EmailUtil;
//import com.gy.util.GenerateNextId;
//import com.gy.util.StringUtil;
//import com.gy.util.UUIDGenerator;
//import com.manage.bean.OprInfo;
//import com.manage.bean.PageModle;
//import com.trade.bean.own.QrcodeMchtInfo;
//import com.trade.bean.own.QrcodeMchtInfoTmp;
//import com.trade.enums.MchtStatusEnum;
//import com.trade.enums.ResponseEnum;
//import com.trade.enums.TradeSourceEnum;
//import com.trade.service.QrcodeMchtInfoService;
//import com.trade.service.QrcodeMchtInfoTmpService;
//import com.trade.service.WxpayService;
//import com.trade.util.BeanUtil;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.PrintStream;
//import java.nio.Buffer;
//import java.nio.ByteBuffer;
//import java.nio.channels.FileChannel;
//import java.security.GeneralSecurityException;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import javax.mail.MessagingException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//
//@Controller
//@RequestMapping(value={"/manage"})
//public class MchtInfoController {
//    private static Logger log = Logger.getLogger(MchtInfoController.class);
//    @Autowired
//    private QrcodeMchtInfoService qrcodeMchtInfoServiceImpl;
//    @Autowired
//    private QrcodeMchtInfoTmpService qrcodeMchtInfoTmpServiceImpl;
//    @Autowired
//    private WxpayService wxpayServiceImpl;
//
//    @RequestMapping(value={"/mchtQuery"}, produces={"application/json; charset=utf-8"})
//    @ResponseBody
//    public PageModle<QrcodeMchtInfo> mchtQuery(HttpServletRequest request) {
//        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
//        PageModle<QrcodeMchtInfo> pageModle = null;
//        try {
//            int pageNum = 1;
//            Map param = this.buildRequestParam(request);
//            if (!StringUtil.isEmpty((String)param.get("pageNum"))) {
//                pageNum = StringUtil.parseInt((String)param.get("pageNum"));
//            }
//            System.out.println(param);
//            pageModle = this.qrcodeMchtInfoServiceImpl.listMchtInfoByPage(param, pageNum, 10, oprInfo);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
//        }
//        return pageModle;
//    }
//
//    @RequestMapping(value={"/mchtRecheckQuery"}, produces={"application/json; charset=utf-8"})
//    @ResponseBody
//    public PageModle<QrcodeMchtInfoTmp> mchtRecheckQuery(HttpServletRequest request) {
//        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
//        PageModle<QrcodeMchtInfoTmp> pageModle = null;
//        try {
//            int pageNum = 1;
//            Map param = this.buildRequestParam(request);
//            if (!StringUtil.isEmpty((String)param.get("pageNum"))) {
//                pageNum = StringUtil.parseInt((String)param.get("pageNum"));
//            }
//            System.out.println(param);
//            pageModle = this.qrcodeMchtInfoTmpServiceImpl.listMchtInfoTmp4CheckByPage(param, pageNum, 10, oprInfo);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
//        }
//        return pageModle;
//    }
//
//    @RequestMapping(value={"/mchtQuerySingle"}, produces={"application/json; charset=utf-8"})
//    @ResponseBody
//    public ModelAndView mchtQuerySingle(HttpServletRequest request, String mchtNo) {
//        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
//        ModelAndView view = new ModelAndView("/manage/mcht_update");
//        try {
//            QrcodeMchtInfo mchtInfo = this.qrcodeMchtInfoServiceImpl.getMchtInfo(mchtNo);
//            request.setAttribute("mchtInfo", (Object)mchtInfo);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
//        }
//        return view;
//    }
//
//    @RequestMapping(value={"/mchtAdd"}, produces={"application/json; charset=utf-8"})
//    @ResponseBody
//    public String mchtAdd(HttpServletRequest request, QrcodeMchtInfo mchtInfo) {
//        block7 : {
//            OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
//            try {
//                String checkResult = this.checkMcht(mchtInfo);
//                if (!"00".equals(checkResult)) {
//                    return checkResult;
//                }
//                String tempMchtNo = GenerateNextId.getNextMchntId(mchtInfo.getCompany_id());
//                mchtInfo.setSecretKey(UUIDGenerator.getUUID());
//                mchtInfo.setReturn_native_qrcode("1");
//                mchtInfo.setCrtTime(DateUtil.getCurrTime());
//                mchtInfo.setMchtNo(tempMchtNo);
//                mchtInfo.setStatus(MchtStatusEnum.NORMAL.getCode());
//                mchtInfo.setSingle_extra_fee(200);
//                if (StringUtil.isNotEmpty(mchtInfo.getTrade_source_list())) {
//                    mchtInfo.setTrade_source_list(mchtInfo.getTrade_source_list().replace(",", ":"));
//                }
//                if (StringUtil.isEmpty(mchtInfo.getJump_flag())) {
//                    mchtInfo.setJump_flag("0");
//                }
//                if (!"1".equals(mchtInfo.getJump_flag()) || !StringUtil.isEmpty(mchtInfo.getJump_group())) break block7;
//                return "\u8df3\u7801\u7ec4\u4e0d\u80fd\u4e3a\u7a7a";
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//                log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
//                return ResponseEnum.FAIL_SYSTEM.getMemo();
//            }
//        }
//        mchtInfo.setIs_t1_liq("1");
//        if (mchtInfo.getDfcard_day_limit() == null) {
//            mchtInfo.setDfcard_day_limit(20000000);
//        }
//        TradeMchtAccount tradeMchtAccount = new TradeMchtAccount();
//        tradeMchtAccount.setBalance(0);
//        tradeMchtAccount.setMchtNo(mchtInfo.getMchtNo());
//        tradeMchtAccount.setCashMax(20000000);
//        tradeMchtAccount.setCashMin(0);
//        tradeMchtAccount.setStatus("00");
//        tradeMchtAccount.setUpdateTime(DateUtil.getCurrTime());
//        this.qrcodeMchtInfoServiceImpl.saveMchtInfo(mchtInfo, tradeMchtAccount);
//        this.sendMchtInfoToManager(mchtInfo);
//        return ResponseEnum.SUCCESS.getCode();
//    }
//
//    /*
//     * Enabled aggressive block sorting
//     * Enabled unnecessary exception pruning
//     * Enabled aggressive exception aggregation
//     */
//    @RequestMapping(value={"/mchtAccept"}, produces={"application/json; charset=utf-8"})
//    @ResponseBody
//    public String mchtAccept(HttpServletRequest request, QrcodeMchtInfo paramMchtInfo) {
//        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
//        try {
//            if (paramMchtInfo == null) {
//                return "\u53c2\u6570\u6709\u8bef";
//            }
//            QrcodeMchtInfoTmp tempMchtInfoTmp = this.qrcodeMchtInfoTmpServiceImpl.getMchtInfo(paramMchtInfo.getMchtNo());
//            if (tempMchtInfoTmp == null) {
//                return "\u5546\u6237\u4e0d\u5b58\u5728,\u5ba1\u6838\u5931\u8d25";
//            }
//            QrcodeMchtInfo realMchtInfo = this.qrcodeMchtInfoServiceImpl.getMchtInfo(paramMchtInfo.getMchtNo());
//            if (MchtStatusEnum.ADD.getCode().equals(tempMchtInfoTmp.getStatus())) {
//                if (realMchtInfo != null) {
//                    return "\u5546\u6237\u53f7\u91cd\u590d";
//                }
//                tempMchtInfoTmp.setStatus(MchtStatusEnum.NORMAL.getCode());
//                tempMchtInfoTmp.setChannel_id(paramMchtInfo.getChannel_id());
//                tempMchtInfoTmp.setChannelMchtNo(paramMchtInfo.getChannelMchtNo());
//                tempMchtInfoTmp.setJump_flag(paramMchtInfo.getJump_flag());
//                tempMchtInfoTmp.setJump_group(paramMchtInfo.getJump_group());
//                QrcodeMchtInfo newMcht = new QrcodeMchtInfo();
//                BeanUtil.copyPropertiesNotNull(tempMchtInfoTmp, newMcht, null, new String[0]);
//                String checkResult = this.checkMcht(newMcht);
//                if (!"00".equals(checkResult)) {
//                    return checkResult;
//                }
//                if (StringUtil.isNotEmpty(paramMchtInfo.getTrade_source_list())) {
//                    newMcht.setTrade_source_list(paramMchtInfo.getTrade_source_list().replace(",", ":"));
//                }
//                if (CommonFunction.isTradeSourceOpen(newMcht.getTrade_source_list(), TradeSourceEnum.BANLANCE_DAIFU)) {
//                    newMcht.setIs_t1_liq("0");
//                } else {
//                    newMcht.setIs_t1_liq("1");
//                }
//                tempMchtInfoTmp.setIs_t1_liq(newMcht.getIs_t1_liq());
//                newMcht.setDfcard_day_limit(20000000);
//                this.qrcodeMchtInfoServiceImpl.saveMchtInfo(newMcht);
//                this.sendMchtInfoToManager(newMcht);
//            } else {
//                if (!MchtStatusEnum.UPDATE.getCode().equals(tempMchtInfoTmp.getStatus())) {
//                    if (!MchtStatusEnum.FREEZE_TO_CHECK.getCode().equals(tempMchtInfoTmp.getStatus())) return "\u5546\u6237\u72b6\u6001\u5f02\u5e38";
//                }
//                if (realMchtInfo == null) {
//                    return "\u5546\u6237\u4e0d\u5b58\u5728,\u5ba1\u6838\u5931\u8d25";
//                }
//                tempMchtInfoTmp.setStatus(MchtStatusEnum.NORMAL.getCode());
//                tempMchtInfoTmp.setChannel_id(paramMchtInfo.getChannel_id());
//                tempMchtInfoTmp.setChannelMchtNo(paramMchtInfo.getChannelMchtNo());
//                tempMchtInfoTmp.setJump_flag(paramMchtInfo.getJump_flag());
//                tempMchtInfoTmp.setJump_group(paramMchtInfo.getJump_group());
//                BeanUtil.copyPropertiesNotNull(tempMchtInfoTmp, realMchtInfo, null, new String[0]);
//                String checkResult = this.checkMcht(realMchtInfo);
//                if (!"00".equals(checkResult)) {
//                    return checkResult;
//                }
//                if (StringUtil.isNotEmpty(paramMchtInfo.getTrade_source_list())) {
//                    realMchtInfo.setTrade_source_list(paramMchtInfo.getTrade_source_list().replace(",", ":"));
//                }
//                if (CommonFunction.isTradeSourceOpen(realMchtInfo.getTrade_source_list(), TradeSourceEnum.BANLANCE_DAIFU)) {
//                    realMchtInfo.setIs_t1_liq("0");
//                } else {
//                    realMchtInfo.setIs_t1_liq("1");
//                }
//                tempMchtInfoTmp.setIs_t1_liq(realMchtInfo.getIs_t1_liq());
//                realMchtInfo.setDfcard_day_limit(20000000);
//                this.qrcodeMchtInfoServiceImpl.updateMchtInfo(realMchtInfo);
//            }
//            this.qrcodeMchtInfoTmpServiceImpl.updateMchtInfo(tempMchtInfoTmp);
//            return ResponseEnum.SUCCESS.getCode();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
//            return ResponseEnum.FAIL_SYSTEM.getMemo();
//        }
//    }
//
//    @RequestMapping(value={"/mchtRefuse"}, produces={"application/json; charset=utf-8"})
//    @ResponseBody
//    public String mchtRefuse(HttpServletRequest request, QrcodeMchtInfoTmp paramMchtInfo) {
//        OprInfo oprInfo;
//        QrcodeMchtInfoTmp tempMchtInfoTmp;
//        block5 : {
//            oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
//            try {
//                tempMchtInfoTmp = this.qrcodeMchtInfoTmpServiceImpl.getMchtInfo(paramMchtInfo.getMchtNo());
//                if (tempMchtInfoTmp != null) break block5;
//                return "\u5546\u6237\u4e0d\u5b58\u5728,\u5ba1\u6838\u5931\u8d25";
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//                log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
//                return ResponseEnum.FAIL_SYSTEM.getMemo();
//            }
//        }
//        tempMchtInfoTmp.setRecheck_time(DateUtil.getCurrTime());
//        tempMchtInfoTmp.setRecheck_opr(oprInfo.getOpr_id());
//        tempMchtInfoTmp.setRefuse_reason(paramMchtInfo.getRefuse_reason());
//        if (MchtStatusEnum.ADD.getCode().equals(tempMchtInfoTmp.getStatus())) {
//            tempMchtInfoTmp.setStatus(MchtStatusEnum.ADD_REFUSE.getCode());
//        } else {
//            tempMchtInfoTmp.setStatus(MchtStatusEnum.UPDATE_REFUSE.getCode());
//        }
//        this.qrcodeMchtInfoTmpServiceImpl.updateMchtInfo(tempMchtInfoTmp);
//        return ResponseEnum.SUCCESS.getCode();
//    }
//
//    private void sendMchtInfoToManager(QrcodeMchtInfo mchtInfo) {
//        if (StringUtil.isEmpty(mchtInfo.getEmail())) {
//            return;
//        }
//        String title = "\u5546\u6237\u5f00\u901a\u6210\u529f\u901a\u77e5";
//        StringBuilder content = new StringBuilder();
//        content.append("\u60a8\u597d: " + mchtInfo.getMchtName() + "\n");
//        content.append("    \u60a8\u7684\u5546\u6237\u5df2\u7ecf\u6ce8\u518c\u6210\u529f.\n    \u5546\u6237\u53f7:" + mchtInfo.getMchtNo() + "\n    \u4ea4\u6613\u5bc6\u94a5:" + mchtInfo.getSecretKey() + "\n    \u672c\u90ae\u4ef6\u4e3a\u52a0\u5bc6\u90ae\u4ef6.\u53d1\u9001\u4e8e\u7ebf\u4e0a\u4ea4\u6613\u7ba1\u7406\u5e73\u53f0http://www.guoyinpay.com");
//        try {
//            EmailUtil.sendEmal("757174266@qq.com", title, content.toString());
//            EmailUtil.sendEmal(mchtInfo.getEmail(), title, content.toString());
//        }
//        catch (MessagingException e) {
//            e.printStackTrace();
//        }
//        catch (GeneralSecurityException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @RequestMapping(value={"/mchtUpdate"}, produces={"application/json; charset=utf-8"})
//    @ResponseBody
//    public String mchtUpdate(HttpServletRequest request, QrcodeMchtInfo mchtInfo) {
//        OprInfo oprInfo;
//        QrcodeMchtInfo mchtInfoOld;
//        block4 : {
//            oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
//            try {
//                String checkResult = this.checkMcht(mchtInfo);
//                if (!"00".equals(checkResult)) {
//                    return checkResult;
//                }
//                mchtInfoOld = this.qrcodeMchtInfoServiceImpl.getMchtInfo(mchtInfo.getMchtNo());
//                if (mchtInfoOld != null) break block4;
//                return "\u5546\u6237\u4e0d\u5b58\u5728\uff0c\u4fee\u6539\u6709\u8bef";
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//                log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
//                return ResponseEnum.FAIL_SYSTEM.getMemo();
//            }
//        }
//        mchtInfo.setTrade_source_list(StringUtil.isNotEmpty(mchtInfo.getTrade_source_list()) ? mchtInfo.getTrade_source_list().replace(",", ":") : "");
//        BeanUtil.copyPropertiesNotNull(mchtInfo, mchtInfoOld, null, "secretKey");
//        mchtInfoOld.setUpdate_time(DateUtil.getCurrTime());
//        mchtInfoOld.setUpdate_opr(oprInfo.getOpr_id());
//        mchtInfoOld.setDfcard_day_limit(20000000);
//        this.qrcodeMchtInfoServiceImpl.updateMchtInfo(mchtInfoOld);
//        return ResponseEnum.SUCCESS.getCode();
//    }
//
//    @RequestMapping(value={"/mchtFreeze"}, produces={"application/json; charset=utf-8"})
//    @ResponseBody
//    public String mchtFreeze(HttpServletRequest request, String mchtNo) {
//        QrcodeMchtInfo mchtInfo;
//        OprInfo oprInfo;
//        block5 : {
//            block4 : {
//                oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
//                try {
//                    mchtInfo = this.qrcodeMchtInfoServiceImpl.getMchtInfo(mchtNo);
//                    if (mchtInfo != null) break block4;
//                    return "\u5546\u6237\u4e0d\u5b58\u5728";
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                    log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
//                    return ResponseEnum.FAIL_SYSTEM.getMemo();
//                }
//            }
//            if (!MchtStatusEnum.FREEZE.getCode().equals(mchtInfo.getStatus())) break block5;
//            return "\u5546\u6237\u5df2\u88ab\u51bb\u7ed3";
//        }
//        mchtInfo.setStatus(MchtStatusEnum.FREEZE.getCode());
//        mchtInfo.setUpdate_time(DateUtil.getCurrTime());
//        mchtInfo.setUpdate_opr(oprInfo.getOpr_id());
//        this.qrcodeMchtInfoServiceImpl.updateMchtInfo(mchtInfo);
//        return ResponseEnum.SUCCESS.getCode();
//    }
//
//    @RequestMapping(value={"/mchtRecover"}, produces={"application/json; charset=utf-8"})
//    @ResponseBody
//    public String mchtRecover(HttpServletRequest request, String mchtNo) {
//        QrcodeMchtInfo mchtInfo;
//        OprInfo oprInfo;
//        block5 : {
//            block4 : {
//                oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
//                try {
//                    mchtInfo = this.qrcodeMchtInfoServiceImpl.getMchtInfo(mchtNo);
//                    if (mchtInfo != null) break block4;
//                    return "\u5546\u6237\u4e0d\u5b58\u5728";
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                    log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
//                    return ResponseEnum.FAIL_SYSTEM.getMemo();
//                }
//            }
//            if (!MchtStatusEnum.NORMAL.getCode().equals(mchtInfo.getStatus())) break block5;
//            return "\u5546\u6237\u5df2\u6062\u590d";
//        }
//        mchtInfo.setStatus(MchtStatusEnum.NORMAL.getCode());
//        mchtInfo.setUpdate_time(DateUtil.getCurrTime());
//        mchtInfo.setUpdate_opr(oprInfo.getOpr_id());
//        this.qrcodeMchtInfoServiceImpl.updateMchtInfo(mchtInfo);
//        return ResponseEnum.SUCCESS.getCode();
//    }
//
//    @RequestMapping(value={"/mchtExport"}, produces={"application/json; charset=utf-8"})
//    @ResponseBody
//    public String mchtExport(HttpServletRequest request) {
//        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
//        List<QrcodeMchtInfo> mchtList = null;
//        String rootPath = String.valueOf(SysParamUtil.getParam("MCHT_EXPORT_PATH")) + DateUtil.getCurrentDay();
//        File pathFile = new File(rootPath);
//        if (!pathFile.exists()) {
//            pathFile.mkdirs();
//        }
//        String fileName = "\u7ebf\u4e0a\u4ea4\u6613\u5e73\u53f0\u5546\u6237\u4fe1\u606f\u8868_" + oprInfo.getOpr_id() + "_" + DateUtil.getCurrTime() + StringUtil.getRandom(3) + ".csv";
//        String result = "";
//        try {
//            Map param = this.buildRequestParam(request);
//            mchtList = this.qrcodeMchtInfoServiceImpl.listAllMcht(param, oprInfo);
//            result = this.generateTradeFile(mchtList, String.valueOf(rootPath) + "/" + fileName);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
//        }
//        return result;
//    }
//
//    private String generateTradeFile(List<QrcodeMchtInfo> tradeList, String filePath) {
//        File tradeFile = new File(filePath);
//        try {
//            FileChannel fileChannel = new FileOutputStream(tradeFile).getChannel();
//            StringBuffer sBuffer = new StringBuffer();
//            sBuffer.append("\u6ce8\u518c\u65f6\u95f4,\u5546\u6237\u7f16\u53f7,\u5546\u6237\u540d\u79f0,\u516c\u53f8\u7f16\u53f7,\u624b\u673a\u53f7,\u90ae\u7bb1,\u8eab\u4efd\u8bc1\u53f7,\u6e20\u9053\u7f16\u53f7,\u6e20\u9053\u5546\u6237,\u7ed3\u7b97\u5361\u53f7,\u7ed3\u7b97\u94f6\u884c,\u7ed3\u7b97\u4eba,\u8054\u884c\u53f7,\u5546\u6237\u5730\u5740,\u5fae\u4fe1\u8d39\u7387(%),\u652f\u4ed8\u5b9d\u8d39\u7387(%),\u5feb\u6377\u652f\u4ed8\u8d39\u7387(%),\u7f51\u94f6\u652f\u4ed8\u8d39\u7387(%),\u5f00\u901a\u4e1a\u52a1\u7c7b\u578b");
//            sBuffer.append("\n");
//            for (QrcodeMchtInfo mchtBean : tradeList) {
//                sBuffer.append("'").append(StringUtil.trans2Str(mchtBean.getCrtTime()));
//                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getMchtNo()));
//                sBuffer.append(",").append(StringUtil.trans2Str(mchtBean.getMchtName()));
//                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getCompany_id()));
//                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getPhone()));
//                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getEmail()));
//                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getIdentity_no()));
//                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getChannel_id()));
//                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getChannelMchtNo()));
//                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getBank_card_no()));
//                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getBank_name()));
//                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getCard_name()));
//                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getBank_no()));
//                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getLisence_addr()));
//                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getWechat_fee_value()));
//                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getAlipay_fee_value()));
//                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getQuickpay_fee_value()));
//                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getNetpay_fee_value()));
//                sBuffer.append(",'").append(CommonFunction.transferTradeSourceList(StringUtil.trans2Str(mchtBean.getTrade_source_list()), ":"));
//                sBuffer.append("\n");
//            }
//            ByteBuffer buffer = ByteBuffer.wrap(sBuffer.toString().getBytes());
//            fileChannel.write(buffer);
//            buffer.clear();
//            fileChannel.close();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        return filePath;
//    }
//
//    private Map buildRequestParam(HttpServletRequest request) {
//        Map properties = request.getParameterMap();
//        HashMap<String, String> returnMap = new HashMap<String, String>();
//        Iterator entries = properties.entrySet().iterator();
//        String name = "";
//        String value = "";
//        while (entries.hasNext()) {
//            Map.Entry entry = entries.next();
//            name = (String)entry.getKey();
//            Object valueObj = entry.getValue();
//            value = null;
//            if (valueObj == null) {
//                value = "";
//            } else if (valueObj instanceof String[]) {
//                String[] values = (String[])valueObj;
//                int i = 0;
//                while (i < values.length) {
//                    value = value == null ? (values[i] == null ? "" : values[i]) : String.valueOf(value) + "," + (values[i] == null ? "" : values[i]);
//                    ++i;
//                }
//            } else {
//                value = valueObj.toString();
//            }
//            returnMap.put(name, value);
//        }
//        return returnMap;
//    }
//
//    private String checkMcht(QrcodeMchtInfo mchtInfo) {
//        if (mchtInfo == null) {
//            return "\u53c2\u6570\u6709\u8bef";
//        }
//        if (StringUtil.isEmpty(mchtInfo.getMchtName())) {
//            return "\u5546\u6237\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a";
//        }
//        if (StringUtil.isEmpty(mchtInfo.getPhone())) {
//            return "\u624b\u673a\u53f7\u7801\u4e0d\u80fd\u4e3a\u7a7a";
//        }
//        if (StringUtil.isEmpty(mchtInfo.getIdentity_no())) {
//            return "\u8eab\u4efd\u8bc1\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
//        }
//        if (StringUtil.isEmpty(mchtInfo.getCompany_id())) {
//            return "\u516c\u53f8\u7f16\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
//        }
//        if (StringUtil.isEmpty(mchtInfo.getChannel_id())) {
//            return "\u6e20\u9053\u7f16\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
//        }
//        if (StringUtil.isEmpty(mchtInfo.getChannelMchtNo())) {
//            return "\u6e20\u9053\u5546\u6237\u4e0d\u80fd\u4e3a\u7a7a";
//        }
//        if (StringUtil.isEmpty(mchtInfo.getBank_card_no())) {
//            return "\u7ed3\u7b97\u5361\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
//        }
//        if (StringUtil.isEmpty(mchtInfo.getBank_name())) {
//            return "\u7ed3\u7b97\u94f6\u884c\u4e0d\u80fd\u4e3a\u7a7a";
//        }
//        if (StringUtil.isEmpty(mchtInfo.getCard_name())) {
//            return "\u7ed3\u7b97\u4eba\u4e0d\u80fd\u4e3a\u7a7a";
//        }
//        if (StringUtil.isEmpty(mchtInfo.getBank_no())) {
//            return "\u8054\u884c\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
//        }
//        if (mchtInfo.getWechat_fee_value() == null) {
//            return "\u5fae\u4fe1\u8d39\u7387\u4e0d\u80fd\u4e3a\u7a7a";
//        }
//        if (mchtInfo.getAlipay_fee_value() == null) {
//            return "\u652f\u4ed8\u5b9d\u8d39\u7387\u4e0d\u80fd\u4e3a\u7a7a";
//        }
//        if (mchtInfo.getQuickpay_fee_value() == null) {
//            return "\u5feb\u6377\u652f\u4ed8\u8d39\u7387\u4e0d\u80fd\u4e3a\u7a7a";
//        }
//        if (mchtInfo.getNetpay_fee_value() == null) {
//            return "\u7f51\u94f6\u652f\u4ed8\u8d39\u7387\u4e0d\u80fd\u4e3a\u7a7a";
//        }
//        if (mchtInfo.getWechat_fee_value() > 20.0 || mchtInfo.getWechat_fee_value() < 0.0) {
//            return "\u5fae\u4fe1\u8d39\u7387\u683c\u5f0f\u6709\u8bef";
//        }
//        if (mchtInfo.getAlipay_fee_value() > 20.0 || mchtInfo.getAlipay_fee_value() < 0.0) {
//            return "\u652f\u4ed8\u5b9d\u8d39\u7387\u683c\u5f0f\u6709\u8bef";
//        }
//        if (mchtInfo.getQuickpay_fee_value() > 20.0 || mchtInfo.getQuickpay_fee_value() < 0.0) {
//            return "\u5feb\u6377\u652f\u4ed8\u8d39\u7387\u683c\u5f0f\u6709\u8bef";
//        }
//        if (mchtInfo.getNetpay_fee_value() > 20.0 || mchtInfo.getNetpay_fee_value() < 0.0) {
//            return "\u7f51\u94f6\u652f\u4ed8\u8d39\u7387\u683c\u5f0f\u6709\u8bef";
//        }
//        if ("1".equals(mchtInfo.getJump_flag()) && StringUtil.isEmpty(mchtInfo.getJump_group())) {
//            return "\u8df3\u7801\u7ec4\u4e0d\u80fd\u4e3a\u7a7a";
//        }
//        return "00";
//    }
//}
