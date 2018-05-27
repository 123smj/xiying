/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.springframework.web.multipart.MultipartFile
 */
package com.gy.util;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class FileUtil {
    public static String multiUpload(MultipartFile file, String path) throws IllegalStateException, IOException {
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        File destFile = new File(pathFile, file.getOriginalFilename());
        file.transferTo(destFile);
        return destFile.getAbsolutePath();
    }
}
