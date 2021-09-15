<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<!-- TODO 面包屑 -->

<section class="scrollable padder jcGOA-section" id="scrollable">


			<div class="table-wrap" >
				<table class="table table-striped tab_height " id="loadTablePapPageTable">
					<thead>
						<tr>
							<th style="width:100px;">序号</th>
							<c:forEach items="${pageListArea}" var="itemData">
						        <th>${itemData.title}</th>
						   	</c:forEach>
						   	<th style="width:100px;">操作</th>
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
    <div id="loadTablePapPageDiv"></div>
</section>

<script type="text/javascript">
	loadTablePapPage_oTableAoColumns = [
		{mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
		<c:forEach items="${pageListArea}" var="itemData" varStatus="abc">
			{'mData':'${itemData.code}' },
		</c:forEach>
		{mData: function(source) {
			return openLoadTablePapPageList(source.dlh_data_id_,source.dlh_data_src_,source);
		}}
	];
function openLoadTablePapPageList(id,dlh_data_src_,source){
	var url = "<a class=\"a-icon i-edit m-r-xs\" href=\"#myModal-edit\" onclick=\"openLoadTablePapPageListBtn('"+ id+ "','"+dlh_data_src_+"')\" ><i class=\"fa fa-infor-search\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"查看\"></i></a>";
	if(dlh_data_src_=='pt_attach_info'){
		url = "<a class=\"a-icon i-edit m-r-xs\" href=\""+getRootPath()+"/content/attach/download.action?attachId="+source.attach_file_id+"\"  ><i class=\"fa fa-column-selection\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"下载\"></i></a>";

	}

	return url;
}
function openLoadTablePapPageListBtn(id,dlh_data_src_){
	$("#childDetailDiv").html("")
	$("#childDetailDiv").load(getRootPath()+"/supervise/gis/loadDetail.action?viewType=childView&dlhDataId="+id+"&dlh_data_src_="+dlh_data_src_+"&time="+(new Date().getTime()),null,function(){
        dlhTableMapForm.show();
	});
	//window.location.href=getRootPath()+"/dlh/dlhQuery/loadDetail.action?dlhDataId="+id+"&dlh_data_src_="+dlh_data_src_+"&n_"+(new Date().getTime());
}  
</script>

<script src='${sysPath}/js/common/dateUtils.js'></script>
<script src='${sysPath}/js/com/dlh/query/loadTableMapPage.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>