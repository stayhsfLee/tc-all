package com.thenorthw.tc.web.controller.user;

import com.thenorthw.blog.common.ResponseCode;
import com.thenorthw.blog.common.ResponseModel;
import com.thenorthw.blog.common.annotation.LoginNeed;
import com.thenorthw.blog.common.model.user.User;
import com.thenorthw.blog.common.utils.JwtUtil;
import com.thenorthw.blog.face.form.user.UserInfoUpdateForm;
import com.thenorthw.blog.face.form.user.UserInfosFetchForm;
import com.thenorthw.blog.web.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by theNorthW on 07/05/2017.
 * blog: thenorthw.com
 *
 * @autuor : theNorthW
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/web/v1")
public class UserInfoController {
    @Autowired
    HttpServletRequest httpServletRequest;
    @Autowired
    HttpServletResponse httpServletResponse;
    @Autowired
	UserService userService;


    /**
     * 此处就涉及到一个问题,用什么去识别用户
     * 用过user_id去识别user? - 现阶段还是用这个去识别
     * @return 不会反悔用户的详细信息,只会返回头像,名称,个人介绍等信息.
     */
    @RequestMapping(value = "/user/infos",method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel getUserInfos(@Valid UserInfosFetchForm userInfosFetchForm, BindingResult bindingResult){
        //该方法只接受list格式的id数据
        String userId = userInfosFetchForm.getUserIds();

        ResponseModel responseModel = new ResponseModel();

        //判断是否为list格式 - list格式采用#进行user_id之间的划分
        //如果是list,则表示一般不是个人主页等页面,所以需要简洁信息即可
        String[] strings = userId.split(",");


        //只需要返回user_id,用户名,头像即可
        List<User> res = userService.getUserProfileByUserIds(strings);
        System.out.println(res.size());
        responseModel.setData(res);

        return responseModel;
    }

    @RequestMapping(value = "/user/{userId}",method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel getSomeOneUserInfo(@PathVariable(value = "userId")String userId){
        //该方法只需要传入这一个参数即可
        //该方法接受list格式的id数据
        ResponseModel responseModel = new ResponseModel();

        //判断是否为list格式 - list格式采用#进行user_id之间的划分
        //如果是list,则表示一般不是个人主页等页面,所以需要简洁信息即可


        if(userId.matches("[1-9]\\d*")) {
            User user = userService.getUserProfileByUserId(Long.parseLong(userId));
            //不需要详细信息
            if (user != null) {
                user.setGmtCreate(null);
                user.setGmtModified(null);
                responseModel.setData(user);
            }
        }else {
            responseModel.setResponseCode(ResponseCode.PARAMETER_ERROR.getCode());
            responseModel.setMessage(ResponseCode.PARAMETER_ERROR.getMessage());
        }

        return responseModel;
    }

    /**
     * 此处就涉及到一个问题,用什么去识别用户
     * 用过user_id去识别user? - 现阶段还是用这个去识别
     * @return 不会返回用户的详细信息,只会返回头像,名称,个人介绍等信息.
     */
    @RequestMapping(value = "/user/info/me",method = RequestMethod.GET)
    @ResponseBody
    @LoginNeed
    public ResponseModel getUserSelfInfo(){
        ResponseModel responseModel = new ResponseModel();

        User res = userService.getUserProfileByUserId(Long.parseLong(JwtUtil.verify(httpServletRequest.getHeader("x-token")).get("u").asString()));

        if(res == null){
            responseModel.setResponseCode(ResponseCode.NOT_FOUND.getCode());
            responseModel.setMessage(ResponseCode.NOT_FOUND.getMessage());
        }else {
            responseModel.setData(res);
        }

        return responseModel;
    }



    @RequestMapping(value = "/user/info" , method = RequestMethod.POST)
    @ResponseBody
    @LoginNeed
    public ResponseModel updateUserProfile(@Valid UserInfoUpdateForm userInfoUpdateForm, BindingResult bindingResult){
        String nickname = userInfoUpdateForm.getNick();
        String sex = userInfoUpdateForm.getSex();
        String introduction = userInfoUpdateForm.getIntroduction();

        ResponseModel responseModel = new ResponseModel();

        Long userId = Long.parseLong(JwtUtil.verify(httpServletRequest.getHeader("x-token")).get("u").asString());


        //创建User实例
        User user = new User();
        user.setId(userId);       //从token中知晓user_id
        user.setNick(nickname);
        user.setSex(Integer.parseInt(sex));
        user.setIntroduction(introduction);
        user.setGmtModified(new Date());

        //调用服务来更新用户信息
        int result = userService.updateUserInfo(user);
        if(result == 1){
            responseModel.setMessage("Update successfully");
        }else{
            responseModel.setResponseCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode());
            responseModel.setMessage("Update failed");
        }

        return responseModel;
    }

    /**
    //一般都是直接根据图片存储地址直接向图片服务器去获得
    @RequestMapping(value = "user/info/avatar",method = RequestMethod.GET)
    @ResponseBody
    public String getUserAvatar(){
        ResponseModel webReturnResultMessage = new ResponseModel();


        return webReturnResultMessage.toString();
    }
    **/

    /**
     * 需要先对图片进行md5处理之后上传到图片server,上传成功后进行更新.
     * 此处需要用到 aliyun oss
     * @return
     */
    @RequestMapping(value = "user/info/avatar",method = RequestMethod.POST)
    @ResponseBody
    //此处不需要进行apc过滤
    public ResponseModel updateUserAvatar(@RequestParam(value = "avatarFile",required = true)MultipartFile avatar){
        ResponseModel responseModel = new ResponseModel();
        int result = 0;

        try {
            //user id = account id
            result = userService.updateUserAvatar(avatar.getBytes(),Long.parseLong(JwtUtil.verify(httpServletRequest.getHeader("x-token")).get("u").asString()));

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(result == -1){
            responseModel.setResponseCode(ResponseCode.OSS_UPLOAD_ERROR.getCode());
            responseModel.setMessage(ResponseCode.OSS_UPLOAD_ERROR.getMessage());
        }

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
}
