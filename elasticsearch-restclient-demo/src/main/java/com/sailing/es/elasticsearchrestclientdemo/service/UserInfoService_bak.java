package com.sailing.es.elasticsearchrestclientdemo.service;

import org.springframework.stereotype.Service;

/**
 * @Auther: Administrator
 * @Date: 2019/7/3 23:16
 * @Description:
 */
@Service
public class UserInfoService_bak {
//
//    @Autowired
//    RestHighLevelClient highLevelClient;
//
//    public boolean testEsRestClient(){
//        String indexName = "book";
//        SearchRequest searchRequest = new SearchRequest(indexName);
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.query(QueryBuilders.termQuery("title","菜谱"));
//        sourceBuilder.timeout(new TimeValue(60,TimeUnit.SECONDS));
//        searchRequest.source(sourceBuilder);
//        try {
//            SearchResponse response = highLevelClient.search(searchRequest);
//            Arrays.stream(response.getHits().getHits())
//                    .forEach(i->{
//                        System.out.println(i.getIndex());
//                        System.out.println(i.getType());
//                        System.out.println(i.getSourceAsString());
//                    });
//            System.out.println(response.getHits().getHits());
//            return  true;
//        }catch (Exception e){
//            e.printStackTrace();
//            System.out.println("失败");
//        }finally {
//            try {
//                highLevelClient.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return  true;
//    }
//
//    public boolean addField(String indexName,String type){
//        try {
//            IndexRequest indexRequest = new IndexRequest(indexName,type);
//            UserInfo userInfo = new UserInfo();
//            userInfo.setId("001");
//            userInfo.setUserName("wanggang");
//            userInfo.setPassWord("1234");
//            userInfo.setBirth(new Date());
//            String s = new Gson().toJson(userInfo);
//            indexRequest.source(s,XContentType.JSON);
//            highLevelClient.index(indexRequest);
//            return  true;
//        }catch (Exception e){
//            e.printStackTrace();
//            System.out.println("失败");
//        }
//        return  true;
//    }
//
//
//    public boolean createIndex(String indexName){
//        SearchRequest searchRequest = new SearchRequest(indexName);
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.query(QueryBuilders.termQuery("title","菜谱"));
//        sourceBuilder.timeout(new TimeValue(60,TimeUnit.SECONDS));
//        searchRequest.source(sourceBuilder);
//        try {
//            SearchResponse response = highLevelClient.search(searchRequest);
//            Arrays.stream(response.getHits().getHits())
//                    .forEach(i->{
//                        System.out.println(i.getIndex());
//                        System.out.println(i.getType());
//                        System.out.println(i.getSourceAsString());
//                    });
//            System.out.println(response.getHits().getHits());
//            return  true;
//        }catch (Exception e){
//            e.printStackTrace();
//            System.out.println("失败");
//        }
//        return  true;
//    }
//
//    public boolean batchAdd(String index,String type){
//        try {
//            BulkRequest bulkRequest = new BulkRequest();
//            for(int i=0;i<10000;i++){
//                IndexRequest indexRequest = new IndexRequest(index, type);
//                UserInfo userInfo = new UserInfo();
//                userInfo.setId("001");
//                userInfo.setUserName("wanggang");
//                userInfo.setPassWord("1234");
//                userInfo.setBirth(new Date());
//                String s = new Gson().toJson(userInfo);
//                indexRequest.source(s,XContentType.JSON);
//                bulkRequest.add(indexRequest);
//            }
//            System.out.println("begin..."+new Date());
//            highLevelClient.bulk(bulkRequest);
//            System.out.println("end..."+new Date());
//            return  true;
//        }catch (Exception e){
//            e.printStackTrace();
//            System.out.println("失败");
//        }
//        return  true;
//    }
}
