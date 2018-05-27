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
 *  org.springframework.web.servlet.ModelAndView
 */
package com.manage.controller;

import com.gy.util.DateUtil;
import com.gy.util.StringUtil;
import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import com.manage.bean.RateInfo;
import com.manage.enums.RateTypeEnum;
import com.manage.service.OprInfoService;
import com.trade.enums.OprTypeEnum;
import com.trade.enums.ResponseEnum;
import com.trade.service.MerchantInfService;
import com.trade.util.BeanUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value={"/manage"})
public class CompanyController {
    private static Logger log = Logger.getLogger(CompanyController.class);
    @Autowired
    private MerchantInfService merchantInfServiceImpl;
    @Autowired
    private OprInfoService oprInfoServiceImpl;

    @RequestMapping(value={"/companyQuery"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public PageModle<Map<String, String>> companyQuery(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
        PageModle<Map<String, String>> pageModle = null;
        try {
            int pageNum = 1;
            Map param = this.buildRequestParam(request);
            if (!StringUtil.isEmpty((String)param.get("pageNum"))) {
                pageNum = StringUtil.parseInt((String)param.get("pageNum"));
            }
            System.out.println(param);
            pageModle = this.oprInfoServiceImpl.listCompanyInfoByPage(param, pageNum, 10);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return pageModle;
    }

    @RequestMapping(value={"/companyQuerySingle"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public ModelAndView companyQuerySingle(HttpServletRequest request, String company_id) {
        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
        ModelAndView view = new ModelAndView("/manage/company_update");
        try {
            OprInfo companyInfo = this.oprInfoServiceImpl.get(company_id);
            RateInfo rateInfo = this.oprInfoServiceImpl.getRate(company_id);
            request.setAttribute("companyInfo", (Object)companyInfo);
            request.setAttribute("companyRate", (Object)rateInfo);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return view;
    }

    @RequestMapping(value={"/companyAdd"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public String mchtAdd(HttpServletRequest request, OprInfo company, RateInfo rateInfo) {
        OprInfo oprInfo;
        block5 : {
            oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
            try {
                String checkResult = this.checkCompany(company);
                String chectRateRst = this.checkRate(rateInfo);
                if (!"00".equals(checkResult)) {
                    return checkResult;
                }
                if (!"00".equals(chectRateRst)) {
                    return chectRateRst;
                }
                if (this.oprInfoServiceImpl.get(company.getCompany_id()) == null) break block5;
                return "\u5206\u516c\u53f8\u7f16\u53f7\u5df2\u88ab\u6ce8\u518c";
            }
            catch (Exception e) {
                e.printStackTrace();
                log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
                return ResponseEnum.FAIL_SYSTEM.getMemo();
            }
        }
        String currentTime = DateUtil.getCurrTime();
        rateInfo.setOwner_no(company.getCompany_id());
        rateInfo.setOpr_id(oprInfo.getOpr_id());
        rateInfo.setRate_type(RateTypeEnum.COMPANY_RATE.getCode());
        rateInfo.setUpdate_time(currentTime);
        System.out.println(rateInfo);
        company.setLast_upd_ts(currentTime);
        company.setRegister_dt(DateUtil.getCurrentDay());
        company.setOpr_id(company.getCompany_id());
        company.setOpr_name(company.getCompany_name());
        company.setOpr_gender("1");
        company.setOpr_sta("0");
        company.setOpr_type(OprTypeEnum.BRANCH.getCode());
        company.setOpr_degree(2);
        this.oprInfoServiceImpl.save(company, rateInfo);
        return ResponseEnum.SUCCESS.getCode();
    }

    @RequestMapping(value={"/companyUpdate"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public String companyUpdate(HttpServletRequest request, OprInfo company, RateInfo rateInfo) {
        OprInfo companyInfoOld;
        RateInfo rateInfoOld;
        OprInfo oprInfo;
        block7 : {
            block6 : {
                oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
                try {
                    String checkResult = this.checkUpdateCompany(company);
                    String chectRateRst = this.checkRate(rateInfo);
                    if (!"00".equals(checkResult)) {
                        return checkResult;
                    }
                    if (!"00".equals(chectRateRst)) {
                        return chectRateRst;
                    }
                    companyInfoOld = this.oprInfoServiceImpl.get(company.getCompany_id());
                    rateInfoOld = this.oprInfoServiceImpl.getRate(company.getCompany_id());
                    if (companyInfoOld != null) break block6;
                    return "\u516c\u53f8\u4e0d\u5b58\u5728\uff0c\u4fee\u6539\u6709\u8bef";
                }
                catch (Exception e) {
                    e.printStackTrace();
                    log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
                    return ResponseEnum.FAIL_SYSTEM.getMemo();
                }
            }
            if (rateInfoOld != null) break block7;
            return "\u539f\u8d39\u7387\u4fe1\u606f\u6709\u8bef\uff0c\u4fee\u6539\u6709\u8bef";
        }
        BeanUtil.copyPropertiesNotNull(company, companyInfoOld, null, new String[0]);
        BeanUtil.copyPropertiesNotNull(rateInfo, rateInfoOld, null, new String[0]);
        String currentTime = DateUtil.getCurrTime();
        companyInfoOld.setLast_upd_ts(currentTime);
        rateInfoOld.setUpdate_time(currentTime);
        rateInfoOld.setOpr_id(oprInfo.getOpr_id());
        this.oprInfoServiceImpl.update(companyInfoOld, rateInfoOld);
        return ResponseEnum.SUCCESS.getCode();
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

    private String checkCompany(OprInfo company) {
        if (company == null) {
            return "\u53c2\u6570\u6709\u8bef";
        }
        if (StringUtil.isEmpty(company.getCompany_id())) {
            return "\u516c\u53f8\u7f16\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(company.getCompany_name())) {
            return "\u516c\u53f8\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(company.getOpr_pwd())) {
            return "\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(company.getConnact_name())) {
            return "\u8054\u7cfb\u4eba\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(company.getIdentify_no())) {
            return "\u8eab\u4efd\u8bc1\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(company.getOpr_mobile())) {
            return "\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        return "00";
    }

    private String checkUpdateCompany(OprInfo company) {
        if (company == null) {
            return "\u53c2\u6570\u6709\u8bef";
        }
        if (StringUtil.isEmpty(company.getCompany_id())) {
            return "\u516c\u53f8\u7f16\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(company.getCompany_name())) {
            return "\u516c\u53f8\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(company.getConnact_name())) {
            return "\u8054\u7cfb\u4eba\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(company.getIdentify_no())) {
            return "\u8eab\u4efd\u8bc1\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(company.getOpr_mobile())) {
            return "\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        return "00";
    }

    private String checkRate(RateInfo rateInfo) {
        if (rateInfo.getCredit_card_fee_value() == null && rateInfo.getDebit_card_fee_value() == null && rateInfo.getNetpay_fee_value() == null && rateInfo.getQuickpay_fee_value() == null && rateInfo.getWechat_fee_value() == null && rateInfo.getAlipay_fee_value() == null && rateInfo.getQq_fee_value() == null) {
            return "\u8d39\u7387\u4e0d\u80fd\u5168\u4e3a\u7a7a";
        }
        return "00";
    }
}
