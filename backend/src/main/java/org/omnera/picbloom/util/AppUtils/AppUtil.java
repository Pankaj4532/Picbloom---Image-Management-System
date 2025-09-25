package org.omnera.picbloom.util.AppUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

public class AppUtil {

    // Base uploads folder (can be moved to application.properties later)
    public static final String UPLOAD_DIR = "src/main/resources/static/uploads";

    public static Path getUploadPath(String fileName, long albumId, String folderName) throws IOException {
        // Normalize to avoid ".." attacks
        String safeFileName = Paths.get(fileName).getFileName().toString();

        // create album-specific folder
        Path uploadPath = Paths.get(UPLOAD_DIR, String.valueOf(albumId), folderName)
                .toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        return uploadPath.resolve(safeFileName);

    }

    public static BufferedImage getThumbnail(MultipartFile originalFile, Integer width) throws IOException {
        BufferedImage thumbImg = null;
        BufferedImage img = ImageIO.read(originalFile.getInputStream());
        thumbImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, width, Scalr.OP_ANTIALIAS);

        return thumbImg;
    }

    // ---------------- Get File as Resource ----------------

    public static Resource getFileAResource(long album_id, String folderName, String fileName) throws IOException {
        Path location = Paths.get(UPLOAD_DIR, String.valueOf(album_id), folderName, fileName)
                .toAbsolutePath().normalize();
        File file = location.toFile();
        if (file.exists()) {
            return new UrlResource(location.toUri());
        } else {
            return null; // or throw new IOException("File not found: " + fileName);
        }

    }

    public static boolean delete_photo_from_path(String fileName, String folderName, long album_id) {
    try {
        Path filePath = Paths.get(UPLOAD_DIR, String.valueOf(album_id), folderName, fileName)
                             .toAbsolutePath().normalize();
        File f = filePath.toFile();
        if(f.exists() && f.delete()){
            return true;
        } else {
            System.out.println("File not found or cannot delete: " + f.getAbsolutePath());
            return false;
        }
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

}
