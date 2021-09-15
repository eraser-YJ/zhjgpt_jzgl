//奕迅前端
if(!JCF){
    var JCF = {};
}
$.ajaxSetup({
    contentType: "application/x-www-form-urlencoded;charset=utf-8",
    complete: function(XMLHttpRequest, textStatus) {
        if (typeof(XMLHttpRequest.getResponseHeader) == "function") {
            var sessionstatus = XMLHttpRequest.getResponseHeader("sessionstatus");
            if (sessionstatus == "kickout") {
                window.location = getRootPath() + "/login?kickout=true";
            } else if (sessionstatus == "timeout") {
                window.location = getRootPath() + "/login?timeout=true";
            }
        }
    }
});
/**
 * 判断当前浏览器类型 主要判断ie
 * @example
 *  JCF.browser.ie === true  //IE浏览器
 *  JCF.browser.ie8 === true //IE8浏览器
 *  if(JCF.browser.ie && JCF.browser.version == 9) //IE9浏览器
 *  if(JCF.browser.ie && JCF.browser.version == 10) //IE10浏览器
 *  if(JCF.browser.ie && JCF.browser.version == 11) //IE11浏览器
 */
JCF.browser = function(){
    var agent = navigator.userAgent.toLowerCase(),
        browser = {
            ie		:  /(msie\s|trident.*rv:)([\w.]+)/.test(agent),
            webkit	: ( agent.indexOf( ' applewebkit/' ) > -1 )
        };
    var version = 0;
    // Internet Explorer 6.0+
    if ( browser.ie ){
        var v1 =  agent.match(/(?:msie\s([\w.]+))/);
        var v2 = agent.match(/(?:trident.*rv:([\w.]+))/);
        if(v1 && v2 && v1[1] && v2[1]){
            version = Math.max(v1[1]*1,v2[1]*1);
        }else if(v1 && v1[1]){
            version = v1[1]*1;
        }else if(v2 && v2[1]){
            version = v2[1]*1;
        }else{
            version = 0;
        }
        browser.ie8 = !!document.documentMode;
    }
    browser.version = version;
    return browser;
}();

JCF.histObj = null;         //初始化histtorys对象

JCF.version = '3.0';

JCF.$window = $(window);    //缓存window对象

JCF.newMailObject = null;  //存储写邮件的menu对象
JCF.saveMailObject= false; //保存写邮件的状态,跳出时是否需要弹出确认框

//获取某一个插件
JCF.getPlugins = function(name){

}

/**
 *  左菜单展开收起插件
 * @example
 *     html  --> <nav class="nav-primary" id="GLeftMenu">
 *     var sidemenu = new JCF.SideMenu('#JCLeftMenu');
 * @methods
 *   可以指定展开的列表, 传入应被点击的a标签 jQuery元素
 *   if($('#zhankai').length) sidemenu.goActivity($('#zhankai'));
 *
 *   关闭展开的类表
 *   sidemenu.hide();
 */

JCF.SideMenu = Clazz.extend({
    construct : function(element , active){
        this.active = active || 'active';
        this.$element = null;
        this.cacheData = null;

        this.search = null;
        this.dynmicId = null;
        this.$ele = element instanceof jQuery ? element : $(element);
        this._addEvent();
    },
    _addEvent : function(){
        var self = this;
        //点击菜单
        this.$ele.on('click' ,' a' , function(e){
            var $e = $(e.target);
            if(!$e.is('a')){
                $e = $e.closest('a');
            }
            self._activity($e , function(){
                self.$ele[0].style.cssText = ('zoom : 1.00'+Math.floor(Math.random()*100));
            });
            e.preventDefault();
        });
    },

    goActivity : function($e){
        var les = null;
        if($e instanceof jQuery){
            les = $e.parents('li');
        }else{
            this.setThisMeunId($e);
            les = $('#jcleftmenu_'+$e).parents('li');
        }
        for(var i = les.length -1; i >= 0; i--){
            var $a = $(les[i]).find('> a');
            if(!$a.hasClass(this.active)){
                this._activity($a);
            }
        }
    },
    hide : function(){
        if(this.$element.hasClass(this.active)){
            this.goActivity(this.$element);
        }
    },
    getPortalId : function(id){
        var menuData = this.getMenuData();
        var pId = '',pmId = '';
        for(var i = 0,len = menuData.length;i < len;i++){
            var menu = menuData[i];
            if(menu.id == id){
                pId = menu.parentId;
            }
        }
        if(pId){
            for(var i = 0,len = menuData.length;i < len;i++){
                var menu = menuData[i];
                if(menu.id == pId){
                    pmId = menu.id;
                }
            }
        }
        return pmId;
    },
    setData : function(data){
        if (data != "") {
            this.cacheData = this._parse(JSON.parse(data));

            this.search = new JCF.SearchMenu(this.cacheData);
        }
    },
    searchHide : function(){
        this.search.hide();
    },
    getMenuData : function(){
        return this.cacheData;
    },
    getCrumbs : function(id){
        var arys = this.getMenuCrumbs(id);
        var crustr = ['<div class="crumbs"><a href="#" onclick="JCFF.loadHome();">首页</a><i></i>'],
            len    = arys.length;
        for(var i = 0 ; i < len;i++){
            crustr.push('<span>'+ arys[i].name +'</span>');
            //crustr.push('<a href="#">'+ crumbs[i] +'</a>');
            if(i < len -1) crustr.push('<i></i>');
        }
        crustr.push('</div>');
        return crustr.join('')
    },
    getMenuCrumbs : function(id){
        var result = [],
            mData  = this.getMenuData(),
            len    = mData.length;

        function circulation(mid){
            var pId    = mid;
            for(var i = 0;i < len;i++){
                var item = mData[i];
                if(item.id == pId){
                    result.push(item);
                    if(item.parentId != '-1'){
                        circulation(item.parentId);
                    }
                    break;
                }
            }
        }
        circulation(id);
        return result.reverse()
    },
    _parse : function(datas){
        var reslut = [];
        function recur(data){
            for(var i = 0;i < data.length;i++){
                var obj = data[i];
                reslut.push(obj);
                if(obj.action && obj.action.indexOf('ic/mail/manageSend.action') >= 0){
                    JCF.newMailObject = obj;
                }
                if(obj.children){
                    recur(obj.children);
                }
            }
        }
        recur(datas);
        return reslut;
    },
    /**
     * 展开收起事件
     * @param $e
     * @returns {boolean}
     * @private
     */
    _activity : function($e ,callback){
        var self = this;
        var $p = $e.parent();
        var $active = $p.siblings('.'+this.active);
        var isOpen = $e.hasClass(this.active);
        //查看是否同级有展开的
        if($active.length){
            //获取已打开的同级列表,然后关闭
            $active.find('a,l').removeClass(this.active);
            $active.removeClass(this.active).find('ul:visible').slideUp(200);
        }
        if(isOpen){
            var opens = $e.next().find('.'+this.active);
            opens.each(function(index, item){
                var $t = $(item);
                $t.removeClass(self.active);
                if($t.next().is('ul')){
                    $t.next().slideUp(200);
                }
            });
        }
        //将当前的列表展开或收起
        $e.next()[isOpen ? 'slideUp' : 'slideDown'](200 , function(){
            JCF.leftScroll();
            if(callback) callback();
        });
        //置换当前展开收起状态
        this.$element = $e.toggleClass(this.active),$p.toggleClass(this.active);
        //阻止默认事件
        return false;
    },
    /**
     *
     * @param id
     */
    setThisMeunId : function(id){
        this.dynmicId = id;
    },
    /**
     *
     * @returns {null|*}
     */
    getThisMenuId : function(){
        return this.dynmicId;
    }
});
/**
 * 获取iframe的window对象
 * @returns {Window}
 */
JCF.getFrame = function(){
    var ifr = document.getElementById('mainFrame');
    if(ifr){
        return ifr.contentWindow;
    }
}

JCF.getSubsystemUrl = function(action){
    var reg = new RegExp("sys=([^&?]*)", "ig");
    var r = action.match(reg);
    if ("undefined" != typeof subsystemMap && r != null){
        var subsystemId = decodeURI(r[0].substr(4,r[0].length));
        for(var i=0; i<subsystemMap.length; i++){
            var subsystem = subsystemMap[i][subsystemId];
            if(subsystem){
                var menuUrl = subsystem + action;
                if(action.indexOf("?")==-1){
                    menuUrl += "?login-at=true";
                }else{
                    menuUrl += "&login-at=true";
                }
                return menuUrl;
            }
        }
    }
    return getRootPath() + action;
}
//跳转页面方法, 公共
JCF.LoadPage = function(menu , isEmpty){
    if(menu.url){
        function gotoPage(){
            JCF.setMailStatus(false);

            if(isEmpty) JCF.histObj.empty();
            JCF.histObj.setData(menu);
            if(menu.id){
                var menuId = (menu.id.indexOf('jcleftmenu') != -1) ? menu.id.split('jcleftmenu_')[1] : menuId;
                JCF.histObj.sidemenu.setThisMeunId(menuId);
            }
            var thref = mainFrame.location;
            thref.href = JCF.getSubsystemUrl(menu.url);
        }
        if(JCF.getMailStatus()){
            msgBox.confirm({
                content: '您正在写邮件，是否离开此页面?',
                success: function() {
                    var iframe = JCF.getFrame();
                    iframe && iframe.pageMethon();
                    gotoPage();
                },
                noback: function() {
                    gotoPage();
                },
                cancel: function() {
                    JCF.histObj.sidemenu.goActivity(JCF.histObj.sidemenu.getThisMenuId());
                },
                buttons: {
                    "离开并存草稿": "yes",
                    "取消": "no",
                    "离开": "cancel"
                }
            });
            return false;
        }
        gotoPage();
    }
}
//左菜单模拟滚动条
JCF.leftScroll = function(option){
    $('#slim-scroll').slimScroll(option);
}
//IE8圆角
JCF.pie = function(){
    if(JCF.browser.ie8) {
        if (window.PIE) {
            $('.rounded').each(function() {
                PIE.attach(this);
            });
        }
    }
}
/**
 * 获取写邮件的菜单对象
 * @returns {null|*}
 */
JCF.getMail = function(){
    return JCF.newMailObject;
}
JCF.getMailStatus = function(){
    return JCF.saveMailObject;
}
JCF.setMailStatus = function(status){
    JCF.saveMailObject = status;
}
/**
 *  记录浏览页面历史记录
 * @example
 *     html  --> <nav class="nav-primary" id="GLeftMenu">
 *     var historys = new JCF.Historys();
 * @methods
 *  historys.size();    获取当前存储多少个记录
 *  historys.setData({  添加记录
 *      url : 'aaa.action',
 *      name: '用户设置'
 *  });
 *  historys.getData()              获取记录 因为只能存储一个
 *  historys.empty()                清空记录
 */
JCF.Historys = Clazz.extend({
    construct : function() {
        this.records = [];
        this.topFive = [];
    },
    //跳转页面时候 调用此方法,记录点击的历史记录
    setData : function(obj){
        if(!obj.url) return false;
        this.records.push(obj);
    },
    setMenuData : function(data){
        this.menuData = data;
    },
    getData : function(){
        return this.records;
    },
    size : function(){
        return this.records.length;
    },
    //刷新页面
    reload : function(){
        window.location.reload();
    },
    //设置top5数据
    setTopFive : function(data){
        if(data){
            this.topFive = JSON.parse(data);
        }
    },
    pageUp : function(){
        this.records.pop();
        JCF.LoadPage(this.getLastData() , true);
    },
    //获取top5数据
    getTopFive : function(){
        return this.topFive;
    },
    //获取存储的最后一个url  用于goBack
    getLastData : function(){
        var res = this.records[this.size() - 1];
        return res;
    },
    //清空已存储点击的菜单
    empty : function(){
        this.records = [];
    }
});
/**
 * 搜索类
 */
JCF.SearchMenu = Clazz.extend({
    /*construct : function(datas){
        this.$ele    = $('#smartyInput');
        if(!this.$ele.length){
            throw new Error("搜索功框id 不等于smartyInput, 初始化失败");
        }

        this.$widget = $('#searchSmarty');
        this.$menu   = this.$widget.find('#menuContainer');
        this.$biss   = this.$widget.find('#bissContainer');
        this.placeholder = this.$ele.attr('placeholder');
        this.source  = [];
        this.status = false;                            //后台查询状态
        this.menuStatus = false;                        //前台查询状态

        this._addEvent();
        this._parseData(datas || []);
    },*/
    _addEvent : function(){
        var self = this;
        var valOld = '';
        //搜索面板显示
        self.$ele.focus(function(){
            self.$widget.show();
            self._title();
            self.$biss.html('');
        });
        //监听输入
        self.$ele.keyup(function(){
            var val = this.value;
            var searchstr = $.trim(val);

            if(searchstr.length > 1){
                valOld = searchstr;
                self._draw(searchstr);
            }else{
                self._title();
            }
        });
        //点击插叙到的结果后面板隐藏
        self.$widget.on('click' , ' a' , function(){
            setTimeout(function(){
                self.hide();
            } , 0);
        });
        //点击面板和搜索框意外隐藏
        $(document).on('mousedown', function(e){
            if ($(e.target).closest('#searchSmarty').length == 0 && e.target != self.$ele[0]) {
                self.hide();
            }
        });

        self._placeholder();
    },
    //解析全部菜单数据,便于检索
    _parseData : function(datas){
        var self = this;
        for (var i = 0; i < datas.length; i++) {
            var obj = datas[i];
            if(obj.action){
                self.source.push(obj);
            }
        }
    },
    //隐藏
    hide : function(flag){
        this.$widget.hide();
        this.$ele.val('');
        !flag && this.$ele.blur();
    },
    //搜索方法
    _search : function(value){
        var self = this,
            len = self.source.length,
            result = [];

        for(var i = 0;i < len;i++){
            var obj = self.source[i];
            if(obj.name.indexOf(value) >= 0){
                result.push(obj);
            }
        }
        return result;
    },
    _isEmptyObject : function(obj) {
        for (var key in obj) {
            return false;
        }
        return true;
    },
    //向后台请求查询搜索字的全文内容
    _getData : function(key){

        var self = this;

        if(self.status) {
            return false;
        }

        //if(!self.menuStatus){
        //    self._loading();
        //}
        //self.status = true;
        //引入goa相关业务后，需要检索功能，打开以下代码
        /*$.ajax({
            type : "GET",
            url : getRootPath()+'/oa/quickSearch/manage.action?content='+encodeURI(key) + '&v='+(+new Date()),
            dataType : "json",
            success : function(data) {
                self.status = false;
                if(self._isEmptyObject(data)){
                    self._fail('biss');
                    if(!self.menuStatus){
                        self._fail('menu');
                    }
                    return false;
                }
                if(!self.menuStatus){
                    self._fail('empty');
                }
                var result = [];
                //收件箱信息
                if(data.reciveMailUrl){
                    result.push(self._getBusinessTemplate(0 ,data.reciveMailUrl, data.reciveMailTitle));
                }
                //发件箱信息
                if(data.sendMailUrl){
                    result.push(self._getBusinessTemplate(1 ,data.sendMailUrl, data.sendMailTitle));
                }
                //收文
                if(data.reciveDocUrl){
                    result.push(self._getBusinessTemplate(2 ,data.reciveDocUrl, data.reciveDocTitle));
                    //reciveDocState：收文详细页状态（只有当仅查到一条收文数据时才会有reciveDocState）
                }
                //发文
                if(data.sendDocUrl){
                    result.push(self._getBusinessTemplate(3 ,data.sendDocUrl, data.sendDocTitle));
                    //sendDocState：发文详细页状态（只有当仅查到一条发文数据时才会有sendDocState）
                }
                self.$biss.html(result.join(''));
            }
        });*/
    },

    _loading : function(){
        this.$menu.html('正在搜索...');
    },
    //查询失败
    _fail : function(type){
        if(type == 'empty'){
            this.$menu.empty();
        }
        if(type == 'menu'){
            this.$menu.html('未查询到结果!');
        }
        if(type == 'biss'){
            this.$biss.empty();
        }
    },
    _title : function(){
        this.$biss.html('');
        this.$menu.html('请输入大于1个检索文字!');
    },
    _placeholder : function(){
        var self = this;
        //模拟水印
        if(JCF.browser.ie && JCF.browser.version < 10){
            self.$ele.focus(function(e){
                if(this.value == self.placeholder){
                    this.value = '';
                    self.$ele.removeClass('placeholder');
                }
            });

            self.$ele.blur(function(e){
                if(this.value == ''){
                    this.value = self.placeholder;
                    self.$ele.addClass('placeholder');
                }
                self.hide(true);
            });
            self.$ele.val(self.placeholder);
        }
    },
    _draw : function(value){
        var data = this._search(value);
        if(data.length){
            this.menuStatus = true;
            this.$menu.html(this._getMenuTemplate(data)).off().on('click' , ' a' ,function(e){
                var $this = $(e.target);

                if(!$this.is('a')){
                    $this = $this.parent();
                }

                JCF.LoadPage({
                    id : $this.attr('ids'),
                    name : $this.attr('name'),
                    url  : $this.attr('action')
                });
            });
        }else{
            this.menuStatus = false;
            this._fail('menu');
        }
        this._getData(value);
    },
    //菜单模版
    _getMenuTemplate : function(datas){
        var len = datas.length,
            template = [],
            i = 0;
        template.push('<h2>常用功能</h2><ul class="ul-icon clearfix">');

        for(;i<len;i++){
            var obj = datas[i];
            template.push('<li class="icon'+((i%8)+1)+'"> <a href="#" action="'+obj.action+'" ids="jcleftmenu_'+obj.id+'" name="'+obj.name+'" title="'+obj.name+'"> ' +
                    '<i class="fa fa-personage"></i><span>'+(obj.name.length > 4 ? obj.name.substring(0,4):obj.name )+'</span> </a> </li>');
        }
        template.push('</ul>');

        return template.join('');
    },
    //全文模版
    _getBusinessTemplate : function(type ,url , title){
        var tempType = ['收件箱','已发送','收文','发文'],template = [];
        template.push('<h2>'+tempType[type]+'</h2><ul class="relevant-mail">');
        template.push('<li><a href="javascript:;" onclick="JCF.LoadPage({url : \''+url+'\'});">'+title+'</a></li>');
        //template.push(' <li><a href="#"><span>办公</span>室通知，办公桌面要保持干净</a></li>');
        template.push('</ul>');
        return template.join('');
    }
});
/**
 * 个人信息
 */
JCF.UserInfo = Clazz.extend({
    construct : function(config){
        this.$contariner = $('#user-avatar');
        this.$panel = $('#h-nav-list');
        this.$btn = this.$panel.find('ul a,ul button');
        this.status = false;
        this._default(config);
        this._addEvent();
    },
    _default : function(opt){
        var self = this;
        for(var key in opt){
            if(opt.hasOwnProperty(key)){
                self.$btn.filter(function(){
                    var type = $(this).attr('types');
                    if(type == key && this.name == opt[key]){
                        $(this).addClass('active');
                    }
                });
            }
        }
    },
    _addEvent : function(){
        var self = this;
        self.$contariner.hover(function(){
            self.panelShow();
        } , function(){
            self.panelHide();
        });
        self.$btn.on('click' ,function(e){
            if(!self.status){
                self.btnChange($(this).is('a') ? 'a' : 'button' ,this);
            }
            e.preventDefault();
        });
    },
    //用户头像个人设置显示
    panelShow : function(){
        this.$panel.stop(true,true).show();
    },
    panelHide : function(){
        this.$panel.stop(true,true).hide();
    },
    btnChange : function(selector ,ele){
        var $ele = $(ele),type = $ele.attr('types'),value = $ele.attr('name');
        $ele.closest('div').find(selector).removeClass('active');
        $ele.addClass('active');
        this._handle(type ,value);
    },
    _handle : function(type,value){
        if(type === 'voice'){
            this._setVoice(value == 'on');
            return false;
        }
        this._execute(type , value);
    },
    _setVoice : function(flag){
        $.cookie('voiceSwitch', ''+flag);
    },
    _execute : function(type , value){
        var self = this;
        if(self.status){
            return false;
        }
        self.status = true;
        $.ajax({
            url : getRootPath()+'/sys/user/skin.action?'+type+'='+value+'&time='+(+new Date().getTime()) ,
            type : 'GET',
            success : function(data) {
                self.status = false;
                if(data == 'true'){
                    JCF.histObj.reload();
                }
            }
        });
    }
});

//退出登录
function exit() {
    msgBox.confirm({
        content: "是否退出吗？",
        success: function() {
            //promptOnUnLoad();
            $.ajax({
                type : "POST",
                url : getRootPath()+'/logoutlog?time='+(+new Date().getTime()),
                data : null,
                dataType : "json",
                success : function(data) {
                    location.href = getRootPath() + "/logout";
                },error:function(){
                    location.href = getRootPath() + "/logout";
                }
            });
        }
    });
}

function getOnlineCount(){
    $.ajax({
        type : "GET",
        url : getRootPath()+'/system/getOnlineUserCount.action?time='+(+new Date().getTime()),
        data : null,
        dataType : "json",
        success : function(data) {
            if(data){
                $("#lineCount").html(data);
            }
        }
    });
}

$(document).ready(function(){
//判断是否为IE,隐藏全屏按钮
    if($("html").hasClass('ie11')||$("html").hasClass('ie')){
        $('.full-f11').hide();
    }

    //全屏函数
    function launchFullscreen(element) {
        if(element.requestFullscreen) {
            element.requestFullscreen();
        } else if(element.mozRequestFullScreen) {
            element.mozRequestFullScreen();
        } else if(element.webkitRequestFullscreen) {
            element.webkitRequestFullscreen();
        } else if(element.msRequestFullscreen) {
            element.msRequestFullscreen();
        }
    }
//退出全屏函数
    function exitFullscreen() {
        if(document.exitFullscreen) {
            document.exitFullscreen();
        } else if(document.mozCancelFullScreen) {
            document.mozCancelFullScreen();
        } else if(document.webkitExitFullscreen) {
            document.webkitExitFullscreen();
        }
    }

    //点击展开全屏事件
    $(document).on('click','.full-f11', function () {
        launchFullscreen(document.documentElement);
        $(this).addClass('active');
        $(this).removeClass('full-f11');
    });
//点击退出全屏事件
    $(document).on('click', '.navbar-fixed-top>.active', function () {
        exitFullscreen();
        $(this).removeClass('active');
        $(this).addClass('full-f11')
    });

    //$(document).on('keyup',function(e){
    //    if(e.which==27) {
    //        $('.navbar-fixed-top>.active').addClass('full-f11').removeClass('active');
    //    }
    //});
});