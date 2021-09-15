package com.jc.system.security.service;

import java.util.Map;

import com.jc.foundation.service.IBaseService;
import com.jc.system.security.domain.Setting;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface ISettingService extends IBaseService<Setting>{
	Map<String,String> getSettings(Setting setting);
	void updateByMap(Map<String,Object> map);
}