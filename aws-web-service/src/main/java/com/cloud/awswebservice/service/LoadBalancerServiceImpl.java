package com.cloud.awswebservice.service;

import com.amazonaws.services.ec2.model.InstanceStateName;
import com.cloud.awswebservice.constants.Constants;
import com.cloud.awswebservice.repo.EC2Repository;
import com.cloud.awswebservice.repo.SQSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoadBalancerServiceImpl implements LoadBalancerService {

  @Autowired
  private EC2Repository ec2Repository;

  @Autowired
  private SQSRepository sqsRepository;

  public void scaleInOrOut() {
    String queueUrl = sqsRepository.createQueue(Constants.inputQueueTopic);
    while (true) {
      Integer noOfMessages = sqsRepository.getNumberOfMessages(queueUrl);
      Integer noOfRunningInstances = (int) ec2Repository.getAllInstanceStatus()
          .stream()
          .filter(
              instanceStatus -> instanceStatus.getInstanceState()
                  .getName().equals(InstanceStateName.Running.toString())
                  || instanceStatus.getInstanceState()
                  .getName()
                  .equals(InstanceStateName.Pending.toString())
          )
          .count();
      Integer noOfRunningAppInstances = noOfRunningInstances - 1;
      if (noOfMessages > 0 && noOfMessages > noOfRunningAppInstances) {
        Integer reqNewInstances = noOfMessages - noOfRunningAppInstances;
        Integer avlNewInstances = Constants.maxTotalInstances - noOfRunningAppInstances;
        if (avlNewInstances > 0) {
          ec2Repository.createInstance(
              Constants.EC2KeyName,
              Constants.EC2SecurityGroup,
              Constants.EC2ImageId,
              Constants.EC2InstanceType,
              Integer.min(avlNewInstances, reqNewInstances)
          );
        }
      }

      try {
        Thread.sleep(Constants.scaleInOutSleepTime);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
