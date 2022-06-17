package com.cloud.awswebservice.repo;

import com.amazonaws.services.sqs.model.ChangeMessageVisibilityBatchRequestEntry;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.cloud.awswebservice.config.AwsConfig;
import com.cloud.awswebservice.constants.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SQSRepositoryImpl implements SQSRepository {

  @Autowired
  private AwsConfig awsConfig;

  public String getQueue(String queueName) {
    String queueUrl = null;
    try {
      queueUrl = awsConfig.amazonSQS().getQueueUrl(queueName).getQueueUrl();
    } catch (QueueDoesNotExistException queueDoesNotExistException) {
      queueDoesNotExistException.printStackTrace();
    }
    return queueUrl;
  }

  public String createQueue(String queueName) {
    String queueUrl = getQueue(queueName);
    if (queueUrl!=null) return queueUrl;
    CreateQueueRequest create_request = new CreateQueueRequest().withQueueName(queueName);
    try {
      queueUrl = awsConfig.amazonSQS().createQueue(create_request).getQueueUrl();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return queueUrl;
  }

  public void deleteQueue(String queueUrl) {
    try {
      awsConfig.amazonSQS().deleteQueue(queueUrl);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void sendMessage(String queueUrl, String messageBody) {
    SendMessageRequest request = new SendMessageRequest()
        .withQueueUrl(queueUrl)
        .withMessageBody(messageBody)
        .withDelaySeconds(0);
    try {
      awsConfig.amazonSQS().sendMessage(request);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public List<Message> receiveMessages(String queueUrl, Integer visibilityTimeout) {
    List<Message> messages = new ArrayList<>();
    ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl).withMessageAttributeNames("All");
    try {
      messages = awsConfig.amazonSQS().receiveMessage(receiveMessageRequest).getMessages();
      if (!messages.isEmpty()) {
        Integer requestId = 0;
        List<ChangeMessageVisibilityBatchRequestEntry> entries = new ArrayList<>();
        for (Message message: messages) {
          entries.add(new ChangeMessageVisibilityBatchRequestEntry(
                Constants.visibilityBatchRequestIdPrefix + requestId.toString(),
                message.getReceiptHandle()
              ).withVisibilityTimeout(visibilityTimeout));
        }
        awsConfig.amazonSQS().changeMessageVisibilityBatch(queueUrl, entries);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return messages;
  }

  public void deleteMessage(String queueUrl, Message message) {
    DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest(queueUrl, message.getReceiptHandle());
    try {
      awsConfig.amazonSQS().deleteMessage(deleteMessageRequest);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Integer getNumberOfMessages(String queueUrl) {
    Integer noOfMessage = null;
    List<String> attibuteNames = new ArrayList<>();
    attibuteNames.add("ApproximateNumberOfMessages");
    try {
      GetQueueAttributesRequest getQueueAttributesRequest = new GetQueueAttributesRequest(queueUrl, attibuteNames);
      Map attributes = awsConfig.amazonSQS().getQueueAttributes(getQueueAttributesRequest).getAttributes();
      noOfMessage =  Integer.valueOf((String)attributes.get("ApproximateNumberOfMessages"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return noOfMessage;
  }
}
