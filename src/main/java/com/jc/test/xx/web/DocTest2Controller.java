package com.jc.test.xx.web;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.Result;
import com.jc.foundation.util.ResultCode;
import com.jc.system.security.domain.Phrase;
import com.jc.system.security.service.IPhraseService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "API - DocTest2Controller", description = "测试接口详情")
@RestController
@RequestMapping("/test")
public class DocTest2Controller {

	protected final Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private IPhraseService phraseService;

	/**
	 * 获取分页列表
	 * @return
	 */
	@ApiOperation(value="常用词-获取分页列表", notes="常用词-获取分页列表")
	@RequestMapping(value = "/listPage",method = RequestMethod.GET)
	public Result queryPageList() {
		Phrase phrase = new Phrase();
		PageManager page = new PageManager();
		page.setPage(1);
		return Result.success(phraseService.query(phrase,page));
	}

	/**
	 * 分页列表查询
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@ApiOperation(value="常用词-分页列表查询", notes="常用词-分页列表查询")
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Result queryPageList(@RequestParam(name="phraseType", defaultValue="0")  String phraseType,
								@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								@RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		Phrase phrase = new Phrase();
		phrase.setPhraseType(phraseType);
		PageManager page = new PageManager();
		page.setPage(pageNo);
		page.setPageRows(pageSize);
		page = phraseService.query(phrase,page);
		return Result.success(page);
	}

	/**
	 * @ApiOperation 这个注解是指该方法是用来做什么的,一定要加上httpMethod,否则会出现一堆
	 * @param
	 * @return
	 */
	@ApiOperation("查询全部记录")
	@RequestMapping(value = "/phraseList",method = RequestMethod.GET)
	public Result getList(){

		List<Phrase> list = null;
		try {
			list = this.phraseService.queryAll(new Phrase());
		} catch (CustomException e) {
			logger.error(e);
			return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
		}
		return Result.success(list);
	}
	//添加一条数据
	@ApiOperation("添加一条记录")
	@PostMapping(value = "/savePerson",consumes = "application/json", produces = "application/json")
	public Result savePerson(@RequestBody Phrase phrase){
		Phrase p = new Phrase();
		p.setContent(phrase.getContent());
		p.setPhraseType(phrase.getPhraseType());
		try {
			if(p.getId()==null||"".equals(p.getId())) {
				this.phraseService.save(p);
			}else {
				this.phraseService.update(p);
			}
		} catch (CustomException e) {
			logger.error(e);
			return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
		}
		return Result.success();
	}
	//查询单个记录
	@ApiOperation("根据ID查询")
	@RequestMapping(value = "/getPhraseOne/{id}",method = RequestMethod.GET)
	public Result getPersonOne(@ApiParam("ID")@PathVariable String id){
		Result result = new Result();
		if (id == null || "".equals(id)) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}

		Phrase phrase = new Phrase();
		try {
			phrase.setId(id);
			phrase = this.phraseService.get(phrase);
		} catch (CustomException e) {
			logger.error(e);
			return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
		}
		return Result.success(phrase);
	}
	//修改单条记录
	@ApiOperation("根据ID修改记录")
	@RequestMapping(value = "/updatePhrase/{id}",method = RequestMethod.PUT)
	public Result updatePerson(@ApiParam("被修改的ID")@PathVariable String id,@RequestBody Phrase phrase){

		Result result = new Result();

		//参数校验
		if (id == null || "".equals(id)) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		} else if (phrase == null) {
			return Result.failure(ResultCode.PARAM_IS_BLANK);
		} else if (phrase.getContent() == null || "".equals(phrase.getContent())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		} else {
			try {
				Phrase p = new Phrase();
				p.setId(id);
				p.setContent(phrase.getContent());
				p.setPhraseType(phrase.getPhraseType());
				this.phraseService.update(p);
				result.setData(p);
				result.setResultCode(ResultCode.SUCCESS);
			} catch (CustomException e) {
				logger.error(e);
				return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
			}
		}
		return result;
	}

	//删除一条数据
	@ApiOperation("根据ID删除记录")
	@RequestMapping(value = "/deletePhrase/{id}",method = RequestMethod.DELETE)
	public Result deletePersonById(@ApiParam("被删除的ID")@PathVariable String id){

 		Result result = new Result();
		if (id == null || "".equals(id)) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}

		Phrase p = new Phrase();
		p.setPrimaryKeys(new String[]{id});
		try {
			this.phraseService.delete(p);
			result.setData(p);
			result.setResultCode(ResultCode.SUCCESS);
		} catch (CustomException e) {
			logger.error(e);
			return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
		}
		return result;
	}

}
