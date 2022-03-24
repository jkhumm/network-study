package com.dongnaoedu.network.hadoop.mapreduce.flowcount.sort;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlowCountSortMapper extends Mapper<LongWritable, Text, FlowSortBean, Text > {

    @Override
    public void map(LongWritable k1,Text v2,Context context) throws IOException,InterruptedException{
        // 将k1 v1 ==> k2 v2
        String[] split = v2.toString().split("\t");
        // 手机号作为v2
        String phoneNum = split[0];

        FlowSortBean bean = new FlowSortBean();
        bean.setUpFlow(Integer.parseInt(split[1]));
        bean.setDownFlow(Integer.parseInt(split[2]));
        bean.setUpCountFlow(Integer.parseInt(split[3]));
        bean.setDownCountFlow(Integer.parseInt(split[4]));

        // 将k2 v2传递给责任链
        context.write(bean,new Text(phoneNum));
    }
}