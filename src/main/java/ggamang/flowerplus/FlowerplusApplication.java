package ggamang.flowerplus;

import ggamang.flowerplus.security.SecretKeyGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.NoSuchAlgorithmException;

import static ggamang.flowerplus.security.SecretKeyGenerator.generateSHA512SecretKey;

@SpringBootApplication
public class FlowerplusApplication {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		SpringApplication.run(FlowerplusApplication.class, args);
		//String sha512Key = generateSHA512SecretKey(32);
		//System.out.println("Generated SHA-512 Secret Key: " + sha512Key);
	}

}
