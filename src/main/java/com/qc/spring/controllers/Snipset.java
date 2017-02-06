package com.qc.spring.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;

public class Snipset {

    public static void main(String[] args) throws IOException {    	
              
    	List<String> list = new ArrayList<String>();
    	String sender = "wordpress@quantifiedcare.com";
    	list.add("ajay.gupta@kiwitech.com");
    	list.add("akg.mca66@gmail.com");
    	
    	String subject = "Amazon SES test (AWS SDK for Java)";
    	String body = "This email was sent through Amazon SES by using the AWS SDK for Java.";
    	Destination destination = new Destination(list);
    	
    	try {
            String ACCESS_KEY = "AKIAIVG3YYDUT4A3G4PA";
            String SECRET_KEY = "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";

            Content subjectContent = new Content(subject);
            Content bodyContent = new Content(body);
            Body msgBody = new Body(bodyContent);
            Message msg = new Message(subjectContent, msgBody);

            SendEmailRequest request = new SendEmailRequest(sender, destination, msg);

            AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
            AmazonSimpleEmailServiceClient sesClient = new AmazonSimpleEmailServiceClient(credentials);
            SendEmailResult result = sesClient.sendEmail(request);

            System.out.println(result + "Email sent");  
        }catch(Exception e) {
            System.out.println("Exception from EmailSender.java. Email not send" + e);
        }
    }
}
