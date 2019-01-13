package demo.chenj.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenj
 * @date 2019-01-13 17:10
 */
public class DoSomethingServiceImpl implements DoSomethingService {

    private static Logger LOGGER = LoggerFactory.getLogger(DoSomethingServiceImpl.class);

    @Override
    public void doSomething(String userid) {
        LOGGER.info("doSomething(String userid) ...");

    }
}
