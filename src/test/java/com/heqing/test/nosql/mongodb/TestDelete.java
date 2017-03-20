package com.heqing.test.nosql.mongodb;

import org.bson.conversions.Bson;
import org.junit.Test;

import com.heqing.nosql.mongodb.utils.MongoCollectionUtil;
import com.heqing.nosql.mongodb.utils.MongoDBUtil;
import com.heqing.nosql.mongodb.utils.MongoDocumentUtil;
import com.mongodb.client.model.Filters;

public class TestDelete {

	String dbName = "Demo_NoSql";
	String collName = "hqTest";
	
	@Test
	public void closeDB() {
		MongoDBUtil.close();
	}
	
//	@Test
	public void deleteDB() {
		MongoDBUtil.dropDB(dbName);
	}
	
//	@Test
	public void deleteCollection() {
		MongoCollectionUtil.dropCollection(dbName, collName);
	}
	
//	@Test
	public void deleteDocument() {
		String id = "58cf7562ce493112e0c9a41a";
		MongoDocumentUtil.deleteDocumentById(dbName, collName, id);
		
		Bson filter = Filters.eq("name", "heqing");
		MongoDocumentUtil.deleteDocument(id, id, filter);
	}
}
