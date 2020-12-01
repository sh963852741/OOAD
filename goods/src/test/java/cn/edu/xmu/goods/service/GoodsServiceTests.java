package cn.edu.xmu.goods.service;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GoodsServiceTests {

    @Autowired
    private GoodsService goodsService;

    @Test
    void getSkuStates(){}

    @Test
    void getAllSkus(){}

    @Test
    void getDetailSku(){}

    @Test
    void deleteSku(){}

    @Test
    void changeSku(){}

    @Test
    void getDetailSpu(){}

    @Test
    void getSimpleSpu(){}

    @Test
    void newSpu(){}

    @Test
    void changeSpu(){}

    @Test
    void onshelfSpu(){}

    @Test
    void offshelfSpu(){}

    @Test
    void newFloatPrice(){}

    @Test
    void deleteFloatPrice(){}

    @Test
    void addSpuToCategory(){}

    @Test
    void removeSpuFromCategory(){}

    @Test
    void goodsBrandTest(){}

    @Test
    void addSkuToSpu(){}
}
