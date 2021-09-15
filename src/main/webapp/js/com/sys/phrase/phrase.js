var phrase = {}, pageCount=10;
phrase.subState = false;
phrase.oTable = null;

phrase.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(phrase.oTable, aoData);
};

phrase.phraseList = function () {
	if (phrase.oTable == null) {
		phrase.oTable = $('#phraseTable').dataTable( {
			"iDisplayLength": phrase.pageCount,
			"sAjaxSource": getRootPath() + "/sys/phrase/manageList.action",
			"fnServerData": oTableRetrieveData,
			"aoColumns": [
				{mData: function(source) { return "<input type=\"checkbox\" name=\"ids\" value="+ source.id + ">"; }, sTitle: '<input type="checkbox" />', bSortable: false, sWidth: 46},
				{mData: "phraseType", sTitle: '类型', bSortable: false, mRender: function(mData) { return mData == "0" ? "公共" : "个人"; }},
				{mData: "userName", sTitle: '创建人', bSortable: false},
				{mData: "content", sTitle: '内容', bSortable: false},
				{mData: function(source) {
					var edit = "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"phrase.loadUpdateHtml('"+ source.id+ "')\" role=\"button\" data-toggle=\"modal\">" + finalParamEditText + "</a>";
					var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"phrase.deletephrase('"+ source.id+ "')\">" + finalParamDeleteText + "</a>";
					return edit + del ;
				}, sTitle: '操作', sWidth: 130, bSortable: false}
			],
			"fnServerParams": function ( aoData ) {
				phrase.oTableFnServerParams(aoData);
			},
			aaSorting: [],
			aoColumnDefs: []
		} );
	} else {
		phrase.oTable.fnDraw();
	}
};

phrase.clearForm = function(form){	    	  
	$(':input', form).each(function() {
		var type = this.type;
		var tag = this.tagName.toLowerCase(); // normalize case
		if (type == 'text' || type == 'password' || tag == 'textarea') {
			this.value = "";
		}
	});
};

phrase.loadAddHtml = function (){
	if ($.trim($("#phraseEdit").html()) == "") {
		$("#phraseEdit").load(getRootPath() + "/sys/phrase/phraseEdit.action", null, phrase.createPhrase);
	} else {
		phrase.createPhrase();
	}
};

phrase.createPhrase = function() {
	hideErrorMessage();
	$("#id").attr("value","");
	phrase.clearForm(phraseForm);
	if ($("#tempisAdmin").val() == 1) {
		$("#loadtype").html("<input type=\"hidden\" id=\"phraseType\" name=\"phraseType\" value=\"0\">公共");
	} else if($("#tempisAdmin").val() == 0) {
		$("#loadtype").html("<input type=\"hidden\" id=\"phraseType\" name=\"phraseType\" value=\"1\">个人");
	}
	$("#titlephrase").html("新增");
	$("#count").html(30);
	$('#myModal-edit').modal('show');
	ie8StylePatch();
};

phrase.loadUpdateHtml = function (id){
	if ($.trim($("#phraseEdit").html()) == "") {
		$("#phraseEdit").load(getRootPath() + "/sys/phrase/phraseEdit.action", null, function(){
			phrase.get(id);
		});
	} else {
		phrase.get(id);
	}
};

phrase.get = function(id){
	hideErrorMessage();
	jQuery.ajax({
		url: getRootPath() + "/sys/phrase/get.action?id=" + id + "&time=" +(+new Date().getTime()),
		type: 'post', dataType: 'json',
		success: function(data, textStatus, xhr) {
			phrase.clearForm(phraseForm);
			$("#phraseForm").fill(data);
			$("#titlephrase").html("编辑");
			$("#count").html(30 - $("#phraseForm #content").val().length);
			if (data.phraseType == 0) {
				$("#loadtype").html("<input type=\"hidden\" id=\"phraseType\" name=\"phraseType\" value=\"0\">公共");
			} else if(data.phraseType == 1) {
				$("#loadtype").html("<input type=\"hidden\" id=\"phraseType\" name=\"phraseType\" value=\"1\">个人");
			}
			$('#myModal-edit').modal('show');
			ie8StylePatch();
		},error:function(){
			msgBox.tip({content: "加载数据错误", type:'fail'});
		}
	});
};

phrase.deletephrase = function (id) {
	var ids = String(id);
	if (id == 0) {
		var idsArr = [];
		$("[name='ids']:checked").each(function() {
			idsArr.push($(this).val());
		});
		ids = idsArr.join(",");
	}
	if (ids.length == 0) {
		msgBox.info({content: "请选择要删除的数据", type:'fail'});
		return;
	}
	confirmx($.i18n.prop("JC_SYS_034"),function(){phrase.deleteCallBack(ids,idsArr);});
};

phrase.deleteCallBack = function(ids,idsArr) {
	$.ajax({
		type : "POST", url : getRootPath() + "/sys/phrase/deleteByIds.action?time=" + (+new Date().getTime()), data : {"ids": ids}, dataType : "json",
		success : function(data) {
			if (data > 0) {
				msgBox.tip({
					content: "删除成功",
					type:'success',
					callback:function(){
						phrase.phraseList();
						pagingDataDeleteAllForGoBack("phraseTable",idsArr);
					}
				});
			}
		}
	});
};

jQuery(function($) {
	phrase.pageCount = TabNub>0 ? TabNub : 10;
	$("#phraseBottom").click(phrase.loadAddHtml);
	$("#deletephrases").click("click", function(){phrase.deletephrase(0);});
	phrase.phraseList();
});	


