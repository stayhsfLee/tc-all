package com.thenorthw.tc.web.controller.user;

import com.thenorthw.blog.common.ResponseCode;
import com.thenorthw.blog.common.ResponseModel;
import com.thenorthw.blog.common.annotation.LoginNeed;
import com.thenorthw.blog.common.model.account.Account;
import com.thenorthw.blog.common.model.user.User;
import com.thenorthw.blog.common.utils.JwtUtil;
import com.thenorthw.blog.face.form.user.UserLoginForm;
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
 * @autuor : theNorthW
 */
@Controller

@RequestMapping(value = "/web/v1")
public class UserSignInController {
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
     * 登陆过程
     * 1. 从request中获取loginname和password
     * 2. 利用ZeusLc.signin进行登录,获得返回好的token
     * 3. 在token中加入自己的信息 (此处需要加一个功能,就是用户可以往guard中的token的payload中加入自己的东西)
     * 4. 如果登录正确,则返回相应的user_profile信息
     * @return
     */
    @RequestMapping(value = "/user/self",method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel userSigninThroughWeb(@Valid UserLoginForm userLoginForm, BindingResult bindingResult){
        ResponseModel responseModel = new ResponseModel();
        Account account = null;
        User user = null;

        //根据登录方式不同采取不同操作
        if(Integer.valueOf(userLoginForm.getLogintype()) == 1){
            //Todo: 首先对传过来的密码进行进一步操作,来和数据库中的密码进行匹配，当前不做任何操作

            //进入数据库查找相关记录
            account = accountService.getAccountByNP(userLoginForm.getLoginname(),userLoginForm.getPassword());

            if(account == null){
                responseModel.setResponseCode(ResponseCode.NO_SUCH_ACCOUNT_OR_PASSWORD_WRONG.getCode());
                responseModel.setMessage(ResponseCode.NO_SUCH_ACCOUNT_OR_PASSWORD_WRONG.getMessage());
            }else {
                user = userService.userLogin(account);

                if(user == null){
                    responseModel.setResponseCode(ResponseCode.LOGIN_FAIL.getCode());
                    responseModel.setMessage(ResponseCode.LOGIN_FAIL.getMessage());
                }else {
                    //在response header中放入x-token
                    httpServletResponse.addHeader("x-token", JwtUtil.createToken(account.getId().toString()));
                    responseModel.setData(user);
                }
            }
        }else if(Integer.valueOf(userLoginForm.getLogintype()) == 2){
            //使用手机号进行登录
            String phone = userLoginForm.getLoginname();

            //Todo: 手机号登录步骤还未操作
        }

        return responseModel;
    }


    @RequestMapping(value = "/user/self",method = RequestMethod.GET)
    @ResponseBody
    @LoginNeed
    public ResponseModel userSigninThroughWebByToken(){
        ResponseModel responseModel = new ResponseModel();
        Account account = null;
        User user = null;

        String uid = JwtUtil.verify(httpServletRequest.getHeader("x-token")).get("u").asString();

        account = accountService.getAccountById(Long.parseLong(uid));
        user = userService.userLogin(account);

        if(user == null){
            responseModel.setResponseCode(ResponseCode.LOGIN_FAIL.getCode());
            responseModel.setMessage(ResponseCode.LOGIN_FAIL.getMessage());
        }else {
            responseModel.setData(user);
        }

        return responseModel;
    }

    @RequestMapping(value = "/user/self/wechat",method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel userLoginThroughWeChat(){
        ResponseModel responseModel = new ResponseModel();

        //获取用户open_id
        String openId = httpServletRequest.getParameter("openId");

        //判断用户是否已经通过微信注册过
        User user = null;
        if((user = userService.isUserHasRegisterWeChat(openId)) == null){
            //需要注册
            //前端进行判断,跳到注册页面
        }

        ResponseModel<User> userResponseModel = new ResponseModel<User>();
        userResponseModel.setData(user);
        return userResponseModel;
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

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
