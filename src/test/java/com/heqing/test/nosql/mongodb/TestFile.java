package com.heqing.test.nosql.mongodb;

import org.junit.Test;

import com.heqing.nosql.mongodb.utils.MonogoGridFSUtil;

public class TestFile {

	String dbName = "Demo_NoSql";
	MonogoGridFSUtil gridFSUtil = new MonogoGridFSUtil(dbName);
	
//	@Test
	public void readFile() {
		gridFSUtil.write("D:\\test", "test.jpg");
	}
	
//	@Test 
	public void writeFile() {
		gridFSUtil.read("D:\\tools", "test.jpg");
	}
	
	@Test 
	public void removeFile() {
		gridFSUtil.deleteByFileName("test.jpg");
	}
}
