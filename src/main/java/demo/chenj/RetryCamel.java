package demo.chenj;

import demo.chenj.processer.ExceptionProcessor;
import demo.chenj.processer.StringUtil;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpMessage;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ModelCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class RetryCamel extends RouteBuilder{
    private static Logger LOGGER = LoggerFactory.getLogger(RetryCamel.class);

    public static void main(String[] args) throws Exception {
        // 这是camel上下文对象，整个路由的驱动全靠它了。
        ModelCamelContext camelContext = new DefaultCamelContext();
        // 启动route
        camelContext.start();
        // 将我们编排的一个完整消息路由过程，加入到上下文中
        camelContext.addRoutes(new RetryCamel());


        // 通用没有具体业务意义的代码，只是为了保证主线程不退出
        synchronized (RetryCamel.class) {
            RetryCamel.class.wait();
        }
    }

    @Override
    public void configure() throws Exception {
        // in case of io exception then try to redeliver up till 2 times
// (do not use any delay due faster unit testing)
//        onException(IOException.class)
//                .maximumRedeliveries(2).redeliveryDelay(0);

        errorHandler(defaultErrorHandler().maximumRedeliveries(2).onExceptionOccurred(new ExceptionProcessor()));

        from("jetty:http://0.0.0.0:8282/doHelloWorld")
                .to("log:direct:start?showExchangeId=true")
                // call sub route (using direct)
                .to("direct:sub")
                ;

        from("direct:sub")
                // disable error handler, so the entire route can be retried in case of redelivery
                .errorHandler(noErrorHandler())
                .to("log:sub?showExchangeId=true")
                .process(new MyProcessor());


//        from("jetty:http://0.0.0.0:8282/doHelloWorld")
//                .process(new MyProcessor())
//                .to("log:helloworld?showExchangeId=true");
    }


   public class MyProcessor implements Processor {
        private int counter;

        @Override
        public void process(Exchange exchange) throws Exception {
            // use a processor to simulate error in the first 2 calls
            LOGGER.info("MyProcessor:"+exchange.toString());
            if (counter++ < 2) {
                throw new IOException("Forced");
            }
            exchange.getIn().setBody("Bye World");
        }
    }
}







