package com.cloud.awsappservice.repo;

import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.util.EC2MetadataUtils;
import com.cloud.awsappservice.config.AwsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EC2RepositoryImpl implements EC2Repository {

  @Autowired
  private AwsConfig awsConfig;

  @Override
  public void terminateInstance() {
    String instanceId = EC2MetadataUtils.getInstanceId();
    TerminateInstancesRequest terminateInstancesRequest = new TerminateInstancesRequest().withInstanceIds(instanceId);
    try {
      awsConfig.amazonEC2().terminateInstances(terminateInstancesRequest);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
