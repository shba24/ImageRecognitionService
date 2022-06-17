package com.cloud.awswebservice.repo;

import com.amazonaws.services.s3.model.ListVersionsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.VersionListing;
import com.cloud.awswebservice.config.AwsConfig;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class S3RepositoryImpl implements S3Repository {

  private static final Logger LOGGER = LoggerFactory.getLogger(S3RepositoryImpl.class);

  @Autowired
  private AwsConfig awsConfig;

  public void createBucket(String bucketName) {
    if (!awsConfig.amazonS3().doesBucketExistV2(bucketName)) {
      try {
        LOGGER.info("Creating S3 Bucket");
        awsConfig.amazonS3().createBucket(bucketName);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void deleteBucket(String bucketName) {
    LOGGER.info("Deleting S3 Bucket");
    try {
      // Need to delete objects first before deleting buckets
      ObjectListing object_listing = awsConfig.amazonS3().listObjects(bucketName);
      while (true) {
        for (Iterator<?> iterator =
             object_listing.getObjectSummaries().iterator();
             iterator.hasNext(); ) {
          S3ObjectSummary summary = (S3ObjectSummary) iterator.next();
          awsConfig.amazonS3().deleteObject(bucketName, summary.getKey());
        }

        // more object_listing to retrieve?
        if (object_listing.isTruncated()) {
          object_listing = awsConfig.amazonS3().listNextBatchOfObjects(object_listing);
        } else {
          break;
        }
      }

      // Need to remove the versions of buckets before removing bucket
      VersionListing version_listing = awsConfig.amazonS3().listVersions(
          new ListVersionsRequest().withBucketName(bucketName));
      while (true) {
        for (Iterator<?> iterator =
             version_listing.getVersionSummaries().iterator();
             iterator.hasNext(); ) {
          S3VersionSummary vs = (S3VersionSummary) iterator.next();
          awsConfig.amazonS3().deleteVersion(
              bucketName, vs.getKey(), vs.getVersionId());
        }

        if (version_listing.isTruncated()) {
          version_listing = awsConfig.amazonS3().listNextBatchOfVersions(
              version_listing);
        } else {
          break;
        }
      }
      awsConfig.amazonS3().deleteBucket(bucketName);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void uploadFile(String bucketName, MultipartFile multipartFile) {
    LOGGER.debug("Uploading a file to S3 Bucket");
    createBucket(bucketName);
    ObjectMetadata data = new ObjectMetadata();
    data.setContentType(multipartFile.getContentType());
    data.setContentLength(multipartFile.getSize());
    try {
      awsConfig.amazonS3().putObject(
          bucketName,
          multipartFile.getOriginalFilename(),
          multipartFile.getInputStream(),
          data);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
