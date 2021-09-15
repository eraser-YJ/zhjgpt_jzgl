package com.jc.oa.click.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * Created by yangj on 2016/6/23.
 */
public class Click extends BaseBean {
    private String userId;//用户ID
    private String menuId;//菜单ID
    private String menuName;//菜单名称
    private String menuAction;//菜单路径
    private Long clicks;//点击次数
    private Long clickCount;//查询时使用的查询条数
    private String menuClass;//top5样式

    public String getMenuClass() {
        return menuClass;
    }

    public void setMenuClass(String menuClass) {
        this.menuClass = menuClass;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuAction() {
        return menuAction;
    }

    public void setMenuAction(String menuAction) {
        this.menuAction = menuAction;
    }

    public Long getClicks() {
        return clicks;
    }

    public void setClicks(Long clicks) {
        this.clicks = clicks;
    }

	public Long getClickCount() {
		return clickCount;
	}

	public void setClickCount(Long clickCount) {
		this.clickCount = clickCount;
	}
    
}
