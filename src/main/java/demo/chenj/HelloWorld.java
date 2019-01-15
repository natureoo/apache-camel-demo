package demo.chenj;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HelloWorld extends RouteBuilder {
    private static Logger LOGGER = LoggerFactory.getLogger(HelloWorld.class);

    public static void main(String[] args) throws Exception {
        LOGGER.info("helloworld");
        // 这是camel上下文对象，整个路由的驱动全靠它了。
        ModelCamelContext camelContext = new DefaultCamelContext();
        // 启动route
        camelContext.start();
        // 将我们编排的一个完整消息路由过程，加入到上下文中
        camelContext.addRoutes(new HelloWorld());


        // 通用没有具体业务意义的代码，只是为了保证主线程不退出
        synchronized (HelloWorld.class) {
            HelloWorld.class.wait();
        }
    }

    @Override
    public void configure() throws Exception {
        // 在本代码段之下随后的说明中，会详细说明这个构造的含义
        from("jetty:http://0.0.0.0:8282/doHelloWorld")
                .process(new HttpProcessor())
                .to("log:demo.chenj.HelloWorld?showExchangeId=true");//log:匹配logger
    }

    public class HttpProcessor implements Processor {

        /* (non-Javadoc)
         * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
         */
        @Override
        public void process(Exchange exchange) throws Exception {
            // 因为很明确消息格式是http的，所以才使用这个类
            // 否则还是建议使用org.apache.camel.Message这个抽象接口
            HttpMessage message = (HttpMessage)exchange.getIn();
            InputStream bodyStream =  (InputStream)message.getBody();
            String inputContext = StringUtil.analysisMessage(bodyStream);
            bodyStream.close();

            // 存入到exchange的out区域
            if(exchange.getPattern() == ExchangePattern.InOut) {
                Message outMessage = exchange.getOut();
                outMessage.setBody(inputContext + " || out");
            }
        }

    }
}
