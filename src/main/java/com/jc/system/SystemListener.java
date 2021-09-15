package com.jc.system;

import com.jc.foundation.web.BaseListener;
import com.jc.system.common.service.IRegService;
import com.jc.system.common.service.impl.RegServiceImpl;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.service.impl.DicManagerImpl;
import com.jc.system.security.service.IDepartmentService;
import com.jc.system.security.service.IRoleService;
import com.jc.system.security.service.ISubsystemService;
import com.jc.system.security.service.IUserService;
import com.jc.system.security.service.impl.DepartmentServiceImpl;
import com.jc.system.security.service.impl.RoleServiceImpl;
import com.jc.system.security.service.impl.SubsystemServiceImpl;
import com.jc.system.security.service.impl.UserServiceImpl;
import com.jc.system.security.utils.RSASetting;
import com.jc.system.security.utils.SettingInit;
import com.jc.system.sys.util.PinDeptAndUserInit;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class SystemListener extends BaseListener {

	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(this.getClass());

	@Override
	protected void initial(ServletContextEvent event) {
		super.initial(event);

		ServletContext context = event.getServletContext();
		// 可以在其中取得spring初始化好的bean
		WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(context);

		//初始化系统参数
		SettingInit.init();

		//初始化字典
		IDicManager manager = new DicManagerImpl();
		manager.init();

		//初始化子系统
		ISubsystemService subsystemService = new SubsystemServiceImpl();
		subsystemService.init();

		//缓存所有组织
		DepartmentServiceImpl deptService = (DepartmentServiceImpl) applicationContext.getBean(IDepartmentService.class);
		deptService.getAllDepts();

		//缓存所有用户
		UserServiceImpl userService = (UserServiceImpl) applicationContext.getBean(IUserService.class);
		userService.getAllUsers();

		//缓存所有角色
		RoleServiceImpl roleService = (RoleServiceImpl) applicationContext.getBean(IRoleService.class);
		roleService.getAllRoles();

		//初始化拼音树
		PinDeptAndUserInit.init();

		// 获取公钥和私钥sa
		RSASetting.init();

		//生成辅助安装程序
		IRegService regService = new RegServiceImpl();
		regService.zipSetupFile();
	}
}
