/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  javax.servlet.Filter
 *  javax.servlet.FilterChain
 *  javax.servlet.FilterConfig
 *  javax.servlet.ServletException
 *  javax.servlet.ServletRequest
 *  javax.servlet.ServletResponse
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpServletResponse
 *  org.apache.log4j.Logger
 */
package com.gy.system.filter;

import com.gy.system.DictParamUtil;
import com.gy.system.SysParamUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class IPForbidFilter
implements Filter {
    private static Logger log = Logger.getLogger(IPForbidFilter.class);

    public void destroy() {
    }

    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain flter) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)arg0;
        HttpServletResponse response = (HttpServletResponse)arg1;
        String clientIP = request.getRemoteAddr();
        String uri = request.getRequestURI();
        log.info((Object)("client ip:" + clientIP + "----\u8bf7\u6c42url:" + uri));
        if ("true".equals(SysParamUtil.getParam("ip_forbid_switch")) && SysParamUtil.getParam("ignoreIpForbid").indexOf(String.valueOf(uri) + "|") == -1 && !DictParamUtil.isWhiteIp(clientIP)) {
            response.getWriter().write("IP_Forbidden");
            return;
        }
        flter.doFilter((ServletRequest)request, (ServletResponse)response);
    }

    public void init(FilterConfig arg0) throws ServletException {
    }
}
