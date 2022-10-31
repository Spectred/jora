package pers.swd.magic.entity;

import pers.swd.magic.Unsafe0;

public class Counter {

    private volatile int count;

    private static long offset;

    static {
        try {
            offset = Unsafe0.unsafe.objectFieldOffset(Counter.class.getDeclaredField("count"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void incr() {
        int before = count;
        while (!Unsafe0.unsafe.compareAndSwapInt(this, offset, before, before + 1)) {
            before = count;
        }
    }

    public int getCount() {
        return count;
    }
}
