<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<script>
	var tabDefaultid='';
	var loadTablePapPage = {};
</script>
<div class="modal fade panel" id="data-modal-view" aria-hidden="false">
	<div class="modal-dialog " style="width:1400px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h3 id="title" class="tc">查看详情</h3>
			</div>
			<div class="modal-body">
				<table class="table form-table table-td-striped">
					<tbody>
					<c:forEach items="${formData}" var="itemData" varStatus="abcCond">
						<c:if test="${abcCond.index%2 eq 0}">
							<tr>
						</c:if>
								<td style="width:200px">${itemData.title}</td>
								<td>${itemData.value}</td>

						<c:if test="${abcCond.index%2 eq 1}">
							</tr>
						</c:if>
						<c:if test="${abcCond.last and abcCond.index%2 eq 0}">
						<td  class="w140"></td>
						<td></td>
						</tr>
						</c:if>
					</c:forEach>
					<c:if test="${listSize>0}">
						<tr>
							<td colspan="4">
								<section class="tabs-wrap m-t-md">
									<ul class="nav-tabs">
										<c:forEach items="${tabList}" var="tname" varStatus="abb">
											<c:if test="${abb.index==0}">
												<script>tabDefaultid='${tname.id}';</script>
											</c:if>
											<li id="litag_${tname.id}" name="litag_${tname.id}" onclick="openTableMapPage('${tname.id}','${tname.tableCode}')" style="cursor:pointer;">
<%--												<a id="tabOrg" href="#messages1" data-toggle="tab">${tname.tabName}</a>--%>${tname.tabName}
											</li>&nbsp;
											<input type="hidden" id="query_${tname.id}" name = "query_${tname.id}"
												   operationAction="" operationType="${tname.itemType}" operationKey="${tname.colNameV}" value="${tname.value}">
										</c:forEach>
									</ul>
								</section>

								<div class="tab-box">
									<div id="messages1">

									</div>
								</div>

							</td>
						</tr>
					</c:if>
					</tbody>
				</table>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn" type="button" id="dataViewClose">关 闭</button>
			</div>
		</div>
	</div>
</div>
<!-- tabs -->

<script type="text/javascript">
$(document).ready(function(){
	ie8StylePatch();
	$("#dataViewClose").click(function(){
		$('#data-modal-view').modal('hide');
	});
});

function openTableMapPage(id,tableCode){
	//tab页签样式
	$(".nav-tabs>li").removeClass('tab-item-active');
	$("#litag_"+id).addClass("tab-item-active");

	if(id!=''){
		tabDefaultid=id;
	}

	if(id=='-1'){
		$("#messages1").html("");
		$("#messages1").load(getRootPath()+"/dlh/dlhQuery/queryFileList.action?objUrl=&dataId="+id,null,function(){

		});
	}else{
		var dlhDataId = '${dlhDataId}';
		var dlh_data_src_ = '${dlh_data_src_}';
		$("#messages1").html("");
		$("#messages1").load(getRootPath()+"/supervise/gis/loadTableMapPage.action?dlhDataId="+dlhDataId+"&dlh_data_src_="+dlh_data_src_+"&id="+id,null,function(){

		});
	}

}

//modal显示及设置标题
function modalShow(title) {
	$("#title").html(title);//标题
	$('#data-modal-view').modal('show');
	$(".loading").hide();
}
</script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>