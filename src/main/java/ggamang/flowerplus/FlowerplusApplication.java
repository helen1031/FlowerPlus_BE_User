package ggamang.flowerplus;

import ggamang.flowerplus.security.SecretKeyGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlowerplusApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlowerplusApplication.class, args);
		//String secretKey = SecretKeyGenerator.generateSecretKey(32);
		//System.out.println("Secret Key: " + secretKey);
	}

}
