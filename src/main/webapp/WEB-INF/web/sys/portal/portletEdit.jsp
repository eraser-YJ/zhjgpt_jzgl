<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" id="closebtn" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="titleportlet">门户组件添加</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="portletForm">
				<input type="hidden" id="id" name="id">
				<input type="hidden" id="token" name="token" value="${token}">
				<input type="hidden" id="modifyDate" name="modifyDate">
					<table class="table table-td-striped">
						<thead>
						<tr style="height:0;">
							<td style="width:30%;height:0;padding:0;"></td>
							<td style="width:33%;height:0;padding:0;"></td>
							<td style="width:5%;height:0;padding:0;"></td>
							<td style="width:32%;height:0;padding:0;"></td>
						</tr>
						</thead>
						<tbody>
							<tr>
								<td><span class="required">*</span>门户名称</td>
								<td colspan="3"><select id="portalId" name="portalId">
											<option value="">-请选择-</option>
										<c:forEach items="${portalList}" var="portalvo" varStatus="status">
											<option value="${portalvo.id }">${portalvo.portalName }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>  
								<td><span class="required">*</span>门户组件标题</td>
								<td colspan="3">
									<input type="text" id="letTitle" name="letTitle"  onkeypress="if(event.keyCode==13||event.which==13){return false;}">
								</td>
							</tr>
							<tr>  
								<td><span class="required">*</span>组件视图类型</td>
								<td colspan="3">
									<select id="viewType" name="viewType" onchange="portlet.clearFunction(this)">
									<option value="">-请选择-</option>
									<option value="onetable">单组件</option>
									<option value="moretable">多组件</option>
									<option value="shortcut">快捷方式组件</option>
									<option value="textareaEdit">文本域组件</option>
									<option value="printEdit">单图片组件</option>
									<option value="friendlyLink">友情链接组件</option>
									</select>
									<input type="hidden" id="functionId" name="functionId">
									<input type="hidden" id="functionName" name="functionName">
									<select id="tempfunctions" name="tempfunctions" style="display: none;">
										<c:forEach items="${functionList}" var="functionvo" varStatus="status">
											<option id="${functionvo.viewType }_${functionvo.id }" value="${functionvo.id }">${functionvo.funName }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr id="isnotedit">  
								<td><span class="required">*</span>功能名称</td>
								<td>
									<select id="optionfunctions" name="optionfunctions" multiple="multiple" style="text-align:left;height:150px;" ondblclick="portletEdit.addFunction()" >
										
									</select>
								</td>
								<td style="padding:0;">
									<div class="ms2side__options" style="padding-top: 6.5px;padding-left: 2px;">
									<p class="AddOne ms2side__hide" title="添加" onclick="portletEdit.addFunction()" ><span></span></p>
									<p class="RemoveOne ms2side__hide" title="删除" onclick="portletEdit.removeFunction()" ><span></span></p>
									</div>
								</td>
								<td>
									<select id="optfunctions" name="optfunctions" multiple="multiple" class="selectValider selectMaxValider" style=" text-align:left;height:150px;" ondblclick="portletEdit.removeFunction()" >
										
									</select>
								</td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" id="portletbtn">保 存</button>
				<button class="btn" type="button" onclick="$('#myModal-edit').modal('hide');">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script src="${sysPath}/js/com/sys/portal/portletEdit.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/portal/portlet.validate.js" type="text/javascript"></script>
