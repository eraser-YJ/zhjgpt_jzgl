<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<!-- TODO 面包屑 -->

<section class="scrollable padder jcGOA-section" id="scrollable">


	<div class="table-wrap" >
		<table class="table table-striped tab_height " id="loadTablePapPageTable">
			<thead>
			<tr>

				<th>文件夹</th>
				<th>创建时间</th>
				<th>模型路径</th>
				<th>业务字段名称</th>
				<th>业务数据标识</th>
				<th>说明</th>
				<th style="width:100px;">操作</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach items="${dlhFileList}" var="tname" varStatus="abb">
			<tr>
				<td>${dlhFileList.fileName}</td>
				<td>${dlhFileList.createDate}</td>
				<td>${dlhFileList.objUrl}</td>
				<td>${dlhFileList.yewucolname}</td>
				<td>${dlhFileList.yewuid}</td>
				<td>${dlhFileList.remark}</td>
				<td>
					<a class="a-icon i-edit m-r-xs" href="#myModal-edit" onclick="openLoadTablePapPageListBtn('id+','dlh_data_src_')" >
					<i class="fa fa-infor-search" data-toggle="tooltip" data-placement="top" data-container="body" data-original-title="查看"></i>
					</a>
				</td>
			</tr>
			</c:forEach>
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

	function openLoadTablePapPageListBtn(id,dlh_data_src_){
		$("#loadTablePapPageDiv").html("")
		$("#loadTablePapPageDiv").load(getRootPath()+"/dlh/dlhQuery/loadDetail.action?viewType=view&dlhDataId="+id+"&dlh_data_src_="+dlh_data_src_+"&n_"+(new Date().getTime()),null,function(){
			dlhTableMapForm.show();
		});
		//window.location.href=getRootPath()+"/dlh/dlhQuery/loadDetail.action?dlhDataId="+id+"&dlh_data_src_="+dlh_data_src_+"&n_"+(new Date().getTime());
	}
</script>

<script src='${sysPath}/js/common/dateUtils.js'></script>

<%@ include file="/WEB-INF/web/include/foot.jsp"%>