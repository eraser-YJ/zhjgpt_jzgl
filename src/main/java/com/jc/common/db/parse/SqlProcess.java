package com.jc.common.db.parse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jc.common.db.parse.domain.ISegment;
import com.jc.common.db.parse.domain.SqlVO;

/**
 * @author Administrator
 * @description 替换工具
 */
public class SqlProcess {
	public static void main(String[] args) throws Exception {
		Map<String, Object> paramters = new HashMap<>();
		paramters.put("apiType1", 1);
		paramters.put("primaryKeys", "aaa,bbb,ccc1");
		paramters.put("apiType2", 2);
		paramters.put("apiType31", 31);
		paramters.put("apiType32", 32);
		paramters.put("apiType4", 4);
		paramters.put("apiType6", 6);
		paramters.put("apiType5", "zzz1");
//		String cond = "username == 'James' aNd  username1=='James1'";
//		System.out.println(predicate(cond, paramters));
		StringBuilder sql = new StringBuilder("select * from in_sf_flyzjgxx where 1=1 ");
		sql.append("and #{monthBegin,jdbctype=\"function\",interval=\"-5\"}");
//		sql.append("and API_TYPE1 = #{ apiType1,jdbcType=NUMERIC}1234567 1 \r");
//		sql.append("\n <if  test=\"apiType5 == 'zzz'\"> and id  in ");
//		sql.append("<foreach collection=\"primaryKeys\" item=\"primaryKey\" index=\"index\" open=\"(\" close=\")\" separator=\",\">");
//		sql.append("\n <if  test=\"primaryKey == 'ccc'\"> ");
//		sql.append("#{apiType6}");
//		sql.append("<  /  if   >");
//		sql.append("#{primaryKey}");
//		sql.append("</foreach>");
//		sql.append("<  /  if   >");
//		sql.append("and API_TYPE2 = ${ apiType2}\r\n");
//		sql.append("\n<if  test=\"apiType31 &gt;= 10\"> and API_TYPE31 = #{apiType31}, API_TYPE32 = #{apiType32} <  /  if   >");
//		sql.append("<if test=\"apiType4 != null\"> API_TYPE4 = #{apiType4}, <if test=\"apiType31 &gt;= 10\"> and API_TYPE31 = #{apiType31}, API_TYPE32 = #{apiType32}</   if  > </   if  >");
//		sql.append("<if  test=\"apiType4 != null\"> API_TYPE4 = #{apiType4}</if>");
		SqlVO vo = action(sql.toString(), paramters);
		System.out.println(vo.getSql());
		for (Object value : vo.getVoList()) {
			System.out.print(value + " ");
		}
		System.out.println();
	}

	// 缓存
	private static ConcurrentHashMap<String, List<ISegment>> sqlMap = new ConcurrentHashMap<>();

	/**
	 * @description 分析
	 * @param inSql
	 * @param paramters
	 * @return
	 * @throws Exception
	 */
	public static SqlVO action(String inSql, Map<String, Object> paramters) throws Exception {
		// 解析SQL,有缓存优先缓存
		List<ISegment> segmentList = null;
//		if (sqlMap.containsKey(inSql)) {
//			segmentList = sqlMap.get(inSql);
//		} else {
			segmentList = SqlParseTool.parse(inSql);
			sqlMap.put(inSql, segmentList);
//		}
		// 绑定参数
		return SqlParseTool.bound(segmentList, paramters);
	}

}
