package demo.chenj;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ModelCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicCamel  {

    private static Logger LOGGER = LoggerFactory.getLogger("");

    public static void main(String[] args) throws Exception {
        // 这是camel上下文对象，整个路由的驱动全靠它了。
        ModelCamelContext camelContext = new DefaultCamelContext();
        // 启动route
        camelContext.start();
        // 将我们编排的一个完整消息路由过程，加入到上下文中
        camelContext.addRoutes((new DynamicCamel()).new DirectRouteA());
        camelContext.addRoutes((new DynamicCamel()).new DirectRouteB());
        camelContext.addRoutes((new DynamicCamel()).new DirectRouteC());

        // 通用没有具体业务意义的代码，只是为了保证主线程不退出
        synchronized (DynamicCamel.class) {
            DynamicCamel.class.wait();
        }
    }

    public class DirectRouteA extends RouteBuilder {

        /* (non-Javadoc)
         * @see org.apache.camel.builder.RouteBuilder#configure()
         */
        @Override
        public void configure() throws Exception {
            from("jetty:http://0.0.0.0:8282/dynamicCamel")
                    .setExchangePattern(ExchangePattern.InOnly)
                    .recipientList().jsonpath("$.data.routeName").delimiter(",")
                    .end()
                    .process(new OtherProcessor());
        }
    }

    public class DirectRouteB extends RouteBuilder {
        /* (non-Javadoc)
         * @see org.apache.camel.builder.RouteBuilder#configure()
         */
        @Override
        public void configure() throws Exception {
            // 第二个路由和第三个路由的代码都相似
            // 唯一不同的是类型
            from("direct:directRouteB")
                    .to("log:DirectRouteB?showExchangeId=true");
        }
    }

    public class DirectRouteC extends RouteBuilder {
        /* (non-Javadoc)
         * @see org.apache.camel.builder.RouteBuilder#configure()
         */
        @Override
        public void configure() throws Exception {
            // 第二个路由和第三个路由的代码都相似
            // 唯一不同的是类型
            from("direct:directRouteC")
                    .to("log:DirectRouteC?showExchangeId=true");
        }
    }

    public class OtherProcessor implements Processor {
        /* (non-Javadoc)
         * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
         */
        @Override
        public void process(Exchange exchange) throws Exception {
            Message message = exchange.getIn();
            LOGGER.info(" OtherProcessor中的exchange" + exchange);
            String body = message.getBody().toString();

            // 存入到exchange的out区域
            if(exchange.getPattern() == ExchangePattern.InOut) {
                Message outMessage = exchange.getOut();
                outMessage.setBody(body + " || 被OtherProcessor处理");
            }
        }
    }
}
