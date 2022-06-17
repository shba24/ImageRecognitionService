package com.cloud.awswebservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageRecognitionService {
  String recognizeImage(MultipartFile image);
  void receiveOutput();
}
