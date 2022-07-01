package com.gexingw.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {
    @Value("${spring.elasticsearch.rest.uris}")
    private String host;

//    @Bean
//    public RestHighLevelClient client() {
//        RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200, "http"));
//        RestHighLevelClient client = new RestHighLevelClient(builder);
//        return client;
//    }
}
