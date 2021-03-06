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
						placeholder="??????????????????????????????" id="search">
						<!-- 
					<button type="button" class="btn">
						<i class="fa fa-search"></i>
					</button> -->
				</div>
			</div>
			<button type="button" class="btn w0" id="btnReturnDisabled" disabled="disabled">
				<i data-original-title="??????" data-container="body" title=""
					data-placement="top" data-toggle="tooltip" class="fa fa-reply"></i>
			</button>
			<button type="button" class="btn w0 hide" id="btnReturn" >
				<i data-original-title="??????" data-container="body" title=""
					data-placement="top" data-toggle="tooltip" class="fa fa-reply"></i>
			</button>
			<%--<span id="btnNew" ><button type="button" class="btn w0 btn-new">
				<i data-original-title="????????????" data-container="body" title=""
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
				<i data-original-title="??????" data-container="body" title=""
					data-placement="top" data-toggle="tooltip" class="fa fa-upload1"></i>
			</button> -->
			
			<button type="button" class="btn w0 btn-uploading" id="btnUpload">
				<i data-original-title="??????" data-container="body" title=""
					data-placement="top" data-toggle="tooltip" class="fa fa-upload1"></i>
			</button>
					<ul id="attachList"  style=display:none></ul> 
			</span>


			<button class="btn" type="button" id="btnNewFolder">???????????????</button>
			 <button class="btn" type="button" id="btnDelete">????????????</button> 
			<!--	<button class="btn" type="button" id="btnDownload">??????</button>
			-->
			<input type="hidden" id="folderId" value="${folder.id }" /> <input
				type="hidden" id="token" name="token" value="${token}">
			<input type="hidden" id="folder" name ="folder" />
		</section>
		<div class="table-wrap" id="folderTable">
			<!--start ??????-->
			<table class="document-table m-b-md list-table first-td-tc" id="doc_table">
				<thead>
					<tr>
						<th style="width:3%;">
							 <input type="checkbox" id="allFolder" name="checkbox"> 
						</th>
						<th class="w100" style="width:45%;">?????????</th>
						<th class="w100" style="width:10%;">??????</th>
						<th class="w100" style="width:8%;">??????</th>
						<th class="w100" style="width:10%;">??????</th>
						<th class="w100" style="width:14%;">????????????</th>
						<th class="w100" style="width:10%;">?????????</th>
					</tr>
				</thead>
				<!-- ????????? -->
				<tbody>

				</tbody>
			</table>
			<!--end ??????-->
		</div>
	</section>
</section>
<!--start ???????????? ?????????-->
<div class="modal fade panel" id="document-information"
	aria-hidden="false">
	<div class="modal-dialog w1100">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">??</button>
				<h4 class="modal-title">????????????</h4>
			</div>
			<div class="modal-body">
				<div class="form-table">
					<table class="table table-td-striped">
						<tbody>
							<tr>
								<td class="w140">????????????</td>
								<td id="info_dmName">xx????????????</td>
								<td class="w100">????????????</td>
								<td class="w140" id="info_dmSize">52.02MB</td>
								<td class="w100">????????????</td>
								<td class="" id="info_createDate">2014-04-01 10:15</td>
							</tr>
							<tr>
								<td class="w140">????????????</td>
								<td id="info_dmDir">/?????????/OA?????????/xxx???/</td>
								<td class="w100">????????????</td>
								<td class="w140" id="info_currentVersion">1.0</td>
								<td class="w100">????????????</td>
								<td class="" id="info_modifyDate">2014-04-11 10:15</td>
							</tr>
							<tr>
								<td class="w140">????????????</td>
								<td id="info_seq">JCYF-RD-0001-GOA-123456789</td>
								<td class="w100">?????????</td>
								<td class="w140" id="info_createUser">?????????</td>
								<td class="w100"><!-- ???????????? --></td>
								<td class=""   id="info_dmLockStatus"><!-- <span>?????????</span><a href="#" class="a-icon i-new fr"><i
										class="fa fa-locked" data-original-title="??????"
										data-container="body" title="" data-placement="top"
										data-toggle="tooltip"></i>??????</a><a href="#"
									class="a-icon i-new fr"><i class="fa fa-lock-open"
										data-original-title="??????" data-container="body" title=""
										data-placement="top" data-toggle="tooltip"></i>????????????</a> --></td>
							</tr>
							<!-- 
							<tr>
								<td class="w140">?????????</td>
								<td id="info_keyWords">xxx???xxx1???sss <a data-toggle="modal" role="button"
									href="#" class="a-icon i-edit fr"><i
										data-original-title="??????" data-container="body" title=""
										data-placement="top" data-toggle="tooltip" class="fa fa-edit2"></i></a></td>
								<td class="w140">????????????</td>
								<td class="w140" id="info_dmType">????????? <a data-toggle="modal" role="button"
									href="#" class="a-icon i-edit fr"><i
										data-original-title="??????" data-container="body" title=""
										data-placement="top" data-toggle="tooltip" class="fa fa-edit2"></i></a></td>
								<td class="w140"></td>
								<td class="w170"></td>
							</tr> -->
						</tbody>
					</table>
				</div>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" data-dismiss="modal">??? ???</button>
			</div>
		</div>
	</div>
</div>
<!--end ???????????? ?????????-->
<!--start ???????????? ?????????-->
<div class="modal fade panel" id="version-management"
	aria-hidden="false">
	<div class="modal-dialog w1100">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">??</button>
				<h4 class="modal-title">????????????</h4>
			</div>
			<div class="modal-body">
				<table class="table table-striped first-td-tc">
					<thead>
						<tr>
							<th style="width:105px;">?????????</th>
							<th>????????????</th>
							<th>?????????</th>
							<th>????????????</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><a href="#">2.5</a></td>
							<td>xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx</td>
							<td>?????????</td>
							<td>2014-04-11 10:15</td>
						</tr>
					</tbody>
				</table>
				<section class="clearfix m-t-md">
					<section class="pagination fr m-t-none">
						<ul>
							<li><span>???????????? <i>951</i> ???
							</span></li>
							<li class="prev disabled"><a href="#">??</a></li>
							<li><a href="#">1</a></li>
							<li><a href="#">...</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li class="active"><a href="#">5</a></li>
							<li><a href="#">6</a></li>
							<li><a href="#">...</a></li>
							<li><a href="#">10</a></li>
							<li class="next"><a href="#">??</a></li>
						</ul>
					</section>
				</section>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" data-dismiss="modal">??? ???</button>
			</div>
		</div>
	</div>
</div>
<!--end ???????????? ?????????-->
<!--start ???????????? ?????????-->
<div class="modal fade panel" id="document-the-audit"
	aria-hidden="false">
	<div class="modal-dialog w1100">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">??</button>
				<h4 class="modal-title">????????????</h4>
			</div>
			<div class="modal-body" id="audithisListDiv">
				<table id="audithisList" class="table table-striped">
					<thead>
						<tr>
							<th style="width:105px;">?????????</th>
							<th>??????</th>
							<th>IP</th>
							<th>??????</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" data-dismiss="modal">??? ???</button>
			</div>
		</div>
	</div>
</div>
<!--end ???????????? ?????????-->
<!--start ???????????? ?????????-->
<div class="modal fade panel" id="associated-Document" aria-hidden="false">
	<div class="modal-dialog w1100">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">??</button>
				<h4 class="modal-title">????????????</h4>
			</div>
			<div class="modal-body">
				<table class="table table-striped m-t-md">
					<thead>
						<tr>
							<th>??????????????????</th>
							<th>??????????????????</th>
							<th class="w60">??????</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx</td>
							<td>xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx</td>
							<td><a class="a-icon i-remove m-r-xs" href="#"><i
									class="fa fa-remove" data-toggle="tooltip" data-placement="top"
									title="" data-container="body" data-original-title="??????"></i></a></td>
						</tr>
					</tbody>
				</table>
				<section class="clearfix m-t-md">
					<section class="pagination fr m-t-none">
						<ul>
							<li><span>???????????? <i>951</i> ???
							</span></li>
							<li class="prev disabled"><a href="#">??</a></li>
							<li><a href="#">1</a></li>
							<li><a href="#">...</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li class="active"><a href="#">5</a></li>
							<li><a href="#">6</a></li>
							<li><a href="#">...</a></li>
							<li><a href="#">10</a></li>
							<li class="next"><a href="#">??</a></li>
						</ul>
					</section>
				</section>
			</div>
			<div class="modal-footer form-btn">
				<a type="button" href="#relevance" role="button" data-toggle="modal"
					class="btn dark">??? ???</a>
				<button class="btn" type="button" data-dismiss="modal">??? ???</button>
			</div>
		</div>
	</div>
</div>
<!--end ???????????? ?????????-->
<!--start ?????? ?????????-->
<div class="modal fade panel" id="relevance" aria-hidden="false">
	<div class="modal-dialog w1100">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">??</button>
				<h4 class="modal-title">??????</h4>
			</div>
			<div class="modal-body">
				<form class="m-l-n">
					<!--start ????????????-->
					<section class="clearfix">
						<h2 class="panel-heading clearfix">????????????</h2>
						<div class="form-table m-l-20">
							<table class="table table-td-striped m-t-md">
								<tbody>
									<tr>
										<td class="w140">????????????</td>
										<td style="width:35;"><input type="text"></td>
										<td class="w140">?????????</td>
										<td><div class="input-group">
												<input type="text"> <a
													class="btn btn-file input-group-btn" href="#myModal"
													role="button" data-toggle="modal"><i class="fa fa-user"></i>
												</a>
											</div></td>
									</tr>
									<tr>
										<td class="w140">????????????</td>
										<td><input type="text"></td>
										<td>????????????</td>
										<td style="width:40%"><input type="text"></td>
									</tr>
									<tr>
										<td>??????/??????????????????</td>
										<td><div class="input-group w-p100">
												<input class="datepicker-input" type="text" value="2014-5-5"
													data-date-format="yyyy-mm-dd">
											</div></td>
										<td>??????/??????????????????</td>
										<td><div class="input-group w-p100">
												<input class="datepicker-input" type="text" value="2014-5-5"
													data-date-format="yyyy-mm-dd">
											</div></td>
									</tr>
								</tbody>
							</table>
							<section class="form-btn m-b-lg  m-t-md">
								<button class="btn dark query-jump" type="button">??? ???</button>
								<button class="btn" type="reset">??? ???</button>
							</section>
						</div>
					</section>
					<!--end ????????????-->
					<!--start ????????????-->
					<section class="clearfix">
						<!-- <h2 class="panel-heading clearfix m-l-n">????????????</h2> -->
						<div class="m-t-md m-l-20">
							<table class="table table-striped first-td-tc">
								<thead>
									<tr>
										<th class="w46"><input type="checkbox"></th>
										<th>????????????</th>
										<th class="w90">????????????</th>
										<th class="w200">?????????</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class="w46"><input type="checkbox"></td>
										<td>???????????????????????????(????????????)</td>
										<td>45646445665665</td>
										<td>?????????</td>
									</tr>
									<tr>
										<td class="w46"><input type="checkbox"></td>
										<td>???????????????????????????(????????????)</td>
										<td>45646445665665</td>
										<td>?????????</td>
									</tr>
									<tr>
										<td class="w46"><input type="checkbox"></td>
										<td>???????????????????????????(????????????)</td>
										<td>45646445665665</td>
										<td>?????????</td>
									</tr>
								</tbody>
							</table>
						</div>
						<section class="clearfix m-t-md">
							<!--start ??????-->
							<section class="pagination fr m-t-none">
								<ul>
									<li><span>???????????? <i>951</i> ???
									</span></li>
									<li class="prev disabled"><a href="#">??</a></li>
									<li><a href="#">1</a></li>
									<li><a href="#">...</a></li>
									<li><a href="#">3</a></li>
									<li><a href="#">4</a></li>
									<li class="active"><a href="#">5</a></li>
									<li><a href="#">6</a></li>
									<li><a href="#">...</a></li>
									<li><a href="#">10</a></li>
									<li class="next"><a href="#">??</a></li>
								</ul>
							</section>
							<!--end ??????-->
						</section>
					</section>
					<!--end ????????????-->
				</form>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="submit">??? ???</button>
				<button class="btn" type="button" data-dismiss="modal">??? ???</button>
			</div>
		</div>
	</div>
</div>
<!--end ?????? ?????????-->


<!---------------------------------------- ??????????????????-------------------------------------- -->

<div class="modal fade panel" id="file-edit" aria-hidden="false">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" id="closebtn" data-dismiss="modal">??</button>
					<h4 class="modal-title">??????????????????</h4>
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
				                        <div id="filePickerBtn" class="attachBtn"></div><div class="uploadBtn">??? ???</div>
				                    </div>
				                </div>
				            </div>
				        </div>
    				</div>
    
				</div>
				<div class="modal-footer form-btn">
					<button class="btn dark" type="button" id="closeModalBtn" data-dismiss="modal">??? ???</button>
				</div>
			</div>
		</div>
	</div>
<%-- <div class="modal fade panel" id="fileUpload-edit" aria-hidden="false">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" id="btnCloseUpload"
					data-dismiss="modal">??</button>
				<h4 class="modal-title">??????????????????</h4>
			</div>
			<div class="modal-body">
				<!--?????????????????????????????????????????????????????????????????????????????????-->
				<input type="hidden" name="businessId" id="businessId"
					value="${mail.id }" /> <input type="hidden" name="businessTable"
					id="businessTable" value="TTY_IC_MAIL" />
				<!-- upload_type 1?????????  0?????????-->
				<input type="hidden" id="upload_type" value="0">
				        	<input type="hidden" id="upload_div_name" value="fileUpload-edit">
			 	<input type="hidden" id="upload_close_callback" value="archive_doc.finishUpload">  
				<%@ include file="/WEB-INF/web/attach/attachManage.jsp"%>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" data-dismiss="modal"
					id="btnFinishUpload">???&nbsp;???</button>
			</div>
		</div>
	</div>
</div> --%>

<!-- -------------------------------------??????????????????---------------------------------------------- -->
<div class="modal fade panel" id="folderSelector" aria-hidden="false">
</div>

<!-- IE8?????? -->
 <%@ include file="/WEB-INF/web/include/foot.jsp"%>