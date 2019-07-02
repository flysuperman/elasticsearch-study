package com.sailing.es.test.config.es;

import io.swagger.models.auth.In;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.util.logging.Logger;

/**
 * @Auther: Administrator
 * @Date: 2019/7/2 22:35
 * @Description:
 */
@Configuration
@Log4j2
public class ElasticsearchConfig {

   //elk集群地址
    @Value("${elasticsearch.ip}")
    private String hostName;

    //端口
    @Value("${elasticsearch.port}")
    private Integer port;

    //集群名称
    @Value("${elasticsearch.cluster.name}")
    private String clusterName;

    //es连接池大小
    @Value("${elasticsearch.pool}")
    private Integer poolSize;

    public TransportClient transportClient(){
        log.info("Elasticsearch初始化开始");
        TransportClient transportClient = null;
        try {
            //配置信息
            Settings esSetting = Settings.builder()
                    .put("cluster.name",clusterName)
                    .put("client.transport.sniff",true)
                    .put("thread_pool.search.size",poolSize)
                    .build();
            transportClient = new PreBuiltTransportClient(esSetting);
            TransportAddress transportAddress = new TransportAddress(InetAddress.getByName(hostName),port);
            transportClient.addTransportAddress(transportAddress);
        }catch (Exception e){
            log.error("elasticsearch TransportClient create error!!",e);
        }
        return transportClient;
    }
}
