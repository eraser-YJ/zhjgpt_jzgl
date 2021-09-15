<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<!-- TODO 面包屑 -->
<section class="scrollable padder jcGOA-section" id="scrollable">	 
	<section class="panel m-t-md" id="body">
	    <script >
			//设置每行按钮
			function oTableSetButtones (source) {
				var buttonStr = "";
				var edit = "<a class=\"a-icon i-new m-r-xs\" href=\"#myModal-edit\" onclick=\"dlhUserRoleList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">授权</a>";
				buttonStr = edit;
				return buttonStr;
			};
			</script>
			<div class="table-wrap">
				<table class="table table-striped tab_height first-td-tc" id="dlhUserRoleTable">
					<thead>
						<tr>
							<th>系统编码</th>
							<th>用户</th>
							<th>一次最大量</th>
							<th>创建时间</th>
							<th class="w170">操作</th>
						</tr>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			
			<section class="clearfix" id="footer_height">
				<section class="form-btn fl m-l">
				</section>
			</section>
	</section>
	<div id="dlhUserRoleModuleDiv"></div>
	</section>
</section>
<script src='${sysPath}/js/com/dlh/dlhUserRole/dlhUserRoleList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>