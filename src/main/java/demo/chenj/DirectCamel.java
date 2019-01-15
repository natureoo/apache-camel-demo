package demo.chenj;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ModelCamelContext;

public class DirectCamel {

    public static void main(String[] args) throws Exception {
        // 这是camel上下文对象，整个路由的驱动全靠它了。
        ModelCamelContext camelContext = new DefaultCamelContext();
        // 启动route
        camelContext.start();
        // 首先将两个完整有效的路由注册到Camel服务中
        camelContext.addRoutes((new DirectCamel()).new DirectRouteA());
        camelContext.addRoutes((new DirectCamel()).new DirectRouteB());

        // 通用没有具体业务意义的代码，只是为了保证主线程不退出
        synchronized (DirectCamel.class) {
            DirectCamel.class.wait();
        }
    }

    public class DirectRouteA extends RouteBuilder {

        /* (non-Javadoc)
         * @see org.apache.camel.builder.RouteBuilder#configure()
         */
        @Override
        public void configure() throws Exception {
            from("jetty:http://0.0.0.0:8282/directCamel")
                    // 连接路由：DirectRouteB
                    .to("direct:directRouteB")
                    .to("log:DirectRouteA?showExchangeId=true");
        }
    }


    public class DirectRouteB extends RouteBuilder {
        /* (non-Javadoc)
         * @see org.apache.camel.builder.RouteBuilder#configure()
         */
        @Override
        public void configure() throws Exception {
            from("direct:directRouteB")
                    .to("log:DirectRouteB?showExchangeId=true");
        }
    }
}
