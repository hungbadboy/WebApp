brotControllers.controller('VideoManagerController', 
  ['$rootScope','$scope', '$modal', '$routeParams', 'VideoService', 'MentorService', 'HomeService',
                                       function ($rootScope,$scope, $modal, $routeParams, VideoService, MentorService, HomeService) {


    var userId = localStorage.getItem('userId');
    
    $scope.subject = [0];
    $scope.videoTab = 1;

    var cacheVideos = [];
    var cacheTopRatedVideos = [];
    var cacheTopViewedVideos = [];
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
              var subjects = angular.copy(data.data.request_data_result);
              subjects = removeItem(subjects);
               subjects.splice(0, 0, {
                'subjectId': 0,
                'subject' : 'All'
               });
               $scope.videoMgrSubjects = subjects;
               localStorage.setItem("videoManagerSubjects", JSON.stringify($scope.videoMgrSubjects), 10)
               $scope.subject = $scope.videoMgrSubjects[0].subjectId;
           }
         });
      }
    }

    function loadVideos(){
      loadNewestVideos(0);
      loadTopRatedVideos(0); 
      loadTopViewedVideos(0);
    }

    function loadNewestVideos(offset){
      VideoService.getVideos(userId, offset).then(function(data){
        var result = data.data.request_data_result;
        if (result && result != "Found no data") {
          $scope.videos = formatData(result);
          cacheVideos = $scope.videos.slice(0);
        } else
          $scope.videos = null;
      });
    }

    function loadTopRatedVideos(offset){
      VideoService.getVideosTopRated(userId, offset).then(function(data){
        var result = data.data.request_data_result;
        if (result && result != "Found no data") {
          $scope.topRatedVideos = formatData(result);
          cacheTopRatedVideos = $scope.topRatedVideos.slice(0);
        } else
          $scope.topRatedVideos = null;
      });
    }

    function loadTopViewedVideos(offset){
      VideoService.getVideosTopViewed(userId, offset).then(function(data){
        var result = data.data.request_data_result;
        if (result && result != "Found no data") {
          $scope.topViewedVideos = formatData(result);
          cacheTopViewedVideos = $scope.topViewedVideos.slice(0);
        } else
          $scope.topViewedVideos = null;
      });
    }

    function formatData(data){
      for (var i = 0; i < data.length; i++) {    
        data[i].playlistname = data[i].playlistname == null ? 'None' : data[i].playlistname;
        data[i].averageRating = data[i].averageRating != null ? data[i].averageRating : 0;
        data[i].timeStamp = convertUnixTimeToTime(data[i].timeStamp);
        data[i].selected = $scope.selectedAll == true ? $scope.selectedAll : false;
      }
      return data;
    }
    
    $scope.loadMoreVideos = function(){
      var keyword = $('input#srch-term').val();
      var offset = 0;
      if ($scope.videoTab == 1) {
        if ($scope.videos && $scope.videos.length > 0) 
        offset = $scope.videos.length;        
        if ($scope.subject == 0) {
          if (keyword && keyword.length > 0)
            searchMoreNewestVideos(keyword, $scope.videoTab, $scope.subject, offset);
          else
            loadMoreNewestVideos(offset);
        } else {
          if (keyword && keyword.length > 0)
            searchMoreNewestVideos(keyword, $scope.videoTab, $scope.subject, offset);
          else{
            VideoService.getVideosBySubject(userId, $scope.videoTab, $scope.subject, offset).then(function(data){
              if(data.data.request_data_result != null && data.data.request_data_result != "Found no data")
                var oldArr = $scope.videos;
                var newArr = formatData(data.data.request_data_result);
                $scope.videos = oldArr.concat(newArr);
            });
          }
        }
      } else if ($scope.videoTab == 2) {
        if ($scope.videos && $scope.videos.length > 0) 
          offset = $scope.videos.length;        
        if ($scope.subject == 0) {
          if (keyword && keyword.length > 0)
            searchMoreTopViewedVideos(keyword, $scope.videoTab, $scope.subject, offset);
          else
            loadMoreTopViewedVideos(offset);
        } else {
          if (keyword && keyword.length > 0)
            searchMoreTopViewedVideos(keyword, $scope.videoTab, $scope.subject, offset);
          else{
            VideoService.getVideosBySubject(userId, $scope.videoTab, $scope.subject, offset).then(function(data){
              if(data.data.request_data_result != null && data.data.request_data_result != "Found no data")
                var oldArr = $scope.topViewedVideos;
                var newArr = formatData(data.data.request_data_result);
                $scope.topViewedVideos = oldArr.concat(newArr);
            });
          }
        }
      } else{
        if ($scope.topRatedVideos && $scope.topRatedVideos.length > 0) 
          offset = $scope.topRatedVideos.length;        
        if ($scope.subject == 0) {
          if (keyword && keyword.length > 0)
            searchMoreTopRatedVideos(keyword, $scope.videoTab, $scope.subject, offset);
          else
            loadMoreTopRatedVideos(offset);
        } else {
          if (keyword && keyword.length > 0)
            searchMoreTopRatedVideos(keyword, $scope.videoTab, $scope.subject, offset);
          else{
            VideoService.getVideosBySubject(userId, $scope.videoTab, $scope.subject, offset).then(function(data){
              if(data.data.request_data_result != null && data.data.request_data_result != "Found no data")
                var oldArr = $scope.topRatedVideos;
                var newArr = formatData(data.data.request_data_result);
                $scope.topRatedVideos = oldArr.concat(newArr);
            });
          }
        }
      }            
    }

    function searchMoreNewestVideos(keyword, type, subjectId, offset) {
      var request = {
        "uid": userId,
        "keySearch": keyword,
        "offset": offset,
        "subjectId": subjectId,
        "type": type
      };
      VideoService.searchVideosMentor(request).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
          var oldArr = $scope.videos;
          var newArr = formatData(data.data.request_data_result);
          $scope.videos = oldArr.concat(newArr);
        }
      });
    }

    function searchMoreTopRatedVideos(keyword, type, subjectId, offset) {
      var request = {
        "uid": userId,
        "keySearch": keyword,
        "offset": offset,
        "subjectId": subjectId,
        "type": type
      };
       VideoService.searchVideosMentor(request).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
          var oldArr = $scope.videos;
          var newArr = formatData(data.data.request_data_result);
          $scope.videos = oldArr.concat(newArr);
        }
      });
    }

    function searchMoreTopViewedVideos(keyword, type, subjectId, offset) {
      var request = {
        "uid": userId,
        "keySearch": keyword,
        "offset": offset,
        "subjectId": subjectId,
        "type": type
      };
       VideoService.searchVideosMentor(request).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
          var oldArr = $scope.videos;
          var newArr = formatData(data.data.request_data_result);
          $scope.videos = oldArr.concat(newArr);
        }
      });
    }

    function loadMoreNewestVideos(offset){
      VideoService.getVideos(userId, offset).then(function(data){        
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
          var oldArr = $scope.videos;
          var newArr = formatData(data.data.request_data_result);
          $scope.videos = oldArr.concat(newArr);
          cacheVideos.length = 0;
          cacheVideos = $scope.videos.slice(0);
        }
      });
    }

    function loadMoreTopRatedVideos(offset) {
      VideoService.getVideosTopRated(userId, offset).then(function(data){
        var result = data.data.request_data_result;
        if (result && result != "Found no data") {
          var oldArr = $scope.topRatedVideos;
          var newArr = formatData(result);
          $scope.topRatedVideos = oldArr.concat(newArr);
          cacheTopRatedVideos.length = 0;
          cacheTopRatedVideos = $scope.topRatedVideos.slice(0);
        }
      });
    }

    function loadMoreTopViewedVideos(offset) {
      VideoService.getVideosTopViewed(userId, offset).then(function(data){
        var result = data.data.request_data_result;
        if (result && result != "Found no data") {
          var oldArr = $scope.topViewedVideos;
          var newArr = formatData(result);
          $scope.topViewedVideos = oldArr.concat(newArr);
          cacheTopViewedVideos.length = 0;
          cacheTopViewedVideos = $scope.topViewedVideos.slice(0);
        }
      });
    }

    function getNewestVideoBySubject(){
      VideoService.getVideosBySubject(userId, $scope.videoTab, $scope.subject, 0).then(function(data){
        if(data.data.request_data_result != null && data.data.request_data_result != "Found no data"){
          $scope.videos = formatData(data.data.request_data_result);
        } else
          $scope.videos = null;
          $scope.checkAll = false;
      });
    }

    function getTopRatedVideoBySubject(){
      VideoService.getVideosBySubject(userId, $scope.videoTab, $scope.subject, 0).then(function(data){
        if(data.data.request_data_result != null && data.data.request_data_result != "Found no data"){
          $scope.topRatedVideos = formatData(data.data.request_data_result);
        } else
          $scope.topRatedVideos = null;
          $scope.checkAll = false;
      });
    }

    function getTopViewedVideoBySubject(){
      VideoService.getVideosBySubject(userId, $scope.videoTab, $scope.subject, 0).then(function(data){
        if(data.data.request_data_result != null && data.data.request_data_result != "Found no data"){
          $scope.topViewedVideos = formatData(data.data.request_data_result);
        } else
          $scope.topViewedVideos = null;
          $scope.checkAll = false;
      });
    }

    $scope.checkAll = function(){
      var status = !$scope.selectedAll;

      if ($scope.videoTab == 1) {
        angular.forEach($scope.videos, function(v){
          v.selected = status;
        });
      } else if ($scope.videoTab == 2) {
        angular.forEach($scope.topViewedVideos, function(v){
          v.selected = status;
        });
      } else{
        angular.forEach($scope.topRatedVideos, function(v){
          v.selected = status;
        });
      }
    };

    // $scope.optionSelected = function(){
    //   if ($scope.videoTab == 1) {
    //     $scope.selectedAll = $scope.videos.every(function(v){
    //       return v.selected;
    //     });
    //   } else if ($scope.videoTab == 2) {
    //     $scope.selectedAll = $scope.topViewedVideos.every(function(v){
    //       return v.selected;
    //     });
    //   } else{
    //     $scope.selectedAll = $scope.topRatedVideos.every(function(v){
    //       return v.selected;
    //     });
    //   }
    // }

    $scope.deleteMultiple = function(){
      var selectedVideos = checkSelectedVideos();
      if (selectedVideos.length > 0) {
        var ModalInstanceCtrl = function($scope, $modalInstance) {
          $scope.ok = function() {
            $modalInstance.close();
            $scope.$broadcast('open');
            VideoService.deleteMultipleVideo(selectedVideos, userId).then(function(data){
              refreshVideos();
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

    $scope.deleteVideo = function(vid){
      VideoService.deleteVideo(vid, userId).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result.length > 0) {
          refreshVideos();
        }
      });
    }

    function checkSelectedVideos(){
      var selectedVideos = [];
      if ($scope.videoTab == 1) {
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
      } else if ($scope.videoTab == 2) {
        angular.forEach($scope.topViewedVideos, function(v){
          if (!!v.selected) {
            selectedVideos.push(v.vid);
          }else{
            var i = selectedVideos.indexOf(v.vid);
            if(i != -1){
              selectedVideos.splice(i, 1);
            }
          }
        });
      } else{
        angular.forEach($scope.topRatedVideos, function(v){
          if (!!v.selected) {
            selectedVideos.push(v.vid);
          }else{
            var i = selectedVideos.indexOf(v.vid);
            if(i != -1){
              selectedVideos.splice(i, 1);
            }
          }
        });
      }
      return selectedVideos;
    }

    $scope.changeTab = function(tab){
      if ($scope.videoTab != tab) {
        $scope.videoTab = tab;
        $scope.selectedAll = false;
        if ($scope.subject > 0) {
          if ($scope.videoTab == 1)
            getNewestVideoBySubject();
          else if ($scope.videoTab == 2)
            getTopViewedVideoBySubject();
          else
            getTopRatedVideoBySubject();
        } else{
          $scope.videos = updateSelectedVideo($scope.videos, $scope.selectedAll);
          $scope.topViewedVideos = updateSelectedVideo($scope.topViewedVideos, $scope.selectedAll);
          $scope.topRatedVideos = updateSelectedVideo($scope.topRatedVideos, $scope.selectedAll);
        }
      }
    }

    function updateSelectedVideo(data, status) {
      for (var i = data.length - 1; i >= 0; i--) {
        data[i].selected = status;
      }
      return data;
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
        if ($scope.videoTab == 1) {
          if (keyword && keyword.length > 0) {
            searchNewestVideos(keyword, $scope.subject);
          } else if(!keyword || keyword.length == 0 && cacheVideos.length > 0) {
            if ($scope.videos && $scope.videos.length > 0)
              $scope.videos = null;
            $scope.videos = cacheVideos.slice(0);
          } else{
            loadNewestVideos(0);
          }
        } else if ($scope.videoTab == 2) {
          if (keyword && keyword.length > 0) {
            searchTopViewedVideos(keyword, $scope.subject);
          } else if(!keyword || keyword.length == 0 && cacheTopViewedVideos.length > 0) {
            if ($scope.topViewedVideos && $scope.topViewedVideos.length > 0)
              $scope.topViewedVideos = null;
            $scope.topViewedVideos = cacheTopViewedVideos.slice(0);
          } else{
            loadTopViewedVideos(0);
          }
        } else{
          if (keyword && keyword.length > 0) {
            searchTopRatedVideos(keyword, $scope.subject);
          } else if(!keyword || keyword.length == 0 && cacheTopRatedVideos.length > 0) {
            if ($scope.topRatedVideos && $scope.topRatedVideos.length > 0)
              $scope.topRatedVideos = null;
            $scope.topRatedVideos = cacheTopRatedVideos.slice(0);
          } else{
            loadTopRatedVideos(0);
          }
        }
      } else{
        if ($scope.videoTab == 1) {
          if (keyword && keyword.length > 0) {
            searchNewestVideos(keyword, $scope.subject);
          } else {
            getNewestVideoBySubject();          
          }
        } else if ($scope.videoTab == 2) {
          if (keyword && keyword.length > 0) {
            searchTopViewedVideos(keyword, $scope.subject);
          } else {
            getTopViewedVideoBySubject();          
          }
        } else{
          if (keyword && keyword.length > 0) {
            searchTopRatedVideos(keyword, $scope.subject);
          } else {
            getTopRatedVideoBySubject();          
          }
        }
      }
    }

    $scope.search = function(){
      var keyword = $('input#srch-term').val();
      if (keyword && keyword.length > 0) {
        if ($scope.videoTab == 1)
          searchNewestVideos(keyword, $scope.subject);
        else if ($scope.videoTab == 2)
          searchTopViewedVideos(keyword, $scope.subject);
        else
          searchTopRatedVideos(keyword, $scope.subject);
      } else if(!keyword && $scope.subject > 0){
        if ($scope.videoTab == 1)
          getNewestVideoBySubject();
        else if ($scope.videoTab == 2)
          getTopViewedVideoBySubject();
        else
          getTopRatedVideoBySubject();
      } else{
        if ($scope.subject > 0) {
          if ($scope.videoTab == 1) 
            getNewestVideoBySubject();
          else if ($scope.videoTab == 2)
            getTopViewedVideoBySubject();
          else
            getTopRatedVideoBySubject();
        } else{
          $scope.videos = cacheVideos;
          $scope.topViewedVideos = cacheTopViewedVideos;
          $scope.topRatedVideos = cacheTopRatedVideos;
        }
      }
    }

    $scope.onSelect = function (selected) {
      if (selected !== undefined) {
        if ($scope.videoTab == 1)
          searchNewestVideos(selected.title, $scope.subject);
        else if ($scope.videoTab == 2)
          searchTopViewedVideos(selected.title, $scope.subject);
        else
          searchTopRatedVideos(selected.title, $scope.subject);
      }      
    };

    function searchNewestVideos(keyword, subjectId){
      var request = {
        "uid": userId,
        "keySearch": keyword,
        "offset": 0,
        "subjectId": subjectId,
        "type": $scope.videoTab
      };
      VideoService.searchVideosMentor(request).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
          $scope.videos = formatData(data.data.request_data_result);
        } else
          $scope.videos = null;
      });
    }

    function searchTopRatedVideos(keyword, subjectId){
      var request = {
        "uid": userId,
        "keySearch": keyword,
        "offset": 0,
        "subjectId": subjectId,
        "type": $scope.videoTab
      };
      VideoService.searchVideosMentor(request).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
          $scope.topRatedVideos = formatData(data.data.request_data_result);
        } else
          $scope.topRatedVideos = null;
      });
    }

    function searchTopViewedVideos(keyword, subjectId){
      var request = {
        "uid": userId,
        "keySearch": keyword,
        "offset": 0,
        "subjectId": subjectId,
        "type": $scope.videoTab
      };
      VideoService.searchVideosMentor(request).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
          $scope.topViewedVideos = formatData(data.data.request_data_result);
        } else
          $scope.topViewedVideos = null;
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
      var result;
      var index = -1;
      if ($scope.videoTab == 1) {
        result = $.grep($scope.videos, function(v){
          return v.vid == vid;
        });
        index = $scope.videos.indexOf(result[0]);
      } else if ($scope.videoTab == 2) {
        result = $.grep($scope.topViewedVideos, function(v){
          return v.vid == vid;
        });
        index = $scope.topViewedVideos.indexOf(result[0]);
      } else{
        result = $.grep($scope.topRatedVideos, function(v){
          return v.vid == vid;
        });
        index = $scope.topRatedVideos.indexOf(result[0]);
      }
      return index;
    }

    $scope.$on('passing', function(e,a){
      var index = getIndex(a.vid);
      if (index != -1) {
        if ($scope.videoTab == 1) {
          refreshEditVideo($scope.videos, index, a);
        } else if ($scope.videoTab == 2) {
          refreshEditVideo($scope.topViewedVideos, index, a);
        } else{
          refreshEditVideo($scope.topRatedVideos, index, a);
        }          
      }
    });

    function refreshEditVideo(data , index, a){
      data[index].title = a.title;
      data[index].description = a.description;
      if (a.plid && a.plid > 0){
        data[index].plid = a.plid;
        data[index].playlistname = a.playlistname;
      } else{
        data[index].plid = null;
        data[index].playlistname = "None";
      }
      data[index].subjectId = a.subjectId;
    }

    function reloadPlaylistCache(){
      VideoService.getPlaylist(userId).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
          var playlists = data.data.request_data_result;
          playlists.splice(0,0,{
            'plid':0,
            'name': "Select a Playlist"
          });
          localStorage.removeItem("playlists");
          localStorage.setItem("playlists", JSON.stringify(playlists), 10);
        }
      });
    }

    $scope.$on('addPlaylistVideo', function(e,a){
      reloadPlaylistCache();
      var index = getIndex(a.vid);
      if (index != -1) {
        if ($scope.videoTab == 1)
          refreshPlaylistVideo($scope.videos, index, a);
        else if ($scope.videoTab == 2)
          refreshPlaylistVideo($scope.topViewedVideos, index, a);
        else
          refreshPlaylistVideo($scope.topRatedVideos, index, a);
      }
    });

    function refreshPlaylistVideo(data, index, a) {
      if (a.plid && a.plid > 0){
        data[index].plid = a.plid;
        data[index].playlistname = a.name;
      }
    }

    $scope.$on('addPlaylist', function(){
      refreshVideos();
    });

    $scope.$on('uploadNew', function(){
      loadNewestVideos(0);
    });

    function refreshVideos() {
      if ($scope.videoTab == 1)
        loadNewestVideos(0);
      else if ($scope.videoTab == 2) 
        loadTopViewedVideos(0);
      else
        loadTopRatedVideos(0);
    }
}]);