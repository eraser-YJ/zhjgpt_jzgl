/**
 * 文档管理 zhanglg
 */
var base64 = new Base64();
String.prototype.trim=function(){return this.replace(/(^\s*)|(\s*$)/g,"");};
var archive_doc = {
	subState:false,
	documentType : 'doc',
	viewType : 1,// null只读，1编辑
	documentPath : '',
	model:1,
	folderSelector:null,
	currentSelectedRowId:null,
	folder:null
};
//已打开文档页面
archive_doc.hasOpen = false;
var currentUserId = 0;
archive_doc.pageRows = null;
archive_doc.oTable = null;//分页对象
archive_doc.clearParam = function() {
	this.documentType = 'doc';
	this.viewType = 1;// null只读，1编辑
	this.documentPath = '';
	this.currentDMName = '';
	$('#search').val('');
};
var backFlag = -1;

archive_doc.option = {};

archive_doc.init = function() {
	archive_doc.loadData($('#folderId').val(),null,null,null,true);
};

archive_doc.reloadData = function() {
	archive_doc.clearParam();
	archive_doc.loadData(archive_doc.getFolderId());
};
archive_doc.getFolderId = function() {
	return $('#folderId').val();
};
// WebOffice在线查看Office
archive_doc.viewFile = function(contentType, isEdit, path) {
	// word xls ppt 文件
	archive_doc.viewType = isEdit;// null 只读， 1 编辑
	//archive_doc.documentPath = basePath_  +"/"+ path;// null 只读， 1 编辑 DJweboffice使用
	archive_doc.documentPath = path;// null 只读， 1 编辑 JGweboffice使用

	if (contentType == '1') {
		archive_doc.newWord();
	} else if (contentType == '2') {
		archive_doc.newExl();
	} else if (contentType == '3') {
		archive_doc.newPPT();
	}
	// archive_doc.viewType = 1;
};
// 异步加载数据
archive_doc.loadData = function(folderId, rowType, contentType, path,isClick) {
	$('#allFolder').attr('checked',false);
	$('#search').attr('value','');
	$('#search').blur();
	if(folderId==0){
		return;
	}
	if (rowType == 1) {
		// 加载文档,
		// 需要先验证是否有权限
		// 先验证有没有操作权限
		if (folderId && path && path.length > 0) {
			path = base64.decode(path);
		}
		if (contentType == '0') {

			// 其他文档
		} else {
			archive_doc.viewFile(contentType, 1, path);
		}
	} else {
		// 加载文件夹
		return $.ajax({
			type : "GET",
			url : getRootPath() + "/archive/folder/getMyDirDocs.action",
			data : {
				id : folderId,
				t:(new Date()).getTime()
			},
			dataType : "json",
			success : function(data) {
				$('#token').val(data.token);
				if (data.success == "true") {
					$('#doc_table > tbody').empty();
					$('#folderId').val(data.folder.id);
					archive_doc.folder=data.folder;
					backFlag += 1;
					archive_doc.render(data.folder);
//					archive_doc.permission();
					if(isClick){
						archive_doc.pathBar.appendPath(data.folder.id,data.folder.folderName);
					}
					$('#btnNewFolder').removeAttr("disabled");
				} else {
					if (data.errorMessage) {
						msgBox.tip({
							content : data.errorMessage
						});
					} else {
						msgBox.tip({
							content : $.i18n.prop("JC_SYS_060")
						});
					}
				}
			}
		});
	}

};
var folder_ = null;
// ---------表格操作开始-------------------
// 界面渲染
archive_doc.render = function(folder) {
	currentUserId = folder.currentUserId;
	var tbody = $('#doc_table > tbody');
	var dirRow = "";
	var docRow = "";
	var self = this;
	folder_ = folder;
	// process subdirs
	$.each(folder.subdirs, function(i, o) {
		dirRow += ("<tr id=\"dir_" + o.id + "\" class=\"dir\">");
		dirRow += (self.createCheckCell(o));
		dirRow += (self.createDirNameCell(o));
		dirRow += (self.createDirOpCell(o));
		//dirRow += (self.createSimpleCell(""));
		dirRow += (self.createSimpleCell("文件夹"));
		dirRow += (self.createSimpleCell(""));
		dirRow += (self.createSimpleCell(o.modifyDate));
		dirRow += (self.createSimpleCell(o.owner));
		dirRow += "</tr>";
	});
	tbody.append(dirRow);
	// process documents
	$.each(folder.documents, function(i, o) {
		docRow += ("<tr id=\"doc_" + o.id + "\" class=\"doc\">");
		docRow += (self.createCheckCell(o));
		docRow += (self.createDocNameCell(o));
		docRow += (self.createDocOpCell(o));
		//docRow += (self.createSimpleCell(o.dmLockStatus == 1 ? "锁定" : '未锁定'));
		// docRow+=(self.createSimpleCell(o.contentType));
		docRow += (self.createSimpleCell(o.dmSuffix));
		// docRow+=(self.createSimpleCell(o.dmLockStatus));
		// docRow+=(self.createSimpleCell(o.contentType));
		docRow += (self.createSimpleCell(o.dmSize));
		docRow += (self.createSimpleCell(o.modifyDate));
		docRow += (self.createSimpleCell(o.owner));
		docRow += "</tr>";
	});
	tbody.append(docRow);
	//如果没有记录，显示空白行
	archive_doc.processEmptyTable();
	$("i[data-toggle='tooltip']").tooltip();
	//root目录返回按钮不可用
	if(folder.parentFolderId==0){
		$('#btnReturnDisabled').show();
		$('#btnReturn').hide();
	}else{
		$('#btnReturnDisabled').hide();
		$('#btnReturn').show();
	}
	//IE8隔行变色
	ie8StylePatch();
	$("[data-toggle=dropdown]").dropdown();
	ListTable();
};
//处理表格无数据的情况
archive_doc.processEmptyTable=function(){
	var tbody = $('#doc_table > tbody');
	if(tbody.children().length==0){
		tbody.append(archive_doc.createEmptyRow());
		return ;
	}
	if(tbody.children().length>1 ){
		tbody.children('.empty_row').remove();
	}
};

//生成空白行
archive_doc.createEmptyRow=function(){
	var row='<tr class="empty_row"><td valign="top" colspan="8" class="dataTables_empty">没有匹配的记录</td></tr>';
	return row;
};
// 生成目录操作单元
archive_doc.createDirOpCell = function(folder) {
	var option = "<td>";
	option += "<div class=\"dropdown inline\"> ";
	option += "<a href=\"#\" type=\"button\" class=\"btn-uploading\" data-toggle=\"dropdown\"><i data-original-title=\"更多\" data-container=\"body\" title=\"\" data-placement=\"top\" data-toggle=\"tooltip\"  class=\"fa fa-more-list\"></i></a> ";
	option += "        <ul class=\"dropdown-menu animated fadeInRight\">            ";
	option += "          <li><a type=\"button\" href=\"javascript:archive_doc.clickCopy('#dir_"+folder.id+"')\" role=\"button\" >复制到...</a></li> ";
	option += "          <li><a type=\"button\" href=\"javascript:archive_doc.clickCut('#dir_"+folder.id+"')\" role=\"button\" >剪切到...</a></li> ";
	option += "			 <li><a type=\"button\" href=\"javascript:archive_doc.rename('#dir_" + folder.id + "');\" role=\"button\" >重命名</a></li> ";
	option += "          <li><a type=\"button\" href=\"javascript:archive_doc.rowDeleteClick('#dir_"+folder.id+"')\" role=\"button\" >删除</a></li>  ";
	option += "        </ul> ";
	option += "    </div> </td>";
	return option;
};

// 生成文件操作单元
archive_doc.createDocOpCell = function(doc) {
	var p = doc.dmAlias;
	if (p && p.length > 0) {
		p = base64.encode(p);
	}
	/*var p = doc.physicalPath;
	if (p && p.length > 0) {
		p = base64.encode(p);
	}*/
	var option = "<td>";
	
	option += "<a href=\"javascript:javascript:archive_doc.download('"
					+ doc.id
					+ "');\" ><i data-original-title=\"下载\" data-container=\"body\" title=\"\" data-placement=\"top\" data-toggle=\"tooltip\"  class=\"fa fa-download\"></i></a>";
	var office = (doc.contentType == 1 || doc.contentType == 2 || doc.contentType == 3);
	if(office) {
		option += "<a href=\"javascript:void(0)\"><i data-original-title=\"编辑\" data-container=\"body\" title=\"\" data-placement=\"top\" data-toggle=\"tooltip\" onclick=\"archive_doc.editDoc('"
					+ doc.contentType
					+ "','"
					+ p
					+ "','"
					+ doc.dmName
					+ "','"
					+ doc.id
					+ "')\"  class=\"fa fa-compose\"></i></a>";
	}
	
	option += "<div class=\"dropdown inline\"> ";
	option += "<a href=\"#\" type=\"button\" class=\"btn-uploading\" data-toggle=\"dropdown\"><i data-original-title=\"更多\" data-container=\"body\" title=\"\" data-placement=\"top\" data-toggle=\"tooltip\"  class=\"fa fa-more-list\"></i></a> ";
	option += "        <ul class=\"dropdown-menu animated fadeInRight\">            ";
	option += "			<li><a type=\"button\" href=\"javascript:archive_doc.clickCopy('#doc_"+doc.id+"')\" role=\"button\" >复制到...</a></li> ";
	option += "			<li><a type=\"button\" href=\"javascript:archive_doc.clickCut('#doc_"+doc.id+"')\" role=\"button\" >剪切到...</a></li> ";
	option += "			<li><a type=\"button\" href=\"javascript:archive_doc.rename('#doc_" + doc.id + "');\" role=\"button\" >重命名</a></li> ";
	option += "			<li><a type=\"button\" href=\"javascript:archive_doc.rowDeleteClick('#doc_"+doc.id+"')\" role=\"button\" >删除</a></li> ";
	option += "			 <li><a type=\"button\" href=\"javascript:archive_doc.getDocInfo('"+doc.id+"');\" role=\"button\">文档信息</a></li> ";
//	option += "          <li><a type=\"button\" href=\"#version-management\" role=\"button\" data-toggle=\"modal\">版本管理</a></li> ";
	option += "          <li><a type=\"button\" href=\"javascript:archive_doc.audithis('"+doc.id+"');\" role=\"button\">文档审计</a></li> ";
//	option += "          <li><a type=\"button\" href=\"#associated-Document\" role=\"button\" data-toggle=\"modal\">关联文档</a></li>  ";
	option += "        </ul> ";
	option += "    </div> </td>";
	return option;
};
// 生成简单显示单元
archive_doc.createSimpleCell = function(content) {
	if(content===undefined){
		content="";
	}
	return "<td>" + content + "</td>";
};
// 生成CheckBox单元
archive_doc.createCheckCell = function(bean) {
	var id = null;
	if (bean.dmName != undefined) {
		id = "#doc_" + bean.id+",1";
	} else if (bean.folderName != undefined) {
		id = "#dir_" + bean.id+",0";
	}
	return "<td><input type=\"checkbox\" name=\"ids\" value=\"" + id
			+ "\"></td>";
	//暂时去除Checkbox
	//return "";
};


// 创建文件夹名称单元
archive_doc.createDirNameCell = function(bean) {
	var name = bean.folderName;
	var id = "#dir_" + bean.id;
	var inputId = 'input_';
	var cell = null;
	// 文件夹
	var rowType = 2;

	var label = "<label class=\"btn document-con disable\"";
	label += " onclick=\"archive_doc.loadData(\'";
	label += bean.id;
	label += "','";
	label += rowType;
	label += "','";
	label += "','";
	label += "',true);\">";
	label += '<img width=\"39\" height=\"44\" src=\"../../images/demoimg/document-file.png\"></label>';

	var nameSpan = "<span class=\"file_name\">";
	nameSpan += "<a href=\"javascript:void(0);\" onclick=\"archive_doc.loadData('";
	nameSpan += bean.id;
	nameSpan += "','";
	nameSpan += rowType;
	nameSpan += "','";
	nameSpan += "','";
	nameSpan += "',true);\">";
	nameSpan += name;
	nameSpan += "</a></span>";

	//var nameInputSpan = "<span class=\"file_name_input hide\"><input id='"+inputId+"' style=\"position:relative;top:5px\" type=\"text\" onkeydown=\"if(event.keyCode==13){archive_doc.save('"+id+"');}\" name=\"folder_name\" maxlength=\"64\"  value=\"";
	var nameInputSpan = "<span class=\"file_name_input hide\"><input style=\"position:relative;top:5px\" type=\"text\" name=\"folder_name\" onkeydown=\"if(event.keyCode==13){archive_doc.save('"
			+ id + "');}\" maxlength=\"64\" value=\"";
	nameInputSpan += name;
	nameInputSpan += "\"/><a class=\"a-icon i-new m-l-xs m-r-xs\" type=\"button\" href=\"javascript:archive_doc.save('";
	nameInputSpan += id;
	nameInputSpan += "');\" role=\"button\" >确 定</a><a class=\"a-icon i-new\" type=\"button\" href=\"javascript:archive_doc.cancel('";
	nameInputSpan += id;
	nameInputSpan += "');\" role=\"button\" >取 消</a></span>";
	// <label class="btn i-word"><i class="fa">W99</i></label>

	cell = "<td>";
	cell += label;
	cell += nameSpan;
	cell +=nameInputSpan;
	cell += "</td>";

	return cell;
};
// 创建文档名称单元
archive_doc.createDocNameCell = function(bean) {
	var name = bean.dmName;
	var id = "#doc_" + bean.id;
	var cell = null;

	var clickFunc= " onclick=\"archive_doc.showDocContent(\'";
	clickFunc += bean.id;
	clickFunc += "');\"";
	var label = "";
	if (bean.contentType == 1) {
		label = '<label class=\"btn document-con\" '+clickFunc+'><img width=\"39\" height=\"44\" src=\"../../images/demoimg/document-word.png\"></label>';
	} else if (bean.contentType == 2) {
		label = '<label class=\"btn document-con\" '+clickFunc+'><img width=\"39\" height=\"44\" src=\"../../images/demoimg/document-ex.png\"></label>';
	} else if (bean.contentType == 3) {
		label = '<label class=\"btn document-con\" '+clickFunc+'><img width=\"39\" height=\"44\" src=\"../../images/demoimg/document-ppt.png\"></label>';
	} else {
		if(bean.dmSuffix == "pdf") {
			label = '<label class=\"btn document-con\" '+clickFunc+'><img width=\"39\" height=\"44\" src=\"../../images/demoimg/document-pdf.png\"></label>';
		} else if(bean.dmSuffix == "rar" || bean.dmSuffix == "zip") {
			label = '<label class=\"btn document-con\" '+clickFunc+'><img width=\"39\" height=\"44\" src=\"../../images/demoimg/document-rar.png\"></label>';
		} else if(bean.dmSuffix == "bmp" || bean.dmSuffix == "gif"
				|| bean.dmSuffix == "jpg" || bean.dmSuffix == "jpeg"
				|| bean.dmSuffix == "jpe" || bean.dmSuffix == "jfif"
				|| bean.dmSuffix == "pic" || bean.dmSuffix == "png"
				|| bean.dmSuffix == "tif" || bean.dmSuffix == "tiff") {
			label = '<label class=\"btn document-con\" '+clickFunc+'><img width=\"39\" height=\"44\" src=\"../../images/demoimg/document-picture.png\"></label>';

		}else if(bean.dmSuffix == "wav" || bean.dmSuffix == "aif"
				|| bean.dmSuffix == "au" || bean.dmSuffix == "mp3"
				|| bean.dmSuffix == "ram"
				|| bean.dmSuffix == "wma" || bean.dmSuffix == "mmf"
				|| bean.dmSuffix == "amr" || bean.dmSuffix == "aac"
				|| bean.dmSuffix == "flac") {
			label = '<label class=\"btn document-con\" '+clickFunc+'><img width=\"39\" height=\"44\" src=\"../../images/demoimg/document-radio.png\"></label>';

		}else if(bean.dmSuffix == "mpeg" || bean.dmSuffix == "mpg"
				|| bean.dmSuffix == "avi" || bean.dmSuffix == "wmv"
				|| bean.dmSuffix == "rmvb" || bean.dmSuffix == "rm"
				|| bean.dmSuffix == "flv" || bean.dmSuffix == "mp4"
				|| bean.dmSuffix == "3gp" || bean.dmSuffix == "mov") {
			label = '<label class=\"btn document-con\" '+clickFunc+'><img width=\"39\" height=\"44\" src=\"../../images/demoimg/document-video.png\"></label>';

		}else {
			label = '<label class=\"btn document-con\" '+clickFunc+'><img width=\"39\" height=\"44\" src=\"../../images/demoimg/document-text.png\"></label>';
		}
	}

	var nameSpan = "<span class=\"file_name\">";
	nameSpan += "<a href=\"javascript:archive_doc.showDocContent('";
	nameSpan += bean.id;
	nameSpan += "');\">";
	nameSpan += name;
	nameSpan += "</a></span>";

	//var nameInputSpan = "<span class=\"file_name_input hide\"><input style=\"position:relative;top:5px\" type=\"text\" name=\"folder_name\" maxlength=\"64\"  value=\"";
	var nameInputSpan = "<span class=\"file_name_input hide\"><input style=\"position:relative;top:5px\" type=\"text\" name=\"folder_name\" onkeydown=\"if(event.keyCode==13){archive_doc.save('"
			+ id + "');}\" maxlength=\"64\" value=\"";
	nameInputSpan += name;
	nameInputSpan += "\"/><a class=\"a-icon i-new m-l-xs m-r-xs\" type=\"button\" href=\"javascript:archive_doc.save('";
	nameInputSpan += id;
	nameInputSpan += "');\" role=\"button\" >确 定</a><a class=\"a-icon i-new\" type=\"button\" href=\"javascript:archive_doc.cancel('";
	nameInputSpan += id;
	nameInputSpan += "');\" role=\"button\" >取 消</a></span>";

	cell = "<td>";
	cell += label;
	cell += nameSpan;
	cell += nameInputSpan;
	cell += "</td>";

	return cell;
};
// 插入新目录行
archive_doc.insertDirRow = function(o, editable) {
	var dirRow = "";
	dirRow += ("<tr id=\"dir_" + o.id + "\" class=\"dir\">");
	dirRow += (archive_doc.createCheckCell(o));
	dirRow += (archive_doc.createDirNameCell(o));
//	dirRow += (archive_doc.createDirOpCell(o));新建文件夹时不显示操作
	dirRow += (archive_doc.createSimpleCell(""));
	dirRow += (archive_doc.createSimpleCell("文件夹"));
	dirRow += (archive_doc.createSimpleCell(""));
	dirRow += (archive_doc.createSimpleCell(o.modifyDate));
	dirRow += (archive_doc.createSimpleCell(o.owner));
	dirRow += "</tr>";

	var firstRow = $('#doc_table > tbody .dir');
	if (firstRow.length > 0) {
		$(dirRow).insertBefore(firstRow[0]);
	} else {
		$('#doc_table > tbody').append(dirRow);
	}

	$('#btnNewFolder').attr("disabled", "disabled");

	if (editable) {
		$("#dir_" + o.id).children('td').children('.file_name').hide();
		$("#dir_" + o.id).children('td').children('.file_name_input').show();
		$("#dir_" + o.id).children('td').children('.file_name_input').children(
				'input').focus();
	} else {
		$("#dir_" + o.id).children('td').children('.file_name').show();
		$("#dir_" + o.id).children('td').children('.file_name_input').hide();
	}
	archive_doc.processEmptyTable();
	$("i[data-toggle='tooltip']").tooltip();
};
// 插入文件行
archive_doc.insertDocRow = function(o, editable) {
	docRow = "";
	docRow += ("<tr id=\"doc_" + o.id + "\" class=\"doc\">");
	docRow += (archive_doc.createCheckCell(o));
	docRow += (archive_doc.createDocNameCell(o));
	docRow += (archive_doc.createDocOpCell(o));
	// docRow += (archive_doc.createSimpleCell(o.dmLockStatus == 1 ? "锁定" : '未锁定'));
	// docRow+=(self.createSimpleCell(o.contentType));
	docRow += (archive_doc.createSimpleCell(o.dmSuffix));
	docRow += (archive_doc.createSimpleCell(o.dmSize));
	docRow += (archive_doc.createSimpleCell(o.modifyDate));
	docRow += (archive_doc.createSimpleCell(o.createUser));
	docRow += "</tr>";

	var firstRow = $('#doc_table > tbody .doc').first();
	if (firstRow.length > 0) {
		$(docRow).insertBefore(firstRow);
	} else {
		$('#doc_table > tbody').append(docRow);
	}
	archive_doc.processEmptyTable();
	$("i[data-toggle='tooltip']").tooltip();
};

// ---------表格操作结束-------------------

// 返回上级目录
archive_doc.goParent = function() {
	$('#btnNewFolder').removeAttr("disabled");
	if (backFlag <= 0) {
		return;
	}
	
	archive_doc.pathBar.back();
//	$.ajax({
//		type : "GET",
//		url : getRootPath() + "/archive/folder/getParentPubDirDocs.action",
//		data : {
//			id : $('#folderId').val()
//		},
//		dataType : "json",
//		success : function(data) {
//			if (data.success == "true") {
//				$('#doc_table > tbody').empty();
//				$('#folderId').val(data.folder.id);
//				backFlag -= 1;
//				archive_doc.render(data.folder);
////				archive_doc.permission();
//			} else {
//				if (data.errorMessage) {
//					msgBox.tip({
//						content : data.errorMessage
//					});
//				}
//			}
//		}
//	});
	$('#btnReturn').blur();
};
// 显示上传组件
archive_doc.showUpload = function() {
	$('#file-edit').modal("show");
	attachPool.clearFileQueue('#uploader');
	attachPool.add({
		form: {
			containerId: '#uploader',
			businessId: $('#id').val(),
			businessTable: 'toa_archive_document',
			category: 'picList1'
		}
	});
};
// 完成文件上传的回调
archive_doc.finishUpload = function() {
	// 取到上传的文件信息//循环插入文件信息行
	var files = new Array();

	$.each($("input[name='fileid']"), function(i, o) {
		files.push(o.value);
	});
	//没有上传文件
	if(files.length==0){
		return;
	}
	
	$.ajax({
		type : "Post",
		url : getRootPath() + "/archive/folder/uploadDocs.action",
		data : {
			'fileids' : files.join(),
			model:1,
			id : $('#folderId').val(),
			
		},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({
					type : "success",
					content : data.successMessage
				});
				$.each(data.documents, function(i, d) {
					archive_doc.insertDocRow(d, false);
				});
				/*clearTable();*/
				attachPool.clearFileQueue('#uploader');
				archive_doc.loadData(archive_doc.getFolderId());
			} else {
				if (data.errorMessage) {
					msgBox.info({
					//msgBox.tip({
						content : data.errorMessage
					});
				}
			}
		}
	});
};
// 取消或点关闭删除上传的文件
archive_doc.deleteAttach = function() {
	var ids = "";
	var fileids = new Array();
	$.each($("input[name='fileid']"), function(i, o) {
		fileids.push(o.value);
	});
	if (fileids == null) {
		return;
	}
	ids = fileids.join(",");
	$.ajax({
		type : "POST",
		url : getRootPath() + "/content/attach/delete.action",
		data : {
			"ids" : ids
		},
		dataType : "json",
		success : function(data) {

		}
	});
};
// 新建文件夹插入新行
archive_doc.insertNewRow = function() {
	//getToken();
	var o = {
		folderName : '',
		id : 0,
		modifyDate : '',
		createUser : ''
	};
	archive_doc.insertDirRow(o, true);
};

// 重命名文件/文件夹名称
archive_doc.rename = function(id) {
	//取消其它行的编辑状态
	var editing=$('tr >td > .file_name:hidden');
	if(editing!="undefined" && editing.length>0){
		$.each(editing,function(i,ele){
			archive_doc.cancel('#'+$(ele).closest('tr').attr('id'));
		});
	}
	
	$(id).children('td').children('.file_name').hide();
	$(id).children('td').children('.file_name_input').show();
	$(id).children('td').children('.file_name_input').children('input').select();
};

// 收藏
archive_doc.controller = function(id) {
	$.ajax({
				type : "GET",
				url : getRootPath()
						+ "/archive/document/selectCollection.action",
				data : {
					"id" : id
				},
				dataType : "json",
				success : function(data) {
					if (data.success == "true") {
						msgBox
								.confirm({
									content : "已收藏是否覆盖原数据!",
									success : function() {
										$
												.ajax({
													type : "GET",
													url : getRootPath()
															+ "/archive/document/updateCollection.action",
													data : {
														"id" : id
													},
													dataType : "json",
													success : function(data) {
														if (data.success == "true") {
															msgBox
																	.tip({
																		type : "success",
																		content : data.successMessage
																	});
														} else {
															msgBox
																	.info({
																		content : data.errorMessage
																	});
														}
													}
												});
									}
								});
					} else {
						$
								.ajax({
									type : "GET",
									url : getRootPath()
											+ "/archive/document/insertCollection.action",
									data : {
										"id" : id
									},
									dataType : "json",
									success : function(data) {
										if (data.success == "true") {
											msgBox.tip({
												type : "success",
												content : data.successMessage
											});
										} else {
											msgBox.tip({
												content : data.errorMessage
											});
										}
									}
								});
					}
				}
			});
};
// 新建、修改名称之后的保存操作
archive_doc.save = function(id) {

	var file_name_node = $(id).children('td').children('.file_name_input')
			.children('input');
	$(id).children('td').children('.file_name_input').children('label[for]').remove();
	var formId = "form";
	file_name_node.wrap("<form id=\"form\"></form>");
	//archive_doc.bindingFolderValidator();
	// 验证文件/文件夹名通过后，提交保存
	if (archive_doc.bindingFolderValidator(id)) {
		file_name_node.unwrap();
		var url = getRootPath() + "/archive";
		var data = {};
		// 保存文件夹名
		if (id.indexOf('dir') > -1) {
			url += "/folder";
			data = {
				"folderName" : $(id).children('td')
						.children('.file_name_input').children('input').val(),
				"parentFolderId" : $('#folderId').val(),
				"folderType" : 1,
				"kmAppFlag" : 0,
				"token" : $('#token').val()
			};
			if ( id.substr(5) == '0') {
				url += "/save.action";
			} else {
				url += "/update.action";

				data.id = id.substr(5);
			}

		}
		// 保存文件名
		else if (id.indexOf('doc') > -1) {
			url += "/document";
			data = {
				"dmName" : $(id).children('td')
						.children('.file_name_input').children('input').val(),
				"folderId" : $('#folderId').val(),
				"folderType" : 0,
				"token" : $('#token').val()
			};
		
			url += "/update.action";
			data.id = id.substr(5);
			
		} else {
			// error
		}
		data.model = 1;

		$("#dataLoad").show();

		$.ajax({
			type : "POST",
			url : url,
			data : data,
			dataType : "json",
			success : function(data) {
				$('#token').val(data.token);
				if (data.success == "true") {
					msgBox.tip({
						type : "success",
						content : data.successMessage
					});

					// 如果是新建操作的保存，应该取消新建文件夹按钮的禁用状态，并且重设RowId
					if (id == '#dir_0') {
						$('#btnNewFolder').removeAttr("disabled");
//						$(id).remove();
//						archive_doc.insertDirRow(data.folder);
					} else {
						$(id).children('td').children('.file_name').children(
								'a').html(
								$(id).children('td').children(
										'.file_name_input').children('input')
										.val());
						$(id).children('td').children('.file_name').show();
						$(id).children('td').children('.file_name_input')
								.hide();
					}

					$('#btnNewFolder').removeAttr("disabled");
					archive_doc.reloadData();
					archive_doc.processEmptyTable();
				} else {
					if (data.labelErrorMessage) {
						msgBox.tip({
							content : data.labelErrorMessage
						});
					}
					if (data.errorMessage) {
						msgBox.tip({
							content : data.errorMessage
						});
					}

				}
				$("#dataLoad").hide();
				//getToken();
			},
			error : function() {
				msgBox.tip({
					content : $.i18n.prop("JC_SYS_002")
				});
				$("#dataLoad").hide();
			}
		});
	}
	else{
		file_name_node.unwrap();
	}
};
// 取消修改、取消新建
archive_doc.cancel = function(id) {
	$(id).children('td').children('.file_name').show();
	$(id).children('td').children('.file_name_input').hide();
	$(id).children('td').children('.file_name_input').children('input').val($(id).children('td').children('.file_name').children("a").html());
	// 如果是新建操作的取消，应该取消新建文件夹按钮的禁用状态，并且Remove当前行
	if (id == '#dir_0') {
		$('#btnNewFolder').removeAttr("disabled");
		$(id).remove();
	}
	if(folder_.subdirs.length < 1 && folder_.documents.length < 1) {
		archive_doc.processEmptyTable();
	}
	//archive_doc.reloadData();
};

// 显示授权窗口
archive_doc.auth = function(folerId) {
	$('#folder').val(folerId);
	// archive_doc.oTable = null;
	$('#authority-management').modal('show');
	//添加按钮绑定事件
	$("#addBtn").click(archive_doc.addBtn);
	//分页查询方法
	archive_doc.initList();
};


// 删除方法
archive_doc.deleteClick = function() {
	var idsArr = [];
	$("[name='ids']:checked").each(function() {
		var id = $(this).val();
		var rowId = id.substring(1);
		rowId = rowId.substring(0,rowId.length-2);
		//alert(rowId);
		var r = document.getElementById(rowId);
		if("none" != r.style.display) {
			idsArr.push($(this).val());
		}
		//alert(r.style.visible);
	});
	var ids = idsArr.join(";");

	if (ids.length == 0) {
		msgBox.tip({
			content : $.i18n.prop("JC_SYS_061"),
			type : "fail"
		});
		return;
	}

	msgBox.confirm({
		content : $.i18n.prop("JC_OA_ARCHIVE_013"),
		success : function() {
			archive_doc.bulkdeleteDirDoc(ids);
		}
	});
};
archive_doc.rowDeleteClick = function(rowId) {
	msgBox.confirm({
		content : $.i18n.prop("JC_OA_ARCHIVE_013"),
		success : function() {
			if (rowId.indexOf('dir') > -1){
				archive_doc.deleteDirDoc(rowId,0);
			}else{
				archive_doc.deleteDirDoc(rowId,1);
			}
			
		}
	});
};
//批量删除的方法
archive_doc.bulkdeleteDirDoc = function(rowId) {
	$.ajax({
		type : "GET",
		url : getRootPath() + "/archive/folder/bulkDelete.action",
		data : {"id": rowId},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({
					content : data.successMessage,
					type : "success"
				});
				
				//$(rowId).remove();
				$("[name='checkbox']").removeAttr("checked");//取消全选 
				archive_doc.processEmptyTable();
				archive_doc.reloadData();
			} else {
				if (data.labelErrorMessage) {
					msgBox.tip({
						content : data.labelErrorMessage
					});
				}
				if (data.errorMessage) {
					msgBox.tip({
						content : data.errorMessage
					});
				}

			}
		},
		error : function() {
			msgBox.tip({
				content : $.i18n.prop("JC_SYS_002")
			});
		}
	});
};
archive_doc.deleteDirDoc = function(rowId,type) {
	$.ajax({
		type : "GET",
		url : getRootPath() + "/archive/folder/batchDelete.action",
		data : {"id": rowId.substr(5), "type":type},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({
					content : data.successMessage,
					type : "success"
				});
				
				$(rowId).remove();
				archive_doc.processEmptyTable();
				archive_doc.reloadData();
			} else {
				if (data.labelErrorMessage) {
					msgBox.tip({
						content : data.labelErrorMessage
					});
				}
				if (data.errorMessage) {
					msgBox.tip({
						content : data.errorMessage
					});
				}

			}
		},
		error : function() {
			msgBox.tip({
				content : $.i18n.prop("JC_SYS_002")
			});
		}
	});
};

archive_doc.currentDMName = '';
archive_doc.editDoc = function(contentType, path, name, id,dmLockStatus) {
	archive_doc.currentDMName = name;
	archive_doc.currentDocumentId = id;
	if("1" == dmLockStatus) {
		msgBox.tip({
			content : $.i18n.prop("JC_OA_ARCHIVE_015")
		});
		return;
	}
	if(archive_doc.hasOpen) {
		return;
	}
	if (contentType == 1 || contentType == 2 || contentType == 3) {
		archive_doc.viewFile(contentType, 1, base64.decode(path));
		archive_doc.hasOpen = true;
		// word
		// ppt
		// exl
	} else {
		// 其他
	}
};

//检查是否为锁定文档
archive_doc.checkLock = function(id) {
	var lock = $('#doc_'+id).children("td").eq(2).html();
	if("锁定" == lock) {
		msgBox.tip({
			content : $.i18n.prop("JC_OA_ARCHIVE_015")
		});
		return false;
	}
	return true;
};
//下载文档
archive_doc.download = function(id) {
	if(archive_doc.checkLock(id)) {
		$.ajax({
			type : "post",
			url : getRootPath()+"/archive/folder/checkDocFileExist.action",
			data : {"id":id},
			dataType : "json",
			success : function(data) {
				if (data == true) {
					window.open(getRootPath() + "/archive/folder/download.action?id="
							+ id, "下载文档");
				} else {
					msgBox.tip({
						content: $.i18n.prop("JC_OA_ARCHIVE_023")
					});
				}
			}
		});
	}
	
};

// 显示文档
archive_doc.showDocContent = function(id) {
	$.ajax({
	type : "post",
	url : getRootPath()+"/archive/folder/checkDocFileExist.action",
	data : {"id":id},
	dataType : "json",
	success : function(data) {
		if (data == true) {
			window.open(getRootPath() + "/archive/folder/showDocContent.action?id="
					+ id, "查看文档");
		} else {
			msgBox.tip({
				content: $.i18n.prop("JC_OA_ARCHIVE_023")
			});
		}
	}
});
};

// 页面搜索
archive_doc.onFilter = function(ev) {
	var tbody = $('#doc_table > tbody');
	tbody.children('.empty_row').remove();
	var filterField = jQuery(this);
	var contacts = $('#doc_table');
	var input = filterField.val();

	var names = contacts.find("tr .file_name");
	var bool =0;
	input = input.toUpperCase();
	names.each(function(i, node) {
		var el = jQuery(node);
		var name = el.text();
		name = name.toUpperCase();
		var match = name.indexOf(input) >= 0;
		if(match==true){
			bool = 1;
		}
		var contact = el.closest("tr");
		if (match) {
			contact.show();
		} else {
			contact.hide();
		}
	});
	if(bool==0){
		tbody.append(archive_doc.createEmptyRow());
	}
};

/**
 * 绑定文件夹行的验证器 文件夹名称：不能为空，最大长度为64
 */
/*archive_doc.bindingFolderValidator = function() {
	$('#form').validate({
		ignore : ".ignore",
		rules : {
			folder_name : {
				required : true,
				maxlength : 64,
				fileName:true
			}
		}
	});
};*/


/**
 * 绑定文件夹行的验证器 文件夹名称：不能为空，最大长度为64
 */
archive_doc.bindingFolderValidator = function(id) {
	var reg = new RegExp('^[^\\\\\\/:*\'"?\\"<>|]+$');// 转义 \ 符号也不行
	//var $ele = $('#input_');
	var $ele = $(id).find('input[name!="ids"]');
	function valid(value ,target){
		var val = $.trim(value);
		var errorText = '';
		if(!val.length){
			errorText = '此信息不能为空';
		}else if(val.length > 64) {
			errorText = '名称长度不能大于64';
		}
		if(errorText == '' && !reg.test(val)){
			errorText = '名称中含有特殊字符';
		}
		if(errorText == ''){
			if(target) $ele.removeClass('error').next('label').remove();
			return true;
		}else{
			if($('#archiveDocFormLabel').length){
				$('#archiveDocFormLabel').remove();
			}
			var errorHtml = '<label class="error" id="archiveDocFormLabel" style="display: inline;width:200px;">'+errorText+'</label>';
			$ele.addClass('error').after(errorHtml).off().on('keyup' , function(e){
				valid(e.target.value,true);
				return false;
			});
			return false;
		}
	}
	return valid($ele.val());
};

archive_doc.newWord = function(flag) {
	if(archive_doc.hasOpen) {
		return;
	}
	archive_doc.documentType = 'doc';
	var url=getRootPath() + "/archive/folder/newDocument.action?documentType=doc";
	url+=("&documentPath="+archive_doc.documentPath);
	url+=("&folderId="+archive_doc.getFolderId());
	url+=("&currentDocumentId="+archive_doc.currentDocumentId);
	url+=("&model="+archive_doc.model);
	archive_doc.hasOpen = true;
	window.open(encodeURI(url));
};
archive_doc.newExl = function() {
	if(archive_doc.hasOpen) {
		return;
	}
	archive_doc.documentType = 'xls';
	var url=getRootPath() + "/archive/folder/newDocument.action?documentType=xls";
	url+=("&documentPath="+archive_doc.documentPath);
	url+=("&folderId="+archive_doc.getFolderId());
	url+=("&currentDocumentId="+archive_doc.currentDocumentId);
	url+=("&model="+archive_doc.model);
	archive_doc.hasOpen = true;
	window.open(encodeURI(url));
};
archive_doc.newPPT = function() {
	archive_doc.documentType = 'ppt';
	var url=getRootPath() + "/archive/folder/newDocument.action?documentType=ppt";
	url+=("&documentPath="+archive_doc.documentPath);
	url+=("&folderId="+archive_doc.getFolderId());
	url+=("&currentDocumentId="+archive_doc.currentDocumentId);
	url+=("&model="+archive_doc.model);
	window.open(encodeURI(url));
};


/**
 * 复制点击事件
 */
archive_doc.clickCopy=function(rowId){
	archive_doc.currentSelectedRowId=rowId;
	if(rowId.indexOf('dir')>0){
		archive_doc.folderSelector.setCallback(archive_doc.copyDirTo);
	}else{
		archive_doc.folderSelector.setCallback(archive_doc.copyDocTo);
	}
	archive_doc.folderSelector.show();
};


/**
 * 文件夹：复制到 文件夹选择树回调函数
 */
archive_doc.copyDirTo=function(id){
	if(archive_doc.currentSelectedRowId==null ){
		msgBox.tip({
			content : $.i18n.prop("JC_SYS_060")
		});
	}
	$.ajax({
		type : "GET",
		url : getRootPath() + "/archive/folder/copyDirTo.action",
		data : {
			id:archive_doc.currentSelectedRowId.substr(5),
			parentFolderId : id
		},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({
						type : "success",
						content : data.successMessage
				});
				archive_doc.reloadData()
				archive_doc.currentSelectedRowId=null;
				$('#folderSelector').modal('hide');
			} else {
				if (data.errorMessage) {
					msgBox.tip({
						content : data.errorMessage
					});
				} else {
					msgBox.tip({
						content : $.i18n.prop("JC_SYS_060")
					});
				}
			}
		}
	});
};

/**
 * 文档：复制到 文件夹选择树回调函数
 */
archive_doc.copyDocTo=function(id){
	if(archive_doc.currentSelectedRowId==null ){
		msgBox.tip({
			content : $.i18n.prop("JC_SYS_060")
		});
	}
	$.ajax({
		type : "GET",
		url : getRootPath() + "/archive/folder/copyDocTo.action",
		data : {
			id:archive_doc.currentSelectedRowId.substr(5),
			folderId : id
		},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({
						type : "success",
						content : data.successMessage
				});
				archive_doc.reloadData()
				archive_doc.currentSelectedRowId=null;
				$('#folderSelector').modal('hide');
			} else {
				if (data.errorMessage) {
					msgBox.tip({
						content : data.errorMessage
					});
				} else {
					msgBox.tip({
						content : $.i18n.prop("JC_SYS_060")
					});
				}
			}
		}
	});
};

/**
 * 剪切点击事件
 */
archive_doc.clickCut=function(rowId){
	archive_doc.currentSelectedRowId=rowId;
	if(rowId.indexOf('dir')>0){
		archive_doc.folderSelector.setCallback(archive_doc.cutDirTo);
	}else{
		archive_doc.folderSelector.setCallback(archive_doc.cutDocTo);
	}
	archive_doc.folderSelector.show();
};

/**
 * 文件夹：剪切到 文件夹选择树回调函数
 */
archive_doc.cutDirTo=function(id){
	if(archive_doc.currentSelectedRowId==null ){
		msgBox.tip({
			content : $.i18n.prop("JC_SYS_060")
		});
	}
	$.ajax({
		type : "GET",
		url : getRootPath() + "/archive/folder/cutDirTo.action",
		data : {
			id:archive_doc.currentSelectedRowId.substr(5),
			parentFolderId : id
		},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({
						type : "success",
						content : data.successMessage
				});
				archive_doc.reloadData()
				archive_doc.currentSelectedRowId=null;
				$('#folderSelector').modal('hide');
			$(archive_doc.currentSelectedRowId).remove();
			} else {
				if (data.errorMessage) {
					msgBox.tip({
						content : data.errorMessage
					});
				} else {
					msgBox.tip({
						content : $.i18n.prop("JC_SYS_060")
					});
				}
			}
		}
	});
};

/**
 * 文档：剪切到 文件夹选择树回调函数
 */
archive_doc.cutDocTo=function(id){
	if(archive_doc.currentSelectedRowId==null ){
		msgBox.tip({
			content : $.i18n.prop("JC_SYS_060")
		});
	}
	$.ajax({
		type : "GET",
		url : getRootPath() + "/archive/folder/cutDocTo.action",
		data : {
			id:archive_doc.currentSelectedRowId.substr(5),
			folderId : id
		},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				$(archive_doc.currentSelectedRowId).remove();
				msgBox.tip({
						type : "success",
						content : data.successMessage
				});
				$('#folderSelector').modal('hide');
				archive_doc.reloadData();
				archive_doc.currentSelectedRowId=null;
			} else {
				if (data.errorMessage) {
					msgBox.tip({
						content : data.errorMessage
					});
				} else {
					msgBox.tip({
						content : $.i18n.prop("JC_SYS_060")
					});
				}
			}
		}
	});
};

/**
 * 文档信息
 */
archive_doc.getDocInfo=function(id){
	$.ajax({
		type : "GET",
		url : getRootPath() + "/archive/document/get.action",
		data : {
			id:id
		},
		dataType : "json",
		success : function(data) {
			if (data!=null) {
				var lock='未锁定';
				var unlock='锁定';

				var nameHtml = '<span title="'+data.dmName+'">'+data.dmName+'</span>';
				$('#info_dmName').html(nameHtml);
				$('#info_dmSize').html(data.dmSize);
				$('#info_createDate').html(data.createDate);
				$('#info_dmDir').html(data.dmDir);
				document.getElementById('info_dmDir').title = data.dmDir;
				//$('#info_dmName').html(data.dmName);
				$('#info_currentVersion').html(data.maxVersion);
				$('#info_modifyDate').html(data.modifyDate);
				$('#info_seq').html(data.seq);
				$('#info_createUser').html(data.owner);
				//$('#info_dmLockStatus').html(data.dmLockStatus == 1 ? unlock :lock);
				$('#info_keyWords').html(data.keyWords);
				$('#info_dmType').html(data.dmType);
				$('#document-information').modal('show');
			} else {
				if (data.errorMessage) {
					msgBox.tip({
						content : data.errorMessage
					});
				} else {
					msgBox.tip({
						content : $.i18n.prop("JC_SYS_060")
					});
				}
			}
		}
	});
};


//显示列信息
//
archive_doc.oTableAudithis = [
  {mData:function(source) {
	  return "<span title='"+source.userName+"'>"+source.userName+"</span>";
  }},// 操作人员
	{mData:function(source) {
		return "<span title='"+source.operDesc+"'>"+source.operDesc+"</span>";
	}},// 事件描述
	{mData: "ip"},// 操作IP
	{mData: "operTime"}//操作时间
];

var auditId = null;
//组装后台参数
archive_doc.oTableFnServerParamsAudithis = function(aoData){
	//排序条件
	getTableParameters(archive_doc.auditTable, aoData);
	aoData.push({"name":"dataId","value":auditId});
};

//文档审计
archive_doc.audithis = function(id){
	 archive_doc.auditTable = null;
	auditId = id;
	$('#document-the-audit').modal('show');
	archive_doc.audithisList(id);
};

archive_doc.audithisList = function (id) {
	if (archive_doc.auditTable == null) {
		var tableHtml = '<table class="table table-striped tab_height" id="audithisList" >';
		tableHtml += '<thead><tr>';
		tableHtml += '<th style="width:105px;">操作人</th>';
		tableHtml += '<th>日志</th>';
		tableHtml += '<th>IP</th>';

		tableHtml += '<th>时间</th></tr>';
		tableHtml += '</thead><tbody></tbody>';
		tableHtml += '</table>';
		$('#audithisListDiv').html(tableHtml);
		archive_doc.auditTable = $('#audithisList').dataTable( {
			bDestroy:true,
			"iDisplayLength": archive_doc.pageRows,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/archive/audithis/manageList.action",//后台分页查询地址url
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": archive_doc.oTableAudithis,//table显示列
			"fnServerParams": function ( aoData ) {
				archive_doc.oTableFnServerParamsAudithis(aoData,id);
			},
			aaSorting:[],//设置表格默认排序列
			//默认不排序列
	        "aoColumnDefs": [{"bSortable": false, "aTargets": [0,1]}]
		});
	} else {
		//table不为null时重绘表格
		archive_doc.auditTable.fnDraw();
	}
};

//退出本页面前回调
function pageRedirecting(){
	archive_doc=undefined;
};

// 初始化
jQuery(function($) {
	//计算分页显示条数
	archive_doc.pageRows = TabNub > 0 ? TabNub : 10;
	// $('#dSection').css('height',$(window).height() - 270);
	$('#btnNewFolder').click(archive_doc.insertNewRow);
	$('#btnReturn').click(archive_doc.goParent);
	$('#btnReturnDisabled').click(archive_doc.goParent);
	$('#btnUpload').click(archive_doc.showUpload);
	$('#closeModalBtn').click(archive_doc.finishUpload);
	$('#closebtn').click(archive_doc.finishUpload);
	$('#btnDelete').click(archive_doc.deleteClick);
	$('#newPPT').click(archive_doc.newPPT);
	$('#newWord').click(archive_doc.newWord);
	$('#newExl').click(archive_doc.newExl);
	$('#search').on("input", archive_doc.onFilter);
	$('#search').on("keyup", archive_doc.onFilter);
	$(".tree-list .tree-btn").on(
			'click',
			function(e) {
				e.preventDefault();
				e.stopPropagation();
				$(this).find("i").toggleClass("fa-chevron-down").end().closest(
						".tree-list").next().slideToggle();
			});
	$(".btn-new").mouseenter(function() {
		$(".document-new").show();
		setTimeout('$(".document-new").hide()', 5000);
	});
	$(".document-new").mouseleave(function() {
		$(this).hide();
	});

	$('#businessSource').val('0');// 默认的值为0 表示来源于OA，1表示来源于CRM
	
	archive_doc.folderSelector=new FolderSelectTree($('#folderSelector'),null,1);
	
	// menuswrite.statue = true;
	
	archive_doc.pathBar=new FolderPathBar($('#folderTable'),archive_doc.loadData);
	
	archive_doc.init();
});

//@ sourceURL=my_doc.js