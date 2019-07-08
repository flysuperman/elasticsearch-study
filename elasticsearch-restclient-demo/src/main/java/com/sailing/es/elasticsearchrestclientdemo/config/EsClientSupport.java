package com.sailing.es.elasticsearchrestclientdemo.config;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;


/**
 * @Auther: Administrator
 * @Date: 2019/7/6 18:08
 * @Description:
 */

@Component
public class EsClientSupport {

    @Resource(name="restHighLevelClient")
    private RestHighLevelClient restHighLevelClient;

    private static RestHighLevelClient client;

    /**
     * @PostConstruct是spring框架的注解，spring容器初始化的时候执行该方法
     */
    @PostConstruct
    public void init(){
        client = this.restHighLevelClient;
        System.out.println("client="+client);
    }

    public static boolean isExistIndex(String index){
        try {
            Response response = client.getLowLevelClient().performRequest("HEAD", index);
            boolean exist = response.getStatusLine().getReasonPhrase().equals("OK");
            return exist;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return false;
    }
    //判断索引是否存在
    public static boolean isExistIndex2(String index){
        try {
            GetIndexRequest request = new GetIndexRequest();
            request.indices(index);
            request.local(false);
            request.humanReadable(true);
            return client.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            System.out.println("判断索引是否存在");
            e.printStackTrace();
        }
        return false;
    }



    public static boolean createIndex(String index,String alias,int
            numberOfShards,int numberOfRelicas){
        try {
            CreateIndexRequest request = new CreateIndexRequest(index);
            request.settings(Settings.builder()
                    .put("index.number_of_shards",numberOfShards)
                    .put("index.number_of_replicas",numberOfRelicas)
            );
            //如果不需要指定字段属性可以忽略以下mapping设置，新增文档后会自动创建mapping
//            request.mapping("data", " {\n" + " \""+type+"\": {\n" + " \"properties\": {\n" + 需要的设计的字段属性+ " }\n" + " }\n" + " }", xcontenttype.json);
            if(null!=alias && "".equals(alias)){
                request.alias(new Alias(alias));
            }
            CreateIndexResponse indexResponse =client.indices().create(request,RequestOptions.DEFAULT);
            if(indexResponse.isAcknowledged()){
                System.out.println("索引创建成功");
            }else{
                System.out.println("索引创建失败");
            }
            return indexResponse.isAcknowledged();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return false;
    }

    public static boolean createIndex(String index){
        try {
            CreateIndexRequest request = new CreateIndexRequest(index);
            CreateIndexResponse indexResponse =client.indices().create(request,RequestOptions.DEFAULT);
            if(indexResponse.isAcknowledged()){
                System.out.println("索引创建成功");
            }else{
                System.out.println("索引创建失败");
            }
            return indexResponse.isAcknowledged();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return false;
    }

    /**
     * 删除索引
     * @param index
     * @return
     */
    public static boolean deleteIndex(String index){
        try {
            DeleteIndexRequest  request = new DeleteIndexRequest (index);
            request.indicesOptions(IndicesOptions.lenientExpandOpen());
            DeleteIndexResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
            return  response.isAcknowledged();
        }catch (Exception e){
            System.out.println("删除索引："+index+" 失败");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 增加索引数据
     * @param index 索引名称
     * @param type 索引类型
     * @param id 索引id
     * @param indexData 索引数据，json格式的字符串
     * @return 索引id
     */
    public static String addIndexData(String index,String type,String id,String indexData){
        try {
            IndexRequest indexRequest = new IndexRequest (index,type,id);
            indexRequest.source(indexData, XContentType.JSON);
            IndexResponse indexResponse = client.index(indexRequest);
            return indexResponse.getId();
        } catch (Exception e) {
            System.out.println("写入数据失败");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 批量插入数据
     * @param index
     * @param type
     * @param list
     * @return
     */
    public static int batchAddIndexData(String index, String type, List list){
        try {
            BulkRequest bulkRequest = new BulkRequest();
            for(Object userInfo: list){
                IndexRequest indexRequest = new IndexRequest (index,type);
                indexRequest.source(JSON.toJSONString(userInfo),XContentType.JSON);
                bulkRequest.add(indexRequest);
            }
            BulkResponse bulk = client.bulk(bulkRequest);
            return  bulk.status().getStatus();
        } catch (Exception e) {
            System.out.println("写入数据失败");
           e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取低水平的客户端
     * @return
     */
    public static RestClient getLowLevelClient(){
        return client.getLowLevelClient();
    }
}
