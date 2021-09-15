/***
 * openlayers 平移缩放控制控件 
 */
OpenLayers.Control.SCGISPanZoomBar = OpenLayers.Class(OpenLayers.Control.PanZoom, {
    zoomStopWidth: 18,
    zoomStopHeight: 11,
    slider: null,
    sliderEvents: null,
    zoombarDiv: null,
    navbarDiv: null,
    zoomWorldIcon: false,
    panIcons: true,
    forceFixedZoomLevel: false,
    mouseDragStart: null,
    deltaY: null,
    zoomStart: null,
    divEvents: null,
    navbardivEvents: null,
    destroy: function() {
        this._removeZoomBar();
        this.map.events.un({
            "changebaselayer": this.redraw,
            scope: this
        });
        OpenLayers.Control.PanZoom.prototype.destroy.apply(this, arguments);
        delete this.mouseDragStart;
        delete this.zoomStart;
    },
    setMap: function(map) {
        OpenLayers.Control.PanZoom.prototype.setMap.apply(this, arguments);
        this.map.events.register("changebaselayer", this, this.redraw);
    },
    redraw: function() {
        if (this.div != null) {
            this.removeButtons();
            this._removeZoomBar();
        }
        this.draw();
    },
    draw: function(px) {
        OpenLayers.Control.prototype.draw.apply(this, arguments);
        px = this.position.clone();
        px.x = 4;
        px.y = 4;
        this.buttons = [];
        var sz = {
            w: 18,
            h: 18
        };
        if (this.panIcons) {
            var centered = new OpenLayers.Pixel(px.x + sz.w / 2, px.y);
            var wposition = sz.w;
            if (this.zoomWorldIcon) {
                centered = new OpenLayers.Pixel(px.x + sz.w, px.y);
            }
            var navpos = new OpenLayers.Pixel(px.x - 5, px.y + 16);
            var navsz = {
                w: 45,
                h: 45
            };
            px.y = centered.y + sz.h;
            this._addNavBar(navpos, navsz);
            this._addButton("zoomin", "SCGISzoom-plus-mini.png", centered.add(0, sz.h * 3 + 5), sz);
            centered = this._addZoomBar(centered.add(0, sz.h * 4 + 5));
            this._addButton("zoomout", "SCGISzoom-minus-mini.png", centered, sz);
        } else {
            this._addButton("zoomin", "SCGISzoom-plus-mini.png", px, sz);
            centered = this._addZoomBar(px.add(0, sz.h));
            this._addButton("zoomout", "SCGISzoom-minus-mini.png", centered, sz);
            if (this.zoomWorldIcon) {
                centered = centered.add(0, sz.h + 3);
                this._addButton("zoomworld", "SCGISzoom-world-mini.png", centered, sz);
            }
        }
        return this.div;
    },
    _addNavBar: function(centered, sz) {
        var imgLocation = OpenLayers.Util.getImageLocation("");
        var id = this.id + "_" + this.map.id;
        var div = null;
        div = OpenLayers.Util.createDiv('OpenLayers_Control_SCGISPanZoomBar_NavBar' + this.map.id, centered, sz, imgLocation);
        div.style.cursor = "pointer";
        this.navbarDiv = div;
        this.div.appendChild(div);
        this.navbardivEvents = new OpenLayers.Events(this, this.navbarDiv, null, true, {
            includeXY: true
        });
        this.navbardivEvents.on({
            "mousemove": this.navbarDivmousemove,
            "mousedown": this.navbarDivmousedown
        });
        this.navbarDiv.style.backgroundImage = "url('./public/style/images/SCGISNavBar.png')";
        this.navbarDiv.className = "olControlSCGISNavBar_pan";
    },
    navbarDivmousemove: function(evt) {
        var x = evt.xy.x;
        var y = evt.xy.y;
        var imgstr;
        if (x > 15 && x < 30 && y < 15) this.navbarDiv.className = "olControlSCGISNavBar_panup";
        else if (x < 15 && y > 15 && y < 30) this.navbarDiv.className = "olControlSCGISNavBar_panleft";
        else if (x > 30 && x < 45 && y > 15 && y < 30) this.navbarDiv.className = "olControlSCGISNavBar_panright";
        else if (x > 15 && x < 30 && y > 30 && y < 45) this.navbarDiv.className = "olControlSCGISNavBar_pandown";
        else this.navbarDiv.className = "olControlSCGISNavBar_pan";
    },
    navbarDivmousedown: function(evt) {
        var x = evt.xy.x;
        var y = evt.xy.y;
        if (x > 15 && x < 30 && y < 15) this.map.pan(0, -300);
        else if (x < 15 && y > 15 && y < 30) this.map.pan(-300, 0);
        else if (x > 30 && x < 45 && y > 15 && y < 30) this.map.pan(300, 0);
        else if (x > 15 && x < 30 && y > 30 && y < 45) this.map.pan(0, 300);
        //实现地图可视范围回归初始大小
        else if (x > 15 && x < 30 && y > 15 && y < 30) mapPage.mapManager.mapUtils.fullExtent();
    },
    _removeNavBar: function() {
        this.div.removeChild(this.navbarDiv);
        this.navbarDiv = null;
    },
    _addZoomBar: function(centered) {
        var imgLocation = OpenLayers.Util.getImageLocation("SCGISslider.png");
        var id = this.id + "_" + this.map.id;
        var zoomsToEnd = this.map.getNumZoomLevels() - 1 - this.map.getZoom();
        var slider = OpenLayers.Util.createAlphaImageDiv(id, centered.add(-1, zoomsToEnd * this.zoomStopHeight), {
            w: 20,
            h: 9
        }, imgLocation, "absolute");
        slider.style.cursor = "move";
        this.slider = slider;
        this.sliderEvents = new OpenLayers.Events(this, slider, null, true, {
            includeXY: true
        });
        this.sliderEvents.on({
            "touchstart": this.zoomBarDown,
            "touchmove": this.zoomBarDrag,
            "touchend": this.zoomBarUp,
            "mousedown": this.zoomBarDown,
            "mousemove": this.zoomBarDrag,
            "mouseup": this.zoomBarUp
        });
        var sz = {
            w: this.zoomStopWidth,
            h: this.zoomStopHeight * this.map.getNumZoomLevels()
        };
        var imgLocation = OpenLayers.Util.getImageLocation("SCGISzoombar.png");
        var div = null;
        if (OpenLayers.Util.alphaHack()) {
            var id = this.id + "_" + this.map.id;
            div = OpenLayers.Util.createAlphaImageDiv(id, centered, {
                w: sz.w,
                h: this.zoomStopHeight
            }, imgLocation, "absolute", null, "crop");
            div.style.height = sz.h + "px";
        } else {
            div = OpenLayers.Util.createDiv('OpenLayers_Control_SCGISPanZoomBar_Zoombar' + this.map.id, centered, sz, imgLocation);
        }
        div.style.cursor = "pointer";
        div.className = "olButton";
        this.zoombarDiv = div;
        this.div.appendChild(div);
        this.startTop = parseInt(div.style.top);
        this.div.appendChild(slider);
        this.divEvents = new OpenLayers.Events(this, this.zoombarDiv, null, true, {
            includeXY: true
        });
        this.divEvents.on({
            "mousemove": this.zoombarDivmousemove
        });
        this.map.events.register("zoomend", this, this.moveZoomBar);
        centered = centered.add(0, this.zoomStopHeight * this.map.getNumZoomLevels());
        return centered;
    },
    zoombarDivmousemove: function(evt) {
        var numLevel = this.map.getNumZoomLevels();
        var m_zoomlevel;
        if (numLevel == 17 || numLevel == 19) {
            m_zoomlevel = Math.floor(this.map.getNumZoomLevels() + 2 - (evt.xy.y) / 11);
            if (m_zoomlevel < this.map.getNumZoomLevels() + 2 && m_zoomlevel > 1) {
                var scale = parseInt(this.map.baseLayer.scales[m_zoomlevel - 2] / 100) * 100;
                this.zoombarDiv.title = "第" + m_zoomlevel + "级,1:" + scale;
            } else this.zoombarDiv.title = "";
        } else if (numLevel == 16 || numLevel == 18) {
            m_zoomlevel = Math.floor(this.map.getNumZoomLevels() + 3 - (evt.xy.y) / 11);
            if (m_zoomlevel < this.map.getNumZoomLevels() + 3 && m_zoomlevel > 2) {
                var scale = parseInt(this.map.baseLayer.scales[m_zoomlevel - 3] / 100) * 100;
                this.zoombarDiv.title = "第" + m_zoomlevel + "级,1:" + scale;
            } else this.zoombarDiv.title = "";
        } else {
            m_zoomlevel = Math.floor(21 - (evt.xy.y) / 11);
            if (m_zoomlevel < 21 && m_zoomlevel > 20 - this.map.getNumZoomLevels()) {
                var scale = parseInt(this.map.baseLayer.scales[m_zoomlevel + numLevel - 21] / 100) * 100;
                this.zoombarDiv.title = "第" + m_zoomlevel + "级";
            } else this.zoombarDiv.title = "";
        }
    },
    _removeZoomBar: function() {
        this.sliderEvents.un({
            "touchstart": this.zoomBarDown,
            "touchmove": this.zoomBarDrag,
            "touchend": this.zoomBarUp,
            "mousedown": this.zoomBarDown,
            "mousemove": this.zoomBarDrag,
            "mouseup": this.zoomBarUp
        });
        this.sliderEvents.destroy();
        this.div.removeChild(this.zoombarDiv);
        this.zoombarDiv = null;
        this.div.removeChild(this.slider);
        this.slider = null;
        this.map.events.unregister("zoomend", this, this.moveZoomBar);
    },
    onButtonClick: function(evt) {
        OpenLayers.Control.PanZoom.prototype.onButtonClick.apply(this, arguments);
        if (evt.buttonElement === this.zoombarDiv) {
            var levels = evt.buttonXY.y / this.zoomStopHeight;
            if (this.forceFixedZoomLevel || !this.map.fractionalZoom) {
                levels = Math.floor(levels);
            }
            var zoom = (this.map.getNumZoomLevels() - 1) - levels;
            zoom = Math.min(Math.max(zoom, 0), this.map.getNumZoomLevels() - 1);
            this.map.zoomTo(zoom);
        }
    },
    passEventToSlider: function(evt) {
        this.sliderEvents.handleBrowserEvent(evt);
    },
    zoomBarDown: function(evt) {
        if (!OpenLayers.Event.isLeftClick(evt) && !OpenLayers.Event.isSingleTouch(evt)) {
            return;
        }
        this.map.events.on({
            "touchmove": this.passEventToSlider,
            "mousemove": this.passEventToSlider,
            "mouseup": this.passEventToSlider,
            scope: this
        });
        this.mouseDragStart = evt.xy.clone();
        this.zoomStart = evt.xy.clone();
        this.div.style.cursor = "move";
        this.zoombarDiv.offsets = null;
        OpenLayers.Event.stop(evt);
    },
    zoomBarDrag: function(evt) {
        if (this.mouseDragStart != null) {
            var deltaY = this.mouseDragStart.y - evt.xy.y;
            var offsets = OpenLayers.Util.pagePosition(this.zoombarDiv);
            if ((evt.clientY - offsets[1]) > 0 && (evt.clientY - offsets[1]) < parseInt(this.zoombarDiv.style.height) - 2) {
                var newTop = parseInt(this.slider.style.top) - deltaY;
                this.slider.style.top = newTop + "px";
                this.mouseDragStart = evt.xy.clone();
            }
            this.deltaY = this.zoomStart.y - evt.xy.y;
            OpenLayers.Event.stop(evt);
        }
    },
    zoomBarUp: function(evt) {
        if (!OpenLayers.Event.isLeftClick(evt) && evt.type !== "touchend") {
            return;
        }
        if (this.mouseDragStart) {
            this.div.style.cursor = "";
            this.map.events.un({
                "touchmove": this.passEventToSlider,
                "mouseup": this.passEventToSlider,
                "mousemove": this.passEventToSlider,
                scope: this
            });
            var zoomLevel = this.map.zoom;
            if (!this.forceFixedZoomLevel && this.map.fractionalZoom) {
                zoomLevel += this.deltaY / this.zoomStopHeight;
                zoomLevel = Math.min(Math.max(zoomLevel, 0), this.map.getNumZoomLevels() - 1);
            } else {
                zoomLevel += this.deltaY / this.zoomStopHeight;
                zoomLevel = Math.max(Math.round(zoomLevel), 0);
            }
            this.map.zoomTo(zoomLevel);
            this.mouseDragStart = null;
            this.zoomStart = null;
            this.deltaY = 0;
            OpenLayers.Event.stop(evt);
        }
    },
    moveZoomBar: function() {
        var newTop = ((this.map.getNumZoomLevels() - 1) - this.map.getZoom()) * this.zoomStopHeight + this.startTop + 1;
        this.slider.style.top = newTop + "px";
    },
    CLASS_NAME: "OpenLayers.Control.SCGISPanZoomBar"
});
