package com.nowcoder.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.sql.Time;
import java.util.List;

@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool pool = null;
    public static void print(int index,Object obj){
        System.out.println(String.format("%d,%s",index,obj.toString()));
    }
/*
    public static void main(String[] args){
        Jedis jedis = new Jedis();
        jedis.flushAll();
        jedis.set("hello","world");
        print(1,jedis.get("hello"));
        jedis.rename("hello","hi");
        print(1,jedis.get("hi"));
//        jedis.setex("hello",10,"haha");
        jedis.set("pv","100");
        jedis.incr("pv");
        print(2,jedis.get("pv"));
        jedis.incrBy("pv",5);
        print(2,jedis.get("pv"));
        //列表操作
        String listName = "list1";
        for(int i=0;i<10;i++){
            jedis.lpush(listName,"a"+String.valueOf(i));
        }
        print(3,jedis.lrange(listName,0,9));
        print(4,jedis.llen(listName));
        print(5,jedis.lpop(listName));
        print(6,jedis.lrange(listName,0,9));
        print(7,jedis.lindex(listName,3));
        print(8,jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER,"a5","aa"));
        print(9,jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE,"a5","bb"));
        print(10,jedis.lrange(listName,0,5));
        String userKey = "usr11";
        jedis.hset(userKey,"name","laowang");
        jedis.hset(userKey,"age","15");
        print(11,jedis.hget(userKey,"name"));
        print(12,jedis.hgetAll(userKey));
        jedis.hdel(userKey,"age");
        print(13,jedis.hgetAll(userKey));
        //set
        String setKey1 = "setKey1";
        String setKey2 = "setKey2";
        for(int i=0;i<10;i++){
            jedis.sadd(setKey1,String.valueOf(i));
            jedis.sadd(setKey2,String.valueOf(2*i));
        }
        print(14,jedis.smembers(setKey1));
        print(15,jedis.smembers(setKey2));
        print(16,jedis.sinter(setKey1,setKey2));
        print(17,jedis.sunion(setKey1,setKey2));
        print(18,jedis.sdiff(setKey1,setKey2));
        print(19,jedis.sismember(setKey1,"5"));
        jedis.srem(setKey1,"5");
        print(20,jedis.smembers(setKey1));
        print(21,jedis.scard(setKey1));
        jedis.smove(setKey2,setKey1,"14");
        print(22,jedis.scard(setKey1));
        print(23,jedis.smembers(setKey1));
        String rankKey = "rankKey";
        jedis.zadd(rankKey,15,"lee");
        jedis.zadd(rankKey,55,"mei");
        jedis.zadd(rankKey,65,"lucy");
        jedis.zadd(rankKey,85,"rose");
        jedis.zadd(rankKey,75,"lof");
        print(24,jedis.zcard(rankKey));
        print(25,jedis.zcount(rankKey,60,100));
        print(26,jedis.zscore(rankKey,"mei"));
        jedis.zincrby(rankKey,2,"mei");
        jedis.zincrby(rankKey,5,"meimei");
        print(27,jedis.zscore(rankKey,"mei"));
        print(28,jedis.zcount(rankKey,0,100));
        print(29,jedis.zrange(rankKey,0,3));
        print(30,jedis.zrevrange(rankKey,0,3));
        for(Tuple tuple:jedis.zrangeByScoreWithScores(rankKey,0,100)){
            print(31,tuple.getElement()+":"+tuple.getScore());
        }
        print(32,jedis.zrank(rankKey,"mei"));
        print(33,jedis.zrevrank(rankKey,"mei"));
        JedisPool jedisPool = new JedisPool();
        for(int i=0;i<50;i++){
            Jedis jedis1 = jedisPool.getResource();
            jedis1.set("a","b");
            System.out.println("Pool"+i);
            jedis1.close();
        }
    }
*/
    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("localhost",6379);
    }
    private Jedis getJedis(){
        return pool.getResource();
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long sadd(String key,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public long srem(String key,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public long scard(String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public boolean sismember(String key,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return false;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public void setObject(String key,Object object){
        set(key, JSON.toJSONString(object));
    }

    public <T> T getObject(String key, Class<T> calzz){
        String value = get(key);
        if(value != null){
            return JSON.parseObject(value,calzz);
        }
        return null;
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
