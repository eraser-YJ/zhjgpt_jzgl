
//边界
var REMIND_SIDE = 1;
//固定间隔
var REMIND_INTERVAL = 2;

// 周期
var REMIND_CYCLE = 3;

//一次性
var REMIND_ONCE = 4;

//天
var CYCLE_DAY = 1;
//周
var CYCLE_WEEK = 2;

// 月
var CYCLE_MONTH = 3;

//年
var CYCLE_YEAR = 4;
function selectControlCallback_(data) {
	//alert('33'+data); type = 1 人员，type = 2 组织
	opts_['echoData'] = [];
	if(data && data.length > 0) {
		opts_['echoData'] = data;
		//var r = JSON.stringify(remindObj_);
		remindObj_.receiveId = JSON.stringify(data);
		//alert(remindObj_.receiveId);
	}
}
var opts_ = {
		widgetId : "remindcontrol",							//控件ID
		widgetName : "remindcontrol",		
		//回显数据，没有可以不写
		echoData: [],
		isReadonly:false,
		callbackFunction: selectControlCallback_
	};
var remindObj_ = new Remind();
var editRemind_ = null;
//{tableName:'a',remind:r1,readonly:false,startTime:null,echoData:null,callback:null}
jQuery.fn.showRemind_ = function(setting) {
	setting_ = setting;
	
	//editRemind_ = setting.remind;
	// background-color: #f9f8f5; font-weight: bold;
	var windowDiv = jQuery('<div id="remindDiv_" class="modal fade panel"></div>');
	var div1 = jQuery('<div class="modal-dialog">')
	$(windowDiv).append(div1);
	var div2 = jQuery('<div class="modal-content">')
	$(div2).append('<div class="modal-header"><button type="button" class="close" data-dismiss="modal">×</button><h4 class="modal-title">提醒设置</h4></div>');
	var html = [];
	html[html.length] = '<form id="remindForm_">';
	html[html.length] = '<input type="hidden" id="remindReloadMark_" value="0">';
	
	html[html.length] = '<div class="modal-body">';
	html[html.length] = '<div class="table-wrap input-default form-table">';
	html[html.length] = '<table id="remindTable" class="table table-td-striped">';
	html[html.length] = '<tbody id="remindTable">';
	html[html.length] = '<tr>';
	html[html.length] = '<td style="width:150px;text-align: right;font-weight: bold;">';
	html[html.length] = '<span id="titleSpan" class="required">*</span>';
	html[html.length] = '标题';
	html[html.length] = '</td>';
	html[html.length] = '<td>';
	html[html.length] = '<input type="text" id="title_" name="title_"/>';
	html[html.length] = '</td>';
	html[html.length] = '</tr>';
	html[html.length] = '<tr>';
	html[html.length] = '<td style="width:150px;text-align: right;font-weight: bold;">';
	html[html.length] = '<span class="required">*</span>';
	html[html.length] = '提醒内容';
	html[html.length] = '</td>';
	html[html.length] = '<td>';
	html[html.length] = '<textarea rows="3"  name="remindContent_" id="remindContent_"></textarea>';
	html[html.length] = '</td>';
	html[html.length] = '</tr>';
	html[html.length] = '<tr>';
	html[html.length] = '<td style="width:150px;text-align: right;font-weight: bold;">';
	html[html.length] = '提醒方式';
	html[html.length] = '</td>';
	html[html.length] = '<td>';
	html[html.length] = '<label class="radio inline"><input type="radio" name="txfs" index ="0" id="noremind_"  onclick="onclickRemind(0);setNoRemind(true);"/>不提醒 </label>';
	html[html.length] = '<label class="radio inline"><input type="radio" name="txfs" index ="1" onclick="onclickRemind(1);setNoRemind(false)"/>短信 </label>';
	html[html.length] = '<label class="radio inline"><input type="radio" name="txfs" index ="2" checked onclick="onclickRemind(2);setNoRemind(false)"/>邮件 </label>';
	html[html.length] = '</td>';
	html[html.length] = '</tr>';
	html[html.length] = '<tr>';
	html[html.length] = '<td style="width:150px;text-align: right;font-weight: bold;">';
	html[html.length] = '<span class="required">*</span>';
	html[html.length] = '提醒接收人';
	html[html.length] = '</td>';
	html[html.length] = '<td>';
	html[html.length] = '<div id="remindControlTree_"></div>';
	html[html.length] = '</td>';
	html[html.length] = '</tr>';
	html[html.length] = '<tr>';
	html[html.length] = '<td style="width:150px;text-align: right;font-weight: bold;">';
	html[html.length] = '类型';
	html[html.length] = '</td>';
	html[html.length] = '<td>';
	html[html.length] = '<select id="remindType_" onchange="selectRemindType_($(this).val())">';
	html[html.length] = '<option value="1">边界提醒</option>';
	html[html.length] = '<option value="2" >固定间隔</option>';
	html[html.length] = '<option value="3">周期提醒</option>';
	html[html.length] = '<option value="4" selected>一次性</option>';
	html[html.length] = '</select>';
	html[html.length] = '</td>';
	html[html.length] = '</tr>';
	html[html.length] = '<tr id="startTimeTR" type="">';
	html[html.length] = '<td style="width:150px;text-align: right;font-weight: bold;">';
	html[html.length] = '<span class="required">*</span>';
	html[html.length] = '开始时间';
	html[html.length] = '</td>';
	html[html.length] = '<td>';
	html[html.length] = '<input class="datepicker-input dateFormate" type="text" data-pick-time="true" data-date-format="yyyy-MM-dd HH:mm:ss" id="remindStartTime_" name="remindStartTime_" data-ref-obj="#remindEndTime_ lt" style="width:200px"/>';
	html[html.length] = '</td>';
	html[html.length] = '</tr>';
	html[html.length] = '<tr id="endTimeTR">';
	html[html.length] = '<td style="width:150px;text-align: right;font-weight: bold;">';
	//html[html.length] = '<span class="required" id="endtimespan_">*</span>';
	html[html.length] = '&nbsp;&nbsp;&nbsp;结束时间';
	html[html.length] = '</td>';
	html[html.length] = '<td>';
	html[html.length] = '<input class="datepicker-input dateFormate" data-pick-time="true" data-date-format="yyyy-MM-dd HH:mm:ss" type="text" id="remindEndTime_" name="remindEndTime_" data-ref-obj="#remindStartTime_ gt" style="width:200px"/>';
	html[html.length] = '</td>';
	html[html.length] = '</tr>';
	
	//html = html.concat(gdjgHtml_);
	
	html[html.length] = '</tbody>';
	html[html.length] = '</table>';
	html[html.length] = '</div>';
	html[html.length] = '</div>';
	html[html.length] = '</form>';
	html[html.length] = '<div class="modal-footer no-all form-btn">';
	html[html.length] = '<button class="btn dark" id="save_" onclick="saveRemindValidate_();">保 存</button>';
	html[html.length] = '<button class="btn" id="close_" onclick="closeRemind_();">关 闭</button>';
	html[html.length] = '</div>';
	//$('#someElement').find('option:selected');
	$(div2).append(html.join(' '));
	$(div1).append(div2);
	//$("#remindDiv_").append();
	//include("", this.context.head);
	//var doc = setting.doc.body;
	//$(doc).append(windowDiv);
	$(this.context.body).append(windowDiv);
	$('#endTimeTR').css('display','none');
	/*remindObj_.cycle = 4;
	remindObj_.remindType = 3;
	remindObj_.viewCycle = '2411';*/
	//initRemindEdit_();
	$(".datepicker-input").datepicker();
    $("#remindDiv_").modal('show');
	//$('#remindDiv_').modal('show');
    if(setting.remind) {
    	try{
    		remindObj_ = JSON.parse(setting.remind);
    		if(remindObj_.receiveId && remindObj_.receiveId.length > 0) {
    			//var r = JSON.parse(remindObj_.receiveId);
    			var r = JSON.parse(remindObj_.receiveId);
    			opts_['echoData'] = [];
    			for(var i = 0; i < r.length; i++) {
    				opts_['echoData'][i] = {id:r[i]['id'],text:r[i]['text'],type:r[i]['type']};
    				/*alert(r[i]['id']);
    				alert(r[i]['text']);
    				alert(r[i]['type']);*/
    			}
    		} else {
    			opts_['echoData'] = [];
    		}
    		
    		initRemindEdit_();
    	}catch(e) {
    		
    	}
    } else {
    	opts_['echoData'] = [];
    	//回显页面的开始时间和选择的人
    	if(setting.startTime && setting.startTime.length > 0) {
    		$('#remindStartTime_').val(setting.startTime);
    	}
    	if(setting.remindContent && setting.remindContent.length > 0) {
    		$('#remindContent_').val(setting.remindContent);
    	}
    	if(setting.echoData && setting.echoData.length > 0) {
    		opts_['echoData'] = setting.echoData; 
    	}
    	onclickRemind(0);
    	$('#titleSpan').css('visibility',"visible");
    	if('0' == $('#remindReloadMark_').val()) {
    		//清空提醒行
    	}
    }
 // 清除验证信息
	hideErrorMessage();
    doReadonly();
    //$("#remindControlTree_").remove();
    if(typeof(setting_.readonly) != "undefined" && setting_.readonly) {
    	//selectControl.init("remindControlTree_", "remindcontrol", true, true, null, opts_, true);
    	//selectControl.select2Readonly("remindcontrol")
    	opts_['isReadonly'] = true;
    	
    } else {
    	opts_['isReadonly'] = false;
    }
    $("#remindControlTree_").deptAndPersonControl(opts_);
    validateRemindForm_();
}
/*
 * 
 * */
function include(path,head){ 
    var a=document.createElement("script");
    a.type = "text/javascript"; 
    a.src=path; 
  //  var head=document.getElementsByTagName("head")[0];
    head.appendChild(a);
}

function validateRemindForm_() {
	/*$.validator.addMethod("dateFormate", function(value, element) {
//		alert("2"+$("input[type=radio][name=txfs][index=0]:checked").length == 0 );
		alert("-"+typeof($("input[type=radio][name=txfs][index=0]:checked").length()));
		alert("*"+$("input[type=radio][name=txfs][index=0]:checked").length() == 'undefined');
		alert("="+$("input[type=radio][name=txfs][index=0]:checked").length() == null);
		//alert($("input[type=radio][name=txfs][index=0]:checked").length == 0);
		if($("input[type=radio][name=txfs][index=0]:checked").length == 0) {
			return false;
		}
		if(value) {
			return !this.optional(element) && _reTimeReg.test(value);
		} else {
			return false;
		}
	}, "请设置正确的时间格式");
	$.validator.addMethod("hourFormate", function(value, element) {
		if($("input[type=radio][name=txfs][index=0]:checked").length == 0) {
			return false;
		}else{
			var formate = /^(([0-9])|(1[0-9])|(2[0-3]))$/;
			return formate.test(value);
		}
	}, "请输入0到23的数字");
	$.validator.addMethod("minuteFormate", function(value, element) {
		if($("input[type=radio][name=txfs][index=0]:checked").length == 0) {
			return false;
		}else {
			var formate = /^(([0-9])|([1-5][0-9]))$/;
			return formate.test(value);
		}
	}, "请输入0到59的数字");*/
	jQuery.validator.addMethod("hour", function(value, element) {
		var hour = +value;
		return hour >= 0 && hour <= 23;   
	}, "请输入合理的时间");
	jQuery.validator.addMethod("minute", function(value, element) {
		var hour = +value;
		return hour >= 0 && hour <= 59;   
	}, "请输入合理的时间");
	$("#remindForm_").validate({
		ignore: ".ignore",
        rules: {
           remindContent_: 
  		   {
        	   required: function(element) {
			    	if(remindObj_.remindMode != 0) {
			    		return true;
			    	} else {
			    		return false;
			    	}
			      },
  			    maxlength: 70
  		   },
  		 remindcontrol:{
  			 required: function(element) {
			    	if(remindObj_.remindMode != 0) {
			    		return true;
			    	} else {
			    		return false;
			    	}
			      }
  		 },
  		 intervalHour: 
  		   {
  			 required: function(element) {
			    	if(remindObj_.remindMode != 0) {
			    		return true;
			    	} else {
			    		return false;
			    	}
			      },
  			 number:true
  		   },
  		   intervalMinute: 
  		   {
  			   number:true,
  			 required: function(element) {
			    	if(remindObj_.remindMode != 0) {
			    		return true;
			    	} else {
			    		return false;
			    	}
			      }
  		   },
  		   remindStartTime_: 
  		   {
  			 required: function(element) {
			    	if(remindObj_.remindMode != 0) {
			    		return true;
			    	} else {
			    		return false;
			    	}
			      }
  		   },
		   title_: 
		   {
			   //required:true,
			   required: function(element) {
			    	if(remindObj_.remindMode == 2) {
			    		return true;
			    	} else {
			    		return false;
			    	}
			      },
			      maxlength: 70
		   },
		/*   remindContent_: 
		   {
			    required: function(element) {
			    	alert("1"+ $("input[type=radio][name=txfs][index=0]:checked").length != 0);
			        return false;
			      },
			    maxlength: 70
		   }*/
		/*   loginName: 
		   {
			    required: true,
			    username: true,
			    remote:{
                    url: getRootPath()+"/sys/user/checkLoginName.action",
                    type: "get",
                    dataType: 'json',
                    data: {
                        'loginNameOld': function(){return $("#loginNameOld").val();}
                    }
			    }

		   },*/
		   
		 
		   /*   intervalHour:{
			   maxlength: 3
		   },
		   intervalMinute:{
			   maxlength: 3
		   },
		   remindNum:{
			   number:function(e){
				   var formate = /^(([1-9]{1,4}))$/;
				   return formate.test($('#remindNum').val());
			   },
			   maxlength: 4
		   },*/
		   /*,
		   orderNo: 
		   {
			    required: true,
			    maxlength: 6,
			    digits: true
		   },
		   mobile: 
		   {
			    required: false,
			    maxlength: 20,
			    mobile: true
		   },
		   email: 
		   {
			    required: false,
			    maxlength: 30,
			    email: true
		   },
		   
		   birthday: 
		   {
			    required: false,
			    dateISO: true
		   },
		 
		   otherDeptNo: 
		   {
			    required: false,
			    maxlength: 6,
			    number: true
		   }*/
	    },
	    errorPlacement: function(error, element) {
	    	   //error === 错误信息 
	    	   //element === 当前验证对象
	    	    /**
	    	    *  人员选择树验证可以使用以下方法
	    	    *  如果不是表单closest参数传入选择树容器的父元素
	    	    */
	    	    element.closest("td").append(error);
	    }
	});
}




/**
 * 初始化编辑页面
 */
function initRemindEdit_() {
	//remindObj_ = setting_.remind;
	$('#remindContent_').val(remindObj_.content);
	//alert(remindObj_.remindMode)
	$("input[type=radio][name=txfs][index="+remindObj_.remindMode+"]").prop("checked",'checked');
	if(remindObj_.remindMode == 0) {
		onclickRemind(0);
	}
//	var index = $("input[type='radio'][name='year_time']:checked").attr('index');
	$("#remindType_ option[value="+remindObj_.remindType+"]").prop("selected","true");
	/*
	 * remindObj_.viewStartTimeStr = "2014-04-28 08:10:00";
	remindObj_.viewEndTimeStr = "2014-04-28 15:30:00";
	*/
	$('#remindStartTime_').val(remindObj_.viewStartTime);
	$('#remindEndTime_').val(remindObj_.viewEndTime);
	$('#title_').val(remindObj_.title);
	selectRemindType_(remindObj_.remindType);
	var viewCycle = remindObj_.viewCycle;
	if(REMIND_SIDE == remindObj_.remindType) {
		//边界提醒
		$("#bjtxBeforeStart option[value="+remindObj_.startForwardTime+"]").attr("selected","true")
		$("#bjtxBeforeEnd option[value="+remindObj_.endForwardTime+"]").attr("selected","true")
		//$('#bjtxBeforeStart').find("option[timeValue='5']").attr("selected",true);
		//alertx($("#remindTable").val(selectedIndex));
	} else if(REMIND_INTERVAL == remindObj_.remindType) {
		//固定间隔
		$('#intervalHour').val(remindObj_.intervalHour);
		$('#intervalMinute').val(remindObj_.intervalMinute);
		$('#remindNum').val(remindObj_.remindNum);
		
	} else if(REMIND_CYCLE == remindObj_.remindType) {
		$('#intervalHour').val(remindObj_.intervalHour);
		$('#intervalMinute').val(remindObj_.intervalMinute);
		$("input[type=radio][name=txzq][index="+remindObj_.cycle+"]").attr("checked",'checked');
		//周期提醒
		selectCycle_(remindObj_.cycle);
		if(remindObj_.cycle == CYCLE_WEEK) {
			 //选择周  0110000
			//var viewCycle  = "0110000";//remindObj_.viewCycle;
			for(var i = 0; i < viewCycle.length; i++) {
				if('1' == viewCycle[i]) {
					$("input[type=checkbox][name=gdzq_week][id="+(i + 1)+"]").attr("checked",'checked');
				}
			}
		} else if(remindObj_.cycle == CYCLE_MONTH) {
			//选择月
			//var viewCycle  = "334";//remindObj_.viewCycle;
			$("input[type=radio][name=month_time][index="+viewCycle[0]+"]").attr("checked",'checked');
			if(viewCycle.length == 3) {
				$("#month_cycle_index option[value="+viewCycle[1]+"]").attr("selected","true")
				$("#month_cycle_week option[value="+viewCycle[2]+"]").attr("selected","true")
			} else if(viewCycle.length == 2) {
				$("#month_days option[value="+viewCycle[1]+"]").attr("selected","true")
			}
		} else if(remindObj_.cycle == CYCLE_YEAR) {
			// 选择年
			//var viewCycle  = "2411";//remindObj_.viewCycle;
			$("input[type=radio][name=year_time][index="+viewCycle[0]+"]").attr("checked",'checked');
			if(viewCycle.length == 3) {
				$("#year_cycle_1months option[value="+viewCycle[1]+"]").attr("selected","true")
				$("#year_cycle_1days option[value="+viewCycle[2]+"]").attr("selected","true")
			} else if(viewCycle.length == 4) {
				$("#year_cycle_2months option[value="+viewCycle[1]+"]").attr("selected","true")
				$("#year_cycle_2index option[value="+viewCycle[2]+"]").attr("selected","true")
				$("#year_cycle_2week option[value="+viewCycle[3]+"]").attr("selected","true")
			}
		} else {
			// 选择天
		}
	} else if(REMIND_ONCE == remindObj_.remindType) {
		//一次性
		//$('#remindTable tr[type="bjtx"]');
	}	
	doReadonly();
}

function doReadonly() {
	var mark = "";
	if(typeof(setting_.readonly) != "undefined" && setting_.readonly) {
		mark = "disable";
		$('#close_').addClass('dark');
		$('#save_').css("display",'none'); 
	}  else {
		$('#close_').removeClass('dark');
		$('#save_').css("display",'inline-block'); 
	}
	$('#remindForm_ #remindContent_').attr("disabled",mark); 
	//('#remindForm_ #s2id_remindcontrol').prop("disabled",mark); 
	$('#remindForm_ #openBtn1').attr("disabled",mark);
	var inputs = $('#remindForm_ input');
	var selects = $('#remindForm_ select');
	var checkboxs = $('#remindForm_ checkbox');
	var radios = $('#remindForm_ radio');
	$.merge(inputs, selects);
	$.merge(inputs, checkboxs);
	$.merge(inputs, radios);
	//	var eles = inputs.concat(selects, checkboxs, radios);
	if(mark) {
		inputs.each(function(){
			$(this).attr("disabled",mark);  
		});
	} else {
		inputs.each(function(){
			$(this).removeAttr("disabled");  
		});
		//$('#remindForm_ #s2id_remindcontrol').removeAttr("disabled"); 
		$('#remindForm_ #openBtn1').removeAttr("disabled"); 
		$('#save_').removeAttr("disabled"); 
		$('#remindForm_ #remindContent_').removeAttr("disabled"); 
		
	}
}
function closeRemind_() {
	$('#remindReloadMark_').attr('value', 1);
	$("#remindDiv_").modal('hide');
}
function getTest() {
	$.ajax({
		type : "post",
		url : getRootPath() + "/sys/remind/test.action",
		//data : {"ids": ids},
		//dataType : "json",
		cache:false,
		success : function(data) {
//				alertx("删除成功");
		}
	});
}
//选择提醒方式
function onclickRemind(index) {
	remindObj_.remindMode = index;
	remindObj_.isActive = '1';
	$('#titleSpan').css('visibility',"hidden");
	if(index == 0) {
		$('#remindForm_')[0].reset();
		//$("#remindType_ option[value=4]").attr("selected","true");
		selectRemindType_(4);
		remindObj_ = new Remind();
		 
	} else if(index == 1) {
	} else if(index == 2) {
		$('#titleSpan').css('visibility',"visible");
	}
}

function setNoRemind(flag) {
	var mark = "";
	if(flag) {	
		remindObj_.remindMode = 0;
		$('#noremind_').prop("checked",true);
		mark = "disable";
	}else{
		$('#noremind_').prop("checked",false);
	}
	var inputs = $('#remindForm_ input[type!="radio"]');
	var selects = $('#remindForm_ select');
	var checkboxs = $('#remindForm_ checkbox');
	$('#remindForm_ #remindContent_').attr("disabled",mark); 
	$('#remindForm_ #openBtn1').attr("disabled",mark); 
	$.merge(inputs, selects);
	$.merge(inputs, checkboxs);
	//	var eles = inputs.concat(selects, checkboxs, radios);
	if(mark) {
		opts_['echoData'] = [];
		opts_.isReadonly = true;//使人员选择树不可用
		$("#remindControlTree_").deptAndPersonControl(opts_);//清空人员
		//selectControl.clearValue("");
		inputs.each(function(){
			$(this).attr("disabled",mark);  
		});
		//$('#remindForm_ #s2id_remindcontrol').empty();
	} else {
		opts_['echoData'] = [];
		opts_.isReadonly = false;//恢复人员选择树可使用
		$("#remindControlTree_").deptAndPersonControl(opts_);//清空人员
		$('#remindForm_ #openBtn1').removeAttr("disabled");  
		$('#remindForm_ #remindContent_').removeAttr("disabled");  
		inputs.each(function(){
			$(this).removeAttr("disabled");  
		});
	}
}
//选择提醒周期
function selectCycle_(index){
	$('#week_').css('display','none');
	$('#month_').css('display','none');
	$('#year_').css('display','none');
	$('#intervalSpan').css('display','none');
	
	remindObj_.cycle = index;
	if(index == CYCLE_WEEK) {
		 //选择周
		$('#week_').css('display','table-row');
	} else if(index == CYCLE_MONTH) {
		//选择月
		$('#month_').css('display','table-row');
	} else if(index == CYCLE_YEAR) {
		// 选择年
		$('#year_').css('display','table-row');
	} else {
		$('#intervalSpan').css('display','table-row');
		// 选择天
	}
}

//选择提醒类型
function selectRemindType_(index) {
	//alertx($(obj).val());
	var settingTR = $('#remindTable tr[type="settingTR"]');
	var gdjg = $('#remindTable tr[type="gdjg"]');
	var gdzq = $('#remindTable tr[type="gdzq"]');
	$(settingTR).each(function(){
		//$(this).text();
		$(this).remove();
	});
	 
	//var trs2 = $('#remindTable tr')['name="gdjg"']
	//var trs3 = $('#remindTable tr')['="gdzq"']
	//边界
	//var REMIND_SIDE = 1;
	//固定间隔
	//var REMIND_INTERVAL = 2;

	// 周期
	//var REMIND_CYCLE = 3;

	//一次性
	//var REMIND_ONCE = 4;
	$('#endTimeTR').css('display','table-row');
	remindObj_.remindType = index;
	if(REMIND_SIDE == index) {
		//边界提醒
		$('#remindTable').append(bjtxHtml_.join(' '));
		//$('#bjtxBeforeStart').find("option[timeValue='5']").attr("selected",true);
		//alertx($("#remindTable").val(selectedIndex));
	} else if(REMIND_INTERVAL == index) {
		//固定间隔
		$('#remindTable').append(gdjgHtml_.join(' '));
		
	} else if(REMIND_CYCLE == index) {
		//周期提醒
		$('#remindTable').append(gdzqHtml_.join(' '));
		$('#week_').css('display','none');
		$('#month_').css('display','none');
		$('#year_').css('display','none');
	} else if(REMIND_ONCE == index) {
		//一次性
		//$('#remindTable tr[type="bjtx"]');
		$('#endTimeTR').css('display','none');
	}	
	/*if(1 == $(obj).val()) {
		//边界提醒
		$('#remindTable').append(bjtxHtml_.join(' '));
	} else if(2 == $(obj).val()) {
		//固定间隔
		$('#remindTable').append(gdjgHtml_.join(' '));
		
	} else if(3 == $(obj).val()) {
		//周期提醒
		$('#remindTable').append(gdzqHtml_.join(' '));
		$('#week_').css('display','none');
		$('#month_').css('display','none');
		$('#year_').css('display','none');
	} else if(4 == $(obj).val()) {
		//一次性
		//$('#remindTable tr[type="bjtx"]');
		$('#endTimeTR').css('display','none');
	}	*/
	
}

//保存公共提醒前做短信校验 add by songht start
function saveRemindValidate_(){
	if($("#remindForm_").valid()) {
		if(remindObj_.remindMode=='1'){//是短信
			jQuery.ajax({
				url : getRootPath()+"/sys/remind/validRemind.action?time=" + new Date(),
				type : 'get',
				async: false,
				dataType : "json",
				data : {'receiveId':remindObj_.receiveId},
				success : function(data) {
					if(data.success=="success"){
						saveRemind_();
					}else{
						if(data.success){
							msgBox.confirm({
								content: data.successMessage,
								success: function(){
									saveRemind_();
								},
								cancel:function(){
								}
							});
						}else{
							msgBox.info({
								content: data.errorMessage
							});
						} 
					}
				},
				error : function() {
					msgBox.tip({
						type:"fail",
						content:$.i18n.prop("JC_OA_PO_015"),
						callback:function(){
						}
					});
				}
			});
		}else{//是邮件
			saveRemind_();
		}
	}
}
//保存公共提醒前做短信校验 add by songht end

function saveRemind_() {
	//alert("--"+$("#remindForm_").valid());
	//if($("#remindForm_").valid()) {
		var isSave = assembleRemind();
		if(isSave) {
			var r = JSON.stringify(remindObj_);
			//var d = JSON.parse(r);
			if(setting_.objId) {
				$('#'+setting_.objId).attr('value',r);
			}
			if(setting_.callback) {
				setting_.callback(r);
			}
			$('#remindReloadMark_').attr('value', 1);
			remindObj_ = new Remind();
			$("#remindDiv_").modal('hide');
			//$("#remindControlTree_").empty();
		} else {
			setting_.callback(null);
		}
	//}
}
/*
 //    /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/
 * */
var _reDataTime = /^([1-9]d{3}-((0?[1-9])|(1[0-2]))-((0[1-9])|([1-2]?d)|(3[0-1])))?$/     //日期格式,可为空
var _reTimeReg = /^[1-9][0-9]{3}-(0?[1-9]|1[0|1|2])-(0?[1-9]|[1|2][0-9]|3[0|1])\s(0?[1-9]|1[0-9]|2[0-3]):(0?[0-9]|[1|2|3|4|5][0-9]):(0?[0-9]|[1|2|3|4|5][0-9])$/;
function assembleRemind() {
	remindObj_.viewStartTimeStr = $('#remindStartTime_').val();
	remindObj_.viewEndTimeStr = $('#remindEndTime_').val();//"2014-04-28 15:30:00";
	remindObj_.viewStartTime = $('#remindStartTime_').val();
	remindObj_.viewEndTime = $('#remindEndTime_').val();//"2014-04-28 15:30:00";
	remindObj_.content = $('#remindContent_').val();
	remindObj_.title = $('#title_').val();
	remindObj_.tableName = setting_.tableName;
	if(remindObj_.remindMode == 0) {
		//选择不提醒
		return true;
	} else if(remindObj_.remindMode == 1) {
		/*if($.trim(remindObj_.title) == "") {
			msgBox.tip({
				//type:"success",
				content: $.i18n.prop("JC_SYS_073")
			});
			return false;
		}*/
		if($.trim(remindObj_.receiveId) == "") {
			msgBox.tip({
				//type:"success",
				content: $.i18n.prop("JC_SYS_074")
			});
			return false;
		}
	}
//	if(!remindObj_.viewStartTimeStr || remindObj_.viewStartTimeStr.length < 1) {
//		//alertx("请设定开始时间");
//		/*msgBox.tip({
//			//type:"success",
//			//结束时间不能小于开始时间
//			content: $.i18n.prop("JC_SYS_064")
//		});
//		return false;*/
//	} else {
//		if(!_reTimeReg.test(remindObj_.viewStartTimeStr)) {
//			alertx("请设置正确的时间格式,如9999-01-01 01:01:01");
//			return false;
//		}
//	}
//	if(remindObj_.viewEndTimeStr && remindObj_.viewEndTimeStr.length > 0) {
//		if(!_reTimeReg.test(remindObj_.viewEndTimeStr)) {
//			alertx("请设置正确的时间格式,如9999-01-01 01:01:01");
//			return false;
//		}
//	}
	if(REMIND_SIDE == remindObj_.remindType) {
		//边界提醒
		remindObj_.startForwardTime = $('#bjtxBeforeStart').val();
		remindObj_.startForwardTimeValue = $("#bjtxBeforeStart").find("option:selected").attr("timeValue");
		remindObj_.endForwardTime = $('#bjtxBeforeEnd').val();
		remindObj_.endForwardTimeValue = $("#bjtxBeforeEnd").find("option:selected").attr("timeValue");
		//alertx(remindObj_.startForwardTimeValue);
		
		//设置了结束前提醒， 但没有设置结束时间 不能保存
		if("0" != remindObj_.endForwardTime && !remindObj_.viewEndTimeStr) {
			//alertx("请设置结束时间或取消结束前提醒。");
			msgBox.tip({
				//type:"success",
				//结束时间不能小于开始时间
				content: $.i18n.prop("JC_SYS_063")
			});
			return false;
		}
		
	} else if(REMIND_INTERVAL == remindObj_.remindType) {
		//固定间隔
		remindObj_.intervalHour = $('#intervalHour').val();
		remindObj_.intervalMinute = $('#intervalMinute').val();
		remindObj_.remindNum = $('#remindNum').val();
		if(!remindObj_.intervalHour && !remindObj_.intervalMinute) {
			//alertx("请设置间隔时间");
			msgBox.tip({
				//type:"success",
				//结束时间不能小于开始时间
				content: $.i18n.prop("JC_SYS_062")
			});
			return false;
		}
	} else if(REMIND_CYCLE == remindObj_.remindType) {
		//周期提醒
		remindObj_.intervalHour = $('#intervalHour').val();
		remindObj_.intervalMinute = $('#intervalMinute').val();
		if(!remindObj_.intervalHour && !remindObj_.intervalMinute) {
			//alertx("请设置提醒时间");
			//return false;
		}
		return assembleCycle();
	} else if(REMIND_ONCE == remindObj_.remindType) {
		//一次性
		//$('#remindTable tr[type="bjtx"]');
	}	
	var end_ = new Date(remindObj_.viewEndTimeStr.replace(/-/g,"/"));
	var start_ = new Date(remindObj_.viewStartTimeStr.replace(/-/g,"/"));
	if(remindObj_.viewEndTimeStr) {
		if(end_.getTime() <= start_.getTime()) {
			msgBox.tip({
				//type:"success",
				//结束时间不能小于开始时间
				content: $.i18n.prop("JC_SYS_015")
			});
			return false;
		}
		
	}
	return true;
}

//组织提醒周期数据 
function assembleCycle() {
	var viewCycle = '';
	//var cronExpression = '';
	if(remindObj_.cycle == CYCLE_WEEK) {
		 //选择周
	//	cronExpression = "00 " + remindObj_.intervalMinute + " " + remindObj_.intervalHour + " ? * " ;
		var isSelect = false;
		$("input[name='gdzq_week']").each(function(i, item){
			if(this.checked) {
				isSelect = true;
				//cronExpression += this.id + ",";
				viewCycle += '1';
			} else {
				viewCycle += '0';
			}
		});
		if(!isSelect) {
			msgBox.tip({
				//type:"success",
				//结束时间不能小于开始时间
				content: $.i18n.prop("JC_SYS_065")
			});
			//alertx("前选择需要提醒的星期");
			return false;
		}
		
	} else if(remindObj_.cycle == CYCLE_MONTH) {
		//选择月
		//$("input:radio[value=http://www.2cto.com/kf/201110/'rd2']").attr('checked','true');
		var index = $("input[type='radio'][name='month_time']:checked").attr('index');
		var selectId = $("input[type='radio'][name='month_time']:checked").attr('selectId');
		viewCycle += index;
		if(selectId) {
			var selects = $("select[id^='"+selectId+"']");
			selects.each(function(){
				var selectedValue = $(this).val();
				viewCycle += selectedValue;
			});
		}
	} else if(remindObj_.cycle == CYCLE_YEAR) {
		// 选择年
		var index = $("input[type='radio'][name='year_time']:checked").attr('index');
		var selectId = $("input[type='radio'][name='year_time']:checked").attr('selectId');
		viewCycle += index;
		if(selectId) {
			var selects = $("select[id^='"+selectId+"']");
			selects.each(function(){
				var selectedValue = $(this).val();
				viewCycle += selectedValue;
			});
		}
	} else {
		// 选择天
		//表达式意义 每天的intervalHour点intervalMinute几分都执行
		//cronExpression = "00 " + remindObj_.intervalMinute + " " + remindObj_.intervalHour + " ? * 1-7" ;
	}
	remindObj_.viewCycle = viewCycle;
	return true;
}

var selectTimeOptions = '<option timeValue="0" value="0">无</option>';
selectTimeOptions += '<option timeValue="5" value="1">5分钟</option>';
selectTimeOptions += '<option timeValue="10" value="2">10分钟</option>';
selectTimeOptions += '<option timeValue="15" value="3">15分钟</option>';
selectTimeOptions += '<option timeValue="30" value="4">30分钟</option>';
selectTimeOptions += '<option timeValue="60" value="5">1小时</option>';
selectTimeOptions += '<option timeValue="90" value="6">1.5小时</option>';
selectTimeOptions += '<option timeValue="120" value="7">2小时</option>';
selectTimeOptions += '<option timeValue="150" value="8">2.5小时</option>';
selectTimeOptions += '<option timeValue="180" value="9">3小时</option>';
selectTimeOptions += '<option timeValue="240" value="10">4小时</option>';
selectTimeOptions += '<option timeValue="480" value="11">8小时</option>';
selectTimeOptions += '<option timeValue="720" value="12">12小时</option>';
selectTimeOptions += '<option timeValue="1440" value="13">1天</option>';
selectTimeOptions += '<option timeValue="2160" value="14">1.5天</option>';
selectTimeOptions += '<option timeValue="2880" value="15">2天</option>';
selectTimeOptions += '<option timeValue="4320" value="16">3天</option>';
selectTimeOptions += '<option timeValue="5760" value="17">4天</option>';
selectTimeOptions += '<option timeValue="7200" value="18">5天</option>';
selectTimeOptions += '<option timeValue="14400" value="19">10天</option>';
var indexOptions = '<option value="1">第一个</option>';
indexOptions += '<option value="2">第二个</option>';
indexOptions += '<option value="3">第三个</option>';
indexOptions += '<option value="4">最后一个</option>';

var weekOptions = '<option value="1">星期日</option>';
weekOptions += '<option value="2">星期一</option>';
weekOptions += '<option value="3">星期二</option>';
weekOptions += '<option value="4">星期三</option>';
weekOptions += '<option value="5">星期四</option>';
weekOptions += '<option value="6">星期五</option>';
weekOptions += '<option value="7">星期六</option>';
var monthOptions = '<option value="1">1</option>';
monthOptions += '<option value="2">2</option>';
monthOptions += '<option value="3">3</option>';
monthOptions += '<option value="4">4</option>';
monthOptions += '<option value="5">5</option>';
monthOptions += '<option value="6">6</option>';
monthOptions += '<option value="7">7</option>';
monthOptions += '<option value="8">8</option>';
monthOptions += '<option value="9">9</option>';
monthOptions += '<option value="a">10</option>';
monthOptions += '<option value="b">11</option>';
monthOptions += '<option value="c">12</option>';

var dayOptions = '<option value="1">1</option>';
dayOptions += '<option value="2">2</option>';
dayOptions += '<option value="3">3</option>';
dayOptions += '<option value="4">4</option>';
dayOptions += '<option value="5">5</option>';
dayOptions += '<option value="6">6</option>';
dayOptions += '<option value="7">7</option>';
dayOptions += '<option value="8">8</option>';
dayOptions += '<option value="9">9</option>';
dayOptions += '<option value="a">10</option>';
dayOptions += '<option value="b">11</option>';
dayOptions += '<option value="c">12</option>';
dayOptions += '<option value="d">13</option>';
dayOptions += '<option value="e">14</option>';
dayOptions += '<option value="f">15</option>';
dayOptions += '<option value="g">16</option>';
dayOptions += '<option value="h">17</option>';
dayOptions += '<option value="i">18</option>';
dayOptions += '<option value="j">19</option>';
dayOptions += '<option value="k">20</option>';
dayOptions += '<option value="l">21</option>';
dayOptions += '<option value="m">22</option>';
dayOptions += '<option value="n">23</option>';
dayOptions += '<option value="o">24</option>';
dayOptions += '<option value="p">25</option>';
dayOptions += '<option value="q">26</option>';
dayOptions += '<option value="r">27</option>';
dayOptions += '<option value="s">28</option>';
dayOptions += '<option value="t">29</option>';
dayOptions += '<option value="u">30</option>';
dayOptions += '<option value="v">31</option>';
////边界提醒设置
var bjtxHtml_ = [];
bjtxHtml_[bjtxHtml_.length] = '<tr type="settingTR">';
bjtxHtml_[bjtxHtml_.length] = '<td style="width:150px;text-align: right;font-weight: bold;">';
bjtxHtml_[bjtxHtml_.length] = '开始前';
bjtxHtml_[bjtxHtml_.length] = '</td>';
bjtxHtml_[bjtxHtml_.length] = '<td>';
bjtxHtml_[bjtxHtml_.length] = '<select id="bjtxBeforeStart" style="width:200px">';
bjtxHtml_[bjtxHtml_.length] = selectTimeOptions;
bjtxHtml_[bjtxHtml_.length] = '</select>';
bjtxHtml_[bjtxHtml_.length] = '提醒';
bjtxHtml_[bjtxHtml_.length] = '</td>';
bjtxHtml_[bjtxHtml_.length] = '</tr>';
bjtxHtml_[bjtxHtml_.length] = '<tr type="settingTR">';
bjtxHtml_[bjtxHtml_.length] = '<td style="width:150px;text-align: right;font-weight: bold;">';
bjtxHtml_[bjtxHtml_.length] = '结束前';
bjtxHtml_[bjtxHtml_.length] = '</td>';
bjtxHtml_[bjtxHtml_.length] = '<td>';
bjtxHtml_[bjtxHtml_.length] = '<select id="bjtxBeforeEnd" style="width:200px">';
bjtxHtml_[bjtxHtml_.length] = selectTimeOptions;
bjtxHtml_[bjtxHtml_.length] = '</select>';
bjtxHtml_[bjtxHtml_.length] = '提醒';
bjtxHtml_[bjtxHtml_.length] = '</td>';
bjtxHtml_[bjtxHtml_.length] = '</tr>';


//固定间隔设置
var gdjgHtml_ = [];
gdjgHtml_[gdjgHtml_.length] = '<tr type="settingTR">';
gdjgHtml_[gdjgHtml_.length] = '<td style="width:150px;text-align: right;font-weight: bold;">';
gdjgHtml_[gdjgHtml_.length] = '<span class="required">*</span>';
gdjgHtml_[gdjgHtml_.length] = '间隔时间';
gdjgHtml_[gdjgHtml_.length] = '</td>';
gdjgHtml_[gdjgHtml_.length] = '<td>';
gdjgHtml_[gdjgHtml_.length] = ' <input type="text" id="intervalHour" value="0" class="hourFormate" name="intervalHour" style="width:100px"/>';
gdjgHtml_[gdjgHtml_.length] = ' 小时';
gdjgHtml_[gdjgHtml_.length] = '&nbsp;&nbsp;<input type="text" class="" value="10" id="intervalMinute" class="minuteFormate" name="intervalMinute" style="width:100px"/>';
gdjgHtml_[gdjgHtml_.length] = ' 分';
gdjgHtml_[gdjgHtml_.length] = '</td>';
gdjgHtml_[gdjgHtml_.length] = '</tr>';
gdjgHtml_[gdjgHtml_.length] = '<tr type="settingTR">';
gdjgHtml_[gdjgHtml_.length] = '<td style="width:150px;text-align: right;font-weight: bold;">';
gdjgHtml_[gdjgHtml_.length] = '提醒次数';
gdjgHtml_[gdjgHtml_.length] = '</td>';
gdjgHtml_[gdjgHtml_.length] = '<td>';
gdjgHtml_[gdjgHtml_.length] = ' <input type="text" id="remindNum" name="remindNum" style="width:100px"/>';
gdjgHtml_[gdjgHtml_.length] = '</td>';
gdjgHtml_[gdjgHtml_.length] = '</tr>';


//固定周期 设置
var gdzqHtml_ = [];
gdzqHtml_[gdzqHtml_.length] = '<tr type="settingTR">';
gdzqHtml_[gdzqHtml_.length] = '<td style="width:150px;text-align: right;font-weight: bold;">';
gdzqHtml_[gdzqHtml_.length] = '提醒周期';
gdzqHtml_[gdzqHtml_.length] = '</td>';
gdzqHtml_[gdzqHtml_.length] = '<td>';
gdzqHtml_[gdzqHtml_.length] = '<label class="radio inline"><input type="radio" name="txzq" index="1" checked onclick="selectCycle_(1)" />天 </label>';
gdzqHtml_[gdzqHtml_.length] = '<label class="radio inline"><input type="radio" name="txzq" index="2" onclick="selectCycle_(2)"/>周 </label>';
gdzqHtml_[gdzqHtml_.length] = '<label class="radio inline"><input type="radio" name="txzq" index="3" onclick="selectCycle_(3)"/>月 </label>';
gdzqHtml_[gdzqHtml_.length] = '<label class="radio inline"><input type="radio" name="txzq" index="4" onclick="selectCycle_(4)"/>年 </label>';
gdzqHtml_[gdzqHtml_.length] = '</td>';
gdzqHtml_[gdzqHtml_.length] = '</tr>';
/*gdzqHtml_[gdzqHtml_.length] = '<tr type="gdzq" id="day_">';
gdzqHtml_[gdzqHtml_.length] = '<td colspan="2">';
gdzqHtml_[gdzqHtml_.length] = ' <input type="text" id="intervalHour" name="intervalHour" style="width:100px"/>';
gdzqHtml_[gdzqHtml_.length] = ' 点';
gdzqHtml_[gdzqHtml_.length] = '&nbsp;&nbsp;<input type="text" id="intervalHour" name="intervalMinute" style="width:100px"/>';
gdzqHtml_[gdzqHtml_.length] = ' 分';
gdzqHtml_[gdzqHtml_.length] = '</td>';
gdzqHtml_[gdzqHtml_.length] = '</tr>';*/
gdzqHtml_[gdzqHtml_.length] = '<tr type="settingTR" id="week_">';
gdzqHtml_[gdzqHtml_.length] = '<td style="width:150px;text-align: right;font-weight: bold;">';
gdzqHtml_[gdzqHtml_.length] = '<span id="">周期</span>';
gdzqHtml_[gdzqHtml_.length] = '</td>';
gdzqHtml_[gdzqHtml_.length] = '<td>';
gdzqHtml_[gdzqHtml_.length] = '每个&nbsp;</br>';
gdzqHtml_[gdzqHtml_.length] = '<label class="checkbox inline"><input type="checkbox" id="1" name="gdzq_week" />星期日</label>';
gdzqHtml_[gdzqHtml_.length] = '<label class="checkbox inline"><input type="checkbox" id="2" name="gdzq_week" />星期一</label>';
gdzqHtml_[gdzqHtml_.length] = '<label class="checkbox inline"><input type="checkbox" id="3" name="gdzq_week" />星期二</label>';
gdzqHtml_[gdzqHtml_.length] = '<label class="checkbox inline"><input type="checkbox" id="4" name="gdzq_week" />星期三</label>';
gdzqHtml_[gdzqHtml_.length] = '<label class="checkbox inline"><input type="checkbox" id="5" name="gdzq_week" />星期四</label>';
gdzqHtml_[gdzqHtml_.length] = '<label class="checkbox inline"><input type="checkbox" id="6" name="gdzq_week" />星期五</label>';
gdzqHtml_[gdzqHtml_.length] = '<label class="checkbox inline"><input type="checkbox" id="7" name="gdzq_week" />星期六</label>';
gdzqHtml_[gdzqHtml_.length] = '</td>';
gdzqHtml_[gdzqHtml_.length] = '</tr>';
gdzqHtml_[gdzqHtml_.length] = '<tr type="settingTR" id="month_">';
gdzqHtml_[gdzqHtml_.length] = '<td>';
gdzqHtml_[gdzqHtml_.length] = '<span id="">周期</span>';
gdzqHtml_[gdzqHtml_.length] = '</td>';
gdzqHtml_[gdzqHtml_.length] = '<td>';
gdzqHtml_[gdzqHtml_.length] = '</br>';
gdzqHtml_[gdzqHtml_.length] = '<label class="radio inline"><input type="radio" selectId="month_days" index="1" name="month_time" checked />每月</label>';
gdzqHtml_[gdzqHtml_.length] = '<select id="month_days" style="width:100px">';
gdzqHtml_[gdzqHtml_.length] = dayOptions;
gdzqHtml_[gdzqHtml_.length] = '</select>';
gdzqHtml_[gdzqHtml_.length] = '日';
gdzqHtml_[gdzqHtml_.length] = '</br>';
gdzqHtml_[gdzqHtml_.length] = '<label class="radio inline"><input type="radio" index="2" name="month_time"  />每周最后一天 </label>';
gdzqHtml_[gdzqHtml_.length] = '</br>';

gdzqHtml_[gdzqHtml_.length] = '<label class="radio inline"><input type="radio" selectId="month_cycle_" index="3" name="month_time"  />每月 </label>';
gdzqHtml_[gdzqHtml_.length] = '<select id="month_cycle_index" style="width:100px">';
gdzqHtml_[gdzqHtml_.length] = indexOptions;
gdzqHtml_[gdzqHtml_.length] = '</select>';
gdzqHtml_[gdzqHtml_.length] = '<select id="month_cycle_week" style="width:100px">';
gdzqHtml_[gdzqHtml_.length] = weekOptions;
gdzqHtml_[gdzqHtml_.length] = '</select>';
gdzqHtml_[gdzqHtml_.length] = '</br>';
//gdzqHtml_[gdzqHtml_.length] = '</br>';

gdzqHtml_[gdzqHtml_.length] = '</td>';
gdzqHtml_[gdzqHtml_.length] = '</tr>';
gdzqHtml_[gdzqHtml_.length] = '<tr type="settingTR" id="year_">';
gdzqHtml_[gdzqHtml_.length] = '<td>';
gdzqHtml_[gdzqHtml_.length] = '<span id="">周期</span>';
gdzqHtml_[gdzqHtml_.length] = '</td>';
gdzqHtml_[gdzqHtml_.length] = '<td>';
gdzqHtml_[gdzqHtml_.length] = '</br>';
gdzqHtml_[gdzqHtml_.length] = '<div><label class="radio inline"><input type="radio" selectId="year_cycle_1" index="1" name="year_time" checked />每年</label>';
gdzqHtml_[gdzqHtml_.length] = '<select id="year_cycle_1months" style="width:80px">';
gdzqHtml_[gdzqHtml_.length] = monthOptions;
gdzqHtml_[gdzqHtml_.length] = '</select>';
gdzqHtml_[gdzqHtml_.length] = '月';
gdzqHtml_[gdzqHtml_.length] = '<select id="year_cycle_1days" style="width:80px">';
gdzqHtml_[gdzqHtml_.length] = dayOptions;
gdzqHtml_[gdzqHtml_.length] = '</select>';
gdzqHtml_[gdzqHtml_.length] = '日';
gdzqHtml_[gdzqHtml_.length] = '</div>';
gdzqHtml_[gdzqHtml_.length] = '<div style="margin-top:5px;">';
gdzqHtml_[gdzqHtml_.length] = '<label class="radio inline"><input type="radio" selectId="year_cycle_2" index="2" name="year_time"  />每年</label>';
gdzqHtml_[gdzqHtml_.length] = '<select id="year_cycle_2months" style="width:80px">';
gdzqHtml_[gdzqHtml_.length] = monthOptions;
gdzqHtml_[gdzqHtml_.length] = '</select>';
gdzqHtml_[gdzqHtml_.length] = '月';
gdzqHtml_[gdzqHtml_.length] = '<select id="year_cycle_2index" style="width:80px">';
gdzqHtml_[gdzqHtml_.length] = indexOptions;
gdzqHtml_[gdzqHtml_.length] = '</select>';
gdzqHtml_[gdzqHtml_.length] = '<select id="year_cycle_2week" style="width:80px">';
gdzqHtml_[gdzqHtml_.length] = weekOptions;
gdzqHtml_[gdzqHtml_.length] = '</select>';
gdzqHtml_[gdzqHtml_.length] = '</div>';
gdzqHtml_[gdzqHtml_.length] = '</br>';


gdzqHtml_[gdzqHtml_.length] = '</td>';
gdzqHtml_[gdzqHtml_.length] = '</tr>';
gdzqHtml_[gdzqHtml_.length] = '<tr type="settingTR" id="intervalTime_">';
gdzqHtml_[gdzqHtml_.length] = '<td>';
gdzqHtml_[gdzqHtml_.length] = '<span id="intervalSpan1"></span>';
gdzqHtml_[gdzqHtml_.length] = '</td>';
gdzqHtml_[gdzqHtml_.length] = '<td>';
gdzqHtml_[gdzqHtml_.length] = ' <nobr><span class="required">*</span><span id="intervalSpan">每天</span><input type="text" class="hour" id="intervalHour" name="intervalHour" value="0" style="width:100px"/>';
gdzqHtml_[gdzqHtml_.length] = ' 时';
gdzqHtml_[gdzqHtml_.length] = '<span class="required">*</span>&nbsp;&nbsp;<input type="text" id="intervalMinute" value="0" class="minute" name="intervalMinute" style="width:100px"/>';
gdzqHtml_[gdzqHtml_.length] = ' 分';
//gdzqHtml_[gdzqHtml_.length] = '</br>时取值范围为0-23, 分取值范围为0-59</td>';
gdzqHtml_[gdzqHtml_.length] = '</nobr></td>';
gdzqHtml_[gdzqHtml_.length] = '</tr>';

function Remind() {
	//提醒内容
	this.content = '';
	
	// 提醒方式
	//0 不提醒
    //2 内部邮件
    //1 短信
	this.remindMode = 2;
	
	//接收提醒的地址,逗号分隔
	this.receiveId = '';
	
	/*1 边界提醒 
	 * 2固定间隔 
	 * 3 周期日期
	 * 4 一次性*/
	this.remindType = 4;
	
	
	/*提醒周期
    1 天
    2 周
    3 月
    4 年*/
	this.cycle = 1;
	
	this.isActive = '0';
	
	this.tableName = '';
	//存放设定的周期值
	/*选周viewCycle存放长度为7的字符串第一位是周日最后一位是周六，0代表为选中 1代表选中
	 * 
	 * */
	this.viewCycle = '';
	
	//开始时间 回显
	this.viewStartTime = null;
//	this.
	//结束时间 回显
	this.viewEndTime = null;
	
	//开始前 选项option 的value
	this.startForwardTime = 0;
	
	//结束前 选项option 的value
	this.endForwardTime = 0;
	
	
	//结束前时间值 以分钟为单位
	this.endForwardTimeValue = 0;
	
	//开始前时间值 以分钟为单位
	this.startForwardTimeValue = 0;
	this.title = '';
	
	//页面填写的小时数
	this.intervalHour = 0;
	
	//页面天写的分钟数
	this.intervalMinute = 0;
	
	this.remindNum = 0;
	
	//cron表达式
	this.cronExpression = '';
	
	//间隔 数 以分钟为参数
	this.remindInterval = 0;
	
}


/*
 * 
 * 比如<select class="selector"></select>

1、设置value为pxx的项选中

     $(".selector").val("pxx");

2、设置text为pxx的项选中

    $(".selector").find("option[text='pxx']").attr("selected",true);

    这里有一个中括号的用法，中括号里的等号的前面是属性名称，不用加引号。很多时候，中括号的运用可以使得逻辑变得很简单。

3、获取当前选中项的value

    $(".selector").val();

4、获取当前选中项的text

    $(".selector").find("option:selected").text();

    这里用到了冒号，掌握它的用法并举一反三也会让代码变得简洁。

 

很多时候用到select的级联，即第二个select的值随着第一个select选中的值变化。这在jquery中是非常简单的。

如：$(".selector1").change(function(){

     // 先清空第二个

      $(".selector2").empty();

     // 实际的应用中，这里的option一般都是用循环生成多个了

      var option = $("<option>").val(1).text("pxx");

      $(".selector2").append(option);

});



/*
 * JSON.stringify()
 * @specify : serialization(序列化)
 * @method : JSON.stringify(value,filter,indent);
 * @return : JSON字符串
 
 * @param : value {type : String|Object|String|Number|Boolean|null} {explain : 传入的类型可以是列出的這些}
 * @param : filter : {type : []|{}} {explain : 过滤器可以是个数组，也可以是个函数}
 * @param : indent : {type : Number | 特殊符号} {explain : 如果是数字则代表空白符的个数，最多10个;也可以直接传入缩进的符号}
*/
/*var gather = {
    id : 1314,
    name : 'pom',
    infor : {
        age : 20,
        sex : 'man',
        marry : false,
        identity : 622421,
        habit : ['篮球','台球','乒乓球','游戏',true]
    },
    family : ['妈妈','爸爸','弟弟'],
    likeGames : ['PCgame','Netgame']
 
};
var jsonText = JSON.stringify(gather,null,4);
 
//第二个参数是数组，只会序列化返回数组里列出的名称
var jsonText1 = JSON.stringify(gather,['id','family'],'=');
 
var jsonText2 = JSON.stringify(gather,function(key,val){
    switch(key){
        case 'id' :
            return 'id is ' + val;
        case 'family' :
            return val.join('@');
        case 'infor' :
            //infor的val 还可以使用JSON.stringify()
            //return JSON.stringify(val,["age","sex"]);
            return Object.prototype.toString.call(val).slice(8, -1);
        case 'likeGames' :
            //通过返回undefined删除该属性
            return undefined;
        //一定要default,以便传入的其他值能正常的返回到序列化结果中。
        default :
            return val;
    }
},10);
// console.log(jsonText);
// console.log(jsonText1);
// console.log(jsonText2)
 
/*
 * toJSON()
 * @specify : JSON.stringify()不能满足对某些对象进行自定义序列化的需求，可以通过对象那个上调用toJSON()方法
 * @method : date.toJSON()
 * @return : 返回任何序列化的值。
 *
 * JSON.parse() ,eval() 也可以解析 并返回js对象和数组等。但IE8以下浏览器会有安全隐患。
 * @specify : 将json字符串解析为原生的javascript值。
 * @method : JSON.parse(val,replacer)
 
 * @param : val{type : String} {explain : 需要解析的json字符串}
 * @param : replacer {type : function}
                     {explain : 和JSON.stringify()第二个参数类似，接受2个参数，key,val,不过是用来还原json串的函数}
 
*/
/*var products = {
    name : "leading",
    "time" : new Date(2012,03,1),
    toJSON : function(){
        //只返回name
        return this.name;
    }
}
var proStr = JSON.stringify(products);
console.log(proStr);
 
//obj中的relaeseDate对象序列化之后成了有效的json串
var obj = {
    title : '对象的标题',
    type : 'primitive' ,
    releaseDate : new Date(2012,03,1)
};
//转化为json串
var o = JSON.stringify(obj);
console.log(o);
 
//在parsedO中还原为一个Date对象(会基于相应的值创建一个新的Date对象，结果parsedO.releaseDate属性中保存了一个Date对象)
var parsedO = JSON.parse(o,function(k,v){
    if(k == 'releaseDate'){
        return new Date(v);
    }else{
        return v;
    }
});
console.log(parsedO);
//可以调用getFullYear()
console.log(parsedO.releaseDate.getFullYear()); //2012
 执行原理：

    /*
     * toJSON() 作为JSON.stringify中第二个参数(函数过滤器)补充，理解内部顺序很重要。
     * 假设把一个对象传入JSON.stringify() 序列化对象的顺序如下：
     * 
     * (1) 如果存在toJSON()方法而且能通过它取得有效的值，则调用该方法。否则，按默认顺序执行序列化
     * (2) 如果提供了第二个参数，应用这个函数过滤器，传入的函数过滤器的值是第(1)步返回的值。
     * (3) 对第(2)步 返回的每个值进行相应的序列化。
     * (4) 如果提供了第三个参数，执行相应的格式化操作。
    */
 /*
var reg = new RegExp('^[^\\\\\\/:*?\\"<>|]+$');// 转义 \ 符号也不行
alertx(reg.test("新建文件\\夹")); // 弹出 true
*/