package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.vo.CategoryVo;
import cn.edu.xmu.goods.service.CategoryService;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.sun.mail.iap.Response;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/goods", produces = "application/json;charset=UTF-8")
public class CategoryController {

    @Autowired
    HttpServletResponse httpServletResponse;

    @Autowired
    CategoryService categoryService;

    /**
     * 查询商品分类关系
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "查询商品分类关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id", value = "种类id",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("/categories/{id}/subcategories")
    public Object selectCategories(@PathVariable Long id){
        ReturnObject ret=categoryService.getSubCategories(id);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 管理员新增商品类目
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     * modifiedBy XuYue   2020/12 10:08
     */
    @ApiOperation(value = "管理员新增商品类目")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id", value = "种类id",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("/shops/{shopId}/categories/{id}/subcategories")
    @Audit
    public Object addCategories(@PathVariable("id") Long id,@Valid @RequestBody CategoryVo vo, BindingResult bindingResult){
        var res = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(res != null){
            return res;
        }

        ReturnObject ret=categoryService.newCategory(id,vo.getName());

        if (ret.getCode() == ResponseCode.OK){
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
        }
        return Common.decorateReturnObject(ret);
    }

    /**
     * 管理员修改商品类目
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员修改商品类目")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value = "种类id",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PutMapping("/shops/{shopId}/categories/{id}")
    @Audit
    public Object changeCategories(@PathVariable("id") Long id, @Valid @RequestBody CategoryVo vo, BindingResult bindingResult){
        var res = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(res != null){
            return res;
        }

        ReturnObject ret=categoryService.changeCategory(id,vo.getName());
        return Common.decorateReturnObject(ret);
    }

    /**
     * 管理员删除商品类目
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员删除商品类目")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value = "种类id",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @DeleteMapping("/shops/{shopId}/categories/{id}")
    @Audit
    public Object deleteCategories(@PathVariable("id") Long id){
        ReturnObject ret=categoryService.deleteCategoryById(id);
        return Common.decorateReturnObject(ret);
    }

}
