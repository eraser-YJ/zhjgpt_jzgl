<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<script>
	var tabDefaultid='';
	var loadTablePapPage = {};
</script>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="crumbs">
			${pageHeader}
		</div>
	</header>
	<section class="panel m-t-md clearfix">
		<form class="table-wrap m-20-auto" id="entityForm">
			<h3>${objectTitle}</h3>

			<table class="table form-table table-td-striped">
				<tbody>
				<c:forEach items="${formData}" var="itemData" varStatus="abcCond">
					<c:if test="${abcCond.index%2 eq 0}">
						<tr>
					</c:if>
							<td style="width:10%">${itemData.title}</td>
							<td style="width:40%">${itemData.value}</td>

					<c:if test="${abcCond.index%2 eq 1}">
						</tr>
					</c:if>
					<c:if test="${abcCond.last and abcCond.index%2 eq 0}">
					<td style="width:10%"></td>
					<td style="width:40%"></td>
					</tr>
					</c:if>
				</c:forEach>
				</tbody>
			</table>

				<c:if test="${listSize>0}">
				<table class="table table-td-striped">
					<tr>
						<td >
							<section class="tabs-wrap m-t-md">
								<ul class="nav-tabs">
									<c:forEach items="${tabList}" var="tname" varStatus="abb">
										<c:if test="${abb.index==0}">
											<script>tabDefaultid='${tname.id}';</script>
										</c:if>
										<li id="litag_${tname.id}" name="litag_${tname.id}" onclick="openTableMapPage('${tname.id}','${tname.tableCode}')">
											<%--<a id="tabOrg" href="#messages1" data-toggle="tab">--%>${tname.tabName}<%--</a>--%>
										</li>&nbsp;
										<input type="hidden" id="query_${tname.id}" name = "query_${tname.id}"
											   operationAction="" operationType="${tname.itemType}" operationKey="${tname.colNameV}" value="${tname.value}">
									</c:forEach>


								</ul>
							</section>
							<%--<section class="panel tab-content">--%>
								<div class="tab-box">
									<div id="messages1">

									</div>

								</div>



							<%--</section>--%>
						</td>
					</tr>
				</table>

				</c:if>


		</form>
		<div class="modal-footer form-btn">

			<button class="btn" type="button" id="formDivClose">返 回</button>
		</div>


	</section>

</section>
<!-- tabs -->

<script  type="text/javascript">
$(document).ready(function(){
	ie8StylePatch();
	$("#formDivClose").click(function(){history.go(-1)});
	// openTableMapPage("");

});
/*function openTableMapPage2(){
	loadTablePapPage.dlhDataobjectList();
}*/
function openTableMapPage(id,tableCode){
	//var constr = '{"operationAction":"","operationKey":"'+tableCode+'","'+itemType+'":"varchar","value":"'+value+'"}'
	if(id!=''){
		tabDefaultid=id;
	}

	$("li[name^='litag_']").each(function(objIndex,object){
		$(object).removeClass("tab-item-active");
	})
	$("#litag_"+id).addClass("tab-item-active");

	if(id=='-1'){
		$("#messages1").html("")
		$("#messages1").load(getRootPath()+"/dlh/dlhQuery/queryFileList.action?objUrl=&dataId="+id,null,function(){
			/*myDetailFormModalShow();*/
			// $(".datepicker-input").each(function(){$(this).datepicker();});
			//计算分页显示条数
			// loadTablePapPage.pageCount = TabNub>0 ? TabNub : 10;
			//初始化列表方法
			// loadTablePapPage.dlhDataobjectList();
			// openTableMapPage2()
		});
	}else{
		var dlhDataId = '${dlhDataId}';
		var dlh_data_src_ = '${dlh_data_src_}';
		$("#messages1").html("")
		$("#messages1").load(getRootPath()+"/dlh/dlhQuery/loadTableMapPage.action?dlhDataId="+dlhDataId+"&dlh_data_src_="+dlh_data_src_+"&id="+id,null,function(){
			/*myDetailFormModalShow();*/
			// $(".datepicker-input").each(function(){$(this).datepicker();});
			//计算分页显示条数
			// loadTablePapPage.pageCount = TabNub>0 ? TabNub : 10;
			//初始化列表方法
			// loadTablePapPage.dlhDataobjectList();
			// openTableMapPage2()
		});
	}



}
</script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>