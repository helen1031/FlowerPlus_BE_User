package ggamang.flowerplus.security;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;

public class SecretKeyGenerator {
    public static String generateSecretKey(int length) {
        byte[] randomBytes = new byte[length];
        new SecureRandom().nextBytes(randomBytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : randomBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
