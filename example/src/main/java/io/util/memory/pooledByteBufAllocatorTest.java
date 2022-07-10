package io.util.memory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class pooledByteBufAllocatorTest {
    public static void main(String[] args) {
        PooledByteBufAllocator pooledByteBufAllocator = PooledByteBufAllocator.DEFAULT;

        //small内存管理
        ByteBuf buffer = pooledByteBufAllocator.buffer(10);

        buffer.writeInt(11111);
        System.out.println(buffer.readInt());

        buffer.release();

        //验证内存回收,其数据并未初始化
        buffer = pooledByteBufAllocator.buffer(10);

        buffer.writerIndex(4);
        System.out.println(buffer.readInt());

        //normal内存管理,申请容量大于28K
        buffer = pooledByteBufAllocator.buffer(1024*24);
        buffer.writeInt(11111);
        System.out.println(buffer.readInt());
    }
}