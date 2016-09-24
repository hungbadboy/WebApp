package com.saatya.ws.common;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import org.apache.commons.logging.Log;

/**
 * @author vbongoni
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class BaseException extends Exception
{

	private Throwable throwable;
	
	private String userMessageKey;
	
	private Serializable[] valueReplacementArray;
	
	private String uniqueID;
	
	private ErrorLevel errorLevel;
	
	private boolean logged;
	
	/**
	 * @param aThrowable
	 * @param aUserMessage
	 * @param level
	 */
	public BaseException(Throwable aThrowable, String aUserMessageKey, Serializable[] aValueReplacementArray, ErrorLevel level)
	{
		this.throwable = aThrowable;
		this.userMessageKey = aUserMessageKey;
		this.valueReplacementArray = aValueReplacementArray;
		this.errorLevel = level;
		this.uniqueID = new Double(Math.random()).toString();
	}
	
	public void log(Log log)
	{
		if (errorLevel.equals(ErrorLevel.INFO)&& log.isDebugEnabled())
		{
			log.debug("Info Message: ID - " + uniqueID + " User Message: " + userMessageKey);
			log.debug(StackTracer.getStackTrace(throwable));
		}
		else if ( errorLevel.equals(ErrorLevel.WARNING)&& log.isWarnEnabled())
		{
			log.warn("Warn Message: ID - " + uniqueID + " User Message: " + userMessageKey);
			log.warn(StackTracer.getStackTrace(throwable));
		}
		else if ( errorLevel.equals(ErrorLevel.ERROR)&& log.isErrorEnabled())
		{
			log.error("Error Message: ID - " + uniqueID + " User Message: " + userMessageKey);
			log.error(StackTracer.getStackTrace(throwable));
		}
		else if ( errorLevel.equals(ErrorLevel.FATAL)&& log.isFatalEnabled())
		{
			log.fatal("Fatal Message: ID - " + uniqueID + " User Message: " + userMessageKey);
			log.fatal(StackTracer.getStackTrace(throwable));
		}
		logged = true;
	}
	
	public boolean isLogged()
	{
		return logged;
	}

	private static class StackTracer 
	{
		static String getStackTrace(Throwable exception)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.print(" [ " );
			pw.print(exception.getClass().getName());
			pw.print(" ] ");
			exception.printStackTrace(pw);
			return sw.toString();
		}
	}	

	/**
	 * @return
	 */
	public String getUserMessageKey()
	{
		return userMessageKey;
	}

	/**
	 * @return
	 */
	public String getUniqueID()
	{
		return uniqueID;
	}

	/**
	 * @return
	 */
	public Serializable[] getValueReplacementArray()
	{
		return valueReplacementArray;
	}

	public String toString()
	{
		return getClass().getName() + "[ UniqueID=" + uniqueID + 
			", UserMessage Key=" + userMessageKey + "]" + "\n\n" +
			"Nested Exception=" + throwable.getMessage();
	}

}