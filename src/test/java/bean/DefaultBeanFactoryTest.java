package bean;
import com.dongnaoedu.network.spring.bean.BeanDefinition;
import com.dongnaoedu.network.spring.bean.DefaultBeanFactory;
import com.dongnaoedu.network.spring.bean.GenericBeanDefinition;
import org.junit.Test;

public class DefaultBeanFactoryTest {
    static DefaultBeanFactory bf = new DefaultBeanFactory();

    @Test
    public void testRegist() throws Exception {
        //普通的构造方法
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(Lad.class);
        bd.setScope(BeanDefinition.SCOPE_SINGLETON);
        // bd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        bd.setInitMethod("init");
        bd.setDestroyMethod("destroy");
        bf.registerBeanDefinition("lad", bd);
        System.out.println("构造方法方式------------");
        for (int i = 0; i < 3; i++) {
            Boy boy = (Boy) bf.getBean("lad");
            boy.sayLove();
        }
    }

    @Test
    public void testRegistStaticFactoryMethod() throws Exception {
        //静态工厂
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(StaticBoyFactory.class);
        bd.setFactoryMethodName("getBean");
        bf.registerBeanDefinition("staticBoyFactory", bd);
        System.out.println("静态工厂方法方式------------");
        for (int i = 0; i < 3; i++) {
            Boy ab = (Boy) bf.getBean("staticBoyFactory");
            ab.sayLove();
        }
    }

    @Test
    public void testRegistFactoryMethod() throws Exception {
        //普通工厂
        String beanName1 = "factoryBean";//工厂bean
        String beanName2 = "factoryBoy";//你想要生成的实例bean

        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(BoyFactory.class);
        bf.registerBeanDefinition(beanName1, bd);

        bd = new GenericBeanDefinition();
        bd.setFactoryBeanName(beanName1);
        bd.setFactoryMethodName("buildBoy");
        bd.setScope(BeanDefinition.SCOPE_PROTOTYPE);

        bf.registerBeanDefinition(beanName2, bd);
        System.out.println("工厂方法方式------------");
        for (int i = 0; i < 3; i++) {
            Boy ab = (Boy) bf.getBean("factoryBoy");
            ab.sayLove();
        }
    }

    //@AfterClass
    public static void testGetBean() throws Exception {
        System.out.println("构造方法方式------------");
        for (int i = 0; i < 3; i++) {
            Boy boy = (Boy) bf.getBean("lad");
            boy.sayLove();
        }

        System.out.println("静态工厂方法方式------------");
        for (int i = 0; i < 3; i++) {
            Boy ab = (Boy) bf.getBean("staticBoyFactory");
            ab.sayLove();
        }

        System.out.println("工厂方法方式------------");
        for (int i = 0; i < 3; i++) {
            Boy ab = (Boy) bf.getBean("factoryBoy");
            ab.sayLove();
        }

        bf.close();
    }
    
}
