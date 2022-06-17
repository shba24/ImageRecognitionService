package com.cloud.awswebservice.repo;

import com.amazonaws.services.ec2.model.InstanceStatus;
import java.util.List;

public interface EC2Repository {
  String createInstance(
      String keyName,
      String securityGroup,
      String imageId,
      String instanceType,
      Integer newInstances);
  void startInstance(String instanceId);
  void stopInstance(String instanceId);
  void terminateInstance(String instanceId);
  List<InstanceStatus> getAllInstanceStatus();
}
