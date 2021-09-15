package com.jc.system.security.check;

import java.util.List;

import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.UserToken;
import com.jc.system.security.domain.User;
import com.jc.system.security.exception.OnlineCountException;
import com.jc.system.security.license.UKBean;
import com.jc.system.security.license.VerificationSession;

/**
 * 判断并发人数
 * @author Administrator
 *  * @date 2020-07-01
 */
public class CheckForOnlineCount extends CheckVal{

	@Override
	public void check(User user,UserToken token) throws Exception {
		/*if(VerificationSession.a() != null){
			List licenseList = VerificationSession.a();
			if(licenseList.size() > 0){
				UKBean ukbean = (UKBean) licenseList.get(0);
				if(SystemSecurityUtils.getOnlineCountForLicense(token.getHost()) >= Integer.parseInt(ukbean.e())){
					throw new OnlineCountException();
				}
			}
		}*/
	}

}
