brotControllers.controller('AllEssayCtrl', ['$rootScope','$scope', '$location', '$window', '$modal', 'EssayService', 
  function ($rootScope, $scope, $location, $window, $modal, EssayService) {
  var userType = localStorage.getItem('userType');
  var userId = localStorage.getItem('userId');
  var schoolId = localStorage.getItem('school');
  var eid = $location.search().eid;
  var tab = $location.search().t;
  var NO_DATA = "Found no data";
  var newestEssayCache = [];
  var processingEssayCache = [];
  var ignoredEssayCache = [];
  var repliedEssayCache = [];
  var alreadySearch = false;
  $scope.userId = userId;
  $scope.avatar = localStorage.getItem('imageUrl');
  $scope.mentorName = localStorage.getItem('firstName') + ' ' + localStorage.getItem('lastname');
  $scope.tabpane = 1;
  $scope.pos = 0;
  $scope.currentId = 0;
  $scope.averageRating = 5;

  init();

  function init(){
    if (eid != null && !checkValid(eid)) {
      window.location.href = '#/mentor/dashboard';
    } 
    if (tab != null && !checkValid(tab) || tab > 4) {
      window.location.href = '#/mentor/dashboard';
    }
    if (userId && userId > 0) {
      $(window).scroll(function(){ 
        var qa_scroll = $(window).scrollTop();
        var heighttab = $(window).height() - 258;
            heighttab_scroll = $(window).height() - 320;
        if (qa_scroll > 75) {
          $(".mentor-manage-essay .left-qa").css({"top":"105px"});
              $(".mentor-manage-essay .left-qa .tab-content .tab-pane").css({"height":+ heighttab + "px"});
          }
          else {
              $(".mentor-manage-essay .left-qa").css({"top":"auto"});
              $(".mentor-manage-essay .left-qa .tab-content .tab-pane").css({"height":+ heighttab_scroll + "px"});
          } 
      })
      getAllEssay();
      if (!tab)
        $scope.tabpane = 1;
      else
        $scope.tabpane = tab;
    } else {
      window.localStorage.clear();
      window.location.href = '/';
    }      
  }
  
  function checkValid(number){
    if (number % 1 !== 0) 
      return false;
    else if (isNaN(number)) 
      return false;
    else if (number <= 0)
      return false;
    else
      return true;
  }

  function getAllEssay(){
    getSuggestionEssay();
    getNewestEssay();
    getProcessingEssay();
    getIgnoredEssay();
    getRepliedEssay();
  }

  function getSuggestionEssay(){
    if (schoolId == undefined) {
      $scope.listAllEssay = null;
    } else {
      EssayService.getSuggestionEssay(schoolId).then(function(data){
        var result = data.data.request_data_result;
        if (result != NO_DATA) {
          $scope.listAllEssay = result;
        } else {
          $scope.listAllEssay = null;
        }
      });
    }
  }

  function getRepliedEssay(){
    if (schoolId == undefined || schoolId == null) {
      $scope.repliedEssays = null;
      return;
    }
    EssayService.getRepliedEssay(userId, schoolId, 10, 0).then(function(data){
      var result = data.data.request_data_result;
      $scope.notifyReplied = data.data.count != null ? data.data.count : 0;
      if (result && result != NO_DATA) {
        $scope.repliedEssays = formatEssay(result);
        repliedEssayCache = $scope.repliedEssays.slice(0);
        if (tab == 4) {
          $scope.pos = getIndexEssay(eid, $scope.repliedEssays);
          getEssayById(eid, userId);
        }
        if (justReplied) {
          $scope.changeTab(4);
          justReplied = false;          
        }
      } else{
        $scope.repliedEssays = null;
        repliedEssayCache.length = 0;
      }
    });
  }

  function getIgnoredEssay(){
    if (schoolId == undefined || schoolId == null) {
      $scope.ignoredEssays = null;
      return;
    }
    EssayService.getIgnoredEssay(userId, schoolId, 10, 0).then(function(data){
      var result = data.data.request_data_result;
      $scope.notifyIgnored = data.data.count != null ? data.data.count : 0;
      if (result && result != NO_DATA) {
        $scope.ignoredEssays = formatEssay(result);
        ignoredEssayCache = $scope.ignoredEssays.slice(0);
        if (tab == 3) {
          $scope.pos = getIndexEssay(eid, $scope.ignoredEssays);
          getEssayById(eid, userId);
        }
      } else{
        $scope.ignoredEssays = null;
        ignoredEssayCache.length = 0;
      }
    });
  }

  function getProcessingEssay(){
    if (schoolId == undefined || schoolId == null) {
      $scope.processingEssays = null;
      return;
    }
    EssayService.getProcessingEssay(userId, schoolId, 10, 0).then(function(data){
      var result = data.data.request_data_result;
      $scope.notifyProcessing = data.data.count != null ? data.data.count : 0;
      if (result && result != NO_DATA) {
        $scope.processingEssays = formatEssay(result);
        processingEssayCache = $scope.processingEssays.slice(0);
        if (tab == 2) {
          $scope.pos = getIndexEssay(eid, $scope.processingEssays);
          getEssayById(eid, userId);
        }
      } else{
        $scope.processingEssays = null;
        processingEssayCache.length = 0;
      }
    });
  }

  function getNewestEssay(){
    if (schoolId == undefined || schoolId == null) {
      $scope.newestEssays = null;
      return;
    }
    EssayService.getNewestEssay(userId, schoolId, 10, 0).then(function(data){
      var result = data.data.request_data_result;
      $scope.notifyNewest = data.data.count != null ? data.data.count : 0;
      if (result && result != NO_DATA) {
        $scope.newestEssays = formatEssay(result);
        newestEssayCache = $scope.newestEssays.slice(0);
        if (!tab) {
          if (eid != null && eid > 0) {
            $scope.pos = getIndexEssay(eid, $scope.newestEssays);
            $scope.eid = eid;
          } else{
            $scope.eid = $scope.newestEssays[0].uploadEssayId;
          }
          getEssayById($scope.eid, userId);
        }
      } else{
        $scope.newestEssays = null;
        newestEssayCache.length = 0;
      }
    });
  }

  function getIndexEssay(eid, data){
    var result = $.grep($scope.newestEssays, function(e){
      return e.uploadEssayId == eid;
    });

    var index = data.indexOf(result[0]) != -1 ? data.indexOf(result[0]) : 0;
    return index;
  }

  function getRepliedByEssay(eid, userId) {
    EssayService.getCommentEssay(eid, userId).then(function(data){
      var result = data.data.request_data_result;
      if (result && result != NO_DATA) {
        for (var i = result.length - 1; i >= 0; i--) {
          var fullname = result[i].firstName + ' ' + result[i].lastName;
          result[i].fullName = fullname != ' ' ? fullname : result[i].userName.substr(0, result[i].userName.indexOf('@'));
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
            result[i].fullName = fullname != ' ' ? fullname : result[i].userName.substr(0, result[i].userName.indexOf('@'));
          }
          $scope.essay = result[0];
          $scope.currentId = $scope.essay.uploadEssayId;
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
      data[i].fullName = fullname != ' ' ? fullname : data[i].userName.substr(0, data[i].userName.indexOf('@'));
    }
    return data;
  }

  $scope.loadMoreEssay = function(){
    var offset = 0;
    var keyword = $('input#essay-term').val();
    if ($scope.tabpane == 1) {
      offset = getOffset($scope.newestEssays);
      if (offset == undefined || offset == null)
        offset = 0;
      if (keyword && keyword.length > 0)
        loadMoreSearch(keyword, offset);
      else
        loadMoreNewestEssay(offset);
    } else if ($scope.tabpane == 2) {
      offset = getOffset($scope.processingEssays);
      if (keyword && keyword.length > 0)
        loadMoreSearch(keyword, offset);
      else
        loadMoreProcessingEssay(offset);
    } else if ($scope.tabpane == 3) {
      offset = getOffset($scope.ignoredEssays);
      if (keyword && keyword.length > 0)
        loadMoreSearch(keyword, offset);
      else
        loadMoreIgnoredEssay(offset);
    } else {
      offset = getOffset($scope.repliedEssays);
      if (keyword && keyword.length > 0)
        loadMoreSearch(keyword, offset);
      else
        loadMoreRepliedEssay(offset);
    }
  }

  function loadMoreNewestEssay(offset){
    EssayService.getNewestEssay(userId, schoolId, 10, offset).then(function(data){
      var result = data.data.request_data_result;
      if (result && result != NO_DATA) {
        var oldEss = $scope.newestEssays;
        var newEss = formatEssay(result);
        if (oldEss && oldEss.length > 0)
          $scope.newestEssays = oldEss.concat(newEss);
        else
          $scope.newestEssays = newEss;
        newestEssayCache.length = 0;
        newestEssayCache = $scope.newestEssays.slice(0);
        $scope.eid = $scope.newestEssays[0].uploadEssayId;
        getEssayById($scope.eid, userId);
      }
    });
  }

  function loadMoreProcessingEssay(offset){
    EssayService.getProcessingEssay(userId, schoolId, 10, offset).then(function(data){
      var result = data.data.request_data_result;
      if (result && result != NO_DATA) {
        var oldEss = $scope.processingEssays;
        var newEss = formatEssay(result);
        if (oldEss && oldEss.length > 0)
          $scope.processingEssays = oldEss.concat(newEss);
        else
          $scope.processingEssays = newEss;
        processingEssayCache.length = 0;
        processingEssayCache = $scope.processingEssays.slice(0);
      }
    });
  }

  function loadMoreIgnoredEssay(offset){
    EssayService.getIgnoredEssay(userId, schoolId, 10, offset).then(function(data){
      var result = data.data.request_data_result;
      if (result && result != NO_DATA) {
        var oldEss = $scope.ignoredEssays;
        var newEss = formatEssay(result);
        if (oldEss && oldEss.length > 0)
          $scope.ignoredEssays = oldEss.concat(newEss);
        else
          $scope.ignoredEssays = newEss;
        ignoredEssayCache.length = 0;
        ignoredEssayCache = $scope.ignoredEssays.slice(0);
      }
    });
  }
  
  function loadMoreRepliedEssay(offset){
    EssayService.getRepliedEssay(userId, schoolId, 10, offset).then(function(data){
      var result = data.data.request_data_result;
      if (result && result != NO_DATA) {
        var oldEss = $scope.repliedEssays;
        var newEss = formatEssay(result);
        if (oldEss && oldEss.length > 0)
          $scope.repliedEssays = oldEss.concat(newEss);
        else
          $scope.repliedEssays = newEss;
        repliedEssayCache.length = 0;
        repliedEssayCache = $scope.repliedEssays.slice(0);
      }
    });
  }

  function getOffset(array){
    return (array && array.length > 0) ? array.length : 0;
  }

  $scope.changeTab = function(val){
    $scope.fileName = null;
    $scope.fileSize = null;
    $scope.tabpane = val;
    var keyword = $('input#essay-term').val();
    if (keyword && keyword.length > 0) {
      if (alreadySearch == true) {
        switchTab(val);
      } else {
        searchEssay(keyword, 0);
      }
    } else {
      $scope.newestEssays = newestEssayCache.slice(0);
      $scope.processingEssays = processingEssayCache.slice(0);
      $scope.ignoredEssays = ignoredEssayCache.slice(0);
      $scope.repliedEssays = repliedEssayCache.slice(0);
      switchTab(val);
    }    
  }

  function switchTab(val){
    if (val == 1) {      
      if ($scope.newestEssays && $scope.newestEssays.length > 0){
        $scope.eid = $scope.newestEssays[0].uploadEssayId;
        $scope.ignored = false;
      }
      else{
        $scope.eid = null;
      }
    } else if (val == 2) {
      if ($scope.processingEssays && $scope.processingEssays.length > 0)
        $scope.eid = $scope.processingEssays[0].uploadEssayId;
      else{
        $scope.eid = null;
      }
    } else if (val == 3) {
      if ($scope.ignoredEssays && $scope.ignoredEssays.length > 0){
        $scope.eid = $scope.ignoredEssays[0].uploadEssayId;
        $scope.ignored = true;
      }
      else{
        $scope.eid = null;
      }
    } else {
      if ($scope.repliedEssays && $scope.repliedEssays.length > 0)
        $scope.eid = $scope.repliedEssays[0].uploadEssayId;
      else{
        $scope.eid = null;
      }
    }
    $scope.pos = 0;
    getEssayById($scope.eid, userId);
  }

  $scope.changeStatus = function (eid,status) {
    $rootScope.$broadcast('open');
    EssayService.updateStatusEssay(eid, userId, status).then(function (data) {
      if (data.data.request_data_result == "Success") {
        getAllEssay();
        updateUI();
      } else {
        var result = data.data.request_data_result;
        if (result == "Processed") {
          var ModalInstanceCtrl = function($scope, $modalInstance) {
            $scope.ok = function() {
              getAllEssay();
              updateUI();
              $modalInstance.dismiss('cancel');
              $rootScope.$broadcast('close');
            };
          };
          var message =  "This essay has been processing by other mentor.";
          var modalHtml = ' <div class="modal-body">' + message + '</div>';
              modalHtml += '<div class="modal-footer"><button class="btn btn-danger" ng-click="ok()">OK</button></div>';

          var modalInstance = $modal.open({
              template: modalHtml,
              size: 'sm',
              controller: ModalInstanceCtrl
          });
        }
        getAllEssay();
        updateUI();
      }
      $rootScope.$broadcast('close');
    });
  }

  function updateUI(){
    if ($scope.tabpane == 1) {
      $scope.pos = updateScopePos($scope.pos, $scope.newestEssays);
      if ($scope.pos = -1)
        getEssayById($scope.pos, userId);
      else
        getEssayById($scope.newestEssays[$scope.pos].uploadEssayId, userId);
    } else if ($scope.tabpane == 2) {
      $scope.pos = updateScopePos($scope.pos, $scope.processingEssays);
      if ($scope.pos = -1)
        $scope.changeTab(1);
      else
        getEssayById($scope.processingEssays[$scope.pos].uploadEssayId, userId);
    }
  }

  function updateScopePos(pos, data){
    if (data && data.length > 0) {
      if (pos < data.length - 1) {
        pos += 1;
      } else {
        pos -= 1;
      }
    } else 
      pos = -1;
    return pos;
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
  $scope.onFileSelect = function($files, errFile){
    var errFile = errFile && errFile[0];
    if (errFile) {
      $scope.fileName = null;
      $scope.fileSize = null;
      if (errFile.size > 5000000) {
        $scope.error = "File maximum is 5MB.";
      } else
        $scope.error = "File is not valid.";
      return;
    }
    if ($files && $files.length > 0) {
      $scope.error = null;
      file = $files[0];
      if (file == undefined){
        $scope.error = "Only accept document file.";
      } else{
        $scope.fileName = file.name;
        $scope.fileSize = formatBytes(file.size);
      }
    }
  }

  var justReplied = false;
  $scope.reply = function(){
    var comment = $('#comment').val();
    if (comment == undefined || comment.trim().length == 0) {
      $scope.error = "Please input your comment.";
      return;
    } else{
      $scope.checked = true;
      $scope.error = null;
      var fd = new FormData();

      fd.append('file', file);
      fd.append('comment', comment);
      fd.append('essayId', $scope.essay.uploadEssayId);
      fd.append('mentorId', userId);
      fd.append('studentId', $scope.essay.userId);
      fd.append('isUpdate', false);
      $rootScope.$broadcast('open');
      EssayService.insertUpdateCommentEssay(fd).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result == "Success") {
          $scope.success = "Reply successful.";
          getAllEssay();
          justReplied = true;
          $scope.error = null;
          $scope.fileName = null;
          $scope.fileSize = null;
        } else{
          $scope.error = data.data.request_data_result;
        }
        $scope.checked = false;
        $rootScope.$broadcast('close');
      });
    }
  }

  $scope.search = function(){
    var keyword = $('input#essay-term').val();
    if (keyword && keyword.length > 0)
      searchEssay(keyword, 0);
    else{
      $scope.newestEssays = newestEssayCache.slice(0);
      $scope.processingEssays = processingEssayCache.slice(0);
      $scope.ignoredEssays = ignoredEssayCache.slice(0);
      $scope.repliedEssays = repliedEssayCache.slice(0);
      switchTab($scope.tabpane);
    }
  }

  $scope.onSelect = function(selected){
    if (selected != undefined)
      searchEssay(selected.title, 0);
  }

  $scope.textChanged = function(str){
    if (str && str.length > 0)
      alreadySearch = false;
  }

  function searchEssay(keyword, offset){
    console.log('searchEssay');
    var request = {
      "keySearch": keyword,
      "schoolId": schoolId,
      "limit": 10,
      "offset": offset,
      "mentorId": userId
    }
    $rootScope.$broadcast('open');
    EssayService.search(request).then(function(data){
      alreadySearch = true;
      var result = data.data;
      if (result.status == 'true') {
        var collection = result.request_data_result;
        if (collection.newestEssay != NO_DATA) {
          $scope.newestEssays = formatEssay(collection.newestEssay);
        } else {
          $scope.newestEssays = null;
        }

        if (collection.processingEssay != NO_DATA) {
          $scope.processingEssays = formatEssay(collection.processingEssay);
        } else {
          $scope.processingEssays = null;
        }

        if (collection.ignoredEssay != NO_DATA) {
          $scope.ignoredEssays = formatEssay(collection.ignoredEssay);
        } else {
          $scope.ignoredEssays = null;
        }

        if (collection.repliedEssay != NO_DATA) {
          $scope.repliedEssays = formatEssay(collection.repliedEssay);
        } else {
          $scope.repliedEssays = null;
        }
      } else {
        $scope.newestEssays = null;
        $scope.processingEssays = null;
        $scope.ignoredEssays = null;
        $scope.repliedEssays = null;
        $scope.essay = null;
      }
      $rootScope.$broadcast('close');
      switchTab($scope.tabpane);
    });
  }

  function loadMoreSearch(keyword, offset){
    var request = {
      "keySearch": keyword,
      "schoolId": schoolId,
      "limit": 10,
      "offset": offset,
      "mentorId": userId
    }
    $rootScope.$broadcast('open');
    EssayService.search(request).then(function(data){
      alreadySearch = true;
      var result = data.data;
      if (result.status == 'true') {
        var collection = result.request_data_result;
        if (collection.newestEssay != NO_DATA) {
          $scope.newestEssays = formatEssay(collection.newestEssay);
        }
        if (collection.processingEssay != NO_DATA) {
          $scope.processingEssays = formatEssay(collection.processingEssay);
        }
        if (collection.ignoredEssay != NO_DATA) {
          $scope.ignoredEssays = formatEssay(collection.ignoredEssay);
        }
        if (collection.repliedEssay != NO_DATA) {
          $scope.repliedEssays = formatEssay(collection.repliedEssay);
        }
      }
      $rootScope.$broadcast('close');
    });
  }

  $scope.openEdit = function(cid, content, fileName, fileSize){
    $scope.edit = true;
    $scope.editEssay = {
      'content' : content,
      'cid': cid
    };
    $scope.fileName = fileName;
    $scope.fileSize = fileSize;
  }

  $scope.clearEdit = function(){
    $scope.edit = false;
    $scope.fileName = null;
    $scope.fileSize = null;
    file = null;
  }

  $scope.removeFile = function(){
    $scope.fileName = null;
    $scope.fileSize = null;
    file = null;
  }

  $scope.updateComment = function(comment){
    if (comment == null || comment.trim().length == 0) {
      $scope.error = "Please input your comment.";
      return;
    }
    $scope.error = null;
    var fd = new FormData();

    fd.append('file', file);
    fd.append('comment', comment);
    fd.append('essayId', $scope.essay.uploadEssayId);
    fd.append('mentorId', userId);
    fd.append('isUpdate', true);
    fd.append('commentId', $scope.editEssay.cid);
    fd.append('fileOld', $scope.fileName);

    $rootScope.$broadcast('open');
    EssayService.insertUpdateCommentEssay(fd).then(function(data){
      if (data.data.request_data_result != null && data.data.request_data_result == "Success") {
        $scope.success = "Reply successful.";
        getEssayById($scope.essay.uploadEssayId, userId);        
        $scope.edit= false;
        $scope.editEssay = null;
        $scope.error = null;
        $scope.file = null;
        $scope.fileName = null;
        $scope.fileSize = null;
      } else{
        $scope.error = data.data.request_data_result;
      }
      $scope.checked = false;
      $rootScope.$broadcast('close');
    });
  }

  $scope.clearAnswer = function(){
    $('#comment').val('');
    $scope.fileName = null;
    $scope.fileSize = null;
    file = null;
  }

  $scope.loadTo = function(){
    if ($scope.tabpane == 1)
      angular.element(document.getElementById('newestEssaysTab')).mCustomScrollbar('scrollTo','#newestEssaysList' + $scope.currentId);
    else if ($scope.tabpane == 2)
      angular.element(document.getElementById('processingEssaysTab')).mCustomScrollbar('scrollTo','#processingEssaysList' + $scope.currentId);
    else if ($scope.tabpane == 3)
      angular.element(document.getElementById('ignoredEssaysTab')).mCustomScrollbar('scrollTo','#ignoredEssaysList' + $scope.currentId);
    else
      angular.element(document.getElementById('repliedEssaysTab')).mCustomScrollbar('scrollTo','#repliedEssaysList' + $scope.currentId);
  }

  $scope.prevEssay = function(pos){
    if ($scope.tabpane == 1) {
      if ($scope.newestEssays && $scope.newestEssays.length > 0) {
        if (pos == 0) {
          $scope.pos = $scope.newestEssays.length - 1;
        } else {
          $scope.pos = pos-1;
        }
        $scope.currentId = $scope.newestEssays[$scope.pos].uploadEssayId;
        getEssayById($scope.newestEssays[$scope.pos].uploadEssayId, userId);
        angular.element(document.getElementById('newestEssaysTab')).mCustomScrollbar('scrollTo','#newestEssaysList' + $scope.currentId);
      }
    } else if ($scope.tabpane == 2) {
      if ($scope.processingEssays && $scope.processingEssays.length > 0) {
        if (pos == 0) {
          $scope.pos = $scope.processingEssays.length - 1;
        } else {
          $scope.pos = pos-1;
        }
        $scope.currentId = $scope.processingEssays[$scope.pos].uploadEssayId;
        getEssayById($scope.processingEssays[$scope.pos].uploadEssayId, userId);
        angular.element(document.getElementById('processingEssaysTab')).mCustomScrollbar('scrollTo','#processingEssaysList' + $scope.currentId);
      }
    } else if ($scope.tabpane == 3) {
      if ($scope.ignoredEssays && $scope.ignoredEssays.length > 0) {
        if (pos == 0) {
          $scope.pos = $scope.ignoredEssays.length - 1;
        } else {
          $scope.pos = pos-1;
        }
        $scope.currentId = $scope.ignoredEssays[$scope.pos].uploadEssayId;
        getEssayById($scope.ignoredEssays[$scope.pos].uploadEssayId, userId);
        angular.element(document.getElementById('ignoredEssaysTab')).mCustomScrollbar('scrollTo','#ignoredEssaysList' + $scope.currentId);
      }
    } else {
      if ($scope.repliedEssays && $scope.repliedEssays.length > 0) {
        if (pos == 0) {
          $scope.pos = $scope.repliedEssays.length - 1;
        } else {
          $scope.pos = pos-1;
        }
        $scope.currentId = $scope.repliedEssays[$scope.pos].uploadEssayId;
        getEssayById($scope.repliedEssays[$scope.pos].uploadEssayId, userId);
        angular.element(document.getElementById('repliedEssaysTab')).mCustomScrollbar('scrollTo','#repliedEssaysList' + $scope.currentId);
      }
    }
  }

  $scope.nextEssay = function(pos){
    if ($scope.tabpane == 1) {
      if ($scope.newestEssays && $scope.newestEssays.length > 0) {
        if (pos == $scope.newestEssays.length - 1) {
          $scope.pos = 0;
        } else {
          $scope.pos = pos+1;
        }
        $scope.currentId = $scope.newestEssays[$scope.pos].uploadEssayId;
        getEssayById($scope.newestEssays[$scope.pos].uploadEssayId, userId);
        angular.element(document.getElementById('newestEssaysTab')).mCustomScrollbar('scrollTo','#newestEssaysList' + $scope.currentId);
      }
    } else if ($scope.tabpane == 2) {
      if ($scope.processingEssays && $scope.processingEssays.length > 0) {
        if (pos == $scope.processingEssays.length - 1) {
          $scope.pos = 0;
        } else {
          $scope.pos = pos+1;
        }
        $scope.currentId = $scope.processingEssays[$scope.pos].uploadEssayId;
        getEssayById($scope.processingEssays[$scope.pos].uploadEssayId, userId);
        angular.element(document.getElementById('processingEssaysTab')).mCustomScrollbar('scrollTo','#processingEssaysList' + $scope.currentId);
      }
    } else if ($scope.tabpane == 3) {
      if ($scope.ignoredEssays && $scope.ignoredEssays.length > 0) {
        if (pos == $scope.ignoredEssays.length - 1) {
          $scope.pos = 0;
        } else {
          $scope.pos = pos+1;
        }
        $scope.currentId = $scope.ignoredEssays[$scope.pos].uploadEssayId;
        getEssayById($scope.ignoredEssays[$scope.pos].uploadEssayId, userId);
        angular.element(document.getElementById('ignoredEssaysTab')).mCustomScrollbar('scrollTo','#ignoredEssaysList' + $scope.currentId);
      }
    } else {
      if ($scope.repliedEssays && $scope.repliedEssays.length > 0) {
        if (pos == $scope.repliedEssays.length - 1) {
          $scope.pos = 0;
        } else {
          $scope.pos = pos+1;
        }
        $scope.currentId = $scope.repliedEssays[$scope.pos].uploadEssayId;
        getEssayById($scope.repliedEssays[$scope.pos].uploadEssayId, userId);
        angular.element(document.getElementById('repliedEssaysTab')).mCustomScrollbar('scrollTo','#repliedEssaysList' + $scope.currentId);
      }
    }
  }
}]);