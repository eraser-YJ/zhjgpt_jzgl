package com.jc.common.db.parse.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class StringFragment implements IFragment {

	private String oriSql;

	public StringFragment(String inSql) {
		this.oriSql = inSql;

	}

	@Override
	public List<SqlExePara> boundSql(Map<String, Object> paraMap) {
		return Arrays.asList(new SqlExePara(" " + this.oriSql + " ", null));
	}

}
