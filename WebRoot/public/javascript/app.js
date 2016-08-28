function Ajax() {
}
Ajax.prototype = {    
    ajax: function (url, type, data, successfn) {
        $.ajax({
            url: url,
            type: type,
            data: data,
            dataType: "JSON",
            success: successfn,
            err: function (err) {
                alert("操作失败");
            }
        });
    },
    
    get: function (url, data, successfn) {
        this.ajax(url, "GET", data, successfn);
    },
        
    post: function (url, data, successfn) {
        this.ajax(url, "POST", data, successfn);
    },
}


$.fn.CheckVal = function (key) {
    var $ids = "";
    $(this).each(function (i) {
        if ($(this).is(':checked')) {
            if ($ids != "") $ids += ",";
            if(key==="value" || !key)
                $ids += $(this).val();
            else
            	$ids += $(this).data(key);
        }
    });

    return $ids;
};
$(function(){
  $(".input_date").datetimepicker({
    step:1,
    lang:'ch',
    format:'Y-m-d H:i'
  });
  /*$("[name='chkall']").click(function () {
	  if ($(this).attr("disabled") != "disabled") {
	    if ($(this).is(":checked")) {
	      $(":checkbox").prop("checked",true);
	    }else{
	      $(":checkbox").prop("checked",false);
	    }
      }
  });*/
  $("[name=chkall]").bind("click", function () {
      var chk = this.checked;
      $("[name = chk]:checkbox").each(function () {
          if ($(this).attr("disabled") != "disabled") {
              $(this).prop('checked', chk);
          }
      });
  });

  $("#delete").on("click",function(){
    var _ids = "";
    $("input[name='chk']").each(function () {
      if ($(this).is(":checked")) {
        if (_ids !== "") _ids += ",";
        _ids += $(this).val();
      }
    });
    if(_ids==="")return;
    if (confirm("确定要删除选中项吗？")) {
      location.href=$(this).data("url")+"?ids="+_ids;
    }
  });
  $("[data-toggle='tooltip']").tooltip();
  $(".refresh").on("click",function(){
	  location.reload();
  })
})