package com.dongnaoedu.network.hadoop.weblog.click;

import com.dongnaoedu.network.hadoop.weblog.pre.WebLogBean;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 预处理下一步 ==> (ip,对象bean)
 */
public class ClickSteamMapper extends Mapper<LongWritable, Text, Text, WebLogBean> {

    @Override
    protected void map(LongWritable k2, Text v2, Context context) throws IOException, InterruptedException {
        // 预处理后的行
        String preLine = v2.toString();
        String[] split = preLine.split("\001");

        if (split.length>9) {
            WebLogBean webLogBean = new WebLogBean(
                    "true".equals(split[0]),
                    split[1],split[2],split[3],
                    split[4],split[5],split[6],
                    split[7],split[8]);
            if (webLogBean.getValid()){
                // 有效才会进行后续处理
                context.write(new Text(webLogBean.getRemote_addr()),webLogBean);
            }

        }
    }
}