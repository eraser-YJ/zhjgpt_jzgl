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
    <link rel="shortcut icon" href="favicon.ico" />
    <link href="${sysPath}/css/font-face.css" rel="stylesheet" type="text/css"/>
    <link href="${sysPath}/css/${theme}/jcap.css?v=7b89ce1005" rel="stylesheet" type="text/css"/>

    <script type="text/javascript" src='${sysPath}/js/jquery.min.js?v=517bc0d319'></script>
    <script type="text/javascript" src='${sysPath}/js/lib/jquery/jquery.cookie.js?v=a79ce0f6ee'></script>
    <script type="text/javascript" src="${sysPath}/js/jbox/jbox.min.js?v=ce217626b3"></script>

    <script src="${sysPath}/js/lib/common/navigationbar_2.5.js?v=8ec9204c8a" type="text/javascript"></script>

    <script type="text/javascript" src="${sysPath}/js/slimscroll/silmScroll.js"></script>
    <script type="text/javascript" src="${sysPath}/js/com/sys/subMenu/subMenu.js"></script>

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
<section class="jcGOA-wrap">
    <header class="header navbar-fixed-top" id="desktop_header">
        <div class="navbar-topbar clearfix">
            <input type="hidden" id="urlParameter" name="urlParameter" value=""/>
            <!-- 桌面精灵使用勿动 开始 -->
            <!-- 页面读取完成标志-->
            <input id="loadOver" type="hidden" value="0"/>
            <!-- 未读邮件-->
            <a id="15e880df-f229-47a9-9a44-8f9eaa969f6e" name="15e880df-f229-47a9-9a44-8f9eaa969f6e" hidden="hidden" href="javascript:void(0)" onClick="loadrightmenu('/ic/mail/manageUnread.action','','/ic/mail/manageUnread.action');"></a>
            <!-- 待办发文-->
            <a id="31e029e0-4599-4696-bd9c-f3290a8cf6eb" name="31e029e0-4599-4696-bd9c-f3290a8cf6eb" hidden="hidden"  href="javascript:void(0)" onClick="menuClick('2084','/workFlow/form/jumpToList.action?url=/doc/send/sendWaitTransact',2,'待办发文')"></a>
            <!-- 待办收文-->
            <a id="384a1ce1-132f-4856-b3ff-885f1c3ee7d9" name="384a1ce1-132f-4856-b3ff-885f1c3ee7d9" hidden="hidden"  href="javascript:void(0)" onClick="menuClick('2089','/workFlow/form/jumpToList.action?url=/doc/receive/receiveToDoList',2,'待办收文')"></a>
            <!-- 待办流程-->
            <a id="3d18eabf-3ee3-4cce-a932-57319c9e8d26" name="3d18eabf-3ee3-4cce-a932-57319c9e8d26" hidden="hidden"  href="javascript:void(0)" onClick="loadrightmenu('/workFlow/processInstance/getTodoList.action','','/workFlow/processInstance/getTodoList.action');"></a>
            <!-- 桌面精灵使用勿动 结束-->
            <a onClick="window.open('${sysPath}/help/help.html','_blank','width=1000px,height=600px');return false;" href="#" class="m-t-xs fl icon-help" >帮助</a>
            <div class="m-t-xs fr dropdown" id="dropdownMenu">
                <!-- onClick="skin()" -->
                    <c:forEach items="${subUserList}" var="subUser" varStatus="counts">
                        <c:if test="${subUser.theme == 'sel'}">
                            <a href="javascript:void(0)" class="subUserLink active"  name="${subUser.deptId}"  onclick="selSubDuty('${subUser.deptId}')" title="${subUser.deptName}-${subUser.dutyId}">岗位${counts.count}</a>
                        </c:if>
                        <c:if test="${subUser.theme != 'sel'}">
                            <a href="javascript:void(0)" class="subUserLink"  name="${subUser.deptId}"  onclick="selSubDuty('${subUser.deptId}')" title="${subUser.deptName}-${subUser.dutyId}">岗位${counts.count}</a>
                        </c:if>
                    </c:forEach>
                <a href="javascript:void(0)" class="icon-voice on" id="voiceSwitch" onclick="turnVoice(this);">声音</a>`
                <a href="javascript:void(0)" class="icon-skin" data-toggle="dropdown">换肤</a>
                <a href="javascript:void(0)" onClick="exit()" class="icon-exit">退出</a>
                <ul class="dropdown-menu animated fadeInRight" style="min-width: 125px;">
                    <li name="classics" class="active"><a href="javascript:;" >经典</a></li>
                    <li name="standard"><a href="javascript:;" >标准</a></li>
                </ul>
            </div>
        </div>
        <nav class="header-nav fl">
            <ul class="clearfix" id="navigation-header">
                <li class="padd">
                    <h1 style="min-width:207px;">
                        <!-- <span  class="navbar-brand">长春新区智慧建管平台</span> -->
                        <img width="160" height="70" src="${sysPath}/images/demoimg/logo.png?v=988e75263f" onClick="window.location.reload();"/>
                    </h1>
                </li>
                <c:forEach items="${menuList}" var="menu" >
                    <li id="header_${menu.id}">
                        <a id="${menu.id}" name="${menu.name}" action="${menu.actionName}" potal="${menu.permission}" data-head="menu">
                            <i class="${menu.icon}"></i>${menu.name}
                        </a>
                    </li>
                </c:forEach>
                <li id="topnav-other_modules">
                    <a href="#">
                        <i class="topnav-other_modules"></i>其他模块
                    </a>
                </li>
            </ul>
        </nav>
        <section class="userInfo m-r-sm fr clearfix">
            <div class="user-avatar fl tc">
                <a href="#" onClick="JCF.LoadPage({name : '个人信息' , url : '/sys/user/userInfo.action'});">
                    <img src="${sysPath}/<shiro:principal property="photo"/>" height="74"/>
                </a>
            </div>
            <div class="user-info fr">
                <div class="clearfix name">
                    <p class="fl inter">
                        <a href="#" style="cursor:pointer;" id="" title="<shiro:principal property="displayName"/><shiro:principal property="dutyIdValue"/>">

                            <i class="fa fa-user"></i><span><shiro:principal
                                property="displayName" />
									<shiro:principal property="dutyIdValue" /></span>

                        </a>
                    </p>
						<span class="fr">
							<a href="#" id="onlineCount">在线：
                                <span id="lineCount">0</span>人
                            </a>
						</span>
                </div>
                <div class="topbtn">
                    <ul>
                        <%--<common:remindList>
                            <shiro:hasPermission name="${button.id}">
                                <li class="dropdown">
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" onClick="JCFF.loadPage({url:'${button.onclickurl}'});">
                                        <i class="${button.iclass}" data-toggle="tooltip" data-placement="top" title="" data-container="body" data-original-title="${button.dataoriginaltitle}"></i>
                                        <b class="badge bg-red pull-right rounded hide" id="${button.divid}b"><div id="${button.divid}"></div></b>
                                    </a>
                                </li>
                            </shiro:hasPermission>
                        </common:remindList>--%>
                    </ul>
                </div>
            </div>
        </section>
    </header>
    <section>
        <section class="jcGOA-con">
            <aside class="aside-md bg-light" id="nav">
                <section class="jcGOA-wrap jcGOA-menu">
                    <header class="menu-header tc clearfix">
                        <div class="clearfix">
                            <a href="#nav" data-toggle="class:nav-xs" class="fr menu-toggle" data-tip="menu">
                                <!-- <i class="fa fa-align-justify"></i> -->
                                <i data-toggle="tooltip" data-placement="top" title="" data-container="body" data-original-title="隐藏菜单" class="fa fa-align-justify"></i>
                            </a>
                        </div>
                        <%--<div class="sidebar-search clearfix">
                            <div class="input-box dropdown clearfix hide">
                                <a href="javascript:;" class="remove"></a>
                                <input type="text" placeholder="快速查询..." />

                                <button type="submit" class="submit"><i class="fa fa-search"></i></button>
                                <button type="submit" class="submit nav-xs-submit dropdown-toggle" data-toggle="dropdown"><i class="fa fa-search"></i></button>
                                <button type="submit" class="submit nav-xs-submit remove dropdown-toggle" data-toggle="dropdown"><i class="fa fa-remove"></i></button>
                                <section class="dropdown-menu animated fadeInRight wrapper">
                                    <form role="search">
                                        <div class="input-append m-b-none">
                                            <input type="text" class="form-control" placeholder="快速查询...">
                                            <button type="submit" class="btn">
                                                <i class="fa fa-search"></i>
                                            </button>
                                        </div>
                                    </form>
                                </section>
                            </div>
                        </div>--%>
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

            <section style="width:100%;">
                <iframe id="mainFrame" name="mainFrame" src="" style="overflow:visible;"
                        scrolling="yes" frameborder="no" width="100%" height="100%"></iframe>
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
<div class="nav-xs-dd bg-light" id="nav-menu-tie">
    <div id="nav-menu-con" class="nav-primary"></div>
</div>
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
<script src="${sysPath}/js/com/sys/user/duressPassword.js"></script>
<script src="${sysPath}/js/lib/common/license.js"></script>
<%--引入工作流相关业务需要打开js--%>
<%--<script src="${sysPath}/js/com/workFlow/workFlowUtils.js" type="text/javascript"></script>--%>
<%--引入通知公告业务需要打开js--%>
<%--<script src="${sysPath}/js/lib/common/msgTip.js" type="text/javascript"></script>--%>
<script>
    function goDefaultMenu(){
        //跳转指定页面
        if ('${openUrl}') {
            JCF.LoadPage({
                url  : '${openUrl}'
            });
        }else if ('${portalurl}') {
            //跳转默认门户
            JCF.LoadPage({
                url  : '${portalurl}'
            });
        }
    }

    function upPwd(){
        //跳转修改密码页
        JCF.LoadPage({
            url  : '/sys/user/userPwd.action'
        });
    }

    JCFF.$document.ready(function(){
        var historyUrl = new Array();
        var i18nData = {}; //全局资源文件数据
        if(!JCF.histObj){
            JCF.histObj = new JCF.Historys();
        }

        if($.cookie('voiceSwitch') === 'false'){
            $('#voiceSwitch').removeClass("on icon-voice").addClass("off icon-no-voice");
        }

        $('#loadOver').attr('value', '1');
        //loadbirthday();
        var mydropdown = new customDropDown($("#dropdownMenu"));

        //左菜单初始化
        JCF.histObj.sidemenu = new JCF.SideMenu('#JCLeftMenu');
        if ('${menuJsonStr}') {
            JCF.histObj.sidemenu.setData('${menuJsonStr}');
        }
        //点击导航事件
        JCFF.$document.on('click' , '[data-head="menu"]' , function(e){
            var $this = $(e.target);
            if(!$this.is('a')) $this = $this.parent();
            var actcion = $this.attr('action'),
                    id  = $this.attr('id'),
            isRootPotal = $this.attr('potal');

            JCF.navigation(id);
            JCF.histObj.sidemenu.headMenu = null;
            JCF.histObj.sidemenu.drawLeft(id);

            if(actcion){
                JCF.LoadPage({
                    id : id,
                    name : $this.attr('name'),
                    url  : actcion + '&menuId='+id,
                } , true);
            }else{
                if(isRootPotal){
                    goDefaultMenu();
                }
            }
        });
        //初始化其他模块
        $("#topnav-other_modules").other();

        goDefaultMenu();
        $(document).on("keydown", function(e) {
            if (e.keyCode == 8 && e.target.tagName != "INPUT" && e.target.tagName != "TEXTAREA")
                return false;
        });

        $("#onlineCount").on("click", function() {
            showOnlinePerson.init();
        });
    });
</script>
</body>
</html>