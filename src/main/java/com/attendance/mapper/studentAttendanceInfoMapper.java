package com.attendance.mapper;

import cn.hutool.core.date.DateTime;
import com.attendance.domain.studentAttendanceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface studentAttendanceInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(List<studentAttendanceInfo> studentAttendanceInfo);

    studentAttendanceInfo selectByPrimaryKey(Integer id);

    /**
     * 选择所有
     *
     * @param userId 用户id
     * @return {@link List}<{@link studentAttendanceInfo}>
     */
    List<studentAttendanceInfo> selectAll(String userId);

    List<studentAttendanceInfo> searchAllByTime(@Param("userId")String userId , @Param("startTime") String startTime, @Param("endTime") String endTime);

    int updateByPrimaryKey(studentAttendanceInfo record);
}