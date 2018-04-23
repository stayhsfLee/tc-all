package com.thenorthw.tc.web.listener;

import com.thenorthw.tc.common.context.TcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by theNorthW on 20/07/2017.
 * blog: thenorthw.com
 *
 * @autuor : theNorthW
 */
public class TcListener implements ServletContextListener{
    private static Logger logger = LoggerFactory.getLogger(TcListener.class);

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        long startTime  = System.currentTimeMillis();
        logger.info("myblog service  context initialize begin {}",startTime);

        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContextEvent.getServletContext());
        TcContext.setYummyContext(ctx);

        logger.info("myblog service  context initialize success, cost time {}",System.currentTimeMillis() - startTime);
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
