package com.dongnaoedu.network.hadoop.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class JobMain extends Configured implements Tool{

    @Override
    public int run(String[] strings) throws Exception{
        Job job = Job.getInstance(super.getConf(), "mapReduce_wordCount");
        
        //打包放在集群下运行，需要做一个配置
        job.setJarByClass(JobMain.class);
        
        // 第一步：设置读取文件的类：k1 v1
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job, new Path("hdfs://node01:8082/wordcount"));
        
        // 第二步：设置mapper类
        job.setMapperClass(WordCountMapper.class);
        // 设置map阶段输出类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);
        
        // 第三 四 五 六步采用默认方式（分区 排序 规约 分组）
        
        // 第七步:设置Reduce类
        job.setReducerClass(WordCountReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);
        
        // 第八步：设置输出类
        job.setOutputFormatClass(TextOutputFormat.class);
        // 设置输出路径
        TextOutputFormat.setOutputPath(job, new Path("hafs://node01:8082/wordcount_cout"));
        
        boolean b = job.waitForCompletion(true);
        
        return b ? 0:1;
    }
    
    public static void main(String[] args) throws Exception{
        Configuration configuration = new Configuration();
        // 启动一个任务  run=0成功
        int run = ToolRunner.run(configuration, new JobMain(), args);
        System.exit(run);  
    }
    
}