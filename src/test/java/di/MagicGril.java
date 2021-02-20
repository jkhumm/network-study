package di;

public class MagicGril implements Gril {
    private String name;
    private Boy friend;
    public MagicGril(){}
    public MagicGril(String name) {
        this.name = name;
    }

    public Boy getFriend() {
        return friend;
    }
    public void setFriend(Boy friend) {
        this.friend = friend;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MagicGril{" +
                "name='" + name + '\'' +
                '}';
    }
}
