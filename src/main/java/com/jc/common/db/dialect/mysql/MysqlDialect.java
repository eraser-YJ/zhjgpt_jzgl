package com.jc.common.db.dialect.mysql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.jc.busi.standard.util.UserContext;
import com.jc.common.check.CheckRule;
import com.jc.common.check.vo.RuleVO;
import com.jc.common.db.dialect.IDBDialect;
import com.jc.common.db.dialect.mysql.column.MysqlColumn;
import com.jc.common.kit.ListTool;
import com.jc.common.kit.StringUtil;
import com.jc.common.kit.vo.PageManagerEx;
import com.jc.common.log.Uog;
import com.jc.dlh.constanst.DlhConst;
import com.jc.dlh.domain.DlhDatamodel;
import com.jc.dlh.domain.DlhDatamodelItem;
import com.jc.dlh.domain.DlhDataobject;
import com.jc.dlh.domain.DlhDataobjectField;
import com.jc.dlh.domain.DlhUser;
import com.jc.dlh.service.IDlhDatamodelItemService;
import com.jc.dlh.service.IDlhDataobjectFieldService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.JsonUtilOld;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.dic.domain.Dic;
import com.jc.system.dic.service.IDicService;

public class MysqlDialect implements IDBDialect {
	// 日志
	private Uog log = Uog.getInstanceOnOper();

	@Override
	public void publish(DlhDatamodel head) throws CustomException {
		MysqlDao dao = null;
		try {
			DlhDatamodelItem itemCond = new DlhDatamodelItem();
			itemCond.setModelId(head.getId());
			itemCond.addOrderByField("t.item_seq");
			IDlhDatamodelItemService dlhDatamodelItemService = SpringContextHolder.getBean(IDlhDatamodelItemService.class);
			List<DlhDatamodelItem> itemList = dlhDatamodelItemService.queryAll(itemCond);
			if (itemList == null || itemList.size() == 0) {
				throw new CustomException("没有定义模型属性");
			}
			// 创建连接
			dao = MysqlDao.getInstance(head.getDbSource());
			// 是否有主键
			boolean hasPrimaryKey = false;
			List<String> pkList = new ArrayList<>();
			for (DlhDatamodelItem citem : itemList) {
				if ("Y".equalsIgnoreCase(citem.getItemKey())) {
					hasPrimaryKey = true;
					pkList.add(citem.getItemName());
				}
			}
			if (!hasPrimaryKey) {
				throw new CustomException("定义模型属性中必须定义主键");
			}
			MysqlTableVO MysqlTableVO = null;
			try {
				if (head.getTableCode() != null && head.getTableCode().trim().length() >= 0) {
					MysqlTableVO = dao.queryTableMetaData(head.getTableCode().trim());
				}
			} catch (Exception ex1) {
				MysqlTableVO = null;
			}
			String table = head.getTableCode().trim();
			String pkIndex = "pk_" + table + "_index";

			// 新建表
			if (MysqlTableVO == null) {
				StringBuilder sql = new StringBuilder();
				sql.append("create table ").append(table).append("( ");

				for (DlhDatamodelItem citem : itemList) {
					MysqlColumnType type = MysqlColumnType.getType(citem.getItemType());
					sql.append(type.getService().getSqlOnCreate(citem.getItemName(), citem.getItemLen()));
					if (citem.getItemComment() != null) {
						sql.append(" COMMENT '" + citem.getItemComment() + "'");
					}
					sql.append(",");
				}
				// 默认数据列
				sql.append(DlhConst.DEFAULT_ID).append("  varchar(100) COMMENT '导入主键' , ");
				sql.append(DlhConst.DEFAULT_DATE).append(" datetime  COMMENT '最后的更新时间' , ");
				sql.append(DlhConst.DEFAULT_KEY).append(" varchar(64) NOT NULL COMMENT '数据来源接口' ,");
				sql.append(DlhConst.DEFAULT_USER).append(" varchar(100) NOT NULL COMMENT '数据来源'");
				sql.append(") ENGINE=InnoDB CHARSET=utf8 COMMENT='" + head.getTableName() + "'");
				dao.execute(sql.toString());
				// 建立索引
				dao.execute("CREATE INDEX dlh_pk_" + table + "_index ON " + table + "(" + DlhConst.DEFAULT_ID + ")");

			}
			// 修改表
			else {
				List<MysqlColumn> cList = MysqlTableVO.getCols();
				Map<String, MysqlColumn> cMap = new HashMap<String, MysqlColumn>();
				for (MysqlColumn c : cList) {
					cMap.put(c.getName().toUpperCase(), c);
				}
				List<DlhDatamodelItem> addItemList = new ArrayList<DlhDatamodelItem>();
				for (DlhDatamodelItem citem : itemList) {
					if (cMap.containsKey(citem.getItemName().toUpperCase())) {
						continue;
					}
					addItemList.add(citem);
				}
				if (!cMap.containsKey(DlhConst.DEFAULT_ID.toUpperCase())) {
					// 添加列
					dao.execute("alter table " + table + " add " + DlhConst.DEFAULT_ID + " varchar(100) ");
					// 建立索引
					dao.execute("CREATE INDEX dlh_pk_" + table + "_index ON " + table + "(" + DlhConst.DEFAULT_ID + ")");
				}
				if (!cMap.containsKey(DlhConst.DEFAULT_DATE.toUpperCase())) {
					dao.execute("alter table " + table + " add " + DlhConst.DEFAULT_DATE + " datetime ");
				}
				if (!cMap.containsKey(DlhConst.DEFAULT_KEY.toUpperCase())) {
					dao.execute("alter table " + table + " add " + DlhConst.DEFAULT_KEY + " varchar(100) ");
				}
				if (!cMap.containsKey(DlhConst.DEFAULT_USER.toUpperCase())) {
					dao.execute("alter table " + table + " add " + DlhConst.DEFAULT_USER + " varchar(100) ");
				}
				for (DlhDatamodelItem additem : addItemList) {
					StringBuilder sql = new StringBuilder();
					sql.append("alter table ").append(table).append(" add ");
					MysqlColumnType type = MysqlColumnType.getType(additem.getItemType());
					sql.append(type.getService().getSqlOnCreate(additem.getItemName(), additem.getItemLen()));
					dao.execute(sql.toString());
				}
				try {
					// 删除索引
					String sqlDelIndex = "drop index " + pkIndex + " on " + table;
					dao.execute(sqlDelIndex);
				} catch (Exception ex) {

				}
				// 建立索引
				String sqlIndex = "CREATE INDEX " + pkIndex + " ON " + table + "(" + ListTool.join(pkList) + ")";
				dao.execute(sqlIndex.toString());

			}

		} catch (Exception ex) {
			throw new CustomException(ex);
		} finally {
			if (dao != null) {
				dao.close();
			}
		}
	}


	private String buildConStr(List<Map> condMapList){
		StringBuilder where = new StringBuilder(" where 1=1 ");
		if(condMapList==null||condMapList.size()<=0){
			return where.toString();
		}
		for (Map cond : condMapList) {
			String operationType = (String) cond.get("operationType");
			String operationAction = (String) cond.get("operationAction");
			String operationKey = (String) cond.get("operationKey");
			String value = (String) cond.get("value");
			MysqlColumnType ctype = MysqlColumnType.getType(operationType);
			where.append(" and ").append(ctype.getService().getCondOnQuery(operationAction, operationKey, value));
		}
		return where.toString();
	}
	@Override
	public boolean deleteData(DlhDataobject object, List<Map> condMapList){
		StringBuilder sqlExists = new StringBuilder();
		sqlExists.append("delete from ").append(object.getTableCode());
		String conStr = buildConStr(condMapList);

		Statement stat = null;
		PreparedStatement insertStat = null;
		MysqlDao dao = null;
		boolean rb = false;
		try {
			dao = MysqlDao.getInstance(object.getDbSource());
			dao.setAutoCommit(false);
			stat = dao.createStatement();
			sqlExists.append(conStr);


		} catch (CustomException e) {
			new CustomException("创建链接异常");
			return false;
		}
		try {
			rb = stat.execute(sqlExists.toString());
		} catch (SQLException e) {
			new CustomException("sql执行异常："+e.getMessage());
			System.out.println(sqlExists);
			e.printStackTrace();
		}finally {
			this.closeConnection(stat,insertStat,dao);
		}
		return rb;

	}

	/*
	* dataList:[
	* 	(map){col1:value1,col1:value1,col1:value1},
	* 	(map){col1:value1,col1:value1,col1:value1}
	* ]   每条记录一个map*/
	@Override
	public boolean addData(DlhDataobject object, List<Map<String, Object>> dataList)throws CustomException{
		List<DlhDataobjectField> dlhDataobjectFields = null;
		try {
			dlhDataobjectFields = loadFieldLis(object.getId());
		} catch (CustomException e) {
			throw new CustomException("加载对象属性错误");
		}
		Map<String, DlhDataobjectField> primaryKey = new HashMap<String, DlhDataobjectField>();
		Map<String, List<RuleVO>> fieldRuleMap = new HashMap<String, List<RuleVO>>();
		try {
			this.getPrimaryKey(primaryKey,fieldRuleMap,dlhDataobjectFields);
		} catch (CustomException e) {
			throw e;
		}
		//校验数据格式
		String message = checkData(dataList,fieldRuleMap);
		if(!StringUtil.isEmpty(message)){
			throw new CustomException(message);
		}

		Statement stat = null;
		PreparedStatement insertStat = null;
		MysqlDao dao = null;
		/*Map<String, Object> connection = this.createConnection(object.getDbSource(), stat, insertStat, dao, sqlInsert);

		stat = (Statement) connection.get("stat");
		dao = (MysqlDao) connection.get("dao");
		insertStat = (PreparedStatement) connection.get("insertStat");*/
		try {
			dao = MysqlDao.getInstance(object.getDbSource());
			dao.setAutoCommit(false);
			stat = dao.createStatement();

			for (Map<String, Object> line : dataList) {
				boolean ishave = this.checkIsHave(primaryKey,line,object,dlhDataobjectFields);
				if(!ishave){
					/*不存在*/
					StringBuilder sqlInsert = new StringBuilder();
					try {
						buildStrlCol(object,dlhDataobjectFields,sqlInsert);
					} catch (Exception e) {
						e.printStackTrace();
					}
					this.doSetPara(dlhDataobjectFields,sqlInsert,object);
					insertStat = dao.prepareStatement(sqlInsert.toString());
					this.buildInsertValus(line,dlhDataobjectFields,insertStat);

					try {

						insertStat.execute();

					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			dao.commit();

		} catch (CustomException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(stat,insertStat,dao);
		}

		return false;
	}

	@Override
	public boolean updateData(DlhDataobject object, Map<String, Object> dataMap, String column, String uuid)throws CustomException{
		List<DlhDataobjectField> dlhDataobjectFields = null;
		try {
			dlhDataobjectFields = loadFieldLis(object.getId());
		} catch (CustomException e) {
			throw new CustomException("加载对象属性错误");

		}
		StringBuilder sqlUpdate = new StringBuilder();
		sqlUpdate.append("UPDATE ").append(object.getTableCode()).append(" set ");
		Map<Integer,DlhDataobjectField> valueMap = new HashMap<>();
		int count = 1;
		for(DlhDataobjectField field:dlhDataobjectFields){
			if(dataMap.containsKey(field.getFieldCode())){
				sqlUpdate.append(field.getItemName()).append("=? , ");
				valueMap.put(count,field);
				count++;
			}
		}
		if (count<=0){
			throw new CustomException("没有设置值");
		}else {
			sqlUpdate.append(" dlh_data_modify_date_=now() ");
			sqlUpdate.append(" where ");
			if(StringUtil.isEmpty(column)){
				sqlUpdate.append(DlhConst.DEFAULT_ID + "='" + uuid + "' ");
			}else{
				sqlUpdate.append(column + "='" + uuid + "' ");
			}
			Statement stat = null;
			PreparedStatement insertStat = null;
			MysqlDao dao = null;

			dao = MysqlDao.getInstance(object.getDbSource());
			dao.setAutoCommit(false);
			insertStat = dao.prepareStatement(sqlUpdate.toString());

			for (Integer key:valueMap.keySet()){
				DlhDataobjectField field = valueMap.get(key);
				MysqlColumnType ctype = MysqlColumnType.getType(field.getItemType());
				try {
					ctype.getService().setStatementParam(insertStat,key, dataMap.get(field.getFieldCode()));
				} catch (Exception e) {
					e.printStackTrace();
					throw new CustomException("设置参数值过程中发生错误"+e.getMessage());
				}

			}

			try {
				insertStat.execute();
				dao.commit();
			} catch (SQLException e) {
				e.printStackTrace();
				this.closeConnection(stat,insertStat,dao);
			}
			this.closeConnection(stat,insertStat,dao);
		}
		return true;
	}

	/*根据主键查询数据*/
	@Override
	public List<Map<String, String>> queryDataByPrimaryKey(DlhDataobject object, String uuid) throws CustomException {
		List<DlhDataobjectField> dlhDataobjectFields = null;
		Map<String, List<RuleVO>> fieldRuleMap = new HashMap<String, List<RuleVO>>();
		try {
			dlhDataobjectFields = loadFieldLis(object.getId());
		} catch (CustomException e) {
			throw new CustomException("加载对象属性错误");
		}
		Map<String, DlhDataobjectField> primaryKey = new HashMap<String, DlhDataobjectField>();
		this.getPrimaryKey(primaryKey,fieldRuleMap,dlhDataobjectFields);
		List<Map<String, String>> mapList = new ArrayList<>();
		for (DlhDataobjectField key : primaryKey.values()) {

			MysqlColumnType ctype = MysqlColumnType.getType(key.getItemType());
			mapList = this.queryById(object,uuid, dlhDataobjectFields, key.getFieldCode());

		}
		return mapList;
	}

	/*加载对象的所有属性*/
	private List<DlhDataobjectField> loadFieldLis(String objectId) throws CustomException {
		DlhDataobjectField fieldCond = new DlhDataobjectField();
		fieldCond.setObjectId(objectId);
		IDlhDataobjectFieldService dlhDataobjectFieldService = SpringContextHolder.getBean(IDlhDataobjectFieldService.class);
		List<DlhDataobjectField> fieldList = dlhDataobjectFieldService.queryAll(fieldCond);
		return fieldList;
	}
	/*获取主键及校验规则Map*/
	private void getPrimaryKey(Map<String, DlhDataobjectField> primaryKey,Map<String, List<RuleVO>> fieldRuleMap,List<DlhDataobjectField> fieldList) throws CustomException {

		for (DlhDataobjectField item : fieldList) {
			if (item.getFieldCheck() != null && item.getFieldCheck().length() > 5) {
				fieldRuleMap.put(item.getFieldCode(), JsonUtilOld.json2Array(item.getFieldCheck(), RuleVO.class));
			}

			MysqlColumnType ctype = MysqlColumnType.getType(item.getItemType());
			if (ctype == null) {
				throw new CustomException("交换对象属性类型没有支持：" + item.getItemType());
			}
			if ("Y".equalsIgnoreCase(item.getItemKey())) {
				primaryKey.put(item.getId(), item);
			}
		}
	}

	/*校验数据格式*/
	private String checkData( List<Map<String, Object>> dataList,Map<String, List<RuleVO>> fieldRuleMap) throws CustomException {
		List<RuleVO> rules;
		int row = 0;
		String message = "";
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
						message="第" + row + "行出现错误：" + msg;
					}
				}
			}
		}
		return message;
	}

	/*构建sql语句中列*/
	private void buildStrlCol(DlhDataobject object,List<DlhDataobjectField> fieldList,StringBuilder sqlInsert){

		sqlInsert.append("insert into ").append(object.getTableCode()).append(" (");
		DlhDataobjectField field;
		for (int i = 0; i < fieldList.size(); i++) {
			field = fieldList.get(i);
			sqlInsert.append(field.getItemName()).append(",");
		}
		sqlInsert.append(DlhConst.DEFAULT_ID).append(",");
		sqlInsert.append(DlhConst.DEFAULT_KEY).append(",");
		sqlInsert.append(DlhConst.DEFAULT_USER).append(",");
		sqlInsert.append(DlhConst.DEFAULT_DATE);
		sqlInsert.append(") values (");
	}

	/*生成sql 中的values*/
	private  void doSetPara(List<DlhDataobjectField> fieldList,StringBuilder sqlInsert,DlhDataobject object) throws CustomException {


		for (int i = 0; i < fieldList.size(); i++) {

			sqlInsert.append("?,");
		}
		sqlInsert.append("?,");
		sqlInsert.append("'").append(object.getObjUrl()).append("',");
		DlhUser user = UserContext.getUser();
		if (user == null) {
			sqlInsert.append("'',");
		} else {
			sqlInsert.append("'").append(user.getDlhUsername()).append("',");
		}

		sqlInsert.append("now()");
		sqlInsert.append(")");
	}
	/*校验主键记录是否存在*/
	private boolean checkIsHave(Map<String, DlhDataobjectField> primaryKey,Map<String, Object> line,DlhDataobject object,List<DlhDataobjectField> fieldList) throws CustomException {



		for (DlhDataobjectField key : primaryKey.values()) {
			Object value = line.get(key.getFieldCode());
			if (value == null || value.toString().trim().length() <= 0) {
				value = "";
				throw new CustomException("主键不允许为空");
			}
			MysqlColumnType ctype = MysqlColumnType.getType(key.getItemType());
			List<Map<String, String>> mapList = this.queryById(object, value.toString(), fieldList, key.getFieldCode());
			if(mapList!=null&&mapList.size()>0){
				return true;
			}
		}
		return false;
	}
	/*设置值*/
	private void buildInsertValus(Map<String, Object> line,List<DlhDataobjectField> fieldList,PreparedStatement insertStat) throws CustomException {


		// 插入新数据
		DlhDataobjectField field =null;
		for (int i = 0; i < fieldList.size(); i++) {
			field = fieldList.get(i);
			MysqlColumnType ctype = MysqlColumnType.getType(field.getItemType());
			try {
				ctype.getService().setStatementParam(insertStat, i + 1, line.get(field.getFieldCode()));
			} catch (Exception e) {
				e.printStackTrace();
				throw new CustomException("设置参数值过程中发生错误"+e.getMessage());
			}
		}
		try {
			insertStat.setString(fieldList.size() + 1, generatorBusiId());

		} catch (SQLException e) {
			e.printStackTrace();
			throw new CustomException("数据执行发生错误"+e.getMessage());
		}

	}

	/*创建链接*/
	private Map<String,Object> createConnection(String dbSource,Statement stat,PreparedStatement insertStat,MysqlDao dao,StringBuilder sql) throws CustomException {

		Map<String,Object> map = new HashMap<>();

		dao = MysqlDao.getInstance(dbSource);
		dao.setAutoCommit(false);
		map.put("dao",dao);

		stat = dao.createStatement();
		map.put("stat",stat);

		insertStat = dao.prepareStatement(sql.toString());
		map.put("insertStat",insertStat);

		return map;

	}
	private void closeConnection(Statement stat,PreparedStatement insertStat,MysqlDao dao){
		if (dao != null) {
			try {
				dao.rollback();
			} catch (Exception e) {
				log.error("dao回滚异常：" + e.getMessage(), e);
			}
		}
		if (stat != null) {
			try {
				stat.close();
			} catch (Exception e) {
				log.error("关闭stat异常：" + e.getMessage(), e);
			}
		}
		if (insertStat != null) {
			try {
				insertStat.close();

			} catch (Exception e) {
				log.error("关闭insertStat异常：" + e.getMessage(), e);
			}
		}
		if (dao != null) {
			dao.close();
		}
	}



	@Override
	public void modify(DlhDataobject object, List<Map<String, Object>> dataList) throws CustomException {
		if (dataList == null || dataList.size() == 0) {
			log.info("**插入（" + object.getTableCode() + "）完成，更新条数：0");
		}
        dataList = list2Map(dataList);
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
				fieldRuleMap.put(item.getFieldCode(), JsonUtilOld.json2Array(item.getFieldCheck(), RuleVO.class));
			}

			MysqlColumnType ctype = MysqlColumnType.getType(item.getItemType());
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
		// 数据保存
		Statement stat = null;
		PreparedStatement insertStat = null;
		MysqlDao dao = null;
		try {
			// 创建连接
			dao = MysqlDao.getInstance(object.getDbSource());
			// 插入SQL
			StringBuilder sqlInsert = new StringBuilder();
			sqlInsert.append("insert into ").append(object.getTableCode()).append(" (");
			DlhDataobjectField field;
			for (int i = 0; i < fieldList.size(); i++) {
				field = fieldList.get(i);
				sqlInsert.append(field.getItemName()).append(",");
			}
			sqlInsert.append(DlhConst.DEFAULT_ID).append(",");
			sqlInsert.append(DlhConst.DEFAULT_KEY).append(",");
			sqlInsert.append(DlhConst.DEFAULT_USER).append(",");
			sqlInsert.append(DlhConst.DEFAULT_DATE);
			sqlInsert.append(") values (");
			for (int i = 0; i < fieldList.size(); i++) {
				field = fieldList.get(i);
				sqlInsert.append("?,");
			}
			sqlInsert.append("?,");
			sqlInsert.append("'").append(object.getObjUrl()).append("',");
			DlhUser user = UserContext.getUser();
			if (user == null) {
				sqlInsert.append("'',");
			} else {
				sqlInsert.append("'").append(user.getDlhUsername()).append("',");
			}

			sqlInsert.append("now()");
			sqlInsert.append(")");
			// 取得连接
			dao.setAutoCommit(false);
			stat = dao.createStatement();
			insertStat = dao.prepareStatement(sqlInsert.toString());
			// 判断是修改还是删除
			for (Map<String, Object> line : dataList) {
				// 判断是新增还是修改
				boolean haveData = true;
				for (DlhDataobjectField key : primaryKey.values()) {
					Object value = line.get(key.getFieldCode());
					if (value == null || value.toString().trim().length() <= 0) {
						value = "";
						throw new CustomException("主键不允许为空!");
					}
					List<Map<String, String>> mapList = this.queryById(object, value.toString(), fieldList, key.getFieldCode());
					if(mapList!=null&&mapList.size()>0){
						this.updateData(object,line,key.getFieldCode(),value.toString());

					}else{
						haveData=false;
					}
//
				}

				// 插入新数据
				if(!haveData){
					for (int i = 0; i < fieldList.size(); i++) {
						field = fieldList.get(i);
						MysqlColumnType ctype = MysqlColumnType.getType(field.getItemType());
						ctype.getService().setStatementParam(insertStat, i + 1, line.get(field.getFieldCode()));
					}
					insertStat.setString(fieldList.size() + 1, generatorBusiId());
					insertStat.execute();
				}

			}
			dao.commit();
			log.info("**插入（" + object.getTableCode() + "）完成，更新条数：" + dataList.size());
		} catch (Exception ex) {
			log.error("发送异常，本次提交回滚，异常信息：" + ex.getMessage(), ex);
			if (dao != null) {
				try {
					dao.rollback();
				} catch (Exception e) {
				}
			}
		} finally {
			if (stat != null) {
				try {
					stat.close();
				} catch (Exception e) {
				}
			}
			if (insertStat != null) {
				try {
					insertStat.close();
				} catch (Exception e) {
				}
			}
			if (dao != null) {
				dao.close();
			}
		}
	}

	/**
	 * @Document 业务主键
	 * @return
	 */
	private String generatorBusiId() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

    private List<Map<String, Object>> list2Map(List<Map<String, Object>> dataList){

        List<Map<String, Object>> retlist = new ArrayList<>();
        Map<String, Object> hashMap = new HashMap<>();

        for(Map<String, Object> map:dataList){
            hashMap.put((String) map.get("name"),map.get("value"));
        }
        retlist.add(hashMap);
        return retlist;

    }
    private Map<String,DlhDataobjectField> fieldList2Map(List<DlhDataobjectField> fieldList){
		Map<String, DlhDataobjectField> fieldMap = new HashMap<String, DlhDataobjectField>();
		if (fieldList != null) {
			for (DlhDataobjectField f : fieldList) {
				fieldMap.put(f.getItemName(), f);
			}
		}
		return fieldMap;
	}

	/*对返回数据进行格式化
	* return [{id:111,name:222},{id:111,name:222}]*/
	private List<Map<String,Object>> dataFormate(List<Map<String, Object>> list,Map<String, DlhDataobjectField> fieldMap){
		Map<String, Object> newLine;
		MysqlColumnType ctype;
		DlhDataobjectField temp;
		IDicService dicService = SpringContextHolder.getBean(IDicService.class);
		List<Map<String, Object>> newList = new ArrayList<>();
		for (Map<String, Object> line : list) {
			newLine = new HashMap<>();
			for (Map.Entry<String, Object> item : line.entrySet()) {
				temp = fieldMap.get(item.getKey());
				if (temp != null) {

					ctype = MysqlColumnType.getType(temp.getItemType());
					if (ctype != null) {
						newLine.put(item.getKey(), ctype.getService().getDisplayValue(item.getValue()));
					} else {
						newLine.put(item.getKey(), item.getValue());
					}
					if (!StringUtil.isEmpty(temp.getItemDicCode())) {
						Dic dic = new Dic();
						if(newLine.get(item.getKey())!=null&&!"".equals(newLine.get(item.getKey()))){
							dic.setParentId(temp.getItemDicCode());
							dic.setCode(newLine.get(item.getKey()).toString());
							dic = dicService.get(dic);
							if(dic!=null&&dic.getValue()!=null){
								newLine.put(item.getKey(),dic.getValue());
							}
						}
					}
				} else {
					newLine.put(item.getKey(), item.getValue());
				}
			}
			newList.add(newLine);
		}
		return newList;
	}

    /*查询所有记录*/
	@Override
	public List<Map<String, Object>> queryAll(DlhDataobject object,String condJson)throws CustomException{


		List<Map> condMapList = new ArrayList<Map>();
		if (!StringUtil.isEmpty(condJson)) {
			condMapList = JsonUtilOld.json2Array(condJson, Map.class);
		}
		List<DlhDataobjectField> dlhDataobjectFields = this.loadFieldLis(object.getId());
		if(dlhDataobjectFields==null||dlhDataobjectFields.size()<=0){
			throw new CustomException("对象属性为空");
		}

		MysqlDao dao = MysqlDao.getInstance(object.getDbSource());
		StringBuilder where = new StringBuilder(" ");
		where.append(buildConStr(condMapList));
		where.append(" and dlh_data_src_ = '" + object.getObjUrl() + "'");
		String sqlPage = "select * from " + object.getTableCode() + " " + where;
		List<Map<String, Object>> list = dao.queryForList(sqlPage);
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		if (list==null||list.size()<=0){
			throw new CustomException("未查找到数据");
		}else{
			Map<String, DlhDataobjectField> stringDlhDataobjectFieldMap = this.fieldList2Map(dlhDataobjectFields);
			maps = this.dataFormate(list, stringDlhDataobjectFieldMap);
		}
		return maps;
	}

	/**
	 * @Document 分页查询
	 * @return
	 */
	@Override
    @SuppressWarnings("rawtypes")
	public PageManagerEx<Map<String, Object>> query(DlhDataobject object, PageManager page, String condJson) throws CustomException {
		MysqlDao dao = null;
		try {
			// 构建查询条件

			List<Map> condMapList = new ArrayList<Map>();
			if (!StringUtil.isEmpty(condJson)) {
				condMapList = JsonUtilOld.json2Array(condJson, Map.class);
			}
			StringBuilder where = new StringBuilder(" ");
			where.append(buildConStr(condMapList));
			where.append(" and dlh_data_src_ = '" + object.getObjUrl() + "'");
			// 创建连接
			dao = MysqlDao.getInstance(object.getDbSource());

			PageManagerEx<Map<String, Object>> page_ = new PageManagerEx<Map<String, Object>>();
			String sql = "select count(*) from " + object.getTableCode() + " " + where;
			Integer rowsCount = dao.queryForObject(sql, Integer.class);
			if (rowsCount == null) {
				rowsCount = 0;
			}
			page_.setTotalCount(rowsCount);
			if (page.getPageRows() == -1) {
				page.setPageRows(Integer.MAX_VALUE);
			}
			page_.setPageRows(page.getPageRows());
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
			String sqlPage = "select * from " + object.getTableCode() + " " + where;
			sqlPage += " limit " + (pageStart) * page.getPageRows() + "," + page.getPageRows();
			List<Map<String, Object>> list = dao.queryForList(sqlPage);
			if (list != null && list.size() > 0) {
				/*获取属性列表*/
				List<DlhDataobjectField> fieldList = this.loadFieldLis(object.getId());
				/*将属性转为Map*/
				Map<String, DlhDataobjectField> fieldMap = this.fieldList2Map(fieldList);
				// 转换显示的值
				List<Map<String, Object>> newList = this.dataFormate(list,fieldMap);




				page_.setData(newList);
			} else {
				page_.setData(list);
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
		MysqlDao dao = null;
		try {
			// 创建连接
			dao = MysqlDao.getInstance(object.getDbSource());

			String sql = "select * from " + object.getTableCode() + " where " + DlhConst.DEFAULT_ID + "='" + uuid + "' ";
			if(!StringUtil.isEmpty(yewuKey)){
				sql = "select * from " + object.getTableCode() + " where " + yewuKey + "='" + uuid + "' ";
			}

			List<Map<String, Object>> dbDataList = dao.queryForList(sql);
			if (dbDataList == null || dbDataList.size() == 0) {
				return new ArrayList<>();
			}
			Map<String, Object> dbData = dbDataList.get(0);

			List<Map<String, String>> resList = new ArrayList<Map<String, String>>();
			if (dbData == null || dbData.size() == 0) {
				return resList;
			}
			Map<String, Object> data = new HashMap<>();
			for (Map.Entry<String, Object> entry : dbData.entrySet()) {
				data.put(entry.getKey().toUpperCase(), entry.getValue());
			}
			Map<String, String> newLine;
			IDicService dicService = SpringContextHolder.getBean(IDicService.class);
			for (DlhDataobjectField field : fieldList) {
				String title = field.getFieldName();
				/*表单显示，详细信息才进行显示*/
				/*if(field.getFieldFormShow()==null||field.getFieldFormShow().intValue()<=0){
					continue;
				}*/
				if (title == null || title.trim().length() <= 0) {
					title = field.getFieldCode();
				}

				newLine = new HashMap<>();
				newLine.put("title", title);
				newLine.put("itemName", field.getItemName());
				newLine.put("itemType",field.getItemType());
				newLine.put("formShow",field.getFieldFormShow()+"");
				newLine.put("condShow",field.getFieldCondShow()+"");
				newLine.put("listShow",field.getFieldListShow()+"");
				newLine.put("dlh_data_src_",data.get("dlh_data_src_".toUpperCase())+"");


				Object value = data.get(field.getItemName().toUpperCase());
				MysqlColumnType ctype = MysqlColumnType.getType(field.getItemType());

				/*字典回显*/
				if (!StringUtil.isEmpty(field.getItemDicCode())) {
					Dic dic = new Dic();
					if(value!=null&&!"".equals(value)){
						dic.setParentId(field.getItemDicCode());
						dic.setCode(value.toString());
						dic = dicService.get(dic);
						if(dic!=null&&dic.getValue()!=null){
							newLine.put("dicCode",value.toString());
							newLine.put("value", dic.getValue());
						}
					}
				}else{
					newLine.put("value", ctype.getService().getDisplayValue(value));
				}
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
