package com.qc.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qc.spring.configs.Configuration;
import com.qc.spring.utils.Encrypter;


/**
 * The Class HomeController.
 */
@Controller
public class HomeController {    
  
    
	/** The env. */
	@Autowired
    Configuration env;

    /**
     * Aes enc.
     *
     * @return the string
     */
    @RequestMapping(value = "/enc", method = RequestMethod.GET)
    public String aesEnc() {
    	
       return "AESTest";
    }
    
    /**
     * Aes enc decr.
     *
     * @return the string
     */
    @RequestMapping(value = "/encrypt", method = RequestMethod.GET)
    public String aesEncDecr() {
    	
       return "encrypt";
    }
    
    /**
     * Aes decr.
     *
     * @return the string
     */
    @RequestMapping(value = "/decrypt", method = RequestMethod.GET)
    public String aesDecr() {
    	
       return "decrypt";
    }
    
    /**
     * Test.
     *
     * @return the string
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
    	
       return "404";
    }
    
    /**
     * Aes enc test.
     *
     * @param t1 the t1
     * @param b1 the b1
     * @param model the model
     * @return the string
     * @throws Exception the exception
     */
    @RequestMapping(value = "/encTest", method = RequestMethod.POST)
    public String aesEncTest(@RequestParam("t1") String t1, @RequestParam("b1") String b1, Model model) throws Exception {
    	
    	String result = null;
    	String key = env.get("AES_KEY");

    	if( b1.equals("Encrypt")){
    		result = Encrypter.encrypt(t1, key);
    	}
    	else if(b1.equals("Decrypt")){
    		result = Encrypter.decrypt(t1, key);
    	}
    	model.addAttribute("text", result);
       return "AESTest";
    }
    
    /**
     * Aes encrypt.
     *
     * @param t1 the t1
     * @param model the model
     * @return the string
     * @throws Exception the exception
     */
    @ResponseBody
    @RequestMapping(value = "/encrypt", method = RequestMethod.POST)
    public String aesEncrypt(@RequestParam("t1") String t1, Model model) throws Exception {
    	
    	String result = null;
    	String key = env.get("AES_KEY");

    	result = Encrypter.encrypt(t1, key);
       return result;
    }
    
    /**
     * Aes decrypt.
     *
     * @param t1 the t1
     * @param model the model
     * @return the string
     * @throws Exception the exception
     */
    @ResponseBody
    @RequestMapping(value = "/decrypt", method = RequestMethod.POST)
    public String aesDecrypt(@RequestParam("t1") String t1, Model model) throws Exception {
    	
    	String result = null;
    	String key = env.get("AES_KEY");

    	result = Encrypter.decrypt(t1, key);
       return result;
    }
}