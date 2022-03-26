package com.dongnaoedu.network.hadoop.weblog.pre;



import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author heian
 * @date 2022/3/26 3:09 下午
 * @description 解析每一行工具类
 */
public class WebLoParse {

    public static SimpleDateFormat df1 = new SimpleDateFormat("dd/MMM/yyy:HH:mm:ss", Locale.US);
    public static SimpleDateFormat dfw = new SimpleDateFormat("dd-MMM-yyy HH:mm:ss", Locale.US);


    public static WebLogBean logParse(String line){
        WebLogBean bean = new WebLogBean();
        String[] arr = line.split(" ");
        // 数据是否有效
        if (arr.length > 11) {
            bean.setRemote_addr(arr[0]);
            bean.setRemote_user(arr[1]);
            String time_local = "";
/*            if("".equals(time_local)){
                time_local = "-invalid_time-";
                bean.setTime_local(time_local);
            }*/
            bean.setRequest(arr[6]);
            bean.setStatus(arr[8]);
            bean.setBody_bytes_sent(arr[9]);
            bean.setHttp_referer(arr[10]);

            // 如果 useragent元素过多，拼接
            if (arr.length>12) {
                StringBuilder sb = new StringBuilder();
                for (int i = 11; i < arr.length; i++) {
                    sb.append(arr[i]);
                }
                bean.setHttp_user_agent(sb.toString());
            }else {
                bean.setHttp_user_agent(arr[11]);
            }
            // http错误  无效时间
            if (Integer.parseInt(bean.getStatus()) >= 400 || "-invalid_time-".equals(bean.getTime_local()))
                bean.setValid(false);
        }else {
            bean = null;
        }
        return bean;
    }

}
