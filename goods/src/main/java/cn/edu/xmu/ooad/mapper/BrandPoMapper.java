package cn.edu.xmu.ooad.mapper;

import cn.edu.xmu.ooad.model.po.BrandPo;

public interface BrandPoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table good_brand
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table good_brand
     *
     * @mbg.generated
     */
    int insert(BrandPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table good_brand
     *
     * @mbg.generated
     */
    int insertSelective(BrandPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table good_brand
     *
     * @mbg.generated
     */
    BrandPo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table good_brand
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(BrandPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table good_brand
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(BrandPo record);
}