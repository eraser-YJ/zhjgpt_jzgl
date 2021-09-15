<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
    <div class="modal-dialog w900">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">×</button>
                <h4 class="modal-title" id="actionTitle">新增</h4>
            </div>
            <div class="modal-body">
            	
<form class="table-wrap form-table" id="portalFriendlylinkForm">
		<input type="hidden" id="id" name = "id"  >
		<input type="hidden" id="token" name="token" value="${data.token}">
		<input type="hidden" id="modifyDate" name="modifyDate">
			<table class="table table-td-striped">
				<tbody>
					<tr>
						<td style="width: 18%;"><span class="required">*</span>友情链接名称</td>
						<td style="width: 32%;">
				       		<input type="text" id="linkName" name = "linkName" >
						</td>
						<td style="width: 18%;"><span class="required">*</span>友情链接地址</td>
						<td style="width: 32%;">
				       		<input type="text" id="linkUrl" name = "linkUrl" >
						</td>
					</tr>
					<tr>
						<td><span class="required">*</span>所属门户</td>
						<td>
				       		<select id="portalId" name="portalId" onchange="portalFriendlylinkModule.findportlets(this.value)">
								<option value="">-请选择-</option>
								<c:forEach items="${data.portallist}" var="portalvo" varStatus="status">
									<option value="${portalvo.id }">${portalvo.portalName }</option>
								</c:forEach>
							</select>
						</td>
						<td><span class="required">*</span>所属业务组件</td>
						<td>
				       		<select id="portletId" name="portletId">
								<option value="">-请选择-</option>
							</select>
							<select id="tempportlet" name="tempportlet" style="display: none;">
								<c:forEach items="${data.portletlist}" var="portletvo" varStatus="status">
									<option name="${portletvo.portalId }" value="${portletvo.id }">${portletvo.letTitle }</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td><span class="required">*</span>排序号</td>
						<td>
				       		<input type="text" id="sequence" maxlength="3" name = "sequence" >
						</td>
				</tbody>
			</table>
		</form>


            </div>
            <div class="modal-footer form-btn">
                <!-- <button class="btn dark" type="button" id="saveOrModify">保存继续</button> -->
                <button class="btn dark" type="button" id="saveClose">保 存</button><button class="btn" type="button" id="formDivClose">关 闭</button>
            </div>
        </div>
    </div>
</div>
<script src='${sysPath}/js/com/sys/portal/portalFriendlylinkedit.js'></script>
<script src='${sysPath}/js/com/sys/portal/portalFriendlylink.validate.js'></script>
