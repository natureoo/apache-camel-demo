package demo.chenj.failover;

import demo.chenj.processer.ExceptionProcessor;
import demo.chenj.processer.HttpProcessor;
import demo.chenj.processer.OtherProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ModelCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class FailoverJavaCamel extends RouteBuilder {

    private static Logger LOGGER = LoggerFactory.getLogger(FailoverJavaCamel.class);

    public static void main(String[] args) throws Exception {
        // 这是camel上下文对象，整个路由的驱动全靠它了。
        ModelCamelContext camelContext = new DefaultCamelContext();
        // 启动route
        camelContext.start();
        // 将我们编排的一个完整消息路由过程，加入到上下文中
        camelContext.addRoutes(new FailoverJavaCamel());

        // 通用没有具体业务意义的代码，只是为了保证主线程不退出
        synchronized (FailoverJavaCamel.class) {
            FailoverJavaCamel.class.wait();
        }
    }

    @Override
    public void configure() throws Exception {
        List<String> list = new ArrayList<>();
        list.add("http://localhost:8081/camel/post?bridgeEndpoint=true");
        list.add("http://localhost:8082/camel/post?bridgeEndpoint=true");
        errorHandler(defaultErrorHandler()  .onExceptionOccurred(new ExceptionProcessor()));


        from("jetty:http://0.0.0.0:8282/httpCamel")
                .process(new HttpProcessor())
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .loadBalance().failover(1, true, false)
//                .to("http://localhost:8081/camel/post?bridgeEndpoint=true").
//                to("http://localhost:8082/camel/post?bridgeEndpoint=true")
                .to((String[])list.toArray(new String[0]))
                .end()
                 .process(new OtherProcessor());
    }










}