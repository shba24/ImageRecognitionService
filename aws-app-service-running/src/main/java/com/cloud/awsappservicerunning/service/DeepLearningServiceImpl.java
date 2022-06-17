package com.cloud.awsappservicerunning.service;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.cloud.awsappservicerunning.constants.Constants;
import com.cloud.awsappservicerunning.repo.S3Repository;
import com.cloud.awsappservicerunning.repo.SQSRepository;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeepLearningServiceImpl implements DeepLearningService {

  @Autowired
  private SQSRepository sqsRepository;

  @Autowired
  private S3Repository s3Repository;

  private String runDeepLearningAlgorithm(String imagePath) {
    String output = "NA";
    Process p;
    try {
      p = new ProcessBuilder("/bin/bash", "-c",
          "python3 /home/ubuntu/classifier/image_classification.py " + imagePath).start();
      p.waitFor();
      BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String temp = br.readLine();
      if (temp!=null) {
        output = temp.split(",")[1];
      }
      Files.deleteIfExists(Path.of(imagePath));
      p.destroy();
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
      if (message==null) continue;
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
  }
}
