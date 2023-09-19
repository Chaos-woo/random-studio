package per.chaos.infrastructure.utils;

import com.google.common.eventbus.EventBus;

/**
 * 消息总线
 */
public class EventBusHolder {

    /**
     * Guava事件总线
     */
    private static final EventBus eventBus = new EventBus();

    /**
     * 注册监听器
     */
    public static <T> void register(T listener) {
        eventBus.register(listener);
    }

    /**
     * 取消注册监听器
     */
    public static <T> void unregister(T listener) {
        eventBus.unregister(listener);
    }

    /**
     * 发布事件
     */
    public static <E> void publish(E event) {
        eventBus.post(event);
    }

}
