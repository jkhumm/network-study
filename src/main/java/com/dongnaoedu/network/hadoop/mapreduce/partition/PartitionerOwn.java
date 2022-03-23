package com.dongnaoedu.network.hadoop.mapreduce.partition;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;


public class PartitionerOwn extends Partitioner<Text, LongWritable>{
    /**
     * text：表示k2
     * longWritable:表示v2
     * i:reduce个数
     */

    @Override
    public int getPartition(Text text,LongWritable longWritable,int i) {
        // 如果单词的长度>=5进入第一个分区==>第一个reduceTask==>reduce编号是0
       if(text.toString().length() >= 5){
           return 0;
       }else{
           // 如果长度<5，进入第二个分区==>第二个reduceTask==>reduce编号是1
           return 1;
       }
    }
}