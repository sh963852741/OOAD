package cn.edu.xmu.ooad.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.model.vo.BrandVo;
import cn.edu.xmu.ooad.service.BrandService;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@Controller
public class BrandController {
    @Autowired
    BrandService brandService;
    @Autowired
    private HttpServletResponse httpServletResponse;
    @Audit
    @DeleteMapping("shops/{shopId}/brands/{id}")
    public Object deleteBrand(@PathVariable("id") Long id){
        ReturnObject returnObject = brandService.deleteBrand(id);
        return Common.decorateReturnObject(returnObject);
    }
    @Audit
    @PutMapping("shops/{shopId}/brands/{id}")
    public Object setBrand(@PathVariable Long id, @RequestBody BrandVo vo, BindingResult bindingResult){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            return returnObject;
        }
        ReturnObject<Object> retObject = brandService.setBrand(id, vo);
        return Common.decorateReturnObject(retObject);
    }
    @Audit
    @PostMapping("shops/{id}/brands")
    public Object addBrand( @RequestBody BindingResult bindingResult,BrandVo vo) {
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            return returnObject;
        }
        ReturnObject<VoObject> retObject = brandService.addBrand(vo);
        httpServletResponse.setStatus(HttpStatus.CREATED.value());
        return Common.decorateReturnObject(retObject);
    }

    @Audit
    @GetMapping("brands")
    public Object selectAllRoles(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {

        page = (page == null)?1:page;
        pageSize = (pageSize == null)?10:pageSize;

        ReturnObject<PageInfo<VoObject>> returnObject =  brandService.selectAllBrands(page, pageSize);
        return Common.getPageRetObject(returnObject);
    }

}
