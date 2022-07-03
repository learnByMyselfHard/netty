package io.util.concurrent;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.FastThreadLocalThread;
import io.netty.util.internal.InternalThreadLocalMap;

public class FastThreadTest {
    public static void main(String[] args) throws InterruptedException {

        FastThreadLocalThread fastThreadLocalThread1 = new FastThreadLocalThread(() -> {
            InternalThreadLocalMap internalThreadLocalMap = ((FastThreadLocalThread) Thread.currentThread()).threadLocalMap();
            //
            FastThreadLocal<String> fastThreadLocal1 = new FastThreadLocal<String>() {
                @Override
                protected String initialValue() throws Exception {
                    // 16 CodecOutputList per Thread are cached.
                    return "hello1";
                }
            };
            System.out.println(fastThreadLocal1.get());
            //
            FastThreadLocal<String> fastThreadLocal2= new FastThreadLocal<String>() {
                @Override
                protected String initialValue() throws Exception {
                    // 16 CodecOutputList per Thread are cached.
                    return "hello2";
                }
            };
            System.out.println(fastThreadLocal2.get());
            //
            FastThreadLocal<String> fastThreadLocal3= new FastThreadLocal<String>() {
                @Override
                protected String initialValue() throws Exception {
                    // 16 CodecOutputList per Thread are cached.
                    return "hello3";
                }
            };

            System.out.println(fastThreadLocal3.get());
        });
        fastThreadLocalThread1.start();

        Thread.sleep(1000);
        FastThreadLocalThread fastThreadLocalThread2 = new FastThreadLocalThread(() -> {
            InternalThreadLocalMap internalThreadLocalMap = ((FastThreadLocalThread) Thread.currentThread()).threadLocalMap();
            //
            FastThreadLocal<String> fastThreadLocal1 = new FastThreadLocal<String>() {
                @Override
                protected String initialValue() throws Exception {
                    // 16 CodecOutputList per Thread are cached.
                    return "hello1";
                }
            };
            System.out.println(fastThreadLocal1.get());
            //
            FastThreadLocal<String> fastThreadLocal2= new FastThreadLocal<String>() {
                @Override
                protected String initialValue() throws Exception {
                    // 16 CodecOutputList per Thread are cached.
                    return "hello2";
                }
            };
            System.out.println(fastThreadLocal2.get());
            //
            FastThreadLocal<String> fastThreadLocal3= new FastThreadLocal<String>() {
                @Override
                protected String initialValue() throws Exception {
                    // 16 CodecOutputList per Thread are cached.
                    return "hello3";
                }
            };

            System.out.println(fastThreadLocal3.get());
        });
        fastThreadLocalThread2.start();
    }
}