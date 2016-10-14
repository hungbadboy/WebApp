brotControllers.controller('MentorVideoManageController', ['$rootScope','$scope', '$modal', '$routeParams', '$http', '$location', 'VideoService', 'MentorService','$sce',
                                       function ($rootScope, $scope, $modal, $routeParams, $http, $location, VideoService, MentorService, $sce) {


    var userId = localStorage.getItem('userId');
    // var userId = 111;
    $scope.fullName = localStorage.getItem('firstName') + ' ' + localStorage.getItem('lastname');
    $scope.avatar = localStorage.getItem('imageUrl');
    $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';

    $scope.video_subject = [0];
    $scope.video_playlist = [0];
    $scope.averageRating = 0.1;
    $scope.newestAverageRating = 0.1;
    $scope.topViewedAverageRating = 0.1;
    $scope.topRatedAverageRating = 0.1;
    init();

    function init(){
      getDashboardInfo();
      loadVideos();
      getLatestRatings();
      getLatestComments();
    }

    function getDashboardInfo(){
      MentorService.getDashboardInfo(userId).then(function(data){
        if (data.data.request_data_result != null) {
          $scope.dashboard = data.data.request_data_result;
          $scope.dashboard.avg_rating = parseFloat(Math.round($scope.dashboard.avg_rating * 100) / 100).toFixed(1);
          $scope.averageRating = $scope.dashboard.avg_rating;
        }
      });
    }

    function getLatestComments(){
      MentorService.getLatestComments(userId, 10, 0).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data"){
          $scope.comments = formatCommentProfile(data.data.request_data_result);
        }
      });
    }

    function formatCommentProfile(data){      
      for (var i = 0; i < data.length; i++) {
        if (data[i].imageUrl == null || data[i].imageUrl.length == 0)
          data[i].imageUrl = 'http://www.capheseo.com/Content/T000/Images/no-avatar.png';
        else
          data[i].imageUrl = data[i].imageUrl.indexOf('http') == -1 ? $scope.baseIMAGEQ + data[i].imageUrl: data[i].imageUrl;
        var firstname = data[i].firstName != null ? data[i].firstName : '';
        var lastname = data[i].lastName != null ? data[i].lastName : '';
        data[i].fullName = firstname + ' ' + lastname;
        data[i].timestamp = convertUnixTimeToTime(data[i].timestamp);
        data[i].content = decodeURIComponent(data[i].content);
      }
      return data;
    }

    function getLatestRatings(){
      MentorService.getLatestRatings(userId).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data"){
          $scope.ratings = formatRatingProfile(data.data.request_data_result);
        }
      });
    }

    function formatRatingProfile(data){
      for (var i = 0; i < data.length; i++) {
        if (data[i].imageUrl == null || data[i].imageUrl.length == 0)
          data[i].imageUrl = 'http://www.capheseo.com/Content/T000/Images/no-avatar.png';
        else
          data[i].imageUrl = data[i].imageUrl.indexOf('http') == -1 ? $scope.baseIMAGEQ + data[i].imageUrl: data[i].imageUrl;

        var firstname = data[i].firstName != null ? data[i].firstName : '';
        var lastname = data[i].lastName != null ? data[i].lastName : '';
        data[i].fullName = firstname + ' ' + lastname;
      }
      return data;
    }

    $scope.loadMoreComments = function(){
      var offset = 0;
      if ($scope.comments && $scope.comments.length > 0)
        offset = $scope.comments.length;

      MentorService.getLatestComments(userId, 10, offset).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
          var oldArr = $scope.comments;
          var newArr = formatCommentProfile(data.data.request_data_result);
          var totalArr = oldArr.concat(newArr);
          $scope.comments = totalArr;
        }
      });
    }

    function loadVideos(){
        // clearContent();
        getNewestVideos();

        VideoService.getVideosTopRated(0, 0).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
            $scope.videosTopRated = formatData(data.data.request_data_result);
            $scope.vTopRated = $scope.videosTopRated[0];
            $scope.topRatedPos = 0;
            $scope.topRatedAverageRating = $scope.videosTopRated.averageRating;
          } else
            $scope.videosTopRated = null;
        });

        VideoService.getVideosTopViewed(0, 0).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
            $scope.videosTopViewed = formatData(data.data.request_data_result);
            $scope.vTopViewed = $scope.videosTopViewed[0];
            $scope.topViewedPos = 0;
            $scope.topViewedAverageRating = $scope.videosTopViewed.averageRating;
          } else
            $scope.videosTopViewed = null;
        });
    }    

    function getNewestVideos(){
      VideoService.getVideos(0, 0).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
          $scope.videos = formatData(data.data.request_data_result);
          $scope.v = $scope.videos[0];
          $scope.newestPos = 0;
          $scope.newestAverageRating = $scope.v.averageRating;
        } else
          $scope.videos = null;
      });
    }

    function formatData(data){
      for (var i = 0; i < data.length; i++) {           
        var playlist = data[i].playlistname;

        if (playlist == null)
          data[i].playlist = 'None';
        data[i].timeStamp = convertUnixTimeToTime(data[i].timeStamp);
        var avg = data[i].averageRating != null ? data[i].averageRating : 0;
        data[i].averageRating = parseFloat(Math.round(avg * 100) / 100).toFixed(1);
      }
      return data;
    }
    
  $scope.newestPrev = function(pos){
    if ($scope.videos && $scope.videos.length > 0) {
      if (pos == 0) {
        $scope.newestPos = $scope.videos.length - 1;
        $scope.v = $scope.videos[$scope.videos.length - 1];
      }
      else{
        $scope.newestPos = pos - 1;
        $scope.v = $scope.videos[pos - 1];
      }
      $scope.newestAverageRating = $scope.v.averageRating;
    }
  }

  $scope.newestNext = function(pos){
    if ($scope.videos && $scope.videos.length > 0) {
      if (pos == $scope.videos.length - 1) {
        $scope.newestPos = 0;
        $scope.v = $scope.videos[0];
      }
      else{
        $scope.newestPos = pos + 1;
        $scope.v = $scope.videos[pos + 1];
      }
      $scope.newestAverageRating = $scope.v.averageRating;
    }
  }

  $scope.topRatedPre = function(pos){
    if ($scope.videosTopRated && $scope.videosTopRated.length > 0) {
      if (pos == 0) {
        $scope.topRatedPos = $scope.videosTopRated.length - 1;
        $scope.vTopRated = $scope.videosTopRated[$scope.videosTopRated.length - 1];
      }
      else{
        $scope.topRatedPos = pos - 1;
        $scope.vTopRated = $scope.videosTopRated[pos - 1];
      }
      $scope.topRatedAverageRating = $scope.vTopRated.averageRating;
    }
  }

  $scope.topRatedNext = function(pos){
    if ($scope.videosTopRated && $scope.videosTopRated.length > 0) {
      if (pos == $scope.videosTopRated.length - 1) {
        $scope.topRatedPos = 0;
        $scope.vTopRated = $scope.videosTopRated[0];
      }
      else{
        $scope.topRatedPos = pos + 1;
        $scope.vTopRated = $scope.videosTopRated[pos + 1];
      }
    }    
    $scope.topRatedAverageRating = $scope.vTopRated.averageRating;
  }

  $scope.topViewedPre = function(pos){
    if ($scope.videosTopViewed && $scope.videosTopViewed.length > 0) {
      if (pos == 0) {
        $scope.topViewedPos = $scope.videosTopViewed.length - 1;
        $scope.vTopViewed = $scope.videosTopViewed[$scope.videosTopViewed.length - 1];
      }
      else{
        $scope.topViewedPos = pos - 1;
        $scope.vTopViewed = $scope.videosTopViewed[pos - 1];
      }
      $scope.topViewedAverageRating = $scope.vTopViewed.averageRating;
    }
  }

  $scope.topViewedNext = function(pos){
    if ($scope.videosTopViewed && $scope.videosTopViewed.length > 0) {
      if (pos == $scope.videosTopViewed.length - 1) {
        $scope.topViewedPos = 0;
        $scope.vTopViewed = $scope.videosTopViewed[0];
      }
      else{
        $scope.topViewedPos = pos + 1;
        $scope.vTopViewed = $scope.videosTopViewed[pos + 1];
      }
      $scope.topViewedAverageRating = $scope.vTopViewed.averageRating;
    }
  }

  $scope.goToDetail = function(v){
    if (v.plid && v.plid > 0) {
      setStorage('vidInPlaylist', v.vid, 30);
      window.location.href = '#/mentor/playlist/playall/'+v.plid+'';
      window.location.reload();
    } else{
      window.location.href = '#/mentor/video/detail/'+v.vid+'';
      window.location.reload();
    }
  }
	 /**
	  * Convert content CKEditor to html
	  */
	$scope.comvertToDisplayComment  = function(str){
		 return $sce.trustAsHtml(decodeURIComponent(str));
	}

  $scope.$on('uploadNew', function(){
    getDashboardInfo();
    getNewestVideos();
  })
}]);
