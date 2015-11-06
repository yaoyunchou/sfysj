define(function(require,exports,module){
	require("jquery");
	require("ag");
	var $=this.jQuery,jQuery=this.jQuery;
	
	
	//console.log(yao);
     
   var angular=this.angular;
  var yao = angular.module('myapp',吗 []);  
  

  yao.controller('PhoneListCtrl', ['$scope','$http' , function($scope,$http) {  

  yao.controller('BannerCtrl', ['$scope','$http' , function($scope,$http) {  

  	$scope.phones = [
		    {"name": "Nexus S",
		     "snippet": "Fast just got faster with Nexus S.","age":"30"},
		    {"name": "Motorola XOOM™ with Wi-Fi",
		     "snippet": "The Next, Next Generation tablet.","age":"20"},
		    {"name": "MOTOROLA XOOM™",
		     "snippet": "The Next, Next Generation tablet.","age":"22"}
		  ];        

	 $http.get('../php/postandget.php?url=https://www.p2pdi.com/appinterface/indexbanner.json').success(function(data) {
		    console.log(data);
		   $scope.phone = data;
		    
		  });
    $scope.hjf=

	 $http.get('https://www.p2pdi.com/appinterface/indexbanner.json').success(function(data) {
		    
		   $scope.hjfimgs = data.result.object;
		    console.log( $scope.hjfimgs);
		  });
  

		   $scope.orderProp = 'age';
  }]);  
 
});
