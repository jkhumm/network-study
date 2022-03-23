package com.dongnaoedu.network.hadoop.mapreduce.sort;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class PairWritable implements WritableComparable<PairWritable> {

    private String first;
    private int second;
    
    @Override
    public int compareTo(PairWritable other){
        // 先比较first，first相同比较second
        int result = this.first.compareTo(other.first);
        if(result == 0){
           return this.second - other.second;
        }
        return result;
    }
                   
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        // 序列化
        dataOutput.writeUTF(first);
        dataOutput.writeInt(second);
    }
    
    @Override
    public void readFields(DataInput dataInput) throws IOException {
        // 反列化
       this.first = dataInput.readUTF();
       this.second = dataInput.readInt();
    }
                         


    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

}