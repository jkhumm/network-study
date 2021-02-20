package com.dongnaoedu.network.spring.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;

/**
 * 有序监听器，抽象类实现事件源以及事件的通用判断
 */
public abstract class AbstractPaymentStatusListener implements SmartApplicationListener {

    //必须满足我们这两个条件才能实现监听

    /**
     * 事件源必须是PaymentStatusUpdateEvent
     */
    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> aClass) {
        return aClass == PaymentStatusUpdateEvent.class;
    }

    /**
     * 支持的数据源类型必须是PaymentInfo
     */
    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return sourceType == PaymentInfo.class;
    }

}
