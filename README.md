NoSql
	泛指非关系型的数据库。。NoSQL数据库的产生就是为了解决大规模数据集合多重数据种类带来的挑战，尤其是大数据应用难题。用于超大规模数据的存储,这些类型的数据存储不需要固定的模式，无需多余操作就可以横向扩展。

参考资料
	8 种 NoSQL 数据库系统对比	http://blog.jobbole.com/1344/
	
	Redis				http://www.redis.net.cn/tutorial/3501.html
	学习Redis从这里开始		http://www.epubit.com.cn/article/200
	Java使用Jedis			http://www.cnblogs.com/liuling/p/2014-4-19-04.html
	Redis 安装			http://www.runoob.com/redis/redis-install.html
	Redis可视化工具			http://blog.csdn.net/joyhen/article/details/47358999
	
	MongoDB 教程			http://www.runoob.com/mongodb/mongodb-tutorial.html
	8天学通MongoDB			http://www.cnblogs.com/huangxincheng/archive/2012/02/18/2356595.html
	mongDB安装			http://jingyan.baidu.com/article/ed15cb1b52b8661be2698162.html
	Robomongo可视化工具		http://www.cnblogs.com/sxdcgaq8080/p/6144211.html

实践
	mongDB 可视化工具不要选择 mongDBVue.连接不上，可能不支持 MongoDB 3.0及以上的版本（亲测，浪费几小时）。建议使用 Robomongo
	1、含日期查询 从起始时间到结束时间
		BasicDBObject queryObj = new BasicDBObject();
		queryObj.put("date",new BasicDBObject().append("gte",starttime).append("gte",starttime).append("lt",endtime));
		
总结
	一、Java连接mongodb数据库
		1.连单台mongodb 
			Mongo mg = newMongo();			//默认连本机127.0.0.1  端口为27017
			Mongo mg = newMongo(ip);		//可以指定ip 端口默认为27017
			Mongo mg = newMongo(ip,port);	//也可以指定ip及端口号
		2.连两台mongodb 
			DBAddress left = new DBAddress("ip:port/dataBaseName");	//ip为主机ip地址，port为端口号，dataBaseName相当于数据库名
			DBAddress right = new DBAddress("ip:port/dataBaseName");
			Mongo mongo = new Mongo(left, right);					//若一个mongodb出现问题，会自动连另外一台
		3.连多台mongodb 
			List<ServerAddress> mongoHostList = newArrayList<ServerAddress>();
			mongoHostList.add(newServerAddress("ip",port));
			mongoHostList.add(newServerAddress("ip",port));
			mongoHostList.add(newServerAddress("ip",port));
			Mongo mg = newMongo(mongoHostList);
	
	二、DB
		1.获取mongodb的db
			//dataBaseName相当于关系数据库里的数据库名，mongodb中若没有该数据库名也不会报错，默认mongodb会建立这个数据库名，为空。
			DB db = mg.getDB(dataBaseName); 	//注意：mongodb区分大小写，程序中一定要注意
		2.mongodb的db安全认证
			//若这个时候要想访问db中的collection（相当于关系数据库里的表），就必须通过安全认证才可以访问，否则后台会报您没有通过安全认证。 
			//安全认证java代码 返回true表示通过，false表示没通过不能进行操作
			boolean auth =db.authenticate("userName", "password".toCharArray());
			
	三、collection
		1.得到mongodb中的db的collection
			DBCollection users = db.getCollection(tableName); //参数tableName相当于关系数据库里的表名,若mongodb中没有该tableName，默认会创建该tableName,为空 
		2.mongodb中的db的collection自增长主键 
			Mongodb中也像传统的关系数据库里表一样，有主键（_id）概念，用来唯一标识他们。当用户往collection中插入一条新记录的时候，
			如果没有指定_id属性，那么mongodb会自动生成一个ObjectId类型的值，保存为_id的值。 

		3.java对collection进行插入操作 
			第一种插入方式
				DBObject data1 = newBasicDBObject();
				data1.put("cust_Id", "123456");
				data1.put("is_Show", 1);
				data1.put("start_time", newDate());
				users.insert(data1);	//等同于users.save(data1); 
			第二中插入方式
				BasicDBObjectBuilderdata1 =BasicDBObjectBuilder.start().add(...).add(...); 
				users.insert(data1.get());//等同于users.save(data1.get()); 
			第三种插入方式
				Map<String, Object> data1 = new HashMap<String, Object>();
				data1.put(...);
				……  
				users.insert(new BasicDBObject(data1));//等同于users.save(new BasicDBObject(data1));
			第四中插入方式
				String json = "{'database': 'mkyongDB','table' : 'hosting'," +  "'detail' : {'records' : 99, 'index' : 'vps_index1', 'active' :'true'}}}";
				DBObject data1 = (DBObject)JSON.parse(json);
				users.insert(data1);//等同于users.save(data1); 
		4.java对collection的查询操作
			1.DBObject fields = new BasicDBObject();
				fields.put("_id", false);
				fields.put("tag", true);
				fields.put("tv_sec", true);
				DBCursor cur = users.find(query, fields); //可以用来过滤不需要获取的字段， 减少IO
				DBCursor cur = users.find(); while(cur.hasNext()){...} 
			2.查询id大于等于1的记录，并且只取10条记录
				DBCursor cur = users.find(newBasicDBObject("_id",newBasicDBObject("$gte",1))).limit(10);
			3.查询id大于等于1的记录，并按id进行降序-1表示降序，1升序。
				DBCursor cur = users.find(new BasicDBObject("_id",newBasicDBObject("$gte",1))).sort(newBasicDBObject("_id",-1)); 
			4.查询id大于等于1的记录，并跳过前10条记录显示 相当于id>10
				DBCursor cur = users.find(newBasicDBObject("_id",newBasicDBObject("$gte",1))).skip(10); 
			5.查询id大于等于1的记录，跳过前10条记录并且只显示10条记录。相当//于分页功能where id>10 and id<=20
				DBCursor cur = users.find(newBasicDBObject("_id", newBasicDBObject("$gte",1))).skip(10).limit(10);
			6.查询id大于等于1的所有记录数 返回int型
				users.find(newBasicDBObject("_id",newBasicDBObject("$gte",1))).count()
			7.findAndRemove() 查询_id=30000的数据，并且删除
				users.findAndRemove(newBasicDBObject("_id", 30000));
		5.java对collection的更新操作 
			//查询id为300的记录，将cust_Id的值更新为6533615，一定要注意大小写，以及数据//类型，返回值为int表示所影响的记录条数可以用users.findOne(newBasicDBObject("_id",300));查看下，会发现这条记录//只返回两个字段了，分别为_id,cust_Id，别的字段都删除了。
			users.update(newBasicDBObject("_id",300), newBasicDBObject ("cust_Id","6533615")).getN();
			 //这种写法可以实现只更新id为300的cust_Id值为6533615，而不删除这条记录的别的字//段属性
			users.update(newBasicDBObject("_id",300), newBasicDBObject("$set",newBasicDBObject("cust_Id","6533615"))).getN();
		6.java对collection的删除操作
			//移除cust_Id为6533615的数据。注意 用remove方法不释放磁盘空间，
			//mongodb只在collection中做了标志，没有正在删除。
			users.remove(newBasicDBObject("cust_Id","6533615")).getN();
			 //移除id>=1的数据
			users.remove(newBasicDBObject("_id",new BasicDBObject("$gte",1))).getN();
			//移除整个collection，drop也不释放磁盘空间
			users.drop();

	四、mongodb 小计(查询和导出) $in $gt $lt 
		分类： 数据库开发技术
		mongodb 查询集合之内
		> db.roothomesCol.find({"ID":{"$in":[48493297,48701795]}});
		mongodb 查询大小之间
		> db.roothomesCol.find({"ID":{$gt : 48493297, $lt : 48701795}});


	五、mongo IN 关键字，在集合里面索引限定范围
		roothomes $ /mongo/mongodb-2.0.2/bin/mongoexport 
		--host 127.0.0.1 --port 30000 --db roothomesDB -c roothomesCol 
		--query '{"ID":{"$in":[48493297,48701795]}}' 
		--fields _id,AB,AC --csv 
		>  roothomes.csv

	六、mongodb $gt $lt 关键字，在集合里面索引限定范围
		roothomes $ /mongo/mongodb-2.0.2/bin/mongoexport 
		--host 127.0.0.1 --port 30000 --db roothomesDB -c roothomesCol 
		--query '{"ID":{$gt : 48420000, $lt : 48430000}}' 
		--fields _id,AB,AC  --csv 
		>  roothomes.csv
		
	七、使用索引 
		创建索引语句如：coll.createIndex(new BasicDBObject(“i”, 1)); 
		其中i表示要索引的字段，1表示升序（-1表示降序）。可以看到，DBObject成为java客户端通用的结构表示。查看索引使用DBCollection.getIndexInfo()函数。

	八、MongoDB Java Driver的并发性 
		Java MongoDB Driver使用了连接的池化处理，这个连接池默认是保持10个连接，可以通过Option进行修改，在应用中使用Mongo的一个实例即可。
		连接池中的每个连接使用DBPort结构表示（而不是DBCollection），并寄存于DBPortPool中，所以对DBCollection的操作并不意味着使用同一个连接。
		如果在应用的一次请求过程中，需要保证使用同一个连接，可以使用下面的代码片断：
		DB db...; db.requestStart();//code.... db.requestDone(); 
		在requestStart和requestDone之间使用的连接就不是来自于DBPortPool，而是当前线程中的ThreadLocal结构变量（MyPort中保持了DBPort成员）。



