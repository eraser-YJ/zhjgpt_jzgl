<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">

				<button type="button" class="close" data-dismiss="modal">×</button>
				<%--<h4 class="modal-title"></h4>--%>
			</div>
			<div class="modal-body">
				<h3>数据信息查看</h3>
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


					</tbody>
				</table>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn" type="button" id="formDivClose">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script>

	var dlhTableMapForm = {};
	dlhTableMapForm.show = function (){

		$('#myModal-edit').modal('show');

	};
	$(document).ready(function(){
		ie8StylePatch();

		$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});
	});
</script>
