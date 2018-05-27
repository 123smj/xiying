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
package com.account.controller;

import com.account.bean.TradeMchtAccount;
import com.account.bean.TradeMchtAccountDetail;
import com.account.service.TradeMchtAccountService;
import com.gy.system.SysParamUtil;
import com.gy.util.DateUtil;
import com.gy.util.StringUtil;
import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import com.trade.bean.own.QrcodeChannelInf;
import com.trade.bean.own.QrcodeMchtInfo;
import com.trade.enums.ResponseEnum;
import com.trade.service.QrcodeMchtInfoService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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

@Controller
@RequestMapping(value = {"/manage"})
public class MchtAccountController {
    private static Logger log = Logger.getLogger(MchtAccountController.class);
    @Autowired
    private QrcodeMchtInfoService qrcodeMchtInfoServiceImpl;
    @Autowired
    private TradeMchtAccountService tradeMchtAccountServiceImpl;

    @RequestMapping(value = {"/accountAdd"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public String accountAdd(HttpServletRequest request, String mchtNo, int amount, int mchtFeeValue, int mchtIncome) {
        OprInfo oprInfo;
        QrcodeMchtInfo mchtBaseInf;
        String response;
        block3:
        {
            oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
            response = ResponseEnum.SUCCESS.getCode();
            mchtBaseInf = this.qrcodeMchtInfoServiceImpl.getMchtInfo(mchtNo);
            if (mchtBaseInf != null) break block3;
            return "\u5546\u6237\u4e0d\u5b58\u5728";
        }
        try {
            this.tradeMchtAccountServiceImpl.addAccountDetail(mchtBaseInf, amount, mchtFeeValue, mchtIncome);
        } catch (Exception e) {
            response = "\u7cfb\u7edf\u5f02\u5e38";
            e.printStackTrace();
            log.error((Object) ("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return response;
    }

    @RequestMapping(value = {"/accountQuery"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public PageModle<TradeMchtAccount> accountQuery(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
        PageModle<TradeMchtAccount> pageModle = null;
        try {
            int pageNum = 1;
            Map param = this.buildRequestParam(request);
            param.put("mchtNo", oprInfo.getOpr_id());
            if (!StringUtil.isEmpty((String) param.get("pageNum"))) {
                pageNum = StringUtil.parseInt((String) param.get("pageNum"));
            }
            System.out.println(param);
            pageModle = this.tradeMchtAccountServiceImpl.listMchtAccountByPage(param, pageNum, 10);
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) ("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return pageModle;
    }

    @RequestMapping(value = {"/accountDetail"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public PageModle<TradeMchtAccountDetail> accountDetail(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
        PageModle<TradeMchtAccountDetail> pageModle = null;
        try {
            int pageNum = 1;
            Map param = this.buildRequestParam(request);
            if (!StringUtil.isEmpty((String) param.get("pageNum"))) {
                pageNum = StringUtil.parseInt((String) param.get("pageNum"));
            }
            System.out.println(param);
            pageModle = this.tradeMchtAccountServiceImpl.listMchtAccountDetailByPage(param, "2", pageNum, 10);
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) ("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return pageModle;
    }

    @RequestMapping(value = {"/accountIncomeDetail"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public PageModle<TradeMchtAccountDetail> accountIncomeDetail(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
        PageModle<TradeMchtAccountDetail> pageModle = null;
        try {
            int pageNum = 1;
            Map param = this.buildRequestParam(request);
            if (!StringUtil.isEmpty((String) param.get("pageNum"))) {
                pageNum = StringUtil.parseInt((String) param.get("pageNum"));
            }
            System.out.println(param);
            pageModle = this.tradeMchtAccountServiceImpl.listMchtAccountDetailByPage(param, "1", pageNum, 10);
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) ("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return pageModle;
    }

    @RequestMapping(value = {"/accountDetailExport"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public String accountDetailExport(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
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
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) ("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return result;
    }

    private String generateTradeFile(List<TradeMchtAccountDetail> tradeList, String filePath) {
        File tradeFile = new File(filePath);
        try {
            FileChannel fileChannel = new FileOutputStream(tradeFile).getChannel();
            StringBuffer sBuffer = new StringBuffer();
            sBuffer.append("\u4ee3\u4ed8\u65e5\u671f,\u4ee3\u4ed8\u65f6\u95f4,\u5546\u6237\u7f16\u53f7,\u5546\u6237\u4ee3\u4ed8\u5355\u53f7,\u5e73\u53f0\u4ee3\u4ed8\u5355\u53f7,\u6536\u6b3e\u94f6\u884c,\u94f6\u884c\u5361\u53f7,\u6536\u6b3e\u4eba,\u4ee3\u4ed8\u91d1\u989d(\u5143),\u624b\u7eed\u8d39\u91d1\u989d(\u5143),\u8d26\u6237\u6263\u9664\u91d1\u989d(\u5143),\u4ee3\u4ed8\u72b6\u6001,\u8ba2\u5355\u5b8c\u6210\u65f6\u95f4");
            sBuffer.append("\n");
            for (TradeMchtAccountDetail tradeBean : tradeList) {
                sBuffer.append("'").append(tradeBean.getReceiptTime().substring(0, 8));
                sBuffer.append(",'").append(tradeBean.getReceiptTime().substring(8, 14));
                sBuffer.append(",'").append(tradeBean.getMchtNo());
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
