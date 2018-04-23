package com.thenorthw.tc.common.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

/**
 * Created by theNorthW on 19/07/2017.
 * blog: thenorthw.com
 *
 * @autuor : theNorthW
 */
public class TcContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(TcContext.class);


    private static ApplicationContext yummyContext;

    public static void setYummyContext(ApplicationContext applicationContext){yummyContext = applicationContext;}

    public static boolean isReady() {
        return yummyContext != null;
    }

    /**
     * 从spring content 获取bean 实例
     */
    public static Object getBean(String name) {
        if (StringUtils.isEmpty(name)) {
            LOGGER.warn("bean name is empty..");
            return null;
        }
        try {
            return yummyContext.getBean(name);
        } catch (BeansException e) {
            LOGGER.warn("Get bean from sdpContent by bean name {} ,but not exist..", name, e);
            return null;
        }
    }

    /**
     * 从spring content 获取bean 实例
     */
    public static <T> T getBean(Class<T> clazz) {
        if (clazz == null) {
            LOGGER.warn("clazz is empty..");
            return null;
        }
        try {
            return yummyContext.getBean(clazz);
        } catch (BeansException e) {
            LOGGER.warn("Get bean from sdpContent by bean name {} ,but not exist..", clazz.getName(), e);
            return null;
        }
    }

    /**
     * 从spring content 获取bean 实例
     */
    public static <T> T getBean(String name, Class<T> type) {
        if (StringUtils.isEmpty(name)) {
            LOGGER.warn("bean name is empty..");
            return null;
        }
        try {
            return yummyContext.getBean(name, type);
        } catch (BeansException e) {
            LOGGER.warn("Get bean from sdpContent by bean name {} ,but not exist..", name, e);
            return null;
        }
    }
}
