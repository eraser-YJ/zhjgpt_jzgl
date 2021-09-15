/**
 * 版本筛选器
 */
var teme = function(resCons,filters){
	//筛选内容
	var resCon = this.resCon = resCons;
	//筛选结果{key:[]}
	var filterRes = this.filterRes = {};
	//筛选的key
	var filter = this.filter = filters;
	
	//初始化
	this._init = function(){
		if(resCon){
			for(var i = 0; i < resCon.length; i++) {
				//筛选的key的值
				var key = resCon[i][filter];
				if(!key) continue;
				//获取已筛选结果
				var res = filterRes[key];
				//判断筛选结果是否含有此命中的值
				if(res) {
					res.push(resCon[i]);
				} else {
					var temp = new Array();
					temp.push(resCon[i]);
					filterRes[key] = temp;
				}
			}
		}
	}
	//添加key到所在单元格
	this.appendValues = function(){
		html = '';
		for(key in filterRes){
			html += '<p>'+key+'</p>';
		}
		html += '<p>清除</p>';
		$('.version-list').append(html);
	}
	//返回分类结果
	this.getResCont = function(){
		return this.filterRes;
	}
	this._init.apply(this, arguments);
}