package com.cloud.awsappservice;

import com.cloud.awsappservice.config.AppConfig;
import com.cloud.awsappservice.service.DeepLearningService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class AwsAppServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwsAppServiceApplication.class, args);
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		//context.refresh();
		DeepLearningService deepLearningService = context.getBean(DeepLearningService.class);
		deepLearningService.recognizeImage();
		context.close();
	}
}
