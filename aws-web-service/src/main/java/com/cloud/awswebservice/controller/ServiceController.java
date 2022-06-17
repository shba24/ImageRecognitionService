package com.cloud.awswebservice.controller;

import com.cloud.awswebservice.service.ImageRecognitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@EnableAutoConfiguration
@RequestMapping("/")
public class ServiceController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ServiceController.class);

  @Autowired
  private ImageRecognitionService imageRecognitionService;

  @PostMapping(value = "recognise", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> inputUrl(@RequestParam("myfile")MultipartFile image) {
    LOGGER.debug("Received HTTP Request from the user");
    String output = imageRecognitionService.recognizeImage(image);
    return new ResponseEntity<String>(output, HttpStatus.OK);
  }
}
