package com.dongnaoedu.network.spring.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 短信服务，有序监听器
 */
@Service
public class SmsPaymentStatusUpdateListener extends AbstractPaymentStatusListener implements SmartApplicationListener {

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    //@Async 这个注解一般配合 EnableAsync来用
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        System.out.println("Thread: " + Thread.currentThread().getName()+
                " 短信服务, 收到支付状态更新的通知. " + applicationEvent);
    }
}
