package bean;

public class Lad implements Boy {
    @Override
    public void sayLove() {
        System.out.println("I love you, my dear!"+hashCode());
    }
    
    public void init() {
        System.out.println("我还没谈过恋爱，初始化一个对象吧");
    }
    
    public void destroy(){
        System.out.println("七夕到底是牵手还是分手？");
    }
}
