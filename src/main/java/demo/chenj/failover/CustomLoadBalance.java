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
    public void process(Exchange exchange)  {
        List<Processor> processorsList = getProcessors();

        //选取其中一个调用
        try {
            LOGGER.info("调用路由1" );
            processorsList.get(0).process(exchange);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("调用路由1:" +e.toString());
        }
        LOGGER.info("after调用路由1" );

        try {
            processorsList.get(1).process(exchange);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("调用路由2:" +e.toString());
        }
        LOGGER.info("after调用路由2" );
    }
}
