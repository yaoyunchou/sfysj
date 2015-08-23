define(function(require,exports,module){
	require("jquery");
	var $=this.jQuery;
var max;
	$(document).ready(function () {
	  var xf_winwidth = $(window).width();
	if(xf_winwidth>1024){
		xf_winwidth=1024;
		$("html").css("fontSize",xf_winwidth/230*7+"px");
	}else{
		$("html").css("fontSize",xf_winwidth/230*7+"px");
	}
	var xf_winheight = $(window).height();
	
		
});
$(window).on('resize',function(){
	var xf_winwidth = $(window).width();
	if(xf_winwidth>1024){
		xf_winwidth=1024;
		$("html").css("fontSize",xf_winwidth/230*7+"px");
	}else{
		$("html").css("fontSize",xf_winwidth/230*7+"px");
	}
	var xf_winheight = $(window).height();
	$("html").css("fontSize",xf_winwidth/230*7+"px");
	
});
window.onload=function(){
    $(".loading").hide();
    $().yaotoch();
}
$('input').blur(function(){
	if($(this).val()){
		$(this).addClass("inputbler");
		
	}else{
		$(this).removeClass("inputbler");
	}
}).keyup(function(){
	if($(this).val()){
		$(this).addClass("inputbler");
	}else{
		$(this).removeClass("inputbler");
	}
})
if($('input').val()){
		$('input').addClass("inputbler");
		
	}
$(document).stop().scroll(function(){
     
	$('.header').css('top',$(this).scrollTop());
	
})





;(function($){
	$.fn.extend({
		//yaotochk开始
	
		"yaotoch":function(){
			var startX = 0;
			var startY = 0;
			var distX = 0;
			var distY = 0;
			var dist = 0; //手指滑动距离
			var isTouchPad = (/hp-tablet/gi).test(navigator.appVersion); //获取浏览器的信息     用正则检测是否含XX元素
			var hasTouch = 'ontouchstart' in window && !isTouchPad;//过滤浏览器类型
			var touchStart = hasTouch ? 'touchstart' : 'mousedown'; //如果浏览器有toch元素就用tocuh   否则就用mousedown
			//var touchMove = hasTouch ? 'touchmove' : 'mousemove';
			var touchMove = hasTouch ? 'touchmove' : '';
			var touchEnd = hasTouch ? 'touchend' : 'mouseup';
			var slideH=0;
			
			var twCell;
			var scrollY ;
			
			
			window.document.addEventListener(tStart,function(e){            
				
				
			})

			//触摸开始函数
				var tStart = function(e){
					
					scrollY = undefined;
					distX = 0;
					var point = hasTouch ? e.touches[0] : e;
					startX =  point.pageX;
					startY =  point.pageY;
			
			
			       console.log(startX);
			       $('.sx').html(startX);
			       $('.sy').html(startY);
			        console.log(startY);
					//添加“触摸移动”事件监听
					window.document.addEventListener(touchMove, tMove,false);
					//添加“触摸结束”事件监听
					window.document.addEventListener(touchEnd, tEnd ,false);
				}

				//触摸移动函数
				var tMove = function(e){
					if( hasTouch ){ if ( e.touches.length > 1 || e.scale && e.scale !== 1) return }; //多点或缩放
			
					var point = hasTouch ? e.touches[0] : e;
					distX = point.pageX-startX;
					distY = point.pageY-startY;
			
					if ( typeof scrollY == 'undefined') { scrollY = !!( scrollY || Math.abs(distX) < Math.abs(distY) ); }
					
					if(startX<100&&distX>0){
						console.log("yao");
						console.log("我的高度"+$(".all").height())
						$('.all').animate({'marginLeft':'50%'},200);
						
					}
					console.log(distX);
			        console.log(distY);
			         $('.mx').html(distX);
			       $('.my').html(distY);
				}

				//触摸结束函数
				var tEnd = function(e){
					if(distX==0) return;
					e.preventDefault(); 
					
			
					window.document.removeEventListener(touchMove, tMove, false);
					window.document.removeEventListener(touchEnd, tEnd, false);
					 console.log(startX);
			        console.log(startY);
			          $('.ex').html(startX);
			       $('.ey').html(startY);
				}
              
   
				//添加“触摸开始”事件监听
				window.document.addEventListener(touchStart, tStart ,false);
			
		}
		//yaotoch结束
		
		
	});
	
	
})(jQuery);
 
});