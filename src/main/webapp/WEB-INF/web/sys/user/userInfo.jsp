<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="navigationMenu">
		<h1></h1><div class="crumbs"></div>
	</header>
	<section class="panel m-t-md search-box clearfix">
		<h2 class="panel-heading clearfix">个人信息</h2>
		<div class="flex-container user-info" style="display: flex;display: -moz-flex;">
		<div style="margin:30px 30px 0 50px;" class="flex-item">

			<section class="dis-table-cell w105" >
				<img src="${sysPath}/images/touphoto.png" id="userPhotoImg"  style="background: #f5f5f5;width:134px;height:188px;margin-bottom:10px;"/>
				<div class="form-btn"><button class="btn dark w105" type="button" data-target="#myModal2"data-toggle="modal" style="margin-left: 0 !important;">上传头像</button></div>
			</section>

		</div>
		<form class="table-wrap form-table flex-item" id="userInfoForm" style="width:100%;" >
    		<input type="hidden" id="id" name="id" value="0" />
        	<input type="hidden" id="token" name="token" value="0" />
        	<input type="hidden" id="modifyDate" name="modifyDate" />
			<input type="hidden" id="mobile" name="mobile" />
			<input type="hidden" id="mobileOld" name="mobileOld" />
			<input type="hidden" id="groupTel" name="groupTel" />
			<input type="hidden" id="email" name="email" />
			<input type="hidden" id="officeAddress" name="officeAddress" />
			<input type="hidden" id="officeTel" name="officeTel" />
			<input type="hidden" id="entryPoliticalDate" name="entryPoliticalDate" />
			<input type="hidden" id="cardBank" name="cardBank" />
			<input type="hidden" id="cardName" name="cardName" />
			<input type="hidden" id="payCardNo" name="payCardNo" />
			<table class="table table-td-striped"><tbody>
				<tr>
					<td class="w240">登录名称</td>
					<td><input type="text" id="loginName" name="loginName" readonly="readonly" style="width:30%;"/></td>
				</tr>
				<tr>
					<td>姓名</td>
					<td><input type="text" id="displayName" name="displayName" readonly="readonly" style="width:30%;"/></td>
				</tr>
                <%--<tr>--%>
                	<%--<td>用户头像</td>--%>
                    <%--<td>--%>
						<%--<section class="dis-table-cell w105" >--%>
							<%--<img src="${sysPath}/images/demoimg/iphoto.jpg" id="userPhotoImg" height="105" />--%>
							<%--<div class="form-btn"><button class="btn dark w105" type="button" data-target="#myModal2"data-toggle="modal" style="margin-left: 0 !important;">上传头像</button></div>--%>
						<%--</section>--%>
					<%--</td>--%>
				<%--</tr>--%>
				<tr>
					<td>出生日期</td>
					<td><input id="birthday" name="birthday" class="datepicker-input" type="text" data-date-format="yyyy-MM-dd" style="width:30%;"/></td>
			  	</tr>
				<tr>
					<td>民族</td>
					<td><dic:select name="ethnic" id="ethnic" dictName="ethnic" parentCode="user" headName="-请选择-" headValue="" defaultValue="" cssStyle="width:30%;"/></td>
				</tr>
                <tr>
					<td>婚姻状况</td>
					<td><dic:select name="maritalStatus" id="maritalStatus" dictName="maritalStatus" parentCode="user" headName="-请选择-" headValue="" defaultValue="" cssStyle="width:30%;"/></td>
				</tr>
                <tr>
					<td>籍贯</td>
					<td><input type="text" id="hometown" name="hometown" style="width:30%;"/></td>
				</tr>
                <tr>
					<td>学历</td>
					<td><dic:select name="degree" id="degree" dictName="degree" parentCode="user" headName="-请选择-" headValue="" defaultValue="" cssStyle="width:30%;"/></td>
				</tr>

                <tr>
					<td>密码提示问题</td>
					<td>
                    	<dic:select name="question" id="question" dictName="question" parentCode="user" headName="-请选择-" headValue="" defaultValue="" cssStyle="width:30%;"/>
					</td>
				</tr>
                <tr>
					<td>密码问题答案</td>
					<td>
                    	<input type="text" id="answer" name="answer" style="width:30%;">
					</td>
				</tr>
			</tbody></table>
			<section class="form-btn m-b-lg"><button class="btn dark ok" type="button" id="saveBtn" style="margin-left: -210px !important;">保 存</button></section>
		</form>
		</div>
   </section>
</section>
<!-- webuploader start -->
<div class="modal fade panel" id="myModal2" aria-hidden="false">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" id="closebtn" data-dismiss="modal">×</button>
				<h4 class="modal-title">上传头像</h4>
			</div>
			<div class="modal-body">
				<div id="wrapper">
					<div id="container">
						<div id="userUploader" class="attach">
							<div class="queueList">
								<div id="dndArea" class="placeholder">
									<div id="userFilePicker"></div>
									<p></p>
								</div>
							</div>
							<div class="statusBar" style="display:none;">
								<div class="progress">
									<span class="text">0%</span>
									<span class="percentage"></span>
								</div>
								<div class="info"></div>
								<div class="btns"><div id="filePickerBtn" class="attachBtn"></div><div class="uploadBtn">开始上传</div></div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button"  data-dismiss="modal">关 闭</button>
			</div>
		</div>
	</div>
</div>
<!-- webuploader end -->
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<script src="${sysPath}/js/com/sys/user/userInfo.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/user/userInfo.validate.js" type="text/javascript"></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>