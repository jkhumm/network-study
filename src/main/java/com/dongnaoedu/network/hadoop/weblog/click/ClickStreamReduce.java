package com.dongnaoedu.network.hadoop.weblog.click;

import com.dongnaoedu.network.hadoop.weblog.pre.WebLogBean;
import lombok.SneakyThrows;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ClickStreamReduce extends Reducer<Text, WebLogBean, NullWritable,Text> {
    /**
     *   192.168   <对象，对象>
     */
    @SneakyThrows
    @Override
    protected void reduce(Text k2, Iterable<WebLogBean> v2, Context context) throws IOException,InterruptedException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        List<WebLogBean> list = new ArrayList<>();
        for (WebLogBean webLogBean : v2) {
            WebLogBean bean = new WebLogBean();
            BeanUtils.copyProperties(webLogBean,bean);
            list.add(bean);
        }
        Collections.sort(list,(o1, o2) -> {
            try {
                // 比较两个对象时间
                Date d1 = sdf.parse(o1.getTime_local());
                Date d2 = sdf.parse(o2.getTime_local());
                return d1.compareTo(d2);
            }catch (Exception e){
                e.printStackTrace();
                return 0;
            }
        });
        /**
         * 有序bean中分辨出各次visit,并对一次visit中所访问的page按顺序标号step
         * 核心思想：
         * 就是比较相邻两条记录的时间差，如果时间差<30分钟，则该记录属于同一个session,否则就是另外session
         */
        int step = 1;
        String session = UUID.randomUUID().toString();
        String blank = "\001";
        for (int i = 0; i < list.size(); i++) {
            WebLogBean bean = list.get(i);
            if (1 == list.size()){
                String newLogBean =  session + blank +
                        k2.toString() + blank +
                        bean.getRemote_user() + blank +
                        bean.getTime_local() + blank +
                        bean.getRequest() + blank +
                        step + blank +  // 添加默认值
                        (60) + blank +
                        bean.getStatus() + blank +
                        bean.getBody_bytes_sent() + blank +
                        bean.getHttp_referer() + blank +
                        bean.getHttp_user_agent() + blank;
                context.write(NullWritable.get(),new Text(newLogBean));
                session = UUID.randomUUID().toString();
                break;
            }
            // 如果不止1条数据。则将第一条跳过不输出，遍历到第二条在输出
            if (i==0){
                continue;
            }
            // 比较近两次时间差
            Date d1 = sdf.parse(bean.getTime_local());
            Date d2 = sdf.parse(list.get(i-1).getTime_local());
            long timeDiff = d1.getTime() - d2.getTime();
            // 如果本次时间-上次时间差<30分钟，则输出前一次页面的访问信息
            WebLogBean beforeBean = list.get(i - 1);
            if (timeDiff< 30 * 60 * 1000){
                String newLogBean =  session + blank +
                        k2.toString() + blank +
                        beforeBean.getRemote_user() + blank +
                        beforeBean.getTime_local() + blank +
                        beforeBean.getRequest() + blank +
                        step + blank +  // 添加默认值
                        (timeDiff/1000) + blank +
                        beforeBean.getStatus() + blank +
                        beforeBean.getBody_bytes_sent() + blank +
                        beforeBean.getHttp_referer() + blank +
                        beforeBean.getHttp_user_agent() + blank;
                context.write(NullWritable.get(),new Text(newLogBean));
                step ++;
            }else {
                // 如果本次 - 上次时间 > 30分钟，则输出前一次的页面访问信息并且将setp 重置
                String newLogBean =  session + blank +
                        k2.toString() + blank +
                        beforeBean.getRemote_user() + blank +
                        beforeBean.getTime_local() + blank +
                        beforeBean.getRequest() + blank +
                        step + blank +  // 添加默认值
                        (timeDiff/1000) + blank +
                        beforeBean.getStatus() + blank +
                        beforeBean.getBody_bytes_sent() + blank +
                        beforeBean.getHttp_referer() + blank +
                        beforeBean.getHttp_user_agent() + blank;
                context.write(NullWritable.get(),new Text(newLogBean));
                step = 1;
                session = UUID.randomUUID().toString();
            }
            if ( i == list.size() -1){
                // 设置默认停留 60s
                String newLogBean =  session + blank +
                        k2.toString() + blank +
                        bean.getRemote_user() + blank +
                        bean.getTime_local() + blank +
                        bean.getRequest() + blank +
                        step + blank +  // 添加默认值
                        (60) + blank +
                        bean.getStatus() + blank +
                        bean.getBody_bytes_sent() + blank +
                        bean.getHttp_referer() + blank +
                        bean.getHttp_user_agent() + blank;
                context.write(NullWritable.get(),new Text(newLogBean));
            }


        }

    }


}