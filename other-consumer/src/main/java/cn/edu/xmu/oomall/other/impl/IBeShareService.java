package cn.edu.xmu.oomall.other.impl;

/**
 * 2 * @author: LiangJi3229
 * 3 * @date: 2020/12/12 下午8:24
 * 4
 */
public interface IBeShareService {
    /**
     * 用户点击分享链接时 尝试读取share对象以生成一个beShare对象
     * @return boolean 表示当前这个skuid是否在shareid中
     */
    boolean createBeShare(Long customerId,Long shareId,Long skuId);
}
