//先计算left、right的宽高
$(function(){

	$(window).on('beforeunload',function(){
        return '请确保任务项已经保存，确定离开此吗？';
    }); 
	$(window).on('unload',function(){ 
	    alert("获取到了页面要关闭的事件了！"); 
	}); 
	var ajax=new Ajax(),menu=null;
	if(task.field.t_simple===1){
		menu={
				width: 150,
				items:[
				    { text: "添加文件", icon: "glyphicon glyphicon-plus", alias: "addfile", action: menuAction },
				    { type: "splitLine" },
				    { text: "添加直播", icon: "glyphicon glyphicon-plus", alias: "addlive", action: menuAction }, 
				    { type: "splitLine" },
				    { text: "添加链接", icon: "glyphicon glyphicon-plus", alias: "addlink", action: menuAction },
				    { type: "splitLine" },
				    { text: "完成任务", icon: "glyphicon glyphicon-share-alt", alias: "taskcomplete", action: menuAction },
				    { type: "splitLine" },
				    { text: "放弃任务", icon: "glyphicon glyphicon-trash", alias: "taskdelete", action: menuAction }
				]
			}
	}else{
		menu={
		    width: 150,
		    items: [{
		        text: "添加布局", icon: "glyphicon glyphicon-plus", alias: "addscreen",
		        action: menuAction
		    }, { text: "保存布局", icon: "glyphicon glyphicon-ok", alias: "savescreen", action: menuAction },
		    { text: "导入布局", icon: "glyphicon glyphicon-import", alias: "importscreen", action: menuAction },
		    { text: "另存为布局", icon: "glyphicon glyphicon-ok-sign", alias: "saveas", action: menuAction },
		    { type: "splitLine" },
		    {
		        text: "添加资源", icon: "glyphicon glyphicon-plus-sign", alias: "addsource", type: "group", width: 150, items: [
		            { text: "添加文件", icon: "glyphicon glyphicon-plus", alias: "addfile", action: menuAction },
		            { text: "添加时间", icon: "glyphicon glyphicon-plus", alias: "addtime", action: menuAction }, { type: "splitLine" },
		            { text: "添加天气预报", icon: "glyphicon glyphicon-plus", alias: "addwether", action: menuAction }, { type: "splitLine" },
		            { text: "添加数据库", icon: "glyphicon glyphicon-plus", alias: "adddatabase", action: menuAction }, { type: "splitLine" },
		            { text: "添加汇率", icon: "glyphicon glyphicon-plus", alias: "addhuilv", action: menuAction }, { type: "splitLine" },
		            { text: "添加字幕", icon: "glyphicon glyphicon-plus", alias: "addtxt", action: menuAction }, { type: "splitLine" },
		            { text: "添加直播", icon: "glyphicon glyphicon-plus", alias: "addlive", action: menuAction }, { type: "splitLine" },
		            { text: "添加链接", icon: "glyphicon glyphicon-plus", alias: "addlink", action: menuAction }]
		    }, { type: "splitLine" },
		    { text: "平分时间", icon: "glyphicon glyphicon-time", alias: "averagetime", action: menuAction },
		    { text: "编辑任务项", icon: "glyphicon glyphicon-edit", alias: "edittask", action: menuAction },
		    { type: "splitLine" },
		    { text: "返回任务项", icon: "glyphicon glyphicon-share-alt", alias: "taskitem", action: menuAction },
		    { text: "返回任务", icon: "glyphicon glyphicon-share-alt", alias: "task", action: menuAction }]
		}
	}
	
	$("body").contextmenu(menu);
	var left=$("#left"),right=$("#right");
	
	var layout=new Screen({left:left,right:right,task:task,ajax:ajax});
	//-----------------------------------------------------
	layout.create();
	layout.bindFile(task.file);
	left.css({
		width:layout.getWidth()+"px",
		height:layout.getHeight()+"px",
		//border: "0px solid #aaa",
		backgroundColor: "#2f3e8e"})
	right.css({
		width:"240px",height:layout.getHeight()+"px",
		left:layout.getWidth()+"px",backgroundColor: "#fff",
		position: "absolute",top: "0px",border:"1px solid rgb(170, 170, 170)"
	});
	//编辑分辨率
	task.field.web_mr_w=layout.getWidth(),task.field.web_mr_h=layout.getHeight();
	Otess.utils.initForm();
	function loadFileHtml(param){
		var dom=$("#dialog-modal-com");
		console.log(param);
		$.get(task.url.addfile,{tf_type:param.tf_type},function(result){
			dom.html(result);
			console.log(result);
			layout.setDosition($("[name='position']"));
			//判断是否为default0,default0显示选择背景音乐
			if(Number(dom.find("[name='position']").val())){
				 $("#selectMusicGroup").hide();
			}else{
				 $("#selectMusicGroup").show();			
			}
			dom.dialog({
				title:param.title,resizable: false, width: param.width, height: param.height, modal: true, autoOpen: "open",
	            buttons:{
	                "确定": function () {
	                	var taskfile=dom.serializeArray();
	                	console.log(param.tf_type);
	                	if(param.tf_type===-5 || param.tf_type===-4){
	                		if(!Otess.utils.isUrl($("#url").val())){
	                			$("#url").parent().addClass('has-error');
	                			if(param.tf_type===-5)Otess.utils.fail("链接地址非法");
	                			if(param.tf_type===-4)Otess.utils.fail("直播地址非法");
	                			return false;
	                		}
	                	}
	                	taskfile.push({name:"tf_task_item_id",value:task.item.ti_id})
	                	taskfile.push({name:"tf_duration",value:Otess.utils.getDuration(param.position)});
	                	taskfile.push({name:"tf_isadd",value:0});
	                	taskfile.push({name:"tf_type",value:param.tf_type});
	                	taskfile.push({name:"tf_scale",value:layout.scale()});
	                	
	                	var url=param.tf_type===-1?task.url.addmedia:task.url.savefile;
	                	layout.addFile(url,taskfile,function(data){
	                		if(data.code==200){
		                		task.file=data.list;
		                		console.log(data.list);
		                		layout.bindFile(data.list);
			                	dom.dialog("destroy");
								Otess.utils.success();
	                		}
	                	});
	                },"上传": function () {
	                	//上传
	                	upload();
	                },"关闭": function () {
	                	dom.dialog("destroy");
	                }
	            } ,
	            close: function (event, ui) {}
	        })
	        if(param.tf_type!==-1){
	        	var button=$(".ui-dialog-buttonpane button").eq(1);
	        	button.data("id",'medias.upload').hide();
	        }	        
		})
	}
	
	function dbAction(param){
		var dom=$("#dialog-modal-com");
		console.log(param);
		$.get(task.url.addfile,{tf_type:param.tf_type},function(result){
			dom.html(result);
			console.log(result);
			layout.setDosition($("[name='position']"));
			dom.dialog({
				title:param.title,resizable: false, width: param.width, height: param.height, modal: true, autoOpen: "open",
	            buttons: {
	                "连接": function () {
	                	var taskfile=dom.serializeArray();
	                	var step=$(".ui-dialog-buttonpane button").eq(0).find("span").data("step");
	                	console.log(typeof step)
	                	
	                	//判断dialog button 按钮text
	                	if(!step){
		                	var index = layer.load(1, {
		                		shade: [0.5,'#000'] //0.1透明度的白色背景
		                	});
		                	$.get(task.url.loadtables,taskfile,function(result){
		                		console.log(result);
		                		if(result.code===200){
			                		$("#dbtables").empty().append('<option value="">请选择表</option>');
		                			$("#dbinfo").hide();
		                			$("#sqlinfo").show();
		                			result.list.forEach(function(m,i){
		                				$("#dbtables").append("<option value='"+m.TABLE_NAME+"'>"+m.TABLE_NAME+"</option>")
		                			})
		                			//点链接后 按钮text变为上一部
		                			$(".ui-dialog-buttonpane button").eq(0).html('<span class="ui-button-text" data-step="1">上一步</span>');
		                			//下一步显示出来
		                	        $(".ui-dialog-buttonpane button").eq(1).show();
		                		}else{
		                			Otess.utils.fail('连接数据库超时！');
		                		}
		            	        layer.close(index);
		                	})
	                	}else if(step===1){//第一次上一步
	                		//显示数据库连接info
		                	$("#dbinfo").show();
		                	//隐藏sql语句info
                			$("#sqlinfo").hide();
                			//按钮恢复到连接
                			$(".ui-dialog-buttonpane button").eq(0).html('<span class="ui-button-text">连接</span>');
                			//下一步隐藏了
                			$(".ui-dialog-buttonpane button").eq(1).hide();
	                	}else if(step===2){//第二次上一步
    	                	$("#dbinfo").hide();
                			$("#sqlinfo").show();
    	                	$("#tableinfo").hide();
    	                	dom.dialog( "option", "width" ,param.width);
    	                	dom.dialog( "option", "height" ,param.height);
    	                	$(".ui-dialog-buttonpane button").eq(0).html('<span class="ui-button-text" data-step="1">上一步</span>');
    	                	$(".ui-dialog-buttonpane button").eq(1).html('<span class="ui-button-text" data-step="0">下一步</span>');
	                	}else if(step===3){//第三次上一步
	                		$("#dbinfo").hide();
                			$("#sqlinfo").hide();
    	                	$("#tableinfo").show();
    	                	$("#columninfo").hide();
    	                	$(".ui-dialog-buttonpane button").eq(0).html('<span class="ui-button-text" data-step="2">上一步</span>');
    	                	//下一步显示出来
		                	$(".ui-dialog-buttonpane button").eq(1).html('<span class="ui-button-text" data-step="1">下一步</span>');
	                	}

	                },
	                "下一步": function () {//编辑颜色|宽度|页数|周期等
	                	var step=$(".ui-dialog-buttonpane button").eq(1).find("span").data("step");
	                	if(!step || step===0){
		                	dom.dialog( "option", "width" ,task.field.web_mr_w);
		                	dom.dialog( "option", "height" ,task.field.web_mr_h-50);
		                	$(".ui-dialog-buttonpane button").eq(0).html('<span class="ui-button-text" data-step="2">上一步</span>');
		                	//下一步显示出来
		                	$(".ui-dialog-buttonpane button").eq(1).html('<span class="ui-button-text" data-step="1">下一步</span>');
		                	$("#dbinfo").hide();
	            			$("#sqlinfo").hide();
		                	$("#tableinfo").show();
		                	$("[name='db_width']").val($("#default"+$("[name='position']").val()).width());
		                	$("[name='db_height']").val($("#default"+$("[name='position']").val()).height());
		                	var index = layer.load(1, {
		                		shade: [0.5,'#000'] //0.1透明度的白色背景
		                	});
		                	var taskfile=dom.serializeArray();
		                	$.get(task.url.loadtabledata,taskfile,function(result){
		                		var table="<table id='selectcolumns' class=\"table table-hover table-bordered table-condensed\">";
		                		var thead="";
		                		var tbody="";
		                		result.list.forEach(function(m,i){
		                			var tr="<tr>";
		                			Object.keys(m).forEach(function(n,j){
		                				if(i===0){
		                					thead+="<th><select name='"+n+"' data-column='"+n+"'><option value='0'>不选</option><option value='1' selected>文字</option><option value='2'>图片</option></select></th>"
		                				}
		                				tr+="<td data-name='"+n+"'>"+m[n]+"</td>";
		                			})
		                			tr+="</tr>";
		                			tbody+=tr;
		                		})
		                		table+="<thead><tr class='active'>"+thead+"</tr></thead>";
		                		table+="<tbody>"+tbody+"</tbody>";
		                		table+="</table>";
		                		$("#datainfo").html(table);
		            	        layer.close(index);
		                	})
		                	
	                	}else if(step===1){//改变列宽
	                		//下一步显示出来
	                		var columns=[],field="";
	                		//判断哪些列被选中了
	                		$("#selectcolumns").find("thead th select").each(function(i){
	                			if($(this).val()!=='0'){
	                				if(field!=='')field+=",";
	                				field+=$(this).data("column");
	                				columns.push({name:$(this).data("column"),type:$(this).val(),index:i})
	                			}
	                		})	                		
	                		if(columns.length===0){
	                			Otess.utils.fail('请选择列');return false;
	                		}
	            			$("#dbsql").val("select "+field+" from "+$("#dbtables").val());
	                		var table=$("<table/>").addClass("table table-bordered").width($("[name='db_width']").val()).height($("[name='db_height']").val());
	                		//循环绑定数据
	                		$("#selectcolumns").find("tbody tr").each(function(i){
	                			var thead=$("<tr/>");
	                			var tr=$("<tr/>");
	                			$(this).find("td").each(function(j){
	                				var name=$(this).data("name");
	                				console.log(name);
		                			var item=_.find(columns, function(chr) {
		                	            return chr.name==name;
		                	        });
		                			if(item){
			                			if(i===0){
			                				thead.append("<th data-columntype='"+item.type+"' style='white-space: nowrap;overflow: hidden;'>col"+j+"</th>");
			                			}
		                				if(Number(item.type)===2){//图片
		                					tr.append("<td><img style='border:0px;max-width:100%' src='"+$("[name='path']").val()+$(this).text()+"'></td>");
	                					}else if(Number(item.type)===1){
	                						tr.append("<td>"+$(this).text()+"</td>");
	                					}
		                			}
	                			})
	                			if(i===0)table.append(thead);
		                		table.append(tr);
	                		})
	                		var picpath = $("[name='database_picpath']").val();
	                		var style={
	                			color:$("[name='color']").val(),//颜色
	                			fontsize:$("[name='fontsize']").val(),
	                			family:$("[name='family']").val(),fonttype:$("[name='fonttype']").val(),
	                			valign:$("[name='valign']").val(),align:$("[name='align']").val(),
	                			lineheight:$("[name='lineheight']").val(),bordercolor:$("[name='bordercolor']").val(),
	                			borderwidth:$("[name='borderwidth']").val()
	                		}
	                		//设置表样式
	                		table.css({border:style.borderwidth + "px solid #" + style.bordercolor}).find("td").css({
	                			color: "#" + style.color, fontSize: style.fontsize + "px",
	                			fontFamily: style.family, fontStyle: style.fonttype, textAlign: style.align,
	                			verticalAlign: style.valign ,height: style.lineheight,border:style.borderwidth + "px solid #" + style.bordercolor
				            });
		                	$(".ui-dialog-buttonpane button").eq(0).html('<span class="ui-button-text" data-step="3">上一步</span>');
		                	$(".ui-dialog-buttonpane button").eq(1).html('<span class="ui-button-text" data-step="2">保存</span>');
	                		$("#tableinfo").hide();
	                		$("#columninfo").show().html(table);
	                		$("#columninfo").find("table").resizableColumns();
	                	}else if(step===2){
	                		//保存
		                	var taskfile=dom.serializeArray();

		                	//taskfile.push({name:"tf_type",value:type});
		                	console.log(taskfile);
		                	var dbcolumnswidth="",columnstype="";
		                	
		                	$("#columninfo").find("table tr th").each(function(i){
		                		//获取列是图片还是文字  1=文字 2=图片
		                		if(columnstype)columnstype+=",";
		                		columnstype+=$(this).data("columntype")
		                		//获取每列宽
		                		if(dbcolumnswidth)dbcolumnswidth+=",";
		                		dbcolumnswidth+=$(this).width()
		                	})
		                	console.log(dbcolumnswidth);
		                	taskfile.push({name:"dbcolumnswidth",value:dbcolumnswidth});
		                	taskfile.push({name:"dbcolumnstype",value:columnstype});
		                	taskfile.push({name:"tf_task_item_id",value:task.item.ti_id})
		                	taskfile.push({name:"tf_duration",value:Otess.utils.getDuration('db')});
		                	taskfile.push({name:"dbrefresh",value:Otess.utils.getDuration('refresh')});
		                	taskfile.push({name:"tf_isadd",value:0});
		                	taskfile.push({name:"tf_type",value:param.tf_type});
		                	taskfile.push({name:"tf_scale",value:layout.scale()});
		                	layout.addFile(task.url.savefile,taskfile,function(data){
		                		console.log(data);
		                		if(data.code==200){
			                		task.file=data.list;
			                		console.log(data.list);
			                		layout.bindFile(data.list);
				                	dom.dialog("destroy");
									Otess.utils.success();
		                		}
		                	});
	                	}

	                },
	                "关闭": function () {
	                	dom.dialog("destroy");
	                }
	            },
	            close: function (event, ui) {}
	        })
	        $(".ui-dialog-buttonpane button").eq(1).hide();
		})
	}
	function dbEditAction(param){
		var dom=$("#dialog-modal-com");
		console.log(param);
		dom.html(param.result);
		layout.setDosition($("[name='position']"));
		dom.dialog({
			title:param.title,resizable: false, width: param.width, height: param.height, modal: true, autoOpen: "open",
            buttons: {
                "连接": function () {
                	var taskfile=dom.serializeArray();
                	var step=$(".ui-dialog-buttonpane button").eq(0).find("span").data("step");
                	console.log(typeof step)
                	//判断dialog button 按钮text
                	if(!step){
                		var index = layer.load(1, {
	                		shade: [0.5,'#000'] //0.1透明度的白色背景
	                	});
	                	$.get(task.url.loadtables,taskfile,function(result){
	                		if(result.code===200){
		                		$("#dbtables").empty().append('<option value="">请选择表</option>');
	                			$("#dbinfo").hide();
	                			$("#sqlinfo").show();
	                			result.list.forEach(function(m,i){
	                				var selected="";
	                				if($("#dbtables").data("selected")===m.TABLE_NAME)selected="selected";
	                				$("#dbtables").append("<option value='"+m.TABLE_NAME+"' "+selected+">"+m.TABLE_NAME+"</option>")
	                			})
	                			//点链接后 按钮text变为上一部
	                			$(".ui-dialog-buttonpane button").eq(0).html('<span class="ui-button-text" data-step="1">上一步</span>');
	                			//下一步显示出来
	                	        $(".ui-dialog-buttonpane button").eq(1).show();
	                	        layer.close(index);
	                		}
	                	})
                	}else if(step===1){//第一次上一步
                		//显示数据库连接info
	                	$("#dbinfo").show();
	                	//隐藏sql语句info
            			$("#sqlinfo").hide();
            			//按钮恢复到连接
            			$(".ui-dialog-buttonpane button").eq(0).html('<span class="ui-button-text">连接</span>');
            			//下一步隐藏了
            			$(".ui-dialog-buttonpane button").eq(1).hide();
                	}else if(step===2){//第二次上一步
	                	$("#dbinfo").hide();
            			$("#sqlinfo").show();
	                	$("#tableinfo").hide();
	                	dom.dialog( "option", "width" ,param.width);
	                	dom.dialog( "option", "height" ,param.height);
	                	$(".ui-dialog-buttonpane button").eq(0).html('<span class="ui-button-text" data-step="1">上一步</span>');
	                	$(".ui-dialog-buttonpane button").eq(1).html('<span class="ui-button-text" data-step="0">下一步</span>');
                	}else if(step===3){//第三次上一步
                		$("#dbinfo").hide();
            			$("#sqlinfo").hide();
	                	$("#tableinfo").show();
	                	$("#columninfo").hide();
	                	$(".ui-dialog-buttonpane button").eq(0).html('<span class="ui-button-text" data-step="2">上一步</span>');
	                	//下一步显示出来
	                	$(".ui-dialog-buttonpane button").eq(1).html('<span class="ui-button-text" data-step="1">下一步</span>');
                	}

                },
                "下一步": function () {//编辑颜色|宽度|页数|周期等
                	var step=$(".ui-dialog-buttonpane button").eq(1).find("span").data("step");
                	console.log(step)
                	if(!step || step===0){
	                	dom.dialog( "option", "width" ,task.field.web_mr_w);
	                	dom.dialog( "option", "height" ,task.field.web_mr_h-50);
	                	$(".ui-dialog-buttonpane button").eq(0).html('<span class="ui-button-text" data-step="2">上一步</span>');
	                	//下一步显示出来
	                	$(".ui-dialog-buttonpane button").eq(1).html('<span class="ui-button-text" data-step="1">下一步</span>');
	                	$("#dbinfo").hide();
            			$("#sqlinfo").hide();
	                	$("#tableinfo").show();
	                	$("[name='db_width']").val($("#default"+$("[name='position']").val()).width());
	                	$("[name='db_height']").val($("#default"+$("[name='position']").val()).height());
	                	
	                	var taskfile=dom.serializeArray();
	                	var index = layer.load(1, {
	                		shade: [0.5,'#000'] //0.1透明度的白色背景
	                	});
	                	$.get(task.url.loadtabledata,taskfile,function(result){
	                		var table="<table id='selectcolumns' class=\"table table-hover table-bordered table-condensed\">";
	                		var thead="";
	                		var tbody="";
	                		result.list.forEach(function(m,i){
	                			var tr="<tr>";
	                			Object.keys(m).forEach(function(n,j){
	                				if(i===0){
	                					thead+="<th><select name='"+n+"' data-column='"+n+"'><option value='0'>不选</option><option value='1' selected>文字</option><option value='2'>图片</option></select></th>"
	                				}
	                				tr+="<td data-name='"+n+"'>"+m[n]+"</td>";
	                			})
	                			tr+="</tr>";
	                			tbody+=tr;
	                		})
	                		table+="<thead><tr class='active'>"+thead+"</tr></thead>";
	                		table+="<tbody>"+tbody+"</tbody>";
	                		table+="</table>";
	                		$("#datainfo").html(table);
	                		layer.close(index);
	                	})
	                	
                	}else if(step===1){//改变列宽
                		//下一步显示出来
                		var columns=[],field="";
                		//判断哪些列被选中了
                		$("#selectcolumns").find("thead th select").each(function(i){
                			if($(this).val()!=='0'){
                				if(field!=='')field+=",";
                				field+=$(this).data("column");
                				columns.push({name:$(this).data("column"),type:$(this).val(),index:i})
                			}
                		})	                		
                		if(columns.length===0){
                			Otess.utils.fail('请选择列');return false;
                		}
            			$("#dbsql").val("select "+field+" from "+$("#dbtables").val());
        				var columnswidth=$("[name='dbcolumnswidth']").val().split(",");
                		var table=$("<table/>").addClass("table table-bordered").width($("[name='db_width']").val()).height($("[name='db_height']").val());
                		//循环绑定数据
                		$("#selectcolumns").find("tbody tr").each(function(i){
                			var thead=$("<tr/>");
                			var tr=$("<tr/>");
                			$(this).find("td").each(function(j){
                				var name=$(this).data("name");
                				console.log(name);
	                			var item=_.find(columns, function(chr) {
	                	            return chr.name==name;
	                	        });
	                			if(item){
		                			if(i===0){
		                				thead.append("<th data-columntype='"+item.type+"' style='white-space: nowrap;overflow: hidden;width:"+columnswidth[j]+"px'>col"+j+"</th>");
		                			}
	                				if(Number(item.type)===2){//图片
	                					tr.append("<td><img style='border:0px;max-width:100%' src='"+$("[name='path']").val()+$(this).text()+"'></td>");
                					}else if(Number(item.type)===1){
                						tr.append("<td>"+$(this).text()+"</td>");
                					}
	                			}
                			})
                			if(i===0)table.append(thead);
	                		table.append(tr);
                		})
                		var picpath = $("[name='database_picpath']").val();
                		var style={
                			color:$("[name='color']").val(),//颜色
                			fontsize:$("[name='fontsize']").val(),
                			family:$("[name='family']").val(),fonttype:$("[name='fonttype']").val(),
                			valign:$("[name='valign']").val(),align:$("[name='align']").val(),
                			lineheight:$("[name='lineheight']").val(),bordercolor:$("[name='bordercolor']").val(),
                			borderwidth:$("[name='borderwidth']").val()
                		}
                		//设置表样式
                		table.css({border:style.borderwidth + "px solid #" + style.bordercolor}).find("td").css({
                			color: "#" + style.color, fontSize: style.fontsize + "px",
                			fontFamily: style.family, fontStyle: style.fonttype,fontWeight:style.fonttype, textAlign: style.align,
                			verticalAlign: style.valign ,height: style.lineheight,border:style.borderwidth + "px solid #" + style.bordercolor
			            });
	                	$(".ui-dialog-buttonpane button").eq(0).html('<span class="ui-button-text" data-step="3">上一步</span>');
	                	$(".ui-dialog-buttonpane button").eq(1).html('<span class="ui-button-text" data-step="2">保存</span>');
                		$("#tableinfo").hide();
                		$("#columninfo").show().html(table);
                		$("#columninfo").find("table").resizableColumns();
                	}else if(step===2){
                		//保存

	                	//taskfile.push({name:"tf_type",value:type});
	                	console.log(taskfile);
	                	var dbcolumnswidth="",columnstype="";
	                	
	                	$("#columninfo").find("table tr th").each(function(i){
	                		//获取列是图片还是文字  1=文字 2=图片
	                		if(columnstype)columnstype+=",";
	                		columnstype+=$(this).data("columntype")
	                		//获取每列宽
	                		if(dbcolumnswidth)dbcolumnswidth+=",";
	                		dbcolumnswidth+=$(this).width()
	                	})
	                	console.log("edit database:"+dbcolumnswidth);
	                	$("[name='dbcolumnswidth']").val(dbcolumnswidth);
	                	$("[name='dbcolumnstype']").val(columnstype);
	                	var taskfile=dom.serializeArray();
	                	taskfile.push({name:"tf_task_item_id",value:task.item.ti_id})
	                	taskfile.push({name:"tf_duration",value:Otess.utils.getDuration('db')});
	                	taskfile.push({name:"dbrefresh",value:Otess.utils.getDuration('refresh')});
	                	taskfile.push({name:"tf_isadd",value:0});
	                	taskfile.push({name:"tf_type",value:param.tf_type});
	                	taskfile.push({name:"tf_scale",value:layout.scale()});
	                	console.table(taskfile)
	                	layout.addFile(task.url.savefile,taskfile,function(data){
	                		console.log(data);
	                		if(data.code==200){
		                		task.file=data.list;
		                		console.log(data.list);
		                		layout.bindFile(data.list);
			                	dom.dialog("destroy");
								Otess.utils.success();
	                		}
	                	});
                	}
                },
                "关闭": function () {
                	dom.dialog("destroy");
                }
            },
            close: function (event, ui) {}
        })
        $(".ui-dialog-buttonpane button").eq(1).hide();
	}
	function menuAction(){
		switch(this.data.alias){
			case "addscreen":
				layout.add();
				break;
			case "savescreen":
				layout.save(function(result){
					if(result.code==200){
						Otess.utils.success('保存布局成功！');
					}
				});
				break;
			case "addhuilv":
		        loadFileHtml({title:"添加汇率",tf_type:-11,width:860,height:760,position:"huilv"});
		        break;
			case "addtxt":
		        loadFileHtml({title:"添加字幕",tf_type:-12,width:479,height:679,position:"txt"});
				break;
			case "addlink":
		        loadFileHtml({title:"添加链接",tf_type:-5,width:459,height:322,position:"link"});
				break;
			case "addlive":		        
				loadFileHtml({title:"添加直播",tf_type:-4,width:459,height:322,position:"live"});	
				break;
			case "addwether":
				loadFileHtml({title:"添加天气预报",tf_type:-9,width:459,height:512,position:"weather"});
				break;
			case "addtime":
				loadFileHtml({title:"添加时间",tf_type:-8,width:459,height:459,position:"time"});				
				break;
			case "task":
				window.location.href =task.url.edit+task.field.t_id;
				break;
			case "addfile":	
				loadFileHtml({title:"添加文件",tf_type:-1,width:922,height:620,position:"file"});			        
				break;
			case "taskitem":
				window.location.href =task.url.item+task.field.t_id;
				break;
			case "tasklist":
				window.location.href =task.url.tasklist;
				break;
			case "taskcomplete":
				if(!right.find(".itemchild div").size()){
					Otess.utils.fail("请添加任务资源！");
				}else{
					$.get(task.url.updatetaskstatus,{t_id:task.field.t_id},function(result){
						if(result.code===200){
							window.location.href =task.url.tasklist;
						}else{
							Otess.utils.fail(result.message);
						}
					})
				}
				break;
			case "taskdelete":
				$.smkConfirm({
			      text:"确认放弃任务？",
			      accept:'确认',
			      cancel:'取消'
			    },function(res){
			      if (res) {
			    	$.get(task.url.deletetask,{ids:task.field.t_id},function(result){
						window.location.href =task.url.tasklist;
			    	})
			      }
			    });
				break;
			case "adddatabase":
				dbAction({title:"添加数据库",tf_type:-10,width:459,height:459,position:"db"});	
				break;
			case "edittask":
				$.get(task.url.detail,{ti_id:task.item.ti_id},function(result){
					$("[name='ti_name']").val(result.data.ti_name);
					Otess.utils.setDuration("task",result.data.ti_duration);				
					var dom=$("#dialog-modal-edittaskitem");
					dom.dialog({
						resizable: false, autoOpen: "open", modal: true, width: 280, height: 300,
			            buttons: {
			                "确定": function () {
			                	var taskitem=dom.serializeArray();
			                	taskitem.push({name:"ti_duration",value:Otess.utils.getDuration("task")});
			                	taskitem.push({name:"ti_id",value:task.item.ti_id});
			                	task.item.ti_name=$("[name='ti_name']").val();
			                	task.item.ti_duration=Otess.utils.getDuration("task");
			                	ajax.post(task.url.edititem,taskitem,function(result){
			                		if(result.code==200){
					                	dom.dialog("destroy");	
										Otess.utils.success('编辑任务项成功!');
			                		}
			                	})
			                },"关闭": function () {
			                	dom.dialog("destroy");
			                }
			            },
			            close: function (event, ui) {
		                	dom.dialog("destroy");
		                }
			        })
				})
				
				break;
			case "averagetime":
				if (confirm(task.prompt.averagetime)) {
					ajax.post(task.url.averagetime,task.item,function(result){
						if(result.code==200){
							location.reload();
						}
					})
	            }
				break;
			case "saveas":				
				var dom=$("#dialog-modal-saveas");
				dom.dialog({
					resizable: false, height: 210, width: 210, modal: true, autoOpen: "open", closeText: "hide",
		            buttons: {
		                "确定": function () {
		                	var screen=dom.serializeArray();
		                	screen.push({name:"sc_info",value:layout.toString()});
		                	screen.push({name:"sc_target",value:layout.targetToString()});
		                	ajax.post(task.url.saveas,screen,function(result){
		                		if(result.code==200){
				                	dom.dialog("destroy");
									Otess.utils.success('另存布局成功！');
		                		}
		                	})
		                },"关闭": function () {
		                	dom.dialog("destroy");
		                }
		            },
		            close: function (event, ui) {}
		        })
				break;
			case "importscreen":
				$.get(task.url.loadscreen,{},function(result){
					console.log(result);
					if(result.code===200){
						var dom=$("#dialog-modal-import");
						var screen=dom.find("[name='ti_screen_type']");
						screen.empty();
						result.list.forEach(function(n,i){	
							var param = n.sc_target.split(',');
							if(Number(task.field.mr_w)===Number(param[3]) && Number(task.field.mr_h)===Number(param[4])){	
								console.log(n);
								screen.append($("<option/>").html(n.sc_name).val(n.sc_info));
							}
						})
						dom.dialog({
							resizable: false, height: 210, width: 210, modal: true, autoOpen: "open", closeText: "hide",
				            buttons: {
				                "确定": function () {
				                	var screen=dom.serializeArray();
				                	screen.push({name:"ti_id",value:task.item.ti_id});
				                	$.post(task.url.importscreen,screen,function(data){
				                		if(data.code===200){
				                			Otess.utils.success("导入布局成功，页面将要重新加！");
				            				setTimeout("location.reload()", 2000); 
				                		}else{
				                			Otess.utils.fail(data.message);
				                		}
				                	})
				                },"关闭": function () {
				                	dom.dialog("destroy");
				                }
				            },
				            close: function (event, ui) {
			                	dom.dialog("destroy");
			                }
				        })
					}else{
						Otess.utils.fail("出现异常！");
					}
				})
				
				break;
		}
	}
	function editfile(options){
		var index=options.index,type=options.type;
		console.log(options);
		var title="编辑文件",width=922,height=679,position="file";	
		switch(type){
			case -8:
				title="编辑时间",width=459,height=459,position="time";
				break;
			case -9:
				title="编辑天气预报",width=459,height=512,position="weather";
				break;
			case -10:
				title="编辑数据库",width=459,height=459,position="db";
				break;
			case -11:
				title="编辑汇率",width=860,height=760,position="huilv";
				break;
			case -12:
				title="编辑字幕",width=479,height=679,position="txt";
				break;
			case -4:
				title="编辑直播",width=459,height=322,position="live";
				break;
			case -5:
				title="编辑链接",width=459,height=322,position="link";
				break;
			case 1:
				title="编辑文件",width=479,height=679,position="txt";
				break;
			case 3://修改图片
				title="编辑文件",width=459,height=322,position="file";
				break;
			default:
				title="编辑文件",width=459,height=322,position="file";
				break;
		}
		$.get(task.url.editfile,{tf_id:options.tf_id},function(result){
			if(type===-10){
				dbEditAction({title:title,width:width,height:height,result:result,tf_type:type});
			}else{
				var dom=$("#dialog-modal-com");
				dom.html(result);
				layout.setDosition($("[name='position']"));
				dom.dialog({
					title:title,resizable: false, width: width, height: height, modal: true, autoOpen: "open",
		            buttons: {
		                "确定": function () {
		                	if(type===-5 || type===-4){
		                		if(!Otess.utils.isUrl($("#url").val())){
		                			$("#url").parent().addClass('has-error');
		                			if(type===-5)Otess.utils.fail("链接地址非法");
		                			if(type===-4)Otess.utils.fail("直播地址非法");
		                			return false;
		                		}
		                	}
		                	var taskfile=dom.serializeArray();
		                	taskfile.push({name:"tf_task_item_id",value:task.item.ti_id})
		                	taskfile.push({name:"tf_duration",value:Otess.utils.getDuration(position)});
		                	taskfile.push({name:"tf_isadd",value:0});
		                	taskfile.push({name:"tf_type",value:type});
		                	taskfile.push({name:"tf_scale",value:layout.scale()});
		                	console.log(taskfile);
		                	//return false;
		                	layout.addFile(task.url.savefile,taskfile,function(data){
		                		if(data.code==200){
			                		task.file=data.list;
			                		layout.bindFile(data.list);
				                	dom.dialog("destroy");
									Otess.utils.success();
		                		}
		                	});
		                },"关闭": function () {
		                	dom.dialog("destroy");
		                }
		            },
		            close: function (event, ui) {}
		        })
			}
		})
	}
	$(document).on("click",".delscreen",function(){//删除布局
		var index=$(this).data("index");		
		$.smkConfirm({
	      text:task.prompt.deletescreen,
	      accept:'删除',
	      cancel:'取消'
	    },function(res){
	      if (res) {
	    	$("[data-index='"+index+"']").remove();
	    	$("[data-itemindex='"+index+"']").remove();
			//删除布局的资源
			layout.save(function(result){
				console.log(result);
				$.get(task.url.deleteFileByPosition,{ti_id:task.item.ti_id,tf_position:index},function(data){
					console.log(data);
				})
			});
	      }
	    });
	}).on("click",".editscreen",function(){//修改布局中文件属性
		var dom=$("#dialog-modal-editscreen");
		console.log(task);
		var screen=$("#default"+$(this).data("index"));
		var x=$("[name='x']"),y=$("[name='y']"),w=$("[name='width']"),h=$("[name='height']");
		x.val(screen.offset().left),y.val(screen.offset().top),w.val(screen.width()),h.val(screen.height());
		$("#maxwidth").html(layout.getWidth());
		$("#maxheight").html(layout.getHeight());
		dom.dialog({
			title:"编辑布局-default"+$(this).data("index"),resizable: false, autoOpen: "open", modal: true, width: 320, height: 320,
            buttons: {
                "确定": function () {
                	var left = parseInt(x.val()),
                		top = parseInt(y.val()),
                		width = parseInt(w.val()),
                		height = parseInt(h.val());
                    var s_width = parseInt(left) + parseInt(width);
                    var s_height = parseInt(top) + parseInt(height);
                    if (left < 0 || left > task.field.web_mr_w || 
                    		width > task.field.web_mr_w || width < 0 || s_width > task.field.web_mr_w) {
						Otess.utils.fail('窗口X坐标或者窗口宽度输入错误');
                        return false;
                    }
                    if (top < 0 || top > task.field.web_mr_h || 
                    		height > task.field.web_mr_h || height < 0 || s_height > task.field.web_mr_h) {
						Otess.utils.fail('窗口Y坐标或者窗口高度输入错误');
                        return false;
                    }
                    var datainfo = "default"+$(this).data("index") + " (" + left + "," + top + "," + width + "," + height + ")";

                    var a = screen.attr("data-info");
                    var b = screen.find(".coordinate").text();
                    if ($.trim(a) == $.trim(b)) {
                        screen.attr({ "data-info": datainfo }).find(".coordinate").text(datainfo);
                    }
                    screen.css({
                        left: left + "px", top: top + "px", overflow: "hidden"
                    }).width(width).height(height);
                    layout.save(function(result){
    					if(result.code==200){
    						Otess.utils.success('保存布局成功！');
    	                	dom.dialog("destroy");
    					}
    				});
                },"关闭": function () {
                	dom.dialog("destroy");
                }
            },
            close: function (event, ui) {}
        })
	}).on("dblclick",".itemchild div",function(){
		console.log({
			index:$(this).find(".editfile").data("itemindex"),
			type:$(this).find(".editfile").data("type"),
			tf_id:$(this).find("editfile").data("fileid")
		});
		editfile({
			index:$(this).find(".editfile").data("itemindex"),
			type:$(this).find(".editfile").data("type"),
			tf_id:$(this).find(".editfile").data("fileid")
		});
	}).on("click",".editfile",function(){
		editfile({
			index:$(this).data("itemindex"),
			type:$(this).data("type"),
			tf_id:$(this).data("fileid")
		});
	}).on("click",".deletefile",function(){//删除布局下资源
		var tf_id=$(this).data("fileid"),tf_position=$(this).data("itemindex");
		$.smkConfirm({
	      text:task.prompt.deletefile,
	      accept:'删除',
	      cancel:'取消'
	    },function(res){
	      if (res) {
	    	$.post(task.url.deletefile,{tf_id:tf_id},function(result){
				if(result.code==200){				
					$("div[data-fileid='"+tf_id+"']").remove();	
					$.get(task.url.loadfile,{tf_position:tf_position,tf_task_item_id:task.item.ti_id},function(json){
						console.log(json)
						if(json.code===200){
							if(json.list.length>0)layout.bindDefault(json.list[0]);
							else{
								layout.remove(tf_position);								
							}
						}
					})
				}
			})
	      }
	    });
	}).on("keydown",function(e){//鼠标移动
		layout.move(e);
	}).on("keyup",function(e){
		if (e.which == 37 || e.which == 38 || e.which == 39 || e.which == 40) {
			layout.save(function(result){
				console.log(result);
			});
        }
	})
	$(document).find("#right .itemchild").sortable({
        revert: true,
        axis: "y",
        helper: 'clone', cursor: 'move', //拖动的时候鼠标样式
        opacity: 0.6, change: function (event, ui) {
        }, update: function (event, ui) {
        	var tf_id_array=$(this).sortable("toArray")
        	var tf_id = tf_id_array.join(",");
            $.get(task.url.sortfile,{tf_id:tf_id},function(result){
            	//移动后，继续自动绑定第一个
            	var item=_.find(task.file, function(chr) {
                    return chr.tf_id==tf_id_array[0];
                  });
            	layout.bindDefault(item);
            })
        }
    });
	$(document).on("change","[name='position']",function(){
		if(Number($(this).val())){
			 $("#selectMusicGroup").hide();
		}else{
			 $("#selectMusicGroup").show();			
		}
	})
})