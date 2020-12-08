package cn.edu.xmu.goods.client;


import cn.edu.xmu.goods.client.dubbo.ShopDTO;

/**
 * @Author: Yifei Wang
 * @Date: 2020/12/6 10:48
 */
public interface IShopService {



	/**
	 * 根据ID获取店铺信息
	 * @param id
	 * @return
	 */
	ShopDTO getShopById(Long id);
}
