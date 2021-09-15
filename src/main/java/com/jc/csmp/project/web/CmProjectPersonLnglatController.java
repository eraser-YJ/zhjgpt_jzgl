package com.jc.csmp.project.web;

import com.jc.csmp.project.domain.CmProjectPersonLnglat;
import com.jc.csmp.project.service.ICmProjectPersonLnglatService;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.resource.util.ResourceDbServer;
import com.jc.system.applog.ActionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 建设管理-人员定位列表
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/project/personlnglat")
public class CmProjectPersonLnglatController extends BaseController{

	@Autowired
	private ICmProjectPersonLnglatService CmProjectPersonLnglatService;

	/**
	 * 获取单条记录方法
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="get",operateDescribe="对进行单条查询操作")
	public CmProjectPersonLnglat get(CmProjectPersonLnglat entity) throws Exception{
		return CmProjectPersonLnglatService.get(entity);
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(CmProjectPersonLnglat entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager page_ = CmProjectPersonLnglatService.query(entity, page);
		int no = page.getPage() * page.getPageRows() + 1;
		if (page.getPage() != 0) {
			no = (page.getPage() - 1) * page.getPageRows() + 1;
		}
		List<CmProjectPersonLnglat> dataList = (List<CmProjectPersonLnglat>) page_.getData();
		if (dataList != null) {
			for (CmProjectPersonLnglat obj : dataList) {
				obj.setTableRowNo(no++);
				obj.setProjectName(ResourceDbServer.getInstance().getProjectName(obj.getProjectNumber()));
			}
		}
		return page_;
	}

	/**
	* @description 跳转方法
	* @return String 跳转的路径
	* @throws Exception
	* @author
	* @version  2020-04-10
	*/
	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="对进行跳转操作")
	public String manage(HttpServletRequest request) throws Exception{
		return "csmp/project/personlnglat/cmProjectPersonLnglatList";
	}

	/**
	 * 删除方法
	 * @param entity
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="deleteByIds",operateDescribe="对进行删除")
	public  Map<String, Object> deleteByIds(CmProjectPersonLnglat entity, String ids) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		entity.setPrimaryKeys(ids.split(","));
		if(CmProjectPersonLnglatService.deleteByIds(entity) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

    /**
     * 分页查询方法
     * @param entity
     * @param entity
     * @return
     */
    @RequestMapping(value="queryAll.action",method=RequestMethod.GET)
    @ResponseBody
    public List<CmProjectPersonLnglat> queryAll(CmProjectPersonLnglat entity) throws CustomException {
        List<CmProjectPersonLnglat> list;
        if(StringUtil.isEmpty(entity.getOrderBy())) {
            entity.addOrderByField("t.CREATE_DATE");
        }
        list = CmProjectPersonLnglatService.queryAll(entity);
        return list;
    }

}

