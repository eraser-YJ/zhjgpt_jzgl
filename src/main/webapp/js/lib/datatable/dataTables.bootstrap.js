/* Set the defaults for DataTables initialisation */
$.extend( true, $.fn.dataTable.defaults, {
	//"sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>",
	"sPaginationType": "bootstrap",
	"bLengthChange": false,//改变显示条数
	"bFilter": false,//内置搜索
	"bAutoWidth": false,//字段调整列宽
	"bDeferRender": true,
	"bInfo": false,//是否显示页脚信息
	"bServerSide": true,//指定从服务器端获取数据   
	"sDefaultContent" : "", //此列默认值为""，以防数据中没有此值，DataTables加载数据的时候报错 
	//"sPaginationType": "full_numbers",
	"oLanguage": {
	    "sProcessing":   "正在加载中...",
	    "sLengthMenu":   "_MENU_ 记录/页",
	    "sZeroRecords":  "没有匹配的记录",
	    "sEmptyTable":   "没有匹配的记录",
	    "sInfo":         "显示第 _START_ 至 _END_ 项记录，共 _TOTAL_ 项",
	    "sInfoEmpty":    "显示第 0 至 0 项记录，共 0 项",
	    "sInfoFiltered": "(由 _MAX_ 项记录过滤)",
	    "sInfoPostFix":  "",
	    "sSearch":       "过滤:",
	    "sUrl":          "",
	    "oPaginate": {
	        "sFirst":    "首页",
	        "sPrevious": "上页",
	        "sNext":     "下页",
	        "sLast":     "末页"
	    }
	}
} );


/* Default class modification */
$.extend( $.fn.dataTableExt.oStdClasses, {
	"sWrapper": "dataTables_wrapper "
} );

//获取页数
$.fn.dataTableExt.oApi.fnPag = function ( oSettings)
{
	return oSettings._iDisplayLength === -1 ? 0 : Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength );
};

//分页查询回调方法
oTableRetrieveData = function ( sSource, aoData, fnCallback ) {
    $.ajax({
		type : "GET",
		url : sSource,
		data : aoData,
		dataType : "json",
		success : function(resp) {
			fnCallback(resp);
			//重新绑定编辑和删除提交框
			$("i[data-toggle='tooltip']").tooltip();
			ie8TableStyle();
		}
	});
};

//隐藏列
setColumnVis = function ( table, index )
{
	var state = true;
	if(table){
		var length = table.fnSettings()._iDisplayLength;
		var totalCount = table.fnSettings()._iRecordsTotal;
		if(totalCount==0)return;
		if(totalCount < length){
			length = totalCount;
		}
		for(var i=0;i<length;i++){
			if(table.fnGetNodes(i)){
				if($.trim(table.fnGetData(i,index)) != ""){
					state = false;
				}
			}
		}
		table.fnSetColumnVis( index, !state);
	}
	
};

//改变页数
pageChange = function(table){
	if(table){
		var page = $.cookie("page");
		table.fnPageChange(page);
	}
};

getTableParameters = function (table, aoData) {
	//排序条件 iSortCol_0排序下标	 sSortDir_0排序方式	 mDataProp_n排序字段
	 var sortField = null, index = aoData.getValue("iSortCol_0");
	 if(index >= 0){
		 $.each(aoData, function(i, o) {
			if(o.name == "mDataProp_" + index){
				sortField = o.value;
			}
		 });
	 }
	 aoData.push({ "name": "page", "value": table ? table.fnPag() : 0 });
	 if(sortField != null && sortField != ""){
		 aoData.push({ "name": "orderBy", "value": sortField+" "+aoData.getValue("sSortDir_0")});
	 }
};

/* API method to get paging information */
$.fn.dataTableExt.oApi.fnPagingInfo = function ( oSettings )
{
	return {
		"iStart":         oSettings._iDisplayStart,
		"iEnd":           oSettings.fnDisplayEnd(),
		"iLength":        oSettings._iDisplayLength,
		"iTotal":         oSettings.fnRecordsTotal(),
		"iFilteredTotal": oSettings.fnRecordsDisplay(),
		"iPage":          oSettings._iDisplayLength === -1 ?
			0 : Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength ),
		"iTotalPages":    oSettings._iDisplayLength === -1 ?
			0 : Math.ceil( oSettings.fnRecordsDisplay() / oSettings._iDisplayLength )
	};
};


/* Bootstrap style pagination control */
$.extend( $.fn.dataTableExt.oPagination, {
	"bootstrap": {
		"fnInit": function( oSettings, nPaging, fnDraw ) {
			var oLang = oSettings.oLanguage.oPaginate;
			var fnClickHandler = function ( e ) {
				e.preventDefault();
				if ( oSettings.oApi._fnPageChange(oSettings, e.data.action) ) {
					fnDraw( oSettings );
				}
			};
 
			$(nPaging).addClass('pagination').append(
					'<ul>' +
					'<li class="allpage"><span>共 <i></i> 页 <i></i> 条记录 </span></li>' +
				    '<li class="first disabled"><a href="#">' + oLang.sFirst + '</a></li>' +
				    '<li class="prev  disabled"><a href="#">' + oLang.sPrevious + '</a></li>' +
				    '<li class="next  disabled"><a href="#">' + oLang.sNext + '</a></li>' +
				    '<li class="last  disabled"><a href="#">' + oLang.sLast + '</a></li>' +
				    '</ul>'
			);
			var els = $('a', nPaging);
            $(els[0]).bind('click.DT', { action: "first" }, fnClickHandler);
            $(els[1]).bind('click.DT', { action: "previous" }, fnClickHandler);
            $(els[2]).bind('click.DT', { action: "next" }, fnClickHandler);
            $(els[3]).bind('click.DT', { action: "last" }, fnClickHandler);
		},
 
		"fnUpdate": function ( oSettings, fnDraw ) {
			var totalPage = oSettings._iDisplayLength === -1 ?
					0 : Math.ceil( oSettings.fnRecordsDisplay() / oSettings._iDisplayLength );
			
			var iListLength = 5;
			var oPaging = oSettings.oInstance.fnPagingInfo();
			var an = oSettings.aanFeatures.p;
			var i, ien, j, sClass, iStart, iEnd, iHalf=Math.floor(iListLength/2);
 
			if ( oPaging.iTotalPages < iListLength) {
				iStart = 1;
				iEnd = oPaging.iTotalPages;
			}
			else if ( oPaging.iPage <= iHalf ) {
				iStart = 1;
				iEnd = iListLength;
			} else if ( oPaging.iPage >= (oPaging.iTotalPages-iHalf) ) {
				iStart = oPaging.iTotalPages - iListLength + 1;
				iEnd = oPaging.iTotalPages;
			} else {
				iStart = oPaging.iPage - iHalf + 1;
				iEnd = iStart + iListLength - 1;
			}
 
			for (i = 0, iLen = an.length ; i < iLen ; i++) {
                // Remove the middle elements
                $('li:gt(2)', an[i]).filter(':not(.next,.last,.allpage)').remove();
				$('i:first', an[i]).html(totalPage);
				$('i:last', an[i]).html(oSettings._iRecordsTotal);

             // Add the new list items and their event handlers
				for ( j=iStart ; j<=iEnd ; j++ ) {
					sClass = (j==oPaging.iPage+1) ? 'class="active"' : '';
					$('<li '+sClass+'><a href="#">'+j+'</a></li>')
						.insertBefore($('.next,.last', an[i])[0])
						.bind('click', function (e) {
							e.preventDefault();
							oSettings._iDisplayStart = (parseInt($('a', this).text(),10)-1) * oPaging.iLength;
							fnDraw( oSettings );
						} );
				}
				// Add / remove disabled classes from the static elements
				if ( oPaging.iPage === 0 ) {
					$('li:first', an[i]).next().addClass('disabled');
				} else {
					$('li:first', an[i]).next().removeClass('disabled');
				}
 
				if ( oPaging.iPage === oPaging.iTotalPages-1 || oPaging.iTotalPages === 0 ) {
					$('li:last', an[i]).addClass('disabled');
				} else {
					$('li:last', an[i]).removeClass('disabled');
				}
				
	       if (oPaging.iPage === 0) {
                    $('.first,.prev', an[i]).addClass('disabled');
                } else {
                    $('.first,.prev', an[i]).removeClass('disabled');
                }
 
                if (oPaging.iPage === oPaging.iTotalPages - 1 || oPaging.iTotalPages === 0) {
                    $('.next,.last', an[i]).addClass('disabled');
                } else {
                    $('.next,.last', an[i]).removeClass('disabled');
                }
			}
		}
	}
} );


/*
 * TableTools Bootstrap compatibility
 * Required TableTools 2.1+
 */
if ( $.fn.DataTable.TableTools ) {
	// Set the classes that TableTools uses to something suitable for Bootstrap
	$.extend( true, $.fn.DataTable.TableTools.classes, {
		"container": "DTTT btn-group",
		"buttons": {
			"normal": "btn",
			"disabled": "disabled"
		},
		"collection": {
			"container": "DTTT_dropdown dropdown-menu",
			"buttons": {
				"normal": "",
				"disabled": "disabled"
			}
		},
		"print": {
			"info": "DTTT_print_info modal"
		},
		"select": {
			"row": "active"
		}
	} );

	// Have the collection use a bootstrap compatible dropdown
	$.extend( true, $.fn.DataTable.TableTools.DEFAULTS.oTags, {
		"collection": {
			"container": "ul",
			"button": "li",
			"liner": "a"
		}
	} );
}
