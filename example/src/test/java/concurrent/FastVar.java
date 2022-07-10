package concurrent;

import io.netty.util.concurrent.FastThreadLocal;

public class FastVar {
    private static final FastThreadLocal<FastVar> fastVar =
            new FastThreadLocal<FastVar>() {
                @Override
                protected FastVar initialValue() throws Exception {
                    // 16 CodecOutputList per Thread are cached.
                    return new FastVar();
                }
            };
}