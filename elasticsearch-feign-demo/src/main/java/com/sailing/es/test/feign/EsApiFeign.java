package com.sailing.es.test.feign;

import com.sailing.es.test.config.FeignConfiguration;
import com.sailing.es.test.feign.hystrix.EsApiFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description TODO
 * @Author Administrator
 * @Date 2019/11/7 16:41
 * @Version 1.0
 **/
@Component
@FeignClient(name="es-api-feign"
        ,url = "${es_url}"
        ,configuration = FeignConfiguration.class
        ,fallbackFactory = EsApiFeignFallbackFactory.class)
public interface EsApiFeign {


    /**
     * 删除索引
     *
     * @param index
     * @return
     */
    @RequestMapping(value = "/{indexName}"
            ,method = RequestMethod.DELETE
            ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map  deleteIndex(@PathVariable("indexName") @RequestBody String index);


    /**
     * 创建索引
     * feigin两点坑 1. @GetMapping不支持   2. @PathVariable得设置value
     * @param index
     * @return
     */
    @RequestMapping(value = "/{indexName}"
            ,method = RequestMethod.PUT
            ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map  createIndex(@PathVariable("indexName") String index, @RequestBody Map map);


    //查询操作
    @RequestMapping(value = "/_cat/indices'"
            ,method = RequestMethod.GET)
    public Map findAllIndexs();



    /**
     * 查询索引信息信息
     * @param index
     * @return
     */
    @RequestMapping(value = "/{indexName}"
            ,method = RequestMethod.GET
            ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map  findIndexInfo(@PathVariable("indexName") String index);

}
