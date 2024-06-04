package memory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class pooledByteBufAllocatorTest {

    static PooledByteBufAllocator pooledByteBufAllocator = PooledByteBufAllocator.DEFAULT;


    /**
     * -Dio.netty.leakDetectionLevel=paranoid
     *
     * @throws IOException
     */
    @Test
    public void leak() throws IOException, InterruptedException {
        ByteBuf buffer = pooledByteBufAllocator.buffer(10, 10);
        System.out.println(buffer.getClass());
        for (int i = 0; i < 8; i++) {
            buffer.writeByte(1);
        }
        //使用完ByteBuf但没realease
        buffer = null;
        //GC以后触发ResourceLeakDetector.DefaultResourceLeak的回收
        System.gc();
        Thread.sleep(2000);
        //再次申请ByteBuf，此时会发现refQueue不为空就会触发内存泄露探测
        pooledByteBufAllocator.buffer(10, 10);
        System.in.read();
    }



    @Test
    public void small() {
        //small内存管理
        ByteBuf buffer = pooledByteBufAllocator.buffer(10);
        buffer = pooledByteBufAllocator.buffer(10);
        buffer.writeInt(11111);
        System.out.println(buffer.readInt());

        buffer.release();

        //验证内存回收,其数据并未初始化
        buffer = pooledByteBufAllocator.buffer(31);
    }

    @Test
    public void normal() {
        //大于28kb分配normal
        ByteBuf buffer = pooledByteBufAllocator.buffer(1024 * 28 + 1);
        buffer.release();
    }

    @Test
    public void huge() {
        ByteBuf buffer = pooledByteBufAllocator.buffer(1024 * 1024 * 16 + 1);
        System.out.println();
    }

    @Test
    public void other() {

        //small内存管理
        ByteBuf buffer = pooledByteBufAllocator.buffer(10);

        buffer.writeInt(11111);
        System.out.println(buffer.readInt());

        buffer.release();

        //验证内存回收,其数据并未初始化
        buffer = pooledByteBufAllocator.buffer(10);

        buffer.writerIndex(4);
        System.out.println(buffer.readInt());
    }
}