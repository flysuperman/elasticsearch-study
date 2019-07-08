package com.sailing.es.elasticsearchrestclientdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Objects;

/**
 * @Auther: Administrator
 * @Date: 2019/7/6 17:19
 * @Description:
 */

@Slf4j
@Configuration
public class EsClientConfig {

    private static final int ADDRESS_LENGTH = 2;
    private static final String HTTP_SCHEME = "http";

    private static boolean uniqueConnectTimeConfig = false;
    private static boolean uniqueConnectNumConfig = true;


    /**
     * 使用冒号隔开ip和端口1
     */
    @Value("${elasticsearch.ip}")
    private String[] ipAddress;
    @Value("${connect_time_out}")
    private  int connect_time_out;
    @Value("${socket_time_out}")
    private  int socket_time_out;

    @Value("${max_conn_num}")
    private int max_conn_num;

    @Value("${max_conn_per_route}")
    private int max_conn_per_route;

    @Value("${connection_request_time_out}")
    private int connection_request_time_out;

    @Bean
    public RestClientBuilder restClientBuilder() {
        HttpHost[] hosts = Arrays.stream(ipAddress)
                .map(this::makeHttpHost)
                .filter(Objects::nonNull)
                .toArray(HttpHost[]::new);
        log.debug("hosts:{}", Arrays.toString(hosts));
        RestClientBuilder builder = RestClient.builder(hosts);
        if(uniqueConnectTimeConfig){
            this.setConnectTimeOutConfig(builder);
        }
        if(uniqueConnectNumConfig){
             this.setMutiConnectConfig(builder);
        }
        return builder;
    }

    //主要关于异步httpclient的连接延时配置
    private void setConnectTimeOutConfig(RestClientBuilder builder){
        builder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
            @Override
            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder builder) {
                builder.setConnectTimeout(connect_time_out);
                builder.setSocketTimeout(socket_time_out);
                builder.setConnectionRequestTimeout(connection_request_time_out);
                return builder;
            }
       });
    }

    //主要关于异步httpclient的连接数配置
    private void setMutiConnectConfig(RestClientBuilder builder){
        builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                httpAsyncClientBuilder.setMaxConnTotal(max_conn_num);
                httpAsyncClientBuilder.setMaxConnPerRoute(max_conn_per_route);
                return httpAsyncClientBuilder;
            }
        });
    }

    @Bean(name = "restHighLevelClient")
    public RestHighLevelClient highLevelClient(@Autowired RestClientBuilder restClientBuilder) {
        restClientBuilder.setMaxRetryTimeoutMillis(60000);
        return new RestHighLevelClient(restClientBuilder);
    }


    private HttpHost makeHttpHost(String s) {
        assert StringUtils.isNotEmpty(s);
        String[] address = s.split(":");
        if (address.length == ADDRESS_LENGTH) {
            String ip = address[0];
            int port = Integer.parseInt(address[1]);
            return new HttpHost(ip, port, HTTP_SCHEME);
        } else {
            return null;
        }
    }
}
