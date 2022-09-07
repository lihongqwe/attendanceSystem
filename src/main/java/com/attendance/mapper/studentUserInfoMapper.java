package com.attendance.mapper;

import com.attendance.domain.studentUserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface studentUserInfoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(studentUserInfo record);

    int insertList(List<studentUserInfo> list);

    studentUserInfo selectByPhoneNumber(String PhoneNumber);

    studentUserInfo selectByPrimaryKey(String userId);

    List<studentUserInfo> selectAll();

    int update(studentUserInfo record);

    int updateByPrimaryKey( String userId,String password);

    int updateList(List<studentUserInfo> list);

    int delete();
}