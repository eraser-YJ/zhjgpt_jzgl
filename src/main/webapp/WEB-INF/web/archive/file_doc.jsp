<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String base = request.getContextPath();
	String IPAddr = null;
	IPAddr = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() ;
%>

<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<script src="${sysPath}/js/com/common/Base64.js" type="text/javascript"></script>
<script async src="${sysPath}/js/com/archive/folderPathBar.js" type="text/javascript"></script>
<script async src="${sysPath}/js/com/archive/FolderSelectTree.js" type="text/javascript"></script>
<script async src="${sysPath}/js/com/archive/file_doc.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/archive/ArchiveFolder.validator.js"  type="text/javascript"></script>
<script src="${sysPath}/js/com/archive/docFormView.js" type="text/javascript"></script>
<script type="text/javascript">
var basePath_ = "<%=IPAddr%>";
	function oTableSetButtons(id,extStr5) {
		var bat = "<shiro:hasPermission name='recycle:delectfiling'><a class='a-icon i-remove m-l-xs m-r-xs' href='#' onclick='archive_doc.rowDeleteClick("+id+")' role='button' data-toggle='modal'><i class='fa fa-remove' data-toggle='tooltip' data-placement='top' title='删除' data-container='body' data-original-title='删除'></i></a></shiro:hasPermission>";
		var batsend = "";
		if(extStr5!=1){
			 batsend ="<shiro:hasPermission name='recycle:delectfiling'><a class='a-icon i-new m-l-xs m-r-xs' href='#' onclick='archive_doc.send("+id+")' role='button' data-toggle='modal'>归档</a></shiro:hasPermission>";

		}
			return bat;
};

function oTableDeleteButtons(id) {
	var batUpdate = "<shiro:hasPermission name='recycle:updatefiling'><a class='a-icon i-new m-l-xs m-r-xs' href='#' onclick='archive_doc.rename("+id+")' role='button' data-toggle='modal'>重命名</a></shiro:hasPermission>";
	var batDelete = "<shiro:hasPermission name='recycle:delectfiling'><a class='a-icon i-remove m-l-xs m-r-xs' href='#' onclick='archive_doc.rowDelete("+id+")' role='button' data-toggle='modal'><i class='fa fa-remove' data-toggle='tooltip' data-placement='top' title='删除' data-container='body' data-original-title='删除'></i></a></shiro:hasPermission>";
	var batCopy = "<shiro:hasPermission name='recycle:copyfiling'><a class='a-icon i-new m-l-xs m-r-xs' href='#' onclick='archive_doc.clickCut("+id+")' role='button' data-toggle='modal'>移&nbsp;动</a></shiro:hasPermission>";
	return batUpdate+batDelete+batCopy;
};


/*
 * 
 archive_doc.createDirOpCell = function(folder) {
		var option = "<td>";
		option += "<div class=\"dropdown inline\"> ";
		option += "<a href=\"#\" type=\"button\" class=\"btn-uploading\" data-toggle=\"dropdown\"><i data-original-title=\"更多\" data-container=\"body\" title=\"\" data-placement=\"top\" data-toggle=\"tooltip\"  class=\"fa fa-more-list\"></i></a> ";
		option += "        <ul class=\"dropdown-menu animated fadeInRight\">            ";
		if(archive_doc.permissionValue.substring(0, 1)==1){
			if(archive_doc.permissionValue.substring(5, 6)==1){
				option += "          <li><a type=\"button\" href=\"#version-management\" role=\"button\" data-toggle=\"modal\">复制</a></li> ";
				option += "          <li><a type=\"button\" href=\"#document-the-audit\" role=\"button\" data-toggle=\"modal\">剪切</a></li> ";
			}
			if(archive_doc.permissionValue.substring(6, 7)==1){
				option += "			 <li><a type=\"button\" href=\"javascript:archive_doc.rename('#dir_" + folder.id + "');\" role=\"button\" >重命名</a></li> ";
			}
			if(archive_doc.permissionValue.substring(4, 5)==1){
				option += "          <li><a type=\"button\" href=\"#associated-Document\" role=\"button\" data-toggle=\"modal\">删除</a></li>  ";
			}
		}
		option += "			 <li><a type=\"button\" href=\"#document-information\" role=\"button\" data-toggle=\"modal\">授权</a></li> ";
		option += "        </ul> ";
		option += "    </div> </td>";
		return option;
	};
 
 */
</script>

<section class="scrollable padder jcGOA-section" id="scrollable">
<header class="con-header pull-in" id="header_1">
    <div class="con-heading fl" id="navigationMenu">
        <h1></h1>
        <div class="crumbs"></div>
    </div>
</header>
	<section class="panel email m-t-md" id="dSection">
		<section class="email-btn form-btn document-btn">
			<div role="search" class="email-search fr">
				<div class="input-append m-b-none">
					<input type="text" class="form-control aside-md w200"
						placeholder="请输入关键字搜索当前页" id="search">
						<!-- 
					<button type="submit" class="btn">
						<i class="fa fa-search"></i>
					</button> -->
				</div>
			</div>
			<button type="button" class="btn w0" id="btnReturnDisabled" disabled="disabled">
				<i data-original-title="返回" data-container="body" title=""
					data-placement="top" data-toggle="tooltip" class="fa fa-reply"></i>
			</button>
			<button type="button" class="btn w0 hide" id="btnReturn" >
				<i data-original-title="返回" data-container="body" title=""
					data-placement="top" data-toggle="tooltip" class="fa fa-reply"></i>
			</button>
			<!-- <span id="btnNew" ><button type="button" class="btn w40 btn-new">
				<i data-original-title="新建文件" data-container="body" title=""
					data-placement="top" data-toggle="tooltip" class="fa fa-plus"></i>
			</button></span>
			<div class="document-new">
				<a href="javascript:void(0)" id="newWord" class="btn i-word"><i
					class="fa">Word</i></a> <a href="javascript:void(0)" id="newExl"
					class="btn i-excel"><i class="fa">Excel</i></a> <a
					href="javascript:void(0)" id="newPPT" class="btn i-pdf"><i
					class="fa">PPT</i></a>


				   <a href="#" class="btn i-file"><i class="fa fa-folder-close"></i></a>
                               <a href="#" class="btn i-file"><i class="fa fa-folder-close"></i></a>
                                <a href="#" class="btn i-default"><i class="fa fa-document-text"></i></a>
			</div>
			<span id="upload" ><button type="button" class="btn w40 btn-uploading" id="btnUpload">
				<i data-original-title="上传" data-container="body" title=""
					data-placement="top" data-toggle="tooltip" class="fa fa-upload1"></i>
			</button></span> -->


			<shiro:hasPermission name='folder:newfiling'><button class="btn" type="button" id="btnNewFolder">新建文件夹</button></shiro:hasPermission>
			<shiro:hasPermission name='folder:filingpermission'><button class="btn" type="button" id="btnAuth">授 权</button></shiro:hasPermission>
			<%-- <shiro:hasPermission name='folder:delete'><button class="btn" type="button" id="btnDelete">删除文件夹</button></shiro:hasPermission> --%>
			<!--	<button class="btn" type="button" id="btnDownload">下载</button>
			（下载只针对文档,根据选择的行显示和隐藏该按钮）
			<button class="btn" type="button">复制</button>
								<button class="btn" type="button">剪切</button>
                                <button class="btn" type="button">删除</button>
                              
                                <button class="btn" type="button">收藏</button>
                                <button class="btn" type="button">授权</button>
								
                                <div class="dropdown inline">
                                    <button class="btn dropdown-toggle" data-toggle="dropdown" type="button">操作<i class="fa fa-chevron-down"></i></button>
                                    <ul class="dropdown-menu animated fadeInRight">
                                        <li><a href="#">移动</a></li>
                                        <li><a href="#">复制到</a></li>
                                        <li><a href="#">在线编辑</a></li>
                                        <li><a href="#">版本更新</a></li>
                                        <li><a href="#">发送到知识库</a></li>
                                    </ul>
                                </div>
                                <div class="dropdown inline">
                                    <button class="btn dropdown-toggle" data-toggle="dropdown" type="button">更多<i class="fa fa-chevron-down"></i></button>
                                    <ul class="dropdown-menu animated fadeInRight">
                                        
										<li><a type="button" href="#document-information" role="button" data-toggle="modal">文档信息</a></li>
                                        <li><a type="button" href="#version-management" role="button" data-toggle="modal">版本管理</a></li>
                                        <li><a type="button" href="#document-the-audit" role="button" data-toggle="modal">文档审计</a></li>
                                        <li><a type="button" href="#associated-Document" role="button" data-toggle="modal">关联文档</a></li>
                                        <li><a type="button" href="#authority-management" role="button" data-toggle="modal">权限管理</a></li>
                                    </ul>
                                </div>-->
			<input type="hidden" id="folderId" value="${folder.id }" /> <input
				type="hidden" id="token" name="token" value="${token}">
			<input type="hidden" id="folder" name ="folder" />
		</section>
		<div class="table-wrap" id="folderTable">
			<!--start 列表-->
			<table class="document-table m-b-md list-table" id="doc_table">
				<thead>
					<tr><!--
						<th class="w46">
							 <input type="checkbox"> 
						</th>-->
						<th class="w20" style ="width:800px;">文件名</th>
						<!-- <th class="w120">操作</th> -->
						<!-- <th class="w74">状态</th>
						<th>格式</th>
						<th class="w74">大小</th> -->
						<th class="w20" style ="width:200px;">归档时间</th>
						<th class="w20" style ="width:150px;">归档人</th>
						<th class="w20" style ="width:250px;">操作</th>
					</tr>
				</thead>
				<!-- 数据集 -->
				<tbody>

				</tbody>
			</table>
			<!--end 列表-->
		</div>
	</section>
</section>
<!--start 文档信息 弹出层-->
<div class="modal fade panel" id="document-information"
	aria-hidden="false">
	<div class="modal-dialog w1100">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">文档信息</h4>
			</div>
			<div class="modal-body">
				<div class="form-table">
					<table class="table table-td-striped">
						<tbody>
							<tr>
								<td class="w140">文档名称</td>
								<td id="info_dmName">xx需求文档</td>
								<td class="w140">文档大小</td>
								<td class="w140" id="info_dmSize">52.02MB</td>
								<td class="w140">创建时间</td>
								<td class="w170" id="info_createDate">2014-04-01 10:15</td>
							</tr>
							<tr>
								<td class="w140">文档目录</td>
								<td id="info_dmDir">/研发部/OA产品部/xxx组/</td>
								<td class="w140">最新版本</td>
								<td class="w140" id="info_currentVersion">1.0</td>
								<td class="w140">修改时间</td>
								<td class="w170" id="info_modifyDate">2014-04-11 10:15</td>
							</tr>
							<tr>
								<td class="w140">文档编号</td>
								<td id="info_seq">JCYF-RD-0001-GOA-123456789</td>
								<td class="w140">创建者</td>
								<td class="w140" id="info_createUser">刘锡来</td>
								<td class="w140">锁定状态</td>
								<td class="w170"   id="info_dmLockStatus"><span>未锁定</span><a href="#" class="a-icon i-new fr"><i
										class="fa fa-locked" data-original-title="锁定"
										data-container="body" title="" data-placement="top"
										data-toggle="tooltip"></i>锁定</a><a href="#"
									class="a-icon i-new fr"><i class="fa fa-lock-open"
										data-original-title="锁定" data-container="body" title=""
										data-placement="top" data-toggle="tooltip"></i>解除锁定</a></td>
							</tr>
							<!-- 
							<tr>
								<td class="w140">关键字</td>
								<td id="info_keyWords">xxx，xxx1，sss <a data-toggle="modal" role="button"
									href="#" class="a-icon i-edit fr"><i
										data-original-title="修改" data-container="body" title=""
										data-placement="top" data-toggle="tooltip" class="fa fa-edit2"></i></a></td>
								<td class="w140">文档类型</td>
								<td class="w140" id="info_dmType">工程类 <a data-toggle="modal" role="button"
									href="#" class="a-icon i-edit fr"><i
										data-original-title="修改" data-container="body" title=""
										data-placement="top" data-toggle="tooltip" class="fa fa-edit2"></i></a></td>
								<td class="w140"></td>
								<td class="w170"></td>
							</tr> -->
						</tbody>
					</table>
				</div>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" data-dismiss="modal">关 闭</button>
			</div>
		</div>
	</div>
</div>
<!--end 文档信息 弹出层-->
<!--start 版本管理 弹出层-->
<div class="modal fade panel" id="version-management"
	aria-hidden="false">
	<div class="modal-dialog w1100">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">版本管理</h4>
			</div>
			<div class="modal-body">
				<table class="table table-striped first-td-tc" id="versionTable">
					<thead>
						<tr>
							<th style="width:105px;">版本号</th>
							<th>版本说明</th>
							<th>更新者</th>
							<th>更新时间</th>
						</tr>
					</thead>
					<tbody>
						
					</tbody>
				</table>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" data-dismiss="modal">关 闭</button>
			</div>
		</div>
	</div>
</div>
<!--end 版本管理 弹出层-->
<!--start 文档审计 弹出层-->
<div class="modal fade panel" id="document-the-audit"
	aria-hidden="false">
	<div class="modal-dialog w1100">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">文档审计</h4>
			</div>
			<div class="modal-body">
				<table id="audithisList" class="table table-striped">
					<thead>
						<tr>
							<th style="width:105px;">操作人</th>
							<th>日志</th>
							<th>IP</th>
							<th>时间</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" data-dismiss="modal">关 闭</button>
			</div>
		</div>
	</div>
</div>
<!--end 文档审计 弹出层-->
<!--start 关联文档 弹出层-->
<form id="pubDocRelateForm" name="pubDocRelateForm">
	<input type="hidden" id="id" name="id" />
	<div class="modal fade panel" id="pubDoc-relate" aria-hidden="false">
		<div class="modal-dialog w900">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">×</button>
					<h4 class="modal-title">关联文档</h4>
				</div>
				<div class="modal-body">
		            <div class="table-wrap">
		                <table class="table table-striped tab_height" id="pubDocRelateTable">
		                    <thead>
		                        <tr>
									<th>关联文档名称</th>
									<th class="w170">关联文档目录</th>
									<th class="w170">操作</th>
		                        </tr>
		                    </thead>
		                    <tbody>
		                       
		                    </tbody>
		                </table>
		            </div>
				</div>
				<div class="modal-footer form-btn">
					<button class="btn dark" type="button" id="chooseRelate">关联文档</button>
					<button class="btn" type="reset" onclick="$('#pubDoc-relate').modal('hide')">关 闭</button>
				</div>
			</div>
		</div>
	</div>
</form>
<!--end 关联文档 弹出层-->
<!--start 关联 弹出层-->
<div class="modal fade panel" id="relate-doc" aria-hidden="false">
	<div class="modal-dialog w1100">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">关联</h4>
			</div>
			<div class="modal-body">
				<form class="m-l-n" id="queryRelateForDocForm" >
					<!--start 查询条件-->
					<section class="clearfix">
						<div class="form-table m-l-20">
							<table class="table table-td-striped m-t-md">
								<tbody>
									<tr>
										<td class="w140">文档名称</td>
										<td style="width:40%;"><input type="text" id="dmName" name="dmName"></td>
										<td class="w140">创建者</td>
										<td style="width:40%;">
											<div id="queryCreateUserTree"></div>
										</td>
									</tr>
									<tr>
										<td class="w140">文档编号</td>
										<td><input type="text" id="seq" name="seq"></td>
										<td>创建时间</td>
										<td>
				                           <div class="input-group w-p100">
					                            <input class="datepicker-input" type="text" id="createDateBegin" name="createDateBegin" value="" data-date-format="yyyy-MM-dd" data-ref-obj="#createDateEnd lt">
					                            <span class="input-group-btn w30">-</span>
					                            <input class="datepicker-input" type="text" id="createDateEnd" name="createDateEnd" value="" data-date-format="yyyy-MM-dd" data-ref-obj="#createDateBegin gt">
					                       </div>
										</td>
									</tr>
								</tbody>
							</table>
							<section class="form-btn m-b-lg  m-t-md">
				               <button id="queryRelateForDoc" class="btn dark query-jump" type="button" >查 询</button>
				               <button id="resetRelateForDoc" class="btn" type="button">重 置</button>
							</section>
						</div>
					</section>
					<!--end 查询条件-->
				</form>
			       <section class="panel tab-content">
			           <div class="table-wrap">
					<!--start 查询结果-->
							<table class="table table-striped first-td-tc" id="relateForDocTable">
								<thead>
									<tr>
										<th class="w46"><input type="checkbox"></th>
										<th>文档名称</th>
										<th class="w90">文档编号</th>
										<th class="w200">创建者</th>
									</tr>
								</thead>
								<tbody>
								
								</tbody>
							</table>
						</div>
       			</section>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="submit" id="saveRelate">确 定</button>
				<button class="btn" type="button" data-dismiss="modal">关 闭</button>
			</div>
		</div>
	</div>
</div>
<!--end 关联 弹出层-->
<!--start 人员选择弹出层-->
<div class="modal fade panel" id="myModal" aria-hidden="false">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">组织人员选择</h4>
			</div>
			<div class="modal-body" id="deptAndPersonControlTree"></div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="submit">保 存</button>
				<button class="btn" type="reset">重 置</button>
			</div>
		</div>
	</div>
</div>
<!--end 人员选择弹出层-->
<!--start 权限管理 弹出层-->
<div class="modal fade panel" id="authority-management" aria-hidden="false">
	<div class="modal-dialog w1100">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">授权</h4>
			</div>
			<div class="modal-body">
				<table id="permission" class="table table-striped">
					<thead>
						<tr>
							<th class="w100" style="width:50%;">人员/部门</th>
							<th class="w100" style="width:18%;">浏览</th>
							<th class="w100" style="width:18%;"><!-- /上传 -->下载</th>
							<!-- <th class="w100" style="width:6%;">编辑</th>
							<th class="w100" style="width:6%;">删除</th>
							<th class="w100" style="width:6%;">复制/剪切</th>
							<th class="w100" style="width:8%;">重命名</th>
							<th class="w100" style="width:6%;">收藏</th>
							<th class="w100" style="width:6%;">版本管理</th>
							<th class="w100" style="width:6%;">文档审计</th>
							<th class="w100" style="width:6%;">文档关联</th> -->
							<th class="w100" style="width:14%;">操作</th>
						</tr>
					</thead>
					<tbody>
						
					</tbody>
				</table>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" id="addBtn">添加权限</button>
				<button class="btn" type="button" data-dismiss="modal">关 闭</button>
			</div>
		</div>
	</div>
</div>
<!--end 权限管理 弹出层-->
<!--start 附件管理弹出层-->
<div class="modal fade panel" id="filing" aria-hidden="false">
	<div class="modal-dialog w1100">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">归档信息</h4>
			</div>
			<div class="modal-body">
			<form id="downLoadForm" >
				<div class="form-table">
				
				<input id="filingId" name="filingId" type="hidden">
					<table class="table table-td-striped">
						<tbody>
							<tr>
								<td class="w170">文档名称</td>
								<td id="filing_dmName"></td>
								<td class="w170">归档人</td>
								<td id="filing_createUser"></td>
							</tr>
							<tr>
								<td class="w170">文档目录</td>
								<td id="filing_dmDir"></td>
								<td class="w170">归档时间</td>
								<td id="filing_createDate"></td>
							</tr>
						</tbody>
					</table>
					
				</div>
				</form>
				<div>
				<table id="permissionFiling" class="table table-striped m-t-md">
					<thead>
						<tr>
							<th class="w100" style="width:80%;">附件名</th>
							<th class="w100" style="width:20%;">附件大小</th>
							<!-- <th class="w100" style="width:30%;">路径</th>
							<th class="w100" style="width:14%;">操作</th> -->
						</tr>
					</thead>
					<tbody>
						
					</tbody>
				</table>
				</div>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" id="filingDownLoad">附件下载</button>
				<button class="btn" type="button" id="filingForm">查看表单</button>
				<button class="btn" type="button" data-dismiss="modal">关 闭</button>
			</div>
		</div>
	</div>
</div>
<!--end 附件管理 弹出层-->

<!---------------------------------------- 附件上传部分-------------------------------------- -->
<div class="modal fade panel" id="fileUpload-edit" aria-hidden="false">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" id="btnCloseUpload"
					data-dismiss="modal">×</button>
				<h4 class="modal-title">添加上传文件</h4>
			</div>
			<div class="modal-body">
				<!--业务关联表的属性值，新增附件时不需要填写或填写空字符串-->
				<input type="hidden" name="businessId" id="businessId"
					value="${mail.id }" /> <input type="hidden" name="businessTable"
					id="businessTable" value="TTY_IC_MAIL" />
				<!-- upload_type 1为单传  0为多传-->
				<input type="hidden" id="upload_type" value="0">
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" data-dismiss="modal"
					id="btnFinishUpload">关闭</button>
			</div>
		</div>
	</div>
</div>

<!-- -------------------------------------文件夹选择器---------------------------------------------- -->
<div class="modal fade panel" id="folderSelector" aria-hidden="false">
</div>
<!-- ---------------------------------------------------------------------------------------------- -->
<div class="modal fade panel" id="formContent" aria-hidden="false">
	<div class="modal-dialog w900"  style="width:1200px;">
		<div class="modal-content fileback">
			<div class="modal-header">
				<button type="button" class="close" id="_btnCloseUpload"
					data-dismiss="modal">×</button>
				<h4 class="modal-title">归档表单</h4>
			</div>
			<div class="modal-body" id="formContentBody">
				
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" data-dismiss="modal"
					id="print_">打 印</button>
				<button class="btn" type="button" data-dismiss="modal"
					id="btnFinishUpload__">关 闭</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade panel" id="folderSelector" aria-hidden="false">
</div>


<!-- IE8水印 -->
 <%@ include file="/WEB-INF/web/include/foot.jsp"%>