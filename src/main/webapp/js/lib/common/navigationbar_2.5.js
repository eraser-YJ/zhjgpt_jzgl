//奕迅前端
var JCF = {};
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

JCF.version = '2.5';

JCF.$window = $(window);    //缓存window对象

JCF.newMailObject = null;

JCF.saveMailObject= false;
/**
 * 一级导航添加背景色
 * @param id  导航id
 */
JCF.navigation = function(id){
    var $nav = $('#navigation-header');
    var $childs = $nav.find(' li');

    $childs.removeClass('active');

    var $this = $('#header_'+id);
    $this.addClass('active');
    var other = $this.closest('#topnav-other_modules');
    if(other.length) other.addClass('active');
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

        this.level = [];
        this.dynmicId = null;
        this.headMenu = null;
        this.rootMenu = null;
        this.$ele = element instanceof jQuery ? element : $(element);
        this._addEvent();
    },
    _addEvent : function(){
        var self = this,
            dropdown = $('#dropdownMenu');
        //点击菜单
        this.$ele.on('click' ,' a' , function(e){
            var $e = $(e.target);
            if(!$e.is('a')){
                $e = $e.closest('a');
            }
            self._activity($e);
            if(dropdown.hasClass('open')) dropdown.removeClass('open');
            e.preventDefault();
        });
        //点击连接,跳转页面
        $(document).on('click' , ' [data-href="menu"]' , function(e){
            var $this = $(this);

            if(!$this.is('a')){
                $this = $this.parent();
            }

            JCF.LoadPage({
                id : $this.attr('id'),
                name : $this.attr('name'),
                url  : $this.attr('action')
            } , true);
            e && e.preventDefault();
            return false;
        });

    },
    goActivity : function(menuId){
        if(menuId){
            this.level = [];
            this.setThisMeunId(menuId);
            var rootMenu = this.getByRoot(menuId);
            this.headMenu = this.level.pop();
            this.getTemplateLeft1(rootMenu , this.level);
            JCF.leftScroll();
        }
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
        return crustr.join('');
    },
    drawLeft : function(id){
        var obj = null;
        for(var i = 0;i < this.cacheData.length;i++){
            var menu = this.cacheData[i];
            if(menu.id == id){
                obj = menu; break;
            }
        }
        if(obj) {
            this.rootMenu = obj;
            this.getTemplateLeft1(obj);
            JCF.leftScroll();
        }
    },
    hide : function(){
        if(this.$element.hasClass(this.active)){
            this.goActivity(this.$element);
        }
    },
    getEventDom : function(menu){
        return menu.children ? '' :'data-href="menu"';
    },
    checked : function(les ,id){
        var rs = '';
        if(les){
            for(var i = 0;i < les.length;i++){
                var le = les[i];
                if(le.id == id) rs = 'class="active"';
            }
        }
        return rs;
    },
    getTemplateLeft1 : function(menus ,levels){
        if(this.headMenu) JCF.navigation(this.headMenu.id);
        if(menus && menus.children){
            var result = [];
            for(var i = 0;i < menus.children.length;i++){
                var menu = menus.children[i];
                var ac  = this.checked(levels , menu.id);
                result.push('<li '+ac+'><a href="javascript:void(0)" '+ac+' name="'+menu.name+'" id="jcleftmenu_'+menu.id+'" action="'+menu.action+'" '+this.getEventDom(menu)+'>');
                result.push('<i class="'+menu.icon+'"><b class="b-bg-'+(i%8)+'"></b></i>');
                if(menu.children){
                    result.push('<span class="pull-right"><i class="fa fa-angle-down text"></i><i class="fa fa-angle-up text-active"></i></span>');
                }
                result.push('<span>'+menu.name+'</span>');
                result.push('</a>');
                if(menu.children){
                    result.push('<ul class="nav lt">');
                    result.push(this.getTemplateLeft2(menu.children , levels));
                    result.push('</ul>');
                }
                result.push('</li>');
            }
            this.$ele.html(result.join(''));
        }
    },
    getTemplateLeft2 : function(menus , levels){
        var ary = [];
        //菜单第二级
        for(var i = 0;i < menus.length;i++){
            var obj = menus[i];
            var ay = this.checked(levels , obj.id);
            var aStyle = "";
            if (ay == 'class="active"') {
                aStyle = 'style="color: #1572e8 !important"';
            }
            ary.push('<li '+ay+'><a href="javascript:void(0)" '+aStyle+' name="'+obj.name+'" id="jcleftmenu_'+obj.id+'" action="'+obj.action+'" '+this.getEventDom(obj)+'>');
            if(obj.children){
                ary.push('<i class="fa fa-angle-down text" ' + aStyle + '></i><i class="fa fa-angle-up text-active"></i>');
            }else{
                ary.push('<i class="fa fa-angle-right" ' + aStyle + '></i>');
            }
            ary.push('<span>'+obj.name+'</span>');
            ary.push('</a>');
            if(obj.children){
                ary.push('<ul class="nav lt">');
                ary.push(this.getTemplateLeft3(obj.children , levels));
                ary.push('</ul>');
            }
            ary.push('</li>');
        }
        return ary.join('');
    },
    getTemplateLeft3 : function(menus , levels){
        var ary = [];
        //左菜单第三级
        for(var i = 0;i < menus.length;i++){
            var obj = menus[i];
            var ax = this.checked(levels , obj.id);
            var aStyle = "";
            if (ax == 'class="active"') {
                aStyle = 'style="color: #1572e8 !important"';
            }
            ary.push('<li '+ax+'><a href="javascript:void(0)" '+aStyle+' name="'+obj.name+'" id="jcleftmenu_'+obj.id+'" action="'+obj.action+'" '+this.getEventDom(obj)+'>');
            ary.push('<i class="fa fa-angle-right"></i>');
            ary.push('<span>'+obj.name+'</span></a>');
            ary.push('</li>');
        }
        return ary.join('');
    },
    setData : function(data , callback){
        this.cacheData = this._parse(JSON.parse(data));
        if(typeof callback === 'function'){
            callback.call(this , this.cacheData);
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
    getRootMenu : function(menuId){
        var self = this,isMenu = null;
        for(var i = 0,len = self.cacheData.length;i< len;i++){
            var menu = self.cacheData[i];
            if(menuId == menu.id){
                self.level.push(menu);
                self.oldRootMenu = menu;
                if(menu.parentId == '-1'){
                    self.rootMenu = menu;
                }else{
                    self.getRootMenu(menu.parentId);
                }
                break;
            }
        }
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
    getByRoot : function(menuId){
        this.oldRootMenu = null;
        this.getRootMenu(menuId);
        return this.rootMenu;
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
    getMenuData : function(){
        return this.cacheData;
    },
    _activity : function($e){
        var $p = $e.parent();
        var $active = $p.siblings('.'+this.active);
        //查看是否同级有展开的
        if($active.length){
            //获取已打开的同级列表,然后关闭
            $active.find('> a').removeClass(this.active);
            $active.removeClass(this.active).find('> ul:visible').slideUp(200);
        }
        //将当前的列表展开或收起
        $e.next()[$e.hasClass(this.active) ? 'slideUp' : 'slideDown'](200 , function(){
            JCF.leftScroll();
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

JCF.getSubsystemUrl = function(action){
    var reg = new RegExp("sys=([^&?]*)", "ig");
    var r = action.match(reg);
    var subsystemId = getRootPath();
    if ("undefined" != typeof subsystemMap && r != null){
        subsystemId = decodeURI(r[0].substr(4,r[0].length));
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
        subsystemId = '/' + subsystemId;
        if (subsystemId == '/center' || subsystemId == '/') {
            subsystemId = getRootPath();
        }
    }
    if (action.indexOf('http') > -1) {
        return action;
    }
    if (subsystemId.indexOf('/form') > -1) {
        window.open(subsystemId + action);
        return "";
    }else {
        if (subsystemId.indexOf('/scs') > -1) {
            subsystemId = getRootPath();
        }
        return subsystemId + action;
    }
}
//跳转页面方法, 公共
JCF.LoadPage = function(menu , isEmpty){
    if(menu.url){
        function gotoPage(){
            var url = JCF.getSubsystemUrl(menu.url);
            if (url == ""){
                return;
            }
            JCF.setMailStatus(false);

            if(isEmpty) JCF.histObj.empty();
            JCF.histObj.setData(menu);
            if(menu.id){
                var menuId = (menu.id.indexOf('jcleftmenu') != -1) ? menu.id.split('jcleftmenu_')[1] : menuId;
                JCF.histObj.sidemenu.setThisMeunId(menuId);
            }
            mainFrame.location = url;
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
    //跳转页面时候 调用此方法,记录点击的菜单数据
    setData : function(obj){
        if(!obj.url) return false;
        this.records.push(obj);
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
        this.topFive = JSON.parse(data);
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
    pageUp : function(){
        this.records.pop();
        JCF.LoadPage(this.getLastData() , true);
    },
    //清空已存储点击的菜单
    empty : function(){
        this.records = [];
    }
});
//判断当前左菜单是否隐藏
var isConcealMenu = false;

//换肤下拉菜单点击事件
function customDropDown(ele) {
    this.dropdown = ele;
    this.options = this.dropdown.find("ul.dropdown-menu > li");
    this.val = '';
    this.initEvents();
}

customDropDown.prototype = {
    initEvents: function() {
        var obj = this;
        //点击下拉列表的选项
        obj.options.on("click", function() {
            var opt = $(this).attr("name");
            skin(opt);
        });
    }
};
//退出登录
function exit() {
    msgBox.confirm({
        content: "是否退出吗？",
        success: function() {
            // promptOnUnLoad();
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
//换肤
var subState = false;
function skin(theme){
    if(subState)return;
    subState = true;
    jQuery.ajax({
        url : getRootPath()+'/sys/user/skin.action?theme='+theme+'&time='+(+new Date().getTime()) ,
        type : 'GET',
        success : function(data) {
            subState = false;
            if(data == "true"){
                JCF.histObj.reload();
            }
        },
        error : function() {
            subState = false;
        }
    });
}