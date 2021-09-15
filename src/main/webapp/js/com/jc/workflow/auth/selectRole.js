/**
 * 	流程权限角色选择
 */
selectControl = {};	//选择控件[人员、组织]主对象
selectControl.subState = false;
var f = true;
selectControl.init = function() {
	
	var personHtml = '<div class="modal fade" id="selectDeptRole" aria-hidden="false">'+
		'<div class="modal-dialog w1100 modal-tree">'+
			'<div class="modal-content">'+
				'<div class="modal-header clearfix">'+
					'<button type="button" class="close" data-dismiss="modal">×</button>'+
					'<h4 class="modal-title fl">授权</h4>'+
					'<div class="fl btn-group m-l-lg" data-toggle="buttons">'+
					'</div>'+
				'</div>'+
				'<div class="modal-body clearfix"></div>'+
				'<div class="modal-footer form-btn">'+
					'<button class="btn dark" type="button" onClick="selectControl.saveRoleProcess();">确 定</button>'+
					'<button class="btn" type="button" onClick="selectControl.allCheckbox(\'backValue\');">全 选</button>'+
					'<button class="btn" type="button" onClick="selectControl.closeModel()">关 闭</button>'+
				'</div>'+
			'</div>'+
		'</div>'+
	'</div>';
	
	$("[id^=openPersonDiv]").remove();
	$("body").append(personHtml);
	
	$("#selectDeptRole").find(".modal-body").load(getRootPath()+"/sys/role/getAllDeptAndRole.action", null, function(data,textStatus,XMLHttpRequest){
		if(data == "[]"){
			$(this).html("");
			return;
		}
		$(this).html(selectControl.showDeptUserPage(eval(data)[0]));
	});	
	
};

selectControl.closeModel = function(){
	$('#selectDeptRole').modal('hide');
	f = true;
}


selectControl.allCheckbox = function(selectControlId){
	if(f){
		$('input[name="deptRoles"]').prop("checked", true);
		$('input[name="deptRoles"]').each(function(){
			$('input[name="'+this.id+'"]').prop("checked", true);
			$('input[name="'+this.id+'"]').each(function(){
				$("select[name='"+selectControlId+"'] option[value='"+this.value.split(",")[0]+"']").remove();
				$("select[name='"+selectControlId+"']").append("<option value='"+this.value.split(",")[0]+"'>"+this.value.split(",")[1]+"</option>");
			});
		});
		f = false;
	}else{
		$('input[name="deptRoles"]').prop("checked", false);
		$('input[name="deptRoles"]').each(function(){
			$('input[name="'+this.id+'"]').prop("checked", false);
		});
		$("select[name='"+selectControlId+"']").empty();
		f = true;
	}
}

selectControl.getSelectRoles = function(){
	
}

selectControl.saveRoleProcess = function () {
	if (selectControl.subState)return;
	selectControl.subState = true;
	var url = getRootPath()+"/definitionAuth/saveDefinitionRole.action?time=" + (new Date()).getTime().toString();
	var roleIds = selectControl.getCheckedRoles();
	var formData = {definitionId:startAuth.definitionId,roleIds:roleIds.join(",")};
	//添加其他表单信息
	jQuery.ajax({
		url : url,
		type : 'POST',
		async:false,
		data : formData,
		success : function(data) {
			selectControl.subState = false;
			msgBox.tip({
				type:"success",
				content:"保存成功"
			});
			startAuth.roleList();
			$('#selectDeptRole').modal('hide');
		},
		error : function() {
			selectControl.subState = false;
			alertx("保存失败");
		}
	});
};


//添加部门、角色、机构表单数据
selectControl.getCheckedRoles = function (){
	var checkedRoles = new Array();
	$("select[name='backValue']").find("option").each(function(i, val){
		checkedRoles.push(this.value); //获取选中节点的值
	});
	return checkedRoles;
};


selectControl.openPerson = function(){
	var templateId = startAuth.definitionId;
	$("#selectDeptRole").modal("show");
	selectControl.toEcho(templateId);
}

//页面回显
selectControl.toEcho = function(templateId){
	jQuery.ajax({
		url : getRootPath()+"/definitionAuth/getDefinitionRoles.action?time=" + (new Date()).getTime().toString(),
		type : 'POST',
		async:false,
		data : {definitionId:templateId},
		success : function(data) {
			$("input:checkbox:checked").each(function(i){
				this.checked = false;
			})
			$("select[name='backValue']").empty();
			$(data).each(function(i, v){
				$.each($("#selectDeptRole input[type='checkbox']").not("input[name='deptRoles']"), function(ii, vv){
					var $this = $(vv);
					var cbValue = vv.value.split(",");
					if(cbValue[0] == v.roleId){
						$this.click();
					}
				});
				$(".tree-list .tree-btn").unbind("click");
				$(".tree-list .tree-btn").on('click',function (e) {
			        e.preventDefault()
			        e.stopPropagation()
			        $(this).find("i").toggleClass("fa-chevron-down").end().closest(".tree-list").next().slideToggle();
			    });
			});
		},
		error : function() {
		}
	});
}

/**
 * 选择部门级联人员操作
 * @param obj				本控件对象
 */
selectControl.selectDeptUser = function(obj){
	if($("#selectDeptRole #"+obj.id).prop("checked")){
		$("input[name='"+obj.id+"']").prop("checked", true);
		$("input[name='"+obj.id+"']").each(function(){
			var v = this.value.split(",");
			$("#backValue option[value='"+v[0]+"']").remove();
			$("#backValue").append("<option value='"+v[0]+"'>"+v[1]+"</option>");
		});
	}else{
		$("input[name='"+obj.id+"']").prop("checked", false);
		$("input[name='"+obj.id+"']").each(function(){
			var v = this.value.split(",");
			$("#backValue option[value='"+v[0]+"']").remove();
		});
	}
}

/**
 * 选择人员级联部门操作
 * @param id		本控件ID
 * @param value		本控件Value
 * @param deptId	部门ID
 */
selectControl.selectUserDept = function(id, value, deptId){
	var v = value.split(",");
	if($("#"+id).prop("checked")){
		$("#backValue").append("<option value="+v[0]+">"+v[1]+"</option>");
	}else{
		$("#backValue option[value="+v[0]+"]").remove();
	}
	var sTmp = 0;
	var IE8 = isIE8();
	if(IE8){
		$("input[name='"+deptId[0].id+"']").each(function(){
			if(this.checked)
				sTmp++;
		});
		if(sTmp == $("input[name='"+deptId[0].id+"']").length){
			$("input[id='"+deptId[0].id+"'][name='deptRoles']").prop("checked", true);
		}else{
			$("input[id='"+deptId[0].id+"'][name='deptRoles']").removeAttr("checked");
		}
	}else{
		$("input[name='"+deptId.id+"']").each(function(){
			if(this.checked)
				sTmp++;
		});
		if(sTmp == $("input[name='"+deptId.id+"']").length){
			$("input[id='"+deptId.id+"'][name='deptRoles']").prop("checked", true);
		}else{
			$("input[id='"+deptId.id+"'][name='deptRoles']").removeAttr("checked");
		}
	}
	
	
}
/**
 * 显示人员控件
 * @param d	部门人员数据
 * @returns
 */
selectControl.showDeptUserPage = function(d){
	var lv1_dept = '<div>';
	var lv1_user = '<div>';
	if(d.role.length > 0){
		lv1_dept += '<label for="" class="checkbox fl"><input type="checkbox" id="D'+d.id+'" name="deptRoles" onClick="selectControl.selectDeptUser(this)"> '+d.name+'</label> ';
		for(var l1=0;l1<d.role.length;l1++){
			lv1_user += 
				'<label for="" class="checkbox inline"> '+
					'<input type="checkbox" id="'+d.role[l1].id+'" name="D'+d.role[l1].deptId+'" value="'+d.role[l1].id+','+d.role[l1].name+'" onClick="selectControl.selectUserDept(this.id,this.value,D'+d.role[l1].deptId+')"> '+ d.role[l1].name +
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
			'<select id="backValue" name="backValue" multiple="true" class="w170 tree-scroll-right tree-select">'+
            '</select>'+
            '<div class="tree-move"> '+
            '</div>'+
        '</section>'
	);
	if(d.subDept.length > 0)
		selectControl.recur(d.subDept, showlist.find("#lv"), 2);
	return showlist;
}

/**
 * 递归查询下级菜单
 * @param deptList			部门集合
 * @param parentHtml		上级html
 * @param level				level-css样式
 */
selectControl.recur = function(deptList, parentHtml, level){
	for(var i=0; i<deptList.length; i++){
		if(deptList[i].subDept) {
			var main_dept = '<div>';
			var main_user = '<div>';
			if(deptList[i].role.length > 0){
				main_dept += '<label for="" class="checkbox fl"><input type="checkbox" id="D'+deptList[i].id+'" name="deptRoles" onClick="selectControl.selectDeptUser(this)"> '+deptList[i].name+'</label> ';
				if(deptList[i].role != undefined){
					if(deptList[i].role.length){
						for(var m=0; m<deptList[i].role.length; m++){
							main_user += 
								'<label for="" class="checkbox inline"> '+
									'<input type="checkbox" id="'+deptList[i].role[m].id+'" name="D'+deptList[i].id+'" value="'+deptList[i].role[m].id+','+deptList[i].role[m].name+'" onClick="selectControl.selectUserDept(this.id,this.value,D'+deptList[i].role[m].deptId+')"> '+ deptList[i].role[m].name +
								'</label>';
						}
					}
				}else{
					for(var m=0; m<deptList[i].subDept[0].role.length; m++){
						main_user += 
							'<label for="" class="checkbox inline"> '+
								'<input type="checkbox" id="'+deptList[i].subDept[0].role[m].id+'" name="D'+deptList[i].subDept[0].role[m].deptId+'" value="'+deptList[i].subDept[0].role[m].id+','+deptList[i].subDept[0].role[m].name+'" onClick="selectControl.selectUserDept(this.id,this.value,D'+deptList[i].subDept[0].role[m].deptId+')"> '+ deptList[i].subDept[0].role[m].name +
							'</label>';
					}
				}
			} else {
				main_dept += '<label for="" class="checkbox fl"><span>'+deptList[i].name+'</span></label> ';
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
			selectControl.recur(deptList[i].subDept, $(main_sub).children().last(), level+1);
		} else {
			var sub_dept = '<div>';
			var sub_user = '<div>';
			if(deptList[i].role.length > 0){
				sub_dept += '<label for="" class="checkbox fl"><input type="checkbox" id="D'+deptList[i].id+'" name="deptRoles" onClick="selectControl.selectDeptUser(this)"> '+deptList[i].name+'</label> ';
				for(var s=0;s<deptList[i].role.length;s++){
					sub_user += 
						'<label for="" class="checkbox inline"> '+
							'<input type="checkbox" id="'+deptList[i].role[s].id+'" name="D'+deptList[i].role[s].deptId+'" value="'+deptList[i].role[s].id+','+deptList[i].role[s].name+'" onClick="selectControl.selectUserDept(this.id,this.value,D'+deptList[i].role[s].deptId+')"> '+ deptList[i].role[s].name +
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
