var liuAttachPool = {};
/*
 * 使用说明
 * 页面中加入
 *
 */
//初始化页面
liuAttachPool.initPageAttach = function(index,id,tableName){
	$('#file-edit_a_'+index).show();
	$('#file-edit_div_'+index).show();
	$('#file-edit-mode_'+index).val("EDIT");
	var tableNameStr = liuAttachPool.isNull(tableName)?"TABLE_SYS_"+index:tableName;
	if($("#UploadAttachDiv001L").length > 0){
	} else {
		$(document.body).append("<div id=\"UploadAttachDiv001L\"></div>");
	}
	if (index != 1) {
		$("#UploadAttachDiv001L").html($("#UploadAttachDiv001L").html() + liuAttachPool.addHideAttachDiv(index));
	} else {
		$("#UploadAttachDiv001L").html(liuAttachPool.addHideAttachDiv(index));
	}
	liuAttachPool.initUpload(index,id,tableNameStr,true);
}

//初始化页面
liuAttachPool.initPageAttachOnView = function(index,id,tableName){
	$('#file-edit_a_'+index).hide();
	$('#file-edit_div_'+index).hide();
	$('#file-edit-mode_'+index).val("VIEW");
	var tableNameStr = liuAttachPool.isNull(tableName)?"TABLE_SYS_"+index:tableName;
	if($("#UploadAttachDiv001L").length > 0){
	} else {
		$(document.body).append("<div id=\"UploadAttachDiv001L\"></div>");
	}
	if (index != 1) {
		$("#UploadAttachDiv001L").html($("#UploadAttachDiv001L").html() + liuAttachPool.addHideAttachDiv(index));
	} else {
		$("#UploadAttachDiv001L").html(liuAttachPool.addHideAttachDiv(index));
	}
	liuAttachPool.initUpload(index,id,tableNameStr,false);
}
//初始化多个单体页面
liuAttachPool.inittMultiplePageAttach = function(index,id,tableName,category){
	$('#file-edit_a_'+index).show();
	$('#file-edit_div_'+index).show();
	$('#file-edit-mode_'+index).val("EDIT");
	var tableNameStr = liuAttachPool.isNull(tableName)?"TABLE_SYS_"+index:tableName;
	if($("#UploadAttachDiv001L").length > 0){
	} else {
		$(document.body).append("<div id=\"UploadAttachDiv001L\"></div>");
	}
	if (index != 1) {
		$("#UploadAttachDiv001L").html($("#UploadAttachDiv001L").html() + liuAttachPool.addHideAttachDiv(index));
	} else {
		$("#UploadAttachDiv001L").html(liuAttachPool.addHideAttachDiv(index));
	}
	liuAttachPool.initMultipleUpload(index,id,tableNameStr,category,true);
}
//多个单体附件上传
liuAttachPool.initMultiplePageAttachOnView = function(index,id,tableName,category){
	$('#file-edit_a_'+index).hide();
	$('#file-edit_div_'+index).hide();
	$('#file-edit-mode_'+index).val("VIEW");
	var tableNameStr = liuAttachPool.isNull(tableName)?"TABLE_SYS_"+index:tableName;
	if($("#UploadAttachDiv001L").length > 0){
	} else {
		$(document.body).append("<div id=\"UploadAttachDiv001L\"></div>");
	}
	if (index != 1) {
		$("#UploadAttachDiv001L").html($("#UploadAttachDiv001L").html() + liuAttachPool.addHideAttachDiv(index));
	} else {
		$("#UploadAttachDiv001L").html(liuAttachPool.addHideAttachDiv(index));
	}
	liuAttachPool.initMultipleUpload(index,id,tableNameStr,category,false);
	$('.uploadButtModule').hide();
}

//添加回调
liuAttachPool.addAttachCallback = function(listId,fileId){
	var index = listId.substr("#attachList".length);
	$('#file-attachLisThead_'+index).show();
	var mode =$('#file-edit-mode_'+index).val();
	if("VIEW" == mode){
		$('#file-view_div_'+index).show();
	}
}

//清空数据
liuAttachPool.clearPageAttach = function(index){
	attachPool.clearFileQueue("#uploader"+index);
}

//新增数据
liuAttachPool.getFiles = function(index){
	var inputs = document.getElementById("attachList" + index).getElementsByTagName("input");
	var res = [];
	for(var i = 0; i < inputs.length; i++){
		if(inputs[i].name == 'fileid') {
			res.push(inputs[i].value);
		}
	}
	if (res.length > 0) {
		return res.join(',');
	}
	return "";
}
//新增数据
liuAttachPool.getAllFiles = function(){
	var res = [];
	$("input[name='fileid']").each(function(){
		res.push($(this).val());
	});
	if (res.length > 0) {
		 res.join(',');
	}
	return res

}

//删除数据
liuAttachPool.getDeleteFiles = function(index){
	return attachPool.getDeleteFiles();
}

//附件上传隐藏区域
liuAttachPool.addHideAttachDiv = function(i){
	var html = "";
	html += "<div class=\"modal fade panel\" id=\"file-edit"+i+"\" aria-hidden=\"false\">";
	html += "<div class=\"modal-dialog\">";
	html += "	<div class=\"modal-content\">";
	html += "		<div class=\"modal-header\">";
	html += "			<button type=\"button\" class=\"close\" id=\"closebtn\" data-dismiss=\"modal\">×</button>";
	html += "			<h4 class=\"modal-title\">添加上传文件</h4>";
	html += "		</div>";
	html += "		<div class=\"modal-body\">";
	html += "			<div id=\"wrapper\">";
	html += "				<div id=\"container\">";
	html += "					<div id=\"uploader"+i+"\" class=\"attach\">";
	html += "						<div class=\"queueList\">";
	html += "							<div id=\"dndArea"+i+"\" class=\"placeholder\">";
	html += "								<div id=\"filePicker"+i+"\"></div>";
	html += "								<p></p>";
	html += "							</div>";
	html += "						</div>";
	html += "						<div class=\"statusBar\" style=\"display:none;\">";
	html += "							<div class=\"progress\">";
	html += "								<span class=\"text\"></span>";
	html += "								<span class=\"percentage\"></span>";
	html += "							</div>";
	html += "							<div class=\"info\"></div>";
	html += "							<div class=\"btns\">";
	html += "								<div id=\"filePickerBtn"+i+"\" class=\"attachBtn\"></div>";
	html += "								<div class=\"uploadBtn\">上传</div>";
	html += "							</div>";
	html += "						</div>";
	html += "					</div>";
	html += "				</div>";
	html += "			</div>";
	html += "		</div>";
	html += "		<div class=\"modal-footer form-btn\">";
	html += "			<button class=\"btn dark\" type=\"button\" id=\"closeModalBtn\" data-dismiss=\"modal\">关 闭</button>";
	html += "		</div>";
	html += "	</div>";
	html += "</div>";
	html += "</div>";
	return html;
}

//附件上传功能初始化
liuAttachPool.initUpload = function(index,id,tableName,flag){
	//单上传实例，限制只能上传图片
	var idValue= liuAttachPool.isNull(id)?"-1":id;
	var formTemp = {
		businessId: id,
		businessTable: tableName,
		category: tableName
	}
	attachPool.add({
		containerId: '#uploader'+index,
		isEcho:true,
		flag:flag,
		falg:flag,
		listId: '#attachList'+index,
		uploadInitId: '#filePicker'+index,
		dndArea: '#dndArea'+index,
		form:formTemp,
		accept:null,
		toButtonView:'#file-edit_a_'+index,
		addButton:{id:"#filePickerBtn"+index,label:"添加"}
	});
};

//多个单体附件上传功能初始化
liuAttachPool.initMultipleUpload = function(index,id,tableName,category,flag){
	//单上传实例，限制只能上传图片
	var idValue= liuAttachPool.isNull(id)?"-1":id;
	var formTemp = {
		businessId: id,
		businessTable: tableName,
		category: category
	}
	attachPool.add({
		containerId: '#uploader'+index,
		isEcho:true,
		flag:flag,
		falg:flag,
		listId: '#attachList'+index,
		uploadInitId: '#filePicker'+index,
		dndArea: '#dndArea'+index,
		form:formTemp,
		accept:null,
		toButtonView:'#file-edit_a_'+index,
		addButton:{id:"#filePickerBtn"+index,label:"添加"}
	});
};
liuAttachPool.isNull = function(value){
	if(value){
		if(value == null || value == '' || typeof(value) == 'undefined' || value == 'undefined'){
			return true;
		}
	} else {
		return true;
	}
	return false;
};

var AttachControl = window.AttachControl = AttachControl || {};
(function () {
	/**
	 * renderElement: 渲染元素id
	 * itemName: '工作流的东西'
	 * titleWidth: '标题宽度' 数字
	 * businessTable: 业务表，也是表示,同时也是附件分类的可以，因此建议配置附件分类的时候设置好
	 * @type {Window.AttachControl.AttachListControl}
	 */
	var AttachListControl = AttachControl.AttachListControl = function (option) {
		this._option = option;
		this._option.titleWidth = this._option.titleWidth || 200;
		this.drawRenderElement();
	};

	/*画控件渲染位置*/
	AttachListControl.prototype.drawRenderElement = function() {
		var me = this;
		$.ajax({
			type: "GET", data: {code: me._option.businessTable}, dataType: "json", url: getRootPath() + "/config/attach/getAttachListByItemCode.action",
			success : function(data) {
				me._option.configData = data;
				var content = '';
				for (var i = 0 ; i < data.length; i++) {
					var row = data[i];
					var index = i + 1;
					content += '<tr>'
						+ '<td style="width: ' + me._option.titleWidth + 'px; white-space: pre-wrap; text-align: right;">'
						+ (row.isRequired == 0 ? '<span class="required">*</span>' : '') + row.itemAttach
						+ '</td>'
						+ '<td style="width: 60px;" class="uploadButtModule"><div class="uploadButt" style="width: 60px;"><a class="btn dark" type="button" data-target="#file-edit' + index + '" id="file-edit_a_' + index + '" role="button" data-toggle="modal">上传</a></div></td>'
						+ '<td>'
						+ (me.itemName != undefined ? "<span workFlowForm='attach' itemName=\"" + me.itemName + "\">" : '')
						+ '<div class="fjList">'
						+ '<ul class="fjListTop nobt"><li><span class="enclo">已上传附件</span><span class="enclooper">操作</span></li></ul>'
						+ '<ul class="fjListTop" id="attachList' + index + '"></ul>'
						+ '</div>'
						+ (me.itemName != undefined ? "</span>" : '')
						+ '</td>'
						+ '</tr>';
					$('#' + me._option.renderElement).html(content);
				}
			}
		});
	};

	/**
	 * 加载可以写的控件
	 */
	AttachListControl.prototype.initAttach = function (id) {
		var me = this;
		if (me._option.configData == undefined) {
			setTimeout(function() {
				for (var i = 0; i < me._option.configData.length; i++) {
					var businessTable = me._option.businessTable;
					if (me._option.configData[i].id != "0") {
						businessTable = businessTable + "_" + me._option.configData[i].id
					}
					liuAttachPool.inittMultiplePageAttach(i + 1, id, me._option.businessTable, businessTable);
				}
			}, 1000);
		} else {
			for (var i = 0; i < me._option.configData.length; i++) {
				var businessTable = me._option.businessTable;
				if (me._option.configData[i].id != "0") {
					businessTable = businessTable + "_" + me._option.configData[i].id
				}
				liuAttachPool.inittMultiplePageAttach(i + 1, id, me._option.businessTable, businessTable);
			}
		}
	};

	/**
	 * 加载查看的附件
	 * @param id
	 */
	AttachListControl.prototype.initAttachOnView = function (id) {
		var me = this;
		if (me._option.configData == undefined) {
			setTimeout(function(){
				for (var i = 0; i < me._option.configData.length; i++) {
					var businessTable = me._option.businessTable;
					if (me._option.configData[i].id != "0") {
						businessTable = businessTable + "_" + me._option.configData[i].id
					}
					liuAttachPool.initMultiplePageAttachOnView(i + 1, id, me._option.businessTable, businessTable);
				}
			}, 1000);
		} else {
			for (var i = 0; i < me._option.configData.length; i++) {
				var businessTable = me._option.businessTable;
				if (me._option.configData[i].id != "0") {
					businessTable = businessTable + "_" + me._option.configData[i].id
				}
				liuAttachPool.initMultiplePageAttachOnView(i + 1, id, me._option.businessTable, businessTable);
			}
		}
	};

	/**
	 * 验证上传的附件是否符合要求
	 */
	AttachListControl.prototype.validate = function () {
		var me = this;
		var result = true;
		for (var i = 0; i < me._option.configData.length; i++) {
			var row = me._option.configData[i];
			var files = liuAttachPool.getFiles(i + 1);
			if (row.isRequired == 0) {
				//必填
				if (files == '') {
					msgBox.tip({ type:"fail", content: '请上传' + row.itemAttach });
					result = false;
					break;
				}
			}
			if (row.isCheckbox == 1) {
				//只能上传一个附件
				if (files.indexOf(',') > -1) {
					msgBox.tip({ type:"fail", content: row.itemAttach + "只能上传一个附件" });
					result = false;
					break;
				}
			}
		}
		return result;
	};

	AttachListControl.prototype.loadOpinionCtrl = function(pid, itemId, renderId) {
		$.ajax({
			type: "GET", data: {pid: pid, itemId: itemId}, dataType: "json",
			url: getRootPath() + "/common/api/suggestList.action",
			success : function(data) {
				if (data) {
					var content = "<thead><tr><th style='width: 200px;'>审批时间</th><th style='width: 150px;'>审批人</th><th>办理意见</th></tr></thead>";
					content += "<tbody>"
					if (data.length == 0) {
						content += "<tr><td class='dataTables_empty' colspan='3'>没有匹配的记录</td></tr>";
					} else {
						for (var i = 0; i < data.length; i++) {
							var row = data[i];
							content += "<tr>";
							content += "<td>" + dateFormat(row.createDate, 'yyyy-MM-dd HH:mm:ss') + "</td>";
							content += "<td>" + row.userName + "</td>";
							content += "<td>" + row.message + "</td>";
							content += "</tr>";
						}
					}
					content += "</tbody>";
					content += "</table>";
					$('#' + renderId).html(content);
				}
			}
		});
	};

	var dateFormat  = function (millisecond, format) {
		var resultDate = new Date(millisecond);
		if(format){
			var dateJson = {
				"y+": resultDate.getFullYear(),
				"M+": resultDate.getMonth() + 1,
				"d+": resultDate.getDate(),
				"H+": resultDate.getHours(),
				"m+": resultDate.getMinutes(),
				"s+": resultDate.getSeconds(),
			};
			for(var key in dateJson){
				if(new RegExp("(" + key + ")").test(format)){
					format = format.replace(RegExp.$1, (dateJson[key] + "").length < 2 ? "0" + dateJson[key] : dateJson[key]);
				}
			}
			return format;
		}
		return resultDate;
	};
})();