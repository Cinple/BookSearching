package com.cinkle.Test;

import com.cinkle.jdbcT.jdbcTest;
import com.cinkle.swingT.UIT;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
    private static ExecutorService executorService=new ThreadPoolExecutor(4,8,60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>());
    public static ExecutorService getExecutorService(){
        return executorService;
    }
    public static void main(String[] args) {
        executorService.submit(new jdbcTest());
        executorService.submit(new UIT());
    }
}
