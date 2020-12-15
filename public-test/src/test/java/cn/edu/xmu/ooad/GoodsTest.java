package cn.edu.xmu.ooad;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import com.auth0.jwt.JWTCreator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

@SpringBootTest
public class GoodsTest {

    private WebTestClient webClient;

    private static String adminToken;
    private static String shopToken;

    private static JwtHelper jwtHelper;

    public GoodsTest(){
        this.webClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8907")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();
        jwtHelper = new JwtHelper();
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
                .jsonPath("$.data.total").isNumber()
                .jsonPath("$.data.pages").isNumber()
                .returnResult()
                .getResponseBodyContent();
    }

    //通过id获取sku
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


    //添加sku到spu  已登录
    @Test
    public void addSkuToSpu1() throws Exception{
        byte[] responseBuffer = null;
        String token = jwtHelper.createToken(1L,0L,11222);
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
        WebTestClient.RequestHeadersSpec res = webClient.post().uri(getPath("/shops/0/spus/300/skus")).bodyValue(bodyValue).header("authorization",token);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data.name").isEqualTo("测试商品")
                .jsonPath("$.data.inventory").isEqualTo(100)
                .returnResult()
                .getResponseBodyContent();
        String response = new String(responseBuffer, "utf-8");
        ObjectMapper mapper = new ObjectMapper().registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
        JsonNode node;
        Long id = null;
        try {
            node = mapper.readTree(response);
            JsonNode leaf = node.get("data");
            if (leaf != null) {
                JsonNode temp = leaf.get("id");
                if(temp != null){
                    id = temp.asLong();
                }
            }
        } catch (IOException e) {
            id =null;
        }

        WebTestClient.RequestHeadersSpec res2 = webClient.get().uri(getPath("/skus/"+id.toString()));
        responseBuffer = res2.exchange().expectStatus().isNotFound().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();
    }


    //添加sku到spu 未登录
    @Test
    public void addSkuToSpu2() throws Exception{
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
                .jsonPath("$.errno").isEqualTo(ResponseCode.AUTH_NEED_LOGIN.getCode())
                .returnResult()
                .getResponseBodyContent();
        String response = new String(responseBuffer, "utf-8");
    }

    //添加sku到spu 登录 但是操作的sku不存在
    @Test
    public void addSkuToSpu3() throws Exception{
        byte[] responseBuffer = null;
        String token = jwtHelper.createToken(1L,0L,11222);
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
        WebTestClient.RequestHeadersSpec res = webClient.post().uri(getPath("/shops/0/spus/9000/skus")).bodyValue(bodyValue).header("authorization",token);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();
        String response = new String(responseBuffer, "utf-8");
    }


    //正常删除 快乐路径
    @Test
    public void deleteSku(){
        byte[] responseBuffer = null;
        String token = jwtHelper.createToken(1L,1L,11222);
        WebTestClient.RequestHeadersSpec res = webClient.delete().uri(getPath("/shops/1/skus/8989")).header("authorization", token);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();

        WebTestClient.RequestHeadersSpec res2 = webClient.get().uri(getPath("/skus/8989"));
        responseBuffer = res2.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();
    }

    //删除的sku不属于自己的商铺
    @Test
    public void deleteSku2(){
        byte[] responseBuffer = null;
        String token = jwtHelper.createToken(1L,1L,11222);
        WebTestClient.RequestHeadersSpec res = webClient.delete().uri(getPath("/shops/1/skus/300")).header("authorization", token);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
                .returnResult()
                .getResponseBodyContent();
    }


    //新建浮动价格
    @Test
    public void newFloatPrice(){
        byte[] responseBuffer = null;
        String token = jwtHelper.createToken(1L,1L,11222);
        String bodyValue = "{\n" +
                "  \"activityPrice\": 12,\n" +
                "  \"beginTime\": \"2020-12-15 22:55:00\",\n" +
                "  \"endTime\": \"2020-12-20 22:55:00\",\n" +
                "  \"quantity\": 10\n" +
                "}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri(getPath("/shops/1/skus/8989/floatPrices")).bodyValue(bodyValue).header("authorization", token);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data.activityPrice").isEqualTo(12)
                .jsonPath("$.data.quantity").isEqualTo(10)
                .returnResult()
                .getResponseBodyContent();

        WebTestClient.RequestHeadersSpec res2 = webClient.post().uri(getPath("/shops/1/skus/8989/floatPrices")).bodyValue(bodyValue).header("authorization", token);
        responseBuffer = res2.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.SKUPRICE_CONFLICT.getCode())
                .returnResult()
                .getResponseBodyContent();
    }

    //新建浮动价格 操作的sku不是自己的店铺的
    @Test
    public void newFloatPrice2(){
        byte[] responseBuffer = null;
        String token = jwtHelper.createToken(1L,1L,11222);
        String bodyValue = "{\n" +
                "  \"activityPrice\": 12,\n" +
                "  \"beginTime\": \"2020-12-15 22:55:00\",\n" +
                "  \"endTime\": \"2020-12-20 22:55:00\",\n" +
                "  \"quantity\": 10\n" +
                "}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri(getPath("/shops/1/skus/300/floatPrices")).bodyValue(bodyValue).header("authorization", token);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
                .returnResult()
                .getResponseBodyContent();
    }

    @Test
    public void deleteFloatPrice1(){
        byte[] responseBuffer = null;
        String token = jwtHelper.createToken(1L,1L,11222);
        WebTestClient.RequestHeadersSpec res = webClient.delete().uri(getPath("/shops/1/floatPrices/9000")).header("authorization", token);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        WebTestClient.RequestHeadersSpec res2 = webClient.delete().uri(getPath("/shops/1/floatPrices/9000")).header("authorization", token);
        responseBuffer = res2.exchange().expectStatus().isNotFound().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST)
                .returnResult()
                .getResponseBodyContent();
    }


    //删除的floatprice 不属于自己的商店
    @Test
    public void deleteFloatPrice2(){
        byte[] responseBuffer = null;
        String token = jwtHelper.createToken(1L,1L,11222);
        WebTestClient.RequestHeadersSpec res = webClient.delete().uri(getPath("/shops/1/floatPrices/9001")).header("authorization", token);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
                .returnResult()
                .getResponseBodyContent();
    }

}
