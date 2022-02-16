package com.feige.im.test;

import java.util.concurrent.CompletableFuture;

/**
 * @author feige<br />
 * @ClassName: CompletableFutureTest <br/>
 * @Description: <br/>
 * @date: 2022/1/21 10:37<br/>
 */
public class CompletableFutureTest {

    public static void main(String[] args) {
        CompletableFuture
                .supplyAsync(() -> "hello")
                .thenApplyAsync((s1) -> s1 + " ")
                .thenApplyAsync((s2) -> s2 + "world")
                .thenAcceptAsync(System.out::println);
    }
}
