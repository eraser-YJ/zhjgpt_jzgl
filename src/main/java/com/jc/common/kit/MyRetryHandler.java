package com.jc.common.kit;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * Created by zhouq on 2019-12-2.
 */
public class MyRetryHandler implements HttpRequestRetryHandler {
    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        if(executionCount<5){
            System.out.println(executionCount+"  1 MyRetryHandler  context"+context);
            return true;
        }
        if(executionCount>5){
            System.out.println(executionCount+"  2 MyRetryHandler  context"+context);
            return false;
        }
        if (exception instanceof ConnectTimeoutException) {
            System.out.println(executionCount+"  3 MyRetryHandler  context"+context);


            return true;
        }
        return false;
    }
}
