package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Author: LR
 * @Descriprition: Redis连接池构建
 * @Date: Created in 18:07 2018/8/4
 * @Modified By:
 **/
public class RedisPool {

    private static JedisPool pool; // jedis 连接池
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20")); // 最大连接数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "10"));// 在jedispool中最大的idle状态(空闲的)的jedis实例的个数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.total", "2"));// 在jedispool中最小的idle状态(空闲的)的jedis实例的个数
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));// 在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值为true，则得到的jedis实例肯定是可以用的
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "true"));// 在return一个jedis实例的时候，是否要进行验证操作，如果赋值为true，则放回idle中的jedis实例肯定是可以用的

    private static String redisIp = PropertiesUtil.getProperty("redis.ip");
    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));

    // 初始化连接池
    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setBlockWhenExhausted(true); // 连接耗尽时，是否阻塞，false会抛出异常， true阻塞直到超时，默认为true

        pool = new JedisPool(config, redisIp, redisPort, 1000*2);
    }

    static {
        initPool();
    }

    // 得到连接
    public static Jedis getJedis(){
        return pool.getResource();
    }

    // 把连接放回去
//    public static void returnBrokenResource(Jedis jedis){
//        pool.close();
//    }
//
//    public static void returnResource(Jedis jedis){
//        pool.close();
//    }

    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("lrkey", "lrvalue");
        jedis.close();

        System.out.println("program is end");
    }

}
