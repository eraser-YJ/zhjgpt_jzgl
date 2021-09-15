package com.jc.common.check;

import com.jc.common.kit.ListTool;
import com.jc.common.kit.vo.DicVO;
import com.jc.dlh.service.IDlhCommonService;
import com.jc.foundation.util.SpringContextHolder;

import java.util.ArrayList;
import java.util.List;

public class RuleDic implements ICheckRule {

	@Override
	public String check(String cmd, String key, Object value) {
		if (value == null || key.trim().length() <= 0) {
			return null;
		}
		if (cmd == null || cmd.trim().length() <= 0) {
			return key + "的检查项字典不存在";
		}
		try {
			IDlhCommonService service = SpringContextHolder.getBean(IDlhCommonService.class);
			List<DicVO> dicList = service.getDic(cmd);
			if (dicList == null || dicList.size() == 0) {
				return key + "的检查项字典属性不存在";
			}
			String code = value.toString();
			List<String> disMsgList = new ArrayList<String>();
			for (DicVO dic : dicList) {
				disMsgList.add(dic.getName() + "(" + dic.getCode() + ")");
				if (code.equals(dic.getCode())) {
					return null;
				}
			}
			if (disMsgList.size() > 0) {
				return key + "的值不合法，字典列表：" + ListTool.join(disMsgList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return key + "的检查字典异常：" + e.getMessage();
		}
		return null;
	}

}
