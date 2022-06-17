package com.cloud.awswebservice.constants;

import com.amazonaws.regions.Regions;

public class Constants {
  public static final String accessKey = "AKIAVPMB4KPNQN4VJRU5";
  public static final String secretKey = "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";
  public static final Regions region = Regions.US_EAST_1;
  public static final String EC2KeyName = "shubham.bansal";
  public static final String EC2SecurityGroup = "sg-07b96c6852408efd0";
  public static final String EC2ImageId = "ami-0b091047cf17e580d";
  public static final String EC2InstanceType = "t2.micro";
  public static final Integer visibilityTimeout = 100;
  public static final String inputQueueTopic = "inputqueue";
  public static final String outputQueueTopic = "outputqueue";
  public static final Integer maxTotalInstances = 20;
  public static final String imageBucketName = "cse546-imagebucket";
  public static final Integer scaleInOutSleepTime = 50;
  public static final Integer outputSleepTime = 50;
  public static final String visibilityBatchRequestIdPrefix = "unique_id_msg";
  public static final String outputAttributeKey = "imageid";
}
