package com.qc.spring.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.qc.spring.configs.Configuration;
import com.qc.spring.entities.Users;


/**
 * The Class Mail.
 */
public class Mail {

	/** The env. */
	@Autowired
	Configuration env;

	/** The instance. */
	private static Mail instance = null;

	/**
	 * Instantiates a new mail.
	 */
	private Mail() {
	}

	/**
	 * Gets the single instance of Mail.
	 *
	 * @return single instance of Mail
	 */
	public static Mail getInstance() {
		if (instance == null) {
			instance = new Mail();
		}
		return instance;
	}

	public static String sendForgotPasswordMail(Users usr, String a_key, String s_key, String sender, String subject, String[] body, String[] restBody, String url, String uri) {
		
		SendEmailResult result = null;
		List<String> receiver = new ArrayList<String>();
		receiver.add(usr.getEmailId());
		String mailBody=null;
		Destination destination = new Destination(receiver);
		
		
		 try {
			 mailBody = "Hi " + usr.getFirstName() + "," + body[0]+ ", " +body[1]+", "+body[2]+ url +uri + usr.getForgetToken()+"/"+  usr.getUserId() + restBody[0]+", "+restBody[1]+", "+restBody[2]+", "+restBody[3]+", "+restBody[4]+", "+restBody[5];
			            Content subjectContent = new Content(subject);
			            Content bodyContent = new Content(mailBody);
			            Body msgBody = new Body(bodyContent);
			            Message msg = new Message(subjectContent, msgBody);

			            SendEmailRequest request = new SendEmailRequest(sender, destination, msg);
			            
			            AWSCredentials credentials = new BasicAWSCredentials(a_key, s_key);
			            AmazonSimpleEmailServiceClient sesClient = new AmazonSimpleEmailServiceClient(credentials);
			            result = sesClient.sendEmail(request);
			           
			   
			        }catch(Exception e) {
			            System.out.println("Exception from EmailSender.java. Email not send" + e);
			        }	
		
		/*try {
			body = body + url + uri + usr.getForgetToken() + "/" + usr.getUserId() + restBody;
            Content subjectContent = new Content(subject);
            Content bodyContent = new Content(body);
            Body msgBody = new Body(bodyContent);
            Message msg = new Message(subjectContent, msgBody);

            SendEmailRequest request = new SendEmailRequest(sender, destination, msg);
            
            AWSCredentials credentials = new BasicAWSCredentials(a_key, s_key);
            AmazonSimpleEmailServiceClient sesClient = new AmazonSimpleEmailServiceClient(credentials);
            result = sesClient.sendEmail(request);
            
            System.out.println(result + "Email sent");
            
            *//********This commented code you can use to send HTML email************************//*
			Content subjContent = new Content().withData("Test of Amazon SES");
			Message msg = new Message().withSubject(subjContent);

			// Include a body in both text and HTML formats
			Content textContent = new Content().withData("Hello - I hope you're having a good day.");
			Content htmlContent = new Content().withData("<h1>Hello - I hope you're having a good day.</h1>");
			Body body = new Body().withHtml(htmlContent).withText(textContent);
			msg.setBody(body);  
			
        }catch(Exception e) {
            System.out.println("Exception from EmailSender.java. Email not send" + e);
        }*/
		
		return result.getMessageId();
	}
	
public static String sendEmailForChangeEmail(Users usr, String a_key, String s_key, String sender, String subject, String body, String restBody, String url, String uri) {
		
		SendEmailResult result = null;
		List<String> receiver = new ArrayList<String>();
		receiver.add(usr.getNewEmail());
		
		Destination destination = new Destination(receiver);
		
		try {
			body = body + url + uri + usr.getEmailToken() + "/" + usr.getUserId() + restBody;
            Content subjectContent = new Content(subject);
            Content bodyContent = new Content(body);
            Body msgBody = new Body(bodyContent);
            Message msg = new Message(subjectContent, msgBody);

            SendEmailRequest request = new SendEmailRequest(sender, destination, msg);
            
            AWSCredentials credentials = new BasicAWSCredentials(a_key, s_key);
            AmazonSimpleEmailServiceClient sesClient = new AmazonSimpleEmailServiceClient(credentials);
            result = sesClient.sendEmail(request);
            
            System.out.println(result + "Email sent");
            
            /********This commented code you can use to send HTML email************************/
			/*Content subjContent = new Content().withData("Test of Amazon SES");
			Message msg = new Message().withSubject(subjContent);

			// Include a body in both text and HTML formats
			Content textContent = new Content().withData("Hello - I hope you're having a good day.");
			Content htmlContent = new Content().withData("<h1>Hello - I hope you're having a good day.</h1>");
			Body body = new Body().withHtml(htmlContent).withText(textContent);
			msg.setBody(body);  */
			
        }catch(Exception e) {
            System.out.println("Exception from EmailSender.java. Email not send" + e);
        }
		
		return result.getMessageId();
	}
	
}
