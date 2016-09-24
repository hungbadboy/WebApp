brotControllers.controller('AdmissionCtrl', ['$scope', '$rootScope', '$log', '$location', '$http', '$timeout', 'AdmissionService', 'StudentService',
	function ($scope, $rootScope, $log, $location, $http, $timeout, AdmissionService, StudentService) {
	
  var userType = localStorage.getItem('userType');

  AdmissionService.getAdmission().then(function(data) {
    if(data.data.status) {
      $scope.admission = data.data.request_data_result;
      AdmissionService.getSubAdmission($scope.admission[0].id).then(function(data) {
        if(data.data.status) {
          $scope.subAdmission = data.data.request_data_result;
        }
      });
    }
  });

  AdmissionService.getArticles(2, 1, 3).then(function(data) {
    if(data.data.status) {
      $scope.countArticle = data.data.count;
      $scope.articles = data.data.request_data_result;
      
      if($scope.articles.length === 0) {
        $scope.errorData = DATA_ERROR.noDataFound;  
      }

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

        if($scope.articles[i].image != null) {
          $scope.articles[i].image = AdmissionService.getImageArticle($scope.articles[i].arId);
        }

        $scope.articles[i].creationDate = convertUnixTimeToTime($scope.articles[i].creationDate);
      }
    }
  });

  $scope.beforeApplying = function(idSubAdmission, idTopicSubAdmission) {
    $location.url('/admission/videoadmission/listvideoadmission/' + idSubAdmission + '/' + idTopicSubAdmission);
  };

  $scope.application = function(idSubAdmission) {
    $location.url('/admission/videoadmission/listvideoadmission/' + idSubAdmission + '/3');
  };

  $scope.uploadEssay = function() {
    if(userType == 'S') {
      window.location.href = '#/upload_essay';
    }
  };

  $scope.afterApplying = function(idSubAdmission) {
    $location.url('/admission/videoadmission/listvideoadmission/' + idSubAdmission + '/6');
  };

  $scope.showArticle = function() {
    $location.path('/admission/article');
  };

  $scope.showArticleDetail = function(idArtilce) {
    $location.url('/admission/article/articledetail/' + idArtilce);
  };

}]);

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

brotControllers.controller('ListVideoAdmissionCtrl', ['$scope', '$rootScope', '$routeParams', '$log', '$location', 'AdmissionService', 
  function ($scope, $rootScope, $routeParams, $log, $location, AdmissionService) {
  
  var userId = localStorage.getItem('userId');
  var idSubAdmission = $routeParams.idSubAdmission;
  var idTopic = $routeParams.idTopic;
  var numberPage, idSubTopic;
  var listPage = [];
  // $scope.page = $routeParams.page;

  $rootScope.page = 1;

  $scope.errorData = DATA_ERROR.noDataFound;
  $scope.orderType = $routeParams.order;

  init();

  function init() {

    if($scope.orderType != 'syllabus' && $scope.orderType != 'rating' && $scope.orderType != 'popular') {
      $scope.orderType = 'syllabus';
    }

    AdmissionService.getTopicSubAdmission(idSubAdmission).then(function(data) {
      if(data.data.status) {
        $scope.topicSubAdmission = data.data.request_data_result;
        for(var i in $scope.topicSubAdmission) {
          if($scope.topicSubAdmission[i][0].id == idTopic) {
            idSubTopic = $scope.topicSubAdmission[i][0].idSubAdmission;
            $scope.title = $scope.topicSubAdmission[i][0].subName;
            $scope.seletedCatName = $scope.topicSubAdmission[i][0].name;
            delete data.data.request_data_result[i];
            $scope.topicSubAdmission = data.data.request_data_result;
            break;
          }
        }
      }
    });

    AdmissionService.orderVideoAdmissionPN(idTopic, $rootScope.page, 10, $scope.orderType).then(function(data) {
      dataVideoList(data);
    });
  }

  $scope.openAnotherCat = function(topicId) {
    $location.url('/admission/videoadmission/listvideoadmission/' + idSubAdmission + '/' + topicId);
  };

  $scope.showVideoDetail = function(vId) {
    $location.url('/admission/videoadmission/videodetail/' + idSubAdmission + '/' + idTopic + '/' + vId);
  };

  $scope.addClassActive = function(page) {
    if(page == $rootScope.page) {
      return 'span_active';
    }
    return '';
  };

  $scope.showListVideoWithPage = function(page) {
    $rootScope.page = page;
    init();
  };

  $scope.prevPage = function() {
    if($rootScope.page > 1) {
      $rootScope.page = parseInt($rootScope.page, 10) - 1;
      init();
    }
  };

  $scope.nextPage = function() {
    if($rootScope.page < numberPage) {
      $rootScope.page = parseInt($rootScope.page, 10) + 1;
      init();
    }
  };

  $scope.orderVideos = function(order) {
    $location.url('/admission/videoadmission/listvideoadmission/' + idSubAdmission + '/' + idTopic + '?order=' + order);
  };

  function dataVideoList (data) {
    if(data.data.status) {
      $scope.countVideo = data.data.count;
      $scope.videos = data.data.request_data_result;
      numberPage = Math.ceil((data.data.count) / 10);

      showPage(numberPage, $rootScope.page, function(response) {
        $scope.listPage = response;
      });

      $.each($scope.videos, function(index, value) {
        // $scope.videos[index].image = AdmissionService.getImageVideoAdmission(value.vId);
        
        var stripped = $scope.videos[index].description.replace(/(<([^>]+)>)/ig, "");
        $scope.videos[index].description = stripped.replace(/&nbsp;/ig, " ");

        var rate = value.rating;
        if(rate > 0) {
          showRating(rate, function(response) {
            $scope.videos[index].arr_rate = response;
          });
        }
      });
      $scope.showItem = true;
      if(userId != null) {
        AdmissionService.getIdVideoAdmissionWatched(userId).then(function(data) {
          var listWatchedVideoAdmission = data.data.request_data_result;
          for(var i in listWatchedVideoAdmission) {
            $('#' + listWatchedVideoAdmission[i].vid).find('.status').css({'opacity':1});
          }
        });
      }
    }
  }

  $scope.loadAdmission = function() {
    $location.path('/admission');
  };

  $scope.loadSubAdmission = function() {
    $location.path('/admission/videoadmission/' + idSubAdmission);
  };

  $('.select_topic').on('show.bs.dropdown', function(e){
    var $dropdown = $(this).find('.dropdown-menu');
    var orig_margin_top = parseInt($dropdown.css('margin-top'), 10);
    $dropdown.css({'margin-top': (orig_margin_top + 10) + 'px', opacity: 0}).animate({'margin-top': orig_margin_top + 'px', opacity: 1}, 300, function(){
      $(this).css({'margin-top':''});
    });
  });
   // Add slidedown & fadeout animation to dropdown
  $('.select_topic').on('hide.bs.dropdown', function(e){
    var $dropdown = $(this).find('.dropdown-menu');
    var orig_margin_top = parseInt($dropdown.css('margin-top'), 10);
    $dropdown.css({'margin-top': orig_margin_top + 'px', opacity: 1, display: 'block'}).animate({'margin-top': (orig_margin_top + 10) + 'px', opacity: 0}, 300, function(){
      $(this).css({'margin-top':'', display:''});
    });
  });

}]);

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

brotControllers.controller('VideoAdmissionDetailCtrl', ['$scope', '$rootScope', '$routeParams', '$log', '$location', '$sce', '$timeout', 'AdmissionService', 'StudentService', 'CommentService', 
  function ($scope, $rootScope, $routeParams, $log, $location, $sce, $timeout, AdmissionService, StudentService, CommentService) {

  $scope.userId = localStorage.getItem('userId');
  var idSubAdmission = $routeParams.idSubAdmission;
  var idTopicSubAdmission = $routeParams.idTopicSubAdmission;
  var nextVideo, idRemove, listDiscuss = [];
  $scope.rated = false;
  $scope.vId = $routeParams.vId;
  $scope.clickD = 1;

  init();

  function init() {
    AdmissionService.updateViewVideoAdmission($scope.vId).then(function(data) {
      if(data.data.request_data_result == 'Done') {
        if($scope.userId != null) {
          AdmissionService.updateVideoAdmissionWatched($scope.userId, $scope.vId).then(function(data) {
          });  
        }

        AdmissionService.getVideoAdmissionDetail(idSubAdmission, idTopicSubAdmission, $scope.vId).then(function(data) {
          if(data.data.status) {
            $scope.video = data.data.request_data_result[0];
            
            $scope.video.description = $sce.trustAsHtml($scope.video.description);

            var url = $scope.video.youtubeUrl;
            if($scope.video.youtubeUrl.charAt(4) != 's') {
              onYouTubeIframeAPIReady(url.substring(21, url.length));
              // $scope.video.youtubeUrl = url.substring(0,19) + 'embed' + url.substring(20, url.length) + '?autoplay=1';
            } else {
              onYouTubeIframeAPIReady(url.substring(32, url.length));
              // $scope.video.youtubeUrl = url.substring(0,24) + 'embed/' + url.substring(32, url.length) + '?autoplay=1';
            }

            AdmissionService.checkUserRatingVideoAdmission($scope.userId, $scope.vId).then(function(data) {
              if(data.data.status) {
                if(data.data.request_data_result.length > 0) {
                  $scope.rated = true;
                  showRating($scope.video.rating, function(response) {
                    $scope.video.arr_rate = response;
                  });
                } else {
                  showClickRating(function(response) {
                    $scope.video.arr_rate = response;
                  });
                }
              }
            });
          }
        });
      }
    });

    AdmissionService.getVideoTopicSubAdmissionPN(idTopicSubAdmission).then(function(data) {
      if(data.data.status) {
        $scope.listVideos = data.data.request_data_result;
        if($scope.listVideos.length < 5) {
          $(this).find('.wrap_science').css({'overflow-y': 'hidden'});
        }

        for(var i = 0; i < $scope.listVideos.length; i++) {
          if($scope.vId == $scope.listVideos[i].vId) {
            if($scope.listVideos[i + 1].vId !== undefined) {
              nextVideo = $scope.listVideos[i + 1].vId;
              break;  
            }
          }
        }
      }
    });

    AdmissionService.getVideoTopicSubAdmission(idTopicSubAdmission).then(function(data) {
      if(data.data.status == 'true') {
        $scope.listVideo = data.data.request_data_result;
        $scope.idVideoPrev = 0;
        $scope.idVideoNext = 0;
        for(var i in $scope.listVideo) {
          if($scope.listVideo[i].vId == $scope.vId) {
            if(i > 0) {
              $scope.idVideoPrev = $scope.listVideo[parseInt(i, 10) - 1].vId;
            }
            if(i < $scope.listVideo.length - 1) {
              $scope.idVideoNext = $scope.listVideo[parseInt(i, 10) + 1].vId;
            }
          }
        }
        
        setTimeout(function(){
          $('.wrap_science').animate({
            scrollTop: $('.wrap_science .item#' + $scope.vId).offset().top - $('.wrap_science').offset().top
          }, 2000);
        }, 1500);
      }
    });

    var editor = CKEDITOR.replace( 'txtDiscus');
    editor.config.width = 598;
    editor.config.height = 200;
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
          content += "<img style='max-width: 460px;' src='"+ local +"'/>";
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

    AdmissionService.checkUserRatingVideoAdmission($scope.userId, $scope.vId).then(function(data) {
      if(data.data.status) {
        if(data.data.request_data_result.length > 0) {
          $scope.rated = true;
        }
      }
    });

    getDiscuss(1);
  }

  function getDiscuss(page) {
    AdmissionService.getVideoAdmissionComment($scope.vId, page, 4).then(function(data) {
      if(data.data.status == 'true') {
        $scope.count = data.data.count;
        $scope.discussiones = data.data.request_data_result;
        for(var i = 0; i < $scope.discussiones.length; i++) {
          if($scope.discussiones[i].idFacebook != null || $scope.discussiones[i].idFacebook != null) {
            //TODO
          } else if($scope.discussiones[i].imageUser == null) {
            $scope.discussiones[i].imageUser = 'https://pbs.twimg.com/profile_images/345217437/man.jpg';
          } else {
            $scope.discussiones[i].imageUser = StudentService.getAvatar($scope.discussiones[i].authorId);
          }
          $scope.discussiones[i].content = decodeURIComponent($scope.discussiones[i].content);
          $scope.discussiones[i].content = $sce.trustAsHtml($scope.discussiones[i].content);

          $scope.discussiones[i].creationDate = convertUnixTimeToTime($scope.discussiones[i].creationDate);
          listDiscuss.push($scope.discussiones[i]);
        }
        $scope.discussiones = listDiscuss;
      }
    });
  }

  $(".wrap_title_rate").on('mouseenter', '.rating', function() {
    $('.wrap_title_rate').find('.rating').attr('src', 'assets/img/grey_star.png');
    for(var i = 1; i <= $(this).context.id; i++) {
      $(".wrap_rate").find('#' + i).attr('src', 'assets/img/yellow _star.png');
    }
  });

  $(".wrap_title_rate").on('mouseleave', '.block_rate', function() {
    for(var i = 0; i < 5; i++) {
      $(".wrap_rate").find('#' + (parseInt(i, 10) + 1)).attr('src', $scope.video.arr_rate[i]);  
    }
  });

  $(".wrap_rate").on('click', '.rating', function() {
    if($scope.userId == null) {
      $('#popSignIn').modal('show');
    } else {
      var rate = $(this).context.id;
      AdmissionService.rateVideoAdmission($scope.userId, $scope.vId, rate).then(function(data) {
        if(data.data.status) {
          $scope.rated = true;
          AdmissionService.getRatingVideoAdmission($scope.vId).then(function(data) {
            if(data.data.status) {
              showRating(data.data.request_data_result[0].rating, function(response) {
                $scope.video.arr_rate = response;
              });
            }
          });
        }
      });
    }
  });

  function onYouTubeIframeAPIReady(youtubeId) {
    player = new YT.Player('video', {
      height: '400',
      width: '630',
      videoId: youtubeId,
      events: {
        'onReady': onPlayerReady,
        'onStateChange': onPlayerStateChange
      },
      playerVars: {
        showinfo: 0,
        autohide: 1,
        theme: 'light'
      }
    });
  }

  function onPlayerReady(event) {
    event.target.playVideo();
  }

  function onPlayerStateChange(event) {        
    if(event.data === 0) {
      window.location.href = '#/admission/videoadmission/videodetail/' + idSubAdmission + '/' + idTopicSubAdmission + '/' + nextVideo;
    }
  }


  $scope.showPageSubAdmission = function(idSubAdmission) {
    $location.url('/admission/videoadmission/' + idSubAdmission);
  };

  $scope.showPageTopicSubAdmission = function(idSubAdmission, idTopicSubAdmission) {
    $location.url('/admission/videoadmission/listvideoadmission/' + idSubAdmission + '/' + idTopicSubAdmission);
  };

  $scope.showVideo = function(vid) {
    $location.url('/admission/videoadmission/videodetail/' + idSubAdmission + '/' + idTopicSubAdmission + '/' + vid);
  };

  $scope.addVideoAdmissionComment = function() {
    if($scope.joinDiscussion != null) {
      AdmissionService.addCommentVideoAdmission($scope.userId, $scope.joinDiscussion, $scope.vId).then(function(data) {
        if(data.data.status == 'true') {
          AdmissionService.getVideoAdmissionComment($scope.vId, 1, 4).then(function(data) {
            if(data.data.status == 'true') {
              $scope.count = data.data.count;
              $scope.discussiones = data.data.request_data_result;
              for(var i = 0; i < $scope.discussiones.length; i++) {
                if($scope.discussiones[i].idFacebook != null || $scope.discussiones[i].idGoogle != null) {
                  //TODO
                } else if($scope.discussiones[i].imageUser == null) {
                  $scope.discussiones[i].imageUser = 'https://pbs.twimg.com/profile_images/345217437/man.jpg';
                } else {
                  $scope.discussiones[i].imageUser = StudentService.getAvatar($scope.discussiones[i].authorId);
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

  $scope.loadMoreDisscusion = function() {
    $scope.clickD++;
    AdmissionService.getVideoAdmissionComment($scope.vId, $scope.clickD, 4).then(function(data) {
      if(data.data.status == 'true') {
        var objectDiss = data.data.request_data_result;
        for(var i = 0; i < objectDiss.length; i++) {
          if(objectDiss[i].idFacebook != null || objectDiss[i].idGoogle != null) {
            //TODO
          } else if(objectDiss[i].imageUser == null) {
            objectDiss[i].imageUser = 'https://pbs.twimg.com/profile_images/345217437/man.jpg';
          } else {
            objectDiss[i].imageUser = StudentService.getAvatar(objectDiss[i].authorId);
          }

          objectDiss[i].content = decodeURIComponent(objectDiss[i].content);
          objectDiss[i].content = $sce.trustAsHtml(objectDiss[i].content);

          objectDiss[i].creationDate = convertUnixTimeToTime(objectDiss[i].creationDate);
          $scope.discussiones.push(objectDiss[i]);
        }
      } else {
        $location.url('/video_tutorial');
      }

      AdmissionService.getVideoAdmissionComment($scope.vId, $scope.clickD + 1, 4).then(function(data) {
        if(data.data.request_data_result.length === 0) {
          $scope.featureD = 1;
        }
      });
    });
  };

  $scope.likeCommentVideoAdmission = function(cid) {
    if($scope.userId != null) {
      AdmissionService.likeCommentVideoAdmission($scope.userId, cid).then(function(data) {
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

  $scope.addClassHide = function() {
    if($scope.userId == null) {
      return 'hide';
    }
    return '';
  };

  $scope.showCKEditor = function(event) {
    showCKEditor = true;
    var ele = event.currentTarget;
    $('.content .discus .boxCkComment').toggle('slow');
    $('.content .discus .ckComment').removeClass('hide');
    $('.content .discus .go').addClass('hide');
    $('.content .discus .btnHide').removeClass('hide');
  };

  $scope.cancel = function() {
    revertCKeditor(showCKEditor);
  };

  $scope.sendDiscus = function() {
    var content = CKEDITOR.instances["txtDiscus"].getData();
    if(content.length > 0) {
      AdmissionService.addCommentVideoAdmission($scope.userId, content, $scope.vId).then(function(data) {
        if(data.data.status == 'true') {
          AdmissionService.getVideoAdmissionComment($scope.vId, 1, 4).then(function(data) {
            if(data.data.status == 'true') {
              $scope.count = data.data.count;
              $scope.discussiones = data.data.request_data_result;
              for(var i = 0; i < $scope.discussiones.length; i++) {
                if($scope.discussiones[i].idFacebook != null || $scope.discussiones[i].idGoogle != null) {
                  //TODO
                } else if($scope.discussiones[i].imageUser == null) {
                  $scope.discussiones[i].imageUser = 'https://pbs.twimg.com/profile_images/345217437/man.jpg';
                } else {
                  $scope.discussiones[i].imageUser = StudentService.getAvatar($scope.discussiones[i].authorId);
                }
                $scope.discussiones[i].content = decodeURIComponent($scope.discussiones[i].content);
                $scope.discussiones[i].content = $sce.trustAsHtml($scope.discussiones[i].content);
                $scope.discussiones[i].creationDate = convertUnixTimeToTime($scope.discussiones[i].creationDate);
              }
            }
          });
          revertCKeditor(showCKEditor);
        }
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
        listDiscuss = [];
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
        listDiscuss = [];
        for(var i = 1; i <= $scope.clickD; i++) {
          getDiscuss(i);
        }
      }
    });
  };

}]);