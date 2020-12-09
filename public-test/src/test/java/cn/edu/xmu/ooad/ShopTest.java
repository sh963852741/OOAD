package cn.edu.xmu.ooad;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class ShopTest {

    private WebTestClient webClient;

    private static String adminToken;
    private static String shopToken;

    public ShopTest() {
        this.webClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();
    }

    @BeforeAll
    private static void login() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, 0L, 3600);
        shopToken = jwtHelper.createToken(59L, 1L, 3600);
    }

    /**
     * 获取商铺的所有状态
     */
    @Test
    public void getAllState() {
        byte[] responseBuffer = null;
        WebTestClient.RequestHeadersSpec res = webClient.get().uri("/shop/shops/states");
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 新建商铺(正常)
     */
    @Test
    public void applyShop() {
        byte[] responseBuffer = null;
        String requestJson = "{\"name\": \"张三商铺\"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/shop/shops")
                .header("authorization", shopToken)
                .bodyValue(requestJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();
        try {
            String response = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新建商铺(名称是空格)
     */
    @Test
    public void applyShop_null() {
        byte[] responseBuffer = null;
        String requestJson = "{\"name\": \"    \"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/shop/shops")
                .header("authorization", shopToken)
                .bodyValue(requestJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();
        try {
            String response = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 卖家重复申请店铺
     */
    @Test
    public void applyShop_again() {
        byte[] responseBuffer = null;
        String requestJson = "{\"name\": \"张三商铺\"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/shop/shops")
                .header("authorization", shopToken)
                .bodyValue(requestJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();
        try {
            String response = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /**
     * 正常流程修改商铺
     */
    @Test
    public void modifyShop() {
        byte[] responseBuffer = null;
        String requestJson = "{\"name\": \"已经改了哈\"}";
        WebTestClient.RequestHeadersSpec res = webClient.put().uri("/shop/shops/1")
                .header("authorization", shopToken)
                .bodyValue(requestJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();
        try {
            String response = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改未通过审核的商铺
     */
    @Test
    public void modifyShop_unaudit() {
        byte[] responseBuffer = null;
        String requestJson = "{\"name\": \"它应该没通过审核吧\"}";
        WebTestClient.RequestHeadersSpec res = webClient.put().uri("/shop/shops/2")
                .header("authorization", shopToken)
                .bodyValue(requestJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();
        try {
            String response = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改过程不传名称
     */
    @Test
    public void modifyShop_null() {
        byte[] responseBuffer = null;
        String requestJson = "{\"name\": \"\"}";
        WebTestClient.RequestHeadersSpec res = webClient.put().uri("/shop/shops/1")
                .header("authorization", shopToken)
                .bodyValue(requestJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();
        try {
            String response = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改商铺(多传一个ID)
     */
    @Test
    public void modifyShop_ID() {
        byte[] responseBuffer = null;
        String requestJson = "{\"name\": \"这个想改ID\",\"id\":\"123\"}";
        WebTestClient.RequestHeadersSpec res = webClient.put().uri("/shop/shops/1")
                .header("authorization", shopToken)
                .bodyValue(requestJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();
        try {
            String response = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /**
     * 修改商铺(多传一个state)
     */
    @Test
    public void modifyShop_STATE() {
        byte[] responseBuffer = null;
        String requestJson = "{\"name\": \"这个想改state\",\"state\":\"3\"}";
        WebTestClient.RequestHeadersSpec res = webClient.put().uri("/shop/shops/1")
                .header("authorization", shopToken)
                .bodyValue(requestJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();
        try {
            String response = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 审核商铺
     */
    @Test
    public void auditShop() {
        byte[] responseBuffer = null;
        String requestJson = "{\"conclusion\": true}";
        WebTestClient.RequestHeadersSpec res = webClient.put().uri("/shop/shops/0/newshops/1/audit")
                .header("authorization", adminToken)
                .bodyValue(requestJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();
        try {
            String response = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 审核参数不合法
     */
    @Test
    public void auditShop_abnormal() {
        byte[] responseBuffer = null;
        String requestJson = "{\"conclusion\": 1223}";
        WebTestClient.RequestHeadersSpec res = webClient.put().uri("/shop/shops/0/newshops/1/audit")
                .header("authorization", adminToken)
                .bodyValue(requestJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();
        try {
            String response = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上架商铺
     */
    @Test
    public void onshelfShop() {
        byte[] responseBuffer = null;
        String requestJson = "{\"conclusion\": true}";
        WebTestClient.RequestHeadersSpec res = webClient.put().uri("/shop/shops/1/onshelves")
                .header("authorization", adminToken)
                .bodyValue(requestJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();
        try {
            String response = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下架商铺
     */
    @Test
    public void offshelfShop() {
        byte[] responseBuffer = null;
        String requestJson = "{\"conclusion\": true}";
        WebTestClient.RequestHeadersSpec res = webClient.put().uri("/shop/shops/1/offshelves")
                .header("authorization", adminToken)
                .bodyValue(requestJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();
        try {
            String response = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭商铺
     */
    @Test
    public void deleteShop() {
        byte[] responseBuffer = null;
        WebTestClient.RequestHeadersSpec res = webClient.delete().uri("/shop/shops/1/offshelves")
                .header("authorization", adminToken);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();
        try {
            String response = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}
