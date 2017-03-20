package com.heqing.nosql.mongodb.utils;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;

public class MongoCollectionUtil extends MongoDBUtil {

    /**
     * 查询DB下的所有表名
     * @param dbName 数据库名
     * @return  数据库中表名
     */
    public static List<String> findAllCollections(String dbName) {
        MongoIterable<String> colls = findDB(dbName).listCollectionNames();
        List<String> _list = new ArrayList<String>();
        for (String s : colls) {
            _list.add(s);
        }
        return _list;
    }
    
    /**
     * 查询DB下的所有表的数量
     * @param dbName	库名
     * @param collName	集合名
     * @return
     */
    public static int findAllDocumentCount(String dbName) {
    	List<String> colls = findAllCollections(dbName);
        return (int) colls.size();
    }
	
	/**
     * 获取某个库下，某个集合中文档数量
     * @param dbName	库名
     * @param collName	集合名
     * @return
     */
    public static int findAllDocumentCount(String dbName, String collName) {
    	MongoCollection<Document> coll = findDB(dbName).getCollection(collName);
        return (int) coll.count();
    }
    
    /**
     * 在库下创建集合
     * @param dbName	库名
     * @param collName	集合名
     * @return
     */
    public static void createCollection(String dbName, String collName) {
    	findDB(dbName).createCollection(collName);
    }
    
    /**
     * 删除某个库下，某个集合
     * @param dbName	库名
     * @param collName	集合名
     * @return
     */
    public static void dropCollection(String dbName, String collName) {
        findDB(dbName).getCollection(collName).drop();
    }
    
    /**
     * 获取某个库下，某个集合中文档
     * @param dbName	库名
     * @param collName	集合名
     * @return
     */
    public static MongoCollection<Document> findCollection(String dbName, String collName) {
        if (null == dbName || "".equals(dbName))  	 return null;
        if (null == collName || "".equals(collName)) return null;
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(collName);
        return collection;
    }

}
