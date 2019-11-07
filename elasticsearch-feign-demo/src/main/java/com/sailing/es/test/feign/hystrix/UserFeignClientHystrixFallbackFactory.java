package com.sailing.es.test.feign.hystrix;

import com.sailing.es.test.feign.EsApiFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Auther: Administrator
 * @Date: 2019/11/7 23:27
 * @Description:
 */
@Component
@Slf4j
public class UserFeignClientHystrixFallbackFactory implements FallbackFactory<EsApiFeign> {
    @Override
    public EsApiFeign create(Throwable throwable) {

        log.error("fallback reason:{}",throwable.getMessage());
        return new EsApiFeign(){
            @Override
            public Map deleteIndex(String index) {
                return null;
            }

            @Override
            public Map createIndex(String index, Map map) {
                return null;
            }

            @Override
            public Map findAllIndexs() {
                return null;
            }

            @Override
            public Map findIndexInfo(String index) {
                return null;
            }
        };
    }
}
