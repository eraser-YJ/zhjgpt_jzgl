<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="navigationMenu">
		<div class="crumbs">
			<span>项目管理</span>
			<span>工程信息管理</span>
		</div>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
		<div class="search-line">
			<form class="table-wrap form-table" id="searchForm" >
				<table class="table form-table table-td-striped" style="width:100% !important; margin-bottom:10px;">
					<tbody>
						<tr>
							<td class="w100">项目统一编号</td>
							<td><input type="text" id="projectNumber" name="projectNumber" /></td>
							<td class="w100">项目名称</td>
							<td><input type="text" id="projectName" name="projectName" /></td>
						</tr>
						<tr>
							<td>所属区域</td>
							<td><dic:select name="region" id="region" dictName="region" parentCode="csmp" defaultValue="" headType="1" headValue="" /></td>
							<td>项目类型</td>
							<td><dic:select name="projectType" id="projectType" dictName="project_type" parentCode="csmp" defaultValue="" headType="1" headValue="" /></td>
						</tr>
					</tbody>
				</table>
				<div  class="btn-tiwc">
					<button class="btn" type="button" id="queryBtn">查 询</button>
					<button class="btn" type="button" id="resetBtn">重 置</button>
				</div>
			</form>
		</div>
	</section>
	<section class="panel ">
		<script>
			function oTableSetButtons(source) {
				var person = "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"cmProjectInfoListPanel.distribution('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('人员分配', 'fa-users') + "</a>";
				var look = "<shiro:hasPermission name='projectLook'><a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"cmProjectInfoListPanel.loadModuleForLook('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('查看', 'fa-infor-search') + "</a></shiro:hasPermission>";
				var edit = "<shiro:hasPermission name='projectUpdate'><a class=\"a-icon i-cemt\" href=\"javascript:void(0);\" onclick=\"cmProjectInfoListPanel.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('修改', 'fa-edit2') + "</a></shiro:hasPermission>";
				return person + look + edit;
			}
		</script>

		<div class="table-wrap">
			<section class="form-btn fl m-l">
				<shiro:hasPermission name='projectAdd'>
					<a class="btn dark" role="button" id="addBtn" style="margin: 0 !important;">新 增</a>
				</shiro:hasPermission>
			</section>
			<table class="table table-striped tab_height" id="gridTable"></table>
		</div>
		<section class="clearfix" id="footer_height"></section>
	</section>
	<div id="formModule"></div>
	<div id="chooseCompanyModule"></div>
	<div id="chooseResourceModule"></div>
</section>

<div class="modal fade panel" id="approval-modal" aria-hidden="false">
	<div class="modal-dialog w358">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" onclick="$('#approval-modal').modal('hide');">×</button>
				<h4 class="modal-title">选择项目来源方式</h4>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn dark" type="button" onclick="cmProjectInfoListPanel.chooseResource();">资源库立项信息</button>
				<button class="btn dark" type="button" onclick="cmProjectInfoListPanel.addProject();">本地新增</button>
			</div>
		</div>
	</div>
</div>
<script src='${sysPath}/js/com/jc/csmp/common/plugin/choose-resource.js'></script>
<script src='${sysPath}/process-editor/editor-app/libs/layer/layer.js'></script>
<script src='${sysPath}/js/com/jc/csmp/project/info/cmProjectInfoList.js?n=1'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
