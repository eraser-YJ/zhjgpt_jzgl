//设置抓取查看的发文单或者收文单表单元素为文本
docFromView = {};

//各个组件的状态判断
docFromView.type={};

docFromView.type["textinput"]={
		hide:function(obj){
			obj.remove();
		},
		read:function(obj){
			var itemId = obj.attr("itemName");
			if($("#"+itemId).is('input')){
				var label = $("#"+itemId).val();
				obj.html(label);
			}else{
				obj.html(obj.text());
			}
		}
};

docFromView.type["hidden"]={
	hide:function(obj){
		obj.hide();
	},
	read:function(obj){
		obj.hide();
	}
};


docFromView.type["radio"]={
		hide:function(obj){
			obj.remove();
		},
		read:function(obj){
			var checkedLabel =  obj.text();
			var checkEle = obj.find("input:radio:checked");
			if(checkEle.length==1){
				checkedLabel = 	checkEle.attr("label");
			}
			obj.html(checkedLabel);
		}
};

docFromView.type["checkbox"]={
		hide:function(obj){
			obj.remove();
		},
		read:function(obj){
			var checkedLabel =  obj.text();
			obj.find("input:checkbox:checked").each(function(index,item){
				checkedLabel+=$(item).attr("label")+",";
			});
			if(checkedLabel.length>0){
				checkedLabel = checkedLabel.substring(0,checkedLabel.length);
			}
			obj.html(checkedLabel);
		}
};

docFromView.type["select"]={
		hide:function(obj){
			obj.remove();
		},
		read:function(obj){
			var selectLabel = obj.text();
			var itemname = obj.attr('itemname');
			var selects = obj.find("select[name='"+itemname+"'] option[selected=selected]");
			selects.each(function(index,item){
			//obj.find("select option[selected=selected]").each(function(index,item){
				if($(item).val()==null||$(item).val().length==0){
					selectLabel="";
				}else{
					selectLabel=$(item).html();
				}
			});
			obj.html(selectLabel);
		}
};

docFromView.type["button"]={
		hide:function(obj){
			obj.remove();
		},
		read:function(obj){
			//暂时没有添加只读操作
			obj.hide();
		}
};

//人员选择框
docFromView.type["userSelect"]={
		hide:function(obj){
			obj.remove();
		},
		read:function(obj){
			var itemId = obj.attr("itemName");
			var divId = itemId.split("!")[0];
			$("#"+divId).html(obj.text().replace("没有找到匹配项","").replace("展开","").replace("清空","").replace("收起",""));
		}
};

//动态添加行
docFromView.type["autoRow"]={
		hide:function(obj){
			obj.remove();
		},
		read:function(obj){
			obj.find("[autoFlowForm]").each(function(){
				var autoItem = $(this);
				var autoItemType = $(this).attr("autoFlowForm");
				formPriv.type["autoRow"].type[autoItemType].read(autoItem);
			});
			//去操作列
			obj.find("[operate]").each(function(index,item){
				$(item).remove();
			})
		}
};

//动态添加行
docFromView.type["editor"]={
		hide:function(obj){
			obj.hide();
		},
		read:function(obj){
			var itemId = obj.attr("itemName");
			var editor = UE.getEditor(itemId);
			editor.ready( function( editor ) {
				UE.getEditor(itemId).setDisabled();
			 });
		}
};

//容器
docFromView.type["container"]={
		hide:function(obj){
			obj.hide();
		},
		read:function(obj){
		}
};

//动态添加行具体类型
docFromView.type["autoRow"].type = {};

//动态添加行-文本输入框
docFromView.type["autoRow"].type["textinput"]={
	read:function(obj){
		var textObj = obj.find("input");
		obj.parent().html(textObj.val());
	}
};

//动态添加行-文本输入域
docFromView.type["autoRow"].type["textarea"]={
	read:function(obj){
		var textObj = obj.find("textarea");
		obj.parent().html(textObj.val());
	}
};

//动态添加行-文本输入框
docFromView.type["autoRow"].type["checkbox"]={
	read:function(obj){
		var autoCheckedLabel = "";
		obj.find("input:checkbox:checked").each(function(index,item){
			autoCheckedLabel+=$(item).attr("label")+",";
		});
		if(autoCheckedLabel.length>0){
			autoCheckedLabel = autoCheckedLabel.substring(0,autoCheckedLabel.length-1);
		}
		obj.parent().html(autoCheckedLabel);
	}
};

//动态添加行-文本输入框
docFromView.type["autoRow"].type["button"]={
	read:function(obj){
		obj.remove();
	}
};

//动态添加行-人员选择
docFromView.type["autoRow"].type["userSelect"]={
	read:function(obj){
		var itemId = obj.attr("itemId");
		var results = returnValue(itemId);
		var resultStr = "";
		if(results!=null){
			var users = results.split(",");
			for(var i=0;i<users.length;i++){
				resultStr += users[i].split(":")[1]+",";
			}
		}
		if(resultStr.length>0){
			resultStr = resultStr.substring(0,resultStr.length-1);
		}
		obj.html(resultStr);
	}
};

//动态添加行-div
docFromView.type["autoRow"].type["content"]={
	read:function(obj){
		var text = obj.html();
		text=text.replace(/\r\n/g,"<BR>");  
		text=text.replace(/\n/g,"<BR>");
		obj.parent().html(text);
	}
};
//删除意见域
$("textarea[id*=qianfasuggestTextareaContent]").remove();
$("section[id*=qianfasuggestTextareaContent] a").remove();
$("textarea[id*=zhubansuggestTextareaContent]").remove();
$("section[id*=zhubansuggestTextareaContent] a").remove();
$("textarea[id*=bangongshisuggestTextareaContent]").remove();
$("section[id*=bangongshisuggestTextareaContent] a").remove();
$("textarea[id*=lingdaosuggestTextareaContent]").remove();
$("section[id*=lingdaosuggestTextareaContent] a").remove();
$("textarea[id*=yuebansuggestTextareaContent]").remove();
$("section[id*=yuebansuggestTextareaContent] a").remove();
$("textarea[id*=nibansuggestTextareaContent]").remove();
$("section[id*=nibansuggestTextareaContent] a").remove();

$("textarea[id*=qianfasuggestTextareaContent]").remove();
$("div[id*=qianfasuggestTextareaContent] a").remove();
$("textarea[id*=zhubansuggestTextareaContent]").remove();
$("div[id*=zhubansuggestTextareaContent] a").remove();
$("textarea[id*=bangongshisuggestTextareaContent]").remove();
$("div[id*=bangongshisuggestTextareaContent] a").remove();
$("textarea[id*=lingdaosuggestTextareaContent]").remove();
$("div[id*=lingdaosuggestTextareaContent] a").remove();
$("textarea[id*=yuebansuggestTextareaContent]").remove();
$("div[id*=yuebansuggestTextareaContent] a").remove();
$("textarea[id*=nibansuggestTextareaContent]").remove();
$("div[id*=nibansuggestTextareaContent] a").remove();

//删除附件超链接
$("#filestyle-0").remove();
$("ul[id=attachList] a").attr("href","#");

