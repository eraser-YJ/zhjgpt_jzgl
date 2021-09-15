/***
 * @Description 提示窗口管理类(单例模式)
 * @Author YuanJianHua
 * @CreateTime 2015年12月15日
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark: 
 */
var AlarmTools=(function(){
	
	var _instance;
	
	var _init=function(){
		
		return{
			
			/**
		     * 弹出类型选择警告窗口
		     */
		    popUpAlarmBySelectType:function(){
		    	
		    	jAlert('请选择查询类型', '提示对话框');
		 		$('#popup_container').css("top", "300px");
		 		$('#popup_container').css("left", "400px");
		    },
		    
		    /**
		     * 弹出搜索录入警告窗口
		     */
		    popUpAlarmBySearchKey:function(){
		    	
		    	jAlert('请输入关键字', '提示对话框');
		 		$('#popup_container').css("top", "300px");
		 		$('#popup_container').css("left", "400px");
		    },
		    
		    /**
		     * 弹出图形相交为空警告窗口
		     */
		    popUpAlarmByWKTNULL:function(){
		    	
		    	jAlert('空间范围不相交，请重新设置！', '警告对话框');
		 		$('#popup_container').css("top", "300px");
		 		$('#popup_container').css("left", "400px");
		    },
		    
		    /**
		     * 弹出图形自相交警告窗口
		     */
		    popUpAlarmByWKTSelf:function(){
		    	jAlert('图形自相交，请重新设置！', '警告对话框');
		 		$('#popup_container').css("top", "300px");
		 		$('#popup_container').css("left", "400px");
		    },
		    
		    popUpAlarmByNoResult:function(){
		    	jAlert('未查询到结果！', '提示对话框');
		 		$('#popup_container').css("top", "300px");
		 		$('#popup_container').css("left", "400px");
		    },
		    
		    popUpAlarmByNoResType:function(){
		    	jAlert('请选择类别！', '提示对话框');
		 		$('#popup_container').css("top", "300px");
		 		$('#popup_container').css("left", "400px");
		    },
		    
		    tipsOnline : function(){
		    	$('.tes-infoPrj').css('bottom','0');
		    	setTimeout(function(){
		    		$('.tes-infoPrj').css('bottom','-50px');
		    	},5000);
		    },
			
		};
	};
	
	return{
		
		getInstance:function(){
			if(!_instance){
			  _instance=_init();
		    }
		    return _instance;
		}
	};


})();