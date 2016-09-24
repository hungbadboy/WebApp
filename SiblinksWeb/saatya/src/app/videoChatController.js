brotControllers.controller('VideoChatCtrl', ['$scope', '$routeParams', '$http', 'MentorService', '$location', 
                                       function ($scope, $routeParams, $http, MentorService, $location) {

  
  $scope.checkShool = $scope.checkName = $scope.checkMajor = 
  $scope.checkExtra  = $scope.checkTutor = true;
  $scope.busy = false;

  loadFunc = function(searchKey, page) {
      $scope.busy = true;

      if(page == null || page < 1 )
      {
        page = 0;
      }
      var a = [];
      if(searchKey !== null && searchKey !== undefined ) {
        if($scope.checkShool === true){
          a.push("school");
        }
        if($scope.checkName === true){
          a.push("firstName");
          a.push("lastName");
        }
        if($scope.checkMajor === true){
          //a.push("school");
        }
        if($scope.checkExtra === true){
          //a.push("school");
        }
        if($scope.checkTutor === true){
          //a.push("school");
        }
      }
      $scope.order = $scope.order !== undefined ? $scope.order : "points";
      // $location.path('/video_chat').search({'search': searchKey});
      MentorService.searchMentors(searchKey, a, $scope.order, page).then(function(value){
        if(value.data.status == "true"){
          // check exists point
          $.each(value.data.request_data_result, function(index, val) {
            if(val.point === undefined){
             val.point = 0;
            }
        });
        var items = value.data.request_data_result;
        var n = items.length;
        for (var i = 0; i < items.length; i++) {
          $scope.listMentor.push(items[i]);
        }
        if(n > 0) {
          $scope.busy = false;
        }
      }
    });
  };

  $scope.checkAll = function() {
    if($scope.checkShool & $scope.checkName 
     & $scope.checkMajor & $scope.checkExtra 
     & $scope.checkTutor) {

      $scope.checkShool = $scope.checkName = $scope.checkMajor = 
      $scope.checkExtra  = $scope.checkTutor = false;
    } else {
      $scope.checkShool = $scope.checkName = $scope.checkMajor = 
      $scope.checkExtra  = $scope.checkTutor = true;
    }
  };

  $scope.listMentor = [];

  $scope.searchFunc = function() {
     //$scope.checkAll();
     $scope.listMentor = [];
     loadFunc($scope.key, 1);
     $location.path('/video_chat').search({'search': $scope.key});
  };

  $scope.loadMore = function() {
    var searchKey = $routeParams.search;
    var n = $scope.listMentor.length / 9 + 1;
    if(n === +n && n === (n|0)) {
      loadFunc(searchKey === null ? "" : searchKey, n);
    }
  };

  $scope.filterFunc = function() {
     //$scope.checkAll();
    $scope.listMentor = [];
    var searchKey = $routeParams.search;
    loadFunc(searchKey === null ? "" : searchKey, 1);
  };

}]);