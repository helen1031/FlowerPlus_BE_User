package ggamang.flowerplus.security;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SecretKeyGenerator {
    public static String generateSecretKey(int length) {
        byte[] randomBytes = new byte[length];
        new SecureRandom().nextBytes(randomBytes);
        return bytesToHex(randomBytes);
    }

    public static String generateSHA512SecretKey(int length) throws NoSuchAlgorithmException {
        String secretKey = generateSecretKey(length);
        MessageDigest sha512Digest = MessageDigest.getInstance("SHA-512");
        byte[] hashedBytes = sha512Digest.digest(secretKey.getBytes());
        return bytesToHex(hashedBytes);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
