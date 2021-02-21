package di;

import com.dongnaoedu.network.spring.bean.BeanReference;
import com.dongnaoedu.network.spring.bean.GenericBeanDefinition;
import com.dongnaoedu.network.spring.bean.SingletonBeanPreBuildFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DItest {
	static SingletonBeanPreBuildFactory bf = new SingletonBeanPreBuildFactory();

	@Test
	public void testConstructorDI() throws Exception {

		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setBeanClass(Lad.class);
		List<Object> args = new ArrayList<>();
		args.add("sunwukong");//属性一：名字
		args.add(new BeanReference("magicGril"));//属性二：ioc的bean依赖类型
		bd.setConstructorArgumentValues(args);
		bf.registerBeanDefinition("swk", bd);

		bd = new GenericBeanDefinition();
		bd.setBeanClass(MagicGril.class);
		args = new ArrayList<>();
		args.add("baigujing");//magicGirl的属性一：名字
		bd.setConstructorArgumentValues(args);
		bf.registerBeanDefinition("magicGril", bd);

		bf.preInstantiateSingletons();

		Lad abean = (Lad) bf.getBean("swk");

		abean.sayLove();
	}

	@Test
	public void testStaticFactoryMethodDI() throws Exception {

		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setBeanClass(BoyFactory.class);
		bd.setFactoryMethodName("getBean");
		List<Object> args = new ArrayList<>();
		args.add("niulang");
		args.add(new BeanReference("renmibi"));
		bd.setConstructorArgumentValues(args);
		bf.registerBeanDefinition("niulang", bd);

		bd = new GenericBeanDefinition();
		bd.setBeanClass(Renminbi.class);
		bf.registerBeanDefinition("renmibi", bd);

		bf.preInstantiateSingletons();

		Boy nl = (Boy) bf.getBean("niulang");
		nl.play();
	}

	@Test
	public void testFactoryMethodDI() throws Exception {

		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setFactoryBeanName("boyFactoryBean");
		bd.setFactoryMethodName("buildBoy");
		List<Object> args = new ArrayList<>();
		args.add("猪八戒");
		args.add(new BeanReference("xiaolongnu"));
		bd.setConstructorArgumentValues(args);
		bf.registerBeanDefinition("zhubajie", bd);

		bd = new GenericBeanDefinition();
		bd.setBeanClass(BoyFactoryBean.class);
		bf.registerBeanDefinition("boyFactoryBean", bd);

		bd = new GenericBeanDefinition();
		bd.setBeanClass(MagicGril.class);
		bf.registerBeanDefinition("xiaolongnu", bd);

		bf.preInstantiateSingletons();

		Boy abean = (Boy) bf.getBean("zhubajie");

		abean.sayLove();
	}

	@Test
	public void testChildTypeDI() throws Exception {

		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setBeanClass(Lad.class);
		List<Object> args = new ArrayList<>();
		args.add("niulang");
		args.add(new BeanReference("zhinv"));
		bd.setConstructorArgumentValues(args);
		bf.registerBeanDefinition("nl", bd);

		bd = new GenericBeanDefinition();
		bd.setBeanClass(MagicGril.class);
		args = new ArrayList<>();
		args.add("zhinv");
		bd.setConstructorArgumentValues(args);
		bf.registerBeanDefinition("zhinv", bd);

		bf.preInstantiateSingletons();

		Boy abean = (Boy) bf.getBean("nl");

		abean.sayLove();
	}

}
