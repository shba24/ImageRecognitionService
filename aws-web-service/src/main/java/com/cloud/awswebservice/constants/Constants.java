package com.cloud.awswebservice.constants;

import com.amazonaws.regions.Regions;

public class Constants {
  public static final String accessKey = "";
  public static final String secretKey = "";
  public static final Regions region = Regions.US_EAST_1;
  public static final String EC2KeyName = "shubham.bansal";
  public static final String EC2SecurityGroup = "sg-07b96c6852408efd0";
  public static final String EC2ImageId = "ami-0c12212910aaf2cff";
  public static final String EC2InstanceType = "t2.micro";
  public static final Integer visibilityTimeout = 100;
  public static final String inputQueueTopic = "inputqueue";
  public static final String outputQueueTopic = "outputqueue";
  public static final Integer maxTotalInstances = 20;
  public static final String imageBucketName = "cse546-imagebucket";
  public static final Integer scaleInOutSleepTime = 50;
  public static final Integer outputSleepTime = 10;
  public static final String visibilityBatchRequestIdPrefix = "unique_id_msg";
  public static final String outputAttributeKey = "imageid";
}
