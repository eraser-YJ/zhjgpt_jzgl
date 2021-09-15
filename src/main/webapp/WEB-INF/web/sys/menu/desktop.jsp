<%@ page language="java" pageEncoding="UTF-8" import="com.jc.foundation.util.GlobalContext" %>
<!DOCTYPE html>
<html class="jcGOA">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8">
    <meta charset="utf-8">
    <title><%=GlobalContext.getProperty("title")%></title>
    <%@ include file="/WEB-INF/web/include/taglib.jsp"%>
    <link rel="shortcut icon" href="${sysPath}/favicon.ico" />
    <link href="${sysPath}/css/font-face.css" rel="stylesheet" type="text/css"/>
    <%--<link href="${sysPath}/css/main.css" rel="stylesheet" type="text/css"/>--%>
    <link href="${sysPath}/css/yx_frame.css" rel="stylesheet" type="text/css"/>


    <script type="text/javascript" src='${sysPath}/js/jquery.min.js?v=517bc0d319'></script>
    <script type="text/javascript" src='${sysPath}/js/lib/jquery/jquery.cookie.js?v=a79ce0f6ee'></script>
    <script type="text/javascript" src="${sysPath}/js/jbox/jbox.min.js?v=ce217626b3"></script>
    <script src="${sysPath}/js/lib/common/navigationbar_2.5.js?v=8ec9204c8a" type="text/javascript"></script>
    <script type="text/javascript" src="${sysPath}/js/slimscroll/silmScroll.js"></script>
    <script type="text/javascript">setRootPath('${sysPath}');</script>
    <script type="text/javascript">setTheme('${theme}');</script>
    <script type="text/javascript">setManageHostPath('<%=pageContext.getAttribute("managerHostPath")%>');</script>
    <script type="text/javascript" src='${sysPath}/js/lib/datatable/jquery.dataTables.all.js?v=71e965f0c6'></script>
    <script type="text/javascript" src="${sysPath}/js/app.v2.js?v=e975657b0c"></script>
    <script src='${sysPath}/js/lib/common/showOnlinePerson.js?v=b04775f310' type='text/javascript'></script>
    <!-- Bootstrap -->
    <!--[if lt IE 9]>
    <script src="${sysPath}/js/lib/app/html5pie.min.js?v=758616407d"></script>
    <![endif]-->
    <script>window.onerror = function() {$("#dataLoad").hide();};</script>
</head>
<body ondragstart="return false" id="desktopBody">
<section class="jcGOA-wrap">
    <header class="header navbar-fixed-top" id="desktop_header">
        <%--<div class="navbar-topbar clearfix">--%>
            <%--<input type="hidden" id="urlParameter" name="urlParameter" value=""/>--%>
            <%--<!-- 桌面精灵使用勿动 开始 -->--%>
            <%--<!-- 页面读取完成标志-->--%>
            <%--<input id="loadOver" type="hidden" value="0"/>--%>
            <%--<!-- 未读邮件-->--%>
            <%--<a id="15e880df-f229-47a9-9a44-8f9eaa969f6e" name="15e880df-f229-47a9-9a44-8f9eaa969f6e" hidden="hidden" href="javascript:void(0)" onClick="loadrightmenu('/ic/mail/manageUnread.action','','/ic/mail/manageUnread.action');"></a>--%>
            <%--<!-- 待办发文-->--%>
            <%--<a id="31e029e0-4599-4696-bd9c-f3290a8cf6eb" name="31e029e0-4599-4696-bd9c-f3290a8cf6eb" hidden="hidden"  href="javascript:void(0)" onClick="menuClick('2084','/workFlow/form/jumpToList.action?url=/doc/send/sendWaitTransact',2,'待办发文')"></a>--%>
            <%--<!-- 待办收文-->--%>
            <%--<a id="384a1ce1-132f-4856-b3ff-885f1c3ee7d9" name="384a1ce1-132f-4856-b3ff-885f1c3ee7d9" hidden="hidden"  href="javascript:void(0)" onClick="menuClick('2089','/workFlow/form/jumpToList.action?url=/doc/receive/receiveToDoList',2,'待办收文')"></a>--%>
            <%--<!-- 待办流程-->--%>
            <%--<a id="3d18eabf-3ee3-4cce-a932-57319c9e8d26" name="3d18eabf-3ee3-4cce-a932-57319c9e8d26" hidden="hidden"  href="javascript:void(0)" onClick="loadrightmenu('/workFlow/processInstance/getTodoList.action','','/workFlow/processInstance/getTodoList.action');"></a>--%>
            <%--<!-- 桌面精灵使用勿动 结束-->--%>
            <%--<div class="m-t-xs fr dropdown" id="dropdownMenu">--%>
                <%--<a href="javascript:void(0)" class="icon-help m-r" onclick="JCF.LoadPage({url  : '/sys/user/userPwd.action'});">密码</a>--%>
                <%--<a href="javascript:void(0)" onClick="exit()" class="icon-exit">退出</a>--%>
            <%--</div>--%>
        <%--</div>--%>


        <nav class="header-nav fl">
            <ul class="clearfix" id="navigation-header">
                 <%--<li><h1><img src="${sysPath}/css/login/images/logon.png" style="width:280px;" onClick="window.location.reload();"/></h1></li>--%>

                <li><h1><img src="${sysPath}/images/logo8.png" onClick="window.location.reload();"/></h1></li>
                <c:forEach items="${menuList}" var="menu">
                    <li id="header_${menu.id}">
                        <a id="${menu.id}" name="${menu.name}" action="${menu.actionName}" potal="${menu.permission}" data-head="menu">
                            <%--<i class="${menu.icon}"></i>${menu.name}--%>
                            <img src="${sysPath}/images/menu/${menu.icon}" class="header-icon"/>${menu.name}
                        </a>
                    </li>
                </c:forEach>
                <li id="topnav-other_modules" class="no-active">
                    <a href="#">
                       <%--<i class="topnav-other_modules"></i>--%>
                       <img src="${sysPath}/images/nav-other-icon.png" class="header-icon"/>其他模块
                    </a>
                </li>
            </ul>
        </nav>
        <section class="userInfo m-r-sm fr clearfix">

            <span style="float:left; display: inline-block; line-height: 60px;text-align: center;width: 60px;cursor:pointer;" onclick="JCF.LoadPage({url: '/message/info/manage.action'});"><i class="fa fa-mail"></i> 消息</span>
            <span style="float:left;display: inline-block; line-height: 60px;text-align: center;width: 60px; margin-right: 15px;cursor:pointer;" onclick="JCF.LoadPage({url: '/workflow/todoList.action'});"><i class="fa fa-audit"></i> 待办</span>
            <div class="user-avatar fl tc">
                <a href="javascript:void(0);" onClick="JCF.LoadPage({name : '个人信息' , url : '/sys/user/userInfo.action'});">
                    <%--<img src="${sysPath}/content/attach/originalPath.action?url=<shiro:principal property="photo"/>" />--%>
                    <img src="${sysPath}/images/photo-up.png" />
                </a>
            </div>
            <div class="user-info fr">

                <div class="clearfix name">
                    <p class="fl inter">
                        <a href="#" style="cursor:pointer;" title="<shiro:principal property="displayName"/><shiro:principal property="dutyIdValue"/>">
                            <span><shiro:principal property="displayName" /><shiro:principal property="dutyIdValue" /></span>
                        </a>
                    </p>
                </div>
           <%--<i class="fa fa-user"></i>--%>
            </div>
            <span class="userInfo-pull-right" id="toggo-icon"><i class="fa fa-angle-down" ></i></span>
            <span class="userInfo-pull-right toggo-active" id="toggo-active"><i class="fa fa-angle-up"></i></span>
            <%--<div>--%>
                <%--<div class="m-t-xs fr dropdown" id="dropdownMenu">--%>
                     <%--<a href="javascript:void(0)" class="icon-help m-r" onclick="JCF.LoadPage({url  : '/sys/user/userPwd.action'});">密码</a>--%>
                     <%--<a href="javascript:void(0)" onClick="exit()" class="icon-exit">退出</a>--%>
                <%--</div>--%>
            <%--</div>--%>
            <div class="m-t-xs fr dropdown" id="dropdownMenu">
                <a href="javascript:void(0)" class="icon-help m-r" onclick="JCF.LoadPage({url  : '/sys/user/userPwd.action'});">密码</a>
                <a href="javascript:void(0)" onClick="exit()" class="icon-exit">退出</a>
             </div>
        </section>

    </header>
    <section>
        <section class="jcGOA-con">
            <aside class="aside-md bg-light" id="nav"  >
                <section class="jcGOA-wrap jcGOA-menu" style="margin-top:60px;" id="jcGOA-menu">
                    <header class="menu-header tc clearfix" style="margin-top:70px;">
                        <div class="clearfix">
                            <a href="#nav" data-toggle="class:nav-xs" class="fr menu-toggle" data-tip="menu">
                                <i data-toggle="tooltip" data-placement="top" data-container="body" data-original-title="隐藏菜单" class="fa fa-align-justify  fa-icon" id="fa-align-justify"></i>
                            </a>
                        </div>
                    </header>
                    <section class="menu">
                        <div id="slim-scroll">
                            <nav class="nav-primary hidden-xs nav-back">
                                <ul class="nav" id="JCLeftMenu"></ul>
                            </nav>
                        </div>
                    </section>
                </section>
            </aside>
            <section style="width:100%;"><iframe id="mainFrame" name="mainFrame" src="" style="overflow:visible;" scrolling="yes" frameborder="no" width="100%" height="100%"></iframe></section>
        </section>
        <div class="messageTip messageTip-1" style="z-index: 800;display:none">
            <div class="messageHeader clearfix"><h4 class="fl"><i class="fa fa-chat m-r-xs"></i>消息提醒</h4><button type="button" class="close m-t-sm m-r-sm messageTip-close">×</button></div>
            <ul class="messageList"></ul>
            <div class="message-btm"><a href="javascript:void(0)" onClick="JCF.LoadPage({ url : '/sys/noticeMsg/manage.action'})" class="fr m-r"><i class="fa fa-caret-right m-r-xs"></i>更多</a></div>
        </div>
    </section>
</section>
<div class="nav-xs-dd bg-light"  id="nav-menu-tie">
    <div id="nav-menu-con" class="nav-primary  nav-dis"></div>
</div>

<!-- 强制修改密码DIV start -->
<div id="duressPasswordDiv">
    <div class="modal fade panel" id="dPasswordModal" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header"><h4 class="modal-title">修改密码</h4></div>
                 <div class="modal-body">
                	<form class="table-wrap form-table" id="userPwdForm">
                        <input type="hidden" id="token" name="token" value="${token}">
                        <div class="m-b-sm "><span class="required">为了您的安全请修改密码</span></div>
                        <table class="table table-td-striped">
                            <tbody>
                                <tr>
                                    <td class="w140"><span class="required">*</span>旧密码</td>
                                    <td><input type="password" id="password" name="password" style="width:60%;"/></td>
                                </tr>
                                <tr>
                                    <td><span class="required">*</span>新密码</td>
                                    <td><input type="password" id="newPassword" name="newPassword" style="width:60%;"/></td>
                                </tr>
                                <tr>
                                    <td><span class="required">*</span>确认密码</td>
                                    <td><input type="password" id="confirmPassword" name="confirmPassword" style="width:60%;"/></td>
                                </tr>
                            </tbody>
                        </table>
                    </form>
                </div>
                <div class="modal-footer no-all form-btn"><button class="btn dark" type="submit" id="saveBtn">确 定</button></div>
            </div>
        </div>
    </div>
</div>
<!-- 强制修改密码DIV end -->

<!-- 授权校验DIV start -->
<div id="linceseDiv">
	<div class="modal fade panel" id="dLinceseModal" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header"><h4 class="modal-title">授权提醒</h4></div>
                <div class="modal-body"><table class="table table-td-striped"><tbody><tr><td style="text-align: center;"><span class="required" id="licenseMes"></span></td></tr></tbody></table></div>
                <div class="modal-footer no-all form-btn"><button class="btn dark" type="button" onclick="$('#dLinceseModal').modal('hide');">确 定</button></div>
            </div>
        </div>
    </div>
</div>
<!-- 授权校验DIV end -->
<div id="voiceDiv" style="height:0;"></div>
<script type='text/javascript' src='${sysPath}/js/lib/common/jquery.utils.js?v=7b46332a61'></script>
<script type="text/javascript" src='${sysPath}/js/lib/jquery-validation/jquery.validate.all.js?v=b58d900620'></script>
<script src="${sysPath}/js/com/sys/user/userPwd.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/user/userPwd.validate.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/user/duressPassword.js"></script>
<script src="${sysPath}/js/lib/common/license.js"></script>
    <%--<script src="https://upcdn.b0.upaiyun.com/libs/jquery/jquery-2.0.2.min.js">--%>
    <%--</script>--%>
<script>
    var subsystemMap = ${subsystemMap};
    function getSubsystemUrl(action) {
        var reg = new RegExp("sys=([^&?]*)", "ig");
        var r = action.match(reg);
        if (r != null) {
            var subsystemId = decodeURI(r[0].substr(4,r[0].length));
            for (var i = 0; i < subsystemMap.length; i++) {
                var subsystem = subsystemMap[i][subsystemId];
                if(subsystem){
                    var menuUrl = subsystem + action;
                    menuUrl += (action.indexOf("?") == -1) ? "?login-at=true" : "&login-at=true";
                    return menuUrl;
                }
            }
        }
        return getRootPath() + action;
    }

    function goDefaultMenu() {
        if ('${pageid}') {
            JCF.LoadPage({ url  : '${openUrl}' });
            JCF.histObj.sidemenu.goActivity('${pageid}');
        } else if ('${portalurl}') {
            JCF.LoadPage({ url  : '${portalurl}' });
        }
    }

    function goMenuByToUrl(toUrl) {
        if (toUrl != null) {
            if (document.getElementById(toUrl) != null) {
                document.getElementById(toUrl).click();
            } else {
                //工作流
                if (toUrl.indexOf("todoListHandle,") > -1) {
                    var array = toUrl.split(',');
                    try {
                        var menuIdArray = array[1].split('-');
                        for (var i = 0; i < menuIdArray.length; i++) {
                            document.getElementById(menuIdArray[i]).click();
                        }
                    } catch (e) {}
                    footerToolsModule.returnHandleUrl(array[2], function(url) { JCF.LoadPage({url: url}); });
                } else {
                    JCF.LoadPage({url: toUrl});
                }
            }
        }
    }

    JCFF.$document.ready(function(){
        var historyUrl = new Array();
        var i18nData = {};
        if(!JCF.histObj){
            JCF.histObj = new JCF.Historys();
        }
        $('#loadOver').attr('value', '1');
        //左菜单初始化
        JCF.histObj.sidemenu = new JCF.SideMenu('#JCLeftMenu');
        JCF.histObj.sidemenu.setData('${menuJsonStr}');
        //点击导航事件
        JCFF.$document.on('click' , '[data-head="menu"]' , function(e){
            var $this = $(e.target);
            if(!$this.is('a')) $this = $this.parent();
            var actcion = $this.attr('action'), id  = $this.attr('id'), isRootPotal = $this.attr('potal');
            JCF.navigation(id);
            JCF.histObj.sidemenu.headMenu = null;
            JCF.histObj.sidemenu.drawLeft(id);
            if(actcion){
                JCF.LoadPage({ id : id, name : $this.attr('name'), url  : actcion.indexOf('?') > -1 ? actcion + '&menuId='+id : actcion + '?menuId='+id, } , true);
            }else{
                if(isRootPotal){ goDefaultMenu(); }
            }
        });
        //初始化其他模块
        $("#topnav-other_modules").other();
        goDefaultMenu();
        goMenuByToUrl('${toUrl}')
        $(document).on("keydown", function(e) {
            if (e.keyCode == 8 && e.target.tagName != "INPUT" && e.target.tagName != "TEXTAREA")
                return false;
        });

        $("div#nav-menu-con").on("click","ul li",function(){
           $('#nav-menu-con').hide();
        });
    });

    $(document).ready(function(){
        $("#toggo-icon").click(function(){
            $("#dropdownMenu").show();
            $("#toggo-icon").css("display","none");
            $(".toggo-active").css("display","block");
        });

        $("#toggo-active").click(function(){
            $("#dropdownMenu").hide();
            $(".toggo-active").css("display","none");
            $("#toggo-icon").css("display","block");
        });
    });
</script>
<script src='${sysPath}/js/com/jc/common/footer-tools.js'></script>
</body>
</html>
