/*! Copyright (c) 2011 Piotr Rochala (http://rocha.la)
 * Dual licensed under the MIT (http://www.opensource.org/licenses/mit-license.php)
 * and GPL (http://www.opensource.org/licenses/gpl-license.php) licenses.
 *
 * Version: 1.3.8
 *
 */
/**
 * 左菜单滚动条插件
 * @param width  		宽度        			默认auto
 * @param height 		高度   				默认auto
 * @param size   		滚动条宽度   		默认5px
 * @param color  		滚动条背景色 		默认#333333
 * @param position		滚动条方向			默认right
 * @param opacity  		滚动条透明度			默认0.4
 * @param disableResize 监听窗口变化 		默认false
 */
(function($) {

    $.fn.extend({
        slimScroll: function(options) {

            var defaults = {

                // width in pixels of the visible scroll area
                width : 'auto',

                // height in pixels of the visible scroll area
                height : 'auto',

                // width in pixels of the scrollbar and rail
                size : '5px',

                // scrollbar color, accepts any hex/color value
                color: '#333',

                // scrollbar position - left/right
                position : 'right',

                // distance in pixels between the side edge and the scrollbar
                distance : '1px',

                // default scroll position on load - top / bottom / $('selector')
                start : 'top',

                // sets scrollbar opacity
                opacity : .4,

                // enables always-on mode for the scrollbar
                alwaysVisible : false,

                // check if we should hide the scrollbar when user is hovering over
                disableFadeOut : false,

                // sets visibility of the rail
                railVisible : false,

                // sets rail color
                railColor : '#333',

                // sets rail opacity
                railOpacity : .2,

                // whether  we should use jQuery UI Draggable to enable bar dragging
                railDraggable : true,

                // defautlt CSS class of the slimscroll rail
                railClass : 'slimScrollRail',

                // defautlt CSS class of the slimscroll bar
                barClass : 'slimScrollBar',

                // defautlt CSS class of the slimscroll wrapper
                wrapperClass : 'slimScrollDiv',

                // check if mousewheel should scroll the window if we reach top/bottom
                allowPageScroll : false,

                // scroll amount applied to each mouse wheel step
                wheelStep : 20,

                // scroll amount applied when user is using gestures
                touchScrollStep : 200,

                // sets border radius
                borderRadius: '7px',

                // sets border radius of the rail
                railBorderRadius : '7px'
            };

            var o = $.extend(defaults, options);

            // do it for every element that matches selector
            this.each(function(){

                var isOverPanel, isOverBar, isDragg, queueHide, touchDif,
                    barHeight, percentScroll, lastScroll,
                    divS = '<div></div>',
                    minBarHeight = 30,
                    releaseScroll = false;

                // used in event handlers and for better minification
                var me = $(this);

                // ensure we are not binding it again
                if (me.parent().hasClass(o.wrapperClass))
                {
                    // start from last bar position
                    var offset = me.scrollTop();

                    // find bar and rail
                    bar = me.siblings('.' + o.barClass);
                    rail = me.siblings('.' + o.railClass);

                    getBarHeight();

                    // check if we should scroll existing instance
                    if ($.isPlainObject(options))
                    {
                        // Pass height: auto to an existing slimscroll object to force a resize after contents have changed
                        if ( 'height' in options && options.height == 'auto' ) {
                            me.parent().css('height', 'auto');
                            me.css('height', 'auto');
                            var height = me.parent().parent().height();
                            if(height > 10){
                                me.parent().css('height', height);
                                me.css('height', height);
                            }
                        } else if ('height' in options) {
                            var h = options.height;
                            if(height > 10){
                                me.parent().css('height', h);
                                me.css('height', h);
                            }
                        }

                        if ('scrollTo' in options)
                        {
                            // jump to a static point
                            offset = parseInt(o.scrollTo);
                        }
                        else if ('scrollBy' in options)
                        {
                            // jump by value pixels
                            offset += parseInt(o.scrollBy);
                        }
                        else if ('destroy' in options)
                        {
                            // remove slimscroll elements
                            bar.remove();
                            rail.remove();
                            me.unwrap();
                            return;
                        }

                        // scroll content by the given offset
                        scrollContent(offset, false, true);
                    }

                    return;
                }
                else if ($.isPlainObject(options))
                {
                    if ('destroy' in options)
                    {
                        return;
                    }
                }

                // optionally set height to the parent's height
                o.height = (o.height == 'auto') ? me.parent().height() : o.height;

                // wrap content
                var wrapper = $(divS)
                    .addClass(o.wrapperClass)
                    .css({
                        position: 'relative',
                        overflow: 'hidden',
                        width: o.width,
                        height: o.height
                    });

                // update style for the div
                me.css({
                    overflow: 'hidden',
                    width: o.width,
                    height: o.height
                });

                // create scrollbar rail
                var rail = $(divS)
                    .addClass(o.railClass)
                    .css({
                        width: o.size,
                        height: '100%',
                        position: 'absolute',
                        top: 0,
                        display: (o.alwaysVisible && o.railVisible) ? 'block' : 'none',
                        'border-radius': o.railBorderRadius,
                        background: o.railColor,
                        opacity: o.railOpacity,
                        zIndex: 90
                    });

                // create scrollbar
                var bar = $(divS)
                    .addClass(o.barClass)
                    .css({
                        background: o.color,
                        width: o.size,
                        position: 'absolute',
                        top: 0,
                        opacity: o.opacity,
                        display: o.alwaysVisible ? 'block' : 'none',
                        'border-radius' : o.borderRadius,
                        BorderRadius: o.borderRadius,
                        MozBorderRadius: o.borderRadius,
                        WebkitBorderRadius: o.borderRadius,
                        zIndex: 99
                    });

                // set position
                var posCss = (o.position == 'right') ? { right: o.distance } : { left: o.distance };
                rail.css(posCss);
                bar.css(posCss);

                // wrap it
                me.wrap(wrapper);

                // append to parent div
                me.parent().append(bar);
                me.parent().append(rail);

                // make it draggable and no longer dependent on the jqueryUI
                if (o.railDraggable){
                    bar.bind("mousedown", function(e) {
                        var $doc = $(document);
                        isDragg = true;
                        t = parseFloat(bar.css('top'));
                        pageY = e.pageY;

                        $doc.bind("mousemove.slimscroll", function(e){
                            currTop = t + e.pageY - pageY;
                            bar.css('top', currTop);
                            scrollContent(0, bar.position().top, false);// scroll content
                        });

                        $doc.bind("mouseup.slimscroll", function(e) {
                            isDragg = false;hideBar();
                            $doc.unbind('.slimscroll');
                        });
                        return false;
                    }).bind("selectstart.slimscroll", function(e){
                        e.stopPropagation();
                        e.preventDefault();
                        return false;
                    });
                }

                // on rail over
                rail.hover(function(){
                    showBar();
                }, function(){
                    hideBar();
                });

                // on bar over
                bar.hover(function(){
                    isOverBar = true;
                }, function(){
                    isOverBar = false;
                });

                // show on parent mouseover
                me.hover(function(){
                    isOverPanel = true;
                    showBar();
                    hideBar();
                }, function(){
                    isOverPanel = false;
                    hideBar();
                });

                // support for mobile
                me.bind('touchstart', function(e,b){
                    if (e.originalEvent.touches.length)
                    {
                        // record where touch started
                        touchDif = e.originalEvent.touches[0].pageY;
                    }
                });

                me.bind('touchmove', function(e){
                    // prevent scrolling the page if necessary
                    if(!releaseScroll)
                    {
                        e.originalEvent.preventDefault();
                    }
                    if (e.originalEvent.touches.length)
                    {
                        // see how far user swiped
                        var diff = (touchDif - e.originalEvent.touches[0].pageY) / o.touchScrollStep;
                        // scroll content
                        scrollContent(diff, true);
                        touchDif = e.originalEvent.touches[0].pageY;
                    }
                });

                // set up initial height
                getBarHeight();

                // check start position
                if (o.start === 'bottom')
                {
                    // scroll content to bottom
                    bar.css({ top: me.outerHeight() - bar.outerHeight() });
                    scrollContent(0, true);
                }
                else if (o.start !== 'top')
                {
                    // assume jQuery selector
                    scrollContent($(o.start).position().top, null, true);

                    // make sure bar stays hidden
                    if (!o.alwaysVisible) { bar.hide(); }
                }

                // attach scroll events
                attachWheel(this);

                function _onWheel(e)
                {
                    // use mouse wheel only when mouse is over
                    if (!isOverPanel) { return; }

                    var e = e || window.event;

                    var delta = 0;
                    if (e.wheelDelta) { delta = -e.wheelDelta/120; }
                    if (e.detail) { delta = e.detail / 3; }

                    var target = e.target || e.srcTarget || e.srcElement;
                    if ($(target).closest('.' + o.wrapperClass).is(me.parent())) {
                        // scroll content
                        scrollContent(delta, true);
                    }

                    // stop window scroll
                    if (e.preventDefault && !releaseScroll) { e.preventDefault(); }
                    if (!releaseScroll) { e.returnValue = false; }
                }

                function scrollContent(y, isWheel, isJump)
                {
                    releaseScroll = false;
                    var delta = y;
                    var maxTop = me.outerHeight() - bar.outerHeight();

                    if (isWheel)
                    {
                        // move bar with mouse wheel
                        delta = parseInt(bar.css('top')) + y * parseInt(o.wheelStep) / 100 * bar.outerHeight();

                        // move bar, make sure it doesn't go out
                        delta = Math.min(Math.max(delta, 0), maxTop);

                        // if scrolling down, make sure a fractional change to the
                        // scroll position isn't rounded away when the scrollbar's CSS is set
                        // this flooring of delta would happened automatically when
                        // bar.css is set below, but we floor here for clarity
                        delta = (y > 0) ? Math.ceil(delta) : Math.floor(delta);

                        // scroll the scrollbar
                        bar.css({ top: delta + 'px' });
                    }

                    // calculate actual scroll amount
                    percentScroll = parseInt(bar.css('top')) / (me.outerHeight() - bar.outerHeight());
                    delta = percentScroll * (me[0].scrollHeight - me.outerHeight());

                    if (isJump)
                    {
                        delta = y;
                        var offsetTop = delta / me[0].scrollHeight * me.outerHeight();
                        offsetTop = Math.min(Math.max(offsetTop, 0), maxTop);
                        bar.css({ top: offsetTop + 'px' });
                    }

                    // scroll content
                    me.scrollTop(delta);

                    // fire scrolling event
                    me.trigger('slimscrolling', ~~delta);

                    // ensure bar is visible
                    showBar();

                    // trigger hide when scroll is stopped
                    hideBar();
                }

                function attachWheel(target)
                {
                    if (window.addEventListener)
                    {
                        target.addEventListener('DOMMouseScroll', _onWheel, false );
                        target.addEventListener('mousewheel', _onWheel, false );
                    }
                    else
                    {
                        document.attachEvent("onmousewheel", _onWheel)
                    }
                }

                function getBarHeight()
                {
                    // calculate scrollbar height and make sure it is not too small
                    barHeight = Math.max((me.outerHeight() / me[0].scrollHeight) * me.outerHeight(), minBarHeight);
                    bar.css({ height: barHeight + 'px' });

                    // hide scrollbar if content is not long enough
                    var display = barHeight == me.outerHeight() ? 'none' : 'block';
                    bar.css({ display: display });
                }

                function showBar()
                {
                    // recalculate bar height
                    getBarHeight();
                    clearTimeout(queueHide);

                    // when bar reached top or bottom
                    if (percentScroll == ~~percentScroll)
                    {
                        //release wheel
                        releaseScroll = o.allowPageScroll;

                        // publish approporiate event
                        if (lastScroll != percentScroll)
                        {
                            var msg = (~~percentScroll == 0) ? 'top' : 'bottom';
                            me.trigger('slimscroll', msg);
                        }
                    }
                    else
                    {
                        releaseScroll = false;
                    }
                    lastScroll = percentScroll;

                    // show only when required
                    if(barHeight >= me.outerHeight()) {
                        //allow window scroll
                        releaseScroll = true;
                        return;
                    }
                    bar.stop(true,true).fadeIn('fast');
                    if (o.railVisible) { rail.stop(true,true).fadeIn('fast'); }
                }

                function hideBar()
                {
                    // only hide when options allow it
                    if (!o.alwaysVisible)
                    {
                        queueHide = setTimeout(function(){
                            if (!(o.disableFadeOut && isOverPanel) && !isOverBar && !isDragg)
                            {
                                bar.fadeOut('slow');
                                rail.fadeOut('slow');
                            }
                        }, 1000);
                    }
                }

            });

            // maintain chainability
            return this;
        }
    });

    $.fn.extend({
        slimscroll: $.fn.slimScroll
    });
})(jQuery);
/* ========================================================================
 * Bootstrap: modal.js v3.0.3
 * http://getbootstrap.com/javascript/#modals
 * ========================================================================
 * Copyright 2013 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ======================================================================== */
+function ($) { "use strict";
    // MODAL CLASS DEFINITION
    // ======================
    var Modal = function (element, options) {
        this.options   = options
        this.$element  = $(element)
        this.$backdrop =
            this.isShown   = null
        this.$winWidth = $(window.parent.window).height() || $(window).height();
        if (this.options.remote) this.$element.load(this.options.remote)
    }
    Modal.DEFAULTS = {
        backdrop: 'static'
        , keyboard: true
        , show: true
    }

    Modal.INDEX = 1050;
    Modal.INTEMNUM = 0;

    Modal.prototype.toggle = function (_relatedTarget) {
        return this[!this.isShown ? 'show' : 'hide'](_relatedTarget)
    }

    Modal.prototype.show = function (_relatedTarget) {

        this.$body = $(this.$element[0]).find('.modal-body');
        this.$content = $(this.$element[0]).find('.modal-content');

        if(JCF.browser.ie) setIframeInOffice(this.$element.attr('id'),'modal');
        var that = this
        var e    = $.Event('show.bs.modal', { relatedTarget: _relatedTarget })

        this.$element.trigger(e)

        if (this.isShown || e.isDefaultPrevented()) return

        this.isShown = true

        this.escape()

        this.$element.on('click.dismiss.modal', '[data-dismiss="modal"]', $.proxy(this.hide, this))

        this.backdrop(function () {
            var transition = $.support.transition && that.$element.hasClass('fade')

            if (!that.$element.parent().length) {
                that.$element.appendTo(document.body) // don't move modals dom
                // position
            }
            that.$element.show()

            if (transition) {
                that.$element[0].offsetWidth // force reflow
            }
            that.$element
                .addClass('in')
                .attr('aria-hidden', false);
            that.enforceFocus();

            var e = $.Event('shown.bs.modal', { relatedTarget: _relatedTarget })
            transition ?
                that.$element.find('.modal-dialog') // wait for modal to slide in
                    .one($.support.transition.end, function () {
                        that.$element.focus().trigger(e)
                    })
                    .emulateTransitionEnd(300) :
                that.$element.focus().trigger(e)
        })
        this.addZindex();
        var tempHtml = that.$body.height()
        setInterval(function() {
            if (that.$body.height() != tempHtml) {
                that.setPaddingTop();
                tempHtml = that.$body.height();
            }
        }, 250);
    }

    Modal.prototype.hide = function (e) {
        if (e) e.preventDefault()
        e = $.Event('hide.bs.modal')
        this.$element.trigger(e)
        if (!this.isShown || e.isDefaultPrevented()) return
        this.isShown = false
        this.escape()
        $(document).off('focusin.bs.modal')
        this.$element
            .removeClass('in')
            .attr('aria-hidden', true)
            .off('click.dismiss.modal')

        $.support.transition && this.$element.hasClass('fade') ?
            this.$element
                .one($.support.transition.end, $.proxy(this.hideModal, this))
                .emulateTransitionEnd(300) :
            this.hideModal()

        if(JCF.browser.ie) removeIframeInOffice(this.$element.attr('id'));
    }

    Modal.prototype.enforceFocus = function () {
        var that = this;
        $(document).off('focusin.bs.modal');
        var temp_m = $(that.$body.children().not(":hidden").last()).css('margin-bottom','20px');
        if(JCF.browser.ie8) $("<div id='qswk' style='min-height:1px;margin-top:-1px;'></div>").insertAfter(temp_m);
        that.setMaxHeight();
        setTimeout(function(){that.setPaddingTop();},300);
    }
    // 设置上边距
    Modal.prototype.setPaddingTop = function(){
        $(this.$element[0]).children().css("padding-top",(this.$winWidth - $(this.$element[0]).find('.modal-content').height()) / 2+"px");
    }
    // 设置最大高度
    Modal.prototype.setMaxHeight = function(){
        // 根据屏幕分辨率设置modal-body的最大高度
        var tH = this.$content.height() - this.$body.height();
        this.$body.css('max-height',this.$winWidth - tH - 150+'px');
        // 每次弹出body的滚动条显示在最上方
        this.$body.scrollTop(0);
    }

    Modal.prototype.escape = function () {
        if (this.isShown && this.options.keyboard) {
            this.$element.on('keyup.dismiss.bs.modal', $.proxy(function (e) {
                //按Esc关闭弹出层
                // e.which == 27 && this.hide()
            }, this))
        } else if (!this.isShown) {
            this.$element.off('keyup.dismiss.bs.modal')
        }
    }

    Modal.prototype.hideModal = function () {
        var that = this
        this.$element.hide();

        this.backdrop(function () {
            that.removeBackdrop()
            that.$element.trigger('hidden.bs.modal')
            that.hiddenCallBack()
        })
    }

    Modal.prototype.removeBackdrop = function () {
        this.$backdrop && this.$backdrop.remove()
        this.$backdrop = null;
    }

    Modal.prototype.backdrop = function (callback) {
        var that = this
        var animate = this.$element.hasClass('fade') ? 'fade' : ''
        if (this.isShown && this.options.backdrop) {
            var doAnimate = $.support.transition && animate

            this.$backdrop = $('<div class="modal-backdrop ' + animate + '"/>')
                .appendTo(document.body)

            this.$element.on('click.dismiss.modal', $.proxy(function (e) {
                if (e.target !== e.currentTarget) return
                this.options.backdrop == 'static'
                    ? this.$element[0].focus.call(this.$element[0])
                    : this.hide.call(this)
            }, this))
            if (doAnimate) this.$backdrop[0].offsetWidth // force reflow
            this.$backdrop.addClass('in')
            if (!callback) return
            doAnimate ?
                this.$backdrop
                    .one($.support.transition.end, callback)
                    .emulateTransitionEnd(150) :
                callback()

        } else if (!this.isShown && this.$backdrop) {
            this.$backdrop.removeClass('in')
            $.support.transition && this.$element.hasClass('fade')?
                this.$backdrop
                    .one($.support.transition.end, callback)
                    .emulateTransitionEnd(150) :
                callback()
        } else if (callback) {
            callback()
        }
    }
    // 戈志刚 添加 弹出层层级
    Modal.prototype.addZindex = function(){
        var index = Modal.INDEX + Modal.INTEMNUM * 10;
        this.$element.css('z-index', index);
        this.$backdrop.css('z-index', index - 5);
        Modal.INTEMNUM++;
    }

    Modal.prototype.hiddenCallBack = function(){
        var handler = this.$element.attr('data-handler')
        if(!handler) return false;
        eval(handler);
    }
    // MODAL PLUGIN DEFINITION
    // =======================
    var old = $.fn.modal;

    $.fn.modal = function (option, _relatedTarget) {
        return this.each(function () {
            var $this   = $(this)
            var data    = $this.data('bs.modal')
            var options = $.extend({}, Modal.DEFAULTS, $this.data(), typeof option == 'object' && option)

            if (!data) $this.data('bs.modal', (data = new Modal(this, options)))
            if (typeof option == 'string') data[option](_relatedTarget)
            else if (options.show) data.show(_relatedTarget)
        })
    }

    $.fn.modal.Constructor = Modal
    // MODAL NO CONFLICT
    // =================
    $.fn.modal.noConflict = function () {
        $.fn.modal = old
        return this
    }
    // MODAL DATA-API
    // ==============
    $(document).on('click.bs.modal.data-api', '[data-toggle="modal"]', function (e) {
        var $this   = $(this)
        var href    = $this.attr('href')
        var $target = $($this.attr('data-target') || (href && href.replace(/.*(?=#[^\s]+$)/, ''))) // strip
        // for
        // ie7
        var option  = $target.data('modal') ? 'toggle' : $.extend({ remote: !/#/.test(href) && href }, $target.data(), $this.data())
        e.preventDefault()

        $target.modal(option, this).one('hide', function () {
            $this.is(':visible') && $this.focus()
        })
    })

    $(document)
        .on('show.bs.modal',  '.modal', function () { $(document.body).addClass('modal-open') })
        .on('hidden.bs.modal', '.modal', function () { $(document.body).removeClass('modal-open') })
    //.on('setPaddingTop.bs.modal', '.modal', function () { this.setPaddingTop() })

}(jQuery);



////////////////////////////////////////////////////////////////////原common.js内容
var isIeBrowser = JCF.browser.ie8;
var tokenMessage = "请不要重复提交";
var concurrentMessage = "并发提交出错";

if (typeof(rootPath) == "undefined") {
    var rootPath = "";
}

function setRootPath(path) {
    rootPath = path;
}

function setManageHostPath(path) {
    manageHostPath = path;
}

function getRootPath() {
    return rootPath;
}

function getManageHostPath() {
    if (typeof(manageHostPath) == "undefined" || manageHostPath == '') {
        return getRootPath();
    }
    return manageHostPath;
}

if (typeof(sysTheme) == "undefined") {
    var sysTheme = "0";
}

function setTheme(theme) {
    sysTheme = theme;
}

function getTheme() {
    return sysTheme;
}

function turnVoice(obj) {
    if ($(obj).hasClass("on")) {
        $.cookie('voiceSwitch', 'false');
        $(obj).removeClass("on icon-voice").addClass("off icon-no-voice");
    } else {
        $.cookie('voiceSwitch', 'true');
        $(obj).removeClass("off icon-no-voice").addClass("on icon-voice");
    }
}
//播放声音
function playVoice(obj) {
    if ($.cookie("voiceSwitch") !== 'false') {
        if (JCF.browser.ie) {
            $("#" + obj).html("<embed hidden='true' height='0' width='0' src='" + getRootPath() + "/js/voice/voice.wav'/></embed>");
        } else {
            $("#" + obj).html("<object height='0' width='0' data='" + getRootPath() + "/js/voice/voice.wav'></object>");
        }
    }
}

//生日祝福
function loadbirthday(){
    var url = getRootPath()+"/sys/specialData/birthdayBlessing.action";
    jQuery.ajax({
        url : url,
        type : 'POST',
        data : null,
        dataType : "json",
        success : function(data) {
            if (data) {
                $("#birthdayDiv").fadeIn(1000);
            }
        }
    });
};

/**
 * 提示框相关js
 */
var msgBox = {};
/**
 * 提示框(长信息)
 * opt:content:警告内容
 * 	   type:类型success/fail
 */
msgBox.info = function(opt) {
    if (opt.type == null) {
        opt.type = "fail";
    }
    if (opt.callback != null) {
        jBox.setDefaults({
            defaults: {
                closed: opt.callback
            }
        });
    } else {
        jBox.setDefaults({
            defaults: {
                closed: function() {}
            }
        });
    }
    if (opt.type == "success") {
        jBox.info('', opt.content);
    } else {
        jBox.error('', opt.content, 'S');
    }
    JCF.pie();
}
/**
 * 提示框(短信息)
 * opt:type:类型success/fail
 * 	   content:内容
 */
msgBox.tip = function(opt) {
    if (opt.type == null) {
        opt.type = "fail";
    }
    if (opt.callback != null) {
        jBox.setDefaults({
            defaults: {
                closed: opt.callback
            }
        });
    } else {
        jBox.setDefaults({
            defaults: {
                closed: function() {}
            }
        });
    }
    if (opt.type == "success") {
        jBox.tip(opt.content, '', {
            closed: function() {
                if (opt.callback != null) {
                    opt.callback();
                }
            }
        });
    } else {
        jBox.tip(opt.content, 'error', {
            closed: function() {
                if (opt.callback != null) {
                    opt.callback();
                }
            }
        });
    }
    JCF.pie();
}
/**
 * 确认对话框
 * opt:content:提示内容
 * 	   success:确认时回调函数
 * 	   noback :点击离开时回调函数
 * 	   cancel:取消时回调函数
 * 	   fontSize:B/S
 * 	   buttons:  按钮true/false 样例：{"是":"yes","否":"no","取消":"cancel"};
 *
 *     按钮默认不添加只显示-> 是 否 添加参数后会覆盖，
 *     如需要3个按钮添加值为cancel的按钮名称和noback回调函数
 */
msgBox.confirm = function(opt) {
    //opt.buttons = {"保存并且继续":"yes", "取消":"no"}
    if (opt.buttons == null || opt.buttons == undefined) {
        opt.buttons = {
            "是": "yes",
            "否": "no"
        };
    }
    jBox.setDefaults({
        defaults: {
            closed: function() {},
            buttons: opt.buttons
        }
    });
    var submit = function(v, h, f) {
        switch (v) {
            case 'yes':
                if (opt.success != null) {
                    opt.success();
                }
                break;
            case 'no':
                if (opt.cancel != null) {
                    opt.cancel();
                }
                break;
            case 'cancel':
                if (opt.noback != null) {
                    opt.noback();
                }
                break;
        }

    };
    var fontSize = "";
    if (opt.fontSize == "S" || opt.fontSize == "s") {
        fontSize = "S";
    }
    jBox.warning("", opt.content, submit, fontSize);

    JCF.pie();
};
function promptOnUnLoad(e) {
    var mainWindow = document.getElementById('mainFrame').contentWindow;
    if (typeof mainWindow.pageRedirecting != 'undefined') {
        mainWindow.pageRedirecting(); //名称统一 ，方法内具体实现使用人自行开发
    }
}
//判断页面是否正常退出系统，不是进行提示；----start----
if (window.Event) {
    window.onunload = function(event) {
        return promptOnUnLoad(event);
    };
} else {
    window.onunload = function() {
        return promptOnUnLoad(event);
    };
}

$(document).ready(function(){
    getOnlineCount();
    window.setInterval('getOnlineCount()',180000);
    $(window).resize(function(){
        JCF.leftScroll({
            height : 'auto'
        });
    });
});