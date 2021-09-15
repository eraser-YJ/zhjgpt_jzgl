package com.jc.system.applog;

import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.GlobalContext;
import com.jc.system.applog.domain.Operlog;
import com.jc.system.applog.service.IOperlogService;
import com.jc.system.domain.User;
import com.jc.system.security.MenuCacheUtil;
import com.jc.system.util.OperLogUtils;
import com.jc.system.util.SysUserUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class LogAspect implements MethodInterceptor {
    @Autowired
    private IOperlogService operlogService;

    public LogAspect() {
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {

            Object var2 = null;
            String ip = "";
            String browser = "";
            String loginName = "";
            StopWatch var6 = new StopWatch();
            String var7 = "start";
            ActionLog var10;
            String var11;
            String var12;
            String var13;
            try {
                String var8 = GlobalContext.getProperty("audit.thresholdOff");
                if (var8 != null) {
                    var7 = var8;
                }
                var6.start();
                var2 = mi.proceed();
                var6.stop();
                Object[] var24 = mi.getArguments();
                Object[] var25 = var24;
                int var26 = var24.length;
                for (int var27 = 0; var27 < var26; ++var27) {
                    Object var28 = var25[var27];
                    if (var28 instanceof HttpServletRequest) {
                        ip = this.getIpAddr((HttpServletRequest) var28);
                        browser = this.getSysAndBrowser((HttpServletRequest) var28);
                        this.getMenuId((HttpServletRequest) var28);
                        this.getLoginUserName((HttpServletRequest) var28);
                    }
                }

                if (mi.getMethod().isAnnotationPresent(ActionLog.class) && "start".equals(var7)) {
                    var10 = mi.getMethod().getAnnotation(ActionLog.class);
                    var11 = var10.operateModelNm() == null ? "" : var10.operateModelNm();
                    var12 = var10.operateFuncNm() == null ? "" : var10.operateFuncNm();
                    var13 = var10.operateDescribe() == null ? "" : var10.operateDescribe();
                    String var29 = "";
                    String var16 = "";
                    StringBuffer var30 = new StringBuffer();
                    String var17 = SysUserUtils.getSession().getAttribute("screenSize") != null ? SysUserUtils.getSession().getAttribute("screenSize").toString() : "";
                    if (!"".equals(var17)) {
                        browser = browser + ",分辨率:" + var17;
                    }
                    String var19;
                    if (var2 != null && "error/unauthorized".equals(var2.toString())) {
                        var30.append(var2);
                        var29 = "异常";
                        var16 = "访问异常";
                    } else if (var12.indexOf("delete") != -1) {
                        var29 = "删除";
                        var30.append(var2);
                        var16 = "删除成功";
                    } else if (var12.indexOf("update") != -1) {
                        String var18 = "";
                        var19 = "";
                        var29 = "修改";
                        var30.append(var2);
                        var16 = "修改成功";
                        if (var24[0] instanceof Map) {
                            if (((Map) var24[0]).get("status") != null && ((Map) var24[0]).get("statusOld") != null) {
                                var18 = ((Map) var24[0]).get("status").toString();
                                var19 = ((Map) var24[0]).get("statusOld").toString();
                            }

                            if (!var18.equals(var19) && var18.equals(GlobalContext.USER_STATUS_0)) {
                                var29 = "解锁";
                                var16 = "解锁成功";
                            } else if (!var18.equals(var19) && var18.equals(GlobalContext.USER_STATUS_2)) {
                                var29 = "锁定";
                                var16 = "锁定成功";
                            }
                        }
                    } else if (var12.indexOf("Modify") != -1) {
                        var29 = "修改";
                        var30.append(var2);
                        var16 = "修改成功";
                    } else if (var12.indexOf("save") != -1) {
                        var29 = "添加";
                        var30.append(var2);
                        var16 = "添加成功";
                    } else if (var12.indexOf("view") != -1) {
                        var29 = "查看";
                        var16 = "查看成功";
                    } else {
                        var29 = "查询";
                        var16 = "查询成功";
                    }

                    User user = SysUserUtils.getUser();
                    if(user == null){
                        user = new User();
                    }
                    var19 = mi.getMethod().getName();
                    Operlog var20 = new Operlog();
                    var20.setCreateDate(DateUtils.getSysDate());
                    var20.setUserId(user.getId());
                    var20.setUniqueId(user.getCode());
                    var20.setLoginName(user.getLoginName());
                    var20.setDisplayName(user.getDisplayName());
                    var20.setIp(ip);
                    var20.setOperTime(var6.getTime() + "");
                    var20.setFunName(var19);
                    var20.setOperDesc(var13);
                    var20.setOperType(var29);
                    var20.setOperResultShow(var16);
                    var20.setModifyDate(DateUtils.getSysDate());
                    var20.setIsAdmin(String.valueOf(user.getIsAdmin()));
                    var20.setExtStr2(var11);
                    var20.setExtStr5(browser);
                    var20.setCreateUser(user.getId());
                    var20.setCreateUserDept(user.getDeptId());
                    String var21 = mi.getThis().getClass().getSimpleName().replace("Controller", "");
                    if (var19.indexOf("delete") != -1 || var19.indexOf("update") != -1 || var19.indexOf("save") != -1 || var19.indexOf("view") != -1) {
                        if (OperLogUtils.getOperObjMap(var21) != null) {
                            var20.setExtStr1(OperLogUtils.getOperObjMap(var21));
                        } else if (OperLogUtils.getOperObjsMap(var21) != null) {
                            var20.setExtStr1(OperLogUtils.getOperObjsMap(var21));
                        }

                        var20.setExtStr3(var21);
                        String var22 = GlobalContext.getProperty("subsystem.id");
                        if (var22 != null && !"".equals(var22)) {
                            var20.setExtStr4(var22);
                        }

                        var20.setOperResult(var30.toString());
                    }

                    this.operlogService.save(var20);
                    this.operlogService.monitorOperLog();
                }

                return var2;
            } catch (Exception var23) {
                if (mi.getMethod().isAnnotationPresent(ActionLog.class)) {
                    User var9 = SysUserUtils.getUser();
                    var10 = mi.getMethod().getAnnotation(ActionLog.class);
                    var11 = var10.operateModelNm() == null ? "" : var10.operateModelNm();
                    var12 = var10.operateDescribe() == null ? "" : var10.operateDescribe();
                    var13 = mi.getMethod().getName();
                    Operlog var14 = new Operlog();
                    var14.setLoginName(loginName);
                    if (var9 != null) {
                        var14.setUserId(var9.getId());
                        var14.setLoginName(var9.getLoginName());
                        var14.setDisplayName(var9.getDisplayName());
                        var14.setCreateUser(var9.getId());
                        var14.setCreateUserDept(var9.getDeptId());
                        var14.setIsAdmin(String.valueOf(var9.getIsAdmin()));
                    }
                    var14.setCreateDate(DateUtils.getSysDate());
                    var14.setIp(ip);
                    var14.setOperTime(var6.getTime() + "");
                    var14.setFunName(var13);
                    var14.setOperType("异常");
                    var14.setModifyDate(DateUtils.getSysDate());
                    var14.setExtStr5(browser);
                    var14.setOperDesc(var12);
                    var14.setExtStr2(var11);
                    var14.setOperResult(var23.getMessage());
                    var14.setOperResultShow("数据异常");
                    String var15 = GlobalContext.getProperty("subsystem.id");
                    if (var15 != null && !"".equals(var15)) {
                        var14.setExtStr4(var15);
                    }
                    this.operlogService.save(var14);
                    this.operlogService.monitorOperLog();
                }
                var23.printStackTrace();
                return var2;
            }

    }

    private String getMenuId(HttpServletRequest request) {
        Object var2 = request.getAttribute("menuId");
        if (var2 == null) {
            return "";
        } else {
            if (var2 != null && !"".equals(var2)) {
                MenuCacheUtil.clearMenu();
                MenuCacheUtil.setMenu("menuId", var2.toString());
            }

            return "";
        }
    }

    private String getLoginUserName(HttpServletRequest request) {
        Object var2 = request.getAttribute("username");
        if (var2 == null) {
            return "";
        } else {
            return var2 != null && !"".equals(var2) ? var2.toString() : "";
        }
    }

    private String getIpAddr(HttpServletRequest request) {
        String var2 = request.getHeader("x-forwarded-for");
        if (var2 == null || var2.length() == 0 || "unknown".equalsIgnoreCase(var2)) {
            var2 = request.getHeader("Proxy-Client-IP");
        }

        if (var2 == null || var2.length() == 0 || "unknown".equalsIgnoreCase(var2)) {
            var2 = request.getHeader("WL-Proxy-Client-IP");
        }

        if (var2 == null || var2.length() == 0 || "unknown".equalsIgnoreCase(var2)) {
            var2 = request.getRemoteAddr();
        }

        return var2;
    }

    private String getSysAndBrowser(HttpServletRequest request) {
        String var2 = request.getHeader("user-agent");
        String var3 = "";
        String var4 = "";
        if (var2 == null) {
            return "浏览器版本：" + var3 + ",系统版本：" + var4;
        } else {
            StringTokenizer var5;
            if (var2.indexOf("MSIE") != -1) {
                var5 = new StringTokenizer(var2, ";");
                var5.nextToken();
                var3 = var5.nextToken();
                var4 = var5.nextToken();
            } else if (var2.indexOf("Chrome") != -1) {
                var5 = new StringTokenizer(var2, "()");
                var5.nextToken();
                var4 = var5.nextToken();
                var5.nextToken();
                var5.nextToken();
                var3 = var5.nextToken();
            }

            return "浏览器版本：" + var3 + ",系统版本：" + var4;
        }
    }
}
