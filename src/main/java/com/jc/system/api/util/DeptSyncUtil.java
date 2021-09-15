package com.jc.system.api.util;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.*;
import com.jc.system.api.domain.DeptSynclog;
import com.jc.system.api.service.IDeptSynclogService;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.Subsystem;
import com.jc.system.security.service.ISubsystemService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class DeptSyncUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DeptSyncUtil.class);
	
	public final static int ERROR_NUMBER = 3;
	
	public final static long SLEEP_TIME = 3000;
	
	static SimpleAsyncTaskExecutor asyncTaskExecutor = null;
	
	public static void push(Department dept){
		if(!"true".equals(GlobalContext.getProperty("cas.start"))){
			return;
		}
		ISubsystemService subsystemService = SpringContextHolder.getBean(ISubsystemService.class);
		Subsystem subsystem = new Subsystem();
		subsystem.setDeleteFlag(0);
		List<Subsystem> list = new ArrayList<>();
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
			asyncTaskExecutor.submit(new MessageSender(tempSubsystem.getUrl()+tempSubsystem.getUserSyncUrl().replace("user","dept"),dept,tempSubsystem.getPermission()));
		}
	}
	
	public static void main(String[] args) {
		Department dept = new Department();
		DeptSyncUtil.push(dept);
	}
	
	private static final class MessageSender implements Callable<Boolean> {

		private String subsystem;
		
        private String url;

        private String message;

        private int number = 1;
        
        private Department dept;

        public MessageSender(final String url, final Department dept,String subsystem) {
            this.url = url;
            this.dept = dept;
            this.subsystem = subsystem;
        }

        @Override
        public Boolean call() throws Exception {
            BufferedReader in = null;
            this.message = JsonUtil.java2Json(dept);
            try {
                LOGGER.debug("Attempting to access {}", url);
				Map<String, String> map = (Map) JsonUtil.json2Java(this.message,Map.class);
				JsonResult jsonResult = (JsonResult) JsonUtil.json2Java(HttpClientUtil.post(url, null, map),JsonResult.class);

                int status = jsonResult.getStatus();
                if(status != 0){
                	DeptSyncUtil.deptSyncLog(subsystem, dept, jsonResult);
                }
                LOGGER.debug("Finished sending message to {}", url);
                return true;
            }catch (final Exception e) {
                LOGGER.warn("Error Sending message to url endpoint [{}]. Error is [{}]", url, e.getMessage());
                if(this.number < DeptSyncUtil.ERROR_NUMBER ){
                	this.number = this.number + 1;
                	Thread.sleep(DeptSyncUtil.SLEEP_TIME);
                	return this.call();
                }else{
                	DeptSyncUtil.deptSyncLog(subsystem, dept, null);
                }
                return false;
            } finally {
                IOUtils.closeQuietly(in);
            }
        }

    }
	
	public static void deptSyncLog(String subsystem, Department dept, JsonResult jsonResult){
		IDeptSynclogService deptSynclogService = SpringContextHolder.getBean(IDeptSynclogService.class);
		if(jsonResult == null){
			jsonResult = new JsonResult();
			jsonResult.setStatus(9);
			jsonResult.setMsg("同步接口访问异常");
		}
		try {
			DeptSynclog deptSync = new DeptSynclog();
			deptSync.setDeptId(dept.getId());
			deptSync.setSubsystem(subsystem);
			try{
				DeptSynclog tempDeptSync = deptSynclogService.get(deptSync);
				if(tempDeptSync != null){
					deptSync.setSyncTime(String.valueOf(Integer.parseInt(tempDeptSync.getSyncTime())+1));
				}else{
					deptSync.setSyncTime("1");
				}
			}catch(Exception e){
				deptSync.setSyncTime("1");
				LOGGER.warn("Error getOne message in database", e.getMessage());
			}
			deptSync.primaryKeys = new String[]{String.valueOf(dept.getId())};
			deptSynclogService.delete(deptSync);
			deptSync.setDeptName(dept.getName());
			deptSync.setDeptType(String.valueOf(dept.getDeptType()));
			deptSync.setStatus(""+jsonResult.getStatus());
			deptSync.setMsg(jsonResult.getMsg());
			deptSync.setDeleteFlag(0);
		
			deptSynclogService.save(deptSync);
		} catch (CustomException e) {
			LOGGER.warn("Error save message in database", e.getMessage());
		}
	}
	
}
