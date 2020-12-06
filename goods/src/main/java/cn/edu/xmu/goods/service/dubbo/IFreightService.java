package cn.edu.xmu.goods.service.dubbo;


import cn.edu.xmu.goods.model.bo.dubbo.Freight;

public interface IFreightService {
    /**
     * 根据运费ID获取运费
     * @param freightId 运费ID
     * @return 由于默认运费模板的存在Freight必定不是空
     */
    public Freight getFreightById(long freightId);
}
