var mouse = function(){
	
    /**
     * 
     */
    this.controldiv = function(obj){
    	//经度
    	var lon = obj.lon + '';
    	//纬度
    	var lat = obj.lat + '';
    	//测试
    	var t = this.countLonlat(lat,false);
    	var n = this.countLonlat(lon,true);
    	$('#mouseposition').text(n +','+ t);
    	$('#OpenLayers_Control_MousePosition_5').text(n +','+ t);
    }
    
    /**
     * 计算经纬度，精确到分
     * @param pos 坐标
     * @dre {boolean} true:经度 false:纬度
     */
    this.countLonlat = function(pos,dre){
    	//获取小数点位置,如果没有小数点则直接返回此数
    	var float = pos.indexOf('.');
    	if(float == -1){
    		return pos + '°';
    	}
    	//整数部分
    	var bo = pos.substring(0,float);
    	//截取小数,取得结果为整数
    	var decimals = pos.substring(float);
    	//将获得的小数点后面的数转换为小数,补零
    	var realFloat = Number('0' + decimals);
    	//取分运算
    	var m = realFloat * 60;
    	//判断是否含有秒
    	if(m > 1) {
    		if((m+'').indexOf('.') != -1){	//有miao
    			//四舍五入
    			m = Math.round(m);
    		}
    	} else {
    		var temp = m + '';
    		m = temp.substring(0,temp.indexOf('.'));
    		
    	}
    	//结果配置
    	var return$result = bo + '°' + m + '′';
    	//判断结果属于哪个半球
    	if(dre){	//区分东西半球
    		if(Number(bo) > 0)
    			return$result = '东经:' + return$result;
    		if(Number(bo) < 0)
    			return$result = '西经:' + return$result;
    		if(Number(bo) == 0)
    			return$result = '经度:' + return$result;
    	}else{	//区分南北半球
    		if(Number(bo) > 0)
    			return$result = '北纬:' + return$result;
    		if(Number(bo) < 0)
    			return$result = '南纬:' + return$result;
    		if(Number(bo) == 0)
    			return$result = '纬度:' + return$result;
    	}
    	//去掉-
    	return return$result.replace('-', '');
    }
}