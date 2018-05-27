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
 */
package com.account.controller;

import com.account.bean.TradeAccountFileDetail;
import com.account.bean.TradeAccountFileInfo;
import com.account.bean.TradeMchtAccount;
import com.account.bean.TradeMchtAccountDetail;
import com.account.service.TradeAccountFileInfoService;
import com.account.service.TradeMchtAccountService;
import com.common.model.Response;
import com.gy.system.SysParamUtil;
import com.gy.util.DateUtil;
import com.gy.util.ExcelParse;
import com.gy.util.FileUtil;
import com.gy.util.StringUtil;
import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import com.trade.bean.own.DfParam;
import com.trade.bean.own.MerchantInf;
import com.trade.bean.own.PayChannelInf;
import com.trade.enums.AccountEnum;
import com.trade.enums.MchtStatusEnum;
import com.trade.enums.ResponseEnum;
import com.trade.service.MerchantInfService;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping(value={"/manage"})
public class MchtAccountController {
    private static Logger log = Logger.getLogger(MchtAccountController.class);
    @Autowired
    private MerchantInfService merchantInfServiceImpl;
    @Autowired
    private TradeMchtAccountService tradeMchtAccountServiceImpl;
    @Autowired
    private TradeAccountFileInfoService tradeAccountFileInfoServiceImpl;

    @RequestMapping(value={"/accountAdd"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public String accountAdd(HttpServletRequest request, String mchtNo, double amount, double mchtIncome) {
        MerchantInf mchtBaseInf;
        String response;
        OprInfo oprInfo;
        block3 : {
            oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
            response = ResponseEnum.SUCCESS.getCode();
            mchtBaseInf = this.merchantInfServiceImpl.getMchtInfo(mchtNo);
            if (mchtBaseInf != null) break block3;
            return "\u5546\u6237\u4e0d\u5b58\u5728";
        }
        try {
            int amountFen = StringUtil.changeY2F(amount);
            int mchtIncomeFen = StringUtil.changeY2F(mchtIncome);
            int mchtFeeValueFen = StringUtil.changeY2F(amount) - StringUtil.changeY2F(mchtIncome);
            this.tradeMchtAccountServiceImpl.addAccountDetail(mchtBaseInf, amountFen, mchtFeeValueFen, mchtIncomeFen);
        }
        catch (Exception e) {
            response = "\u7cfb\u7edf\u5f02\u5e38";
            e.printStackTrace();
            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return response;
    }

    @RequestMapping(value={"/reQueryAccountDetail"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public Response<String> reQueryAccountDetail(HttpServletRequest request, String accountOrderNo, String mchtNo) {
        Response<String> response = new Response<String>();
        try {
            DfParam dfParam = new DfParam();
            dfParam.setDfTransactionId(accountOrderNo);
            dfParam.setGymchtId(mchtNo);
            MerchantInf qrcodeMcht = this.merchantInfServiceImpl.getMchtInfo(mchtNo);
            TradeMchtAccountDetail mchtAccountDetail = this.tradeMchtAccountServiceImpl.querySinglePay(dfParam, qrcodeMcht);
            if (mchtAccountDetail != null) {
                response.setCode("00");
                response.setMessage("\u72b6\u6001\u540c\u6b65\u6210\u529f");
            } else {
                response.setCode("02");
                response.setMessage("\u672a\u627e\u5230\u5bf9\u5e94\u8ba2\u5355");
            }
        }
        catch (Exception e) {
            log.error((Object)e.getMessage());
            response.setCode("09");
            response.setMessage("\u72b6\u6001\u540c\u6b65\u5931\u8d25");
        }
        return response;
    }

    @RequestMapping(value={"/accountFileUpload"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public String accountFileUpload(HttpServletRequest request) {
        String response;
        String rootPath;
        OprInfo oprInfo;
        MultipartFile accountFile;
        TradeAccountFileInfo tradeAccountFileInfo;
        block18 : {
            block17 : {
                block16 : {
                    oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
                    response = ResponseEnum.SUCCESS.getCode();
                    tradeAccountFileInfo = null;
                    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
                    accountFile = multipartRequest.getFile("accountFile");
                    if (accountFile != null && !accountFile.isEmpty()) break block16;
                    return "\u8d26\u76ee\u6587\u4ef6\u4e0d\u80fd\u4e3a\u7a7a";
                }
                if (accountFile.getSize() / 1024L <= 15000L) break block17;
                return "\u8d26\u76ee\u6587\u4ef6\u4e0d\u80fd\u8d85\u8fc715mb";
            }
            rootPath = String.valueOf(SysParamUtil.getParam("mcht_account_file")) + DateUtil.getCurrentDay();
            if (!new File(rootPath, accountFile.getOriginalFilename()).exists()) break block18;
            return "\u8d26\u76ee\u6587\u4ef6\u5df2\u5b58\u5728\uff0c\u8bf7\u52ff\u91cd\u590d\u4e0a\u4f20";
        }
        try {
            String filePath = FileUtil.multiUpload(accountFile, rootPath);
            List<List<String>> xlsData = null;
            try {
                xlsData = ExcelParse.readXls(filePath, 0);
            }
            catch (Exception e) {
                e.printStackTrace();
                log.error((Object)("\u989d\u5ea6\u8bbe\u7f6e\u6587\u4ef6\u89e3\u6790\u5f02\u5e38:" + accountFile.getOriginalFilename() + e.getMessage()));
            }
            String currentTime = DateUtil.getCurrTime();
            tradeAccountFileInfo = new TradeAccountFileInfo();
            tradeAccountFileInfo.setOfferDoc(accountFile.getOriginalFilename());
            tradeAccountFileInfo.setOprId(oprInfo.getOpr_id());
            tradeAccountFileInfo.setOprTime(currentTime);
            tradeAccountFileInfo.setPayType("1");
            tradeAccountFileInfo.setOfferTime(currentTime);
            tradeAccountFileInfo.setStatus(MchtStatusEnum.ADD.getCode());
            this.tradeAccountFileInfoServiceImpl.save(tradeAccountFileInfo);
            String checkResult = ResponseEnum.SUCCESS.getCode();
            if (xlsData != null) {
                ArrayList<TradeAccountFileDetail> accountDetails = new ArrayList<TradeAccountFileDetail>();
                String tradeType = "";
                xlsData.remove(0);
                for (List<String> row : xlsData) {
                    if (StringUtil.isNotEmpty(row.get(1))) {
                        tradeType = row.get(1);
                    }
                    if (StringUtil.isEmpty(row.get(2))) {
                        checkResult = "\u5546\u6237\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
                        break;
                    }
                    if (StringUtil.isEmpty(row.get(4))) {
                        checkResult = "\u8d26\u76ee\u5e8f\u5217\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
                        break;
                    }
                    if (StringUtil.isEmpty(row.get(7))) {
                        checkResult = "\u8bbe\u7f6e\u989d\u5ea6\u4e0d\u80fd\u4e3a\u7a7a";
                        break;
                    }
                    TradeAccountFileDetail accountDetail = new TradeAccountFileDetail();
                    accountDetail.setOfferSeq(tradeAccountFileInfo.getOfferSeq());
                    accountDetail.setOpr_time(currentTime);
                    accountDetail.setOffer_time(DateUtil.getCurrentDay());
                    accountDetail.setOfferDoc(tradeAccountFileInfo.getOfferDoc());
                    accountDetail.setTrade_type(tradeType);
                    accountDetail.setMcht_no(row.get(2).trim());
                    accountDetail.setMcht_name(row.get(3));
                    accountDetail.setDfsn(row.get(4));
                    int tradeAmount = 0;
                    if (!"/".equals(row.get(5))) {
                        tradeAmount = StringUtil.changeY2F(Double.valueOf(row.get(5)));
                    }
                    accountDetail.setTrade_amount(tradeAmount);
                    accountDetail.setMcht_income(StringUtil.changeY2F(Double.valueOf(row.get(7))));
                    accountDetail.setStatus(AccountEnum.ACCOUNT_NEVER.getCode());
                    accountDetails.add(accountDetail);
                }
                if (!ResponseEnum.SUCCESS.getCode().equals(checkResult)) {
                    return checkResult;
                }
                this.tradeAccountFileInfoServiceImpl.saveDetails(accountDetails);
            }
        }
        catch (IllegalArgumentException ie) {
            response = ie.getMessage();
        }
        catch (Exception e) {
            response = "\u7cfb\u7edf\u5f02\u5e38";
            tradeAccountFileInfo.setDescription("\u6587\u4ef6\u89e3\u6790\u5f02\u5e38");
            this.tradeAccountFileInfoServiceImpl.update(tradeAccountFileInfo);
            e.printStackTrace();
            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return response;
    }

    @RequestMapping(value={"/accountFileCheck"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public String accountFileCheck(HttpServletRequest request, Integer offerSeq, String checkReslut, String refuseReason) {
        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
        String response = ResponseEnum.SUCCESS.getCode();
        try {
            if (MchtStatusEnum.ADD_REFUSE.getCode().equals(checkReslut)) {
                response = this.tradeAccountFileInfoServiceImpl.checkRefuse(offerSeq, refuseReason);
            } else if (MchtStatusEnum.NORMAL.getCode().equals(checkReslut)) {
                response = this.tradeAccountFileInfoServiceImpl.checkAccept(offerSeq);
            }
        }
        catch (Exception e) {
            response = e.getMessage();
            e.printStackTrace();
            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return response;
    }

    @RequestMapping(value={"/accountFileQuery"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public PageModle<TradeAccountFileInfo> accountFileQuery(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
        PageModle<TradeAccountFileInfo> pageModle = null;
        try {
            int pageNum = 1;
            Map param = this.buildRequestParam(request);
            if (!StringUtil.isEmpty((String)param.get("pageNum"))) {
                pageNum = StringUtil.parseInt((String)param.get("pageNum"));
            }
            pageModle = this.tradeAccountFileInfoServiceImpl.listAccountFileByPage(param, pageNum, 10);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return pageModle;
    }

    @RequestMapping(value={"/accountFile4CheckQuery"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public PageModle<TradeAccountFileInfo> accountFile4CheckQuery(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
        PageModle<TradeAccountFileInfo> pageModle = null;
        try {
            int pageNum = 1;
            Map param = this.buildRequestParam(request);
            param.put("status", MchtStatusEnum.ADD.getCode());
            if (!StringUtil.isEmpty((String)param.get("pageNum"))) {
                pageNum = StringUtil.parseInt((String)param.get("pageNum"));
            }
            pageModle = this.tradeAccountFileInfoServiceImpl.listAccountFileByPage(param, pageNum, 10);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return pageModle;
    }

    @RequestMapping(value={"/accountFileDetail"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public PageModle<TradeAccountFileDetail> accountFileDetail(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
        PageModle<TradeAccountFileDetail> pageModle = null;
        try {
            int pageNum = 1;
            Map param = this.buildRequestParam(request);
            if (!StringUtil.isEmpty((String)param.get("pageNum"))) {
                pageNum = StringUtil.parseInt((String)param.get("pageNum"));
            }
            pageModle = this.tradeAccountFileInfoServiceImpl.
                    listDetailByPage(param, pageNum, 10);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return pageModle;
    }

    @RequestMapping(value={"/accountQuery"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public PageModle<TradeMchtAccount> accountQuery(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
        PageModle<TradeMchtAccount> pageModle = null;
        try {
            int pageNum = 1;
            Map param = this.buildRequestParam(request);
            if (!StringUtil.isEmpty((String)param.get("pageNum"))) {
                pageNum = StringUtil.parseInt((String)param.get("pageNum"));
            }
            System.out.println(param);
            pageModle = this.tradeMchtAccountServiceImpl.listMchtAccountByPage(param, pageNum, 10);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return pageModle;
    }

    @RequestMapping(value={"/accountDetail"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public PageModle<TradeMchtAccountDetail> accountDetail(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
        PageModle<TradeMchtAccountDetail> pageModle = null;
        try {
            int pageNum = 1;
            Map param = this.buildRequestParam(request);
            if (!StringUtil.isEmpty((String)param.get("pageNum"))) {
                pageNum = StringUtil.parseInt((String)param.get("pageNum"));
            }
            System.out.println(param);
            pageModle = this.tradeMchtAccountServiceImpl.listMchtAccountDetailByPage(param, "2", pageNum, 10);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return pageModle;
    }

    @RequestMapping(value={"/accountIncomeDetail"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public PageModle<TradeMchtAccountDetail> accountIncomeDetail(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
        PageModle<TradeMchtAccountDetail> pageModle = null;
        try {
            int pageNum = 1;
            Map param = this.buildRequestParam(request);
            if (!StringUtil.isEmpty((String)param.get("pageNum"))) {
                pageNum = StringUtil.parseInt((String)param.get("pageNum"));
            }
            System.out.println(param);
            pageModle = this.tradeMchtAccountServiceImpl.listMchtAccountDetailByPage(param, "1", pageNum, 10);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return pageModle;
    }

    @RequestMapping(value={"/accountDetailExport"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public String accountDetailExport(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
        List<TradeMchtAccountDetail> tradeList = null;
        String rootPath = String.valueOf(SysParamUtil.getParam("TRADE_EXPORT_PATH")) + DateUtil.getCurrentDay();
        File pathFile = new File(rootPath);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        String fileName = "\u51fa\u8d26\u660e\u7ec6\u62a5\u8868_" + oprInfo.getOpr_id() + "_" + DateUtil.getCurrTime() + StringUtil.getRandom(3) + ".csv";
        String result = "";
        try {
            Map param = this.buildRequestParam(request);
            tradeList = this.tradeMchtAccountServiceImpl.listAllAccountDetail(param);
            result = this.generateTradeFile(tradeList, String.valueOf(rootPath) + "/" + fileName);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return result;
    }

    private String generateTradeFile(List<TradeMchtAccountDetail> tradeList, String filePath) {
        File tradeFile = new File(filePath);
        try {
            FileChannel fileChannel = new FileOutputStream(tradeFile).getChannel();
            StringBuffer sBuffer = new StringBuffer();
            sBuffer.append("\u4ee3\u4ed8\u65e5\u671f,\u4ee3\u4ed8\u65f6\u95f4,\u5546\u6237\u7f16\u53f7,\u5546\u6237\u540d\u79f0,\u5546\u6237\u4ee3\u4ed8\u5355\u53f7,\u5e73\u53f0\u4ee3\u4ed8\u5355\u53f7,\u6536\u6b3e\u94f6\u884c,\u94f6\u884c\u5361\u53f7,\u6536\u6b3e\u4eba,\u4ee3\u4ed8\u91d1\u989d(\u5143),\u624b\u7eed\u8d39\u91d1\u989d(\u5143),\u8d26\u6237\u6263\u9664\u91d1\u989d(\u5143),\u4ee3\u4ed8\u72b6\u6001,\u8ba2\u5355\u5b8c\u6210\u65f6\u95f4");
            sBuffer.append("\n");
            for (TradeMchtAccountDetail tradeBean : tradeList) {
                sBuffer.append("'").append(tradeBean.getReceiptTime().substring(0, 8));
                sBuffer.append(",'").append(tradeBean.getReceiptTime().substring(8, 14));
                sBuffer.append(",'").append(tradeBean.getMchtNo());
                sBuffer.append(",'").append(tradeBean.getMchtName());
                sBuffer.append(",'").append(tradeBean.getDfSn());
                sBuffer.append(",'").append(tradeBean.getAccountOrderNo());
                sBuffer.append(",'").append(tradeBean.getReceiptBankNm());
                sBuffer.append(",'").append(tradeBean.getReceiptPan());
                sBuffer.append(",'").append(tradeBean.getReceiptName());
                sBuffer.append(",'").append(StringUtil.changeF2Y(tradeBean.getReceiptAmount()));
                sBuffer.append(",'").append(StringUtil.changeF2Y(tradeBean.getSingle_extra_fee()));
                sBuffer.append(",'").append(StringUtil.changeF2Y(tradeBean.getMchtIncome()));
                sBuffer.append(",'").append(StringUtil.trans2Str(tradeBean.getStatus()));
                sBuffer.append(",'").append(StringUtil.trans2Str(DateUtil.transDateFormat(tradeBean.getTimeEnd(), "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss")));
                sBuffer.append("\n");
            }
            ByteBuffer buffer = ByteBuffer.wrap(sBuffer.toString().getBytes());
            fileChannel.write(buffer);
            buffer.clear();
            fileChannel.close();
        }
        catch (Exception e) {
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
            Map.Entry entry = (Map.Entry)entries.next();
            name = (String)entry.getKey();
            Object valueObj = entry.getValue();
            value = null;
            if (valueObj == null) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[])valueObj;
                int i = 0;
                while (i < values.length) {
                    value = value == null ? (values[i] == null ? "" : values[i].trim()) : String.valueOf(value) + "," + (values[i] == null ? "" : values[i].trim());
                    ++i;
                }
            } else {
                value = valueObj.toString().trim();
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }

    private String checkChannel(PayChannelInf payChannelInf) {
        if (payChannelInf == null) {
            return "\u53c2\u6570\u6709\u8bef";
        }
        if (StringUtil.isEmpty(payChannelInf.getChannel_id())) {
            return "\u901a\u9053\u7f16\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(payChannelInf.getChannel_name())) {
            return "\u901a\u9053\u5546\u6237\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(payChannelInf.getChannel_mcht_no())) {
            return "\u901a\u9053\u5546\u6237\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(payChannelInf.getSecret_key())) {
            return "\u5bc6\u94a5\u4e0d\u80fd\u4e3a\u7a7a";
        }
        return "00";
    }

    public static void main(String[] args) {
    }
}
