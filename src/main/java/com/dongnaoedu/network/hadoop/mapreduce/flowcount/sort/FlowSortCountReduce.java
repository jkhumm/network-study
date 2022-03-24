package com.dongnaoedu.network.hadoop.mapreduce.flowcount.sort;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FlowSortCountReduce extends Reducer<FlowSortBean, Text, Text,FlowSortBean> {


    @Override
    protected void reduce(FlowSortBean k2, Iterable<Text> v2, Context context) throws IOException,InterruptedException{
        for (Text phone : v2) {
            context.write(phone,k2);
        }
    }

}