package com.qc.spring.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.org.apache.xml.internal.security.utils.Base64;


/**
 * The Class Encrypter.
 */
public class Encrypter {
	
	/** The LOGGER. */
	private static Log LOGGER = LogFactory.getLog(Encrypter.class);

	/** The decrypted text bytes. */
	private static byte[] decryptedTextBytes;

	/**
	 * Encrypt.
	 *
	 * @param plainText the plain text
	 * @param key the key
	 * @return the string
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws NoSuchPaddingException the no such padding exception
	 * @throws InvalidKeyException the invalid key exception
	 * @throws IllegalBlockSizeException the illegal block size exception
	 * @throws BadPaddingException the bad padding exception
	 * @throws InvalidAlgorithmParameterException the invalid algorithm parameter exception
	 */
	public static String encrypt(String plainText, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

		String en = "";
		try {

			Cipher cipher = Cipher.getInstance("AES");

			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Base64.decode("kEyLI1Fy648tzWXGuRcxrg=="), "AES"));
			en = Base64.encode(cipher.doFinal(plainText.getBytes("UTF-8")));

		} catch (InvalidKeyException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage(), e);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage(), e);
		
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage(), e);
		
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage(), e);
	
		} catch (BadPaddingException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage(), e);
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage(), e);
		}

		return en;
	}

	/**
	 * Decrypt.
	 *
	 * @param encryptedText the encrypted text
	 * @param key the key
	 * @return the string
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws NoSuchPaddingException the no such padding exception
	 * @throws InvalidKeyException the invalid key exception
	 * @throws IllegalBlockSizeException the illegal block size exception
	 * @throws BadPaddingException the bad padding exception
	 * @throws InvalidAlgorithmParameterException the invalid algorithm parameter exception
	 */
	public static String decrypt(String encryptedText, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

		if(!encryptedText.equals("")){
			try {
	
				Cipher cipher = Cipher.getInstance("AES");
				cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Base64.decode("kEyLI1Fy648tzWXGuRcxrg=="), "AES"));
				decryptedTextBytes = cipher.doFinal(Base64.decode(encryptedText.getBytes()));
				
			} catch (InvalidKeyException e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage(), e);
			
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage(), e);
			
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage(), e);
			
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage(), e);
			
			} catch (BadPaddingException e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage(), e);
			
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage(), e);
			
			}
		}
		else{
			return null;
		}
		return new String(decryptedTextBytes);
	}

}
