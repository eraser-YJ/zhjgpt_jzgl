/**
 * 文档管理--文件夹选择对话框
 * @auth zhangligang
 */

var FolderSelectDialog=(function($){
	"use strict";
	
	/**
	 * Constructor
	 */
	function FolderSelector(container,folderId,callback, folderType){
		//prepare UI
		this.form='';
		this.form+='	<div class="modal-dialog">';
		this.form+='		<div class="modal-content">';
		this.form+='			<div class="modal-header">';
		this.form+='				<button type="button" class="close" data-dismiss="modal">×</button>';
		this.form+='				<h4 class="modal-title">选择文件夹</h4>';
		this.form+='			</div>';
		this.form+='			<div class="modal-body wrapDoc">';
		this.form+='				<ul class="choDoc clearfix">';
		this.form+='				</ul>';
		this.form+='			</div>';
		this.form+='			<div class="modal-footer form-btn">';
		this.form+='				<button class="btn dark" id="btnOk">确 定</button>';
		this.form+='				<button class="btn " data-dismiss="modal">关 闭</button>';
		this.form+='			</div>';
		this.form+='		</div>';
		this.form+='	</div>';
		
		this.container=container.jquery?container:$(container);
		this.container.append(this.form);
		this.container.find('#btnOk').click($.proxy(this,"onokclick"));
		this.callback=callback;
		this.folderId=folderId;
		this.folderType=folderType;
		
		//加载数据并填充界面
//		this.loadData(folderId);
	}
	
	FolderSelector.prototype.clean=function(){
//		this.folderId=null;
		this.subFolders=null;
		this.selected=null;
		this.self=this;
	};
	
	FolderSelector.prototype.setCallback=function(callback){
		this.callback=callback;
	};
	/**
	 * 加载数据
	 */
	FolderSelector.prototype.loadData=function(){
		this.getSubFolders(this.folderId);
//		this.render(this.subFolders);
		
	};
	/**
	 * 异步取得指定文件夹的子文件夹
	 * @param parentId
	 */
	FolderSelector.prototype.getSubFolders=function(parentId){
		this.parentFolderId=parentId;
		var self=this;
		$.ajax({
			type : "GET",
			url : getRootPath() + "/archive/folder/getSubDir.action",
			data : {
				parentFolderId : parentId,
				folderType:self.folderType
			},
			dataType : "json",
			success : function(data) {
				if (data.success == "true") {
					if(data.folders!=null && data.folders.length>0){
						self.selected=null;
						self.subFolders=data.folders;
						self.render(data.folders,self);
					}
					else{
						msgBox.info({
							type : "success",
							content : $.i18n.prop("JC_OA_ARCHIVE_001")
						});
					}
				} else {
					if (data.errorMessage) {
						msgBox.info({
							content : data.errorMessage
						});
					} else {
						msgBox.info({
							content : $.i18n.prop("JC_SYS_060")
						});
					}
				}
			}
		});
	};
	/**
	 * 画出文件夹列表
	 */
	FolderSelector.prototype.render=function(folders,self){
		self.container.find(".modal-body > ul").empty();
		$.each(folders,function(i,f){
//			var folder=$('<span class="btn"><label class="btn i-file"><i class="fa fa-folder-close"></i></label><span style="display:block">'+f.folderName+'</span></span>');
			var folder=$('<li><i></i><span>'+f.folderName+'</span></li>');
			folder.click($.proxy(self,"onclick"));
			folder.dblclick($.proxy(self,"ondbclick"));
			self.container.find(".modal-body >ul ").append(folder);
		});
	};
	/**
	 * 文件夹的单击事件，
	 * 设置当前文件夹为选中状态，
	 * 设置其它文件夹取消选中状态，
	 * @param ev
	 */
	FolderSelector.prototype.onclick=function(ev){
		var folder=$(ev.currentTarget);
		var index=folder.index();
		this.selected=this.subFolders[index];
		//TODO 设置样式
		folder.addClass("docAct");
		folder.siblings().removeClass("docAct");
	};
	/**
	 * 文件夹的双击事件
	 * 双击进入
	 * 执行异步查询，重新渲染界面
	 * @param ev
	 */
	FolderSelector.prototype.ondbclick=function(ev){
		var folder=$(ev.currentTarget);
		var index=folder.index();
		var folder=this.subFolders[index];
		this.folderId=folder.id;
		this.loadData();
	};
	/**
	 * 确定按钮事件
	 * 关闭窗口
	 * 调用回调函数
	 * 返回: 选中的文件夹对象
	 * @param ev
	 */
	FolderSelector.prototype.onokclick=function(ev){
		this.hide();
		if(this.callback){
			this.callback(this.selected);
		}
		this.clean();
	};
	/**
	 * 显示组件
	 */
	FolderSelector.prototype.show=function(){
		this.container.modal('show');
		this.loadData();
	};
	/**
	 * 隐藏组件
	 */
	FolderSelector.prototype.hide=function(){
		this.container.modal('hide');
	};
	
	return FolderSelector;
}(jQuery));

//@ sourceURL=FolderSelectDialog.js