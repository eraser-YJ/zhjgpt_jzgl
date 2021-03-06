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
    <link href="${sysPath}/css/yx_left_frame.css" rel="stylesheet" type="text/css"/>
    <link href="${sysPath}/css/custom.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src='${sysPath}/js/jquery.min.js?v=517bc0d319'></script>
    <script type="text/javascript" src='${sysPath}/js/lib/jquery/jquery.cookie.js?v=a79ce0f6ee'></script>
    <script type="text/javascript" src="${sysPath}/js/jbox/jbox.min.js?v=ce217626b3"></script>
    <script src="${sysPath}/js/lib/common/navigationbar.js?v=8ec9204c8a" type="text/javascript"></script>
    <script type="text/javascript" src="${sysPath}/js/slimscroll/silmScroll.js"></script>
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
</head>
<body ondragstart="return false" id="desktopBody">
    <input type="hidden" id="urlParameter" name="urlParameter" value=""/>
    <section class="jcGOA-wrap">
        <header class="header navbar-fixed-top" id="desktop_header">
            <h1><a href="javascript:void(0)"><img src="${sysPath}/images/standard/logo.png" onClick="window.location.reload();" class="s-logo" style="margin-top: 10px; margin-left: -15px;" /></a> </h1>
            <section class="userInfo m-r-sm fr clearfix">
                <div class="user-avatar fr" id="user-avatar"><div class="user-avatar-img fl"><a href="#"><img src="${sysPath}/content/attach/originalPath.action?url=<shiro:principal property="photo"/>" height="40"/></a></div></div>
                <div class="topbtn fr tr m-r-sm">
                    <i class="fa fa-user2"></i><a href="javascript:void(0)" class="s-headerH" onClick="JCF.LoadPage({name : '????????????' , url : '/sys/user/userInfo.action'});">????????????</a>
                    <i class="fa fa-user2"></i><a href="javascript:void(0)" class="s-headerH" onclick="JCF.LoadPage({name: '????????????', url  : '/sys/user/userPwd.action' });">??????</a>
                    <i class="fa fa-user2"></i><a href="javascript:void(0)" class="s-headerH" onClick="exit()">????????????</a>
                </div>
            </section>
        </header>
        <section>
            <section class="jcGOA-con">
                <aside class="aside-md bg-light" id="nav">
                    <section class="jcGOA-wrap jcGOA-menu">
                        <section class="menu">
                            <div id="slim-scroll">
                                <nav class="nav-leftmenu nav-back" id="JCLeftMenu">
                                    <ul class="nav" id="leftmenucontent">
                                        <!-- ????????????????????? -->
                                        <c:forEach items="${menuList}" var="headmenu" varStatus="headcounts">
                                            <li>
                                                <a href="javascript:void(0);" id="jcleftmenu_${headmenu.id}" name="${headmenu.name}" <c:if test="${headmenu.actionName != null}"> data-href="menu" </c:if> action="${headmenu.actionName}">
                                                    <i class="fa ${headmenu.icon}"><b class="b-bg-${headcounts.count%8} rounded"></b></i>
                                                    <c:if test="${headmenu.childmenussize>0}"><span class="pull-right"><i class="fa fa-plus text"></i><i class="fa fa-minus text-active"></i></span></c:if>
                                                    <span>${headmenu.name}</span>
                                                </a>
                                                <c:if test="${headmenu.childmenussize>0}">
                                                    <ul class="nav">
                                                        <!-- ????????????????????? -->
                                                        <c:forEach items="${headmenu.childmenus}" var="leftmenu" varStatus="counts">
                                                            <li class="${counts.index < (headmenu.childmenus.size()-1) ? "liHeight" : ""}">
                                                                <a href="javascript:void(0);" id="jcleftmenu_${leftmenu.id}" name="${leftmenu.name}" action="${leftmenu.actionName}" <c:if test="${leftmenu.childmenussize == 0 || leftmenu.childmenussize == nulls}"> data-href="menu" </c:if>>
                                                                    <c:if test="${leftmenu.childmenussize>0}"><span class="pull-right"><i class="fa fa-plus text"></i><i class="fa fa-minus text-active"></i></span></c:if>
                                                                    <span>${leftmenu.name}</span>
                                                                </a>
                                                                <c:if test="${leftmenu.childmenussize>0}">
                                                                    <ul class="nav">
                                                                        <!-- ????????????????????? -->
                                                                        <c:forEach items="${leftmenu.childmenus}" var="childmenu" varStatus="childcounts">
                                                                            <li class="${childcounts.index < (leftmenu.childmenus.size()-1) ? "liHeight" : ""}">
                                                                                <a href="javascript:void(0);" id="jcleftmenu_${childmenu.id}" name="${childmenu.name}" action="${childmenu.actionName}" <c:if test="${childmenu.childmenussize == 0}"> data-href="menu" </c:if> >
                                                                                    <c:if test="${childmenu.childmenussize>0}"><span class="pull-right"><i class="fa fa-plus text"></i><i class="fa fa-minus text-active"></i></span></c:if>
                                                                                    <span>${childmenu.name}</span>
                                                                                </a>
                                                                                <c:if test="${childmenu.childmenussize>0}">
                                                                                    <ul class="nav">
                                                                                        <!-- ????????????????????? -->
                                                                                        <c:forEach items="${childmenu.childmenus}" var="childvo" varStatus="vocounts">
                                                                                            <li class="${vocounts.index < (childmenu.childmenus.size()-1) ? "liHeight" : ""}">
                                                                                                <a href="javascript:void(0);" id="jcleftmenu_${childvo.id}" name="${childvo.name}" action="${childvo.actionName}" <c:if test="${childvo.childmenussize == 0}"> data-href="menu" </c:if> >
                                                                                                    <c:if test="${childvo.childmenussize>0}"><span class="pull-right"><i class="fa fa-plus text"></i><i class="fa fa-minus text-active"></i></span></c:if>
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
                <section style="width:100%;"><iframe id="mainFrame" name="mainFrame" src="" style="overflow:visible;" scrolling="yes" frameborder="no" width="100%" height="100%"></iframe></section>
            </section>
            <div class="messageTip messageTip-1" style="z-index: 800;display:none">
                <div class="messageHeader clearfix">
                    <h4 class="fl"><i class="fa fa-chat m-r-xs"></i>????????????</h4>
                    <button type="button" class="close m-t-sm m-r-sm messageTip-close">??</button>
                </div>
                <ul class="messageList"></ul>
                <div class="message-btm"><a href="javascript:void(0)" onClick="JCF.LoadPage({ url : '/sys/noticeMsg/manage.action'})" class="fr m-r"><i class="fa fa-caret-right m-r-xs"></i>??????</a></div>
            </div>
        </section>
    </section>

    <!-- ??????????????????DIV -->
    <div id="duressPasswordDiv">
        <div class="modal fade panel" id="dPasswordModal" aria-hidden="false">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header"><h4 class="modal-title">????????????</h4></div>
                    <div class="modal-body">
                	    <form class="table-wrap form-table" id="userPwdForm">
                            <input type="hidden" id="token" name="token" value="${token}">
                            <div class="m-b-sm "><span class="required">?????????????????????????????????</span></div>
                            <table class="table table-td-striped"><tbody>
                                <tr>
                                    <td class="w140"><span class="required">*</span>?????????</td>
                                    <td><input type="password" id="password" name="password" style="width:60%;"/></td>
                                </tr>
                                <tr>
                                    <td><span class="required">*</span>?????????</td>
                                    <td><input type="password" id="newPassword" name="newPassword" style="width:60%;"/></td>
                                </tr>
                                <tr>
                                    <td><span class="required">*</span>????????????</td>
                                    <td><input  type="password" id="confirmPassword" name = "confirmPassword" style="width:60%;"/></td>
                                </tr>
                            </tbody></table>
                        </form>
                    </div>
                    <div class="modal-footer no-all form-btn"><button class="btn dark" type="submit" id="saveBtn">??? ???</button></div>
                </div>
            </div>
        </div>
    </div>
    <script type='text/javascript' src='${sysPath}/js/lib/common/jquery.utils.js?v=7b46332a61'></script>
    <script type="text/javascript" src='${sysPath}/js/lib/jquery-validation/jquery.validate.all.js?v=b58d900620'></script>
    <script src="${sysPath}/js/com/sys/user/userPwd.js" type="text/javascript"></script>
    <script src="${sysPath}/js/com/sys/user/userPwd.validate.js" type="text/javascript"></script>

    <!-- ????????????DIV -->
    <div id="linceseDiv">
	    <div class="modal fade panel" id="dLinceseModal" aria-hidden="false">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header"><h4 class="modal-title">????????????</h4></div>
                    <div class="modal-body">
                    	<table class="table table-td-striped"><tbody><tr><td style="text-align: center;"><span class="required" id="licenseMes"></span></td></tr></tbody></table>
                    </div>
                    <div class="modal-footer no-all form-btn"><button class="btn dark" type="button" onclick="$('#dLinceseModal').modal('hide');">??? ???</button></div>
                </div>
            </div>
        </div>
    </div>

    <div id="voiceDiv" style="height:0;"></div>

    <!-- ???????????? -->
    <div id="birthdayDiv" class="birthday">
        <div><button class="close" id="birthdayBtn" onclick='$("#birthdayDiv").fadeOut(1000);'>x</button></div>
        <div><img alt="????????????" width="400" height="300"  src="${sysPath}/images/happybirthday.gif?v=54dda22b36" ></div>
    </div>
    <script src="${sysPath}/js/lib/common/license.js"></script>

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

        var historyUrl = new Array();
        var i18nData = {}; //????????????????????????
        var $doc = $(document);
        $doc.ready(function(){
            /**
             * ??????????????????????????????
             * ????????????????????????iframe??? ??????????????????
             * 1. ??????????????????  JCF.histObj.sidemenu.goActivity(menuId);
             * 2. ???????????????    JCF.histObj.sidemenu.getCrumbs(menuId);
             * 3. ???????????????    JCF.histObj.sidemenu.searchHide();
             */
            if (!JCF.histObj) { JCF.histObj = new JCF.Historys(); }
            //??????top5??????
            JCF.histObj.setTopFive('${topFiveJson}');
            new JCF.UserInfo({
                color : '${sessionScope.color}' || 'blue',
                theme : '${sessionScope.theme}' || 'standard',
                font : '${sessionScope.font}' || 'standard',
                voice : $.cookie('voiceSwitch') === 'false' ? 'off' :'on'
            });
            $("#dataLoad").hide(); //???????????????????????????DIV??????
            //??????????????????
            JCF.histObj.sidemenu = new JCF.SideMenu('#JCLeftMenu');
            JCF.histObj.sidemenu.setData('${menuJsonStr}');
            //??????????????????
            if ('${pageid}') {
                JCF.LoadPage({ url  : '${openUrl}' });
                JCF.histObj.sidemenu.goActivity('${pageid}');
            } else if ('${portalurl}') {
                //??????????????????
                JCF.LoadPage({ url  : '${portalurl}' });
            }

            //?????????????????????????????????
            $doc.on('click' , '[data-href="menu"]' , function(e){
                var $this = $(this);
                if(!$this.is('a')){ $this = $this.parent(); }
                JCF.LoadPage({
                    id : $this.attr('id'),
                    name : $this.attr('name'),
                    url  : $this.attr('action')
                } , true);
                return false;
            });

            $doc.on("keydown", function(e) {
                if (e.keyCode == 8 && e.target.tagName != "INPUT" && e.target.tagName != "TEXTAREA") {
                    return false;
                }
            });

            //?????????????????????????????????????????????
            JCF.leftScroll();

            //?????????????????????
            JCF.pie();
        });
    </script>
</body>
</html>