/**
 * 文档管理--文件夹选择对话框
 * @auth zhangligang
 */

var FolderSelectTree=(function($){
	"use strict";
	
	/**
	 * Constructor
	 */
	function FolderSelector(container,callback, folderType){
		//prepare UI
		this.form='';
		this.form+='	<div class="modal-dialog">';
		this.form+='		<div class="modal-content">';
		this.form+='			<div class="modal-header">';
		this.form+='				<button type="button" class="close" data-dismiss="modal">×</button>';
		this.form+='				<h4 class="modal-title">选择文件夹</h4>';
		this.form+='			</div>';
		this.form+='			<div class="modal-body wrapDoc ztree" style="overflow:auto;">';
		this.form+='			</div>';
		this.form+='			<div class="modal-footer form-btn">';
		this.form+='				<button class="btn dark" id="btnOk">确 定</button>';
		this.form+='				<button class="btn " data-dismiss="modal">取 消</button>';
		this.form+='			</div>';
		this.form+='		</div>';
		this.form+='	</div>';
		
		this.container=container.jquery?container:$(container);
		this.container.append(this.form);
		this.container.find('#btnOk').click($.proxy(this,"onokclick"));
		this.callback=callback;
		this.folderType=folderType;
		this.tree=null;
		//加载数据并填充界面
//		this.loadData(folderId);
	}
	FolderSelector.prototype.setting={
			check:{
				enable:false,
				nocheckInherit:false,
				chkStyle:"radio",
				radioType:"all"
			},
			view:{
				selectedMulti:false,
				showLine:true
			},
			data:{
				simpleData:{enable:true}
			}
	};
	
	FolderSelector.prototype.clean=function(){
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
	};
	/**
	 * 异步取得指定文件夹的子文件夹
	 * @param parentId
	 */
	FolderSelector.prototype.getSubFolders=function(){
		var self=this;
		$.ajax({
			type : "GET",
			url : getRootPath() + "/archive/folder/getSubDirTree.action",
			data : {
				folderType:self.folderType==10000?4:self.folderType
			},
			dataType : "json",
			success : function(data) {
				self.container.find(".modal-body").empty();
				if (data.success == "true") {
					if(data.folders!=null && data.folders.length>0){
						self.selected=null;
						self.subFolders=data.folders;
						self.render(data.folders,self);
					}
					else{
						msgBox.info({
//							type : "success",
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
							content : $.i18n.prop("JC_OA_ARCHIVE_032")
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
		var zNodes=[];
		$.each(folders,function(i,f){
			zNodes[i]={
					id:f.id,
					pId:f.parentFolderId,
					name:f.folderName
			};
		});
		
		this.tree=$.fn.zTree.init(self.container.find(".modal-body"),self.setting,zNodes);
		this.tree.expandAll(true);
	};
	
	
	/**
	 * 确定按钮事件
	 * 关闭窗口
	 * 调用回调函数
	 * 返回: 选中的文件夹对象
	 * @param ev
	 */
	FolderSelector.prototype.onokclick=function(ev){
		if(this.tree == null){
			msgBox.info({
				content : $.i18n.prop("JC_OA_ARCHIVE_018")
			});
			return;
		}
		var nodes=this.tree.getSelectedNodes();
		if(nodes.length==0){
			msgBox.info({
				content : $.i18n.prop("JC_OA_ARCHIVE_018")
			});
			return;
		}
		if(this.folderType==10000 && (!nodes[0].pId || nodes[0].pId==0)){
			msgBox.info({
				content : $.i18n.prop("JC_OA_ARCHIVE_018")
			});
			return;
		}
		if(!this.folderType==1000){
			this.hide();
		}
		if(this.callback){
			this.callback(nodes[0].id,nodes[0].pId);
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

//@ sourceURL=FolderSelectTree.js