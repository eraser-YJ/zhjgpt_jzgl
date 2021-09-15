/**
 * 文档管理 zhanglg
 */
var base64 = new Base64();
var selectControl = null;
var deptAndPersonControl = null;
var updateOrInsert = false; //判断是新增还是修改权限
String.prototype.trim=function(){return this.replace(/(^\s*)|(\s*$)/g,"");};
var archive_doc = {
	permissionValue:"",
	permissionId:"",
	subState:false,
	documentType : 'doc',
	viewType : 1,// null只读，1编辑
	documentPath : '',
	folderSelector:null,
	model:0,
	currentSelectedRowId:null,
	currentDocumentId:null,
	folder:null
};
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
	archive_doc.loadData(null,null,null,null,true);
};

archive_doc.reloadData = function() {
	archive_doc.clearParam();
	archive_doc.loadData(archive_doc.getFolderId(),null,null,null,true);
};
archive_doc.getFolderId = function() {
	return $('#folderId').val();
};
// WebOffice在线查看Office
archive_doc.viewFile = function(contentType, isEdit, path) {
	// word xls ppt 文件
	archive_doc.viewType = isEdit;// null 只读， 1 编辑
	archive_doc.documentPath = basePath_  +"/"+ path;// null 只读， 1 编辑
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
			archive_doc.viewFile(contentType, null, path);
		}
	} else {
		// 加载文件夹
		return $.ajax({
			type : "GET",
			url : getRootPath() + "/archive/folder/getFileDirDocs.action",
			data : {
				id : folderId,
				t:(new Date()).getTime()
			},
			dataType : "json",
			success : function(data) {
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
						msgBox.info({
							content : data.errorMessage
						});
					} else {
						msgBox.info({
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
		// dirRow += (self.createCheckCell(o));
		dirRow += (self.createDirNameCell(o));
		// dirRow += (self.createDirOpCell(o));
		// dirRow += (self.createSimpleCell(""));
		// dirRow += (self.createSimpleCell("文件夹"));
		// dirRow += (self.createSimpleCell(""));
		dirRow += (self.createSimpleCell(""));
		dirRow += (self.createSimpleCell(""));
		dirRow += (self.createDelect(o.id));
		dirRow += "</tr>";
	});
	tbody.append(dirRow);
	// process documents
	$.each(folder.documents, function(i, o) {
		docRow += ("<tr id=\"doc_" + o.id + "\" class=\"doc\">");
		// docRow += (self.createCheckCell(o));
		docRow += (self.createDocNameCell(o));
		// docRow += (self.createDocOpCell(o));
		// docRow += (self.createSimpleCell(o.dmLockStatus == 1 ? "锁定" : '未锁定'));
		// docRow+=(self.createSimpleCell(o.contentType));
		// docRow += (self.createSimpleCell(o.dmSuffix));
		// docRow+=(self.createSimpleCell(o.dmLockStatus));
		// docRow+=(self.createSimpleCell(o.contentType));
		// docRow += (self.createSimpleCell(o.dmSize));
		docRow += (self.createSimpleCell(o.createDate));
		docRow += (self.createSimpleCell(o.owner));
		docRow += (self.createDelectDoc(o.id,o.extStr5));
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
	return ininFolderPermission(folder);
	/*var option = "<td>";
	option += "<div class=\"dropdown inline\"> ";
	option += "<a href=\"#\" type=\"button\" class=\"btn-uploading\" data-toggle=\"dropdown\"><i data-original-title=\"更多\" data-container=\"body\" title=\"\" data-placement=\"top\" data-toggle=\"tooltip\"  class=\"fa fa-more-list\"></i></a> ";
	option += "        <ul class=\"dropdown-menu animated fadeInRight\">            ";
	if(archive_doc.permissionValue.substring(0, 1)==1){
		if(archive_doc.permissionValue.substring(5, 6)==1){
			option += "          <li><a type=\"button\" href=\"javascript:archive_doc.clickCopy('#dir_"+folder.id+"')\" role=\"button\" >复制到...</a></li> ";
			option += "          <li><a type=\"button\" href=\"javascript:archive_doc.clickCut('#dir_"+folder.id+"')\" role=\"button\" >剪切到...</a></li> ";
		}
		if(archive_doc.permissionValue.substring(6, 7)==1){
			option += "			 <li><a type=\"button\" href=\"javascript:archive_doc.rename('#dir_" + folder.id + "');\" role=\"button\" >重命名</a></li> ";
		}
		if(archive_doc.permissionValue.substring(4, 5)==1){
			option += "          <li><a type=\"button\" href=\"javascript:archive_doc.rowDeleteClick('#dir_"+folder.id+"')\" role=\"button\" >删除</a></li>  ";
		}
	}
	option += "			 <li><a type=\"button\" href=\"#document-information\" role=\"button\" data-toggle=\"modal\">授权</a></li> ";
	option += "        </ul> ";
	option += "    </div> </td>";*/
};

// 生成文件操作单元
archive_doc.createDocOpCell = function(doc) {
	var p = doc.physicalPath;
	if (p && p.length > 0) {
		p = base64.encode(p);
	}
	var allPermission  = doc.createUser == currentUserId;
	var option = "<td>";
	if(archive_doc.folder.permView==true || allPermission){
		if(archive_doc.folder.permNewUpDown==true || allPermission){
			option += "<a href=\"javascript:javascript:archive_doc.download('"
					+ doc.id
					+ "');\" ><i data-original-title=\"下载\" data-container=\"body\" title=\"\" data-placement=\"top\" data-toggle=\"tooltip\"  class=\"fa fa-download\"></i></a>";
		}
		var office = (doc.contentType == 1 || doc.contentType == 2 || doc.contentType == 3);
		if(archive_doc.folder.permEdit==true || allPermission){
			if(office) {
				option += "<a href=\"javascript:void(0)\"><i data-original-title=\"编辑\" data-container=\"body\" title=\"\" data-placement=\"top\" data-toggle=\"tooltip\" onclick=\"archive_doc.editDoc('"
					+ doc.contentType
					+ "','"
					+ p
					+ "','"
					+ doc.dmName
					+ "','"
					+ doc.id
					+ "','"
					+ doc.dmLockStatus
					+ "')\"  class=\"fa fa-compose\"></i></a>";
			}
		}
	}
	option += "<div class=\"dropdown inline\"> ";
	option += "<a href=\"#\" type=\"button\" class=\"btn-uploading\" data-toggle=\"dropdown\"><i data-original-title=\"更多\" data-container=\"body\" title=\"\" data-placement=\"top\" data-toggle=\"tooltip\"  class=\"fa fa-more-list\"></i></a> ";
	option += "        <ul class=\"dropdown-menu animated fadeInRight\">            ";
	if(archive_doc.folder.permView==true || allPermission){
		if(archive_doc.folder.permCopyPaste==true || allPermission){
			option += "			<li><a type=\"button\" href=\"javascript:archive_doc.clickCopy('#doc_"+doc.id+"')\" role=\"button\" >复制到...</a></li> ";
			option += "			 <li><a type=\"button\" href=\"javascript:archive_doc.clickCut('#doc_"+doc.id+"')\" role=\"button\" >剪切到...</a></li> ";
		}

		if(archive_doc.folder.permRename==true || allPermission){
			option += "			 <li><a type=\"button\" href=\"javascript:archive_doc.rename('#doc_" + doc.id + "');\" role=\"button\" >重命名</a></li> ";
		}

		if(archive_doc.folder.permDelete==true || allPermission){
			option += "			 <li><a type=\"button\" href=\"javascript:archive_doc.rowDeleteClick('#doc_"+doc.id+"')\" role=\"button\" >删除</a></li> ";
		}

		if(archive_doc.folder.permCollect==true || allPermission){
			option += "			 <li><a type=\"button\" href=\"javascript:archive_doc.controller('" + doc.id + "');\" data-toggle=\"modal\">收藏</a></li> ";
		}
	}

	/*option += "			 <li><a type=\"button\" href=\"#associated-Document\" role=\"button\" data-toggle=\"modal\">锁定</a></li> ";*/
	option += "			 <li><a type=\"button\" href=\"javascript:archive_doc.getDocInfo('"+doc.id+"');\" role=\"button\">文档信息</a></li> ";
	if(archive_doc.folder.permView==true || allPermission){
		var office = (doc.contentType == 1 || doc.contentType == 2 || doc.contentType == 3);
		if(archive_doc.folder.permVersion==true || allPermission){
			if(office) {
			option += "          <li><a type=\"button\" href=\"javascript:archive_doc.version('"+doc.id+"');\" role=\"button\">版本管理</a></li> ";
			}
		}

		if(archive_doc.folder.permHistory==true || allPermission){
			option += "          <li><a type=\"button\" href=\"javascript:archive_doc.audithis('"+doc.id+"');\" role=\"button\">文档审计</a></li> ";
		}

		if(archive_doc.folder.permRelate==true || allPermission){
			option += "          <li><a type=\"button\" href=\"#associated-Document\" role=\"button\" data-toggle=\"modal\" onclick=\"archive_doc.showRelateDiv("+doc.id+")\">关联文档</a></li>  ";
		}
	}
	option += "        </ul> ";
	option += "    </div> </td>";
	return option;
};

//显示列信息
archive_doc.oTableAudithis = [
    {mData:"userName"},// 操作人员
	{mData: "operDesc"},// 事件描述
	{mData: "ip"},// 操作IP
	{mData: "operTime"}//操作时间
];

//组装后台参数
archive_doc.oTableFnServerParamsAudithis = function(aoData){
	//排序条件
	getTableParameters(archive_doc.oTable, aoData);
};

// 文档审计
archive_doc.audithis = function(id){
	archive_doc.oTable = null;
	$('#document-the-audit').modal('show');
	archive_doc.audithisList(id);
};

archive_doc.audithisList = function (id) {
	if (archive_doc.oTable == null) {
		archive_doc.oTable = $('#audithisList').dataTable( {
			bDestroy:true,
			"iDisplayLength": archive_doc.pageRows,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/archive/audithis/manageList.action?dataId="+id,//后台分页查询地址url
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": archive_doc.oTableAudithis,//table显示列
			"fnServerParams": function ( aoData ) {
				archive_doc.oTableFnServerParamsAudithis(aoData,id);
			},
			aaSorting:[],//设置表格默认排序列
			//默认不排序列
	        "aoColumnDefs": [{"bSortable": false, "aTargets": []}]
		});
	} else {
		//table不为null时重绘表格
		archive_doc.oTable.fnDraw();
	}
};

// 生成简单显示单元
archive_doc.createSimpleCell = function(content) {
	if(content===undefined){
		content="";
	}
	return "<td>" +content + "</td>";
};

//生成简单显示单元
archive_doc.createDelect = function(id) {
	return "<td>"+oTableDeleteButtons(id)+"</td>";
};

archive_doc.createNewDelect = function(id) {
	return "<td></td>";
};
//生成简单显示单元
archive_doc.createDelectDoc = function(id,extStr5) {
	return "<td>"+oTableSetButtons(id,extStr5)+"</td>";
};

// 生成CheckBox单元
archive_doc.createCheckCell = function(bean) {
//	var id = null;
//	if (bean.dmName != undefined) {
//		id = "#doc_" + bean.id;
//	} else if (bean.folderName != undefined) {
//		id = "#dir_" + bean.id;
//	}
//	return "<td><input type=\"checkbox\" name=\"ids\" value=\"" + id
//			+ "\"></td>";
	//暂时去除Checkbox
	return "";
};

// 生成目录名/文件名单元
archive_doc.createNameCell = function(bean) {
	var name = null;
	var id = null;
	var cell = null;
	var rowType = 1;

	if (bean.folderName != undefined) {
		name = bean.folderName;
		id = "#dir_" + bean.id;
		// 文件夹
		rowType = 2;
	}
	if (bean.dmName != undefined) {
		name = bean.dmName;
		id = "#doc_" + bean.id;
		rowType = 1;
	}

	var label = "<label class=\"btn document-con\"";
	label += " onclick=\"archive_doc.loadData(\'";
	label += bean.id;
	label += "','";
	label += rowType;
	label += "','";
	label += bean.contentType;
	label += "','";
	var p = label.physicalPath;
	if (p && p.length > 0) {
		p = base64.encode(bean.physicalPath);
	}
	label += p;
	label += "');\">";
	label += '<img width=\"39\" height=\"44\" src=\"../../images/demoimg/document-file.png\"></label>';

	if (bean.dmName != undefined) {
		if (bean.contentType == 1) {
			label = '<label class=\"btn document-con\"><img width=\"39\" height=\"44\" src=\"../../images/demoimg/document-word.png\"></label>';
		} else if (bean.contentType == 2) {
			label = '<label class=\"btn document-con\"><img width=\"39\" height=\"44\" src=\"../../images/demoimg/document-ex.png\"></label>';
		} else if (bean.contentType == 3) {
			label = '<label class=\"btn document-con\"><img width=\"39\" height=\"44\" src=\"../../images/demoimg/document-ppt.png\"></label>';
		} else {
			label = '<label class=\"btn document-con\"><img width=\"39\" height=\"44\" src=\"../../images/demoimg/document-text.png\"></label>';
		}
	}

	var nameSpan = "<span class=\"file_name\">";
	nameSpan += "<a href=\"javascript:archive_doc.showDocContent('";
	nameSpan += bean.dmName;
	nameSpan += "','";
	var p = bean.physicalPath;
	// if(p && p.length > 0) {
	// p = base64.encode(bean.physicalPath);
	// }
	nameSpan += p;
	nameSpan += "');\">";
	nameSpan += name;
	nameSpan += "</a></span>";

	var nameInputSpan = "<span class=\"file_name_input hide\"><input style=\"position:relative;top:5px\" type=\"text\" name=\"folder_name\" value=\"";
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
	cell += "</td>";

	return cell;
};

// 创建文件夹名称单元
archive_doc.createDirNameCell = function(bean) {
	var name = bean.folderName;
	var id = "#dir_" + bean.id;
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

	var nameInputSpan = "<span class=\"file_name_input hide\"><input style=\"position:relative;top:5px\" type=\"text\" onkeydown=\"if(event.keyCode==13){archive_doc.save('"+id+"');}\" name=\"folder_name\" value=\"";
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

	var clickFunc= " onclick=\"archive_doc.filing(\'";
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
	nameSpan += "<a href=\"javascript:archive_doc.filing('";
	nameSpan += bean.id;
	nameSpan += "');\">";
	nameSpan += name;
	nameSpan += "</a></span>";

	var nameInputSpan = "<span class=\"file_name_input hide\"><input style=\"position:relative;top:5px\" type=\"text\" name=\"folder_name\" value=\"";
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
	dirRow += ("<tr id=\"dir_" + o.id + "\">");
	dirRow += (archive_doc.createCheckCell(o));
	dirRow += (archive_doc.createDirNameCell(o));
	// dirRow += (archive_doc.createDirOpCell(o));
	// dirRow += (archive_doc.createSimpleCell(""));
	// dirRow += (archive_doc.createSimpleCell("文件夹"));
	// dirRow += (archive_doc.createSimpleCell(""));
	dirRow += (archive_doc.createSimpleCell(o.createDate));
	dirRow += (archive_doc.createSimpleCell(o.owner));
	dirRow += (archive_doc.createNewDelect());
	dirRow += "</tr>";

	var firstRow = $('#doc_table > tbody .dir:first-child');
	if (firstRow.length > 0) {
		$(dirRow).insertBefore(firstRow);
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
};
// 插入文件行
archive_doc.insertDocRow = function(o, editable) {
	docRow = "";
	docRow += ("<tr id=\"doc_" + o.id + "\" class=\"doc\">");
	// docRow += (archive_doc.createCheckCell(o));
	docRow += (archive_doc.createDocNameCell(o));
	// docRow += (archive_doc.createDocOpCell(o));
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
//		url : getRootPath() + "/archive/folder/getFileReturn.action",
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
//					msgBox.info({
//						content : data.errorMessage
//					});
//				}
//			}
//		}
//	});
};
// 显示上传组件
archive_doc.showUpload = function() {
	$('#fileUpload-edit').modal("show");
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
			model:0,
			id : $('#folderId').val()
		},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				msgBox.info({
					type : "success",
					content : data.successMessage
				});
				$.each(data.documents, function(i, d) {
					archive_doc.insertDocRow(d, false);
				});
				clearTable();
				archive_doc.loadData(archive_doc.getFolderId());
			} else {
				if (data.errorMessage) {
					msgBox.info({
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
	$.ajax({
		type : "get",
		url : getRootPath() + "/archive/permission/queryPermissionByFolder.action",
		data : {
			"folderId" : $('#folderId').val()
		},
		cache :false,
		//dataType : "json",
		success : function(data) {
			//if(data) {去掉新建权限验证，完全交给shiro控制权限
			if(false) {
				//tixing
				msgBox.info({
					content : $.i18n.prop("JC_OA_ARCHIVE_030")
				});
			} else {
				//getToken();
				var o = {
						folderName : '',
						id : 0,
						modifyDate : '',
						createUser : ''
				};
				archive_doc.insertDirRow(o, true);
			}
		}
	});
	
	
	
};

// 重命名文件/文件夹名称
archive_doc.rename = function(rowId,id) {
	rowId = "#dir_"+rowId;
	if(id && !archive_doc.checkLock(id)) {
		return;
	}
	//取消其它行的编辑状态
	var editing=$('tr >td > .file_name:hidden');
	if(editing!="undefined" && editing.length>0){
		$.each(editing,function(i,ele){
			archive_doc.cancel('#'+$(ele).closest('tr').attr('id'));
		});
	}

	$(rowId).children('td').children('.file_name').hide();
	$(rowId).children('td').children('.file_name_input').show();
	$(rowId).children('td').children('.file_name_input').children('input').select();
};

archive_doc.send = function(id) {
				msgBox.confirm({
							content : "是否确认归档!",
							success : function() {
					$.ajax({
											type : "GET",
											url : getRootPath()+ "/archive/document/sendCollection.action",
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
													archive_doc.init();
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
											msgBox.info({
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
				"folderType" : 4,
				"kmAppFlag" : 0,
				"token" : $('#token').val()
			};
			if (id.substr(5) == '0') {
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
				"token" : $('#token').val()
			};
		
			url += "/update.action";
			data.id = id.substr(5);
			
		} else {
			// error
		}
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
						// $(id).remove();
						// archive_doc.insertDirRow(data.folder);
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
					archive_doc.processEmptyTable();
					archive_doc.reloadData();
				} else {
					if (data.success == "error") {
						msgBox.tip({
							content : data.successMessage
						});
					}
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
			//	getToken();
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
//	archive_doc.reloadData();
};

// 显示授权窗口
archive_doc.auth = function(folerId) {
	$('#folder').val(folerId);
	// archive_doc.oTable = null;
	$('#authority-management').modal('show');
	//分页查询方法
	archive_doc.initList();
};

//显示授权窗口
archive_doc.authTop = function() {
	archive_doc.auth($('#folderId').val());
};

// 修改组织人员权限列表
archive_doc.get = function(id,permissionValue,num){
	if(archive_doc.subState)return;
	archive_doc.subState = true;
	$.ajax({
		type : "post",
		url : getRootPath()+"/archive/permission/batchUpdate.action",
		data : {"id": id, "permissionValue": permissionValue, "num": num,"forderId":$('#folder').val()},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				// 修改权限回调方法
				archive_doc.initList();
//				archive_doc.permission();
				archive_doc.reloadData();
			} else {
				msgBox.info({
					content: data.errorMessage
				});
			}
			archive_doc.subState = false;
		}
	});
};

//显示列信息
archive_doc.oTableAoColumns = [
    {mData:"userName"},// 组织人员名
	{mData:function(source) {
			var upPermission = source.permView==1?"<i class='fa fa-ok text-green'></i>":"<i class='fa fa-remove text-red'></i>";
			return upPermission;
		}
  	},
 	{mData:function(source) {
 			var upPermission = source.permNewUpDown==1?"<i class='fa fa-ok text-green'></i>":"<i class='fa fa-remove text-red'></i>";
			return upPermission;
		}
 	}/*,
 	{mData:function(source) {
			var upPermission =  source.permEdit==1?"<i class='fa fa-ok text-green'></i>":"<i class='fa fa-remove text-red'></i>";
			return "<a herf='#' onclick='archive_doc.get("+source.id+","+source.permEdit+",3);'>"+upPermission+"</a>";
		}
 	},
 	{mData : function(source) {
			var upPermission =  source.permDelete==1?"<i class='fa fa-ok text-green'></i>":"<i class='fa fa-remove text-red'></i>";
			return "<a herf='#' onclick='archive_doc.get("+source.id+","+source.permDelete+",4);'>"+upPermission+"</a>";
		}
 	},
 	{mData:function(source) {
			var upPermission =  source.permCopyPaste==1?"<i class='fa fa-ok text-green'></i>":"<i class='fa fa-remove text-red'></i>";
			return "<a herf='#' onclick='archive_doc.get("+source.id+","+source.permCopyPaste+",5);'>"+upPermission+"</a>";
		}
 	},
 	{mData:function(source) {
			var upPermission =  source.permRename==1?"<i class='fa fa-ok text-green'></i>":"<i class='fa fa-remove text-red'></i>";
			return "<a herf='#' onclick='archive_doc.get("+source.id+","+source.permRename+",6);'>"+upPermission+"</a>";
		}
 	},
 	{mData:function(source) {
			var upPermission =  source.permCollect==1?"<i class='fa fa-ok text-green'></i>":"<i class='fa fa-remove text-red'></i>";
			return "<a herf='#' onclick='archive_doc.get("+source.id+","+source.permCollect+",7);'>"+upPermission+"</a>";
		}
 	},
 	{mData:function(source) {
			var upPermission =  source.permVersion==1?"<i class='fa fa-ok text-green'></i>":"<i class='fa fa-remove text-red'></i>";
			return "<a herf='#' onclick='archive_doc.get("+source.id+","+source.permVersion+",8);'>"+upPermission+"</a>";
		}
 	},
 	{mData:function(source) {
			var upPermission =  source.permHistory==1?"<i class='fa fa-ok text-green'></i>":"<i class='fa fa-remove text-red'></i>";
			return "<a herf='#' onclick='archive_doc.get("+source.id+","+source.permHistory+",9);'>"+upPermission+"</a>";
		}
 	},
 	{mData:function(source) {
			var upPermission =  source.permRelate==1?"<i class='fa fa-ok text-green'></i>":"<i class='fa fa-remove text-red'></i>";
			return "<a herf='#' onclick='archive_doc.get("+source.id+","+source.permRelate+",10);'>"+upPermission+"</a>";
 		}
 	}*/,
	{mData: function(source){
			return "<a class='a-icon i-new' href='javascript:void(0)' onclick='archive_doc.updatePermission("+ source.id+ ")' role='button' data-toggle='modal'><i class='fa fa-edit2' data-toggle='tooltip' data-placement='top' title='' data-container='body' data-original-title='编辑'></i></a>" +
				   "<a class='a-icon i-remove m-l-xs m-r-xs' href='javascript:void(0)' onclick='archive_doc.batchDelete("+ source.id+ ")' role='button' data-toggle='modal'><i class='fa fa-remove' data-toggle='tooltip' data-placement='top' data-container='body' data-original-title='删除'></i></a>";
		}
 	}// 操作
 ];

//组装后台参数
archive_doc.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(archive_doc.oTable, aoData);
	aoData.push({ "name": "folderId", "value": $('#folder').val()});	
};

//分页查询方法
archive_doc.initList = function () {
	if (archive_doc.oTable == null) {
		archive_doc.oTable = $('#permission').dataTable( {
			bDestroy:true,
			"iDisplayLength": archive_doc.pageRows,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/archive/permission/manageList.action",//后台分页查询地址url
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": archive_doc.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				archive_doc.oTableFnServerParams(aoData);
			},
			aaSorting:[],//设置默认排序列
			//默认不排序列
	        "aoColumnDefs": [{"bSortable": false, "aTargets": [0,1,2,3]}]
		});
	} else {
		//table不为null时重绘表格
		archive_doc.oTable.fnDraw();
	}
};

// 删除组织人员权限
archive_doc.batchDelete = function(id) {

	msgBox.confirm({
		content : $.i18n.prop("JC_SYS_034"),
		success : function() {
			$.ajax({
				type : "post",
				url : getRootPath() + "/archive/permission/batchDelete.action",
				data : {
					"id" : id,
					"forderId" : $('#folder').val()
				},
				dataType : "json",
				success : function(data) {
					if (data.success == "true") {
						msgBox.tip({
							type : "success",
							content : data.successMessage
						});
						// 修改权限回调方法
						archive_doc.initList();
						// archive_doc.permission();
						archive_doc.reloadData();
						pagingDataDeleteAllForGoBack("permission",[id]);

					} else {
						msgBox.info({
							content : data.errorMessage
						});
					}
				}
			});
		}
	});
};


// 添加权限
archive_doc.addBtn = function(){
	updateOrInsert = true;
	deptAndPersonControl = JCTree.orgDept({
		isBackgrounder:true,
		callback : personCall
	});
	deptAndPersonControl.clearValue();   
	deptAndPersonControl.show();
};

// 修改组织人员权限
function personCall(data) {
	var id="";
	var text="";
	var type="";
	$.each(data, function(i, val){
		id += val.id + ",";
		text += val.text + ",";
		type += val.type + ",";
	});

	if(updateOrInsert){//新增权限
		if(id==null || id.length<1){
			return;
		}
		$.ajax({
			type : "post",
			url : getRootPath()+"/archive/permission/batchInsert.action",
			data : {"folderId": $('#folder').val(),"id":id,"text":text,"type":type},
			dataType : "json",
			success : function(data) {
				if (data.success == "true") {
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					// 修改权限回调方法
					archive_doc.initList();
//					archive_doc.permission();
					archive_doc.reloadData();
				} else {
					msgBox.info({
						content: data.errorMessage
					});
				}
			}
		});
	}else{
		var permissionId = archive_doc.permissionId;
		if(id==null || id.length<1){
			return;
		}
		$.ajax({
			type : "post",
			url : getRootPath()+"/archive/permission/batchPermission.action",
			data : {"permissionId": permissionId,"id":id,"text":text,"type":type,"forderId":$('#folder').val()},
			dataType : "json",
			success : function(data) {
				if (data.success == "true") {
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					// 修改权限回调方法
					archive_doc.initList();
//					archive_doc.permission();
					archive_doc.reloadData();
				} else {
					msgBox.info({
						content: data.errorMessage
					});
				}
			}
		});
	}
}

// 删除方法
archive_doc.deleteClick = function() {
	var idsArr = [];
	$("[name='ids']:checked").each(function() {
		idsArr.push($(this).val());
	});
	var ids = idsArr.join(",");

	if (ids.length == 0) {
		msgBox.tip({
			content : $.i18n.prop("JC_SYS_061"),
			type : "fail"
		});
		return;
	}

	msgBox.confirm({
		content : $.i18n.prop("JC_SYS_034"),
		success : function() {
			archive_doc.deleteDirDoc(idsArr);
		}
	});
};


archive_doc.rowDelete = function(id) {
	msgBox.confirm({
		content : $.i18n.prop("JC_OA_ARCHIVE_013"),
		success : function() {
			archive_doc.deleteDir(id);
		}
	});
};
archive_doc.deleteDir = function(id) {
	$.ajax({
		type : "GET",
		url : getRootPath() + "/archive/folder/deleteFolderFiling.action",
		data : {'id' : id},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({
					content : data.successMessage,
					type : "success"
				});
				archive_doc.reloadData();
			} else {
				msgBox.tip({
					content : data.errorMessage
				});
			}
		},
		error : function() {
			msgBox.tip({
				content : $.i18n.prop("JC_SYS_002")
			});
		}
	});
};


archive_doc.rowDeleteClick = function(id) {
	msgBox.confirm({
		content : $.i18n.prop("JC_OA_ARCHIVE_013"),
		success : function() {
			archive_doc.deleteDirDoc(id);
		}
	});
};
archive_doc.deleteDirDoc = function(id) {
	$.ajax({
		type : "GET",
		url : getRootPath() + "/archive/document/deleteFiling.action",
		data : {'id' : id},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({
					content : data.successMessage,
					type : "success"
				});
				archive_doc.reloadData();
			} else {
				msgBox.tip({
					content : data.errorMessage
				});
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
	if (contentType == 1 || contentType == 2 || contentType == 3) {
		archive_doc.viewFile(contentType, 1, base64.decode(path));
		// word
		// ppt
		// exl
	} else {
		// 其他
	}
};

// 下载文档
archive_doc.filingDownload = function() {
	$.ajax({
		type : "post",
		url : getRootPath()+"/archive/document/filingDownloadCheck.action?id="+ $('#filingId').val(),
		dataType : "json",
		success : function(data) {
			if (data == 0) {
				window.open(getRootPath() + "/archive/document/filingDownload.action?id="+ $('#filingId').val(), "下载附件");
			} else if(data == 1){
				msgBox.info({
					content: $.i18n.prop("JC_OA_ARCHIVE_023")
				});
			} else if(data == 2){
				msgBox.info({
					content: $.i18n.prop("JC_OA_ARCHIVE_037")
				});
			}
		}
	});
	return;
};

//查看表单
archive_doc.filingForm = function() {


	$.ajax({
		type : "GET",
		url : getRootPath() + "/archive/document/getFormContent.action",
		data : {
			'documentId' :$('#filingId').val()
		},
		dataType : "html",
		success : function(data) {
		 	document.getElementById('formContentBody').innerHTML = data;
			//('#formContentBody').html(data);
			$('#formContent').modal('show');
			$(document).find("[workFlowForm]").each(function(){
				try{
					var item = $(this);
					var itemType = $(this).attr("workFlowForm");
					if("button" == itemType) {
						docFromView.type[itemType].hide(item);
					} else {
						//对于浏览模式,所有组件都为只读状态
						docFromView.type[itemType].read(item);
					}

				}catch(e) {}
			});
			//行政办公
			$("a[name=deleteAll]").remove();
			$("tr[name=deleteAll]").remove();
			$("button[name=deleteAll]").remove();
			$("section[id=formButton] button").remove();
			$("#filing_").remove();
			$("td[id^=caozuo_]").remove();
			$("th[id^=caozuo_]").remove();

			if(isIe8Browser){
				var $tables = $('.input-textarea table');
				$tables.hide();
				setTimeout(function(){
					$tables.show();
				},1000/60);
			}

			var objects = $('Object');
			if(objects) {
				objects.each(function(index,item){
					var owidthStr = $(item).css('width');
					var sign = document.getElementById(item.id);
					//if(sign) {
					//	//暂时无用
					//	sign.SetFieldByName("Flag","3");
					//	sign.SetFieldByName("Scale","30");
					//}
					var owidthStrLength = owidthStr.length;
					//var owidth = owidthStr.substring(0,owidthStrLength-2)
					//owidth = +owidth;
					//owidth = owidth*0.85;
					//$(item).css('width',owidth+"px");
					//$(item).css('margin-left','-70px');
				});
			}
			$('#attachList').find('a').each(function(){
				$(this).attr('href','javascript:void(0)');
			});
			$("#sendSimulationForm").find("div").each(function(){
				var item = $(this);

				if(item.hasClass('signature-box')) {
					item.removeClass('signature-box');
				}
			});
				//document.getElementById('formContentBody').innerHTML=data;
		/*	if (data.success == "true") {
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

			}*/
		},
		error : function(e) {
			msgBox.tip({
				content : $.i18n.prop("JC_OA_ARCHIVE_023")
			});
		}
	});
};
var Doc={
		id:""
};
// 显示文档
archive_doc.showFiling = function(id) {
	Doc['id'] = id;
	window.open(getRootPath() + "/archive/document/viewContent.action", "查看文档");
};


// 显示纪要归档文档
archive_doc.showSummaryFiling = function(id) {
	Doc['id'] = id;
	window.open(getRootPath() + "/archive/document/viewSummaryContent.action", "查看文档");
};
// 页面搜索
archive_doc.onFilter = function(ev) {
	var tbody = $('#doc_table > tbody');
	tbody.children('.empty_row').remove();
	var filterField = jQuery(this);
	var contacts = $('#doc_table');
	var input = filterField.val();

	var names = contacts.find("tr .file_name");

	input = input.toUpperCase();
	var bool =0;
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
archive_doc.bindingFolderValidator = function(id) {
	var reg = new RegExp('^[^\\\\\\/:*\'"?\\"<>|]+$');// 转义 \ 符号也不行
	var $ele = $(id).find('input');

	function valid(value ,target){
		var val = $.trim(value);
		var errorText = '';
		if(!val.length){
			errorText = '此信息不能为空';
		}else if(val.length > 50) {
			errorText = '名称长度不能大于50';
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

archive_doc.newWord = function(flag) {
	archive_doc.documentType = 'doc';
	window.open(getRootPath() + "/archive/folder/newDocument.action?documentType=doc&documentPath="+archive_doc.documentPath);
};
archive_doc.newExl = function() {
	archive_doc.documentType = 'xls';
	window.open(getRootPath() + "/archive/folder/newDocument.action?documentType=xls&documentPath="+archive_doc.documentPath);
};
archive_doc.newPPT = function() {
	archive_doc.documentType = 'ppt';
	window.open(getRootPath() + "/archive/folder/newDocument.action?documentType=ppt&documentPath="+archive_doc.documentPath);
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
 * 文件夹：复制到
 */
archive_doc.copyDirTo=function(folder){
	if(archive_doc.currentSelectedRowId==null ){
		msgBox.info({
			content : $.i18n.prop("JC_SYS_060")
		});
	}
	$.ajax({
		type : "GET",
		url : getRootPath() + "/archive/folder/copyDirTo.action",
		data : {
			id:archive_doc.currentSelectedRowId.substr(5),
			parentFolderId : folder.id
		},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				msgBox.info({
						type : "success",
						content : data.successMessage
				});
				archive_doc.currentSelectedRowId=null;
			} else {
				if (data.errorMessage) {
					msgBox.info({
						content : data.errorMessage
					});
				} else {
					msgBox.info({
						content : $.i18n.prop("JC_SYS_060")
					});
				}
			}
		}
	});
};

/**
 * 文档：复制到
 */
archive_doc.copyDocTo=function(folder){
	if(archive_doc.currentSelectedRowId==null ){
		msgBox.info({
			content : $.i18n.prop("JC_SYS_060")
		});
	}
	$.ajax({
		type : "GET",
		url : getRootPath() + "/archive/folder/copyDocTo.action",
		data : {
			id:archive_doc.currentSelectedRowId.substr(5),
			folderId : folder.id
		},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				msgBox.info({
						type : "success",
						content : data.successMessage
				});
				archive_doc.currentSelectedRowId=null;
			} else {
				if (data.errorMessage) {
					msgBox.info({
						content : data.errorMessage
					});
				} else {
					msgBox.info({
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
	archive_doc.folderSelector.setCallback(archive_doc.cutDirTo);
	archive_doc.folderSelector.show();
};

/**
 * 文件夹：剪切到
 */
archive_doc.cutDirTo=function(id){
	if(archive_doc.currentSelectedRowId==null ){
		msgBox.info({
			content : $.i18n.prop("JC_SYS_060")
		});
	}
	
	$.ajax({
		type : "get",
		url : getRootPath() + "/archive/permission/queryPermissionByFolder.action",
		data : {
			"folderId" : id
		},
		cache :false,
		//dataType : "json",
		success : function(data) {
			if(data) {
				//tixing
				msgBox.info({
					content : $.i18n.prop("JC_OA_ARCHIVE_027")
				});
			} else {
				$.ajax({
					type : "GET",
					url : getRootPath() + "/archive/folder/cutDirTo.action",
					data : {
						id:archive_doc.currentSelectedRowId,
						folderType:4,
						parentFolderId : id
					},
					dataType : "json",
					success : function(data) {
						if (data.success == "true") {
							msgBox.tip({
									type : "success",
									content : data.successMessage,
									callback:archive_doc.reloadData()
							});
							archive_doc.currentSelectedRowId=null;
							$('#folderSelector').modal('hide');
							$(archive_doc.currentSelectedRowId).remove();
						} else {
							if (data.errorMessage) {
								msgBox.info({
									content : data.errorMessage
								});
							} else {
								msgBox.info({
									content : $.i18n.prop("JC_SYS_060")
								});
							}
						}
					}
				});
			}
		}
	});
};

/**
 * 文档：剪切到
 */
archive_doc.cutDocTo=function(folder){
	if(archive_doc.currentSelectedRowId==null ){
		msgBox.info({
			content : $.i18n.prop("JC_SYS_060")
		});
	}
	$.ajax({
		type : "GET",
		url : getRootPath() + "/archive/folder/cutDocTo.action",
		data : {
			id:archive_doc.currentSelectedRowId.substr(5),
			folderId : folder.id
		},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				$(archive_doc.currentSelectedRowId).remove();
				archive_doc.currentSelectedRowId=null;
				msgBox.info({
						type : "success",
						content : data.successMessage
				});
			} else {
				if (data.errorMessage) {
					msgBox.info({
						content : data.errorMessage
					});
				} else {
					msgBox.info({
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
				var lock='未锁定<a href="javascript:archive_doc.lockDoc('+data.id+');" class="a-icon i-new fr"><i class="" data-original-title="锁定"';
					lock+='	data-container="body" title="" data-placement="top"';
					lock+=' data-toggle="tooltip"></i>锁定</a>';
				var unlock='锁定<a href="javascript:archive_doc.unlockDoc('+data.id+');" class="a-icon i-new fr"><i class=""';
					unlock+='	 data-original-title="锁定" data-container="body" title=""';
				    unlock+=' data-placement="top" data-toggle="tooltip"></i>解除锁定</a>';
			
				$('#info_dmName').html(data.dmName);
				$('#info_dmSize').html(data.dmSize);
				$('#info_createDate').html(data.createDate);
				$('#info_dmDir').html(data.dmDir);
				$('#info_dmName').html(data.dmName);
				$('#info_currentVersion').html(data.currentVersion);
				$('#info_modifyDate').html(data.modifyDate);
				$('#info_seq').html(data.seq);
				$('#info_createUser').html(data.createUser);
				$('#info_dmLockStatus').html(data.dmLockStatus == 1 ? unlock :lock);
				$('#info_keyWords').html(data.keyWords);
				$('#info_dmType').html(data.dmType);
				$('#document-information').modal('show');
			} else {
				if (data.errorMessage) {
					msgBox.info({
						content : data.errorMessage
					});
				} else {
					msgBox.info({
						content : $.i18n.prop("JC_SYS_060")
					});
				}
			}
		}
	});
};

/**
 * 文档锁定
 */
archive_doc.lockDoc=function(id){
	$.ajax({
		type : "GET",
		url : getRootPath() + "/archive/document/lock.action",
		data : {
			id:id,
			dmLockStatus:1
		},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				var unlock='锁定<a href="javascript:archive_doc.unlockDoc('+id+');" class="a-icon i-new fr"><i class=""';
				unlock+='	 data-original-title="锁定" data-container="body" title=""';
			    unlock+=' data-placement="top" data-toggle="tooltip"></i>解除锁定</a>';

				$('#info_dmLockStatus').html(unlock);
				$('#doc_'+id).children("td").eq(2).html("锁定");
				msgBox.info({
						type : "success",
						content : data.successMessage
				});
			} else {
				if (data.errorMessage) {
					msgBox.info({
						content : data.errorMessage
					});
				} else {
					msgBox.info({
						content : $.i18n.prop("JC_SYS_060")
					});
				}
			}
		}
	});
};

/**
 * 文档解锁
 */
archive_doc.unlockDoc=function(id){
	$.ajax({
		type : "GET",
		url : getRootPath() + "/archive/document/lock.action",
		data : {
			id:id,
			dmLockStatus:0
		},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				var lock='未锁定<a href="javascript:archive_doc.lockDoc('+id+');" class="a-icon i-new fr"><i class="" data-original-title="锁定"';
				lock+='	data-container="body" title="" data-placement="top"';
				lock+=' data-toggle="tooltip"></i>锁定</a>';
				$('#info_dmLockStatus').html(lock);
				$('#doc_'+id).children("td").eq(2).html("未锁定");
				msgBox.info({
						type : "success",
						content : data.successMessage
				});
			} else {
				if (data.errorMessage) {
					msgBox.info({
						content : data.errorMessage
					});
				} else {
					msgBox.info({
						content : $.i18n.prop("JC_SYS_060")
					});
				}
			}
		}
	});
};

//关联关系 start
//关联文档页对象
archive_doc.relateTable = null;
archive_doc.relateFnServerParams = function(aoData){
	getTableParameters(archive_doc.relateTable, aoData);
	aoData.push({ "name": "documentId", "value": $('#pubDocRelateForm #id').val()});	
};
archive_doc.check = function(dmId){
	$.ajax({
		type : "GET",
		url : getRootPath() + "/archive/document/check.action",
		data : {
			id:dmId
		},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				archive_doc.showDocContent(dmId);
			} else {
				if (data.errorMessage) {
					msgBox.info({
						content : data.errorMessage
					});
				} else {
					msgBox.info({
						content : $.i18n.prop("JC_SYS_060")
					});
				}
			}
		}
	});
};
//显示列信息
archive_doc.relateColumns = [
	{ "mData":function(source) {
		return "<a type=\"button\" href=\"javascript:archive_doc.check('"+source.dmId+"')\" role=\"button\" >"+source.dmName+"."+source.dmSuffix+"</a>";
		}
	},
	{ "mData": "dmDir" },
	{ "mData": null, "mRender" : function(mData, type, full) {
		return "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"archive_doc.deleteRelate("+ full.id+ ")\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
	}}
 ];
archive_doc.relateList = function () {
	if (archive_doc.relateTable == null) {
		archive_doc.relateTable = $('#pubDocRelateTable').dataTable( {
			"iDisplayLength": 5,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/archive/relate/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": archive_doc.relateColumns,//table显示列
			"fnServerParams": function (aoData) {//传参
				archive_doc.relateFnServerParams(aoData);
			},
			aaSorting:[],//设置表格默认排序列
			//默认不排序列
	        "aoColumnDefs": [{"bSortable": false, "aTargets": [0,1,2]}]
		} );
	} else {
		archive_doc.relateTable.fnDraw();
	}
};
//显示信息
archive_doc.showRelateDiv = function(id){
	$('#pubDoc-relate').modal('show');
	$('#pubDocRelateForm #id').val(id);
	archive_doc.relateList();
};

//选择关联文档页对象
archive_doc.relateForDocTable = null;
archive_doc.relateForDocFnServerParams = function(aoData){
	getTableParameters(archive_doc.relateForDocTable, aoData);
	$.each($("#queryRelateForDocForm").serializeArray(), function(i, o) {
		if(o.value != ""){
			aoData.push({ "name": o.name, "value": o.value});
		}
	});
	aoData.push({ "name": "model", "value": 0});//公共文档
	aoData.push({ "name": "id", "value": $('#pubDocRelateForm #id').val()});
};

//显示列信息
archive_doc.relateForDocColumns = [
    {mData: function(source) {
   		return "<input type=\"checkbox\" name=\"documentIds\" value="+ source.id + ">";
 	}},
	{ "mData": "dmName" },
	{ "mData": "seq" },
	{ "mData": "owner" }
 ];
archive_doc.relateForDocList = function () {
	if (archive_doc.relateForDocTable == null) {
		archive_doc.relateForDocTable = $('#relateForDocTable').dataTable( {
			"iDisplayLength": 5,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/archive/document/manageByRelateList.action",//不包含已经关联的文档
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": archive_doc.relateForDocColumns,//table显示列
			"fnServerParams": function (aoData) {//传参
				archive_doc.relateForDocFnServerParams(aoData);
			},
			aaSorting:[],//设置表格默认排序列
			//默认不排序列
	        "aoColumnDefs": [{"bSortable": false, "aTargets": [0,1,2,3]}]
		} );
	} else {
		archive_doc.relateForDocTable.fnDraw();
	}
};

archive_doc.resetRelateForDoc = function(){
	$('#queryRelateForDocForm')[0].reset();
	selectControl.clearValue("createUser-createUser");	
};

//显示信息
archive_doc.showRelateForDocDiv = function(id){
	$('#relate-doc').modal('show');
	archive_doc.resetRelateForDoc();
	archive_doc.relateForDocList();
};

//确认选择的关联文档
archive_doc.saveRelateForDoc = function () {
	var idsArr = [];
	var id = $('#pubDocRelateForm #id').val();
	$("[name='documentIds']:checked").each(function() {
		idsArr.push($(this).val());
	});
	ids = idsArr.join(",");
	if (ids.length == 0) {
		msgBox.info({
			content: $.i18n.prop("JC_SYS_029","要关联的文档")
		});
		return;
	}
	$.ajax({
		type : "POST",
		url : getRootPath()+"/archive/relate/saveByDocumentIds.action",
		data : {"documentIds": ids,"documentId": id},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({
					type:"success",
					content: data.successMessage
				});
				$('#relate-doc').modal('hide');
				archive_doc.relateList();
			} else {
				msgBox.info({
					content: data.errorMessage
				});
			}
		}
	});
};
//删除选择的关联文档
archive_doc.deleteRelate = function (id) {
	msgBox.confirm({
		content: $.i18n.prop("JC_SYS_034"),
		success: function(){
			$.ajax({
				type : "POST",
				url : getRootPath()+"/archive/relate/deleteByIds.action",
				data : {"ids": id},
				dataType : "json",
				success : function(data) {
					if (data.success == "true") {
						msgBox.tip({
							type:"success",
							content: data.successMessage
						});
						archive_doc.relateList();
					} else {
						msgBox.info({
							content: data.errorMessage
						});
					}
				}
			});
		}
	});
};
//关联关系 end


//版本管理 Start---------------------------------------------------------
archive_doc.version=function(id){
	$('#version-management').modal('show');
	archive_doc.initVersionList(id);
};

archive_doc.versionTable=null;
//显示列信息
archive_doc.versionTableAoColumns = [
    {mData:"currentVersion"},// 版本号
 	{mData: "versionDesc"},// 版本说明
	{mData:"modifyUser"},// 更新人
	{mData: "modifyDate"}//操作时间
 ];


//分页查询方法
archive_doc.initVersionList = function (id) {
	
	//组装后台参数
	archive_doc.versionTableFnServerParams = function(aoData){
		//排序条件
		getTableParameters(archive_doc.versionTable, aoData);
		aoData.push({ "name": "backUpId", "value": id});
	};
	
	if (archive_doc.versionTable == null) {
		archive_doc.versionTable = $('#versionTable').dataTable( {
			"iDisplayLength": archive_doc.pageRows,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/archive/document/manageVersionList.action",//后台分页查询地址url
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": archive_doc.versionTableAoColumns,//table显示列
			"fnServerParams": function ( aoData ) {
				archive_doc.versionTableFnServerParams(aoData);
			},
			aaSorting:[],//设置表格默认排序列
			//默认不排序列
	        "aoColumnDefs": [{"bSortable": false, "aTargets": []}]
		});
	} else {
		//table不为null时重绘表格
		archive_doc.versionTable.fnDraw();
	}
};

//版本管理 end---------------------------------------------------------------
// 附件管理
archive_doc.filing = function(id){
//	$('#filing').modal('show');
	archive_doc.audithisFilingList(id);
};

archive_doc.audithisFilingList = function (id) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/archive/document/filingList.action?id="+id,
		data : {"ids": id},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				$('#filing').modal('show');
				var nameHtml = '<span title="'+data.doc.dmName+'">'+data.doc.dmName+'</span>';
				$('#filing_dmName').html(nameHtml);
				$('#filingId').val(data.doc.id);
				$('#filing_createUser').html(data.doc.owner);
				$('#filing_createDate').html(data.doc.createDate);
				$('#filing_dmDir').html(data.doc.dmDir);
				// $('#filing_seq').html(data.doc.seq);
				// $('#filing_modifyDate').html(data.doc.modifyDate);
				archive_doc.renderFiling(data.listFiling);
			} else {
				msgBox.info({
					content: data.errorMessage
				});
			}
		}
	});
};

//界面渲染
archive_doc.renderFiling = function(folder) {
	var tbody = $('#permissionFiling > tbody');
	var dirRow = "";
	var self = this;
	tbody.empty();
	$.each(folder, function(i, o) {
		dirRow += ("<tr>");
		dirRow += (self.createFilingNameCell(o));
		dirRow += (self.createFilingCell(o.fizeSize));
		// dirRow += (self.createFilingCell(o.filePath));
		// dirRow += (self.createFilingCell(o.fileName));
		dirRow += "</tr>";
	});
	tbody.append(dirRow);
	//如果没有记录，显示空白行
	// archive_doc.processEmptyTable();
	if(tbody.children().length==0){
		tbody.append(archive_doc.createFiling());
		return ;
	}
	//IE8兼容处理
	ie8StylePatch();
};

//生成空白行
archive_doc.createFiling=function(){
	var row='<tr class="empty_row"><td valign="top" colspan="8" class="dataTables_empty">当前目录下无附件</td></tr>';
	return row;
};

archive_doc.createFilingCell = function(bean) {
	return "<td>"+bean+"</td>";
};

archive_doc.createFilingNameCell = function(bean) {
	var fileName = bean.fileName;
	if(fileName != null && fileName != ""){
		var postfixName = fileName.split(".")[1];
	}
	if(bean.fileType==1){
		return "<td><a href=\"javascript:archive_doc.showFiling("+bean.id+");\" >"+bean.fileName+"</a></td>";
	} else if(bean.fileType==2){
		if(postfixName == "doc"){
			return "<td><a href=\"javascript:archive_doc.showSummaryFiling("+bean.id+");\" >"+bean.fileName+"</a></td>";
		}else {
			return "<td>"+bean.fileName+"</td>";
		}

	}
};

// 附件信息end

//修改组织人员权限
archive_doc.updatePermission = function(id) {
	updateOrInsert = false;
	archive_doc.permissionId = id;
	$.ajax({
		type : "post",
		url : getRootPath()+"/archive/permission/updatePermission.action",
		data : {"id": id},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				var subPermission = [];
				var aa="[";
				for(var i=0;i<data.subPermission.length;i++){
					subPermission.push({
						id : data.subPermission[i].controlId,
						text : data.subPermission[i].controlName,
						type : data.subPermission[i].dataType
					});
					//aa +="{id:"+data.subPermission[i].controlId+",text:'"+data.subPermission[i].controlName+"',type:"+data.subPermission[i].dataType+"},";
				}
				//aa=aa.substring(0, aa.length-1)+"]";
				//var subPermission = aa;
				if(!deptAndPersonControl){//当没有点击添加权限，直接点击编辑时，要重新初始化树
					deptAndPersonControl = JCTree.orgDept({
						isBackgrounder:true,
						callback : personCall
					});
				}
				deptAndPersonControl.clearValue();    //清空人员部门树
				if(subPermission.length) {
					deptAndPersonControl.setData(subPermission,function(){
						deptAndPersonControl.show();
					});//填充已有数据
				} else {
					deptAndPersonControl.show();
				}

			} else {
				msgBox.tip({
					content: data.errorMessage
				});
			}
		}
	});
};

//弹出打印页面
archive_doc.showDocSendPrint = function() {
	//alert($("#formContent").html());
	window.open(getRootPath()+'/archive/document/manageDocPrint.action?time='+new Date());
};

// 初始化
jQuery(function($) {
	//计算分页显示条数
	archive_doc.pageRows = TabNub > 0 ? TabNub : 10;
	// $('#dSection').css('height',$(window).height() - 270);
	//关联关系使用 start
	$('#chooseRelate').click(archive_doc.showRelateForDocDiv);
	//添加按钮绑定事件
	$("#addBtn").click(archive_doc.addBtn);
	selectControl = JCTree.init({
		container   : 'queryCreateUserTree',
		controlId   : 'createUser-createUser',
		isCheckOrRadio: false,
		isPersonOrOrg: true
	});
	$("#queryRelateForDoc").click(archive_doc.relateForDocList);
	$("#resetRelateForDoc").click(archive_doc.resetRelateForDoc);
	$("#saveRelate").click(archive_doc.saveRelateForDoc);
	//关联关系使用 end
	$('#btnNewFolder').click(archive_doc.insertNewRow);
	$('#btnAuth').click(archive_doc.authTop);
	$('#btnReturn').click(archive_doc.goParent);
	$('#btnReturnDisabled').click(archive_doc.goParent);
	$('#btnUpload').click(archive_doc.showUpload);
	//表单打印
	$('#print_').click(archive_doc.showDocSendPrint);
	$('#btnFinishUpload').click(archive_doc.finishUpload);
	$('#btnCloseUpload').click(archive_doc.deleteAttach);
	$('#btnDelete').click(archive_doc.deleteClick);
	$('#newPPT').click(archive_doc.newPPT);
	$('#newWord').click(archive_doc.newWord);
	$('#newExl').click(archive_doc.newExl);
	//全部下载定事件
	$("#filingDownLoad").click(archive_doc.filingDownload);
	// 查看表单
	$("#filingForm").click(archive_doc.filingForm);
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
	archive_doc.folderSelector=new FolderSelectTree($('#folderSelector'),null,4);
	$(".document-new").mouseleave(function() {
		$(this).hide();
	});

	archive_doc.pathBar=new FolderPathBar($('#folderTable'),archive_doc.loadData);
	archive_doc.init();
});

