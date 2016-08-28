$(function(){
  var ajax=new Ajax();
  $("#task_t_loop :radio").on("click",function () {
      if($(this).val()==2){
    	  $(".date").hide();
          $("#week").hide();

      } else if ($(this).val() == 1) {
    	  $(".input_date").datetimepicker({
		    format:'H:i',
		    datepicker:false,
		    value:new Date()
		  });
    	  $(".date").show();
          $("#week").show();
      }
      else { 
    	  $(".input_date").datetimepicker({
  		    format:'Y-m-d H:i:s',
		    datepicker:true,
		    value:new Date()
  		  });
    	  $(".date").show();
          $("#week").hide();
      }
  });
  //验证提交
  $('#formRes').bootstrapValidator().on('success.form.bv', function(e) {
    e.preventDefault();
    var $form = $(e.target),
        validator = $form.data('bootstrapValidator'),
    	url=$form.attr("action");
    var data=$form.serializeArray();
    console.log(data);
    ajax.get(url,data,function(result){
    	alert(result.message);
    	$("#resmodal").modal('hide');
    })
  });
//验证提交
  $('#formTask').bootstrapValidator().on('success.form.bv', function(e) {
    e.preventDefault();
    var $form = $(e.target),
        validator = $form.data('bootstrapValidator'),
    	url=$form.attr("action");
    var data=$form.serializeArray();
    $("[name='task.t_weeks']").val($("[name='t_weeks']").CheckVal());
    ajax.post(url,data,function(result){
    	if(result.code==200){
    		location.href=$("#basepath").val()+result.message;
    	}else{
        	alert(result.message);
    	}
    })
  });
})
