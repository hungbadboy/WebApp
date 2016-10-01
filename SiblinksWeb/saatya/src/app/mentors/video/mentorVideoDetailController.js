brotControllers.controller('MentorVideoDetailController', 
  ['$scope', '$modal', '$routeParams', '$http', '$location', 'VideoService', 'MentorService', 'myCache', 'HomeService',
                                       function ($scope, $modal, $routeParams, $http, $location, VideoService, MentorService, myCache, HomeService) {


    var userId = localStorage.getItem('userId');
    
    $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';

    init();

    function init(){
    }
    
}]);
