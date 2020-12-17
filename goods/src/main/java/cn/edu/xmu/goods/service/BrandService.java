package cn.edu.xmu.goods.service;
import cn.edu.xmu.goods.dao.BrandDao;
import cn.edu.xmu.goods.model.vo.BrandVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.goods.model.bo.Brand;
import cn.edu.xmu.ooad.util.ImgHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class BrandService {


    @Autowired
    private BrandDao brandDao;
    @Value("${brandservice.webdav.username}")
    private String davUsername;

    @Value("${brandservice.webdav.password}")
    private String davPassWord;

    @Value("${brandservice.webdav.baseUrl}")
    private String baseUrl;
    public ReturnObject<Object> deleteBrand(Long id) {
        return brandDao.deleteBrandById(id);
    }

    public ReturnObject<VoObject> addBrand(BrandVo vo) {
        if(brandDao.hasSameName(vo.getName())){
            return new ReturnObject<>(ResponseCode.BRAND_NAME_SAME);
        }

        Brand brand = vo.createBrand();
        ReturnObject<Brand> retObj = brandDao.addBrand(brand);
        ReturnObject<VoObject> retBrand = null;
        if (retObj.getCode().equals(ResponseCode.OK)) {
            retBrand = new ReturnObject<>(retObj.getData());
        } else {
            retBrand = new ReturnObject<>(retObj.getCode(), retObj.getErrmsg());
        }
        return retBrand;
    }
    public ReturnObject <Object> setBrand(Long id, BrandVo vo) {
        if(brandDao.hasSameName(vo.getName())){
            return new ReturnObject<>(ResponseCode.BRAND_NAME_SAME);
        }
        Brand brand = vo.createBrand();
        brand.setId(id);
        brand.setGmtModified(LocalDateTime.now());
        ReturnObject<Brand> retObj = brandDao.setBrand(brand);
        ReturnObject<Object> retBrand;
        if (retObj.getCode().equals(ResponseCode.OK)) {
            retBrand = new ReturnObject<>(retObj.getData());
        } else {
            retBrand = new ReturnObject<>(retObj.getCode(), retObj.getErrmsg());
        }
        return retBrand;
    }


    public ReturnObject<PageInfo<VoObject>> selectAllBrands(Integer pageNum, Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject = brandDao.selectAllBrand(pageNum, pageSize);
        return returnObject;
    }

    public ReturnObject  uploadimage(Long id, MultipartFile multipartFile) {
        //查看是否存在brand
        ReturnObject<Brand> brandRetObject=brandDao.getBrandById(id.longValue());
        if(brandRetObject.getCode()== ResponseCode.INTERNAL_SERVER_ERR ||
                brandRetObject.getCode()==ResponseCode.RESOURCE_ID_NOTEXIST){
            return brandRetObject;
        }
        Brand brand=brandRetObject.getData();
        ReturnObject returnObject=new ReturnObject();

        try{
            returnObject = ImgHelper.remoteSaveImg(multipartFile,2,davUsername, davPassWord,baseUrl);
            //文件上传错误
            if(returnObject.getCode()!=ResponseCode.OK){
                return returnObject;
            }

            //更新数据库
            String oldFilename = brand.getImageUrl();
            Brand updateBrand=new Brand();
            updateBrand.setImageUrl(returnObject.getData().toString());
            updateBrand.setId(brand.getId());
            ReturnObject updateReturnObject = brandDao.setBrand(updateBrand);
            //数据库更新失败，需删除新增的图片
            if(updateReturnObject.getCode()==ResponseCode.FIELD_NOTVALID || updateReturnObject.getCode()==ResponseCode.INTERNAL_SERVER_ERR){
                ImgHelper.deleteRemoteImg(returnObject.getData().toString(),davUsername, davPassWord,baseUrl);
                return updateReturnObject;
            }

            //数据库更新成功，删除原来的图片
            if(updateReturnObject.getCode()==ResponseCode.OK){
                ImgHelper.deleteRemoteImg(oldFilename,davUsername,davPassWord,baseUrl);
                return updateReturnObject;
            }

        }
        catch (IOException e){
            return new ReturnObject(ResponseCode.FILE_NO_WRITE_PERMISSION);
        }
        return returnObject;
    }

}
