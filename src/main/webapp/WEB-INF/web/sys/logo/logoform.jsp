<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">系统登录页样式表</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="logoForm">
					<input type="hidden" id="id" name="id">
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate">
					<input type="hidden" id="logoFlag" name = "logoFlag" value="">
<table class="table table-td-striped">
	<tbody>
	<tr >
		<td class="w100"><span class="required">*</span>
登录页样式名称
		</td>
		<td class="w300">
	<input type="text" id="logoName" name = "logoName" value="">
 
		</td>
	</tr>
	<tr >
		<td class="w100"><span class="required">*</span>
登录页样式标识
		</td>
		<td class="w300">
	<input type="text" id="logoSign" name = "logoSign" value="">
 
		</td>
	</tr>
	<tr >
		<td class="w100">
登录页预览图片路径
		</td>
		<td class="w300">
			<section class="dis-table-cell w105" >
				<div class="form-btn">
					<img src="${sysPath}/images/demoimg/iphoto.jpg" id="logoImg" height="105" width="105"/>
					<button class="btn dark w105" type="button" data-target="#file-edit2" id="logoImgUpload" data-toggle="modal">上传图片</button>
				</div>
			</section>
		</td>
	</tr>
	</tbody>
</table>
				</form>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" id="saveOrModify">保存继续</button>
				<button class="btn" type="button" id="saveAndClose">保存退出</button>
				<button class="btn" type="button" id="formDivClose">关 闭</button>
			</div>
		</div>
	</div>
</div>

<!-- //单上传实例，限制只能上传图片 -->
<div class="modal fade panel" id="file-edit2" aria-hidden="false">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" id="closebtn" data-dismiss="modal">×</button>
				<h4 class="modal-title">添加上传文件</h4>
			</div>
			<div class="modal-body">

				<div id="wrapper">
					<div id="container">
						<div id="uploader2" class="attach">
							<div class="queueList">
								<div id="dndArea2" class="placeholder">
									<div id="filePicker2"></div>
									<p></p>
								</div>
							</div>
							<div class="statusBar" style="display:none;">
								<div class="progress">
									<span class="text">0%</span>
									<span class="percentage"></span>
								</div><div class="info"></div>
								<div class="btns">
									<div id="filePickerBtn2" class="attachBtn"></div><div class="uploadBtn">开始上传</div>
								</div>
							</div>
						</div>
					</div>
				</div>

			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" id="closeModalBtn" data-dismiss="modal">关 闭</button>
			</div>
		</div>
	</div>
</div>

<script src="${sysPath}/js/com/sys/logo/logo.form.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/logo/logo.validate.js" type="text/javascript"></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>