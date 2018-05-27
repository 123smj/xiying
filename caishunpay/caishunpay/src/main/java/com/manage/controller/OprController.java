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

import com.common.model.Response;
import com.gy.util.CommonFunction;
import com.gy.util.StringUtil;
import com.manage.bean.MenuBean;
import com.manage.bean.OprInfo;
import com.manage.bean.TradeFuncInf;
import com.manage.service.OprInfoService;
import com.manage.service.TradeFuncInfService;
import com.trade.service.WxpayService;
import com.trade.util.JsonUtil;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value={"/manage"})
public class OprController {
    private static Logger log = Logger.getLogger(OprController.class);
    @Autowired
    private OprInfoService oprInfoServiceImpl;
//    @Autowired
//    private WxpayService wxpayServiceImpl;
    @Autowired
    private TradeFuncInfService tradeFuncInfServiceImpl;

    @RequestMapping(value={"/login"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public Response<String> login(HttpServletRequest request, OprInfo oprInfo) {
        System.out.println(oprInfo.getOpr_id());
        System.out.println(oprInfo.getOpr_pwd());
        Response<String> response = new Response<String>();
        try {
            OprInfo opr = this.oprInfoServiceImpl.get(oprInfo.getOpr_id());
            if (opr == null || !oprInfo.getOpr_pwd().equals(opr.getOpr_pwd())) {
                response.setCode("01");
                response.setMessage("\u7528\u6237\u4e0d\u5b58\u5728\u6216\u5bc6\u7801\u9519\u8bef");
                return response;
            }
            opr.setBelowMchts(CommonFunction.getBelowMcht(opr.getCompany_id()));
            List<TradeFuncInf> list = this.tradeFuncInfServiceImpl.getFuncByOprDegree(opr.getOpr_degree());
            List<MenuBean> menuTree = this.tradeFuncInfServiceImpl.build(list);
            HashSet<String> authUrlSet = this.tradeFuncInfServiceImpl.getAuthSet(list);
            String menuJson = JsonUtil.buildJson(menuTree);
            System.out.println(menuJson);
            response.setCode("00");
            response.setMessage("\u767b\u5f55\u6210\u529f");
            System.out.println("\u4e0b\u5c5e\u5546\u6237\uff1a" + opr.getBelowMchts());
            request.getSession().setAttribute("menuTree", (Object)menuJson);
            request.getSession().setAttribute("oprInfo", (Object)opr);
            request.getSession().setAttribute("user_auth_set", authUrlSet);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error((Object)("\u767b\u5f55exception------opr_id:" + oprInfo.getOpr_id() + e));
        }
        return response;
    }

    @RequestMapping(value={"/updatePwd"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public Response<String> updatePwd(HttpServletRequest request, OprInfo oprInfo) {
        Response<String> response = new Response<String>();
        try {
            String new_opr_pwd = request.getParameter("new_opr_pwd");
            if (StringUtil.isEmpty(new_opr_pwd)) {
                response.setCode("02");
                response.setMessage("\u65b0\u5bc6\u7801\u4e3a\u7a7a");
                return response;
            }
            OprInfo opr = this.oprInfoServiceImpl.get(oprInfo.getOpr_id());
            if (opr == null || !oprInfo.getOpr_pwd().equals(opr.getOpr_pwd())) {
                response.setCode("01");
                response.setMessage("\u7528\u6237\u4e0d\u5b58\u5728\u6216\u5bc6\u7801\u9519\u8bef");
                return response;
            }
            opr.setOpr_pwd(new_opr_pwd);
            this.oprInfoServiceImpl.update(opr);
            response.setCode("00");
            response.setMessage("\u4fee\u6539\u6210\u529f");
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error((Object)("\u767b\u5f55exception------opr_id:" + oprInfo.getOpr_id() + e));
        }
        return response;
    }

    @RequestMapping(value={"/loginRedirect"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public ModelAndView loginRedirect(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
        ModelAndView view = null;
        try {
            if (oprInfo == null) {
                view = new ModelAndView("/manage/login");
                return view;
            }
            view = new ModelAndView("/manage/main");
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error((Object)("\u767b\u5f55exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return view;
    }

    @RequestMapping(value={"/loginOut"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public ModelAndView loginOut(HttpServletRequest request) {
        request.getSession().invalidate();
        ModelAndView view = new ModelAndView("/manage/login");
        return view;
    }
}
