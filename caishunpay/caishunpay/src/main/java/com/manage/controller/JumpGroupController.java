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
package com.manage.controller;

import com.gy.system.SysParamUtil;
import com.gy.util.CommonFunction;
import com.gy.util.DateUtil;
import com.gy.util.StringUtil;
import com.manage.bean.ChannelRegisterBean;
import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import com.trade.bean.own.JumpListBean;
import com.trade.bean.own.PayChannelInf;
import com.trade.enums.ResponseEnum;
import com.trade.service.JumpListService;
import com.trade.service.MerchantInfService;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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

@Controller
@RequestMapping(value={"/manage"})
public class JumpGroupController {
    private static Logger log = Logger.getLogger(JumpGroupController.class);
    @Autowired
    private MerchantInfService merchantInfServiceImpl;
    @Autowired
    private JumpListService jumpListServiceImpl;

    @RequestMapping(value={"/jumpGroupQuery"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public PageModle<Map<String, String>> jumpGroupQuery(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
        PageModle<Map<String, String>> pageModle = null;
        try {
            int pageNum = 1;
            Map<String, String> param = this.buildRequestParam(request);
            if (!StringUtil.isEmpty(param.get("pageNum"))) {
                pageNum = StringUtil.parseInt(param.get("pageNum"));
            }
            System.out.println(param);
            pageModle = this.jumpListServiceImpl.listGroupByPage(param, pageNum, 10);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return pageModle;
    }

    @RequestMapping(value={"/jumpGroupAdd"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public String jumpGroupAdd(HttpServletRequest request, JumpListBean jumpMcht) {
        block7 : {
            block6 : {
                block5 : {
                    OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
                    try {
                        if (!StringUtil.isEmpty(jumpMcht.getJump_group())) break block5;
                        return "\u8df3\u7801\u7ec4\u7f16\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
                        return ResponseEnum.FAIL_SYSTEM.getMemo();
                    }
                }
                if (!StringUtil.isEmpty(jumpMcht.getGroup_name())) break block6;
                return "\u8df3\u7801\u7ec4\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a";
            }
            JumpListBean oldJump = this.jumpListServiceImpl.getFirstJumpList(jumpMcht.getJump_group().trim());
            if (oldJump == null) break block7;
            return "\u8df3\u7801\u7ec4\u5df2\u5b58\u5728\uff0c\u8bf7\u52ff\u91cd\u590d\u589e\u52a0";
        }
        jumpMcht.setJump_group(jumpMcht.getJump_group().trim());
        jumpMcht.setChannel_id(" ");
        jumpMcht.setTrade_source(" ");
        jumpMcht.setChannel_mcht_no(" ");
        this.jumpListServiceImpl.save(jumpMcht);
        return ResponseEnum.SUCCESS.getCode();
    }

    @RequestMapping(value={"/jumpMchtAdd"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public String jumpGroupUpdate(HttpServletRequest request, JumpListBean jumpMcht) {
        JumpListBean oldJump;
        block13 : {
            block12 : {
                block11 : {
                    block10 : {
                        block9 : {
                            if (!StringUtil.isEmpty(jumpMcht.getChannel_id())) break block9;
                            return "\u6e20\u9053\u7f16\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
                        }
                        if (!StringUtil.isEmpty(jumpMcht.getChannel_mcht_no())) break block10;
                        return "\u5546\u6237\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
                    }
                    if (!StringUtil.isEmpty(jumpMcht.getJump_group())) break block11;
                    return "\u8df3\u7801\u7ec4\u4e0d\u80fd\u4e3a\u7a7a";
                }
                if (!StringUtil.isEmpty(jumpMcht.getTrade_source())) break block12;
                return "\u4ea4\u6613\u7c7b\u578b\u4e0d\u80fd\u4e3a\u7a7a";
            }
            oldJump = this.jumpListServiceImpl.getFirstJumpList(jumpMcht.getJump_group());
            if (oldJump != null) break block13;
            return "\u8df3\u7801\u7ec4\u4fe1\u606f\u6709\u8bef\uff0c\u8bf7\u91cd\u65b0\u6838\u5bf9";
        }
        try {
            String[] tradeSources;
            String groupName = oldJump.getGroup_name();
            String tradeSourceStr = jumpMcht.getTrade_source();
            String[] arrstring = tradeSources = tradeSourceStr.split(",");
            int n = arrstring.length;
            int n2 = 0;
            while (n2 < n) {
                String tradeSource = arrstring[n2];
                JumpListBean jumpListBean = new JumpListBean();
                jumpListBean.setChannel_id(jumpMcht.getChannel_id());
                jumpListBean.setChannel_mcht_no(jumpMcht.getChannel_mcht_no());
                jumpListBean.setJump_group(jumpMcht.getJump_group());
                jumpListBean.setWeight(1);
                jumpListBean.setGroup_name(groupName);
                jumpListBean.setTrade_source(tradeSource);
                this.jumpListServiceImpl.save(jumpListBean);
                ++n2;
            }
            if (StringUtil.isEmpty(oldJump.getChannel_mcht_no().trim())) {
                this.jumpListServiceImpl.delet(oldJump);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEnum.FAIL_SYSTEM.getMemo();
        }
        return ResponseEnum.SUCCESS.getCode();
    }

    @RequestMapping(value={"/jumpMchtDelete"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public String jumpGroupDelete(HttpServletRequest request, String jump_group, String channel_id, String channel_mcht_no, String trade_source) {
        JumpListBean jumpListBean;
        block3 : {
            try {
                jumpListBean = this.jumpListServiceImpl.getJumpList(jump_group, channel_id, channel_mcht_no, trade_source);
                if (jumpListBean != null) break block3;
                return "\u8df3\u7801\u4fe1\u606f\u4e0d\u5b58\u5728";
            }
            catch (Exception e) {
                e.printStackTrace();
                return ResponseEnum.FAIL_SYSTEM.getMemo();
            }
        }
        this.jumpListServiceImpl.delet(jumpListBean);
        return ResponseEnum.SUCCESS.getCode();
    }

    @RequestMapping(value={"/jumpGroupExport"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public String jumpGroupExport(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
        List<Map<String, String>> mchtList = null;
        String rootPath = String.valueOf(SysParamUtil.getParam("MCHT_EXPORT_PATH")) + DateUtil.getCurrentDay();
        File pathFile = new File(rootPath);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        String fileName = "\u7ebf\u4e0a\u4ea4\u6613\u8df3\u7801\u7ec4\u4fe1\u606f_" + oprInfo.getOpr_id() + "_" + DateUtil.getCurrTime() + StringUtil.getRandom(3) + ".csv";
        String result = "";
        try {
            Map<String, String> param = this.buildRequestParam(request);
            mchtList = this.jumpListServiceImpl.listAllJumpgroup(param);
            result = this.generateFile(mchtList, String.valueOf(rootPath) + "/" + fileName);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return result;
    }

    private String generateFile(List<Map<String, String>> dataList, String filePath) {
        File tradeFile = new File(filePath);
        try {
            FileChannel fileChannel = new FileOutputStream(tradeFile).getChannel();
            StringBuffer sBuffer = new StringBuffer();
            sBuffer.append("\u8df3\u7801\u7ec4\u7f16\u53f7,\u8df3\u7801\u7ec4\u540d\u79f0,\u4ea4\u6613\u7c7b\u578b,\u6e20\u9053\u7f16\u53f7,\u6e20\u9053\u540d\u79f0,\u901a\u9053\u5546\u6237\u7f16\u53f7,\u901a\u9053\u5546\u6237\u540d\u79f0,\u8df3\u7801\u6743\u91cd");
            sBuffer.append("\n");
            for (Map<String, String> dataMap : dataList) {
                sBuffer.append("").append(StringUtil.trans2Str(dataMap.get("jump_group")));
                sBuffer.append(",").append(StringUtil.trans2Str(dataMap.get("group_name")));
                sBuffer.append(",").append(CommonFunction.transTradeSource(dataMap.get("trade_source")));
                sBuffer.append(",'").append(StringUtil.trans2Str(dataMap.get("channel_id")));
                sBuffer.append(",").append(StringUtil.trans2Str(dataMap.get("channel_name")));
                sBuffer.append(",'").append(StringUtil.trans2Str(dataMap.get("channel_mcht_no")));
                sBuffer.append(",").append(StringUtil.trans2Str(dataMap.get("channel_name")));
                sBuffer.append(",").append(StringUtil.trans2Str(dataMap.get("weight")));
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

    private Map<String, String> buildRequestParam(HttpServletRequest request) {
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
}
