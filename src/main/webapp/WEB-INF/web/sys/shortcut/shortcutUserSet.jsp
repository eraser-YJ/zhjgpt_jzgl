<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<section class="scrollable padder jcGOA-section" id="scrollable">
<header class="con-header pull-in" id="navigationMenu">
	<h1></h1>
	<div class="crumbs"></div>
</header>
<section class="panel m-t-md search-box clearfix">
	<form class="table-wrap form-table" id="settingForm">
		<table class="table table-td-striped">
			<tbody>
			<tr>
				<td class="w240">快捷功能选项</td>
				<td>
					<c:forEach items="${shortcutlist}" var="shortcutVo">
						<c:if test="${shortcutVo.isChecked == 1 }">
						<label class="checkbox inline">
		   					<input type="checkbox" value="${shortcutVo.id}" checked="checked" name="checkbox" id="checkbox_${shortcutVo.id}" onclick="shortcutuse.setshortcut(this,'${shortcutVo.name}','${shortcutVo.id}')"/>${shortcutVo.name}
						</label>
						</c:if>
						<c:if test="${shortcutVo.isChecked != 1 }">
						<label class="checkbox inline">
		   					<input type="checkbox" value="${shortcutVo.id}" name="checkbox" id="checkbox_${shortcutVo.id}" onclick="shortcutuse.setshortcut(this,'${shortcutVo.name}','${shortcutVo.id}')"/>${shortcutVo.name}
						</label>
						</c:if>
					</c:forEach>	
				</td>
			</tr>
			<tr>
				<td>快捷功能配置</td>
				<td>
					<div id="controlshortcut" style="width:100%;">
						<c:forEach items="${checkedlist}" var="shortVo" varStatus="counts">
							<div id='div_${shortVo.id}'>
		   						<table class='table port-btn' >
		   							<tr>
			   							<td>
			   								<label class='eName evaluate_name' id='index_name_${counts.count-1}'>${shortVo.name}</label>
			   							</td>
		  								<td>
		  									<input class='eAlert evaluate_alert' id='index_alert_time_${counts.count-1}' readonly value='${counts.count}' />
		  									<input hidden="hidden" id="shortcutid_${counts.count-1}" value="${shortVo.id}"/>
		  								</td>
	 								</tr>
	 							</table>
	   						</div>
   						</c:forEach>
					</div>
				</td>
			</tr>
			</tbody>
		</table>
		<section class="form-btn m-b-lg">
			<button class="btn dark ok" type="button" id="settingbtn">保 存</button>
		</section>
	</form>
</section>
</section>
<script src="${sysPath}/js/com/sys/shortcut/jquery.ui.core.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/shortcut/jquery.ui.widget.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/shortcut/jquery.ui.mouse.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/shortcut/jquery.ui.sortable.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/shortcut/shortcutuser.js" type="text/javascript"></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>