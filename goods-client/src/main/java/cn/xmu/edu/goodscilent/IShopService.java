package cn.xmu.edu.goodscilent;


import cn.xmu.edu.goodscilent.dubbo.Shop;

/**
 * @Author: Yifei Wang
 * @Date: 2020/12/6 10:48
 */
public interface IShopService {

	/**
	 * 获取商铺消息
	 * @param skuId
	 * @return
	 */
	Shop getShop(Long skuId);

}
