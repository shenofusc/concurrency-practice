package example.pattern.singleton;

/**
 * 静态内部类模式
 */
public class InnerHolderSingleton {
    private InnerHolderSingleton() {
    }
    private static class SingletonHolder {
        private static final InnerHolderSingleton INSTANCE = new InnerHolderSingleton();
    }
    public static InnerHolderSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}