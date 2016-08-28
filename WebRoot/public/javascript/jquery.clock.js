(function($) {
  $.fn.jclock = function(options) {
    $.fn.jclock.timerID = null;
    $.fn.jclock.running = false;
    $.fn.jclock.el = null;
    var version = '0.1.1';
	// Download by http://www.codefans.net
    // options
    var opts = $.extend({}, $.fn.jclock.defaults, options);
         
    return this.each(function() {
      $this = $(this);
      $.fn.jclock.el = $this;

      var o = $.meta ? $.extend({}, opts, $this.data()) : opts;

      $.fn.jclock.withDate = o.withDate;
	  $.fn.jclock.withWeek = o.withWeek;
	  $.fn.jclock.withTime = o.withTime;
	  $.fn.jclock.timeNotation = o.timeNotation;
      $.fn.jclock.am_pm = o.am_pm;
	  $.fn.jclock.offset = o.offset;
	  $.fn.jclock.br = o.br;
	  $.fn.jclock.city = o.city;

      $this.css({
        fontFamily: o.fontFamily,
        fontSize: o.fontSize,
        backgroundColor: o.background,
        color: o.foreground,
		fontStyle: o.fontStyle,
		textAlign: o.textAlign,
		verticalAlign: o.verticalAlign
      });

      $.fn.jclock.startClock();

    });
  };
       
  $.fn.jclock.startClock = function() {
    $.fn.jclock.stopClock();
    $.fn.jclock.displayTime();
  }
  $.fn.jclock.stopClock = function() {
    if($.fn.jclock.running) {
      clearTimeout(timerID);
    }
    $.fn.jclock.running = false;
  }
  $.fn.jclock.displayTime = function(el) {
	var date = $.fn.jclock.getDate();
	var week = $.fn.jclock.getWeek();
	var time = $.fn.jclock.getTime();
	var datestring=date + week + time;
	if($.fn.jclock.br==true){
		datestring="";
		if(date!="")datestring+=(""+date+"<br>");
	    if(week!="")datestring+=(""+week+"<br>");
	    if(time!="")datestring+=(""+time+"<br>");
	}
    $.fn.jclock.el.html(datestring);
    timerID = setTimeout("$.fn.jclock.displayTime()",1000);
  }
  $.fn.jclock.getDate = function() {
	if($.fn.jclock.withDate == true) {
      var d = new Date();
      var year, month, date;
      
	  var utc = d.getTime() + (d.getTimezoneOffset() * 60000);
	  var now = new Date(utc + (3600000*$.fn.jclock.offset));

      year = now.getFullYear();
      month = now.getMonth() + 1;
      date = now.getDate();
      
      month = ((month < 10) ? "0" : "") + month;
      date = ((date < 10) ? "0" : "") + date;
      
      var dateNow = year + "年" + month + "月" + date + "日 ";
	} else {
      var dateNow = "";
	}

    return dateNow;
  }
  $.fn.jclock.getWeek = function() {
    if($.fn.jclock.withWeek == true) {
	  var d = new Date();
	  var utc = d.getTime() + (d.getTimezoneOffset() * 60000);
	  var now = new Date(utc + (3600000*$.fn.jclock.offset));
	  var week;
	  
	  week = now.getDay();
	  
	  $.each(["日","一","二","三","四","五","六"],function(i,n) {
        if(i == week) {week = n; return;}
	  });
	  
	  var weekNow = "星期" + week + " ";
	} else {
	  var weekNow = "";
	}
	return weekNow;
  }
  $.fn.jclock.getTime = function() {
	if($.fn.jclock.withTime == true){
		var d = new Date();
		var hours, minutes, seconds;
		var utc = d.getTime() + (d.getTimezoneOffset() * 60000);
		var now = new Date(utc + (3600000*$.fn.jclock.offset));

		hours = now.getHours();
		minutes = now.getMinutes();
		seconds = now.getSeconds();

		if ($.fn.jclock.timeNotation == '12h') {
		  hours = ((hours > 12) ? hours - 12 : hours);
		} else {
		  hours   = ((hours <  10) ? "0" : "") + hours;
		}

		minutes = ((minutes <  10) ? "0" : "") + minutes;
		seconds = ((seconds <  10) ? "0" : "") + seconds;

		var timeNow = hours + ":" + minutes + ":" + seconds;
		if ( ($.fn.jclock.timeNotation == '12h') && ($.fn.jclock.am_pm == true) ) {
		  timeNow += (hours >= 12) ? " P.M." : " A.M."
		}
	}else{
		var timeNow="";
	}

    return timeNow;
};
       
  // plugin defaults
  $.fn.jclock.defaults = {
	withDate:false,
	withWeek:false,
	withTime:false,
    timeNotation: '24h',
    am_pm: false,
    br: false,
	offset:'+08',	
	city:'',
    fontFamily: '',
    fontSize: '',
    foreground: '',
    background: '',
	fontStyle:'',
	textAlign:'',
	verticalAlign:''
  };
})(jQuery);
