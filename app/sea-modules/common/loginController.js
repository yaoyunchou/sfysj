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
		    	 
		    },10);
		    
		  });
		  
    
		   //$scope.orderProp = 'age';
  }]);
 hjf.controller('IndexTenderCtrl', ['$scope','$http' , function($scope,$http) {  
  	
	 $http.get('../sea-modules/php/postandget.php?url=https://www.p2pdi.com/appinterface/index.json').success(function(data) {
		    
		    
		 
		   $scope.tenders = data.result.object;
		    //console.log( $scope.tenders);
		    setTimeout(function(){
		    	for(var i=0; i<$scope.tenders.length;i++){
		    	
		    		 jdt.yuancanvas("cv"+$scope.tenders[i].borrow_id,parseInt((($scope.tenders[i].borrow_money-$scope.tenders[i].tenderable_quota)/$scope.tenders[i].borrow_money)*100));
		    		
		    	}
		    	
		    },10); 
		    
		  });
          $scope.linkto=function(a){
           window.location.href="https://www.p2pdi.com/appinterface/projectdetail.json?borrow_id="+($scope.tenders[a].borrow_id);
          }
		
  }]);
    
});
