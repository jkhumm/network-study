package func;

/**
 * @author humingming
 * @date 2024/4/18 22:56
 */
public class Test {



    public static void main(String[] args) {
//        boolean b = true;
//        if (b) {
//            System.out.println("输出true");
//        }else {
//            throw new RuntimeException("输出false");
//        }

        // 主要的核心就是编写函数接口，然后在编写对应接口的实现

        // 方式一: 入参为错误消息
        FuncUtil.getThrowExFunction(false).throwExceptionMsg("输出false");

        // 方式二：入参为两个分支的runnable
        FuncUtil.getBranchHandler(true).trueOrFalse(()->{
            System.out.println("输出true");
        },()->{
            throw new RuntimeException("输出false");
        });

        // 方式三：存在值执行消费，不存在则做空处理操作
        FuncUtil.getPresentOrElseHandler("hello").presentOrElse(System.out::println,()->{
            System.out.println("输出false");
        });


    }



}
