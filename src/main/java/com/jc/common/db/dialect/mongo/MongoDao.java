package com.jc.common.db.dialect.mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.jc.dlh.cache.CacheDlhDbsource;
import com.jc.dlh.domain.DlhDbsource;
import com.jc.foundation.exception.CustomException;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;

/**
 * @description Mongo 操作
 * @author lc Administrator
 */
public class MongoDao {
	// 数据源
	private DlhDbsource source;
	// 地址
	private MongoClientURI clientURI;
	// 客户端
	private MongoClient client;

	/**
	 * @description 实例化
	 * @param dbCode
	 * @return
	 * @throws CustomException
	 */
	public static MongoDao getInstance(String dbCode) throws CustomException {
		// 创建查询连接
		DlhDbsource db = CacheDlhDbsource.queryByCode(dbCode);
		return new MongoDao(db);
	}

	/**
	 * @description 初始化
	 * @param inSource
	 * @throws CustomException
	 */
	private MongoDao(DlhDbsource inSource) throws CustomException {
		source = inSource;
		clientURI = new MongoClientURI(source.getDbAddress());
		// mongodb://172.16.13.91:27017,172.16.13.91:27018/sfsjfw
		if (source.getDbUsername() != null && source.getDbUsername().trim().length() > 0) {
			MongoCredential credential = MongoCredential.createCredential(source.getDbUsername(), clientURI.getDatabase(), source.getDbPassword().toCharArray());
			List<ServerAddress> list = new ArrayList<ServerAddress>();
			for (String host : clientURI.getHosts()) {
				String[] param = host.split(":");
				list.add(new ServerAddress(param[0], Integer.valueOf(param[1])));
			}
			client = new MongoClient(list, credential, MongoClientOptions.builder().build());
		} else {
			client = new MongoClient(clientURI);
		}

	}

	/**
	 * @description 关闭
	 */
	public void close() {
		if (client != null) {
			try {
				client.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @description 取得文书
	 * @param tableName
	 * @return
	 * @throws CustomException
	 */
	public MongoCollection<Document> getMongoCollection(String tableName) throws CustomException {
		try {
			return client.getDatabase(clientURI.getDatabase()).getCollection(tableName);
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), e);
		}
	}
}
