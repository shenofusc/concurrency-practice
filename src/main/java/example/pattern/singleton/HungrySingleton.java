package example.pattern.singleton;

/**
 * 饿汉模式
 */
public class HungrySingleton {
    private static HungrySingleton INSTANCE = new HungrySingleton();
    private HungrySingleton() {
    }
    public static HungrySingleton getInstance() {
        return INSTANCE;
    }
}
