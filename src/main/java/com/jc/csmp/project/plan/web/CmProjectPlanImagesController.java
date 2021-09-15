package com.jc.csmp.project.plan.web;

import com.jc.csmp.project.plan.domain.CmProjectPlanImages;
import com.jc.csmp.project.plan.service.ICmProjectPlanImagesService;
import com.jc.foundation.domain.Attach;
import com.jc.foundation.web.BaseController;
import com.jc.system.content.service.IAttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 建设管理-项目形象进度 管理Controller
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/project/plan/images")
public class CmProjectPlanImagesController extends BaseController{

	@Autowired
	private ICmProjectPlanImagesService cmProjectPlanImagesService;
	@Autowired
	private IAttachService attachService;

	public CmProjectPlanImagesController() {
	}

	@RequestMapping(value="imageList.action",method= RequestMethod.GET)
	@ResponseBody
	public List<CmProjectPlanImages> imageList(String projectId){
		CmProjectPlanImages entity = new CmProjectPlanImages();
		entity.setProjectId(projectId);
		return this.cmProjectPlanImagesService.getByProjectId(projectId);
	}

	@RequestMapping(value="images.action",method= RequestMethod.GET)
	@ResponseBody
	public List<Attach> images(String id) throws Exception{
		Attach attach = new Attach();
		attach.setBusinessIdArray(new String[]{id});
		attach.setBusinessTable("cm_project_plan_images");
		return this.attachService.queryAttachByBusinessIds(attach);
	}
}
