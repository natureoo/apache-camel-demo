package demo.chenj.processer;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.http.common.HttpMessage;

import java.io.InputStream;

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
        Message outMessage = exchange.getOut();
        outMessage.setBody("{\"title\": \"The title\", \"content\": \"The content\"}");
    }

    /**
     * 从stream中分析字符串内容
     * @param bodyStream
     * @return
     */

}