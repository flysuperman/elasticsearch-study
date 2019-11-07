package com.sailing.es.test.web;

import com.alibaba.fastjson.JSON;
import com.sailing.es.test.feign.EsApiFeign;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Administrator
 * @Date: 2019/7/3 05:53
 * @Description:
 */
@Api("es操作")
@RestController
@RequestMapping("/es")
public class EsController {


    @Resource
    private EsApiFeign esApiFeign;

    @PostMapping("/createIndex")
    @ResponseBody
    public String createIndex(String indexName) {
        String jsonStr="{\n" +
                "            \"settings\": {\n" +
                "            \"number_of_shards\" :   1,\n" +
                "            \"number_of_replicas\" : 0\n" +
                "            }\n" +
                "\n" +
                "        }";
        Map<String,Object> map= new HashMap();
        map.put("number_of_shards",1);
        map.put("number_of_replicas",0);
        Map<String,Object> setMap = new HashMap();
        setMap.put("settings",map);
        Map result = esApiFeign.createIndex(indexName,setMap);
        return JSON.toJSONString(result);
    }


    /**
     * 删除记录
     *
     * @return
     */
    @DeleteMapping("/delete")
    @ResponseBody
    public Map  deleteIndex(String indexName) {
        return esApiFeign.deleteIndex(indexName);
    }

    @GetMapping("/findAllIndexs")
    @ResponseBody
    public Map  findAllIndexs(){
        return esApiFeign.findAllIndexs();
    }

    /**
     * 查询索引信息
     * @param index
     * @return
     */
    @GetMapping("/findIndexInfo")
    @ResponseBody
    public Map  findIndexInfo(String index){
        return esApiFeign.findIndexInfo(index);
    }
}

