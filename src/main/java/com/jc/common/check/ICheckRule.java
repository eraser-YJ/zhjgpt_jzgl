package com.jc.common.check;

public interface ICheckRule {
	/**
	 * @description 检查接口
	 * @param cmd   配置的命令
	 * @param value 参数
	 * @return 检查通过返回空，不同过返回错误信息
	 */
	public String check(String cmd, String key, Object value);
}
