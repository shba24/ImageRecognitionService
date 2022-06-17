package com.cloud.awsappservice.repo;

import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.util.EC2MetadataUtils;
import com.cloud.awsappservice.config.AwsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EC2RepositoryImpl implements EC2Repository {

  private static final Logger LOGGER = LoggerFactory.getLogger(EC2RepositoryImpl.class);

  @Autowired
  private AwsConfig awsConfig;

  @Override
  public void terminateInstance() {
    LOGGER.info("Terminating the instance itself");
    String instanceId = EC2MetadataUtils.getInstanceId();
    TerminateInstancesRequest terminateInstancesRequest = new TerminateInstancesRequest().withInstanceIds(instanceId);
    try {
      awsConfig.amazonEC2().terminateInstances(terminateInstancesRequest);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
