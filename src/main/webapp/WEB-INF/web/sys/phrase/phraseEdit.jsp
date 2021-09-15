<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="titlephrase">常用词管理</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="phraseForm">
					<input type="hidden" id="id" name="id">
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate">
					<input type="hidden" id="tempisAdmin" name="tempisAdmin" value="${loguservo.isAdmin }">
					<table class="table table-td-striped">
						<tbody>
							<tr>  
								<td style="width: 30%;">常用词类别</td>
								<td style="width: 70%;" id="loadtype">
									<c:if test="${loguservo.isAdmin == 1 }">
										<input type="hidden" id="phraseType" name="phraseType" value="0">公共
									</c:if>
									<c:if test="${loguservo.isAdmin == 0 }">
										<input type="hidden" id="phraseType" name="phraseType" value="1">个人
									</c:if>
								</td>
							</tr>
							<tr>  
								<td><span class="required">*</span>常用词内容</td>
								<td><textarea id="content" name="content"></textarea>
                        			<div>您还可以输入<span id="count"></span>个文字。 </div>
								</td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" id="phrasebtn">保 存</button>
				<button class="btn" type="button" onclick="$('#myModal-edit').modal('hide');">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script src="${sysPath}/js/com/sys/phrase/phraseEdit.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/phrase/phrase.validate.js" type="text/javascript"></script>