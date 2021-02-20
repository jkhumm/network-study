package com.dongnaoedu.network.spring.event;

import lombok.Data;

/**
 * 支付实体类，将作为事件实体
 */
@Data
public class PaymentInfo {
    private int id;
    private String status;

    public PaymentInfo(int id, String status) {
        this.id = id;
        this.status = status;
    }

}
