package cn.edu.xmu.oomall.other;

import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.webservices.client.AutoConfigureWebServiceClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Consumer;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 2 * @author: LiangJi3229
 * 3 * @date: 2020/12/5 下午4:02
 * 4 * 启动gateway、service以进行测试，否则无法获得成功的返回结果
 */
@SpringBootTest(classes = OtherGatewayApplication.class)
@Slf4j
public class GatewayTest {
    private final WebTestClient webClient;
    public GatewayTest(){
        this.webClient=WebTestClient.bindToServer().baseUrl("http://localhost:8080")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();
    }
    @Test
    public void testTokenNeededSuccessWithOldToken(){
        String token=createToken(5);
        webClient.get().uri("/users").header("authorization",token).exchange().
                expectStatus().isOk().expectHeader().value("authorization",s -> {
                    assertNotEquals(s,token);
        });
    }
    @Test
    public void testTokenNeededSuccessWithNewToken(){
        String token=createToken(1000000);
        webClient.get().uri("/users").header("authorization",token).exchange().
                expectStatus().isOk().expectHeader().value("authorization",s -> {
            assertEquals(s,token);
        });
    }
    @Test
    public void testTokenNeededWithWrongToken(){
        webClient.get().uri("/users").header("authorization","test").exchange().
                expectStatus().isUnauthorized().expectBody().jsonPath("$.errno").isEqualTo(ResponseCode.AUTH_INVALID_JWT.getCode());
    }
    @Test
    public void testTokenNeededFailedTimeExpired() throws InterruptedException {
        String token=createToken(1);
        Thread.sleep(2000);
        webClient.get().uri("/users").header("authorization",token).exchange().
                expectStatus().isUnauthorized().expectBody().
                jsonPath("$.errno").isEqualTo(ResponseCode.AUTH_INVALID_JWT.getCode());

    }
    @Test
    public void testTokenUnneededWithToken(){
        String token=createToken(1);
        String requiredJson="{\n" +
                "\t\"userName\": \"ThisNameCantExist\",\n" +
                "    \"password\": \"wrongPass\"\n" +
                "}";
        webClient.post().uri("/users/login").header("authorization",token).bodyValue(requiredJson).
                exchange().expectBody().jsonPath("$.errno").isEqualTo(700);
    }
    @Test
    public void testTokenUnneededNormally(){
        String requiredJson="{\n" +
                "\t\"userName\": \"ThisNameCantExist\",\n" +
                "    \"password\": \"wrongPass\"\n" +
                "}";
        webClient.post().uri("/users/login").bodyValue(requiredJson).
                exchange().expectBody().jsonPath("$.errno").isEqualTo(700);
    }
    @Test
    public void testTokenWithWrongDepartId(){
        String token=createWrongToken(0L,1);
        webClient.get().uri("/users").header("authorization",token).exchange().
                expectStatus().isUnauthorized().expectBody().
                jsonPath("$.errno").isEqualTo(ResponseCode.AUTH_INVALID_JWT.getCode());
    }
    String createToken(int expireTime){
        JwtHelper jwtHelper=new JwtHelper();
        String token=jwtHelper.createToken(1L,-2L,expireTime);
        return token;
    }
    String createWrongToken(Long departId,int expireTime){
        JwtHelper jwtHelper=new JwtHelper();
        String token=jwtHelper.createToken(1L,departId,expireTime);
        return token;
    }

}
