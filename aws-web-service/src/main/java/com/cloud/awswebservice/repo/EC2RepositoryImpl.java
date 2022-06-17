package com.cloud.awswebservice.repo;

import com.amazonaws.services.ec2.model.DescribeInstanceStatusRequest;
import com.amazonaws.services.ec2.model.InstanceStatus;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.cloud.awswebservice.config.AwsConfig;
import java.util.List;
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
  public String createInstance(
      String keyName,
      String securityGroup,
      String imageId,
      String instanceType,
      Integer newInstances) {
    LOGGER.info("Creating EC2 Instance");
    String instanceId = null;
    RunInstancesRequest runInstancesRequest = new
        RunInstancesRequest()
        .withImageId(imageId)
        .withInstanceType(instanceType)
        .withMinCount(newInstances)
        .withMaxCount(newInstances)
        .withKeyName(keyName)
        .withSecurityGroupIds(securityGroup);
    RunInstancesResult runInstancesResult = null;
    try {
      instanceId = awsConfig
          .amazonEC2()
          .runInstances(runInstancesRequest)
          .getReservation()
          .getInstances()
          .get(0)
          .getInstanceId();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return instanceId;
  }

  @Override
  public void startInstance(String instanceId) {
    LOGGER.info("Starting EC2 Instance");
    StartInstancesRequest startInstanceRequest = new StartInstancesRequest().withInstanceIds(instanceId);
    try {
      awsConfig.amazonEC2().startInstances(startInstanceRequest);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void stopInstance(String instanceId) {
    LOGGER.info("Stopping EC2 Instance");
    StopInstancesRequest stopInstancesRequest = new StopInstancesRequest().withInstanceIds(instanceId);
    try {
      awsConfig.amazonEC2().stopInstances(stopInstancesRequest);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void terminateInstance(String instanceId) {
    LOGGER.info("Terminating EC2 Instance");
    TerminateInstancesRequest terminateInstancesRequest = new TerminateInstancesRequest().withInstanceIds(instanceId);
    try {
      awsConfig.amazonEC2().terminateInstances(terminateInstancesRequest);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<InstanceStatus> getAllInstanceStatus() {
    List<InstanceStatus> instanceStatusList = null;
    DescribeInstanceStatusRequest request = new DescribeInstanceStatusRequest();
    request.setIncludeAllInstances(true);
    try {
      instanceStatusList = awsConfig.amazonEC2().describeInstanceStatus(request).getInstanceStatuses();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return instanceStatusList;
  }
}
