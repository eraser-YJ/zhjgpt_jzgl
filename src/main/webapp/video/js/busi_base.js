//声明公用变量
var hkFun = {};
hkFun.initCount = 0;
hkFun.pubKey = '';
hkFun.oWebControl = '';
hkFun.disHeigth = 800;
hkFun.disWidth = 1000;
hkFun.pluginContainerId = 'playWnd';
hkFun.calculateDisScope = function () {
    var tiWidth = $(window).width();
    var tiHeight = $(window).height();
    hkFun.disWidth = tiWidth - 300;
    hkFun.disHeigth = tiHeight - 30;
};
// 创建WebControl实例与启动插件
hkFun.initPlugin = function (inPageMode) {
    //显示区域设定
    hkFun.calculateDisScope();
    hkFun.oWebControl = new WebControl({
        szPluginContainer: hkFun.pluginContainerId,                       //指定容器id
        iServicePortStart: 15900,                           //指定起止端口号，建议使用该值
        iServicePortEnd: 15909,
        cbConnectSuccess: function () {
            // setCallbacks();
            //实例创建成功后需要启动服务
            hkFun.oWebControl.JS_StartService("window", {
                dllPath: "./VideoPluginConnect.dll"
            }).then(function () {
                //JS_CreateWnd创建视频播放窗口，宽高可设定
                hkFun.oWebControl.JS_CreateWnd(hkFun.pluginContainerId, hkFun.disWidth, hkFun.disHeigth).then(function () {
                    //console.log("JS_CreateWnd success");
                    //创建播放实例成功后初始化
                    hkFun.init(inPageMode);
                });
            }, function () {

            });
        },
        cbConnectError: function () {
            //console.log("cbConnectError");
            hkFun.oWebControl = null;
            $("#" + hkFun.pluginContainerId).html("插件未启动，正在尝试启动，请稍候...");
            WebControl.JS_WakeUp("VideoWebPlugin://");        //程序未启动时执行error函数，采用wakeup来启动程序
            initCount++;
            if (initCount < 3) {
                setTimeout(function () {
                    hkFun.initPlugin();
                }, 3000)
            } else {
                $("#" + hkFun.pluginContainerId).html("插件启动失败，请检查插件是否安装！");
            }
        },
        cbConnectClose: function () {
            //console.log("cbConnectClose");
            hkFun.oWebControl = null;
        }
    });

}
//初始化
hkFun.init = function (inPageMode) {
    hkFun.getPubKey(function () {
        ////////////////////////////////// 请自行修改以下变量值	////////////////////////////////////
        var appkey = videoConfig.appKey;                         //综合安防管理平台提供的appkey，必填
        var secret = hkFun.setEncrypt(videoConfig.secret);       //综合安防管理平台提供的secret，必填
        var ip = videoConfig.ip;                                 //综合安防管理平台IP地址，必填
        var playMode = inPageMode;                               //初始播放模式：0-预览，1-回放
        var port = 443;                                          //综合安防管理平台端口，若启用HTTPS协议，默认443
        var snapDir = "D:\\SnapDir";                             //抓图存储路径
        var videoDir = "D:\\VideoDir";                           //紧急录像或录像剪辑存储路径
        var layout = "1x1";                                      //playMode指定模式的布局
        var enableHTTPS = 1;                                     //是否启用HTTPS协议与综合安防管理平台交互，这里总是填1
        var encryptedFields = 'secret';					         //加密字段，默认加密领域为secret
        var showToolbar = 1;                                     //是否显示工具栏，0-不显示，非0-显示
        var showSmart = 1;                                       //是否显示智能信息（如配置移动侦测后画面上的线框），0-不显示，非0-显示
        var buttonIDs = "0,16,256,257,258,259,260,512,513,514,515,516,517,768,769";  //自定义工具条按钮
        //var reconnectTimes = 2;                            // 重连次数，回放异常情况下有效
        //var reconnectTime = 4;                             // 每次重连的重连间隔 >= reconnectTime
        ////////////////////////////////// 请自行修改以上变量值	////////////////////////////////////

        hkFun.oWebControl.JS_RequestInterface({
            funcName: "init",
            argument: JSON.stringify({
                appkey: appkey,                            //API网关提供的appkey
                secret: secret,                            //API网关提供的secret
                ip: ip,                                    //API网关IP地址
                playMode: playMode,                        //播放模式（决定显示预览还是回放界面）
                port: port,                                //端口
                snapDir: snapDir,                          //抓图存储路径
                videoDir: videoDir,                        //紧急录像或录像剪辑存储路径
                layout: layout,                            //布局
                enableHTTPS: enableHTTPS,                  //是否启用HTTPS协议
                encryptedFields: encryptedFields,          //加密字段
                showToolbar: showToolbar,                  //是否显示工具栏
                showSmart: showSmart,                      //是否显示智能信息
                buttonIDs: buttonIDs                       //自定义工具条按钮
                //reconnectTimes：reconnectTimes,            //重连次数
                //reconnectDuration：reconnectTime           //重连间隔
            })
        }).then(function (oData) {
            hkFun.oWebControl.JS_Resize(hkFun.disWidth, hkFun.disHeigth);  // 初始化后resize一次，规避firefox下首次显示窗口后插件窗口未与DIV窗口重合问题
        });
    });
}

// 获取公钥
hkFun.getPubKey = function (callback) {
    hkFun.oWebControl.JS_RequestInterface({
        funcName: "getRSAPubKey",
        argument: JSON.stringify({
            keyLength: 1024
        })
    }).then(function (oData) {
        //console.log(oData);
        if (oData.responseMsg.data) {
            hkFun.pubKey = oData.responseMsg.data;
            callback()
        }
    })
}

// RSA加密
hkFun.setEncrypt = function (value) {
    var encrypt = new JSEncrypt();
    encrypt.setPublicKey(hkFun.pubKey);
    return encrypt.encrypt(value);
}


// 设置窗口裁剪，当因滚动条滚动导致窗口需要被遮住的情况下需要JS_CuttingPartWindow部分窗口
hkFun.setWndCover = function () {
    var iWidth = $(window).width() - 500;
    var iHeight = $(window).height() - 30;
    var oDivRect = $("#" + hkFun.pluginContainerId).get(0).getBoundingClientRect();

    var iCoverLeft = (oDivRect.left < 0) ? Math.abs(oDivRect.left) : 0;
    var iCoverTop = (oDivRect.top < 0) ? Math.abs(oDivRect.top) : 0;
    var iCoverRight = (oDivRect.right - iWidth > 0) ? Math.round(oDivRect.right - iWidth) : 0;
    var iCoverBottom = (oDivRect.bottom - iHeight > 0) ? Math.round(oDivRect.bottom - iHeight) : 0;

    iCoverLeft = (iCoverLeft > hkFun.disWidth) ? hkFun.disWidth : iCoverLeft;
    iCoverTop = (iCoverTop > hkFun.disHeigth) ? hkFun.disHeigth : iCoverTop;
    iCoverRight = (iCoverRight > hkFun.disWidth) ? hkFun.disWidth : iCoverRight;
    iCoverBottom = (iCoverBottom > hkFun.disHeigth) ? hkFun.disHeigth : iCoverBottom;

    hkFun.oWebControl.JS_RepairPartWindow(0, 0, hkFun.disWidth + 1, hkFun.disHeigth);   // 多1个像素点防止还原后边界缺失一个像素条
    if (iCoverLeft != 0) {
        hkFun.oWebControl.JS_CuttingPartWindow(0, 0, iCoverLeft, hkFun.disHeigth);
    }
    if (iCoverTop != 0) {
        hkFun.oWebControl.JS_CuttingPartWindow(0, 0, hkFun.disWidth + 1, iCoverTop);  // 多剪掉一个像素条，防止出现剪掉一部分窗口后出现一个像素条
    }
    if (iCoverRight != 0) {
        hkFun.oWebControl.JS_CuttingPartWindow(hkFun.disWidth - iCoverRight, 0, iCoverRight, hkFun.disHeigth);
    }
    if (iCoverBottom != 0) {
        hkFun.oWebControl.JS_CuttingPartWindow(0, hkFun.disHeigth - iCoverBottom, hkFun.disWidth, iCoverBottom);
    }
};
//取得设备布局
hkFun.initLayout = function (codeList) {
    if (codeList) {
        var newlayout = "1+1+12";
        if (codeList.length <= 1) {
            newlayout = "1x1";
        } else if (codeList.length <= 2) {
            newlayout = "1x2";
        } else if (codeList.length <= 4) {
            newlayout = "2x2";
        } else if (codeList.length <= 9) {
            newlayout = "3x3";
        }
        hkFun.oWebControl.JS_RequestInterface({
            funcName: "setLayout",
            argument: JSON.stringify({
                layout: newlayout
            })
        });
    }
}


// 显示回调信息
hkFun.showCBInfo = function (message) {
    //console.log(message);
    $("#errorMsg").html(message);
}


// 监听resize事件，使插件窗口尺寸跟随DIV窗口变化
$(window).resize(function () {
    if (hkFun.oWebControl != null) {
        hkFun.oWebControl.JS_Resize(hkFun.disWidth, hkFun.disHeigth);
        hkFun.setWndCover();
    }
});

// 监听滚动条scroll事件，使插件窗口跟随浏览器滚动而移动
$(window).scroll(function () {
    if (hkFun.oWebControl != null) {
        hkFun.oWebControl.JS_Resize(hkFun.disWidth, hkFun.disHeigth);
        hkFun.setWndCover();
    }
});
// 标签关闭
$(window).unload(function () {
    if (hkFun.oWebControl != null) {
        hkFun.oWebControl.JS_HideWnd();   // 先让窗口隐藏，规避插件窗口滞后于浏览器消失问题
        hkFun.oWebControl.JS_Disconnect().then(function () {
        }, function () {
        });
    }
});