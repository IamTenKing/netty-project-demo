package io.netty.example.study.demo2.client;

import io.netty.util.concurrent.CompleteFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * 将请求和该请求的结果 映射关系保存起来
 * Date:2021/5/30
 */
public class FutureMapUtil {

    private  static final ConcurrentHashMap<String,CompletableFuture> map = new ConcurrentHashMap<String,CompletableFuture>();


    public static void put(String id,CompletableFuture<String> completeFuture){

        map.put(id,completeFuture);
    }


    public static CompletableFuture remove(String id){

        return map.remove(id);
    }




}
