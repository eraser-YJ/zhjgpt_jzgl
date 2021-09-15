package com.jc.csmp.common;

import java.io.Serializable;
import java.util.List;

/**
 * formItemPrivJsonStr 结构化对象
 * 
 * @author liubq
 * @since 2018年5月16日
 */
public class WorkflowVarAgg implements Serializable {


	private List<WorkflowVarVO> have;

	private List<WorkflowVarVO> todo;

	public List<WorkflowVarVO> getHave() {
		return have;
	}

	public void setHave(List<WorkflowVarVO> have) {
		this.have = have;
	}

	public List<WorkflowVarVO> getTodo() {
		return todo;
	}

	public void setTodo(List<WorkflowVarVO> todo) {
		this.todo = todo;
	}

}
