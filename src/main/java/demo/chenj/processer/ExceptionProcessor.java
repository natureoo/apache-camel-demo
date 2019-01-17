package demo.chenj.processer;

import demo.chenj.HelloWorld;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.util.*;

public class ExceptionProcessor implements Processor {

    private static Logger LOGGER = LoggerFactory.getLogger(ExceptionProcessor.class);

    private int counter = -1;

       @Override
    public void process(Exchange exchange) throws Exception {
             Map<String, Object> properties = exchange.getProperties();
           Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);

           Object camelFailureEndpoint = properties.get("CamelFailureEndpoint");
           Object camelExceptionCaught = properties.get("CamelExceptionCaught");

           LOGGER.info("camelFailureEndpoint:"+camelFailureEndpoint);
           LOGGER.info("Exception:"+camelExceptionCaught);
    }


}
