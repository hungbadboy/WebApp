brotControllers.controller('AllEssayCtrl', ['$scope', '$location', 'EssayService', function ($scope, $location, EssayService) {
  var userType = localStorage.getItem('userType');
  var userId = localStorage.getItem('userId');
  $scope.userId = userId;
  $scope.avatar = localStorage.getItem('imageUrl');
  $scope.mentorName = localStorage.getItem('firstName') + ' ' + localStorage.getItem('lastname');
  var NO_DATA = "Found no data";
  $scope.tabpane = 1;
  init();

  function init(){
    $(window).scroll(function(){    
      var qa_scroll = $(window).scrollTop();
      if (qa_scroll > 75) {
        $(".mentor-manage-qa-content .left-qa").css({"top":"95px", "height":"90%"});
        $(".mentor-manage-qa-content .left-qa .tab-answered .tab-content").css({"height":"75vh"});
      }
      else {
        $(".mentor-manage-qa-content .left-qa").css("top","auto");
        $(".mentor-manage-qa-content .left-qa .tab-answered .tab-content").css({"height":"70vh"});
      }
    })
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
        $scope.eid = $scope.newestEssays[0].uploadEssayId;
        getEssayById($scope.eid, userId);
      } else
        $scope.newestEssays = null;
    });
  }

  function getRepliedByEssay(eid, userId) {
    EssayService.getCommentEssay(eid, userId).then(function(data){
      var result = data.data.request_data_result;
      if (result && result != NO_DATA) {
        for (var i = result.length - 1; i >= 0; i--) {
          result[i].timestamp = convertUnixTimeToTime(result[i].timestamp);
        }
        $scope.comments = result;
      } else {
        $scope.comments = null;
      }
    });
  }

  function getEssayById(eid, userId){
    EssayService.getEssayById(eid, userId).then(function(data){
      var result = formatEssay(data.data.request_data_result);
      $scope.essay = result[0];
      getRepliedByEssay($scope.essay.uploadEssayId, userId);
    });
  }

  function formatBytes(bytes) {
     if(bytes == 0) return '0 Byte';
     var k = 1000;
     var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
     var i = Math.floor(Math.log(bytes) / Math.log(k));
     return parseFloat((bytes / Math.pow(k, i)).toFixed(1)) + ' ' + sizes[i];
  }

  function formatEssay(data){
    for (var i = data.length - 1; i >= 0; i--) {
      data[i].timeStamp = convertUnixTimeToTime(data[i].timeStamp);
      data[i].fullName = data[i].firstName + ' ' + data[i].lastName;
      data[i].odFilesize = formatBytes(data[i].odFilesize);
    }
    return data;
  }

  $scope.changeTab = function(val){
    $scope.tabpane = val;
    if (val == 1) {
      $scope.eid = $scope.newestEssays[0].uploadEssayId;
    } else if (val == 2) {
      $scope.eid = $scope.processingEssays[0].uploadEssayId;
    } else if (val == 3) {
      $scope.eid = $scope.ignoredEssays[0].uploadEssayId;
    } else {
      $scope.eid = $scope.repliedEssays[0].uploadEssayId;
    }
    getEssayById($scope.eid, userId);
  }

  $scope.changeStatus = function (eid,status) {
    EssayService.updateStatusEssay(eid, userId, status).then(function (data) {
      if (data.data.request_data_result == "Success") {
        getNewestEssay();
      } else {
        console.log(data.data.request_data_result);
      }
    });
  }

  $scope.detailEssay = function(eid){
    $scope.eid = eid;
    getEssayById(eid, userId);
  }

  $scope.goToProfile = function(id){
      if (id == userId) {
          window.location.href = '#/mentor/mentorProfile';
          window.location.reload();
      } else{
          window.location.href = '#/mentor/studentProfile/'+id+'';
          window.location.reload();
      }
  }

  var file;
  $scope.onFileSelect = function($files){
    file = $files[0];
    console.log(file);
  }

  $scope.reply = function(){
    var comment = $('#comment').val();
    if (comment == undefined || comment.trim().length == 0) {
      $scope.error = "Please input your comment.";
      return;
    } else{
      $scope.error = null;
      var fd = new FormData();

      fd.append('file', file);
      fd.append('comment', comment);
      fd.append('essayId', $scope.essay.uploadEssayId);
      fd.append('mentorId', userId);

      EssayService.insertCommentEssay(fd).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result == "Success") {
          $scope.success = "Reply successful.";
          getAllEssay();
        } else{
          $scope.error = data.data.request_data_result;
        }
      });
    }
  }
}]);