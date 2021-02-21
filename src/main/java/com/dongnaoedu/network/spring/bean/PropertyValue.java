package com.dongnaoedu.network.spring.bean;

import lombok.Data;

/**
 * 属性值类型，用作属性依赖注入
 */
@Data
public class PropertyValue {
    private String name;
    private Object value;

    public PropertyValue(String name, Object value) {
        super();
        this.name = name;
        this.value = value;
    }

}
