brotControllers.controller('PlaylistDetailController', 
  ['$scope', '$modal', '$routeParams', '$http', '$location', 'PlaylistService', 'HomeService', 'myCache',
                                       function ($scope, $modal, $routeParams, $http, $location, PlaylistService, HomeService, myCache) {


    var userId = localStorage.getItem('userId'); 

    init();

    function init(){
    }
    
}]);
