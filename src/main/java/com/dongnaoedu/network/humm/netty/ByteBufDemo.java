package com.dongnaoedu.network.humm.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledDirectByteBuf;

import java.util.Arrays;

/**
 * @author Heian
 * @time 19/06/23 21:22
 * 用途：零拷贝（堆外内存省去了jvm复制拷贝）、动态扩容、内存复用（池化）
 */
public class ByteBufDemo {

    //  +-------------------+------------------+------------------+
    //  | discardable bytes |  readable bytes  |  writable bytes  |
    //  |     已读可丢弃           可读区域             待写区域      |
    //  +-------------------+------------------+------------------+
    //  |                   |                  |                  |
    //  0      <=       readerIndex   <=   writerIndex    <=    capacity

    public static void main(String[] args) {
        ByteBuf bf = Unpooled.buffer (10);//分配一个非池话的（堆内内存）UnpooledDirectByteBuf

        byte[] bytes = {1,2,3,4,5,6};
        System.out.println ("堆内内存bf" + bf.toString () + Arrays.toString (bf.array ()));//(ridx: 0, widx: 0, cap: 10)
        bf.writeBytes (bytes);
        System.out.println ("写入后堆内内存bf" +bf.toString () + Arrays.toString (bf.array ()));//(ridx: 0, widx: 5, cap: 10)
        byte b1 = bf.readByte ();
        byte b2 = bf.readByte ();
        System.out.println ("读入后堆内内存bf" +bf.toString () + Arrays.toString (bf.array ()));//(ridx: 2, widx: 6, cap: 10)
        //将读取的内容抛弃  抛弃后的读内存 配腾出来，然后写区域往空出来的移动，然后右边读出来的个数  写入的个数不动
        bf.discardReadBytes ();
        System.out.println ("抛弃后堆内内存bf" + bf.toString () + Arrays.toString (bf.array ()));//(ridx: 0, widx: 4, cap: 10)
        bf.writeBytes (new byte[]{9});//会根据写指针位置继续写入元素，如果该位置有元素则会覆盖
        System.out.println ("重新写入后的内存bf" + bf.toString () + Arrays.toString (bf.array ()));//(ridx: 0, widx: 5, cap: 10)
        //清空指针（元素不变，指针清零）
        bf.clear();
        System.out.println ("清空指针后的内存bf" + bf.toString () + Arrays.toString (bf.array ()));//(ridx: 0, widx: 5, cap: 10)
        bf.writeBytes (new byte[]{8,8,8});//会根据写指针位置继续写入元素，如果该位置有元素则会覆盖
        System.out.println ("清空指针后再次写入bf" + bf.toString () + Arrays.toString (bf.array ()));//(ridx: 0, widx: 5, cap: 10)
        //将bf清零（元素清零，指针位置不变）
        bf.setZero (0,bf.capacity ());
        System.out.println ("清零后bf" + bf.toString () + Arrays.toString (bf.array ()));//(ridx: 0, widx: 5, cap: 10)
        //写入超过容量的字节数组   会自动扩容 3+9=14  与64比较
        bf.writeBytes (new byte[]{1,2,3,4,5,6,7,8,9,0,1,2,3,4});
        System.out.println ("写入超过容量的bf" + bf.toString () + Arrays.toString (bf.array ()));//(ridx: 0, widx: 5, cap: 10)



    }


}
