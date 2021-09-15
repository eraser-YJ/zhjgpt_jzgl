/**
 * 级联数据完整性检查
 * @auther zhanglg
 * 2014/07/28 
 */

var DeleteCascade={};
	
	/**
	 * 验证级联数据是否可以删除:
	 * functionId, 配置文件中function id;
	 * businessValue 待验证的业务值;
	 * callback 回调方法，参数(为true,可以删除；为false，不可以删除)
	 */
DeleteCascade.checkCanDelete=function(functionId,businessValue,callback){
		$.ajax({
			type : "Post",
			url : getRootPath() + "/cascade/canDelete.action",
			data : {
				'bizId' : functionId,
				'columnValue' : businessValue
			},
			dataType : "json",
			success : function(data) {
				if(typeof ( callback)!=undefined && callback !=null){
					callback(data);
				}
			},
			error:function(data){
				msgBox.tip({
					content : $.i18n.prop("JC_SYS_055")
				});
			}
		});
	}
	
	/**
	 * 同步验证级联数据是否可以删除:
	 * functionId, 配置文件中function id;
	 * businessValue 待验证的业务值;
	 * canDel 返回值，为true,可以删除；为false，不可以删除
	 */
DeleteCascade.syncCheckCanDelete=function(functionId,businessValue){
		var canDel=true;
		$.ajax({
			type : "Post",
			url : getRootPath() + "/cascade/canDelete.action",
			data : {
				'bizId' : functionId,
				'columnValue' : businessValue
			},
			async:false, 
			dataType : "json",
			success : function(data) {
				canDel=data;
			},
			error:function(data){
				msgBox.tip({
					content : $.i18n.prop("JC_SYS_055")
				});
			}
		});
		
		return canDel;
	}
	
	/**
	 * 同步验证级联数据是否可以删除:
	 * functionId, 配置文件中function id;
	 * businessValue 待验证的多个业务值，用逗号分隔;
	 * canDel 返回值，为true,可以删除；为false，不可以删除
	 */
DeleteCascade.syncCheckCanBatchDelete=function(functionId,businessValue){
		var canDel=true;
		$.ajax({
			type : "Post",
			url : getRootPath() + "/cascade/canBatchDelete.action",
			data : {
				'bizId' : functionId,
				'columnValue' : businessValue
			},
			async:false, 
			dataType : "json",
			success : function(data) {
				canDel=data;
			},
			error:function(data){
				msgBox.tip({
					content : $.i18n.prop("JC_SYS_055")
				});
			}
		});
		
		return canDel;
	};
/**
 * 同步验证业务数据是否存在:
 * functionId, 配置文件中function id;
 * businessValue 待验证的业务值;
 * exist 返回值，为true,存在；为false，不存在
 */
DeleteCascade.syncCheckExist=function(functionId,businessValue){
	var exist=true;
	$.ajax({
		type : "Post",
		url : getRootPath() + "/cascade/checkExist.action",
		data : {
			'functionId' : functionId,
			'columnValue' : businessValue
		},
		async:false, 
		dataType : "json",
		success : function(data) {
			exist=data;
		},
		error:function(data){
			msgBox.tip({
				content : $.i18n.prop("JC_OA_COMMON_012")
			});
		}
	});
	
	return exist;
};

//@ sourceURL=DeleteCascade.js