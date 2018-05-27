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

import com.account.bean.TradeMchtAccount;
import com.common.dao.ICommQueryDAO;
import com.common.model.Response;
import com.gy.system.SysParamUtil;
import com.gy.util.ContextUtil;
import com.gy.util.DateUtil;
import com.gy.util.GenerateNextId;
import com.gy.util.StringUtil;
import com.gy.util.UUIDGenerator;
import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import com.manage.bean.TradeMchtFile;
import com.manage.service.TradeMchtFileService;
import com.trade.bean.own.QrcodeMchtInfoTmp;
import com.trade.enums.MchtStatusEnum;
import com.trade.enums.ResponseEnum;
import com.trade.service.QrcodeMchtInfoTmpService;
import com.trade.util.BeanUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
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
public class MchtInfoTmpController {
    private static Logger log = Logger.getLogger(MchtInfoTmpController.class);
    @Autowired
    private QrcodeMchtInfoTmpService qrcodeMchtInfoTmpServiceImpl;
    @Autowired
    private TradeMchtFileService tradeMchtFileServiceImpl;
    @Autowired
    private ICommQueryDAO commQueryDAO;

    @RequestMapping(value = {"/mchtTmpQuery2Check"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public ModelAndView mchtTmpQuery2Check(HttpServletRequest request, String mchtNo) {
        OprInfo oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
        ModelAndView view = new ModelAndView("/manage/mcht_recheck_detail");
        try {
            QrcodeMchtInfoTmp mchtInfo = this.qrcodeMchtInfoTmpServiceImpl.getMchtInfo(mchtNo);
            List<TradeMchtFile> mchtFiles = this.tradeMchtFileServiceImpl.getMchtFiles(mchtNo);
            request.setAttribute("mchtInfo", (Object) mchtInfo);
            request.setAttribute("mchtFiles", mchtFiles);
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) ("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return view;
    }

    @RequestMapping(value = {"/mchtTmpQuery2Update"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public ModelAndView mchtTmpQuery2Update(HttpServletRequest request, String mchtNo) {
        OprInfo oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
        ModelAndView view = new ModelAndView("/manage/mcht_tmp_update");
        try {
            QrcodeMchtInfoTmp mchtInfo = this.qrcodeMchtInfoTmpServiceImpl.getMchtInfo(mchtNo);
            List<TradeMchtFile> mchtFiles = this.tradeMchtFileServiceImpl.getMchtFiles(mchtNo);
            request.setAttribute("mchtInfo", (Object) mchtInfo);
            request.setAttribute("mchtFiles", mchtFiles);
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) ("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return view;
    }

    @RequestMapping(value = {"/mchtTmpQuery"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public PageModle<QrcodeMchtInfoTmp> mchtTmpQuery(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
        PageModle<QrcodeMchtInfoTmp> pageModle = null;
        try {
            int pageNum = 1;
            Map param = this.buildRequestParam(request);
            if (!StringUtil.isEmpty((String) param.get("pageNum"))) {
                pageNum = StringUtil.parseInt((String) param.get("pageNum"));
            }
            System.out.println(param);
            pageModle = this.qrcodeMchtInfoTmpServiceImpl.listMchtInfoTmpByPage(param, pageNum, 10, oprInfo);
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) ("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return pageModle;
    }

    @RequestMapping(value = {"/mchtTmpAdd"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public String mchtTmpAdd(HttpServletRequest request, QrcodeMchtInfoTmp mchtInfo) {
        OprInfo oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
        try {
            String checkResult = this.checkMcht(mchtInfo);
            if (!"00".equals(checkResult)) {
                return checkResult;
            }
            String mchtNo = GenerateNextId.getNextMchntId(mchtInfo.getCompany_id());
            mchtInfo.setSecretKey(UUIDGenerator.getUUID());
            mchtInfo.setReturn_native_qrcode("1");
            mchtInfo.setCrtTime(DateUtil.getCurrTime());
            mchtInfo.setMchtNo(mchtNo);
            mchtInfo.setStatus(MchtStatusEnum.ADD.getCode());
            mchtInfo.setSingle_extra_fee(200);
            mchtInfo.setDfcard_day_limit(20000000);
            if (StringUtil.isEmpty(mchtInfo.getJump_flag())) {
                mchtInfo.setJump_flag("0");
            }
            mchtInfo.setIs_t1_liq("1");
            mchtInfo.setDfcard_day_limit(0);
            TradeMchtAccount tradeMchtAccount = new TradeMchtAccount();
            tradeMchtAccount.setBalance(0);
            tradeMchtAccount.setMchtNo(mchtInfo.getMchtNo());
            tradeMchtAccount.setCashMax(20000000);
            tradeMchtAccount.setCashMin(0);
            tradeMchtAccount.setStatus("00");
            tradeMchtAccount.setUpdateTime(DateUtil.getCurrTime());
            this.qrcodeMchtInfoTmpServiceImpl.saveMchtInfo(mchtInfo, tradeMchtAccount);
            String tempMchtNo = request.getParameter("tempMchtNo");
            File mchtFile = new File(String.valueOf(SysParamUtil.getParam("MCHT_UPLOAD_PATH")) + tempMchtNo);
            if (mchtFile.exists()) {
                mchtFile.renameTo(new File(String.valueOf(SysParamUtil.getParam("MCHT_UPLOAD_PATH")) + mchtNo));
            }
            String sql = "update TRADE_MCHT_FILE set mcht_no='" + mchtNo + "',mcht_file_path=REPLACE(mcht_file_path,'" + tempMchtNo + "/', '" + mchtNo + "/') where mcht_no='" + tempMchtNo + "'";
            this.commQueryDAO.excute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) ("exception-----opr_id:" + oprInfo.getOpr_id() + e));
            return ResponseEnum.FAIL_SYSTEM.getMemo();
        }
        return ResponseEnum.SUCCESS.getCode();
    }

    @RequestMapping(value = {"/mchtFileUpload"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public Response<String> mchtFileUpload(HttpServletRequest request) {
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
            ArrayList<TradeMchtFile> mchtFileList = new ArrayList<TradeMchtFile>();
            String rootPath = SysParamUtil.getParam("MCHT_UPLOAD_PATH");
            String crtTime = DateUtil.getCurrTime();
            File mchtPath = new File(String.valueOf(rootPath) + tempMchtNo);
            if (!mchtPath.exists()) {
                mchtPath.mkdirs();
            }
            for (MultipartFile file : files) {
                TradeMchtFile mchtFile = new TradeMchtFile();
                mchtFile.setMchtNo(tempMchtNo);
                mchtFile.setMchtFileName(file.getOriginalFilename());
                mchtFile.setMchtFilePath(String.valueOf(tempMchtNo) + "/" + file.getOriginalFilename());
                mchtFile.setMchtFileType("00");
                mchtFile.setCrtOprId(oprInfo.getOpr_id());
                mchtFile.setRecCrtTs(crtTime);
                mchtFileList.add(mchtFile);
                file.transferTo(new File(String.valueOf(rootPath) + mchtFile.getMchtFilePath()));
            }
            this.tradeMchtFileServiceImpl.save(mchtFileList);
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

    @RequestMapping(value = {"/mchtTmpUpdate"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public String mchtTmpUpdate(HttpServletRequest request, QrcodeMchtInfoTmp mchtInfo) {
        QrcodeMchtInfoTmp mchtInfoOld;
        OprInfo oprInfo;
        block6:
        {
            oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
            try {
                String checkResult = this.checkMcht(mchtInfo);
                if (!"00".equals(checkResult)) {
                    return checkResult;
                }
                mchtInfoOld = this.qrcodeMchtInfoTmpServiceImpl.getMchtInfo(mchtInfo.getMchtNo());
                if (mchtInfoOld != null) break block6;
                return "\u5546\u6237\u4e0d\u5b58\u5728\uff0c\u4fee\u6539\u6709\u8bef";
            } catch (Exception e) {
                e.printStackTrace();
                log.error((Object) ("exception-----opr_id:" + oprInfo.getOpr_id() + e));
                return ResponseEnum.FAIL_SYSTEM.getMemo();
            }
        }
        mchtInfo.setTrade_source_list(StringUtil.isNotEmpty(mchtInfo.getTrade_source_list()) ? mchtInfo.getTrade_source_list().replace(",", ":") : "");
        BeanUtil.copyPropertiesNotNull(mchtInfo, mchtInfoOld, null, "secretKey");
        mchtInfoOld.setUpdate_time(DateUtil.getCurrTime());
        mchtInfoOld.setUpdate_opr(oprInfo.getOpr_id());
        if (MchtStatusEnum.ADD.getCode().equals(mchtInfoOld.getStatus()) || MchtStatusEnum.ADD_REFUSE.getCode().equals(mchtInfoOld.getStatus())) {
            mchtInfoOld.setStatus(MchtStatusEnum.ADD.getCode());
        } else {
            mchtInfoOld.setStatus(MchtStatusEnum.UPDATE.getCode());
        }
        this.qrcodeMchtInfoTmpServiceImpl.updateMchtInfo(mchtInfoOld);
        return ResponseEnum.SUCCESS.getCode();
    }

    @RequestMapping(value = {"/mchtTmpExport"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public String mchtTmpExport(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
        List<QrcodeMchtInfoTmp> mchtList = null;
        String rootPath = String.valueOf(SysParamUtil.getParam("MCHT_EXPORT_PATH")) + DateUtil.getCurrentDay();
        File pathFile = new File(rootPath);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        String fileName = "\u7ebf\u4e0a\u4ea4\u6613\u5e73\u53f0\u5546\u6237\u4fe1\u606f\u8868_" + oprInfo.getOpr_id() + "_" + DateUtil.getCurrTime() + StringUtil.getRandom(3) + ".csv";
        String result = "";
        try {
            Map param = this.buildRequestParam(request);
            mchtList = this.qrcodeMchtInfoTmpServiceImpl.listAllMcht(param, oprInfo);
            result = this.generateTradeFile(mchtList, String.valueOf(rootPath) + "/" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) ("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return result;
    }

    private String generateTradeFile(List<QrcodeMchtInfoTmp> tradeList, String filePath) {
        File tradeFile = new File(filePath);
        try {
            FileChannel fileChannel = new FileOutputStream(tradeFile).getChannel();
            StringBuffer sBuffer = new StringBuffer();
            sBuffer.append("\u6ce8\u518c\u65f6\u95f4,\u5546\u6237\u7f16\u53f7,\u5546\u6237\u540d\u79f0,\u516c\u53f8\u7f16\u53f7,\u624b\u673a\u53f7,\u90ae\u7bb1,\u8eab\u4efd\u8bc1\u53f7,\u7ed3\u7b97\u5361\u53f7,\u7ed3\u7b97\u94f6\u884c,\u7ed3\u7b97\u4eba,\u8054\u884c\u53f7,\u5546\u6237\u5730\u5740,\u5fae\u4fe1\u8d39\u7387(%),\u652f\u4ed8\u5b9d\u8d39\u7387(%),QQ\u94b1\u5305\u8d39\u7387(%),\u5feb\u6377\u652f\u4ed8\u8d39\u7387(%),\u7f51\u94f6\u652f\u4ed8\u8d39\u7387(%),\u4ee3\u4ed8\u8d39\u7528/\u5206");
            sBuffer.append("\n");
            for (QrcodeMchtInfoTmp mchtBean : tradeList) {
                sBuffer.append("'").append(StringUtil.trans2Str(mchtBean.getCrtTime()));
                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getMchtNo()));
                sBuffer.append(",").append(StringUtil.trans2Str(mchtBean.getMchtName()));
                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getCompany_id()));
                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getPhone()));
                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getEmail()));
                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getIdentity_no()));
                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getBank_card_no()));
                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getBank_name()));
                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getCard_name()));
                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getBank_no()));
                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getLisence_addr()));
                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getWechat_fee_value()));
                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getAlipay_fee_value()));
                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getQq_fee_value()));
                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getQuickpay_fee_value()));
                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getNetpay_fee_value()));
                sBuffer.append(",'").append(StringUtil.trans2Str(mchtBean.getSingle_extra_fee()));
                sBuffer.append("\n");
            }
            ByteBuffer buffer = ByteBuffer.wrap(sBuffer.toString().getBytes());
            fileChannel.write(buffer);
            buffer.clear();
            fileChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
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

    private String checkMcht(QrcodeMchtInfoTmp mchtInfo) {
        if (mchtInfo == null) {
            return "\u53c2\u6570\u6709\u8bef";
        }
        if (StringUtil.isEmpty(mchtInfo.getMchtName())) {
            return "\u5546\u6237\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(mchtInfo.getPhone())) {
            return "\u624b\u673a\u53f7\u7801\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(mchtInfo.getIdentity_no())) {
            return "\u8eab\u4efd\u8bc1\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(mchtInfo.getCompany_id())) {
            return "\u516c\u53f8\u7f16\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(mchtInfo.getBank_card_no())) {
            return "\u7ed3\u7b97\u5361\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(mchtInfo.getBank_name())) {
            return "\u7ed3\u7b97\u94f6\u884c\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(mchtInfo.getCard_name())) {
            return "\u7ed3\u7b97\u4eba\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(mchtInfo.getBank_no())) {
            return "\u8054\u884c\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (mchtInfo.getWechat_fee_value() == null) {
            return "\u5fae\u4fe1\u8d39\u7387\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (mchtInfo.getAlipay_fee_value() == null) {
            return "\u652f\u4ed8\u5b9d\u8d39\u7387\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (mchtInfo.getQuickpay_fee_value() == null) {
            return "\u5feb\u6377\u652f\u4ed8\u8d39\u7387\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (mchtInfo.getNetpay_fee_value() == null) {
            return "\u7f51\u94f6\u652f\u4ed8\u8d39\u7387\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (mchtInfo.getWechat_fee_value() > 20.0 || mchtInfo.getWechat_fee_value() < 0.0) {
            return "\u5fae\u4fe1\u8d39\u7387\u683c\u5f0f\u6709\u8bef";
        }
        if (mchtInfo.getAlipay_fee_value() > 20.0 || mchtInfo.getAlipay_fee_value() < 0.0) {
            return "\u652f\u4ed8\u5b9d\u8d39\u7387\u683c\u5f0f\u6709\u8bef";
        }
        if (mchtInfo.getQuickpay_fee_value() > 20.0 || mchtInfo.getQuickpay_fee_value() < 0.0) {
            return "\u5feb\u6377\u652f\u4ed8\u8d39\u7387\u683c\u5f0f\u6709\u8bef";
        }
        if (mchtInfo.getNetpay_fee_value() > 20.0 || mchtInfo.getNetpay_fee_value() < 0.0) {
            return "\u7f51\u94f6\u652f\u4ed8\u8d39\u7387\u683c\u5f0f\u6709\u8bef";
        }
        return "00";
    }
}
