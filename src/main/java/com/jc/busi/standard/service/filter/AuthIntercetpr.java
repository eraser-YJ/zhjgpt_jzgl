package com.jc.busi.standard.service.filter;

import java.io.ByteArrayInputStream;

import javax.xml.soap.SOAPException;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.jc.busi.standard.util.RSAUtil;
import com.jc.busi.standard.util.ThreeDESUtil;
import com.jc.busi.standard.util.UserContext;
import com.jc.common.log.Uog;
import com.jc.dlh.cache.CacheDlhUser;
import com.jc.dlh.domain.DlhUser;

/**
 * @description 授权检查
 * @author lc Administrator
 */
public class AuthIntercetpr extends AbstractPhaseInterceptor<SoapMessage> {

	// 日志
	private Uog log = Uog.getInstanceOnSoap();

	public AuthIntercetpr() {
		// 指定该拦截器在哪个阶段被激发
		super(Phase.READ);
	}

	// 处理消息
	public void handleMessage(SoapMessage message) {
		String xml = UserContext.getXML();
		if (null == xml || xml.length() < 1) {
			SOAPException soapExc = new SOAPException("阁下不是合法用户，传输SOAP的XML格式不正确，内容：" + xml);
			throw new Fault(soapExc);
		}
		xml = xml.replaceAll("wsse:", "wsse_");
		SAXReader reader = null;
		try {
			reader = new SAXReader();
			String pre = "/soapenv:Envelope/soapenv:Header/wsse_Security/";
			Document document = reader.read(new ByteArrayInputStream(xml.getBytes()));
			Element usernameElement = (Element) document.getRootElement().selectObject(pre + "wsse_Username");
			Element passwordElement = (Element) document.getRootElement().selectObject(pre + "wsse_Password");
			Element timestampElement = (Element) document.getRootElement().selectObject(pre + "wsse_Timestamp");
			Element tokenElement = (Element) document.getRootElement().selectObject(pre + "wsse_Token");
			if (null == usernameElement || passwordElement == null || timestampElement == null || tokenElement == null) {
				log.info("SOAP消息头格式不对");
				throw new Fault(new SOAPException("SOAP消息头格式不对哦！"));
			}
			String userName = usernameElement.getText();
			if (userName == null || userName.trim().length() <= 0) {
				log.info("用户不存在，用户为空");
				SOAPException soapExc = new SOAPException("阁下不是合法用户，无登录用户名称！");
				throw new Fault(soapExc);
			}
			String passwordBase64 = passwordElement.getText();
			if (passwordBase64 == null || passwordBase64.trim().length() <= 0) {
				log.info("密码不存在，密码为空");
				SOAPException soapExc = new SOAPException("阁下不是合法用户，无登录用户密码！");
				throw new Fault(soapExc);
			}
			String timestampBase64 = timestampElement.getText();
			if (timestampBase64 == null || timestampBase64.trim().length() <= 0) {
				log.info("时间戳不存在，时间戳为空");
				SOAPException soapExc = new SOAPException("阁下不是合法用户，无登录时间戳！");
				throw new Fault(soapExc);
			}

			String token = tokenElement.getText();
			if (token == null || token.trim().length() <= 0) {
				log.info("无登录Token");
				SOAPException soapExc = new SOAPException("阁下不是合法用户，无登录Token！");
				throw new Fault(soapExc);
			}
			DlhUser user = CacheDlhUser.queryByUserName(userName.trim());
			if (user == null) {
				log.info("用户不存在,没有查询到，userName=" + userName);
				SOAPException soapExc = new SOAPException("阁下不是合法用户！");
				throw new Fault(soapExc);
			}
			// 根据私钥解密取得对称密钥
			String desPwd = RSAUtil.decryptByPrivate(user.getDlhPasswordPrivate(), passwordBase64);
			// 根据私钥解密取得时间戳
			String timestamp = RSAUtil.decryptByPrivate(user.getDlhPasswordPrivate(), timestampBase64);
			// 验证对称密钥的有效性
			String timestampInToken = ThreeDESUtil.decrypt(desPwd, token);

			try {
				Long time = Long.valueOf(timestamp.trim());
				// 超过10分钟的包，不接受，防止重试攻击
				if (System.currentTimeMillis() - time > 1000 * 60 * 10) {
					log.info("时间戳参数不正确，太久远了,timestamp=" + timestamp);
					SOAPException soapExc = new SOAPException("阁下不是合法用户，时间戳参数不正确，太久远了");
					throw new Fault(soapExc);
				}
			} catch (Exception ex) {
				log.info("时间戳参数不正确,timestamp=" + timestamp);
				SOAPException soapExc = new SOAPException("阁下不是合法用户，时间戳参数不正确，异常：" + ex.getMessage());
				throw new Fault(soapExc);
			}

			if (!timestamp.equalsIgnoreCase(timestampInToken)) {
				log.info("对称密钥验证不通过,token=" + token + ",timestamp=" + timestamp);
				SOAPException soapExc = new SOAPException("阁下不是合法用户,对称密钥验证不通过！");
				throw new Fault(soapExc);
			}
			user.setDlhPassword(desPwd);
			UserContext.setUser(user);
		} catch (Exception e) {
			log.error("AuthIntercetpr异常", e);
			throw new Fault(new SOAPException("SOAP消息格式不正确，异常信息：" + e.getMessage(), e));
		}
	}
}
