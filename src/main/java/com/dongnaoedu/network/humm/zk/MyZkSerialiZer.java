package com.dongnaoedu.network.humm.zk;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.io.UnsupportedEncodingException;

/**
 * @author heian
 * @create 2020-03-21-2:06 下午
 * @description
 */ //序列化
public class MyZkSerialiZer implements ZkSerializer {

    @Override
    public byte[] serialize(Object data) throws ZkMarshallingError {
        String s = (String) data;
        try {
            return s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        try {
            return new String(bytes,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
