package com.dongnaoedu.network.hadoop.mapreduce.flowcount;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FlowCountReduce extends Reducer<Text,FlowBean, Text,FlowBean> {


    @Override
    protected void reduce(Text k2, Iterable<FlowBean> values, Context context) throws IOException,InterruptedException{
        FlowBean bean = new FlowBean();

        Integer upFlow = 0;
        Integer downFlow = 0;
        Integer upCountFlow = 0;
        Integer downCountFlow = 0;
        for (FlowBean value : values) {
            upFlow += value.getUpFlow();
            downFlow = value.getDownFlow();
            upCountFlow = value.getUpCountFlow();
            downCountFlow = value.getDownCountFlow();
        }
        bean.setUpFlow(upFlow);
        bean.setDownFlow(downFlow);
        bean.setUpCountFlow(upCountFlow);
        bean.setDownCountFlow(downCountFlow);

        context.write(k2,bean);
    }

}