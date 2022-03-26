package com.dongnaoedu.network.hadoop.mapreduce.flowcount.partition;


import com.dongnaoedu.network.hadoop.mapreduce.flowcount.FlowBean;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * @author heian
 * @date 2022/3/24 4:02 下午
 * @description
 */
public class FlowPartition extends Partitioner<Text, FlowBean> {


    @Override
    public int getPartition(Text k2, FlowBean flowBean, int i) {
        if (k2.toString().startsWith("135")){
            return 0;
        }else if (k2.toString().startsWith("136")){
            return 1;
        }else if (k2.toString().startsWith("137")){
            return 2;
        }else {
            return 3;
        }
    }


}
