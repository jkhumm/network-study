package com.dongnaoedu.network.hadoop.mapreduce.combiner;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class WordCountReduce extends Reducer<Text,LongWritable, Text,LongWritable> {
    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException,InterruptedException{
        long count = 0;
        // 此时集合经过规约处理，元素只有一个而已
        for(LongWritable value : values){
            count += value.get();
        }
        context.write(key,new LongWritable(count));
    }
}