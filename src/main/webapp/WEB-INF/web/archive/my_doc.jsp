<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String base = request.getContextPath();
	String IPAddr = null;
	IPAddr = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() ;
%>
<head>
    <%@ include file="/WEB-INF/web/include/taglib.jsp"%>
    <meta http-equiv="Content-type" content="text/html; UTF-8"/>
    <meta name="renderer" content="ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="X-UA-Compatible" content="edge" />
    <!--[if gt IE 8]><!--> <link href="${sysPath}/css/font-face.css" rel="stylesheet" type="text/css"/> <!--<![endif]-->
    <link href="${sysPath}/css/yx_frame.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src='${sysPath}/js/jquery.min.archive.js'></script>
    <script type='text/javascript' src='${sysPath}/js/lib/common/jquery.utils.js?v=7b46332a61'></script>
    <script type='text/javascript' src='${sysPath}/js/lib/common/common.all.js?v=1'></script>
    <script type="text/javascript" src='${sysPath}/js/lib/datatable/jquery.dataTables.all.js?v=71e965f0c6'></script>
    <script type="text/javascript">setRootPath('${sysPath}');</script>
    <script type="text/javascript">setTheme('${theme}');</script>
    <script type="text/javascript">setManageHostPath('<%=pageContext.getAttribute("managerHostPath")%>');</script>
    <script type="text/javascript" src='${sysPath}/js/lib/jquery-validation/jquery.validate.all.js?v=b58d900620'></script>
    <script type="text/javascript" src="${sysPath}/js/lib/app/datepicker-jbox.min.js?v=b583c73c85"></script>
    <script type="text/javascript" src="${sysPath}/js/app.v2.js?v=e975657b0c"></script>
    <script type="text/javascript" src="${sysPath}/js/lib/common/common-final-param.js"></script>
    <!--[if lt IE 9]>
    <script src="${sysPath}/js/ie/html5shiv.js?v=758616407d"></script>
    <![endif]-->

</head>

<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<script src="${sysPath}/js/com/common/Base64.js" type="text/javascript"></script>
<script async src="${sysPath}/js/com/archive/folderPathBar.js" type="text/javascript"></script>
<script async src="${sysPath}/js/com/archive/FolderSelectTree.js" type="text/javascript"></script>
<script async src="${sysPath}/js/com/archive/my_doc.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/archive/ArchiveFolder.validator.js"  type="text/javascript"></script>
<script type="text/javascript">
var basePath_ = "<%=IPAddr%>";
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
						placeholder="请输入名称搜索当前页" id="search">
						<!-- 
					<button type="button" class="btn">
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
			<%--<span id="btnNew" ><button type="button" class="btn w0 btn-new">
				<i data-original-title="新建文件" data-container="body" title=""
					data-placement="top" data-toggle="tooltip" class="fa fa-plus"></i>
			</button></span>--%>
			<div class="document-new">
				<a href="javascript:void(0)" id="newWord" class="btn document-con"><img width="39" height="44" src="../../images/demoimg/document-word.png"></a> 
				<a href="javascript:void(0)" id="newExl" class="btn document-con"><img width="39" height="44" src="../../images/demoimg/document-ex.png"></a>
				<!-- <a href="javascript:void(0)" id="newPPT" class="btn i-pdf"><i
					class="fa">PPT</i></a> -->


				<!--    <a href="#" class="btn i-file"><i class="fa fa-folder-close"></i></a>
                               <a href="#" class="btn i-file"><i class="fa fa-folder-close"></i></a>
                                <a href="#" class="btn i-default"><i class="fa fa-document-text"></i></a>-->
			</div>
			<span id="upload" ><!-- <button type="button" class="btn w0 btn-uploading" id="btnUpload">
				<i data-original-title="上传" data-container="body" title=""
					data-placement="top" data-toggle="tooltip" class="fa fa-upload1"></i>
			</button> -->
			
			<button type="button" class="btn w0 btn-uploading" id="btnUpload">
				<i data-original-title="上传" data-container="body" title=""
					data-placement="top" data-toggle="tooltip" class="fa fa-upload1"></i>
			</button>
					<ul id="attachList"  style=display:none></ul> 
			</span>


			<button class="btn" type="button" id="btnNewFolder">新建文件夹</button>
			 <button class="btn" type="button" id="btnDelete">批量删除</button> 
			<!--	<button class="btn" type="button" id="btnDownload">下载</button>
			-->
			<input type="hidden" id="folderId" value="${folder.id }" /> <input
				type="hidden" id="token" name="token" value="${token}">
			<input type="hidden" id="folder" name ="folder" />
		</section>
		<div class="table-wrap" id="folderTable">
			<!--start 列表-->
			<table class="document-table m-b-md list-table first-td-tc" id="doc_table">
				<thead>
					<tr>
						<th style="width:3%;">
							 <input type="checkbox" id="allFolder" name="checkbox"> 
						</th>
						<th class="w100" style="width:45%;">文件名</th>
						<th class="w100" style="width:10%;">操作</th>
						<th class="w100" style="width:8%;">类型</th>
						<th class="w100" style="width:10%;">大小</th>
						<th class="w100" style="width:14%;">更新时间</th>
						<th class="w100" style="width:10%;">创建者</th>
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
								<td class="w100">文档大小</td>
								<td class="w140" id="info_dmSize">52.02MB</td>
								<td class="w100">创建时间</td>
								<td class="" id="info_createDate">2014-04-01 10:15</td>
							</tr>
							<tr>
								<td class="w140">文档目录</td>
								<td id="info_dmDir">/研发部/OA产品部/xxx组/</td>
								<td class="w100">最新版本</td>
								<td class="w140" id="info_currentVersion">1.0</td>
								<td class="w100">更新时间</td>
								<td class="" id="info_modifyDate">2014-04-11 10:15</td>
							</tr>
							<tr>
								<td class="w140">文档编号</td>
								<td id="info_seq">JCYF-RD-0001-GOA-123456789</td>
								<td class="w100">创建者</td>
								<td class="w140" id="info_createUser">刘锡来</td>
								<td class="w100"><!-- 锁定状态 --></td>
								<td class=""   id="info_dmLockStatus"><!-- <span>未锁定</span><a href="#" class="a-icon i-new fr"><i
										class="fa fa-locked" data-original-title="锁定"
										data-container="body" title="" data-placement="top"
										data-toggle="tooltip"></i>锁定</a><a href="#"
									class="a-icon i-new fr"><i class="fa fa-lock-open"
										data-original-title="锁定" data-container="body" title=""
										data-placement="top" data-toggle="tooltip"></i>解除锁定</a> --></td>
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
				<table class="table table-striped first-td-tc">
					<thead>
						<tr>
							<th style="width:105px;">版本号</th>
							<th>版本说明</th>
							<th>更新者</th>
							<th>更新时间</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><a href="#">2.5</a></td>
							<td>xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx</td>
							<td>刘锡来</td>
							<td>2014-04-11 10:15</td>
						</tr>
					</tbody>
				</table>
				<section class="clearfix m-t-md">
					<section class="pagination fr m-t-none">
						<ul>
							<li><span>每页显示 <i>951</i> 条
							</span></li>
							<li class="prev disabled"><a href="#">«</a></li>
							<li><a href="#">1</a></li>
							<li><a href="#">...</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li class="active"><a href="#">5</a></li>
							<li><a href="#">6</a></li>
							<li><a href="#">...</a></li>
							<li><a href="#">10</a></li>
							<li class="next"><a href="#">»</a></li>
						</ul>
					</section>
				</section>
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
			<div class="modal-body" id="audithisListDiv">
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
<div class="modal fade panel" id="associated-Document" aria-hidden="false">
	<div class="modal-dialog w1100">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">关联文档</h4>
			</div>
			<div class="modal-body">
				<table class="table table-striped m-t-md">
					<thead>
						<tr>
							<th>关联文档名称</th>
							<th>关联文档目录</th>
							<th class="w60">操作</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx</td>
							<td>xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx</td>
							<td><a class="a-icon i-remove m-r-xs" href="#"><i
									class="fa fa-remove" data-toggle="tooltip" data-placement="top"
									title="" data-container="body" data-original-title="删除"></i></a></td>
						</tr>
					</tbody>
				</table>
				<section class="clearfix m-t-md">
					<section class="pagination fr m-t-none">
						<ul>
							<li><span>每页显示 <i>951</i> 条
							</span></li>
							<li class="prev disabled"><a href="#">«</a></li>
							<li><a href="#">1</a></li>
							<li><a href="#">...</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li class="active"><a href="#">5</a></li>
							<li><a href="#">6</a></li>
							<li><a href="#">...</a></li>
							<li><a href="#">10</a></li>
							<li class="next"><a href="#">»</a></li>
						</ul>
					</section>
				</section>
			</div>
			<div class="modal-footer form-btn">
				<a type="button" href="#relevance" role="button" data-toggle="modal"
					class="btn dark">关 联</a>
				<button class="btn" type="button" data-dismiss="modal">关 闭</button>
			</div>
		</div>
	</div>
</div>
<!--end 关联文档 弹出层-->
<!--start 关联 弹出层-->
<div class="modal fade panel" id="relevance" aria-hidden="false">
	<div class="modal-dialog w1100">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">关联</h4>
			</div>
			<div class="modal-body">
				<form class="m-l-n">
					<!--start 查询条件-->
					<section class="clearfix">
						<h2 class="panel-heading clearfix">查询条件</h2>
						<div class="form-table m-l-20">
							<table class="table table-td-striped m-t-md">
								<tbody>
									<tr>
										<td class="w140">文档名称</td>
										<td style="width:35;"><input type="text"></td>
										<td class="w140">创建者</td>
										<td><div class="input-group">
												<input type="text"> <a
													class="btn btn-file input-group-btn" href="#myModal"
													role="button" data-toggle="modal"><i class="fa fa-user"></i>
												</a>
											</div></td>
									</tr>
									<tr>
										<td class="w140">文档编号</td>
										<td><input type="text"></td>
										<td>文档目录</td>
										<td style="width:40%"><input type="text"></td>
									</tr>
									<tr>
										<td>创建/修改开始时间</td>
										<td><div class="input-group w-p100">
												<input class="datepicker-input" type="text" value="2014-5-5"
													data-date-format="yyyy-mm-dd">
											</div></td>
										<td>创建/修改结束时间</td>
										<td><div class="input-group w-p100">
												<input class="datepicker-input" type="text" value="2014-5-5"
													data-date-format="yyyy-mm-dd">
											</div></td>
									</tr>
								</tbody>
							</table>
							<section class="form-btn m-b-lg  m-t-md">
								<button class="btn dark query-jump" type="button">查 询</button>
								<button class="btn" type="reset">重 置</button>
							</section>
						</div>
					</section>
					<!--end 查询条件-->
					<!--start 查询结果-->
					<section class="clearfix">
						<!-- <h2 class="panel-heading clearfix m-l-n">查询结果</h2> -->
						<div class="m-t-md m-l-20">
							<table class="table table-striped first-td-tc">
								<thead>
									<tr>
										<th class="w46"><input type="checkbox"></th>
										<th>文档名称</th>
										<th class="w90">文档编号</th>
										<th class="w200">创建者</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class="w46"><input type="checkbox"></td>
										<td>软件需求规格说明书(文档管理)</td>
										<td>45646445665665</td>
										<td>刘锡来</td>
									</tr>
									<tr>
										<td class="w46"><input type="checkbox"></td>
										<td>软件需求规格说明书(文档管理)</td>
										<td>45646445665665</td>
										<td>刘锡来</td>
									</tr>
									<tr>
										<td class="w46"><input type="checkbox"></td>
										<td>软件需求规格说明书(文档管理)</td>
										<td>45646445665665</td>
										<td>刘锡来</td>
									</tr>
								</tbody>
							</table>
						</div>
						<section class="clearfix m-t-md">
							<!--start 分页-->
							<section class="pagination fr m-t-none">
								<ul>
									<li><span>每页显示 <i>951</i> 条
									</span></li>
									<li class="prev disabled"><a href="#">«</a></li>
									<li><a href="#">1</a></li>
									<li><a href="#">...</a></li>
									<li><a href="#">3</a></li>
									<li><a href="#">4</a></li>
									<li class="active"><a href="#">5</a></li>
									<li><a href="#">6</a></li>
									<li><a href="#">...</a></li>
									<li><a href="#">10</a></li>
									<li class="next"><a href="#">»</a></li>
								</ul>
							</section>
							<!--end 分页-->
						</section>
					</section>
					<!--end 查询结果-->
				</form>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="submit">确 定</button>
				<button class="btn" type="button" data-dismiss="modal">关 闭</button>
			</div>
		</div>
	</div>
</div>
<!--end 关联 弹出层-->


<!---------------------------------------- 附件上传部分-------------------------------------- -->

<div class="modal fade panel" id="file-edit" aria-hidden="false">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" id="closebtn" data-dismiss="modal">×</button>
					<h4 class="modal-title">添加上传文件</h4>
				</div>
				<div class="modal-body">
				
					<div id="wrapper">
				        <div id="container">
				            <div id="uploader" class="attach">
				                <div class="queueList">
				                    <div id="dndArea" class="placeholder">
				                        <div id="filePicker"></div>
				                        <p></p>
				                    </div>
				                </div>
				                <div class="statusBar" style="display:none;">
				                    <div class="progress">
				                        <span class="text">0%</span>
				                        <span class="percentage"></span>
				                    </div><div class="info"></div>
				                    <div class="btns">
				                        <div id="filePickerBtn" class="attachBtn"></div><div class="uploadBtn">上 传</div>
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
<%-- <div class="modal fade panel" id="fileUpload-edit" aria-hidden="false">
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
				        	<input type="hidden" id="upload_div_name" value="fileUpload-edit">
			 	<input type="hidden" id="upload_close_callback" value="archive_doc.finishUpload">  
				<%@ include file="/WEB-INF/web/attach/attachManage.jsp"%>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" data-dismiss="modal"
					id="btnFinishUpload">关&nbsp;闭</button>
			</div>
		</div>
	</div>
</div> --%>

<!-- -------------------------------------文件夹选择器---------------------------------------------- -->
<div class="modal fade panel" id="folderSelector" aria-hidden="false">
</div>

<!-- IE8水印 -->
 <%@ include file="/WEB-INF/web/include/foot.jsp"%>