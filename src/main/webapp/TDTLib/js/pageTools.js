/**
 * 前台分页工具类
 * @auto 
 */
var pageTools = function(data,pageSize){
	
	//操作数据对象
	var _$data = data;
	//总页数
	var totalPage = 0;
	//当前页
	var currentPage = 1;
	//数据总量
	var count = 0;
	//分页尺寸
	var pageSize = pageSize;
	//起始行
	var startRows = 0;
	/**
	 * 计算分页
	 */
	this.calculateTotalPage = function(){
		//如果总数为0，则返回
		if(count == 0) return;
		//
		var _count = Math.floor(count / pageSize);
		//计算是否有余数
		var remainder = count % pageSize;
		if(remainder > 0) {
			_count++;
		}
		totalPage = _count;
	}
	
	/**
	 * 计算起始行
	 */
	this.calculateStartRows = function(currentPae){
		currentPage = currentPae;
		//如果页数已经为最后一页
		if(currentPage >= totalPage) {
			startRows = (totalPage - 1) * pageSize;
			currentPage = totalPage;
		} else
		//如果当前页第一页
		if(currentPage <= 1) {
			startRows = 0;
			currentPage = 1;
		} else {
			//起始行
			startRows = (currentPage - 1) * pageSize;
		}
		if(startRows < 0){
			startRows = 0;
			currentPage = 1;
		}
		return startRows;
	}
	
	/**
	 * 到某页
	 * 刷新,跳到某页,上一页,下一页,首页,尾页均可调用此方法
	 */
	this.go = function(centPage){
		this.countNum();
		this.calculateTotalPage();
		this.calculateStartRows(centPage);
		var res = this.result();
		return res;
	}
	
	/**
	 * 计算总数
	 */
	this.countNum = function(){
		if(_$data) {
			count = _$data.length
		} else {
			$('#shopCar').html('购物车(0)');
			$('.pageInfo').text('0/0');
			$('.carBot').html('');
			$('.pageNo').text('0');
		}
	}
	
	/**
	 * 返回要获取的数组
	 */
	this.result = function(){
		var temp = [];
		for(var i = startRows;i < (startRows + pageSize); i++) {
			if(i > (count - 1)) break;
			var dat = _$data[i];
			temp.push(dat);
		}
		return temp;
	}
	
	/**
	 * 返回分页信息
	 */
	this.getPageInfo = function(){
		var info = {
			'totalPage' : totalPage,
			'currentPage' : currentPage
		}
		return info;
	}
}