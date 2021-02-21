package di.demo;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Heian
 */
@Setter
@Getter
public class Test {

    private String name;

    private String height;

    List<String> list;

    public Test(String name) {
        this.name = name;
    }

    public Test(String name, String height) {
        this.name = name;
        this.height = height;
    }

    public Test(String name, String height, ArrayList<String> arrayList) {
        this.name = name;
        this.height = height;
        this.list = arrayList;
    }


    @Override
    public String toString() {
        return "test{" +
                "name='" + name + '\'' +
                ", height='" + height + '\'' +
                '}';
    }

    public static void main(String[] args) throws Exception {
        String name = "hmm";
        String height = "120";
        Object[] argsArray = new Object[3];
        argsArray[0] = name;
        argsArray[1] = height;
        argsArray[2] = new ArrayList<>(Arrays.asList("1","2"));
        Class<?>[] classes = new Class<?>[3];
        for (int i = 0; i < classes.length; i++) {
            classes[i] =  argsArray[i].getClass();
        }
        Constructor<Test> constructor = Test.class.getConstructor(classes);

        Test test = constructor.newInstance(argsArray);
        System.out.println(test.toString());

        Constructor ct = null;
        Constructor<?>[] constructors = test.getClass().getConstructors();
        for (Constructor<?> constructor1 : constructors) {
            Class<?>[] parameterTypes = constructor1.getParameterTypes();
            if (parameterTypes.length == argsArray.length){
                for (int i = 0; i < parameterTypes.length; i++) {
                    //判断构造函数的形参是否和我们实参数一致
                    if (!parameterTypes[i].isAssignableFrom(argsArray[i].getClass())){
                        System.out.println("不一致");
                        break;
                    }else {
                        continue;
                    }
                }
                ct = constructor1;
            }
        }
        System.out.println(ct);
        System.out.println(ct.equals(constructor));
    }

}
