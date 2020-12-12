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
                .baseUrl("http://localhost:8907")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();
    }

//    @BeforeAll
//    private static void login(){
//        JwtHelper jwtHelper = new JwtHelper();
//        adminToken =jwtHelper.createToken(1L,0L, 3600);
//        shopToken =jwtHelper.createToken(59L,1L, 3600);
//    }

    String getPath(String a){
        StringBuffer p = new StringBuffer("/goods");
        p.append(a);
        return p.toString();
    }

    @Test
    public void getSkuStates() throws Exception{
        byte[] responseBuffer = null;
        WebTestClient.RequestHeadersSpec res = webClient.get().uri(getPath("/skus/states"));
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
        WebTestClient.RequestHeadersSpec res = webClient.get().uri(getPath("/skus"));
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data.total").isEqualTo(406)
                .jsonPath("$.data.pages").isEqualTo(41)
                .returnResult()
                .getResponseBodyContent();
    }
    @Test
    public void getkuById(){
        byte[] responseBuffer = null;
        WebTestClient.RequestHeadersSpec res = webClient.get().uri(getPath("/skus/300"));
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data.name").isEqualTo("+")
                .jsonPath("$.data.spu.id").isEqualTo("300")
                .jsonPath("$.data.spu.name").isEqualTo("金和汇景•赵紫云•粉彩绣球瓷瓶")
                .returnResult()
                .getResponseBodyContent();
    }

    @Test
    public void addSkuToSpu() throws Exception{
        byte[] responseBuffer = null;
        String bodyValue = "{\n" +
                "  \"sn\": \"string\",\n" +
                "  \"name\": \"测试商品\",\n" +
                "  \"originalPrice\": 100,\n" +
                "  \"configuration\": \"ddddd\",\n" +
                "  \"weight\": 10,\n" +
                "  \"imageUrl\": null,\n" +
                "  \"inventory\": 100,\n" +
                "  \"detail\": \"aaaaa\"\n" +
                "}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri(getPath("/shops/0/spus/300/skus")).bodyValue(bodyValue);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data.name").isEqualTo("+")
                .jsonPath("$.data.spu.id").isEqualTo("300")
                .jsonPath("$.data.spu.name").isEqualTo("金和汇景•赵紫云•粉彩绣球瓷瓶")
                .returnResult()
                .getResponseBodyContent();
    }
}
