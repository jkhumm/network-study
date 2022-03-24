package com.dongnaoedu.network.hadoop.mapreduce.sort;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;


/**
 * 排序
 */
public class SortReducer extends Reducer<PairWritable, Text,PairWritable, NullWritable> {


    // 自定义计数器使用枚举方式创建
    public static enum MyCounter{
        REDUCE_INPUT_RECORDS,
        RECUDE_INPUT_VAL_NUMS
    }

    @Override
    protected void reduce(PairWritable key,Iterable<Text> values,Context context) throws IOException,InterruptedException{
        // 我们仅仅是将v2的值传给v3，然后定义v3为个占位符空对象 
        // context.write(key, NullWritable.get()); 这样写会存在问题，会把重复的值记录到集合中去
        /**
         * a 1
         * a 1
         * ==>  a 1 <a 1,a 1>
         */

        // 统计key个数
        context.getCounter(MyCounter.REDUCE_INPUT_RECORDS).increment(1L);

        for(Text value:values){
            // 为了防止导致重复结果不输出，把每个结果作为key都输出出来
            context.getCounter(MyCounter.RECUDE_INPUT_VAL_NUMS).increment(1L);
            context.write(key, NullWritable.get());
        }
    }
}