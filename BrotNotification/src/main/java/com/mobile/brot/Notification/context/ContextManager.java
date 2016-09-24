package com.mobile.brot.Notification.context;

import com.mobile.brot.Notification.domain.Context;


public class ContextManager {
	
	private static final ThreadLocal<Context> context = new ThreadLocal<Context>();
	
	public static Context get() {
		return context.get();
	}
	
	public static void set(Context ctx) {
		context.set(ctx);
	}
	
	public static void clear() {
		context.remove();
	}
}