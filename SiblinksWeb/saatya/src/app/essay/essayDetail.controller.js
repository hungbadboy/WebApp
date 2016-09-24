brotControllers.controller('EssayDetailCtrl', ['$scope', '$rootScope', '$location', '$timeout', '$routeParams', '$sce', 'EssayService', 'CommentService', 'StudentService', function ($scope, $rootScope, $location, $timeout, $routeParams, $sce, EssayService, CommentService, StudentService) {

  var userId = localStorage.getItem('userId');
  $scope.userType = localStorage.getItem('userType');
  $scope.userId = userId;
  var essayId = $routeParams.essayId;
  var listDiscus = [];
  var clickD = 1;
  var idRemove, editCommentId;
  init();

  function init() {

    var editor = CKEDITOR.replace( 'txtDiscus', {
      toolbar: 'MyToolbar'
    });
    editor.config.width = 933;
    editor.config.height = 250;
    editor.addCommand("mySimpleCommand", {
      exec: function(edt) {
        $('#uploadImage').modal('show');
      }
    });
    editor.ui.addButton('SuperButton', {
      label: "Upload Image",
      command: 'mySimpleCommand',
      toolbar: 'insert',
      icon: '/assets/img/ico-hit.png'
    });

    var editorEdit = CKEDITOR.replace('discuss');
    editorEdit.config.width = 615;
    editorEdit.config.height = 260;
    editorEdit.addCommand("mySimpleCommand", {
      exec: function(edt) {
        $('#uploadImageEdit').modal('show');
      }
    });
    editorEdit.ui.addButton('SuperButton', {
      label: "Upload Image",
      command: 'mySimpleCommand',
      toolbar: 'insert',
      icon: '/assets/img/ico-hit.png'
    });

    EssayService.getEssayById(essayId, userId).then(function(data) {
      if(data.data.status) {
        $scope.showItem = true;
        $scope.essay = data.data.request_data_result[0];
        if($scope.essay.urlReview != null) {
          $scope.essay.urlReview = EssayService.getImageReviewEssay($scope.essay.uploadEssayId);
        }
        var d = moment($scope.essay.docSubmittedDate, 'YYYY-MM-DD hh:mm:ss').format('MM/DD/YYYY');
        $scope.essay.docSubmittedDate = d;
      }
    });

    getDiscuss(clickD);

    $('#select-file').click(function() {
      $('#file').click();
    });

    $('#file').change(function() {
      $('.waitingUp').removeClass('hide');
      CommentService.uploadImage(function(data) {
        if(data.status == 'true') {
          var local = data.request_data_result;
          $('#uploadImage').modal('hide');
          $('.waitingUp').addClass('hide');
          var content = CKEDITOR.instances["txtDiscus"].getData();
          content += "<img style='max-width: 550px;' src='"+ local +"'/>";
          CKEDITOR.instances["txtDiscus"].setData(content);
        } else {
          $('.waitingUp').addClass('hide');
          $scope.errorUpload = data.request_data_result;
          alert($scope.errorUpload);
          return;
        }
      });
    });

    $('#select-edit').click(function() {
      $('#edit').click();
    });

    $('#edit').change(function() {
      $('.waitingUp').removeClass('hide');
      CommentService.uploadImageEdit(function(data) {
        if(data.status == 'true') {
          var local = data.request_data_result;
          $('#uploadImageEdit').modal('hide');
          $('.waitingUp').addClass('hide');
          var content = CKEDITOR.instances["discuss"].getData();
          content += "<img style='max-width: 570px;' src='"+ local +"'/>";
          CKEDITOR.instances["discuss"].setData(content); 
        } else {
        $('.waitingUp').addClass('hide');
          $scope.errorUpload = data.request_data_result;
          alert($scope.errorUpload);
          return;
        }
      });
    });
  }

  $scope.showCKEditor = function(event) {
    showCKEditor = true;
    var ele = event.currentTarget;
    $('#essay_detail .discussion .boxCkComment').toggle('slow');
    $('#essay_detail .discussion .ckComment').removeClass('hide');
    $('#essay_detail .discussion .go').addClass('hide');
    $('#essay_detail .discussion .btnHide').removeClass('hide');
  };

  $scope.cancel = function() {
    revertCKeditorEssayDetail(showCKEditor);
  };

  $scope.sendDiscus = function() {
    var content = CKEDITOR.instances["txtDiscus"].getData();
    if(content.length > 0) {
      clickD = 1;
      CommentService.addCommentEssay(userId, content, essayId).then(function(data) {
        if(data.data.status == 'true') {
          listDiscus = [];
          getDiscuss(clickD);
        }
        revertCKeditorEssayDetail(showCKEditor);
      });
    } else {
      $rootScope.myVar = !$scope.myVar;
      $timeout(function () {
        $rootScope.myVar = false;
        $('#divProgress').addClass('hide');
      }, 2500);
    }
  };

  function getDiscuss(page) {
    EssayService.getDiscussesOnEssay(essayId, page, 4).then(function(data) {
      if(data.data.status == 'true') {
        $scope.count = data.data.count;
        $scope.discussiones = data.data.request_data_result;

        for(var i = 0; i < $scope.discussiones.length; i++) {
          $scope.discussiones[i].image = StudentService.getAvatar($scope.discussiones[i].authorId);
          $scope.discussiones[i].content = decodeURIComponent($scope.discussiones[i].content);
          $scope.discussiones[i].content = $sce.trustAsHtml($scope.discussiones[i].content);
          $scope.discussiones[i].creationDate = convertUnixTimeToTime($scope.discussiones[i].creationDate);
          listDiscus.push($scope.discussiones[i]);
        }
        $scope.discussiones = listDiscus;
      }
      EssayService.getDiscussesOnEssay(essayId, page++, 4).then(function(data) {
        if(data.data.request_data_result.length < 4) {
          $scope.nextMore = 1;
        }
      });
    });
  }

  $scope.loadMoreComment = function() {
    clickD++;
    getDiscuss(clickD);
  };

  $scope.likeCommentEssay = function(cid) {
    if(userId != null) {
      EssayService.likeCommentEssay(userId, cid).then(function(data) {
        var ex = parseInt($('#c' + cid).html(), 10);
        if(data.data.request_data_result[0].likecomment == 'Y') {
          $('#c' + cid).html('+' + (ex + 1));
          $('.data_left').find('.c' + cid).html('Liked');
        } else {
          $('#c' + cid).html('+' + (ex - 1));
          $('.data_left').find('.c' + cid).html('');
        }
      });  
    }
  };

  $scope.deleteComment = function(cid) {
    $('#deleteItem').modal('show');
    idRemove = cid;
  };

  $scope.deleteItem = function() {
    CommentService.deleteComment(idRemove).then(function(data) {
      if(data.data.status) {
        listDiscus = [];
        for(var i = 1; i <= clickD; i++) {
          getDiscuss(i);
        }
      }
    });
  };

  $scope.editComment = function(cid, discuss) {
    editCommentId = cid;
    $scope.discussOld = discuss;
    $('#editDiscuss').modal('show');
    CKEDITOR.instances['discuss'].setData(decodeURIComponent($scope.discussOld));
  };

  $scope.updateComment = function() {
    var content = CKEDITOR.instances['discuss'].getData();
    CommentService.editComment(editCommentId, content).then(function(data) {
      if(data.data.request_data_result) {
        listDiscus = [];
        for(var i = 1; i <= clickD; i++) {
          getDiscuss(i);
        }
      }
    });
  };

  $scope.backupUpload = function() {
    $location.path('/upload_essay');
  };

  $scope.backupView = function() {
    $location.path('/all_essay');
  };

}]);