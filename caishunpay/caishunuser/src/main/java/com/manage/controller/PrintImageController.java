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

import com.gy.system.SysParamUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;

@Controller
@RequestMapping(value = {"/manage"})
public class PrintImageController {
    private static Logger log = Logger.getLogger(PrintImageController.class);

    @RequestMapping(value = {"/printImage"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public void printImage(HttpServletRequest request, HttpServletResponse response, String path) {
        try {
            response.setContentType("image/jpeg");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Expires", "0");
            response.setDateHeader("Expires", 0L);
            String isAbsolute = request.getParameter("isAbsolute");
            File file = "true".equals(isAbsolute) ? new File(request.getParameter("fileName")) : new File(String.valueOf(SysParamUtil.getParam("MCHT_UPLOAD_PATH")) + request.getParameter("fileName"));
            int height = request.getParameter("height") == null ? 300 : Integer.valueOf(request.getParameter("height"));
            FileInputStream fileInputStream = null;
            if (file.exists()) {
                fileInputStream = new FileInputStream(file);
                BufferedImage src = ImageIO.read(fileInputStream);
                int realWidth = src.getWidth(null);
                int realHeight = src.getHeight(null);
                BufferedImage bufferedImage;
                if (realHeight > height) {
                    bufferedImage = new BufferedImage(realWidth * height / realHeight, height, 1);
                    bufferedImage.getGraphics().drawImage(src, 0, 0, realWidth * height / realHeight, height, null);
                } else {
                    bufferedImage = src;
                }
                BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
                ImageIO.write(bufferedImage, "jpg", outputStream);
                outputStream.close();
                fileInputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
