package io.netty.example.study.demo2.client;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Date:2021/5/30
 */
public class TestModelAsyncRpc {


    private static final RpcClient rpcClient = new RpcClient();


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();

        System.out.println(rpcClient.rpcSyncCall("who are you "));

        CompletableFuture future = rpcClient.rpcAsyncCall("who are you ");

        future.whenComplete((v,t)->{

           if(t!=null){
           } else{
               System.out.println(v);
           }



        });

//        System.out.println(System.currentTimeMillis()-start);
    }


}
