package com.attendance.mapper;

import com.attendance.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    int deleteByPrimaryKey(Integer userId);

    int insertUser(User user);

    User selectByPrimaryKey(String userId);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

    User selectUserByUserName(String username);

    List<User> selectAllUser();

    User selectUser(String username);
}