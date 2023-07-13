package ggamang.flowerplus.files;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.bucket.url}")
    private String bucketUrl;

    @Value("${upload.path}")
    private String localUrl;

    private AmazonS3Client amazonS3Client;

    private final Environment environment;

    public String uploadFile(byte[] file, String fileName) {
        // 프로필에 따라 S3 또는 로컬 파일 시스템에 파일 업로드
        String activeProfiles = environment.getProperty("spring.profiles.active");
        String filePath;
        if (activeProfiles != null && activeProfiles.contains("dev")) {
            if (amazonS3Client == null) {
                throw new RuntimeException("AmazonS3Client is not initialized");
            }
            filePath = uploadToS3(file, fileName);
        } else {
            filePath = uploadToLocal(file, fileName);
        }
        return filePath;
    }

    private String uploadToS3(byte[] file, String fileName) {
        try {
            log.info(String.valueOf(file));
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.length);

            amazonS3Client.putObject(bucket, fileName, new ByteArrayInputStream(file), metadata);

            return bucketUrl +"/" + fileName;
        } catch (AmazonS3Exception e) {
            log.error("Failed to upload file to S3: {}", e.getMessage());
            throw new RuntimeException("Failed to upload file to S3", e);
        } catch (Exception e) {
            log.error("General error: {}", e);
            throw e;
        }
    }

    private String uploadToLocal(byte[] file, String fileName) {
        String localFilePath = localUrl + "/" + fileName;

        File directory = new File(localUrl);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File localFile = new File(localFilePath);
        try (FileOutputStream out = new FileOutputStream(localFile)) {
            out.write(file);
            log.info("Content written to file " + localFilePath);
        } catch (IOException e) {
            log.error("Failed to upload file locally: {}", e.getMessage());
            throw new RuntimeException("Failed to upload file locally", e);
        }

        return localFilePath;
    }

    public byte[] convertBase64ToImage(String base64Image) throws IOException {
        if (Objects.isNull(base64Image) || base64Image.isEmpty()) {
            throw new IllegalArgumentException("Base64 image is empty or null");
        }

        // Remove data URL prefix (e.g., "data:image/png;base64,")
        String base64Data = base64Image.replaceAll("^data:image/[a-zA-Z]+;base64,", "");

        byte[] imageBytes = Base64.getDecoder().decode(base64Data);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        return bis.readAllBytes();
    }
}