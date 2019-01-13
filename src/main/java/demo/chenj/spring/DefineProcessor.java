package demo.chenj.spring;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chenj
 * @date 2019-01-13 17:21
 * @email 924943578@qq.com
 */
@Component("defineProcessor")
public class DefineProcessor implements Processor {
    /**
     * 日志
     */
    private static Logger LOGGER = LoggerFactory.getLogger(DefineProcessor.class);

    @Autowired
    private DoSomethingService somethingService;

    @Override
    public void process(Exchange exchange) throws Exception {
        // 调用somethingService，说明它正常工作
        this.somethingService.doSomething("DefineProcessor");
        // 这里在控制台打印一段日志，证明这个Processor正常工作了，就行
        DefineProcessor.LOGGER.info("process(Exchange exchange) ... ");
    }
}
