package com.thenorthw.tc.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.InetAddress;

/**
 * This class is to start blog jetty web app server
 */
public class Main {
    private static Logger logger = LoggerFactory.getLogger("Main");

    public static void main(String[] args) {

        ApplicationContext ctx = new ClassPathXmlApplicationContext("jetty.xml");
        Server server = (Server)ctx.getBean("jettyServer");

        try {
            int port = ((ServerConnector) (server.getConnectors()[0])).getPort();
            String ip = InetAddress.getLocalHost().getHostAddress();
            server.start();
            logger.info(String.format("jetty server is running at http://%s:%d", ip, port));
        } catch (Exception e) {
            logger.error("start jetty server failed", e);
        }
    }
}