package com.thenorthw.tc.web.controller.wechat;

import com.thenorthw.blog.common.utils.encrypt.Encrypt;
import com.thenorthw.blog.common.utils.encrypt.EncryptMode;
import com.thenorthw.blog.common.utils.json.JsonUtil;
import com.thenorthw.blog.face.form.wechat.WeChatEchoForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author theNorthW
 * @date 19/07/2017
 * blog: thenorthw.com
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping(value = "/wechat")
public class WeChatEchoController {
    private static Logger logger = LoggerFactory.getLogger(WeChatEchoController.class);

    private static final String TOKEN = "0iJ8sJDIS91Losmf1g09io013";

    @Autowired
    HttpServletRequest httpServletRequest;
    @Autowired
    HttpServletResponse httpServletResponse;


    @RequestMapping(value = "/echo",method = RequestMethod.GET)
    @ResponseBody
    public String echo(WeChatEchoForm weChatEchoForm){
        System.out.println(JsonUtil.beanToJson(weChatEchoForm));

        List<String> ss = new ArrayList<String>();
        ss.add(TOKEN);
        ss.add(weChatEchoForm.getNonce());
        ss.add(weChatEchoForm.getTimestamp());

        //sort these three parameters
        Collections.sort(ss);

        StringBuilder sb = new StringBuilder();
        sb.append(ss.get(0)).append(ss.get(1)).append(ss.get(2));

        //encrypt the string
        String afterEncrypt = Encrypt.encrypt(sb.toString(), EncryptMode.SHA1);

        if(afterEncrypt.equals(weChatEchoForm.getSignature())) {
            logger.info("WeChat has checked this project.");
            return weChatEchoForm.getEchostr();
        }else{
            logger.warn("WeChat getin check error.");
            return null;
        }
    }


    @RequestMapping(value = "/echo" ,method = RequestMethod.POST)
    @ResponseBody
    public String echoMessage(){

        return null;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        WeChatEchoController.logger = logger;
    }

    public static String getToken() {
        return TOKEN;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }
}
