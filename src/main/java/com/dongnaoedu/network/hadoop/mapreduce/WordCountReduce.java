package com.dongnaoedu.network.hadoop.mapreduce;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class WordCountReduce extends Reducer<Text,LongWritable, Text,LongWritable> {
    /**
     *  自定义我们的reduce逻辑
     *  所有key都是我们的单词，所有的values都是我们单词出现的次数
     */
    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException,InterruptedException{
        long count = 0;
        for(LongWritable value : values){
            count += value.get();
        }
        context.write(key,new LongWritable(count));
    }
}