package com.siblinks.ws.common;

import java.io.Serializable;


public class DAOException extends BaseException
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DAOException(Throwable aThrowable, String userMessageKey, Serializable[] valueReplacementArray, ErrorLevel level)
	{
		super(aThrowable, userMessageKey, valueReplacementArray, level);
	}
	
	
}