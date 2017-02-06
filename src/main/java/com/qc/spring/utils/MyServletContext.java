package com.qc.spring.utils;


/**
 * The Class MyServletContext.
 */
public class MyServletContext {

	/** The context path. */
	private static String   contextPath;

    /**
     * Instantiates a new my servlet context.
     */
    private MyServletContext() {
    }

    /**
     * Gets the context path.
     *
     * @return the context path
     */
    public static String getContextPath() {
        return contextPath;
    }

    /**
     * Sets the context path.
     *
     * @param cp the new context path
     */
    public static void setContextPath(String cp) {
        contextPath = cp;
    }
}
