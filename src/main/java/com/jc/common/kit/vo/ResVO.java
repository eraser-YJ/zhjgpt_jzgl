package com.jc.common.kit.vo;

/**
 * 标准返回结果
 * 
 * @author lc  liubq
 */
public class ResVO implements java.io.Serializable {
	// 成功编码
	public static final String SUCCESS_CODE = "200";
	// 失败编码
	public static final String FAIL_CODE = "201";
	// 编码
	private String code;
	// 消息
	private String message;
	// 数据
	private Object data;

	/**
	 * 成功结果
	 *
	 * @return
	 */
	public static ResVO buildSuccess() {
		return buildSuccess(null);
	}

	/**
	 * 成功结果
	 * 
	 * @param inData
	 * @return
	 */
	public static ResVO buildSuccess(Object inData) {
		ResVO vo = new ResVO();
		vo.setCode(SUCCESS_CODE);
		vo.setMessage("success");
		vo.setData(inData);
		return vo;
	}

	/**
	 * 失败结果
	 * 
	 * @param inMessage
	 * @return
	 */
	public static ResVO buildFail(String inMessage) {
		return buildFail(FAIL_CODE, inMessage, null);
	}

	/**
	 * 失败结果
	 * 
	 * @param inCode
	 * @param inMessage
	 * @param inData
	 * @return
	 */
	public static ResVO buildFail(String inCode, String inMessage, Object inData) {
		ResVO vo = new ResVO();
		vo.setCode(inCode);
		vo.setMessage(inMessage);
		vo.setData(inData);
		return vo;
	}

	/**
	 * 成功结果
	 *
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
	 * 数据
	 * 
	 * @return 数据
	 */
	public Object getData() {
		return data;
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
	 * 数据
	 * 
	 * @param data 数据
	 */
	public void setData(Object data) {
		this.data = data;
	}

}
