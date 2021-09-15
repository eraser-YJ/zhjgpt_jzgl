/**
 * 	角色人员嵌套
 */
selectControl = {};	//人员对象

selectControl.init = function() {
	var openPersonDivId = "openPersonDiv";	//人员层DivId
	var selectBackValueId = "backValue";	//select下拉控件ID
	
	var personHtml = 
	'<div id="'+openPersonDivId+'" aria-hidden="false" style="padding-top: 0px;">'+
		'<div class="w1100 modal-tree">'+
			'<div class="modal-content">'+
				'<div class="modal-body clearfix"></div>'+	//显示人员列表
			'</div>'+
		'</div>'+
	'</div>';
	$(personHtml).empty();
	$("#userSelectDiv").append(personHtml);
	
	$.ajax({
		async : false,
		url : getRootPath()+"/department/getAllDeptAndUser.action",
		type : 'post',
		success : function(data) {
			$("#"+openPersonDivId).find(".modal-body").html(selectControl.showDeptUserPage(eval(data)[0], selectBackValueId, openPersonDivId));
			//人员界面收缩事件
			$(".tree-list .tree-btn").on('click',function (e) {
		        e.preventDefault()
		        e.stopPropagation()
		        $(this).find("i").toggleClass("fa-chevron-down").end().closest(".tree-list").next().slideToggle();
		    });
		},
		error: function(){
			
		}
	});
};
/**
 * 打开人员界面
 * @param openPersonDivId 人员DivID
 */
selectControl.openPerson = function(openPersonDivId){}

/**
 * 选择部门级联人员操作
 * @param obj				本控件对象
 * @param userId			用户ID
 * @param selectId			select的ID
 * @param openPersonDivId	弹出层DIVID
 */
selectControl.selectDeptUser = function(obj, userId, selectId, openPersonDivId){
	if($("#"+openPersonDivId+" #"+obj.id).prop("checked")){
		$("input[name='"+obj.id+"']").prop("checked", true);
		$("input[name='"+obj.id+"']").each(function(){
			$("#"+selectId+" option[value='"+this.value.split(",")[0]+"']").remove();
			$("#"+selectId).append("<option value='"+this.value.split(",")[0]+"'>"+this.value.split(",")[1]+"</option>");
		});
	}else{
		$("input[name='"+obj.id+"']").prop("checked", false);
		$("input[name='"+obj.id+"']").each(function(){
			$("#"+selectId+" option[value='"+this.value.split(",")[0]+"']").remove();
		});
	}
}

/**
 * 选择人员级联部门操作
 * @param obj				本控件对象
 * @param deptId			部门ID对象
 * @param selectId			select的ID
 * @param openPersonDivId	弹出层DIVID
 */
selectControl.selectUserDept = function(obj, deptId, selectId, openPersonDivId){
	if($("#"+openPersonDivId+" #"+obj.id).prop("checked")){
		$("#"+selectId).append("<option value='"+$(obj).val().split(",")[0]+"'>"+$(obj).val().split(",")[1]+"</option>");
	}else{
		$("#"+selectId+" option[value='"+$(obj).val().split(",")[0]+"']").remove();
	}
	var sTmp = 0;
	var IE8 = isIE8();
	if(IE8){
		$("#"+openPersonDivId+" input[name='"+deptId[0].id+"']").each(function(){
			if(this.checked)
				sTmp++;
		});
		if(sTmp == $("#"+openPersonDivId+" input[name='"+deptId[0].id+"']").length){
			$("input[id='"+deptId[0].id+"'][name='depts']").prop("checked", true);
		}else{
			$("input[id='"+deptId[0].id+"'][name='depts']").removeAttr("checked");
		}
	}else{
		$("#"+openPersonDivId+" input[name='"+deptId.id+"']").each(function(){
			if(this.checked)
				sTmp++;
		});
		if(sTmp == $("#"+openPersonDivId+" input[name='"+deptId.id+"']").length){
			$("input[id='"+deptId.id+"']").prop("checked", true);
		}else{
			$("input[id='"+deptId.id+"']").removeAttr("checked");
		}
	}
}

/**
 * 选择框排序上
 * @param selectId
 */
function up(selectId){
	if($("#"+selectId).val() == null){
		msgBox.tip({content: "请选择人员", type:'fail'});
	}else{
		var optionIndex = $("#"+selectId).get(0).selectedIndex; 
		if(optionIndex > 0){ 
			$('#'+selectId+' option:selected').insertBefore($('#'+selectId+' option:selected').prev('option')); 
		}
	}
}

/**
 * 选择框排序下
 * @param selectId
 */
function down(selectId){
	if($("#"+selectId).val() == null){
		msgBox.tip({content: "请选择人员", type:'fail'});
	}else{
		var optionLength = $("#"+selectId)[0].options.length; 
		var optionIndex = $("#"+selectId).get(0).selectedIndex; 
		if(optionIndex < (optionLength-1)){ 
			$('#'+selectId+' option:selected').insertAfter($('#'+selectId+' option:selected').next('option')); 
		} 
	}
}

/**
 * 显示人员控件
 * @param d 				部门人员数据
 * @param selectId 			select的ID
 * @param openPersonDivId	弹出层DIVID
 * @returns
 */
selectControl.showDeptUserPage = function(d, selectId, openPersonDivId){
	var lv1_dept = '<div>';
	var lv1_user = '<div>';
	if(d != null && d.user.length > 0){
		lv1_dept += '<label for="" class="checkbox fl"><input type="checkbox" id="dept'+d.id+'" name="depts" onClick="selectControl.selectDeptUser(this,'+d.id+',\''+selectId+'\',\''+openPersonDivId+'\')"> '+d.name+'</label> ';
		for(var l1=0;l1<d.user.length;l1++){
			lv1_user += 
				'<label for="" class="checkbox inline"> '+
					'<input type="checkbox" id="'+d.user[l1].id+'" name="dept'+d.user[l1].deptId+'" value="'+d.user[l1].id+','+d.user[l1].displayName+'" onClick="selectControl.selectUserDept(this,dept'+d.user[l1].deptId+',\''+selectId+'\',\''+openPersonDivId+'\')"> '+ d.user[l1].displayName +
				'</label>';
		}
	}else{
		lv1_dept += '<label for="" class="checkbox fl">'+d.name+'</label> ';
		lv1_user += '<label for="" class="checkbox inline"></label>';
	}
	lv1_user += '</div>';
	lv1_dept += '<a href="#" class="fr m-r tree-btn"><i class="fa fa-chevron-up"></i></a></div>';
	var showlist = $(
		'<section class="w820 fl tree-ul tree-scroll">'+
			'<ul class="tree-horizontal clearfix">'+
				'<li>'+
				'<div class="level1 clearfix tree-list">'+
					lv1_dept +
					lv1_user +
				'</div>'+
				'<ul id="lv"></ul>'+
				'</li>'+
			'</ul>'+
		'</section>'+
		'<section class="fl m-l pos-rlt">'+
			'<select id="'+selectId+'" name="'+selectId+'" multiple="false" class="w170 tree-scroll-right tree-select">'+
            '</select>'+
            '<div class="tree-move"> '+
            	'<a href="#" class="tree-move-up" onClick="up(\''+selectId+'\');"><i class="fa fa-caret-up"></i></a> '+
            	'<a href="#" class="tree-move-down" onClick="down(\''+selectId+'\');"><i class="fa fa-caret-down"></i></a> '+
            '</div>'+
        '</section>'
	);
	if(d.subDept.length > 0)
		selectControl.recur(d.subDept, showlist.find("#lv"), 2, selectId, openPersonDivId);
	return showlist;
}

/**
 * 递归查询下级菜单
 * @param deptList			部门集合
 * @param parentHtml		上级html
 * @param level				level-css样式
 * @param selectId			select的ID
 * @param openPersonDivId	弹出层DIVID
 */
selectControl.recur = function(deptList, parentHtml, level, selectId, openPersonDivId){
	for(var i=0; i<deptList.length; i++){
		if(deptList[i].subDept) {
			var main_dept = '<div>';
			var main_user = '<div>';
			if(deptList[i].deptType != 1 && deptList[i].user.length > 0){
				main_dept += '<label for="" class="checkbox fl"><input type="checkbox" id="dept'+deptList[i].id+'" name="depts" onClick="selectControl.selectDeptUser(this,'+deptList[i].id+',\''+selectId+'\',\''+openPersonDivId+'\')"> '+deptList[i].name+'</label> ';
				if(deptList[i].user != undefined){
					if(deptList[i].user.length){
						for(var m=0; m<deptList[i].user.length; m++){
							main_user += 
								'<label for="" class="checkbox inline"> '+
									'<input type="checkbox" id="'+deptList[i].user[m].id+'" name="dept'+deptList[i].id+'" value="'+deptList[i].user[m].id+','+deptList[i].user[m].displayName+'" onClick="selectControl.selectUserDept(this,dept'+deptList[i].user[m].deptId+',\''+selectId+'\',\''+openPersonDivId+'\')"> '+ deptList[i].user[m].displayName +
								'</label>';
						}
					}
				}else{
					for(var m=0; m<deptList[i].subDept[0].user.length; m++){
						main_user += 
							'<label for="" class="checkbox inline"> '+
								'<input type="checkbox" id="'+deptList[i].subDept[0].user[m].id+'" name="dept'+deptList[i].subDept[0].user[m].deptId+'" value="'+deptList[i].subDept[0].user[m].id+','+deptList[i].subDept[0].user[m].displayName+'" onClick="selectControl.selectUserDept(this,dept'+deptList[i].subDept[0].user[m].deptId+',\''+selectId+'\',\''+openPersonDivId+'\')"> '+ deptList[i].subDept[0].user[m].displayName +
							'</label>';
					}
				}
			} else {
				main_dept += '<label for="" class="checkbox fl">'+deptList[i].name+'</label> ';
				main_user += '<label for="" class="checkbox inline"></label>';
			}
			main_dept += '<a href="#" class="fr m-r tree-btn"><i class="fa fa-chevron-up"></i></a></div>';
			main_user += '</div>';
			var main_sub = $(
				'<li>'+
					'<div class="level'+level+' clearfix tree-list">'+
						main_dept +
						main_user +
					'</div>'+
					'<ul id="lv'+i+'"></ul>'+
				'</li>'
			);
			$(main_sub).appendTo(parentHtml);
			selectControl.recur(deptList[i].subDept, $(main_sub).children().last(), level+1, selectId, openPersonDivId);
		} else {
			var sub_dept = '<div>';
			var sub_user = '<div>';
			if(deptList[i].deptType != 1 && deptList[i].user.length > 0){
				sub_dept += '<label for="" class="checkbox fl"><input type="checkbox" id="dept'+deptList[i].id+'" name="depts" onClick="selectControl.selectDeptUser(this,'+deptList[i].id+',\''+selectId+'\',\''+openPersonDivId+'\')"> '+deptList[i].name+'</label> ';
				for(var s=0;s<deptList[i].user.length;s++){
					sub_user += 
						'<label for="" class="checkbox inline"> '+
							'<input type="checkbox" id="'+deptList[i].user[s].id+'" name="dept'+deptList[i].user[s].deptId+'" value="'+deptList[i].user[s].id+','+deptList[i].user[s].displayName+'" onClick="selectControl.selectUserDept(this,dept'+deptList[i].user[s].deptId+',\''+selectId+'\',\''+openPersonDivId+'\')"> '+ deptList[i].user[s].displayName +
						'</label>';
				}
			}else{
				sub_dept += '<label for="" class="checkbox fl">'+deptList[i].name+'</label> ';
				sub_user += '<label for="" class="checkbox inline"></label>';
			}
			sub_user += '</div>';
			sub_dept += '</div>';
			var sub = $(
				'<li>'+
					'<div class="level'+level+' clearfix tree-list">'+
						sub_dept +
						sub_user +
					'</div>'+
				'</li>'
			);
			$(sub).appendTo(parentHtml);
		}
	}
}