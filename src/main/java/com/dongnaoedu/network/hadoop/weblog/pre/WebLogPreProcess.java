package com.dongnaoedu.network.hadoop.weblog.pre;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.net.URI;

/**
 * @author heian
 * @date 2022/3/26 2:25 下午
 * @description 对日志进行前期清理
 */
public class WebLogPreProcess extends Configured implements Tool {


    @Override
    public int run(String[] strings) throws Exception {

        Job job = Job.getInstance(super.getConf(), "mapReduce_weblog_preprocess");

        //打包放在集群下运行，需要做一个配置
        job.setJarByClass(WebLogPreProcess.class);

        String inputPath = "hdfs://node01:9000/weblog/" + "20220326" + "/input";
        String outputPath = "hdfs://node01:9000/weblog/" + "20220326" + "/weblogPreOut";

        // 删除存在的输出目录
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://node01:9000"), super.getConf());
        if (fileSystem.exists(new Path(outputPath))) {
            fileSystem.delete(new Path(outputPath),true);
        }
        fileSystem.close();


        // 第一步：设置读取文件的类：k1 v1
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job, new Path(inputPath));

        // 第二步：设置mapper类
        job.setMapperClass(WeblogProcessMapper.class);
        // 设置map阶段输出类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        // 目前我们只是对结果进行了预处理，无需对数据进行合并

        boolean b = job.waitForCompletion(true);
        return b ? 0:1;
    }



    public static void main(String[] args) throws Exception{
        Configuration configuration = new Configuration();
        // 启动一个任务  run=0成功
        int run = ToolRunner.run(configuration, new WebLogPreProcess(), args);
        System.exit(run);
    }




}
