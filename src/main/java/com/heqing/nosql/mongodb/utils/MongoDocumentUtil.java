package com.heqing.nosql.mongodb.utils;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

public class MongoDocumentUtil extends MongoCollectionUtil {
	
	 /**
     * 文档集合中添加文档
     * @param dbName	库名
     * @param collName	集合名
     * @param document	文档
     */
    public static void addDocument(String dbName, String collName, Document document) {
        addDocument(findCollection(dbName, collName), document);
    }
    
    /**
     * 文档集合中添加多个文档
     * @param dbName	库名
     * @param collName	集合名
     * @param document	文档
     */
    public static void addDocument(String dbName, String collName, List<Document> documents) {
        addDocument(findCollection(dbName, collName), documents);
    }

    /**
     * 文档集合中添加文档
     * @param coll		集合文档
     * @param document	文档
     */
    public static void addDocument(MongoCollection<Document> coll, Document document) {
    	List<Document> documents = new ArrayList<Document>();  
        documents.add(document);  
        addDocument(coll, documents);
    }
    
    /**
     * 文档集合中添加多个文档
     * @param coll		集合文档
     * @param documents	多个文档
     */
    public static void addDocument(MongoCollection<Document> coll, List<Document> documents) { 
        coll.insertMany(documents); 
    }
    
    /**
     * 根据文档Id修改文档
     * @param dbName	库名
     * @param collName	集合名
     * @param id		id
     * @param newdoc	新文档
     * @return
     */
    public static Document updateDocumentById(String dbName, String collName, String id, Document newdoc) {
        return updateDocumentById(findCollection(dbName, collName), id, newdoc);
    }
    
    /**
     * 根据文档Id修改文档
     * @param coll		集合文档
     * @param id		id
     * @param newdoc	新文档
     * @return
     */
    public static Document updateDocumentById(MongoCollection<Document> coll, String id, Document newdoc) {
        ObjectId _idobj = null;
        try {
            _idobj = new ObjectId(id);
        } catch (Exception e) {
            return null;
        }
        Bson filter = Filters.eq("_id", _idobj);
        return updateDocument(coll, filter, newdoc);
    }
    
    /**
     * 根据文档Id修改文档
     * @param coll		集合文档
     * @param filter	查询条件
     * @param newdoc	新文档
     * @return
     */
    public static Document updateDocument(String dbName, String collName, Bson filter, Document newdoc) {
        return updateDocument(findCollection(dbName, collName), filter, newdoc);
    }
    
    /**
     * 根据文档Id修改文档
     * @param coll		集合文档
     * @param filter	查询条件
     * @param newdoc	新文档
     * @return
     */
    public static Document updateDocument(MongoCollection<Document> coll, Bson filter, Document newdoc) {
        // coll.replaceOne(filter, newdoc); // 完全替代
        coll.updateOne(filter, new Document("$set", newdoc));
        return newdoc;
    }
    
    /**
     * 通过ID删除文档
     * @param dbName	库名
     * @param collName	集合名
     * @param id		id
     * @return
     */
    public static int deleteDocumentById(String dbName, String collName, String id) {
        return deleteDocumentById(findCollection(dbName, collName), id);
    }

    /**
     * 通过ID删除文档
     * @param coll	集合
     * @param id	id
     * @return
     */
    public static int deleteDocumentById(MongoCollection<Document> coll, String id) {
        ObjectId _id = null;
        try {
            _id = new ObjectId(id);
        } catch (Exception e) {
            return 0;
        }
        Bson filter = Filters.eq("_id", _id);
        return deleteDocument(coll, filter);
    }
    
    /**
     * 通过ID删除文档
     * @param dbName	库名
     * @param collName	集合名
     * @param filter	查询条件
     * @return
     */
    public static int deleteDocument(String dbName, String collName, Bson filter) {
        return deleteDocument(findCollection(dbName, collName), filter);
    }

    /**
     * 通过ID删除文档
     * @param coll		集合
     * @param filter	查询条件
     * @return
     */
    public static int deleteDocument(MongoCollection<Document> coll, Bson filter) {
        int count = 0;
        DeleteResult deleteResult = coll.deleteOne(filter);
        count = (int) deleteResult.getDeletedCount();
        return count;
    }

    /**
     * 查找对象 - 根据主键_id
     * @param dbName	库名
     * @param collName	集合名
     * @param id
     * @return
     */
    public static Document findDocumentById(String dbName, String collName, String id) {
        return findDocumentById(findCollection(dbName, collName), id);
    }
    
    /**
     * 查找对象 - 根据主键_id
     * @param collection  集合
     * @param id
     * @return
     */
    public static Document findDocumentById(MongoCollection<Document> coll, String id) {
        ObjectId _idobj = null;
        try {
            _idobj = new ObjectId(id);
        } catch (Exception e) {
            return null;
        }
        Document myDoc = coll.find(Filters.eq("_id", _idobj)).first();
        return myDoc;
    }
    
    /**
     * 查找所有文档
     * @param dbName	库名
     * @param collName	集合名
     * @return
     */
    public static List<Document> findAllDocument(String dbName, String collName) {
        return findAllDocument(findCollection(dbName, collName));
    }
    
    /**
     * 查找所有文档
     * @param coll		集合文档
     * @return
     */
    public static List<Document> findAllDocument(MongoCollection<Document> coll) {
    	Bson orderBy = new BasicDBObject("_id", -1);
    	MongoCursor<Document> mongoCursor = coll.find().sort(orderBy).iterator();
    	List<Document> result = new ArrayList<Document>();  
        while(mongoCursor.hasNext()){  
            result.add(mongoCursor.next());  
        }  
        return result;
    }
    
    /**
     * 查找所有文档
     * @param dbName	库名
     * @param collName	集合名
     * @param orderBy	排序条件
     * @return
     */
    public static List<Document> findAllDocument(String dbName, String collName, Bson orderBy) {
        return findAllDocument(findCollection(dbName, collName),orderBy);
    }
    
    /**
     * 查找所有文档
     * @param coll		集合文档
     * @param orderBy	排序条件
     * @return
     */
    public static List<Document> findAllDocument(MongoCollection<Document> coll, Bson orderBy) {
    	MongoCursor<Document> mongoCursor = coll.find().sort(orderBy).iterator();
    	List<Document> result = new ArrayList<Document>();  
        while(mongoCursor.hasNext()){  
            result.add(mongoCursor.next());  
        }  
        return result;
    }
    
    /**
     * 根据田间查找文档
     * @param dbName	库名
     * @param collName	集合名
     * @param filter	查询条件
     * @return
     */
    public static List<Document> findDocument(String dbName, String collName, Bson filter) {
        return findDocument(findCollection(dbName, collName), filter);
    }
    
    /**
     * 根据田间查找文档
     * @param coll		集合文档
     * @param filter	查询条件
     * @return
     */
    public static List<Document> findDocument(MongoCollection<Document> coll, Bson filter) {
    	Bson orderBy = new BasicDBObject("_id", -1);
    	MongoCursor<Document> mongoCursor = coll.find(filter).sort(orderBy).iterator();
    	List<Document> result = new ArrayList<Document>();  
        while(mongoCursor.hasNext()){  
            result.add(mongoCursor.next());  
        }  
        return result;
    }
    
    /**
     * 根据田间查找文档
     * @param dbName	库名
     * @param collName	集合名
     * @param filter	查询条件
     * @param orderBy	排序条件
     * @return
     */
    public static List<Document> findDocument(String dbName, String collName, Bson filter, Bson orderBy) {
        return findDocument(findCollection(dbName, collName), filter, orderBy);
    }
    
    /**
     * 根据田间查找文档
     * @param coll		集合文档
     * @param filter	查询条件
     * @param orderBy	排序条件
     * @return
     */
    public static List<Document> findDocument(MongoCollection<Document> coll, Bson filter, Bson orderBy) {
    	MongoCursor<Document> mongoCursor = coll.find(filter).sort(orderBy).iterator();
    	List<Document> result = new ArrayList<Document>();  
        while(mongoCursor.hasNext()){  
            result.add(mongoCursor.next());  
        }  
        return result;
    }

    /**
     * 分页查询文档
     * @param dbName	库名
     * @param collName	集合名
     * @param filter	条件查询
     * @param orderBy	排序查询
     * @param pageNo	页数
     * @param pageSize	每页数量
     * @return
     */
    public static List<Document> findDocumentByPage(String dbName, String collName, Bson filter, Bson orderBy, int pageNo, int pageSize) {
        return findDocumentByPage(findCollection(dbName, collName), filter, orderBy, pageNo, pageSize);
    }
    
    /**
     * 分页查询文档
     * @param coll		集合文档
     * @param filter	条件查询
     * @param orderBy	排序查询
     * @param pageNo	页数
     * @param pageSize	每页数量
     * @return
     */
    public static List<Document> findDocumentByPage(MongoCollection<Document> coll, Bson filter, Bson orderBy, int pageNo, int pageSize) {
        if(orderBy == null) orderBy = new BasicDBObject("_id", -1);
        MongoCursor<Document> mongoCursor = coll.find(filter).sort(orderBy).skip((pageNo - 1) * pageSize).limit(pageSize).iterator();
        List<Document> result = new ArrayList<Document>();  
        while(mongoCursor.hasNext()){  
            result.add(mongoCursor.next());  
        }  
        return result;
    }
}
