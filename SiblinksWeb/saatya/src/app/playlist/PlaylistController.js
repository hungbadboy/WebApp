brotControllers.controller('PlaylistController', ['$scope', '$modal', '$routeParams', '$http', '$location', 'PlaylistService', 
                                       function ($scope, $modal, $routeParams, $http, $location, PlaylistService) {


    var userId = localStorage.getItem('userId'); 
    init();

    function init(){
      loadPlaylist();
    }

    function loadPlaylist(){        
        PlaylistService.loadPlaylist(userId).then(function(data){
            if (data.data.status) {
               $scope.playlist = data.data.request_data_result;   
               console.log($scope.playlist); 
               if($scope.playlist == "No data found"){

               }
               else{
                 $scope.playlist = parseData($scope.playlist);
               }               
            }
        });
    }
    
    function parseData(data){
        for (var i = 0; i < data.length; i++) {
            data[i].numView = data[i].numView == null ? 0 : data[i].numView;
            data[i].numComment = data[i].numComment == null ? 0 : data[i].numComment;
            data[i].numLike = data[i].numLike == null ? 0 : data[i].numLike;
            data[i].numRate = data[i].numRate == null ? 0 : data[i].numRate;
            data[i].createDate = moment(data[i].createDate).format('MMMM Do YYYY, h:mm:ss a');
        }
        return data;
    }


    $scope.addNew = function(){
      var modalInstance = $modal.open({
          templateUrl: 'src/app/playlist/addPlaylist.tpl.html',
          controller: 'AddPlaylistController'
      });
    }

    $scope.update = function(plid){
      var modalInstance = $modal.open({
          templateUrl: 'src/app/playlist/updatePlaylist.tpl.html',
          controller: 'updatePlaylistController',
          resolve:{
            pl_id: function(){
              return plid;
            }
          }
      });
    }

    $scope.delete = function(id){
      if (confirm("Are you sure?")) {
        PlaylistService.deletePlaylist(id).then(function(data){
            if (data.data.status) {
               loadPlaylist();         
            }
        });
    }
  }
}]);
