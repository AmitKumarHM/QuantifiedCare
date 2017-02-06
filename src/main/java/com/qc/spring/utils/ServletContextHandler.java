package com.qc.spring.utils;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * The Class ServletContextHandler.
 */
public class ServletContextHandler implements ServletContextListener {
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
		
		MyServletContext.setContextPath(contextEvent.getServletContext().getRealPath("resources")+File.separator+"logs"+File.separator);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}
}
