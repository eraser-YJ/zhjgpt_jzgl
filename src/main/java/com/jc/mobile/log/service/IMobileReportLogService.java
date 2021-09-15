package com.jc.mobile.log.service;

import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;
import com.jc.mobile.log.domain.MobileReportLog;
import com.jc.mobile.log.enums.MobileReportLogEnum;
import com.jc.system.security.domain.User;

import java.util.List;

/**
 * 建设管理-移动端上报日志service
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface IMobileReportLogService extends IBaseService<MobileReportLog>{

	/**
	 * 保存上报日志
	 * @param businessType: 业务类型
	 * @param entity：实体对象
	 * @param user: 上报人
	 * @return
	 */
	Result saveLog(MobileReportLogEnum businessType, Object entity, User user);
}
