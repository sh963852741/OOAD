package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.bo.Good;
import cn.edu.xmu.goods.model.bo.PresaleActivity;
import cn.edu.xmu.goods.model.bo.Sku;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GoodsControllerTest {

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
     * 获取SPU的状态（以后应当改为SKU的状态）
     * @throws Exception
     */
    @Test
    public void getSPUState() throws Exception {
        ResultActions response = mvc.perform(get("/goods/spus/states")
                .contentType("application/json;charset=UTF-8"));
        String responseString = response.andExpect((status().isOk()))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data.size()").value(Good.State.values().length))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].code").isNumber())
                .andExpect(jsonPath("$.data[0].name").isString())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 根据SPU ID获取SKU列表（有SKU）
     * @throws Exception
     */
    @Test
    public void getSKUBySPUId1() throws Exception {
        ResultActions response = mvc.perform(get("/goods/skus")
                .queryParam("spuId", "601")
                .queryParam("page", "1")
                .queryParam("pageSize", "3")
                .contentType("application/json;charset=UTF-8"));
        String responseString = response.andExpect((status().isOk()))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data.page").value(1))
                .andExpect(jsonPath("$.data.pageSize").value(3))
                .andExpect(jsonPath("$.data.list[0].name").isString())
                .andExpect(jsonPath("$.data.list[0].skuSn").doesNotExist())
                .andExpect(jsonPath("$.data.list[0].imageUrl").isString())
                .andExpect(jsonPath("$.data.list[0].inventory").isNumber())
                .andExpect(jsonPath("$.data.list[0].originalPrice").isNumber())
                .andExpect(jsonPath("$.data.list[0].price").isNumber())
                .andExpect(jsonPath("$.data.list[0].disable").isNumber())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 根据SPU ID获取SKU列表（无SKU）
     * @throws Exception
     */
    @Test
    public void getSKUBySPUId2() throws Exception {
        ResultActions response = mvc.perform(get("/goods/skus")
                .queryParam("spuId", "2039")
                .queryParam("page", "1")
                .queryParam("pageSize", "3")
                .contentType("application/json;charset=UTF-8"));
        String responseString = response.andExpect((status().isOk()))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data.page").value(1))
                .andExpect(jsonPath("$.data.pageSize").value(3))
                .andExpect(jsonPath("$.data.list[0]").doesNotExist())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 根据SPU SN获取SKU列表（有SKU）
     * @throws Exception
     */
    @Test
    public void getSKUBySPUSn1() throws Exception {
        ResultActions response = mvc.perform(get("/goods/skus")
                .queryParam("spuSn", "bcl-b0003")
                .queryParam("page", "1")
                .queryParam("pageSize", "3")
                .contentType("application/json;charset=UTF-8"));
        String responseString = response.andExpect((status().isOk()))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data.page").value(1))
                .andExpect(jsonPath("$.data.pageSize").value(3))
                .andExpect(jsonPath("$.data.list[0].name").isString())
                .andExpect(jsonPath("$.data.list[0].skuSn").doesNotExist())
                .andExpect(jsonPath("$.data.list[0].imageUrl").isString())
                .andExpect(jsonPath("$.data.list[0].inventory").isNumber())
                .andExpect(jsonPath("$.data.list[0].originalPrice").isNumber())
                .andExpect(jsonPath("$.data.list[0].price").isNumber())
                .andExpect(jsonPath("$.data.list[0].disable").isNumber())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 根据SPU SN获取SKU列表（无SKU）
     * @throws Exception
     */
    @Test
    public void getSKUBySPUSn2() throws Exception {
        ResultActions response = mvc.perform(get("/goods/skus")
                .queryParam("spuSn", "QOSR-b0003")
                .queryParam("page", "1")
                .queryParam("pageSize", "3")
                .contentType("application/json;charset=UTF-8"));
        String responseString = response.andExpect((status().isOk()))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data.page").value(1))
                .andExpect(jsonPath("$.data.pageSize").value(3))
                .andExpect(jsonPath("$.data.list[0]").doesNotExist())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 根据SKU SN获取SKU列表（无SKU）  以后要改成有SKU的情况
     * @throws Exception
     */
    @Test
    public void getSKUBySKUSn1() throws Exception {
        ResultActions response = mvc.perform(get("/goods/skus")
                .queryParam("skuSn", "bcl-b0003")
                .queryParam("page", "1")
                .queryParam("pageSize", "3")
                .contentType("application/json;charset=UTF-8"));
        String responseString = response.andExpect((status().isOk()))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data.page").value(1))
                .andExpect(jsonPath("$.data.pageSize").value(3))
                .andExpect(jsonPath("$.data.list").isArray())
                .andExpect(jsonPath("$.data.list[0]").doesNotExist())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 根据SKU SN获取SKU列表（无SKU）
     * @throws Exception
     */
    @Test
    public void getSKUBySKUSn2() throws Exception {
        ResultActions response = mvc.perform(get("/goods/skus")
                .queryParam("skuSn", "bcl-b0003")
                .queryParam("page", "1")
                .queryParam("pageSize", "3")
                .contentType("application/json;charset=UTF-8"));
        String responseString = response.andExpect((status().isOk()))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data.page").value(1))
                .andExpect(jsonPath("$.data.pageSize").value(3))
                .andExpect(jsonPath("$.data.list").isArray())
                .andExpect(jsonPath("$.data.list[0]").doesNotExist())
                .andReturn().getResponse().getContentAsString();
    }


    @Test
    public void getSKUById()throws Exception{
        ResultActions response = mvc.perform(get("/goods/skus/281")
                .contentType("application/json;charset=UTF-8"));
        String responseString = response.andExpect((status().isOk()))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.name").isString())
                .andExpect(jsonPath("$.data.skuSn").doesNotExist())
                .andExpect(jsonPath("$.data.detail").isString())
                .andExpect(jsonPath("$.data.imageUrl").isString())
                .andExpect(jsonPath("$.data.originalPrice").isNumber())
                .andExpect(jsonPath("$.data.price").isNumber())
                .andExpect(jsonPath("$.data.inventory").isNumber())
                .andExpect(jsonPath("$.data.state").isNumber())
                .andExpect(jsonPath("$.data.configuration").isString())
                .andExpect(jsonPath("$.data.weight").isNumber())
                .andExpect(jsonPath("$.data.gmtCreate").isString())
                .andExpect(jsonPath("$.data.gmtModified").isString())
                .andExpect(jsonPath("$.data.spu.id").isNumber())
                .andExpect(jsonPath("$.data.spu.name").isString())
                .andExpect(jsonPath("$.data.spu.goodsSn").isString())
                .andExpect(jsonPath("$.data.spu.detail").isString())
                .andExpect(jsonPath("$.data.spu.imageUrl").isString())
                .andExpect(jsonPath("$.data.spu.gmtCreate").isString())
                .andExpect(jsonPath("$.data.spu.gmtModified").isString())
                .andExpect(jsonPath("$.data.spu.disable").isString())
                .andReturn().getResponse().getContentAsString();
    }
}
