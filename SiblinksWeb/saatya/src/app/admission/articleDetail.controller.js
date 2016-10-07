brotControllers.controller('ArticleDetailCtrl', ['$scope', '$rootScope', '$routeParams', '$log', '$location', '$sce', '$timeout', 'AdmissionService', 'StudentService', 'CommentService',
  function ($scope, $rootScope, $routeParams, $log, $location, $sce, $timeout, AdmissionService, StudentService, CommentService) {
  
  var userId = localStorage.getItem('userId');
  var arId = $routeParams.idArtilce;
  $scope.clickD = 1;
  $scope.userId = userId;
  var idRemove, editCommentId;
  var listDiscus = [];

  init();

  function init() {
    var editor = CKEDITOR.replace( 'txtDiscus', {
      toolbar: 'MyToolbar'
    });
    editor.config.width = 933;
    editor.config.height = 300;
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

    AdmissionService.getArticleDetail(arId).then(function(data) {
      if(data.data.status) {
        $scope.articleDetail = data.data.request_data_result[0];
        if($scope.articleDetail.imageUser == null) {
          $scope.articleDetail.imageUser = 'https://pbs.twimg.com/profile_images/345217437/man.jpg';
        } else {
          $scope.articleDetail.imageUser = StudentService.getAvatar($scope.articleDetail.userid);
        }

        if($scope.articleDetail.image != null) {
          $scope.articleDetail.image = AdmissionService.getImageArticle($scope.articleDetail.arId);
        }
        var content = decodeURIComponent($scope.articleDetail.content);
        $scope.articleDetail.content = $sce.trustAsHtml(content);
        $scope.articleDetail.creationDate = convertUnixTimeToTime($scope.articleDetail.creationDate);
      }
    });

    getDiscuss($scope.clickD);

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

  $scope.addArticleComment = function() {
    if($scope.joinDiscussion != null) {
      AdmissionService.addCommentArticle(userId, $scope.joinDiscussion, arId).then(function(data) {
        if(data.data.status == 'true') {
          AdmissionService.getDiscussesOnArticle(arId, 1, 4).then(function(data) {
            if(data.data.status == 'true') {
              $scope.count = data.data.count;
              $scope.discussiones = data.data.request_data_result;

              for(var i = 0; i < $scope.discussiones.length; i++) {
                if($scope.discussiones[i].idFacebook == null && $scope.discussiones[i].idGoogle == null) {
                  $scope.discussiones[i].imageUrl = StudentService.getAvatar($scope.discussiones[i].authorId);  
                }
                
                $scope.discussiones[i].creationDate = convertUnixTimeToTime($scope.discussiones[i].creationDate);
              }
            }
          });
        }
      });
      $scope.joinDiscussion = null;
    }
  };

  $scope.likeCommentArticle = function(cid) {
    if(userId != null) {
      AdmissionService.likeCommentArticle(userId, cid).then(function(data) {
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

  $scope.loadMoreDisscusion = function() {
    $scope.clickD++;
    getDiscuss($scope.clickD);
  };

  function getDiscuss (page) {
    AdmissionService.getDiscussesOnArticle(arId, $scope.clickD, 4).then(function(data) {
      if(data.data.status == 'true') {
        var objectDiss = {};
        $scope.count = data.data.count;
        objectDiss = data.data.request_data_result;
        for(var i = 0; i < objectDiss.length; i++) {
          if(objectDiss[i].idFacebook == null && objectDiss[i].idGoogle == null) {
            objectDiss[i].imageUrl = StudentService.getAvatar(objectDiss[i].authorId);
          }
          
          objectDiss[i].content = decodeURIComponent(objectDiss[i].content);
          objectDiss[i].content = $sce.trustAsHtml(objectDiss[i].content);

          objectDiss[i].creationDate = convertUnixTimeToTime(objectDiss[i].creationDate);
          listDiscus.push(objectDiss[i]);
          $scope.discussiones = listDiscus;
        }
      } else {
        $location.url('/video_tutorial');
      }

      AdmissionService.getDiscussesOnArticle(arId, $scope.clickD + 1, 4).then(function(data) {
        if(data.data.request_data_result.length === 0) {
          $scope.featureD = 1;
        }
      });
    });
  }

  $scope.addClassHide = function() {
    if(userId == null) {
      return 'hide';
    }
    return '';
  };

  $scope.showCKEditor = function(event) {
    showCKEditor = true;
    var ele = event.currentTarget;
    $('.bottom_detailArticle .discussion .boxCkComment').toggle('slow');
    $('.bottom_detailArticle .discussion .ckComment').removeClass('hide');
    $('.bottom_detailArticle .discussion .go').addClass('hide');
    $('.bottom_detailArticle .discussion .btnHide').removeClass('hide');
  };

  $scope.cancel = function() {
    revertCKeditorArticleDetail(showCKEditor);
  };

  $scope.sendDiscus = function() {
    var content = CKEDITOR.instances["txtDiscus"].getData();
    if(content.length > 0) {
      AdmissionService.addCommentArticle(userId, content, arId).then(function(data) {
        if(data.data.status == 'true') {
          $scope.clickD = 1;
          listDiscus = [];
          getDiscuss($scope.clickD);
        }
        revertCKeditorArticleDetail(showCKEditor);
      });
    } else {
      $rootScope.myVar = !$scope.myVar;
      $timeout(function () {
        $rootScope.myVar = false;
        $('#divProgress').addClass('hide');
      }, 2500);
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
        for(var i = 1; i <= $scope.clickD; i++) {
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
        for(var i = 1; i <= $scope.clickD; i++) {
          getDiscuss(i);
        }
      }
    });
  };

}]);