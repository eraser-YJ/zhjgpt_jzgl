/**
 * @title GOA V2.0
 * @description 公共组别和个人组别验证功能
 * Copyright (c) 2014 ccyixun.cn Inc. All Rights Reserved
 * Company 长春奕迅建筑智能化工程有限公司
 * @author 都业广
 * @version  2014-07-24 13:33
 */
$(document).ready(function(){
	
    //初始化校验方法
	$("#groupForm").validate({
		ignore: ".ignore",
		//验证规则(required:必须输入,maxlength:最大长度,digits:正整数)
        rules: {
        	//组名称
        	name: 
        	{
        		required: true,
        		specialChar: true,
			    maxlength: 50
        	},
        	//组成员
        	groupMember:
        	{
        		validSelect2 : "groupMember"
        	}
        }
	});
});