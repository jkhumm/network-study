package com.dongnaoedu.network.hadoop.mapreduce.sort;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordCountMapper extends Mapper<LongWritable,Text,PairWritable,Text>{
    @Override
    public void map(LongWritable key, Text value, Mapper.Context context) throws IOException,InterruptedException{
        // 对每一行数据进行拆分，然后封装到PairWritable对象中作为k2
        String[] split =  value.toString().split("\t");
        PairWritable obj = new PairWritable();
        obj.setFirst(split[0]);
        obj.setSecond(Integer.parseInt(split[1]));
        
        //将k2和v2传递给context 
        context.write(obj,value);
    }
}