package com.jc.common.kit.vo;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.domain.PageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述
 * 
 * @author lc  liubq
 */
public class PageVO implements java.io.Serializable {
	// 成功编码
	public static final String SUCCESS_CODE = "000000";
	// 失败编码
	public static final String FAIL_CODE = "111111";
	// 编码
	private String code = SUCCESS_CODE;
	// 消息
	private String message;
	// 行数
	private Integer pageRows = 10;
	// 页号
	private Integer page = 0;
	// 总页数
	private Integer totalPage = 0;
	// 总数
	private Integer totalCount = 0;
	// 当前页数据
	private List<? extends BaseBean> dataList = new ArrayList<BaseBean>();

	/**
	 * 失败结果
	 * 
	 * @param inMessage
	 * @return
	 */
	public static PageVO buildFail(String inMessage) {
		PageVO vo = new PageVO();
		vo.setCode(FAIL_CODE);
		vo.setMessage(inMessage);
		return vo;
	}

	/**
	 * 成功结果
	 * 
	 * @param inData
	 * @return
	 */
	public boolean getSuccess() {
		return SUCCESS_CODE.equals(code);
	}

	/**
	 * 编码
	 * 
	 * @return 编码
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 消息
	 * 
	 * @return 消息
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 编码
	 * 
	 * @param code 编码
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 消息
	 * 
	 * @param message 消息
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 行数
	 * 
	 * @return 行数
	 */
	public Integer getPageRows() {
		return pageRows;
	}

	/**
	 * 行数
	 * 
	 * @param pageRows 行数
	 */
	public void setPageRows(Integer pageRows) {
		this.pageRows = pageRows;
	}

	/**
	 * 页号
	 * 
	 * @return 页号
	 */
	public Integer getPage() {
		return page;
	}

	/**
	 * 页号
	 * 
	 * @param page 页号
	 */
	public void setPage(Integer page) {
		this.page = page;
	}

	/**
	 * 总页数
	 * 
	 * @return 总页数
	 */
	public Integer getTotalPages() {
		return totalPage;
	}

	/**
	 * 总页数
	 * 
	 * @param totalPage 总页数
	 */
	public void setTotalPages(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	/**
	 * 总数
	 * 
	 * @param totalCount 总数
	 */
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 当前页数据
	 * 
	 * @return 当前页数据
	 */
	public List<? extends BaseBean> getData() {
		return dataList;
	}

	/**
	 * 当前页数据
	 * 
	 * @param inDataList 当前页数据
	 */
	public void setData(List<? extends BaseBean> inDataList) {
		this.dataList = inDataList;
	}

	/**
	 * 转化
	 * 
	 * @param pageVO
	 * @return
	 */
	public static PageManager buildPage(PageVO pageVO) {
		PageManager page = new PageManager();
		page.setPage(pageVO.getPage());
		page.setPageRows(pageVO.getPageRows());
		return page;
	}

	/**
	 * 转化
	 * 
	 * @param page
	 * @return
	 */
	public static PageVO buildVO(PageManager page) {
		PageVO pageVO = new PageVO();
		pageVO.setCode(SUCCESS_CODE);
		pageVO.setPage(page.getPage());
		pageVO.setPageRows(page.getPageRows());
		pageVO.setTotalCount(page.getTotalCount());
		pageVO.setTotalPages(page.getTotalPages());
		pageVO.setData(page.getData());
		return pageVO;
	}
}
