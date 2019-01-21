package demo.chenj.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author chenj
 * @date 2019-01-13 17:10
 */
@Service
public class DoSomethingServiceImpl implements DoSomethingService {

    private static Logger LOGGER = LoggerFactory.getLogger(DoSomethingServiceImpl.class);

    @Override
    public void doSomething(String userid) {
        LOGGER.info("doSomething(String userid) ...");

    }
}
