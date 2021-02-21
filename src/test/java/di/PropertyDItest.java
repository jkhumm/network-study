package di;

import com.dongnaoedu.network.spring.bean.BeanReference;
import com.dongnaoedu.network.spring.bean.GenericBeanDefinition;
import com.dongnaoedu.network.spring.bean.PropertyValue;
import com.dongnaoedu.network.spring.bean.SingletonBeanPreBuildFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PropertyDItest {
	static SingletonBeanPreBuildFactory bf = new SingletonBeanPreBuildFactory();

	@Test
	public void testPropertyDI() throws Exception {

		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setBeanClass(Lad.class);
		List<Object> args = new ArrayList<>();
		args.add("孙悟空");
		args.add(new BeanReference("bgj"));
		bd.setConstructorArgumentValues(args);
		bf.registerBeanDefinition("swk", bd);

		bd = new GenericBeanDefinition();
		bd.setBeanClass(MagicGril.class);
		List<PropertyValue> propertyValues = new ArrayList<>();
		propertyValues.add(new PropertyValue("name", "白骨精"));
		propertyValues.add(new PropertyValue("friend", new BeanReference("swk")));
		bd.setPropertyValues(propertyValues);
		bf.registerBeanDefinition("bgj", bd);

		bf.preInstantiateSingletons();

		MagicGril g = (MagicGril) bf.getBean("bgj");
		System.out.println(g.getName() + " " + g.getFriend());
		g.getFriend().sayLove();
	}

}
