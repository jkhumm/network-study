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



}
