package com.ssp.Service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3ServiceImpl implements IS3Service {

	@Autowired
	private S3Client s3client;

	@Value("${spring.cloud.aws.s3.bucket}")
	private String bucketName;

	@Value("${spring.cloud.aws.region.static}")
	private String region;
	
	@Override
	public String uploadFile(MultipartFile file) throws IOException {

		String fileName = "testing/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

		s3client.putObject(
				PutObjectRequest.builder().bucket(bucketName).key(fileName).contentType(file.getContentType()).build(),
				RequestBody.fromBytes(file.getBytes()));

		return "https://"+bucketName+".s3."+region+".amazonaws.com/" + fileName;
	}
}
