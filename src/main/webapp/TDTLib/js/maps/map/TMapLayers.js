/***
 * @Description 该类用于实现就接入国家天地图的底图，该类继承OpenLayers2的OpenLayers.Layer.Grid类。
 */
OpenLayers.Layer.TiandituLayer = OpenLayers.Class(OpenLayers.Layer.Grid, {
    mapType : "EMap",
    mirrorUrls : [
        "http://t0.tianditu.com/DataServer",
        "http://t1.tianditu.com/DataServer",
        "http://t2.tianditu.com/DataServer",
        "http://t3.tianditu.com/DataServer",
        "http://t4.tianditu.com/DataServer",
        "http://t5.tianditu.com/DataServer",
        "http://t6.tianditu.com/DataServer",
        "http://t7.tianditu.com/DataServer"
    ],
    topLevel : 2,
    bottomLevel : 18,
    topLevelIndex : 0,
    bottomLevelIndex : 18,
    topTileFromX : -180,
    topTileFromY : 90,
    topTileToX : 180,
    topTileToY : -270,
    isBaseLayer : false,
    initialize : function (name, url, options) {
        options.topLevel = options.topLevel ? options.topLevel : this.topLevel;
        options.bottomLevel = options.bottomLevel ? options.bottomLevel : this.bottomLevel;
        options.maxResolution = this.getResolutionForLevel(options.topLevel);
        options.minResolution = this.getResolutionForLevel(options.bottomLevel);
        var newArguments = [name, url, {}, options];
        OpenLayers.Layer.Grid.prototype.initialize.apply(this, newArguments);
    },
    clone : function (obj) {
        if (obj == null) {
            obj = new OpenLayers.Layer.TiandituLayer(this.name, this.url, this.options);
        }
        obj = OpenLayers.Layer.Grid.prototype.clone.apply(this, [obj]);
        return obj;
    },
    getURL : function (bounds) {
        var level = this.getLevelForResolution(this.map.getResolution());
        var coef = 360 / Math.pow(2, level);
        var x_num = this.topTileFromX < this.topTileToX ? Math.round((bounds.left - this.topTileFromX) / coef) : Math.round((this.topTileFromX - bounds.right) / coef);
        var y_num = this.topTileFromY < this.topTileToY ? Math.round((bounds.bottom - this.topTileFromY) / coef) : Math.round((this.topTileFromY - bounds.top) / coef);

        var type = this.mapType;
        
        switch(type){
            case "EMap":
                type = "vec_c";
                break;
            case "EMapANNO":
                type = "eva_c";
                break;
            case "CMapANNO":
                type = "cva_c";
                break;
            case "Img":
                type = "img_c";
                break;
            case "ImgANNO":
                type = "cia_c";
                break;
            case "Terrain":
                type = "ter_c";
                break;
            case "TerrainANNO":
                type = "cta_c";
                break;
        }
        var url = this.url;
        if (this.mirrorUrls != null) {
            url = this.selectUrl(x_num+y_num, this.mirrorUrls);
        }

        return this.getFullRequestString({
            T : type,
            X : x_num,
            Y : y_num,
            L : level
        }, url);
    },
    selectUrl : function (a, b) {
        return b[a % b.length];
    },
    getLevelForResolution : function (res) {
        var ratio = this.getMaxResolution() / res;
        if (ratio < 1)
            return 0;
        for (var level = 0; ratio / 2 >= 1; ) {
            level++;
            ratio /= 2;
        }
        return level;
    },
    getResolutionForLevel : function (level) {
        return 360 / 256 / Math.pow(2, level);
    },
    getMaxResolution : function () {
        return this.getResolutionForLevel(this.topLevelIndex);
    },
    getMinResolution : function () {
        return this.getResolutionForLevel(this.bottomLevelIndex);
    },
    addTile : function (bounds, position) {
        var url = this.getURL(bounds);
        return new OpenLayers.Tile.Image(this, position, bounds, url, this.tileSize);
    },

    CLASS_NAME : "OpenLayers.Layer.TiandituLayer"
});

