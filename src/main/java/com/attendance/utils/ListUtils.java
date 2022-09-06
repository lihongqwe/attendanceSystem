package com.attendance.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 列表分页工具
 *
 * @author lihong
 * @date 2022/09/06
 */
public class ListUtils {

    public static <T> List<List<T>> getPageData(List<T> list, int pageNum) {
        if (list == null || list.size() == 0) {
            return null;
        }
        List<List<T>> result = new ArrayList<>();
        if (pageNum < 1) {
            result.add(list);
            return result;
        }

        for (int i = 0; i < Math.ceil((double)list.size() / pageNum); i++) {
            List<T> tempList = list.stream().skip((long) i * pageNum).limit(pageNum).collect(Collectors.toList());
            result.add(tempList);
        }
        return result;
    }

    public static List startPage(List list, Integer pageNum,
                                 Integer pageSize) {
        if (list == null || list.size() == 0) {
            return null;
        }
        int count = list.size(); // 记录总数
        int pageCount = 0; // 页数
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }
        if (pageNum > pageCount) {
            pageNum = pageCount;
        }

        int fromIndex = 0; // 开始索引
        int toIndex = 0; // 结束索引

        if (pageNum != pageCount) {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = fromIndex + pageSize;
        } else {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = count;
        }
        return list.subList(fromIndex, toIndex);
    }


}
