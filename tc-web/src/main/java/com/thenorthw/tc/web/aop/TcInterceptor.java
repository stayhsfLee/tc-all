package com.thenorthw.tc.web.aop;

import com.auth0.jwt.interfaces.Claim;
import com.thenorthw.tc.common.ResponseCode;
import com.thenorthw.tc.common.ResponseModel;
import com.thenorthw.tc.common.annotation.LoginNeed;
import com.thenorthw.tc.common.utils.JwtUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by theNorthW on $date.
 * blog: thenorthw.com
 *
 * @autuor : theNorthW
 */
@Component
@Aspect
public class TcInterceptor {
    private static Logger logger = LoggerFactory.getLogger("httpValidLogger");

    @Autowired
    HttpServletRequest httpServletRequest;
    @Autowired
    HttpServletResponse httpServletResponse;

    @Around("execution(* com.thenorthw.blog.web.controller..*(..)) && args(..,bindingResult))")
    public Object doAround(ProceedingJoinPoint pjp, BindingResult bindingResult) throws Throwable {
        Object retVal;
        ResponseModel responseModel = new ResponseModel();

        //登录验证
        LoginNeed loginNeed = ((MethodSignature) pjp.getSignature()).getMethod().getAnnotation(LoginNeed.class);
        if(loginNeed != null){
            Claim uidc = JwtUtil.verify(httpServletRequest.getHeader("x-token")).get("u");
            String uid = uidc == null ? null : uidc.asString();
            if (uid == null){
                responseModel.setResponseCode(ResponseCode.UNAUTHORIZED.getCode());
                responseModel.setMessage(ResponseCode.UNAUTHORIZED.getMessage());
                return responseModel;
            }
            //权限验证交给后面service
        }

        if (bindingResult.hasErrors()) {
            errorHandler(bindingResult);

            responseModel.setResponseCode(ResponseCode.PARAMETER_ERROR.getCode());
            responseModel.setMessage(ResponseCode.PARAMETER_ERROR.getMessage());
            return responseModel;
        } else {
            retVal = pjp.proceed();
        }


        return retVal;
    }

    @Around("execution(* com.thenorthw.blog.web.controller..*(..)) && args()")
    public Object doAround1(ProceedingJoinPoint pjp) throws Throwable {
        Object retVal;
        ResponseModel responseModel = new ResponseModel();

        //登录验证
        LoginNeed loginNeed = ((MethodSignature) pjp.getSignature()).getMethod().getAnnotation(LoginNeed.class);
        if(loginNeed != null){
            Claim uidc = JwtUtil.verify(httpServletRequest.getHeader("x-token")).get("u");
            String uid = uidc == null ? null : uidc.asString();
            if (uid == null){
                responseModel.setResponseCode(ResponseCode.UNAUTHORIZED.getCode());
                responseModel.setMessage(ResponseCode.UNAUTHORIZED.getMessage());
                return responseModel;
            }
        }
        //权限验证交给后面service

        retVal = pjp.proceed();
        return retVal;
    }

    private void errorHandler(BindingResult bindingResult){
        StringBuilder sb = new StringBuilder();
        for(FieldError e : bindingResult.getFieldErrors()){
            sb.append(e.getObjectName() + " -> " +e.getField() + " -> " + (e.getRejectedValue() == null ? "null" : e.getRejectedValue().toString()+"\n"));
        }
        logger.error("Found invalid http parameters request, \n result: {}",sb.toString());
    }
}
