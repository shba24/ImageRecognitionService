package com.cloud.awsappservice.service;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.cloud.awsappservice.constants.Constants;
import com.cloud.awsappservice.repo.EC2Repository;
import com.cloud.awsappservice.repo.S3Repository;
import com.cloud.awsappservice.repo.SQSRepository;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeepLearningServiceImpl implements DeepLearningService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DeepLearningServiceImpl.class);

  @Autowired
  private SQSRepository sqsRepository;

  @Autowired
  private S3Repository s3Repository;

  @Autowired
  private EC2Repository ec2Repository;

  private String runDeepLearningAlgorithm(String imagePath) {
    LOGGER.debug("Running the deep learning model");
    String output = "NA";
    Process p;
    try {
      p = new ProcessBuilder("/bin/bash", "-c",
          "python3 /home/ubuntu/classifier/image_classification.py " + imagePath).start();
      p.waitFor();
      BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String temp = br.readLine();
      if (temp != null) {
        String[] tokens = temp.split(",");
        if (tokens.length > 1) {
          output = tokens[1];
        }
      }
      p.destroy();
      Files.deleteIfExists(Path.of(imagePath));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return output;
  }

  @Override
  public void recognizeImage() {
    String inputQueueUrl = sqsRepository.createQueue(Constants.inputQueueTopic);
    String outputQueueUrl = sqsRepository.createQueue(Constants.outputQueueTopic);
    while (true) {
      Message message = sqsRepository.receiveMessage(inputQueueUrl, Constants.visibilityTimeout);
      if (message==null) break;
      String imageName = message.getBody();
      String imagePath = s3Repository.downloadObject(Constants.imageBucketName, imageName);
      String prediction = runDeepLearningAlgorithm(imagePath);
      Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
      messageAttributes.put(Constants.outputAttributeKey, new MessageAttributeValue()
          .withStringValue(imageName)
          .withDataType("String"));
      s3Repository.uploadObject(Constants.outputBucketName, imageName, prediction);
      sqsRepository.sendMessage(outputQueueUrl, prediction, messageAttributes);
      sqsRepository.deleteMessage(inputQueueUrl, message);
      try {
        Thread.sleep(Constants.outputSleepTime);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    ec2Repository.terminateInstance();
  }
}
