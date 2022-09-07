package com.attendance.mapper;

import com.attendance.domain.creditTransfer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface creditTransferMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(creditTransfer record);

    creditTransfer selectByPrimaryKey(Integer id);

    List<creditTransfer> selectAll();

    int updateByPrimaryKey(creditTransfer record);

}