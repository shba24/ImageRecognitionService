package com.cloud.awswebservice.config;

import com.cloud.awswebservice.repo.EC2Repository;
import com.cloud.awswebservice.repo.EC2RepositoryImpl;
import com.cloud.awswebservice.repo.S3Repository;
import com.cloud.awswebservice.repo.S3RepositoryImpl;
import com.cloud.awswebservice.repo.SQSRepository;
import com.cloud.awswebservice.repo.SQSRepositoryImpl;
import com.cloud.awswebservice.service.ImageRecognitionService;
import com.cloud.awswebservice.service.ImageRecognitionServiceImpl;
import com.cloud.awswebservice.service.LoadBalancerService;
import com.cloud.awswebservice.service.LoadBalancerServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean
  public SQSRepository sqsRepository() {
    return new SQSRepositoryImpl();
  }

  @Bean
  public EC2Repository ec2Repository() {
    return new EC2RepositoryImpl();
  }

  @Bean
  public AwsConfig awsConfiguration() {
    return new AwsConfig();
  }

  @Bean
  public S3Repository s3Repository() {
    return new S3RepositoryImpl();
  }

  @Bean
  public LoadBalancerService loadBalancerService() {
    return new LoadBalancerServiceImpl();
  }

  @Bean
  public ImageRecognitionService imageRecognitionService() { return new ImageRecognitionServiceImpl(); }

}
