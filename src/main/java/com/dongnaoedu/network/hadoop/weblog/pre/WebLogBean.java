package com.dongnaoedu.network.hadoop.weblog.pre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author heian
 * @date 2022/3/26 2:58 下午
 * @description 每一行的封装bean
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WebLogBean implements Writable {

    private Boolean valid = true; //判断数字书否合法
    private String remote_addr; // 记录客户端ip地址
    private String remote_user;// 记录客户端用户名称，忽略属性"_"
    private String time_local; // 记录访问时间与时区
    private String request; // 请求url和http协议
    private String status; // 记录请求状态 200成功
    private String body_bytes_sent; // 记录发送给客户端文件主题的内容大小
    private String http_referer; // 用来记录从哪个页面链接访问过来的
    private String http_user_agent; // 浏览器相关信息


    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeBoolean(valid);
        dataOutput.writeUTF(remote_addr);
        dataOutput.writeUTF(remote_user);
        dataOutput.writeUTF(time_local);
        dataOutput.writeUTF(request);
        dataOutput.writeUTF(status);
        dataOutput.writeUTF(body_bytes_sent);
        dataOutput.writeUTF(http_referer);
        dataOutput.writeUTF(http_user_agent);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.valid = dataInput.readBoolean();
        this.remote_addr = dataInput.readUTF();
        this.remote_user = dataInput.readUTF();
        this.time_local = dataInput.readUTF();
        this.request = dataInput.readUTF();
        this.status = dataInput.readUTF();
        this.body_bytes_sent = dataInput.readUTF();
        this.http_referer = dataInput.readUTF();
        this.http_user_agent = dataInput.readUTF();
    }
}
