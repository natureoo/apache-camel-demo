package demo.chenj.failover;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.processor.loadbalancer.SimpleLoadBalancerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class CustomLoadBalance extends SimpleLoadBalancerSupport {

    private static Logger LOGGER = LoggerFactory.getLogger(FailoverXmlCamel.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        List<Processor> processorsList = getProcessors();

            LOGGER.info("调用路由1" );
            processorsList.get(0).process(exchange);

        Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        if(cause != null){
            LOGGER.info("after调用路由1:"+cause );
            LOGGER.info("发邮件告警" );

        }


        exchange.setProperty(Exchange.EXCEPTION_CAUGHT, null);
        LOGGER.info("调用路由2" );
        processorsList.get(1).process(exchange);
        cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        if(cause != null){
            LOGGER.info("after调用路由2:"+cause );
            LOGGER.info("发邮件告警" );

        }

        LOGGER.info("结束调用" );
    }
}
