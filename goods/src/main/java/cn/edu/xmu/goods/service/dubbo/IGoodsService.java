package cn.edu.xmu.goods.service.dubbo;

/**
 * @author xincong yao
 * @date 2020-11-28
 */
public interface IGoodsService {

	/**
	 * 获取价格
	 * @param skuId
	 * @return
	 */
	Long getPrice(Long skuId);
}
