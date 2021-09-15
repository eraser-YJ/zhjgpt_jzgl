!(function() {
	/**
	 * [JCTree 人员选择树]
	 * 本控件依赖以下控件
	 *  	JQuery 		控件
	 *  	select2 	选择控件
	 *		zTree 		控件
	 * 		ChinesePY 	汉字转拼音
	 *  文档查看
	 *  http://192.168.200.212:8090/pages/viewpage.action?pageId=328159
	 */
	var JCTree = {};
	//控件ID,Name下标标示
	JCTree.index = 0;

	var modalbody = 'body';

	JCTree.getModalBody = function(){
		return $(modalbody);
	};

	JCTree.setModalBody = function(obj){
		modalbody = obj;
	};

	JCTree.delay = function(fun,callback){
		setTimeout(function(){
			fun();
			if(callback) callback();
		}, 500);
	}

	SelectControl.DEFALUT = {
		container		: null,
		controlId 		: null,
		isCheckOrRadio	: true,
		isPersonOrOrg	: true,
		isBackgrounder  : false,
		orgOrDept		: 'all',
		classification  : true,
		isReadonly 		: false,
		echoData		: null,
		callback		: null,
		icon 			: {
			single: 'fa-user',
			multi : 'fa-users',
			org   : 'fa-group'
		},
		urls : {
			//所有部门和用户
			user 	: getRootPath()+"/api/department/getAllDeptAndUser.action",
			//所有机构和组织
			org	  	: getRootPath()+"/api/department/getOrgTree.action",
			//只有机构没有组织
			orgNoDept 	: getRootPath()+"/api/department/getAllOrgNoDeptTree.action",
			//在线
			line    : getRootPath()+"/api/department/getDeptAndUserByOnLine.action",
			//职务
			post    : getRootPath()+"/api/department/getPostAndUser.action",
			//人员组别
			personGroup : getRootPath()+"/api/department/getPersonGroupAndUser.action",
			//公共组别
			publicGroup : getRootPath()+"/api/department/getPublicGroupAndUser.action",
			//人员详细信息查询
			findById    : getRootPath()+"/system/getUserById.action",
			//人员详细信息查询
			findByIdForSub : getRootPath() + "/api/subDepartment/getUserById.action",
			//部门人员
			orgdept     : getRootPath()+"/api/department/getOrgAndPersonTree.action",
			//权限树
			ztree    	: getRootPath()+"/api/system/managerDeptTree.action",
			//左右选择
			mutual 		: getRootPath()+"/api/system/getRolesForUser.action"
		}
	};
	function SelectControl(options) {
		var x = 1000,
				y = 10;
		//显示层对象
		this.$controlDivId = options.container?$('#' + options.container):null;
		//唯一标识
		this.rand = parseInt(Math.random() * (x - y + 1) + y);
		//组织机构标识
		this.index = JCTree.index = JCTree.index + 1;
		//缓存人员数据对象
		this.userCacheData = [];
		//缓存部门人员数据对象
		this.deptCacheData = [];
		//ztree使用数据
		this.ztreeCacheData = [];
		//搜索数据缓存
		this.searchCacheData = {
			index : 0,
			val   : ''
		};
		//搜索结果缓存
		this.searchResultData = [];
		//全选按钮状态
		this.selectBoxs = false;

		this.searchIndex = 0;
		//load元素对象
		this.dataLoad = null;

		this.option = $.extend({}, SelectControl.DEFALUT, options);

		return this.initialize(this.option);
	}

	SelectControl.prototype = {
		initialize : function(option) {
			var thie = this;
			if(thie.$controlDivId)thie.$controlDivId.empty();
			if (option.isPersonOrOrg) {
				thie.getPersonDom();
			} else {
				thie.getOrgDom();
			}
		},
		//显示层静态方法
		show  : function(callback){
			var thie = this, opt  = thie.option;
			if(typeof callback === 'function') callback();
			if(opt.isPersonOrOrg){
				var mid = 'openPersonDiv'+thie.rand,
					sid = 'backValue'+thie.rand,
					idC = opt.isCheckOrRadio,
					cot = opt.controlId;
				this.openPerson(idC,mid,sid,cot);
			}else{
				var modalId = "tree"   + thie.index,
						treeId  = "myTree" + thie.index;
				thie.select2InitZtree(modalId,treeId);
			}
		},
		hide  : function(callback){
			var mid = 'openPersonDiv'+this.rand,
				sid = 'backValue'+this.rand,
				tid = 'tree'+this.index;
			if(this.option.isPersonOrOrg){
				SelectControl.personClose(mid,sid);
			}else{
				SelectControl.orgClose(tid,this.option.controlId);
			}
			if(typeof callback === 'function'){
				setTimeout(function(){callback();},350);
			}
		},
		//1005
		setData : function(str,callback){
			var opt = this.option,
				cacheData = opt.isPersonOrOrg?this.userCacheData:this.ztreeCacheData,
				datas     = [],
				backId    = opt.controlId,
				mydata    = null;

			if(str){
				//处理在ie下有时因执行顺序导致回显不显示问题
				if(typeof str === 'string'){
					var items = str.split(",");
					for(var i=0;i<items.length;i++){
						var id = items[i];
						for(var j=0;j < cacheData.length;j++){
							if(cacheData[j].id == id){
								datas.push({id:id, text:cacheData[j].text}); break;
							}
						}
					}
					if(!opt.isCheckOrRadio){
						if(datas.length == 0) return false;
					}
					mydata = opt.isCheckOrRadio ? datas : {id:datas[0].id,text:datas[0].text};
				}else{
					mydata = str;
				}

				function assignment(){
					if(opt.isBackgrounder){
						if($('#'+backId+'_backValue').length){
							$('#'+backId+'_backValue').val(JSON.stringify(mydata));
						}else{
							$('#scrollable').append("<input type='hidden' id='"+backId+"_backValue'/>");
							$('#'+backId+'_backValue').val(JSON.stringify(mydata));
						}
					}else{
						$('#'+backId).select2("data", mydata);
					}
					SelectControl.openPutAwayClear(backId);
				}
				//如果获取不到容器的宽度,说明元素是不可见状态,延时500毫秒执行赋值函数
				//避免因获取不到高度而导致的问题   如:单选宽度,多选展开收起按钮显示
				if($("#"+opt.container).width() == 0){
					JCTree.delay(assignment,callback);
				}else{
					assignment();
					if(typeof callback === 'function') callback.call(null ,str);
				}
			}
		},
		getData : function(){
			var opt = this.option;
			return opt.isBackgrounder?$('#'+opt.controlId+'_backValue').val():$('#'+opt.controlId).select2('data');
		},
		//拼装人员dom树
		getPersonDom: function() {
			var thie = this,
				opt = thie.option;

			var modalDivId = "openPersonDiv" + this.rand, 		//人员层DivId
				personBtnId = "openPersonBtn" + this.rand, 		//人员按钮ID
				selectBackValueId = "backValue" + this.rand, 	//select下拉控件ID
				allPersonBtnId = "allPersonBtn" + this.rand, 	//全选按钮ID
				okPersonBtnId = "okPersonBtn" + this.rand, 		//确认按钮ID
				searchBtnId = "searchBtn" + this.rand,
				searchInputId = "searchInput" + this.rand,
				typeList = "typeList" + this.rand,
				clearBtnId = "clearBtn" + this.rand;
			//输入框与选择按钮界面
			var pCheck = ['<div class="select2-wrap input-group w-p100">'];
			pCheck.push('<div class="fl w-p100">');
			pCheck.push('<input type="hidden" id="' + opt.controlId + '" name="' + opt.controlId.split("-")[1] + '" style="width:100%" ' + (opt.isReadonly ? "disabled" : "") + '/></div>');
			pCheck.push('<a class="btn btn-file no-all input-group-btn" ' + (opt.isReadonly ? 'style="cursor:default"' : "href='###' id='" + personBtnId + "'") + '>');
			pCheck.push('<i class="fa ' + opt.icon[opt.isCheckOrRadio ? 'multi' : 'single'] + '"></i>');
			pCheck.push('</a>');
			pCheck.push(SelectControl.getZhaiKaiDom(clearBtnId));
			pCheck.push('</div>');
			pCheck.push('<label class="help-block hide"></label>');
			var pRadio = ['<div class="select2-wrap input-group w-p100">'];
			pRadio.push('<input type="hidden" id="' + opt.controlId + '" name="' + opt.controlId.split("-")[1] + '" style="width:100%" ' + (opt.isReadonly ? "disabled" : "") + '/>');
			pRadio.push('<a class="btn btn-file no-all input-group-btn" ' + (opt.isReadonly ? 'style="cursor:default"' : "href='###' id='" + personBtnId + "'") + '>');
			pRadio.push('<i class="fa ' + (opt.isCheckOrRadio ? "fa-users" : "fa-user") + '"></i>');
			pRadio.push('</a></div>');
			pRadio.push('<label class="help-block hide"></label>');

			function getClassification(){
				var button = [];
				if(opt.classification){
					button.push('<button type="button" name="line" 		  class="btn m-r-sm"> 在线人员</button>');
					button.push('<button type="button" name="dept" 		  class="btn m-r-sm dark"> 根据组织</button>');
					button.push('<button type="button" name="post" 		  class="btn m-r-sm"> 根据职务</button>');
					button.push('<button type="button" name="personGroup" class="btn m-r-sm"> 个人组别</button>');
					button.push('<button type="button" name="publicGroup" class="btn m-r-sm"> 公共组别</button>');
				}
				return button.join('');
			}

			var pModal = ['<div class="modal fade" id="' + modalDivId + '" aria-hidden="false" openPersonNum="' + thie.rand + '">',
				'<div class="modal-dialog w1100 modal-tree" style="padding-top: 8%;">',
				'<div class="modal-content">',
				'<div class="modal-header clearfix">',
				'<button type="button" class="close" data-dismiss="modal" onclick="JCTree.personClose(\'' + modalDivId + '\',\'' + searchInputId + '\');">×</button>',
				'<h4 class="modal-title fl">人员选择</h4>',
				// '<div class="fl btn-group form-btn m-l-lg" data-toggle="buttons" id="'+typeList+'">',
				// getClassification(),
				// '</div>',
				// '<form role="search" class="fr input-append m-b-none m-r-lg">',
				// '<span class="add-on">按姓名</span> ',
				// '<input id="' + searchInputId + '" class="form-control m-r-sm" style="height: 30px;" onKeydown="JCTree.searchKeydown();">',
				// '<button type="button" class="btn" id="' + searchBtnId + '">',
				// '<i class="fa fa-search"></i>',
				// '</button>',
				// '</form>',
				'</div>',
				'<div class="jstree-father">',
				'<div class="fl btn-group form-btn m-l-lg" data-toggle="buttons" id="'+typeList+'">',
				getClassification(),
				'</div>',
				'<form role="search" class="fr input-append m-b-none m-r-lg">',
				'<span class="add-on">按姓名</span> ',
				'<input id="' + searchInputId + '" class="form-control m-r-sm" style="height: 30px;" onKeydown="JCTree.searchKeydown();">',
				'<button type="button" class="btn" id="' + searchBtnId + '">',
				'<i class="fa fa-search"></i>',
				'</button>',
				'</form>',
				'</div>',
				'<div class="loading hide" id="dataLoad' + modalDivId + '"></div>',
				'<div class="modal-body clearfix" id="modal_' + modalDivId + '"></div>',
				'<div class="modal-footer form-btn">',
				'<button id="' + okPersonBtnId + '" class="btn dark" type="button">确 定</button>'+
				(opt.isCheckOrRadio?'<button id="' + allPersonBtnId + '" class="btn" type="button" >全 选</button>':''),
				'<button id="close" class="btn" type="button" onClick="JCTree.personClose(\'' + modalDivId + '\',\'' + searchInputId + '\');">取 消</button>',
				'</div>',
				'</div>',
				'</div>',
				'</div>'];
			if(thie.$controlDivId){
				//页面dom装填
				thie.$controlDivId.html(opt.isCheckOrRadio?pCheck.join(''):pRadio.join(''));
			}
			//初始化select2和数据
			thie.initPersonData(opt.controlId, modalDivId, selectBackValueId);

			JCTree.getModalBody().append(pModal.join(''));

			thie.dataLoad = $('#dataLoad'+modalDivId);
			//打开显示层事件
			$("#"+personBtnId).on("click", function(){
				thie.selectBoxs = false;
				thie.searchCacheData.val = '';
				thie.openPerson(opt.isCheckOrRadio, modalDivId, selectBackValueId, opt.controlId);
			});
			//清空按钮事件
			if(opt.isCheckOrRadio){
				$("#"+clearBtnId).bind("click", function(){
					thie.clearValue();
					if(typeof resetPostscript == 'function'){
						resetPostscript();
					}
					$("#"+personBtnId).hide().show();
				});
			}
			//确定按钮
			$('#'+okPersonBtnId).on('click', function(){
				thie.showPersonValue(opt.controlId, selectBackValueId, opt.isCheckOrRadio,modalDivId);
			});
			//全选事件
			$('#'+allPersonBtnId).on('click',function(){
				thie.selectAllCheckbox(modalDivId,selectBackValueId);
			});
			//搜索事件
			$('#'+searchBtnId).on('click',function(){
				thie.search(searchInputId);
			});
			//切换组别事件
			if(opt.classification){
				$("#"+typeList).on('click','button',function(){
					thie.switchingType(this,searchInputId,modalDivId,selectBackValueId);
				});
			}
		},
		//拼装组织树
		getOrgDom: function() {
			var thie = this,
				opt  = thie.option,
				orgObj = {
					treeId 		: "tree"   + thie.index,//DivID
					myTreeId	: "myTree" + thie.index,//树控件ID
					orgbtnId 	: "orgbtn" + thie.index,//组织按钮ID
					openBtnId 	: "open"   + thie.index,//打开按钮ID
					clearBtnId 	: "clearBtn" + thie.index
				};
			/**
			 * 组织树界面
			 */
			var orgDom = ['<div class="modal fade" id="'+orgObj.treeId+'" aria-hidden="false">',
				'<div class="modal-dialog">',
				'<div class="modal-content">',
				'<div class="modal-header">',
				'<button type="button" class="close" data-dismiss="modal">×</button>',
				'<h4 class="modal-title">选择</h4>',
				'</div>',
				'<div class="loading hide" id="treeLoad' + orgObj.myTreeId + '"></div>',
				'<div class="modal-body">',
				'<div id="'+orgObj.myTreeId+'" class="ztree"></div>',
				'</div>',
				'<div class="modal-footer no-all form-btn">',
				'<button class="btn dark" type="button" id="'+orgObj.orgbtnId+'">确 定</button>',
				'<button class="btn" type="reset" id="close" onClick="JCTree.orgClose(\''+orgObj.treeId+'\',\''+opt.controlId+'\');">取 消</button>',
				'</div>',
				'</div>',
				'</div>',
				'</div>'];
			/**
			 * 输入框与选择按钮界面[组织控件主界面]
			 */
			var pageDom = ['<div class="select2-wrap input-group w-p100">'];
			if(opt.isCheckOrRadio)pageDom.push('<div class="fl w-p100">');//btn-in-area
			pageDom.push('<input type="hidden" id="'+opt.controlId+'" name="'+opt.controlId.split("-")[1]+'" search="search" style="width:100%"  '+(opt.isReadonly?"disabled":"")+'/>');
			if(opt.isCheckOrRadio)pageDom.push('</div>');
			pageDom.push('<a class="btn btn-file no-all input-group-btn" '+(opt.isReadonly?"style='cursor: default;'":"")+' href="###"');
			if(!opt.isReadonly){
				pageDom.push('id="'+orgObj.openBtnId+'" role="button"');
			}
			pageDom.push('><i class="fa '+opt.icon.org+'"></i></a>');
			if(opt.isCheckOrRadio){
				pageDom.push(SelectControl.getZhaiKaiDom(orgObj.clearBtnId));
			}
			pageDom.push('</div>');
			pageDom.push('<label class="help-block hide"></label>');
			if(thie.$controlDivId){
				thie.$controlDivId.html(pageDom.join(''));
			}
			JCTree.getModalBody().append(orgDom.join(''));

			thie.initOrgData();

			thie.dataLoad = $('#treeLoad'+orgObj.myTreeId);
			//清空按钮事件
			if(opt.isCheckOrRadio){
				$("#"+orgObj.clearBtnId).bind("click", function(){
					thie.clearValue();
					if(typeof resetPostscript == 'function'){
						resetPostscript();
					}
					$('#'+orgObj.openBtnId).hide().show();
				});
			}
			$('#'+orgObj.openBtnId).on('click',function(){
				thie.select2InitZtree(orgObj.treeId,orgObj.myTreeId);
			});

			$('#'+orgObj.orgbtnId).on('click',function(){
				thie.showOrgValue(orgObj.treeId,orgObj.myTreeId);
			});
		},
		/**
		 * [initPersonData 初始化人员数据和select2]
		 * @param  {[type]} controlId         [控件ID]
		 * @param  {[type]} personBtnId       [人员按钮ID]
		 * @param  {[type]} modalDivId   	  [弹出层ID]
		 * @param  {[type]} selectId  		  [选择框ID]
		 */
		initPersonData : function(controlId, modalDivId, selectId){
			var thie = this, opt  = thie.option;
			if(!thie.deptCacheData.length){
				//查询人员数据用于搜索
				//这里使用同步ajax 否则下边的静态方法获取不到thie.deptCacheData
				$.ajax({
					async : (opt.container?true:false),
					url : opt.urls.user,
					type : 'GET',
					success : function(data) {
						var datas = thie.deptCacheData = data[0];
						if (datas && datas.subDept) {
							thie.getAllUser(datas.subDept, thie.userCacheData);
						}
					},
					error: function(){
						msgBox.tip({content: "获取人员失败", type:'fail'});
					}
				});
			}
			thie.select2InitToPerson(controlId);
			if(typeof opt.ready === 'function'){
				opt.ready.call(null);
			}
		},
		/**
		 * [initOrgData 初始化组织数据]
		 * @return {[type]} [description]
		 */
		initOrgData : function(){
			var thie = this,
				opt  = thie.option,
				orgType = opt.orgOrDept;
			if(!thie.ztreeCacheData.length){
				$.ajax({
					async: (opt.container?true:false),
					url : orgType === 'onlyOrg'?opt.urls.orgNoDept:opt.urls.org,
					type : 'get',
					success : function(data) {
						if(data){
							for(var i = 0,len = data.length;i < len;i++){
								var obj;
								if(typeof opt.parseData === 'function'){
									obj = opt.parseData.call(thie , data[i]);
								}else{
									obj = thie.parseData(data[i]);
								}

								if(obj)thie.ztreeCacheData.push(obj);
							}
						}
					},
					error: function(){
						msgBox.tip({content: "获取组织失败", type:'fail'});
					}
				});
			}
			thie.select2InitToOrg(thie.ztreeCacheData);
			if(typeof opt.ready === 'function'){
				opt.ready.call(null);
			}
		},
		znode : function(node){
			var orgType = this.option.orgOrDept;
			var falg = false;
			if(orgType == 'org'){
				falg = node == 0 ? true : false;
			}else if(orgType == 'dept'){
				falg = node == 1 ? true : false;
			}else{
				falg = false;
			}
			return falg;
		},
		parseData : function(node){
			return {
				id : node.id,						//ID
				text : node.name,					//select2用于搜索数据
				name : node.name,					//ztree显示显示的内容
				queue: node.queue,					//排序
				pId: node.parentId,					//组织父节点
				deptType: node.deptType,			//组织类型
				isChecked: node.isChecked,			//是否选中
				chkDisabled : this.znode(node.deptType),//设置节点是否禁用
				jp: Pinyin.GetJP(node.name)			//汉字的简拼
			}
		},
		openPerson : function(isCheckOrRadio, modalDivId, selectId, controlId){
			this.actions.dept.call(this,modalDivId, selectId);
			$('#'+modalDivId).modal('show').find('button[name="dept"]').addClass('dark').siblings().removeClass("dark");
		},
		actions : {//1005
			//根据在线人员
			line : function(modalDivId,selectId){
				this.getAjaxData(modalDivId,selectId,'line');
			},
			//根据组织
			dept : function(modalDivId,selectId){
				$('#modal_'+modalDivId).css('height','300px');
				var thie = this,
						opt  = thie.option,
						dat = $('#'+opt.controlId+'_backValue').val(),
						data = opt.isBackgrounder?(dat ? JSON.parse(dat) : null):$('#'+opt.controlId).select2('data');
				thie.eachData = data;
				thie.loading('show');
				SelectControl.openPutAwayClear(opt.controlId);
				//延时300 填充人员，避免人员数量大时页面长时间没响应
				thie.showUserPage(thie.deptCacheData, selectId, modalDivId, opt.isCheckOrRadio);
			},
			//根据职务
			post : function(modalDivId,selectId){
				this.getAjaxData(modalDivId,selectId,'post');
			},
			//个人组别
			personGroup : function(modalDivId,selectId){
				this.getAjaxData(modalDivId,selectId,'personGroup');
			},
			//公共组别
			publicGroup : function(modalDivId,selectId){
				this.getAjaxData(modalDivId,selectId,'publicGroup');
			}
		},
		/**
		 * [getAjaxData 不同类型组别获取数据]
		 * @param  {[type]} url        [description]
		 * @param  {[type]} modalDivId [description]
		 * @param  {[type]} selectId   [description]
		 */
		getAjaxData : function(modalDivId,selectId,type){
			var thie = this, opt  = thie.option;
			thie.loading('show');
			$.ajax({
				url : opt.urls[type],
				type : 'get',
				success : function(data) {
					//这里不要用JSON.parse(data)[0]因为有些数据根目录可能有多条
					switch(type){
						case 'line':
							thie.showUserPage(data[0], selectId, modalDivId, opt.isCheckOrRadio,type);
							break;
						case 'post':
							thie.showGroupPage(data, selectId, modalDivId, opt.isCheckOrRadio,'user');
							break;
						case 'personGroup':
						case 'publicGroup':
							thie.showGroupPage(data, selectId, modalDivId, opt.isCheckOrRadio,'group');
							break;
						default :
							thie.showUserPage(data[0], selectId, modalDivId, opt.isCheckOrRadio);
					}
					thie.loading('hide');
				},
				error: function(){
					thie.loading('hide');
					msgBox.tip({content: "获取个人组别失败", type:'fail'});
				}
			});
		},
		/**
		 * [showPersonValue 人员回显[回写值]]
		 * @return {[type]} [description]
		 */
		showPersonValue : function(controlId,selectId,isCorR,modalId){
			var thie  = this, users = [];
			$("#"+selectId).find("option").each(function(i, val){
				var gv = val.value.split(",");
				users[i] = {
					id: gv[0],
					text: val.text
				};
			});

			if(users.length > 0){
				var data = isCorR?users:{id: users[0].id, text: users[0].text};
				var func = thie.option.callback;
				if(thie.option.isBackgrounder){
					var backId = thie.option.controlId;
					$('#'+backId+'_backValue').length?$('#'+backId+'_backValue').val(JSON.stringify(data)):$('#scrollable').append('<input type="hidden" id="'+backId+'_backValue" value=\''+JSON.stringify(data)+'\'/>');
					if(typeof func === 'function') func.call(undefined,data);
				}else{
					$("#"+controlId).select2("data", data);
				}
			}else{
				msgBox.info({content:'请选择人员',type:'fail'});
				return false;
			}
			thie.removeValidSelect2(controlId, isCorR);
			thie.setFocus(controlId);

			if(modalId) $('#'+modalId).modal('hide');

			JCTree.delay(function(){
				SelectControl.openPutAwayClear(controlId);
			});
			return users;
		},
		/**
		 * [showOrgValue 组织回显]
		 * @param  {[type]} modalId [description]
		 * @param  {[type]} treeId  [description]
		 * @return {[type]}         [description]
		 */
		showOrgValue : function(modalId,treeId){
			var thie = this,
				opt  = thie.option,
				backId = opt.controlId,
				$tree = $.fn.zTree.getZTreeObj(treeId);
			nodes = $tree.getChangeCheckedNodes();
			if(nodes.length == 0){
				$("#"+modalId).modal("hide");
				$('#'+backId+'_backValue').val("");
				thie.clearValue();
				return false;
			}
			var rov = [];//人员控件返回的数据集合
			$.each(nodes,function(i, val){
				rov[i] = {
					id: this.id,
					text: this.name
				};
			});

			var data = opt.isCheckOrRadio ? rov :{id: nodes[0].id, text: nodes[0].name};
			if(opt.isBackgrounder){
				var func = thie.option.callback;
				$('#'+backId+'_backValue').length?$('#'+backId+'_backValue').val(JSON.stringify(data)):$('#scrollable').append('<input type="hidden" id="'+backId+'_backValue" value=\''+JSON.stringify(data)+'\'/>');
				if(typeof func === 'function') func.call(undefined,data);
			}else{
				$("#"+opt.controlId).select2("data", data);//多选回显值
			}

			thie.removeValidSelect2(opt.controlId, opt.isCheckOrRadio);

			if(modalId) $('#'+modalId).modal('hide');
			JCTree.delay(function(){
				SelectControl.openPutAwayClear(opt.controlId);
			});
		},
		setFocus : function(id){
			$("#s2id_input_"+id).focus();
		},
		/**
		 * [showUserPage 显示人员]
		 * @param  {[Object]}  eData          [人员数据]
		 * @param  {[String]}  selectId       [select的ID]
		 * @param  {[String]}  modalDivId     [弹出层DIVID]
		 * @param  {Boolean}   isCheckOrRadio [单选或多选]
		 * @param  {String}    type 		  [显示人员类型]
		 */
		showUserPage : function(eData, selectId, modalDivId, isCheckOrRadio,type){
			var thie  = this,
				dList = ['<div>'],
				uList = ['<div>'];
			if(eData){
				if(eData.length){
					//成立的话就是有多个根节点
					for (var i = 0,len = eData.length;i < len; i++) {
						var dydata = eData[i];
						dList.push((isCheckOrRadio?'<label class="checkbox fl"><input type="checkbox" id="dept'+dydata.id+'" name="depts" onClick="JCTree.addSelect(this,\''+selectId+'\',\''+modalDivId+'\')"> <span class="fl w100">'+dydata.name+'</span></label> ':'<label class="radio fl"><span class="fl w100">'+dydata.name+'</span></label> '));

						uList.push(thie.joinString(dydata.user,isCheckOrRadio,selectId,modalDivId,((type == 'line')?'#60AAE9':'')));
					}
				}else{
					if(eData.user && eData.user.length > 0){
						dList.push((isCheckOrRadio?'<label class="checkbox fl"><input type="checkbox" id="dept'+eData.id+'" name="depts" onClick="JCTree.addSelect(this,\''+selectId+'\',\''+modalDivId+'\')"> <span class="fl w100">'+eData.name+'</span></label> ':'<label class="radio fl"><span class="fl w100">'+eData.name+'</span></label> '));
						uList.push(thie.joinString(eData.user,isCheckOrRadio,selectId,modalDivId,((type == 'line')?'#60AAE9':'')));
					}else{
						dList.push('<label class="checkbox fl"><span class="fl w100">'+eData.name+'</span></label> ');
						uList.push('<label class="checkbox inline"></label>');
					}
				}
				uList.push('</div>');
				dList.push('<a href="#" class="fr m-r tree-btn" onclick="JCTree.dataTreeToogle(this);"><i class="fa fa-chevron-up"></i></a></div>');
			}else{
				dList.push('<label class="fl"><span class="fl w100"></span></label></div> ');
				uList.push('<div style="text-align:center;">'+$.i18n.prop("JC_SYS_007")+'</div>');
				thie.loading('hide');
			}
			var showlist = $(
					'<section class="w820 fl tree-ul tree-scroll" id="searchVessel'+thie.rand+'">' +
					'<ul class="tree-horizontal clearfix">' +
					'<li>'+
					'<div class="level1 clearfix tree-list">' +
					dList.join('') +
					uList.join('') +
					'</div>'+
					'<ul id="lv"></ul>'+
					'</li>'+
					'</ul>'+
					'</section>'+
					'<section class="fl m-l pos-rlt">'+
					'<select id="'+selectId+'" name="'+selectId+'" multiple="false" class="w170 tree-scroll-right tree-select">'+
					'</select>'+(thie.option.isCheckOrRadio ?

					'<div class="tree-sort"><a href="#" onClick="JCTree.sort(\''+selectId+'\');" title="全部排序"><i id="'+selectId+'Sort" class="fa fa-sort-xia"></i></a></div>'+
					'<div class="tree-move"> '+
					'<a href="#" class="tree-move-up" onClick="JCTree.up(\''+selectId+'\');"><i class="fa fa-caret-up"></i></a> '+
					'<a href="#" class="tree-move-down" onClick="JCTree.down(\''+selectId+'\');"><i class="fa fa-caret-down"></i></a> '+
					'</div>':'')+

					'</section>'
			);
			$("#modal_"+modalDivId).html(showlist);
			if(eData && eData.subDept && eData.subDept.length > 0){
				thie.recur(eData.subDept, showlist.find("#lv"), 2, selectId, modalDivId, isCheckOrRadio,type);
			}else{
				thie.loading('hide');
			}
			//return showlist;
		},
		showGroupPage : function(eData, selectId, modalDivId, isCheckOrRadio,type){
			var thie  = this;
			var list = [];
			if(eData && eData.length){
				for (var i = 0,len = eData.length;i < len; i++) {
					var dList = ['<div>'],
							uList = ['<div>'],
							dydata = eData[i];
					list.push('<li><div class="level1 clearfix tree-list" style="'+((i>0)?"  border-top: 0 !important;":"")+'">');
					dList.push((isCheckOrRadio?'<label class="checkbox fl"><input type="checkbox" id="dept'+dydata.id+'" name="depts" onClick="JCTree.addSelect(this,\''+selectId+'\',\''+modalDivId+'\')"> <span class="fl w100">'+dydata.name+'</span></label> ':'<label class="radio fl"><span class="fl w100">'+dydata.name+'</span></label> '));
					uList.push(thie.joinString(dydata[type],isCheckOrRadio,selectId,modalDivId,null,type));
					dList.push('</div>');
					uList.push('</div>');
					list.push(dList.join('') + uList.join('') + '</div></li>');
				}
			}else{
				list.push('<div style="text-align:center;">'+$.i18n.prop("JC_SYS_007")+'</div>');
				thie.loading('hide');
			}
			var showlist = ['<section class="w820 fl tree-ul tree-scroll" id="searchVessel'+thie.rand+'">' ,
				'<ul class="tree-horizontal clearfix">' ,
				list.join('') ,
				'</ul>',
				'</section>',
				'<section class="fl m-l pos-rlt">',
				'<select id="'+selectId+'" name="'+selectId+'" multiple="false" class="w170 tree-scroll-right tree-select">',
				'</select>'];
			if(thie.option.isCheckOrRadio){
				showlist.push('<div class="tree-sort"><a href="#" onClick="JCTree.sort(\''+selectId+'\');" title="全部排序"><i id="'+selectId+'Sort" class="fa fa-sort-xia"></i></a></div>',
						'<div class="tree-move"> ',
						'<a href="#" class="tree-move-up" onClick="JCTree.up(\''+selectId+'\');"><i class="fa fa-caret-up"></i></a> ',
						'<a href="#" class="tree-move-down" onClick="JCTree.down(\''+selectId+'\');"><i class="fa fa-caret-down"></i></a></div>');
			}

			showlist.push('</section>');
			$('#modal_'+modalDivId).html(showlist.join(''));
			//return showlist;
		},
		/**
		 * [recur 	递归查询下级菜单]
		 * @param  {[type]}  deptList        [部门数据集合]
		 * @param  {[type]}  parentHtml      [上级html]
		 * @param  {[type]}  level           [level-css样式]
		 * @param  {[type]}  selectId        [select的ID]
		 * @param  {[type]}  modalId 		 [弹出层DIVID]
		 * @param  {Boolean} isCrR  		 [单选或多选]
		 */
		recur : function(deptList,  parentHtml, level, selectId, modalId, isCrR,type){
			var thie = this,
				dLen = deptList.length;
			//	index = 0,
			//	timer;
			//timer = setInterval(function(){
			//	if(index < dLen){
			//		thie.delayed(deptList[index],parentHtml, level, selectId, modalId, isCrR,type);
			//		index ++;
			//	}else{
			//		thie.switching(modalId, selectId, thie.option.isCheckOrRadio, thie.eachData);
			//		clearInterval(timer);
			//		thie.loading('hide');
			//	}
			//},30);
			for (var i = 0; i < dLen; i++) {
				thie.delayed(deptList[i],parentHtml, level, selectId, modalId, isCrR,type);
				if(i == dLen - 1){
					thie.switching(modalId, selectId, thie.option.isCheckOrRadio, thie.eachData);
					thie.loading('hide');
				}
			}
		},
		delayed : function(di,parentHtml, level, selectId, modalId, isCrR,type){
			var thie = this;
			if(di.subDept){
				var mDept = ['<div>'],
						mUser = ['<div>'];
				if(di && di.user.length > 0){
					mDept.push(isCrR?'<label class="checkbox fl"><input type="checkbox" id="dept' + di.id + '" name="depts" onClick="JCTree.addSelect(this,\''+selectId+'\',\''+modalId+'\')"> <span class="fl w100">' + di.name + '</span></label> ':'<label class="radio fl"><span class="fl w100">' + di.name + '</span></label> ');
					mUser.push(thie.joinString(di.user, isCrR, selectId, modalId, ((type == 'line')?'#60AAE9':'')));
				}else{
					mDept.push('<label class="checkbox fl"><span class="fl w100">' + di.name + '</span></label> <label class="checkbox inline"></label>');
				}
				mDept.push('<a href="#" class="fr m-r tree-btn" onclick="JCTree.dataTreeToogle(this);"><i class="fa fa-chevron-up"></i></a></div>');
				mUser.push('</div>');
				var mSub = $([
					'<li>',
					'<div class="level' + level + ' clearfix tree-list">',
					mDept.join(''),
					mUser.join(''),
					'</div>',
					'<ul id="lv' + i + '"></ul>',
					'</li>'
				].join(''));

				mSub.appendTo(parentHtml);
				thie.recur(di.subDept, mSub.children().last(), level + 1, selectId, modalId, isCrR,type);
			}else{
				var sDept = ['<div>'],
						sUser = ['<div>'];
				if(di.user && di.user.length > 0){
					sDept.push(isCrR?'<label class="checkbox fl"><input type="checkbox" id="dept' + di.id + '" name="depts" onClick="JCTree.addSelect(this,\''+selectId+'\',\''+modalId+'\')"> <span class="fl w100">' + di.name + '</span></label> ':'<label class="radio fl"><span class="fl w100">' + di.name + '</span></label> ');
					sUser.push(thie.joinString(di.user, isCrR, selectId, modalId, ((type == 'line')?'#60AAE9':'')));
				}else{
					sDept.push('<label class="checkbox fl"><span class="fl w100">' + di.name + '</span></label> ');
					sUser.push('<label class="checkbox inline"></label>');
				}
				sDept.push('</div>');
				sUser.push('</div>');
				var sSub = ['<li>',
					'<div class="level' + level + ' clearfix tree-list">',
					sDept.join(''),
					sUser.join(''),
					'</div>',
					'</li>'].join('');
				parentHtml.append(sSub);

			}
		},
		/**
		 * 拼装字符串
		 * @param  obj				数据集合
		 * @param  isCheckOrRadio	单选或多选
		 * @param  selectId			select的ID
		 * @param  modalDivId		弹出层DIVID
		 * @param  color			字体颜色
		 * @param  type				判断类型，例如是职务还是组别
		 */
		joinString : function(obj,isCheckOrRadio,selectId,modalDivId,color,type){
			var len  = obj.length,
				i    = 0,
				arys = new Array();
			for(; i<len; i++){
				var temp = obj[i];
				var isSubSystem = false;
				if (temp.subSystem != null){
					isSubSystem = temp.subSystem;
				}
				var tempId = type=='group'?temp.userId.trim():temp.id.trim();
				arys.push('<label class="'+((isCheckOrRadio)?"checkbox":"radio")+' inline"> '+
						'<input type="'+((isCheckOrRadio)?"checkbox":"radio")+
						'" id="'+(type == 'group'?temp.userId:temp.id)+
						'" dept="dept'+(function(){
							if(type == 'user'){
								return temp.dutyId;
							}
							if(type == 'group'){
								return temp.groupId;
							}
							return temp.deptId;
						}())+
						'" name="'+((isCheckOrRadio)?temp.id:'radioUser')+
						'" value="'+(function(){
							if(type == 'group'){
								return temp.userId;
							}
							return temp.id;
						}())+','+temp.displayName+','+((isCheckOrRadio)?temp.orderNo:"")+
						'" onClick="JCTree.addSelect(this,\''+selectId+'\',\''+modalDivId+'\')"> '+
						'<a href="#" onclick="JCTree.showPersonInfo('+"'"+tempId.trim()+"','"+temp.deptId+"',"+isSubSystem+');" class="'+((color)?"text-persion-line":"")+'"><span>'+temp.displayName+'</span></a></label> ');
			}
			return arys.join('');
		},
		/**
		 * [switching 按钮切换调用]
		 * @param  {[type]}  ModalId        [description]
		 * @param  {[type]}  selectId       [description]
		 * @param  {Boolean} isCheckOrRadio [description]
		 * @param  {[type]}  eData          [description]
		 */
		switching : function(ModalId, selectId, isCheckOrRadio, eData){
			var thie    = this;
			if(isCheckOrRadio){
				if(eData){
					thie.checkAddSelect(ModalId,selectId,eData);
					if(thie.userCacheData.length == eData.length){
						thie.selectBoxs = true;
					}
				}
			}else{
				thie.radioAddSelect(ModalId,selectId,eData);
			}
			$('#modal_'+ModalId).css('height','');

			thie.loading('hide');
		},
		/**
		 * [switchingType 切换人员组别]
		 * @param  {[Dom]}    ele      [description]
		 * @param  {[String]} searchId [搜索框id]
		 * @param  {[String]} modalId  [显示层id]
		 * @param  {[String]} selectId [select框id]
		 */
		switchingType : function(ele,searchId,modalId,selectId){
			var thie = this,
				type = ele.name,
				$obj = $(ele);
			$("#"+searchId).val("");
			thie.searchCacheData.val = '';
			if(thie.checkSelectIsNull(modalId, selectId)){
				msgBox.confirm({
					content: "切换页签将会取消所选择人员，是否继续？",
					type: 'fail',
					success: function(){
						thie.selectBoxs = false;
						$obj.addClass("dark").siblings().removeClass("dark");
						thie.clearValue();
						thie.eachData = thie.option.isCheckOrRadio ? [] : null;
						thie.actions[type].call(thie,modalId, selectId);
					}
				});
			}else{
				$obj.addClass("dark").siblings().removeClass("dark");
				thie.actions[type].call(thie,modalId, selectId);
			}
		},
		//
		checkAddSelect : function(modalId,selectId,eData){
			//获取所有的input标签对象,取出所有的checkbox
			var thie = this,
				dom  = null,
				$modal = $('#'+modalId);
			$modal.find(":checkbox").prop("checked", false);
			thie.clearAllUser(selectId);
			if(eData.length){
				for(var i = 0,len = eData.length;i < len;i ++){
					dom = $modal.find("[name='"+eData[i].id+"']");
					dom.prop("checked", true);
					SelectControl.addSelect(dom[0],selectId,modalId);
				}
			}else{
				if(eData){
					dom = $modal.find("[name='"+eData.id+"']");
					dom.prop("checked", true);
					SelectControl.addSelect(dom[0],selectId,modalId);
				}
			}
		},
		//modalId,selectId,eData
		radioAddSelect : function(modalId,selectId,eData){
			var thie = this,
				dom  = null,
				openDiv = $('#'+modalId);
			//如果是radio
			if(eData){
				if(eData.id){
					dom = openDiv.find('#'+eData.id);
					dom.prop("checked", true);
					SelectControl.addSelect(dom[0],selectId,modalId);
				}else{
					msgBox.tip({content: "单选回显参数不能用数组形式！！！", type:'fail'});
					return false;
				}
			}
		},

		selectAllCheckbox : function(modalId,selectId){
			var thie = this,
					opt  = thie.option,
					boxs = $('#'+modalId).find(':checkbox'),
					$select = $('#'+selectId),
					results = [];
			thie.selectBoxs = !thie.selectBoxs;
			for(var i = 0,len = boxs.length;i < len;i++){
				var box = boxs[i];
				if(box.name != 'depts'){
					var val = box.value.split(",");
					results.push("<option value='"+val[0]+","+val[2]+"' title='"+val[1]+"'>"+val[1]+"</option>");
				}
				box.checked = thie.selectBoxs;
			}
			thie.clearAllUser(selectId);
			if(thie.selectBoxs){
				$select.append(results.join(''));
			}
		},
		/**
		 * [clearAllUser 清空已选中数据]
		 * @param  {[type]} selectId [select区域id]
		 */
		clearAllUser : function(selectId){
			$("#"+selectId).empty();
		},
		/**
		 * [search 搜索人员]
		 * @param  {[String]} searchInputId [搜索输入框id]
		 * @param  {[String]} modalId       [显示层id]
		 */
		search : function(searchId){
			var thie 	 = this,
				siv 	 = $("#"+searchId).val(),			//搜索框
				container= $("#searchVessel"+thie.rand), 	//人员显示列表容器
				len 	 = thie.searchResultData.length,	//上次搜索结果
							//默认			//查询			//选中
				color     = {DEFAULT : '#555',QUERY:'#ff7800',SELEDTED : '#f4a642'};

			function getIndex(str){
				if(!str) return false;
				var cache = thie.searchCacheData,
					data  = thie.searchResultData;

				if(cache.val == str){
					if(!len) return false;
					var index = data.length == 1 ? 0 :++cache.index;
					setSearchPosition(container,data.css({'background-color':'','color':color.SELEDTED}).eq(index));
					if(data.length - 1 === index){
						cache.index = -1;
					}
					return false;
				}
				cache.val = str;
				return true;
			}

			function setSearchPosition(p,o){
				o.css({
					'background-color':color.QUERY,
					'color':'#fff'
				});
				p.scrollTop(Math.abs(o.offset().top + p.scrollTop() - p.offset().top)-100);
			}

			if(getIndex(siv)){
				thie.searchCacheData.index = 0;
				var reslut = container.find('span:contains('+siv+')').filter(function(i ,o){
					//过滤掉机构名称
					if(o.className){
						return false;
					}
					return true;
				});

				if(len){
					//thie.searchResultData.css('color',color.DEFAULT);
					thie.searchResultData.css({
						'background-color': '',
						'color':''
					});
				}
				thie.searchResultData = reslut;
				if(reslut.length){
					reslut.css('color',color.SELEDTED);
					//以后要添加下一个功能时，可以直接变换reslut对象
					setSearchPosition(container,$(reslut[0]));
				}else{
					container.scrollTop(0);
				}
			}
		},
		/**
		 * [select2InitToPerson 初始化人员树]
		 * @param  {[type]}   cacheData      [description]
		 * @param  {[type]}   controlId      [description]
		 * @param  {Boolean}  isCheckOrRadio [description]
		 * @param  {[type]}   echoData       [description]
		 * @param  {Function} callback       [description]
		 * @return {[type]}                  [description]
		 */
		select2InitToPerson : function(controlId){
			var thie = this,
				opt  = thie.option,
			    delay = $("#"+opt.container).width() == 0;

			var echoData = opt.echoData,
				datas = thie.userCacheData;
//
			$("#"+controlId).select2({						//文本框占位符显示
				allowClear	: true,						//允许清除
				maximumInputLength: 10,					//最大输入长度
				seledFun	: (typeof opt.callback === 'function'?opt.callback:undefined),
				delay : delay,
				multiple	: opt.isCheckOrRadio,//单选or多选
				placeholder : opt.placeholder || ' ',
				query: function (query){
					var text = $.trim(query.term),data = {results: []};
					if(datas.length > 0){
						$.each(datas, function(){
							if(text.length == 0 || this.text.toUpperCase().indexOf(text.toUpperCase()) >= 0
									|| this.jp.toUpperCase().indexOf(text.toUpperCase()) >= 0){
								data.results.push({id: this.id, text: this.text,disabled : (this.isChecked === true ? true : false)});
							}
						});
					}
					query.callback(data);
				}
			});
			if(echoData){
				if(opt.isBackgrounder){
					$('#scrollable').append('<input type="hidden" id="'+controlId+'_backValue" value=\''+JSON.stringify(echoData)+'\'/>');
				}else{
					if(delay && !opt.isCheckOrRadio){
						JCTree.delay(function(){
							$("#"+controlId).select2("data",echoData);
						});
					}else{
						$("#"+controlId).select2("data",echoData);
					}
				}
			}
			SelectControl.openPutAwayClear(controlId);
		},
		/**
		 * [select2InitToOrg 初始化组织树]
		 * @param  {[type]}  orgData        [组织数据*已拼装好的]
		 */
		select2InitToOrg : function(orgData){
			var thie = this,
				opt  = thie.option,
			    delay = $("#"+opt.container).width() == 0;

			$("#"+opt.controlId).select2({
				placeholder	: opt.placeholder || ' ',	//文本框占位符显示
				allowClear	: true,						//允许清除
				maximumInputLength: 10,					//最大输入长度
				seledFun	: (typeof opt.callback === 'function'?opt.callback:undefined),
				delay : delay,
				multiple	: opt.isCheckOrRadio,			//单选or多选
				query 		: function (query){
					var text = $.trim(query.term),data = {results: []};
					$.each(orgData, function(){
						if(text.length == 0 || this.text.toUpperCase().indexOf(text.toUpperCase()) >= 0
								|| this.jp.toUpperCase().indexOf(text.toUpperCase()) >= 0){
							if(opt.orgOrDept == 'org'){
								data.results.push({id: this.id, text: this.text, disabled: this.deptType==1 && this.isChecked==1?false:true});
							}else if(opt.orgOrDept == 'dept'){
								data.results.push({id: this.id, text: this.text, disabled: this.deptType==0 && this.isChecked==1?false:true});
							}else{
								data.results.push({id: this.id, text: this.text, disabled: this.isChecked==1?false:true});
							}
						}
					});
					query.callback(data);
				}
			});
			if(opt.echoData){
				if(opt.isBackgrounder){
					$('#scrollable').append('<input type="hidden" id="'+opt.controlId+'_backValue" value=\''+JSON.stringify(opt.echoData)+'\'/>');
				}else{
					if(delay && !opt.isCheckOrRadio){
						JCTree.delay(function(){
							$("#"+opt.controlId).select2("data", opt.echoData);
						});
					}else{
						$("#"+opt.controlId).select2("data", opt.echoData);
					}
				}
			}
			SelectControl.openPutAwayClear(opt.controlId);
		},
		/**
		 * [select2InitZtree 初始化ztree]
		 * @param  {[type]} modalId [description]
		 * @param  {[type]} treeId  [description]
		 */
		select2InitZtree : function(modalId,treeId){
			var thie = this, opt  = thie.option;
			/**
			 * tree控件的设置[单选][默认]
			 */
			var settingRadio = opt.setting ? opt.setting : {
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

			var settingCheck = opt.setting ? opt.setting : {
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

			var zOpt = opt.isCheckOrRadio ? settingCheck : settingRadio

			if(opt.setting){
				zOpt = opt.setting;
			}

			var zTreeObject = $.fn.zTree.init($("#"+modalId+" #"+treeId), zOpt, thie.ztreeCacheData);

			zTreeObject.expandAll(true);

			var eData = thie.returnOrgValue(opt.controlId);
			if(eData){
				$.each(eData.split(","),function(i, v){
					var node = zTreeObject.getNodeByParam("id", v.split(":")[0], null);
					if(node) zTreeObject.checkNode(node, true, false);
				});
			}
			$("#"+modalId).modal("show");

		},
		//递归循环添加人员
		getAllUser : function(dept, data){
			function recursion(d, arrays){
				if (d) {
					var dLen = d.length;
					for (var i = 0; i < dLen; i++) {
						var di = d[i];
						if (di.user) {
							var duLen = di.user.length;
							for (var j = 0; j < duLen; j++) {
								var duj = di.user[j];
								arrays.push({
									id: duj.id, //ID
									text: duj.displayName, //显示的内容
									orderNo: duj.orderNo, //排序
									jp: Pinyin.GetJP(duj.displayName) //汉字的简拼
								});
							}
							if (di.subDept) {
								recursion(di.subDept, arrays);
							}
						} else {
							if (di.subDept) {
								recursion(di.subDept, arrays);
							}
						}
					}
				}
			}
			recursion(dept,data);
		},
		/**
		 * [readonly 禁用组件]
		 */
		readonly : function(){
			var opt = this.option,
					id = opt.isPersonOrOrg?('#openPersonBtn'+this.rand):('#open'+this.index);
			$("#"+opt.controlId).select2("readonly", true);
			$(id).css('cursor','default').off().parent().find('a.i-trash').hide();
		},
		/**
		 * [unReadonly 解除禁用状态]
		 * @return {[type]} [description]
		 */
		unReadonly : function(){
			var thie = this,
					opt  = this.option,
					id 	 = opt.isPersonOrOrg?('#openPersonBtn'+this.rand):('#open'+this.index);
			if(opt.isPersonOrOrg){
				var mid  = 'openPersonDiv'+thie.rand,
						sid  =  'backValue'+thie.rand;
				$(id).css('cursor','pointer').on("click", function(){
					thie.openPerson(opt.isCheckOrRadio, mid, sid, opt.controlId);
				});
			}else{
				var treeId 	= "tree"   + thie.index,
						myTreeId= "myTree" + thie.index;
				$(id).css('cursor','pointer').on('click',function(){
					thie.select2InitZtree(treeId,myTreeId);
				});
			}
			$(id).parent().find('a.i-trash').show();
			$("#"+opt.controlId).select2("readonly", false);
		},
		returnOrgValue : function (inputId){
			var opt = this.option,
					dat = $('#'+opt.controlId+'_backValue').val(),
					datas = opt.isBackgrounder?(dat?JSON.parse(dat):null):$('#'+inputId).select2('data');
			v   = "";
			if(datas == null || datas.length == 0){ return null; }

			if(datas.length > 0){
				$.each(datas, function(j, d){
					v += d.id+":"+d.text+",";
				});
			}else{
				v += datas.id+":"+datas.text+",";
			}
			return v.substring(0, v.length-1);
		},
		/**
		 * [removeValidSelect2 清除验证错误信息]
		 * @param  {[String]}  controlId      [description]
		 * @param  {Boolean}   isCheckOrRadio [description]
		 */
		removeValidSelect2 : function(controlId, isCheckOrRadio){
			var $ele = $("#s2id_"+controlId);
			$ele.removeClass("error");
			if(isCheckOrRadio){
				$ele.parent().parent().next(".help-block").html("");
			}else{
				$ele.parent().next(".help-block").html("");
			}
		},
		/**
		 * [checkSelectIsNull 验证人员是否有选中]
		 * @param  {[String]} modalId
		 * @param  {[String]} selectId
		 * @return {[Boolean]}
		 */
		checkSelectIsNull : function(modalId, selectId){
			var users = [];
			$("#"+modalId+" select[name='"+selectId+"']").find("option").each(function(i, val){
				var gv = val.value.split(",");
				users[i] = {
					id: gv[0],
					text: val.text
				};
			});
			if(users.length > 0){
				return true;
			}else{
				return false;
			}
		},
		loading : function(type){
			this.dataLoad[type]();
		},
		clearValue : function(){
			var id = this.option.controlId;
			$("#"+id).select2("data","");
			SelectControl.openPutAwayClear(id);
		}
	};

	SelectControl.setConfig = function(options){
		options = options || {};
		for(var i in options){
			SelectControl.DEFALUT[i] = options[i];
		}
	};
	/**
	 * 取消回车搜索
	 */
	SelectControl.searchKeydown = function(){
		var ev = event || window.event;
		if(ev.keyCode == 13) {
			ev.keyCode = 0;
			ev.returnValue = false;
		}
	};
	/**
	 * 自动排序
	 * [排序标识  默认排序字段从小到大排序]
	 */
	SelectControl.sort = function(sId){
		//select容器
		var $container = $("#"+sId);
		//所有已选中人员
		var options = $container.find("option");
		if(options.length){
			var arr = [];
			options.each(function(index,item){
				var option = item.value.split(",");
				arr.push({
					text: item.text,
					value: option[0],
					orderNo: option[1]
				});
			});
			//清空原有已选人员
			$container.empty();
			//按照orderNo字段从小到大排序
			arr.sort(function(x,y){return x.orderNo-y.orderNo});
			var reslut = [];
			$.each(arr, function(index,item){
				reslut.push("<option value='"+item.value+","+item.orderNo+"'>"+item.text+"</option>");
			});
			//填装已排序好
			$container.html(reslut.join(''));
		}
	}
	/*
	 * 排序向上
	 */
	SelectControl.up = function(sId){
		var $container = $("#"+sId);
		if($container.val()){
			var selected = $container.find("option:selected");
			if(selected.length > 1){
				msgBox.tip({content: "请选择一项进行调整", type:'fail'});
			}else{
				var optionIndex = $container.get(0).selectedIndex;
				if(optionIndex > 0) selected.insertBefore(selected.prev('option'));
			}
		}
	}
	/*
	 * 排序向下
	 */
	SelectControl.down = function(sId){
		var $container = $("#"+sId);
		if($container.val()){
			var selected = $container.find("option:selected");
			if(selected.length > 1){
				msgBox.tip({content: "请选择一项进行调整", type:'fail'});
			}else{
				var oIndex = $container.get(0).selectedIndex;
				var olength= $container[0].options.length;
				if(oIndex < (olength - 1)) selected.insertAfter(selected.next('option'));
			}
		}
	}
	/**
	 * [showPersonInfo 显示人员详细信息]
	 * @param  {[type]} userId [description]
	 */
	SelectControl.showPersonInfo = function(userId,deptId,isSubSystem){
		var thie = this,url  = this.DEFALUT.urls.findById;
		if (isSubSystem) {
			url = this.DEFALUT.urls.findByIdForSub;
		}
		function getName(name){
			return name?name:'';
		}
		var modalDom = [
			'<div class="modal fade panel" id="showPersonInfo'+thie.index+'" aria-hidden="false">',
			'<div class="modal-dialog">',
			'<div class="modal-content">',
			'<div class="modal-header">',
			'<button type="button" class="close" data-dismiss="modal">×</button>',
			'<h4 class="modal-title">用户基本信息</h4>',
			'</div>',
			'<div class="modal-body dis-table" id="showPersonModal'+thie.index+'" style="max-height: 661.5999999999999px;">',
			'</div>',
			'</div>',
			'</div>',
			'</div>'
		];
		if(!$("#showPersonInfo"+thie.index).length){
			JCTree.getModalBody().append(modalDom.join(''));
		}
		$.ajax({
			url : url,
			type : 'post',
			data: {id: userId,deptId:deptId},
			success : function(data) {
				if(data){
					var infoDom = [
						'<section class="dis-table-cell" style="width:162px;">',
						'<img src="'+(getRootPath()+(data.photo?'/'+data.photo:'/images/demoimg/userPhoto.png'))+'" width="147" height="177">',
						'</section>',
						'<section class="panel-tab-con dis-table-cell" style="padding-bottom:20px;">',
						'<table class="basicMsg" id="pInfo">',
						'<tbody>',
						'<tr>',
						'<td class="w115">用户显示名</td>',
						'<td>'+getName(data.displayName),
						'<i class="fa fa-user m-l-sm" style="color:'+(data.extStr5 == 1 ?"#60aae9;":"")+'" title="'+(data.extStr5 == 1 ?"在线":"离线")+'"></i>',
						'</td>',
						'</tr>',
						'<tr>',
						'<td>所在部门</td>',
						'<td>'+getName(data.deptName)+'</td>',
						'</tr>',
						'<tr>',
						'<td>职务</td>',
						'<td>'+getName(data.dutyIdValue)+'</td>',
						'</tr>',
						'<tr>',
						'<td>级别</td>',
						'<td>'+getName(data.levelValue)+'</td>',
						'</tr>',
						'<tr>',
						'<td>用户性别</td>',
						'<td>'+getName(data.sexValue)+'</td>',
						'</tr>',
						'<tr>',
						'<td>用户手机号码</td>',
						'<td>'+getName(data.mobile)+'</td>',
						'</tr>',
						'<tr>',
						'<td>用户邮箱</td>',
						'<td>'+getName(data.email)+'</td>',
						'</tr>',
						'<tr>',
						'<td>用户办公电话</td>',
						'<td>'+getName(data.officeTel)+'</td>',
						'</tr>',
						'<tr>',
						'<td>直接领导</td>',
						'<td>'+getName(data.leaderIdValue)+'</td>',
						'</tr>',
						'</tbody>',
						'</table>',
						'</section>'
					];
					$("#showPersonModal"+thie.index).html(infoDom.join(''));
					$("#showPersonInfo"+thie.index).modal("show");
				}
			},
			error: function(){
				msgBox.tip({content: "获取人员详细信息失败", type:'fail'});
			}
		});
	};
	/**
	 * [personClose 关闭人员选择树]
	 * @param  {[type]} modalId  [description]
	 * @param  {[type]} searchId [description]
	 */
	SelectControl.personClose = function(modalId, searchId){
		$("#"+searchId).val("");
		$("#"+modalId).modal("hide");
		// allcheckboxFlag = true;
		// clearInterval(JCTree.userList.timer);
		// $("#"+openPersonDivId).modal("hide");
		// if(isOP != null || isOP != undefined)
		// 	isOpenSinglePerson = true;
	};
	SelectControl.orgClose = function(modalId,controlId){
		$("#"+modalId).modal("hide");
		SelectControl.openPutAwayClear(controlId);
	};
	/**
	 * 人员界面收缩事件
	 */
	SelectControl.dataTreeToogle = function(obj){
		$(obj).find("i").toggleClass("fa-chevron-down").end().closest(".tree-list").next().slideToggle();
		return false;
	};
	/**
	 *拼装右侧展开按钮
	 */
	SelectControl.getZhaiKaiDom = function(clearBtnId) {
		var str = '<div class="input-group-btn m-l-xs selection-tree-btn fr">' +
				'<a class="a-icon i-trash fr m-b" href="###" id="' + clearBtnId + '"><i class="fa fa-trash"></i>清空</a>' +
				'<a class="a-icon i-new zk fr" href="###"><i class="fa fa-chevron-down"></i>展开</a>' +
				'<a class="a-icon i-new sq fr" href="###"><i class="fa fa-chevron-up"></i>收起</a></div>';
		return str;
	};
	/**
	 * 选择部门级联人员操作
	 * @param obj				dom对象
	 * @param selectId			select的ID
	 * @param modalId			弹出层DIVID
	 *
	 * 现在有过滤重复ID人员,因有人会出现在不同机构下
	 */
	SelectControl.addSelect = function(obj ,selectId, modalId){
		if(!obj) return false;
		var openDiv = $("#"+modalId),
				selDiv  = $("#"+selectId),
				filterObj = {},
				deptId  = $(obj).attr('dept'),
				value   = obj.value.split(","),
				isOps   = 0;
		if(deptId){
			if(obj.checked){
				//是单选就先清空
				if(obj.type == 'radio'){selDiv.empty();}
				var isAdd = SelectControl.selectUserFilter(selDiv.find("option"),value[1]);
				selDiv.append("<option value='"+value[0]+","+value[2]+"' title='"+value[1]+"'>"+value[1]+"</option>");
			}else{
				selDiv.find("option[value='"+value[0]+","+value[2]+"']").remove();
			}
			isOps = openDiv.find("input[dept='"+deptId+"']").not(":checked").length;
			openDiv.find("#"+deptId).prop("checked", !isOps);
		}else{
			var $objs  = openDiv.find("input[dept='"+obj.id+"']");
			var filterObj = {};
			for(var i = 0,len = $objs.length; i < len; i++){
				var v = $objs[i].value.split(",");
				$objs[i].checked = obj.checked;
				selDiv.find("option[value='"+v[0]+","+v[2]+"']").remove();
				if(!filterObj['jctree'+v[0]]){
					filterObj['jctree'+v[0]] = true;
					if(obj.checked){
						selDiv.append("<option value='"+v[0]+","+v[2]+"' title='"+v[1]+"'>"+v[1]+"</option>");
					}
				}
			}
		}
	};
	/**
	 * 过滤选中人员
	 * @param objs				select下所有的option
	 * @param str				需要过滤的字符串
	 */
	SelectControl.selectUserFilter = function(objs,str){
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
	};
	/**
	 * 设置样式，使ie浏览器，重新绘制当前dom元素，避免图标位置不正确
	 * @param dom
	 */
	SelectControl.ieReflow = function(dom){
		dom.style.cssText = dom.style.cssText + ';zoom : 1.00'+(Math.floor(Math.random()*11))+';'
	}
	/**
	 * [openPutAwayClear 人员控件的展开、收起]
	 * @param  {[String]} controlId [控件ID]
	 * 此方法有待优化
	 */
	SelectControl.openPutAwayClear = function(controlId){
		var ell = $("#"+controlId);
		if(ell.length){
			var $ele  = ell.closest(".select2-wrap");
			if($ele){
				$btns = $ele.find(".selection-tree-btn"),
				$open = $btns.find('.zk'),
				$close= $btns.find('.sq'),
				$container = $ele.find('.select2-container'),
				$openBtn = $ele.find('.btn-file');
				var height = $ele.find(".select2-choices").actual("height");
				if(height > 70){
					$container.css("max-height","67px");
					$btns.show(),$close.hide(),$open.show();
				}else{
					$btns.hide(),$close.hide(),$open.hide();
				}
				SelectControl.ieReflow($openBtn[0]);
				$open.on('click',function(){
					$container.css("max-height","100%");
					$close.show();
					$(this).hide();
					SelectControl.ieReflow($openBtn[0]);
				});

				$close.on('click',function(){
					$container.css("max-height","67px");
					$(this).hide();
					$open.show();
					SelectControl.ieReflow($openBtn[0]);
					if(typeof resetPostscript == 'function'){
						resetPostscript();
					}
				});
			}
		}
	};
	/**
	 * [SelectMove 组织人员控件 左右移动方式]
	 *
	 * @param {[type]} options [description]
	 */
	function SelectMove(options){
		this.index = JCTree.index = JCTree.index + 1;
		this.select2CacheData = [];
		this.ztreesCacheData   = [];
		this.$zTree = null;
		this.option = $.extend({}, this.getOptions(options), options);
		return this.initialize(this.option);
	}

	SelectMove.prototype = {
		initialize : function(opt){
			this.$element = opt.container?$('#'+opt.container):null;
			this.getModalDom();
		},
		getModalDom: function(opt){
			if(this.$element) this.$element.empty();
			var opt = this.option,
					checkDom = [
						'<div class="select2-wrap input-group w-p100">',
						'<div class="fl w-p100">',
						'<input type="hidden" id="'+opt.widgetId+'" name="'+opt.widgetName+'" style="width:100%"/>',
						'</div>',
						'<a class="btn btn-file no-all input-group-btn" '+(opt.isReadonly?'style="cursor:default"':'id="'+opt.openBtnId+'"')+'>',
						'<i class="fa fa-group"></i>',
						'</a>',
						SelectControl.getZhaiKaiDom(opt.clearBtnId),
						'</div>',
						'<label class="help-block hide"></label>'
					],
					radioDom = [
						'<div class="select2-wrap input-group w-p100">',
						'<input type="hidden" id="'+opt.widgetId+'" name="'+opt.widgetName+'" style="width:100%"/>',
						'<a class="btn btn-file no-all input-group-btn" '+(opt.isReadonly?'style="cursor:default"':'id="'+opt.openBtnId+'"')+'>',
						'<i class="fa fa-group"></i>',
						'</a>',
						'</div>',
						'<label class="help-block hide"></label>'
					],
					modalDom = [
						'<div class="modal fade" id="'+opt.modalId+'" aria-hidden="false">',
							'<div class="modal-dialog modal-tree">',
								'<div class="modal-content">',
									'<div class="modal-header clearfix">',
										'<button type="button" class="close" data-dismiss="modal">×</button>',
										'<h4 class="modal-title fl">组织人员选择</h4>',
									'</div>',
									'<div class="modal-body clearfix" style="overflow-x: hidden;">',
										'<div class="w240 fl">',
										'<section class="tree-ul tree-scroll">',
										'<div id="'+opt.treeId+'" class="ztree"></div>',
										'</section>',
										'<section class="m-t-md">',
										'<select id="'+opt.selectPersonId+'" multiple="true" class="w240 tree-select"></select>',
										'</section>',
										'</div>',
										'<section class="m-l m-r fl tree-operate">',
										'<a href="#" class="tree-move-down" id="'+opt.rightId+'"><i class="fa fa-double-angle-right"></i></a> ',
										'<a href="#" class="tree-move-up" id="'+opt.leftId+'"><i class="fa fa-double-angle-left"></i></a>',
										'</section>',
										'<section class="fl pos-rlt">',
										'<select id="'+opt.selectDeptAndPersonId+'" multiple="true" class="w200 tree-scroll-right tree-s-r tree-select"></select>',
										'<div class="tree-move tree-ryzz"> ',
										'<a href="###" class="tree-move-up" id="'+opt.upId+'"><i class="fa fa-caret-up"></i></a> ',
										'<a href="###" class="tree-move-down" id="'+opt.downId+'"><i class="fa fa-caret-down"></i></a> ',
										'</div>',
										'</section>',
									'</div>',
									'<div class="modal-footer form-btn">',
										'<button id="'+opt.confirmBtnId+'" class="btn dark" type="button">确 定</button>',
										'<button class="btn" type="reset" data-dismiss="modal">取 消</button>',
									'</div>',
								'</div>',
							'</div>',
						'</div>'
					];

			if(this.$element) {
				this.$element.append(opt.single?radioDom.join(''):checkDom.join(''));
			}

			JCTree.getModalBody().append(modalDom.join(''));

			if(this.select2CacheData.length){
				this.initSelect2();
			}else{
				this.initData();
			}
		},
		show : function(callback){
			var that = this,
				opt  = that.option,
				$deptAndPerson = $('#'+opt.selectDeptAndPersonId);
			$deptAndPerson.empty();
			$('#'+opt.selectPersonId).empty();
			setTimeout(function(){
				var data =  opt.isBackgrounder ? (function(){
					var reslut =  $('#'+opt.widgetId+'_backValue').val();
					return reslut && reslut.length ? JSON.parse(reslut) : null;
				}()) :$('#'+opt.widgetId).select2("data");

				if(opt.single &&　data){
					data = [data];
				}

				if(data){
					for (var i = 0,len = data.length;i < len;i++){
						var v = data[i];
						$deptAndPerson.append("<option value='"+v.id+","+v.type+"'>"+(v.text+''+(v.type == 1?opt.surnamePerson:opt.surnameDept))+"</option>");
					}
				}
				that.initZtree(data);
				that.initEvent();
				$("#"+that.option.modalId).modal("show");
			} , 10);
			if(typeof callback === 'function'){
				setTimeout(function(){callback.call(that);},350);
			}
		},
		hide : function(callback){
			$("#"+this.option.modalId).modal("hide");
			if(typeof callback === 'function')callback.call(this);
		},
		readonly : function(){
			var opt = this.option,
					$select = $('#'+opt.widgetId);
			$select.select2("readonly", true);
			$("#"+opt.openBtnId).css('cursor','default').off().parent().find('a.i-trash').hide();
		},
		unReadonly : function(){
			var opt = this.option,
					$select = $('#'+opt.widgetId);
			$select.select2("readonly", false);
			$("#"+opt.openBtnId).css('cursor','pointer').parent().find('a.i-trash').show();
			this.initEvent();
		},
		//1005
		setData : function(str , callback){
			var opt = this.option,
				that = this,
				cacheData = this.ztreesCacheData,
				datas     = [], tObj = null;

			if(typeof str === 'string'){
				var items = str.split(",");
				for(var i=0;i<items.length;i++){
					var id = items[i];
					for(var j=0;i<cacheData.length;j++){
						if(cacheData[j].id == id){
							datas.push({id:id, text:cacheData[j].text});
							break;
						}
					}
				}
				if(opt.single){
					if(datas.length == 0){
						return false;
					}
				}
				tObj = opt.single ? {id:datas[0].id,text:datas[0].text} : datas;
			}else{
				tObj = str;
			}

			function assignment(){
				that.setElementData(str , false);
			}

			if($("#"+opt.container).width() == 0 && opt.single){
				JCTree.delay(assignment,callback);
			}else{
				assignment();
				if(typeof callback === 'function') callback.call(null ,str);
			}
		},
		setElementData : function(obj , falg){
			var that = this;
			var opt = this.option;
			if(obj){
				if(opt.isBackgrounder){
					if(!$('#'+opt.widgetId+'_backValue').length){
						$('body').append("<input type='hidden' id='"+opt.widgetId+"_backValue'/>");
					}
					$('#'+opt.widgetId+'_backValue').val(JSON.stringify(obj));
				}else{
					if(opt.single && Object.prototype.toString.call(obj) === '[object Array]'){
						obj = obj[0];
					}
					$('#'+opt.widgetId).select2("data", obj);
					SelectControl.openPutAwayClear(opt.widgetId);
				}
				if(falg && typeof opt.callback === 'function'){
					opt.callback.call(that,obj);
				}
				that.removeValidSelect2(opt.controlId ,opt.single);
			}
		},
		removeValidSelect2 : function(controlId, single){
			var $ele = $("#s2id_"+controlId);
			$ele.removeClass("error");
			if(single){
				$ele.parent().next(".help-block").hide();
			}else{
				$ele.parent().parent().next(".help-block").hide();
			}
		},
		getData : function(){
			var opt = this.option;
			return opt.isBackgrounder?$('#'+opt.widgetId+'_backValue').val():$('#'+opt.widgetId).select2('data');
		},
		clearValue : function(){
			var opt = this.option,
					$select = $('#'+opt.widgetId);
			opt.isBackgrounder?$('#'+opt.widgetId+'_backValue').val(''):$select.select2("data", '');
			if(!opt.single){
				SelectControl.openPutAwayClear(opt.widgetId);
			}
		},
		/**
		 * [initData 初始化数据]
		 * @return {[type]} [description]
		 */
		initData   : function(){
			var thie = this,
					opt  = thie.option;
			$.ajax({
				async : (opt.container?true:false),
				url : opt.url,
				type : 'get',
				success : function(data) {
					if(!data.length) return false;
					var oData = [],
							pData = [];
					for (var i = 0,len = data.length; i < len; i++){
						var item = data[i];
						oData.push({
							id : item.id,				//ID
							text : item.name,			//显示的内容
							type : "2",					//组织类型
							isChecked: item.isChecked,
							jp: Pinyin.GetJP(item.name)	//汉字的简拼
						});

						for (var k = 0; k < item.users.length; k++){
							var obj = item.users[k];
							pData.push({
								id : obj.id,					// ID
								text : obj.displayName,			// 显示的内容
								type : "1",						// 用户类型
								isChecked: obj.isChecked,
								jp: Pinyin.GetJP(obj.displayName)// 汉字的简拼
							});
						}

						thie.ztreesCacheData.push({
							id : item.id,
							pId : item.parentId,
							name : item.name,
							deptType : item.deptType,
							iconSkin : item.deptType == 0 ? "fa-flag" : "fa-office",
							users: item.users,
							isChecked: item.isChecked,
							chkDisabled : item.isChecked == 1 ? false : true
						});
					}
					//不使用select2情况下不用初始化
					if(thie.$element) {
						thie.select2CacheData.push(oData);
						thie.select2CacheData.push(pData);
					}

					thie.initSelect2();

					if(typeof opt.ready === 'function'){
						opt.ready.call(null);
					}
				},
				error: function(){
					msgBox.tip({content: "获取人员组织失败", type:'fail'});
				}
			});
			
		},
		initSelect2 : function(){
			var thie = this,
				opt  = thie.option,
				$ele = $('#'+opt.widgetId),
				datas= thie.select2CacheData,
				delay = $("#"+opt.container).width() == 0;
			$ele.select2({
				placeholder	: opt.placeholder || ' ',	//文本框占位符显示
				allowClear	: true,						//允许清除
				maximumInputLength: 10,					//最大输入长度
				delay : delay,
				multiple 	: !opt.single,
				query		: function (query){
					var text = $.trim(query.term),data = {results: []};
					if(datas.length > 0){
						$.each(datas, function(){
							$.each(this, function(){
								if(text.length == 0 || this.text.toUpperCase().indexOf(text.toUpperCase()) >= 0
										|| this.jp.toUpperCase().indexOf(text.toUpperCase()) >= 0){
									if(this.isChecked==undefined){
										data.results.push({id: this.id, text: this.text, type: this.type, disabled: false});
									}else{
										data.results.push({id: this.id, text: this.text, type: this.type, disabled: this.isChecked==1?false:true});
									}
								}
							});
						});
					}
					query.callback(data);
				}
			});

			if(delay && opt.single){
				JCTree.delay(function(){
					thie.showValue(opt.echoData);
				});
			}else{
				thie.showValue(opt.echoData);
			}
			if(opt.isReadonly){
				$ele.select2("readonly", true);
			}
			thie.initEvent();
		},
		initZtree : function(data){
			var thie = this,
					opt  = thie.option;
			var $deptAndPerson = $('#'+opt.selectDeptAndPersonId),
					setting = {
						check:{
							enable: true,		 // 设置 zTree 的节点上是否显示 checkbox/radio
							nocheckInherit: true,// 是否自动继承父节点属性
							chkStyle: "checkbox",// 勾选框类型(checkbox 或 radio)
							chkboxType: { "Y" : "s", "N" : "ps" }
						},
						view:{
							selectedMulti: true,	// 设置是否允许同时选中多个节点
							showLine 	 : true,	// 设置 zTree 是否显示节点之间的连线
							dblClickExpand : false	//关闭双击展开节点功能
						},
						data:{// 确定zTree数据不需要转换为JSON格式,true是需要
							simpleData:{enable:true}
						},
						callback:{
							beforeClick: function(treeId, treeNode){
								if(treeNode.isChecked == 0){
									return false;
								}
								return true;
							},
							// 节点改变删除select框中的数据
							beforeCheck: function(treeId, treeNode){
								var unSelectNodes = tree.getCheckedNodes(true);
								for(var i=0; i<unSelectNodes.length; i++) {
									if(unSelectNodes[i].users.length > 0){
										for(var j=0;j<unSelectNodes[i].users.length;j++){
											$("#"+opt.selectPersonId+" option[value='"+unSelectNodes[i].users[j].id+"']").remove();
										}
									}
								}
							},
							// 选中节点把人员添加到select框中
							onCheck: function(treeId, treeNode){
								var selectNodes = tree.getCheckedNodes(true);
								for(var i=0; i<selectNodes.length; i++) {
									if(selectNodes[i].users.length > 0){
										for(var j=0;j<selectNodes[i].users.length;j++){
											$("#"+opt.selectPersonId).append("<option value='"+selectNodes[i].users[j].id+"'>"+selectNodes[i].users[j].displayName+"</option>");
										}
									}
								}
							},
							// 用于捕获节点被点击的事件回调函数
							onClick: function(event, treeId, treeNode, clickFlag){
							},
							// 节点双击的事件回调函数
							onDblClick: function(event, treeId, treeNode){
								var treeObj = $.fn.zTree.getZTreeObj(opt.treeId);
								var nodes = treeObj.getSelectedNodes();
								if(nodes.length==0){
									return false;
								}
								if(opt.single){
									$deptAndPerson.find("option:last").remove();
									$deptAndPerson.append("<option value='"+nodes[0].id+",2'>"+(nodes[0].name+opt.surnameDept)+"</option>");
									treeObj.cancelSelectedNode(nodes[0]);
								}else{
									$deptAndPerson.find("option[value='"+nodes[0].id+",2']").remove();
									$deptAndPerson.append("<option value='"+nodes[0].id+",2'>"+(nodes[0].name+opt.surnameDept)+"</option>");
									treeObj.cancelSelectedNode(nodes[0]);
								}
								return false;
							}
						}
					};
			var zNodes = this.ztreesCacheData;
			if(zNodes.length){
				var tree = $.fn.zTree.init($("#"+opt.treeId), setting, zNodes);
				tree.expandAll(true);
				if(data){
					for(var i = 0; i < data.length;i++){
						var v = data[i];
						if(v.type == 2){
							var node = tree.getNodeByParam("id", v.id, null);
							if(node && node.parentTId)tree.checkNode(node, true, false, true);
						}
					}
				}
			}
		},
		/**
		 * [initEvent 初始化事件]
		 * @return {[type]} [description]
		 */
		initEvent  : function(){
			var thie = this,
					opt  = thie.option,
					$deptAndPerson = $('#'+opt.selectDeptAndPersonId);
			/**
			 * [弹出显示事件]
			 */
			$("#"+opt.openBtnId).off().on('click',function(){
				$deptAndPerson.empty();
				$('#'+opt.selectPersonId).empty();
				var data =  opt.isBackgrounder ? (function(){
					var reslut =  $('#'+opt.widgetId+'_backValue').val();
					return reslut.length ? reslut : null;
				}()) :$('#'+opt.widgetId).select2("data");

				if(opt.single &&　data){
					data = [data];
				}
				if(data){
					for (var i = 0,len = data.length;i < len;i++){
						var v = data[i];
						$deptAndPerson.append("<option value='"+v.id+","+v.type+"'>"+(v.text+''+(v.type == 1?opt.surnamePerson:opt.surnameDept))+"</option>");
					}
				}
				thie.initZtree(data);
				$("#"+opt.modalId).modal("show");	//显示部门人员界面
			});
			/**
			 * 页面清空已选信息事件
			 */
			if(!opt.single){
				$("#"+opt.clearBtnId).off().on("click", function(){
					$("#"+opt.widgetId).select2("data", "");
					SelectControl.openPutAwayClear(opt.widgetId);
					if(typeof resetPostscript == 'function'){
						resetPostscript();
					}
					$("#"+opt.openBtnId).hide().show();
				});
			}
			/**
			 * [向右]
			 */
			$('#'+opt.rightId).off().on('click',function(){
				var persons = $("#"+opt.selectPersonId).find("option:selected");
				thie.actions['right'].call(thie,persons);
			});
			/**
			 * [向左]
			 */
			$('#'+opt.leftId).off().on('click',function(){
				var persons = $deptAndPerson.find("option:selected");
				thie.actions['left'].call(thie,persons);
			});
			/**
			 * [向上]
			 */
			$('#'+opt.upId).off().on('click',function(){
				thie.actions['up'].call(thie,$deptAndPerson);
			});
			/**
			 * [向下]
			 */
			$('#'+opt.downId).off().on('click',function(){
				thie.actions['down'].call(thie,$deptAndPerson);
			});
			/**
			 * [select选择框双击事件--向右添加]
			 */
			$('#'+opt.selectPersonId).off().on('dblclick',function(){
				thie.actions['dbright'].call(thie,$("#"+opt.selectPersonId),$deptAndPerson);
			});
			/**
			 * [select选择框双击事件--向左添加]
			 */
			$deptAndPerson.off().on('dblclick',function(){
				thie.actions['dbleft'].call(thie,$deptAndPerson,$("#"+opt.selectPersonId));
			});
			/**
			 * [确定按钮]
			 */
			$('#'+opt.confirmBtnId).off().on('click',function(){
				var selecteds = $deptAndPerson.find("option");
				if(selecteds.length){
					var result =[];
					for(var i = 0,len = selecteds.length;i < len;i++){
						var data  = selecteds[i],
								value = data.value.split(','),
								text  = data.text.substr(0,data.text.indexOf(value[1] == '1'?opt.surnamePerson:opt.surnameDept));
						result.push({
							id	: value[0],
							type: value[1],
							text: text
						});
					}
					$("#"+opt.selectPersonId).empty();
					$deptAndPerson.empty();
					thie.showValue(result);
				}else{
					msgBox.info({content:'请选择组织或人员',type:'fail'});
					return false;
				}
				$("#"+opt.modalId).modal("hide");
			});
		},
		/**
		 * [showValue select2回显]
		 * @param  {[type]} data [回显数据]
		 */
		showValue  : function(data){
			this.setElementData(data , true);
		},
		/**
		 * 移动人员事件
		 */
		actions : {
			up : function(select){
				if(select.val() == null){
					msgBox.tip({content: "请选择升序的组织或人员", type:'fail'});
				}else{
					var $selects = select.find('option:selected');
					if($selects.length > 1){
						msgBox.tip({content: "请选择一项进行调整", type:'fail'});
					}else{
						var optionIndex = select.get(0).selectedIndex;
						if(optionIndex > 0){
							$selects.insertBefore($selects.prev('option'));
						}
					}
				}
			},
			down : function(select){
				if(select.val() == null){
					msgBox.tip({content: "请选择升序的组织或人员", type:'fail'});
				}else{
					var $selects = select.find('option:selected');
					if($selects.length > 1){
						msgBox.tip({content: "请选择一项进行调整", type:'fail'});
					}else{
						var optionIndex = select.get(0).selectedIndex;
						if(optionIndex === 0){
							var container = select.find('option');
							if(container.length > 1){
								$selects.insertAfter(container.get(1));
							}
						}else{
							var $next = $selects.next('option');
							$next.length && $selects.insertAfter($next);
						}
					}
				}
			},
			/**
			 * 向左移动
			 */
			left : function(persons){
				var opt = this.option,
						len = persons.length;
				if(len > 0){
					for(var i = 0,len = persons.length;i < len;i++){
						var v = persons[i],
								item = v.value.split(',');
						if(item[1] == '1'){
							$("#"+opt.selectPersonId).append("<option value='"+item[0]+"'>"+v.text.substr(0,v.text.indexOf(opt.surnamePerson))+"</option>");
							$(v).remove();
						}else{
							$(v).remove();
						}
					}
				}else{
					msgBox.tip({content: "请选择要移除的组织或人员", type:'fail'});
				}
			},
			/**
			 * 向右移动
			 */
			right : function(persons){
				var opt     = this.option,
						treeObj = $.fn.zTree.getZTreeObj(opt.treeId),
						nodes   = treeObj.getSelectedNodes(),
						$select = $('#'+opt.selectDeptAndPersonId),
						len     = persons.length;
				if(nodes.length <= 0 && len <= 0){
					msgBox.tip({content: "请选择要添加的组织或人员", type:'fail'});
					return false;
				}
				//移动组织
				if(nodes.length > 0){
					opt.single?$select.empty():$select.find("option[value='"+nodes[0].id+",2']").remove();
					$select.append("<option value='"+nodes[0].id+",2'>"+nodes[0].name+''+opt.surnameDept+"</option>");
					treeObj.cancelSelectedNode(nodes[0]);
				}
				//移动人员
				if(len > 0){
					for(var i = 0,len = persons.length;i < len;i++){
						var v = persons[i];
						if(opt.single){
							$select.empty();
						}else{
							$select.find("option[value='"+v.value+",1']").remove();
							$("#"+opt.selectPersonId+" option[value='"+v.value+"']").remove();
						}
						$select.append("<option value='"+v.value+",1'>"+(v.text+opt.surnamePerson)+"</option>");
					}
				}
			},
			/**
			 *双击向右移动
			 */
			dbright : function(ele,newElement){
				var opt  = this.option,
						objs = ele.find('option:selected');
				if(objs.length){
					var v = objs[0];
					opt.single?newElement.empty():objs.remove();
					newElement.find("option[value='"+v.value+",1']").remove();
					newElement.append("<option value='"+v.value+",1'>"+(v.text+opt.surnamePerson)+"</option>");
				}
			},
			/**
			 *双击向左移动
			 */
			dbleft  : function(ele,newElement){
				var opt  = this.option,
						objs = ele.find('option:selected');
				if(objs.length){
					var v = objs[0],
							value = v.value.split(',');
					if(value[1] == '1' && !opt.single){
						newElement.find("option[value='"+v.value+",1']").remove();
						newElement.append("<option value='"+value[0]+",1'>"+v.text.substr(0,v.text.indexOf(opt.surnamePerson))+"</option>");
					}
					objs.remove();
				}
			}
		},
		/**
		 * [getOptions 获取参数]
		 * @return {[type]} [description]
		 */
		getOptions : function(opt){
			var index = this.index,
					option = {
						container 	: null,
						//  单选或多选
						single 		: false,
						//控件id
						controlId   : null,
						//url
						url  		: SelectControl.DEFALUT.urls.orgdept,
						//	主页文本框控件ID
						widgetId 	: (opt.controlId?opt.controlId:'orgAndPersonId'+index),
						//	主页文本框控件Name
						widgetName 	: (opt.controlId?opt.controlId.split("-")[1]:'orgAndPersonName'+index),
						//  回显数据
						echoData 	: null,
						//  是否只读
						isReadonly 	: false,
						//  回调函数
						callback 	: null,
						//	主页按钮ID
						openBtnId 	: 'openBtn'+ index,
						//	modalDIV的ID
						modalId  	: 'modal_'+ index,
						//	树控件的ID
						treeId 		: 'tree_'+ index,
						//	选择人员控件的ID
						selectPersonId 	: 'selectPerson'+ index,
						//	选择部门与人员的ID
						selectDeptAndPersonId 	: 'selectDeptAndPerson'+ index,
						//  向右按钮ID
						rightId 	: 'rightBtn'+ index,
						//  向左按钮ID
						leftId 		: 'leftBtn'+ index,
						//  向上按钮ID
						upId  		: 'upBtn'+ index,
						//  向下按钮ID
						downId 		: 'downBtn'+ index,
						//  确认按钮ID
						confirmBtnId: 'confirmBtn'+ index,
						//  清空按钮ID
						clearBtnId 	: 'clearBtn'+ index,

						surnamePerson : '[人员]',

						surnameDept   : '[组织]',

						isBackgrounder: false,
						ready: null
					};
			return option;
		}
	};
	/**
	 * [SelectZtree description]
	 * @param
	 * 		id
	 * 		url
	 * 		onClick
	 * 		onCheck
	 * 		expand
	 * 		selectNode
	 */
	function SelectZtree(options){
		this.ztreesCacheData   = [];
		this.option   = $.extend({}, this.getOptions(options), options);
		//常鹏添加，加入搜索框
		if (this.option.isSearch != undefined && this.option.isSearch == true) {
			if ($('#ztreeSearchKeyword').val() == undefined) {
				var parentObj = $('#' + this.option.container).parent();
				var html = '<div style="height: 35px; line-height: 35px;padding:0 5px; margin: 0 auto 10px;display: flex;"><input type="text" placeholder="关键词查询" id="ztreeSearchKeyword" name="ztreeSearchKeyword" containerId="' + this.option.container + '" style="flex:1; margin-bottom: 0 !important;"/>';
				html += '<a class="btn dark" role="button" id="ztreeSearchBtn" style="margin: 0 !important; background:#1572e8 !important; color:#fff;width:60px !important;"><i class="fa fa-search" style="font-size: 19px !important;"></i> </a></div>';
				parentObj.html(html + parentObj.html());
			}
		}

		return this.initialize(this.option);
	}

	SelectZtree.prototype = {
		initialize : function(){
			var thie = this,
				opt  = thie.option,
				$element = $("#"+opt.container),
				rootns   = null;
			if(!$element.length){
				msgBox.tip({content: "容器id填写错误！", type:'fail'});
			}
			$element.addClass('ztree');
			if(opt.zNodes) thie.ztreesCacheData = opt.zNodes;
			if(thie.ztreesCacheData.length){
				thie.initZtree($element,thie.ztreesCacheData);
				return false;
			}
			$.ajax({
				url : opt.url,
				type : 'get',
				success : function(data) {
					if(data){
						for(var i = 0,len = data.length;i < len;i++){
							var o;
							if(typeof opt.parseData === 'function'){
								o = opt.parseData.call(thie,data[i]);
							}else{
								o = thie.parseData(data[i]);
							}
							thie.ztreesCacheData.push(o);
						}
						thie.initZtree($element,thie.ztreesCacheData);
					}
				}
			});
		},
		parseData : function(node){
			return {
				id : node.id,
				pId : node.parentId,
				name : node.name,
				deptType : node.deptType,
				checkState : false,
				//iconSkin : node.deptType==0 ? "fa-flag" : "fa-office",
				iconSkin: node.deptTypeTreeIcon,
				leaderName: node.displayName,
				userType: node.userType,
				isChecked: node.isChecked,
                weight: node.weight
			}
		},
		initZtree  : function($element,nodes){
			var thie = this,
				opt  = thie.option,
				tree = $.fn.zTree.init($element, opt.setting, nodes);
			if(opt.expand){
				tree.expandAll(true);
				if(opt.selectNode){
					var node = tree.getNodeByParam("id",opt.selectNode);
					tree.selectNode(node, true);
					node.checkState = true;
					if(typeof opt.onClick === 'function'){
						opt.onClick.call(thie,$,opt.selectNode,node);
					}
				}else{
					var rootns = opt.rootNode?nodes[0]:null;
					if(opt.rootNode && rootns){
						var node = tree.getNodeByParam("id",rootns.id);
						tree.selectNode(node, true);
						node.checkState = true;
						if(typeof opt.onClick === 'function'){
							opt.onClick.call(thie,$,node.id,node);
						}
					}
				}
			}
			if(typeof opt.ready === 'function'){
				opt.ready.call(null, nodes);
			}
			thie.setScrollbar();
		},
		setScrollbar : function(){
			if(this.option.autoLeftPadding){
				var windowHeight = $("#content").height(),
						$container   = $("#scrollable");
				var treeDom = $("#LeftHeight");
				$(".tree-right").css("padding-left","215px");
				//定义高度
				var leftTreeHeight = windowHeight - 100;
				treeDom.css('height',leftTreeHeight+'px');
				$container.scroll(function() {
					var sTop = $container.scrollTop();
					treeDom[sTop >= 60?'addClass':'removeClass']('fixedNav').css('height',(sTop >= 60?(windowHeight - 40):leftTreeHeight)+"px")
				});
			}
		},
		addNodes : function(pId , node){
			var treeObj = this.getZTreeObj();
			treeObj.addNodes(pId, node);
			treeObj.refresh();
		},
		removeNode : function(node){
			var treeObj = this.getZTreeObj();
			treeObj.removeNode(node);
			treeObj.refresh();
		},
		getRootNode: function(){
			var ztreeObj = $.fn.zTree.getZTreeObj(this.option.container);
			var nodes = ztreeObj.getNodes();
			return nodes[0];
		},
		getZTreeObj : function(){
			return $.fn.zTree.getZTreeObj(this.option.container);
		},
		/**
		 * [getChildNodesId description]
		 * @param  {[type]} node [ZTree对象]
		 * @return
		 * 		字符串id以,间隔  '1231,41,51,516'
		 */
		getChildNodesId : function(node){
			var opt = this.option,
					treeObj = $.fn.zTree.getZTreeObj(opt.container),
					childNodes = treeObj.transformToArray(node),
					nodes = "";
			for(var i = 0; i < childNodes.length; i++) {
				if(childNodes[i].isChecked == 1){
					nodes = nodes + childNodes[i].id + ",";
				}
			}
			return nodes.substring(0, nodes.length-1);
		},
		getOptions : function(opt){
			function getZtreeSetting(){
				return {
					//权限树
					check:{
						enable: opt.enable,//设置 zTree 的节点上是否显示 checkbox/radio
						nocheckInherit: false,//是否自动继承父节点属性
						chkStyle: opt.chkStyle,//勾选框类型(checkbox 或 radio)
						radioType : "all"//radio的分组范围[setting.check.enable=true且setting.check.chkStyle="radio"时生效]
					},
					view:{
						selectedMulti: false,//设置是否允许同时选中多个节点
						showLine: false//设置 zTree 是否显示节点之间的连线
					},
					data:{
						simpleData:{
							enable:true//确定zTree数据不需要转换为JSON格式,true是需要
						}
					},
					callback:{
						beforeClick: typeof opt.onBeforeClick === 'function'?opt.onBeforeClick:null,
						////用于捕获 checkbox / radio 被勾选 或 取消勾选的事件回调函数
						onCheck: typeof opt.onCheck === 'function'?opt.onCheck:null,
						//用于捕获节点被点击的事件回调函数
						onClick: typeof opt.onClick === 'function'?opt.onClick:null,
						//onRemove : onRemove
					}
				};
			}
			var index = this.index,
				option = {
					container: null,
					url      : SelectControl.DEFALUT.urls.ztree,
					setting  : (opt.setting ? opt.setting : getZtreeSetting()),
					zNodes   : null,
					autoLeftPadding : true,
					rootNode : false,	//默认选中根节点
					expand   : false,	//是否默认展开
					ready 	 : null,
					selectNode : null,	//默认选中节点 需要expand为true
					enable   : false,	//是否显示 checkbox/radio
					chkStyle : 'radio'	//勾选框类型(checkbox 或 radio)
				};
			return option;
		}
	};

	function SelectMutual(options){
		this.index = JCTree.index = JCTree.index + 1;
		this.selectCacheData   = [];
		this.option   = $.extend({}, this.getOptions(options), options);
		return this.initialize(this.option);
	}

	SelectMutual.prototype = {
		initialize : function(){
			this.appendDom();
			this.initData();
			this.initEvent();
		},
		show : function(callback){
			var thie = this,
					opt  = thie.option,
					sDate= $('#'+opt.controlId).select2('data'),
					$ele = $('#'+opt.leftSelectId);
			thie.refresh();
			if(sDate){
				var results;
				if(opt.single){
					results = $ele.find("option[value="+sDate.id+"]");
					if(results.length)thie.toRight(results[0]);
				}else{
					for(var i=0;i<sDate.length;i++){
						results = $ele.find("option[value="+sDate[i].id+"]");
						if(results.length)thie.toRight(results[0]);
					}
				}
			}
			if(typeof callback === 'function') callback();
			$('#'+opt.modalId).modal('show');
		},
		hide : function(callback){
			$('#'+this.option.modalId).modal('hide');
			if(typeof callback === 'function'){
				setTimeout(function(){callback();},350);
			}
		},
		readonly : function(){
			var opt = this.option;
			$('#'+opt.openBtnId).css('cursor','default').off().parent().find('a.i-trash').hide();
			$("#"+opt.controlId).select2("readonly", true);
		},
		unReadonly : function(){
			var opt = this.option;
			$('#'+opt.openBtnId).css('cursor','pointer').parent().find('a.i-trash').show();
			this.initEvent();
			$("#"+opt.controlId).select2("readonly", false);
		},
		//1005
		setData : function(str ,callback){
			var opt = this.option,
				cacheData = this.selectCacheData,
				datas     = [],tObj = null;

			if(typeof str === 'string'){
				var items = str.split(",");
				for(var i=0;i<items.length;i++){
					var id = items[i];
					for(var j=0;i<cacheData.length;j++){
						if(cacheData[j].id == id){
							datas.push({id:id, text:cacheData[j].text});
							break;
						}
					}
				}
				if(opt.single){
					if(datas.length == 0){
						return false;
					}
				}
				tObj = opt.single?{id:datas[0].id,text:datas[0].text}:datas;
			}else if(typeof str === 'object'){
				tObj = opt.single?{id:str[0].id,text:str[0].text}:str;
			}

			function assignment(){
				$('#'+opt.controlId).select2("data", tObj);
				SelectControl.openPutAwayClear(opt.controlId);
			}

			if($("#"+opt.container).width() == 0 && opt.single){
				JCTree.delay(assignment,callback);
			}else{
				assignment();
				SelectControl.openPutAwayClear(opt.controlId);
				if(typeof callback === 'function') callback.call(null ,str);
			}
		},
		getData : function(){
			var opt = this.option;
			return $('#'+opt.controlId).select2('data');
		},
		clearValue : function(){
			var opt = this.option;
			$("#"+opt.controlId).select2("data", '');
			if(!opt.single){
				SelectControl.openPutAwayClear(opt.controlId);
			}
		},
		refresh : function(){
			var opt 	  = this.option,
					cacheData = this.selectCacheData,
					$ele  	  = $('#'+opt.leftSelectId);
			$ele.empty();
			$('#'+opt.rightSelectId).empty();
			for(var i = 0,len = cacheData.length;i < len;i++){
				var node = cacheData[i];
				$ele.append('<option value="'+node.id+'">'+node.text+'</option>');
			}
		},
		initData   : function(){
			var thie = this, opt  = thie.option,obj;
			var localData = opt.data;
			if($.isArray(localData) && localData.length){
				for(var i = 0,len = localData.length;i < len;i++){
					if(typeof opt.parseData === 'function'){
						obj = opt.parseData.call(thie , localData[i]);
					}else{
						obj = thie.parseData(localData[i]);
					}
					thie.selectCacheData.push(obj);
				}
				if(opt.container)thie.initSelect2();
			}else{
				$.ajax({
					async : (opt.container?true:false),
					url : opt.url,
					type : 'get',
					success : function(data) {
						if(data){
							for(var i = 0,len = data.length;i < len;i++){
								if(typeof opt.parseData === 'function'){
									obj = opt.parseData.call(thie , data[i]);
								}else{
									obj = thie.parseData(data[i]);
								}
								thie.selectCacheData.push(obj);
							}
							if(opt.container)thie.initSelect2();
							if(typeof opt.ready === 'function'){
								opt.ready.call(null);
							}
						}
					},
					error: function(){
						msgBox.tip({content: "获取人员失败", type:'fail'});
					}
				});
			}
		},
		initSelect2 : function(){
			var opt = this.option,
				datas = this.selectCacheData,
				delay = $("#"+opt.container).width() == 0;
			$("#"+opt.controlId).select2({
				placeholder	: opt.placeholder || ' ',	//文本框占位符显示
				allowClear	: true,						//允许清除
				maximumInputLength: 10,					//最大输入长度
				delay : delay,
				multiple: !opt.single,  //单选or多选
				query: function (query){
					var text = $.trim(query.term),data = {results: []};
					$.each(datas, function(){
						if(text.length == 0 || this.text.toUpperCase().indexOf(text.toUpperCase()) >= 0
								|| this.hp.toUpperCase().indexOf(text.toUpperCase()) >= 0
								|| this.qp.toUpperCase().indexOf(text.toUpperCase()) >= 0
								|| this.jp.toUpperCase().indexOf(text.toUpperCase()) >= 0
								|| this.wc.toUpperCase().indexOf(text.toUpperCase()) >= 0){

							data.results.push({id: this.id, text: this.text});
						}
					});
					query.callback(data);
				}
			});
			if(opt.echoData){
				var d = opt.echoData;
				if(delay && opt.single){
					JCTree.delay(function(){
						$("#" +opt.controlId).select2('data', {id: d[0].id, text: d[0].text});
					});
				}else{
					$("#" +opt.controlId).select2('data', opt.single ? {id: d[0].id, text: d[0].text} : d);
				}
			}
		},
		parseData : function(node){
			return {
				id : node.code,						//ID
				text : node.text,					//显示的内容
				hp: Pinyin.GetHP(node.text),		//两个汉字输入全拼,三个以上的汉字，第一个汉字全拼，后面的汉字简拼
				qp: Pinyin.GetQP(node.text),		//汉字的全拼
				jp: Pinyin.GetJP(node.text),		//汉字的简拼
				wc: Pinyin.getWordsCode(node.text)	//单词首字母获取
			}
		},
		initEvent : function(){
			var thie = this,
					opt  = thie.option;
			//确定
			$('#'+opt.confirmId).on('click',function(){
				thie.showMutualValue();
			});
			//打开
			$('#'+opt.openBtnId).on('click',function(){
				thie.show();
			});
			//左右移动
			$('#move'+opt.modalId).on('click','p',function(){
				var type = $(this).attr('name'),
						$ele = ((type == 'left' || type == 'leftAll')?$('#'+opt.leftSelectId):$('#'+opt.rightSelectId));
				if(opt.single && (type == 'leftAll' || type == 'rightAll')){
					return false;
				}
				thie.actions[type].call(thie,$ele,opt.single);
			});
			//左侧选择框双击
			$('#'+opt.leftSelectId).on('dblclick',function(){
				var option = $("option:selected",this)[0];
				thie.toRight(option);
			});
			//右侧选择框双击
			$('#'+opt.rightSelectId).on('dblclick',function(){
				var option = $("option:selected",this)[0];
				thie.toLeft(option);
			});
			//清空按钮
			if(!opt.single){
				$('#'+opt.clearBtnId).on('click',function(){
					$("#"+opt.controlId).select2("data", "");
					SelectControl.openPutAwayClear(opt.controlId);
					if(typeof resetPostscript == 'function'){
						resetPostscript();
					}
					$("#"+opt.openBtnId).hide().show();
				});
			}
		},
		actions : {
			left : function($ele,single){
				var opt = this.option;
				var selecteds = $ele.find('option:selected');
				if(single && selecteds.length > 1){
					msgBox.tip({content: "只能选择一个条目", type:'fail'});
					return false;
				}
				if(selecteds.length){
					this.toRight(opt.single ? selecteds[0] : selecteds);
				}
			},
			leftAll : function($ele){
				var selecteds = $ele.find('option');
				if(selecteds.length){
					this.toRight(selecteds);
				}
			},
			right : function($ele,single){
				var opt = this.option;
				var selecteds = $ele.find('option:selected');
				if(single && selecteds.length > 1){
					msgBox.tip({content: "只能选择一个条目", type:'fail'});
					return false;
				}
				if(selecteds.length){
					this.toLeft(opt.single ? selecteds[0] : selecteds);
				}
			},
			rightAll :function($ele){
				var selecteds = $ele.find('option');
				if(selecteds.length){
					this.toLeft(selecteds);
				}
			}
		},
		toLeft : function(obj){
			var opt = this.option,
					$ele= $('#'+opt.leftSelectId);
			this.move(obj,$ele,opt.single);
		},
		toRight : function(obj){
			var opt = this.option,
					$ele= $('#'+opt.rightSelectId);
			this.move(obj,$ele,opt.single);
		},
		move : function(obj,$ele,single){
			if(!obj.length){
				$ele[single?'html':'append']('<option value="'+obj.value+'">'+obj.text+'</option>');
				if(!single)$(obj).remove();
			}else{
				for(var i = 0,len = obj.length;i < len;i++){
					$ele.append('<option value="'+obj[i].value+'">'+obj[i].text+'</option>');
					$(obj).remove();
				}
			}
		},
		showMutualValue : function(){
			var thie = this,
					opt  = thie.option,
					sDate   = $("#"+opt.rightSelectId).find("option"),
					results = [];

			if(sDate.length == 0){
				msgBox.info({content:"请选择数据",type:"fail"});
				return false;
			}
			$.each(sDate,function(i, val){
				results.push({
					id: this.value,
					text: this.text
				});
			});
			$('#'+opt.controlId).select2("data", opt.single?{id: results[0].id, text: results[0].text}:results);
			$('#'+opt.modalId).modal('hide');
			SelectControl.openPutAwayClear(opt.controlId);
			thie.removeValidSelect2(opt.controlId ,opt.single);
			if(typeof opt.callback === 'function'){
				opt.callback.call(thie,results);
			}
		},
		removeValidSelect2 : function(controlId, single){
			var $ele = $("#s2id_"+controlId);
			$ele.removeClass("error");
			if(single){

				$ele.parent().next(".help-block").hide();
			}else{
				$ele.parent().parent().next(".help-block").hide();
			}
		},
		appendDom : function(){
			var opt = this.option;
			var pDom = [
						'<div class="select2-wrap input-group  w-p100">',
						(opt.single?'':'<div class="fl w-p100">'),
						'<input type="hidden" id="'+opt.controlId+'" name="'+opt.controlId.split("-")[1]+'" search="search" style="width:100%"/>',
						(opt.single?'':'</div>'),
						'<a class="btn btn-file no-all input-group-btn" href="###"  id="'+opt.openBtnId+'" role="button">',
						'<i class="fa ' + (opt.single ? "fa-user" : "fa-group") + '"></i>',
						'</a>',
						(opt.single?'':SelectControl.getZhaiKaiDom(opt.clearBtnId)),
						'</div>',
						'<label class="help-block hide"></label>'
					],
					mDom = [
						'<div class="modal fade" id="'+opt.modalId+'" aria-hidden="false">',
						'<div class="modal-dialog modal-tree">',
						'<div class="modal-content">',
						'<div class="modal-header clearfix">',
						'<button type="button" class="close" data-dismiss="modal" onClick="">×</button>',
						'<h4 class="modal-title fl">'+opt.title+'</h4>',
						'</div>',
						'<div class="modal-body clearfix">',
						'<div class="ms2side__div">',
						'<div class="ms2side__select">',
						'<div class="ms2side__header">'+opt.title+'选择框</div>',
						'<select title="'+opt.title+'选择框" name="'+opt.leftSelectId+'" id="'+opt.leftSelectId+'" size="0" multiple="multiple" class="select-list-h"></select>',
						'</div>',
						'<div class="ms2side__options" id="move'+opt.modalId+'">',
						'<p class="AddOne" title="添加" name="left"><span></span></p>',
						'<p class="AddAll" title="添加所有" name="leftAll"><span></span></p>',
						'<p class="RemoveOne" title="删除" name="right"><span></span></p>',
						'<p class="RemoveAll" title="删除所有" name="rightAll"><span></span></p>',
						'</div>',
						'<div class="ms2side__select">',
						'<div class="ms2side__header">已选'+opt.title+'框</div>',
						'<select title="已选'+opt.title+'框" name="'+opt.rightSelectId+'" id="'+opt.rightSelectId+'" size="0" multiple="multiple" class="select-list-h"></select>',
						'</div>',
						'</div>',
						'</div>',
						'<div class="modal-footer form-btn">',
						'<button class="btn dark" type="button" id="'+opt.confirmId+'">保 存</button>',
						'<button class="btn" type="button" data-dismiss="modal">关 闭</button>',
						'</div>',
						'</div>',
						'</div>',
						'</div>'
					];
			if(opt.container){
				$('#'+opt.container).html(pDom.join(''));
			}
			JCTree.getModalBody().append(mDom.join(''));
		},
		getOptions : function(opt){
			var option = {
				container : null,
				controlId : 'mutualName'+this.index+'-mutualName',
				url       : SelectControl.DEFALUT.urls.mutual,
				single    : false,
				title     : '项目',
				callback  : null,
				echoData  : null,
				ready  	  : null,
				modalId   : 'openLeftRightDiv'+this.index,
				openBtnId : 'openLeftRightBtn'+this.index,
				clearBtnId: 'leftRightClearBtn'+this.index,
				leftSelectId : 'leftList'+this.index,
				rightSelectId: 'rightList'+this.index,
				confirmId    : 'okMutualBtn'+this.index
			};
			return option;
		}
	};

	JCTree.init = function(option){
		return new SelectControl(option);
	};

	JCTree.orgDept = function(option){
		return new SelectMove(option);
	};

	JCTree.ztree = function(option){
		return new SelectZtree(option);
	};

	JCTree.mutual = function(option){
		return new SelectMutual(option);
	};

	for (var i in SelectControl) {
		JCTree[i] = SelectControl[i];
	}

	window.JCTree = JCTree;
})();

/*!

 @Name : layPage v1.3- 分页插件
 @Author: 贤心
 @Site：http://sentsin.com/layui/laypage
 @License：MIT

 */

;!function(){
"use strict";

function laypage(options){
    var skin = 'laypagecss';
    laypage.dir = 'dir' in laypage ? laypage.dir : Page.getpath + '/skin/laypage.css';
    new Page(options);
    //if(laypage.dir && !doc[id](skin)){
    //    Page.use(laypage.dir, skin);
    //}
}

laypage.v = '1.3';

var doc = document, id = 'getElementById', tag = 'getElementsByTagName';
var index = 0, Page = function(options){
    var that = this;
    var conf = that.config = options || {};
    conf.item = index++;
    that.render(true);
};

Page.on = function(elem, even, fn){
    elem.attachEvent ? elem.attachEvent('on'+ even, function(){
        fn.call(elem, window.even); //for ie, this指向为当前dom元素
    }) : elem.addEventListener(even, fn, false);
    return Page;
};

Page.getpath = (function(){
    var js = document.scripts, jsPath = js[js.length - 1].src;
    return jsPath.substring(0, jsPath.lastIndexOf("/") + 1);
}())

Page.use = function(lib, id){
    var link = doc.createElement('link');
    link.type = 'text/css';
    link.rel = 'stylesheet';
    link.href = laypage.dir;
    id && (link.id = id);
    doc[tag]('head')[0].appendChild(link);
    link = null;
};

//判断传入的容器类型
Page.prototype.type = function(){
    var conf = this.config;
    if(typeof conf.cont === 'object'){
        return conf.cont.length === undefined ? 2 : 3;
    }
};

//分页视图
Page.prototype.view = function(){
    var that = this, conf = that.config, view = [], dict = {};
    conf.pages = conf.pages|0;
    conf.curr = (conf.curr|0) || 1;
    conf.groups = 'groups' in conf ? (conf.groups|0) : 5;
    conf.first = 'first' in conf ? conf.first : '&#x9996;&#x9875;';
    conf.last = 'last' in conf ? conf.last : '&#x5C3E;&#x9875;';
    conf.prev = 'prev' in conf ? conf.prev : '&#x4E0A;&#x4E00;&#x9875;';
    conf.next = 'next' in conf ? conf.next : '&#x4E0B;&#x4E00;&#x9875;';

    if(conf.pages <= 1){
        return '';
    }

    if(conf.groups > conf.pages){
        conf.groups = conf.pages;
    }

    //计算当前组
    dict.index = Math.ceil((conf.curr + ((conf.groups > 1 && conf.groups !== conf.pages) ? 1 : 0))/(conf.groups === 0 ? 1 : conf.groups));

    //当前页非首页，则输出上一页
    if(conf.curr > 1 && conf.prev){
        view.push('<a href="javascript:;" class="laypage_prev" data-page="'+ (conf.curr - 1) +'">'+ conf.prev +'</a>');
    }

    //当前组非首组，则输出首页
    if(dict.index > 1 && conf.first && conf.groups !== 0){
        view.push('<a href="javascript:;" class="laypage_first" data-page="1"  title="&#x9996;&#x9875;">'+ conf.first +'</a><span>&#x2026;</span>');
    }

    //输出当前页组
    dict.poor = Math.floor((conf.groups-1)/2);
    dict.start = dict.index > 1 ? conf.curr - dict.poor : 1;
    dict.end = dict.index > 1 ? (function(){
        var max = conf.curr + (conf.groups - dict.poor - 1);
        return max > conf.pages ? conf.pages : max;
    }()) : conf.groups;
    if(dict.end - dict.start < conf.groups - 1){ //最后一组状态
        dict.start = dict.end - conf.groups + 1;
    }
    for(; dict.start <= dict.end; dict.start++){
        if(dict.start === conf.curr){
            view.push('<span class="laypage_curr" '+ (/^#/.test(conf.skin) ? 'style="background-color:'+ conf.skin +'"' : '') +'>'+ dict.start +'</span>');
        } else {
            view.push('<a href="javascript:;" data-page="'+ dict.start +'">'+ dict.start +'</a>');
        }
    }

    //总页数大于连续分页数，且当前组最大页小于总页，输出尾页
    if(conf.pages > conf.groups && dict.end < conf.pages && conf.last && conf.groups !== 0){
         view.push('<span>&#x2026;</span><a href="javascript:;" class="laypage_last" title="&#x5C3E;&#x9875;"  data-page="'+ conf.pages +'">'+ conf.last +'</a>');
    }

    //当前页不为尾页时，输出下一页
    dict.flow = !conf.prev && conf.groups === 0;
    if(conf.curr !== conf.pages && conf.next || dict.flow){
        view.push((function(){
            return (dict.flow && conf.curr === conf.pages)
            ? '<span class="page_nomore" title="&#x5DF2;&#x6CA1;&#x6709;&#x66F4;&#x591A;">'+ conf.next +'</span>'
            : '<a href="javascript:;" class="laypage_next" data-page="'+ (conf.curr + 1) +'">'+ conf.next +'</a>';
        }()));
    }

    return '<div name="laypage'+ laypage.v +'" class="laypage_main laypageskin_'+ (conf.skin ? (function(skin){
        return /^#/.test(skin) ? 'molv' : skin;
    }(conf.skin)) : 'default') +'" id="laypage_'+ that.config.item +'">'+ view.join('') + function(){
        return conf.skip
        ? '<span class="laypage_total"><label>&#x5230;&#x7B2C;</label><input type="number" min="1" onkeyup="this.value=this.value.replace(/\\D/, \'\');" class="laypage_skip"><label>&#x9875;</label>'
        + '<button type="button" class="laypage_btn">&#x786e;&#x5b9a;</button></span>'
        : '';
    }() +'</div>';
};

//跳页
Page.prototype.jump = function(elem){
    if(!elem) return;
    var that = this, conf = that.config, childs = elem.children;
    var btn = elem[tag]('button')[0];
    var input = elem[tag]('input')[0];
    for(var i = 0, len = childs.length; i < len; i++){
        if(childs[i].nodeName.toLowerCase() === 'a'){
            Page.on(childs[i], 'click', function(){
                var curr = this.getAttribute('data-page')|0;
                conf.curr = curr;
                that.render();

            });
        }
    }
    if(btn){
        Page.on(btn, 'click', function(){
            var curr = input.value.replace(/\s|\D/g, '')|0;
            if(curr && curr <= conf.pages){
                conf.curr = curr;
                that.render();
            }
        });
    }
};

//渲染分页
Page.prototype.render = function(load){
    var that = this, conf = that.config, type = that.type();
    var view = that.view();
    if(type === 2){
        conf.cont.innerHTML = view;
    } else if(type === 3){
        conf.cont.html(view);
    } else {
        doc[id](conf.cont).innerHTML = view;
    }
    conf.jump && conf.jump(conf, load);
    that.jump(doc[id]('laypage_' + conf.item));
    if(conf.hash && !load){
        location.hash = '!'+ conf.hash +'='+ conf.curr;
    }
};

//for 页面模块加载、Node.js运用、页面普通应用
"function" === typeof define ? define(function() {
    return laypage;
}) : "undefined" != typeof exports ? module.exports = laypage : window.laypage = laypage;

}();
if(!window.JCFF){
	var JCFF = {};
}

if(!JCFF.JCTree){
	JCFF.JCTree = {};
}
/**
 * JCTree 基类   所有人员树都继承此类
 */
JCFF.JCTree = Clazz.extend({
	//类名称, 方便调试查看
	clazzName : 'JCFF.JCTree',
	//构造器, new 执行
	construct: function(options){
		this.option = this._getOption(options);
	},
	//获取所有类型树公共默认参数
	_getOption: function(options){
		this.uuid = this._guid();
		this.$document = $(document);
		return $.extend({
		//默认参数
			container	: null,				//容器id  必填
			//生成select2的id和name  一般用于提交表单参数
			controlId	: null,
			url 		: getRootPath() + '/api/system/managerDeptTree.action',
			selectUrl 	: getRootPath() + '/api/pinDepartment/getSelUsers.action',
			cancel 		: null,				//关闭弹出时回调
			show 		: null,				//打开弹出层时回调
			single		: false,			//单选 or 多选
			funFormData : null,				//为所有请求接口追加参数
			title		: '请选择',			//弹出框title
			callback	: null,				//回调函数,添加或删除触发
			echoData	: null,				//初始化回显数据
			modalId		: 'modal-'+ this.uuid,//弹出层id
			openBtnId	: 'open-'+ this.uuid,//打开按钮id
			clearBtnId  : 'clear-'+ this.uuid,//清空按钮id
			icon 		: 'fa-user',
			isBackgrounder : false,			//是否按钮触发,不初始化select2
			ready 		: null
		},(options || {}));
	},
	//selct2容器
	_select2Template: function(){
		var opt = this.option,
			temp = [],
			_this = this;
		_this.formParams = (function(){
			var obj = {id : 'input-id-' + _this.uuid,name: 'input-name-' + _this.uuid};
			if(opt.controlId){
				var control = opt.controlId.split('-');
				if(control.length == 2){
					obj.id = control[0];
					obj.name = control[1];
				}else if(control.length == 1){
					obj.id = control[0];
					obj.name = control[0];
				}
			}
			return obj;
		})();
		temp.push('<div class="select2-wrap input-group w-p100">');
			temp.push('<div class="fl w-p100">');
				temp.push('<input type="hidden" id="'+_this.formParams.id+'" name="'+_this.formParams.name+'" style="width:100%;"/>');
			temp.push('</div>');
			temp.push('<a class="btn btn-file no-all input-group-btn" id="'+opt.openBtnId+'">');
				temp.push('<i class="fa ' + (opt.single ? "fa-user" : "fa-users") + '"></i>');
			temp.push('</a>');
			if(!opt.single && _this.addAll === undefined){
				temp.push(_this._spreadTemplate());
			}
		temp.push('</div>');
		temp.push('<label class="help-block hide"></label>');
		return temp.join('');
	},
	//展开收起
	_spreadTemplate: function(){
		return '<div class="input-group-btn m-l-xs selection-tree-btn fr" id="spread-'+this.uuid+'">' +
			'<i title="点击左侧按钮查看所有选中人员." class="fa fa-question-sign" style="position: absolute;top:-30px;left:-60px;font-size:26px;color:#f90;"></i>' +
			'<a class="a-icon i-trash fr m-b" href="return false;" id="' + this.option.clearBtnId + '"><i class="fa fa-trash"></i>清空</a>' +
			'<a class="a-icon i-new zk fr" href="return false;"><i class="fa fa-chevron-down"></i>展开</a>' +
			'<a class="a-icon i-new sq fr" href="return false;"><i class="fa fa-chevron-up"></i>收起</a>'+
			'</div>';
	},
	//初始化组件  此方法在获取机构数据后触发
	_initModule: function(){},
	//绑定基础事件 按钮打开弹出层事件和是否包含清空,展开收起...
	_addBaseEvent: function(){
		var _this = this,opt = _this.option,
		clickName = 'click.' + _this.uuid;
		if(!opt.isBackgrounder){
			var $openBtn = $('#'+ opt.openBtnId);
			//绑定打开按钮事件
			$openBtn.off(clickName+'.open').on(clickName+'.open' ,function(e){
				e.preventDefault();
				_this._open();
			});

			if(!opt.single){
				//清空已选
				$('#'+ opt.clearBtnId).off(clickName+'.clear').on(clickName+'.clear' ,function(e){
					e.preventDefault();
					_this.clearValue();
					$openBtn.hide().show();
				});
			}
		}
	},
	//绘制select2容器
	_drawSelect2: function(){
		var opt = this.option;
		if(!opt.isBackgrounder){
			this.$container = opt.container instanceof jQuery ? opt.container : $('#'+ opt.container);
			if(!this.$container.length){
				msgBox.info({content:'输入初始化容器的id',type:'fail'});
				return false;
			}
			this.$container.html(this._select2Template());
			this._initSelect2(this.$container.width() == 0);
		}
	},
	//初始化select2 插件
	_initSelect2: function(delay){
		var opt = this.option,_this = this;
		_this.$select2 = $("#"+this.formParams.id).select2({
			ajax: {
				url: opt.selectUrl,
				delay: 250,
				quietMillis: 250,
				dataType: 'json',

				data: function (params,b,c) {
					var param = {search: params.replace(/(^\s+)|(\s+$)/g,'')};
					if($.isFunction(opt.funFormData)){
						var formData = opt.funFormData();
						if($.isPlainObject(formData)){
							param = $.extend(param ,formData);
						}
					}
					//去掉空格
				    return param;
			    },
				results: function(data ,page ,query){
					var obj = {};
					if(_this.addAll){
						obj.results = [];
					}else{
						obj.results = data.length > 50 ? data.slice(0 ,50) : data;
					}
					query.callback(obj);
				},
				cache: true
			},
			minimumInputLength: 1,					//最小输入长度,满足触发请求
			delay: delay,	
			placeholder	: opt.placeholder || ' ',	//文本框占位符显示
			multiple 	: !opt.single,
			//处理手动输入回调
			seledFun  	: function(data){			//回调函数
				if(!opt.single && _this.addAll != undefined){
					if(data.length > opt.maxSelectedShowNum){
						var itemData = _this.$select2.select2('data');
						_this.selectedData = itemData;
						_this._setMultiData(itemData);
					}
					if(data && data[0] === 'jc_max_ellipsis'){
						return false;
					}
				}
				if(typeof opt.callback === 'function'){
					opt.callback(data);
				}
			},				
			allowClear	: true,						//允许清除
			maximumInputLength: 10,					//最大输入长度
			maximumSelectionSize: 40
		});

		if(!opt.single){
			_this.$select2.on('change' ,function(){
				_this._openPutAwayClear();
			});
		}

		_this.$select2Container = this.$container.find('.select2-container');

		if(opt.echoData && opt.echoData.length){
			var eData = opt.echoData;
			if(opt.single && $.isArray(opt.echoData)){
				eData = opt.echoData[0];
			}
			_this.$select2.select2('data' , eData);
		}
	},
	//展开收起事件
	_openPutAwayClear: function(){
		var $spread = $('#spread-'+this.uuid),
			$container = $("#"+'s2id_'+this.formParams.id),
			$ul = $container.find('>ul'),
			height = $ul.actual("height"),
			$unfold	= $spread.find('.zk'),
			$pack	= $spread.find('.sq');
			clickName = 'click.'+this.uuid,
			dom = $('#'+ this.option.openBtnId).get(0);

		if(height > 70){
			//$container.css("max-height","67px");
			$spread.show(),$pack.hide(),$unfold.show();
		}else{
			$spread.hide()
		}

		$unfold.off(clickName).on(clickName ,function(e){
			e.preventDefault();
			$container.css("max-height","100%");
			$pack.show(),$unfold.hide();
			dom.style.cssText = dom.style.cssText + ';zoom : 1.00'+(Math.floor(Math.random()*11))+';'
		});

		$pack.off(clickName).on(clickName ,function(e){
			e.preventDefault();
			$container.css("max-height","67px");
			$pack.hide(),$unfold.show();
			dom.style.cssText = dom.style.cssText + ';zoom : 1.00'+(Math.floor(Math.random()*11))+';'
			if(typeof resetPostscript == 'function'){
				resetPostscript();
			}
		});
	},
	//子类重写,打开弹出层事件
	_open: function(){},
	//获取已选数据
	getData: function(){
		return this.$select2.select2('data');
	},
	//添加数据
	setData: function(datas){
		var opt = this.option;
		if(!opt.isBackgrounder){
			this.$select2.select2('data',datas);
			if(!opt.single) this._openPutAwayClear();
		}
	},
	//清空已选数据
	clearValue: function(){
		var opt = this.option;
		if(!opt.isBackgrounder){
			this.$select2 && this.$select2.select2('data','');
			if(!opt.single) this._openPutAwayClear();
		}
	},
	//显示树弹出层
	show: function(){
		var opt = this.option;
		$('#'+opt.modalId).modal('show');
		if(typeof opt.show === 'function'){
			setTimeout(function(){opt.show();},350);
		}
	},
	//隐藏树弹出层
	hide: function(){
		var opt = this.option;
		if(typeof opt.cancel === 'function'){
			opt.cancel();
		}
		//this._resetParameter();
		$('#'+opt.modalId).modal('hide');
	},
	//禁用组件
	readonly: function(flag){
		var opt = this.option,flag = flag === true;
		if(flag){
			$('#'+opt.openBtnId).css('cursor','default').off().parent().find('a.i-trash').hide();
		}else{
			$('#'+opt.openBtnId).css('cursor','pointer').off().parent().find('a.i-trash').show();
			this._addBaseEvent();
		}
		this.$select2.select2("readonly", flag);
	},
	//生成动态id
	_guid: function(){
		return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
			var r = Math.random() * 16 | 0,
			v = c == 'x' ? r : (r & 0x3 | 0x8);
			return v.toString(16);
		}).toUpperCase();
	}
});
/**
 * 人员选择树
 */
JCFF.JCTree.Lazy = JCFF.JCTree.extend({
	clazzName : 'JCFF.JCTree.Lazy',
	//构造器   new执行
	construct: function(option){
		arguments.callee.$.construct.apply(this, arguments);
		var _this = this;
		_this.selectedFilterData = []; 		//缓存已选中变量 用于备选区域回显
		_this.selectedData = [];			//缓存已选中变量 用于选中区域回显
		_this.currPageData = [];			//当前面板上显示的人员数据  用于全选当前
		_this.confirmData = [];				//缓存选中人员变量,保证已选区操作不影响获取结果
		_this.addAll = false;
		_this.option = $.extend(_this.option,{
			//机构树数据接口
			ztreeUrl: getRootPath() + '/api/pinDepartment/getAllDeptAndUser.action',
			//搜索接口
			searchUrl: getRootPath() + '/api/pinDepartment/getSelDeptAndUser.action',
			//保存组接口
			saveGroupUrl: getRootPath() + '/api/pinDepartment/saveGroup.action',
			autoSort: false,			//是否自动排序已选中
			pageNum: 120,				//分页显示个数
			maxSelectedShowNum: 10,		//select2最大显示数量
			isShowAll: false,			//是否显示所有人功能
			saveGroup: false,			//是否显示保存为组
			expandRootNode: true,		//是否默认展开根节点
			ztreeBeforeClick: null,		//左侧树点击判断函数,返回false则不执行click事件
			//人员树参数
			tabsPermissions: [{
				name: '根据组织',
				type: '1'
			}/*,{
				name: '根据职务',
				type: '2'
			},{
				name: '个人组别',
				type: '3'
			},{
				name: '公共组别',
				type: '4'
			},{
				name: '在线人员',
				type: '5'
			}*/],
		} ,option);
		//向页面添加弹出层
		$('body').append(this._getModalTemplate());
		//获取数据
		this._getTreeData();
		//绑定事件
		this._addEvent();

		this.$standby = $('#standby-li-'+_this.uuid);
		this.$selected = $('#selected-li-'+_this.uuid);
		this.$sTitle = $('#selected-title-'+_this.uuid);
		//请求人员数据的参数初始化
		this._resetParameter();
	},
	getData: function(){
		if(this.option.single){
			return this.$select2.select2('data');
		}else{
			if(this.selectedData.length > this.option.maxSelectedShowNum){
				return this.selectedData;
			}else{
				return this.$select2.select2('data');
			}
		}
	},
	//添加数据
	setData: function(datas){
		var opt = this.option;
		//如果是单选
		if(opt.single){
			if($.isPlainObject(datas)){
				if(!datas.userId){
					datas.userId = datas.id;
					datas.userName = datas.text;
				}
				this.$select2.select2('data' ,datas);
			}
		}else{
		//多选
			if($.isArray(datas)){
				this.selectedData = datas;
			}
			if($.isPlainObject(datas)){
				this.selectedData = [datas];
			}

			this._setMultiData(this.selectedData);
			this._openPutAwayClear();
		}
	},
	//隐藏树弹出层
	hide: function(isCancel){
		var _this = this ,opt = _this.option;
		if(!opt.single && isCancel){
			_this.selectedData = _this.confirmData;
		}
		if(typeof opt.cancel === 'function'){
			opt.cancel();
		}
		//this._resetParameter();
		$('#'+opt.modalId).modal('hide');
	},
	readonly: function(flag){
		this.$select2.select2("readonly", flag === true);
	},
	//清空已选数据
	clearValue: function(){
		var opt = this.option;
		this.selectedData = [];
		if(!opt.isBackgrounder){
			this.$select2 && this.$select2.select2('data','');
			if(!opt.single) this._openPutAwayClear();
		}
	},
	_openPutAwayClear: function(){},
	//获取左侧树的数据
	_getTreeData: function(){
		var _this = this,opt = _this.option;
		var ajaxOption = {
			type: 'GET',
			url : opt.url,
			dataType: 'json',
			success: function(data){
				if(!$.isPlainObject(data) && !$.isArray(data)){
					data = JSON.parse(data);
				}
				if(!opt.isBackgrounder){
					_this._drawSelect2();
					_this._addBaseEvent();
				}
				_this._initModule(data);
				//初始化完成 触发ready回调
				opt.ready && opt.ready();
			}
		};

		if($.isFunction(opt.funFormData)){
			var formData = opt.funFormData();
			if($.isPlainObject(formData)){
				ajaxOption.data = formData;
			}
		}

		$.ajax(ajaxOption);
	},
	//请求参数初始化
	_resetParameter: function(){
		this._classGetParameter = {
			orgId: '',	
			tabType: this.option.tabsPermissions[0].type,
			dutyId:'',
			spellType: ''
		};
	},
	//设置弹出层的最大宽度
	_getMaxWidth: function(){
		var width = 1300;
		if(window.screen.availWidth <= 1024) width = 900;
		return width;
	},
	//设置弹出层的最大高度
	_getMaxHeight: function(){
		var height = 560;
		if(window.screen.availHeight <= 768) height = 460;
		return height;
	},
	_addEvent: function(){
		var _this = this,opt = _this.option;
		var $search = $('#search-'+ _this.uuid);
		_this.$searchResults = $('#search-results-'+ _this.uuid);
		//绑定搜索结构面板事件
		_this.$searchResults.on('click' ,function(e){
			var target = $(e.target).closest('button, li');
			if(target.length === 1){
				var handler = target.data('handler-type');
				switch(handler){
					case 'closeSearchWarp':
					//关闭搜索面板
						_this._searchWrapHide();
						break;
					case 'addUser':
					//类别
						if(!_this.addAll){
							var userId = target.data('userid');
							if(target.hasClass('on')){
								_this._removeSelected(userId ,true);
								target.removeClass('on');
							}else{
								_this._addSelected(userId ,true);
								if(opt.single){
									//去掉其他位置选中效果
									target.siblings().removeClass('on');
								}
								target.addClass('on');
							}
						}
						break;
					case 'addDept':
					//类别
						_this._classGetParameter.orgId = target.data('id');
						_this._getFilterData();
						break;
				}
			}
		});
		//绑定搜索按钮事件
		_this.$document.on('click' ,(' #search-btn-'+_this.uuid) ,function(e){
			var val = $search.val().replace(/(^\s+)|(\s+$)/g,'');
			if(val.length){
				_this._getInputSearch(val);
			}
		});
		//统一绑定右侧人员组别过滤事件
		_this.$document.on('click' ,'#event-'+_this.uuid ,function(e){
			var target = $(e.target).closest('button, li ,b');
			_this._searchWrapHide();
			if(target.length === 1){
				var handler = target.data('handler-type');
				switch(handler){
					case 'class':
					//类别
						if(!target.hasClass('dark')){
							target.siblings().removeClass('dark');
							target.addClass('dark');
							_this._class(target.data('type'));
						}
						break;
					case 'add':
					//选中或取消人员
						if(!_this.addAll){
							var userId = target.attr('id').split('-')[1];
							if(target.hasClass('on')){
								_this._removeSelected(userId);
								target.removeClass('on');
							}else{
								_this._addSelected(userId);
								if(opt.single){
									target.siblings().removeClass('on');
								}
								target.addClass('on');
							}
						}
						break;
					case 'userInfo':
						var userId = target.parent().attr('id').split('-')[1];
						var deptId = target.parent().attr('deptCode').split('-')[0];
						var subSystem = target.parent().attr('deptCode').split('-')[1];
						if(window.showOnlinePerson){
							showOnlinePerson.showPersonInfo(userId,deptId,subSystem);
						}
						break;
					case 'remove':
					//取消选中人员
						_this._removeSelected(target.attr('id') ,true);
						break;
					case 'selectedAll':
					//所有人
						if(!opt.single){
							_this._selectedAll();
						}
						break;
					case 'currentAll':
					//全选当前
						if(!opt.single){
							_this._currentAll();
						}
						break;
					case 'clearAll':
					//清空已选
						_this._clearAll();
						break;
					case 'filter':
					//按照字母过滤
						_this._filter(target.data('type'));
						break;
					case 'saveGroup':
					//保存组
						_this._saveGroup();
						break;
					case 'fold':
					//折叠备选区域
						var flag = target.hasClass('down');
						if(flag){
							target.removeClass('down');
						}else{
							target.addClass('down');
						}
						_this._fold(!flag);
						break;
					case 'ok':
					//确定
						_this._ok();
						break;
					case 'close':
					//关闭
						_this.hide(true);
						break;
					case 'more':
						_this._more();
						break;
				}
			}
			e.preventDefault();
			e.stopPropagation();
		});
	},
	//前端分页
	_laypage: function(){
		var _this = this,pageNum = _this.option.pageNum,len = _this.cacheAllData.length;
		//调用分页
		laypage({
		    cont: document.getElementById('laypage-' + _this.uuid),
		    pages: Math.ceil(len / pageNum),
		    jump: function(obj){
				var last = obj.curr * pageNum - 1;
				_this.currPageData = [];
				last = last >= len ? (len - 1) : last;
				for(var i = (obj.curr * pageNum - pageNum); i <= last; i++){
					_this.currPageData.push(_this.cacheAllData[i]);
			    }
				_this._drawStandby();        
		    }
		});
	},
	//获取后台搜索数据
	_getInputSearch: function(value){
		var _this = this,$searchWarp = $('#lazy-search-' +_this.uuid);
		$searchWarp.html('&nbsp;&nbsp;&nbsp;&nbsp;正在查询数据...');
		_this._searchWrapShow();
		var param = {search: value};
		if($.isFunction(_this.option.funFormData)){
			var formData = _this.option.funFormData();
			if($.isPlainObject(formData)){
				param = $.extend(param ,formData);
			}
		}
		$.ajax({
			type: 'GET',
			url : _this.option.searchUrl,
			data: param,
			dataType: 'json',
			success: function(data){
				var temp = [];
				if($.isArray(data.users)){
					temp.push(_this._drawSearchTemplate(data.users ,'用户' ,'addUser' ,'userName'));
				}
				// if($.isArray(data.depts)){
				// 	temp.push(_this._drawSearchTemplate(data.depts ,'部门' ,'addDept' ,'deptName'));
				// }
				if(temp.length == 0){
					$searchWarp.html('&nbsp;&nbsp;&nbsp;&nbsp;未查询到结果!!!');
				}else{
					$searchWarp.html(temp.join(''));
				}
			}
		});
	},
	_clearSearch: function(){
		$('#search-'+ this.uuid).val('');
	},
	//绘制搜索结果数据
	_drawSearchTemplate: function(data ,typeName ,evtType ,key){
		var _this = this,temp = [];
		temp.push('<li class="title">'+(typeName + "("+data.length+")")+'</li>');
		if(data.length){
			temp.push('<li><ul>');
			//结果大于50条数据时只显示50条
			for (var i = 0,len = (data.length > 50 ? 50 : data.length);i < len;i++) {
				var item = data[i];
				if(key === 'userName'){
					_this.thisCacheData[item.userId] = item;
				}
				temp.push('<li class="list '+(_this.selectedFilterData[item.userId] ? "on" : "")+'" data-handler-type="'+evtType+'" data-id="'+item.id+'" data-userid="'+item.userId+'">'+item[key]+'</li>');
				
			}
			temp.push('</ul></li>');
		}
		return temp.join('');
	},
	//搜索结构数据显示
	_searchWrapShow: function(){
		this.$searchResults.animate({height: this._getMaxHeight() - 50 + 'px'});
	},
	//搜索结构数据隐藏
	_searchWrapHide: function(){
		this.$searchResults.animate({height: 0});
	},
	//获取查询结果数据
	_getFilterData: function(){
		var _this = this,opt = _this.option;

		if($.isFunction(opt.funFormData)){
			var formData = opt.funFormData();
			if($.isPlainObject(formData)){
				_this._classGetParameter = $.extend(_this._classGetParameter ,formData);
			}
		}

		$.ajax({
			type: 'GET',
			url : opt.ztreeUrl,
			data: _this._classGetParameter,
			dataType: 'json',
			success: function(data){
				if($.isArray(data)){
					_this._parseData(data);
				}
			}
		});
	},
	//初始化组件  目前只有ztree
	_initModule: function(treeData){
		var _this = this,opt = _this.option;

		function ztreeClick(evt ,id , node){
			_this._classGetParameter.orgId = node.id;
			_this._clearSearch();
			_this._getFilterData();
		}

		var zTreeSetting = {
				view:{
					selectedMulti: false,
					showLine: false
				},
				data:{
					simpleData:{
						enable:true,
						pIdKey: 'parentId'
					}
				},
				callback:{
					beforeClick: opt.ztreeBeforeClick,
					onClick: ztreeClick
				}
			};
		_this.zTreeObj = $.fn.zTree.init($('#lazy-tree-'+this.uuid), zTreeSetting, (treeData || []));
		var nodes = _this.zTreeObj.getNodes();
		if (nodes.length > 0 && opt.expandRootNode) {
			_this.zTreeObj.expandNode(nodes[0], true, false, true);
		}
		if(!opt.single){
			_this.$select2Container.css('max-height' ,'38px');
		}
	},
	//用于配置解析字段
	_parseData: function(datas){
		this.cacheAllData = datas;
		this._laypage();  //绘制分页
	},
	//添加人员
	_addSelected: function(userId ,flag){
		var opt = this.option;
		if(!this.addAll){
			if(opt.single){
				this.selectedData = [this.thisCacheData[userId]];
				this.selectedFilterData = [];
				this.selectedFilterData[userId] = this.thisCacheData[userId];
				this.$standby.find('li').removeClass('on');
			}else{
				this.selectedData.push(this.thisCacheData[userId]);
				this.selectedFilterData[userId] = this.thisCacheData[userId];
			}
			if(flag){
				$('#user-'+userId).addClass('on');
			}
			this._drawSelected();
		}
	},
	//删除已选人员
	_removeSelected:function(userId ,flag){
		var _this = this,opt = _this.option;
		if(_this.addAll && _this.selectedData[0] && _this.selectedData[0].userId == 'all'){
			this.addAll = false;
			_this.selectedFilterData = [];
			_this.selectedData = [];
		}else{
			if(flag) $('#user-'+userId).removeClass('on');
			if(_this.selectedFilterData[userId]){
				delete _this.selectedFilterData[userId];
			}
			for (var i = 0,len = _this.selectedData.length;i < len; i++) {
				var item = _this.selectedData[i];
				if(item.userId == userId){
					_this.selectedData.splice(i ,1);
					break;
				}
			}
		}
		this._drawSelected();
	},
	//绘制备选区域
	_drawStandby: function(){
		var _this = this,opt = _this.option,temp = [];
		_this.thisCacheData = [],
		len = _this.currPageData.length;
		if(len == 0){
			_this.$standby.html('暂无数据!!!');
			return false;
		}
		for(var i = 0;i < len; i++){
			var item = _this.currPageData[i];
			var isSubSystem = false;
			if (item.subSystem != null){
				isSubSystem = item.subSystem;
			}
			_this.thisCacheData[item.userId] = item;
			temp.push('<li class="'+(_this.selectedFilterData[item.userId] ? "on" : "")+'" data-handler-type="add" deptCode="'+item.deptId+'-'+isSubSystem+'" id="user-'+item.userId+'">'+item.userName+'<b data-handler-type="userInfo"></b></li>');
		}
		_this.$standby.html(temp.join(''));
	},
	//绘制选中区域
	_drawSelected: function(isPage){
		var _this = this,
			opt   = _this.option,
			temp  = [],
			len   = _this.selectedData.length;
		//判断是否需要自动排序
		if(opt.autoSort && !opt.single){
			_this.selectedData.sort(function(a,b){return a.orderNo-b.orderNo});
		}
		//如果是选中区加载更多则++ 否则重置为0
		if(isPage){
			$('#more-' + _this.uuid).remove();
			_this.currIndex++;
		}else{
			_this.currIndex = 1;
		}
		if(len){
			var last = _this.currIndex * 120 - 1;
			last = last >= len ? (len - 1) : last;
			//本次加载数据开始下标
			for(var i = (_this.currIndex * 120 - 120); i <= last; i++){
				var item = _this.selectedData[i];
				temp.push('<li data-handler-type="remove" id="'+item.userId+'">'+item.userName+'</li>');
				if(last !== len - 1 && i == last){//len > 120 && (mLen !== len) && 
					temp.push('<li data-handler-type="more" class="more" id="more-'+_this.uuid+'">加载更多...</li>');
				}
		    }

		}
		_this.$sTitle.html(len);
		_this.$selected[isPage ? 'append' : 'html'](temp.join(''));
	},
	_more: function(){
		this._drawSelected(true);
	},
	//重写父类_open方法  此方法在点击select2按钮触发
	_open: function(){
		var _this = this;
		var data = _this.$select2.select2('data');

		_this.currIndex = 1;
		//清空弹层中的搜索框内容
		_this._clearSearch();
		//判断如果是超过最大显示人员的话  直接选中区人员数据赋值给回显值
		if(!_this.option.single && data.length == 1 && data[0].id == 'jc_max_ellipsis'){
			data = _this.selectedData;
		}
		//_this.selectedData = [];
		_this.selectedFilterData = [];
		//重置字母过滤条件
		_this._classGetParameter.spellType = '';
		//判断是单选不能是空 , 多选时数组length不能为0
		if((_this.option.single && !$.isEmptyObject(data)) || ($.isArray(data) && data.length)){
			if(_this.option.single){
				if(data.userId === undefined){
					data.userId = data.id;
					data.userName = data.text;
				}
				_this.selectedData = [data];
				_this.selectedFilterData[data.userId] = [data];
			}else{
				//confirmData变量默认是 [] 
				//只要点击确定按钮就会清空,否则继承上一次打开时候数据
				if(_this.confirmData.length){
					_this.selectedData = _this.confirmData;
				}
				//判断是否是select2输入
				if(!_this.option.single && data[0] && data[0].id != 'jc_max_ellipsis' && data.length <= _this.option.maxSelectedShowNum){
					_this.selectedData = data;
				}
				//_this.selectedData = data;
				for (var i = 0, len = _this.selectedData.length; i < len; i++) {
					var item = _this.selectedData[i];
					if(item.userId === undefined){
						item.userId = item.id;
						item.userName = item.text;
					}
					_this.selectedFilterData[item.userId] = item;
				}
				//将选中数据备份一份,避免操作选中区域数据后点击取消后再次打开回显数据错误
				_this.confirmData = _this.selectedData.concat();
			}
			if(!_this.selectedData.length){
				_this.addAll = false;
			}
		}else{
			_this.selectedData = [];
		}
		this.show();
		this._drawSelected();
		this._getFilterData();
	},
	//点击确定执行事件
	_ok: function(){
		var _this = this;
		if(!_this.selectedData.length){
			msgBox.info({content:'请选择人员',type:'fail'});
			return false;
		}

		var data = _this.option.single ? (function(datas){
			return {id: datas.id, userId: datas.userId, text: datas.userName,orderNo: datas.orderNo,userName: datas.userName};
		})(_this.selectedData[0]) : (function(datas){
			_this.confirmData = [];
			return $.map(datas, function(item, key){
				return {
					id: item.id,
					userId: item.userId,
					text: item.userName,
					orderNo: item.orderNo,
					userName: item.userName
				}
			});
		})(_this.selectedData);

		// if(_this.addAll){
		// 	data.parameter = _this._classGetParameter;
		// }

		if(!_this.option.isBackgrounder){
			if(_this.option.single){
				_this.$select2.select2('data' ,data);
			}else{
				_this._setMultiData(data);
			}
			if(!_this.option.single){
				setTimeout(function(){_this._openPutAwayClear();},350);
			}
		}
		_this.hide();
	},
	//设置多选的select2回显
	_setMultiData: function(datas){
		var num = this.option.maxSelectedShowNum;
		if(datas.length > num){
			var item = $.map(datas.slice(0 ,num) ,function(obj){
				return obj.text || obj.userName;
			}).join(',') + '...... 共('+datas.length+')人';
			this.$select2.select2('data' ,[{
				id: 'jc_max_ellipsis',
				text: item
			}]);
			this.readonly(true);
			return false;
		}
		this.readonly();
		this.$select2.select2('data' ,datas);
	},
	//按照字母过滤事件
	_filter: function(type){
		this._classGetParameter.spellType = type;
		this._clearSearch();
		this._getFilterData();
	},
	//点击切换类型执行事件
	_class: function(type){
		this._classGetParameter.tabType = type;
		this._clearSearch();
		this._getFilterData();
	},
	//选择所有人
	_selectedAll: function(){
		this.$standby.find('li').removeClass('on');
		this.addAll = true;
		this.selectedFilterData = [];
		this.selectedData = [{
			id: 'all',
			text: '所有人',
			userId : 'all',
			userName: '所有人',
			orderNo: 1
		}];
		this._drawSelected();
	},
	//选中所有当前人
	_currentAll: function(){
		var _this = this,opt = _this.option;
		if(!_this.addAll){
			//备选区所以人添加选中状态
			_this.$standby.find('li').addClass('on');
			for(var i = 0,len = _this.currPageData.length;i < len; i++){
				var item = _this.currPageData[i];
				//过滤掉已选中的人员
				if(!_this.selectedFilterData[item.userId]){
					_this.selectedFilterData[item.userId] = item;
					_this.selectedData.push(item);
				}
			}
			_this._drawSelected();
		}
	},
	//清空已选
	_clearAll: function(){
		//清楚备选区域选中效果
		this.$standby.find('li').removeClass('on');
		this.selectedData = [];
		this.selectedFilterData = [];
		this.addAll = false;
		this._drawSelected();
	},
	//保存为组
	_saveGroup: function(){
		var _this = this,opt = _this.option;
		if(_this.selectedData.length > 2){
			$.ajax({
				type: 'POST',
				url : opt.saveGroupUrl,
				data: {
					userIds : $.map(_this.selectedData, function(item, key){
					    return item.userId;     
					}).join(',')
				},
				dataType: 'json',
				success: function(data){
					if(data.success){
						alertx('保存成功');
					}
				},
				error: function(){
                    alertx('保存失败');
				}
			});
		}
	},
	//	已选区域放大缩小事件
	_fold: function(flag){
		var $standby = $('#standby-'+this.uuid),
			$selected = $('#selected-'+this.uuid),
			dyH = this._getMaxHeight() - 255;
		$standby.css('height' ,(flag ? 38 : dyH) + 'px');
		$selected.css('height' ,(flag ? 432 : 165) + 'px');
	},
	//弹出层模版
	_getModalTemplate: function(){
		var temp = [],_this = this,opt = _this.option;
		var maxHeight = _this._getMaxHeight();
		temp.push('<div class="modal fade lazy-tree" id="'+opt.modalId+'">');
		    temp.push('<div class="modal-dialog" style="width:'+_this._getMaxWidth()+'px;">');
		        temp.push('<div class="modal-content">');
		            temp.push('<div class="modal-content-cell cell-left">');
		               	temp.push('<div class="modal-header">'+opt.title+'</div>');
		               	temp.push('<div style="height:'+maxHeight+'px;" class="lazy-sidebar">');
		               		temp.push('<div class="lazy-search">');
		               			temp.push('<input type="text" name="search" id="search-'+_this.uuid+'">');
		               			temp.push('<a class="search-btn" id="search-btn-'+_this.uuid+'"><i class="sprite"></i></a>');
		               		temp.push('</div>');
		               		temp.push('<div id="lazy-tree-'+_this.uuid+'" class="ztree lazy"></div>');
		               		temp.push('<div class="search-results" id="search-results-'+_this.uuid+'">');
		               			temp.push('<button type="button" class="close" data-handler-type="closeSearchWarp"><span aria-hidden="true">×</span></button>');
		               			temp.push('<ul class="lazy-list-wrap" id="lazy-search-'+_this.uuid+'"></ul>');
		               		temp.push('</div>');
		               	temp.push('</div>');
		            temp.push('</div>');
		            temp.push('<div class="modal-content-cell" id="event-'+_this.uuid+'">');
		            	temp.push('<div class="modal-header">');
		            	//顶部按钮
		            		temp.push('<div class="btn-title" style="display: contents; width: 100px;">');
		            			temp.push('<h4 style="line-height: 40px;font-size: 16px; width: 100px; display: inline-block;">人员选择</h4>');
		            		temp.push('</div>');
		            		temp.push(' <button type="button" class="close" data-handler-type="close"><span aria-hidden="true">x</span></button>');
		            	temp.push('</div>');
		            	temp.push('<div class="lazy-btn-group">');
		            		//操作按钮区域
		            		temp.push('<div class="operational">');
			            		if(!opt.single){
									if(opt.isShowAll){
										temp.push('<button type="button" name="dept" class="btn-icon" data-handler-type="selectedAll"><i class="all"></i>所有人</button>');
									}			            			
									temp.push('<button type="button" name="dept" class="btn-icon" data-handler-type="currentAll"><i class="selected"></i>全选当前</button>');
			            		}
		            			temp.push('<button type="button" name="dept" class="btn-icon" data-handler-type="clearAll"><i class="remove"></i>清空已选</button>');
		            		temp.push('</div>');
		            		temp.push('<div class="retrieval">');
		            			temp.push('<ul>'+_this._getEnglishTemplate()+'</ul>');
		            		temp.push('</div>');
		            	temp.push('</div>');
		            	temp.push('<div class="standby" id="standby-'+_this.uuid+'" style="height:'+(maxHeight - 255)+'px">');
		            		temp.push('<ul id="standby-li-'+_this.uuid+'"></ul>');
		            		temp.push('<div id="laypage-'+_this.uuid+'" class="layapge-pagination"></div>');
		            	//备选区域
		            	temp.push('</div>');
		            	temp.push('<div class="standby-toggle">');
		            		temp.push('<button type="button" class="sprite standby-icon" data-handler-type="fold"></button>');
		            	//分割线
		            	temp.push('</div>');
		            	temp.push(' <div class="selected" id="selected-'+_this.uuid+'">');
		            		//选中区域
		            		temp.push('<div class="selected-info">');
		            			temp.push('<span class="selected-title m-l" style="margin-left: 20px;">已选择(<span id="selected-title-'+_this.uuid+'">0</span>)</span>');
		            			if(!opt.single && opt.saveGroup){
			            			temp.push('<button type="button" class="sprite btn-icon m-l-md" data-handler-type="saveGroup">');
			            			temp.push('<i class="add"></i>保存组</button>');
		            			}
			            	temp.push('</div>');
			            	temp.push('<ul id="selected-li-'+_this.uuid+'"></ul>');
		            	temp.push('</div>');
		            	temp.push('<div class="modal-footer">');
		            	//底部按钮
		            		temp.push('<div class="btn-group">');
		            			temp.push('<button type="button" name="dept" class="btn dark m-r-sm" data-handler-type="ok">确 定</button>');
		            			temp.push('<button type="button" name="dept" class="btn" data-handler-type="close">取 消</button>');
		            		temp.push('</div>');
		            	temp.push('</div>');
		            temp.push('</div>');
		        temp.push('</div>');
		    temp.push('</div>');
		temp.push('</div>');
		return temp.join('');
	},
	//字母过滤模版
	_getEnglishTemplate: function(){
		var temp = [];
		temp.push('<li data-type="all" class="all" data-handler-type="filter">全部</li>');
		for(var i = 0; i < 26;i++){
		    var e =  String.fromCharCode((65+i));
		    temp.push('<li data-type="'+ e.toLowerCase() +'" data-handler-type="filter">'+e+'</li>')
		}
		temp.push('<li data-type="#" data-handler-type="filter">#</li>');
		return temp.join('');
	},
	//类型选项模版
	_getTabsPermissions: function(){
		var _this = this, len = _this.option.tabsPermissions.length,temp = [];
		for(var i = 0; i < len;i++){
			var item = _this.option.tabsPermissions[i];
			temp.push('<button type="button" data-type="'+item.type+'" data-handler-type="class" class="btn '+(i == 0 ? "dark" : "")+'">'+item.name+'</button>');
		}
		return temp.join('');
	}
});
//机构树
JCFF.JCTree.Org = JCFF.JCTree.extend({
	clazzName: 'JCFF.JCTree.Org',
	construct: function(option){
		arguments.callee.$.construct.apply(this, arguments);
	}
});
//人员树
JCFF.JCTree.Person = JCFF.JCTree.extend({
	clazzName: 'JCFF.JCTree.Person',
	construct: function(option){
		arguments.callee.$.construct.apply(this, arguments);
		var _this = this;
		//整合当前人员树参数和老人员树参数,在不改变老人员树参数前提下
		_this.option = $.extend(_this.option,{
			//单选多选,支持原JCTree的参数
			single: (option.isCheckOrRadio === false || option.single),
			parseData: null
		} ,option);
		//容器id
		_this.$controlDivId = option.container ? $('#' + options.container) : null;
		//缓存人员数据对象
		_this.userCacheData = [];
		//缓存部门人员数据对象
		_this.deptCacheData = [];
		//ztree使用数据
		_this.ztreeCacheData = [];
		//搜索数据缓存
		_this.searchCacheData = {
			index : 0,
			val   : ''
		};
	}
});