package com.jc.csmp.common.mongo;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lc Administrator
 * @description Mongo 操作
 */
public class MongoDao {

    // 地址
    private static MongoClientURI clientURI;
    // 客户端
    private static MongoClient client;
    private static String url = GlobalContext.getProperty("mongo.url");
    private static String userName = GlobalContext.getProperty("mongo.username");
    private static String password = GlobalContext.getProperty("mongo.password");

    /**
     * @return
     * @throws CustomException
     * @description 实例化
     */
    public static MongoDao getInstance() throws CustomException {
        return new MongoDao();
    }

    /**
     * @throws CustomException
     * @description 初始化
     */
    private MongoDao() throws CustomException {
//        MongoClientOptions.Builder build = new MongoClientOptions.Builder();
//        build.connectionsPerHost(50);   //与目标数据库能够建立的最大connection数量为50
//        build.autoEncryptionSettings(true);   //自动重连数据库启动
//        build.threadsAllowedToBlockForConnectionMultiplier(50); //如果当前所有的connection都在使用中，则每个connection上可以有50个线程排队等待
//        /*
//         * 一个线程访问数据库的时候，在成功获取到一个可用数据库连接之前的最长等待时间为2分钟
//         * 这里比较危险，如果超过maxWaitTime都没有获取到这个连接的话，该线程就会抛出Exception
//         * 故这里设置的maxWaitTime应该足够大，以免由于排队线程过多造成的数据库访问失败
//         */
//        build.maxWaitTime(1000*60*2);
//        build.connectTimeout(1000*60*1);    //与数据库建立连接的timeout设置为1分钟

        if (client == null) {
            clientURI = new MongoClientURI(url);
            // mongodb://172.16.13.91:27017,172.16.13.91:27018/sfsjfw
            if (userName != null && userName.trim().length() > 0) {
                MongoCredential credential = MongoCredential.createCredential(userName, clientURI.getDatabase(), password.toCharArray());
                List<ServerAddress> list = new ArrayList<ServerAddress>();
                for (String host : clientURI.getHosts()) {
                    String[] param = host.split(":");
                    list.add(new ServerAddress(param[0], Integer.valueOf(param[1])));
                }
                client = new MongoClient(list, credential, MongoClientOptions.builder().build());
            } else {
                client = new MongoClient(clientURI.getHosts().get(0), MongoClientOptions.builder().build());
            }
        }
    }


    /**
     * @param tableName
     * @return
     * @throws CustomException
     * @description 取得文书
     */
    public MongoCollection<Document> getMongoCollection(String tableName) throws CustomException {
        try {
            return client.getDatabase(clientURI.getDatabase()).getCollection(tableName);
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), e);
        }
    }
}
