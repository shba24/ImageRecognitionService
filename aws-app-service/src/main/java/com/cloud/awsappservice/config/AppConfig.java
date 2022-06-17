package com.cloud.awsappservice.config;

import com.cloud.awsappservice.repo.EC2Repository;
import com.cloud.awsappservice.repo.EC2RepositoryImpl;
import com.cloud.awsappservice.repo.S3Repository;
import com.cloud.awsappservice.repo.S3RepositoryImpl;
import com.cloud.awsappservice.repo.SQSRepository;
import com.cloud.awsappservice.repo.SQSRepositoryImpl;
import com.cloud.awsappservice.service.DeepLearningService;
import com.cloud.awsappservice.service.DeepLearningServiceImpl;
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
  public DeepLearningService deepLearningService() {
    return new DeepLearningServiceImpl();
  }

}
