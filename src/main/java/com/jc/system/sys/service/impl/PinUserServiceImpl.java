package com.jc.system.sys.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.jc.foundation.cache.CacheClient;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.MessageUtils;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.IUserService;
import com.jc.system.sys.domain.PinReUser;
import com.jc.system.sys.util.Pinyin4jUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jc.foundation.exception.CustomException;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.service.impl.BaseServiceImpl;

import com.jc.system.sys.dao.IPinUserDao;
import com.jc.system.sys.domain.PinUser;
import com.jc.system.sys.service.IPinUserService;

/***
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class PinUserServiceImpl extends BaseServiceImpl<PinUser> implements IPinUserService{

	@Autowired
	private IUserService userService;

	private IPinUserDao pinUserDao;

	public PinUserServiceImpl(){}

	@Autowired
	public PinUserServiceImpl(IPinUserDao pinUserDao){
		super(pinUserDao);
		this.pinUserDao = pinUserDao;
	}

	@Override
    @Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(PinUser pinUser) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(pinUser,true);
			result = pinUserDao.delete(pinUser);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(pinUser);
			throw ce;
		}
		return result;
	}

	@Override
	public List<PinReUser> queryPinUser(PinUser pinUser) throws CustomException {
		return pinUserDao.queryPinUser(pinUser);
	}

	@Override
	public void deleteBack(PinUser pinUser) {
		pinUserDao.deleteBack(pinUser);
	}

	@Override
    @Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Map<String, Object> infoLoading() throws CustomException {
		Map<String, Object> resultMap = new HashMap<>();
		List<User> allUserList = userService.queryAll(new User());
		if (allUserList != null && allUserList.size() > 0){
			List<PinUser> pinUserList = new ArrayList<>();
			for(User user:allUserList){
				PinUser pinUser = new PinUser();
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
					pinUser.setExtStr1(String.valueOf(user.getIsAdmin()));
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
			List<PinUser> delPinUsers = pinUserDao.queryAll(new PinUser());
			if(delPinUsers != null && delPinUsers.size() > 0){
				String delIds = "";
				for(PinUser delUser:delPinUsers){
					if ("".equals(delIds)){
						delIds = String.valueOf(delUser.getId());
					}else {
						delIds = delIds + "," + String.valueOf(delUser.getId());
					}
				}
				PinUser delTemps = new PinUser();
				delTemps.setPrimaryKeys(delIds.split(","));
				pinUserDao.delete(delTemps,false);
			}

			pinUserDao.save(pinUserList);
			List<PinReUser> pinReUserList = queryPinUser(new PinUser());
			CacheClient.removeCache("cache_user_info_pins");
			CacheClient.putCache("cache_user_info_pins", JsonUtil.java2Json(pinReUserList));
			resultMap.put("success", "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
		}
		return resultMap;
	}

	@Override
	public List<Object> queryPinUsers() throws CustomException {
		List<Object> objects = new ArrayList<>();
		List<PinUser> pinUsers = pinUserDao.queryAll(new PinUser());
		if(pinUsers != null && pinUsers.size() > 0){
			for(PinUser delUser:pinUsers){
				objects.add(delUser.getUserId());
			}
		}
		return objects;
	}

}