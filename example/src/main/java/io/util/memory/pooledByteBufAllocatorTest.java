package io.util.memory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class pooledByteBufAllocatorTest {
    public static void main(String[] args) {
        PooledByteBufAllocator pooledByteBufAllocator = PooledByteBufAllocator.DEFAULT;

        ByteBuf buffer = pooledByteBufAllocator.buffer(10);

        buffer.writeInt(11111);
        System.out.println(buffer.readInt());

        buffer.release();

        //内存数据没重置..
        buffer = pooledByteBufAllocator.buffer(10);

        buffer.writerIndex(4);
        System.out.println(buffer.readInt());

    }
}