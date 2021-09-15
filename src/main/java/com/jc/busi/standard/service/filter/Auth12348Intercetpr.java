package com.jc.busi.standard.service.filter;

import java.io.ByteArrayInputStream;

import javax.xml.soap.SOAPException;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.jc.busi.standard.util.UserContext;
import com.jc.common.log.Uog;
import com.jc.dlh.cache.CacheDlhUser;
import com.jc.dlh.domain.DlhUser;

/**
 * @description 授权检查
 * @author lc Administrator
 */
public class Auth12348Intercetpr extends AbstractPhaseInterceptor<SoapMessage> {
	// 日志
	private Uog log = Uog.getInstanceOnSoap();

	public Auth12348Intercetpr() {
		// 指定该拦截器在哪个阶段被激发
		super(Phase.READ);
	}

	// 处理消息
	public void handleMessage(SoapMessage message) {
		String xml = UserContext.getXML();
		if (null == xml || xml.length() < 1) {
			throw new Fault(new SOAPException("SOAP消息头格式不对哦！"));
		}
		xml = xml.replaceAll("wsse:", "wsse_");
		SAXReader reader = null;
		try {
			reader = new SAXReader();
			Document document = reader.read(new ByteArrayInputStream(xml.getBytes()));
			org.dom4j.Element username = (org.dom4j.Element) document.getRootElement().selectObject("/soapenv:Envelope/soapenv:Header/wsse_Security/wsse_UsernameToken/wsse_Username");
			org.dom4j.Element password = (org.dom4j.Element) document.getRootElement().selectObject("/soapenv:Envelope/soapenv:Header/wsse_Security/wsse_UsernameToken/wsse_Password");
			if (null == username || password == null) {
				log.info("SOAP消息头格式不对");
				throw new Fault(new SOAPException("SOAP消息头格式不对哦！"));
			}
			if (username.getText() == null || username.getText().trim().length() <= 0) {
				log.info("用户不存在，用户为空");
				SOAPException soapExc = new SOAPException("阁下不是合法用户！");
				throw new Fault(soapExc);
			}
			DlhUser user = CacheDlhUser.queryByUserName(username.getText());
			if (user == null) {
				log.info("用户不存在，用户：" + username.getText());
				SOAPException soapExc = new SOAPException("阁下不是合法用户！");
				throw new Fault(soapExc);
			}
			if (!user.getDlhPassword().trim().equalsIgnoreCase(password.getText())) {
				log.info("密码不正确");
				SOAPException soapExc = new SOAPException("阁下可能不是合法用户！");
				throw new Fault(soapExc);
			}
			UserContext.setUser(user);
		} catch (Exception e) {
			log.error("Auth12348Intercetpr异常", e);
			throw new Fault(new SOAPException("SOAP消息格式不正确，异常信息：" + e.getMessage(), e));
		}
	}
}
