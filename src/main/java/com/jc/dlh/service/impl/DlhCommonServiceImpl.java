package com.jc.dlh.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.jc.common.kit.RedisDistributedLock;
import com.jc.common.kit.vo.DicVO;
import com.jc.dlh.cache.DlhCacheConst;
import com.jc.dlh.service.IDlhCommonService;
import com.jc.foundation.util.JsonUtilOld;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.dic.domain.Dic;
import com.jc.system.dic.service.IDicService;

/**
 * @title 统一数据资源中心
 * @description 业务服务类
 * @author lc
 * @version 2020-03-13
 */
@Service
public class DlhCommonServiceImpl implements IDlhCommonService {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	// 数据中心
	private JdbcTemplate jdbcServiceTemplate;
	// dic个数
	private Long nowCount = null;
	// dic最大时间
	private Long nowMaxTime = null;

	private ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

	@PostConstruct
	public void initQuartz() {
		// 监控作业
		service.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				process();
			}
		}, 60 * 5, 30, TimeUnit.SECONDS);
	}

	private void process() {
		// 初始化
		if (jdbcServiceTemplate == null) {
			jdbcServiceTemplate = new JdbcTemplate((DataSource) SpringContextHolder.getBean("dataSourceServer"));
		}
		if (jdbcServiceTemplate == null) {
			System.out.print("取得数据中心数据配置失败");
			System.out.print("取得数据中心数据配置失败");
			System.out.print("取得数据中心数据配置失败");
			System.out.print("取得数据中心数据配置失败");
			return;
		}
		RedisDistributedLock lock = new RedisDistributedLock(redisTemplate, "rule_dic_check");
		if (lock.lock(1000 * 60 * 10)) {
			try {
				String sql = "SELECT COUNT(*) count, UNIX_TIMESTAMP(MAX(t.MODIFY_DATE)) maxTime FROM tty_sys_dics t";
				Map<String, Object> objMap = jdbcServiceTemplate.queryForMap(sql);
				if (objMap.size() > 0) {
					long count = Long.valueOf(objMap.get("count").toString());
					long maxTime = Long.valueOf(objMap.get("maxTime").toString());
					if (nowCount == null) {
						nowCount = count;
						nowMaxTime = maxTime;
						// 第一次清理，重启服务每次拿到最新的
						redisTemplate.delete(DlhCacheConst.REDIS_DIC_KEY);
					} else {
						// 变化后清理
						if (nowCount != count || nowMaxTime != maxTime) {
							// 删除所有缓存数据
							redisTemplate.delete(DlhCacheConst.REDIS_DIC_KEY);
							nowCount = count;
							nowMaxTime = maxTime;
						}
					}
				}
			} finally {
				lock.releaseLock();
			}
		}
	}

	/**
	 * @description 字典
	 * @return
	 * @throws Exception
	 */
	public List<DicVO> getDic(String typeCode) throws Exception {
		if (typeCode == null || typeCode.trim().length() <= 0) {
			return new ArrayList<>();
		}
		Object jsonStr = redisTemplate.opsForHash().get(DlhCacheConst.REDIS_DIC_KEY, typeCode);
		if (jsonStr == null || jsonStr.toString().length() <= 0) {
			IDicService service = SpringContextHolder.getBean(IDicService.class);
			Dic cond = new Dic();
			cond.setParentId(typeCode);
			List<Dic> dicList = service.query(cond);
			if (dicList == null) {
				dicList = new ArrayList<>();
			}
			List<DicVO> dataList = new ArrayList<DicVO>();
			for (Dic dic : dicList) {
				DicVO nvo = new DicVO();
				nvo.setCode(dic.getCode());
				nvo.setName(dic.getName());
				nvo.setParentId(dic.getParentId());
				dataList.add(nvo);
			}
			redisTemplate.opsForHash().put(DlhCacheConst.REDIS_DIC_KEY, typeCode, JsonUtilOld.java2Json(dataList));
			return dataList;
		} else {
			List<DicVO> dataList = JsonUtilOld.json2Array(jsonStr.toString(), DicVO.class);
			return dataList;
		}

	}

	@Override
	public RedisTemplate<Object, Object> getRedis() {
		return redisTemplate;
	}
}