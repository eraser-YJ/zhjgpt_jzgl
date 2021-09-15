<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal-ExInfo" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="entityExInfoTitle">详细信息</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="entityExInfoForm">
					<input type="hidden" id="id" name="id" value="${data.id}"/>
					<table class="table table-td-striped">
						<tbody>
						<c:forEach items="${disItemList}" var="itemRowData" varStatus="abcCond">
							<tr>
								<c:forEach items="${itemRowData.rowList}" var="itemData" varStatus="abcItemCond">
									<td style="width:120px;">${itemData.itemName}</td>
									<td colspan="${itemData.formShowLen}">
										<c:choose>
											<c:when test="${itemData.placeholder eq 'Y' }">
												<div></div>
											</c:when>
											<c:otherwise>
												<input type="text" id="show_${itemData.itemCode}" name="show_${itemData.itemCode}" value="${itemData.itemShowValue}" readonly/>
											</c:otherwise>
										</c:choose>
									</td>
								</c:forEach>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn" type="button" id="closeExInfoBtn">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script src='${sysPath}/js/com/jc/csmp/equipment/info/equipmentExinfoForm.js'></script>