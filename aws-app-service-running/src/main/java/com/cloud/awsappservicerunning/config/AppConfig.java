package com.cloud.awsappservicerunning.config;

import com.cloud.awsappservicerunning.repo.S3Repository;
import com.cloud.awsappservicerunning.repo.S3RepositoryImpl;
import com.cloud.awsappservicerunning.repo.SQSRepository;
import com.cloud.awsappservicerunning.repo.SQSRepositoryImpl;
import com.cloud.awsappservicerunning.service.DeepLearningService;
import com.cloud.awsappservicerunning.service.DeepLearningServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean
  public SQSRepository sqsRepository() {
    return new SQSRepositoryImpl();
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
