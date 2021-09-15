package com.jc.test.web;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(value = "API - DocTestController", description = "测试接口详情")
@RestController
@RequestMapping("/test")
public class DocTestController {

	/**
	 * @ApiOperation 这个注解是指该方法是用来做什么的,一定要加上httpMethod,否则会出现一堆
	 * @param userVo
	 * @return
	 */ 
	@ApiOperation(value = "查看用户",httpMethod="GET") 
	@ApiImplicitParam(required = true, name = "userName", value = "用户名称")
	@RequestMapping(value = "findUser", method = RequestMethod.GET) 
	public String findUser(@RequestBody String userName){
		System.out.println(userName);
		return "{\"userName\":\""+userName+"\"}"; 
	}
	
}
