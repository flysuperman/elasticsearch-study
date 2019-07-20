package com.sailing.es.elasticsearchrestclientdemo.service;

import com.sailing.es.elasticsearchrestclientdemo.config.EsClientSupport;
import com.sailing.es.elasticsearchrestclientdemo.model.UserInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2019/7/14 09:53
 * @Description:
 */
@Service
public class EsOperateService {

    public boolean isExistIndex(String index){
        return EsClientSupport.isExistIndex(index);
    }

    public String[] getIndex(String indexReg){
        return EsClientSupport.getIndex(indexReg);
    }

    public boolean createIndex(String index){
        return EsClientSupport.createIndex(index);
    }

    public boolean deleteIndex(String index){
        return EsClientSupport.deleteIndex(index);
    }

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
        return EsClientSupport.batchAddDocument(index,type,list)>0;
    }
}
