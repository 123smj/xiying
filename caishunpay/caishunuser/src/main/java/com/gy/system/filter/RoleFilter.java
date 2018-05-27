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
 *  javax.servlet.http.HttpSession
 *  org.apache.log4j.Logger
 */
package com.gy.system.filter;

import com.gy.system.SysParamUtil;
import com.gy.util.CommonFunction;

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
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class RoleFilter
        implements Filter {
    private static Logger log = Logger.getLogger(RoleFilter.class);
    private static final String X_REQUESTED_WITH = "x-requested-with";
    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";

    public void destroy() {
    }

    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;
        HttpSession session = request.getSession();
        String url = request.getRequestURL().toString();
        String res = CommonFunction.urlToRoleId(url);
        Object mchtInfo = session.getAttribute("mchtInfo");
        if (mchtInfo == null && SysParamUtil.getParam("ignoreLogin").indexOf("|" + res + "|") == -1) {
            String xRequestedWith = request.getHeader("x-requested-with");
            if (xRequestedWith != null && xRequestedWith.equalsIgnoreCase("XMLHttpRequest")) {
                response.setHeader("sessionstatus", "timeout");
                response.setHeader("locationURL", String.valueOf(request.getContextPath()) + "/manage/login.jsp");
                return;
            }
            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<script>");
            out.println("window.open ('" + request.getContextPath() + "/manage/login.jsp','_top')");
            out.println("</script>");
            out.println("</html>");
            return;
        }
        arg2.doFilter((ServletRequest) request, (ServletResponse) response);
    }

    public void init(FilterConfig arg0) throws ServletException {
    }
}
