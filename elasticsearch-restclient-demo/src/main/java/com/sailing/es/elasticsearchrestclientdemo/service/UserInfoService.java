package com.sailing.es.elasticsearchrestclientdemo.service;

import com.sailing.es.elasticsearchrestclientdemo.config.EsClientSupport;
import com.sailing.es.elasticsearchrestclientdemo.model.UserInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2019/7/3 23:16
 * @Description:
 */
@Service
public class UserInfoService {

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

    public boolean batchAdd(String index,String type){
        List<UserInfo> list = new ArrayList<>();
            for(int i=0;i<10000;i++){
                UserInfo userInfo = new UserInfo();
                userInfo.setId("001");
                userInfo.setUserName("wanggang");
                userInfo.setPassWord("1234");
                userInfo.setBirth(new Date());
                list.add(userInfo);
            }
          return EsClientSupport.batchAddIndexData(index,type,list)>0;
    }

    public boolean deleteIndex(String index){
        return  EsClientSupport.deleteIndex(index);
    }
}
