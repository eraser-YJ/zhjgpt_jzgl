
var formate = function(obj){
	var index = 1,topL = 0,topR = 0,left = 0,_top = 0;
	
	obj.each(function(){
		//获取当前元素高度
		var height = $(this).height() + 5;
		//宽度
		var width = $(this).width() + 5;
		//根据高度判断浮动位置
		if(topL > topR) {
			left = 1 * width;
			_top = topR;
			topR += height;
		} else {
			left = 0 * width;
			_top = topL
			topL += height;
		}
//		$(this).animate({
//			top : _top,
//			left : left
//		},100);
		$(this).css({'left':left+'px','top':_top+'px'});
	});
	if(topL>topR){
		return topL;
	}else{
		return topR
	}
}
