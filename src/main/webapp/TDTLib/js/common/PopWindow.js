/**
 *  该类 用于创建通用的弹出信息框
 *  def：{
 *     info： {String} 信息内容
 *     btnName： {String} 按钮1名称 (默认为“确定”)
 *     btnClick:{function} 按钮1的事件 (默认为关闭窗口)
 *     hasCancelBtn: {boolean} 是否有取消按钮 (默认为没有该按钮)
 * }
 * 
 */
var PopWindow = function(def) {
    var CLASS = PopWindow, thi$ = CLASS.prototype;
    if (CLASS.__defined__) {
        this._init.apply(this, arguments);
        return;
    }
    CLASS.__defined__ = true;
    
    /**
     *  关闭弹出信息框 
     */
    thi$.closeWin = function(){
        $("#popInfoWin").remove();
    };
    
    var _closeBtnClick = function(e){
        var $this = e.data;
        $this.closeWin();
    };
    
    var _okBtnClick = function(e){
        var $this = e.data;       
        if($this.btnClick){
            $this.btnClick.call(this);
        }
        $this.closeWin();
    };
    
    /**
     *   弹出信息
     */        
    var _popWin = function(def){
        var info = def.info?def.info:"页面出错";
        var btnName = def.btnName?def.btnName:"确定";
        
        var str = '<div class="popInfoWin" id="popInfoWin" style="position:fixed;"><div class="header">';
            str += '<div class="closeBtn" id="popWinCloseBtn"></div></div>';
            str += '<div class="info">'+info+'</div><div class="btnBox" id="popWinBtnBox">';
            str += '<div class="btn left" id="popWinBtn">'+ btnName+'</div>';
            if(def.hasCancelBtn == true || def.hasCancelBtn == "true"){
                str += '<div class="btn right" id="popWincancelBtn">取消</div>';
            }
            str += '</div>';
        //判断页面是否已经存在一个提示框
        if( $('#popInfoWin').length > 0 ) {
        	$("#popInfoWin").remove();
        }
        
        $("body").append($(str));
        
        var position = _getPosition();
        var win = $("#popInfoWin");
        win.css("top",position.top+"px");
        win.css("left",position.left+"px");
        win.css('z-index','999999999999');
        $("#popWinCloseBtn").bind("click",this,_closeBtnClick);
        $("#popWincancelBtn").bind("click",this,_closeBtnClick);
        
        $("#popWinBtn").bind("click",this,_okBtnClick);
        
        if(def.hasCancelBtn == true || def.hasCancelBtn == "true"){
            $("#popWinBtnBox").css("width","150px");
        }else{
            $("#popWinBtnBox").css("width","70px");
        }

    };
    
    /**
     *  计算位置，居中 
     */
    var _getPosition = function(){
        var top = $(window).height()/2 -105/2;
        var left =$(window).width()/2 - 257/2;
        if(location.href.indexOf("mapDataAction") != -1){
        	left = 300;
        }
        return {top : top,left: left};
    };
       
    thi$._init = function(def) {
        _popWin.call(this,def);
        if(def.btnClick){
            this.btnClick = def.btnClick;
        }
    };

    this._init.apply(this, arguments);
};

