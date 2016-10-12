brotControllers.controller('AllEssayCtrl', ['$scope', '$location', 'EssayService', function ($scope, $location, EssayService) {
  var userType = localStorage.getItem('userType');
  var userId = localStorage.getItem('userId');
  $scope.userId = userId;
  var clickE = 1;
  var listEssay = [];
  var NO_DATA = "Found no data";
  $scope.tabpane = 1;
  init();

  function init(){
    // getAllEssay(clickE, userType, userId);
    getAllEssay();
    
  }
  
  function getAllEssay(){
    getNewestEssay();
    getProcessingEssay();
    getIgnoredEssay();
    getRepliedEssay();
  }

  function getRepliedEssay(){
    EssayService.getRepliedEssay(userId, 0).then(function(data){
      var result = data.data.request_data_result;
      if (result && result != NO_DATA) {
        $scope.repliedEssays = formatEssay(result);
      } else
        $scope.repliedEssays = null;
    });
  }

  function getIgnoredEssay(){
    EssayService.getIgnoredEssay(userId, 0).then(function(data){
      var result = data.data.request_data_result;
      if (result && result != NO_DATA) {
        $scope.ignoredEssays = formatEssay(result);
      } else
        $scope.ignoredEssays = null;
    });
  }

  function getProcessingEssay(){
    EssayService.getProcessingEssay(userId, 0).then(function(data){
      var result = data.data.request_data_result;
      if (result && result != NO_DATA) {
        $scope.processingEssays = formatEssay(result);
      } else
        $scope.processingEssays = null;
    });
  }

  function getNewestEssay(){
    EssayService.getNewestEssay(userId, 0).then(function(data){
      var result = data.data.request_data_result;
      if (result && result != NO_DATA) {
        $scope.newestEssays = formatEssay(result);
      } else
        $scope.newestEssays = null;
      console.log($scope.newestEssays);
    });
  }

  function formatEssay(data){
    for (var i = data.length - 1; i >= 0; i--) {
      data[i].timeStamp = convertUnixTimeToTime(data[i].timeStamp);
      data[i].fullName = data[i].firstName + ' ' + data[i].lastName;
    }
    return data;
  }

  // function getAllEssay(page, userType, userId) {
  //   EssayService.getAllEssay(page, userType, userId).then(function(data) {
  //     $scope.showItem = true;
  //     var objs = data.data.request_data_result;

  //     if(objs.length !== 0) {
  //       $scope.countEssay = data.data.count;
  //       totalUpdateEssay = Math.ceil(data.data.count / 5);

  //       showPage(totalUpdateEssay, page, function(response) {
  //         $scope.listPage = response;
  //       });

  //       if(objs == null) {
  //         objs = [];
  //       }
  //       for(var i = 0; i < objs.length; i++) {
  //         var obj = objs[i];
  //         obj.reviewImage = EssayService.getImageReviewEssay(obj.uploadEssayId);
  //         var d = moment(obj.docSubmittedDate, 'YYYY-MM-DD hh:mm:ss').format('MM/DD/YYYY');
  //         obj.docSubmittedDate = d;
  //         listEssay.push(obj);
  //       }
  //       $scope.essays = listEssay;
  //     } else {
  //       $scope.showError = true;
  //       $scope.errorData = DATA_ERROR.noDataFound;
  //     }

  //     EssayService.getAllEssay(page++, userType, userId).then(function(data) {
  //       if(data.data.request_data_result.length < 5) {
  //         $scope.nextMore = 1;
  //       }
  //     });
  //   });
  // }

  $scope.changeTab = function(val){
    $scope.tabpane = val;
  }

  $scope.loadMoreEssay = function() {
    clickE++;
    getAllEssay(clickE, userType, userId);
  };

  $scope.essayDetail = function(essayId) {
    $location.url('/essay_detail/' + essayId);
  };

  $scope.deleteEssay = function(event, essayId, essayName) {
    $scope.essayDelete = essayName;
    $('#delete').modal('show');
    essayRemoveId = essayId;
    eventRemove = event;
  };

  $scope.deleteEssayWithDialog = function() {
    EssayService.removeEssay(essayRemoveId).then(function(data) {
      if(data.data.request_data_result == 'Done') {
        listEssay = [];
        for(var i = 1; i <= clickE; i++) {
          getAllEssay(i, userType, userId);  
        }
      }
    });
  };

}]);