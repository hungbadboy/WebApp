var brotApp = angular.module('brotApp', [ 'ngRoute', 'brotControllers',
		'paginationListVideo', 'ngResource', 'brotServices',
		'angularFileUpload','ui.bootstrap'
//		, 'suggestSearch'
		]
);

var brotControllers = angular.module('brotControllers', ['infinite-scroll','stars']);
var brotServices = angular.module('brotServices', ['ngResource']);
//var suggestSearch = angular.module('suggestSearch', ['autocomplete']);

brotApp.controller('MainController', function($scope, $http, $location) {
	   //your code here
	  $scope.title = "Main";
	  $scope.idbody ="";
	  $scope.isActive ="";
	  
	  $scope.$on('$routeChangeSuccess', function() {
          var path = $location.path();
          $scope.isActive = $location.path();
          if(path.indexOf('/mentor')>=0){
              $scope.idbody = 'mentor';
              return;
          }
      });
	
	  var userId = "";
      var userType = "";
      if (localStorage.getItem('userId') !== undefined || localStorage.getItem('userId') != 'undefined') {
          userId = localStorage.getItem('userId');
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
	  
	  // Check login
	  $scope.sidebarLeft="src/app/sidebarLeft/sidebarLeftMenu.tpl.html";
	  console.log('11');
	  $scope.sidebarRight="src/app/sidebarRight/sidebarRight.tpl.html";
	  $scope.topMentor="src/app/topMentor/topMentors.tpl.html";
	  $scope.topVideo="src/app/topVideo/topVideos.tpl.html";
});

brotServices.factory('myCache', function($cacheFactory) {
	  return $cacheFactory('myData');
	});