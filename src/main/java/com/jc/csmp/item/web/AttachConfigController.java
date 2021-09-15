package com.jc.csmp.item.web;

import com.jc.csmp.item.domain.ItemClassify;
import com.jc.csmp.item.domain.ItemClassifyAttach;
import com.jc.csmp.item.domain.validator.ItemClassifyValidator;
import com.jc.csmp.item.service.IItemClassifyAttachService;
import com.jc.csmp.item.service.IItemClassifyService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.security.domain.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.FetchProfile;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * 附件配置控制器
 * @author 常鹏
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/config/attach")
public class AttachConfigController extends BaseController{

	@Autowired
	private IItemClassifyService itemClassifyService;
	@Autowired
	private IItemClassifyAttachService itemClassifyAttachService;

	@org.springframework.web.bind.annotation.InitBinder("itemClassify")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new ItemClassifyValidator());
	}

	public AttachConfigController() {
	}

	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	public String manage() throws Exception{
		return "csmp/config/attach/attachConfig";
	}

	@RequestMapping(value = "saveOrUpdateItem.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="项目分类附件",operateFuncNm="save",operateDescribe="新增操作")
	public Result saveOrUpdateItem(ItemClassify entity, HttpServletRequest request) throws Exception{
		if (StringUtil.isEmpty(entity.getId())) {
			return itemClassifyService.saveEntity(entity);
		}
		return itemClassifyService.updateEntity(entity);
	}

	@RequestMapping(value="getItem.action",method=RequestMethod.GET)
	@ResponseBody
	public ItemClassify getItem(ItemClassify entity) throws Exception {
		return itemClassifyService.get(entity);
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(ItemClassifyAttach entity, PageManager page){
		entity.addOrderByField("t.ext_num1");
		PageManager page_ = itemClassifyAttachService.query(entity, page);
		GlobalUtil.setTableRowNo(page_, page.getPageRows());
		return page_;
	}

	@RequestMapping(value="deleteItemById.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="项目分类附件",operateFuncNm="deleteItemById",operateDescribe="删除操作")
	public Result deleteByIds(String id) throws Exception{
		if (StringUtil.isEmpty(id)) {
			return Result.failure(ResultCode.PARAM_IS_BLANK);
		}
		ItemClassify entity = new ItemClassify();
		entity.setPrimaryKeys(new String[]{id});
		if(itemClassifyService.deleteByIds(entity) != 0){
			return Result.success();
		} else {
			return Result.failure(1, "删除失败");
		}
	}

	@RequestMapping(value = "itemTree.action", method = RequestMethod.GET)
	@ResponseBody
	public List<Department> itemTree() throws Exception{
		List<Department> treeList = new ArrayList<>();
		treeList.add(Department.createTreeData("0", "-1", "附件分类"));
		List<ItemClassify> dataList = this.itemClassifyService.queryAll(new ItemClassify());
		if (dataList != null) {
			for (ItemClassify item : dataList) {
				treeList.add(Department.createTreeData(item.getId(), "0", item.getItemClassify()));
			}
		}
		return treeList;
	}

	@RequestMapping(value = "saveOrUpdateAttach.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="项目分类附件",operateFuncNm="save",operateDescribe="新增操作")
	public Result saveOrUpdateAttach(ItemClassifyAttach entity, HttpServletRequest request) throws Exception{
		if (StringUtil.isEmpty(entity.getId())) {
			return itemClassifyAttachService.saveEntity(entity);
		}
		return itemClassifyAttachService.updateEntity(entity);
	}

	@RequestMapping(value="getAttach.action",method=RequestMethod.GET)
	@ResponseBody
	public ItemClassifyAttach getAttach(ItemClassifyAttach entity) throws Exception {
		return itemClassifyAttachService.get(entity);
	}

	@RequestMapping(value="deleteAttachById.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="项目分类附件",operateFuncNm="deleteItemById",operateDescribe="删除操作")
	public Result deleteAttachById(String id) throws Exception{
		if (StringUtil.isEmpty(id)) {
			return Result.failure(ResultCode.PARAM_IS_BLANK);
		}
		ItemClassifyAttach entity = new ItemClassifyAttach();
		entity.setPrimaryKeys(new String[]{id});
		if(itemClassifyAttachService.deleteByIds(entity) != 0){
			return Result.success();
		} else {
			return Result.failure(1, "删除失败");
		}
	}

	@RequestMapping(value="getAttachListByItemCode.action",method=RequestMethod.GET)
	@ResponseBody
	public List<ItemClassifyAttach> getAttachListByItemCode(String code) {
		if (StringUtil.isEmpty(code)) {
			return Collections.emptyList();
		}
		ItemClassifyAttach param = new ItemClassifyAttach();
		param.setItemCode(code);
		param.addOrderByField(" t.ext_num1 ");
		try {
			List<ItemClassifyAttach> dbList = this.itemClassifyAttachService.queryAll(param);
			if (dbList == null ||dbList.size() == 0) {
				dbList = new ArrayList<>();
				ItemClassifyAttach entity = new ItemClassifyAttach();
				entity.setIsCheckbox(1);
				entity.setIsRequired(1);
				entity.setItemAttach("附件信息");
				entity.setId("0");
				dbList.add(entity);
			}
			return dbList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return Collections.emptyList();
		}
	}
}

