package cn.xmu.edu.goods.client;


import cn.xmu.edu.goods.client.dubbo.ShopDTO;

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
	ShopDTO getShop(Long skuId);

}
