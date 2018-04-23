package com.thenorthw.tc.common.dao.user;

import com.thenorthw.blog.common.model.user.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by theNorthW on 03/05/2017.
 * blog: thenorthw.com
 *
 * @autuor : theNorthW
 */
@Repository
public interface UserDao {
    public int insertNewUser(User user);

    public User getUserProfileByUserId(long userId);

    public List<User> getUserProfileByUserIds(@Param("userIds") List<String> userIds);

    public int updateUserProfile(User user);

    public int updateUserAvatar(@Param("userId") long userId, @Param("avatar") String avatar);
}
