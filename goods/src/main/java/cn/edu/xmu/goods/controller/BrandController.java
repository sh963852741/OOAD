package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.bo.Brand;
import cn.edu.xmu.goods.model.vo.BrandVo;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.goods.service.BrandService;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class BrandController {

    @Autowired
    BrandService brandService;
    @Autowired
    private HttpServletResponse httpServletResponse;

    @DeleteMapping("/shops/{shopId}/brands/{id}")
    public Object deleteBrand(@PathVariable("id") Long id){
        ReturnObject returnObject = brandService.deleteBrand(id);
        return Common.decorateReturnObject(returnObject);
    }


    /**
     * 修改角色信息
     *
     * @author 24320182203281 王纬策
     * @param id 角色id
     * @param vo 角色视图
     * @param bindingResult 校验数据
     * @return Object 角色返回视图
     * createdBy 王纬策 2020/11/04 13:57
     * modifiedBy 王纬策 2020/11/7 19:20
     */
    @ApiOperation(value = "修改角色信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "RoleVo", name = "vo", value = "可修改的用户信息", required = true)
    })
    @PutMapping("/shops/{shopId}/brands/{id}")
    public Object updateRole(@PathVariable("shopId") Long shopId, @PathVariable("id") Long id, @Validated @RequestBody BrandVo vo, BindingResult bindingResult,
                             @Depart @ApiIgnore @RequestParam(required = false) Long DepartId)
    {
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            return returnObject;
        }

        if(shopId.equals(DepartId)){
            Brand brand = vo.createBrand();
            brand.setId(id);

            ReturnObject<Object> retObject = brandService.setBrand(id,vo);
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return Common.decorateReturnObject(retObject);
        }
        else{
            return Common.getNullRetObj(new ReturnObject<>(ResponseCode.FIELD_NOTVALID, String.format("店铺id不匹配：" + shopId)), httpServletResponse);
        }


    }


    /**
     * 新增一个品牌
     * @param vo 品牌视图
     * @param bindingResult 校验错误
     * @param userId 当前用户id
     * @return Object 品牌返回视图
     * createdBy 徐悦 2020/11/20 23：39
     */
    @ApiOperation(value = "新增品牌", produces = "application/json")
    @PostMapping("/shops/{id}/brands")
    public Object insertBrand(@Validated @RequestBody BrandVo vo, BindingResult bindingResult,
                             @LoginUser @ApiIgnore @RequestParam(required = false) Long userId,
                             @Depart @ApiIgnore @RequestParam(required = false) Long departId) {
        //校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            return returnObject;
        }

        ReturnObject<VoObject> retObject = brandService.addBrand(vo);
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return Common.decorateReturnObject(retObject);
    }


    @GetMapping("/brands")
    public Object selectAllRoles(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {

        page = (page == null)?1:page;
        pageSize = (pageSize == null)?10:pageSize;

        ReturnObject<PageInfo<VoObject>> returnObject =  brandService.selectAllBrands(page, pageSize);
        return Common.getPageRetObject(returnObject);
    }
}
