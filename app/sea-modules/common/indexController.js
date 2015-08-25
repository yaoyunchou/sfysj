define(function(require,exports,module){
	//require("jquery");
	require("ag");
	
	
	var $=this.jQuery,jQuery=this.jQuery;
	
	console.log($)
	//console.log(yao);
     
   var angular=this.angular;
  var hjf = angular.module('myapp', []);  
  
  hjf.controller('BannerCtrl', ['$scope','$http' , function($scope,$http) {  
  	
	 $http.get('../sea-modules/php/postandget.php?url=https://www.p2pdi.com/appinterface/indexbanner.json').success(function(data) {
		    
		   $scope.hjfimgs = data.result.object;
		    
		  });
    
		   //$scope.orderProp = 'age';
  }],function(){
  	var b = require('common/base')
  	
  	
  });
 
});
