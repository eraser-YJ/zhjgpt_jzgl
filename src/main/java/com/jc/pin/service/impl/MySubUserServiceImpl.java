package com.jc.pin.service.impl;

import com.jc.foundation.cache.CacheClient;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.JsonUtil;
import com.jc.pin.domain.PinReSubUser;
import com.jc.pin.domain.PinSubUser;
import com.jc.pin.service.IPinSubUserService;
import com.jc.sys.domain.SubUser;
import com.jc.sys.service.ISubUserService;
import com.jc.system.security.domain.User;
import com.jc.system.sys.domain.PinReUser;
import com.jc.system.sys.domain.PinUser;
import com.jc.system.sys.util.Pinyin4jUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Administrator
 * @date 2020-06-30
 */
@Service
public class MySubUserServiceImpl {

    private static Logger logger = Logger.getLogger(MySubUserServiceImpl.class);

    private static final String CACHE_SUB_USER_INFO_PINS = "cache_sub_user_info_pins";

    @Autowired
    private ISubUserService subUserService;
    @Autowired
    private IPinSubUserService pinUserService;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void afterMethod(Object[] o, Method method){
        String pathstr = method.toGenericString();
        if (pathstr.indexOf("SubUserController.update") != -1) {
            try {
                Map<String,Object> userMap= (Map<String,Object>)JsonUtil.json2Java(JsonUtil.java2Json(o[0]),Map.class);
                SubUser subUser = new SubUser();
                subUser.setId(userMap.get("id").toString());
                subUser.setDeptId(userMap.get("deptId").toString());
                SubUser user = subUserService.get(subUser);
                PinSubUser filterPinUser = filterPinUser(user);

                PinSubUser pinUser = new PinSubUser();
                pinUser.setUserId(user.getId());
                pinUser = pinUserService.get(pinUser);
                if (pinUser == null) {
                    pinUser = filterPinUser(user);
                    pinUserService.save(pinUser);
                    putCacheClient(pinUser,1);
                } else {
                    pinUser.setUserName(user.getDisplayName());
                    pinUser.setUserAbbreviate(filterPinUser.getUserAbbreviate());
                    pinUser.setUserInitials(filterPinUser.getUserInitials());
                    pinUser.setUserFull(filterPinUser.getUserFull());
                    pinUser.setDutyId(user.getDutyId());
                    pinUser.setDeptId(user.getDeptId());
                    pinUser.setOrderNo(user.getOrderNo());
                    pinUser.setText(user.getDisplayName());
                    pinUser.setExtStr1("0");
                    pinUserService.update(pinUser);
                    putCacheClient(pinUser,2);
                }
            } catch (CustomException e) {
                logger.error(e.getMessage());
            }
        } else if(pathstr.indexOf("SubUserController.save") != -1){
            try {
                Map<String,Object> userMap= (Map<String,Object>)JsonUtil.json2Java(JsonUtil.java2Json(o[0]),Map.class);
                SubUser subUser = new SubUser();
                subUser.setCode(userMap.get("code").toString());
                subUser.setDeptId(userMap.get("deptId").toString());
                SubUser user = subUserService.get(subUser);
                if(user != null) {
                    PinSubUser pinUserVal = new PinSubUser();
                    pinUserVal.setUserId(user.getId());
                    pinUserVal.setDeptId(user.getDeptId());
                    pinUserVal = pinUserService.get(pinUserVal);
                    if(pinUserVal == null) {
                        PinSubUser pinUser = filterPinUser(user);
                        pinUserService.save(pinUser);
                        putCacheClient(pinUser,1);
                    }
                }
            } catch (CustomException e) {
                logger.error(e.getMessage());
            }
        } else if(pathstr.indexOf("SubUserController.deleteByIds") != -1){
            String ids = o[1].toString();
            PinSubUser pinUser = new PinSubUser();
            pinUser.setPrimaryKeys(ids.split(","));
            try {
                pinUserService.delete(pinUser);
                putCacheClient(pinUser,3);
            } catch (CustomException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private PinSubUser filterPinUser(SubUser user){
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
            username = sb.toString();
            if (username != null && !"".equals(username)){
                String abbreviate = Pinyin4jUtil.converterToFirstSpell(username);
                String userFull = Pinyin4jUtil.converterToSpell(username);
                pinUser.setUserId(user.getId());
                pinUser.setUserName(user.getDisplayName());
                pinUser.setUserAbbreviate(abbreviate);
                pinUser.setUserInitials(abbreviate.substring(0,1));
                pinUser.setUserFull(userFull);
                pinUser.setDutyId(user.getDutyId());
                pinUser.setDeptId(user.getDeptId());
                pinUser.setOrderNo(user.getOrderNo());
                pinUser.setText(user.getDisplayName());
                pinUser.setExtStr1("0");
            } else {
                String abbinit = user.getDisplayName().substring(0,1);
                if (Pattern.matches("[a-zA-Z]+", String.valueOf(abbinit))){
                    pinUser.setUserAbbreviate(abbinit);
                    pinUser.setUserInitials(abbinit);
                    pinUser.setUserFull(user.getDisplayName());
                } else if(Pattern.matches("[\u4e00-\u9fa5]", String.valueOf(abbinit))){
                    username = sb.toString();
                    if (username != null && !"".equals(username)){
                        String abbreviate = Pinyin4jUtil.converterToFirstSpell(username);
                        String userFull = Pinyin4jUtil.converterToSpell(username);
                        pinUser.setUserFull(userFull);
                        pinUser.setUserAbbreviate(abbreviate);
                        pinUser.setUserInitials(abbreviate.substring(0,1));
                    }
                } else {
                    pinUser.setUserInitials("#");
                    pinUser.setUserAbbreviate("#");
                    pinUser.setUserFull(user.getDisplayName());
                }
                pinUser.setUserId(user.getId());
                pinUser.setUserName(user.getDisplayName());
                pinUser.setDutyId(user.getDutyId());
                pinUser.setDeptId(user.getDeptId());
                pinUser.setOrderNo(user.getOrderNo());
                pinUser.setText(user.getDisplayName());
                pinUser.setExtStr1("0");
            }
        }

        return pinUser;
    }

    private void putCacheClient(PinSubUser pinUser,int operType) throws CustomException {
        String cacheStr = CacheClient.getCache(CACHE_SUB_USER_INFO_PINS);
        List<PinReSubUser> pinReUserList = null;
        if(cacheStr != null &&  !"".equals(cacheStr)
                && cacheStr.length() > 0){
            pinReUserList= JsonUtil.json2Array(cacheStr,PinReSubUser.class);
        } else {
            pinReUserList = new ArrayList<>();
        }

        PinReSubUser pinReUser = new PinReSubUser();
        if (operType == 1){
            fullPinReUser(pinUser,pinReUser);
            pinReUserList.add(pinReUser);
        }else if(operType == 2){
            for (PinReSubUser reUser : pinReUserList){
                if (reUser.getUserId().equals(pinUser.getUserId())){
                    pinReUserList.remove(reUser);
                    break;
                }
            }
            fullPinReUser(pinUser,pinReUser);
            pinReUserList.add(pinReUser);
        }else if(operType == 3){
            String[] userIds = pinUser.getPrimaryKeys();
            for(int x=0; x<userIds.length; x++) {
                for (PinReSubUser reUser : pinReUserList) {
                    if (userIds[x].equals(reUser.getUserId())) {
                        pinReUserList.remove(reUser);
                        break;
                    }
                }
            }
        }else if(operType == 4){
            fullPinReUser(pinUser,pinReUser);
            pinReUserList.add(pinReUser);
        }
        CacheClient.removeCache(CACHE_SUB_USER_INFO_PINS);
        CacheClient.putCache(CACHE_SUB_USER_INFO_PINS, JsonUtil.java2Json(pinReUserList));
    }

    private void fullPinReUser(PinSubUser pinUser,PinReSubUser pinReUser){
        pinReUser.setId(pinUser.getUserId());
        pinReUser.setUserId(pinUser.getUserId());
        pinReUser.setUserName(pinUser.getUserName());
        pinReUser.setText(pinUser.getText());
        pinReUser.setUserInitials(pinUser.getUserInitials());
        pinReUser.setUserAbbreviate(pinUser.getUserAbbreviate());
        pinReUser.setUserFull(pinUser.getUserFull());
        pinReUser.setDeptId(pinUser.getDeptId());
        pinReUser.setDutyId(pinUser.getDutyId());
        pinReUser.setOrderNo(pinUser.getOrderNo());
        pinReUser.setWeight(pinUser.getExtStr1());
    }
}
