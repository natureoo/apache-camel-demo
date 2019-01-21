package demo.chenj.ctrl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenj
 * @date 2019-01-13 17:21
 * @email 924943578@qq.com
 */
@RestController
@RequestMapping("/camel")
public class CamelController {

    @PostMapping("/post")
    public String post(@RequestBody String payload) throws JsonProcessingException {
        // TODO
        return payload;

    }



}