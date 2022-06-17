package com.cloud.awsappservicerunning.repo;

public interface S3Repository {
  void createBucket(String bucketName);
  String downloadObject(String bucketName, String key);
  void uploadObject(String bucketName, String key, String value);
}
