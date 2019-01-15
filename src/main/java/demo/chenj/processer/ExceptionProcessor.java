package demo.chenj.processer;

import demo.chenj.HelloWorld;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ExceptionProcessor implements Processor {

    private static Logger LOGGER = LoggerFactory.getLogger(ExceptionProcessor.class);

    private int counter = -1;

       @Override
    public void process(Exchange exchange) throws Exception {
//        exchange.getContext().getEndpoints();
        LOGGER.info("Exception:"+exchange.getException());
    }

    private void prepareExchangeForFailover(Exchange exchange) {
        exchange.setException(null);

        exchange.setProperty(Exchange.ERRORHANDLER_HANDLED, null);
        exchange.setProperty(Exchange.FAILURE_HANDLED, null);
        exchange.setProperty(Exchange.EXCEPTION_CAUGHT, null);
        exchange.getIn().removeHeader(Exchange.REDELIVERED);
        exchange.getIn().removeHeader(Exchange.REDELIVERY_COUNTER);
    }
}
