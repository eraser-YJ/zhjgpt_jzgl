/**
 * 文档目录导航条
 */
var FolderPathBar=(function($){
	"use strict";
	
	var busy=false;
	
	/**
	 * Constructor
	 * dom 导航条插入此DOM元素之前
	 * callback 点击、返回的回调方法
	 */
	function pathBar(dom,callback){
		this.container=$("<ul class='breadcrumb-list  hide' style='height:30px;'></ul>");
		
		this.beforeDom=dom;
		this.container.insertBefore(this.beforeDom);
		this.callback=callback;
		//目录层次的数组
		this.idArray=new Array();
	}
	
	/**
	 * 增加一级目录
	 * @param id 目录id
	 * @param name 目录名
	 */
	pathBar.prototype.appendPath=function(id,name){
		if(this.idArray[this.idArray.length-1]===id)
		{
			return;
		}
		var link='<a href="javascript:void(0);"></a>';
		var lastli=this.container.children("li:last-child");
		lastli.children('span:eq(0)').wrap(link);
		//绑定点击事件
		lastli.children('a').one("click",$.proxy(this,"pathClick"));
		//创建目录节点
		var li=$('<li style="float:left;"><span>'+name+'</span> <span class="divider">/</span></li>');
		//新节点添加到目录数组中
		this.idArray.push(id);
		//新节点添加到界面
		this.container.append(li);
		//如果界面上的目录层次大于1，显示
		if(this.container.children().length>1){
			this.show();
		}
		else{
			this.hide();
		}
	};
	/**
	 * 目录单击事件
	 */
	pathBar.prototype.pathClick=function(ev){
		if(this.callback && busy==false){
			busy=true;
			var link=$(ev.currentTarget);
			//取得当前点击li的索引
			var index=link.parent().index();
			//根据索引取得目录id
			var id=this.idArray[index];
			$.when(
				this.callback(id)
			).then(
				this.pathClickSuccess(index),void(0)
			);
			
			busy=false;
		}
	};
	
	pathBar.prototype.pathClickSuccess=function(index){
		//删除目录数组中当前节点之后数据
		this.idArray.splice(index+1, this.idArray.length-index);
		//删除界面上当前节点之后节点
		this.container.children('li:gt('+index+')').remove();
		//设置最后一个节点不可点击
		this.container.children("li:last-child").children("a").children("span:eq(0)").unwrap();
		if(this.container.children().length>1){
			this.show();
		}
		else{
			this.hide();
		}
	};
	/**
	 * 向上返回一级目录
	 */
	pathBar.prototype.back=function(){
		if(busy==false){
			this.container.children("li:last-child").prev().children("a").click();
		}
	};
	
	pathBar.prototype.hide=function(){
		this.container.hide();
	};
	
	pathBar.prototype.show=function(){
		this.container.show();
	};
	
	return pathBar;
}(jQuery));

//@ sourceURL=FolderPathBar.js