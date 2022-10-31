package pers.swd.magic;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pers.swd.magic.entity.Counter;
import pers.swd.magic.entity.Entity;
import sun.misc.Unsafe;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Unsafe0Test {

    private static Unsafe unsafe;

    @BeforeAll
    public static void setup() {
        unsafe = Unsafe0.unsafe;
    }

    @DisplayName("Unsafe初始化类(只分配内存不调用构造方法)")
    @Test
    public void test_allocateInstance() throws InstantiationException {
        Entity e = (Entity) unsafe.allocateInstance(Entity.class);
        System.out.println(e.getId()); // 0
    }

    @DisplayName("修改私有属性的值")
    @Test
    public void test_putXXX() throws NoSuchFieldException {
        Entity entity = new Entity(1);
        System.out.println("原值: " + entity.getId()); // 1

        Field field = Entity.class.getDeclaredField("id");
        long objectFieldOffset = unsafe.objectFieldOffset(field);
        unsafe.putInt(entity, objectFieldOffset, 2);

        System.out.println("修改后: " + entity.getId()); // 2
    }

    @DisplayName("正常的受检异常需要在签名中throws，unsafe不需要")
    @Test
    public void test_throwException() {
        unsafe.throwException(new IOException("This is an IOException"));
    }

    @DisplayName("使用堆外内存")
    @Test
    public void test_allocateMemory() {
        unsafe.allocateMemory(1024);
        unsafe.freeMemory(1024);
    }

    @DisplayName("CAS")
    @Test
    public void test_compareAndSwapInt() throws InterruptedException {
        Counter counter = new Counter();
        ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            pool.submit(() -> IntStream.range(0, 10000).forEach(j -> counter.incr()));
        }
        pool.shutdown();
        Thread.sleep(1000L);
        System.out.println(counter.getCount());
    }
}
