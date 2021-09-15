package com.jc.system.common.web;

import java.util.ArrayList;
import java.util.List;

import com.jc.foundation.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jc.foundation.domain.BaseBean;

/**
 * @author Administrator
 * @date 2020-06-30
 */
@Controller
public class TreeController extends BaseController {

	public static class TreeBean{
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public long getParentId() {
			return parentId;
		}
		public void setParentId(long parentId) {
			this.parentId = parentId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		private long id ;
		private long parentId;
		private String name;
		private String title;
		 
	} 
	
	public TreeController() {
	}

	@RequestMapping(value = "/tree/getNodeList.action",method=RequestMethod.GET)
	@ResponseBody
	public List<TreeBean> getTreeNodeList(BaseBean bean){
		List<TreeBean> list = new ArrayList<>();
		TreeBean root = new TreeBean();
		root.setId(0L);
		root.setName("根");
		root.setParentId(-1L);
		list.add(root);

		long parentId = 0L;
		for (int i = 1; i < 5; i++) {
			TreeBean node = new TreeBean();
			long id = 100L*i+i;
			node.setId(id);
			node.setName("二层节点"+i);
			node.setParentId(parentId);
			list.add(node);
			for (int j = 1; j < 6; j++) {
				TreeBean temp = new TreeBean();
				temp.setId( Long.parseLong( id+""+1000*j+j));
				temp.setName("三层节点"+i+j+"");
				temp.setParentId(id);
				list.add(temp);
			}
		}
		return list;
	}
}
