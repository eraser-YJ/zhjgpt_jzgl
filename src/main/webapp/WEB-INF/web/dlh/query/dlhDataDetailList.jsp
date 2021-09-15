<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>

<!-- TODO 面包屑 -->
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="navigationMenu">
		<div class="crumbs">
			${pageHeader}
		</div>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
	    <div class="search-line">
	    	<form class="table-wrap form-table" id="dlhDataDetailQueryForm">

		        <table class="table table-td-striped">
		            <tbody>
		            	<c:forEach items="${pageCondArea}" var="itemData"  varStatus="abcCond">
		            	
		            		<c:if test="${abcCond.index%2 eq 0}">
		            			<tr>
		            		</c:if>
                            <c:set var="inputType" value="text"/>
                            <c:set var="inputValue" value=""/>
							<c:set var="titleStyle" value=""/>
                            <c:if test="${not empty itemData.defaultCon}">
                                <c:set var="inputType" value="hidden"/>
                                <c:set var="inputValue" value="${itemData.defaultValue}"/>
								<c:set var="titleStyle" value="display: none"/>
                            </c:if>
		            		<td style="width: 10%"><span style="${titleStyle}"> ${itemData.title}</span></td>
							<td style="width: 40%">
								<c:choose>							    
								    <c:when test="${itemData.operationType eq 'datetime' }">
								    	<div class="input-group w-p100">
										 <input class="datepicker-input" type="text" id="query_${itemData.cond.begin.code}" name="query_${itemData.cond.begin.code}" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#query_${itemData.cond.end.code} lt" operationAction="${itemData.cond.begin.operationAction}" operationType="${itemData.cond.begin.operationType}" operationKey="${itemData.cond.begin.operationKey}">
										 <div class="input-group-btn w30">-</div>
										 <input class="datepicker-input" type="text" id="query_${itemData.cond.end.code}" name="query_${itemData.cond.end.code}" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#query_${itemData.cond.begin.code} gt"  operationAction="${itemData.cond.end.operationAction}" operationType="${itemData.cond.end.operationType}" operationKey="${itemData.cond.end.operationKey}">
										</div>								    	
								    </c:when>
								    <c:when test="${itemData.operationType eq 'decimal' }">
								    	<div class="input-group w-p100"> 
										 <input type="text" id="query_${itemData.cond.begin.code}" name="query_${itemData.cond.begin.code}" operationAction="${itemData.cond.begin.operationAction}" operationType="${itemData.cond.begin.operationType}" operationKey="${itemData.cond.begin.operationKey}">
										 <div class="input-group-btn w30">-</div>
										 <input type="text" id="query_${itemData.cond.end.code}" name="query_${itemData.cond.end.code}" operationAction="${itemData.cond.end.operationAction}" operationType="${itemData.cond.end.operationType}" operationKey="${itemData.cond.end.operationKey}">
										</div>
								    </c:when>
									<c:when test="${itemData.operationType eq 'dic' && itemData.cond.oper.code!=itemData.defaultCon}">
										<select id="query_${itemData.cond.oper.code}" name = "query_${itemData.cond.oper.code}" operationAction="${itemData.cond.oper.operationAction}" operationType="${itemData.cond.oper.operationType}" operationKey="${itemData.cond.oper.operationKey}">
											<option value="" selected="">-请选择-</option>
											<c:forEach items="${itemData.dicList}" var="dicData"  varStatus="dicCond">
												<option value="${dicData.code}">${dicData.value}</option>
											</c:forEach>

										</select>
									</c:when>
								    <c:otherwise>
                                        <input type="${inputType}" value="${inputValue}" id="query_${itemData.cond.oper.code}" name = "query_${itemData.cond.oper.code}" operationAction="${itemData.cond.oper.operationAction}" operationType="${itemData.cond.oper.operationType}" operationKey="${itemData.cond.oper.operationKey}">

								    </c:otherwise>
								</c:choose>
							</td>			 
							<c:if test="${abcCond.index%2 eq 1}">
		            			</tr>
		            		</c:if> 
					        <c:if test="${abcCond.last and abcCond.index%2 eq 0}">
					        	<td  style="width: 10%"></td>
					        	<td style="width: 40%"></td>
		            			</tr>
		            		</c:if>
					   	</c:forEach>

						</tr>
		            </tbody>
		        </table>
				<div class="btn-tiwc">
					<button class="btn dark" type="button" id="queryDlhDataobject">查 询</button>
					<button class="btn" type="button" id="queryReset">重 置</button>
				</div>
		    </form>
	    </div>

	</section>
	<section class="panel m-t-md" id="body">
		    <button class="btn daochu" style="float: right;margin-right: 30px;margin-top: 10px;background-color: #1572e8;color: white;" type="button" id="excel">导 出</button>
		<form method="post" action="/excelDownload.action" id="excelForm">
			<input type="hidden" id="data_src_" name="data_src_" value="${data_src_}">
			<input type="hidden" id="condJson" name="condJson" value="">

		</form>
			<div class="table-wrap" >

				<table class="table table-striped tab_height " id="dlhDataobjectTable">
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
	</section>
	</section>
</section>

<script type="text/javascript">
dlhDataobjectList_oTableAoColumns = [
	{mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
	<c:forEach items="${pageListArea}" var="itemData" varStatus="abc">		    
	    {'mData':'${itemData.code}' },		
	</c:forEach> 
	{mData: function(source) {
		return openFormPageOnDetailList(source.dlh_data_id_,source.dlh_data_src_);
	}}
];
function openFormPageOnDetailList(id,dlh_data_src_){
	return "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"openFormPageOnDetailListBtn('"+ id+ "','"+dlh_data_src_+"')\" >查看</a>";
}
function openFormPageOnDetailListBtn(id,dlh_data_src_){
	/*$("#dlhDataFormModuleDiv").html("")
	$("#dlhDataFormModuleDiv").load(getRootPath()+"/dlh/dlhQuery/loadDetail.action?dlhDataId="+id+"&dlh_data_src_="+dlh_data_src_+"&n_"+(new Date().getTime()),null,function(){
		myDetailFormModalShow();
	});*/
	window.location.href=getRootPath()+"/dlh/dlhQuery/loadDetail.action?dlhDataId="+id+"&dlh_data_src_="+dlh_data_src_+"&n_"+(new Date().getTime());
}  
</script>
<div id="dlhDataFormModuleDiv"></div>
<script src='${sysPath}/js/common/dateUtils.js'></script>
<script src='${sysPath}/js/com/dlh/query/dlhDataDetailList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>