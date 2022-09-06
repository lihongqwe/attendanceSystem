package com.attendance.mapper;

import com.attendance.domain.UserName;
import com.dingtalk.api.response.OapiUserListsimpleResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserNameMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserName record);

    UserName selectByPrimaryKey(Integer id);

    List<UserName> selectAll();

    int updateByPrimaryKey(UserName record);

    int UpdateUsers();

    int insertUserName(OapiUserListsimpleResponse.ListUserSimpleResponse userName);
}