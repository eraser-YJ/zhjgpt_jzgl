var logoList = {};

logoList.selLogo = function(logoId,logoFlag,logoPath){
	//alert(logoId);
	$("#id").attr("value",logoId);
	$("#viewImg").attr("src",logoPath);
};

var loginSystem = {};
//保存登录页设置
logoList.save = function(){
	/*if(loginSystem.useObj){
		alert(loginSystem.useObj.id);
	}*/
	//return false;
	$.ajax({
		type : 'POST',
		url : getRootPath()+"/sys/logo/updateSet.action",
		data : {"id": loginSystem.useObj.id},
		dataType : "json",
		success : function(data) {
			//data.type == success | fail 返回成功或失败属性和说明
			msgBox.tip({
				type:"success",
				content: "保存成功"
			});
		}
	});
}

logoList.logoView = function(){
	//后台传递来的img对象数组
	loginSystem.imgs = JSON.parse(logojson);
	//初始化轮播图
	$('#loginSlide').slide({
		mainCell: '.bd ul',
		effect: 'left',
		trigger: 'click',
		vis : 2,
		autoPlay: false,
		endFun: function(index, conut, context){
			loginSystem.useObj = loginSystem.imgs[index];
			//index    当前移动到的元素下标
			//conut    一共有多少个元素
			//context  当前轮播图根元素jQuery对象
		}
	});
	//选中当前使用的登录页面
	for(var i = 0;i < loginSystem.imgs.length;i++){
		var obj = loginSystem.imgs[i];
		if(obj.logoFlag == 1){
			$('#defaultImg'+obj.id).trigger('click');
			break;
		}
	}
}

$(document).ready(function(){
	logoList.logoView();

});