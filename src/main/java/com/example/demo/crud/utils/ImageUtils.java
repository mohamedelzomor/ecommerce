package com.example.demo.crud.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class ImageUtils {

    public static String compressAndSave(MultipartFile file, String uploadDir) throws IOException {

        // اسم جديد للصورة
        String fileName = System.currentTimeMillis() + ".jpg";

        File dest = new File(uploadDir, fileName);

        // ضغط الصورة وتحويلها JPG
        Thumbnails.of(file.getInputStream())
                .scale(1.0)        // نفس الأبعاد
                .outputQuality(0.3) // تقليل الحجم ~70%
                .outputFormat("jpg")
                .toFile(dest);

        return fileName;
    }
}