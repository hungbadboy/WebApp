brotControllers.controller('VideoAdmissionCtrl', ['$scope', '$rootScope', '$routeParams', '$log', '$location', 'AdmissionService',
	function ($scope, $rootScope, $routeParams, $log, $location, AdmissionService) {
	
  var idSubAdmission = $routeParams.idSubAdmission;
  AdmissionService.getTopicSubAdmission(idSubAdmission).then(function(data) {
    if(data.data.status) {
      $scope.topicSubAdmission = data.data.request_data_result;
      $scope.errorData = DATA_ERROR.noDataFound;

      for(var i = 0; i < $scope.topicSubAdmission.length; i++) {
        for(var j = 0; j < $scope.topicSubAdmission[i][1].videos.length; j++) {

          var mystring = $scope.topicSubAdmission[i][1].videos[j].description;
          $scope.topicSubAdmission[i][1].videos[j].description = mystring.replace(/(<([^>]+)>)/ig," ");

          // $scope.topicSubAdmission[i][1].videos[j].image = AdmissionService.getImageVideoAdmission($scope.topicSubAdmission[i][1].videos[j].vId);
          var rate = $scope.topicSubAdmission[i][1].videos[j].numRatings;
          if(rate > 0) {
            var unrate = 5 - rate;
            var arr_rate = [];
            for(var k = 0; k < rate; k++) {
              arr_rate.push('assets/img/yellow _star.png');
            }
            for(var n = 0; n < unrate; n++) {
              arr_rate.push('assets/img/grey_star.png');
            }
            $scope.topicSubAdmission[i][1].videos[j].arr_rate = arr_rate;  
          }
        }
      }
    }
  });

  $scope.showmore = function(idTopic) {
    $location.url('/admission/videoadmission/listvideoadmission/' + idSubAdmission + '/' + idTopic + '/1');
  };

  $scope.showVideoDetail = function(idSubAdmission, idTopicSubAdmission, vId) {
    $location.url('/admission/videoadmission/videodetail/' + idSubAdmission + '/' + idTopicSubAdmission + '/' + vId);
  };

}]);