package com.sailing.es.elasticsearchrestclientdemo.web;

import com.sailing.es.elasticsearchrestclientdemo.service.UserInfoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: Administrator
 * @Date: 2019/7/4 23:12
 * @Description:
 */

@RestController
@Api("测试")
@RequestMapping("test")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("batchAdd")
    public void batchAdd(String indexName,String type){
     userInfoService.batchAdd(indexName,type);
    }

    @GetMapping("deleteIndex")
    @ResponseBody
    public boolean deleteIndex(String index){
       return userInfoService.deleteIndex(index);
    }
}
