package com.cloud.awswebservice.repo;

import com.amazonaws.services.sqs.model.Message;
import java.util.List;

public interface SQSRepository {
  String getQueue(String queueName);
  String createQueue(String queueName);
  void deleteQueue(String queueUrl);
  void sendMessage(String queueUrl, String messageBody);
  List<Message> receiveMessages(String queueUrl, Integer visibilityTimeout);
  void deleteMessage(String queueUrl, Message message);
  Integer getNumberOfMessages(String queueUrl);
}
