/**
 * js
 */
var myCollection = {};

myCollection.pageRows = null;//分页变量
myCollection.subState = false;//重复提交标识
myCollection.oTable = null;//分页对象

//界面渲染
myCollection.render=function(folder){
	var tbody=$('#myCollectionList > tbody');
	var dirRow="";
	var self=this;
	if(folder.length==0){
		tbody.append(myCollection.createEmptyRow());
		return;
	}
	$.each(folder, function(i,o){
		dirRow+=("<tr id=\"dir_"+o.id+"\" class=\"dir\">");
		dirRow+=(self.check(o));
		dirRow+=(self.createNameCell(o));
		// dirRow+=(self.createContentType(o));
		dirRow+=(self.createType(o));
		dirRow+=(self.createDmSize(o));
		dirRow+=(self.createcurrentVersion(o));
		dirRow+=(self.createModifyDate(o));
		dirRow+=(self.createOperate(o));
		dirRow+="</tr>";
	});
	tbody.append(dirRow);
	//IE8隔行变色
	ie8StylePatch();
	ListTable();
};

//格式
myCollection.check=function(bean){
	var cell=null;
	var label = "<input type=\'checkbox\' name=\'ids\' value='"+bean.id+"'>";
	cell="<td>"+label+"</td>";
		  
	return cell;
};

// 格式
myCollection.createNameCell=function(bean){
	var cell=null;
	var label = '';
	var clickFunc = " onclick=\"myCollection.loadData(\'";
	clickFunc += bean.id;
	clickFunc += "');\"";
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

	cell="<td>"+label+"&nbsp;&nbsp;<span class=\"file_name\"><a type=\"button\" href=\"javascript:myCollection.loadData("+bean.id+")\" role=\"button\" >"+bean.dmName+"</a></span></td>";
		  
	return cell;
};
//生成空白行
myCollection.createEmptyRow=function(){
	var row='<tr class="empty_row"><td valign="top" colspan="8" class="dataTables_empty">没有匹配的记录</td></tr>';
	return row;
};

//生成目录名/文件名单元
myCollection.createType=function(bean){
	return "<td>"+bean.dmSuffix+"</td>";
};

//生成目录名/文件名单元
myCollection.createContentType=function(bean){
	var cell=null;
	if(bean.contentType==0){
		cell="<td>未知</td>";
	} else if(bean.contentType==1){
		cell="<td>word</td>";
	} else if(bean.contentType==2){
		cell="<td>excel</td>";
	} else if(bean.contentType==3){
		cell="<td>ppt</td>";
	}
		  
	return cell;
};

//大小
myCollection.createDmSize=function(bean){
	var cell="<td>"+bean.dmSize+"</td>";
	return cell;
};

//版本号
myCollection.createcurrentVersion=function(bean){
	var cell="<td>"+bean.currentVersion+"</td>";
	return cell;
};

//时间
myCollection.createModifyDate=function(bean){
	var cell="<td>"+bean.createDate+"</td>";
	return cell;
};

// 创建人
myCollection.createOperate=function(bean){
	return "<td>"+bean.collectName+"</td>";
};
//分页查询方法
myCollection.initList = function () {
	$.ajax({
		type : "post",
		url : getRootPath() + "/archive/document/intoMyCollectionFolder.action",//后台分页查询地址url
		dataType : "json",
		success : function(data) {
			if(data.success == "true"){
				$('#myCollectionList > tbody').empty();
				myCollection.render(data.list);
				$("#checkbox").attr("checked",false);
			} else {
				if(data.errorMessage){
					msgBox.info({
						content: data.errorMessage
					});
				}
			}
		}
	});
};

//清空表单数据
myCollection.queryFormReset = function(){
	//清空表单
	$('#myCollectionListForm')[0].reset();
};

// 删除
myCollection.batchDelete = function () {
	var idsArr = [];
	$('tr:visible').children().children("[name='ids']:checked").each(function() {
		idsArr.push($(this).val());
	});
	var ids = idsArr.join(",");
	if (ids.length == 0) {
		msgBox.info({
			content: "请选择要删除的记录"
		});
		return;
	}
	msgBox.confirm({
		content: $.i18n.prop("JC_OA_ARCHIVE_013"),
		success: function(){
			myCollection.deleteGroupCallBack(ids);
		}
	});
};


myCollection.deleteGroupCallBack = function(ids) {
	$.ajax({
		type : "GET",
		url : getRootPath()+"/archive/document/deleteCollection.action",
		data : {"ids": ids},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({
					type:"success",
					content: data.successMessage
				});
				$('#search').val("");
				$('#search').blur();
				myCollection.initList();
			} else {
				msgBox.info({
					content: data.errorMessage
				});
			}
		}
	});
};

myCollection.loadData = function (id) {
	$('#search').attr('value','');
	$('#search').blur();
	$.ajax({
		type : "GET",
		url : getRootPath()+"/archive/folder/checkDocFileExist.action",
		data : {"id": id},
		dataType : "json",
		success : function(data) {
			if (data == true) {
				window.open(getRootPath()+"/archive/folder/showDocContent.action?id="+id,"查看文档");
			} else {
				msgBox.tip({
					content: $.i18n.prop("JC_OA_ARCHIVE_023")
				});
			}
		}
	});
};

//页面搜索
myCollection.onFilter = function() {
	var tbody = $('#myCollectionList > tbody');
	tbody.children('.empty_row').remove();
	var filterField = jQuery(this);
	var contacts = $('#myCollectionList');
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
		tbody.append(myCollection.createEmptyRow());
	}
};

/**
 * 初始化方法
 */
jQuery(function($){
	//计算分页显示条数
	// myCollection.pageRows = TabNub > 0 ? TabNub : 10;
	//查询按钮绑定事件
	// $("#myCollectionFolderBtn").click(myCollection.initList);
	//重置按钮绑定事件
	// $("#myCollectionFolderResetBtn").click(myCollection.queryFormReset);
	$('#search').on("keyup", myCollection.onFilter);
	$('#search').on("input", myCollection.onFilter);
	//批量删除
	$("#deleteMyCollectionFolder").click(myCollection.batchDelete);
	//分页查询方法
	myCollection.initList();
});

//@ sourceURL=myCollectionFolder.js