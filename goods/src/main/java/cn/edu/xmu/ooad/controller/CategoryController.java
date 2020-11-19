package cn.edu.xmu.ooad.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.model.vo.CategoryVo;
import cn.edu.xmu.ooad.service.CategoryService;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    private HttpServletResponse httpServletResponse;
    @Audit
    @DeleteMapping("/categories/{id}")
    public Object deleteCategory(@PathVariable("id") Long id){
        ReturnObject returnObject = categoryService.deleteCategory(id);
        return Common.decorateReturnObject(returnObject);
    }
    @Audit
    @PutMapping("/categories/{id}")
    public Object setCategory(@PathVariable Long id, @RequestBody CategoryVo vo, BindingResult bindingResult){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            return returnObject;
        }
        ReturnObject<Object> retObject = categoryService.setCategory(id, vo);
        return Common.decorateReturnObject(retObject);
    }
    @Audit
    @PostMapping("categories/{id}/subcategories")
    public Object addCategory(@RequestBody BindingResult bindingResult, CategoryVo vo) {
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            return returnObject;
        }
        ReturnObject<VoObject> retObject = categoryService.addCategory(vo);
        httpServletResponse.setStatus(HttpStatus.CREATED.value());
        return Common.decorateReturnObject(retObject);
    }
    @Audit
    @GetMapping("categories/{id}/subcategories")
    public Object getCategory(@PathVariable Long id, @RequestBody  BindingResult bindingResult){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            return returnObject;
        }
        ReturnObject<Object> retObject = categoryService.getCategory(id);
        return Common.decorateReturnObject(retObject);
    }
}
