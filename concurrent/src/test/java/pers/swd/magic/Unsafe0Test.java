package pers.swd.magic;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pers.swd.magic.entity.Entity;
import sun.misc.Unsafe;

public class Unsafe0Test {

    private static Unsafe unsafe;

    @BeforeAll
    public static void setup() {
        unsafe = Unsafe0.unsafe;
    }

    /**
     * public class Entity {
     *     private int id;
     *     public Entity(int id) {
     *         this.id = 1;
     *     }
     * }
     */
    @DisplayName("Unsafe初始化类(只分配内存不调用构造方法)")
    @Test
    public void test_() throws InstantiationException {
        Entity e = (Entity) unsafe.allocateInstance(Entity.class);
        System.out.println(e.getId()); // 0
    }
}
