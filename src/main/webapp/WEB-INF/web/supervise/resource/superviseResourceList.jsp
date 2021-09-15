<%@ page language="java" pageEncoding="UTF-8" import="com.jc.system.dic.domain.Dic,java.util.Map,java.util.ArrayList,com.jc.resource.bean.DlhDataobjectField,java.util.List,com.jc.resource.bean.ResourceOperatorTypeEnum" %>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%
	List<DlhDataobjectField> condList = (List<DlhDataobjectField>) request.getAttribute("pageCondArea");
	List<Map<String, Object>> headList = (List<Map<String, Object>>) request.getAttribute("pageColumnArea");
	if (condList == null) {
		condList = new ArrayList<>();
	}
	if (headList == null) {
		headList = new ArrayList<>();
	}
%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="navigationMenu">
		<div class="crumbs">
			<span>全生命周期</span>
			<span>数据串联监管 > </span><span>生命周期监管信息 > </span><span>${pageTitle}</span>
		</div>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
		<div class="search-line">
			<form class="table-wrap form-table" id="searchForm">
				<input type="hidden" id="sign" name="sign" value="${sign}" />
				<table class="table table-td-striped">
					<tbody>
					<%
						int showCount = 0;
						for (DlhDataobjectField condField : condList) {
							if (condField.getFieldCondShow() == null || condField.getFieldCondShow().equals("0")) {
								continue;
							}
							if (showCount == 0) { %><tr><% }
							%><td style="width: 8%"><%=condField.getFieldName() %></td><%
							if (condField.getItemType().equals(ResourceOperatorTypeEnum.datetime.toString())) {
								%><td style="width: 25%">
								<div class="input-group w-p100">
									<input class="datepicker-input" type="text" id="query_<%=condField.getFieldCode() %>Begin" name="query_<%=condField.getFieldCode() %>Begin" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#query_<%=condField.getFieldCode() %>End lt" operationAction="gt" operationType="<%=condField.getItemType() %>" operationKey="<%=condField.getFieldCode() %>">
									<div class="input-group-btn w30">-</div>
									<input class="datepicker-input" type="text" id="query_<%=condField.getFieldCode() %>End" name="query_<%=condField.getFieldCode() %>End" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#query_<%=condField.getFieldCode() %>Begin gt"  operationAction="lt" operationType="<%=condField.getItemType() %>" operationKey="<%=condField.getFieldCode() %>">
								</div></td>
								<%
							} else if (condField.getItemType().equals(ResourceOperatorTypeEnum.dic.toString())) {
								List<Dic> dicList = condField.getOptionList();
								if (dicList != null && dicList.size() > 0) {
									%><td style="width: 25%"><select name="query_<%=condField.getFieldCode() %>" id="query_<%=condField.getFieldCode() %>">
										<option value="">-请选择-</option>
										<% for (Dic dic : dicList) { %>
											<option value="<%=dic.getCode() %>"><%=dic.getValue() %></option>
										<% } %>
									</select></td><%
								}
							} else {
								%><td style="width: 25%"><input type="text" id="query_<%=condField.getFieldCode() %>" name = "query_<%=condField.getFieldCode() %>" operationAction="like" operationType="<%=condField.getItemType() %>" operationKey="<%=condField.getFieldCode() %>"></td><%
							}
							showCount++;
							if (showCount == 3) { showCount = 0; %></tr><% }
						}
						if (showCount != 0) {
							for (int i = showCount; i >= 0; i--)
							%><td style="width: 8%"></td><td style="width: 25%"></td><%
							%></tr><%
						}
					%>
					</tbody>
				</table>
				<div  class="btn-tiwc">
					<button class="btn" type="button" onclick="superviseResourceList.superviseResourceList()">查 询</button>
					<button class="btn" type="button" onclick="superviseResourceList.queryReset();">重 置</button>
				</div>
			</form>
		</div>
	</section>
	<section class="panel ">
		<div class="table-wrap"><table class="table table-striped tab_height" id="gridTable"></table></div>
		<section class="clearfix" id="footer_height">
			<section class="form-btn fl m-l"></section>
		</section>
	</section>
	<div id="formModule"></div>
</section>
<script>
	var superviseResourceList_oTableAoColumns = [{mData: 'no', sTitle: '序号', bSortable: false, sWidth: 60}];
	<%
		for (Map<String, Object> headMap : headList) {
			Integer listShow = (Integer) headMap.get("listShow");
			if (listShow != null && listShow.intValue() == 1) {
				%>superviseResourceList_oTableAoColumns.push({mData: '<%=((String) headMap.get("code")) %>', sTitle: '<%=((String) headMap.get("title")) %>', bSortable: false});<%
			}
		}
	%>
	superviseResourceList_oTableAoColumns.push({mData: function(source) {
		return "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"superviseResourceList.look('" + source.dlh_data_id_+ "')\" role=\"button\">查看</a>";
	}, sTitle: '操作', bSortable: false, sWidth: 60});
</script>
<script src='${sysPath}/js/com/jc/supervise/resource/superviseResourceList.js'></script>
<%@ include file="/WEB-INF/web/supervise/resource/superviseResourceDetail.jsp" %>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>