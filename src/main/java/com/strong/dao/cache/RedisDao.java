package com.strong.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.strong.model.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by Ting on 17/3/29.
 */
public class RedisDao {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final JedisPool jedisPool;

    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public Seckill getSeckill(Long seckillId) {
        //缓存redis操作逻辑
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                //并么有实现内部序列化操作
                //get -> byte[] -> 反序列化 -> object(Seckill)
                // 采用自定义序列化
                //protostuff: pojo
                byte[] bytes = jedis.get(getKey(seckillId).getBytes());
                //缓存获取到
                if (bytes != null) {
                    //空对像
                    Seckill seckill = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                    return seckill;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public String pullSeckill(Seckill seckill) {
        // set object(Seckill) -> 序列化 -> byte[]
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //超时缓存
                int timeout = 60 * 60;
                String result = jedis.setex(getKey(seckill.getSeckillId()).getBytes(), timeout, bytes);
                return result;
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private String getKey(Long seckillId) {
        final String key = "seckill:" + seckillId;
        return key;
    }

}
