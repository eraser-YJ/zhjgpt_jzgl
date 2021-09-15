package com.jc.plugin.kafka.web;

import com.jc.foundation.util.Result;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * kafka测试
 * @Author changpeng
 * @Date 2020/7/3 14:47
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/kafka/demo")
public class KafkaController extends BaseController {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @RequestMapping("put.action")
    @ResponseBody
    public Result put(HttpServletRequest request) {
        String content = request.getParameter("c");
        if (!StringUtil.isEmpty(content)) {
            kafkaTemplate.sendDefault(content);
        }
        return Result.success();
    }
}
