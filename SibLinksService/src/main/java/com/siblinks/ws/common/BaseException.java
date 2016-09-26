/*
 * Copyright (c) 2016-2017, Tinhvan Outsourcing JSC. All rights reserved.
 *
 * No permission to use, copy, modify and distribute this software
 * and its documentation for any purpose is granted.
 * This software is provided under applicable license agreement only.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.siblinks.ws.common;

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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	public BaseException(final Throwable aThrowable, final String aUserMessageKey, final Serializable[] aValueReplacementArray, final ErrorLevel level)
	{
		this.throwable = aThrowable;
		this.userMessageKey = aUserMessageKey;
		this.valueReplacementArray = aValueReplacementArray;
		this.errorLevel = level;
		this.uniqueID = new Double(Math.random()).toString();
	}
	
	public void log(final Log log)
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
		static String getStackTrace(final Throwable exception)
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

	@Override
    public String toString()
	{
		return getClass().getName() + "[ UniqueID=" + uniqueID + 
			", UserMessage Key=" + userMessageKey + "]" + "\n\n" +
			"Nested Exception=" + throwable.getMessage();
	}

}