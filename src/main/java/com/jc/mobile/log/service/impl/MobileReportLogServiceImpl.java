package com.jc.mobile.log.service.impl;

import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.Result;
import com.jc.foundation.util.ResultCode;
import com.jc.mobile.log.dao.IMobileReportLogDao;
import com.jc.mobile.log.domain.MobileReportLog;
import com.jc.mobile.log.enums.MobileReportLogEnum;
import com.jc.mobile.log.service.IMobileReportLogService;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.DeptCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 建设管理-项目管理serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class MobileReportLogServiceImpl extends BaseServiceImpl<MobileReportLog> implements IMobileReportLogService {

	private IMobileReportLogDao mobileReportLogDao;

	public MobileReportLogServiceImpl(){}

	@Autowired
	public MobileReportLogServiceImpl(IMobileReportLogDao mobileReportLogDao){
		super(mobileReportLogDao);
		this.mobileReportLogDao = mobileReportLogDao;
	}

	@Override
	public Result saveLog(MobileReportLogEnum businessType, Object entity, User user) {
		if (businessType == null || entity == null || user == null) {
			return Result.failure(ResultCode.PARAM_IS_BLANK);
		}
		try {
			MobileReportLog db = new MobileReportLog();
			db.setBusinessType(businessType.toString());
			if (user != null) {
				db.setUserId(user.getId());
				Department department = DeptCacheUtil.getDeptById(user.getDeptId());
				if (department != null) {
					db.setDeptCode(department.getCode());
					db.setDeptId(department.getId());
				}
			}
			if (businessType.getEnumService() != null) {
				Map<String, String> entityMap =	businessType.getEnumService().getIdAndContent(entity);
				db.setBusinessId(entityMap.get("businessId"));
				db.setContent(entityMap.get("content"));
			}
			save(db);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return Result.success();
	}
}
