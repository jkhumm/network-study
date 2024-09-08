package com.dongnaoedu.network.humm.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.hadoop.shaded.org.eclipse.jetty.util.ajax.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author heian
 * @date 2022/4/11 10:52 下午
 */
@Data
@AllArgsConstructor
public class GroupByDemo {

    private Integer year;

    private Integer month;

    private Integer day;

    public static void main(String[] args) {
        List<GroupByDemo> list = new ArrayList<>();
        list.add(new GroupByDemo(2020, 1, 1));
        list.add(new GroupByDemo(2020, 2, 2));
        list.add(new GroupByDemo(2020, 2, 3));

        list.add(new GroupByDemo(2021, 1, 4));
        list.add(new GroupByDemo(2021, 2, 5));
        list.add(new GroupByDemo(2021, 2, 6));

        list.add(new GroupByDemo(2022, 1, 7));
        list.add(new GroupByDemo(2022, 2, 8));
        list.add(new GroupByDemo(2022, 2, 9));


        // 对list进行多字段按照年月进行分组
        Map<Integer, Map<Integer, List<GroupByDemo>>> collect = list.stream().collect(Collectors.groupingBy(GroupByDemo::getYear,
                Collectors.groupingBy(GroupByDemo::getMonth)));

        System.out.println(JSON.toString(collect));
/**
 * {
 *     "2020":{
 *         "1":[
 *             "GroupByDemo(year=2020, month=1, day=1)"
 *         ],
 *         "2":[
 *             "GroupByDemo(year=2020, month=2, day=2)",
 *             "GroupByDemo(year=2020, month=2, day=3)"
 *         ]
 *     },
 *     "2021":{
 *         "1":[
 *             "GroupByDemo(year=2021, month=1, day=4)"
 *         ],
 *         "2":[
 *             "GroupByDemo(year=2021, month=2, day=5)",
 *             "GroupByDemo(year=2021, month=2, day=6)"
 *         ]
 *     },
 *     "2022":{
 *         "1":[
 *             "GroupByDemo(year=2022, month=1, day=7)"
 *         ],
 *         "2":[
 *             "GroupByDemo(year=2022, month=2, day=8)",
 *             "GroupByDemo(year=2022, month=2, day=9)"
 *         ]
 *     }
 * }
 */

    }



}
