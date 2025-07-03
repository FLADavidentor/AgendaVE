package com.uam.agendave.service.imagen;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class ImageStorageService {

    @Value("${app.image.storage.path}")
    private String imageStoragePath;

    public String saveImage(String base64Image) throws IOException, NoSuchAlgorithmException {
        if (base64Image == null || base64Image.trim().isEmpty()) {
            return null;
        }

        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
        String hash = sha256Hex(decodedBytes);
        String filename = hash + ".jpg";
        Path imagePath = Paths.get(imageStoragePath, filename);

        if (!Files.exists(imagePath)) {
            Files.write(imagePath, decodedBytes, StandardOpenOption.CREATE_NEW);
        }

        return filename;
    }


    public void deleteImageIfUnused(String filename, long countOfUsages) {
        if (filename == null || countOfUsages > 1) return;

        try {
            Path imagePath = Paths.get(imageStoragePath, filename);
            Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            System.out.println("Failed to delete image: " + filename);
        }
    }

    private String sha256Hex(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data);
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }
}