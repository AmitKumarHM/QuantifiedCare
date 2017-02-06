package com.qc.spring.utils;

import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;



/**
 * The Class S3Save.
 */
public class S3Save {

	/** The s3. */
	AmazonS3 s3 = null;
	
	/** The ap ne. */
	Region apNE = null;
/*	private static Logger logger = Logger.getLogger(S3Save.class.getName());
	private static FileHandler fh;
	static {
		try {
			fh = new FileHandler("Log/S3Save.log");
			logger.addHandler(fh);
		} catch (Exception e) {
			logger.log(Level.INFO, "Log file couldn't be created.");
			e.printStackTrace();
		}
	}*/
	/**
 * Instantiates a new s3 save.
 *
 * @param strCredentialFilePath the str credential file path
 * @param rgRegion the rg region
 */
public S3Save(String strCredentialFilePath, Regions rgRegion) {

		try {	
			
			
			ClientConfiguration cc = new ClientConfiguration();
			cc.setMaxErrorRetry(5);
			cc.setConnectionTimeout(300000);
			
			s3 = new AmazonS3Client(
					new ClasspathPropertiesFileCredentialsProvider(strCredentialFilePath), cc);
			
			apNE = Region.getRegion(rgRegion);
			s3.setRegion(apNE);
		} catch (IllegalArgumentException e) {
			//logger.log(Level.SEVERE, "S3 connection error " + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * Instantiates a new s3 save.
	 */
	public S3Save() {

		try {
			s3 = new AmazonS3Client(
					new ClasspathPropertiesFileCredentialsProvider());
			apNE = Region.getRegion(Regions.US_EAST_1);
			s3.setRegion(apNE);
		} catch (IllegalArgumentException e) {
			//logger.log(Level.SEVERE, "S3 connection error " + e.getMessage());
			e.printStackTrace();
		}

	}
	
	
	/**
	 * Save.
	 *
	 * @param strBucket the str bucket
	 * @param strKey the str key
	 * @param flUpload the fl upload
	 * @return true, if successful
	 */
	public boolean Save(String strBucket, String strKey, File flUpload) {

		boolean blRet=false;
		
		try {
			
			PutObjectRequest putObj = new PutObjectRequest(strBucket, strKey,flUpload);
			putObj.setCannedAcl(CannedAccessControlList.PublicRead);
			s3.putObject(putObj);
			blRet=true;
			
			//logger.log(Level.INFO, flUpload.getName() + " Uploaded successfully on S3");
		} catch (AmazonServiceException e) {
			//logger.log(Level.SEVERE, "S3 Caught an AmazonServiceException " + e.getMessage());
			e.printStackTrace();
		} catch (AmazonClientException e) {
			//logger.log(Level.SEVERE, "S3 Caught an AmazonClientException " + e.getMessage());
			e.printStackTrace();
		}

		return blRet;
	}

}
