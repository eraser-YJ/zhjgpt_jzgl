package com.jc.common.kit.vo;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;

import java.util.ArrayList;
import java.util.List;

public class PageManagerEx<T> extends BaseBean {

	private static final long serialVersionUID = -7333606777664101156L;
	private int page;
	private List<T> dataList = new ArrayList<T>();
	private int totalPage = 0;
	private int pageRows = GlobalContext.ROWS_DEFAULT;// 每页显示记录数
	private int totalCount = 0;
	private int sEcho;

	public List<T> getData() {
		return this.dataList;
	}

	public int getiDisplayLength() {
		return getPageRows();
	}

	public int getiTotalRecords() {
		// 本次加载记录数量
		return getTotalCount();
	}

	public int getPage() {
		return page;
	}

	public int getTotalPages() {
		return totalPage;
	}

	public int getPageRows() {
		return pageRows;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getsEcho() {
		return sEcho;
	}

	public void setData(List<T> list) {
		this.dataList = list;
	}

	public void setiDisplayLength(int iDisplayLength) {
		setPageRows(iDisplayLength);
	}

	public void setiTotalRecords(int iTotalRecords) {
		setTotalCount(iTotalRecords);
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setTotalPages(int pageCount) {
		this.totalPage = pageCount;
	}

	public void setPageRows(int rows) {
		this.pageRows = rows;
	}

	public void setTotalCount(int rowsCount) {
		this.totalCount = rowsCount;
	}

	public void setsEcho(int sEcho) {
		this.sEcho = sEcho;
	}

	public void setAaData(List<T> list) {
		setData(list);
	}

	public List<T> getAaData() {
		return getData();
	}

	public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
		// 总记录数量
		setTotalCount(iTotalDisplayRecords);
	}

	public int getiTotalDisplayRecords() {
		return getTotalCount();
	}
	public PageManagerEx() {}
	public PageManagerEx(PageManager page, List<T> dataList, int pageRows) {
		this.setPage(page.getPage());
		this.setTotalPages(page.getTotalPages());
		this.setPageRows(pageRows);
		this.setTotalCount(page.getTotalCount());
		this.setsEcho(page.getsEcho());
		this.setData(dataList);
	}

}
