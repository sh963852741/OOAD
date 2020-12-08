package cn.edu.xmu.goods.client;

/**
 * @author xincong yao
 * @date 2020-11-28
 */
public interface IFlashSaleService {

	/**
	 * 该商品在当前时刻是否是秒杀商品
	 * @param skuId
	 * @return 不为秒杀商品返回负数，否则返回秒杀活动id
	 */
	Long getSeckillId(Long skuId);

	/**
	 * 获取秒杀活动下商品的价格, 不校验时间
	 * @param skuId
	 * @return
	 */
	Long getPrice(Long skuId);
}
