package com.gexingw.shop.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;

@Configuration
public class RedisConfig {

    @Bean
    RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();

        //序列化
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);

//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);

        template.setConnectionFactory(factory);
//        //key序列化方式
        template.setKeySerializer(redisSerializer);
//        //value序列化
////        template.setValueSerializer(redisSerializer);
        template.setValueSerializer(fastJsonRedisSerializer);
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        // hash
        template.setHashKeySerializer(redisSerializer);
        template.setHashValueSerializer(fastJsonRedisSerializer);
//        template.setHashValueSerializer(jackson2JsonRedisSerializer);
//
        template.afterPropertiesSet();
        return template;
    }

}
//    /**
//     * 重写序列化器
//     *
//     * @author /
//     */
//    class MyStringRedisSerializer implements RedisSerializer<Object> {
//
//        private final Charset charset;
//
//        MyStringRedisSerializer() {
//            this(StandardCharsets.UTF_8);
//        }
//
//        private MyStringRedisSerializer(Charset charset) {
//            this.charset = charset;
//        }
//
//        @Override
//        public String deserialize(byte[] bytes) {
//            return (bytes == null ? null : new String(bytes, charset));
//        }
//
//        @Override
//        public byte[] serialize(Object object) {
//            String string = JSON.toJSONString(object);
//            if (StringUtils.isBlank(string)) {
//                return null;
//            }
//            string = string.replace("\"", "");
//            return string.getBytes(charset);
//        }
//    }

///**
// * Value 序列化
// *
// * @author /
// * @param <T>
// */
//class FastJsonRedisSerializer<T> implements RedisSerializer<T> {
//
//    private final Class<T> clazz;
//
//    FastJsonRedisSerializer(Class<T> clazz) {
//        super();
//        this.clazz = clazz;
//    }
//
//    @Override
//    public byte[] serialize(T t) {
//        if (t == null) {
//            return new byte[0];
//        }
//        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(StandardCharsets.UTF_8);
//    }
//
//    @Override
//    public T deserialize(byte[] bytes) {
//        if (bytes == null || bytes.length <= 0) {
//            return null;
//        }
//        String str = new String(bytes, StandardCharsets.UTF_8);
//        return JSON.parseObject(str, clazz);
//    }
//
//}
