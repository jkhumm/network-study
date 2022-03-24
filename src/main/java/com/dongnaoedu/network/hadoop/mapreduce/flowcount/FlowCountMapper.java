package com.dongnaoedu.network.hadoop.mapreduce.flowcount;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlowCountMapper extends Mapper<LongWritable, Text,Text,FlowBean> {
    @Override
    public void map(LongWritable k1,Text v2,Context context) throws IOException,InterruptedException{
        String[] split = v2.toString().split("\t");
        String phoneNum = split[1];
        FlowBean bean = new FlowBean();
        bean.setUpFlow(Integer.parseInt(split[6]));
        bean.setDownFlow(Integer.parseInt(split[7]));
        bean.setUpCountFlow(Integer.parseInt(split[8]));
        bean.setDownCountFlow(Integer.parseInt(split[9]));
        // 将k2 v2传递给责任链
        context.write(new Text(phoneNum),bean);
    }
}