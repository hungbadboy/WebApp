brotControllers.controller('AllEssayCtrl', ['$scope', '$location', 'EssayService', function ($scope, $location, EssayService) {
  var userType = localStorage.getItem('userType');
  var userId = localStorage.getItem('userId');
  var schoolId = localStorage.getItem('school');
  $scope.userId = userId;
  $scope.avatar = localStorage.getItem('imageUrl');
  $scope.mentorName = localStorage.getItem('firstName') + ' ' + localStorage.getItem('lastname');
  var NO_DATA = "Found no data";
  $scope.tabpane = 1;
  init();

  function init(){
    if (userId && userId > 0) {
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
    } else {
      window.localStorage.clear();
      window.location.href = '/';
    }      
  }
  
  function getAllEssay(){
    getNewestEssay();
    getProcessingEssay();
    getIgnoredEssay();
    getRepliedEssay();
  }

  function getRepliedEssay(){
    EssayService.getRepliedEssay(userId, schoolId, 10, 0).then(function(data){
      var result = data.data.request_data_result;
      if (result && result != NO_DATA) {
        $scope.repliedEssays = formatEssay(result);
        if (justReplied) {
          $scope.changeTab(4);
          justReplied = false;          
        }
      } else
        $scope.repliedEssays = null;
    });
  }

  function getIgnoredEssay(){
    EssayService.getIgnoredEssay(userId, schoolId, 10, 0).then(function(data){
      var result = data.data.request_data_result;
      if (result && result != NO_DATA) {
        $scope.ignoredEssays = formatEssay(result);
      } else
        $scope.ignoredEssays = null;
    });
  }

  function getProcessingEssay(){
    EssayService.getProcessingEssay(userId, schoolId, 10, 0).then(function(data){
      var result = data.data.request_data_result;
      if (result && result != NO_DATA) {
        $scope.processingEssays = formatEssay(result);
      } else
        $scope.processingEssays = null;
    });
  }

  function getNewestEssay(){
    EssayService.getNewestEssay(userId, schoolId, 10, 0).then(function(data){
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
    if (eid && eid > 0) {
      EssayService.getEssayById(eid, userId).then(function(data){
        var result = data.data.request_data_result;
        if (result && result != NO_DATA) {
          for (var i = result.length - 1; i >= 0; i--) {
            result[i].timeStamp = convertUnixTimeToTime(result[i].docSubmittedDate);
            result[i].odFilesize = formatBytes(result[i].odFilesize);
            result[i].rdFilesize = formatBytes(result[i].rdFilesize);
            var fullname = result[i].firstName + ' ' + result[i].lastName;
            result[i].fullName = fullname != ' ' ? fullname : result[i].userName;
          }
          $scope.essay = result[0];
          getRepliedByEssay($scope.essay.uploadEssayId, userId);
        } else {
          $scope.essay = null;
        }     
      });
    } else
      $scope.essay = null;
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
      var fullname = data[i].firstName + ' ' + data[i].lastName;
      data[i].fullName = fullname != ' ' ? fullname : data[i].userName;
    }
    return data;
  }

  $scope.changeTab = function(val, eid){
    $scope.tabpane = val;
    if (val == 1) {
      if ($scope.newestEssays){
        $scope.eid = $scope.newestEssays[0].uploadEssayId;
        $scope.ignored = false;
      }
      else{
        $scope.eid = null;
      }
    } else if (val == 2) {
      if ($scope.processingEssays)
        $scope.eid = $scope.processingEssays[0].uploadEssayId;
      else{
        $scope.eid = null;
      }
    } else if (val == 3) {
      if ($scope.ignoredEssays){
        $scope.eid = $scope.ignoredEssays[0].uploadEssayId;
        $scope.ignored = true;
      }
      else{
        $scope.eid = null;
      }
    } else {
      if ($scope.repliedEssays)
        $scope.eid = $scope.repliedEssays[0].uploadEssayId;
      else{
        $scope.eid = null;
      }
    }
    if (eid && eid > 0) {
      getEssayById(eid, userId);  
    } else
      getEssayById($scope.eid, userId);
  }

  $scope.changeStatus = function (eid,status) {
    EssayService.updateStatusEssay(eid, userId, status).then(function (data) {
      if (data.data.request_data_result == "Success") {
        getAllEssay();
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
      } else{
          window.location.href = '#/mentor/studentProfile/'+id+'';
      }
  }

  var file;
  $scope.onFileSelect = function($files){
    if ($files && $files.length > 0) {
      file = $files[0];
      if (file == undefined) 
        alert('Only accept document file..');
    }
  }

  var justReplied = false;
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
          justReplied = true;
        } else{
          $scope.error = data.data.request_data_result;
          console.log($scope.error);
        }
      });
    }
  }
}]);