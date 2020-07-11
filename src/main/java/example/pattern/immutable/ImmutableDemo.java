package example.pattern.immutable;

/**
 * Created by shy on 7/11/20.
 */
public final class ImmutableDemo {//确保无子类
    private final String field;//使用final关键字确保属性的不变性

    /**
     * 创建对象时为对象赋值
     */
    public ImmutableDemo(String field) {
        this.field = field;
    }

    //只对外提供查询方法

    public String getField() {
        return field;
    }
}
