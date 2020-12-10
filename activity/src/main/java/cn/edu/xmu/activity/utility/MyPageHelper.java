package cn.edu.xmu.activity.utility;

import com.github.pagehelper.PageInfo;

public class MyPageHelper {
    public static void transferPageParams(PageInfo<?> from, PageInfo<?> to){
        to.setPageNum(from.getPageNum());
        to.setPages(from.getPages());
        to.setTotal(from.getTotal());
        to.setPageSize(from.getPageSize());
    }
}
