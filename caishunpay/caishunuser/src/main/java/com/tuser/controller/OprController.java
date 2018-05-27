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
package com.tuser.controller;

import com.common.model.Response;
import com.gy.util.DateUtil;
import com.gy.util.MD5Encrypt;
import com.gy.util.StringUtil;
import com.manage.bean.MenuBean;
import com.manage.bean.OprInfo;
import com.manage.service.TradeUserMenuService;
import com.trade.bean.own.QrcodeMchtInfo;
import com.trade.dao.QrcodeMchtInfoDao;
import com.trade.util.JsonUtil;
import com.tuser.bean.TradeUserMcht;
import com.tuser.bean.TradeUserMenu;
import com.tuser.service.TradeUserMchtService;

import java.io.PrintStream;
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
@RequestMapping(value = {"/tuser"})
public class OprController {
    private static Logger log = Logger.getLogger(OprController.class);
    @Autowired
    private TradeUserMchtService tradeUserMchtServiceImpl;
    @Autowired
    private TradeUserMenuService tradeUserMenuServiceImpl;
    @Autowired
    private QrcodeMchtInfoDao qrcodeMchtInfoDaoImpl;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @RequestMapping(value = {"/login"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public Response<String> login(HttpServletRequest request, String mchtNo, String mcht_password) {
        Response<String> response = new Response<String>();
        try {
            QrcodeMchtInfo qrcodeMchtInfo = this.qrcodeMchtInfoDaoImpl.getMchtInfo(mchtNo);
            if (qrcodeMchtInfo == null) {
                response.setCode("01");
                response.setMessage("\u5546\u6237\u4e0d\u5b58\u5728");
                return response;
            }
            if (StringUtil.isEmpty(mcht_password)) {
                response.setCode("09");
                response.setMessage("\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a");
                return response;
            }
            TradeUserMcht userMcht = this.tradeUserMchtServiceImpl.get(mchtNo);
            if (StringUtil.isEmpty(qrcodeMchtInfo.getIdentity_no()) || qrcodeMchtInfo.getIdentity_no().length() < 6) {
                response.setCode("02");
                response.setMessage("\u5546\u6237\u4fe1\u606f\u4e0d\u5168,\u8bf7\u5b8c\u5584\u8d44\u6599");
                return response;
            }
            String checkPwd = "";
            if (userMcht == null) {
                String identityNo = qrcodeMchtInfo.getIdentity_no().trim();
                checkPwd = MD5Encrypt.getMessageDigest(identityNo.substring(identityNo.length() - 6), new String[0]);
                if (!checkPwd.equalsIgnoreCase(mcht_password)) {
                    response.setCode("03");
                    response.setMessage("\u521d\u59cb\u5bc6\u7801\u9519\u8bef");
                    return response;
                }
                userMcht = new TradeUserMcht();
                userMcht.setMchtNo(mchtNo);
                userMcht.setCreateTime(DateUtil.getCurrTime());
                userMcht.setCreateType("1");
                userMcht.setPassword(mcht_password);
                this.tradeUserMchtServiceImpl.save(userMcht);
            } else {
                checkPwd = userMcht.getPassword();
                if (!checkPwd.equalsIgnoreCase(mcht_password)) {
                    response.setCode("04");
                    response.setMessage("\u5bc6\u7801\u9519\u8bef");
                    return response;
                }
            }
            List<TradeUserMenu> list = this.tradeUserMenuServiceImpl.getFuncByOprDegree(1);
            List<MenuBean> menuTree = this.tradeUserMenuServiceImpl.build(list);
            String menuJson = JsonUtil.buildJson(menuTree);
            System.out.println(menuJson);
            response.setCode("00");
            response.setMessage("\u767b\u5f55\u6210\u529f");
            OprInfo oprInfo = new OprInfo();
            oprInfo.setOpr_id(qrcodeMchtInfo.getMchtNo());
            oprInfo.setOpr_name(qrcodeMchtInfo.getMchtName());
            request.getSession().setAttribute("menuTree", (Object) menuJson);
            request.getSession().setAttribute("oprInfo", (Object) oprInfo);
            request.getSession().setAttribute("mchtInfo", (Object) qrcodeMchtInfo);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) ("\u5546\u6237\u767b\u5f55exception------mchtNo:" + mchtNo + e));
        }
        return response;
    }

    @RequestMapping(value = {"/updatePwd"}, produces = {"application/json; charset=utf-8"})
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
            TradeUserMcht userMcht = this.tradeUserMchtServiceImpl.get(oprInfo.getOpr_id());
            if (userMcht == null || !oprInfo.getOpr_pwd().equals(userMcht.getPassword())) {
                response.setCode("01");
                response.setMessage("\u7528\u6237\u4e0d\u5b58\u5728\u6216\u5bc6\u7801\u9519\u8bef");
                return response;
            }
            userMcht.setPassword(new_opr_pwd);
            this.tradeUserMchtServiceImpl.update(userMcht);
            response.setCode("00");
            response.setMessage("\u4fee\u6539\u6210\u529f");
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) ("\u5546\u6237\u4fee\u6539\u5bc6\u7801\u5f02\u5e38:" + oprInfo.getOpr_id() + e));
            response.setCode("09");
            response.setMessage("\u672c\u6b21\u64cd\u4f5c\u5931\u8d25");
        }
        return response;
    }

    @RequestMapping(value = {"/loginRedirect"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public ModelAndView loginRedirect(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
        ModelAndView view = null;
        try {
            if (oprInfo == null) {
                view = new ModelAndView("/manage/login");
                return view;
            }
            view = new ModelAndView("/manage/main");
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) ("\u5546\u6237\u767b\u5f55exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return view;
    }

    @RequestMapping(value = {"/loginOut"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public ModelAndView loginOut(HttpServletRequest request) {
        request.getSession().invalidate();
        ModelAndView view = new ModelAndView("/manage/login");
        return view;
    }
}
