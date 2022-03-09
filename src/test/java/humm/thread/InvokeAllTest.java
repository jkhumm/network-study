package humm.thread;
 
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
 
/**
 * 模拟业务场景：处理耗时任务，它会以最长的业务时间返回，不是响应式
 */
 
public class InvokeAllTest {
 
    //任务一
    public String do3Second() {
        try {
            System.out.println ("正在执行3秒的方法");
            TimeUnit.SECONDS.sleep (3);
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return Thread.currentThread ().getName () + "##3";
    }
    //任务二
    public String do5Second() {
        try {
            System.out.println ("正在执行5秒的方法");
            TimeUnit.SECONDS.sleep (5);
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return Thread.currentThread ().getName () + "##5";
    }
 
    public static void main(String[] args) throws Exception{
        long start = System.currentTimeMillis ();
        InvokeAllTest demo = new InvokeAllTest ();
        ExecutorService executorService = Executors.newFixedThreadPool (20);
        List<Callable<String>> callList = new ArrayList<> ();
        for (int value = 0; value < 5; value++) {
            callList.add (() -> demo.do3Second ());
            callList.add (() -> demo.do5Second ());
        }
        //把所有的任务放入到线程池里
   /*     FutureTask futureTask = new FutureTask(() -> {
            return "多线程执行同一个task只能被执行一次  里面有状态!= new";
        });*/
        List<Future<String>> futures =  executorService.invokeAll (callList);
        futures.forEach (strFture ->{
            while (true){
                //只有当所有任务执行了，返回结果集
                if (strFture.isDone ()){
                    System.out.println("is done"+ (System.currentTimeMillis () - start));
                    String s = null;
                    try {
                        s = strFture.get ();
                        System.out.println (s + "耗时：" + (System.currentTimeMillis () - start));
                        break;
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace ();
                    }
                }
            }
        });
        System.out.println ("阻塞此处 执行完毕");
        executorService.shutdown ();
    }
 
}