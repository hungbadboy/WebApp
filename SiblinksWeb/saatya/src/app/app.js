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
	  $scope.$on('$routeChangeSuccess', function() {
          var path = $location.path();
          if(path.indexOf('/mentor')>=0){
              $scope.idbody = 'mentor';
              return;
          }
        $scope.isActive = $location.path();
      });
	  // Check login
	  $scope.sidebarLeft="src/app/sidebarLeft/sidebarLeftMenu.tpl.html";
	  $scope.sidebarRight="src/app/sidebarRight/sidebarRight.tpl.html";
});

brotServices.factory('myCache', function($cacheFactory) {
	  return $cacheFactory('myData');
	});