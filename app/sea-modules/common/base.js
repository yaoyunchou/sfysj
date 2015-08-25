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

/*这个是加载完毕后运行*/
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
//$(document).stop().scroll(function(){
//   
//	$('.header').css('top',$(this).scrollTop());
//	
//})




//   封装的手指滑动事件
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
			
			
			      /* console.log(startX);
			       $('.sx').html(startX);
			       $('.sy').html(startY);
			        console.log(startY);*/
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
					
					if(startX<50&&distX>0){
						/*console.log("yao");
						console.log("我的高度"+$(".all").height())*/
						$('.all').animate({'marginLeft':'50%'},20);
						$(".header").animate({'marginLeft':'50%'},20);
						$('.leftnav').css("position","fixed").animate({'left':'0'},20);
					}else if(distX<0&&Math.abs(distX)>=Math.abs(distY)){
						$('.leftnav').animate({'left':'-50%'},20);
						$('.all').animate({'marginLeft':'0'},20);
						$(".header").animate({'marginLeft':'0'},20);
						
					}
					/*console.log(distX);
			        console.log(distY);
			         $('.mx').html(distX);
			       $('.my').html(distY);*/
				}

				//触摸结束函数
				var tEnd = function(e){
					if(distX==0) return;
					e.preventDefault(); 
					
			
					window.document.removeEventListener(touchMove, tMove, false);
					window.document.removeEventListener(touchEnd, tEnd, false);
					/* console.log(startX);
			        console.log(startY);
			          $('.ex').html(startX);
			       $('.ey').html(startY);*/
				}
              
   
				//添加“触摸开始”事件监听
				window.document.addEventListener(touchStart, tStart ,false);
			
		}
		//yaotoch结束
		
		
	});
	
	
})(jQuery);

//这个是canvas 环形进度条封装
(function(window,undefined){
	
	  
	  //定义进度条对象 版本  作者  时间
	 var jdt = window.jdt={
	 	vesition:"1.0",
	 	anther:'yyc',
	 	time:2015-6-6,
	 	yuancanvas : function(id,num){
	 		
	 		var can = document.getElementById(id);
	 		//console.log(6*window.innerWidth/230*7);
	 		var x=6.3*window.innerWidth/230*7<150?6.3*window.innerWidth/230*7:150;
	 		console.log(window.innerWidth);
	 		var y=can.parentNode.clientHeight;
	 		var canvas = can.getContext("2d");
	 		
	 		canvas.save();//先保存
	 		canvas.beginPath();
	 		
	 		/*canvas.arc(x/2,y/2,x/2-10,0,Math.PI*2,false);
	 		canvas.lineWidth=10;*/
	 		canvas.strokeStyle="#efeef3";
	 		canvas.stroke();
	 		canvas.font=x/5+"pt 微软雅黑";
	 		canvas.fillStyle="#1c8ad7";
	 		canvas.textAlign="center";
	 		
	 		canvas.textBaseline="center";
	 		canvas.fillText(num+"%",x/2,x/2+5)
	 		canvas.beginPath();
	 		//canvas.rotate(-Math.PI)
	 		canvas.arc(x/2,x/2,x/2-10,Math.PI*3/2,Math.PI*(2*num/100-0.5),false);
	 		canvas.lineWidth=x/20;
	 		canvas.strokeStyle="#1c8ad7";
	 		canvas.stroke();
	 		console.log(x+"------"+y);
	 		
	 	},
	 	myalert:function(){
	 		alert("!!!");
	 	},
	 };
	 // 输入对应id  中间的显示数字num  
	
	 
	
	 
	
})(window)


//轮番图
function Swipe(container, options) {

  "use strict";

  // utilities
  var noop = function() {}; // simple no operation function
  var offloadFn = function(fn) { setTimeout(fn || noop, 0) }; // offload a functions execution
  
  // check browser capabilities
  var browser = {
    addEventListener: !!window.addEventListener,
    touch: ('ontouchstart' in window) || window.DocumentTouch && document instanceof DocumentTouch,
    transitions: (function(temp) {
      var props = ['transformProperty', 'WebkitTransform', 'MozTransform', 'OTransform', 'msTransform'];
      for ( var i in props ) if (temp.style[ props[i] ] !== undefined) return true;
      return false;
    })(document.createElement('swipe'))
  };

  // quit if no root element
  if (!container) return;
  var element = container.children[0];
  var slides, slidePos, width;
  options = options || {};
  var index = parseInt(options.startSlide, 10) || 0;
  var speed = options.speed || 300;
  options.continuous = options.continuous ? options.continuous : true;

  function setup() {

    // cache slides
    slides = element.children;

    // create an array to store current positions of each slide
    slidePos = new Array(slides.length);

    // determine width of each slide
    width = container.getBoundingClientRect().width || container.offsetWidth;

    element.style.width = (slides.length * width) + 'px';

    // stack elements
    var pos = slides.length;
    while(pos--) {

      var slide = slides[pos];

      slide.style.width = width + 'px';
      slide.setAttribute('data-index', pos);

      if (browser.transitions) {
        slide.style.left = (pos * -width) + 'px';
        move(pos, index > pos ? -width : (index < pos ? width : 0), 0);
      }

    }

    if (!browser.transitions) element.style.left = (index * -width) + 'px';

    container.style.visibility = 'visible';

  }

  function prev() {

    if (index) slide(index-1);
    else if (options.continuous) slide(slides.length-1);

  }

  function next() {

    if (index < slides.length - 1) slide(index+1);
    else if (options.continuous) slide(0);

  }

  function slide(to, slideSpeed) {

    // do nothing if already on requested slide
    if (index == to) return;
    
    if (browser.transitions) {

      var diff = Math.abs(index-to) - 1;
      var direction = Math.abs(index-to) / (index-to); // 1:right -1:left

      while (diff--) move((to > index ? to : index) - diff - 1, width * direction, 0);

      move(index, width * direction, slideSpeed || speed);
      move(to, 0, slideSpeed || speed);

    } else {

      animate(index * -width, to * -width, slideSpeed || speed);

    }

    index = to;

    offloadFn(options.callback && options.callback(index, slides[index]));

  }

  function move(index, dist, speed) {

    translate(index, dist, speed);
    slidePos[index] = dist;

  }

  function translate(index, dist, speed) {

    var slide = slides[index];
    var style = slide && slide.style;

    if (!style) return;

    style.webkitTransitionDuration = 
    style.MozTransitionDuration = 
    style.msTransitionDuration = 
    style.OTransitionDuration = 
    style.transitionDuration = speed + 'ms';

    style.webkitTransform = 'translate(' + dist + 'px,0)' + 'translateZ(0)';
    style.msTransform = 
    style.MozTransform = 
    style.OTransform = 'translateX(' + dist + 'px)';

  }

  function animate(from, to, speed) {

    // if not an animation, just reposition
    if (!speed) {
      
      element.style.left = to + 'px';
      return;

    }
    
    var start = +new Date;
    
    var timer = setInterval(function() {

      var timeElap = +new Date - start;
      
      if (timeElap > speed) {

        element.style.left = to + 'px';

        if (delay) begin();

        options.transitionEnd && options.transitionEnd.call(event, index, slides[index]);

        clearInterval(timer);
        return;

      }

      element.style.left = (( (to - from) * (Math.floor((timeElap / speed) * 100) / 100) ) + from) + 'px';

    }, 4);

  }

  // setup auto slideshow
  var delay = options.auto || 0;
  var interval;

  function begin() {

    interval = setTimeout(next, delay);

  }

  function stop() {

    delay = 0;
    clearTimeout(interval);

  }


  // setup initial vars
  var start = {};
  var delta = {};
  var isScrolling;      

  // setup event capturing
  var events = {

    handleEvent: function(event) {

      switch (event.type) {
        case 'touchstart': this.start(event); break;
        case 'touchmove': this.move(event); break;
        case 'touchend': offloadFn(this.end(event)); break;
        case 'webkitTransitionEnd':
        case 'msTransitionEnd':
        case 'oTransitionEnd':
        case 'otransitionend':
        case 'transitionend': offloadFn(this.transitionEnd(event)); break;
        case 'resize': offloadFn(setup.call()); break;
      }

      if (options.stopPropagation) event.stopPropagation();

    },
    start: function(event) {

      var touches = event.touches[0];

      // measure start values
      start = {

        // get initial touch coords
        x: touches.pageX,
        y: touches.pageY,

        // store time to determine touch duration
        time: +new Date

      };
      
      // used for testing first move event
      isScrolling = undefined;

      // reset delta and end measurements
      delta = {};

      // attach touchmove and touchend listeners
      element.addEventListener('touchmove', this, false);
      element.addEventListener('touchend', this, false);

    },
    move: function(event) {

      // ensure swiping with one touch and not pinching
      if ( event.touches.length > 1 || event.scale && event.scale !== 1) return

      if (options.disableScroll) event.preventDefault();

      var touches = event.touches[0];

      // measure change in x and y
      delta = {
        x: touches.pageX - start.x,
        y: touches.pageY - start.y
      }

      // determine if scrolling test has run - one time test
      if ( typeof isScrolling == 'undefined') {
        isScrolling = !!( isScrolling || Math.abs(delta.x) < Math.abs(delta.y) );
      }

      // if user is not trying to scroll vertically
      if (!isScrolling) {

        // prevent native scrolling 
        event.preventDefault();

        // stop slideshow
        stop();

        // increase resistance if first or last slide
        delta.x = 
          delta.x / 
            ( (!index && delta.x > 0               // if first slide and sliding left
              || index == slides.length - 1        // or if last slide and sliding right
              && delta.x < 0                       // and if sliding at all
            ) ?                      
            ( Math.abs(delta.x) / width + 1 )      // determine resistance level
            : 1 );                                 // no resistance if false
        
        // translate 1:1
        translate(index-1, delta.x + slidePos[index-1], 0);
        translate(index, delta.x + slidePos[index], 0);
        translate(index+1, delta.x + slidePos[index+1], 0);

      }

    },
    end: function(event) {

      // measure duration
      var duration = +new Date - start.time;

      // determine if slide attempt triggers next/prev slide
      var isValidSlide = 
            Number(duration) < 250               // if slide duration is less than 250ms
            && Math.abs(delta.x) > 20            // and if slide amt is greater than 20px
            || Math.abs(delta.x) > width/2;      // or if slide amt is greater than half the width

      // determine if slide attempt is past start and end
      var isPastBounds = 
            !index && delta.x > 0                            // if first slide and slide amt is greater than 0
            || index == slides.length - 1 && delta.x < 0;    // or if last slide and slide amt is less than 0
      
      // determine direction of swipe (true:right, false:left)
      var direction = delta.x < 0;

      // if not scrolling vertically
      if (!isScrolling) {

        if (isValidSlide && !isPastBounds) {

          if (direction) {

            move(index-1, -width, 0);
            move(index, slidePos[index]-width, speed);
            move(index+1, slidePos[index+1]-width, speed);
            index += 1;

          } else {

            move(index+1, width, 0);
            move(index, slidePos[index]+width, speed);
            move(index-1, slidePos[index-1]+width, speed);
            index += -1;

          }

          options.callback && options.callback(index, slides[index]);

        } else {

          move(index-1, -width, speed);
          move(index, 0, speed);
          move(index+1, width, speed);

        }

      }

      // kill touchmove and touchend event listeners until touchstart called again
      element.removeEventListener('touchmove', events, false)
      element.removeEventListener('touchend', events, false)

    },
    transitionEnd: function(event) {

      if (parseInt(event.target.getAttribute('data-index'), 10) == index) {
        
        if (delay) begin();

        options.transitionEnd && options.transitionEnd.call(event, index, slides[index]);

      }

    }

  }

  // trigger setup
  setup();

  // start auto slideshow if applicable
  if (delay) begin();


  // add event listeners
  if (browser.addEventListener) {
    
    // set touchstart event on element    
    if (browser.touch) element.addEventListener('touchstart', events, false);

    if (browser.transitions) {
      element.addEventListener('webkitTransitionEnd', events, false);
      element.addEventListener('msTransitionEnd', events, false);
      element.addEventListener('oTransitionEnd', events, false);
      element.addEventListener('otransitionend', events, false);
      element.addEventListener('transitionend', events, false);
    }

    // set resize event on window
    window.addEventListener('resize', events, false);

  } else {

    window.onresize = function () { setup() }; // to play nice with old IE

  }

  // expose the Swipe API
  return {
    setup: function() {

      setup();

    },
    slide: function(to, speed) {

      slide(to, speed);

    },
    prev: function() {

      // cancel slideshow
      stop();

      prev();

    },
    next: function() {

      stop();

      next();

    },
    getPos: function() {

      // return current index position
      return index;

    },
    kill: function() {

      // cancel slideshow
      stop();

      // reset element
      element.style.width = 'auto';
      element.style.left = 0;

      // reset slides
      var pos = slides.length;
      while(pos--) {

        var slide = slides[pos];
        slide.style.width = '100%';
        slide.style.left = 0;

        if (browser.transitions) translate(pos, 0, 0);

      }

      // removed event listeners
      if (browser.addEventListener) {

        // remove current event listeners
        element.removeEventListener('touchstart', events, false);
        element.removeEventListener('webkitTransitionEnd', events, false);
        element.removeEventListener('msTransitionEnd', events, false);
        element.removeEventListener('oTransitionEnd', events, false);
        element.removeEventListener('otransitionend', events, false);
        element.removeEventListener('transitionend', events, false);
        window.removeEventListener('resize', events, false);

      }
      else {

        window.onresize = null;

      }

    }
  }

}


if ( window.jQuery || window.Zepto ) {
  (function($) {
    $.fn.Swipe = function(params) {
      return this.each(function() {
        $(this).data('Swipe', new Swipe($(this)[0], params));
      });
    }
  })( window.jQuery || window.Zepto )
}

exports.mydo=function(){
	
	if($('.swipe-wrap figure').length>1){
	var slider =Swipe(document.getElementById('slider'), { // 最大盒子的id
	    auto: 3000,// 3秒 自动切换
	    continuous: true,
	    callback: function(pos) {  // 返回值  pos 是返回当前的索引值
	
	    	//console.log(pos);
	    	for(var i=0;i<=circle.length-1;i++)// 遍历数组
	    	{
	    		circle[i].className="";  //  其余的所有li 清除 current类
	    		circle[pos].className="current";// 只留下当前的这个li添加current
	    	}
	
	    
	    }
	  });
	var circle = document.getElementById('circle').getElementsByTagName('li');//获取li 的数组
	return 0;
}

	
	
}


});