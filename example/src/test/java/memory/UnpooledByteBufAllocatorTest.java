package memory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.util.ReferenceCountUtil;

public class UnpooledByteBufAllocatorTest {
    public static void main(String[] args) {
        UnpooledByteBufAllocator unpooledByteBufAllocator = UnpooledByteBufAllocator.DEFAULT;

        ByteBuf buffer = unpooledByteBufAllocator.buffer(10);

        buffer.writeInt(1);
        System.out.println(buffer.readInt());

      //  buffer.release();
            ReferenceCountUtil.retain(buffer);
        ReferenceCountUtil.retain(buffer);
        ReferenceCountUtil.retain(buffer);
        ReferenceCountUtil.release(buffer);
        ReferenceCountUtil.release(buffer);
        ReferenceCountUtil.release(buffer);
        ReferenceCountUtil.release(buffer);
        ReferenceCountUtil.release(buffer);
    }
}