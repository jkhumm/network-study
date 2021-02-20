package di;

public class Lad implements Boy {
    private String name;
    private Gril friend;
    private Money money;
    
    public Lad(String name) {
        this.name = name;
    }
    public Lad(String name, Gril gf) {
        this.name = name;
        this.friend = gf;
        System.out.println("调用了含有Gril参数的构造方法");
    }
    public Lad(String name, MagicGril gf) {
        this.name = name;
        this.friend = gf;
        System.out.println("调用了含有MagicGril参数的构造方法");
    }
    public Lad(String name, Money m) {
        this.name = name;
        this.money = m;
        System.out.println("调用了含有Money参数的构造方法");
    }

    public Gril getFriend() {
        return friend;
    }

    public void setFriend(Gril friend) {
        this.friend = friend;
    }

    @Override
    public void sayLove() {
        if(friend != null){
            System.out.println("I love you, my gril! "+friend);
        }else {
            System.out.println("I love you, my air! "+hashCode());
        }
    }

    @Override
    public void play() {
        if(money != null) {
            System.out.println("I have money, let's play.");
        }else{
            System.out.println("I have no money, can't play.");
        }
    }

    public void init() {
        System.out.println("我还没谈过恋爱，初始化一个对象吧");
    }
    
    public void destroy(){
        System.out.println("七夕到底是牵手还是分手？");
    }
    
}
