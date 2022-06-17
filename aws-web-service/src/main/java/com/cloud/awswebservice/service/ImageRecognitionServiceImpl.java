package com.cloud.awswebservice.service;

import com.amazonaws.services.sqs.model.Message;
import com.cloud.awswebservice.constants.Constants;
import com.cloud.awswebservice.repo.S3RepositoryImpl;
import com.cloud.awswebservice.repo.SQSRepository;
import com.cloud.awswebservice.repo.SQSRepositoryImpl;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thavam.util.concurrent.blockingMap.BlockingHashMap;

@Service
public class ImageRecognitionServiceImpl implements ImageRecognitionService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ImageRecognitionServiceImpl.class);

  @Autowired
  private S3RepositoryImpl s3Repository;

  @Autowired
  private SQSRepository sqsRepository;

  private static BlockingHashMap<String, String> recognitionResults = new BlockingHashMap<>();

  public String recognizeImage(MultipartFile image) {
    LOGGER.debug("Received image for recognition: " + image.getOriginalFilename());
    String output = null;
    s3Repository.uploadFile(Constants.imageBucketName, image);
    sqsRepository.sendMessage(Constants.inputQueueTopic, image.getOriginalFilename());
    try {
      output = recognitionResults.take(image.getOriginalFilename());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return output;
  }

  public void receiveOutput() {
    String queueUrl = sqsRepository.createQueue(Constants.outputQueueTopic);
    while (true) {
      try {
        List<Message> messageList = sqsRepository.receiveMessages(queueUrl, Constants.visibilityTimeout);
        for (Message message: messageList) {
          String key = message.getMessageAttributes().get(Constants.outputAttributeKey).getStringValue();
          String value = message.getBody();
          recognitionResults.offer(key, value);
          sqsRepository.deleteMessage(queueUrl, message);
        }
        Thread.sleep(Constants.outputSleepTime);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
