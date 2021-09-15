package com.jc.common.db.dialect.mongo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.bson.Document;

import com.jc.busi.standard.util.UserContext;
import com.jc.common.check.CheckRule;
import com.jc.common.check.vo.RuleVO;
import com.jc.common.db.dialect.IDBDialect;
import com.jc.common.db.dialect.mysql.MysqlColumnType;
import com.jc.common.kit.vo.PageManagerEx;
import com.jc.common.log.Uog;
import com.jc.dlh.constanst.DlhConst;
import com.jc.dlh.domain.DlhDatamodel;
import com.jc.dlh.domain.DlhDataobject;
import com.jc.dlh.domain.DlhDataobjectField;
import com.jc.dlh.domain.DlhUser;
import com.jc.dlh.service.IDlhDataobjectFieldService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class MongoDialect implements IDBDialect {

	// 日志
	private Uog log = Uog.getInstanceOnOper();

	@Override
	public void publish(DlhDatamodel head) throws CustomException {
		throw new CustomException("该模型不需要发布");
	}

	@Override
	public boolean addData(DlhDataobject object, List<Map<String, Object>> dataList) throws CustomException {
		return false;
	}

	@Override
	public boolean updateData(DlhDataobject object, Map<String, Object> dataMap, String column, String uuid) throws CustomException {
		return false;
	}

	@Override
	public List<Map<String, String>> queryDataByPrimaryKey(DlhDataobject object, String uuid) throws CustomException {
		return null;
	}


	@Override
	public void modify(DlhDataobject object, List<Map<String, Object>> dataList) throws CustomException {
		if (dataList == null || dataList.size() == 0) {
			log.info("**插入（" + object.getTableCode() + "）完成，更新条数：0");
		}
		DlhDataobjectField fieldCond = new DlhDataobjectField();
		fieldCond.setObjectId(object.getId());
		IDlhDataobjectFieldService dlhDataobjectFieldService = SpringContextHolder.getBean(IDlhDataobjectFieldService.class);
		List<DlhDataobjectField> fieldList = dlhDataobjectFieldService.queryAll(fieldCond);
		if (fieldList == null || fieldList.size() == 0) {
			throw new CustomException("没有定义交换对象属性");
		}

		Map<String, DlhDataobjectField> primaryKey = new HashMap<String, DlhDataobjectField>();
		Map<String, List<RuleVO>> fieldRuleMap = new HashMap<String, List<RuleVO>>();
		for (DlhDataobjectField item : fieldList) {
			if (item.getFieldCheck() != null && item.getFieldCheck().length() > 5) {
				fieldRuleMap.put(item.getFieldCode(), JsonUtil.json2Array(item.getFieldCheck(), RuleVO.class));
			}
			MongoColumnType ctype = MongoColumnType.getType(item.getItemType());
			if (ctype == null) {
				throw new CustomException("交换对象属性类型没有支持：" + item.getItemType());
			}
			if ("Y".equalsIgnoreCase(item.getItemKey())) {
				primaryKey.put(item.getId(), item);
			}
		}
		// 数据检查
		int row = 0;
		List<RuleVO> rules;
		for (Map<String, Object> line : dataList) {
			row++;
			for (Map.Entry<String, Object> item : line.entrySet()) {
				rules = fieldRuleMap.get(item.getKey());
				if (rules == null || rules.size() == 0) {
					continue;
				}
				for (RuleVO rule : rules) {
					CheckRule crule = CheckRule.getRule(rule.getCode());
					if (crule == null) {
						continue;
					}
					String msg = crule.getRule().check(rule.getValue(), item.getKey(), item.getValue());
					if (msg != null && msg.trim().length() > 0) {
						throw new CustomException("第" + row + "行出现错误：" + msg);
					}
				}
			}
		}

		MongoDao dao = null;
		try {
			// 创建连接
			dao = MongoDao.getInstance(object.getDbSource());
			// 当前用户
			DlhUser user = UserContext.getUser();
			String userName;
			if (user == null) {
				userName = "";
			} else {
				userName = user.getDlhUsername();
			}
			// 当前时间
			String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			MongoCollection<Document> dataDocument = dao.getMongoCollection(object.getTableCode());
			// 判断是修改还是删除
			for (Map<String, Object> line : dataList) {
				BasicDBObject delFilter = new BasicDBObject();
				// 删除旧数据
				for (DlhDataobjectField key : primaryKey.values()) {
					Object value = line.get(key.getFieldCode());
					if (value == null || value.toString().trim().length() <= 0) {
						value = "";
					}
					delFilter.put(key.getItemName(), value);
				}
				// 删除旧数据
				dataDocument.deleteMany(delFilter);
				// 插入新数据
				Document document2 = new Document();
				for (DlhDataobjectField item : fieldList) {
					if (!item.getFieldCode().equals(item.getItemName())) {
						line.put(item.getItemName(), line.get(item.getFieldCode()));
					}
				}
				line.put(DlhConst.DEFAULT_ID, generatorBusiId());
				line.put(DlhConst.DEFAULT_KEY, object.getObjUrl());
				line.put(DlhConst.DEFAULT_USER, userName);
				line.put(DlhConst.DEFAULT_DATE, now);
				document2.putAll(line);
				dataDocument.insertOne(document2);
			}
			log.info("**插入（" + object.getTableCode() + "）完成，更新条数：" + dataList.size());
		} catch (Exception ex) {
			log.error("发送异常，异常信息：" + ex.getMessage(), ex);
		} finally {
			if (dao != null) {
				dao.close();
			}
		}
	}

	@Override
	public boolean deleteData(DlhDataobject object, List<Map> condMapList) throws CustomException {
		return false;
	}

	@Override
	public List<Map<String, Object>> queryAll(DlhDataobject object, String condJson) throws CustomException {
		return null;
	}

	/**
	 * @Document 业务主键
	 * @return
	 */
	private String generatorBusiId() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	@Override
	public PageManagerEx<Map<String, Object>> query(DlhDataobject condObj, PageManager page, String condJson) throws CustomException {
		MongoDao dao = null;
		try {
			// 暂时不支持条件

			// 创建连接
			dao = MongoDao.getInstance(condObj.getDbSource());
			MongoCollection<Document> coll = dao.getMongoCollection(condObj.getTableCode());

			Integer rowsCount = Long.valueOf(coll.countDocuments()).intValue();
			PageManagerEx<Map<String, Object>> page_ = new PageManagerEx<Map<String, Object>>();
			page_.setTotalCount(rowsCount);
			if (page.getPageRows() == -1) {
				page.setPageRows(Integer.MAX_VALUE);
			}
			// 计算页数 page.getRows()获得每页显示条数，系统中固定值
			int pageCount = rowsCount / page.getPageRows();
			// 分页数据查询开始锚点值
			int pageStart = pageCount;
			if (rowsCount % page.getPageRows() > 0) {
				pageCount++;
			}
			// 如果传过来的当前页码大于总页码 则把当前页码设置为最大页码
			if (page.getPage() > pageCount) {
				page.setPage(pageCount);
			} else if (page.getPage() == pageCount && pageCount != 0) {
				page.setPage(pageCount - 1);
				pageStart = page.getPage();
			} else {
				pageStart = page.getPage();
			}

			// 将页面传过来的当前页传回到前台
			if (rowsCount % page.getPageRows() == 0 && page.getPage() == pageCount) {
				page_.setPage(page.getPage());
			} else {
				page_.setPage(page.getPage() + 1);
			}

			FindIterable<Document> dataSet = coll.find().skip((pageStart) * page.getPageRows()).limit(page.getPageRows());
			List<Map<String, Object>> dataList = new ArrayList<>();
			dataSet.forEach(new Consumer<Document>() {
				@Override
				public void accept(Document doc) {
					Map<String, Object> line = new HashMap<String, Object>();
					doc.forEach((key, value) -> {
						line.put(key, value);
					});
					dataList.add(line);
				}

			});

			if (dataList.size() > 0) {
				DlhDataobjectField condField = new DlhDataobjectField();
				condField.setObjectId(condObj.getId());
				IDlhDataobjectFieldService dlhDataobjectFieldService = SpringContextHolder.getBean(IDlhDataobjectFieldService.class);
				List<DlhDataobjectField> fieldList = dlhDataobjectFieldService.queryAll(condField);
				Map<String, DlhDataobjectField> fieldMap = new HashMap<String, DlhDataobjectField>();
				if (fieldList != null) {
					for (DlhDataobjectField f : fieldList) {
						fieldMap.put(f.getItemName(), f);
					}
				}
				// 转换显示的值
				List<Map<String, Object>> newList = new ArrayList<>();
				Map<String, Object> newLine;
				MongoColumnType ctype;
				DlhDataobjectField temp;
				for (Map<String, Object> line : dataList) {
					newLine = new HashMap<>();
					for (Map.Entry<String, Object> item : line.entrySet()) {
						temp = fieldMap.get(item.getKey());
						if (temp != null) {
							ctype = MongoColumnType.getType(temp.getItemType());
							if (ctype != null) {
								newLine.put(item.getKey(), ctype.getService().getDisplayValue(item.getValue()));
							} else {
								newLine.put(item.getKey(), item.getValue());
							}
						} else {
							newLine.put(item.getKey(), item.getValue());
						}
					}
					newList.add(newLine);
				}
				page_.setData(newList);
			} else {
				page_.setData(dataList);
			}
			page_.setTotalPages(pageCount);
			page_.setsEcho(page.getsEcho());
			return page_;
		} finally {
			if (dao != null) {
				dao.close();
			}
		}

	}

	@Override
	public List<Map<String, String>> queryById(DlhDataobject object, String uuid, List<DlhDataobjectField> fieldList,String yewuKey) throws CustomException {
		MongoDao dao = null;
		try {
			// 创建连接
			dao = MongoDao.getInstance(object.getDbSource());
			MongoCollection<Document> coll = dao.getMongoCollection(object.getTableCode());

			BasicDBObject idFilter = new BasicDBObject();
			idFilter.put(DlhConst.DEFAULT_ID, uuid);

			FindIterable<Document> dataSet = coll.find(idFilter);
			List<Map<String, Object>> dataList = new ArrayList<>();
			dataSet.forEach(new Consumer<Document>() {

				@Override
				public void accept(Document doc) {
					Map<String, Object> line = new HashMap<String, Object>();
					doc.forEach((key, value) -> {
						line.put(key, value);
					});
					dataList.add(line);
				}

			});

			List<Map<String, String>> resList = new ArrayList<Map<String, String>>();
			if (dataList == null || dataList.size() == 0) {
				return resList;
			}
			Map<String, Object> data = new HashMap<>();
			for (Map.Entry<String, Object> entry : dataList.get(0).entrySet()) {
				data.put(entry.getKey().toUpperCase(), entry.getValue());
			}
			Map<String, String> newLine;
			for (DlhDataobjectField field : fieldList) {
				String title = field.getFieldName();
				if (title == null || title.trim().length() <= 0) {
					title = field.getFieldCode();
				}
				newLine = new HashMap<>();
				newLine.put("title", title);
				Object value = data.get(field.getItemName().toUpperCase());
				MysqlColumnType ctype = MysqlColumnType.getType(field.getItemType());
				newLine.put("value", ctype.getService().getDisplayValue(value));
				resList.add(newLine);

			}
			return resList;
		} finally {
			if (dao != null) {
				dao.close();
			}
		}
	}

}
