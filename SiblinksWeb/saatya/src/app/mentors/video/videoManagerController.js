brotControllers.controller('VideoManagerController', 
  ['$rootScope','$scope', '$modal', '$routeParams', 'VideoService', 'MentorService', 'myCache', 'HomeService',
                                       function ($rootScope,$scope, $modal, $routeParams, VideoService, MentorService, myCache, HomeService) {


    var userId = localStorage.getItem('userId');
    
    $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';
    $scope.subject = [0];

    var cacheVideos = [];
    var sub = myCache.get("subjects");
    init();

    function init(){
      if (userId && userId > 0) {
        initSubject();
        loadVideos();
        getAllVideos();
      } else {
        window.localStorage.clear();
        window.location.href = '/';
      }      
    }

    function initSubject(){
      var videoMgrSubjects = localStorage.getItem("videoManagerSubjects");
      if (videoMgrSubjects != null) {
         $scope.videoMgrSubjects = JSON.parse(videoMgrSubjects);
         $scope.subject = $scope.videoMgrSubjects[0].subjectId;        
      } else{
        HomeService.getAllCategory().then(function (data) {
           if (data.data.status) {
              var arr = angular.copy(data.data.request_data_result);
               arr.splice(0, 0, {
                'subjectId': 0,
                'subject' : 'All'
               });
               $scope.videoMgrSubjects = arr;
               localStorage.setItem("videoManagerSubjects", JSON.stringify($scope.videoMgrSubjects), 10)
               $scope.subject = $scope.videoMgrSubjects[0].subjectId;
           }
         });
      }
    }

    function loadVideos(){
      VideoService.getVideos(userId, 0).then(function(data){
        var result = data.data.request_data_result;
        if (result && result != "Found no data") {
          $scope.videos = formatData(result);
          cacheVideos = $scope.videos.slice(0);
        } else
          $scope.videos = null;
      });
    }    

    function formatData(data){
      for (var i = 0; i < data.length; i++) {    
        data[i].playlistname = data[i].playlistname == null ? 'None' : data[i].playlistname;
        data[i].averageRating = data[i].averageRating != null ? data[i].averageRating : 0;
        data[i].timeStamp = convertUnixTimeToTime(data[i].timeStamp);
        data[i].selected = false;
      }
      return data;
    }
    
    $scope.loadMoreVideos = function(){
      var offset = 0;
      if ($scope.videos && $scope.videos.length > 0) 
        offset = $scope.videos.length;

      var oldArr = $scope.videos;
      var newArr = [];
      if ($scope.subject == 0) {
        VideoService.getVideos(userId, offset).then(function(data){        
          if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
            newArr = formatData(data.data.request_data_result);
            $scope.videos = oldArr.concat(newArr);
            cacheVideos.length = 0;
            cacheVideos = $scope.videos.slice(0);
          }
        });
      } else {
        VideoService.getVideosBySubject(userId, $scope.subject, offset).then(function(data){
          if(data.data.request_data_result != null && data.data.request_data_result != "Found no data")
            newArr = formatData(data.data.request_data_result);
            $scope.videos = oldArr.concat(newArr);
        });
      }      
    }

    function getVideoBySubject(){
      VideoService.getVideosBySubject(userId, $scope.subject, 0).then(function(data){
        if(data.data.request_data_result != null && data.data.request_data_result != "Found no data"){
          $scope.videos = formatData(data.data.request_data_result);
        } else
          $scope.videos = null;
      });
    }

    $scope.checkAll = function(){
      console.log($scope.selectedAll);
      var status = !$scope.selectedAll;

      angular.forEach($scope.videos, function(v){
        v.selected = status;
      });
    };

    $scope.optionSelected = function(){
      $scope.selectedAll = $scope.videos.every(function(v){
        return v.selected;
      });
    }

    $scope.deleteMultiple = function(){
      var selectedVideos = checkSelectedVideos();

      if (selectedVideos.length > 0) {
        var ModalInstanceCtrl = function($scope, $modalInstance) {
            $scope.ok = function() {
              $modalInstance.close();
              $scope.$broadcast('open');
              VideoService.deleteMultipleVideo(selectedVideos, userId).then(function(data){
                loadVideos();
                $scope.$broadcast('close');        
              });
            };
            $scope.cancel = function() {
              $modalInstance.dismiss('cancel');
            };
        };
        var message = 'Are you sure you want to delete?';
        var modalHtml = ' <div class="modal-body">' + message + '</div>';
            modalHtml += '<div class="modal-footer"><button class="btn btn-danger" ng-click="ok()">' +
                'OK</button><button class="btn btn-default" ng-click="cancel()">Cancel</button></div>';

        var modalInstance = $modal.open({
            template: modalHtml,
            size: 'sm',
            controller: ModalInstanceCtrl
        });
      }
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

    $scope.deleteVideo = function(vid){
      VideoService.deleteVideo(vid, userId).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result.length > 0) {
          loadVideos();
        }
      });
    }

    $scope.addToPlaylist = function(v){
      var selectedVideos = [v.vid];
      openAddPlaylistPopup(selectedVideos);
    }

    $scope.addMultipleToPlaylist = function(){
      var selectedVideos = checkSelectedVideos();
      if (selectedVideos.length > 0) {
        openAddPlaylistPopup(selectedVideos);
      }
    }

    function openAddPlaylistPopup(selectedVideos){
      var modalInstance = $modal.open({
        templateUrl: 'src/app/mentors/video/choose_playlist_popup.tpl.html',
        controller: 'ChoosePlaylistController',
        resolve: {
            u_id: function () {
                return userId;
            },
            v_ids: function(){
              return selectedVideos;
            }
        }
      });
    }

    function getAllVideos() {
        VideoService.getAllVideos().then(function (response) {
          if (response.data.status) {
              $scope.listAllVideos = response.data.request_data_result;
          }
        });
    }

    $scope.loadVideosBySubject = function(e){
      $scope.subject = e;
      var keyword = $('input#srch-term').val();
      if($scope.subject == 0){
        if (keyword && keyword.length > 0) {
          searchVideosMentor(keyword, $scope.subject);
        } else if(!keyword || keyword.length == 0 && cacheVideos.length > 0) {
          if ($scope.videos && $scope.videos.length > 0)
            $scope.videos = null;
          $scope.videos = cacheVideos.slice(0);
        } else{
          loadVideos();
        }
      } else{
        if (keyword && keyword.length > 0) {
          searchVideosMentor(keyword, $scope.subject);
        } else {
          getVideoBySubject();          
        }
      }
    }

    $scope.search = function(){
      var keyword = $('input#srch-term').val();
      if (keyword && keyword.length > 0) {
        searchVideosMentor(keyword, $scope.subject);
      } else if(!keyword && $scope.subject > 0){
        getVideoBySubject();
      } else{
        $scope.videos = cacheVideos;
      }
    }

    $scope.onSelect = function (selected) {
      if (selected !== undefined) {
        searchVideosMentor(selected.title, $scope.subject);
      }      
    };

    function searchVideosMentor(keyword, subjectId){
      VideoService.searchVideosMentor(userId, keyword, subjectId, 0).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
          $scope.videos = formatData(data.data.request_data_result);
        } else
          $scope.videos = null;
      });
    }

    $scope.edit = function(vid){
      var modalInstance = $modal.open({
        templateUrl: 'src/app/mentors/video/upload_tutorial_popup.tpl.html',
        controller: 'UploadTutorialController',
        resolve:{
          u_id: function () {
              return userId;
          },
          v_id: function(){
            return vid;
          }
        }
      });
    }

    function getIndex(vid){
      var result = $.grep($scope.videos, function(v){
        return v.vid == vid;
      });

      return $scope.videos.indexOf(result[0]);
    }

    $scope.$on('passing', function(e,a){
      var index = getIndex(a.vid);
      if (index != -1) {
          $scope.videos[index].title = a.title;
          $scope.videos[index].description = a.description;
          if (a.plid && a.plid > 0){
            $scope.videos[index].plid = a.plid;
            $scope.videos[index].playlistname = a.playlistname;
          } else{
            $scope.videos[index].plid = null;
            $scope.videos[index].playlistname = "None";
          }
          $scope.videos[index].subjectId = a.subjectId;
      }
    });

    $scope.$on('addPlaylistVideo', function(e,a){
      var index = getIndex(a.vid);
      if (index != -1) {
        if (a.plid && a.plid > 0){
          $scope.videos[index].plid = a.plid;
          $scope.videos[index].playlistname = a.name;
        }
      }
    });
    $scope.$on('addPlaylist', function(){
      loadVideos();
    });

    $scope.$on('uploadNew', function(){
      loadVideos();
    });
}]);