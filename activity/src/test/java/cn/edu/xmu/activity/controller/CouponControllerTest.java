package cn.edu.xmu.activity.controller;

import cn.edu.xmu.activity.model.vo.CouponActivityVo;
import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class CouponControllerTest {
    private static String adminToken;
    private static String shopToken;
    @Autowired
    private MockMvc mvc;

    @BeforeAll
    private static void login(){
        JwtHelper jwtHelper = new JwtHelper();
        adminToken =jwtHelper.createToken(1L,0L, 3600);
        shopToken =jwtHelper.createToken(59L,1L, 3600);
    }

    /**
     * 获取上线的优惠活动
     * @throws Exception
     */
    @Test
    public void getCouponActivityList1() throws Exception {
        mvc.perform(get("/coupon/couponactivities").contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.data.page").value(1))
                .andExpect(jsonPath("$.data.pageSize").value(10))
                .andExpect(jsonPath("$.data.list.length()").value(4))
                .andDo(MockMvcResultHandlers.print());
        mvc.perform(get("/coupon/couponactivities?page=2&pageSize=2").contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.data.page").value(2))
                .andExpect(jsonPath("$.data.pageSize").value(2))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 按照店铺获取优惠活动
     * @throws Exception
     */
    @Test
    public void getCouponActivityList2() throws Exception {
        mvc.perform(get("/coupon/couponactivities?shopId=2").contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.data.list.length()").value(0))
                .andDo(MockMvcResultHandlers.print());
        mvc.perform(get("/coupon/couponactivities?shopId=0").contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.data.list.length()").value(4))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 按照时间线获取优惠活动
     * @throws Exception
     */
    @Test
    public void getCouponActivityList3() throws Exception {
        for(int i = 1;i<=3;++i){
            mvc.perform(get("/coupon/couponactivities?timeline=" + i).contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json;charset=UTF-8"))
                    .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                    .andDo(MockMvcResultHandlers.print());
        }
    }

    /**
     * 管理员获取下线的优惠活动
     * @throws Exception
     */
    @Test
    public void getCouponActivityList4() throws Exception {
        mvc.perform(get("/coupon/shops/0/couponactivities/invalid").contentType("application/json;charset=UTF-8").header("authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.data.list.length()").value(4))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 管理员获取其他店铺下线的优惠活动
     * @throws Exception
     */
    @Test
    public void getCouponActivityList5() throws Exception {
        mvc.perform(get("/coupon/shops/1/couponactivities/invalid").contentType("application/json;charset=UTF-8").header("authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.data.list.length()").value(0))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 获取优惠活动中的商品
     */
    @Test
    public void getSKUsInCouponActivity() throws Exception {
        mvc.perform(get("/coupon/couponactivities/1/skus").contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.data.list.length()").value(3))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 查看优惠活动详情
     */
    @Test
    public void getCouponActivityDetail() throws Exception {
        mvc.perform(get("/coupon/shops/0/couponactivities/1").contentType("application/json;charset=UTF-8").header("authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 获取优惠券的所有状态
     */
    @Test
    public void getCouponStates() throws Exception {
        mvc.perform(get("/coupon/coupons/states").contentType("application/json;charset=UTF-8").header("authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 新建优惠活动
     */
    @Test
    public void addCouponActivity1() throws Exception {
        CouponActivityVo vo = new CouponActivityVo();
        vo.setName("润涵的测试");
        vo.setBeginTime(LocalDateTime.now().plusMinutes(3));
        vo.setEndTime(LocalDateTime.now().plusMinutes(20));
        vo.setCouponTime(LocalDateTime.now().plusMinutes(1));
        vo.setStrategy("{}");
        vo.setValidTerm((byte)1);
        vo.setQuantity(1235);
        vo.setQuantityType((byte)0);
        String requestJson = JacksonUtil.toJson(vo);


        mvc.perform(post("/coupon/shops/0/couponactivities").contentType("application/json;charset=UTF-8")
                .header("authorization", adminToken).content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 修改活动
     */
    @Test
    public void modifyCouponActivity()throws Exception{
        CouponActivityVo vo = new CouponActivityVo();
        vo.setName("润涵的测试-改");
        vo.setBeginTime(LocalDateTime.now().plusMinutes(3));
        vo.setEndTime(LocalDateTime.now().plusMinutes(20));
        vo.setCouponTime(LocalDateTime.now().plusMinutes(1));
        vo.setStrategy("{}");
        vo.setValidTerm((byte)1);
        vo.setQuantity(1235);
        vo.setQuantityType((byte)0);
        String requestJson = JacksonUtil.toJson(vo);

        mvc.perform(put("/coupon/shops/0/couponactivities/0").contentType("application/json;charset=UTF-8")
                .header("authorization", adminToken).content(requestJson))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.RESOURCE_ID_NOTEXIST.getCode()))
                .andDo(MockMvcResultHandlers.print());

        mvc.perform(put("/coupon/shops/1/couponactivities/1").contentType("application/json;charset=UTF-8")
                .header("authorization", shopToken).content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode()))
                .andDo(MockMvcResultHandlers.print());

        mvc.perform(put("/coupon/shops/0/couponactivities/2").contentType("application/json;charset=UTF-8")
                .header("authorization", adminToken).content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.COUPONACT_STATENOTALLOW.getCode()))
                .andDo(MockMvcResultHandlers.print());

        mvc.perform(put("/coupon/shops/0/couponactivities/1").contentType("application/json;charset=UTF-8")
                .header("authorization", adminToken).content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 删除优惠活动
     * @throws Exception
     */
    @Test
    public void delCouponActivity()throws Exception{
        mvc.perform(delete("/coupon/shops/0/couponactivities/0").contentType("application/json;charset=UTF-8")
                .header("authorization", adminToken))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.RESOURCE_ID_NOTEXIST.getCode()))
                .andDo(MockMvcResultHandlers.print());

        mvc.perform(delete("/coupon/shops/1/couponactivities/1").contentType("application/json;charset=UTF-8")
                .header("authorization", shopToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode()))
                .andDo(MockMvcResultHandlers.print());

        mvc.perform(delete("/coupon/shops/0/couponactivities/2").contentType("application/json;charset=UTF-8")
                .header("authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.COUPONACT_STATENOTALLOW.getCode()))
                .andDo(MockMvcResultHandlers.print());

        mvc.perform(delete("/coupon/shops/0/couponactivities/1").contentType("application/json;charset=UTF-8")
                .header("authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 上线优惠活动
     * @throws Exception
     */
    @Test
    public void onlineCouponActivity()throws Exception{
        mvc.perform(put("/coupon/shops/0/couponactivities/1/onshelves").contentType("application/json;charset=UTF-8")
                .header("authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 下线优惠活动
     * @throws Exception
     */
    @Test
    public void offlineCouponActivity()throws Exception{
        mvc.perform(put("/coupon/shops/0/couponactivities/1/offshelves").contentType("application/json;charset=UTF-8")
                .header("authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void addSKUToActivity() throws Exception {
        List<Long> body=new ArrayList<>();
        body.add((long)275);
        body.add((long)276);

        /* 加入SKU */
        byte[] responseBuffer = null;
        mvc.perform(post("/coupon/shops/0/couponactivities/5/skus").contentType("application/json;charset=UTF-8")
                .header("authorization",adminToken).content(body.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andDo(MockMvcResultHandlers.print());

        /* 重复加入SKU */
        mvc.perform(post("/coupon/shops/0/couponactivities/5/skus").contentType("application/json;charset=UTF-8")
                .header("authorization",adminToken).content(body.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.COUPONACT_STATENOTALLOW.getCode()))
                .andDo(MockMvcResultHandlers.print());


        /* SKU不是自己的 */
        mvc.perform(post("/coupon/shops/1/couponactivities/5/skus").contentType("application/json;charset=UTF-8")
                .header("authorization",shopToken).content(body.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode()))
                .andDo(MockMvcResultHandlers.print());


        /* 活动上线中 */
        mvc.perform(post("/coupon/shops/0/couponactivities/6/skus").contentType("application/json;charset=UTF-8")
                .header("authorization",adminToken).content(body.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.COUPONACT_STATENOTALLOW.getCode()))
                .andDo(MockMvcResultHandlers.print());


        /* 活动不是自己的 */
        mvc.perform(post("/coupon/shops/1/couponactivities/5/skus").contentType("application/json;charset=UTF-8")
                .header("authorization",shopToken).content(body.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode()))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 移除SKU
     * @throws Exception
     */
    @Test
    public void removeSkuFromActivity() throws Exception {
        mvc.perform(delete("/coupon//shops/0/couponspus/1").contentType("application/json;charset=UTF-8")
                .header("authorization",adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andDo(MockMvcResultHandlers.print());
    }
}
