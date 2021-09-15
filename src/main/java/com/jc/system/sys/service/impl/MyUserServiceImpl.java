package com.jc.system.sys.service.impl;

import com.jc.foundation.cache.CacheClient;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.JsonUtil;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.ISystemService;
import com.jc.system.sys.domain.PinReUser;
import com.jc.system.sys.domain.PinUser;
import com.jc.system.sys.service.IPinUserService;
import com.jc.system.sys.util.Pinyin4jUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/***
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class MyUserServiceImpl {
    protected transient final Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private ISystemService systemService;
    @Autowired
    private IPinUserService pinUserService;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void afterMethod(Object[] o, Method method){
        String pathStr = method.toGenericString();
        if (pathStr.indexOf("UserController.update") != -1){
            try {
                Map<String,Object> userMap= (Map<String,Object>)JsonUtil.json2Java(JsonUtil.java2Json(o[0]),Map.class);
                User user = systemService.get(userMap.get("loginName").toString());
                PinUser filterPinUser = filterPinUser(user);

                PinUser pinUser = new PinUser();
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
                    pinUser.setExtStr1(user.getIsAdmin()+"");
                    pinUserService.update(pinUser);
                    putCacheClient(pinUser,2);
                }
            } catch (CustomException e) {
                logger.error(e.getMessage());
            }
        } else if(pathStr.indexOf("UserController.save") != -1){
            try {
                Map<String,Object> userMap= (Map<String,Object>)JsonUtil.json2Java(JsonUtil.java2Json(o[0]),Map.class);
                User user = systemService.get(userMap.get("loginName").toString());
                if (user != null){
                    PinUser pinUser = filterPinUser(user);
                    pinUserService.save(pinUser);

                    putCacheClient(pinUser,1);
                }
            } catch (CustomException e) {
                logger.error(e.getMessage());
            }
        } else if(pathStr.indexOf("UserController.deleteByIds") != -1){
            String ids = o[1].toString();
            PinUser pinUser = new PinUser();
            pinUser.setPrimaryKeys(ids.split(","));
            try {
                pinUserService.delete(pinUser);
                putCacheClient(pinUser,3);
            } catch (CustomException e) {
                logger.error(e.getMessage());
            }
        } else if(pathStr.indexOf("UserController.deleteBackByIds") != -1){
            try {
                String ids = o[1].toString();
                PinUser pinUser = new PinUser();
                pinUser.setPrimaryKeys(ids.split(","));
                pinUserService.deleteBack(pinUser);
                putCacheClient(pinUser,4);
            } catch (CustomException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private PinUser filterPinUser(User user){
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
                pinUser.setExtStr1(user.getIsAdmin()+"");
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
                pinUser.setExtStr1(user.getIsAdmin()+"");
            }
        }

        return pinUser;
    }

    private void putCacheClient(PinUser pinUser,int operType) throws CustomException {
        List<PinReUser> pinReUserList= JsonUtil.json2Array(CacheClient.getCache("cache_user_info_pins"),PinReUser.class);
        PinReUser pinReUser = new PinReUser();
        if (operType == 1) {
            //add
            fullPinReUser(pinUser,pinReUser);
            pinReUserList.add(pinReUser);
        } else if(operType == 2) {
            //update
            for (PinReUser reUser : pinReUserList){
                if (reUser.getUserId().equals(pinUser.getUserId())){
                    pinReUserList.remove(reUser);
                    fullPinReUser(pinUser,pinReUser);
                    pinReUserList.add(pinReUser);
                    break;
                }
            }
        } else if(operType == 3) {
            //del
            String[] userIds = pinUser.getPrimaryKeys();
            for (int x=0; x<userIds.length; x++) {
                for (PinReUser reUser : pinReUserList) {
                    if (userIds[x].equals(reUser.getUserId())) {
                        pinReUserList.remove(reUser);
                        break;
                    }
                }
            }
        } else if(operType == 4) {
            //delback
            fullPinReUser(pinUser,pinReUser);
            pinReUserList.add(pinReUser);
        }
        CacheClient.removeCache("cache_user_info_pins");
        CacheClient.putCache("cache_user_info_pins", JsonUtil.java2Json(pinReUserList));
    }

    private void fullPinReUser(PinUser pinUser,PinReUser pinReUser){
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
