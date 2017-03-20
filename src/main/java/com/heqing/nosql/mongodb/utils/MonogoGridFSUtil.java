package com.heqing.nosql.mongodb.utils;

import java.io.File;
import java.io.FileOutputStream;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

@SuppressWarnings("deprecation")
public class MonogoGridFSUtil extends MongoDBUtil {

	GridFS gridFS = null;
	
	public MonogoGridFSUtil(String dbName) {
		DB db = MongoDBUtil.getMongoClient().getDB(dbName);
		gridFS = new GridFS(db);
	}
	
	/** 
     * 将文件保存在MongoDB中
     * @param file  文件
     * @return 是否成功
     */  
    public boolean write(File file){ 
    	return write(file.getAbsolutePath(), file.getName());
    }  
	
	/** 
     * 将文件保存在MongoDB中
     * @param filePath  文件地址
     * @param fileName	文件名 
     * @return 是否成功
     */  
    public boolean write(String filePath, String fileName){ 
    	File file =new File(filePath+"/"+fileName);
    	if(!file.exists()) return false;
    	try {
	    	Object id = System.currentTimeMillis();
	        DBObject query  = new BasicDBObject("_id", id);  
	        GridFSDBFile gridFSDBFile = gridFS.findOne(query);
	        if(gridFSDBFile == null){  
	             GridFSInputFile gridFSInputFile = gridFS.createFile(file);  
	             gridFSInputFile.setId(id);  
	             gridFSInputFile.setFilename(fileName);  
	             gridFSInputFile.save();
	        }  
    	} catch(Exception e) {
    		e.printStackTrace();
    		return false;
    	}
        return true;
    }  
    
	/** 
     * 将保存在MongoDB中的文件输出到指定地址
     * @param filePath  文件地址
     * @param fileName	文件名 
     * @return 是否成功
     */  
    public boolean read(String filePath, String fileName){ 
    	 GridFSDBFile gridFSDBFile = getByFileName(fileName);
         return read(gridFSDBFile, filePath, fileName);
    }  
    
    /** 
     * 将保存在MongoDB中的文件输出到指定地址
     * @param gridFSDBFile  DB文件
     * @param filePath  文件地址
     * @param fileName	文件名 
     * @return 是否成功
     */  
    public boolean read(GridFSDBFile gridFSDBFile, String filePath, String fileName){ 
         if(gridFSDBFile != null){  
        	 try {
        		 gridFSDBFile.writeTo(new FileOutputStream(filePath+"/"+fileName)); 
        	 } catch(Exception e) {
        		 e.printStackTrace();
        		 return false;
        	 }
             return true;
         }
         return false;
    }  
      
    /** 
     * 据id返回文件 
     * @param id 
     * @return 
     */  
    public GridFSDBFile getById(Object id){  
        DBObject query  = new BasicDBObject("_id", id);  
        GridFSDBFile gridFSDBFile = gridFS.findOne(query);  
        return gridFSDBFile;  
    }  
      
    /** 
     * 据文件名返回文件，只返回第一个 
     * @param fileName 
     * @return 
     */  
    public GridFSDBFile getByFileName(String fileName){  
        DBObject query  = new BasicDBObject("filename", fileName);  
        GridFSDBFile gridFSDBFile = gridFS.findOne(query);
        return gridFSDBFile;  
    }  
    
    /** 
     * 根据文件名删除文件
     * @param fileName 文件名
     * @return 
     */  
    public void deleteByFileName(String fileName){  
    	gridFS.remove(fileName);
    } 
    
    /** 
     * 根据文件Id删除文件
     * @param id 文件Id
     * @return 
     */  
    public void deleteByFileId(String id){  
    	gridFS.remove(id);
    }

}
