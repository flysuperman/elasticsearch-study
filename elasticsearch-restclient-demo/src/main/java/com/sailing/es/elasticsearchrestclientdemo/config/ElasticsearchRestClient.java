package com.sailing.es.elasticsearchrestclientdemo.config;

/**
 * @Auther: Administrator
 * @Date: 2019/7/3 23:10
 * @Description:
 */

public class ElasticsearchRestClient {
//    private static final int ADDRESS_LENGTH = 2;
//    private static final String HTTP_SCHEME = "http";
//
//    /**
//     * 使用冒号隔开ip和端口1
//     */
//    @Value("${elasticsearch.ip}")
//    String[] ipAddress;
//
//    @Bean
//    public RestClientBuilder restClientBuilder() {
//        HttpHost[] hosts = Arrays.stream(ipAddress)
//                .map(this::makeHttpHost)
//                .filter(Objects::nonNull)
//                .toArray(HttpHost[]::new);
//        log.debug("hosts:{}", Arrays.toString(hosts));
//        return RestClient.builder(hosts);
//    }
//
//
//    @Bean(name = "highLevelClient")
//    public RestHighLevelClient highLevelClient(@Autowired RestClientBuilder restClientBuilder) {
//        restClientBuilder.setMaxRetryTimeoutMillis(60000);
//        return new RestHighLevelClient(restClientBuilder);
//    }
//
//
//    private HttpHost makeHttpHost(String s) {
//        assert StringUtils.isNotEmpty(s);
//        String[] address = s.split(":");
//        if (address.length == ADDRESS_LENGTH) {
//            String ip = address[0];
//            int port = Integer.parseInt(address[1]);
//            return new HttpHost(ip, port, HTTP_SCHEME);
//        } else {
//            return null;
//        }
//    }
}