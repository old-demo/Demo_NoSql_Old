package com.heqing.test.nosql.mongodb;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;

import com.heqing.nosql.mongodb.utils.MongoDocumentUtil;
import com.mongodb.client.model.Filters;

public class TestUpdate {
	
	String dbName = "Demo_NoSql";
	String collName = "hqTest";
	
	@Test
	public void updateDocById() {
		String id = "58cf7562ce493112e0c9a41a";
		Document document = new Document("name", "heqing1_new").  
		         append("age", 27).  
		         append("e_mail", "975656343@qq.com").  
		         append("address", "安徽/安庆");
		MongoDocumentUtil.updateDocumentById(dbName, collName, id, document);
	}
	
//	@Test
	public void updateDoc() {
		Bson filter = Filters.eq("name", "heqing2");
		Document document = new Document("name", "heqing2_new").  
		         append("age", 27).  
		         append("e_mail", "975656343@qq.com").  
		         append("address", "安徽/安庆");
		MongoDocumentUtil.updateDocument(dbName, collName, filter, document);
	}
}
