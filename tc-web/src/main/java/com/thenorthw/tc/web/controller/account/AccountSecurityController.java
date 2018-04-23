package com.thenorthw.tc.web.controller.account;

import com.thenorthw.blog.common.ResponseCode;
import com.thenorthw.blog.common.ResponseModel;
import com.thenorthw.blog.web.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by theNorthW on 07/05/2017.
 * blog: thenorthw.com
 *
 * @autuor : theNorthW
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping(value = "web/v1")
public class AccountSecurityController {
    @Autowired
    HttpServletRequest httpServletRequest;
    @Autowired
    HttpServletResponse httpServletResponse;

    //Service
    @Autowired
    AccountService accountService;



    @RequestMapping(value = "account/password" , method = RequestMethod.POST)
    @ResponseBody
    public String changeUserPassword(){
        String oldpassword = httpServletRequest.getParameter("oldpassword");
        String newpassword = httpServletRequest.getParameter("newpassword");

        ResponseModel responseModel = new ResponseModel();


        //调用service来修改密码,并且根据service返回结果判断修改结果
        int result = accountService.changePassword(0L,oldpassword,newpassword);

        //result : 1代表成功,-1代表不符
        if(result == 1){
            responseModel.setResponseCode(ResponseCode.OK.getCode());
            responseModel.setMessage("\"change successfully\"");
        }else{
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            responseModel.setResponseCode(ResponseCode.FORBIDDEN.getCode());
            responseModel.setMessage("\"not exist account or password wrong.\"");
        }

        //调用下线操作
        //在Session中删除相应信息

        return responseModel.toString();
    }

    @RequestMapping(value = "account/email",method = RequestMethod.POST)
    public String changeAccountEmail(){



        return null;
    }

    @RequestMapping(value = "account/phone",method = RequestMethod.POST)
    public String changeAccountPhone(){



        return null;
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

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
