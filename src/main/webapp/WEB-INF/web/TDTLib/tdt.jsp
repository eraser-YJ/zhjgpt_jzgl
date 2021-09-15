<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>地图服务</title>
    <link rel="stylesheet" href="https://js.arcgis.com/3.21/esri/css/esri.css">
    <script src="https://js.arcgis.com/3.21/"></script>
</head>
<style>
    html,
    body, #map {
        height: 100%;
        width: 100%;
        margin: 0;
        padding: 0;
    }
</style>

<script>
    var package_path = window.location.pathname.substring(0, window.location.pathname.lastIndexOf('/'));
    // console.log(package_path);
    var dojoConfig = {
        async: true,
        locale: 'zh-cn'
    }

    function getArcGisAddress() {
        return "${arcGisAddress}";
    }
</script>

<script>
    require([
            "${sysPath}/TDTLib/lib/TDTMapLayer.js",
            "esri/map",
            "esri/dijit/Scalebar",
            "esri/symbols/SimpleMarkerSymbol", "esri/symbols/SimpleLineSymbol", "esri/symbols/PictureMarkerSymbol", "esri/geometry/Point", "esri/layers/GraphicsLayer",
            "esri/symbols/PictureFillSymbol", "esri/symbols/CartographicLineSymbol", "esri/SpatialReference", "esri/InfoTemplate",
            "esri/graphic",
            "esri/layers/ArcGISDynamicMapServiceLayer", "esri/symbols/TextSymbol", "esri/symbols/Font", "esri/Color",
            "dojo/domReady!"
        ], function (TDTMapLayer, Map, Scalebar, SimpleMarkerSymbol, SimpleLineSymbol, PictureMarkerSymbol, Point, GraphicsLayer,
                     PictureFillSymbol, CartographicLineSymbol, SpatialReference, InfoTemplate,
                     Graphic, ArcGISDynamicMapServiceLayer, TextSymbol, Font, Color) {

            $.ajax({
                url: getRootPath() + "/desktop/manage/mapInfo.action", type: "GET",
                async: false,
                dataType: "json",
                success: function (dataList) {
                    graphicLoadCallback(dataList);
                }
            });

            function graphicLoadCallback(dataList) {
                var position = [125.7160000, 44.0550000];
                const map = new Map('map', {
                    logo: false,
                    center: position,
                    zoom: 10
                });
                /**
                 * TDTMapLayer 中包含四个参数，分别为
                 * 1.mapType：该值的默认值为vec,代表矢量图层，可供选择的值有vec|img|ter，分别代表矢量、影像、地形
                 * 2.mapCoordinate：该值的默认值为w,代表投影坐标，可供选择的值有c|w分比为：地理坐标，投影坐标
                 * 3.isAnnoLayer：默认值为"",可供选择的值为"ss"|"ano",分别代表非注记图层，注记图层
                 * 4.mapAnnotation：默认值为cva,不同注记类型的默认值分别为cva,cia,cta,代表矢量中文注记图层，不同底图的矢量注记可选项有：
                 *   vec(矢量):cva(中文)、eva(英文)、mva(蒙古文)、wva(维吾尔文)
                 *   img(影像):cia(中文)、eia(英文)、mia(蒙古文)、wia(维吾尔文)
                 *   ter(地形图):cta(中文)、eta(英文)、mta(蒙古文)、wta(维吾尔文)
                 */
                    //1.加载矢量图层（默认投影web墨卡托）
                let verLayer = new TDTMapLayer();
                //2.加载矢量注记图层
                let verAnnoLayer = new TDTMapLayer("ano");
                //3.加载影像图层
                let imgLayer = new TDTMapLayer("img", "c");
                //4.加载影像注记图层
                let imgAnnoLayer = new TDTMapLayer("img", "c", "ano");

                var _Dynamiclayer = new ArcGISDynamicMapServiceLayer(getArcGisAddress());
                _Dynamiclayer.setOpacity("60%");
                map.addLayer(verLayer);
                map.addLayer(verAnnoLayer);
                map.addLayer(_Dynamiclayer);
                var item;
                for (var i = 0; i < dataList.length; i++) {
                    item = dataList[i];
                    if (item.y_point && item.y_point != "" && item.x_point && item.x_point != "") {
                        var newPoint = new Point(item.x_point, item.y_point, new SpatialReference({wkid: 4326}));
                        var picSymbol = new PictureMarkerSymbol("${sysPath}/TDTLib/images/4.png", 22, 35);
                        picSymbol.setOffset(0, 30);
                        var picGraphic = new Graphic(newPoint, picSymbol);
                        picGraphic.setAttributes( {"projectCode":item.code});
                        var infoTemplate = new InfoTemplate();
                        infoTemplate.setTitle(item.code);
                        infoTemplate.setContent(item.code);
                        picGraphic.setInfoTemplate(infoTemplate);
                        map.graphics.add(picGraphic);

                        var graphicsLayer = new GraphicsLayer();
                        var font = new Font("12pt");
                        font.setFamily("Arial");
                        font.setWeight(Font.WEIGHT_BOLD);
                        font.setDecoration("none");
                        font.setStyle(Font.STYLE_NORMAL);
                        var textSym = new TextSymbol();
                        textSym.setAlign(TextSymbol.ALIGN_RIGHT);
                        textSym.setColor(new Color("#602EFF"));
                        textSym.setFont(font);
                        // textSym.setText(item.title);//注释掉项目名称
                        textSym.setHaloColor(new Color([255, 255, 255, 1]));  //
                        textSym.setHaloSize(1);  //

                        //添加为每个文字graphic添加位置、文字内容
                        var graphicText = new Graphic(newPoint, textSym);
                        //将graphic加入图层
                        graphicsLayer.add(graphicText);
                        //map添加graphiclayer图层
                        map.addLayer(graphicsLayer);

                    }
                }
                map.graphics.on("click",function(e){
                    var g = e.graphic;
                    var nowProjectCode = g.attributes.projectCode;
                    if(nowProjectCode){
                        clickProjectTarget(nowProjectCode);
                    }
                    e.stopPropagation();
                });
                dojo.connect(map, "onExtentChange", showExtent);
                var scalebar = new Scalebar({
                    map: map,
                    attachTo: "bottom-left",
                    scalebarUnit: "dual"
                });


                function showExtent(extent) {
                    var s = "";
                    s = "XMin: " + extent.xmin + "&nbsp;"
                        + "YMin: " + extent.ymin + "&nbsp;"
                        + "XMax: " + extent.xmax + "&nbsp;"
                        + "YMax: " + extent.ymax;                    //showExtent函数的最后一行显示在页面上添加完成字符串的“info”DIV坐标
                    dojo.byId("info").innerHTML = s;
                };

            }

        }
    )

    function clickProjectTarget(projectCode) {
        if (projectCode) {
            var projectCodeStr = projectCode.replace(/^\s+|\s+$/g, '');
            $("#projectDisDiv").load(getRootPath() + "/desktop/manage/projectDesktop.action?projectCode=" + projectCodeStr + "&n_=" + new Date().getTime(), null, function () {
                projectDesktopInfoJsForm.init({title: '查看'});
            });
        }
    }


</script>
</head>
<body>
<div id="info">&nbsp;</div>
<div id="map"></div>
<g id="graphicsLayer1_layer" data-geometry-type="point" style="display: block;">
    <circle fill="rgb(70, 230, 255)"
            fill-opacity="0.8"
            stroke="none" stroke-opacity="0"
            stroke-width="1"
            stroke-linecap="butt"
            stroke-linejoin="miter"
            stroke-miterlimit="4"
            cx="653"
            cy="519"
            r="6"
            transform="matrix(1.00000000,0.00000000,0.00000000,1.00000000,0.00000000,0.00000000)"
            fill-rule="evenodd"></circle>
    <circle fill="rgb(70, 230, 255)"
            fill-opacity="0.8"
            stroke="none"
            stroke-opacity="0"
            stroke-width="1"
            stroke-linecap="butt"
            stroke-linejoin="miter"
            stroke-miterlimit="4"
            cx="764"
            cy="533"
            r="6"
            transform="matrix(1.00000000,0.00000000,0.00000000,1.00000000,0.00000000,0.00000000)"
            fill-rule="evenodd"></circle>
</g>
<div id="projectDisDiv"></div>
<div id="formDialogDisDiv"></div>
</body>
</html>