package com.thenorthw.tc.web.service.user.impl;

import com.thenorthw.blog.common.dao.user.UserDao;
import com.thenorthw.blog.common.enums.RoleType;
import com.thenorthw.blog.common.model.account.Account;
import com.thenorthw.blog.common.model.user.User;
import com.thenorthw.blog.common.utils.encrypt.Encrypt;
import com.thenorthw.blog.web.service.account.AccountService;
import com.thenorthw.blog.web.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by theNorthW on 03/05/2017.
 * blog: thenorthw.com
 *
 * @autuor : theNorthW
 */
@SuppressWarnings("ALL")
@Service
public class UserServiceImpl implements UserService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserDao userDao;

    @Autowired
    AccountService accountService;

    public User userLogin(Account account){

        //登录成功后,根据account获取user_profile
        //更新登录时间
        account.setLastLoginTime(new Date(System.currentTimeMillis()));
        accountService.updateLoginTime(account);

		//获取用户信息
        User user = userDao.getUserProfileByUserId(account.getId());

        if(account.getRoleId() == RoleType.ROOT.roleId){
            user.setAdmin(true);
        }

        return user;
    }

    public int createNewUser(User user) {
        //向db中添加user记录
        int result = 0;
        try {
            result = userDao.insertNewUser(user);
        }catch (Exception e){
            result = -1;
            logger.error("Database operation error", e);
        }
        return result;
    }

    public User getUserProfileByUserId(Long userId) {
        return userDao.getUserProfileByUserId(userId);
    }

    public int updateUserInfo(User user) {
        return userDao.updateUserProfile(user);
    }

    public int updateUserAvatar(byte[] avatarBytes,Long userId) {
        //首先利用oss上传图片到图片服务器
        String md5 = null;
        md5 = Encrypt.md5(avatarBytes);

        if(md5 == null){
            md5 = "default";
        }

        String avatarKey = "/user/avatar/"+md5;
//        if(OssImgWrapper.updateImg(avatarBytes,avatarKey) == -1){
//            return -1;
//        }

        int result = userDao.updateUserAvatar(userId, avatarKey);

        return result;
    }

    public List<User> getUserProfileByUserIds(String[] userIds) {
        List<String> list = Arrays.asList(userIds);
        return userDao.getUserProfileByUserIds(list);
    }


    public User isUserHasRegisterWeChat(String openId) {
        Account account  =  accountService.getAccountByWeChatId(openId);
        Long userId = account.getId();
        return this.getUserProfileByUserId(userId);
    }


    //创建admin账户


    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}
