/**
 * js
 */
var recycle = {};

recycle.pageRows = null;//分页变量
recycle.subState = false;//重复提交标识
recycle.oTable = null;//分页对象

//界面渲染
recycle.render=function(folder){
	var tbody=$('#recycleList > tbody');
	var dirRow="";
	var self=this;
	if(folder.length==0){
		tbody.append(recycle.createEmptyRow());
		return;
	}
	$('#clearBtn').attr("disabled", false);
	$.each(folder, function(i,o){
		dirRow+=("<tr id=\"dir_"+o.id+"\" class=\"dir\">");
		dirRow+=(self.createNameCell(o));
		// dirRow+=(self.createContentType(o));
		dirRow+=(self.createType(o));
		dirRow+=(self.createDmSize(o));
		dirRow+=(self.createModifyDate(o));
		dirRow+=(self.createOperate(o));
		dirRow+="</tr>";
	});
	tbody.append(dirRow);
	$("i[data-toggle='tooltip']").tooltip();
	//IE8隔行变色
	ie8StylePatch();
	ListTable();
};

// 格式
recycle.createNameCell=function(bean){
	var cell=null;
	var label = '';
	var clickFunc  = '';
	if(bean.type==0){
		label='<label class=\"btn document-con disable\"><img width=\"39\" height=\"44\" src=\"../../images/demoimg/document-file.png\"></label>';
	} else {
		if(bean.contentType==1){
			label = '<label class=\"btn document-con disable\"><img width=\"39\" height=\"44\" src=\"../../images/demoimg/document-word.png\"></label>';
		} else if(bean.contentType==2){
			label = '<label class=\"btn document-con disable\"><img width=\"39\" height=\"44\" src=\"../../images/demoimg/document-ex.png\"></label>';
		} else if(bean.contentType==3){
			label = '<label class=\"btn document-con disable\"><img width=\"39\" height=\"44\" src=\"../../images/demoimg/document-ppt.png\"></label>';
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
	}

	cell="<td>"+label+"&nbsp;&nbsp;<span class=\"file_name\">"+bean.dmName+"</span></td>";
		  
	return cell;
};
//生成空白行
recycle.createEmptyRow=function(){
	$('#clearBtn').attr("disabled", true);
	var row='<tr class="empty_row"><td valign="top" colspan="8" class="dataTables_empty">没有匹配的记录</td></tr>';
	return row;
};
//生成目录名/文件名单元
recycle.createContentType=function(bean){
	var cell=null;
	if(bean.type==0){
		cell="<td>文件夹</td>";
	} else {
		if(bean.contentType==0){
			cell="<td>未知</td>";
		} else if(bean.contentType==1){
			cell="<td>word</td>";
		} else if(bean.contentType==2){
			cell="<td>excel</td>";
		} else if(bean.contentType==3){
			cell="<td>ppt</td>";
		}
	}
		  
	return cell;
};

//大小
recycle.createDmSize=function(bean){
	var cell=null;
	if(bean.type==0){
		cell="<td></td>";
	} else {
		cell="<td>"+bean.dmSize+"</td>";
	}
	return cell;
};

//时间
recycle.createModifyDate=function(bean){
	var cell="<td>"+bean.modifyDate+"</td>";
	return cell;
};

//时间
recycle.createType=function(bean){
	var cell="<td>"+bean.dmSuffix+"</td>";
	return cell;
};

//操作
recycle.createOperate=function(bean){
	return oTableSetButtons(bean);
};

// 删除
recycle.batchDelete = function (id, type) {
	msgBox.confirm({
		content: $.i18n.prop("JC_OA_ARCHIVE_013"),
		success: function(){
			
					$.ajax({
						type : "post",
						url : getRootPath() + "/archive/folder/batchRecycleDelete.action",
						data : {"id": id, "type":type},
						dataType : "json",
						success : function(data) {
							if(data.success == "true"){
								msgBox.tip({
									type:"success",
									content: data.successMessage
								});
								// 回调方法
								recycle.initList();
							} else {
								if(data.errorMessage){
									msgBox.info({
										content: data.errorMessage
									});
								}
							}
						}
					});
		}});
	
	
	
};

//还原
recycle.updateRecycle = function (id, type) {
	
	msgBox.confirm({
		content: $.i18n.prop("JC_OA_ARCHIVE_014"),
		success: function(){
			
				$.ajax({
					type : "post",
					url : getRootPath() + "/archive/folder/batchRecycle.action",
					data : {"id": id, "type":type},
					dataType : "json",
					success : function(data) {
						if(data.success == "true"){
							msgBox.tip({
								type:"success",
								content: data.successMessage
							});
							// 回调方法
							recycle.initList();
						} else {
							if(data.errorMessage){
								msgBox.info({
									content: data.errorMessage
								});
							}
						}
					}
				});
			
		}});
};

//分页查询方法
recycle.initList = function () {
	$('#search').val('');
	$('#search').blur();
	$.ajax({
		type : "post",
		url : getRootPath() + "/archive/folder/selectRecycle.action",//后台分页查询地址url
		dataType : "json",
		success : function(data) {
			if(data.success == "true"){
				$('#clearBtn').attr("disabled", false);
				$('#recycleList > tbody').empty();
				recycle.render(data.lecycleRecycle);
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

// 清空回收站
recycle.clearBtn = function () {
	msgBox.confirm({
		content: $.i18n.prop("JC_OA_ARCHIVE_028"),
		success: function(){
			$.ajax({
				type : "post",
				url : getRootPath() + "/archive/folder/clearRecycl.action",//后台分页查询地址url
				dataType : "json",
				success : function(data) {
					if(data.success == "true"){
						msgBox.tip({
							type:"success",
							content: data.successMessage
						});
						recycle.initList();
					} else {
						if(data.errorMessage){
							msgBox.info({
								content: data.errorMessage
							});
						}
					}
				}
			});
			
		}});
};

//页面搜索
recycle.onFilter = function(ev) {
	var tbody = $('#recycleList > tbody');
	tbody.children('.empty_row').remove();
	var filterField = jQuery(this);
	var contacts = $('#recycleList');
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
		tbody.append(recycle.createEmptyRow());
	} else {
		//$('#clearBtn').removeAttr("disabled");
		$('#clearBtn').attr("disabled", false);
	}
};

/**
 * 初始化方法
 */
jQuery(function($){

	$('#search').on("input", recycle.onFilter);
	$('#search').on("keyup", recycle.onFilter);
	$('#clearBtn').click(recycle.clearBtn);
	//分页查询方法
	recycle.initList();
});