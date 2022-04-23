package com.dongnaoedu.network.humm.算法.牛客网刷题;


import java.util.*;

/**
 * 描述
 * 写出一个程序，接受一个由字母、数字和空格组成的字符串，和一个字符，然后输出输入字符串中该字符的出现次数。（不区分大小写字母）
 *
 * 数据范围： 1 \le n \le 1000 \1≤n≤1000
 * 输入描述：
 * 第一行输入一个由字母和数字以及空格组成的字符串，第二行输入一个字符。
 *
 * 输出描述：
 * 输出输入字符串中含有该字符的个数。（不区分大小写字母）
 */
public class Main {

    // 写出一个程序，接受一个由字母、数字和空格组成的字符串，和一个字符，然后输出输入字符串中该字符的出现次数。（不区分大小写字母）
    public static void fun1(String str,String charStr){
        // 判断字符串是否是由字母、数字和空格组成
        if(str.matches("[a-zA-Z0-9 ]+")){
            System.out.println("输入的字符串是由字母、数字和空格组成");
            String[] split = str.split("");
            // 查找字符串在数组中出现的次数
            int count = 0;
            for (int i = 0; i < split.length; i++) {
                if(split[i].equalsIgnoreCase(charStr)){
                    count++;
                }
            }
            System.out.println("输入的字符串中"+charStr+"出现的次数是："+count);
        }else{
            System.out.println("error:---输入的字符串不是由字母、数字和空格组成");
        }
    }


    public static void main(String[] args) {

        System.out.println(7/8);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String str = scanner.nextLine();
            String charStr = scanner.nextLine();
            System.out.println("输入的字符串是："+str + "，输入的第二个字符是："+charStr);
            fun1(str,charStr);
        }
    }


    /**
     * 输入一个字符串，返回其最长的数字子串，以及其长度。若有多个最长的数字子串，则将它们全部输出（按原字符串的相对位置）
     * 本题含有多组样例输入。
     */
    public static void main92(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String next = sc.nextLine();
            // 将非数字的char转换为空格
            next = next.replaceAll("[^0-9]", " ");
            // 把多个空格替换为一个空格
            next = next.replaceAll("\\s+", " ");
            String[] s = next.split(" ");
            List<String> list = new ArrayList<>(Arrays.asList(s));
            list.removeAll(Arrays.asList("", null));


            List<String> result = new ArrayList<>();
            // 获取到list元素中每个字符的长度，求出最大的长度和对应的字符串
            int maxLength = 1;
            for (String str : list) {
                if (str.length() == maxLength){
                    result.add(str);
                }
                else if (str.length() > maxLength) {
                    maxLength = str.length();
                    result.clear();
                    result.add(str);
                }
            }
            for (String s1 : result) {
                System.out.print(s1);
            }
            System.out.println("," + result.get(0).length());

        }

    }

    /**
     * 解析：nextLine是遇到回车认为结束输入，next是遇到空白认为结束输入。
     * 所以如果有空格的情况下，当nextLine输入完毕之后回车就结束了nextLine然后输出了，
     * 然后到next输入的时候是早上好之后有了空格，那么他就认为是结束输入了，你后面输入的他都认为没有，
     * 所以输出的时候就是早上好，没有后面的东西。
     */
    public static void main80(String[] args) {
        /**
         * 将两个整型数组按照升序合并，并且过滤掉重复数组元素。
         * 输出时相邻两数之间没有空格。
         */
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            Set<Integer> set = new TreeSet<>();
            int size1 = sc.nextInt();
            for (int i = 0; i < size1; i++) {
                set.add(Integer.parseInt(sc.next()));
            }

            int size2 = sc.nextInt();
            for (int i = 0; i < size2; i++) {
                set.add(Integer.parseInt(sc.next()));
            }

            for (Integer integer : set) {
                System.out.print(integer);
            }
        }

    }

    public static void main81(String[] args) {
        /**
         * 将两个整型数组按照升序合并，并且过滤掉重复数组元素。
         * 输出时相邻两数之间没有空格。
         */
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] small = line.split("");
            Set<String> set1 = new HashSet<>(Arrays.asList(small));


            String line2 = sc.nextLine();
            String[] big = line2.split("");
            Set<String> set2 = new HashSet<>(Arrays.asList(big));
            int oldSize = set2.size();
            set2.addAll(set1);
            System.out.println(oldSize == set2.size());

        }

    }


    /**
     * 写出一个程序，接受十六进制的数，输出该数值的十进制表示。可输入多个
     */
    public static void mainUn1(String[] args) {
        Scanner sc = new Scanner(System.in);
        // 循环连续读取
        while(sc.hasNext()){
            String line = sc.nextLine();
            printResult(line);
        }
    }

    private static void printResult(String line) {
        // 截取前面的 0x，转为十进制,不能有前缀 0x
        Integer integer = Integer.parseInt(line.substring(2), 16);
        System.out.println(integer);
        // 十进制再转为16进制
        // String toHexString = Integer.toHexString(integer);
        // System.out.println("0x"+toHexString);
    }




}
