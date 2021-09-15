package com.jc.busi.standard.service.impl;

import com.jc.busi.standard.domain.StandardParaVO;
import com.jc.busi.standard.domain.StandardResVO;
import com.jc.busi.standard.service.IDataService;
import com.jc.busi.standard.util.ThreeDESUtil;
import com.jc.busi.standard.util.UserContext;
import com.jc.common.log.Uog;
import com.jc.dlh.domain.DlhDataobject;
import com.jc.dlh.domain.DlhUser;
import com.jc.dlh.service.IDlhDataobjectService;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.SpringContextHolder;

public class DataServiceImpl implements IDataService {
	// 日志
	private Uog log = Uog.getInstanceOnSoap();

	public String sendData(String action, String base64jsonOri) {
		Long start = System.currentTimeMillis();
		StandardResVO resvo = new StandardResVO();
		try {
			DlhUser user = UserContext.getUser();
			if (user != null) {
				DlhDataobject cond = new DlhDataobject();
				cond.setObjUrl(action);
				// 解密数据
				String base64json = ThreeDESUtil.decrypt(user.getDlhPassword(), base64jsonOri);
				// json转对象
				StandardParaVO vo = (StandardParaVO) JsonUtil.json2Java(base64json, StandardParaVO.class);
				if (vo.getInfo() == null || vo.getInfo().size() == 0) {
					resvo.setCode("E900");
					resvo.setMsg("数据不能为空");
				}
				Integer max = user.getBatchNum();
				if (max == null || max <= 0) {
					max = 100;
				}
				if (vo.getInfo().size() > max) {
					resvo.setCode("E901");
					resvo.setMsg("数据不能为空");
				}
				// 调用
				IDlhDataobjectService service = SpringContextHolder.getBean(IDlhDataobjectService.class);
				service.modify(cond, vo.getInfo());
				resvo.setCode("0000");
				resvo.setMsg("success");
			} else {
				resvo.setCode("E100");
				resvo.setMsg("当前用户不存在");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resvo.setCode("E999");
			resvo.setMsg(e.getMessage());
		} finally {
			log.info("调用" + action + "耗时：" + (System.currentTimeMillis() - start));
		}
		String res = JsonUtil.java2Json(resvo);
		log.info("返回结果：" + res);
		return res;
	}
}
