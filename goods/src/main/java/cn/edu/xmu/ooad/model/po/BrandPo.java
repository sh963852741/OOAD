package cn.edu.xmu.ooad.model.po;

public class BrandPo {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column good_brand.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column good_brand.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column good_brand.detail
     *
     * @mbg.generated
     */
    private String detail;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column good_brand.image_url
     *
     * @mbg.generated
     */
    private String imageUrl;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column good_brand.id
     *
     * @return the value of good_brand.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column good_brand.id
     *
     * @param id the value for good_brand.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column good_brand.name
     *
     * @return the value of good_brand.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column good_brand.name
     *
     * @param name the value for good_brand.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column good_brand.detail
     *
     * @return the value of good_brand.detail
     *
     * @mbg.generated
     */
    public String getDetail() {
        return detail;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column good_brand.detail
     *
     * @param detail the value for good_brand.detail
     *
     * @mbg.generated
     */
    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column good_brand.image_url
     *
     * @return the value of good_brand.image_url
     *
     * @mbg.generated
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column good_brand.image_url
     *
     * @param imageUrl the value for good_brand.image_url
     *
     * @mbg.generated
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }
}