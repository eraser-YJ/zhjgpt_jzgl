<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<script >
//if (!window.console || !console.firebug){
//    var names = ["log", "debug", "info", "warn", "error", "assert", "dir", "dirxml", "group", "groupEnd", "time", "timeEnd", "count", "trace", "profile", "profileEnd"];
//
//    window.console = {};
//    for (var i = 0; i < names.length; ++i)
//        window.console[names[i]] = function() {};
//}

window.onerror = function(msg,url,l) {
	if(isIe8Browser){
		return true;
	}
};

</script>

<style>
.ui-portlet-column {
	float: left;
	padding-bottom: 1px;
}

.ui-portlet-header {
	padding: .4em 0.7em
}

.ui-portlet-header .ui-icon {
	float: right;
}

.ui-portlet-content {
	overflow-y:auto;
}

.ui-portlet-header .ui-portlet-header-icon {
	float: left;
	margin: 0 0.2em 0 -0.5em;
}

.ui-sortable-placeholder {
	border: 1px dotted black;
	visibility: visible !important;
	height: 50px !important;
}

.ui-sortable-placeholder * {
	visibility: hidden;
}
</style>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<c:choose>
		<c:when test="${sessionScope.theme == 'standard'}">
			<header class="panel panel-index-wrap" id="topfive">
				<ul>
				</ul>
			</header>
			<DIV id="loadLayout" style="margin-right: 240px;">
		</c:when>
		<c:otherwise>
			<header class="panel panel-index-wrap" id="shortcutShow">
				<ul id="controlshortcut">
				</ul>
			</header>
			<DIV id="loadLayout">
		</c:otherwise>
	</c:choose>
	   <div id="portalsLayout1" class="ui-portlet1-1 clearfix"></div>
	   <div id="portalsLayout2" class="ui-portlet2-1 clearfix"></div>
	   <div id="portalsLayout3" class="clearfix"></div>
	</DIV>
	<c:if test="${sessionScope.theme == 'standard'}">
			<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
			<script src="${sysPath}/js/com/po/diary/portalDiarySet.js"></script>
			<script src="${sysPath}/js/com/common/remind.js"></script>
			<script src="${sysPath}/js/fullcalendar/fullcalendar.js"></script>
			<script src="${sysPath}/js/com/po/diary/diary.validate.js"></script>
			<section class="m-b-md aside-md" id="suspend">
				<div class="panel suspend m-t m-l-sm padder">
					<h2>日程活动</h2>
					<p id="diary-title">最新活动</p>
					<ul class="suspend-list m-t-md m-b-lg" id="diary-list">
					</ul>
					<button class="btn" type="button" onclick="schedule.showAddDiaryDiv(1,null,null);">添加活动</button>
				</div>
			</section>
			<form id="diaryForm">
				<input type="hidden" id="id" name="id" value="0"/>
				<input type="hidden" id="possessorId" name="possessorId"/>
				<input type="hidden" id="businessId" name="businessId"/>
				<input type="hidden" id="modifyDate" name="modifyDate"/>
				<input type="hidden" id="millisecond" name="millisecond"/>
				<input type="hidden" id="periodType" name="periodType" value="0"/>
				<input type="hidden" id="periodWay" name="periodWay" value="0"/>
				<input type="hidden" id="moduleFlag" name="moduleFlag" value="0"/>
				<input type="hidden" id="diaryType" name="diaryType" value="0"/>
				<input type="hidden" id="modifyFlag" name="modifyFlag"/><!-- 0周期性编辑当前事件 1周期性编辑所有事件 2非周期性日程 3非周期性日程编辑成周期性日程 4周期性日程改成非周期性 5拖拽拉伸（包含周期性编辑当前事件） -->
				<input type="hidden" id="newOrModify" name="newOrModify"/><!-- 0 新建 1 修改 -->
				<input type="hidden" id="newOk" name="newOk"/><!-- 0 没点击过确定 1 点击过确定 -->
				<input type="hidden" id="modifyExistValue" name="modifyExistValue"/><!-- 0 修改有值 1 修改没有值 -->
				<input type="hidden" id="tmpMaster" name="tmpMaster"/>
				<input type="hidden" id="tmpPeriodType" name="tmpPeriodType"/>
				<input type="hidden" id="loginUser" name="loginUser" value="${requestScope.loginUserId}"/>
				<div id="diaryDiv"></div>
			</form>
		</c:if>
</section>
<input type="hidden" id="portalId" name="portalId" value="${portalId }"/>
<input type="hidden" id="portalType" name="portalType" value="${portalType }"/>
</section>
<c:forEach items="${portletLists }" var="portalvo">
<c:set var="portalid" value=",${portalvo.id},"/>
<c:choose>
<c:when test="${fn:indexOf(rolePortalStr,portalid) != -1}">
	<c:if test="${portalvo.viewType == 'shortcut' }">
		<div id="fun_${portalvo.id }" style="display:none">
			
		</div>
	</c:if>
	<c:if test="${portalvo.viewType == 'onetable'}">
		<div id="div_${portalvo.id }" style="display:none">
			<div class="index-inform panel" >
				<h2 class=m_title>
					<span>${portalvo.letTitle}</span>
					<c:choose>
						<c:when test="${sessionScope.theme == 'classics'}">
							<em id="href_${portalvo.id }" style="cursor: pointer;" onclick="portalview.loadMore('${portalvo.id }')">更多</em>
						</c:when>
						<c:otherwise>
							<i class="fa fa-plus" onclick="portalview.loadMore('${portalvo.id }')"></i>
							<i class="fa fa-enlarge2 full-screen" type="${portalvo.viewType}"></i>
							<i class="fa fa-shrink2 exit-full" type="${portalvo.viewType}"></i>
						</c:otherwise>
					</c:choose>
				</h2>
				<div class="table-wrap input-default super-slide" id="fun_${portalvo.id }">

				</div>
			</div>
		</div>
	</c:if>
	<c:if test="${portalvo.viewType == 'moretable'}">
		<div id="div_${portalvo.id }" style="display:none">
		<div class="index-inform panel" >
		<h2 class=m_title>
			<span>${portalvo.letTitle}</span>

			<c:choose>
				<c:when test="${sessionScope.theme == 'classics'}">
					<%--<em id="href_${portalvo.id }" style="cursor: pointer;" onclick="portalview.loadMore('${portalvo.id }')">更多222</em>--%>
				</c:when>
				<c:otherwise>
			<%--		<i class="fa fa-plus" onclick="portalview.loadMore('${portalvo.id }')"></i>--%>
					<i class="fa fa-enlarge2 full-screen" type="${portalvo.viewType}"></i>
					<i class="fa fa-shrink2 exit-full" type="${portalvo.viewType}"></i>
				</c:otherwise>
			</c:choose>
		</h2>
		<div class="table-wrap input-default" id="fun_${portalvo.id }">
	
		</div>
		</div>
		</div>
	</c:if>
	<c:if test="${portalvo.viewType == 'textareaEdit' }">
		<div id="div_${portalvo.id }" style="display:none">
		<div class="index-inform panel">
		<h2 class=m_title>
			<span>${portalvo.letTitle}</span>

			<c:choose>
				<c:when test="${sessionScope.theme == 'classics'}">
					<%--<em id="href_${portalvo.id }" style="cursor: pointer;" onclick="portalview.loadMore('${portalvo.id }')">更多11</em>--%>
				</c:when>
				<c:otherwise>
			<%--		<i class="fa fa-plus" onclick="portalview.loadMore('${portalvo.id }')"></i>--%>
					<i class="fa fa-enlarge2 full-screen" type="${portalvo.viewType}"></i>
					<i class="fa fa-shrink2 exit-full" type="${portalvo.viewType}"></i>
				</c:otherwise>
			</c:choose>
		</h2>
		<div class="index-inform panel h320" style="overflow-y:auto; ">
		<div class="table-wrap">
            <p class="index-p-text" style="text-indent: 0" id="letTextarea_${portalvo.id }">${portalvo.letTextarea }</p>
        </div>
     	</div>
		</div>
		</div>
	</c:if>
	<c:if test="${portalvo.viewType == 'printEdit' }">
		<div id="div_${portalvo.id }" style="display:none">
		<div class="index-inform panel">
		<h2 class=m_title>
			<span>${portalvo.letTitle}</span>

			<c:choose>
				<c:when test="${sessionScope.theme == 'classics'}">
					<%--<em id="href_${portalvo.id }" style="cursor: pointer;" onclick="portalview.loadMore('${portalvo.id }')">更多22</em>--%>
				</c:when>
				<c:otherwise>
					<%--<i class="fa fa-plus" onclick="portalview.loadMore('${portalvo.id }')"></i>--%>
					<i class="fa fa-enlarge2 full-screen" type="${portalvo.viewType}"></i>
					<i class="fa fa-shrink2 exit-full" type="${portalvo.viewType}"></i>
				</c:otherwise>
			</c:choose>
		</h2>
		<div class="index-inform panel h320" style="overflow: hidden;">
		<div align="center" style="margin:0 10px 15px;" class="dbzx">
			<c:if test="${portalvo.letFile != null && portalvo.letFile != '' }">
<%-- 				<img src="${sysPath}/${filepath}/${portalvo.letFile }"> --%>
				<img src="${sysPath}/content/attach/originalFile/${portalvo.attachId }">
			</c:if>
			<c:if test="${portalvo.letFile == null || portalvo.letFile == '' }">
			<img src="${sysPath}/images/demoimg/iphoto.jpg">
			</c:if>
		</div>
		</div>
		</div>
		</div>
	</c:if>
	<c:if test="${portalvo.viewType == 'friendlyLink' }">
		<div id="div_${portalvo.id }" style="display:none">
			<div class="index-inform panel">
			<h2 class=m_title>
				<span>${portalvo.letTitle}</span>

				<c:choose>
					<c:when test="${sessionScope.theme == 'classics'}">
						<%--<em id="href_${portalvo.id }" style="cursor: pointer;" onclick="portalview.loadMore('${portalvo.id }')">更多</em>--%>
					</c:when>
					<c:otherwise>
						<%--<i class="fa fa-plus" onclick="portalview.loadMore('${portalvo.id }')"></i>--%>
						<i class="fa fa-enlarge2 full-screen" type="${portalvo.viewType}"></i>
						<i class="fa fa-shrink2 exit-full" type="${portalvo.viewType}"></i>
					</c:otherwise>
				</c:choose>
			</h2>
			<div class="table-wrap input-default" id="fun_${portalvo.id }">
				
			</div>
			</div>
		</div>
	</c:if>
</c:when>
<c:otherwise>
	<c:if test="${portalvo.viewType == 'shortcut' }">
		<div id="div_${portalvo.id }" style="display:none">
			
		</div>
	</c:if>
	<c:if test="${portalvo.viewType != 'shortcut' }">
		<div id="div_${portalvo.id }" style="display:none">
		<div class="index-inform panel" >
		<h2 class=m_title>
			<span>${portalvo.letTitle}</span>
			<c:choose>
				<c:when test="${sessionScope.theme == 'classics'}">
					<em id="href_${portalvo.id }" style="cursor: pointer;" onclick="portalview.loadMore('${portalvo.id }')">更多</em>
				</c:when>
				<c:otherwise>
					<i class="fa fa-plus" onclick="portalview.loadMore('${portalvo.id }')"></i>
					<i class="fa fa-enlarge2 full-screen" type="${portalvo.viewType}"></i>
					<i class="fa fa-shrink2 exit-full" type="${portalvo.viewType}"></i>
				</c:otherwise>
			</c:choose>
		</h2>
		<div class="table-wrap input-default">
		<table class="table table-striped table-bordered b-light first-td-tc over-hide-wrap">
		<thead>
			<tr><TD>无访问权限</TD></tr>
		</thead>
		</table>
		</div>
		</div>
		</div>
	</c:if>
</c:otherwise>
</c:choose>
</c:forEach>


<DIV id="loadLayout1" style="display: none">
    <div id="portalsLayout1" class="clearfix"></div>
    <div id="portalsLayout2" class="clearfix"></div>
    <div id="portalsLayout3" class="clearfix"></div>
</DIV>
<DIV id="loadLayout2" style="display: none">
	<div id="portalsLayout1" class="ui-portlet1-1 clearfix"></div>
    <div id="portalsLayout2" class="clearfix"></div>
    <div id="portalsLayout3" class="clearfix"></div>
</DIV>
<DIV id="loadLayout3" style="display: none">
    <div id="portalsLayout1" class="ui-portlet1-1 clearfix"></div>
    <div id="portalsLayout2" class="ui-portlet2-1 clearfix"></div>
    <div id="portalsLayout3" class="clearfix"></div>
</DIV>
<DIV id="loadLayout4" style="display: none">
    <div id="portalsLayout1" class="ui-portlet1-1 clearfix"></div><!--1.5:1.5:0  -->
    <div id="portalsLayout2" class="ui-portlet1-1 clearfix"></div><!--1.5:1.5:0  -->
    <div id="portalsLayout3" class="ui-portlet1-1 clearfix"></div><!--1.5:1.5:0  -->
</DIV>
<DIV id="loadLayout5" style="display: none">
    <div id="portalsLayout1" class="ui-portlet1-2-1 clearfix"></div>
    <div id="portalsLayout2" class="ui-portlet1-2-1 clearfix"></div>
    <div id="portalsLayout3" class="ui-portlet1-2-1 clearfix"></div>
</DIV>
<DIV id="loadLayout6" style="display: none">
    <div id="portalsLayout1" class="ui-portlet2-1 clearfix"></div><!--2:1:0  -->
    <div id="portalsLayout2" class="ui-portlet2-1 clearfix"></div><!--2:1:0  -->
    <div id="portalsLayout3" class="ui-portlet2-1 clearfix"></div><!--2:1:0  -->
</DIV> 
<DIV id="loadLayout7" style="display: none">
    <div id="portalsLayout1" class="clearfix"></div><!--1:1:1  -->
    <div id="portalsLayout2" class="ui-portlet-1 clearfix"></div><!--1:0:0  -->
    <div id="portalsLayout3" class="ui-portlet-1 clearfix"></div><!--1:0:0  -->
</DIV>  
<DIV id="loadLayout8" style="display: none">
    <div id="portalsLayout1" class="ui-portlet1-2 clearfix"></div><!--1:2:0  -->
    <div id="portalsLayout2" class="ui-portlet1-2 clearfix"></div><!--1:2:0  -->
    <div id="portalsLayout3" class="ui-portlet1-2 clearfix"></div><!--1:2:0  -->
</DIV> 
<DIV id="loadLayout9" style="display: none">
    <div id="portalsLayout1" class="ui-portlet-1 clearfix"></div><!--1:0:0  -->
    <div id="portalsLayout2" class="ui-portlet-1 clearfix"></div><!--1:0:0  -->
    <div id="portalsLayout3" class="ui-portlet-1 clearfix"></div><!--1:0:0  -->
</DIV>

<script src="${sysPath}/js/lib/jqueryui/jquery-ui.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/portal/jquery.portlet.pack.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/portal/jquery.superSlide.js"></script>
<script src="${sysPath}/js/lib/echarts/echarts-plain.js" type="text/javascript"></script>
<script src="${sysPath}/js/lib/echarts/eccommon.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/portal/portalView.js"></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>