package com.cloud.awswebservice.repo;

import org.springframework.web.multipart.MultipartFile;

public interface S3Repository {
  void createBucket(String bucketName);
  void deleteBucket(String bucketName);
  void uploadFile(String bucketName, MultipartFile multipartFile);
}
