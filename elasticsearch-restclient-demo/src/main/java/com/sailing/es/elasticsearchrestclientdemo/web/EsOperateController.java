package com.sailing.es.elasticsearchrestclientdemo.web;

import com.sailing.es.elasticsearchrestclientdemo.service.EsOperateService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: Administrator
 * @Date: 2019/7/14 09:51
 * @Description:
 */
@RestController
@Api("测试")
@RequestMapping("esoperate")
public class EsOperateController {

    @Autowired
    private EsOperateService esOperateService;

    @GetMapping("isExistIndex")
    @ResponseBody
    public boolean isExistIndex(String index){
        return esOperateService.isExistIndex(index);
    }

    @GetMapping("createIndex")
    @ResponseBody
    public boolean createIndex(String index){
        return esOperateService.createIndex(index);
    }

    @GetMapping("deleteIndex")
    @ResponseBody
    public boolean deleteIndex(String index){
        return  esOperateService.deleteIndex(index);
    }

    @GetMapping("getIndex")
    @ResponseBody
    public String[] getIndex(String indexReg){
        return esOperateService.getIndex(indexReg);
    }

    @GetMapping("queryAll")
    @ResponseBody
    public String queryAll(String index,String type,
                           String fieldName,String value){
        return esOperateService.queryAll(index,type,fieldName,value);
    }

    @GetMapping("queryDocument")
    @ResponseBody
    public String queryDocument(String index,String type,String id){
        return esOperateService.queryDocument(index,type,id);
    }
}
