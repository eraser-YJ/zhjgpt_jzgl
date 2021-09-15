package com.jc.system.security;

import com.jc.crypto.utils.SM2Utils;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.RsaUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.common.util.Encodes;
import com.jc.system.security.check.CheckFactory;
import com.jc.system.security.domain.*;
import com.jc.system.security.exception.*;
import com.jc.system.security.service.IMenuService;
import com.jc.system.security.service.ISystemService;
import com.jc.system.security.service.IUserExtService;
import com.jc.system.security.service.IUserService;
import com.jc.system.security.utils.RSASetting;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class SystemAuthorizingRealm extends AuthorizingRealm {
	protected transient final Logger logger = Logger.getLogger(this.getClass());
	private ISystemService systemService;
	private IUserService userService;
	private IMenuService menuService;
	private IUserExtService userExtService;

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;

	/**
	 * 认证回调函数, 登录时调用
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		try{
			UserToken token = (UserToken) authcToken;
			if(token.isThirdly()){
				SM2Utils.generateKeyPair();
				User user = token.getLoginUser();
				token.setPassword(GlobalContext.PASSWORD_DEFAULT_VALUE.toCharArray());
				user.setPassword(SystemSecurityUtils.entryptPassword(GlobalContext.PASSWORD_DEFAULT_VALUE, SM2Utils.getPubKey()));
				byte[] salt = SystemSecurityUtils.getSalt(user.getPassword(), SM2Utils.getPriKey());
				Principal principal = new Principal(user);
				principal.setIp(token.getHost());
				return new SimpleAuthenticationInfo(principal, SystemSecurityUtils.getShaPassword(user.getPassword(), SM2Utils.getPriKey()), ByteSource.Util.bytes(salt), getName());
			}
			token.setPassword(RSASetting.decryptByPrivateKey(new String(token.getPassword())).toCharArray());
			User user = getSystemService().get(token.getUsername());
			if (user != null) {
				CheckFactory.check(user, token);
				Department department = new Department();
				department.setId(user.getDeptId());
				department = systemService.queryOrgIdAndName(department);
				user.setOrgId(department.getOrgId());
				try {
					UserExt userExt = new UserExt();
					userExt.setUserId(user.getId());
					List<UserExt> userExtList = getUserExtService().queryAll(userExt);
					for (UserExt tempvo : userExtList){
						if ("color".equals(tempvo.getCssType())){
							user.setColor(tempvo.getCssValue());
						} else if ("font".equals(tempvo.getCssType())){
							user.setFont(tempvo.getCssValue());
						}
					}

					byte[] salt = SystemSecurityUtils.getSalt(user.getPassword(),user.getKeyCode());
					Principal principal = new Principal(user);
					principal.setIp(token.getHost());
					return new SimpleAuthenticationInfo(principal, SystemSecurityUtils.getShaPassword(user.getPassword(),user.getKeyCode()), ByteSource.Util.bytes(salt), getName());
				} catch(Exception e){
					logger.error(e,e.getCause());
					throw new AuthenticationException(e);
				}
			} else {
				return null;
			}
		} catch (OnlineCountException e) {
			throw new OnlineCountException();
		} catch (UserDisabledException e) {
			throw new UserDisabledException();
		} catch (UserIpException e) {
			throw new UserIpException();
		} catch (UserPasswordException e) {
			throw new UserPasswordException(e.getMessage());
		} catch (UnknownAccountException e) {
			throw new UnknownAccountException();
		} catch (UserLockedException e) {
			throw new UserLockedException();
		} catch (AuthenticationException e) {
			throw new AuthenticationException(e);
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		List<Menu> menuList = null;
		if(SystemSecurityUtils.getUser().getIsSystemAdmin()){
			try {
				Menu menu = new Menu();
				menu.setDeleteFlag(0);
				menuList = getMenuService().queryAll(menu);
			} catch (CustomException e) {
				logger.error(e);
			}
		} else {
			menuList = SystemSecurityUtils.getMenuList();
		}
		if (menuList != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			for (Menu menu : menuList) {
				if (menu != null && StringUtils.isNotEmpty(menu.getPermission())) {
					info.addStringPermission(menu.getPermission());
				}
			}
			return info;
		} else {
			return null;
		}
	}

	/**
	 * 清空用户关联权限认证，待下次使用时重新加载
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清空所有关联认证
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

	/**
	 * 设定密码校验的Hash算法与迭代次数
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(HASH_ALGORITHM);
		matcher.setHashIterations(HASH_INTERATIONS);
		setCredentialsMatcher(matcher);
	}

	private static final String OR_OPERATOR = " or ";
	private static final String AND_OPERATOR = " and ";
	private static final String NOT_OPERATOR = "not ";

	/**
	 * 支持or and not 关键词  不支持and or混用
	 *
	 * @param principals
	 * @param permission
	 * @return
	 */
	@Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
		if (permission.contains(OR_OPERATOR)) {
			String[] permissions = permission.split(OR_OPERATOR);
			for (String orPermission : permissions) {
				if (isPermittedWithNotOperator(principals, orPermission)) {
					return true;
				}
			}
			return false;
		} else if (permission.contains(AND_OPERATOR)) {
			String[] permissions = permission.split(AND_OPERATOR);
			for (String orPermission : permissions) {
				if (!isPermittedWithNotOperator(principals, orPermission)) {
					return false;
				}
			}
			return true;
		} else {
			return isPermittedWithNotOperator(principals, permission);
		}
	}

	private boolean isPermittedWithNotOperator(PrincipalCollection principals, String permission) {
		if (permission.startsWith(NOT_OPERATOR)) {
			return !super.isPermitted(principals, permission.substring(NOT_OPERATOR.length()));
		} else {
			return super.isPermitted(principals, permission);
		}
	}

	/**
	 * 获取系统业务对象
	 */
	public ISystemService getSystemService() {
		if (systemService == null) {
			systemService = SpringContextHolder.getBean(ISystemService.class);
		}
		return systemService;
	}

	/**
	 * 获取系统业务对象
	 */
	public IUserService getUserService() {
		if (userService == null) {
			userService = SpringContextHolder.getBean(IUserService.class);
		}
		return userService;
	}

	/**
	 * 获取系统业务对象
	 */
	public IMenuService getMenuService() {
		if (menuService == null) {
			menuService = SpringContextHolder.getBean(IMenuService.class);
		}
		return menuService;
	}

	/**
	 * 获取系统业务对象
	 */
	public IUserExtService getUserExtService() {
		if (userExtService == null) {
			userExtService = SpringContextHolder.getBean(IUserExtService.class);
		}
		return userExtService;
	}

}
