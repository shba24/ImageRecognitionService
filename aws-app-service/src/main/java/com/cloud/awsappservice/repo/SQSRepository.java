package com.cloud.awsappservice.repo;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import java.util.Map;

public interface SQSRepository {
  String getQueue(String queueName);
  String createQueue(String queueName);
  void sendMessage(String queueUrl, String messageBody, Map<String, MessageAttributeValue> messageAttributes);
  Message receiveMessage(String queueUrl, Integer visibilityTimeout);
  void deleteMessage(String queueUrl, Message message);
}
