package com.openclassrooms.chatop.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path root = Path.of("uploads");

    public String store(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("PICTURE_REQUIRED");
            }

            Files.createDirectories(root);

            String original = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
            String ext = "";
            int dot = original.lastIndexOf('.');
            if (dot >= 0) ext = original.substring(dot).toLowerCase();

            // on force png/jpg uniquement (recommandé)
            if (!ext.equals(".png") && !ext.equals(".jpg") && !ext.equals(".jpeg")) {
                throw new IllegalArgumentException("UNSUPPORTED_IMAGE_FORMAT");
            }

            String filename = UUID.randomUUID() + ".jpg"; // on normalise en jpg
            Path destination = root.resolve(filename).normalize();

            // Redimension + compression
            Thumbnails.of(file.getInputStream())
                    .size(960, 639)          // max width/height
                    .outputFormat("jpg")
                    .outputQuality(0.85)
                    .toFile(destination.toFile());

            return "/uploads/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("FILE_UPLOAD_FAILED", e);
        }
    }
}
