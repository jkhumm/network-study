package com.dongnaoedu.network.hadoop.mapreduce.flowcount.partition;

import com.dongnaoedu.network.hadoop.mapreduce.flowcount.FlowBean;
import com.dongnaoedu.network.hadoop.mapreduce.flowcount.FlowCountMapper;
import com.dongnaoedu.network.hadoop.mapreduce.flowcount.FlowCountReduce;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class JobMain extends Configured implements Tool{

    @Override
    public int run(String[] strings) throws Exception{
        Job job = Job.getInstance(super.getConf(), "mapReduce_flowCount");
        
        //打包放在集群下运行，需要做一个配置
        job.setJarByClass(JobMain.class);
        
        // 第一步：设置读取文件的类：k1 v1
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job, new Path("hdfs://node01:8082/input/flowcount"));
        
        // 第二步：设置mapper类
        job.setMapperClass(FlowCountMapper.class);
        // 设置map阶段输出类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);
        
        // 第三 四 五 六步采用默认方式（分区 排序 规约 分组）
        job.setPartitionerClass(FlowPartition.class);

        // 第七步:设置Reduce类
        job.setReducerClass(FlowCountReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        // 设置reduce分区个数
        job.setNumReduceTasks(4);
        
        // 第八步：设置输出类
        job.setOutputFormatClass(TextOutputFormat.class);
        // 设置输出路径,会自动创建
        TextOutputFormat.setOutputPath(job, new Path("hafs://node01:8082/out/flowcount"));
        
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