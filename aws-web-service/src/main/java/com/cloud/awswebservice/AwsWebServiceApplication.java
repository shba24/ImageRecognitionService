package com.cloud.awswebservice;

import com.cloud.awswebservice.config.AppConfig;
import com.cloud.awswebservice.service.ImageRecognitionService;
import com.cloud.awswebservice.service.LoadBalancerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class AwsWebServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwsWebServiceApplication.class, args);
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		//context.refresh();
		ImageRecognitionService imageRecognitionService = context.getBean(ImageRecognitionService.class);
		new Thread(imageRecognitionService::receiveOutput).start();
		LoadBalancerService loadBalancerService = context.getBean(LoadBalancerService.class);
		loadBalancerService.scaleInOrOut();
		context.close();
	}

}
