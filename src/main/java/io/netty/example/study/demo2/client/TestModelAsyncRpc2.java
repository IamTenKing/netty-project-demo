package io.netty.example.study.demo2.client;

import io.reactivex.Flowable;

import java.util.concurrent.ExecutionException;

/**
 * Date:2021/5/30
 */
public class TestModelAsyncRpc2 {


    private static final RpcClient rpcClient = new RpcClient();


    public static void main(String[] args) throws ExecutionException, InterruptedException {


        //发起rpc调用后马上返回一个Flowable对象,但是没有真正的发起rpc调用，等订阅了流对象时才真正的发起rpc调用
        //
        Flowable result = rpcClient.rpcAsyncCallFlowable("who are you");

        result.subscribe(/*onNext*/r->{
            System.out.println(Thread.currentThread().getName()+":"+r);

        },/*onError*/error -> {
            System.out.println(Thread.currentThread().getName()+"error:"+((Throwable)error).getLocalizedMessage());
        });


        System.out.println("-----------async rpc call end---------------");

    }


}
