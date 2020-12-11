package cn.edu.xmu.ooad;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class FlashSaleTest {
    @Autowired
    private ObjectMapper mObjectMapper;

    private WebTestClient webClient;

    private static String adminToken;
    private static String shopToken;

    public FlashSaleTest(){
        this.webClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();
    }

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
        byte[] responseBuffer = null;
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/timesegments/0/flashsales").bodyValue(requestJson);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isMap()
                .returnResult()
                .getResponseBodyContent();

        String response = new String(responseBuffer, "utf-8");

        JsonNode jsonNode = mObjectMapper.readTree(response).findPath("data").get("id");
        int insertId = jsonNode.asInt();

        responseBuffer= webClient.delete().uri("/flashsales/"+insertId)
                .exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").doesNotExist()
                .returnResult()
                .getResponseBodyContent();

        responseBuffer= webClient.delete().uri("/flashsales/"+insertId)
                .exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .jsonPath("$.data").doesNotExist()
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 新建今天的活动，应当阻止；
     */
    @Test
    public void addFlashSaleActivity2() throws Exception{
        LocalDate dateTime = LocalDate.now();

        String requestJson = "{\"flashDate\":\"" + dateTime + "\"}";
        byte[] responseBuffer = null;
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/timesegments/0/flashsales").bodyValue(requestJson);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(503)
                .jsonPath("$.data").doesNotExist()
                .returnResult()
                .getResponseBodyContent();

        String response = new String(responseBuffer, "utf-8");
    }

    /**
     * 获取秒杀活动
     * @throws Exception
     */
    @Test
    public void getFlashSaleActivity1()throws Exception {
        byte[] responseBuffer = null;
        WebTestClient.RequestHeadersSpec res = webClient.get().uri("/flashsales/current?page=1&pageSize=10");
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8").expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$[?(@.id == 4)].product.id").isEqualTo(275)
                .jsonPath("$[?(@.id == 3)].product.id").isEqualTo(290)
                .returnResult()
                .getResponseBodyContent();

        String response = new String(responseBuffer, "utf-8");
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
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/timesegments/0/flashsales").bodyValue(requestJson);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isMap()
                .returnResult()
                .getResponseBodyContent();

        String response = new String(responseBuffer, "utf-8");

        JsonNode jsonNode = mObjectMapper.readTree(response).findPath("data").get("id");
        int insertActivityId = jsonNode.asInt();

        requestJson = "{\"skuId\": 280,\"price\": 2365,\"quantity\": 36}";
        responseBuffer = webClient.post().uri("flashsales/" + insertActivityId + "/flashitems").bodyValue(requestJson).exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isMap()
                .returnResult()
                .getResponseBodyContent();

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

        webClient.delete().uri("flashsales/"+ insertActivityId +"/flashitems/"+ insertItemId).exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").doesNotExist()
                .returnResult()
                .getResponseBodyContent();
    }
}
