var brotApp = angular.module('brotApp', [ 'ngRoute', 'brotControllers',
		'paginationListVideo', 'ngResource', 'brotServices',
		'ngFileUpload','ui.bootstrap','ngMask'
//		, 'suggestSearch'
		]
);
var isToggleUserInfo= false;
var brotControllers = angular.module('brotControllers', ['infinite-scroll','stars','angular-sticky-kit']);

brotControllers.run(function ($templateCache) {
    $templateCache.put('testimonials', jQuery('#testimonials').html());
});

//$location.path('/sampleurl', false); to prevent reloading
brotControllers.run(['$route', '$rootScope', '$modalStack', '$location', function ($route, $rootScope, $modalStack, $location) {
    $rootScope.$on('$locationChangeStart', function (event) {
        var top = $modalStack.getTop();
        if (top) {
            $modalStack.dismiss(top.key);
            event.preventDefault();
        }
    });
	var original = $location.path;
	$location.path = function (path, reload) {
		if (reload === false) {
			var lastRoute = $route.current;
			var un = $rootScope.$on('$locationChangeSuccess', function () {
				$route.current = lastRoute;
				un();
			});
		}
		return original.apply($location, [path]);
	};
	// diplay name user
	 $rootScope.displayUserName = function(firstName, lastName, userName) {
		 displayUserName(firstName, lastName, userName);
     };
     
}])

var brotServices = angular.module('brotServices', ['ngResource']);
//var suggestSearch = angular.module('suggestSearch', ['autocomplete']);

brotApp.controller('MainController', function($scope, $http, $location, HomeService) {
	   //your code here
	  $scope.title = "Main";
	  $scope.idbody ="";
	  $scope.logined="";
	  $scope.currentPath="";
	  
	
	  var userId = "";
      var userType = "";
      // Initial get Category
      init();
      
      if (localStorage.getItem('userId') !== undefined || localStorage.getItem('userId') != 'undefined') {
          userId = localStorage.getItem('userId');
          $scope.logined = true;
      } else {
    	  $scope.logined = false;
      }
      
      if (localStorage.getItem('userType') !== undefined || localStorage.getItem('userType') != 'undefined') {
      	userType = localStorage.getItem('userType');
      }
      
      if (localStorage.getItem('userType') !== undefined || localStorage.getItem('userType') != 'undefined') {
        	userType = localStorage.getItem('userType');
        }
      
      if (userId != null) {
	      if (userType != null && userType != undefined && userType == 'S') {
	      	$scope.headerByUser="src/app/header/StudentHeader.tpl.html";
	      	$scope.footerUser="src/app/footer/footer.tpl.html";
	      } else if (userType != null && userType != undefined && userType == 'M') {
	      	$scope.headerByUser="src/app/header/MentorHeader.tpl.html";
	      }
	  } else {
	  	// User is not yet login
	  	$scope.headerByUser="src/app/header/CommonHeader.tpl.html";
	  	$scope.footerUser="src/app/footer/footer.tpl.html";
	      
	  }
       
      $scope.$on('$routeChangeSuccess', function() {
          var path = $location.path();
          $scope.currentPath = $location.path();
          if(userType=='M'){
              $scope.idbody = 'mentor';
              if($location.path() == "/") {
            	  window.location.href ='#/mentor/dashboard';
              }
              return;
          } else {
        	  $scope.idbody = '';
          }
          
      });
      
	  // Check login
	  $scope.sidebarLeft="src/app/sidebarLeft/sidebarLeftMenu.tpl.html";
	  $scope.sidebarRight="src/app/sidebarRight/sidebarRight.tpl.html";
	  $scope.topMentor="src/app/topMentor/topMentors.tpl.html";
	  $scope.topVideo="src/app/topVideo/topVideos.tpl.html";
	  $scope.subscribed="src/app/subscribed/subscribed.tpl.html";
	  
	  function init() {
          if (localStorage.getItem('subjects') == null || localStorage.getItem('subjects') === undefined ) {
              HomeService.getAllCategory().then(function (data) {
                  if (data.data.status) {
                      //console.log("Get service subject with category");
                      setStorage('subjects',JSON.stringify(data.data.request_data_result), 30);
                  }
              });
          }
          
	  }
	  
});

brotServices.factory('myCache', function($cacheFactory) {
	  return $cacheFactory('myData');
	});