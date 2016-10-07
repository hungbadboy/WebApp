brotControllers.controller('ArticleCtrl', ['$scope', '$rootScope', '$routeParams', '$log', '$location', 'AdmissionService', 'StudentService', 'CommentService',
  function ($scope, $rootScope, $routeParams, $log, $location, AdmissionService, StudentService, CommentService) {  
  $scope.clickA = 1;
  $scope.userType = localStorage.getItem('userType');

  init();

  function init() {
    AdmissionService.getArticles(2, 1, 7).then(function(data) {
      if(data.data.status) {
        $scope.countArticle = data.data.count;
        $scope.articles = data.data.request_data_result;
        
        for(var i = 0; i < $scope.articles.length; i++) {

          var mystring = $scope.articles[i].description;
          var stripped = mystring.replace(/(<([^>]+)>)/ig," ");

          if($scope.articles[i].description != null && $scope.articles[i].description.length > 180) {
            $scope.articles[i].description = stripped.substring(0, 180) + ' ...';
          } else {
            $scope.articles[i].description = stripped;
          }

          if($scope.articles[i].imageUser == null) {
            $scope.articles[i].imageUser = 'https://pbs.twimg.com/profile_images/345217437/man.jpg';
          } else {
            $scope.articles[i].imageUser = StudentService.getAvatar($scope.articles[i].userid);
          }
          $scope.articles[i].image = AdmissionService.getImageArticle($scope.articles[i].arId);

          $scope.articles[i].creationDate = convertUnixTimeToTime($scope.articles[i].creationDate);        
        }
      }
    });
  }
  
  $scope.showAddArticle = function() {
    $('#create_article').modal('show');
    ckeditor('description');
  };

  function ckeditor(option) {
    var editor;
    $('#create_article #upload-file-input').val('');
    $('#create_article .title').val('');
    if(CKEDITOR.instances[option]) {
      CKEDITOR.instances[option].destroy(true);
      editor = CKEDITOR.replace(option, {
        width: 700,
        height: 310
      });
      editor.config.allowedContent = true;
      configCKEditor(editor);
    } else {
      editor = CKEDITOR.replace(option, {
        width: 700,
        height: 310
      });
      editor.config.allowedContent = true;
      configCKEditor(editor);
    }
  }

  function configCKEditor(editor) {
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
  }

  $('#select-file').click(function() {
    $('#file').click();
  });

  $('#file').change(function() {
    $('.waitingUp').removeClass('hide');
    CommentService.uploadImageEdit(function(data) {
      if(data.status == 'true') {
        var local = data.request_data_result;
        $('#uploadImage').modal('hide');
        $('.waitingUp').addClass('hide');
        var content = CKEDITOR.instances["description"].getData();
        content += "<img style='max-width: 915px;' src='"+ local +"'/>";
        CKEDITOR.instances["description"].setData(content);
      } else {
        $('.waitingUp').addClass('hide');
        $scope.errorUpload = data.request_data_result;
        alert($scope.errorUpload);
        return;
      }
    });
  });

  $scope.createArticle = function() {
    var image = $('#create_article #upload-file-input').val();
    var title = $('#create_article .title').val();
    var content = CKEDITOR.instances['description'].getData();
    var stripped = content.replace(/(<([^>]+)>)/ig, "");
    var description = stripped.replace(/&nbsp;/ig, " ");
    if(description.length > 340) {
      description = description.substring(0, 340) + '...';  
    }
    if(image) {
      AdmissionService.uploadImage(function(data) {
        if(data.status == 'true') {
          image = data.request_data_result;
          AdmissionService.createArticle(title, image, description, content).then(function(data) {
            if(data.data.status) {
              $('#create_article').modal('hide');
              init();
            }
          });
        } else {
          $('.imageMess').text(data.request_data_result).removeClass('hide');
        }
      });  
    } else {
      image = null;
      AdmissionService.createArticle(title, image, description, content).then(function(data) {
        if(data.data.status) {
          $('#create_article').modal('hide');
          init();
        }
      });
    }
  };

  $scope.showArticleDetail = function(idArtilce) {
    $location.path('/admission/article/articledetail/' + idArtilce);
  };

  $scope.loadMoreArticle = function() {
    $scope.clickA++;
    AdmissionService.getArticles(2, $scope.clickA, 7).then(function(data) {
      if(data.data.status) {
        var objectArticle = {};
        objectArticle = data.data.request_data_result;
        for(var i = 0; i < objectArticle.length; i++) {

          if(objectArticle[i].imageUser == null) {
            objectArticle[i].imageUser = 'https://pbs.twimg.com/profile_images/345217437/man.jpg';
          } else {
            objectArticle[i].imageUser = StudentService.getAvatar(objectArticle[i].userid);
          }

          objectArticle[i].image = AdmissionService.getImageArticle(objectArticle[i].arId);
          objectArticle[i].creationDate = convertUnixTimeToTime(objectArticle[i].creationDate);
          $scope.articles.push(objectArticle[i]);
        }
      }

      AdmissionService.getArticles(2, $scope.clickA + 1, 7).then(function(data) {
        if(data.data.request_data_result.length === 0) {
          $scope.featureAr = 1;
        }
      });
    });
  };
}]);