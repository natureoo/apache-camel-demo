package demo.chenj.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author chenj
 * @date 2019-01-13 17:06
 */
public class SpringCamel {

    private static Logger LOGGER = LoggerFactory.getLogger(SpringCamel.class);

    public static void main(String[] args) throws Exception {

        ApplicationContext ap = new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
        LOGGER.info("初始化....." + ap);
        // 没有具体的业务含义，只是保证主线程不退出
        synchronized (SpringCamel.class) {
            SpringCamel.class.wait();
        }
    }
}
