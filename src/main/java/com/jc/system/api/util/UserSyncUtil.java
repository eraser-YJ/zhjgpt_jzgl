package com.jc.system.api.util;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.*;
import com.jc.system.api.domain.UserSynclog;
import com.jc.system.api.service.IUserSynclogService;
import com.jc.system.security.domain.Subsystem;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.ISubsystemService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.io.BufferedReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class UserSyncUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserSyncUtil.class);
	
	final public static int ERROR_NUMBER = 3;
	
	final public static long SLEEP_TIME = 3000;
	
	static SimpleAsyncTaskExecutor asyncTaskExecutor = null;
	
	public static void push(User user){
		if(!"true".equals(GlobalContext.getProperty("cas.start"))){
			return;
		}
		ISubsystemService subsystemService = SpringContextHolder.getBean(ISubsystemService.class);
		Subsystem subsystem = new Subsystem();
		subsystem.setDeleteFlag(0);
		List<Subsystem> list;
		try {
			list = subsystemService.queryAll(subsystem);
			if(list == null || list.size() <1) {
                return;
            }
		} catch (CustomException e) {
			LOGGER.debug("query subsystem error!", e.getMessage());
			return;
		}
		if(asyncTaskExecutor == null) {
            asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        }
		for(Subsystem tempSubsystem : list){
			if(StringUtil.isEmpty(tempSubsystem.getUserSyncUrl())) {
                continue;
            }
			user.setOrgId(user.getOrgId());
			asyncTaskExecutor.submit(new MessageSender(tempSubsystem.getUrl()+tempSubsystem.getUserSyncUrl(),user,tempSubsystem.getPermission()));
		}
	}
	
	public static void main(String[] args) {
		User user = new User();
		UserSyncUtil.push(user);
	}
	
	private static final class MessageSender implements Callable<Boolean> {

		private String subsystem;
		
        private String url;

        private String message;

        private int number = 1;
        
        private User user;

        public MessageSender(final String url, final User user,String subsystem) {
            this.url = url;
            this.user = user;
            this.subsystem = subsystem;
        }

        @Override
        public Boolean call() throws Exception {
            BufferedReader in = null;
            this.message = JsonUtil.java2Json(user);
            try {
                LOGGER.debug("Attempting to access {}", url);
                Map<String, String> map = (Map) JsonUtil.json2Java(this.message,Map.class);
                JsonResult jsonResult = (JsonResult) JsonUtil.json2Java(HttpClientUtil.post(url, null, map),JsonResult.class);

                int status = jsonResult.getStatus();
                if(status != 0){
                	UserSyncUtil.userSyncLog(subsystem, user, jsonResult);
                }
                LOGGER.debug("Finished sending message to {}", url);
                return true;
            }catch (final Exception e) {
                LOGGER.warn("Error Sending message to url endpoint [{}]. Error is [{}]", url, e.getMessage());
                if(this.number < UserSyncUtil.ERROR_NUMBER ){
                	this.number = this.number + 1;
                	Thread.sleep(UserSyncUtil.SLEEP_TIME);
                	return this.call();
                }else{
                	UserSyncUtil.userSyncLog(subsystem, user, null);
                }
                return false;
            } finally {
                IOUtils.closeQuietly(in);
            }
        }

    }
	
	public static void userSyncLog(String subsystem, User user, JsonResult jsonResult){
		IUserSynclogService userSynclogService = SpringContextHolder.getBean(IUserSynclogService.class);
		if(jsonResult == null){
			jsonResult = new JsonResult();
			jsonResult.setStatus(9);
			jsonResult.setMsg("同步接口访问异常");
		}
		try {
			UserSynclog userSync = new UserSynclog();
			userSync.setUserId(user.getId());
			userSync.setSubsystem(subsystem);
			try{
				UserSynclog tempUserSync = userSynclogService.get(userSync);
				if(tempUserSync != null){
					userSync.setSyncTime(String.valueOf(Integer.parseInt(tempUserSync.getSyncTime())+1));
				}else{
					userSync.setSyncTime("1");
				}
			}catch(Exception e){
				userSync.setSyncTime("1");
				LOGGER.warn("Error getOne message in database", e.getMessage());
			}
			userSync.primaryKeys = new String[]{String.valueOf(user.getId())};
			userSynclogService.delete(userSync);
			userSync.setLoginName(user.getLoginName());
			userSync.setDisplayName(user.getDisplayName());
			userSync.setStatus(""+jsonResult.getStatus());
			userSync.setMsg(jsonResult.getMsg());
			userSync.setDeleteFlag(0);
		
			userSynclogService.save(userSync);
		} catch (CustomException e) {
			LOGGER.warn("Error save message in database", e.getMessage());
		}
	}
	
}
