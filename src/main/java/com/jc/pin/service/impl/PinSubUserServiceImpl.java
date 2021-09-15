package com.jc.pin.service.impl;

import com.jc.foundation.cache.CacheClient;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.MessageUtils;
import com.jc.pin.dao.IPinSubUserDao;
import com.jc.pin.domain.PinReSubUser;
import com.jc.pin.domain.PinSubUser;
import com.jc.pin.service.IPinSubUserService;
import com.jc.sys.domain.SubUser;
import com.jc.sys.service.ISubUserService;
import com.jc.system.sys.util.Pinyin4jUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 用户拼音处理接口实现类
 * @author Administrator
 * @date 2020-06-30
 */
@Service
public class PinSubUserServiceImpl extends BaseServiceImpl<PinSubUser> implements IPinSubUserService{

    private static final String CACHE_USER_INFO_PINS = "cache_user_info_pins";

	@Autowired
	private ISubUserService userService;
	
	private IPinSubUserDao pinSubUserDao;
	
	public PinSubUserServiceImpl(){}
	
	@Autowired
	public PinSubUserServiceImpl(IPinSubUserDao pinSubUserDao){
		super(pinSubUserDao);
		this.pinSubUserDao = pinSubUserDao;
	}

	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	@Override
	public Integer deleteByIds(PinSubUser pinSubUser) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(pinSubUser,true);
			result = pinSubUserDao.delete(pinSubUser);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(pinSubUser);
			throw ce;
		}
		return result;
	}

	@Override
	public List<PinReSubUser> queryPinUser(PinSubUser pinUser) throws CustomException {
		return pinSubUserDao.queryPinUser(pinUser);
	}

	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	@Override
	public Map<String, Object> infoLoading() throws CustomException {
		Map<String, Object> resultMap = new HashMap<>();
		List<SubUser> allUserList = userService.queryAll(new SubUser());
		if (allUserList != null && allUserList.size() > 0){
			List<PinSubUser> pinUserList = new ArrayList<>();
			for(SubUser user:allUserList){
				PinSubUser pinUser = new PinSubUser();
				String username = user.getDisplayName();
				if(username != null && !"".equals(username.trim()))
				{
					StringBuffer sb= new StringBuffer();
					char [] aa = username.toCharArray();
					for (int i = 0; i < aa.length; i++) {
						char c = aa[i];
						if (Pattern.matches("[\u4e00-\u9fa5]", String.valueOf(c))) {
							sb.append(c);
						}
					}
					String abbinit = user.getDisplayName().substring(0,1);
					pinUser.setUserId(user.getId());
					pinUser.setUserName(user.getDisplayName());
					pinUser.setDeptId(user.getDeptId());
					pinUser.setExtStr1("0");
					if (Pattern.matches("[a-zA-Z]+", String.valueOf(abbinit))){
						pinUser.setUserAbbreviate(abbinit);
						pinUser.setUserInitials(abbinit);
						pinUser.setUserFull(user.getDisplayName());
					} else if(Pattern.matches("[\u4e00-\u9fa5]", String.valueOf(abbinit))){
						username = sb.toString();
						if (username != null && !"".equals(username)){
							String abbreviate = Pinyin4jUtil.converterToFirstSpell(username);
							String userFull = Pinyin4jUtil.converterToSpell(username);
							pinUser.setUserAbbreviate(abbreviate);
							pinUser.setUserInitials(abbreviate.substring(0,1));
							pinUser.setUserFull(userFull);
						}
					} else {
						pinUser.setUserAbbreviate("#");
						pinUser.setUserInitials("#");
						pinUser.setUserFull(user.getDisplayName());
					}
					propertyService.fillProperties(pinUser,false);
				}
				pinUserList.add(pinUser);
			}
			List<PinSubUser> delPinUsers = pinSubUserDao.queryAll(new PinSubUser());
			if(delPinUsers != null && delPinUsers.size() > 0){
				String delIds = "";
				for(PinSubUser delUser:delPinUsers){
					if ("".equals(delIds)){
						delIds = String.valueOf(delUser.getId());
					}else {
						delIds = delIds + "," + String.valueOf(delUser.getId());
					}
				}
				PinSubUser delTemps = new PinSubUser();
				delTemps.setPrimaryKeys(delIds.split(","));
				pinSubUserDao.delete(delTemps,false);
			}

			pinSubUserDao.save(pinUserList);
			List<PinReSubUser> pinReUserList = queryPinUser(new PinSubUser());
			CacheClient.removeCache(CACHE_USER_INFO_PINS);
			CacheClient.putCache(CACHE_USER_INFO_PINS, JsonUtil.java2Json(pinReUserList));
			resultMap.put("success", "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
		} else {

			List<PinSubUser> delPinUsers = pinSubUserDao.queryAll(new PinSubUser());
			if(delPinUsers != null && delPinUsers.size() > 0){
				String delIds = "";
				for(PinSubUser delUser:delPinUsers){
					if ("".equals(delIds)){
						delIds = String.valueOf(delUser.getId());
					}else {
						delIds = delIds + "," + String.valueOf(delUser.getId());
					}
				}
				PinSubUser delTemps = new PinSubUser();
				delTemps.setPrimaryKeys(delIds.split(","));
				pinSubUserDao.delete(delTemps,false);
			}
			CacheClient.removeCache(CACHE_USER_INFO_PINS);

			resultMap.put("success", "false");
		}
		return resultMap;
	}

	@Override
	public List<Object> queryPinUsers() throws CustomException {
		List<Object> objects = new ArrayList<>();
		List<PinSubUser> pinUsers = pinSubUserDao.queryAll(new PinSubUser());
		if(pinUsers != null && pinUsers.size() > 0){
			for(PinSubUser delUser:pinUsers){
				objects.add(delUser.getUserId());
			}
		}
		return objects;
	}

}