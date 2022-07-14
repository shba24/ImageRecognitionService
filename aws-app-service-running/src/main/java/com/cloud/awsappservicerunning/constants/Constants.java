package com.cloud.awsappservicerunning.constants;

import com.amazonaws.regions.Regions;

public class Constants {
  public static final String accessKey = "";
  public static final String secretKey = "";
  public static final Regions region = Regions.US_EAST_1;
  public static final Integer visibilityTimeout = 100;
  public static final String inputQueueTopic = "inputqueue";
  public static final String outputQueueTopic = "outputqueue";
  public static final String imageBucketName = "cse546-imagebucket";
  public static final String outputBucketName = "cse546-outputbucket";
  public static final Integer outputSleepTime = 100;
  public static final String outputAttributeKey = "imageid";
}
