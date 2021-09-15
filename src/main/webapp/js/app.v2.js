/*!
 * Bootstrap v3.0.3 (http://getbootstrap.com)
 * Copyright 2013 Twitter, Inc.
 * Licensed under http://www.apache.org/licenses/LICENSE-2.0
 */
if (typeof jQuery === "undefined") { throw new Error("Bootstrap requires jQuery") }
/* ========================================================================
 * Bootstrap: transition.js v3.0.3
 * http://getbootstrap.com/javascript/#transitions
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
    // CSS TRANSITION SUPPORT (Shoutout: http://www.modernizr.com/)
    // ============================================================
    function transitionEnd() {
        var el = document.createElement('bootstrap')

        var transEndEventNames = {
            'WebkitTransition' : 'webkitTransitionEnd'
            , 'MozTransition'    : 'transitionend'
            , 'OTransition'      : 'oTransitionEnd otransitionend'
            , 'transition'       : 'transitionend'
        }

        for (var name in transEndEventNames) {
            if (el.style[name] !== undefined) {
                return { end: transEndEventNames[name] }
            }
        }
    }
    // http://blog.alexmaccaw.com/css-transitions
    $.fn.emulateTransitionEnd = function (duration) {
        var called = false, $el = this
        $(this).one($.support.transition.end, function () { called = true })
        var callback = function () { if (!called) $($el).trigger($.support.transition.end) }
        setTimeout(callback, duration)
        return this
    }

    $(function () {
        $.support.transition = transitionEnd()
    })
}(jQuery);
/* ============================================================
 * bootstrap-collapse.js v2.3.2
 * http://getbootstrap.com/2.3.2/javascript.html#collapse
 * =============================================================
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
 * ============================================================ */
!function ($) {
    "use strict"; // jshint ;_;
    /* COLLAPSE PUBLIC CLASS DEFINITION
     * ================================ */
    var Collapse = function (element, options) {
        this.$element = $(element)
        this.options = $.extend({}, $.fn.collapse.defaults, options)

        if (this.options.parent) {
            this.$parent = $(this.options.parent)
        }
        this.options.toggle && this.toggle()
    }
    Collapse.prototype = {
        constructor: Collapse,
        dimension: function () {
            var hasWidth = this.$element.hasClass('width')
            return hasWidth ? 'width' : 'height'
        },
        show: function () {
            var dimension
                , scroll
                , actives
                , hasData

            if (this.transitioning || this.$element.hasClass('in')) return

            dimension = this.dimension()
            scroll = $.camelCase(['scroll', dimension].join('-'))
            actives = this.$parent && this.$parent.find('> .accordion-group > .in')

            if (actives && actives.length) {
                hasData = actives.data('collapse')
                if (hasData && hasData.transitioning) return
                actives.collapse('hide')
                hasData || actives.data('collapse', null)
            }

            this.$element[dimension](0)
            this.transition('addClass', $.Event('show'), 'shown')
            $.support.transition && this.$element[dimension](this.$element[0][scroll])
        }
        , hide: function () {
            var dimension
            if (this.transitioning || !this.$element.hasClass('in')) return
            dimension = this.dimension()
            this.reset(this.$element[dimension]())
            this.transition('removeClass', $.Event('hide'), 'hidden')
            this.$element[dimension](0)
        }
        , reset: function (size) {
            var dimension = this.dimension()
            this.$element
                .removeClass('collapse')
                [dimension](size || 'auto')
                [0].offsetWidth
            this.$element[size !== null ? 'addClass' : 'removeClass']('collapse')
            return this
        }
        , transition: function (method, startEvent, completeEvent) {
            var that = this
                , complete = function () {
                if (startEvent.type == 'show') that.reset()
                that.transitioning = 0
                that.$element.trigger(completeEvent)
            }
            this.$element.trigger(startEvent)
            if (startEvent.isDefaultPrevented()) return
            this.transitioning = 1
            this.$element[method]('in')
            $.support.transition && this.$element.hasClass('collapse') ?
                this.$element.one($.support.transition.end, complete) :
                complete()
        }
        , toggle: function () {
            this[this.$element.hasClass('in') ? 'hide' : 'show']()
        }
    }
    /* COLLAPSE PLUGIN DEFINITIO
     * ========================== */
    var old = $.fn.collapse
    $.fn.collapse = function (option) {
        return this.each(function () {
            var $this = $(this)
                , data = $this.data('collapse')
                , options = $.extend({}, $.fn.collapse.defaults, $this.data(), typeof option == 'object' && option)
            if (!data) $this.data('collapse', (data = new Collapse(this, options)))
            if (typeof option == 'string') data[option]()
        })
    }

    $.fn.collapse.defaults = {
        toggle: true
    }
    $.fn.collapse.Constructor = Collapse
    /* COLLAPSE NO CONFLICT
     * ==================== */
    $.fn.collapse.noConflict = function () {
        $.fn.collapse = old
        return this
    }
    /* COLLAPSE DATA-API
     * ================= */
    $(document).on('click.collapse.data-api', '[data-toggle=collapse]', function (e) {
        var $this = $(this), href
            , target = $this.attr('data-target')
            || e.preventDefault()
            || (href = $this.attr('href')) && href.replace(/.*(?=#[^\s]+$)/, '') //strip for ie7
            , option = $(target).data('collapse') ? 'toggle' : $this.data()
        $this[$(target).hasClass('in') ? 'addClass' : 'removeClass']('collapsed')
        $(target).collapse(option)
    })

}(window.jQuery);
/**
 * @introduce	常用词插件
 * @author  	gezhigang1005@163.com
 * @date    	2014-10-09 14:33:20
 */
+ function($) {
    "use strict";
    // Phrase CLASS DEFINITION
    // ====================
    var PhraseSwitch = function(element) {
        this.element = $(element)
    }
    PhraseSwitch.prototype.show = function() {
        var $this = this.element;
        var position = 'absolute';
        var zIndex = 888;
        var phr = "#" + $this.attr('name');
        var place = $this.offset() //按钮
        var scroll = $("#scrollable");
        //显示常用词
        $(phr).addClass('g-nav-block');
        //判断是否在弹出层中显示常用词
        if (this.isInFixed($this)) {
            position = 'fixed';
            zIndex = 1888;
            place.top = $this.offset().top;
            place.left = $this.offset().left - $(phr).width();
            var myBody = $this.closest('.modal-body');
            myBody.scroll(function() {
                $("div[type=phrase]").removeClass('g-nav-block');
            });
        } else {
            place.left = place.left - $(phr).width();
            place.top = place.top;
        }

        var scrTop = place.top + scroll.scrollTop();

        if (scroll.outerHeight(true) < place.top + 340) {
            //防止向上弹出被导航遮罩，常用词高度为340px
            place.top = place.top < 340 ? scrTop : (scrTop - 340 + 25);
        }else{
            place.top = scrTop;
        }

        if (place.left < 0) {
            place.left = place.left + 530 + $this.width() + 10;
        }
        $(phr).css({
            position: position,
            "top": place.top,
            "left": place.left,
            "z-index": zIndex
        })
        this.showIframe(place);
    }

    PhraseSwitch.prototype.isInFixed = function(obj) {
        if (obj) {
            var parents = obj.parents();
            var inFixed = false;
            for (var i = 0; i < parents.length; i++) {
                if ($(parents[i]).css('position') == 'fixed') {
                    inFixed = true;
                    break;
                }
            };
            return inFixed;
        } else {
            return false;
        }
    }

    PhraseSwitch.prototype.showIframe = function(obj) {
        $('#__iframeBodyCovere_Common').remove();
        this.setIframe(obj);
    }

    PhraseSwitch.prototype.setIframe = function(offset){
        var iframeBodyCover = document.createElement("iframe");
        iframeBodyCover.id = "__iframeBodyCovere_Common";
        iframeBodyCover.style.backgroundColor= "#000";
        iframeBodyCover.style.cssText = "position:absolute;top:"+offset.top+"px;left:"+offset.left+"px;width:530px;height:336px;filter:alpha(opacity=7);display:none;";
        iframeBodyCover.setAttribute('frameborder', '0', 0);
        iframeBodyCover.src ="javascript:'';";
        document.getElementById('scrollable').appendChild(iframeBodyCover);

        $('#__iframeBodyCovere_Common').show();
    }

    var old = $.fn.phrase

    $.fn.phrase = function(option) {
        return this.each(function() {
            var $this = $(this)
            var data = $this.data('bs.common')
            if (!data) $this.data('bs.common', (data = new PhraseSwitch(this)))
            if (typeof option == 'string') data[option]()
        })
    }

    $.fn.phrase.Constructor = PhraseSwitch

    $.fn.phrase.noConflict = function() {
        $.fn.phrase = old
        return this
    }

    $(document).on('click.bs.common', '[data-toggle="common"]', function(e) {
        $(e.target).phrase('show');
        e.preventDefault()
    })
    /*关闭常用词提示框*/
    $(document).on('click', '[data-toggle="close"]', function(e) {
        $("div[type=phrase]").removeClass('g-nav-block');
        $('#__iframeBodyCovere_Common').remove();
    })
}(jQuery);

/**
 * @全局变量    用于判断浏览器类型
 */
var isIeBrowser  = navigator.userAgent.indexOf("MSIE")>0;
var isIe8Browser = navigator.userAgent.indexOf("MSIE 8.0")>0;

/**
 * @introduce	其他模块
 * @author  	gezhigang1005@163.com
 * @date    	2014-10-09 14:33:20
 */
+ function($) {"use strict";
    var OtherModule = function(element) {
        this.$element = $(element);
        this.headerN = this.$element.closest(".header-nav");
        this.initNav();
    }

    OtherModule.prototype.initNav = function() {
        var uWidths = this.headerN.next().outerWidth(true),
            headliW = this.headerN.find("ul li:first-child").next().outerWidth(),
            dcWidth = $(window.parent.document).width(),
            $header  = this.headerN,
            modules = this.$element,
            liNum = 1,
            ohterWidth = (getTheme()==2)?325:380;

        this.headerN.find("ul li").hide();
        if (dcWidth < 1300) {
            this.headerN.find("li h1").css("font-size", "20px");
            $("#nav").removeClass("aside-md").addClass("aside-smd");
        } else {
            this.headerN.find("li h1").css("font-size", "24px");
            $("#nav").removeClass("aside-smd").addClass("aside-md");
        }

        this.headerN.find("ul li").each(function(e) {
            if ($header.width() + headliW * 2 + uWidths + 5 > dcWidth) {
                !modules.find("ul")[0] && modules.append("<ul></ul>").show();
                $(this).next()[0] && modules.find("ul").width(liNum>4?ohterWidth:headliW*liNum);
                $(this).next()[0] && modules.find("ul").append($(this))&&liNum++;
            }
            $(this).show();
        });
        if(modules.find("ul li").length == 1){
            this.headerN.find("#").before(this.headerN.find("li li"));
        }
        if(!modules.find("ul li")[0]){
            modules.hide().find("ul").remove();
        }
    }

    var orthershow = function (obj){
        var nx = $(obj);
        if(!nx.is(":visible")){
            $(obj).parent().addClass("other");
            nx.show();
            if(isIeBrowser) initIframe(nx.offset().top,nx.offset().left,nx.width(),nx.height());
        }
    }

    var ortherhide = function(obj){
        $(obj).parent().removeClass("other");
        if(isIeBrowser) $("#iframeHeadCovere_e").remove();
        $(obj).hide();
    }

    var initIframe = function (x,y,w,h){
        var ifr = document.createElement("iframe");
        ifr.id = "iframeHeadCovere_e";
        ifr.style.backgroundColor= "transparent";
        ifr.style.cssText = "position:absolute;top:"+x+"px;left:"+y+"px;width:"+w+"px;height:"+h+"px;filter:alpha(opacity=0);";
        ifr.setAttribute('frameborder', '0', 0);
        ifr.src ="javascript:'';";
        document.body.appendChild(ifr);
        $("#iframeHeadCovere_e").show();
    }
    var old = $.fn.other;

    $.fn.other = function(option) {
        return this.each(function() {
            var $this = $(this)
            var data = $this.data('bs.other')
            $this.data('bs.other', (data = new OtherModule(this)))
            if (typeof option == 'string') data[option]()
        })
    }
    $.fn.other.Constructor = OtherModule;

    $.fn.other.noConflict = function() {
        $.fn.other = old;
        return this;
    }

    $(window).resize(function(e){
        var headN = $(".header-nav");
        headN.find("#topnav-other_modules").before(headN.find("li li"));
        var $m = $("#topnav-other_modules");
        $m.other("initNav");
        $m.removeClass("other");
        $m.find("ul").hide();
        if(isIeBrowser) {
            if($("#iframeHeadCovere_e").length) $("#iframeHeadCovere_e").remove();
        }
    });

    $(document).on("mouseenter.bs.other","#topnav-other_modules",function(e){
        orthershow($(this).children().eq(1));
        e && e.preventDefault();
    });

    $(document).on("mouseleave.bs.other","#topnav-other_modules",function(e){
        ortherhide($(this).children().eq(1));
        e && e.preventDefault();
    });
    //点击事件
    /*$(document).on("click.bs.other", "#topnav-other_modules > a", function(e) {
     var nx = $(this).next();
     if(nx.is(":visible")){
     $(this).parent().removeClass("active");
     nx.hide();
     if(isIeBrowser) $("#iframeHeadCovere_e").remove();
     }else{
     $(this).parent().addClass("active");
     nx.show();
     if(isIeBrowser) initIframe(nx.offset().top,nx.offset().left,nx.width(),nx.height());
     }
     e.preventDefault();
     e.stopPropagation();
     });*/
    // 点击关闭其他模块
    //$(document).on("click",hiddenOtherModules);
    //按esc隐藏元素
    //$(document).bind('keyup', function(e){
    //    if(e.which === 27){
    //        ortherhide($(this).children().eq(1));
    //    }
    //});
}(jQuery);
/* ========================================================================
 * Bootstrap: dropdown.js v3.0.3
 * http://getbootstrap.com/javascript/#dropdowns
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
    var backdrop = '.dropdown-backdrop'
    var toggle   = '[data-toggle=dropdown]'
    var Dropdown = function (element) {
        $(element).on('click.bs.dropdown', this.toggle)
    }
    Dropdown.prototype.toggle = function (e) {
        var $this = $(this)

        if ($this.is('.disabled, :disabled')) return

        var $parent  = getParent($this)
        var isActive = $parent.hasClass('open')

        clearMenus()

        if (!isActive) {
            if ('ontouchstart' in document.documentElement && !$parent.closest('.navbar-nav').length) {
                // if mobile we use a backdrop because click events don't delegate
                $('<div class="dropdown-backdrop"/>').insertAfter($(this)).on('click', clearMenus)
            }

            $parent.trigger(e = $.Event('show.bs.dropdown'))

            if (e.isDefaultPrevented()) return

            $parent
                .toggleClass('open')
                .trigger('shown.bs.dropdown')

            var otop = $this.offset().top
            var ttop = $parent.find('.dropdown-menu').height()
            var wtop = window.screen.availHeight
            if(otop+ttop+135 > wtop){$parent.addClass('dropup')}else{$parent.removeClass('dropup')}
            $this.focus()
        }
        return false;
    }

    Dropdown.prototype.keydown = function (e) {
        if (!/(38|40|27)/.test(e.keyCode)) return
        var $this = $(this)
        e.preventDefault()
        e.stopPropagation()

        if ($this.is('.disabled, :disabled')) return

        var $parent  = getParent($this)
        var isActive = $parent.hasClass('open')

        if (!isActive || (isActive && e.keyCode == 27)) {
            if (e.which == 27) $parent.find(toggle).focus()
            return $this.click()
        }

        var $items = $('[role=menu] li:not(.divider):visible a', $parent)

        if (!$items.length) return

        var index = $items.index($items.filter(':focus'))

        if (e.keyCode == 38 && index > 0)                 index--                        // up
        if (e.keyCode == 40 && index < $items.length - 1) index++                        // down
        if (!~index)                                      index=0

        $items.eq(index).focus()
    }

    function clearMenus() {
        $(backdrop).remove()
        $(toggle).each(function (e) {
            var $parent = getParent($(this))
            if (!$parent.hasClass('open')) return
            $parent.trigger(e = $.Event('hide.bs.dropdown'))
            if (e.isDefaultPrevented()) return
            $parent.removeClass('open').trigger('hidden.bs.dropdown')
        })
    }

    function getParent($this) {
        var selector = $this.attr('data-target')

        if (!selector) {
            selector = $this.attr('href')
            selector = selector && /#/.test(selector) && selector.replace(/.*(?=#[^\s]*$)/, '') //strip for ie7
        }

        var $parent = selector && $(selector)

        return $parent && $parent.length ? $parent : $this.parent()
    }
    // DROPDOWN PLUGIN DEFINITION
    // ==========================

    var old = $.fn.dropdown

    $.fn.dropdown = function (option) {
        return this.each(function () {
            var $this = $(this)
            var data  = $this.data('bs.dropdown')

            if (!data) $this.data('bs.dropdown', (data = new Dropdown(this)))
            if (typeof option == 'string') data[option].call($this)
        })
    }

    $.fn.dropdown.Constructor = Dropdown
    // DROPDOWN NO CONFLICT
    // ====================

    $.fn.dropdown.noConflict = function () {
        $.fn.dropdown = old
        return this
    }
    $(document)
        .on('click.bs.dropdown.data-api', clearMenus)
        .on('click.bs.dropdown.data-api', '.dropdown form', function (e) { e.stopPropagation() })
        .on('click.bs.dropdown.data-api'  , toggle, Dropdown.prototype.toggle)
        .on('keydown.bs.dropdown.data-api', toggle + ', [role=menu]' , Dropdown.prototype.keydown)
}(jQuery);
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
        this.options   = options;
        this.$element  = $(element);
        this.lodTime = 0;
        this.timer = null;
        this.$backdrop = null;
        this.isShown   = null;
        this.$winWidth = $(window.parent.window).height() || $(window).height();
        if (this.options.remote) this.$element.load(this.options.remote)
    }
    Modal.DEFAULTS = {
        backdrop: 'static'
        , keyboard: true
        , show: true
    }

    Modal.prototype.toggle = function (_relatedTarget) {
        return this[!this.isShown ? 'show' : 'hide'](_relatedTarget)
    }

    Modal.time = 0;

    Modal.prototype.show = function (_relatedTarget) {
        var time = +new Date();
        if((time - Modal.time > 1000)){
            Modal.time = time ;

            this.$body = this.$element.find('.modal-body');
            this.$content = this.$element.find('.modal-content');

            if(isIeBrowser || $("html").hasClass("ie11")) setIframeInOffice(this.$element.attr('id'),'modal');
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
                closeIframeMenu();
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
            this.timer = setInterval(function() {
                if(!tempHtml){
                    that.$body = that.$element.find('.modal-body');
                }
                if (that.$body.height() != tempHtml) {
                    that.setPaddingTop();
                    tempHtml = that.$body.height();
                }
            }, 500);
        }
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

        clearInterval(this.timer);

        $.support.transition && this.$element.hasClass('fade') ?
            this.$element
                .one($.support.transition.end, $.proxy(this.hideModal, this))
                .emulateTransitionEnd(300) :
            this.hideModal()
        if(isIe8Browser) this.$element.find("#qswk").remove();
        if(isIeBrowser || $("html").hasClass("ie11")) removeIframeInOffice(this.$element.attr('id'));
    }

    Modal.prototype.enforceFocus = function () {
        var that = this;
        $(document).off('focusin.bs.modal');
        var temp_m = $(that.$body.children().not(":hidden").last()).css('margin-bottom','20px');
        if(isIe8Browser) $("<div id='qswk' style='min-height:1px;margin-top:-1px;'></div>").insertAfter(temp_m);
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
        openIframeMenu();

        var  oth  = document.getElementById('topnav-other_modules') || window.parent.document.getElementById('topnav-other_modules');
        setTimeout(function(){if(!$(".modal").hasClass("in")){$(oth).show().other();}},300);
    }

    Modal.prototype.backdrop = function (callback) {
        var dialogId = this.$element.attr('id');
        var that = this
        var animate = this.$element.hasClass('fade') ? 'fade' : ''
        if (this.isShown && this.options.backdrop) {
            var doAnimate = $.support.transition && animate

            this.$backdrop = $('<div class="modal-backdrop ' + animate + '" id="'+dialogId+'-backdrop"/>')
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
    // =======================
    var zindexNumber = 1050
    Modal.prototype.addZindex = function(){
        zindexNumber += 10
        this.$element.css('z-index', zindexNumber)
        var dialog = this.$element.attr('id')
        $("div#"+dialog+"-backdrop").css('z-index',zindexNumber - 5)
    }

    Modal.prototype.hiddenCallBack = function(){
        var back = this.$element.attr('data-back')
        if(back == undefined || back == "") return;
        eval(back);
    }
    // MODAL PLUGIN DEFINITION
    // =======================
    var old = $.fn.modal

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

        $target
            .modal(option, this)
            .one('hide', function () {
                $this.is(':visible') && $this.focus()
            })
    })

    $(document)
        .on('show.bs.modal',  '.modal', function () { $(document.body).addClass('modal-open') })
        .on('hidden.bs.modal', '.modal', function () { $(document.body).removeClass('modal-open') })
        .on('setPaddingTop.bs.modal', '.modal', function () { this.setPaddingTop() })

}(jQuery);
/**
 * jQuery placeholder Plugins
 * version 1.0              2014.09.25戈志刚
 */
(function(h, j, e) {

    var k = e.fn;
    var d = e.valHooks;
    var b = e.propHooks;
    var m;
    var l;

    l = k.placeholder = function() {
        var n = this;
        n.filter((false ? "textarea" : ":input") + "[placeholder]").not(".placeholder").bind({
            "focus.placeholder": c,
            "blur.placeholder": g
        }).data("placeholder-enabled", true).trigger("blur.placeholder");
        return n
    };

    m = {
        get: function(o) {
            var n = e(o);
            var p = n.data("placeholder-password");
            if (p) {
                return p[0].value
            }
            return n.data("placeholder-enabled") && n.hasClass("placeholder") ? "" : o.value
        },
        set: function(o, q) {
            var n = e(o);
            var p = n.data("placeholder-password");
            if (p) {
                return p[0].value = q
            }
            if (!n.data("placeholder-enabled")) {
                return o.value = q
            }
            if (q == "") {
                o.value = q;
                if (o != j.activeElement) {
                    g.call(o)
                }
            } else {
                if (n.hasClass("placeholder")) {
                    c.call(o, true, q) || (o.value = q)
                } else {
                    o.value = q
                }
            }
            return n
        }
    };

    d.input = m;
    b.value = m
    d.textarea = m;

    e(function() {
        e(j).delegate("form", "submit.placeholder", function() {
            var n = e(".placeholder", this).each(c);
            setTimeout(function() {
                n.each(g)
            }, 10)
        })
    });
//  e(h).bind("beforeunload.placeholder", function() {
//    e(".placeholder").each(function() {
//      this.value = ""
//    })
//  })

    function i(o) {
        var n = {};
        var p = /^jQuery\d+$/;
        e.each(o.attributes, function(r, q) {
            if (q.specified && !p.test(q.name)) {
                n[q.name] = q.value
            }
        });
        return n
    }

    function c(o, p) {
        var n = this;
        var q = e(n);
        if (n.value == q.attr("placeholder") && q.hasClass("placeholder")) {
            if (q.data("placeholder-password")) {
                q = q.hide().next().show().attr("id", q.removeAttr("id").data("placeholder-id"));
                if (o === true) {
                    return q[0].value = p
                }
                q.focus()
            } else {
                n.value = "";
                q.removeClass("placeholder");
                //n == j.activeElement && n.select()
            }
        }
    }

    function g() {
        var r;
        var n = this;
        var q = e(n);
        var p = this.id;
        if (n.value == "") {
            if (n.type == "password") {
                if (!q.data("placeholder-textinput")) {
                    try {
                        r = q.clone().attr({
                            type: "text"
                        })
                    } catch (o) {
                        r = e("<input>").attr(e.extend(i(this), {
                            type: "text"
                        }))
                    }
                    r.removeAttr("name").data({
                        "placeholder-password": q,
                        "placeholder-id": p
                    }).bind("focus.placeholder", c);
                    q.data({
                        "placeholder-textinput": r,
                        "placeholder-id": p
                    }).before(r)
                }
                q = q.removeAttr("id").hide().prev().attr("id", p).show()
            }
            q.addClass("placeholder");
            q[0].value = q.attr("placeholder");
        } else if (n.value == q.attr("placeholder") && q.hasClass("placeholder")) {
            return;
        }else{
            q.removeClass("placeholder")
        }
    }
}(this, document, jQuery));;
/* ========================================================================
 * Bootstrap: tooltip.js v3.0.3
 * http://getbootstrap.com/javascript/#tooltip
 * Inspired by the original jQuery.tipsy by Jason Frame
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
    var Tooltip = function (element, options) {
        this.type       =
            this.options    =
                this.enabled    =
                    this.timeout    =
                        this.hoverState =
                            this.$element   = null

        this.init('tooltip', element, options)
    }

    Tooltip.DEFAULTS = {
        animation: true
        , placement: 'top'
        , selector: false
        , template: '<div class="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>'
        , trigger: 'hover focus'
        , title: ''
        , delay: 0
        , html: false
        , container: false
    }

    Tooltip.prototype.init = function (type, element, options) {
        this.enabled  = true
        this.type     = type
        this.$element = $(element)
        this.options  = this.getOptions(options)

        var triggers = this.options.trigger.split(' ')

        for (var i = triggers.length; i--;) {
            var trigger = triggers[i]

            if (trigger == 'click') {
                this.$element.on('click.' + this.type, this.options.selector, $.proxy(this.toggle, this))
            } else if (trigger != 'manual') {
                var eventIn  = trigger == 'hover' ? 'mouseenter' : 'focus'
                var eventOut = trigger == 'hover' ? 'mouseleave' : 'blur'

                this.$element.on(eventIn  + '.' + this.type, this.options.selector, $.proxy(this.enter, this))
                this.$element.on(eventOut + '.' + this.type, this.options.selector, $.proxy(this.leave, this))
                this.$element.on('click' + '.' + this.type, this.options.selector, $.proxy(this.leave, this))
            }
        }

        this.options.selector ?
            (this._options = $.extend({}, this.options, { trigger: 'manual', selector: '' })) :
            this.fixTitle()
    }

    Tooltip.prototype.getDefaults = function () {
        return Tooltip.DEFAULTS
    }

    Tooltip.prototype.getOptions = function (options) {
        options = $.extend({}, this.getDefaults(), this.$element.data(), options)

        if (options.delay && typeof options.delay == 'number') {
            options.delay = {
                show: options.delay
                , hide: options.delay
            }
        }

        return options
    }

    Tooltip.prototype.getDelegateOptions = function () {
        var options  = {}
        var defaults = this.getDefaults()

        this._options && $.each(this._options, function (key, value) {
            if (defaults[key] != value) options[key] = value
        })

        return options
    }

    Tooltip.prototype.enter = function (obj) {
        var self = obj instanceof this.constructor ?
            obj : $(obj.currentTarget)[this.type](this.getDelegateOptions()).data('bs.' + this.type)
        clearTimeout(self.timeout)

        self.hoverState = 'in'

        if (!self.options.delay || !self.options.delay.show) return self.show()

        self.timeout = setTimeout(function () {
            if (self.hoverState == 'in') self.show()
        }, self.options.delay.show)
    }

    Tooltip.prototype.leave = function (obj) {
        var self = obj instanceof this.constructor ?
            obj : $(obj.currentTarget)[this.type](this.getDelegateOptions()).data('bs.' + this.type)
        clearTimeout(self.timeout)
        self.hoverState = 'out'

        if (!self.options.delay || !self.options.delay.hide) return self.hide()

        self.timeout = setTimeout(function () {
            if (self.hoverState == 'out') self.hide()
        }, self.options.delay.hide)
    }

    Tooltip.prototype.show = function () {
        var e = $.Event('show.bs.'+ this.type)

        if (this.hasContent() && this.enabled) {
            this.$element.trigger(e)

            if (e.isDefaultPrevented()) return

            var $tip = this.tip()

            this.setContent()

            if (this.options.animation) $tip.addClass('fade')

            var placement = typeof this.options.placement == 'function' ?
                this.options.placement.call(this, $tip[0], this.$element[0]) :
                this.options.placement

            var autoToken = /\s?auto?\s?/i
            var autoPlace = autoToken.test(placement)
            if (autoPlace) placement = placement.replace(autoToken, '') || 'top'

            $tip
                .detach()
                .css({ top: 0, left: 0, display: 'block' })
                .addClass(placement)

            this.options.container ? $tip.appendTo(this.options.container) : $tip.insertAfter(this.$element)

            var pos          = this.getPosition()
            var actualWidth  = $tip[0].offsetWidth
            var actualHeight = $tip[0].offsetHeight

            if (autoPlace) {
                var $parent = this.$element.parent()

                var orgPlacement = placement
                var docScroll    = document.documentElement.scrollTop || document.body.scrollTop
                var parentWidth  = this.options.container == 'body' ? window.innerWidth  : $parent.outerWidth()
                var parentHeight = this.options.container == 'body' ? window.innerHeight : $parent.outerHeight()
                var parentLeft   = this.options.container == 'body' ? 0 : $parent.offset().left

                placement = placement == 'bottom' && pos.top   + pos.height  + actualHeight - docScroll > parentHeight  ? 'top'    :
                    placement == 'top'    && pos.top   - docScroll   - actualHeight < 0                         ? 'bottom' :
                        placement == 'right'  && pos.right + actualWidth > parentWidth                              ? 'left'   :
                            placement == 'left'   && pos.left  - actualWidth < parentLeft                               ? 'right'  :
                                placement

                $tip
                    .removeClass(orgPlacement)
                    .addClass(placement)
            }

            var calculatedOffset = this.getCalculatedOffset(placement, pos, actualWidth, actualHeight)

            this.applyPlacement(calculatedOffset, placement)
            this.$element.trigger('shown.bs.' + this.type)
        }
    }

    Tooltip.prototype.applyPlacement = function(offset, placement) {
        var replace
        var $tip   = this.tip()
        var width  = $tip[0].offsetWidth
        var height = $tip[0].offsetHeight

        // manually read margins because getBoundingClientRect includes difference
        var marginTop = parseInt($tip.css('margin-top'), 10)
        var marginLeft = parseInt($tip.css('margin-left'), 10)
        var winW = $("#scrollable").width();
        // we must check for NaN for ie 8/9
        if (isNaN(marginTop))  marginTop  = 0
        if (isNaN(marginLeft)) marginLeft = 0

        offset.top  = offset.top  + marginTop
        offset.left = offset.left + marginLeft

        if(this.$element.closest('.input-textarea').length != 0){
            if(winW < offset.left+this.$element.offset().left){
                offset.left = offset.left - width + this.$element.width()+15;
                $tip.addClass('pop-right')
            }
        }

        $tip
            .offset(offset)
            .addClass('in')

        // check to see if placing tip in new offset caused the tip to resize itself
        var actualWidth  = $tip[0].offsetWidth
        var actualHeight = $tip[0].offsetHeight

        if (placement == 'top' && actualHeight != height) {
            replace = true
            offset.top = offset.top + height - actualHeight
        }

        if (/bottom|top/.test(placement)) {
            var delta = 0
            if (offset.left < 0) {
                delta       = offset.left * -2
                offset.left = 0

                $tip.offset(offset)

                actualWidth  = $tip[0].offsetWidth
                actualHeight = $tip[0].offsetHeight
            }

            this.replaceArrow(delta - width + actualWidth, actualWidth, 'left')
        } else {
            this.replaceArrow(actualHeight - height, actualHeight, 'top')
        }

        if (replace) $tip.offset(offset)
    }

    Tooltip.prototype.replaceArrow = function(delta, dimension, position) {
        this.arrow().css(position, delta ? (50 * (1 - delta / dimension) + "%") : '')
    }

    Tooltip.prototype.setContent = function () {
        var $tip  = this.tip()
        var title = this.getTitle()

        $tip.find('.tooltip-inner')[this.options.html ? 'html' : 'text'](title)
        $tip.removeClass('fade in top bottom left right')
    }

    Tooltip.prototype.hide = function () {
        var that = this
        var $tip = this.tip()
        var e    = $.Event('hide.bs.' + this.type)

        function complete() {
            if (that.hoverState != 'in') $tip.detach()
        }

        this.$element.trigger(e)

        if (e.isDefaultPrevented()) return

        $tip.removeClass('in')

        $.support.transition && this.$tip.hasClass('fade') ?
            $tip
                .one($.support.transition.end, complete)
                .emulateTransitionEnd(150) :
            complete()

        this.$element.trigger('hidden.bs.' + this.type)
        return this
    }

    Tooltip.prototype.fixTitle = function () {
        var $e = this.$element
        if ($e.attr('title') || typeof($e.attr('data-original-title')) != 'string') {
            $e.attr('data-original-title', $e.attr('title') || '').attr('title', '')
        }
    }

    Tooltip.prototype.hasContent = function () {
        return this.getTitle()
    }

    Tooltip.prototype.getPosition = function () {
        var el = this.$element[0]
        return $.extend({}, (typeof el.getBoundingClientRect == 'function') ? el.getBoundingClientRect() : {
            width: el.offsetWidth
            , height: el.offsetHeight
        }, this.$element.offset())
    }

    Tooltip.prototype.getCalculatedOffset = function (placement, pos, actualWidth, actualHeight) {
        return placement == 'bottom' ? { top: pos.top + pos.height,   left: pos.left + pos.width / 2 - actualWidth / 2  } :
            placement == 'top'    ? { top: pos.top - actualHeight, left: pos.left + pos.width / 2 - actualWidth / 2  } :
                placement == 'left'   ? { top: pos.top + pos.height / 2 - actualHeight / 2, left: pos.left - actualWidth } :
                    /* placement == 'right' */ { top: pos.top + pos.height / 2 - actualHeight / 2, left: pos.left + pos.width   }

    }

    Tooltip.prototype.getTitle = function () {
        var title
        var $e = this.$element
        var o  = this.options

        title = $e.attr('data-original-title')
            || (typeof o.title == 'function' ? o.title.call($e[0]) :  o.title)

        return title
    }

    Tooltip.prototype.tip = function () {
        return this.$tip = this.$tip || $(this.options.template)
    }

    Tooltip.prototype.arrow = function () {
        return this.$arrow = this.$arrow || this.tip().find('.tooltip-arrow')
    }

    Tooltip.prototype.validate = function () {
        if (!this.$element[0].parentNode) {
            this.hide()
            this.$element = null
            this.options  = null
        }
    }

    Tooltip.prototype.enable = function () {
        this.enabled = true
    }

    Tooltip.prototype.disable = function () {
        this.enabled = false
    }

    Tooltip.prototype.toggleEnabled = function () {
        this.enabled = !this.enabled
    }

    Tooltip.prototype.toggle = function (e) {
        var self = e ? $(e.currentTarget)[this.type](this.getDelegateOptions()).data('bs.' + this.type) : this
        self.tip().hasClass('in') ? self.leave(self) : self.enter(self)
    }

    Tooltip.prototype.destroy = function () {
        this.hide().$element.off('.' + this.type).removeData('bs.' + this.type)
    }
    // TOOLTIP PLUGIN DEFINITION
    // =========================
    var old = $.fn.tooltip

    $.fn.tooltip = function (option) {
        return this.each(function () {
            var $this   = $(this)
            var data    = $this.data('bs.tooltip')
            var options = typeof option == 'object' && option

            if (!data) $this.data('bs.tooltip', (data = new Tooltip(this, options)))
            if (typeof option == 'string') data[option]()
        })
    }

    $.fn.tooltip.Constructor = Tooltip
    // TOOLTIP NO CONFLIC
    // ==================
    $.fn.tooltip.noConflict = function () {
        $.fn.tooltip = old
        return this
    }
}(jQuery);
/**
 * 邮件收件人 tootip提示插件
 * 基于tootip插件扩展
 */
+function ($) { "use strict";
    var PoMail = function (element, options) {
        this.init('pomail', element, options);
        this.show();
    }
    if (!$.fn.tooltip) throw new Error('PoMail requires tooltip.js')
    PoMail.DEFAULTS = $.extend({} , $.fn.tooltip.Constructor.DEFAULTS, {
        html:true
        , placement: 'right'
        , trigger: 'hover'
        , content: ''
        , template: '<div class="popover pomail fade in bottom"><div class="arrow"></div><h3 class="pomail-title"></h3><div class="pomail-content" style="padding:9px 14px;background-color:#f3f3f3;word-break: break-all;word-wrap: break-word;"></div></div>'
    })
    // NOTE: PoMail EXTENDS tooltip.js
    // ================================
    PoMail.prototype = $.extend({}, $.fn.tooltip.Constructor.prototype);

    PoMail.prototype.constructor = PoMail;

    PoMail.prototype.show = function () {
        var e = $.Event('show.bs.'+ this.type);
        if (this.hasContent() && this.enabled) {
            this.$element.trigger(e);

            if (e.isDefaultPrevented()) return;

            var $tip = this.tip();

            this.setContent();
            if (this.options.animation) $tip.addClass('fade');

            var placement = typeof this.options.placement == 'function' ?
                this.options.placement.call(this, $tip[0], this.$element[0]) :
                this.options.placement;

            $tip
                .detach()
                .css({ top: 0, left: 0, display: 'block' })
                .addClass(placement);

            this.$element.append($tip);

            var pos          = this.getPosition();
            var actualWidth  = $tip[0].offsetWidth;
            var actualHeight = $tip[0].offsetHeight;

            var calculatedOffset = this.getCalculatedOffset(placement, pos, actualWidth, actualHeight);

            this.applyPlacement(calculatedOffset, placement);
            this.$element.trigger('shown.bs.' + this.type);
        }
    }

    PoMail.prototype.getDefaults = function () {
        return PoMail.DEFAULTS;
    }

    PoMail.prototype.setContent = function () {
        var that = this;
        var $tip    = this.tip();
        var title   = this.getTitle();
        var content = this.getContent();
        $tip.find('.pomail-title')[this.options.html ? 'html' : 'text'](title)
        $tip.find('.pomail-content')[this.options.html ? 'html' : 'text'](content)

        $tip.removeClass('fade top bottom left right in')
        if (!$tip.find('.pomail-title').html()) $tip.find('.pomail-title').hide();
    }

    PoMail.prototype.hasContent = function () {
        return this.getTitle() || this.getContent();
    }

    PoMail.prototype.getContent = function () {
        var $e = this.$element;
        var o  = this.options;
        return $e.attr('data-content') || (typeof o.content == 'function' ?
                o.content.call($e[0]) :  o.content);
    }

    PoMail.prototype.arrow = function () {
        return this.$arrow = this.$arrow || this.tip().find('.arrow');
    }

    PoMail.prototype.tip = function () {
        if (!this.$tip) this.$tip = $(this.options.template);
        return this.$tip;
    }
    // PoMail PLUGIN DEFINITION
    // =========================
    var old = $.fn.pomail;

    $.fn.pomail = function (option) {
        return this.each(function () {
            var $this   = $(this);
            var data    = $this.data('bs.pomail');
            var options = typeof option == 'object' && option;
            if (!data) $this.data('bs.pomail', (data = new PoMail(this, options)));
            if (typeof option == 'string') data[option]();
        })
    }

    $.fn.pomail.Constructor = PoMail;
    // POPOVER NO CONFLICT
    // ===================
    $.fn.pomail.noConflict = function () {
        $.fn.pomail = old;
        return this;
    }
}(jQuery);
/* ========================================================================
 * Bootstrap: popover.js v3.0.3
 * http://getbootstrap.com/javascript/#popovers
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
    var Popover = function (element, options) {
        this.init('popover', element, options)
    }
    if (!$.fn.tooltip) throw new Error('Popover requires tooltip.js')

    Popover.DEFAULTS = $.extend({} , $.fn.tooltip.Constructor.DEFAULTS, {
        placement: 'right'
        , trigger: 'click'
        , content: ''
        , template: '<div class="popover downarea fade in bottom" style="display:block;"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>'
    })
    // NOTE: POPOVER EXTENDS tooltip.js
    // ================================
    Popover.prototype = $.extend({}, $.fn.tooltip.Constructor.prototype)

    Popover.prototype.constructor = Popover

    Popover.prototype.getDefaults = function () {
        return Popover.DEFAULTS
    }

    Popover.prototype.setContent = function () {
        var $tip    = this.tip()
        var title   = this.getTitle()
        var content = this.getContent()

        $tip.find('.popover-title')[this.options.html ? 'html' : 'text'](title)
        $tip.find('.popover-content')[this.options.html ? 'html' : 'text'](content)

        $tip.removeClass('fade top bottom left right in')

        // IE8 doesn't accept hiding via the `:empty` pseudo selector, we have to do
        // this manually by checking the contents.
        if (!$tip.find('.popover-title').html()) $tip.find('.popover-title').hide()
    }

    Popover.prototype.hasContent = function () {
        return this.getTitle() || this.getContent()
    }

    Popover.prototype.getContent = function () {
        var $e = this.$element
        var o  = this.options

        return $e.attr('data-content')
            || (typeof o.content == 'function' ?
                o.content.call($e[0]) :
                o.content)
    }

    Popover.prototype.arrow = function () {
        return this.$arrow = this.$arrow || this.tip().find('.arrow')
    }

    Popover.prototype.tip = function () {
        if (!this.$tip) this.$tip = $(this.options.template)
        return this.$tip
    }
    // POPOVER PLUGIN DEFINITION
    // =========================
    var old = $.fn.popover

    $.fn.popover = function (option) {
        return this.each(function () {
            var $this   = $(this)
            var data    = $this.data('bs.popover')
            var options = typeof option == 'object' && option

            if (!data) $this.data('bs.popover', (data = new Popover(this, options)))
            if (typeof option == 'string') data[option]()
        })
    }

    $.fn.popover.Constructor = Popover
    // POPOVER NO CONFLICT
    // ===================
    $.fn.popover.noConflict = function () {
        $.fn.popover = old
        return this
    }

}(jQuery);

+function ($) { "use strict";
    var downarea = function (element, options) {
        this.init('downarea', element, options)
    }
    downarea.DEFAULTS = $.extend({} , $.fn.tooltip.Constructor.DEFAULTS, {
        placement: 'bottom'
        , trigger: 'focus '
        , content: '<textarea rows="10" style="width:700px"></textarea>'
        , html:true
        , container:'#scrollable'
        , template: '<div class="popover downarea"><div class="arrow"></div><div class="popover-content"></div></div>'
    })
    downarea.prototype = $.extend({}, $.fn.tooltip.Constructor.prototype)
    downarea.prototype.constructor = downarea
    downarea.prototype.init = function (type, element, options) {
        this.enabled  = true
        this.type     = type
        this.$element = $(element)
        //this.values   = this.$element.val()
        this.options  = this.getOptions(options)

        var triggers = this.options.trigger.split(' ')

        for (var i = triggers.length; i--;) {
            var trigger = triggers[i]
            if (trigger == 'click') {
                this.$element.on('click.' + this.type, this.options.selector, $.proxy(this.toggle, this))
            } else if (trigger != 'manual') {
                var eventIn  = trigger == 'hover' ? 'mouseenter' : 'focus'
                this.$element.on(eventIn  + '.' + this.type, this.options.selector, $.proxy(this.enter, this))
            }
        }
        this.options.selector ?
            (this._options = $.extend({}, this.options, { trigger: 'manual', selector: '' })) :
            this.fixTitle()
    }
    downarea.prototype.getDefaults = function () {
        return downarea.DEFAULTS
    }
    downarea.prototype.setContent = function () {
        var $tip    = this.tip()
        var textareaValue = $tip.find('textarea').val();
        var title   = this.getTitle()
        var content = this.getContent()
        $tip.find('.popover-title')[this.options.html ? 'html' : 'text'](title)
        $tip.find('.popover-content')[this.options.html ? 'html' : 'text'](content)
        //console.log(this.values)

        var descrip_1 = this.$element.html();
        $tip.find('textarea').val(this.br2Enter(descrip_1));
        //$tip.find('textarea').val(this.$element.html())
        $tip.removeClass('fade top bottom left right in')
        if (!$tip.find('.popover-title').html()) $tip.find('.popover-title').hide()
    }
    downarea.prototype.enter2Br = function (str){
        return str.replace(/\n/g,'<br/>').replace(/\s/g,"&nbsp;");
    }
    downarea.prototype.br2Enter = function (str){
        return str.replace(/<br>/ig,"\r\n").replace(/(&nbsp;)/g," ").replace(/(&amp;)/g,"&").replace(/(&lt;)/g,"<").replace(/(&gt;)/g,">");
    }
    downarea.prototype.hasContent = function () {
        return this.getTitle() || this.getContent()
    }
    downarea.prototype.getCalculatedOffset = function (placement, pos, actualWidth, actualHeight) {
        return { top: pos.top + pos.height,   left: pos.left}
    }
    downarea.prototype.getContent = function () {
        var $e = this.$element
        var o  = this.options
        return $e.attr('data-content')
            || (typeof o.content == 'function' ?
                o.content.call($e[0]) :
                o.content)
    }
    downarea.prototype.arrow = function () {
        return this.$arrow = this.$arrow || this.tip().find('.arrow')
    }
    downarea.prototype.tip = function () {
        if (!this.$tip) this.$tip = $(this.options.template)
        return this.$tip
    }
    downarea.prototype.out = function () {
        this.hoverState = 'out'
        this.hide()
    }
    downarea.prototype.focus = function () {
        var $tip = this.tip()
        var $e   = this.$element;
        var $this = this;
        $tip.find("textarea").unbind("blur");
        $tip.find("textarea").focus().on("blur",function(a){
            var descrip = $(a.target).val();
            $(a.target).val($this.enter2Br(descrip));
            if($(a.target).val()== ""){ $e.html("")};
            !!$(a.target).val()&&$e.html($(a.target).val());
            $e.downarea("out")
        });
        $(function(){
            var t=$tip.find('textarea').val();
            $tip.find('textarea').val('').select().val(t);
        })
    }

    var old = $.fn.downarea
    $.fn.downarea = function (option) {
        return this.each(function (e) {
            var $this   = $(this)
            var data    = $this.data('bs.downarea')
            var options = typeof option == 'object' && option
            if (!data) $this.data('bs.downarea', (data = new downarea(this, options)))
            if (typeof option == 'string') data[option]()
        })
    }
    $.fn.downarea.Constructor = downarea
    $.fn.downarea.noConflict = function () {
        $.fn.downarea = old
        return this
    }
}(jQuery);
/**
 * 查询条件显示隐藏功能代码
 */
+function ($) { "use strict";
    $.fn.searchControl = function(b){
        return $.fn.searchControl.defaults = {
            btnShow   : "显示查询条件",
            btnHide   : "收起查询条件",
            btnClik   : ".search-btn-one",
            iconUp    : "fa-caret-up",
            iconDown  : "fa-caret-down",
            container : ".search-line",
            defaultShow:b
        },this.each(function(b){

            var option = $.extend({}, $.fn.searchControl.defaults, b),
                reThis = $(this).find("div.active").length?null:reThis,
                $this  = reThis || $(this);

            var b = function(){
                var child = $(this).children();
                if($(child[0]).hasClass(option.iconDown)){
                    $(child[0]).removeClass(option.iconDown).addClass(option.iconUp);
                    $(child[1]).text(option.btnHide)
                }else{
                    $(child[0]).removeClass(option.iconUp).addClass(option.iconDown);
                    $(child[1]).text(option.btnShow)
                }
                $this.find(option.container).slideToggle(300);
            }

            if(option.defaultShow) {
                var child = $(option.btnClik).children();
                $(child[0]).removeClass(option.iconDown).addClass(option.iconUp);
                $(child[1]).text(option.btnHide);
                $this.find(option.container).show();
            }

            $this.off().on("click",option.btnClik,b);
        });
    }
}(jQuery);
/* ========================================================================
 * Bootstrap: tab.js v3.0.3
 * http://getbootstrap.com/javascript/#tabs
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
    // TAB CLASS DEFINITION
    // ====================
    var Tab = function (element) {
        this.element = $(element)
    }

    Tab.prototype.show = function () {
        var $this    = this.element
        var $ul      = $this.closest('ul:not(.dropdown-menu)')
        var selector = $this.data('target')

        if (!selector) {
            selector = $this.attr('href')
            selector = selector && selector.replace(/.*(?=#[^\s]*$)/, '') //strip for ie7
        }

        if ($this.parent('li').hasClass('active')) return

        var previous = $ul.find('.active:last a')[0]
        var e        = $.Event('show.bs.tab', {
            relatedTarget: previous
        })

        $this.trigger(e)

        if (e.isDefaultPrevented()) return

        var $target = $(selector)

        this.activate($this.parent('li'), $ul)
        this.activate($target, $target.parent(), function () {
            $this.trigger({
                type: 'shown.bs.tab'
                , relatedTarget: previous
            })
        })
    }

    Tab.prototype.activate = function (element, container, callback) {
        var $active    = container.find('> .active')
        var transition = callback
            && $.support.transition
            && $active.hasClass('fade')

        function next() {
            $active
                .removeClass('active')
                .find('> .dropdown-menu > .active')
                .removeClass('active')

            element.addClass('active')

            if (transition) {
                element[0].offsetWidth // reflow for transition
                element.addClass('in')
            } else {
                element.removeClass('fade')
            }

            if (element.parent('.dropdown-menu')) {
                element.closest('li.dropdown').addClass('active')
            }

            callback && callback()
        }

        transition ?
            $active
                .one($.support.transition.end, next)
                .emulateTransitionEnd(150) :
            next()

        $active.removeClass('in')
    }
    // TAB PLUGIN DEFINITION
    // =====================
    var old = $.fn.tab

    $.fn.tab = function ( option ) {
        return this.each(function () {
            var $this = $(this)
            var data  = $this.data('bs.tab')

            if (!data) $this.data('bs.tab', (data = new Tab(this)))
            if (typeof option == 'string') data[option]()
        })
    }

    $.fn.tab.Constructor = Tab
    // TAB NO CONFLICT
    // ===============
    $.fn.tab.noConflict = function () {
        $.fn.tab = old
        return this
    }
    // TAB DATA-API
    // ============
    $(document).on('click.bs.tab.data-api', '[data-toggle="tab"], [data-toggle="pill"]', function (e) {
        $(this).tab('show');
        e.preventDefault();
        e.stopPropagation();
    })
}(jQuery);
/*
 鼠标经过切换事件
 */
+function ($) { "use strict";
    // TAB CLASS DEFINITION
    // ====================
    var TabHover = function (element) {
        this.element = $(element)
    }

    TabHover.prototype.show = function () {
        var $this    = this.element
        var $ul      = $this.closest('ul:not(.dropdown-menu)')
        var selector = $this.data('target')

        if (!selector) {
            selector = $this.attr('href')
            selector = selector && selector.replace(/.*(?=#[^\s]*$)/, '') //strip for ie7
        }

        if ($this.parent('li').hasClass('active')) return

        var previous = $ul.find('.active:last a')[0]
        var e        = $.Event('show.bs.tab', {
            relatedTarget: previous
        })

        $this.trigger(e)

        if (e.isDefaultPrevented()) return

        var $target = $(selector)
        $target.css("display","")
        this.activate($this.parent('li'), $ul)
        this.activate($target, $target.parent(), function () {
            $this.trigger({
                type: 'shown.bs.tab'
                , relatedTarget: previous
            })
        })
    }

    TabHover.prototype.activate = function (element, container, callback) {
        var $active    = container.find('> .active')
        var transition = callback
            && $.support.transition
            && $active.hasClass('fade')

        $active
            .removeClass('active')
            .find('> .dropdown-menu > .active')
            .removeClass('active')

        element.addClass('active')

        if (transition) {
            element[0].offsetWidth // reflow for transition
            element.addClass('in')
        } else {
            element.removeClass('fade')
        }

        if (element.parent('.dropdown-menu')) {
            element.closest('li.dropdown').addClass('active')
        }

        callback && callback()
        $active.removeClass('in')
    }
    // TAB PLUGIN DEFINITION
    // =====================
    var old = $.fn.tabhover

    $.fn.tabhover = function ( option ) {
        return this.each(function () {
            var $this = $(this)
            var data  = $this.data('bs.tab')

            if (!data) $this.data('bs.tab', (data = new TabHover(this)))
            if (typeof option == 'string') data[option]()
        })
    }

    $.fn.tabhover.Constructor = TabHover
    // TAB NO CONFLICT
    // ===============
    $.fn.tabhover.noConflict = function () {
        $.fn.tabhover = old
        return this
    }
    // TAB DATA-API
    // ============
    $(document).on('mouseover.bs.tab.data-api', '[data-toggle="hover"]', function (e) {
        e.preventDefault()
        $(this).tabhover('show')
    })
}(jQuery);
/**在html根节点上添加浏览器class**/
window.Modernizr=function(a,b,c){function w(a){j.cssText=a}function x(a,b){return w(m.join(a+";")+(b||""))}function y(a,b){return typeof a===b}function z(a,b){return!!~(""+a).indexOf(b)}function A(a,b,d){for(var e in a){var f=b[a[e]];if(f!==c)return d===!1?a[e]:y(f,"function")?f.bind(d||b):f}return!1}var d="2.6.2",e={},f=!0,g=b.documentElement,h="modernizr",i=b.createElement(h),j=i.style,k,l={}.toString,m=" -webkit- -moz- -o- -ms- ".split(" "),n={},o={},p={},q=[],r=q.slice,s,t=function(a,c,d,e){var f,i,j,k,l=b.createElement("div"),m=b.body,n=m||b.createElement("body");if(parseInt(d,10))while(d--)j=b.createElement("div"),j.id=e?e[d]:h+(d+1),l.appendChild(j);return f=["&#173;",'<style id="s',h,'">',a,"</style>"].join(""),l.id=h,(m?l:n).innerHTML+=f,n.appendChild(l),m||(n.style.background="",n.style.overflow="hidden",k=g.style.overflow,g.style.overflow="hidden",g.appendChild(n)),i=c(l,a),m?l.parentNode.removeChild(l):(n.parentNode.removeChild(n),g.style.overflow=k),!!i},u={}.hasOwnProperty,v;!y(u,"undefined")&&!y(u.call,"undefined")?v=function(a,b){return u.call(a,b)}:v=function(a,b){return b in a&&y(a.constructor.prototype[b],"undefined")},Function.prototype.bind||(Function.prototype.bind=function(b){var c=this;if(typeof c!="function")throw new TypeError;var d=r.call(arguments,1),e=function(){if(this instanceof e){var a=function(){};a.prototype=c.prototype;var f=new a,g=c.apply(f,d.concat(r.call(arguments)));return Object(g)===g?g:f}return c.apply(b,d.concat(r.call(arguments)))};return e}),n.touch=function(){var c;return"ontouchstart"in a||a.DocumentTouch&&b instanceof DocumentTouch?c=!0:t(["@media (",m.join("touch-enabled),("),h,")","{#modernizr{top:9px;position:absolute}}"].join(""),function(a){c=a.offsetTop===9}),c};for(var B in n)v(n,B)&&(s=B.toLowerCase(),e[s]=n[B](),q.push((e[s]?"":"no-")+s));return e.addTest=function(a,b){if(typeof a=="object")for(var d in a)v(a,d)&&e.addTest(d,a[d]);else{a=a.toLowerCase();if(e[a]!==c)return e;b=typeof b=="function"?b():b,typeof f!="undefined"&&f&&(g.className+=" "+(b?"":"no-")+a),e[a]=b}return e},w(""),i=k=null,e._version=d,e._prefixes=m,e.testStyles=t,g.className=g.className.replace(/(^|\s)no-js(\s|$)/,"$1$2")+(f?" js "+q.join(" "):""),e}(this,this.document);Modernizr.addTest('android',function(){return!!navigator.userAgent.match(/Android/i)});Modernizr.addTest('chrome',function(){return!!navigator.userAgent.match(/Chrome/i)});Modernizr.addTest('firefox',function(){return!!navigator.userAgent.match(/Firefox/i)});Modernizr.addTest('iemobile',function(){return!!navigator.userAgent.match(/IEMobile/i)});Modernizr.addTest('ie',function(){return!!navigator.userAgent.match(/MSIE/i)});Modernizr.addTest('ie10',function(){return!!navigator.userAgent.match(/MSIE 10/i)});Modernizr.addTest('ie11',function(){return!!navigator.userAgent.match(/Trident.*rv:11\./)});Modernizr.addTest('ios',function(){return!!navigator.userAgent.match(/iPhone|iPad|iPod/i)});(function(a,b){"use strict";var c="undefined"!=typeof Element&&"ALLOW_KEYBOARD_INPUT"in Element,d=function(){for(var a,c,d=[["requestFullscreen","exitFullscreen","fullscreenElement","fullscreenEnabled","fullscreenchange","fullscreenerror"],["webkitRequestFullscreen","webkitExitFullscreen","webkitFullscreenElement","webkitFullscreenEnabled","webkitfullscreenchange","webkitfullscreenerror"],["webkitRequestFullScreen","webkitCancelFullScreen","webkitCurrentFullScreenElement","webkitCancelFullScreen","webkitfullscreenchange","webkitfullscreenerror"],["mozRequestFullScreen","mozCancelFullScreen","mozFullScreenElement","mozFullScreenEnabled","mozfullscreenchange","mozfullscreenerror"]],e=0,f=d.length,g={};f>e;e++)if(a=d[e],a&&a[1]in b){for(e=0,c=a.length;c>e;e++)g[d[0][e]]=a[e];return g}return!1}(),e={request:function(a){var e=d.requestFullscreen;a=a||b.documentElement,/5\.1[\.\d]* Safari/.test(navigator.userAgent)?a[e]():a[e](c&&Element.ALLOW_KEYBOARD_INPUT)},exit:function(){b[d.exitFullscreen]()},toggle:function(a){this.isFullscreen?this.exit():this.request(a)},onchange:function(){},onerror:function(){},raw:d};return d?(Object.defineProperties(e,{isFullscreen:{get:function(){return!!b[d.fullscreenElement]}},element:{enumerable:!0,get:function(){return b[d.fullscreenElement]}},enabled:{enumerable:!0,get:function(){return!!b[d.fullscreenEnabled]}}}),b.addEventListener(d.fullscreenchange,function(a){e.onchange.call(e,a)}),b.addEventListener(d.fullscreenerror,function(a){e.onerror.call(e,a)}),a.screenfull=e,void 0):a.screenfull=!1})(window,document);

+function($){"use strict";
    var Shift=function(element){
        this.$element = $(element)
        this.$prev    = this.$element.prev()
        !this.$prev.length&&(this.$parent=this.$element.parent())
    }
    Shift.prototype = {
        constructor:Shift,
        init:function(){
            var $el = this.$element,
                method  = $el.data()['toggle'].split(':')[1],
                $target = $el.data('target')
            $el.hasClass('in')||$el[method]($target).addClass('in')
        },
        reset:function(){
            this.$parent&&this.$parent['prepend'](this.$element)
            !this.$parent&&this.$element['insertAfter'](this.$prev)
            this.$element.removeClass('in')}
    }
    $.fn.shift = function(option){
        return this.each(function(){
            var $this=$(this),
                data=$this.data('shift')
            if(!data)$this.data('shift',(data=new Shift(this)))
            if(typeof option=='string')data[option]()
        })
    }
    $.fn.shift.Constructor=Shift
}(jQuery);

Date.now=Date.now||function(){return+new Date;};



$(document).on('click','.popover-title .close',function(e){
    var $target=$(e.target),
        $popover=$target.closest('.popover').prev();
    $popover&&$popover.popover('hide');
});

$.fn.dropdown.Constructor.prototype.change=function(e){
    e.preventDefault();
    var $item=$(e.target),
        $select,
        $checked=false,
        $menu,
        $label;
    !$item.is('a')&&($item=$item.closest('a'));
    $menu=$item.closest('.dropdown-menu');
    $label=$menu.parent().find('.dropdown-label');
    $labelHolder=$label.text();
    $select=$item.find('input');
    $checked=$select.is(':checked');
    if($select.is(':disabled'))return;
    if($select.attr('type')=='radio'&&$checked)return;
    if($select.attr('type')=='radio')$menu.find('li').removeClass('active');
    $item.parent().removeClass('active');
    !$checked&&$item.parent().addClass('active');
    $select.prop("checked",!$select.prop("checked"));
    $items=$menu.find('li > a > input:checked');
    if($items.length){
        $text=[];
        $items.each(function(){
            var $str=$(this).parent().text();
            $str&&$text.push($.trim($str));
        });
        $text=$text.length<4?$text.join(', '):$text.length+' selected';
        $label.html($text);
    }else{
        $label.html($label.data('placeholder'));
    }
}

$(document).on('click.dropdown-menu','.dropdown-select > li > a',$.fn.dropdown.Constructor.prototype.change);
///////
$(document).on('click','[data-toggle^="class"]',function(e){
    e&&e.preventDefault();
    var $this=$(e.target),$class,$target,$tmp,$classes,$targets;
    !$this.data('tip')&&($this=$this.closest('[data-toggle^="class"]'));
    $class=$this.data()['toggle'];
    $target=$this.data('target')||$this.attr('href');
    $class&&($tmp=$class.split(':')[1])&&($classes=$tmp.split(','));
    $target&&($targets=$target.split(','));
    $targets&&$targets.length&&$.each($targets,function(index,value){(
    $targets[index]!='#')&&$($targets[index]).toggleClass($classes[index]);
    });
    $this.hasClass('active')?($this.find('i').attr("data-original-title","隐藏菜单")):($this.find('i').attr("data-original-title","显示菜单"));
    $this.toggleClass('active');
    $('#nav').hide().show();
});
////////
$(document).on('click','#nav-primary a',function(e){
    var $this=$(e.target),$active,$parent = $this.closest('.nav-primary').hasClass("nav-infomation");
    $this.is('a')||($this=$this.closest('a'));
    if($('.nav-vertical').length){return;}
    $active=$this.parent().siblings(".active");
    $active&&!$parent&&$active.find('> a').toggleClass('active')&&$active.toggleClass('active').find('> ul:visible').slideUp(200);
    ($this.hasClass('active')&&$this.next().slideUp(200))||$this.next().slideDown(200);
    $this.toggleClass('active').parent().toggleClass('active');
    $this.closest(".nav-back").length&&$this.closest('.menus').hasClass('active')&&$this.parent().addClass('active');
    $this.closest(".nav-back").length&&!$this.next().is('ul')&&$this.parent().addClass('active');
    $this.next().is('ul')&&e.preventDefault();
    $('#nav').hide().show();
});
//
$(document).on('mouseenter.nav', '.nav-xs .nav-primary li', function(e) {
    $("#nav-menu-tie").show();
    $("#nav-menu-con").html("");
    var ulhtml = $(this).find("ul");
    if (ulhtml.length) {
        var liNav = ulhtml.clone();
        var offset = $(this).offset();
        $("#nav-menu-con").append("<ul class='nav lt'>" + liNav.html() + "</ul>").css({
            position: "absolute",
            width: "226px",
            background: "#fff",
            height: "auto",
            border: "1px solid #ececec",
            overflow: "visible",
            top: offset.top,
            left: offset.left + 60
        }).show();
        var righth = $("#nav-menu-con").height();
        if (righth + offset.top - 60 > $("#nav").height()) {
            $("#nav-menu-con").css("top", offset.top - righth + 55 + "px");
        }
    } else {
        $("#nav-menu-tie").hide();
    }
    e && e.preventDefault();
});

$(document).on("mouseleave.nav", "#nav-menu-con", function(e) {
    $("#nav-menu-tie").hide();
});

//门户布局图标焦点
$(document).on('click','.layout-tabs a',function(e){
    var $this = $(this);
    var $active = $this.parent().children();
    $active.each(function(){$(this).removeClass('active');});
    $this.addClass('active');
    //解决切换图标焦点后ie8下 选中图标不变色问题
    $this.parent().hide().show();
});

$(document).on('change', 'table thead [type="checkbox"]', function(e) {
    var $table = $(e.target).closest('table'),
        $checked = $(e.target).is(':checked');
    $('tbody [type="checkbox"]', $table).prop('checked', $checked);
});
// 修改dataTable中,主checkbox全选,子checkbox取消选中时,主checkbox无法同时取消选中的bug
// 2014-06-18 ver 1.2 modify by 都业广 start
$(document).on('change', 'table tbody [type="checkbox"]', function(e) {
    // 获得所有tr中checkbox的数量
    var chkNum = $(this).closest('table').find('tbody').find(':checkbox').length;
    // 获得所有被选中checkbox的数量
    var chkCount = $(this).closest('table').find('tbody').find(':checkbox:checked').length;
    // 数量相等时,选中主数据中的checkbox,否则取消
    $(this).closest('table').find('thead').find(':checkbox').prop('checked', chkNum == chkCount);
});
// 2014-06-18 ver 1.2 modify by 都业广 end
$(document).on('click', '[data-toggle^="progress"]', function(e) {
    e && e.preventDefault();
    $el = $(e.target);
    $target = $($el.data('target'));
    $('.progress', $target).each(function() {
        var $max = 50,
            $data, $ps = $('.progress-bar', this).last();
        ($(this).hasClass('progress-xs') || $(this).hasClass('progress-sm')) && ($max = 100);
        $data = Math.floor(Math.random() * $max) + '%';
        $ps.css('width', $data).attr('data-original-title', $data);
    });
});

if ($.fn.select2) {
    $(".select2-tags").select2({
        tags: ["red", "green", "blue"],
        tokenSeparators: [",", " "]
    });
}

(function(){

    $(document).on('change','#SMS',function(e){
        $(e.target).is(':checked')?$("#dxqz").show() : $("#dxqz").hide();
    });
    $(document).on('change','#s-dsfs',function(e){
        $("#dsfs")[$(e.target).is(':checked') ? 'removeAttr' : 'attr']("disabled","disabled")
    });
    $(document).on('focus','.input-textarea tbody td input[type="text"]',function(e){
        $(e.target).hasClass("fzrOnfocus")&&$('#myModal').modal('show');
        $(e.target).hasClass("down-area")&&$(e.target).downarea("show");
        $(e.target).on('shown.bs.downarea', function (e){$(e.target).downarea("focus")})
    });/* 计划编制input获取焦点弹出人员选择树或文本域 */
    $(document).on('focus','.input-textarea tbody td div',function(e){
        $(e.target).hasClass("fzrOnfocus")&&$('#myModal').modal('show');
        $(e.target).hasClass("down-area")&&$(e.target).downarea("show");
        $(e.target).on('shown.bs.downarea', function (e){
            $(e.target).downarea("focus")
        })
    });
    /* 计划编制textarea获取焦点弹出人员选择树或文本域 */
    $(document).on('change', '.remind', function(e) {
        var a = $(e.target).get(0).selectedIndex;
        a != 0 && a == 1 ? $(".border").show().parents().find(".interval").hide().end().find(".circle").hide() : a == 2 ? $(".interval").show().parents().find(".border").hide().end().find(".circle").hide() : $(".circle").show().parents().find(".interval").hide().end().find(".border").hide();
    });
    /* 提醒设置类型显示隐藏 */
    $(document).on('click','.nav-tree-btn',function(e){
        e.stopPropagation();
    });
    $(document).on('click','.abtn',function(e){
        e.preventDefault();
        $(".abtn").parent().removeClass();
        $(e.target).parent().addClass("active");
        $(e.target).hasClass("planning-jy")?$(".planning-jyTbz").hide():$(".planning-jyTbz").show()
    });
    $(document).on('click','.ok',function(e){
        if($("#ok").is(":visible")||$("#ko").is(":visible")) return false;
        var a = "#ok";
        $(a).fadeIn("fast",function(){
            function out(a){
                $(a).fadeOut()
            }
            setTimeout(function(){
                out(a)
            },2000);
        });
    });
    $(document).on('click','.ko',function(e){
        if($("#ok").is(":visible")||$("#ko").is(":visible"))return false;
        var a = "#ko";
        $(a).fadeIn("fast",function(){
            setTimeout(function(){
                out(a)
            },2000);
            function out(a){
                $(a).append("<span id='o-x'>x</span>").find("#o-x").css({"position":"absolute","top":"1px","right":"6px","cursor":"pointer","line-height":"24px"}).click(
                    function(){
                        $(a).fadeOut().find("#o-x").remove()
                    })
            }
        });
    });
    $(document).on('change','input[name="bindType"]',function(e){
        $("#zdip").is(':checked')?$("#ipz").show().next().hide():$("#ipz").hide().next().show();
    });
    $(document).on('change','input[name="hbsx"]',function(e){
        $("#hbsx-ts").is(':checked')?$("#hbsx-rq").hide().prev().show().prev().show():$("#hbsx-rq").show().prev().hide().prev().hide();
    });
    $(document).on('click','#us-append',function(e){
        $(".us-list").is(":visible")?$(".us-list").eq(0).clone().insertAfter($(".us-list").last()):$(".us-list").show();
    });
    $(document).on('click','#us-remove',function(e){
        $(".us-list").length==1?$(".us-list").hide():$(".us-list").last().remove();
    });
    $(document).on('change','#zdsz>table>tbody>tr input[type="checkbox"]',function(e){
        // $(e.target).is(':checked')?
        $(e.target).parent().parent().find("input").not('input[type="checkbox"]')[$(e.target).is(':checked') ? 'removeAttr' : 'attr']("disabled","disabled")
    });
    $(document).on('change','input[name="xb4"]',function(e){
        $("#sfsld").is(':checked')?$(".sfsld").show() : $(".sfsld").hide();
    });
    $(document).on('click','#radio-gly',function(e){
        $("#sfsld").is(':checked')?$(".sfsld").show() : $(".sfsld").hide();
    });
    $(document).on('click','.email-menu li a',function(e){
        !$(e.target).hasClass("write-email")&&$(".email-menu li a").not(".write-email").removeClass("on")&&$(e.target).addClass("on");
    });
    $(document).on('blur','.folder-input',function(e){
        $(".folder-checkbox")[$(e.target).val() ? 'removeAttr' : 'attr']("disabled","disabled");
        !$(".folder-checkbox").is(':disabled')&&$(".folder-checkbox").is(':checked')?$(".folder-user").show():$(".folder-user").hide()
    });
    $(document).on('change','.folder-checkbox',function(e){
        !$(e.target).is(':checked')?$(".folder-user").hide():$(".folder-user").show()
    });
}());
// ---互动交流,公文管理,列表滑过变色star---
function ListTable(){
    $(".list-table tbody tr").mousemove(function(){
        $(this).find("td").css("background","#f9f8f5");
    })
    $(".list-table tbody tr").mouseout(function(){
        $(this).find("td").css("background","#fff");
    })
}
// ---互动交流,公文管理,列表滑过变色end---
// IE8相关补丁函数
// IE8 label表单左对齐，及加粗，页面加载完成后调用
function ie8StylePatch(){
    if(isIeBrowser) {
        if(isIe8Browser) {
            $(".form-btn .btn").each(function(e){
                $(this).text().length>5?$(this).css("padding","4px 12px"):$(this).css("padding","4px 0");
            })
            $(".email-btn.form-btn .btn").each(function(e){
                $(this).text().length>5?$(this).css("padding","4px 12px"):$(this).css("padding","4px 0");
            })
            $(function(){
                $(".btn.w0").css("padding","4px 12px")
            })
            try{
                //ie8TableStyle();
                $("table.table-td-striped>tbody>tr>td:even").css({"text-align":"right","background-color":((getTheme()==0)?"#f9f8f5":"#fcfcfc"),"font-weight":((getTheme()==2)?"normal":"bold")});
                $(".formTable  tr:even").css("background-color","#f9f8f4");
            }catch (e){}
        };
    };
}
// ie8 表格隔行变色，分页查询结束后调用
function ie8TableStyle(){
//	if(isIeBrowser) {
//		if(isIe8Browser) {
//			$("table.table-striped tr:even td").css("background-color","#fdfcfb");
//        }
//  }
}
// ie8水印功能
function iePlaceholderPatch(){
    $('input[placeholder], textarea[placeholder]').placeholder();
}
/* 查询条件隐藏显示 */
function searchConditionControl(){
    $(".search-shrinkage").searchControl();
}
// ie8圆角
function ie8InRounded(){
    if(isIe8Browser) {
        if (window.PIE) {
            $('.rounded').each(function() {
                PIE.attach(this);
            });
        }
    }
}
// 在页面上追加个iframe ie下WebOffice遮挡alert和modal问题解决
function setIframeInOffice(id,type){
    var iframeBodyCover = document.createElement("iframe");
    iframeBodyCover.id = "__iframeBodyCovere"+id;
    iframeBodyCover.style.backgroundColor= "transparent";
    iframeBodyCover.style.cssText = "position:absolute;top:0;left:0;width:100%;height:100%;filter:alpha(opacity=0);"+((type=="modal")?"":"z-index:8888;")+"display:none";
    iframeBodyCover.setAttribute('frameborder', '0', 0);
    iframeBodyCover.src ="javascript:'';";
    document.body.appendChild(iframeBodyCover);
    $('#__iframeBodyCovere'+id).show();

}
// 清除追加的iframe
function removeIframeInOffice(id){
    if($('#__iframeBodyCovere'+id)) $('#__iframeBodyCovere'+id).remove();
}

function closeIframeMenu(){
    menuSwitch(false);
}

function openIframeMenu(){
    if($(".modal").hasClass("in"))return;
    if($(".jbox-body").length)return;
    menuSwitch(true);
}

function menuSwitch(isOpen){
    var pDom = $(parent.document);
    var pHead= pDom.find("#desktop_header");
    var pNav = pDom.find("#nav");
    var pJame=pDom.find("#jcGOA-menu");
    var toph = JCFF.version3() ? 80 : 60;
    var ztoph=toph-120;
    isOpen?(pHead.show(),pNav.show(),pJame.css('top',ztoph+'px'),pHead.next().css('top',toph+'px')):(pHead.hide(),pNav.hide(),pHead.next().css('top','0'));
}

function hiddenOtherModules(){
    $("#nav-menu-tie").hide();
    var otherModules = document.getElementById('topnav-other_modules') || window.parent.document.getElementById('topnav-other_modules');
    var dropdownMenu = window.parent.document.getElementById('dropdownMenu');
    if(dropdownMenu) $(dropdownMenu).removeClass('open');
    if($(otherModules).find("ul").is(":hidden"))return;
    $(otherModules).removeClass("other");
    $(otherModules).find("ul").hide();
    if(isIeBrowser) $("#iframeHeadCovere_e").remove();
}

$(function() {
    $("[data-toggle=tooltip]").tooltip();
    $("[data-toggle=popover]").popover();
    if($.fn.datepicker) $(".datepicker-input").datepicker();

    $(".dataTable").length > 0 && $(".dataTable").each(function () {
        if (!$(this).hasClass("dataTable-custom")) {
            var a = {
                sPaginationType: "full_numbers",
                oLanguage: {
                    sSearch: "<span>Search:</span> ",
                    sInfo: "Showing <span>_START_</span> to <span>_END_</span> of <span>_TOTAL_</span> entries",
                    sLengthMenu: "_MENU_ <span>entries per page</span>"
                },
                sDom: "lfrtip"
            };
            if ($(this).hasClass("dataTable-noheader") && (a.bFilter = !1, a.bLengthChange = !1), $(this).hasClass("dataTable-nofooter") && (a.bInfo = !1, a.bPaginate = !1), $(this).hasClass("dataTable-nosort")) {
                var e = $(this).attr("data-nosort");
                if (e) {
                    e = e.split(",");
                    for (var t = 0; t < e.length; t++) e[t] = parseInt(e[t]);
                    a.aoColumnDefs = [{
                        bSortable: !1,
                        aTargets: e
                    }]
                } else {
                    a.bSort = !1
                }
            }
            $(this).hasClass("dataTable-scroll-x") && (a.sScrollX = "100%", a.bScrollCollapse = !0, $(window).resize(function () {
                s.fnAdjustColumnSizing()
            })),
            $(this).hasClass("dataTable-scroll-y") && (a.sScrollY = "300px", a.bPaginate = !1, a.bScrollCollapse = !0, $(window).resize(function () {
                s.fnAdjustColumnSizing()
            })),
            $(this).hasClass("dataTable-reorder") && (a.sDom = "R" + a.sDom),
            $(this).hasClass("dataTable-colvis") && (a.sDom = "C" + a.sDom, a.oColVis = {
                buttonText: "Change columns <i class='fa fa-angle-down'></i>"
            }),
            $(this).hasClass("dataTable-tools") && (a.sDom = "T" + a.sDom, a.oTableTools = {
                sSwfPath: "js/plugins/datatable/swf/copy_csv_xls_pdf.swf"
            }),
            $(this).hasClass("dataTable-scroller") && (a.sScrollY = "300px", a.bDeferRender = !0, a.sDom = $(this).hasClass("dataTable-tools") ? "TfrtiS" : "frtiS", a.sAjaxSource = "js/plugins/datatable/demo.txt"),
            $(this).hasClass("dataTable-grouping") && "expandable" == $(this).attr("data-grouping") && (a.bLengthChange = !1, a.bPaginate = !1);
            var s = $(this).dataTable(a);

            s.fnDraw(),
                s.fnAdjustColumnSizing()
        }
    });
    TabNub = 10;

    //首页设置文字超出显示 ...
    $(".over-hide").each(function () {
        $(this).css("max-width", $(this).width()).css({
            "white-space": "nowrap",
            "text-overflow": "ellipsis",
            "overflow": "hidden"
        });
    })

    // 日志管理6.6戈志刚
    $(document).on('click', '.tree-right h2', function (e) {
        var $this = $(e.target), $active;
        $this.is('a') || ($this = $this.closest('a'));
        if ($('.nav-vertical').length) {
            return;
        }
        $active = $this.parent().siblings(".active");

        ($this.hasClass('active') && $this.next().animate({
            height: 'toggle',
            opacity: 'toggle'
        }, 200)) || $this.next().animate({height: 'toggle', opacity: 'toggle'}, 200);
        $this.toggleClass('active').parent().toggleClass('active');
    });

    /* 收件人 */
    $(document).on('click', '.email-pucker', function (e) {
        var href = $(this).attr('href')
        var body = $(href).find('.modal-content')
        var email = $(href).find('.modal-footer>span>a')
        email.text('显示全部')
        body.addClass('email-unfold')
    })
    // 邮箱收件人弹出层
    $(document).on('click', '.modal-footer .email-f-c', function (e) {
        var body = $(e.target).closest('.modal-content');
        var that = body.closest(".modal");
        if (body.hasClass('email-unfold')) {
            $(e.target).text('收起')
            body.removeClass('email-unfold')
            $(body.children()[1]).attr('max-height', '500px')
        } else {
            $(e.target).text('显示全部')
            body.addClass('email-unfold')
            $(body.children()[1]).scrollTop(0)
        }
        that.modal("setPaddingTop");
    });
});
/**
 * 奕迅前端  Frame
 */
var JCFF = {};

JCFF.getTop = function(){
    if(window.name === 'mainFrame'){
        return window.top;
    }
    return window;
}

JCFF.loadHome = function(){
    var frame = JCFF.getTop();
    if(frame.JCF){
        frame.JCF.histObj.reload();
    }
}

JCFF.version3 = function(){
    var temp = JCFF.getTop().JCF;
    return temp ? temp.version === '3.0' : false;
}

JCFF.loadPage = function(option){
    JCFF.getTop().JCF.LoadPage(JCFF.addSubsystemId(option));
}

JCFF.addSubsystemId = function(option){
    var url = option.url;
    if(url.indexOf('?sys=')<0 && url.indexOf('&sys=')<0) {
        if (url.indexOf('?') > -1) {
            url = url + '&sys=' + subsystemId;
        } else {
            url = url + '?sys=' + subsystemId;
        }
        option.url = url;
    }
    return option;
}


JCFF.$document = $(document);

 //页面筛选栏table宽度版画
// $(document).ready(function () {
//     $("#searchForm").resize(function () {
//         var  Sfw=parseInt($("#searchForm").width());
//         var  Tde=parseInt(Sfw-185)+"px";
//         $(".btn-tiwc").css("width","155px !important");
//         $("#searchForm table").css("width",Tde);
//     })
//
// })


