package cn.edu.xmu.goods.utility;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHelper implements ApplicationContextAware {

    private static ApplicationContext context=null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHelper.context=applicationContext;
    }

    public static <T> T getBean(String beanName){
        return (T)context.getBean(beanName);
    }

    public static <T> T getBean(Class<T> c){
        return (T)context.getBean(c);
    }

}
