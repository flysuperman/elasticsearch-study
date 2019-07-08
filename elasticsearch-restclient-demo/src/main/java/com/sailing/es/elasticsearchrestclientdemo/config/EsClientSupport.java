package com.sailing.es.elasticsearchrestclientdemo.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
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
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;


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

    public static String queryAllByLower(String index,String type){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("match_all","{}");
            JSONObject queryObject = new JSONObject();
            queryObject.put("query",jsonObject);
            String queryStr = JSON.toJSONString(queryObject);
            HttpEntity entity = new NStringEntity(queryStr,ContentType.APPLICATION_JSON);
            String endPoint = "/"+index+"/"+type+"/_search";
            Response response = client.getLowLevelClient().performRequest("POST", endPoint, Collections.<String, String>emptyMap(), entity);
            return response.toString();
        } catch (Exception e) {
            System.out.println("写入数据失败");
            e.printStackTrace();
        }
        return "";
    }

    public static String queryPage(String index,String type,
                                   int start,int size,
                                   String fieldName,String value,
                                   String startDate, String endDate){

        // 这个sourcebuilder就类似于查询语句中最外层的部分。包括查询分页的起始，
        // 查询语句的核心，查询结果的排序，查询结果截取部分返回等一系列配置
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //查询等待时间
        sourceBuilder.timeout(new TimeValue(60,TimeUnit.SECONDS));
        //结果开始处
        sourceBuilder.from(start);
        //查询结果结束处
        sourceBuilder.size(size);
        //
        MatchQueryBuilder matchbuilder = QueryBuilders.matchQuery(fieldName,value);
        //同时满足两个条件
        matchbuilder.operator(Operator.AND);

        RangeQueryBuilder rangbuilder = QueryBuilders.rangeQuery("date");
        if(!"".equals(startDate)){
            rangbuilder.gte(startDate);
        }
        if(!"".equals(endDate)){
            rangbuilder.lte(endDate);
        }
        // 等同于bool，将两个查询合并
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(matchbuilder);
        boolQueryBuilder.must(rangbuilder);
        //排序
        FieldSortBuilder fieldSortBuilder = SortBuilders.fieldSort("date");
        fieldSortBuilder.order(SortOrder.DESC);
        sourceBuilder.sort(fieldSortBuilder);
        //
        sourceBuilder.query(boolQueryBuilder);
        System.out.println(sourceBuilder);
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        searchRequest.searchType(SearchType.QUERY_THEN_FETCH);
        searchRequest.source(sourceBuilder);
        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            long  totalHits = hits.totalHits;
//            for(SearchHit searchHit:hits){
//
//            }
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";



    }

    /**
     * 获取低水平的客户端
     * @return
     */
    public static RestClient getLowLevelClient(){
        return client.getLowLevelClient();
    }
}
