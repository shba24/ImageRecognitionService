package com.cloud.awsappservice.repo;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.cloud.awsappservice.config.AwsConfig;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class S3RepositoryImpl implements S3Repository {

  private static final Logger LOGGER = LoggerFactory.getLogger(S3RepositoryImpl.class);

  @Autowired
  private AwsConfig awsConfig;

  @Override
  public void createBucket(String bucketName) {
    if (!awsConfig.amazonS3().doesBucketExistV2(bucketName)) {
      try {
        LOGGER.info("Creating S3 bucket");
        awsConfig.amazonS3().createBucket(bucketName);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public String downloadObject(String bucketName, String key) {
    String filePath = null;
    createBucket(bucketName);
    try {
      LOGGER.debug("Downloading object from S3 bucket");
      S3Object imageObject = awsConfig.amazonS3().getObject(
          bucketName,
          key);
      if (imageObject != null) {
        byte[] data = imageObject.getObjectContent().readAllBytes();
        imageObject.close();
        String tempPath = "/tmp/" + key;
        Files.write(Paths.get(tempPath), data);
        filePath = tempPath;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return filePath;
  }

  @Override
  public void uploadObject(String bucketName, String key, String value) {
    createBucket(bucketName);
    try {
      byte[] content = value.getBytes(StandardCharsets.UTF_8);
      ObjectMetadata data = new ObjectMetadata();
      data.setContentLength(content.length);
      LOGGER.debug("Inserting object into S3 bucket");
      awsConfig.amazonS3().putObject(
          bucketName,
          key,
          new ByteArrayInputStream(content),
          data);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
