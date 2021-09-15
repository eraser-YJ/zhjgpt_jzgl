package com.jc.common.db.parse.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jc.common.db.parse.SqlParseTool;

public class StringSegment implements ISegment {

	private String segmentSql;

	private List<IFragment> fragmentList;

	public StringSegment(String inSql) {
		this.segmentSql = inSql;
		if (segmentSql == null) {
			segmentSql = "";
		}
		segmentSql = segmentSql.trim();
		fragmentList = SqlParseTool.processFragment(segmentSql);
	}

	public List<SqlExePara> boundSql(Map<String, Object> paraMap) {
		List<SqlExePara> list = new ArrayList<>();
		List<SqlExePara> listTemp;
		for (IFragment fo : fragmentList) {
			listTemp = fo.boundSql(paraMap);
			if (listTemp != null && listTemp.size() > 0) {
				list.addAll(listTemp);
			}
		}
		return list;

	}

}
