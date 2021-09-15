dateUtils = {};	

/**
 * 时间差
 */
dateUtils.diffDay = function(beginDateStr,endDateStr) {
	//开始时间
	var date1 = new Date(dateUtils.getDate(beginDateStr)) 
	//结束时间
	var date2 = new Date(dateUtils.getDate(endDateStr)) 
	var date3=date2.getTime()-date1.getTime()  //时间差的毫秒数
	//计算出相差天数
	var days=Math.floor(date3/(24*3600*1000))
	return days;
};

//字符串转日期格式，strDate要转为日期格式的字符串
dateUtils.getDate = function(strDate) {
    var date = eval('new Date(' + strDate.replace(/\d+(?=-[^-]+$)/,
     function (a) { return parseInt(a, 10) - 1; }).match(/\d+/g) + ')');
    return date;
}

/**
 * 格式化时间 返回yyyy-MM-dd
 */
dateUtils.format = function(paraDate) {
	var year = paraDate.getFullYear();// 年
	var month = paraDate.getMonth() + 1; // 月
	var m = "";
	if (month < 10) {
		m = "0" + month;
	} else {
		m = month;
	}
	var day = paraDate.getDate(); // 日
	var d = "";
	if (day < 10) {
		d = "0" + day;
	} else {
		d = day;
	}
	var str = year + "-" + m + "-" + d;
	return str;
};

/**
 * 格式化时间 返回yyyy-MM
 */
dateUtils.formatYM = function(paraDate) {
	var year = paraDate.getFullYear();// 年
	var month = paraDate.getMonth() + 1; // 月
	var m = "";    
	if (month < 10) {
		m = "0" + month;
	} else {
		m = month;
	}
	var str = year + "-" + m;
	return str;
};

/**
 * 格式化成MM-dd
 */
dateUtils.formatDM = function(paraDate) {
	var year = paraDate.getFullYear();// 年
	var month = paraDate.getMonth() + 1; // 月
	var m = month;
	var day = paraDate.getDate(); // 日
	var d = day;
	var str = m + "-" + d;
	return str;
};

/**
 * 格式化成MM月dd日
 */
dateUtils.formatDM2 = function(paraDate) {
	var year = paraDate.getFullYear();// 年
	var month = paraDate.getMonth() + 1; // 月
	var m = month;
	var day = paraDate.getDate(); // 日
	var d = day;
	var str = m + "月" + d + "日";
	return str;
};

/**
 * 时间+n天 通过毫秒数实现
 */
dateUtils.addDays = function(paraDate, paraCount) {
	var temp = paraDate.valueOf() + (1 * 24 * 60 * 60 * 1000) * (paraCount * 1);
	return new Date(temp);
};

/**
 * 是否在范围内
 */
dateUtils.isIn = function(beginTime, endTime, nowTime) {
	var b = new Date(beginTime.replace(/-/g, "/"));
	var e = new Date(endTime.replace(/-/g, "/"));
	var n = new Date(nowTime.replace(/-/g, "/"));
	if (n >= b && n <= e) {
		return true;
	} else {
		return false;
	}
}
/**
 * 是否在范围内
 */
dateUtils.isDateIn = function(beginTime, endTime, nowTime) {
	var b = new Date(beginTime.replace(/-/g, "/"));
	var e = new Date(endTime.replace(/-/g, "/"));
	var n = nowTime;
	if (n >= b && n <= e) {
		return true;
	} else {
		return false;
	}
}

/*
 * 时间格式化工具 
 * 把Long类型的1527672756454日期还原yyyy-MM-dd 00:00:00格式日期   
 */    
function datetimeFormat(longTypeDate){    
    var dateTypeDate = "";    
    var date = new Date();    
    date.setTime(longTypeDate);    
    dateTypeDate += date.getFullYear();   //年    
    dateTypeDate += "-" + getMonth(date); //月     
    dateTypeDate += "-" + getDay(date);   //日    
    dateTypeDate += " " + getHours(date);   //时    
    dateTypeDate += ":" + getMinutes(date);     //分  
    dateTypeDate += ":" + getSeconds(date);     //分  
    return dateTypeDate;  
}   
/*  
 * 时间格式化工具 
 * 把Long类型的1527672756454日期还原yyyy-MM-dd格式日期   
 */    
function dateFormat(longTypeDate){    
    var dateTypeDate = "";    
    var date = new Date();    
    date.setTime(longTypeDate);    
    dateTypeDate += date.getFullYear();   //年    
    dateTypeDate += "-" + getMonth(date); //月     
    dateTypeDate += "-" + getDay(date);   //日    
    return dateTypeDate;  
}   
//返回 01-12 的月份值     
function getMonth(date){    
    var month = "";    
    month = date.getMonth() + 1; //getMonth()得到的月份是0-11    
    if(month<10){    
        month = "0" + month;    
    }    
    return month;    
}    
//返回01-30的日期    
function getDay(date){    
    var day = "";    
    day = date.getDate();    
    if(day<10){    
        day = "0" + day;    
    }    
    return day;    
}  
//小时  
function getHours(date){  
    var hours = "";  
    hours = date.getHours();  
    if(hours<10){    
        hours = "0" + hours;    
    }    
    return hours;    
}  
//分  
function getMinutes(date){  
    var minute = "";  
    minute = date.getMinutes();  
    if(minute<10){    
        minute = "0" + minute;    
    }    
    return minute;    
}  
//秒  
function getSeconds(date){  
    var second = "";  
    second = date.getSeconds();  
    if(second<10){    
        second = "0" + second;    
    }    
    return second;    
}

