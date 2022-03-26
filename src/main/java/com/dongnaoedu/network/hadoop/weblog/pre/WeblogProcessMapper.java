package com.dongnaoedu.network.hadoop.weblog.pre;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class WeblogProcessMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
        // 用来存储网站URL分类数据
        Set<String> pages = new HashSet<>();
        Text k2 = new Text();

    /**
     *  从外部配置文件中加载网站的有用的url分类数据，存储到mapTask的内存中，用来对日志数据进行过滤
     *  第一次运行MapReduce会执行代码
     */
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        // 把那些静态资源
        pages.add("/about");
        pages.add("/black-ip-list/");
        pages.add("/cassandra-clustor/");
        pages.add("/finance-rhive-repurchase/");
        pages.add("/hadoop-family-roadmap/");
        pages.add("/hadoop-hive-intro/");

        // super.setup(context);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        WebLogBean webLogBean = WebLoParse.logParse(value.toString());
        if (webLogBean != null){
            // 过滤js 图片 css 静态资源
            if (pages.contains(webLogBean.getRequest())) {
                webLogBean.setValid(false);
            }
            k2.set(webLogBean.toString());
            context.write(k2,NullWritable.get());
        }
    }
}