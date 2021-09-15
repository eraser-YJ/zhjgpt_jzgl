/**
 * 	依赖:	JQuery 		控件
 * 			zTree 		控件
 * 			ChinesePY 	汉字转拼音
 * 			select2 	选择控件
 * 
 * 	控件中有两种组件:[1.组织控件、2.组织人员控件]分别支持多选与单选
 * 
 * 	选择控件调用方法:[
 * 		在相对应需要引入控件的页面引入依赖及其本控件的JS调用
 * 		selectControl.init = function(showControlDivId, controlId, isCheckOrRadio, isPersonOrOrg, orgOrDept, echoData, callback);
 * 		参数:[
 *			showControlDivId 	是要显示本控件的DivID		注释:页面多次调用控件传入的值不能重复
 *			controlId 			是本控件中文本框的id与name	注释:页面多次调用控件传入的值不能重复[*规则是"id-name",id不要重名]以便后台取值
 *			isCheckOrRadio		是多选还是单选				注释:true:多选,flase:单选
 *			isPersonOrOrg		是人员还是组织				注释:true:多选,flase:单选
 *			orgOrDept			机构/部门					注释:1:机构,0:部门,“”或者null全部
 *			echoData			是回显数据					注释:没有回显数据可以不填
 *			callback			回调方法					注释:没有不填，未实现
 * 		]
 * 	]
 *  清值:[重置按钮中调用selectControl.clearValue方法传入控件ID]
 */

selectControl = {};		//选择控件[人员、组织]主对象

selectControl.index = 0;//控件ID,Name下标标示

var orgPersonData = [],	//组织人员数据
personData = [],		//搜索框人员数据
orgData = [],			//全组织数据
zkAndSqCount = 0;		//展开收起计数

selectControl.person = {};			//人员对象
selectControl.person.url = getRootPath()+"/common/getAllDeptAndUser.action";//getRootPath()+"/department/searchUserList.action";	//人员数据URL

selectControl.org = {}; 			//组织对象
selectControl.org.url = getRootPath()+"/department/getOrgTree.action";			//组织数据URL

/**
 * 选择控件初始化
 * [后三位参数采用默认可以不填,不采用默认必须依次填写,最后一个参数是用来回显数据的,要是没有回显可以不填]
 * @param[*]代表必填项
 * 
 * @param[*] showControlDivId 	要显示控件的divId
 * @param[*] controlId 			要显示控件的controlId[一个页面上重复调用不能重名][规则是"id-name",id不要重名]
 * @param isCheckOrRadio		是单选还是多选[false:单选,true:多选][默认单选]
 * @param isPersonOrOrg			是人员还是组织[false:组织,true:人员][默认人员]
 * @param orgOrDept				机构\部门[1:组织,0:部门,""或者null:全部][默认全部]
 * @param echoData				回显数据[格式为JSON][默认undefined]
 * 								echoData 单选的{id:value,text:value} , 多选的[{id:value,text:value},{id:value,text:value}]
 * @param callback				未实现
 */
selectControl.init = function(showControlDivId, controlId, isCheckOrRadio, isPersonOrOrg, orgOrDept, echoData, isReadonly, callback) {
	var x = 1000, y = 10;
    this.rand = parseInt(Math.random() * (x - y + 1) + y);
	this.index = parseInt(selectControl.index) + 1;//控件下标	
	if(isPersonOrOrg){	//人员控件
		var $showControlDivId_ = $("#" + showControlDivId);
		$showControlDivId_.empty();
		
		var openPersonDivId = "openPersonDiv"+ this.rand,	//人员层DivId
			personBtnId = "openPersonBtn" + this.rand,		//人员按钮ID
			selectBackValueId = "backValue" + this.rand,	//select下拉控件ID
			allPersonBtnId = "allPersonBtn" + this.rand,	//全选按钮ID
			okPersonBtnId = "okPersonBtn" + this.rand,		//确认按钮ID
			searchBtnId = "searchBtn" + this.rand,
			searchInputId = "searchInput" + this.rand,
			onLineId = "onLine" + this.rand,
			onDeptId = "onDept" + this.rand,
			onPostId = "onPost" + this.rand,
			onPersonGroupId = "onPersonGroup" + this.rand,
			onPublicGroupId = "onPublicGroupId" + this.rand,
			clearBtnId = "clearBtn" + this.rand;
		/**
		 * 输入框与选择按钮界面
		 */
		var personMainHtmlCheck = ['<div class="select2-wrap input-group w-p100">'];
		personMainHtmlCheck.push('<div class="fl w-p100">');
		personMainHtmlCheck.push('<input type="hidden" id="'+controlId+'" name="'+controlId.split("-")[1]+'" style="width:100%"/></div>');
		
		if(isReadonly != undefined || isReadonly != null){
			personMainHtmlCheck.push('<a class="btn btn-file no-all input-group-btn" style="cursor:default">');
		}else{
			personMainHtmlCheck.push('<a class="btn btn-file no-all input-group-btn" href="#" id="'+personBtnId+'">');
		}
			
		if(isCheckOrRadio){
			personMainHtmlCheck.push('<i class="fa fa-users"></i>');
		}else{
			personMainHtmlCheck.push('<i class="fa fa-user"></i>');
		}
		
		personMainHtmlCheck.push('</a>');
		personMainHtmlCheck.push('<div class="input-group-btn m-l-xs selection-tree-btn fr">');
		personMainHtmlCheck.push('<a class="a-icon i-trash fr m-b" href="#" id="'+clearBtnId+'"><i class="fa fa-trash"></i>清空</a>');
		personMainHtmlCheck.push('<a class="a-icon i-new zk fr" href="#"><i class="fa fa-chevron-down"></i>展开</a>');
		personMainHtmlCheck.push('<a class="a-icon i-new sq fr" href="#"><i class="fa fa-chevron-up"></i>收起</a></div>');
		personMainHtmlCheck.push('</div><label class="help-block hide"></label>'); 
		
		var personMainHtmlRadio = ['<div class="select2-wrap input-group w-p100">'];
		personMainHtmlRadio.push('<input type="hidden" id="'+controlId+'" name="'+controlId.split("-")[1]+'" style="width:100%"/>');
		
		if(isReadonly != undefined || isReadonly != null){
			personMainHtmlRadio.push('<a class="btn btn-file no-all input-group-btn" style="cursor:default">');
		}else{
			personMainHtmlRadio.push('<a class="btn btn-file no-all input-group-btn" href="#" id="'+personBtnId+'">');
		}
		
		if(isCheckOrRadio){
			personMainHtmlRadio.push('<i class="fa fa-users"></i>');
		}else{
			personMainHtmlRadio.push('<i class="fa fa-user"></i>');
		}
		
		personMainHtmlRadio.push('</a></div><label class="help-block hide"></label>');
		
		var personHtml = ['<div class="modal fade" id="'+openPersonDivId+'" aria-hidden="false" openPersonNum="'+this.rand+'">'];
		personHtml.push('<div class="modal-dialog w1100 modal-tree">');
			personHtml.push('<div class="modal-content">');
				personHtml.push('<div class="modal-header clearfix">');
					personHtml.push('<button type="button" class="close" data-dismiss="modal" onclick="selectControl.personClose(\''+openPersonDivId+'\',\''+searchInputId+'\');">×</button>');
						personHtml.push('<h4 class="modal-title fl">人员选择</h4>');
						personHtml.push('<div class="fl btn-group form-btn m-l-lg" data-toggle="buttons">');
								personHtml.push('<button type="button" name="options" class="btn m-r-sm" id="'+onLineId+'"> 在线人员</button>');
								personHtml.push('<button type="button" name="options" class="btn m-r-sm dark" id="'+onDeptId+'"> 根据组织</button>');
								personHtml.push('<button type="button" name="options" class="btn m-r-sm" id="'+onPostId+'"> 根据职务</button>');
								personHtml.push('<button type="button" name="options" class="btn m-r-sm" id="'+onPersonGroupId+'"> 个人组别</button>');
								personHtml.push('<button type="button" name="options" class="btn m-r-sm" id="'+onPublicGroupId+'"> 公共组别</button>');
						personHtml.push('</div>');
						personHtml.push('<form role="search" class="fr input-append m-b-none m-r-lg">');
								personHtml.push('<span class="add-on">按姓名</span> ');
								personHtml.push('<input id="'+searchInputId+'" class="form-control m-r-sm" onKeydown="selectControl.searchKeydown();">');
								personHtml.push('<button type="button" class="btn" id="'+searchBtnId+'" onclick="selectControl.search(\''+searchInputId+'\',\''+openPersonDivId+'\');">');
								personHtml.push('<i class="fa fa-search"></i>');
								personHtml.push('</button>');
						personHtml.push('</form>');
					personHtml.push('</div>');
					personHtml.push('<div class="modal-body clearfix" id="modal_'+openPersonDivId+'"></div>');	//显示人员列表
					personHtml.push('<div class="modal-footer form-btn">');
					personHtml.push('<button id="'+okPersonBtnId+'" class="btn dark" type="button">确 定</button>');
					//personHtml.push('<button id="'+okPersonBtnId+'" class="btn dark" type="button">确 定</button>');
						if(isCheckOrRadio){
							personHtml.push('<button id="'+allPersonBtnId+'" class="btn" type="button" onClick="selectControl.allCheckbox(\''+selectBackValueId+'\',\''+openPersonDivId+'\');">全 选</button>');
						}
							personHtml.push('<button id="close" class="btn" type="button" onClick="selectControl.personClose(\''+openPersonDivId+'\',\''+searchInputId+'\');">取 消</button>');
					personHtml.push('</div>');
				personHtml.push('</div>');
			personHtml.push('</div>');
		personHtml.push('</div>');;
		
		if(isCheckOrRadio){
			$showControlDivId_.append(personMainHtmlCheck.join(''));//添加控件到外层DIV
		}else{
			$showControlDivId_.append(personMainHtmlRadio.join(''));//添加控件到外层DIV
		}
		
		selectControl.mainInputDataInit(isCheckOrRadio, controlId, personBtnId, openPersonDivId, selectBackValueId, onDeptId);
		
		$(personHtml.join('')).appendTo("body");
		
		$("#"+okPersonBtnId).on("click", function(){
			selectControl.showPersonValue(searchInputId, controlId, isCheckOrRadio, openPersonDivId, selectBackValueId, callback);
		});		
		
		$(document).on("shfitUser",function(e){
			var that = e.target;
			switch (that.id) {
			case onLineId:
				controllShfitUser($(that),"onLine");
				break;
			case onDeptId:
				controllShfitUser($(that),"dept");
				break;
			case onPostId:
				controllShfitUser($(that),"post");
				break;
			case onPersonGroupId:
				controllShfitUser($(that),"personGroup",undefined);
				break;
			case onPublicGroupId:
				controllShfitUser($(that),"publicGroup",undefined);
				break;
			default:
				break;
			}
		});
		
		function controllShfitUser(obj,id,pam){
			$("#"+searchInputId).val("");
			if(selectControl.checkSelectIsNull(openPersonDivId, selectBackValueId)){
				msgBox.confirm({
					content: "确定放弃当前所选择的人员？", 
					type: 'fail', 
					success: function(){
						$("button[name='options']").removeClass("dark");
						obj.addClass("dark");
						selectControl[id].call(this,openPersonDivId, selectBackValueId, isCheckOrRadio, echoData, controlId,pam);
					}
				});
			}else{
				$("button[name='options']").removeClass("dark");
				obj.addClass("dark");
				selectControl[id].call(this,openPersonDivId, selectBackValueId, isCheckOrRadio, echoData, controlId,pam);
			}
		}
		
		$("#"+onLineId).bind("click", function(){$(this).trigger("shfitUser");});
		$("#"+onDeptId).bind("click", function(){$(this).trigger("shfitUser");});
		$("#"+onPostId).bind("click", function(){$(this).trigger("shfitUser");});
		$("#"+onPersonGroupId).bind("click", function(){$(this).trigger("shfitUser");});
		$("#"+onPublicGroupId).bind("click", function(){$(this).trigger("shfitUser");});
		
		$("#"+ controlId).bind("change", function(e){
			if(e.added){
				selectControl.openPutAwayClear(controlId);
			}
			if(e.removed){
				selectControl.openPutAwayClear(controlId);
			}
		});
		
		$("#"+clearBtnId).bind("click", function(){
			selectControl.clearValue(controlId);
			selectControl.openPutAwayClear(controlId);
			if(typeof resetPostscript == 'function'){
				resetPostscript();
			}
		});
		
		$("#"+controlId).on("select2-focus", function(e){
			if(personData.length == 0){
				//查询人员数据用于搜索
				$.ajax({
					async: false,
					url : selectControl.person.url,
					type : 'post',
					success : function(data) {
						orgPersonData = eval(data)[0];
						if(orgPersonData != null){
							if(orgPersonData.subDept != null && orgPersonData.subDept.length > 0){
								$.each(orgPersonData.subDept, function(i, o) {
									if(o.user.length > 0){
										for(var i=0;i<o.user.length;i++){
											personData.push({
												id : o.user[i].id,							//ID
												text : o.user[i].displayName,				//显示的内容
												otherDeptFlag:o.user[i].otherDeptFlag,
												orderNo: o.user[i].orderNo,
												jp: Pinyin.GetJP(o.user[i].displayName)		//汉字的简拼
											});
										}
										if(o.subDept != null && o.subDept.length > 0){
											selectControl.getAllUser(o.subDept, personData);
										}
									}else{
										if(o.subDept != null && o.subDept.length > 0){
											selectControl.getAllUser(o.subDept, personData);
										}
									}
								});
							}
						}
					},
					error: function(){
						msgBox.tip({content: "获取人员失败", type:'fail'});
					}
				});
				
				
			}
		});
		
		select2InitToPerson(personData, controlId, isCheckOrRadio, echoData, callback);
		
		if(echoData != null || echoData != undefined){
			select2InitEchoToPerson(controlId, echoData, openPersonDivId, selectBackValueId);
		}
	}else{
		
		var $showControlDivId_org = $("#" + showControlDivId);
		$showControlDivId_org.empty(); 
		
		//组织控件
		var treeId    = "tree"   + this.index,	//DivID
		myTreeId  = "myTree" + this.index,		//树控件ID
		orgbtnId  = "orgbtn" + this.index,		//组织按钮ID
		openBtnId = "open"   + this.index,		//打开按钮ID
		clearBtnId = "clearBtn" + this.index;	
		/**
		 * 组织树界面
		 */
		var orgHtml = ['<div class="modal fade" id="'+treeId+'" aria-hidden="false">'];
			orgHtml.push('<div class="modal-dialog">');
				orgHtml.push('<div class="modal-content">');
					orgHtml.push('<div class="modal-header">');
						orgHtml.push('<button type="button" class="close" data-dismiss="modal">×</button>');
							orgHtml.push('<h4 class="modal-title">选择</h4>');
								orgHtml.push('</div>');
									orgHtml.push('<div class="modal-body">');
										orgHtml.push('<div id="'+myTreeId+'" class="ztree"></div>');
									orgHtml.push('</div>');
							orgHtml.push('<div class="modal-footer no-all form-btn">');
									orgHtml.push('<button class="btn dark" type="button" onClick="showOrgValue(\''+myTreeId+'\',\''+controlId+'\',\''+treeId+'\','+isCheckOrRadio+')" id="'+orgbtnId+'">确 定</button>');
									orgHtml.push('<button class="btn" type="reset" id="close" onClick="selectControl.org.close(\''+treeId+'\',\''+controlId+'\');">取 消</button>');
							orgHtml.push('</div>');
					orgHtml.push('</div>');
			orgHtml.push('</div>');
		orgHtml.push('</div>');
		/**
		 * 输入框与选择按钮界面[组织控件主界面]
		 */
		var mainHtmlCheck = ['<div class="select2-wrap input-group w-p100">'];
		mainHtmlCheck.push('<div class="fl w-p100">');	//btn-in-area 
		mainHtmlCheck.push('<input type="hidden" id="'+controlId+'" name="'+controlId.split("-")[1]+'" search="search" style="width:100%"/></div>');
		mainHtmlCheck.push('<a class="btn btn-file no-all input-group-btn" href="#" onClick="selectControl.org.open('+isCheckOrRadio+',\''+treeId+'\',\''+myTreeId+'\',\''+controlId+'\',\''+orgOrDept+'\');" id="'+openBtnId+'" role="button"');
		mainHtmlCheck.push('data-toggle="modal"><i class="fa fa-group"></i></a>');
		mainHtmlCheck.push('<div class="input-group-btn m-l-xs selection-tree-btn fr">');
		mainHtmlCheck.push('<a class="a-icon i-trash fr m-b" href="#" id="'+clearBtnId+'"><i class="fa fa-trash"></i>清空</a>');
		mainHtmlCheck.push('<a class="a-icon i-new zk fr" href="#"><i class="fa fa-chevron-down"></i>展开</a>');
		mainHtmlCheck.push('<a class="a-icon i-new sq fr" href="#"><i class="fa fa-chevron-up"></i>收起</a></div>');
		mainHtmlCheck.push('</div><label class="help-block hide"></label>');
		
		var mainHtmlRadio = ['<div class="select2-wrap input-group  w-p100">'];
		mainHtmlRadio.push('<input type="hidden" id="'+controlId+'" name="'+controlId.split("-")[1]+'" search="search" style="width:100%"/>');
		mainHtmlRadio.push('<a class="btn btn-file no-all input-group-btn" href="#"');
		mainHtmlRadio.push('onClick="selectControl.org.open('+isCheckOrRadio+',\''+treeId+'\',\''+myTreeId+'\',\''+controlId+'\',\''+orgOrDept+'\');" id="'+openBtnId+'" role="button"');
		mainHtmlRadio.push('data-toggle="modal"><i class="fa fa-group"></i></a>');
		mainHtmlRadio.push('</div><label class="help-block hide"></label>');
		
		if(isCheckOrRadio){
			$showControlDivId_org.append(mainHtmlCheck.join(''));//添加控件到外层DIV
		}else{
			$showControlDivId_org.append(mainHtmlRadio.join(''));//添加控件到外层DIV
		}
		
		$(orgHtml.join('')).appendTo("body");
		
		//查询组织数据用于搜索
		if(orgData.length == 0){
			$.ajax({
				async: false,
				url : selectControl.org.url,
				type : 'post',
				success : function(data) {
					$.each(data, function(i, o) {	
						orgData[i] = {
							id : o.id,						//ID
							text : o.name,					//显示的内容
							queue: o.queue,					//排序
							parentId: o.parentId,			//组织父节点
							deptType: o.deptType,			//组织类型
							isChecked: o.isChecked,
							jp: Pinyin.GetJP(o.name)		//汉字的简拼
						};
					});
					select2InitToOrg(orgData, controlId, isCheckOrRadio, orgOrDept,callback);
					if(echoData != undefined || echoData != null || echoData != "")
						select2InitEchoToOrg(controlId, echoData);
				},
				error: function(){
					msgBox.tip({content: "获取组织失败", type:'fail'});
				}
			});
		}else{
			select2InitToOrg(orgData, controlId, isCheckOrRadio, orgOrDept,callback);
			if(echoData != undefined || echoData != null || echoData != "")
				select2InitEchoToOrg(controlId, echoData);
		}
		$("#"+ controlId).bind("change", function(e){
			if(e.added){
				selectControl.openPutAwayClear(controlId);
				selectControl.setFocus(controlId);
			}
			if(e.removed){
				selectControl.openPutAwayClear(controlId);
				selectControl.setFocus(controlId);
			}
		});
		$("#"+clearBtnId).bind("click", function(){
			zkAndSqCount = 0;
			selectControl.clearValue(controlId);
			selectControl.openPutAwayClear(controlId);
		});
	}
};

/**
 * 初始化select2与按钮
 */
selectControl.mainInputDataInit = function(isCheckOrRadio, controlId, personBtnId, openPersonDivId, selectBackValueId, onDeptId){
	//$("#"+openPersonDivId).find(".modal-body").empty();
	$("#"+personBtnId).bind("click", function(){
		if(personData.length == 0){
			//查询人员数据用于搜索
			$.ajax({
				async: false,
				url : selectControl.person.url,
				type : 'post',
				success : function(data) {
					if(data){
						orgPersonData = JSON.parse(data)[0];
					if(orgPersonData != null){
							if(orgPersonData.subDept != null && orgPersonData.subDept.length > 0){
								$.each(orgPersonData.subDept, function(i, o) {
									if(o.user && o.user.length > 0){
										for(var i=0;i<o.user.length;i++){
											personData.push({
												id : o.user[i].id,							//ID
												text : o.user[i].displayName,				//显示的内容
												orderNo: o.user[i].orderNo,
												jp: Pinyin.GetJP(o.user[i].displayName),	//汉字的简拼
												otherDeptFlag:o.user[i].otherDeptFlag
											});
										}
										if(o.subDept != null && o.subDept.length > 0){
											selectControl.getAllUser(o.subDept, personData);
										}
									}else{
										if(o.subDept != null && o.subDept.length > 0){
											selectControl.getAllUser(o.subDept, personData);
										}
									}
								});
							}
						}
					}
				},
				error: function(){
					msgBox.tip({content: "获取人员失败", type:'fail'});
				}
			});
		}
		selectControl.openPerson(isCheckOrRadio, openPersonDivId, selectBackValueId, controlId, onDeptId);
	});
	$("#"+ controlId).bind("change", function(e){
		selectControl.openPutAwayClear(controlId);
	});
}

/**
 * 根据在线人员
 */
selectControl.onLine = function(openPersonDivId, selectBackValueId, isCheckOrRadio, echoData, controlId){
	allcheckboxFlag = true;
	//$("#"+openPersonDivId).find(".modal-body").empty();
	var url = getRootPath()+"/department/getDeptAndUserByOnLine.action";
	$.ajax({
		async : false,
		url : url,
		type : 'post',
		success : function(data) {
			$("#modal_"+openPersonDivId).html(selectControl.showOnlineUserPage(eval(data)[0], selectBackValueId, openPersonDivId, isCheckOrRadio));
				if(controlId != null || controlId != undefined)
					selectControl.switching(openPersonDivId, selectBackValueId, isCheckOrRadio, $("#"+controlId).select2("data"));
				else
					selectControl.switching(openPersonDivId, selectBackValueId, isCheckOrRadio, echoData);
		},
		error: function(){
			msgBox.tip({content: "获取在线人员失败", type:'fail'});
		}
	});
}

/**
 * 根据组织
 */

selectControl.dept = function(openPersonDivId, selectBackValueId, isCheckOrRadio, echoData, controlId){
	allcheckboxFlag = true;
	selectControl.openPutAwayClear(controlId);
	//1005
	$("#modal_"+openPersonDivId).html(selectControl.showDeptUserPage(orgPersonData, selectBackValueId, openPersonDivId, isCheckOrRadio));
	if(!!controlId){
		var data = $("#"+controlId).select2("data");
		selectControl.switching(openPersonDivId, selectBackValueId, isCheckOrRadio, data);
	}else{
		selectControl.switching(openPersonDivId, selectBackValueId, isCheckOrRadio, echoData);
	}
}

/**
 * 根据职务
 */
selectControl.post = function(openPersonDivId, selectBackValueId, isCheckOrRadio, echoData, controlId, searchInputId){
	allcheckboxFlag = true;
	var url = getRootPath()+"/department/getPostAndUser.action";
	$.ajax({
		async : false,
		url : url,
		type : 'post',
		success : function(data) {
			$("#modal_"+openPersonDivId).html(selectControl.showUserPage(JSON.parse(data), selectBackValueId, openPersonDivId, isCheckOrRadio));
			if(controlId != null || controlId != undefined){
				selectControl.switching(openPersonDivId, selectBackValueId, isCheckOrRadio, $("#"+controlId).select2("data"));
			}else{
				selectControl.switching(openPersonDivId, selectBackValueId, isCheckOrRadio, echoData);
			}
		},
		error: function(){
			msgBox.tip({content: "获取在线人员失败", type:'fail'});
		}
	});
}

/**
 * 个人组别
 */
selectControl.personGroup = function(openPersonDivId, selectBackValueId, isCheckOrRadio, echoData, controlId, isOpenPerson){
	allcheckboxFlag = true;
	var url = getRootPath()+"/department/getPersonGroupAndUser.action";
	$.ajax({
		async : false,
		url : url,
		type : 'post',
		success : function(data) {
			$("#modal_"+openPersonDivId).html(selectControl.showGroupPage(eval(data), selectBackValueId, openPersonDivId, isCheckOrRadio));
			if(controlId != null || controlId != undefined){
				if(isOpenPerson != undefined){
					selectControl.switching(openPersonDivId, selectBackValueId, isCheckOrRadio, echoData);
				}else{
					selectControl.switching(openPersonDivId, selectBackValueId, isCheckOrRadio, $("#"+controlId).select2("data"));
				}
			}else{
				selectControl.switching(openPersonDivId, selectBackValueId, isCheckOrRadio, echoData);
			}
			$("#"+openPersonDivId+" select[name='"+selectBackValueId+"']").each(function(i,n){
		        var options = "";
		        $(n).find("option").each(function(j,m){
		            if(options.indexOf($(m)[0].outerHTML) == -1){
		                options += $(m)[0].outerHTML;
		            }
		        });
		        $(n).html(options);
		    });
		},
		error: function(){
			msgBox.tip({content: "获取个人组别失败", type:'fail'});
		}
	});
}

/**
 * 公共组别
 */
selectControl.publicGroup = function(openPersonDivId, selectBackValueId, isCheckOrRadio, echoData, controlId, isOpenPerson){
	allcheckboxFlag = true;
	var url = getRootPath()+"/department/getPublicGroupAndUser.action";
	$.ajax({
		async : false,
		url : url,
		type : 'post',
		success : function(data) {
			$("#modal_"+openPersonDivId).html(selectControl.showGroupPage(eval(data), selectBackValueId, openPersonDivId, isCheckOrRadio));
			if(controlId != null || controlId != undefined){
				if(isOpenPerson != undefined){
					selectControl.switching(openPersonDivId, selectBackValueId, isCheckOrRadio, echoData);
				}else{
					selectControl.switching(openPersonDivId, selectBackValueId, isCheckOrRadio, $("#"+controlId).select2("data"));
				}
			}else{
				selectControl.switching(openPersonDivId, selectBackValueId, isCheckOrRadio, echoData);
			}
			$("#"+openPersonDivId+" select[name='"+selectBackValueId+"']").each(function(i,n){
		        var options = "";
		        $(n).find("option").each(function(j,m){
		            if(options.indexOf($(m)[0].outerHTML) == -1){
		                options += $(m)[0].outerHTML;
		            }
		        });
		        $(n).html(options);
		    });
		},
		error: function(){
			msgBox.tip({content: "获取公共组别失败", type:'fail'});
		}
	});
}

/**
 * 取消回车搜索
 */
selectControl.searchKeydown = function(){
	var ev = event || window.event;
    if(ev.keyCode == 13) {
    	ev.keyCode = 0;
    	ev.returnValue = false;
    }
}
/**
 * 搜索人员定位
 */
selectControl.getContainer = function(){
	
}
/**
 * 设置已搜索到人员位置
 */
selectControl.setSearchPosition = function(parent,obj){
	if(selectControl.searchFalg){
        parent.scrollTop(Math.abs(obj.offset().top + parent.scrollTop() - parent.offset().top)-100);
        selectControl.searchFalg = false;
    }
}
//用于判断当搜索到多个对象时只显示第一个
selectControl.searchFalg = false;
//用于装填搜索到的结果，以便再次搜索是重置上一次搜索结果加重颜色
selectControl.reslutArys = [];
/**
 * 搜索人员
 */
selectControl.search = function(searchInputId, openPersonDivId){
	var siv = $("#"+searchInputId).val(),
	 	nameDatas = $("#"+openPersonDivId+" span:not([class])"),
	 	container = $("#treeSearchContainer");
		selectControl.searchFalg = true;
	var len = selectControl.reslutArys.length;
	if(len){
		for(var i = 0; i < len; i++){
			selectControl.reslutArys[i].style.color = "#555";
		}
		selectControl.reslutArys = [];
	}
	if(!!siv){
		for (var i = 0; i < nameDatas.length; i++) {
	          var thay = nameDatas[i],
	              text = thay.innerText;
	              if(text.indexOf(siv) != -1){
	            	  selectControl.reslutArys.push(thay); 
	            	  selectControl.setSearchPosition(container,$(thay));
	            	  thay.style.color = "#F4A642";
	              }
	    }; 
	}
	//selectControl.showOnLine(openPersonDivId, searchInputId);
}

/**
 * 显示在线人员
 */
selectControl.showOnLine = function(openPersonDivId, searchInputId){
//	var searchKey = $("#"+searchInputId).val();
	$.ajax({
		async: false,
		url : getRootPath()+"/system/getOnlineUsers.action",
		type : 'post',
		success : function(data) {
			var dateLen = data.length;
			for(var i = 0;i < dateLen;i++){
				var temp = data[i].deptId;
				if(!!temp){
					var nameDatas = $("#"+openPersonDivId+" input[name='dept"+temp+"']");
					for(var j = 0;j < nameDatas.length;j++){
						var thay = nameDatas[j],
		                text = thay.innerText;
						if(text.indexOf(data[i].displayName) != -1)thay.style.color = "#60AAE9";
					}
				}
			}
		},
		error: function(){
			msgBox.tip({content: "获取在线人员失败", type:'fail'});
		}
	});
}

/**
 * 关闭人员选择树
 */
selectControl.personClose = function(openPersonDivId, searchInputId, isOP){
	$("#"+searchInputId).val("");
	allcheckboxFlag = true;
	$("#"+openPersonDivId).modal("hide");
	if(isOP != null || isOP != undefined)
		selectControl.isOpenSinglePerson = true;
}

/**
 * 打开人员界面
 */
selectControl.openPerson = function(isCheckOrRadio, openPersonDivId, selectBackValueId, controlId, onDeptId){
	$("button[name='options']").removeClass("dark");
	$("#"+onDeptId).addClass("dark");
	var inputData = $("#"+controlId).select2("data");
	selectControl.dept(openPersonDivId, selectBackValueId, isCheckOrRadio, inputData);
	zkAndSqCount = 0;
	$("#"+openPersonDivId).modal("show");
}

/**
 * 按钮切换调用
 */
selectControl.switching = function(openPersonDivId, selectBackValueId, isCheckOrRadio, eData){
	if(!!eData){
		var openDiv = $("#"+openPersonDivId),
			sBack  = $("#"+selectBackValueId);
		if(isCheckOrRadio){
			//获取所有的input标签对象,取出所有的checkbox
			var inputBoxs = openDiv.find(":checkbox"),
				allBoxs	  = openDiv.find("input[name='depts']"),
				cbValue = [],
				cdArray = [];
			openDiv.find("input:checkbox").attr("checked", false);
			selectControl.clearAllUser(selectBackValueId);
			//根据回显数据勾选相应的checkbox以及select框中的赋值
			for(var i = 0;i < eData.length;i ++){
				var docDom = openDiv.find("[dept='otherDept"+eData[i].id+"']");
				for(var j = 0; j < docDom.length; j++){
					var dom = docDom[j];
					if(dom && dom.value){
						dom.checked = true;
						cbValue = dom.value.split(",");
						if(!cdArray[cbValue[0]]){
							cdArray[cbValue[0]] = cbValue[1];
							sBack.append("<option dept='Dept"+cbValue[0]+"' value='"+cbValue[0]+","+cbValue[2]+"'>"+cbValue[1]+"</option>");
						}
					}
				}
			}
			//根据每组中的checkbox选中情况来勾选全选checkbox
			for(var i = 0; i < allBoxs.length; i++){
				k = 0;
				var boxId = allBoxs[i].id,
					as = openDiv.find("input[name='"+boxId+"']");
				for(var j = 0;j < as.length;j ++){
					if(as[j].checked) k++;
				}
				if(k == as.length) allBoxs[i].checked = true;
			}
		}else{
			//如果是radio
			openDiv.find(":radio").attr("checked", false);
			var selectUsers = openDiv.find("input[name='selectUser']"),
				len = selectUsers.length,
				i	= 0;
			for(; i < len; i++){
				var vals = selectUsers[i].value.split(","),
					val   = vals[0];
				if(val == eData.id){
					sBack.find("option[value='"+val+"']").remove();
					sBack.append("<option value='"+val+"'>"+vals[1]+"</option>");
					$(selectUsers[i]).click();
				}
			}
		}
	}
	//人员界面收缩事件
	$(".tree-list .tree-btn").on('click',function (e) {
        e.preventDefault();
        e.stopPropagation();
        $(this).find("i").toggleClass("fa-chevron-down").end().closest(".tree-list").next().slideToggle();
    });
}
selectControl.isOpenSinglePerson = true; 	//判断弹出窗口是否开启

/**
 * 弹出人员列表[文本框、按钮、超链接]
 * @param controlId			控件ID
 * @param isCheckOrRadio	单选还是多选[false:单选,true:多选]
 * @param callbackFunction	回调函数
 * @param echoData			回显数据
 * @param isBlankCheck		是否进行为空检查
 */
selectControl.singlePerson = function(controlId, isCheckOrRadio, callbackFunction, echoData, isBlankCheck,closeCall) {
	var isInitModalDiv = $("#openPersonDiv"+ this.index);
	if(!isInitModalDiv.length) {
		this.index = parseInt(selectControl.index) + 1;
		var openPersonDivId   = "openPersonDiv"+ this.index,//人员层DivId
			selectBackValueId = "backValue" + this.index,	//select下拉控件ID
			allPersonBtnId = "allPersonBtn" + this.index,	//全选按钮ID
			okPersonBtnId = "okPersonBtn" + this.index,		//确认按钮ID
			searchInputId = "searchInput" + this.index,
			onLineId = "onLine" + this.index,
			onDeptId = "onDept" + this.index,
			onPostId = "onPost" + this.index,
			onPersonGroupId = "onPersonGroup" + this.index,
			onPublicGroupId = "onPublicGroupId" + this.index,
			personHtml = '<div class="modal fade" id="'+openPersonDivId+'" aria-hidden="false">'+
			 	'<div class="modal-dialog w1100 modal-tree">'+
			 '<div class="modal-content">'+
				 '<div class="modal-header clearfix">'+
				 '<button type="button" class="close close_'+openPersonDivId+'" data-dismiss="modal" onclick="selectControl.singleClose(\''+openPersonDivId+'\',\''+searchInputId+'\',true);">×</button>'+
				 '<h4 class="modal-title fl">人员选择</h4>'+
					 '<div class="fl btn-group form-btn m-l-lg" data-toggle="buttons">'+
						 '<button type="button" name="options" class="btn m-r-sm" id="'+onDeptId+'"> 根据组织</button>'+
					 '</div>'+
				 '<form role="search" class="fr input-append m-b-none m-r-lg">'+
						 '<span class="add-on">按姓名</span> '+
						 '<input id="'+searchInputId+'" class="form-control m-r-sm" onKeydown="selectControl.searchKeydown();">'+
						 '<button type="button" class="btn" onclick="selectControl.search(\''+searchInputId+'\',\''+openPersonDivId+'\');">'+
							 '<i class="fa fa-search"></i>'+
						 '</button>'+
				 '</form>'+
				 '</div>'+
				 '<div class="modal-body clearfix" id="modal_'+openPersonDivId+'"></div>'+	//显示人员列表
				 '<div class="modal-footer form-btn">'+
						 '<button id="'+okPersonBtnId+'" class="btn dark" type="button" onClick="selectControl.call(\''+searchInputId+'\',\''+isCheckOrRadio+'\',\''+openPersonDivId+'\',\''+selectBackValueId+'\',\''+controlId+'\','+callbackFunction+','+isBlankCheck+');">确 定</button>'+
						((isCheckOrRadio)?'<button id="'+allPersonBtnId+'" class="btn" type="button" onClick="selectControl.allCheckbox(\''+selectBackValueId+'\',\''+openPersonDivId+'\');">全 选</button>':"")+
						 '<button id="close" class="btn close_'+openPersonDivId+'" type="button">清空</button>'+
							 '<button id="close" class="btn" type="button" onClick="selectControl.personClose(\''+openPersonDivId+'\',\''+searchInputId+'\',true);">取 消</button>'+
				 '</div>'+
			   '</div>'+
			 '</div>'+
		 '</div>';
		$("body").append(personHtml);
	
		$(document).on("shfitUser",function(e){
			var that = e.target;
			switch (that.id) {
			case onLineId:
				controllShfitUser($(that),"onLine");
				break;
			case onDeptId:
				controllShfitUser($(that),"dept");
				break;
			case onPostId:
				controllShfitUser($(that),"post");
				break;
			case onPersonGroupId:
				controllShfitUser($(that),"personGroup",true);
				break;
			case onPublicGroupId:
				controllShfitUser($(that),"publicGroup",true);
				break;
			default:
				break;
			}
		});
		

		$('.close_'+openPersonDivId).on("click", function(){
			selectControl.personClose(openPersonDivId,searchInputId,true);
			closeCall();
		});
		
		function controllShfitUser(obj,id,pam){
			$("#"+searchInputId).val("");
			if(selectControl.checkSelectIsNull(openPersonDivId, selectBackValueId)){
				msgBox.confirm({
					content: "确定放弃当前所选择的人员？", 
					type: 'fail', 
					success: function(){
						selectControl.clearAllUser(selectBackValueId);
						selectControl[id].call(this,openPersonDivId, selectBackValueId, isCheckOrRadio, echoData, controlId,pam);
					}
				});
			}else{
				selectControl[id].call(this,openPersonDivId, selectBackValueId, isCheckOrRadio, echoData, controlId,pam);
			}
			$("button[name='options']").removeClass("dark");
			obj.addClass("dark");
		}
		
		$("#"+onLineId).bind("click", function(){$(this).trigger("shfitUser");});
		$("#"+onDeptId).bind("click", function(){$(this).trigger("shfitUser");});
		$("#"+onPostId).bind("click", function(){$(this).trigger("shfitUser");});
		$("#"+onPersonGroupId).bind("click", function(){$(this).trigger("shfitUser");});
		$("#"+onPublicGroupId).bind("click", function(){$(this).trigger("shfitUser");});
		
		if(orgPersonData && orgPersonData.length == 0){
			$.ajax({
				async: false,
				url : selectControl.person.url,
				type : 'post',
				success : function(data) {
					orgPersonData = eval(data)[0];
				},
				error: function(){
					msgBox.tip({content: "获取人员失败", type:'fail'});
				}
			});
		}
	}
	
	if(selectControl.isOpenSinglePerson){
		if(!isInitModalDiv.length){
			$("#modal_"+openPersonDivId).html(selectControl.showDeptUserPage(orgPersonData, selectBackValueId, openPersonDivId, isCheckOrRadio));
		}
		selectControl.singleOpenPerson("openPersonDiv"+ this.index, "backValue" + this.index, isCheckOrRadio, echoData, "searchInput" + this.index);
		selectControl.isOpenSinglePerson = false;
	}
}

/**
 * 回调方法
 * @param isCheckOrRadio 	是多选还是单选
 * @param openPersonDivId 	打开人员DIVID
 * @param selectBackValueId 选择框ID
 * @param controlId 		控件ID
 * @param cbf				回调函数
 * @param isBlankCheck		是否弹出确认
 */
selectControl.call = function(searchInputId, isCheckOrRadio, openPersonDivId, selectBackValueId, controlId, cbf, isBlankCheck){
	var data = selectControl.showPersonValue(searchInputId, controlId, isCheckOrRadio, openPersonDivId, selectBackValueId, cbf, isBlankCheck, true);
	if(isBlankCheck && isBlankCheck != undefined){
		if(data==undefined || data.length==0){
			msgBox.info({content:'请选择人员',type:'fail'});
			return false;
		}
	}
	if(data==undefined || data.length==0){
		selectControl.isOpenSinglePerson = true;
		return false;
	}else{
		cbf(data, controlId);
		selectControl.isOpenSinglePerson = true;
	}
	$("#"+searchInputId).val("");
	selectControl.removeValidSelect2(controlId, isCheckOrRadio);
	selectControl.openPutAwayClear(controlId);
}

var allcheckboxFlag = true;
/**
 * 人员全选
 * @param selectControlId 人员select控件ID
 * @param openPersonDivId 打开DIV的ID
 */
selectControl.allCheckbox = function(selectControlId, openPersonDivId){
	//获取所有的input标签对象,取出所有的checkbox
	var inputs = $("#"+openPersonDivId+" input[type='checkbox']");
	var arrayAllCheckbox = [];	//每组全选按钮对象
	var checkboxArray = [];		//用来存放checkbox对象
	
	for(var i=0;i<inputs.length;i++){
		var obj = inputs[i];
		if(obj.type=='checkbox'){
			if(obj.value != "on"){
				checkboxArray.push(obj);
			}else{
				arrayAllCheckbox.push(obj);
			}
		}
	}
	
	if(allcheckboxFlag){
		selectControl.clearAllUser(selectControlId);
		//根据回显数据勾选相应的checkbox以及select框中的赋值
		for(var j=0;j<checkboxArray.length;j++){
			var cbValue = checkboxArray[j].value.split(",");
			checkboxArray[j].checked = true;
			$("#"+openPersonDivId+" #"+selectControlId).append("<option dept='Dept"+cbValue[0]+"'  value='"+cbValue[0]+","+cbValue[2]+"'>"+cbValue[1]+"</option>");
		}
		for(var i=0;i<arrayAllCheckbox.length;i++){
			arrayAllCheckbox[i].checked = true;
		}
		allcheckboxFlag = false;
	}else{
		$("#"+openPersonDivId+" input[name='depts']").prop("checked", false);
		$("#"+openPersonDivId+" input[name='depts']").each(function(){
			$("#"+openPersonDivId+" input[name="+this.id+"]").prop("checked", false);
		});
		selectControl.clearAllUser(selectControlId);
		allcheckboxFlag = true;
	}
}

/**
 * 打开弹出人员[文本框、按钮、超链接]
 */
selectControl.singleOpenPerson = function(openPersonDivId, selectBackValueId, isCheckOrRadio, eData){
	if(!!eData){
		var openDiv = $("#"+openPersonDivId),
			sBack  = $("#"+selectBackValueId);
		if(isCheckOrRadio){
			//获取所有的input标签对象,取出所有的checkbox
			var inputBoxs = openDiv.find(":checkbox"),
				allBoxs	  = openDiv.find("input[name='depts']"),
				cbValue = [];
			openDiv.find("input:checkbox").attr("checked", false);
			//根据回显数据勾选相应的checkbox以及select框中的赋值
			selectControl.clearAllUser(selectBackValueId);
			for(var i = 0;i < eData.length;i ++){
				var docDom = openDiv.find("#"+eData[i].id)[0];
				if(docDom && docDom.value){
					docDom.checked = true;
					cbValue = docDom.value.split(",");
					sBack.append("<option dept='Dept"+cbValue[0]+"' value='"+cbValue[0]+","+cbValue[2]+"'>"+cbValue[1]+"</option>");
				}
			}
			//根据每组中的checkbox选中情况来勾选全选checkbox
			for(var i = 0; i < allBoxs.length; i++){
				k = 0;
				var boxId = allBoxs[i].id,
					as = openDiv.find("input[name='"+boxId+"']");
				for(var j = 0;j < as.length;j ++){
					if(as[j].checked) k++;
				}
				if(k == as.length) allBoxs[i].checked = true;
			}
		}else{
			//如果是radio
			openDiv.find(":radio").attr("checked", false);
			var selectUsers = openDiv.find("input[name='selectUser']"),
				len = selectUsers.length,
				i	= 0;
			for(; i < len; i++){
				var vals = selectUsers[i].value.split(","),
					val   = vals[0];
				if(val == eData.id){
					sBack.find("option[value='"+val+"']").remove();
					sBack.append("<option value='"+val+"'>"+vals[1]+"</option>");
					$(selectUsers[i]).click();
				}
			}
		}
	}
	selectControl.showOnLine(openPersonDivId);
	//1007
	$("#"+openPersonDivId).modal("show");
	//人员界面收缩事件
	$(".tree-list .tree-btn").on('click',function (e) {
        e.preventDefault();
        e.stopPropagation();
        $(this).find("i").toggleClass("fa-chevron-down").end().closest(".tree-list").next().slideToggle();
    });
	
}

/**
 * 人员select2初始化数据
 * @param personData 		数据
 * @param controlId 		控件ID
 * @param isCheckOrRadio 	单选或多选
 */
function select2InitToPerson(personData, controlId, isCheckOrRadio, echoData, callback){
	$("#"+controlId).select2({
	    placeholder: " ",						//水印提示
	    allowClear: true,						//允许清除
	    maximumInputLength:	10,					//最大输入长度
	    multiple: isCheckOrRadio ? true : false,//单选or多选
	    seledFun:(typeof callback === 'function'? callback : undefined),
	    query: function (query){
	        var data = {results: []};
	        if(personData.length > 0){
	        		
	        	$.each(personData, function(){
		            if(query.term.length == 0 || this.text.toUpperCase().indexOf(query.term.toUpperCase()) >= 0 
		            		|| this.jp.toUpperCase().indexOf(query.term.toUpperCase()) >= 0){
		            	//判断当为辅助部门时，不加入到查询结果中
		            	if(this.otherDeptFlag !=1) data.results.push({id: this.id, text: this.text});
		            }
		        });
	        }
	        query.callback(data);
	    }
	});
	zkAndSqCount=0;
	selectControl.openPutAwayClear(controlId);
}

/**
 * 人员select2初始化回显
 * @param controlId 控件ID
 * @param echoData 回显数据
 */
function select2InitEchoToPerson(controlId, echoData, openPersonDivId, selectBackValueId){
	$("#"+controlId).select2("data", echoData);
	selectControl.openPutAwayClear(controlId);
}

/**
 * 组织select2初始化数据
 * @param orgData 			数据
 * @param controlId 		控件ID
 * @param isCheckOrRadio 	单选或多选
 * @param orgOrDept			组织类型
 */
function select2InitToOrg(orgData, controlId, isCheckOrRadio, orgOrDept,callback){
	$("#"+controlId).select2({
	    placeholder: " ",						//文本框占位符显示
	    allowClear: true,						//允许清除
	    maximumInputLength: 10,					//最大输入长度
	    multiple: isCheckOrRadio ? true : false,//单选or多选
	    seledFun:(typeof callback == 'function'?callback:undefined),
	    query: function (query){
	        var data = {results: []};
	        $.each(orgData, function(){
	            if(query.term.length == 0 || this.text.toUpperCase().indexOf(query.term.toUpperCase()) >= 0
	            		|| this.jp.toUpperCase().indexOf(query.term.toUpperCase()) >= 0){
	            	if(orgOrDept == 1){
	            		data.results.push({id: this.id, text: this.text, disabled: this.deptType==1 && this.isChecked==1?false:true});
	            	}else if(orgOrDept == 0){
	            		data.results.push({id: this.id, text: this.text, disabled: this.deptType==0 && this.isChecked==1?false:true});
	            	}else{
	            		data.results.push({id: this.id, text: this.text, disabled: this.isChecked==1?false:true});
	            	}
	            }
	        });
	        query.callback(data);
	    }
	});
	zkAndSqCount=0;
	selectControl.openPutAwayClear(controlId);
}

/**
 * 组织select2初始化回显
 * @param controlId 控件ID
 * @param echoData 回显数据
 */
function select2InitEchoToOrg(controlId, echoData){
	$("#"+controlId).select2("data", echoData);
	selectControl.openPutAwayClear(controlId);
}


/**
 * 人员选择回显[回写值]
 * @param controlInuptId 	文本控件ID
 * @param isCheckOrRadio	单选多选
 * @param openPersonDivId	弹出控件的DIVID
 * @param selectControlId 	人员界面select控件ID
 */
selectControl.showPersonValue = function(searchInputId, controlInuptId, isCheckOrRadio, openPersonDivId, selectControlId, callback, isBlankCheck, openPerson){	
	var users = [];
	$("#"+openPersonDivId+" select[name='"+selectControlId+"']").find("option").each(function(i, val){
		var gv = val.value.split(",");
		users[i] = {
			id: gv[0],
			text: val.text
		}
    });
	if(users.length > 0){
		if(openPerson == undefined ){
			if(isCheckOrRadio){
				$("#"+controlInuptId).select2("data", users);//多选回显
			}else{
				$("#"+controlInuptId).select2("data", {id: users[0].id, text: users[0].text});//单选回显
			}
		}
	}else{
		msgBox.info({content:'请选择人员',type:'fail'});
		return false;
	}
	
	if(!isBlankCheck || isBlankCheck == undefined)
		$("#"+openPersonDivId).modal("hide");

	$("#"+searchInputId).val("");
	selectControl.removeValidSelect2(controlInuptId, isCheckOrRadio);
	selectControl.openPutAwayClear(controlInuptId);
	selectControl.setFocus(controlInuptId);
	return users;
}

/**
 * 打开组织树
 * @param isCheckOrRadio 	单选多选
 * @param treeId			显示树DivID
 * @param myTreeId			树控件ID
 * @param controlInuptId 	文本控件ID
 * @param orgOrDept 		机构/部门
 */
selectControl.org.open = function(isCheckOrRadio, treeId, myTreeId, controlInuptId, orgOrDept){
	/**
	 * tree控件的设置[单选][默认]
	 */
	var settingRadio = {
		check:{
			enable: true,
			nocheckInherit: true,
			chkStyle: "radio",
			radioType : "all"
		},
		view:{
			selectedMulti: false,
			showLine: false
		},
		data:{
			simpleData:{
				enable:true
			}
		},
		callback:{
			beforeClick: function(id, node){
				return false;
			}
		}
	};

	/**
	 * tree控件的设置[多选]
	 */
	var settingCheck = {
		check:{
			enable: true,
			chkStyle: "checkbox",
			chkboxType: { "Y" : "s", "N" : "ps" }
		},
		view:{
			selectedMulti: false,
			showLine: false
		},
		data:{
			simpleData:{
				enable:true
			}
		},
		callback:{
			beforeClick: function(id, node){}
		}
	};
	
	var zNodes = [];
	
	if(orgOrDept==1){
		$.each(orgData, function(i, o) {
			zNodes[i] = {
				id : o.id,
				pId : o.parentId,
				name : o.text,
				deptType : o.deptType,
				chkDisabled : o.deptType == 0 ? true : false,
				isChecked: o.isChecked
			};
		});
	}else if(orgOrDept==0){
		$.each(orgData, function(i, o) {
			zNodes[i] = {
				id : o.id,
				pId : o.parentId,
				name : o.text,
				deptType : o.deptType,
				chkDisabled : o.deptType == 1 ? true : false,
				isChecked: o.isChecked
			};
		});
	}else{
		$.each(orgData, function(i, o) {
			zNodes[i] = {
				id : o.id,
				pId : o.parentId,
				name : o.text,
				deptType : o.deptType,
				isChecked: o.isChecked,
				chkDisabled: o.isChecked == 1 ? false : true
			};
		});
	}
	
	var zTreeObject = $.fn.zTree.init($("#"+treeId+" #"+myTreeId), isCheckOrRadio ? settingCheck : settingRadio, zNodes);
	zTreeObject.expandAll(true);
	var flagEdata = 0;
	var eData = returnValue(controlInuptId);
	if(eData != null){
		$(eData.split(",")).each(function(i, v){
			var node = zTreeObject.getNodeByParam("id", v.split(":")[0], null);
			if(node != null){
				zTreeObject.checkNode(node, true, false);
				flagEdata++;
			}else{
				flagEdata--;
			}
		});
	}
	$("#"+treeId).modal("show");
};

/**
 * 关闭组织树界面
 * @param treeId 	显示树控件的DivID
 * @param controlId 控件的ID
 */
selectControl.org.close = function(treeId, controlId){
	$("#"+treeId).modal("hide");
	selectControl.openPutAwayClear(controlId);
};

/**
 * 清空控件值
 * @param controlInuptId 	文本控件ID
 */
selectControl.clearValue = function(controlInuptId){
	$("#"+controlInuptId).select2("data","");
	selectControl.openPutAwayClear(controlInuptId);
}

/**
 * 组织选择回显值[回写值]
 * @param myTreeId			组织控件ID
 * @param controlInuptId 	文本控件ID
 * @param treeId			显示树DivID
 * @param isCheckOrRadio	单选多选
 */
function showOrgValue(myTreeId, controlInuptId, treeId, isCheckOrRadio){
	var treeObj = $.fn.zTree.getZTreeObj(myTreeId);
	var nodes = treeObj.getChangeCheckedNodes();
	if(nodes.length == 0){
		return false;
	}
	var rov = [];//人员控件返回的数据集合
	$.each(nodes,function(i, val){
		rov[i] = {
			id: this.id,
			text: this.name
		}
	});
	if(isCheckOrRadio){
		$("#"+controlInuptId).select2("data", rov);//多选回显值
	}else{
		if(rov.length > 0)
			$("#"+controlInuptId).select2("data", {id: nodes[0].id, text: nodes[0].name});//单选回显值
	}
	$("#"+treeId).modal("hide");
	selectControl.removeValidSelect2(controlInuptId, isCheckOrRadio);
	selectControl.openPutAwayClear(controlInuptId);
}

/**
 * 设置组织
 * 参数：	controlInuptId:控件id
 * 		dataStr:回显字符串   多个用","分割
 */
function setOrgData(controlInuptId, dataStr){
	var datas 		= new Array(),
		controlDiv	= $("#"+ controlInuptId);
	if(dataStr!=null&&dataStr.length>0){
		var items = dataStr.split(",");
		for(var i=0;i<items.length;i++){
			var id = items[i];
			for(var j=0;i<orgData.length;j++){
				if(orgData[j].id == id){
					var dataTemp = {id:id, text:orgData[j].text};
					datas.push(dataTemp);
					break;
				}
			}
		}
	}
	if(datas.length == 1){
		datas = {
			id:datas[0].id,
			text:datas[0].text	
		}
	}
	controlDiv.select2("data", datas);
	select_tree(controlDiv);
}

/**
 * 返回选中的值
 * @param inputId	文本控件ID
 * @returns	单选返回格式[id:name]、多选返回格式[id:name,id:name]
 */
function returnValue(inputId){
	var inputDiv = $("#"+inputId);
	if(inputDiv.select2("data") == null ||inputDiv.select2("data").length == 0 ){
		return null;
	}
	var v = "";
	if(inputDiv.select2("data").length > 0){
		$.each(inputDiv.select2("data"), function(j, d){
			v += d.id+":"+d.text+",";
		});
	}else{
		v += inputDiv.select2("data").id+":"+inputDiv.select2("data").text+",";
	}
	return v.substring(0, v.length-1);
}

/**
 * 选择部门级联人员操作
 * @param obj				本控件对象
 * @param userId			用户ID
 * @param selectId			select的ID
 * @param openPersonDivId	弹出层DIVID
 */
selectControl.selectDeptUser = function(obj, userId, selectId, openPersonDivId){
	var openDiv = $("#"+openPersonDivId),
		objDiv  = $("input[name='"+obj.id+"']"),
		selDiv  = $("#"+selectId),
		options = "",
		objLen  = objDiv.length;
	
	objDiv.prop("checked", (obj.checked?true:false));
	
	for(var i = 0; i < objLen; i++){
		var v = objDiv[i].value.split(",");
		if(obj.checked){
			selDiv.find("option[dept='Dept"+v[0]+"']").remove();
			selDiv.append("<option dept='Dept"+v[0]+"' value='"+v[0]+","+v[2]+"'>"+v[1]+"</option>");
		}else{
			if(openDiv.find("[dept='otherDept"+v[0]+"']:checked").length == 0){
				selDiv.find("option[dept='Dept"+v[0]+"']").remove();
			}
		}
	}
	
	selDiv.find("option").each(function(j,m){
        if(options.indexOf(m.outerHTML) == -1) {
        	options    += m.outerHTML;
        }
    });
	selDiv.html(options);
	allcheckboxFlag = true;
}

/**
 * 选择人员级联部门操作
 * @param obj				本控件对象
 * @param deptId			部门input对象 
 * @param selectId			select的ID
 * @param openPersonDivId	弹出层DIVID
 */
selectControl.selectUserDept = function(obj, deptId, selectId, openPersonDivId){
	var v = obj.value.split(","),
		openDiv = $("#"+openPersonDivId),
		selectI = $("#"+selectId),
		sTmp = 0,
		isAdd = selectControl.selectUserFilter(selectI.find("option"),v[1]),
		depts = openDiv.find("[dept='otherDept"+v[0]+"']:checked");
	if(obj.checked){
		if(!isAdd) selectI.append("<option dept='Dept"+v[0]+"' value='"+v[0]+","+v[2]+"'>"+v[1]+"</option>");
	}else{
		if(!depts.length) selectI.find("option[dept='Dept"+v[0]+"']").remove();
	}
	//判断是否需要把全选按钮置为选中状态

	var idName = typeof deptId === 'string'?deptId:deptId.id;
	
	sTmp = openDiv.find("input[name='"+idName+"']").not(":checked").length;

	$("input[id='"+idName+"'][name='depts']").prop("checked", (!sTmp?true:false));
	allcheckboxFlag = true;
}
/**
 * 过滤选中人员
 * @param objs				select下所有的option
 * @param str				需要过滤的字符串
 */
selectControl.selectUserFilter = function(objs,str){
	var len = objs.length,
		i   = 0,
		falg = false;
	for(;i < len;i++){
		if(str === objs[i].text) {
			falg = true;
			break;
		}
	}
	return falg;
}

/**
 * 选择人员
 * @param obj				本控件对象
 * @param selectId			select的ID
 * @param openPersonDivId	弹出层DIVID
 */
selectControl.selectUser = function(obj, selectId, openPersonDivId){
	if(obj.checked){
		$("#"+selectId+" option:last").remove();
		var v = obj.value.split(",");
		$("#"+selectId).append("<option dept='Dept"+v[0]+"' value='"+v[0]+","+v[2]+"'>"+v[1]+"</option>");
	}
}

selectControl.clearAllUser = function(selectId){
	$("#"+selectId).empty();
}


var sortFlag = true;
/**
 * 自动排序
 * @param selectId
 */
function sort(selectId){
	var $s = $("#"+selectId);
	if($s.find("option").length == 0){
		msgBox.tip({content: "无排序数据", type:'fail'});
	}else{
		var arr = [];
		$.each($s.find("option"), function(i,v){
			var vo = v.value.split(",");
			arr[i] = { 
				text: v.text, 
				value: vo[0],
				orderNo: vo[1]
			};
		});
		selectControl.clearAllUser(selectId);
		var $sort = $("#sort");
		if(sortFlag){
			arr.sort(function(x,y){return x.orderNo-y.orderNo});//从小到大排序
			sortFlag = false;
			$sort.removeClass("fa fa-sort-shang").addClass("fa fa-sort-xia");
		}else{
			arr.sort(function(x,y){return y.orderNo-x.orderNo});//从大到小排序
			sortFlag = true;
			$sort.removeClass("fa fa-sort-xia").addClass("fa fa-sort-shang");
		}
		$.each(arr, function(i,v){
			$s.append("<option dept'Dept"+v.value+"' value='"+v.value+","+v.orderNo+"'>"+v.text+"</option>");
		});
	}
}

/**
 * 选择框排序上
 * @param selectId
 */
function up(selectId){
	var $select = $("#"+selectId);
	if($select.val() == null){
		msgBox.tip({content: "请选择人员", type:'fail'});
	}else{
		var ses = $select.find("option:selected");
		if(ses.length > 1){
			msgBox.tip({content: "请选择一项进行调整", type:'fail'});
		}else{
			var optionIndex = $select.get(0).selectedIndex; 
			if(optionIndex > 0) ses.insertBefore(ses.prev('option')); 
		}
	}
}

/**
 * 选择框排序下
 * @param selectId
 */
function down(selectId){
	var $select = $("#"+selectId);
	if($select.val() == null){
		msgBox.tip({content: "请选择人员", type:'fail'});
	}else{
		var ses = $select.find("option:selected");
		if(ses.length > 1){
			msgBox.tip({content: "请选择一项进行调整", type:'fail'});
		}else{
			var optionLength = $select[0].options.length; 
			var optionIndex = $select.get(0).selectedIndex; 
			if(optionIndex < (optionLength-1)) ses.insertAfter(ses.next('option')); 
		}
	}
}

/**
 * 显示在线人员
 * @param d 				人员数据
 * @param selectId 			select的ID
 * @param openPersonDivId	弹出层DIVID
 * @param isCheckOrRadio	单选或多选
 */
selectControl.showOnlineUserPage = function(d, selectId, openPersonDivId, isCheckOrRadio){
	var lv1_dept = ['<div>'], lv1_user = ['<div>'];
	if(d != null){
		//只显示在线人员的
		if(d.user != null && d.user.length > 0){
			if(isCheckOrRadio){
				lv1_dept.push('<label class="checkbox fl"><input type="checkbox" id="online'+d.id+'" name="depts" onClick="selectControl.selectDeptUser(this,\''+d.id+'\',\''+selectId+'\',\''+openPersonDivId+'\')"> <span class="fl w100">'+d.name+'</span></label> ');
			}else{
				lv1_dept.push('<label class="radio fl"><span class="fl w100">'+d.name+'</span></label> ');
			}
			lv1_user.push(selectControl.joinString(d.user,isCheckOrRadio,selectId,openPersonDivId,null,null,"online"));
		}else{
			lv1_dept.push('<label for="" class="fl"><span class="fl w100">'+d.name+'</span></label> ');
			lv1_user.push('<label for="" class="fl"></label>');
		}
		lv1_user.push('</div>');
		lv1_dept.push('<a href="#" class="fr m-r tree-btn"><i class="fa fa-chevron-up"></i></a></div>');
	}else{
		lv1_dept.push('<label class="fl"><span class="fl w100"></span></label></div> ');
		lv1_user.push('<div style="text-align:center;">'+$.i18n.prop("JC_SYS_007")+'</div>');
	}
	var showlist = $([
		'<section class="w820 fl tree-ul tree-scroll" id="treeSearchContainer">',
			'<ul class="tree-horizontal clearfix">',
				'<li>',
				'<div class="level1 clearfix tree-list">',
					lv1_dept.join('') ,
					lv1_user.join('') ,
				'</div>',
				'<ul id="lv"></ul>',
				'</li>',
			'</ul>',
		'</section>',
		'<section class="fl m-l pos-rlt">',
			'<select id="'+selectId+'" name="'+selectId+'" multiple="false" class="w170 tree-scroll-right tree-select">',
            '</select>',
            '<div class="tree-sort"><a href="#" onClick="sort(\''+selectId+'\');"><i id="sort" class="fa fa-sort-shang"></i></a></div>',
            '<div class="tree-move"> ',
            	'<a href="#" class="tree-move-up" onClick="up(\''+selectId+'\');"><i class="fa fa-caret-up"></i></a> ',
            	'<a href="#" class="tree-move-down" onClick="down(\''+selectId+'\');"><i class="fa fa-caret-down"></i></a> ',
            '</div>',
        '</section>'
	].join(''));
	if(d != null && d.subDept != null && d.subDept.length > 0)
		selectControl.recurOnline(d.subDept, showlist.find("#lv"), 2, selectId, openPersonDivId, isCheckOrRadio);
	return showlist;
}

/**
 * 显示部门人员
 * @param d 				人员数据
 * @param selectId 			select的ID
 * @param openPersonDivId	弹出层DIVID
 * @param isCheckOrRadio	单选或多选
 */
selectControl.showDeptUserPage = function(d, selectId, openPersonDivId, isCheckOrRadio){
	var lv1_dept = ['<div>'], lv1_user = ['<div>'];
	if(d != null){
		if(d.user != null && d.user.length > 0){
			if(isCheckOrRadio){
				lv1_dept.push('<label class="checkbox fl"><input type="checkbox" id="dept'+d.id+'" name="depts" onClick="selectControl.selectDeptUser(this,\''+d.id+'\',\''+selectId+'\',\''+openPersonDivId+'\')"> <span class="fl w100">'+d.name+'</span></label> ');
			}else{
				lv1_dept.push('<label class="radio fl"><span class="fl w100">'+d.name+'</span></label> ');
			}
			lv1_user.push(selectControl.joinString(d.user,isCheckOrRadio,selectId,openPersonDivId,null,null,"dept"));
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
	var showlist = $(
		'<section class="w820 fl tree-ul tree-scroll" id="treeSearchContainer">'+
			'<ul class="tree-horizontal clearfix">'+
				'<li>'+
				'<div class="level1 clearfix tree-list">'+
					lv1_dept.join('') +
					lv1_user.join('') +
				'</div>'+
				'<ul id="lv"></ul>'+
				'</li>'+
			'</ul>'+
		'</section>'+
		'<section class="fl m-l pos-rlt">'+
			'<select id="'+selectId+'" name="'+selectId+'" multiple="false" class="w170 tree-scroll-right tree-select">'+
            '</select>'+
            '<div class="tree-sort"><a href="#" onClick="sort(\''+selectId+'\');"><i id="sort" class="fa fa-sort-shang"></i></a></div>'+
            '<div class="tree-move"> '+
            	'<a href="#" class="tree-move-up" onClick="up(\''+selectId+'\');"><i class="fa fa-caret-up"></i></a> '+
            	'<a href="#" class="tree-move-down" onClick="down(\''+selectId+'\');"><i class="fa fa-caret-down"></i></a> '+
            '</div>'+
        '</section>'
	);

	if(d != null && d.subDept != null && d.subDept.length > 0)
		selectControl.recur(d.subDept, showlist.find("#lv"), 2, selectId, openPersonDivId, isCheckOrRadio);
	return showlist;
}

/**
 * 递归查询下级菜单
 * @param deptList			部门集合
 * @param parentHtml		上级html
 * @param level				level-css样式
 * @param selectId			select的ID
 * @param openPersonDivId	弹出层DIVID
 * @param isCheckOrRadio	单选或多选
 */
selectControl.recur = function(deptList, parentHtml, level, selectId, openPersonDivId, isCheckOrRadio){
	var deptList_len = deptList.length;
	for(var i=0; i<deptList_len; i++){
		var deptList_dept = deptList[i];
		if(deptList_dept.subDept) {
			var main_dept = ['<div>'],
				main_user = ['<div>'];
			if(deptList_dept.user.length > 0){
				if(isCheckOrRadio){
					main_dept.push('<label class="checkbox fl"><input type="checkbox" id="dept'+deptList_dept.id+'" name="depts" onClick="selectControl.selectDeptUser(this,'+deptList_dept.id+',\''+selectId+'\',\''+openPersonDivId+'\')"> <span class="fl w100">'+deptList_dept.name+'</span></label> ');
				}else{
					main_dept.push('<label class="radio fl"><span class="fl w100">'+deptList_dept.name+'</span></label> ');
				}
				if(deptList_dept.user != undefined){
					if(deptList_dept.user.length > 0){
						main_user.push(selectControl.joinString(deptList_dept.user,isCheckOrRadio,selectId,openPersonDivId,deptList_dept.id,null,"dept"));
					}
				}else{
					main_user.push(selectControl.joinString(deptList_dept.subDept[0].user,isCheckOrRadio,selectId,openPersonDivId,null,null,"dept"));
				}
			} else {
				main_dept.push('<label class="checkbox fl"><span class="fl w100">'+deptList[i].name+'</span></label> ');
				main_dept.push('<label class="checkbox inline"></label>');
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
			selectControl.recur(deptList[i].subDept, $(main_sub).children().last(), level+1, selectId, openPersonDivId, isCheckOrRadio);
		} else {
			var sub_dept = ['<div>'],
				sub_user = ['<div>'];
			if(deptList_dept.user && deptList_dept.user.length > 0){
				if(isCheckOrRadio){
					sub_dept.push('<label class="checkbox fl"><input type="checkbox" id="dept'+deptList_dept.id+'" name="depts" onClick="selectControl.selectDeptUser(this,'+deptList_dept.id+',\''+selectId+'\',\''+openPersonDivId+'\')"> <span class="fl w100">'+deptList_dept.name+'</span></label> ');
				}else{
					sub_dept.push('<label class="radio fl"><span class="fl w100">'+deptList_dept.name+'</span></label> ');
				}
				sub_user.push(selectControl.joinString(deptList_dept.user,isCheckOrRadio,selectId,openPersonDivId,null,null,"dept"));
			}else{
				sub_dept.push('<label class="checkbox fl"><span class="fl w100">'+deptList[i].name+'</span></label> ');
				sub_user.push('<label class="checkbox inline"></label>');
			}
			sub_user.push('</div>');
			sub_dept.push('</div>');//<a href="#" class="fr m-r tree-btn"><i class="fa fa-chevron-up"></i></a>去掉叶子节点的箭头
			parentHtml.append([
			   				'<li>',
							'<div class="level'+level+' clearfix tree-list">',
								sub_dept.join('') ,
								sub_user.join('') ,
							'</div>',
						'</li>'
					].join(''));
		}
	}
}
/**
 * 拼装字符串
 * @author gezhigang
 * @param  obj				数据集合
 * @param  isCheckOrRadio	单选或多选
 * @param  selectId			select的ID
 * @param  openPersonDivId	弹出层DIVID
 * @param  raID				组件name  不传入参数默认输出遍历obj的deptId
 * @param  color			字体颜色
 * @param  raFirst			name前缀
 */
selectControl.joinString = function(obj,isCheckOrRadio,selectId,openPersonDivId,raName,color,raFirst){
	var len  = obj.length,
    	i    = 0,
    	arys = new Array();
    for(; i<len; i++){
        var temp = obj[i];
        arys.push('<label class="'+((isCheckOrRadio)?"checkbox":"radio")+' inline"> '
            +'<input type="'+((isCheckOrRadio)?"checkbox":"radio")
            +'" id="'+temp.id
            +'" dept="otherDept'+temp.id
            +'" name="'+(((isCheckOrRadio)?raFirst+((raName)?raName:temp.deptId):"selectUser"))
            +'" value="'+temp.id+','+temp.displayName+','+((isCheckOrRadio)?temp.orderNo:"")
            +'" onClick="selectControl.selectUser'+((isCheckOrRadio)?"Dept":"")+'(this,'+((isCheckOrRadio)?"'"+(raFirst+temp.deptId)+"',":"")+'\''+selectId+'\',\''+openPersonDivId+'\')"> '+
        '<a href="#" onclick="selectControl.showPersonInfo('+temp.id+');"><span style="color:'+((color)?color:"")+'">'+temp.displayName+'</span></a></label> ');
    }
    return arys.join(" ");
}
/**
 * 递归查询在线下级菜单--显示所有组织
 * @param deptList			部门集合
 * @param parentHtml		上级html
 * @param level				level-css样式
 * @param selectId			select的ID
 * @param openPersonDivId	弹出层DIVID
 * @param isCheckOrRadio	单选或多选
 */
selectControl.recurOnline = function(deptList, parentHtml, level, selectId, openPersonDivId, isCheckOrRadio){
	var deptList_len = deptList.length;
	for(var i=0; i<deptList_len; i++){
		var deptList_dept = deptList[i];
		if(deptList_dept.subDept) {
			var main_dept = ['<div>'],
				main_user = ['<div>'];
			if(deptList_dept.user.length > 0){
				if(isCheckOrRadio){
					main_dept.push('<label class="checkbox fl"><input type="checkbox" id="online'+deptList_dept.id+'" name="depts" onClick="selectControl.selectDeptUser(this,'+deptList_dept.id+',\''+selectId+'\',\''+openPersonDivId+'\')"> <span class="fl w100">'+deptList_dept.name+'</span></label> ');
				}else{
					main_dept.push('<label class="radio fl"><span class="fl w100">'+deptList_dept.name+'</span></label> ');
				}
				if(deptList_dept.user != undefined){
					if(deptList_dept.user.length > 0){
						main_user.push(selectControl.joinString(deptList_dept.user,isCheckOrRadio,selectId,openPersonDivId,deptList_dept.id,"#60AAE9","online"));
					}
				}else{
					main_user.push(selectControl.joinString(deptList_dept.subDept[0].user,isCheckOrRadio,selectId,openPersonDivId,null,"#60AAE9","online"));
				}
			} else {
				main_dept.push('<label class="checkbox fl"><span class="fl w100">'+deptList[i].name+'</span></label> ');
				main_user.push('<label class="checkbox inline"></label> ');
			}
			main_dept.push('<a href="#" class="fr m-r tree-btn"><i class="fa fa-chevron-up"></i></a></div> ');
			main_user.push('</div> ');
			var main_sub = $(
				'<li>'+
					'<div class="level'+level+' clearfix tree-list">'+
						main_dept.join(" ") +
						main_user.join(" ") +
					'</div>'+
					'<ul id="lv'+i+'"></ul>'+
				'</li>'
			);
			$(main_sub).appendTo(parentHtml);
			selectControl.recurOnline(deptList[i].subDept, $(main_sub).children().last(), level+1, selectId, openPersonDivId, isCheckOrRadio);
		} else {
			var sub_dept = ['<div>'];
			var sub_user = ['<div>'];
			if(deptList_dept.user.length > 0){
				if(isCheckOrRadio){
					sub_dept.push('<label class="checkbox fl"><input type="checkbox" id="online'+deptList_dept.id+'" name="depts" onClick="selectControl.selectDeptUser(this,'+deptList_dept.id+',\''+selectId+'\',\''+openPersonDivId+'\')"> <span class="fl w100">'+deptList_dept.name+'</span></label> ');
				}else{
					sub_dept.push('<label class="radio fl"><span class="fl w100">'+deptList_dept.name+'</span></label> ');
				}
				sub_user.push(selectControl.joinString(deptList_dept.user,isCheckOrRadio,selectId,openPersonDivId,null,"#60AAE9","online"));
			}else{
				sub_dept.push('<label class="checkbox fl"><span class="fl w100">'+deptList[i].name+'</span></label> ');
				sub_user.push('<label class="checkbox inline"></label> ');
			}
			sub_user.push('</div> ');
			sub_dept.push('</div> ');
			parentHtml.append([
				   				'<li>',
								'<div class="level'+level+' clearfix tree-list">',
									sub_dept.join(''),
									sub_user.join(''),
								'</div>',
							'</li>'
						].join(''));
		}
	}
}

/**
 * 单层调用 -- 职务
 */
selectControl.showUserPage = function(d, selectId, openPersonDivId, isCheckOrRadio){
	var h = ['<section class="w820 fl tree-ul tree-scroll" id="treeSearchContainer"><ul class="tree-horizontal clearfix">'];
	if(d != null && d.length > 0){
		var d_len = d.length;
		for(var i=0;i<d_len;i++){
			var d_i = d[i];
			var li = ['<li><div class="level1 clearfix tree-list">'];
			var lv1_dept = ['<div>'];
			if(isCheckOrRadio){
				lv1_dept.push('<label class="checkbox fl"><input type="checkbox" id="'+d_i.id+'" name="depts" onClick="selectControl.selectDeptUser(this,'+d_i.id+',\''+selectId+'\',\''+openPersonDivId+'\')"> <span class="fl w100">'+d_i.name+'</span></label>');
			}else{
				lv1_dept.push('<label class="radio fl"><span class="fl w100">'+d_i.name+'</span></label>');
			}
			var lv1_user = ['<div>'];
			var d_user_len = d_i.user.length;
			for(var l1=0;l1<d_user_len;l1++){
				var d_user_l1 = d_i.user[l1];
				if(isCheckOrRadio){
					lv1_user.push('<label class="checkbox inline"> ');
					lv1_user.push('<input type="checkbox" id="'+d[i].user[l1].id+'" name="'+d_user_l1.dutyId+'" value="'+d_user_l1.id+','+d_user_l1.displayName+','+d_user_l1.orderNo+'" onClick="selectControl.selectUserDept(this,\''+d_user_l1.dutyId+'\',\''+selectId+'\',\''+openPersonDivId+'\')"> '); 
					lv1_user.push('<a href="#" onclick="selectControl.showPersonInfo('+d_user_l1.id+');"><span>'+d_user_l1.displayName +'</span></a>');
					lv1_user.push('</label>');
				}else{
					lv1_user.push('<label class="radio inline"> ');
					lv1_user.push('<input type="radio" id="'+d_user_l1.id+'" name="selectUser" value="'+d_user_l1.id+','+d_user_l1.displayName+'" onClick="selectControl.selectUser(this,\''+selectId+'\',\''+openPersonDivId+'\')"> ');
					lv1_user.push('<a href="#" onclick="selectControl.showPersonInfo('+d_user_l1.id+');"><span>'+d_user_l1.displayName +'</span></a>');
					lv1_user.push('</label>');
				}
			}
			lv1_user.push('</div>');
			lv1_dept.push('</div>');
			li.push(lv1_dept.join('') + lv1_user.join('') + '</div></li>');
			h.push(li.join(''));
		}
	}else{
		var lv1_dept = ['<div style="text-align:center;">'+$.i18n.prop("JC_SYS_007")+'</div>'];//没有符合查询条件的记录
		h.push(lv1_dept.join(''));
	}
	h.push('</ul>');
	h.push('</section>');
	h.push('<section class="fl m-l pos-rlt">');
	h.push('<select id="'+selectId+'" name="'+selectId+'" multiple="false" class="w170 tree-scroll-right tree-select">');
	h.push('</select>');
	h.push('<div class="tree-sort"><a href="#" onClick="sort(\''+selectId+'\');"><i id="sort" class="fa fa-sort-shang"></i></a></div>');
	h.push('<div class="tree-move"> ');
	h.push('<a href="#" class="tree-move-up" onClick="up(\''+selectId+'\');"><i class="fa fa-caret-up"></i></a> ');
	h.push('<a href="#" class="tree-move-down" onClick="down(\''+selectId+'\');"><i class="fa fa-caret-down"></i></a> ');
	h.push('</div>');
	h.push('</section>');
	var showlist = $(h.join(''));
	return showlist;
}

/**
 * 单层调用 -- 组别
 */
selectControl.showGroupPage = function(d, selectId, openPersonDivId, isCheckOrRadio){
	var h = ['<section class="w820 fl tree-ul tree-scroll" id="treeSearchContainer"><ul class="tree-horizontal clearfix">'];
	if(d != null && d.length > 0){
		for(var i=0;i<d.length;i++){
			var d_i = d[i];
			var li = ['<li><div class="level1 clearfix tree-list">'];
			var lv1_dept = ['<div>'];
			if(isCheckOrRadio){
				lv1_dept.push('<label class="checkbox fl"><input type="checkbox" id="personGroup'+d_i.id+'" name="depts" onClick="selectControl.selectDeptUser(this,'+d_i.id+',\''+selectId+'\',\''+openPersonDivId+'\')"> <span class="fl w100">'+d_i.name+'</span></label>');
			}else{
				lv1_dept.push('<label class="radio fl"><span class="fl w100">'+d_i.name+'</span></label>');
			}
			var lv1_user = ['<div>'];
			var d_group_len = d_i.group.length;
			for(var l1=0;l1<d_group_len;l1++){
				var d_group_l1 = d_i.group[l1];
				if(isCheckOrRadio){
					lv1_user.push('<label class="checkbox inline"> ');
					lv1_user.push('<input type="checkbox" id="personGroup'+d_group_l1.groupId+'-'+d_group_l1.userId+'" name="personGroup'+d_group_l1.groupId+'" value="'+d_group_l1.userId+','+d_group_l1.displayName+','+d_group_l1.orderNo+'" onClick="selectControl.selectUserDept(this,personGroup'+d_group_l1.groupId+',\''+selectId+'\',\''+openPersonDivId+'\')"> '); 
					lv1_user.push('<a href="#" onclick="selectControl.showPersonInfo('+d_group_l1.userId+');"><span>'+d_group_l1.displayName +'</span></a></label>');
				}else{
					lv1_user.push('<label class="radio inline"> ');
					lv1_user.push('<input type="radio" id="'+d_group_l1.userId+'-'+d_group_l1.groupId+'" name="selectUser" value="'+d_group_l1.userId+','+d_group_l1.displayName+'" onClick="selectControl.selectUser(this,\''+selectId+'\',\''+openPersonDivId+'\')"> ');
					lv1_user.push('<a href="#" onclick="selectControl.showPersonInfo('+d_group_l1.userId+');"><span>'+d_group_l1.displayName +'</span></a></label>');
				}
			}
			lv1_user.push('</div>');
			lv1_dept.push('</div>');
			li.push(lv1_dept.join('') + lv1_user.join('') + '</div></li>');
			h.push(li.join(''));
		}
	}else{
		var lv1_dept = ['<div style="text-align:center;">'+$.i18n.prop("JC_SYS_007")+'</div>'];//没有符合查询条件的记录
		h.push(lv1_dept.join(''));
	}
	h.push('</ul>');
	h.push('</section>');
	h.push('<section class="fl m-l pos-rlt">');
	h.push('<select id="'+selectId+'" name="'+selectId+'" multiple="false" class="w170 tree-scroll-right tree-select"></select>');
	h.push('<div class="tree-sort"><a href="#" onClick="sort(\''+selectId+'\');"><i id="sort" class="fa fa-sort-shang"></i></a></div>');
	h.push('<div class="tree-move"> ');
	h.push('<a href="#" class="tree-move-up" onClick="up(\''+selectId+'\');"><i class="fa fa-caret-up"></i></a> ');
	h.push('<a href="#" class="tree-move-down" onClick="down(\''+selectId+'\');"><i class="fa fa-caret-down"></i></a></div>');
	h.push('</section>');
	var showlist = $(h.join(''));
	return showlist;
}

/**
 * 显示人员详细信息
 */
selectControl.showPersonInfo = function(userId){
	var url = getRootPath()+"/system/getUserById.action";
	$.ajax({
		async: false,
		url : url,
		type : 'post',
		data: {
			id: userId
		},
		success : function(data) {
			var dateTime = new Date().getTime(),
			showPersonInfo = "showPersonInfo" + dateTime,
			x = 1000,
		    y = 10,
			info = ['<div class="modal fade panel" id="'+showPersonInfo+'" aria-hidden="false">'];
			info.push('<div class="modal-dialog"><div class="modal-content"><div class="modal-header"><button type="button" class="close" data-dismiss="modal">×</button><h4 class="modal-title">用户基本信息</h4></div><div class="modal-body dis-table" style="max-height: 661.5999999999999px;"><section class="dis-table-cell" style="width:162px;">');
			
			if(data.photo != null && data.photo != "")
				/*info.push('<img src="'+getRootPath()+'/'+data.photo+'" width="147" height="177">');*/
				info.push('<img src="'+data.photo+'" width="147" height="177">');
			else
				info.push('<img src="'+getRootPath()+'/img/none.jpg" width="147" height="177">');
			info.push('</section><section class="panel-tab-con dis-table-cell" style="padding-bottom:20px;"><table class="basicMsg" id="pInfo"><tbody><tr><td class="w115">用户显示名</td>');
			if(data.displayName != null && data.displayName != "")
				info.push('<td>'+data.displayName+'</td>');
			else
				info.push('<td></td>');
			info.push('</tr><tr><td>所在部门</td>');
		    if(data.deptName != null && data.deptName != "")
		    	info.push('<td>'+data.deptName+'</td>');
		    else
		    	info.push('<td></td>');
		    info.push('</tr><tr><td>职务</td>');
		    if(data.dutyIdValue != null && data.dutyIdValue != "")
		    	info.push('<td>'+data.dutyIdValue+'</td>');
		    else
		    	info.push('<td></td>');
		    info.push('</tr><tr><td>级别</td>');
		    if(data.levelValue != null && data.levelValue != "")
		    	info.push('<td>'+data.levelValue+'</td>');
		    else
		    	info.push('<td></td>');
		    info.push('</tr><tr><td>用户性别</td>');
		    if(data.sexValue != null && data.sexValue != "")
		    	info.push('<td>'+data.sexValue+'</td>');
		    else
		    	info.push('<td></td>');
		    info.push('</tr><tr><td>用户手机号码</td>');
		    if(data.mobile != null && data.mobile != "")
		    	info.push('<td>'+data.mobile+'</td>');
		    else
		    	info.push('<td></td>');
		    info.push('</tr><tr><td>用户邮箱</td>');
		    if(data.email != null && data.email != "")
		    	info.push('<td>'+data.email+'</td>');
		    else
		    	info.push('<td></td>');
		    info.push('</tr><tr><td>用户办公电话</td>');
		    if(data.officeTel != null && data.officeTel != "")
		    	info.push('<td>'+data.officeTel+'</td>');
		    else
		    	info.push('<td></td>');
		    info.push('</tr><tr><td>直接领导</td>');
		    if(data.leaderIdValue != null && data.leaderIdValue != "")
		    	info.push('<td>'+data.leaderIdValue+'</td>');
		    else
		    	info.push('<td></td>');
		    info.push('</tr></tbody></table></section></div></div></div></div>');
			$(info.join('')).appendTo("body");		
			$("#"+showPersonInfo).modal("show");
		},
		error: function(){
			msgBox.tip({content: "获取人员详细信息失败", type:'fail'});
		}
	});
}

/**
 * 人员控件的展开、收起
 */
selectControl.openPutAwayClear = function(controlId){
	var ell = $("#"+controlId);
	var el = ell.closest(".select2-wrap") || null;
	var $parent = el.find(".selection-tree-btn").parent();
	//var tree_lh = $parent.find(".select2-choices").height();
	var tree_lh = $parent.find(".select2-choices").actual("height");
	if(tree_lh >= 66){
		el.find(".select2-container").css("max-height","67px");
		el.find(".selection-tree-btn").show();
		if(zkAndSqCount==0){
			el.find(".selection-tree-btn .sq").hide();
			el.find(".selection-tree-btn .zk").show();
			//zkAndSqCount++;
		}else{
			el.find(".selection-tree-btn .sq").show();
			el.find(".selection-tree-btn .zk").hide();
		}
	}else{
		el.find(".selection-tree-btn .sq").hide();
		el.find(".selection-tree-btn .zk").hide();
		el.find(".selection-tree-btn").hide();
	}
	$parent.find(".zk").click(function(){
		var $this = this;
		var $parent = el.find(".selection-tree-btn");
		el.find(".select2-container").css("max-height","100%");
		$($this).hide();
		$parent.find(".sq").show();
		selectControl.setPaddingTop($parent);
	});
	$parent.find(".sq").click(function(){
		var $this = this;
		var $parent = el.find(".selection-tree-btn");
		el.find(".select2-container").css("max-height","67px");
		$($this).hide();
		$parent.find(".zk").show();
		if(typeof resetPostscript == 'function'){
			resetPostscript();
		}
		selectControl.setPaddingTop($parent);
	});
}

/**
 * 清除select2验证信息
 * @params controlId 控件ID
 * @params isCheckOrRadio true多选,false单选
 */
selectControl.removeValidSelect2 = function(controlId, isCheckOrRadio){
	$("#s2id_"+controlId).removeClass("error"); 
	if(isCheckOrRadio){
		$("#s2id_"+controlId).parent().parent().next(".help-block").html("");
	}else{
		$("#s2id_"+controlId).parent().next(".help-block").html("");
	}
}

/**
 * 人员选择框select2只读
 */
selectControl.select2Readonly = function(controlId){
	$("#"+controlId).select2("readonly", true);
}

/**
 * 人员选择框select2隐藏--暂时停用
 */
selectControl.select2Hide = function(controlId){
	$("#"+controlId).parent().parent().hide();
}

/**
 * 递归循环添加人员
 */
selectControl.getAllUser = function(dept, personData){
	if(dept != null && dept.length > 0){
		var dept_len = dept.length;
		for(var i=0;i<dept_len;i++){
			var dept_i = dept[i];
			if(dept_i.user != null && dept_i.user.length > 0){
				var dept_user_len = dept_i.user.length;
				for(var j=0;j<dept_user_len;j++){
					var dept_user_j = dept_i.user[j];
					personData.push({
						id : dept_user_j.id,									//ID
						text : dept_user_j.displayName,						//显示的内容
						orderNo: dept_user_j.orderNo,							//排序
						jp: Pinyin.GetJP(dept_user_j.displayName)			//汉字的简拼
					});
				}
				if(dept_i.subDept != null && dept_i.subDept.length > 0){
					selectControl.getAllUser(dept_i.subDept, personData);
				}
			}else{
				if(dept_i.subDept != null && dept_i.subDept.length > 0){
					selectControl.getAllUser(dept_i.subDept, personData);
				}
			}
		}
	}
}

/**
 * 重置弹出层的上边距
 */
selectControl.setPaddingTop = function(parent){
	var modal = parent.closest(".modal");
	if(modal){
		modal.modal("setPaddingTop");
	}
}

/**
 * 设置获取焦点马上失去焦点
 */
selectControl.setFocus = function(controlId){
	$("#s2id_input_"+controlId).focus();
}

/**
 * 设置获取焦点马上失去焦点
 */
selectControl.setBlue = function(controlId){
	$("#s2id_input_"+controlId).blur();
}

/**
 * 验证人员是否有选中的
 */
selectControl.checkSelectIsNull = function(openPersonDivId, selectControlId){
	var users = [];
	$("#"+openPersonDivId+" select[name='"+selectControlId+"']").find("option").each(function(i, val){
		var gv = val.value.split(",");
		users[i] = {
			id: gv[0],
			text: val.text
		}
    });
	if(users.length > 0){
		return true;
	}else{
		return false;
	}
}

function select_tree(el){
	var el = el.closest(".select2-wrap") || null;
	el.find(".selection-tree-btn .sq").hide();
	var $parent = el.find(".selection-tree-btn").parent();
	//var tree_lh = $parent.find(".select2-choices").height();
	var tree_lh = $parent.find(".select2-choices").actual("height");
	if(tree_lh >= 66){
		el.find(".select2-container").css("max-height","67px");
		el.find(".selection-tree-btn").show();
		if(zkAndSqCount==0){
			el.find(".selection-tree-btn .sq").hide();
			el.find(".selection-tree-btn .zk").show();
			//zkAndSqCount++;
		}else{
			el.find(".selection-tree-btn .sq").show();
			el.find(".selection-tree-btn .zk").hide();
		}
	}else{
		el.find(".selection-tree-btn .sq").hide();
		el.find(".selection-tree-btn .zk").hide();
		el.find(".selection-tree-btn").hide();
	}
	$parent.find(".zk").click(function(){
		var $this = this;
		var $parent = el.find(".selection-tree-btn");
		el.find(".select2-container").css("max-height","100%");
		$($this).hide();
		$parent.find(".sq").show();
	});
	$parent.find(".sq").click(function(){
		var $this = this;
		var $parent = el.find(".selection-tree-btn");
		el.find(".select2-container").css("max-height","67px");
		$($this).hide();
		$parent.find(".zk").show();
		if(typeof resetPostscript == 'function'){
			resetPostscript();
		}
	});
}

/**
 * 关闭
 */
selectControl.singleClose  = function(){
	
}

