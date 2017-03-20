package com.heqing.test.nosql.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.heqing.nosql.mongodb.utils.MongoDocumentUtil;
import com.mongodb.client.MongoCollection;

public class TestAdd {
	
	String dbName = "Demo_NoSql";
	String collName = "hqTest";
	
//	@Test
	public void addCollection() {
		MongoDocumentUtil.createCollection(dbName, collName);
	}
	
//	@Test
	public void addDocument() {
		Document document = new Document("name", "heqing").  
		         append("age", 27).  
		         append("e_mail", "975656343@qq.com").  
		         append("address", "安徽/安庆");
		MongoDocumentUtil.addDocument(dbName, collName, document);
		
		List<Document> documents = new ArrayList<>();
		Document document1 = new Document("name", "heqing1").  
		         append("age", 26).  
		         append("e_mail", "975656343@qq.com").  
		         append("address", "安徽/安庆");
		Document document2 = new Document("name", "heqing2").  
		         append("age", 27).  
		         append("e_mail", "975656343@qq.com").  
		         append("address", "安徽/安庆");
		documents.add(document1);documents.add(document2);
		MongoCollection<Document> colls = MongoDocumentUtil.findCollection(dbName, collName);
		MongoDocumentUtil.addDocument(colls, documents);
	}

}
