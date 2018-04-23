package com.thenorthw.tc.web.controller.user;

import com.thenorthw.blog.common.ResponseCode;
import com.thenorthw.blog.common.ResponseModel;
import com.thenorthw.blog.face.form.user.UserRegisterForm;
import com.thenorthw.blog.web.service.account.AccountService;
import com.thenorthw.blog.web.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by theNorthW on 03/05/2017.
 * blog: thenorthw.com
 *
 *
 * 2017.7.19 目前只支持使用手机号进行注册
 * @autuor : theNorthW
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/web/v1")
public class UserSignUpController {
    @Autowired
    HttpServletRequest httpServletRequest;
    @Autowired
    HttpServletResponse httpServletResponse;

    //Services
    @Autowired
    AccountService accountService;
    @Autowired
    UserService userService;

    /**
     * 该接口为了方便微信用户首次使用时进行
     * @return 具体返回结果请到API文档中查看
     */
    @RequestMapping(value = "/user",method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel signup(@Valid UserRegisterForm createForm, BindingResult bindingResult){
        ResponseModel responseModel = new ResponseModel();

        //如果创建成功,返回创建好的user记录的user_id
        int result = 0;

        //判断注册类型是普通用户还是店铺 - 注册类型默认为false
        result = accountService.createNormalUserAccount(createForm);

        if(result == -1){
            responseModel.setResponseCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode());
            responseModel.setMessage("Error:create account error.");
        }else if(result == -2){
            responseModel.setResponseCode(ResponseCode.EXISTED_USER.getCode());
            responseModel.setMessage("Error:existed user.");
        }

        //返回创建好的User信息,并且默认已经登录,返回头上加上token
        //AuthorizedToken token = ZeusLc.login(createForm.getLoginname(),createForm.getPassword(),httpServletResponse);

        return responseModel;
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
