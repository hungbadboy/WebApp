var brotApp = angular.module('brotApp', [ 'ngRoute', 'brotControllers',
		'paginationListVideo', 'ngResource', 'brotServices',
		'angularFileUpload','ui.bootstrap'
//		, 'suggestSearch'
		]
);
var isToggleUserInfo= false;
var brotControllers = angular.module('brotControllers', ['infinite-scroll','stars','angular-sticky-kit']);

brotControllers.run(function ($templateCache) {
    $templateCache.put('testimonials', jQuery('#testimonials').html());
});

var brotServices = angular.module('brotServices', ['ngResource']);
//var suggestSearch = angular.module('suggestSearch', ['autocomplete']);

brotApp.controller('MainController', function($scope, $http, $location) {
	   //your code here
	  $scope.title = "Main";
	  $scope.idbody ="";
	  $scope.logined="";
	  $scope.currentPath="";
	  
	
	  var userId = "";
      var userType = "";
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
});

brotServices.factory('myCache', function($cacheFactory) {
	  return $cacheFactory('myData');
	});