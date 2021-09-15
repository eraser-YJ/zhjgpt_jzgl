<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>升降机监控</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="referrer" content="always"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0,user-scalable=0,user-scalable=no,viewport-fit=cover"/>
    <meta http-equiv="x-dns-prefetch-control" content="on"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta name="x5-fullscreen" content="true"/>
    <style>
        .html, body, div, ul, li, a {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-size: 14px;
            color: #fff;
        }

        .html, body, #app {
            height: 100%;
            height: 100vh;
            overflow: hidden;
            background-color: #000;
        }

        .container {
            position: relative;
            margin-right: auto;
            margin-left: auto;
            width: 800px;
            margin-top: -70px;
            left: 70px;
        }



        .tower .hook {
            position: absolute;
            top: 99px;
            left: 438px;
            width: 55px;
            height: 45px;
            background-image: url(${sysPath}/mockequi/hoist/images/box.png);
            background-repeat: no-repeat;
            background-size: 100% auto;
            background-position: left bottom;
            transition: all 3s;
        }

        .tower .hook::after {
            content: " ";
            position: absolute;
            right: 7px;
            height: calc(100% - 45px);
            width: 2px;
            /*background-color: #f90;*/
        }

        .tower .hook::before {
            content: " ";
            position: absolute;
            left: 9px;
            height: calc(100% - 45px);
            width: 2px;
            /*background-color: #f90;*/
        }


        .table-container {
            border-collapse: collapse;
            width: 300px;
            position: fixed;
            bottom: 30px;
            left: 60px;
            z-index: 10000;

        }

        .table-container, th, td {
            text-align: left;
            /*border: 1px solid #fff;*/
            color:#fff;
        }
        .table-container td, .table-container thead tr{
            line-height: 20px !important;
            height: 20px !important;
        }
    </style>
    <!-- 使用CDN的JS文件 -->
    <script charset="utf-8" type="text/javascript" src="${sysPath}/mockequi/vue.min.js"></script>
</head>
<body>
<div id="app" onclick="app.moveToPosition()">
    <input type="hidden" id="query_id" name="query_id" value="${id}"/>
    <input type="hidden" id="query_code" name="query_code" value="${id}"/>
    <div class="container tower">
        <img src="${sysPath}/mockequi/hoist/images/background.png" alt="" style="width:1000px;">
        <div class="hook" :style="{left: left+'px',height: height+'px'}"></div>
    </div>
</div>
<div>
    <table class="table-container">
        <tbody>
        <tr>
            <td>高度：<span id="real_time_height"></span></td>
        </tr>
        <tr>
            <td>高度百分比：<span id="height_percentage"></span></td>
        </tr>
        <tr>
            <td>重量：<span id="real_time_lifting_weight"></span></td>
        </tr>
        <tr>
            <td>重量百分比：<span id="weight_percentage"></span></td>
        </tr>
        <tr>
            <td>倾斜度1：<span id="real_time_gradient1"></span></td>
        </tr>
        <tr>
            <td>倾斜度2：<span id="real_time_gradient2"></span></td>
        </tr>
        <tr>
            <td>倾斜百分比1：<span id="tilt_percentage1"></span></td>
        </tr>
        <tr>
            <td>倾斜百分比2：<span id="tilt_percentage2"></span></td>
        </tr>
        <tr>
            <td>人数：<span id="real_time_number_of_people"></span></td>
        </tr>
        <tr>
            <td>速度原始格式：<span id="real_time_speed"></span></td>
        </tr>
        <tr>
            <td>速度方向：<span id="real_time_speed_direction"></span></td>
        </tr>
        <tr>
            <td>速度：<span id="wind_speed"></span></td>
        </tr>
        <tr>
            <td>门锁状态：<span id="lock_state"></span></td>
        </tr>
        </tbody>
    </table>
</div>
<script>
    var leftbase = 352;
    var hookbase = 150;
    var hookMax = 535;
    var app = new Vue({
        el: '#app',
        data: function () {
            return {
                left: leftbase,
                height: hookbase,
                weight: true
            }
        },
        methods: {
            moveToPosition1 : function() {
                this.hookMove(hookbase)
            },
            moveToPosition2 : function() {
                this.hookMove(hookMax)
            },
            moveToPosition : function() {
                /**
                 * 这里走http请求, 返回值 调用leftMove，hookMove方法传入参数
                 */
                var queryId = $("#query_id").val();
                $.ajax({
                    type: "GET",
                    data: {id: queryId},
                    dataType: "json",
                    url: getRootPath() + "/monit/realtime/getHoistRealInfo.action",
                    success: function (resData) {
                        if(resData.success){
                            var data = resData.data;
                            var heigth = data.hoist_height_num;
                            var heigthData;
                            if(heigth){
                                heigthData = parseFloat(heigth)*10;
                            } else {
                                heigthData = hookMax;
                            }
                            //纠正
                            heigthData = heigthData;
                            if(heigthData<10){
                                heigthData = 10;
                            } else if(heigthData>hookMax){
                                heigthData = hookMax;
                            }

                            heigthData = hookMax - heigthData;
                            if(heigthData<hookbase){
                                heigthData = hookbase;
                            }
                            app.hookMove(heigthData)
                            for(key in data){
                                $("#"+key).html(data[key]);
                            }
                        } else {
                            app.hookMove(hookMax - 10)
                        }
                    }
                });
            },
            hookMove: function (num) {
                console.log(num)
                this.height = num;
            },
            leftMove: function (num) {
                this.left = num;
            }
        }
    })

    setInterval(resetPageInfo, 1000 * 6);

    var i = 0;

    function resetPageInfo() {
        // if (i % 2 == 0) {
        //     app.moveToPosition1();
        // } else {
        //     app.moveToPosition2();
        // }
        // i++;
        app.moveToPosition();
    }

    setTimeout(resetPageInfo, 1000);
</script>
<div></div>
</body>

</html>