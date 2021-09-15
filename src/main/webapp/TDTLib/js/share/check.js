var ic = function(){
	var CLASS = ic, thi$ = CLASS.prototype;
	if (CLASS.__defined__) {
       this.init.apply(this, arguments);
       return;
	}
	CLASS.__defined__ = true;
	
	//点击选中的
	this.Connection = [];
	//OLayer2
	this.olyaer = null;
	//mappage
	this.mapPage = null;
	//
	this.mana = null;
	/**
	 * dianji
	 */
	this.sur = function(point,list){
		//清空之前点击保存的数据
		this.Connection = [];
		for(var i = 0;i<list.length;i++) {
			var wkt = list[i].geom;
			if(!wkt) continue;
			var vector = this.olyaer.getUtils().getVectorFromWKT(wkt);
			var flag = vector.geometry.intersects(point);
			if(flag) {
				this.Connection.push(list[i]);
			}
		}
		return this.Connection;
	}
	
	this._init = function(def) {
		this.olyaer = def.ol;
		this.mapPage = def.mapPage;
		this.mana = def.mana;
	}
	this._init.apply(this, arguments);
}