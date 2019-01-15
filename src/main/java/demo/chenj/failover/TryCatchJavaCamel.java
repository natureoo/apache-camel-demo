package demo.chenj.failover;

import demo.chenj.processer.ExceptionProcessor;
import demo.chenj.processer.HttpProcessor;
import demo.chenj.processer.OtherProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;


public class TryCatchJavaCamel extends RouteBuilder {

    private static Logger LOGGER = LoggerFactory.getLogger(TryCatchJavaCamel.class);

    public static void main(String[] args) throws Exception {
        // 这是camel上下文对象，整个路由的驱动全靠它了。
        ModelCamelContext camelContext = new DefaultCamelContext();
        // 启动route
        camelContext.start();
        // 将我们编排的一个完整消息路由过程，加入到上下文中
        camelContext.addRoutes(new TryCatchJavaCamel());

        // 通用没有具体业务意义的代码，只是为了保证主线程不退出
        synchronized (TryCatchJavaCamel.class) {
            TryCatchJavaCamel.class.wait();
        }
    }

    @Override
    public void configure() throws Exception {

        errorHandler(defaultErrorHandler()  .onExceptionOccurred(new ExceptionProcessor()));


        from("jetty:http://0.0.0.0:8282/httpCamel")
                .routeId("myRoute1")
                .process(new HttpProcessor())
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .loadBalance().failover()
                .doTry()
                .to("http://localhost:8081/camel/post?bridgeEndpoint=true")
                .doCatch(IOException.class).process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        exchange.getContext();
//                        List<ProcessorDefinition<?>> outputProcessorDefs = exchange.getContext().getRouteDefinition("[routeId]").getOutputs();

                        LOGGER.info("8081 IOException");
                    }
                })
                .doTry()
                .to("http://localhost:8082/camel/post?bridgeEndpoint=true")
                .doCatch(IOException.class).process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        List<ProcessorDefinition<?>> outputProcessorDefs = exchange.getContext().getRouteDefinition("[routeId]").getOutputs();

                        LOGGER.info("8082 IOException");
                    }
                })
                .end()
                .process(new OtherProcessor());
    }










}
