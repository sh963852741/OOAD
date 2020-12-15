package cn.edu.xmu.flashsale.controller;

import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class FlashSaleControllerTest {
    private static String adminToken;
    private static String shopToken;
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mObjectMapper;

    @BeforeAll
    private static void login(){
        JwtHelper jwtHelper = new JwtHelper();
        adminToken =jwtHelper.createToken(1L,0L, 3600);
        shopToken =jwtHelper.createToken(59L,1L, 3600);
    }

    /**
     * 在时段"0"下新建秒杀活动，然后删除活动
     */
    @Test
    public void addFlashSaleActivity1() throws Exception{
        LocalDate dateTime = LocalDate.now().plusDays(3);

        String requestJson = "{\"flashDate\":\"" + dateTime + "\"}";

        String response = mvc.perform(post("/flashsale/timesegments/0/flashsales").contentType("application/json;charset=UTF-8").content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data").isMap())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = mObjectMapper.readTree(response).findPath("data").get("id");
        int insertId = jsonNode.asInt();

        mvc.perform(delete("/flashsale/flashsales/"+insertId).contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(MockMvcResultHandlers.print());

        mvc.perform(delete("/flashsale/flashsales/"+insertId).contentType("application/json;charset=UTF-8"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.RESOURCE_ID_NOTEXIST.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 新建今天的活动，应当阻止；
     */
    @Test
    public void addFlashSaleActivity2() throws Exception{
        LocalDate dateTime = LocalDate.now();

        String requestJson = "{\"flashDate\":\"" + dateTime + "\"}";
        mvc.perform(post("/flashsale/timesegments/0/flashsales").contentType("application/json;charset=UTF-8").content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.FIELD_NOTVALID.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 获取秒杀活动
     * @throws Exception
     */
    @Test
    @Order(1)
    public void getFlashSaleActivity1()throws Exception {
        mvc.perform(get("/flashsale/flashsales/current?page=1&pageSize=10").contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[?(@.id == 8)].product.id").value(275))
                .andExpect(jsonPath("$[?(@.id == 7)].product.id").value(290))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 向秒杀活动中加入SKU、删除SKU
     * @throws Exception
     */
    @Test
    public void addSKUToActivity()throws Exception {
        LocalDate dateTime = LocalDate.now().plusDays(3);

        String requestJson = "{\"flashDate\":\"" + dateTime + "\"}";
        byte[] responseBuffer = null;
        String response = mvc.perform(post("/flashsale/timesegments/0/flashsales")
                .contentType("application/json;charset=UTF-8").content(requestJson).header("authorization",adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data").isMap())
                .andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = mObjectMapper.readTree(response).findPath("data").get("id");
        int insertActivityId = jsonNode.asInt();

        requestJson = "{\"skuId\": 280,\"price\": 2365,\"quantity\": 36}";
        response = mvc.perform(post("/flashsale/flashsales/" + insertActivityId + "/flashitems")
                .contentType("application/json;charset=UTF-8").content(requestJson).header("authorization",adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data").isMap())
                .andReturn().getResponse().getContentAsString();

        jsonNode = mObjectMapper.readTree(response).findPath("data").get("id");
        int insertItemId = jsonNode.asInt();

        /* 重复加入 */
//        requestJson = "{\"skuId\": 280,\"price\": 2365,\"quantity\": 36}";
//        responseBuffer =webClient.post().uri("flashsales/" + insertId + "/flashitems").bodyValue(requestJson).exchange().expectHeader().contentType("application/json;charset=UTF-8")
//                .expectBody()
//                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
//                .jsonPath("$.errmsg").isEqualTo("成功")
//                .jsonPath("$.data").isMap()
//                .returnResult()
//                .getResponseBodyContent();

        mvc.perform(delete("/flashsale/flashsales/"+ insertActivityId +"/flashitems/"+ insertItemId)
                .contentType("application/json;charset=UTF-8").header("authorization",adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    @Order(Integer.MAX_VALUE - 1)
    public void delFlashItem() throws Exception {
        // region 删除今天的秒杀商品
        mvc.perform(delete("/flashsale/flashsales/3/flashitems/5")
                .contentType("application/json;charset=UTF-8").header("authorization",adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andReturn().getResponse().getContentAsString();
        mvc.perform(delete("/flashsale/flashsales/3/flashitems/6")
                .contentType("application/json;charset=UTF-8").header("authorization",adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andReturn().getResponse().getContentAsString();
        mvc.perform(delete("/flashsale/flashsales/4/flashitems/7")
                .contentType("application/json;charset=UTF-8").header("authorization",adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andReturn().getResponse().getContentAsString();
        mvc.perform(delete("/flashsale/flashsales/4/flashitems/8")
                .contentType("application/json;charset=UTF-8").header("authorization",adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andReturn().getResponse().getContentAsString();
        // endregion

        mvc.perform(get("/flashsale/flashsales/current?page=1&pageSize=10")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(Integer.MAX_VALUE)
    public void delFlashSale()throws Exception {
        for(int i=1;i<=4;++i){
            mvc.perform(delete("/flashsale/flashsales/" + i).contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json;charset=UTF-8"))
                    .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                    .andExpect(jsonPath("$.errmsg").value("成功"))
                    .andExpect(jsonPath("$.data").doesNotExist())
                    .andDo(MockMvcResultHandlers.print());
        }

        /*
          继续删，应当报错
         */
        mvc.perform(delete("/flashsale/flashsales/0").contentType("application/json;charset=UTF-8"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.RESOURCE_ID_NOTEXIST.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(MockMvcResultHandlers.print());
    }
}
