package com.cloud.awsappservice.repo;

import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.cloud.awsappservice.config.AwsConfig;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SQSRepositoryImpl implements SQSRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(SQSRepositoryImpl.class);

  @Autowired
  private AwsConfig awsConfig;

  @Override
  public String getQueue(String queueName) {
    String queueUrl = null;
    try {
      queueUrl = awsConfig.amazonSQS().getQueueUrl(queueName).getQueueUrl();
    } catch (QueueDoesNotExistException queueDoesNotExistException) {
      queueDoesNotExistException.printStackTrace();
    }
    return queueUrl;
  }

  @Override
  public String createQueue(String queueName) {
    String queueUrl = getQueue(queueName);
    if (queueUrl!=null) {
      LOGGER.debug("Using existing SQS queue");
      return queueUrl;
    }
    LOGGER.info("Creating the SQS queue");
    CreateQueueRequest create_request = new CreateQueueRequest().withQueueName(queueName);
    try {
      queueUrl = awsConfig.amazonSQS().createQueue(create_request).getQueueUrl();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return queueUrl;
  }

  @Override
  public void sendMessage(String queueUrl, String messageBody, Map<String, MessageAttributeValue> messageAttributes) {
    LOGGER.debug("Sending the message to SQS Queue");
    SendMessageRequest request = new SendMessageRequest()
        .withQueueUrl(queueUrl)
        .withMessageBody(messageBody)
        .withMessageAttributes(messageAttributes)
        .withDelaySeconds(0);
    try {
      awsConfig.amazonSQS().sendMessage(request);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public Message receiveMessage(String queueUrl, Integer visibilityTimeout) {
    LOGGER.debug("Receiving the message from SQS Queue");
    Message message = null;
    ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
        .withMaxNumberOfMessages(1)
        .withVisibilityTimeout(visibilityTimeout);
    try {
      List<Message> messages = awsConfig.amazonSQS().receiveMessage(receiveMessageRequest).getMessages();
      if (!messages.isEmpty()) {
        message = messages.get(0);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return message;
  }

  @Override
  public void deleteMessage(String queueUrl, Message message) {
    LOGGER.debug("Deleting the message from SQS Queue");
    DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest(queueUrl, message.getReceiptHandle());
    try {
      awsConfig.amazonSQS().deleteMessage(deleteMessageRequest);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
