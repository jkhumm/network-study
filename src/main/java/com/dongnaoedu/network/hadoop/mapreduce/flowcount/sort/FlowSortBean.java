package com.dongnaoedu.network.hadoop.mapreduce.flowcount.sort;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author heian
 * @date 2022/3/24 1:56 下午
 * @description 流量对象
 */
@Setter
@Getter
@ToString
public class FlowSortBean implements WritableComparable<FlowSortBean> {

    private Integer upFlow;
    private Integer downFlow;
    private Integer upCountFlow;
    private Integer downCountFlow;

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(upFlow);
        dataOutput.writeInt(downFlow);
        dataOutput.writeInt(upCountFlow);
        dataOutput.writeInt(downCountFlow);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.upFlow = dataInput.readInt();
        this.downFlow = dataInput.readInt();
        this.upCountFlow = dataInput.readInt();
        this.downCountFlow = dataInput.readInt();
    }

    @Override
    public int compareTo(FlowSortBean o) {
        // 按照升序  如果按照降序乘以-1
        return this.getUpFlow().compareTo(o.getUpFlow())*(-1);
    }

}
