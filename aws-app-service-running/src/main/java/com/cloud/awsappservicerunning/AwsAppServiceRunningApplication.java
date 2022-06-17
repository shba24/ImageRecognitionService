package com.cloud.awsappservicerunning;

import com.cloud.awsappservicerunning.config.AppConfig;
import com.cloud.awsappservicerunning.service.DeepLearningService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class AwsAppServiceRunningApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwsAppServiceRunningApplication.class, args);
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		//context.refresh();
		DeepLearningService deepLearningService = context.getBean(DeepLearningService.class);
		deepLearningService.recognizeImage();
		context.close();
	}
}
