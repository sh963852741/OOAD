package cn.edu.xmu.ooad;

import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
public class GoodsTest {

    private WebTestClient webClient;

    private static String adminToken;
    private static String shopToken;

    public GoodsTest(){
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

    @Test
    public void getSkuStates(){
        byte[] responseBuffer = null;
        WebTestClient.RequestHeadersSpec res = webClient.get().uri("/skus/state");
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();
    }
    @Test
    public void getAllSkus(){
        byte[] responseBuffer = null;
        WebTestClient.RequestHeadersSpec res = webClient.get().uri("/skus/state");
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();
    }

}
