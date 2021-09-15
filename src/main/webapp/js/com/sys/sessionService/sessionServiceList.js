var sessionServiceList = {};
var isShowOnlinePerson = true;	//是否显示在线人员窗口
sessionServiceList.init = function() {
console.log("111");
    $.ajax({
        async : false,
        url : getRootPath()+"/sys/sessionService/sessionList.action?time="+(+new Date()),
        type : 'post',
        success : function(data) {
            var value = data[0];console.log(value);
            $("#userOnLineTree").html(sessionServiceList.showPage(value, "userOnLineTree"));
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
    }
}

/**
 * 显示人员
 * @param d 				部门人员数据
 * @param openPersonDivId	弹出层DIVID
 */
sessionServiceList.showPage = function(d, openPersonDivId){
    var lv1_dept = ['<div>'], lv1_user = ['<div style="width:auto">'];
    if(d != null){
        if(d.user != null && d.user.length > 0){
            lv1_dept.push('<label class="radio fl"><span class="fl w100">'+d.name+'</span></label>');
            var d_user_len = d.user.length;
            for(var l1=0;l1<d_user_len;l1++){
                var d_user_l1 = d.user[l1];
                lv1_user.push('<label class="checkbox inline">');
                lv1_user.push('<span>');
                lv1_user.push('<a href="#" onclick="sessionServiceList.deleteSessionService(\''+d_user_l1.id+'\');"><font color="#60AAE9">'+d_user_l1.displayName +'</font></a>');
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
        '<section class="tree-ul tree-scroll" style="height:auto!important;">',
        '<ul class="tree-horizontal clearfix">',
        '<li>',
        '<div class="level1 clearfix tree-list" style="display:block">',
        lv1_dept.join('') ,
        lv1_user.join('') ,
        '</div>',
        '<ul id="lv"></ul>',
        '</li>',
        '</ul>',
        '</section>'
    ].join(''));
    if(d != null && d.subDept != null && d.subDept.length > 0)
        sessionServiceList.recur(d.subDept, showlist.find("#lv"), 2, openPersonDivId);
    return showlist;
}

/**
 * 递归查询下级菜单
 * @param deptList			部门集合
 * @param parentHtml		上级html
 * @param level				level-css样式
 * @param openPersonDivId	弹出层DIVID
 */
sessionServiceList.recur = function(deptList, parentHtml, level, openPersonDivId){
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
                            main_user.push('<a href="#" onclick="sessionServiceList.deleteSessionService(\''+user_m.id+'\');"><span><font color="#60AAE9">'+user_m.displayName +'</font></span></a>');
                            main_user.push('</label>');
                        }
                    }
                }else{
                    var subDept_user_len = deptList_dept.subDept[0].user.length;
                    for(var m=0; m<subDept_user_len; m++){
                        var subDept_user_m = deptList_dept.subDept[0].user[m];
                        main_user.push('<label class="radio inline">');
                        main_user.push('<a href="#" onclick="sessionServiceList.deleteSessionService(\''+subDept_user_m.id+'\');"><span><font color="#60AAE9">'+subDept_user_m.displayName +'</font></span></a>');
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
                '<div class="level'+level+' clearfix tree-list" style="display:block;">',
                main_dept.join('') ,
                main_user.join('') ,
                '</div>',
                '<ul id="lv'+i+'"></ul>',
                '</li>'
            ].join(''));
            $(main_sub).appendTo(parentHtml);
            sessionServiceList.recur(deptList[i].subDept, $(main_sub).children().last(), level+1, openPersonDivId);
        } else {
            var sub_dept = ['<div>'], sub_user = ['<div>'];
            if(deptList_dept.user.length > 0){
                sub_dept.push('<label class="radio fl"><span class="fl w100">'+deptList_dept.name+'</span></label> ');
                var deptList_user_len = deptList_dept.user.length;
                for(var s=0;s<deptList_user_len;s++){
                    var deptList_user_s = deptList_dept.user[s];
                    sub_user.push('<label class="radio inline">');
                    sub_user.push('<a href="#" onclick="sessionServiceList.deleteSessionService(\''+deptList_user_s.id+'\');"><span><font color="#60AAE9">'+deptList_user_s.displayName +'</font></span></a>');
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

//删除对象
sessionServiceList.deleteSessionService = function (id) {
	var ids = String(id);
	if (id == 0) {
		var idsArr = [];
		$("[name='ids']:checked").each(function() {
			idsArr.push($(this).val());
		});
		ids = idsArr.join(",");
	}
	if (ids.length == 0) {
		msgBox.info({
			content: $.i18n.prop("JC_SYS_061")
		});
		return;
	}
	msgBox.confirm({
		content: $.i18n.prop("JC_SYS_160"),
		success: function(){
			sessionServiceList.deleteCallBack(ids);
		}
	});
};

//删除用户回调方法
sessionServiceList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/sys/sessionService/deleteByIds.action",
		data : {"ids": ids},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({
					type:"success",
					content: data.successMessage
				});
			} else {
				msgBox.info({
					content: data.errorMessage
				});
			}
            sessionServiceList.init();
		}
	});
};


$(document).ready(function(){
    ie8StylePatch();
    sessionServiceList.init();
});