$(function(){
  var save=function(url,data){
	  $.post(url,data,function(result){
		  if(data.type===4 || data.type===7 || data.type===11){
			  Otess.utils.success(result.message);
			  setTimeout("location.reload()", 2000); //2秒刷新点击量
		  }else{
			  Otess.utils.success(result.message);
		  }
	  })
  }
  $(document).on("click",".group",function(){
	  var clientids=$("[name='chk']").CheckVal();
  	  if(!clientids){
		  Otess.utils.fail("请选择要发送的终端!");
		  return false;
	  }
  	  
	  $("#groupmodal").modal().find("[type='submit']").on("click",function(e){
		  e.preventDefault();
		  $.post($(this).data("url"),{clientids:clientids,foldid:$("#group").val()},function(result){
			  console.log(result);
			  if(result.code==200){
				  Otess.utils.success(result.message);
				  setTimeout("location.reload()", 2000); //2秒刷新点击量
			  }else{
				  Otess.utils.fail("操作失败!");
			  }
		  })
	  });
  })
  $(document).on("click",".dropdown-menu li .slider-track",function(){
  	//slider-track-high
  	//slider-selection
	
  	var data={
			clientids:$("[name='chk']").CheckVal("deviceid"),
			ids:$("[name='chk']").CheckVal("value"),
			type:$("#ex1").data("type"),
			param:$("#ex1").val()
	}
  	if(!data.clientids){
		  Otess.utils.fail("请选择要发送的终端!");
		  return false;
  	}
  	save($("#ex1").data("url"),data);
  })
  $("#send,#searchtask").on("click",function(e){
	  var clientids=$("[name='chk']").CheckVal();
  	  if(!clientids){
		  Otess.utils.fail("请选择要发送的终端!");
		  return false;
	  }
  	  if(e.target.id==="send")$("#sendmodal").modal("show");
	  loadTask({url:$(this).data("url"),data:{t_name:$("[name='t_name']").val()}});
  });
  function loadTask(options){
	  $.get(options.url,options.data,function(result){
		  if(result.code==200){
              $("#sendTask tbody").empty('');
              result.list.forEach(function(n,i){
            	  if(n.t_loop==2){
            		  n.t_begin_time="--";
            		  n.t_end_time="--";
            	  }else if(n.t_loop==1){
            		  n.t_begin_time=moment(n.t_begin_time).format("HH:mm");
            		  n.t_end_time=moment(n.t_end_time).format("HH:mm");
            	  }else{
            		  n.t_begin_time=moment(n.t_begin_time).format("YYYY-MM-DD HH:mm");
            		  n.t_end_time=moment(n.t_end_time).format("YYYY-MM-DD HH:mm");
            	  }
              })
              $("#client_tpl")
                      .tmpl(result.list)
                      .appendTo("#sendTask tbody");
              $("#pager").empty();
              $("#sendTask").footable().trigger('footable_resize');

		  }
	  })
  }
  $("#okSend").on("click",function(e){
	  e.preventDefault();
	  	var redirect=$(this).data('redirect'),clientids=$("[name='chk']").CheckVal();
//    	$.post($(this).data("url"),{
//    			taskid:$("input[name='taskid']:checked").val(),
//    			clientids:clientids,
//    			sendtype:$("input[name='sendtype']").val(),sendmethod:0
//    		},function(result){
//    		console.log(result);
//    		if(result.code==200){
//    			Otess.utils.success("发送成功!");
//				setTimeout(function(){location.href=redirect;}, 2000); //每隔5秒刷新点击量
//    		}else{
//    			Otess.utils.fail("发送失败!");
//    		}
//    	})
    	var task=$("#form-send-task").serializeArray();
    	task.push({name:"taskid",value:$("input[name='taskid']:checked").val()});
    	task.push({name:"clientids",value:clientids});
    	task.push({name:"sendmethod",value:0});

		$.post($(this).data("url"),task,function(result){
			if(result.code!=200){
				Otess.utils.fail("请选择要发送的终端！")
			}else{
				Otess.utils.success("发送成功！");
				setTimeout(function(){
					location.href=redirect;
				}, 2000);

			}
		})
  })
  //单个取消定时关机
  $(document).on("click",".cancelshuttime",function(){
		var data={
			clientids:$(this).data("deviceid"),
			ids:$(this).data("id"),
			type:$(this).data("type"),
			param:0
		}
		if(!data.clientids){
			Otess.utils.fail("请选择终端!");
    		return ;
    	}
		if(confirm($(this).data("confirm"))){
			save($(this).data("url"),data);
		}
		
  })

  $(".client").find(".command").each(function(){
    var el=$(this);
    el.on("click",function(){
    	var url=$(this).data("url");
    	var info=$(this).data("confirm");
    	var data={
    			clientids:$("[name='chk']").CheckVal("deviceid"),
    			ids:$("[name='chk']").CheckVal("value"),
    			type:$(this).data("type"),
    			param:0
    	}
    	if(!data.clientids){
			Otess.utils.fail("请选择终端!");
    		return ;
    	}
    	console.log(data);
    	switch(data.type){
	    	case 4:
	    		$("#shuttime").datetimepicker({
	    			format:'H:i',
	    			step:1,
	    		    datepicker:false,
	    		});
	    		$("#shuttimemodal").modal().find("[type='submit']").on("click",function(e){
	    			e.preventDefault();
	    			data.param=Otess.utils.getTimeToDuration($("#shuttime").val());
	    			save(url,data);
	    		})
	    		break;
	    	case 5:
	    		data.param=20;
	    		save(url,data);
    		default:
    	    	if(confirm(info)){
    	    		console.log(data);
	    			save(url,data);
    	    	}
    			break;
    	}
    });
  });
})