///*
// * Decompiled with CFR 0_124.
// */
//package com.gy.util;
//
//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGEncodeParam;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;
//import java.awt.Graphics;
//import java.awt.Image;
//import java.awt.image.BufferedImage;
//import java.awt.image.ImageObserver;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.PrintStream;
//import javax.imageio.ImageIO;
//
//public class NarrowImage {
//    public BufferedImage zoomImage(String src) {
//        BufferedImage result = null;
//        try {
//            File srcfile = new File(src);
//            if (!srcfile.exists()) {
//                System.out.println("\u6587\u4ef6\u4e0d\u5b58\u5728");
//            }
//            BufferedImage im = ImageIO.read(srcfile);
//            int width = im.getWidth();
//            int height = im.getHeight();
//            float resizeTimes = 0.3f;
//            int toWidth = (int)((float)width * resizeTimes);
//            int toHeight = (int)((float)height * resizeTimes);
//            result = new BufferedImage(toWidth, toHeight, 1);
//            result.getGraphics().drawImage(im.getScaledInstance(toWidth, toHeight, 4), 0, 0, null);
//        }
//        catch (Exception e) {
//            System.out.println("\u521b\u5efa\u7f29\u7565\u56fe\u53d1\u751f\u5f02\u5e38" + e.getMessage());
//        }
//        return result;
//    }
//
//    public boolean writeHighQuality(BufferedImage im, String fileFullPath) {
//        try {
//            FileOutputStream newimage = new FileOutputStream(fileFullPath);
//            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimage);
//            JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(im);
//            jep.setQuality(0.9f, true);
//            encoder.encode(im, jep);
//            newimage.close();
//            return true;
//        }
//        catch (Exception e) {
//            return false;
//        }
//    }
//}
