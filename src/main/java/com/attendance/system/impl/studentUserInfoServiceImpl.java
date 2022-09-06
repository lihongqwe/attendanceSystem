package com.attendance.system.impl;

import com.attendance.common.Result;
import com.attendance.domain.studentUserInfo;
import com.attendance.mapper.studentUserInfoMapper;
import com.attendance.system.studentUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class studentUserInfoServiceImpl implements studentUserInfoService {


    @Autowired
    private studentUserInfoMapper studentUserInfoMapper;

    /**
     * 学生用户信息通过电话号码
     *
     * @param PhoneNumber 电话号码
     * @return {@link Result}
     */
    @Override
    public studentUserInfo GetStudentUserInfoByPhoneNumber(String PhoneNumber) {
        return studentUserInfoMapper.selectByPhoneNumber(PhoneNumber);
    }
}
