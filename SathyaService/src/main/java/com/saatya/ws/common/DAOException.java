package com.saatya.ws.common;

import java.io.Serializable;


public class DAOException extends BaseException
{
	
	public DAOException(Throwable aThrowable, String userMessageKey, Serializable[] valueReplacementArray, ErrorLevel level)
	{
		super(aThrowable, userMessageKey, valueReplacementArray, level);
	}
	
	
}