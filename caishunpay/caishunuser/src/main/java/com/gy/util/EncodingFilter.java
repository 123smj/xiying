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
 */
package com.gy.util;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class EncodingFilter
        implements Filter {
    protected String encoding = null;
    protected FilterConfig filterConfig = null;
    protected boolean ignore = true;

    public void destroy() {
        this.encoding = null;
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String encoding;
        if ((this.ignore || request.getCharacterEncoding() == null) && (encoding = this.selectEncoding(request)) != null) {
            request.setCharacterEncoding(encoding);
            response.setCharacterEncoding(encoding);
        }
        chain.doFilter(request, response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        this.encoding = filterConfig.getInitParameter("encoding");
        String value = filterConfig.getInitParameter("ignore");
        this.ignore = value == null || value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes");
    }

    protected String selectEncoding(ServletRequest request) {
        return this.encoding;
    }
}
