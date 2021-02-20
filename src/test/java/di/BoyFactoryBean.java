package di;

public class BoyFactoryBean {
    
    public Boy buildBoy(String name, Gril g) {
        return new Lad(name , g);
    }
}
