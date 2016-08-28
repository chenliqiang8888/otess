
function Screen(options){
    this.options=options;
    this._left=this.options.left;
    this._right=this.options.right;
	this._task=this.options.task;
    this._file=this.options.task.file;
    this._item=this.options.task.item;
    this._ajax=this.options.ajax;
    this._width=0;
    this._height=0;
}
Screen.prototype.build=function(el){
	var left=this._left;
	var right=this._right;
	var task=this._task;
	/*左侧布局*/
	var text=$("<div/>").addClass("coordinate").css({border: "1px dashed #fff"}).height(el.height).attr("data-info", el.info).html(el.info);
	var div= $("<div/>").addClass("tada").attr("data-index",el.index)
			.attr("data-isempty","yes")
			.attr("data-info",el.info)
			.attr("id",el.id).append(text)
	 	.css({left: el.left+"px", top: el.top+"px", zIndex: el.index, position: "absolute"
	 		,overflow:"hidden"})
	 	.width(el.width).height(el.height);
	
	if(el.index>0){		
		
		div.css({cursor: "move"});
		div.draggable({containment: this.options.left, scroll: false, snap: false, opacity: 0.85,stop: function (event, ui) {
	        	var attr={
	        			left:parseInt(ui.offset.left),top:parseInt(ui.offset.top),width:parseInt($(this).width()),
	        			height:parseInt($(this).height())
	        		}
	        	attr.info=$(this).attr("id") + " (" + attr.left + "," + attr.top + "," + attr.width + "," + attr.height + ")"; 
	        	
	        	$(this).attr("data-info",attr.info);
	        	if($(this).data("isempty")==="yes" && $(this).data("isfile")!=="yes"){
	        		$(this).find(".coordinate").attr("data-info",attr.info).text(attr.info)
	        	}
	        		
	    	}
	 	}).resizable({
	        containment: this.options.left, handles: "all", minHeight: 15, minWidth: 15, autoHide: true,
	        stop: function (event, ui) {
	        	//if ($("[name='sel_dataindex']").val() != $(this).attr("dataindex")) return false;
                /*$left = parseInt(ui.position.left);
                $top = parseInt(ui.position.top);
                $width = parseInt(ui.size.width);
                $height = parseInt(ui.size.height);

                $text = $(this).attr("id") + " (" + $left + "," + $top + "," + $width + "," + $height + ")";
                a = $(this).attr("datascreen");
                b = $(this).find(".coordinate").css({ width: $width + "px", height: $height + "px" }).text();
                $(this).find(".coordinate").find("div").css({ width: $width + "px", height: $height + "px" });
                if ($.trim(a) == $.trim(b)) {
                    $(this).attr({ datascreen: $text }).find(".coordinate").text($text);
                }*/
	            var attr={
	            		left:parseInt(ui.position.left),top:parseInt(ui.position.top),
	            		width:parseInt(ui.size.width),height:parseInt(ui.size.height)
	        		};
	            attr.info=$(this).attr("id") + " (" + attr.left + "," + attr.top + "," + attr.width + "," + attr.height + ")"; 
	            $(this).attr("data-info",attr.info);
	            //$(this).css({ width: attr.width + "px", height: attr.height + "px" });
	            var coordinate=$(this).find(".coordinate")
	            coordinate.attr("data-info",attr.info).css({ width: attr.width + "px", height: attr.height + "px" });        	
	        	if($(this).data("isempty")==="yes" && $(this).data("isfile")!=="yes")coordinate.text(attr.info)
	        }
	    })
	}else{
		//div.css({ border: "1px dashed #fff"});
	}
	div.on("click",function(){
    	$(document).find("div[data-index]").css({zIndex:1,backgroundColor:"#2f3e8e"}).attr("data-selected",false);
    	$(document).find("div[data-index='" + $(this).data("index")+ "']").attr("data-selected",true).css({ zIndex: 3, backgroundColor: "#4285e2" });
    	task.selected=$(this).data("index");
    });
	left.append(div);
	
	 /*右侧布局列表 */
    var edit = $("<span/>").addClass("editscreen glyphicon glyphicon-pencil").attr({title:"编辑布局"}).attr("data-index", el.index);
    var transh = $("<span/>").addClass("delscreen glyphicon glyphicon-trash").attr({ title: "删除布局" }).attr("data-index", el.index);
    var info = $("<span/>").text(el.id);
    var item = $("<div/>").addClass("item").attr("data-index", el.index)
    	.append("<span class='glyphicon glyphicon-minus'></span>").append(info)
    	.on("click",function(){
    		$(document).find("div[data-index]").css({zIndex:1,backgroundColor:"#2f3e8e"}).attr("data-selected",false);
        	$(document).find("div[data-index='" + $(this).data("index")+ "']").attr("data-selected",true).css({ zIndex: 3, backgroundColor: "#4285e2" });
        	task.selected=$(this).data("index");
    	});
    if(el.index>0)item.append(edit).append(transh);
    //布局文件容器
    itemchild = $("<div/>").addClass("itemchild").attr("data-itemindex",el.index);
    
    right.append(item).append(itemchild);
}
Screen.prototype.create=function(){
	var param = this.options.task.item.ti_screen_type_affix.split(','),nCount = parseInt(param[0]),icount = 1,firstWidth = 0, firstHeight = 0,j = 0;
	
    for (var i = icount; i <= nCount * 5; i += 5) {
    	var el = {
    			left: param[i],top: param[i + 1],
    			width: param[i + 2],height: param[i + 3],
    			id: param[i + 4],index:param[i + 4].replace("default","")};
    	el.info = el.id + " (" + el.left + "," + el.top + "," + el.width + "," + el.height + ")";
    	if(i==1){
    		this.firstWidth=param[i + 2],firstHeight=param[i + 3];
    		this._width=param[i + 2],this._height=param[i + 3];
    	}
    	this.build(el);
    }
}
Screen.prototype.getWidth=function(){
	return this._width;
}
Screen.prototype.getHeight=function(){
	return this._height;
}
//新增布局
Screen.prototype.add=function(){
	var index=[];
	this._left.children().each(function(n){
		index.push($(this).data("index"));
	});
	var el={width:300,height:200,index:_.max(index)+1};
	el.left=Number((this._left.width()-el.width)/2).toFixed(0);
	el.top=Number((this._left.height()-el.height)/2).toFixed(0);
	el.id="default"+el.index;
	el.info=el.id+" (" + el.left + "," + el.top + "," + el.width + "," + el.height + ")";
	
    this.build(el);
    $(document).find("div[data-index]").css({zIndex:1,backgroundColor:"#2f3e8e"}).attr("data-selected",false);
	$(document).find("div[data-index='" + el.index+ "']").attr("data-selected",true).css({ zIndex: 3, backgroundColor: "#4285e2" });
	this._task.selected=el.index;
}
Screen.prototype.remove=function(tf_position){
	var datainfo=this.options.left.find("#default"+tf_position).data("info");
	this.options.left.find("#default"+tf_position).attr("data-isempty","yes")
	.find(".coordinate").html(datainfo);
}
/**
 * 获取用户编辑的布局
 * @returns
 */
Screen.prototype.toString=function(){
	var size = this._left.children().length;
	this.options.left.children().each(function () {
        var attr={
        		left:$(this).offset().left,top:$(this).offset().top,width:$(this).outerWidth(),height:$(this).outerHeight()
        }
        attr.info = attr.left + "," + attr.top + "," + attr.width + "," + attr.height + "," + $(this).attr("id");
        size += "," + attr.info;
    })
    return size;
}
/**
 * 获取客户端的布局
 * @returns
 */
Screen.prototype.targetToString=function(){
    var scale = (Number(task.field.mr_w) / Number(task.field.web_mr_w)).toFixed(2);
    var s = this.toString().split(',');
    var nCount = parseInt(s[0]);
    var icount = 1;
    var result = "";
    var left = "", top = "", width = "", height = "";
    for (var i = icount; i <= nCount * 5; i += 5) {
    	if(i===1){//第一个布局与分辨率保持一致，一面布局任务项有误差，后续的会有误差
            s.splice(i + 2, 1, task.field.mr_w);
            s.splice(i + 3, 1, task.field.mr_h);
    	}else{
            s.splice(i, 1, Math.round(scale * parseFloat(s[i])));
            s.splice(i + 1, 1, Math.round(scale * parseFloat(s[i + 1])));
            s.splice(i + 2, 1, Math.round(scale * parseFloat(s[i + 2])));
            s.splice(i + 3, 1, Math.round(scale * parseFloat(s[i + 3])));
    	}
    }
    return s.toString();
}
/**
 * 缩放比例
 * @returns
 */
Screen.prototype.scale = function(){
	return (Number(task.field.mr_w) / Number(task.field.web_mr_w)).toFixed(2);;
} 
Screen.prototype.bindFile=function(file){
	var arg=this;
	$(document).find(".itemchild").each(function(i){
		var dom=$(this).empty();
		var index=dom.data("itemindex");
		
		_.where(file,{"tf_position":index}).forEach(function(node,no){
			
			var div=$("<div/>").attr("data-fileid",node.tf_id).attr("id",node.tf_id).attr("data-itemindex",index);
			console.log(node);
			var icon=$("<span/>");
			var text="";
			switch(node.tf_type){
				case -8:
					icon.addClass("glyphicon glyphicon-time");
					text="时间";
					break;
				case -9:
					icon.addClass("glyphicon glyphicon-asterisk");
					text="天气";
					break;
				case -10:
					icon.addClass("glyphicon glyphicon-cloud");
					text="数据库";
					break;
				case -5:
					icon.addClass("glyphicon glyphicon-cloud");
					text="链接";
					break;
				case 1:
					icon.addClass("glyphicon glyphicon-file");
					text=node.m_name;
				case -12:
					icon.addClass("glyphicon glyphicon-file");
					text=node.m_name;
					break;
				case 6:
					icon.addClass("glyphicon glyphicon-picture");
					console.log(node.m_file);
					text=node.m_file;
					break;
				case 3:
					icon.addClass("glyphicon glyphicon-picture");
					text=node.m_file;
					break;
				case -4:
					icon.addClass("glyphicon glyphicon-facetime-video");
					text="直播";
					break;
				case -11:
					icon.addClass("glyphicon glyphicon-usd");
					text="汇率";
					break;
				case 5:
					icon.addClass("glyphicon glyphicon-facetime-video");
					text=node.m_name;
					break;
				case 4:
					icon.addClass("glyphicon glyphicon-music");
					text=node.m_name;
					break;
			}
			var edit=$("<span/>").hide().addClass("editfile glyphicon glyphicon-pencil")
				.attr("data-fileid",node.tf_id)
				.attr("data-type",node.tf_type)
				.attr("data-itemindex",index);
			var del=$("<span/>").hide().addClass("deletefile glyphicon glyphicon-trash")
				.attr("data-fileid",node.tf_id)
				.attr("data-type",node.tf_type)
				.attr("data-itemindex",index)
				.attr('data-json',JSON.stringify(node));
			//修改的时候，自动选中对应布局
			edit.on("click",function(){
				$(document).find("div[data-index]").css({zIndex:1,backgroundColor:"#2f3e8e"}).attr("data-selected",false);
		    	$(document).find("div[data-index='" + $(this).data("itemindex")+ "']").attr("data-selected",true).css({ zIndex: 3, backgroundColor: "#4285e2" });
		    	task.selected=$(this).data("itemindex");
			})
			div.on('dblclick',function(){
				$(document).find("div[data-index]").css({zIndex:1,backgroundColor:"#2f3e8e"}).attr("data-selected",false);
		    	$(document).find("div[data-index='" + $(this).data("itemindex")+ "']").attr("data-selected",true).css({ zIndex: 3, backgroundColor: "#4285e2" });
		    	task.selected=$(this).data("itemindex");
			})
			div.append(icon).append("["+Otess.utils.getDurationArray(node.tf_duration).join(':')+"]")
			.append(text).append(edit).append(del)
			.hover(function(){
		    	$(this).css({backgroundColor:"#4285e2",color:"#fff"});
		    	$(this).find(".editfile,.deletefile").show();
		    },function(){
		    	$(this).css({backgroundColor:"#fff",color:"#000"});
		    	$(this).find(".editfile,.deletefile").hide();
		    });
			dom.append(div);
			if(!no){//绑定第一个到left 布局
				arg.bindDefault(node);
			}
		})
	})
}
Screen.prototype.bindDefault=function(file){
	var width=this.options.left.find("#default"+file.tf_position).width(),
		height=this.options.left.find("#default"+file.tf_position).height();
	if(file.tf_type===-9 || file.tf_type===-11 || file.tf_type===-12 || file.tf_type===1 || file.tf_type===-8 || file.tf_type===-10){//天气  字幕 汇率 时间
		_this=this;
		$.get(_this._task.url.getFilehtml,{tf_id:file.tf_id},function(result){
			_this.options.left.find("#default"+file.tf_position).attr("data-isfile","yes")
			.find(".coordinate")
			.html(result);
		})
	}else if(file.tf_type===-5){//链接
		this.options.left.find("#default"+file.tf_position).attr("data-isfile","yes")
		.find(".coordinate").attr("data-isfile","yes")
		.html("<iframe src=\"" + file.tf_http_url + "\"  width=\"100%\" height=\"100%\" marginheight=\"0\" allowTransparency=\"\" align=\"left\" marginwidth=\"0\"  frameborder=\"0\" scrolling=\"no\"></iframe>");
	}else if(file.tf_type===6){
		this.options.left.find("#default"+file.tf_position).attr("data-isfile","yes")
		.find(".coordinate").attr("data-isfile","yes")
		.html("<iframe src=\"" + this._task.url.uploadfile+file.m_file + "\"  width=\"100%\" height=\"100%\" marginheight=\"0\" allowTransparency=\"\" align=\"left\" marginwidth=\"0\"  frameborder=\"0\" scrolling=\"no\"></iframe>");
	}else if(file.tf_type===-4){
		
		this.options.left.find("#default"+file.tf_position).attr("data-isfile","yes")
		.find(".coordinate").attr("data-isfile","yes")
		.html($("<img/>").attr("src", this._task.img.live).width(width).height(height));
	}else if(file.tf_type===3){
		this.options.left.find("#default"+file.tf_position).attr("data-isfile","yes")
		.find(".coordinate").attr("data-isempty","no")
		.html($("<img/>").attr("src", this._task.url.uploadfile+file.m_file).width(width).height(height))
	}else if(file.tf_type===2){
		//网页html,htm,ppt,pps,doc,docx,xls,xlsx,pptx,mht
	}else if(file.tf_type===4){
		//mid,wav,mp3,wma,swf
	}else if(file.tf_type===5){
		//http://blog.jnecw.com/demo/id_00013/index.html
		this.options.left.find("#default"+file.tf_position).attr("data-isfile","yes")
		.find(".coordinate").attr("data-isfile","yes")
		.html($('<video  controls="controls" autoplay="autoplay" id="video-default'+file.tf_position+'"/>').attr("src", this._task.url.uploadfile+file.m_file).css({ width: "100%", height: "100%" }))
		$("#video-default"+file.tf_position).attr("muted",true);
		//document._video.muted=true
		//wpx,avi,wmv,asf,wmv,mpg,ra,rm,rms,rt,rmvb,rom,mns,mov,qt,mpeg,mp4,vob,tp,ts,m2ts,mkv,flv,f4v
	}
}
Screen.prototype.addFile=function(url,model,callback){
	var _this=this;
	this._ajax.post(url,model,function(result){
		if(result.code!=200){
			if(confirm(_this._task.prompt.addfile)){
				model.forEach(function(node,index){
					if(node.name=='tf_isadd')node.value=1;
				})
				_this.addFile(url,model,callback);
			}
		}else{
			callback(result);
		}
	})
}
Screen.prototype.save=function(callback){
	this._task.item.ti_screen_type=this.targetToString();
	this._task.item.ti_screen_type_affix=this.toString();
	this._ajax.post(this._task.url.savescreen,this._task.item,function(result){
		callback(result);
	})
}
Screen.prototype.setDosition=function(el){
	el.empty().append("<option value=''>请选择布局</option>");
	var flag="";
	this._left.children().each(function (i) {
        var val = $(this).attr("data-index"),
        	text = $(this).attr("data-info");
        	selected=$(this).attr("data-selected")=="true"?"selected":"";	
        	if(selected=="selected"){
        		flag=selected;
        	}
        el.append("<option value='" + val + "' "+selected+">" + text + "</option>");        	
    });
	if(flag==""){
		el.val('0');
	}
}
Screen.prototype.move=function(e){
	var task=this._task;
	if(task.selected){
		var screen = {id:$("#default" + task.selected),index:task.selected};
		screen.left=parseInt(screen.id.offset().left);
		screen.top=parseInt(screen.id.offset().top);
		screen.width=parseInt(screen.id.outerWidth());
		screen.height=parseInt(screen.id.outerHeight());
		console.log(screen);
		if (e.which == 37) {
			screen.left -= 1;
            if (screen.left < 0) screen.left = 0;
            screen.id.css({ left: screen.left });
        }
        else if (e.which == 38) {
        	screen.top -= 1;
            if (screen.top < 0) screen.top = 0;
            screen.id.css({ top: screen.top });
        }
        else if (e.which == 39) {
			screen.left += 1;
            if (screen.left+screen.width > task.field.web_mr_w) {
            	screen.left = task.field.web_mr_w - screen.width;
            }
            screen.id.css({ left: screen.left });
        }
        else if (e.which == 40) {
        	screen.top += 1;
            if (screen.top+screen.height > task.field.web_mr_h) {
            	screen.top =  task.field.web_mr_h - screen.height;;
            }
            screen.id.css({ top: screen.top });
        }
		var info = "default" + screen.index + " (" + screen.left + "," + screen.top + "," + screen.width + "," + screen.height + ")";
        
		screen.id.attr("data-info", info);
        console.log(screen.id.data("isempty"));
    	if(screen.id.data("isempty")==="yes" && screen.id.data("isfile")!=="yes")
            screen.id.find(".coordinate").attr("data-info", info).text(info);
	}
}
