package com.jc.resource.bean;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.GlobalContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 常鹏
 * @Date 2020/7/27 10:55
 * @Version 1.0
 */
public class PageManagerMapData extends BaseBean {
    private static final long serialVersionUID = -7333606777664101156L;
    private int page;
    private List data = new ArrayList();
    private int totalPage = 0;
    private int pageRows;
    private int totalCount;
    private int sEcho;

    public PageManagerMapData() {
        this.pageRows = GlobalContext.ROWS_DEFAULT;
        this.totalCount = 0;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public int getiDisplayLength() {
        return this.getPageRows();
    }

    public int getiTotalRecords() {
        return this.getTotalCount();
    }

    public int getPage() {
        return this.page;
    }

    public int getTotalPages() {
        return this.totalPage;
    }

    public int getPageRows() {
        return this.pageRows;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public int getsEcho() {
        return this.sEcho;
    }


    public void setiDisplayLength(int iDisplayLength) {
        this.setPageRows(iDisplayLength);
    }

    public void setiTotalRecords(int iTotalRecords) {
        this.setTotalCount(iTotalRecords);
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

    public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
        this.setTotalCount(iTotalDisplayRecords);
    }

    public int getiTotalDisplayRecords() {
        return this.getTotalCount();
    }
}
