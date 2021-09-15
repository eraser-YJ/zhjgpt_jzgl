/***
 *  行政区域dialog
 */
var AdminArea = function() {
    var CLASS = AdminArea, thi$ = CLASS.prototype;
    if (CLASS.__defined__) {
        this._init.apply(this, arguments);
        return;
    }
    CLASS.__defined__ = true;

    /***
     * 打开dialgo事件
     */
    var _showAdminBtn = function(e) {
        var obj = e.data;
        obj.showAdminDialog(true);
    };

    /***
     * 展开或关闭dialog
     * @param {Object} flag： true为展开，false为关闭dialog
     */
    thi$.showAdminDialog = function(flag) {
        if (flag) {
            $("#adminRegion").show();

        } else {
            $("#adminRegion").hide();
        }

    };
    /***
     * 关闭dialog事件
     * @param {Object} evt
     */
    var closeBtnClick = function(evt) {
        $("#adminRegion").hide();
    };

    /***
     * dialog  确定按钮事件
     * @param {Object} evt
     */
    var okBtnClick = function(evt) {

        var obj = evt.data;
        var fullName="";
        var pac = "";
        $("#adminRegion").hide();
        
        if(obj.provName!=""){
        	fullName=obj.provName;
        	pac = obj.provPac;
        }
        if(obj.cityName!=""){
        	if(fullName!=obj.cityName){
        		fullName=fullName+obj.cityName;
        		pac = obj.cityPac;
        	}
        }
        if(obj.cntyName!=""){
        	fullName=fullName+obj.cntyName;
        	pac = obj.cntyPac;
        }
        if(fullName==""){
        	return false;
        }

        obj.father.father.mapAreaSearch("0",pac);
    };
    /***
     * dialog 选择事件
     * @param {Object} evt
     */
    var selectBtnClick = function(evt) {
        var id = this.id;
        var obj = evt.data;
        if (id == "provBtn") {
            obj.provName = "";
            obj.cityName = "";
            obj.cntyName = "";
            $("#blockBtn").show();
            $("#provBtn").hide();
            $("#cityBtn").hide();
            $("#cntyBtn").hide();

        } else if (id == "cityBtn") {
            obj.cityName = "";
            obj.cntyName = "";
            $("#blockBtn").show();
            $("#cityBtn").hide();
            $("#cntyBtn").hide();
        }
        obj.changeItems();
    };
    /***
     *清除地区选择事件
     */
    var clearItems = function() {
        $("#adminListBox li").remove();
    };
    /***
     * 地区名称选择事件
     * @param {Object} evt
     */
    var itemClick = function(evt) {
        var id = this.id;
        obj = evt.data;
        var pac = $(this).parent().data("pac");

        $(".regionList a").removeClass("selectedArea");
        $(this).addClass("selectedArea");

        if (obj.provName == "") {
            obj.provName = id;
            obj.provPac = pac;
            $("#provBtn").html(id).show();
            obj.changeItems();
        } else if (obj.cityName == "") {
            obj.cityName = id;
            obj.cityPac = pac;
            $("#cityBtn").html(id).show();
            obj.changeItems();
        } else {
            obj.cntyName = id;
            obj.cntyPac = pac;
            $("#cntyBtn").html(id).show();
            $("#blockBtn").hide();
        }
    };
    /***
     * 选择的地区名称改变
     */
    this.changeItems = function() {
        var list;
        if (this.provName == "") {
            list = this.data;
        } else if (this.cityName == "") {
            list = getAdminList(this.data, this.provName);
        } else {
            var arr = getAdminList(this.data, this.provName);
            list = getAdminList(arr, this.cityName);
        }
        this.addItems(list);
    };
    /***
     * 获取某地区包含的子地区
     * @param list {Array} 行政区域列表
     * @param id {String} 指定行政区域名称
     * @return {Array} 子地区列表
     */
    var getAdminList = function(list, id) {
        for (var i = 0; i < list.length; i++) {
            var item = list[i];
            if (item.name == id) {
                return item.children;
            }
        }
        return false;
    };
    /***
     * 增加地区按钮
     * @param array {Array} 行政区域列表
     */
    this.addItems = function(array) {
        clearItems();
        var box = $("#adminListBox");
        for (var i = 0; i < array.length; i++) {
            var name = array[i].name;
            var pac = array[i].pac;
            var str = '<li><a id="' + name + '" >' + name + '</a></li>';
            box.append($(str).data("pac",pac));
        }
        var fn = null;
        $("#adminListBox a").bind("click", this, itemClick);
    };

    thi$._init = function(def) {
        this.father = def.father;
        var data = this.data = window.adminDataStore;
        if (this.data == undefined || this.data.length < 1) {
            console.error("无行政区域数据");
        }

        this.state = 0;
        this.provName = "";
        this.cityName = "";
        this.cntyName = "";
        this.changeItems();

        $("#provBtn").bind("click", this, selectBtnClick);
        $("#cityBtn").bind("click", this, selectBtnClick);
        $(".adminRegion #closeBtn").click(closeBtnClick);
        $(".adminRegion #okBtn").bind("click", this, okBtnClick);

        $("#adminArea").bind("click", this, _showAdminBtn);
    };

    this._init.apply(this, arguments);
};

