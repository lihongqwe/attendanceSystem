package com.attendance.utils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class Paging {
    public void setStartPage(Integer page){
        PageHelper.startPage(page,2);
    }

    public Map<String,Object> Paging( List list){
        Map<String ,Object> result=new HashMap<>();
        PageInfo<List> pageInfo=new PageInfo<>(list);
        result.put("list",pageInfo.getList());
        result.put("total",pageInfo.getPages());
        return result;
    }

}
