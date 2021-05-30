package io.netty.example.study.demo2.client;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Date:2021/5/30
 */
public class TestModelAsyncRpc1 {


    private static final RpcClient rpcClient = new RpcClient();


    public static void main(String[] args) throws ExecutionException, InterruptedException {


        CompletableFuture future1 = rpcClient.rpcAsyncCall("who are you ");
        CompletableFuture future2 = rpcClient.rpcAsyncCall("who are you ");

        //等待两个请求都返回结果对象，然后使用结果做些事情
        CompletableFuture future =  future1.thenCombine(future2,(u, v)->{
            return (String)v + u;

        });

        future.whenComplete((v,t)->{
            if(null !=t){
                ((Throwable)t).printStackTrace();
            }else{
                System.out.println(v);
            }

        });


    }


}
