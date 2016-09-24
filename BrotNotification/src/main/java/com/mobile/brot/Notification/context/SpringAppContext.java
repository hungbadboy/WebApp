package com.mobile.brot.Notification.context;

import org.springframework.context.ApplicationContext;


public class SpringAppContext {

    private static ApplicationContext appContext;

    public static ApplicationContext getAppcontext() {

        return appContext;
    }

    public static void setAppcontext(ApplicationContext appContext) {

        SpringAppContext.appContext = appContext;
    }

}
