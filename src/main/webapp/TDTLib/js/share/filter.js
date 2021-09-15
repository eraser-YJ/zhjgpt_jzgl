
function better(curZoom,betterZoom,list,utils,result){
	if(curZoom <=1){
		var geom = "POINT (108 32)";
		if(result.searchGeom)
			geom = result.searchGeom;
		return merge(list,geom,utils,result);
	}
	//判断展示方式
	if(curZoom <= (betterZoom - 2)){
		//改变聚合的值
		if(result.searchGeom)
			return merge(list,result.searchGeom,utils,result);
	}
	
}
/**
 * 根据
 */
function getZoomByRegion(region){
	//根据层级获取
	switch(region){
    	case 'provName':
    		return 2;
    	case 'cityName':
    		return 5;
    	case 'cntyName':
			return 8;
			default:
	}
}

/**
 * 新增聚合方式
 */
function merge(list,searchGeom,utils,result){
	//获取总数
	var count = 0;
	for(var i = 0; i < list.length; i++){
		var obj = list[i];
		//
		count += parseInt(obj.count);
	}
	//伪造结果
	var kt = utils.getCenterFromWKT(searchGeom);
	//为0处理
	if(!count){
		count = result.count;
	}
	var li = [{name:'',count:count,wkt:kt}];
	return li;
}

/**
 * 信息提示数据量大
 */
function tipsOnline(){
	var bot = $('.tes-infoPrj').css('bottom');
	if(bot != '0px'){
		$('.tes-infoPrj').css('bottom','0px');
	}
	setTimeout(function(){
		$('.tes-infoPrj').css('bottom','-50px');
	},5000);
}