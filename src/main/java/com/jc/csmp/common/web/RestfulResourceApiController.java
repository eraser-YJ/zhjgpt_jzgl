package com.jc.csmp.common.web;

import com.jc.csmp.project.service.ICmProjectInfoService;
import com.jc.mobile.util.MobileApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 开放给资源中心同步的接口
 * @Author 常鹏
 * @Date 2020/8/11 16:28
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/resource/restful")
public class RestfulResourceApiController {
    @Autowired
    private ICmProjectInfoService cmProjectInfoService;
    @RequestMapping(value = "finishProject.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse finishProject(@RequestParam("ids") String projectIds) throws Exception{
        return MobileApiResponse.fromResult(this.cmProjectInfoService.finishProject(projectIds));
    }
}
