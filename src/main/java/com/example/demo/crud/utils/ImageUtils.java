package com.example.demo.crud.utils;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Slf4j
public class ImageUtils {

    public static String compressAndSave(MultipartFile file, String uploadDir) {

        try {
            //  Check if file is empty
            if (file == null || file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            //  Check file type
            String contentType = file.getContentType();

            if (contentType == null ||
                    !(contentType.equals("image/jpeg") ||
                      contentType.equals("image/png") ||
                      contentType.equals("image/jpg"))) {

                throw new RuntimeException("Invalid file type. Only JPG, JPEG, PNG are allowed.");
            }

            //  Check file size (max 2MB)
            if (file.getSize() > 2 * 1024 * 1024) {
                throw new RuntimeException("File size must be less than 2MB");
            }

            //  Generate file name
            String fileName = System.currentTimeMillis() + ".jpg";

            File dest = new File(uploadDir, fileName);

            //  Compress and save
            Thumbnails.of(file.getInputStream())
                    .scale(1.0)
                    .outputQuality(0.3)
                    .outputFormat("jpg")
                    .toFile(dest);

            return fileName;

        } catch (Exception e) {
            // Professional logging
            log.error("Error while uploading image: {}", e.getMessage(), e);

            throw new RuntimeException("Image upload failed");
        }
    }
}