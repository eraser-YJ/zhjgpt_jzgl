/**
 * @title GOA V2.0
 * @description 
 * Copyright (c) 2014 ccyixun.cn Inc. All Rights Reserved
 * Company 长春奕迅建筑智能化工程有限公司
 * @author 都业广
 * @version  2014-07-22 09:50
 */

var commonGroupEdit = {};
  	
//重复提交标识
commonGroupEdit.subState = false;

commonGroupEdit.jctree = null;
/** 保存方法
 * @author 都业广
 * @version  2014-05-22 13:47
 */
commonGroupEdit.groupSubmit = function(){
	
	//二次提交检测
	if (commonGroupEdit.subState){
	    return;
	}else{
		commonGroupEdit.subState = true;
	}
	
	//验证表单数据
	if($("#groupForm").valid()){
		var url;
	  
		//存在数据id时,对数据进行更新
		if($("#id").val().length > 0){
			
			//更新时的请求地址
			url = getRootPath()+"/sys/group/update.action?time="+(+new Date().getTime());
		
		//追加数据
		}else{
			
			//追加时的请求地址
			url = getRootPath()+"/sys/group/save.action?time="+(+new Date().getTime());
		}

		//ajax形式提交
		jQuery.ajax({
			url: url,
			//请求方式参数
			type: 'post',
			//传递表单参数
			data: $("#groupForm").serializeArray(),
			success: function(data, textStatus, xhr) {
				//二次提交位复原
				commonGroupEdit.subState = false;
				//getToken();
				$("#token").val(data.token);
				//后台提交处理数据出错时,errorMessage信息的返回值不为空
				if(data.errorMessage!=null){
					msgBox.tip({content: data.errorMessage, type:'fail'});
				}else{
					
					//提示message 保存成功
					msgBox.tip({content: $.i18n.prop("JC_SYS_001"), type:'success'});
					
					//关闭小窗口
					$('#myModal-edit').modal('hide');
					
					//刷新查询页面的list内容
					commonGroup.groupList();
				}
			},error:function(){
				//保存失败时提示信息 保存失败
				msgBox.tip({content: $.i18n.prop("JC_SYS_002"), type:'fail'});
			}
		});
	} else {
		
		//验证未通过时需要更改二次提交的标志位
		commonGroupEdit.subState = false;
	}
};

/** 处理缓存问题 清空form表单中值
 * @param form:表单的name
 * @author 都业广
 * @version  2014-05-22 13:47
 */
commonGroupEdit.clearForm = function(form){	
	
	//清空验证信息
	hideErrorMessage();
	   
	//选择form中所有的input元素
	$(':input', form).each(function() {
		//获得tag类型
	    var type = this.type;
	    //获得tag名称
	    var tag = this.tagName.toLowerCase(); // normalize case 
	    //清空text,password,和textarea的值
	    if (type == 'text' || type == 'password' || tag == 'textarea'){
		       this.value = "";
	    }
	});
	
	//清空隐藏变量
	$('#membersId').val("");
	$('#userJson').val("");
};

/** 按下新增按钮时,弹出对话框方法
 * @author 都业广
 * @version  2014-05-22 13:47
 */
commonGroupEdit.createGroup = function(){	
	
	//修改小画面的title
	$("#subTitle").text("新增");
	
	//getToken();
	
	//清空页面上用来保存id的隐藏元素的值
	$("#id").attr("value","");
	
	//调用清空form方法
	commonGroupEdit.clearForm(groupForm);
	if(commonGroupEdit.jctree)commonGroupEdit.jctree.clearValue();
	$('#myModal-edit').modal('show');
};
		 		
/** 取得按下修改按钮时,弹出对话框所需要的数据
 * @param id:数据与数据库中对应的id
 * @author 都业广
 * @version  2014-05-22 13:47
 */
commonGroupEdit.get = function(id){
	
	//修改小画面的title
	$("#subTitle").text("编辑");
	
	//getToken();
	
	//ajax提交内容
	jQuery.ajax({
		//请求地址
		url: getRootPath()+"/sys/group/get.action?id="+id+"&time="+(+new Date().getTime()),
		//请求方式
		type: 'post',
		//请求传递参数的数据格式
		dataType: 'json',
		//成功时的回调
		success: function(data, textStatus, xhr) {
			//清空掉form中的内容
			commonGroupEdit.clearForm(groupForm);
			//添加更新后的数据
			$("#groupForm").fill(data);
			if(commonGroupEdit.jctree){
			 	commonGroupEdit.jctree.clearValue();
				commonGroupEdit.jctree.setData(JSON.parse(data.userJson));
			}
		},error:function(){
			//错误信息:加载数据错误
			msgBox.tip({content: $.i18n.prop("JC_SYS_060"), type:'fail'});
		}
	});
	$('#myModal-edit').modal('show');
};


/**
 * 选择人员功能
 * @author 都业广
 * @param data 控件返回的数据
 * @param controlId 标签的ID
 * @version  2014-07-25 09:33
 */
commonGroupEdit.spCall = function(data, controlId) {
	//id
	var id = "";
	//姓名
	var name = "";
	//dataJson
	var userJson = "";
	
	//遍历数据
	if(data != null || data.length > 0){
		$.each(data, function(i, val){
			id += val.id + ",";
			name += val.text + ",";
			userJson += "{id:"+val.id+", text:'"+val.text+"'},";
		});
	}
	$('#userJson').val("[" + userJson.substring(0, userJson.length-1) + "]");  //用于数据回显
	$("#"+controlId).val(name.substring(0, name.length-1));
	$("#membersId").val(id.substring(0, id.length-1));
	//清空验证信息
	hideErrorMessage();
};

/** 页面初始化
 * @author 都业广
 * @version  2014-07-24 13:47
 */
jQuery(function($) 
{
	ie8StylePatch();
	//绑定保存按钮
	$("#groupBtn").click(commonGroupEdit.groupSubmit);
	commonGroupEdit.jctree = JCTree.init({	
		container  	: 'groupMember',
		controlId	: 'groupMember-groupMember'
	});
});	


