package demo.chenj.processer;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * 另一个处理器OtherProcessor
 */
public class OtherProcessor implements Processor {

    private static Logger LOGGER = LoggerFactory.getLogger(ExceptionProcessor.class);


    @Override
    public void process(Exchange exchange) throws Exception {
        Message message = exchange.getIn();
        String returnBody = StringUtil.analysisMessage((InputStream) message.getBody());

        LOGGER.info("The response code is: {} response message is: {}", exchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE), returnBody);

//
        Message outMessage = exchange.getOut();
        outMessage.setBody(returnBody + " || 被OtherProcessor处理");

    }
}