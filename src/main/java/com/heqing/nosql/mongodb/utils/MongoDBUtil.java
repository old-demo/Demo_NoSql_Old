package com.heqing.nosql.mongodb.utils;

import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.client.MongoDatabase;

/*
 * MongoDB工具类 Mongo实例代表了一个数据库连接池，即使在多线程的环境中，一个Mongo实例对我们来说已经足够了<br>
 * 注意Mongo已经实现了连接池，并且是线程安全的。 <br>
 * 设计为单例模式， 因 MongoDB的Java驱动是线程安全的，对于一般的应用，只要一个Mongo实例即可，<br>
 * Mongo有个内置的连接池（默认为10个） 对于有大量写和读的环境中，为了确保在一个Session中使用同一个DB时，<br>
 * DB和DBCollection是绝对线程安全的<br>
 */
@SuppressWarnings("deprecation")
public class MongoDBUtil {

	protected static  MongoClient mongoClient = null;
	
    //mongodb服务器IP
    private static String ADDR = "127.0.0.1";
    //mongodb的端口号
    private static int PORT = 27017;
	
	static {
		mongoClient = new MongoClient(ADDR, PORT);

        // 多个mongodb服务器连接如下
//         List<ServerAddress> listHost = Arrays.asList(new ServerAddress("localhost", 27017),new ServerAddress("localhost", 27018));
//         mongoClient = new MongoClient(listHost);

        // 大部分用户使用mongodb都在安全内网下，但如果将mongodb设为安全验证模式，就需要在客户端提供用户名和密码：
//         boolean auth = db.authenticate(myUserName, myPassword);
		
        Builder options = new MongoClientOptions.Builder();
//        options.autoConnectRetry(true);// 自动重连true
//        options.maxAutoConnectRetryTime(10); // the maximum auto connect retry time
        options.connectionsPerHost(300);// 连接池设置为300个连接,默认为100
        options.connectTimeout(15000);// 连接超时，推荐>3000毫秒
        options.maxWaitTime(5000); //
        options.socketTimeout(0);// 套接字超时时间，0无限制
        options.threadsAllowedToBlockForConnectionMultiplier(5000);// 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
        options.build();
    }
	
	public static MongoClient getMongoClient() {
		if(mongoClient == null) mongoClient = new MongoClient(ADDR, PORT);
		return mongoClient;
	}
	
    /**
     * 关闭Mongodb
     */
    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }
	
	/**
     * 获取所有数据库名称列表
     * @return 所有数据库名称
     */
    public static List<String> findAllDBNames() {
	    return mongoClient.getDatabaseNames();
    }
	
	/**
     * 获取DB实例 - 指定DB
     * @param dbName 数据库名
     * @return 数据库实例
     */
    public static MongoDatabase findDB(String dbName) {
        if (dbName != null && !"".equals(dbName)) {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            return database;
        }
        return null;
    }
    
    /**
     * 删除一个数据库
     */
    public static void dropDB(String dbName) {
        findDB(dbName).drop();
    }

}
