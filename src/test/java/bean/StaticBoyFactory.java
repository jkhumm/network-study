package bean;

public class StaticBoyFactory {
    
    public static Boy getBean() {
        return new Lad();
    }
}
