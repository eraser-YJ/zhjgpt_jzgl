package com.jc.system.security.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.jc.system.security.utils.SettingInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.StringUtil;
import com.jc.system.security.dao.ISettingDao;
import com.jc.system.security.domain.Setting;
import com.jc.system.security.service.ISettingService;
import com.jc.system.util.SettingUtils;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class SettingServiceImpl extends BaseServiceImpl<Setting> implements ISettingService{

	private ISettingDao settingDao;
	
	public SettingServiceImpl(){}
	
	@Autowired
	public SettingServiceImpl(ISettingDao settingDao){
		super(settingDao);
		this.settingDao = settingDao;
	}

	/**
	 * 获取单条记录方法(系统参数配置为统一的一个)
	 * @param setting
	 * @return
	 */
	@Override
    public Map<String,String> getSettings(Setting setting) {
		Map<String,String> result = new HashMap<>();
		List<Setting> templist = settingDao.queryAll(setting);
		for(Setting item:templist){
			result.put(item.getSettingKey(), item.getSettingValue());
		}
		return result;
	}

	@Override
	public Integer update(Setting t) throws CustomException {
		Integer resutl = super.update(t);
		return resutl;
	}
	
	@Override
	public void updateByMap(Map<String, Object> map) {
		Set<Entry<String, Object>> entrySet = map.entrySet();
		for(Entry<String, Object> entry:entrySet){
			if(StringUtil.isEmpty(entry.getKey())){
				continue;
			}
			Setting setting = new Setting();
			setting.setSettingKey(entry.getKey());
			setting.setSettingValue(""+(entry.getValue()==null?"":entry.getValue()));
			setting.setModifyDate(new Date());
			try {
				update(setting);
			} catch (CustomException e) {
				log.error(e);
			}
		}
		SettingInit.refresh();
	}

}