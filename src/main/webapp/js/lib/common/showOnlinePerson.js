var showOnlinePerson = {};			//显示在线人员对象

showOnlinePerson.index = 0;		//弹出窗口基数

var isShowOnlinePerson = true;	//是否显示在线人员窗口

showOnlinePerson.showId = null;
/**
 * 弹出在线人员
 */
showOnlinePerson.init = function() {
	this.index = parseInt(showOnlinePerson.index) + 1;

	if(showOnlinePerson.showId){
		$("#"+showOnlinePerson.showId).modal('show');
		return false;
	}

	var openPersonDivId = showOnlinePerson.showId = "openPersonDiv"+ this.index,

	personHtml = ['<div class="modal fade" id="'+openPersonDivId+'" aria-hidden="false">',
	'<div class="modal-dialog w900 modal-tree">',
		'<div class="modal-content">',
			'<div class="modal-header clearfix">',
				'<button type="button" class="close" data-dismiss="modal">×</button>',
				'<h4 class="modal-title fl">在线人员</h4>',
				'<div class="fl btn-group form-btn m-l-lg" data-toggle="buttons">',
				'</div>',
				'<form role="search" class="fr input-append m-b-none m-r-lg">',
					'<span class="add-on"></span> ',
				'</form>',
				'</div>',
				'<div class="modal-body clearfix"></div>',
				'<div class="modal-footer form-btn">',
				'</div>',
			'</div>',
		'</div>',
	'</div>'].join("");

	$(personHtml).empty();

	$(personHtml).appendTo("body");

	$("#"+openPersonDivId).find(".modal-body").empty();
	$.ajax({
		async : false,
		url : getRootPath()+"/api/department/getDeptAndUserByOnLine.action",
		type : 'post',
		success : function(data) {
			var value = data[0];
			$("#"+openPersonDivId).find(".modal-body").html(showOnlinePerson.showPage(value, openPersonDivId));
		},
		error: function(){
			msgBox.tip({content: "获取在线人员失败", type:'fail'});
		}
	});
	if(isShowOnlinePerson){
		//showOnlinePerson.showOnLine(openPersonDivId);
		$(".tree-list .tree-btn").on('click',function (e) {//人员界面收缩事件
	        e.preventDefault();
	        e.stopPropagation();
	        $(this).find("i").toggleClass("fa-chevron-down").end().closest(".tree-list").next().slideToggle();
	    });
		isShowOnlinePerson = false;
		$("#"+openPersonDivId).modal("show");
	}
}

/**
 * 显示在线人员
 */
showOnlinePerson.showOnLine = function(openPersonDivId){
	$.ajax({
		async: false,
		url : getRootPath()+"/system/getOnlineUsers.action",
		type : 'post',
		success : function(data) {
			$.each(data, function(i, o) {
				$("#"+openPersonDivId+" span:not([class])").each(function(ii, v){
					var t = $(v).text();
					if(t == o.displayName){
						var val = "<font color=\"#60AAE9\">"+t+"</font>";
						$(v).html(val);
					}
				});
			});
		},
		error: function(){
			msgBox.tip({content: "获取在线人员失败", type:'fail'});
		}
	});
}
/**
 * 显示人员
 * @param d 				部门人员数据
 * @param openPersonDivId	弹出层DIVID
 */
showOnlinePerson.showPage = function(d, openPersonDivId){
	var lv1_dept = ['<div>'], lv1_user = ['<div>'];
	if(d != null){
		if(d.user != null && d.user.length > 0){
			lv1_dept.push('<label class="radio fl"><span class="fl w100">'+d.name+'</span></label>');
			var d_user_len = d.user.length;
			for(var l1=0;l1<d_user_len;l1++){
				var d_user_l1 = d.user[l1];
				lv1_user.push('<label class="checkbox inline">');
				lv1_user.push('<span>');
				lv1_user.push('<a href="#" onclick="showOnlinePerson.showPersonInfo(\''+d_user_l1.id+'\');"><font color="#60AAE9">'+d_user_l1.displayName +'</font></a>');
				lv1_user.push('</span></label>');
			}
		}else{
			lv1_dept.push('<label class="checkbox fl"><span class="fl w100">'+d.name+'</span></label> ');
			lv1_user.push('<label class="checkbox inline"></label>');
		}
		lv1_user.push('</div>');
		lv1_dept.push('<a href="#" class="fr m-r tree-btn"><i class="fa fa-chevron-up"></i></a></div>');
	}else{
		lv1_dept.push('<label class="fl"><span class="fl w100"></span></label></div> ');
		lv1_user.push('<div style="text-align:center;">'+$.i18n.prop("JC_SYS_007")+'</div>');
	}
	var showlist = $([
		'<section class="w820 fl tree-ul tree-scroll">',
			'<ul class="tree-horizontal clearfix">',
				'<li>',
				'<div class="level1 clearfix tree-list">',
					lv1_dept.join('') ,
					lv1_user.join('') ,
				'</div>',
				'<ul id="lv"></ul>',
				'</li>',
			'</ul>',
		'</section>'
	].join(''));
	if(d != null && d.subDept != null && d.subDept.length > 0)
		showOnlinePerson.recur(d.subDept, showlist.find("#lv"), 2, openPersonDivId);
	return showlist;
}

/**
 * 递归查询下级菜单
 * @param deptList			部门集合
 * @param parentHtml		上级html
 * @param level				level-css样式
 * @param openPersonDivId	弹出层DIVID
 */
showOnlinePerson.recur = function(deptList, parentHtml, level, openPersonDivId){
	var deptList_len = deptList.length;
	for(var i=0; i<deptList_len; i++){
		var deptList_dept = deptList[i];
		if(deptList_dept.subDept) {
			var main_dept = ['<div>'], main_user = ['<div>'];
			if(deptList_dept.user.length > 0){
				main_dept.push('<label class="radio fl"><span class="fl w100">'+deptList_dept.name+'</span></label>');
				if(deptList_dept.user != undefined){
					if(deptList_dept.user.length > 0){
						var user_len = deptList_dept.user.length;
						for(var m=0; m<user_len; m++){
							var user_m = deptList_dept.user[m];
							main_user.push('<label class="radio inline">');
							main_user.push('<a href="#" onclick="showOnlinePerson.showPersonInfo(\''+user_m.id+'\');"><span><font color="#60AAE9">'+user_m.displayName +'</font></span></a>');
							main_user.push('</label>');
						}
					}
				}else{
					var subDept_user_len = deptList_dept.subDept[0].user.length;
					for(var m=0; m<subDept_user_len; m++){
						var subDept_user_m = deptList_dept.subDept[0].user[m];
						main_user.push('<label class="radio inline">');
						main_user.push('<a href="#" onclick="showOnlinePerson.showPersonInfo(\''+subDept_user_m.id+'\');"><span><font color="#60AAE9">'+subDept_user_m.displayName +'</font></span></a>');
						main_user.push('</label>');
					}
				}
			} else {
				main_dept.push('<label class="radio fl"><span class="fl w100">'+deptList[i].name+'</span></label>');
				main_user.push('<label class="radio inline"></label>');
			}
			main_dept.push('<a href="#" class="fr m-r tree-btn"><i class="fa fa-chevron-up"></i></a></div>');
			main_user.push('</div>');
			var main_sub = $([
				'<li>',
					'<div class="level'+level+' clearfix tree-list">',
						main_dept.join('') ,
						main_user.join('') ,
					'</div>',
					'<ul id="lv'+i+'"></ul>',
				'</li>'
			].join(''));
			$(main_sub).appendTo(parentHtml);
			showOnlinePerson.recur(deptList[i].subDept, $(main_sub).children().last(), level+1, openPersonDivId);
		} else {
			var sub_dept = ['<div>'], sub_user = ['<div>'];
			if(deptList_dept.user.length > 0){
				sub_dept.push('<label class="radio fl"><span class="fl w100">'+deptList_dept.name+'</span></label> ');
				var deptList_user_len = deptList_dept.user.length;
				for(var s=0;s<deptList_user_len;s++){
					var deptList_user_s = deptList_dept.user[s];
					sub_user.push('<label class="radio inline">');
					sub_user.push('<a href="#" onclick="showOnlinePerson.showPersonInfo(\''+deptList_user_s.id+'\');"><span><font color="#60AAE9">'+deptList_user_s.displayName +'</font></span></a>');
					sub_user.push('</label>');
				}
			}else{
				sub_dept.push('<label class="radio fl"><span class="fl w100">'+deptList[i].name+'</span></label> ');
				sub_user.push('<label class="radio inline"></label>');
			}
			sub_user.push('</div>');
			sub_dept.push('</div>');
			var sub = $([
				'<li>',
					'<div class="level'+level+' clearfix tree-list">',
						sub_dept.join('') ,
						sub_user.join('') ,
					'</div>',
				'</li>'
			].join(''));
			$(sub).appendTo(parentHtml);
		}
	}
}

//showOnlinePerson.gotoMail = function(id ,url){
//	JCF.LoadPage({url:url});
//	$('#'+id).modal('hide');
//	$('#'+showOnlinePerson.showId).modal('hide');
//}
var UserInfo = Clazz.extend({
	construct:function(option){
		this.opt = option || {};
		this.index = parseInt(Math.random() * (1000 - 10 + 1) + 10);
		this._addEvent();
		this.jcf = window.JCF ? JCF : JCFF.getTop().JCF;
	},
	getById : function(id,deptId,subSystem){
		this.newmail = this.jcf.getMail() ? this.jcf.getMail().action : null;
		this.$body = $('body');
		var self = this;
		var url = getRootPath()+'/system/getUserById.action';
		if (subSystem) {
			url = getRootPath() + "/api/subDepartment/getUserById.action";
		}
		$.ajax({
			url : url,
			type : 'post',
			data: {id: id,deptId:deptId},
			success : function(data) {
				if(data){
					var html = self._getModalTemplate(data);
					self.$body.append(html);
					self.show();
				}
			}
		});
	},
	show : function(){
		$('#userinfo_'+this.index).modal('show');
		this._addEvent();
	},
	hide : function(flag,callback){
		var self = this;
		$('#userinfo_'+self.index).modal('hide');
		if(flag) $("#"+showOnlinePerson.showId).modal('hide');
		setTimeout(function(){
			$('#userinfo_'+self.index).remove();
			callback && callback.call(self);
		},300);
	},
	_infoTemplate : function(user){
		function getTemp(field){
			var u = user[field];
			return u ? u : '';
		}
		var temp = ['<tr><td class="w115">用户显示名</td><td>'+getTemp('displayName')+'</td></tr>'];
			temp.push('<tr><td>所在部门</td><td>'+getTemp('deptName')+'</td></tr>');
			temp.push('<tr><td>职务</td><td>'+getTemp('dutyIdValue')+'</td></tr>');
			temp.push('<tr><td>级别</td><td>'+getTemp('levelValue')+'</td></tr>');
			temp.push('<tr><td>用户性别</td><td>'+getTemp('sexValue')+'</td></tr>');
			temp.push('<tr><td>用户手机号码</td><td>'+getTemp('mobile')+'</td></tr>');
			temp.push('<tr><td>用户邮箱</td><td>'+getTemp('email')+'</td></tr>');
			temp.push('<tr><td>用户办公电话</td><td>'+getTemp('officeTel')+'</td></tr>');
			temp.push('<tr><td>直接领导</td><td>'+getTemp('leaderIdValue')+'</td></tr>');
		return temp.join('');
	},
	_getModalTemplate : function(user){
		var template = ['<div class="modal fade panel" id="userinfo_'+this.index+'" aria-hidden="false">'];
		template.push('<div class="modal-dialog">');
		template.push('<div class="modal-content">');
		template.push('<div class="modal-header">');
		template.push('<button type="button" class="close" id="userinfo_close'+this.index+'">×</button>');
		template.push('<h4 class="modal-title">用户基本信息</h4>');
		template.push('</div>');
		template.push('<div class="modal-body dis-table">');
		template.push('<section class="dis-table-cell" style="width:162px;">');
		template.push('<img src="'+(apiServerPath+'/'+ (user.photo ? user.photo : '/images/demoimg/userPhoto.png'))+'" width="147" height="177">');
		if(this.newmail){
			var mailUrl = this.newmail + '&receiverId='+user.id+'&receiverName='+user.displayName;
			template.push('<button class="btn" url="'+mailUrl+'" style="width:147px;" id="userinfobtn_'+this.index+'">发邮件</button>');
		}
		template.push('</section>');
		template.push('<section class="panel-tab-con dis-table-cell" style="padding-bottom:20px;">');
		template.push('<table class="basicMsg" id="pInfo"><tbody>');
		template.push(this._infoTemplate(user));
		template.push('</tbody></table>');
		return template.join('');
	},
	_addEvent : function(){
		var self = this;
		$(document).off('click.userinfo1').on('click.userinfo1', ' #userinfobtn_'+self.index, function(){
			var url = $(this).attr('url');
			self.hide(true,function(){
				this.jcf.LoadPage({url: url});
			});
		});
		$(document).off('click.userinfo2').on('click.userinfo2',' #userinfo_close'+self.index, function(){
			self.hide(false);
		});
	}
});

showOnlinePerson.myUserInfo = new UserInfo();
/**
 * 显示人员详细信息
 */
showOnlinePerson.showPersonInfo = function(userId,deptId,subSystem){
	showOnlinePerson.myUserInfo.getById(userId,deptId,subSystem);
}
