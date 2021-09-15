<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal-Dept" aria-hidden="false">
	<div class="modal-dialog w900" >
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="entityFormTitle">部门选择</h4>
			</div>
			<div  style="max-height:500px; padding: 5px;">
				<section class="panel clearfix search-box search-shrinkage">
					<div class="search-line">
						<form class="table-wrap form-table" id="searchDeptForm">
							<table class="table table-td-striped">
								<tbody>
								<tr>
									<td class="w140" align="center" style="text-align:center;width:100px;">部门名称</td>
									<td>
										<input type="text" id="query_deptName" name="query_deptName"/>
									</td>
									<td align="center"  style="text-align:center;width:100px;">
										<button class="btn" type="button" id="queryDeptBtn">查 询</button>
									</td>
								</tr>
								</tbody>
							</table>
						</form>
					</div>
				</section>
				<section class="panel " >
					<div class="table-wrap"><table class="table table-striped tab_height first-td-tc" id="gridDeptTable"></table></div>
					<section class="clearfix">
						<section class="form-btn fl m-l">
						</section>
					</section>
				</section>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
			</div>
		</div>
	</div>
</div>
<script src='${sysPath}/js/com/jc/csmp/doc/dept/deptDlgList.js'></script>