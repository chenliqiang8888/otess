(function ($) {
    var oDrag = null;
    $.scale = function (params) {
        params = params || {};

        //创建水平，垂直标尺
        $("<div id='rh' class='zxxScaleRuler_h'/>").appendTo("body");
        $("<div id='rv' class='zxxScaleRuler_v'/>").appendTo("body");
        //$("<input id='currentline' type='hidden'/>").appendTo("body");
        var rh = $("#rh"), rv = $("#rv"), line = $("#sel_dataindex"), dragFlag = false;
        var w,h;
        var f = {
            
            ui: function () {
                w= $(window).width(), h = $(window).height()
                rh.width(w);
                rv.height(h);
                rh.html("");
                rv.html("");
                //创建标尺数值
                for (var i = 0; i < w; i += 1) {
                    if (i % 50 === 0) {
                        rh.append($('<span class="n">' + i + '</span>').css("left", i + 2));
                    }
                }
                //垂直标尺数值
                for (var i = 0; i < h; i += 1) {
                    if (i % 50 === 0) {
                        rv.append($('<span class="n">' + i + '</span>').css("top", i + 2));
                    }
                }
            },
            ie6: function(){
                if(!window.XMLHttpRequest){
                    $(window).scroll(function(){
                        var t = $(document).scrollTop();
                        x.css("top", t);			
                    });	
                    if(flag){
                        $(window).trigger("scroll");
                    }
                }
            },
            axisv: function (t) {//创建新的垂直参考线，有效宽度3像素
                var id = "zxxRefLineV" + ($(".zxxRefLine_v").size() + 1);
                $('<div class="zxxRefLine_v"></div>')
					.appendTo("body")
					.attr({
					    "id": id,
					    "title": t
					}).css("left", rv.width() + 2)
                    .draggable({ axis: "x", handles: "ew", containment: $("body") })
                    .on("mousedown", function () {
                        line.val(id);
                    });
                return id;
            },
            axish: function (t) {//创建新的垂直参考线，有效宽度3像素
                var id = "zxxRefLineH" + ($(".zxxRefLine_h").size() + 1);
                $('<div class="zxxRefLine_h"></div>')
					.appendTo("body")
					.attr({
					    "id": id,
					    "title": t
					}).css("top", rh.height() + 2).draggable({ axis: "y", handles: "ew", containment: $("body") })
                    .on("mousedown", function () {
                        line.val(id);
					});
                return id;
            },
            clearline:function(){
                $("#"+line.val()).remove();
            },
            init: function () {
                f.ui();
                f.ie6();
            }

        }
        f.init();
        /*浏览器拉伸时，标尺自适应*/
        $(window).resize(function () {
            f.init();
        });
        //鼠标拖动创建水平参考线
        rh.on("mousedown", function () {
            line.val(f.axish());
        })
        
        //鼠标拖动创建垂直参考线
        rv.on("mousedown", function (e) {
            line.val(f.axisv(e.pageX));
        })
        
    }
    //快捷键参数
    var k = {
        "r": 82, //字母R
        "esc": 27,
        "clear": 68,//删除参考线
        "movetoright":39,
        "movetoleft": 37,
        "movetotop": 38,//键盘上移
        "movetobottom": 40,//键盘向下移动
    };    
    $(document).keydown(function (e) {
        var line = $("#sel_dataindex");
        if (e.keyCode == k["r"] || e.keyCode == k["esc"]) {
            $.scaleToggle();
        }
        else if (e.keyCode == k["clear"]) {
            $("#" + line.val()).remove();
        }
        else if (e.keyCode == k["movetoright"] || e.keyCode == k["movetoleft"]) {
            $.moveVline({keycode:e.keyCode,line:line});
        }
        else if (e.keyCode == k["movetobottom"] || e.keyCode == k["movetotop"]) {
            $.moveHline({ keycode: e.keyCode, line: line });
        }
    });
    $.scaleToggle = function (params) {
        if ($("#rh").css("display") == "block" && $("#rv").css("display") == "block") {
            $("#rh,#rv,.zxxRefLine_h,.zxxRefLine_v,#currentline").remove();
        } else {
            $.scale(params);
        }
    }
    $.moveVline = function (params) {
        if (params.line.val().indexOf("LineV") >= 0) {
            var $left = parseInt($("#" + params.line.val()).offset().left);
            if (params.keycode == k["movetoright"])
                $left = $left + 1;
            else
                $left = $left - 1;
            $("#" + params.line.val()).css("left", $left );
        }
    }
    $.moveHline = function (params) {
        if (params.line.val().indexOf("LineH") >= 0) {
            var $top = parseInt($("#" + params.line.val()).offset().top);
            if (params.keycode == k["movetobottom"])
                $top = $top + 1;
            else
                $top = $top - 1;
            $("#" + params.line.val()).css("top", $top);
            
        }
    }
})(jQuery)
