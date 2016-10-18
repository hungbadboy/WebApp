brotControllers.controller('MentorPlaylistDetailController', 
  ['$rootScope', '$scope', '$modal', '$routeParams', '$http', '$location', 'PlaylistService', 'VideoService',
                                       function ($rootScope, $scope, $modal, $routeParams, $http, $location, PlaylistService, VideoService) {

    var plid = $routeParams.plid;
    var userId = localStorage.getItem('userId'); 

    init();

    function init(){
    	// check plid is interger or not
    	if (!isNaN(plid) && plid > 0) {
    		// load playlist information
    		loadPlaylistDetail();
    		getVideosInPlaylist();
    	} else{
    		// reload to home
    		window.location.href = '#/mentor/dashboard';
        	window.location.reload();
    	}
    }
    
    function loadPlaylistDetail(){
    	PlaylistService.loadPlaylistById(plid).then(function(data){
    		if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
    			var data = data.data.request_data_result;
    			data.timeStamp = convertUnixTimeToTime(data.timeStamp); 
    			$scope.playlist = data;
    		}
    	});
    }

    function getVideosInPlaylist(){
    	PlaylistService.getVideoInPlaylist(plid, 0).then(function (data) {
    		if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
    			$scope.videos = formatTime(data.data.request_data_result);
    		} else
                $scope.videos = null;
    	});
    }

    $scope.loadMoreVideoInPLaylist = function(){
        var offset = 0;
        if ($scope.videos)
            offset = $scope.videos.length;

        PlaylistService.getVideoInPlaylist(plid, offset).then(function (data) {
            if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
                var oldArr = $scope.videos;
                var newArr = formatTime(data.data.request_data_result);
                $scope.videos = oldArr.concat(newArr);
            }
        });
    }

    function formatTime(data) {
        for (var i = data.length - 1; i >= 0; i--) {
            data[i].timeStamp = convertUnixTimeToTime(data[i].timeStamp);
            data[i].selected = false;
        }
        return data;
    }

    $scope.editPlaylist = function(plid){
        var modalInstance = $modal.open({
        templateUrl: 'src/app/mentors/playlist/addUpdatePlaylist.tpl.html',
        controller: 'AddUpdatePlaylistController',
        resolve:{
          pl_id: function(){
            return plid;
          },
          v_ids: function(){
            return null;
          }
        }
      });
    }

    $scope.delete = function(vid){
        var selectedVideos = [];
        selectedVideos.push(vid);
        $rootScope.$broadcast('open');
        PlaylistService.deleteVideoInPlaylist(selectedVideos).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result.length > 0) {
            getVideosInPlaylist();
          }
          $rootScope.$broadcast('close');
        });
    }

    $scope.openEdit = function(vid){        
        var modalInstance = $modal.open({
            templateUrl: 'src/app/mentors/video/upload_tutorial_popup.tpl.html',
            controller: 'UploadTutorialController',
            resolve: {
                v_id: function(){
                    return vid;
                },
                u_id: function(){
                    return userId;
                }
            }
        });
    }

    $scope.openAddVideo = function(){
        var modalInstance = $modal.open({
            templateUrl: 'src/app/mentors/playlist/choose_video_popup.tpl.html',
            controller: 'ChooseVideoController',
            resolve:{
                pl_id: function(){
                    return plid;
                }
            }            
        });
    }

    $scope.playAll = function(){
        localStorage.removeItem('vidInPlaylist');
        window.location.href = '#/mentor/playlist/playall/'+plid+'';
        window.location.reload();
    }

    $scope.selectAll = function(){
        var status = !$scope.selectedAll;

        angular.forEach($scope.videos, function(v){
           v.selected = status;
        });
    }

    $scope.removeAll = function(){
        var selectedVideos = checkSelectedVideos();

        if (selectedVideos.length > 0) {
            var ModalInstanceCtrl = function($scope, $modalInstance) {
                $scope.ok = function() {
                    $modalInstance.close();
                    $rootScope.$broadcast('open');
                    PlaylistService.deleteVideoInPlaylist(selectedVideos).then(function(data){
                      if (data.data.request_data_result != null && data.data.request_data_result.length > 0) {
                        $scope.selectedAll = false;
                        getVideosInPlaylist();
                      }
                      $rootScope.$broadcast('close');
                    });
                };
                $scope.cancel = function() {
                  $modalInstance.dismiss('cancel');
                };
            };
            var message = 'Are you sure you want to delete?';
            var modalHtml = ' <div class="modal-body">' + message + '</div>';
                modalHtml += '<div class="modal-footer"><button class="btn btn-primary" ng-click="ok()">' +
                    'OK</button><button class="btn btn-warning" ng-click="cancel()">Cancel</button></div>';

            var modalInstance = $modal.open({
                template: modalHtml,
                size: 'sm',
                controller: ModalInstanceCtrl
            }); 
            
        }
    }

    $scope.optionSelected = function(){
      $scope.selectedAll = $scope.videos.every(function(v){
        return v.selected;
      });
    }

    function checkSelectedVideos(){
      var selectedVideos = [];
      angular.forEach($scope.videos, function(v){
        if (!!v.selected) {
          selectedVideos.push(v.vid);
        }else{
          var i = selectedVideos.indexOf(v.vid);
          if(i != -1){
            selectedVideos.splice(i, 1);
          }
        }
      });
      return selectedVideos;
    }

    $scope.goToDetail = function(v){
      if (v.plid && v.plid > 0) {
        setStorage('vidInPlaylist', v.vid, 30);
        window.location.href = '#/mentor/playlist/playall/'+v.plid+'';
        window.location.reload();
      } else{
        localStorage.removeItem('vidInPlaylist');
        window.location.href = '#/mentor/video/detail/'+v.vid+'';
        window.location.reload();
      }
    }

    $scope.$on('passing', function(e,a){
        var result = $.grep($scope.videos, function(v){
            return v.vid == a.vid;
        });

        var index = $scope.videos.indexOf(result[0]);
        if (index != -1) {
            $scope.videos[index].title = a.title;
            $scope.videos[index].description = a.description;

        }
    });

    $scope.$on('addVideo', function(e,a){
        loadPlaylistDetail();
        getVideosInPlaylist();
    });

    $scope.$on('updatePlaylist', function(e,a){
        $scope.playlist.name = a.name;        
        $scope.playlist.description = a.description;
        if (a.newImage && a.newImage.length > 0) {}
          $scope.playlist.image = a.newImage;
    })
}]);
