package com.mobile.brot.Notification.Servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import com.mobile.brot.Notification.context.ContextManager;
import com.mobile.brot.Notification.context.SpringAppContext;
import com.mobile.brot.Notification.domain.Context;



public class NotificationServlet extends org.apache.cxf.transport.servlet.CXFServlet {
	
	private static final Logger LOG = LoggerFactory.getLogger(NotificationServlet.class);

	@Override
	public void init() {
				    
		//	set application context
        
        ApplicationContext context  = (ApplicationContext) this.getServletContext().getAttribute(
                                                           WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        SpringAppContext.setAppcontext(context);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
		throws ServletException{
		try {
			super.doGet(req, res);
		} catch(Throwable e){
			try {
				throw e;
			} catch (Throwable e1) {
				//processException(e1);
				e1.printStackTrace();
			}
		}
	}


	@SuppressWarnings("unused")
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
		throws ServletException {
		try {
			super.doPost(req, res);
		} catch(Throwable e){
			try {
				throw e;
			} catch (Throwable e1) {
				//processException(e1);
				e1.printStackTrace();
			}
		}

	}

	@Override
	public void doDelete(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException  {
		try {
			super.doDelete(req, res);
		} catch(Throwable e){
			try {
				throw e;
			} catch (Throwable e1) {
				//processException(e1);
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void doPut(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
		
		try {
			super.doPut(req, res);
		} catch(Throwable e){
			try {
				throw e;
			} catch (Throwable e1) {
				//processException(e1);
				e1.printStackTrace();
			}
		}
	}

	private void pre(HttpServletRequest req, HttpServletResponse res) {
		Context ctx = new Context(req, res);
		ctx.getRequest().setAttribute("start", System.currentTimeMillis());
		ContextManager.set(ctx);
	}

	private void post(HttpServletRequest req, HttpServletResponse res) {
		
	}

	
	
	public static void main(String args[]){
		NullPointerException np = new NullPointerException();
		List lst = null;
		try{
			while(lst.size()>0){
			}
		}catch(Exception e){
		}
		
	}
}
