var pagetype=$("#filetype").val(),simple=$("#task_t_simple").val();
$(function(){  
  //验证提交
  $('#formRes').bootstrapValidator().on('success.form.bv', function(e) {
    e.preventDefault();
    var $form = $(e.target),
        validator = $form.data('bootstrapValidator'),
    	url=$form.attr("action");
    var data=$form.serializeArray();
    $.get(url,data,function(result){
    	console.log(result);
    	if(result.code===200){
			Otess.utils.success();
			setTimeout("location.reload()", 2000); //每隔5秒刷新点击量
    	}else{
			Otess.utils.fail(result.message);
    	}
    })
  });
  //验证提交
  $('#formTask').bootstrapValidator().on('success.form.bv', function(e) {
    e.preventDefault();
    var $form = $(e.target),
        validator = $form.data('bootstrapValidator'),
    	url=$form.attr("action");
    $("[name='task.t_weeks']").val($("[name='t_weeks']").CheckVal());
    var val=$('input:radio[name="task.t_loop"]:checked').val();
    var data=$form.serializeArray();
    var target=$("#formClickId").val();
    //判断是否为每日定时任务，自动加上日期
    
    data.forEach(function(n,i){    	
    	if(n.name==='task.t_begin_time' || n.name==='task.t_end_time'){
    		if(val==='1'){
    			n.value=moment().format("YYYY-MM-DD")+" "+n.value;
            }
        	n.value+=":00";
		}
	})
    $.post(url,data,function(result){
    	console.log(result);
    	if(result.code==200){
    		if(pagetype!=="edit"){
        		simple=$("#task_t_simple :radio:checked").val(); 
    		}
    		
			if(simple==="1"){
				if(pagetype==="edit"){
					Otess.utils.success();
					//点编辑任务的时候先保存在跳转到编辑页面
					if(target==="taskitemEdit"){
						$.get(task.url.taskitemfirst,{t_id:task.field.t_id},function(result){
						  	console.log(result);
							if(result.data){
								location.href=task.url.layout+"/"+task.field.t_id+"-"+result.data.ti_id;
							}
						})
					}else{
						setTimeout(function(){
							location.href=task.url.tasklist;
						}, 2000); //每隔5秒刷新点击量
					}
				}else{
					task.field=result.data;
					//简易模式
					if(task.field){
				        task.width=task.width-260;
						if (task.field.mr_w >= task.width) {//布局大于屏幕布局 进行缩放
					      var scale = (Number(task.field.mr_h) / Number(task.field.mr_w));
					      //task.field.mr_w = task.width;
					      //task.field.mr_h = Math.round(task.width * scale);
					      task.height=Math.round(task.width * scale);
					    }else{
					    	task.width=task.field.mr_w;
					    	task.height=task.field.mr_h;
					    }
						task.defaulttype = task.default0.replace('1024',task.field.mr_w ).replace('768',task.field.mr_h);//默认布局
						task.defaulttypeaffix = task.default0.replace('1024',task.width ).replace('768',task.height);//默认布局
						task.item={ti_task_id:task.field.t_id,ti_name:"默认名称",ti_duration:3600,ti_screen_type:task.defaulttype,ti_screen_type_affix:task.defaulttypeaffix};
						console.log(task);
						$.post(task.url.additem,task.item,function(result){
							task.item.ti_id=result.data;
							if(result.data){
								location.href=task.url.layout+"/"+task.field.t_id+"-"+result.data;
							}
						})
					}else{
						Otess.utils.fail("任务不存在")
					}
				}				
			}else{
				Otess.utils.success();
				setTimeout(function(){
					//
					if(pagetype!=="edit"){
						location.href=task.url.taskitem+result.data.t_id;						
					}else{
						if($("#t_status").val()==="2"){
							location.href=task.url.tasklist;
						}else{
							location.href=task.url.taskitem+task.field.t_id;
						}
					}
				}, 2000); //每隔5秒刷新点击量
			}
    	}else{
			Otess.utils.fail(result.message);
    	}
    })
  });
  $("#taskitemEdit").on("click",function(){
	  $("#formClickId").val("taskitemEdit");
  })
  //设置任务简易和高级
  $("#task_t_simple :radio").on("click",function () {
	var val=Number($(this).val());
	if(val===1)$("#taskitem").hide();
	else{
		$("#taskitem").show();
	}
  })
})
