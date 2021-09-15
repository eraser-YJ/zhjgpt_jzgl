package org.apache.shiro.cas;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.foundation.util.StringUtil;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Menu;
import com.jc.system.security.domain.Principal;
import com.jc.system.security.domain.User;
import com.jc.system.security.domain.UserExt;
import com.jc.system.security.service.IMenuService;
import com.jc.system.security.service.ISystemService;
import com.jc.system.security.service.IUserExtService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
public class CasRealm extends AuthorizingRealm
{
    public static final String DEFAULT_REMEMBER_ME_ATTRIBUTE_NAME = "longTermAuthenticationRequestTokenUsed";
    public static final String DEFAULT_VALIDATION_PROTOCOL = "CAS";
    private static Logger logger = LoggerFactory.getLogger(CasRealm.class);
    private String casServerUrlPrefix;
    private String casService;
    private String validationProtocol = "CAS";

    private String rememberMeAttributeName = "longTermAuthenticationRequestTokenUsed";
    private TicketValidator ticketValidator;
    private String defaultRoles;
    private String defaultPermissions;
    private String roleAttributeNames;
    private String permissionAttributeNames;

    private ISystemService systemService;
    private IMenuService menuService;
    private IUserExtService userExtService;

    public CasRealm()
    {
        setAuthenticationTokenClass(CasToken.class);
    }

    @Override
    protected void onInit()
    {
        super.onInit();
        ensureTicketValidator();
    }

    protected TicketValidator ensureTicketValidator() {
        if (this.ticketValidator == null) {
            this.ticketValidator = createTicketValidator();
        }
        return this.ticketValidator;
    }

    protected TicketValidator createTicketValidator() {
        String urlPrefix = getCasServerUrlPrefix();
        if ("saml".equalsIgnoreCase(getValidationProtocol())) {
            return new Saml11TicketValidator(urlPrefix);
        }
        return new Cas20ServiceTicketValidator(urlPrefix);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException
    {
        CasProperties casProperties = CasProperties.getInstance();
        this.setCasServerUrlPrefix(GlobalContext.getProperty("cas.casServerUrl"));
        this.setCasService(casProperties.getLocalUrl()+"/login");
        CasToken casToken = (CasToken)token;
        if (token == null) {
            return null;
        }

        String ticket = (String)casToken.getCredentials();
        if (!StringUtils.hasText(ticket)) {
            return null;
        }

        //TicketValidator ticketValidator = ensureTicketValidator();
        Cas20ServiceTicketValidator ticketValidator = (Cas20ServiceTicketValidator) ensureTicketValidator();
        try
        {
            ticketValidator.setEncoding("UTF-8");
            Assertion casAssertion = ticketValidator.validate(ticket, getCasService());

            AttributePrincipal casPrincipal = casAssertion.getPrincipal();
            String userId = casPrincipal.getName();
            logger.debug("Validate ticket : {} in CAS server : {} to retrieve user : {}", new Object[] { ticket, getCasServerUrlPrefix(), userId });

            Map<String,Object> attributes = casPrincipal.getAttributes();

            casToken.setUserId(userId);
            String rememberMeAttributeName = getRememberMeAttributeName();
            String rememberMeStringValue = (String)attributes.get(rememberMeAttributeName);
            boolean isRemembered = (rememberMeStringValue != null) && (Boolean.parseBoolean(rememberMeStringValue));
            if (isRemembered) {
                casToken.setRememberMe(true);
            }

            CollectionUtils.asList(new Object[] { userId, attributes });
            User user = getSystemService().get(userId);
//      byte[] salt = Encodes.decodeHex(user.getPassword().substring(0,16));

            //添加样式
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

            Principal principal = new Principal(user);
            principal.setIp(String.valueOf(user.getId()));
            principal.setServiceTicket(ticket);
            if(!GlobalContext.isSysCenter() &&  "true".equals(GlobalContext.getProperty("cas.start")) && !StringUtil.isEmpty(GlobalContext.getProperty("api.dataServer"))){
            	if(!StringUtil.isEmpty(principal.getPhoto())){
            		principal.setPhoto(GlobalContext.getProperty("api.dataServer") + "/" + principal.getPhoto());
            	}
            }
            PrincipalCollection principalCollection = new SimplePrincipalCollection(principal, userId);
            addSessionOper(user);
            return new SimpleAuthenticationInfo(principalCollection, ticket);
        } catch (TicketValidationException e) {
            throw new CasAuthenticationException("Unable to validate ticket [" + ticket + "]", e);
        } catch (Exception ec){
            logger.error(ec.toString());
            throw new CasAuthenticationException("error message :",ec);
        }
    }

    public void addSessionOper(User user){

    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
    {
        List<Menu> menuList = null;
        if(SystemSecurityUtils.getUser().getIsSystemAdmin()){
            try {
                Menu menu = new Menu();
                menu.setDeleteFlag(0);
                menuList = getMenuService().queryAll(menu);
            } catch (CustomException e) {
                logger.error(e.toString());
            }
        } else {
            menuList = SystemSecurityUtils.getMenuList();
        }
        if (menuList != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            for (Menu menu : menuList) {
                if (menu != null && org.apache.commons.lang3.StringUtils.isNotEmpty(menu.getPermission()) && !"".equals(menu.getPermission().trim())) {
                    // 添加基于Permission的权限信息
                    info.addStringPermission(menu.getPermission().toString());
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
        SimplePrincipalCollection principals = new SimplePrincipalCollection(
                principal, getName());
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

    @SuppressWarnings("unused")
    private void addRoles(SimpleAuthorizationInfo simpleAuthorizationInfo, List<String> roles)
    {
        for (String role : roles) {
            simpleAuthorizationInfo.addRole(role);
        }
    }

    @SuppressWarnings("unused")
    private void addPermissions(SimpleAuthorizationInfo simpleAuthorizationInfo, List<String> permissions)
    {
        for (String permission : permissions) {
            simpleAuthorizationInfo.addStringPermission(permission);
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

    public String getCasServerUrlPrefix()
    {
        return this.casServerUrlPrefix;
    }

    public void setCasServerUrlPrefix(String casServerUrlPrefix) {
        this.casServerUrlPrefix = casServerUrlPrefix;
    }

    public String getCasService() {
        return this.casService;
    }

    public void setCasService(String casService) {
        this.casService = casService;
    }

    public String getValidationProtocol() {
        return this.validationProtocol;
    }

    public void setValidationProtocol(String validationProtocol) {
        this.validationProtocol = validationProtocol;
    }

    public String getRememberMeAttributeName() {
        return this.rememberMeAttributeName;
    }

    public void setRememberMeAttributeName(String rememberMeAttributeName) {
        this.rememberMeAttributeName = rememberMeAttributeName;
    }

    public String getDefaultRoles() {
        return this.defaultRoles;
    }

    public void setDefaultRoles(String defaultRoles) {
        this.defaultRoles = defaultRoles;
    }

    public String getDefaultPermissions() {
        return this.defaultPermissions;
    }

    public void setDefaultPermissions(String defaultPermissions) {
        this.defaultPermissions = defaultPermissions;
    }

    public String getRoleAttributeNames() {
        return this.roleAttributeNames;
    }

    public void setRoleAttributeNames(String roleAttributeNames) {
        this.roleAttributeNames = roleAttributeNames;
    }

    public String getPermissionAttributeNames() {
        return this.permissionAttributeNames;
    }

    public void setPermissionAttributeNames(String permissionAttributeNames) {
        this.permissionAttributeNames = permissionAttributeNames;
    }
}