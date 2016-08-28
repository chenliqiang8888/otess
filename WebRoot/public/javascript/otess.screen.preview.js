
function Screen(options){
    this.options=options;
    this._left=this.options.left;
	this._task=this.options.task;
}
Screen.prototype.build=function(el){
	var left=this._left;
	/*左侧布局*/
	var text=$("<div/>").addClass("coordinate").height(el.height).attr("data-info", el.info);
	var div= $("<div/>").addClass("tada").attr("data-index",el.index).attr("data-isempty",true).attr("data-info",el.info).attr("id",el.id).append(text)
	 	.css({left: el.left+"px", top: el.top+"px", zIndex: 1, position: "absolute"
	 		,overflow:"hidden",cursor: "move", border: "0px dashed #fff"})
	 	.width(el.width).height(el.height);
	left.append(div);
	
}
Screen.prototype.create=function(){
	var param = this.options.item.ti_screen_type.split(','),nCount = parseInt(param[0]),icount = 1,firstWidth = 0, firstHeight = 0,j = 0;
    for (var i = icount; i <= nCount * 5; i += 5) {
    	var el = {
    			left: param[i],top: param[i + 1],
    			width: param[i + 2],height: param[i + 3],
    			id: param[i + 4],index:param[i + 4].replace("default","")};
    	el.info = el.id + " (" + el.left + "," + el.top + "," + el.width + "," + el.height + ")";
    	this.build(el);
    }
}
Screen.prototype.remove=function(){
	this._left.empty();
}
Screen.prototype.bindFile=function(){
	this._left.empty();
}

Screen.prototype.bindDefault=function(file){
	if(file.tf_type===-9 || file.tf_type===-11 || file.tf_type===-12 || file.tf_type===1  || file.tf_type===-8 || file.tf_type===-10){//天气  字幕 汇率 时间
		_this=this;
		$.get(_this._task.url.getFilehtml,{tf_id:file.tf_id},function(result){
			_this.options.left.find("#default"+file.tf_position).attr("data-isempty",false).find(".coordinate")
			.html(result);
		})
	}else if(file.tf_type===-5){//链接
		this.options.left.find("#default"+file.tf_position).attr("data-isempty",false)
		.find(".coordinate")
		.html("<iframe src=\"" + file.tf_http_url + "\"  width=\"100%\" height=\"100%\" marginheight=\"0\" allowTransparency=\"\" align=\"left\" marginwidth=\"0\"  frameborder=\"0\" scrolling=\"no\"></iframe>");
	}else if(file.tf_type===6){
		this.options.left.find("#default"+file.tf_position).attr("data-isempty",false)
		.find(".coordinate")
		.html("<iframe src=\"" + this._task.url.uploadfile+file.m_file + "\"  width=\"100%\" height=\"100%\" marginheight=\"0\" allowTransparency=\"\" align=\"left\" marginwidth=\"0\"  frameborder=\"0\" scrolling=\"no\"></iframe>");
	}
	else if(file.tf_type===-4){
		this.options.left.find("#default"+file.tf_position).attr("data-isempty",false)
		.find(".coordinate").html($("<img/>").attr("src", this._task.img.live).css("width","100%"));
	}else if(file.tf_type===3){
		this.options.left.find("#default"+file.tf_position).attr("data-isempty",false)
		.find(".coordinate")
		.html($("<img/>").attr("src", this._task.url.uploadfile+file.m_file).css({ width: "100%", height: "100%" }))
	}else if(file.tf_type===2){
		//网页html,htm,ppt,pps,doc,docx,xls,xlsx,pptx,mht
	}else if(file.tf_type===4){
		//mid,wav,mp3,wma,swf
	}else if(file.tf_type===5){
		//http://blog.jnecw.com/demo/id_00013/index.html
		/*this.options.left.find("#default"+file.tf_position).attr("data-isempty",false)
		.find(".coordinate")
		.html($('<video  controls="controls" autoplay="autoplay" id="video-default'+file.tf_position+'"/>').attr("src", this._task.url.uploadfile+file.m_file).css({ width: "100%", height: "100%" }))
		$("#video-default"+file.tf_position).attr("muted",true);*/
		/*alert(this._task.url.uploadfile+file.m_file)
		var flashvars={
			f:this._task.url.uploadfile+file.m_file,
			c:0,
			b:1
			};
		var params={bgcolor:'#FFF',allowFullScreen:true,allowScriptAccess:'always',wmode:'transparent'};
		CKobject.embedSWF(task.url.ck,"default"+file.tf_position,'ckplayer_a1',
				this.options.left.find("#default"+file.tf_position).width(),
				this.options.left.find("#default"+file.tf_position).height(),flashvars,params);*/	
		var width = this.options.left.find("#default"+file.tf_position).width();
        var height = this.options.left.find("#default"+file.tf_position).height();
		

        var pluginspage = "http://www.adobe.com/go/getflashplayer";
        var type = "application/x-shockwave-flash";
        var filepath=this._task.url.uploadfile+file.m_file;
        var src = "#";
        if (filepath.indexOf(".flv") >= 0)
            src = this._task.url.swf;
        else {
            src = filepath;
            pluginspage = "http://www.microsoft.com/Windows/MediaPlayer/";
            type = "application/x-mplayer2";
        }
        this.options.left.find("#default"+file.tf_position).flash(
            {
                src: src,
                width: width,
                height: height,
                type: type,
                pluginspage: pluginspage,
                flashvars: { file: filepath, autostart: 1 }
            },
            { version: 10 }
        );
		//document._video.muted=true
		//wpx,avi,wmv,asf,wmv,mpg,ra,rm,rms,rt,rmvb,rom,mns,mov,qt,mpeg,mp4,vob,tp,ts,m2ts,mkv,flv,f4v
	}

	if(!Number(file.tf_position)){
		
		if(file.tf_bgsound){
			var music=file.tf_bgsound.split('$#$'),div=$("<div/>").hide();
			$("body").append(div.append('<audio src="'+this._task.url.base+music[0]+'" controls="controls" autoplay="true"></audio>'));
		}
	}
}
