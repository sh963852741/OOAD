package cn.edu.xmu.ooad;

import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
public class PresaleTest {

    private WebTestClient webClient;

    private static String adminToken;
    private static String shopToken;

    public PresaleTest(){
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
     * 正常加入一个预售活动
     */
    @Test
    public void addPresaleActivity1(){
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        byte[] responseBuffer = null;
        String requireJson = "{ \"name\": \"预售活动\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/shops/1/spus/290/presales")
                .header("authorization", shopToken)
                .bodyValue(requireJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isMap()
                .jsonPath("$.data.id").isNumber()
                .jsonPath("$.data.name").isEqualTo("预售活动")
                .jsonPath("$.data.advancePayPrice").isEqualTo(20)
                .jsonPath("$.data.restPayPrice").isEqualTo(3000)
                .jsonPath("$.data.quantity").isEqualTo(10)
                .jsonPath("$.data.payTime").isEqualTo(payTime)
                .jsonPath("$.data.beginTime").isEqualTo(beginTime)
                .jsonPath("$.data.endTime").isEqualTo(endTime)
                .jsonPath("$.data.gmtModified").isEmpty()
                .jsonPath("$.data.gmtCreate").isNotEmpty()
                .jsonPath("$.data.state").isNumber()
                .jsonPath("$.data.goodsSpu.shopId").doesNotExist()
                .jsonPath("$.data.goodsSpu.id").isEqualTo(290)
                .jsonPath("$.data.shop.id").isEqualTo(1)
                .jsonPath("$.data.shop.name").isEqualTo("张三商铺")
                .returnResult()
                .getResponseBodyContent();

        try {
            String response = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
