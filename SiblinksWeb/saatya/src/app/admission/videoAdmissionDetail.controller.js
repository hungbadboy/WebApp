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