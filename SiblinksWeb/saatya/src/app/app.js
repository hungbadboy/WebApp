var brotApp = angular.module('brotApp', [ 'ngRoute', 'brotControllers',
		'paginationListVideo', 'ngResource', 'brotServices',
		'angularFileUpload','ui.bootstrap'
//		, 'suggestSearch'
		]
);

var brotControllers = angular.module('brotControllers', ['infinite-scroll','stars']);
var brotServices = angular.module('brotServices', ['ngResource']);
//var suggestSearch = angular.module('suggestSearch', ['autocomplete']);

brotApp.controller('MainController', function($scope, $http) {
	   //your code here
	  $scope.title = "Main";
});

brotServices.factory('myCache', function($cacheFactory) {
	  return $cacheFactory('myData');
	});