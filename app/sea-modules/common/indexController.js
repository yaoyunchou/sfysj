define(function(require,exports,module){
	require("jquery");
	require("ag");
	var b = require("common/base")
	
	var $=this.jQuery,jQuery=this.jQuery;
	
	console.log($)
	//console.log(yao);
     
   var angular=this.angular;
  var hjf = angular.module('myapp', []);  
  
  hjf.controller('BannerCtrl', ['$scope','$http' , function($scope,$http) {  
  	
	 $http.get('../sea-modules/php/postandget.php?url=https://www.p2pdi.com/appinterface/indexbanner.json').success(function(data) {
		    
		   $scope.hjfimgs = data.result.object;
		    console.log($scope.hjfimgs.length);
		    var  mudo=1;
		   
		    setTimeout(function(){
		    	
		    	mudo = b.mydo();
		    	console.log(mudo);
		    	 jdt.yuancanvas("jdt",80);
		    },500);
		    
		  });
    
		   //$scope.orderProp = 'age';
  }]);
 hjf.controller('IndexTenderCtrl', ['$scope','$http' , function($scope,$http) {  
  	
	 $http.get('../sea-modules/php/postandget.php?url=https://www.p2pdi.com/appinterface/index.json').success(function(data) {
		    
		    
		    $scope.$watch('ng-cloak',myshow);
		    var myshow=function(){
		    	alert("完成了!");
		    }
		   $scope.tenders = data.result.object;
		    //console.log( $scope.tenders);
		    
		    
		  });
    
		   //$scope.orderProp = 'age';
  }]);
});
