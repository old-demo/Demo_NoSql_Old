package com.heqing.test.nosql.redis;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;

import com.heqing.nosql.redis.listener.RedisMsgPubSubListener;
import com.heqing.nosql.redis.utils.RedisUtil;

public class TestSubscribe {

    private Jedis jedis; // 获取数据库的连接,非切片客户端连接
    
    @Before
    public void setup() {
    	jedis = RedisUtil.getJedis();
    }
    
    @Test  
    public void testSubscribe() throws Exception{  
	   RedisMsgPubSubListener listener = new RedisMsgPubSubListener();   
	   //可以订阅多个频道  
	   //订阅得到信息在lister的onMessage(...)方法中进行处理  
	   jedis.subscribe(listener, "redisChatTest");   
	   jedis.subscribe(listener, "test1", "test2");  
  	  	//也用数组的方式设置多个频道  
	   jedis.subscribe(listener, new String[]{"hello_foo","hello_test"}); 
      
	   //这里启动了订阅监听，线程将在这里被阻塞  
	   //订阅得到信息在lister的onPMessage(...)方法中进行处理  
	   jedis.psubscribe(listener, new String[]{"hello_*"});//使用模式匹配的方式设置频道
	   jedis.publish("redisChatTest", "heqing"); 
    }  
}
