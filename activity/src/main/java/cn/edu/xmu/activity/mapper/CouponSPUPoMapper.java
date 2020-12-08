package cn.edu.xmu.activity.mapper;

import cn.edu.xmu.activity.model.po.CouponSPUPo;
import cn.edu.xmu.activity.model.po.CouponSPUPoExample;
import java.util.List;

public interface CouponSPUPoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coupon_spu
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coupon_spu
     *
     * @mbg.generated
     */
    int insert(CouponSPUPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coupon_spu
     *
     * @mbg.generated
     */
    int insertSelective(CouponSPUPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coupon_spu
     *
     * @mbg.generated
     */
    List<CouponSPUPo> selectByExample(CouponSPUPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coupon_spu
     *
     * @mbg.generated
     */
    CouponSPUPo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coupon_spu
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(CouponSPUPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coupon_spu
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(CouponSPUPo record);
}