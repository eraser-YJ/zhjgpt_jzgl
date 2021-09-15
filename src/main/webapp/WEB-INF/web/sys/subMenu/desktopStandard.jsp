<%@ page import="com.jc.foundation.util.GlobalContext" %>
<%@ page language="java" pageEncoding="UTF-8"%>
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
    <link href="${sysPath}/css/${sessionScope.theme}/${sessionScope.font}/${sessionScope.color}/jcap.css?v=7b89ce1005" rel="stylesheet" type="text/css"/>

    <script type="text/javascript" src='${sysPath}/js/jquery.min.js?v=517bc0d319'></script>
    <script type="text/javascript" src='${sysPath}/js/lib/jquery/jquery.cookie.js?v=a79ce0f6ee'></script>
    <script type="text/javascript" src="${sysPath}/js/jbox/jbox.min.js?v=ce217626b3"></script>

    <script src="${sysPath}/js/lib/common/navigationbar.js?v=8ec9204c8a" type="text/javascript"></script>

    <script type="text/javascript" src="${sysPath}/js/slimscroll/silmScroll.js"></script>
    <script type="text/javascript" src="${sysPath}/js/com/sys/subMenu/subMenu.js"></script>

    <script type="text/javascript">setRootPath('${sysPath}');</script>
    <script type="text/javascript">setTheme('${theme}');</script>
    <script type="text/javascript">setManageHostPath('<%=pageContext.getAttribute("managerHostPath")%>');</script>

    <script type="text/javascript" src='${sysPath}/js/lib/datatable/jquery.dataTables.all.js?v=71e965f0c6'></script>
    <script type="text/javascript" src="${sysPath}/js/app.v2.js?v=e975657b0c"></script>
    <script src='${sysPath}/js/lib/common/showOnlinePerson.js?v=b04775f310' type='text/javascript'></script>
    <script src="${sysPath}/js/com/sys/user/duressPassword.js"></script>
    <!--[if lt IE 9]>
        <script src="${sysPath}/js/ie/html5shiv.js?v=758616407d"></script>
        <script type="text/javascript" src="${sysPath}/js/ie/PIE_IE678.js"></script>
    <![endif]-->
    <script>window.onerror = function() {$("#dataLoad").hide();};</script>
    <style>
        .subUserLink{
            margin-right: 10px;
            padding: 3px 5px;
        }
        .subUserLink.active{
            background: #fb6b5b;
        }
    </style>
</head>
<body ondragstart="return false" id="desktopBody">
<input type="hidden" id="urlParameter" name="urlParameter" value=""/>
<section class="jcGOA-wrap">
    <header class="header navbar-fixed-top" id="desktop_header">
        <!--LOGO-->
        <h1><a href="javascript:void(0)"><img src="${sysPath}/images/logo.png?v=988e75263f" onClick="window.location.reload();" height="60"/></a></h1>
        <!--全屏展开按钮-->
        <a href="javascript:void(0)" class="full-f11 m-t-md" id=""><i class="fa fa-enlarge2 text"></i><i class="fa fa-shrink2 text-active"></i></a>
        <!--右侧功能区-->
        <section class="userInfo m-r-sm fr clearfix">
            <div class="user-avatar fr" id="user-avatar">
                <div class="user-avatar-img fl">
                    <a href="#">
                        <img src="${sysPath}/<shiro:principal property="photo"/>" height="40"/>
                    </a>
                </div>
                <span class="fl sort"><i class="fa fa-sort-down"></i></span>
                <div class="h-nav-list" id="h-nav-list">
                    <!--<shiro:principal property="dutyIdValue" />-->
                    <div class="h-nav-top"></div>
                    <div class="h-nav-center">
                        <h2>欢迎您，<shiro:principal property="displayName" /></h2>
                        <h3 id="onlineCount">当前有<span id="lineCount">0</span>人在线</h3>
                        <ul>
                            <li>
                                <span><i class="fa fa-skin rounded"></i></span>
                                <div class="h-div-first">
                                    <a href="javascript:void(0)" class="h-nav-red rounded" name="red" types="color"></a>
                                    <a href="javascript:void(0)" class="h-nav-blue rounded" name="blue" types="color"></a>
                                    <a href="javascript:void(0)" class="h-nav-green rounded" name="green" types="color"></a>
                                </div>
                            </li>
                            <li>
                                <span><i class="fa fa-skin rounded"></i></span>
                                <div>
                                    <button class="button-lg rounded" name="classics" types="theme">经典</button>
                                    <button class="button-lg rounded" name="standard" types="theme">标准</button>
                                    <%--<button class="button-lg rounded" name="concise" type="theme">简约</button>--%>
                                </div>
                            </li>
                            <li>
                                <span><i class="fa fa-tumblr rounded"></i></span>
                                <div>
                                    <button class="button-lg rounded" name="standard" types="font">标准</button>
                                    <button class="button-lg rounded" name="big" types="font">大</button>
                                </div>
                            </li>
                            <li>
                                <span><i class="fa fa-volume-off rounded"></i></span>
                                <div>
                                    <button class="button-lg rounded" types="voice" name="on">有声</button>
                                    <button class="button-lg rounded" types="voice" name="off">静音</button>
                                </div>
                            </li>
                            <li>
                                <span><i class="fa fa-volume-off rounded"></i></span>
                                <div>
                                    <c:forEach items="${subUserList}" var="subUser" varStatus="counts">
                                        <c:if test="${subUser.theme == 'sel'}">
                                            <a href="javascript:void(0)" class="subUserLink active"  name="${subUser.deptId}"  onclick="selSubDuty('${subUser.deptId}')" title="${subUser.deptName}-${subUser.dutyId}">岗位${counts.count}</a>
                                        </c:if>
                                        <c:if test="${subUser.theme != 'sel'}">
                                            <a href="javascript:void(0)" class="subUserLink"  name="${subUser.deptId}"  onclick="selSubDuty('${subUser.deptId}')" title="${subUser.deptName}-${subUser.dutyId}">岗位${counts.count}</a>
                                        </c:if>
                                    </c:forEach>
                                </div>
                            </li>
                        </ul>
                        <div class="h-nav-btm m-t">
                            <a href="javascript:void(0)" class="fl" onClick="JCF.LoadPage({name : '个人信息' , url : '/sys/user/userInfo.action'});">个人资料</a>
                            <%--<a href="javascript:void(0)" class="fl" onclick="upPwd(this);">密码</a>--%>
                            <a href="javascript:void(0)" class="fr Logging-Out" onClick="exit()">安全退出</a>
                        </div>
                    </div>
                    <div class="h-nav-bottom"></div>
                </div>
            </div>
            <div class="topbtn fr tr m-r-sm">
                <ul>
                    <li class="dropdown fl">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" onclick="upPwd(this);">
                            <i class="fa fa-locked" data-toggle="tooltip" data-placement="bottom" title="" data-container="body" data-original-title="密码"></i>
                            <b class="badge bg-red pull-right rounded hide" id="unreadb" style="display: none;"><div id="unread"></div></b>
                        </a>
                    </li>
                    <%--<common:remindList>
                        <shiro:hasPermission name="${button.id}">
                           &lt;%&ndash; <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown" onClick="JCF.LoadPage('${button.onclickurl}');">
                                    <i class="${button.iclass}" data-toggle="tooltip" data-placement="bottom" title="" data-container="body" data-original-title="${button.dataoriginaltitle}"></i>
                                    <b class="badge bg-red pull-right rounded" id="${button.divid}b"><div id="${button.divid}"></div></b>
                                </a>
                            </li>&ndash;%&gt;
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown" onClick="JCF.LoadPage({url:'${button.onclickurl}'});">
                                    <i class="${button.iclass}" data-toggle="tooltip" data-placement="bottom" title="" data-container="body" data-original-title="${button.dataoriginaltitle}"></i>
                                    <b class="badge bg-red pull-right rounded hide" id="${button.divid}b"><div id="${button.divid}"></div></b>
                                </a>
                            </li>
                        </shiro:hasPermission>
                    </common:remindList>--%>
                </ul>
            </div>
        </section>
        <!--搜索功能-->
        <div class="search fr rounded">
            <input type="text" id="smartyInput" class="placeholder" placeholder="搜索菜单" />
            <button type="button" class="w0 fl"><i class="fa fa-search"></i></button>
            <div class="smarty" id="searchSmarty">
                <div class="smarty-center">
                    <div class="padder" id="menuContainer">
                        请输入大于1个检索文字!
                    </div>
                    <div class="padder" id="bissContainer">
                    </div>
                </div>
            </div>
        </div>
    </header>
    <section>
        <section class="jcGOA-con">
            <aside class="aside-md bg-light" id="nav">
                <section class="jcGOA-wrap jcGOA-menu">
                    <section class="menu">
                        <div id="slim-scroll">
                            <nav class="nav-leftmenu nav-back" id="JCLeftMenu">
                                <ul class="nav" id="leftmenucontent">
                                    <!-- 第一层导航菜单 -->
                                    <c:forEach items="${menuList}" var="headmenu" varStatus="headcounts">
                                        <li>
                                            <a href="javascript:void(0);" id="jcleftmenu_${headmenu.id}" name="${headmenu.name}"
                                                    <c:if test="${headmenu.actionName != null}">
                                                        data-href="menu"
                                                    </c:if>
                                               action="${headmenu.actionName}">
                                                <i class="fa ${headmenu.icon}">
                                                    <b class="b-bg-${headcounts.count%8} rounded"></b>
                                                </i>
                                                <c:if test="${headmenu.childmenussize>0}">
                                                        <span class="pull-right">
                                                            <i class="fa fa-plus text"></i>
                                                            <i class="fa fa-minus text-active"></i>
                                                        </span>
                                                </c:if>
                                                <span>${headmenu.name}</span>
                                            </a>
                                            <c:if test="${headmenu.childmenussize>0}">
                                                <ul class="nav">
                                                    <!-- 第二层模块菜单 -->
                                                    <c:forEach items="${headmenu.childmenus}" var="leftmenu" varStatus="counts">
                                                        <li class="${counts.index < (headmenu.childmenus.size()-1) ? "liHeight" : ""}">
                                                            <a href="javascript:void(0);" id="jcleftmenu_${leftmenu.id}" name="${leftmenu.name}" action="${leftmenu.actionName}"
                                                                    <c:if test="${leftmenu.childmenussize == 0 || leftmenu.childmenussize == nulls}">
                                                                        data-href="menu"
                                                                    </c:if>
                                                            >
                                                                <c:if test="${leftmenu.childmenussize>0}">
                                                                <span class="pull-right">
                                                                    <i class="fa fa-plus text"></i>
                                                                    <i class="fa fa-minus text-active"></i>
                                                                </span>
                                                                </c:if>
                                                                <span>${leftmenu.name}</span>
                                                            </a>
                                                            <c:if test="${leftmenu.childmenussize>0}">
                                                                <ul class="nav">
                                                                    <!-- 第三层功能菜单 -->
                                                                    <c:forEach items="${leftmenu.childmenus}" var="childmenu" varStatus="childcounts">
                                                                        <li class="${childcounts.index < (leftmenu.childmenus.size()-1) ? "liHeight" : ""}">
                                                                            <a href="javascript:void(0);" id="jcleftmenu_${childmenu.id}" name="${childmenu.name}" action="${childmenu.actionName}"
                                                                                    <c:if test="${childmenu.childmenussize == 0}">
                                                                                        data-href="menu"
                                                                                    </c:if>
                                                                            >
                                                                                <c:if test="${childmenu.childmenussize>0}">
                                                                                    <span class="pull-right">
                                                                                        <i class="fa fa-plus text"></i>
                                                                                        <i class="fa fa-minus text-active"></i>
                                                                                    </span>
                                                                                </c:if>
                                                                                <span>${childmenu.name}</span>
                                                                            </a>
                                                                            <c:if test="${childmenu.childmenussize>0}">
                                                                                <ul class="nav">
                                                                                    <!-- 第四层功能菜单 -->
                                                                                    <c:forEach items="${childmenu.childmenus}" var="childvo" varStatus="vocounts">
                                                                                        <li class="${vocounts.index < (childmenu.childmenus.size()-1) ? "liHeight" : ""}">
                                                                                            <a href="javascript:void(0);" id="jcleftmenu_${childvo.id}" name="${childvo.name}" action="${childvo.actionName}"
                                                                                                    <c:if test="${childvo.childmenussize == 0}">
                                                                                                        data-href="menu"
                                                                                                    </c:if>
                                                                                            >
                                                                                                <c:if test="${childvo.childmenussize>0}">
                                                                                                        <span class="pull-right">
                                                                                                            <i class="fa fa-plus text"></i>
                                                                                                            <i class="fa fa-minus text-active"></i>
                                                                                                        </span>
                                                                                                </c:if>
                                                                                                <span>${childvo.name}</span>
                                                                                            </a>
                                                                                        </li>
                                                                                    </c:forEach>
                                                                                </ul>
                                                                            </c:if>
                                                                        </li>
                                                                    </c:forEach>
                                                                </ul>
                                                            </c:if>
                                                        </li>
                                                    </c:forEach>
                                                </ul>
                                            </c:if>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </nav>
                        </div>
                    </section>
                </section>
            </aside>
            <section style="width:100%;">
                <iframe id="mainFrame" name="mainFrame" src="" style="overflow:visible;" scrolling="yes" frameborder="no" width="100%" height="100%"></iframe>
            </section>
        </section>
        <div class="messageTip messageTip-1" style="z-index: 800;display:none">
            <div class="messageHeader clearfix">
                <h4 class="fl"><i class="fa fa-chat m-r-xs"></i>消息提醒</h4>
                <button type="button" class="close m-t-sm m-r-sm messageTip-close">×</button>
            </div>
            <ul class="messageList">
            </ul>
            <div class="message-btm"><a href="javascript:void(0)" onClick="JCF.LoadPage({ url : '/sys/noticeMsg/manage.action'})" class="fr m-r"><i class="fa fa-caret-right m-r-xs"></i>更多</a></div>
        </div>
    </section>
</section>
<!-- 强制修改密码DIV -->
<div id="duressPasswordDiv">
		<div class="modal fade panel" id="dPasswordModal" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    
                    <h4 class="modal-title">修改密码</h4>
              </div>
                 <div class="modal-body">
                	<form class="table-wrap form-table" id="userPwdForm">
                        <input type="hidden" id="token" name="token" value="${token}">
                        <div class="m-b-sm "><span class="required">为了您的安全请修改密码</span></div>
                        <table class="table table-td-striped">
                        
                            <tbody>
                                <tr>
                                    <td class="w140"><span class="required">*</span>旧密码</td>
                                    <td>
                                        <input type="password" id="password" name="password" style="width:60%;"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td><span class="required">*</span>新密码</td>
                                    <td>
                                        <input type="password" id="newPassword" name="newPassword" style="width:60%;"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td><span class="required">*</span>确认密码</td>
                              <td>
                                    <input  type="password" id="confirmPassword" name = "confirmPassword" style="width:60%;"/>
                                </td>
                              </tr>
                                
                
                            </tbody>
                        </table>
                     
                    </form>
                </div>
                <div class="modal-footer no-all form-btn">
                    <button class="btn dark" type="submit" id="saveBtn">确 定</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script type='text/javascript' src='${sysPath}/js/lib/common/jquery.utils.js?v=7b46332a61'></script>
<script type="text/javascript" src='${sysPath}/js/lib/jquery-validation/jquery.validate.all.js?v=b58d900620'></script>
<script src="${sysPath}/js/com/sys/user/userPwd.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/user/userPwd.validate.js" type="text/javascript"></script>
<!-- 授权校验DIV -->
<div id="linceseDiv">
	<div class="modal fade panel" id="dLinceseModal" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">授权提醒</h4>
              	</div>
                <div class="modal-body">
                    	<table class="table table-td-striped">
                        	<tbody>
                                <tr>
                                    <td style="text-align: center;">
                                        <span class="required" id="licenseMes"></span>
                                    </td>
                                </tr>
                            </tbody>
                    	</table>
                </div>
                <div class="modal-footer no-all form-btn">
                    <button class="btn dark" type="button" onclick="closeDLinceseModal()">确 定</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
function closeDLinceseModal() {
	//隐藏对话框
	$('#dLinceseModal').modal('hide');
}
</script>
<div id="voiceDiv" style="height:0;"></div>
<!-- 生日祝福 -->
<div id="birthdayDiv" class="birthday">
    <div>
        <button class="close" id="birthdayBtn" onclick='$("#birthdayDiv").fadeOut(1000);'>x</button>
    </div>
    <div>
        <img alt="生日提醒" width="400" height="300"  src="${sysPath}/images/happybirthday.gif?v=54dda22b36" >
    </div>
</div>
<script src="${sysPath}/js/lib/common/license.js"></script>
<%--<script src="${sysPath}/js/lib/common/msgTip.js" type="text/javascript"></script>--%>
<script>
    var subsystemMap = ${subsystemMap};
    function getSubsystemUrl(action){
        var reg = new RegExp("sys=([^&?]*)", "ig");
        var r = action.match(reg);
        if (r != null){
            var subsystemId = decodeURI(r[0].substr(4,r[0].length));
            for(var i=0; i<subsystemMap.length; i++){
                var subsystem = subsystemMap[i][subsystemId];
                if(subsystem){
                    var menuUrl = subsystem + action;
                    if(action.indexOf("?")==-1){
                        menuUrl += "?login-at=true";
                    }else{
                        menuUrl += "&login-at=true";
                    }
                    return menuUrl;
                }
            }
        }
        return getRootPath() + action;
    }

    function upPwd(){
        //跳转修改密码页
        JCF.LoadPage({
            url  : '/sys/user/userPwd.action'
        });
    }

    var historyUrl = new Array();
    var i18nData = {}; //全局资源文件数据

    var $doc = $(document);

    $doc.ready(function(){
        /**
         * 初始化页面浏览记录类
         * 此对象将给页面或iframe里 提供一些方法
         * 1. 对左菜单操作  JCF.histObj.sidemenu.goActivity(menuId);
         * 2. 获取面包屑    JCF.histObj.sidemenu.getCrumbs(menuId);
         * 3. 搜索框隐藏    JCF.histObj.sidemenu.searchHide();
         */
        if(!JCF.histObj){
            JCF.histObj = new JCF.Historys();
        }

        //保存top5数据
        JCF.histObj.setTopFive('${topFiveJson}');

        new JCF.UserInfo({
            color : '${sessionScope.color}' || 'blue',
            theme : '${sessionScope.theme}' || 'standard',
            font : '${sessionScope.font}' || 'standard',
            voice : $.cookie('voiceSwitch') === 'false' ? 'off' :'on'
        });

        $("#dataLoad").hide(); //页面加载完毕后即将DIV隐藏

        //$("#desktop_header li[name='menu1']").click();//默认选中主页

        //其他模块
        //$("#topnav-other_modules").other();

        //在线人数
        getOnlineCount();
        window.setInterval('getOnlineCount()',180000);

        //左菜单初始化
        JCF.histObj.sidemenu = new JCF.SideMenu('#JCLeftMenu');

        JCF.histObj.sidemenu.setData('${menuJsonStr}');

        //跳转指定页面
        if ('${pageid}') {
            JCF.LoadPage({
                url  : '${openUrl}'
            });
            JCF.histObj.sidemenu.goActivity('${pageid}');
        }else if ('${portalurl}') {
            //跳转默认门户
            JCF.LoadPage({
                url  : '${portalurl}'
            });
        }

        //换肤下拉菜单点击事件
        //new customDropDown($("#dropdownMenu"));

        //点击左菜单跳转页面事件
        $doc.on('click' , '[data-href="menu"]' , function(e){
            var $this = $(this);

            if(!$this.is('a')){
                $this = $this.parent();
            }

            JCF.LoadPage({
                id : $this.attr('id'),
                name : $this.attr('name'),
                url  : $this.attr('action')
            } , true);
            return false;
        });

        $doc.on("keydown", function(e) {
            if (e.keyCode == 8 && e.target.tagName != "INPUT" && e.target.tagName != "TEXTAREA")
                return false;
        });

        //在线人员
        $("#onlineCount").on("click", function(){
            showOnlinePerson.init();
        });

        //初始化左菜单并监听窗口变化事件
        JCF.leftScroll();

        //初始化圆角插件
        JCF.pie();
    });
</script>
</body>
</html>