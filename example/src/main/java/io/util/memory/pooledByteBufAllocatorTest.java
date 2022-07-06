package io.util.memory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class pooledByteBufAllocatorTest {
    public static void main(String[] args) {
        PooledByteBufAllocator pooledByteBufAllocator = PooledByteBufAllocator.DEFAULT;

        ByteBuf buffer = pooledByteBufAllocator.buffer(10);

        buffer.writeInt(1);
        System.out.println(buffer.readInt());

        buffer.release();
    }
}