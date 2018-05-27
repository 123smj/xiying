/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  javax.servlet.ServletOutputStream
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpServletResponse
 *  org.apache.log4j.Logger
 *  org.springframework.stereotype.Controller
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.ResponseBody
 */
package com.manage.controller;

import com.common.model.Response;
import com.gy.util.StringUtil;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value={"/manage"})
public class AjaxDownLoadController {
    private static Logger log = Logger.getLogger(AjaxDownLoadController.class);

    @RequestMapping(value={"/ajaxDownLoad"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public void ajaxDownLoad(HttpServletRequest request, HttpServletResponse response, String path) {
        FilterInputStream dis = null;
        DataOutputStream dos = null;
        String date = request.getParameter("date");
        String mchtNo = request.getParameter("mchtNo");
        try {
            try {
                path = path.replace("\\", "/");
                File downloadFile = new File(path);
                String fileType = "";
                if (path.indexOf(".") != -1) {
                    fileType = path.substring(path.lastIndexOf(".") + 1);
                }
                if ("txt".equalsIgnoreCase(fileType)) {
                    response.setContentType("text/plain");
                } else if ("xls".equalsIgnoreCase(fileType) || "xlsx".equalsIgnoreCase(fileType)) {
                    response.setContentType("application/vnd.ms-excel");
                } else if ("pdf".equalsIgnoreCase(fileType)) {
                    response.setContentType("application/pdf");
                } else if ("doc".equalsIgnoreCase(fileType) || "docx".equalsIgnoreCase(fileType)) {
                    response.setContentType("application/msword");
                } else {
                    response.setContentType("application/octet-stream");
                }
                response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(path.substring(path.lastIndexOf("/") + 1), "UTF-8"));
                dos = new DataOutputStream((OutputStream)response.getOutputStream());
                dis = new DataInputStream(new FileInputStream(downloadFile));
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = dis.read(buffer, 0, 8192)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                try {
                    if (dis != null) {
                        dis.close();
                    }
                    if (dos != null) {
                        dos.flush();
                        dos.close();
                    }
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        finally {
            try {
                if (dis != null) {
                    dis.close();
                }
                if (dos != null) {
                    dos.flush();
                    dos.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value={"/getReportPath"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public Response<String> getReportPath(HttpServletRequest request, String path) {
        Response<String> response = new Response<String>();
        try {
            File file;
            String date = request.getParameter("date");
            String mchtNo = request.getParameter("mchtNo");
            if (StringUtil.isNotEmpty(date) && path.contains("$$$$$$$$")) {
                path = path.replace("$$$$$$$$", date);
            }
            if (path.contains("****")) {
                if (StringUtil.isNotEmpty(mchtNo)) {
                    path = path.replace("****", mchtNo);
                } else {
                    response.setCode("01");
                    response.setMessage("\u8bf7\u9009\u62e9\u5546\u6237\u7f16\u53f7");
                    return response;
                }
            }
            if (!(file = new File(path)).exists()) {
                response.setCode("02");
                response.setMessage("\u62a5\u8868\u4e0d\u5b58\u5728");
                return response;
            }
            response.setMessage("\u6210\u529f");
            response.setCode("00");
            response.setData(path);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.setCode("09");
            response.setMessage("\u64cd\u4f5c\u5931\u8d25");
            return response;
        }
        return response;
    }
}
