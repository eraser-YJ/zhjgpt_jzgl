package com.jc.common.db.parse.domain;

import java.util.List;
import java.util.Map;

public interface IFragment {

	public List<SqlExePara> boundSql(Map<String, Object> paraMap);
}
