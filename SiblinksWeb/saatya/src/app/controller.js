brotControllers.run(function($rootScope) {
  $rootScope.$on("$routeChangeStart", function() {
    $('.notification .wrap_img').addClass('hide');
    $('.notification .icon_notification_white').removeClass('hide');
    var thisClass = $('.user-setting-wrapper span.current');
    if(thisClass.hasClass('selected')) {
      thisClass.removeClass('selected');
      $('.user-setting').addClass('hide');
    }
  });
});

angular.module('brotApp').controller('MainController', function($scope, $http, $route) {
   //your code here
  $scope.title = "Main";
  });

angular.module('brotApp').controller('SignIn', function($scope, $rootScope, $http, $timeout, StudentService) {
  $scope.login = function() {
    var userName = $('.userName').val();
    var passWord = $('.passWord').val();
    StudentService.loginUser(userName, passWord, function(data) {
      if(data.status == 'true') {
        var dataUser = data.request_data_result[0];
        setStorage('userName', dataUser['username'], 30);
        setStorage('userId', dataUser['userid'], 30);
        setStorage('userType', dataUser['userType'], 10);
        $('#header .login').addClass('hide');
        $('#header .log_out').removeClass('hide');
        var nameHome = '';
        if (dataUser['firstname'] == null || dataUser['lastname'] == null) {
          nameHome = capitaliseFirstLetter(dataUser['username'].substring(0, dataUser['username'].indexOf('@')));
        }
        else {
          nameHome = capitaliseFirstLetter(dataUser['firstname']) + ' ' + capitaliseFirstLetter(dataUser['lastname']);
        }
        setStorage('nameHome', nameHome, 30);
        $('#header .log_out .current').text(nameHome);
        $('#popSignIn').modal('hide');
        location.reload();
      } else {
        $('.loginMess').text(data.request_data_result[0]).removeClass('hide');
      }
    });
  };

  init();

  function init() {
    $scope.username = 'Username*';
    $scope.password = 'Password*';
    $('#popSignIn .passWord').keypress(function(event) {
      if(event.keyCode == 13) {
        $scope.login();
      }
    });

    $('#popSignIn .userName').focus(function(){
      $(this).attr('placeholder', '');
    });
    $('#popSignIn .userName').focusout(function(){
      $(this).attr('placeholder', $scope.username);
    });

    $('#popSignIn .passWord').focus(function(){
      $(this).attr('placeholder', '');
    });
    $('#popSignIn .passWord').focusout(function(){
      $(this).attr('placeholder', $scope.password);
    });
  }

  $scope.showForgotPassword = function() {
  $('.wrap_info .loadingdivForgot').find('#loading').addClass('hide');
    $('.wrap_info .wrap_event').find('.forgot').removeAttr('disabled');
    $('#popSignIn').modal('hide');
    var userName = $('#popSignIn').find('.userName').val();
    $('#forgotPassword').modal('show');
    $('#forgotPassword .userName').val(userName);

    $('#forgotPassword .wrap_info').removeClass('hide');
    $('#forgotPassword .wrap_alert').addClass('hide');
  };

  $scope.loginFacebook = function() {
    loginFBService(function(data) {
      $scope.userName = data.email;
      $scope.firstName = data.first_name;
      $scope.lastName = data.last_name;
      $scope.facebookId = data.id;
      $scope.image = data.picture.data.url;
      StudentService.loginFacebook($scope.userName, 'S', $scope.firstName, $scope.lastName, $scope.image, $scope.facebookId).then(function(data) {
        var dataUser = data.data.request_data_result[0];
        setStorage('userName', dataUser['userName'], 30);
        setStorage('userId', dataUser['userid'], 30);
        setStorage('userType', dataUser['userType'], 10);

        var nameHome = $scope.firstName + ' ' + $scope.lastName;
        setStorage('nameHome', nameHome, 30);
        $('#header .log_out .current').text(nameHome);
        location.reload();
      });
    });
  };

  function onLoadCallback() {
    gapi.client.setApiKey('AIzaSyCRzAc2Jd3r3mhrhBgiWShvEIZNXRWCoRk'); //set your API KEY
    gapi.client.load('plus', 'v1',function(){});//Load Google + API
  }

  function loginCallback(result) {
    if(result['status']['signed_in']) {
      gapi.client.load('plus','v1', function(){
        var request = gapi.client.plus.people.get({
          'userId': 'me'
        });
        request.execute(function(resp) {
          $scope.userName = resp.emails[0].value;
          $scope.firstName = resp.name.givenName;
          $scope.lastName = resp.name.familyName;
          $scope.googleid = resp.id;
          $scope.image = resp.image.url;
          var nameHome = resp.displayName;
          StudentService.loginGoogle($scope.userName, 'S', $scope.firstName, $scope.lastName, $scope.image, $scope.googleid).then(function(data) {
            var dataUser = data.data.request_data_result[0];
            setStorage('userName', dataUser['userName'], 30);
            setStorage('userId', dataUser['userid'], 30);
            setStorage('userType', dataUser['userType'], 10);
            setStorage('nameHome', nameHome, 30);
            $('#header .login').addClass('hide');
            $('#header .log_out').removeClass('hide');
            $('#header .log_out .current').text(nameHome);
            window.location.href = '/';
          });
        });
      });
    }  
  }

  $scope.loginGoogle = function() {
    var myParams = {
      //'clientid' : '667014101821-v1f9a583qgmt280gtci8n4a87k7uja2m.apps.googleusercontent.com', //You need to set client id
      'clientid' : GOOGLEID,
      'cookiepolicy' : 'single_host_origin',
      'callback' : loginCallback, //callback function
      'approvalprompt':'force',
      'scope' : 'https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/plus.profile.emails.read'
    };
    gapi.auth.signIn(myParams);
  };

});

angular.module('brotApp').controller('SignUp', function($scope, $http, StudentService) {
  $scope.showPopSignUp = function() {
    $('#popSignUp').modal('hide');
    $('body').find('.modal-backdrop').remove();
  };
  $scope.showPopSignUpComplex = function() {
    $('#popSignUp').modal('hide');
    $('body').find('.modal-backdrop').remove();
    window.location.href = '#/signupcomplete';
  };

  $scope.loginFacebook = function() {
    loginFBService(function(data) {
      $scope.userName = data.email;
      $scope.firstName = data.first_name;
      $scope.lastName = data.last_name;
      $scope.facebookId = data.id;
      $scope.image = data.picture.data.url;
      StudentService.loginFacebook($scope.userName, 'S', $scope.firstName, $scope.lastName, $scope.image, $scope.facebookId).then(function(data) {
        var dataUser = data.data.request_data_result[0];
        setStorage('userName', dataUser['userName'], 30);
        setStorage('userId', dataUser['userid'], 30);
        setStorage('userType', dataUser['userType'], 10);
        var nameHome = $scope.firstName + ' ' + $scope.lastName;
        setStorage('nameHome', nameHome, 30);
        $('#header .log_out .current').text(nameHome);
        location.reload();
      });
    });
  };

  function onLoadCallback() {
    gapi.client.setApiKey('AIzaSyCRzAc2Jd3r3mhrhBgiWShvEIZNXRWCoRk'); //set your API KEY
    gapi.client.load('plus', 'v1',function(){});//Load Google + API
  }

  function loginCallback(result) {
    if(result['status']['signed_in']) {
      gapi.client.load('plus','v1', function(){
        var request = gapi.client.plus.people.get({
          'userId': 'me'
        });
        request.execute(function(resp) {
          $scope.userName = resp.emails[0].value;
          $scope.firstName = resp.name.givenName;
          $scope.lastName = resp.name.familyName;
          $scope.googleid = resp.id;
          $scope.image = resp.image.url;
          var nameHome = resp.displayName;
          StudentService.loginGoogle($scope.userName, 'S', $scope.firstName, $scope.lastName, $scope.image, $scope.googleid).then(function(data) {
            var dataUser = data.data.request_data_result[0];
            setStorage('userName', dataUser['userName'], 30);
            setStorage('userId', dataUser['userid'], 30);
            setStorage('userType', dataUser['userType'], 10);
            setStorage('nameHome', nameHome, 30);
            $('#header .login').addClass('hide');
            $('#header .log_out').removeClass('hide');
            $('#header .log_out .current').text(nameHome);
            window.location.href = '/';  
          });
        });
      });
    }  
  }

  $scope.loginGoogle = function() {
    var myParams = {
     // 'clientid' : '667014101821-v1f9a583qgmt280gtci8n4a87k7uja2m.apps.googleusercontent.com', //You need to set client id
      'clientid' : GOOGLEID,
      'cookiepolicy' : 'single_host_origin',
      'callback' : loginCallback, //callback function
      'approvalprompt':'force',
      'scope' : 'https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/plus.profile.emails.read'
    };
    gapi.auth.signIn(myParams);
  };
});

angular.module('brotApp').controller('ForgotPassword', function($scope, $http) {
  $scope.showItem = true;
  $scope.forgot = function() {
    $('.wrap_info .loadingdivForgot').find('#loading').removeClass('hide');
    var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    var email = angular.element('#forgotPassword .userName').val();
    if(!regex.test(email)) {
      angular.element('#forgotPassword .error').removeClass('hide');
      angular.element('#forgotPassword .error').html('Email address not valid');
      return;
    } else {
      $scope.showItem = false;
      angular.element('#forgotPassword .error').addClass('hide');
    }
    angular.element('#forgotPassword .forgot').attr('disabled', 'disabled');
    $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'contact/forgotPassword',
      data: {
          "request_data_type": "contact",
          "request_data_method": "forgotPassword",
          "request_data": {
            "email": email
          }
      }
    }).success(function(json) {
        if(json.status) {
          $('#forgotPassword .wrap_info').addClass('hide');
          $('#forgotPassword .wrap_alert').removeClass('hide');
          $scope.showItem = true;
        } else {
          angular.element('#forgotPassword .error').removeClass('hide');
          angular.element('#forgotPassword .error').html('Email address is not exist');
        }
        angular.element('#forgotPassword .forgot').removeAttr('disabled');
    });
  };
});

brotControllers.controller('ForgotPasswordCtrl',
    ['$scope', '$rootScope', '$location', '$timeout', '$routeParams', '$log', 'StudentService',
        function($scope, $rootScope, $location, $timeout, $routeParams, $log, StudentService) {

  var token = $routeParams.token;
  $scope.show = 0;
  $scope.notFound = 0;

  StudentService.confirmToken(token).then(function(data) {
    if(data.data.request_data_result[0].count == 1) {
      $scope.notFound = 1;
    }
  });

  $scope.changePassword = function(newPass, confirmPass) {
    if(newPass == confirmPass && newPass !== undefined && confirmPass !== undefined) {
      StudentService.changePassword(token, newPass).then(function(data) {
        $log.info(data.data.request_data_result[0]);
        $scope.show = 1;
      });
    } else {
      $rootScope.myVar = !$scope.myVar;
      $timeout(function () {
        $rootScope.myVar = false;
      }, 2500);
      return;
    }
  };

  $scope.goHomePage = function() {
    window.location.href = '/';
  };

}]);

brotControllers.controller('UserHeaderController',
    ['$scope', '$rootScope', '$http', '$location', '$log', 'NotificationService',
        function($scope, $rootScope, $http, $location, $log, NotificationService) {

  // check login page
  brot.signin.statusStorageHtml();
  $rootScope.notifications = [];
  var username = localStorage.getItem('userName');
  var userId = localStorage.getItem('userId');

  $('#header').addClass('fixed');

  $('.icon_search').on('click', function () {
    $('.form_search').removeClass('hide').find('input').focus();
    hideNotification();
    hideProfileSetting();
  });

  $(document).on('click', function (e) {
    if(e.target != $('.form_search') && e.target.id != 'img-search' && !$('.form_search').find(e.target).length) {
      $('.form_search').addClass('hide');
      $('#header .w975').find('.search-text').val('');
      $('.form_search').find('.dropdown').removeClass('open');
      $(".check-search").prop('checked', false); 
    }
  });

  $scope.profile = function() {
    $('.user-setting-wrapper span.current').trigger('click');
    $location.path('/studentProfile/' + userId);
  };

  $scope.goToEditStudent = function() {
    $location.path('/editStudent/basic');
  };

  $scope.admission = function() {
    if(userId == null) {
      // brot.signin.signin();
      $('#popSignIn').modal('show');
    } else {
      $location.path('/admission');
    }
  };

  $scope.popupNotification = function() {
    if($('.notification .wrap_img').hasClass('hide')) {
      hideProfileSetting();
      $('.notification').find('.active').addClass('hide');
      showNotification($(this));
    } else {
      hideNotification();
    }
   
  };

  function init() {
    // show or hide notification panel
    if(userId != null) {
      NotificationService.getNotificationByUserId(userId).then(function(data) {
        $scope.listNotifications = data.data.request_data_result;
        $rootScope.countNotification = data.data.count;
        if($rootScope.countNotification == null) {
          NotificationService.getNotificationReaded(userId).then(function(data) {
            if(data.data.request_data_result.length > 0) {
              $scope.listNotifications = data.data.request_data_result;
              for(var i = 0; i < $scope.listNotifications.length; i++) {
                if($scope.listNotifications[i].notification.length > 70) {
                  $scope.listNotifications[i].notification = $scope.listNotifications[i].notification.substring(0, 70) + ' ...';
                }
              }
              $rootScope.notifications = $scope.listNotifications;
            } else {
              $scope.emptyNotification = 1;
              $scope.errorData = DATA_ERROR_NOTIFICATION.noNewNotification;
            }
          });
        } else {
          for(var i = 0; i < $scope.listNotifications.length; i++) {
            if($scope.listNotifications[i].notification.length > 70) {
              $scope.listNotifications[i].notification = $scope.listNotifications[i].notification.substring(0, 70) + ' ...';
            }
          }
          $rootScope.notifications = $scope.listNotifications;  
        }
      });
      
      $('.notification').removeClass('hide');

      // call ws to get notification of user

      $('.notification .icon_notification_white').click(function(event) {
        hideProfileSetting();
        showNotification($(this));
      });

      $('.notification .wrap_img .icon_notification').click(function(event) {
        hideProfileSetting();
        hideNotification();
      });

      $('.user-setting-wrapper span.current').click(function() {
        hideNotification();

        if($(this).hasClass('selected')) {
          $(this).removeClass('selected');
          $('.user-setting').addClass('hide');
        } else {
          $(this).addClass('selected');
          $('.user-setting').removeClass('hide');
        }
      });
    } else {
      $('.notification').addClass('hide');
    }
  }

  function showNotification(ele) {
    NotificationService.updateAllNotification(userId).then(function(data) {
      if(data.data.request_data_result) {
        $rootScope.countNotification = 0;
        $(ele).addClass('hide');
        $('.notification').find('.active').addClass('hide');
        $('.notification .wrap_img').removeClass('hide');
      }
    });
  }

  function hideNotification() {
    $('.notification').find('.active').removeClass('hide');
    $('.notification .wrap_img').addClass('hide');
    $('.notification .icon_notification_white').removeClass('hide');
  }

  function hideProfileSetting() {
    $('.user-setting-wrapper span.current').removeClass('selected');
    $('.user-setting').addClass('hide');
  }
  init();

  $scope.logout = function() {
    window.localStorage.clear();
    window.location.href = '/';
  };

  $scope.viewAllNotification = function() {
    window.location.href = '#/notification';
  };

  $scope.detailNotification = function(nid, subjectId, topicId, videoId, questionId, articleId, idSubAdmission, idTopicSubAdmission, idEssay) {
    NotificationService.getNotificationByUserId(userId).then(function(data) {
      $scope.listNotifications = data.data.request_data_result;
      $rootScope.countNotification = data.data.count;
      if($rootScope.countNotification == null) {
        NotificationService.getNotificationReaded(userId).then(function(data) {
          if(data.data.request_data_result.length > 0) {
            $scope.listNotifications = data.data.request_data_result;
            for(var i = 0; i < $scope.listNotifications.length; i++) {
              if($scope.listNotifications[i].notification.length > 70) {
                $scope.listNotifications[i].notification = $scope.listNotifications[i].notification.substring(0, 70) + ' ...';
              }
            }
            $rootScope.notifications = $scope.listNotifications;
          } else {
            $scope.emptyNotification = 1;
            $scope.errorData = DATA_ERROR_NOTIFICATION.noNewNotification;
          }
        });
      } else {
        for(var i = 0; i < $scope.listNotifications.length; i++) {
          if($scope.listNotifications[i].notification.length > 70) {
            $scope.listNotifications[i].notification = $scope.listNotifications[i].notification.substring(0, 70) + ' ...';
          }
        }
        $rootScope.notifications = $scope.listNotifications;  
      }

      if(subjectId == null) {
        if(articleId != null) {
          window.location.href = '#/admission/article/articledetail/' + articleId;
        } else if(idEssay != null) {
          window.location.href = '#/essay_detail/' + idEssay; 
        } else {
          window.location.href = '#/admission/videoadmission/videodetail/' + idSubAdmission + '/' + idTopicSubAdmission + '/' + videoId;  
        }
      } else {
        if(videoId != null && questionId == null) {
          window.location.href = '#/detailVideo/' + subjectId + '/' + topicId + '/' + videoId;
        } else {
          window.location.href = '#/question_detail?subject=' + subjectId + '&question_id=' + questionId;
        }
      } 
    });
  };

  $('#header .w975').find('.search-text').keypress(function(event) {
    var searchText = $('#header .w975').find('.search-text').val();

    if(event.keyCode == 13) {
      // if($(".item-select input:checked" ).val() == 1) {
      window.location.href = '#/searchVideo/' + searchText + '/' + 1;  
      // }
    }
  });

  $scope.showItemSearch = function() {
    if($('.form_search .dropdown').hasClass('open')) {
      $('.form_search').find('.dropdown').removeClass('open');
    } else {
      $('.form_search').find('.dropdown').addClass('open');
    }
  };

}]);

brotControllers.controller('SignUpController', ['$scope', '$http', '$log', function($scope, $http, $log) {
  $scope.title = 'Please complete the information below in order to login';
  $scope.user = {};

  // $('#signUpEmail .field .dob').mask('99/99/9999');

  function IsEmail(email) {
    var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
    return emailReg.test(email);
  }
  $scope.SignUp = function(user) {
    if(user.email == null || user.email === '') {
      $scope.error = 'Email is required';
      $('#signUpEmail .alert_error').removeClass('hide');
      return;
    } else {
      $('#signUpEmail .alert_error').addClass('hide');
    }
    
    if(new IsEmail(user.email) === false || /^[a-zA-Z0-9-.-_--]{3,}\@[a-zA-Z0-9--]{2,16}\.[a-zA-Z0-9]{2,8}$/.test(user.email)) {
      $('#signUpEmail .alert_error').addClass('hide');
    } else {
      $scope.error = 'Email is invalid';
      $('#signUpEmail .alert_error').removeClass('hide');
        return;
    }

    if(user.password == null || user.password === '') {
      $scope.error = 'Password is required';
      $('#signUpEmail .alert_error').removeClass('hide');
      return;
    } else {
      $('#signUpEmail .alert_error').addClass('hide');
    }

    var url = NEW_SERVICE_URL + '/user/registerUser?username=' + user.email + '&password=' + user.password ;
    $http.get(url).success(function(data) {
      if(data.status == 'true') {
        //active student
        var url = NEW_SERVICE_URL + '/user/loginUser?username=' + user.email + '&password=' + user.password;
        $http.get(url).success(function(data) {
          if(data.status = 'true') {
            var dataUser = data.request_data_result[0];
            setStorage('userName', dataUser['username'], 30);
            setStorage('userId', dataUser['userid'], 30);
            setStorage('userType', dataUser['userType'], 10);
            $('#header .login').addClass('hide');
            $('#header .log_out').removeClass('hide');

            var nameHome = '';
            if (dataUser['firstname'] == null || dataUser['lastname'] == null) {
              nameHome = capitaliseFirstLetter(dataUser['username'].substring(0, dataUser['username'].indexOf('@')));
            } else {
              nameHome = capitaliseFirstLetter(dataUser['firstname']) + ' ' + capitaliseFirstLetter(dataUser['lastname']);
            }
            setStorage('nameHome', nameHome, 30);
            $('#header .log_out .current').text(nameHome);
            window.location.href = '/';
          } else {
            $log.info('Can not active student');
          }
        });
      } else {
        $scope.error = data.request_data_result[0];
        $('#signUpEmail .alert_error').removeClass('hide');
      }
    });
  };

  $scope.Cancel = function() {
    $scope.user = {};
    $('#signUpEmail .alert_error').addClass('hide');
  };
}]);

brotControllers.controller('RegisterSuccessController', ['$scope', function($scope) {
  $scope.title = 'Register successfully';
}]);

//brotControllers.controller('HomeController', ['$scope', '$http', '$location', '$rootScope', '$timeout', '$log', 'HomeService', 'NotificationService', 'StudentService', 'VideoService', 'myCache', function($scope, $http, $location, $rootScope, $timeout, $log, HomeService, NotificationService, StudentService, VideoService, myCache) {
//  var subjects = [];
//  // check login page
//  brot.signin.statusStorageHtml();
//  $scope.login = 0;
//
//  var userId = localStorage.getItem('userId');
//
//  $scope.title = "Home";
//
//  var questionIndex = ['How do I ask a friend out without making a fool of myself?', 'How do I maintain a balance between relationship and studies?'];
//
//  init();  
//
//  function init() {
//    if(userId !== null) {
//      $scope.login = 1;
//    }
//
//    if(myCache.get("subjects") !== undefined) {
//      $log.info("My cache already exists");
//      $scope.subjects = myCache.get("subjects");
//    } else {
//      HomeService.getSubjectsWithTag().then(function(data) {
//        if(data.data.status) {
//          $log.info("Get service subject with category");
//          $scope.subjects = data.data.request_data_result;
//          myCache.put("subjects", data.data.request_data_result);
//        }
//      });
//    }
//
//    $scope.questionIndex = questionIndex[Math.floor(Math.random() * 2)];
//
//    $('#search-text').focus(function(){
//      $(this).attr('placeholder', '');
//    });
//    $('#search-text').focusout(function(){
//      $(this).attr('placeholder', $scope.questionIndex);
//    });
//  }
//
//  $scope.signupcomplete = function() {
//    if(userId === null) {
//      window.location.href = '#/signupcomplete';  
//    }
//  };
//
//  $scope.signupStudent = function() {
//    if(userId === null) {
//      window.location.href = '#/signup';
//    }
//  };
//
//  $scope.choose_subject = function(id, name, topicId) {
//    $('.select-list').addClass('hide');
//    $('.select-value').attr('data_id', id);
//    $('.select-value').text(name);
//  };
//
//  $scope.redirectForum = function() {
//    // get question of student
//    $scope.selectCategory = $('select option:selected').val();
//    var questions = $('.search-inner .search_question').val();
//    if (questions == null || (questions!=null && questions.trim() == '')) {
//      $rootScope.myVarQ = !$scope.myVarQ;
//      $timeout(function () {
//        $rootScope.myVarQ = false;
//      }, 2500);
//      return;
//    }
//
//    if(!$scope.selectCategory) {
//      $rootScope.myVarC = !$scope.myVarC;
//      $timeout(function () {
//        $rootScope.myVarC = false;
//      }, 2500);
//      return;
//    }
//
//    if(userId == null) {
//      $rootScope.myVarU = !$scope.myVarU;
//      $timeout(function () {
//        $rootScope.myVarU = false;
//      }, 2500);
//      return;
//    }
//    
//    HomeService.postQuestion(userId, $scope.selectCategory, questions).then(function(data) {
//      pid = data.data.request_data_result;
//      window.location.href = '/#/question_detail/?subject=' + $scope.selectCategory + '&question_id=' + pid;
//    });
//  };
//
//
//  $scope.uploadEssay = function() {
//    var userId = localStorage.getItem('userId');
//    if(userId == null) {
//      // brot.signin.signin();
//      $('#popSignIn').modal('show');
//    } else {
//      $location.path('/upload_essay/1');
//    }
//  };
//
//  $scope.gotoForum = function(subjectId) {
//    window.location.href = '/#/forum?subjectId=' + subjectId;
//  };
//
//  $scope.loginFacebook = function() {
//    loginFBService(function(data) {
//      $scope.userName = data.email;
//      $scope.firstName = data.first_name;
//      $scope.lastName = data.last_name;
//      $scope.facebookId = data.id;
//      $scope.image = data.picture.data.url;
//      StudentService.loginFacebook($scope.userName, 'S', $scope.firstName, $scope.lastName, $scope.image, $scope.facebookId).then(function(data) {
//        var dataUser = data.data.request_data_result[0];
//        setStorage('userName', dataUser['userName'], 30);
//        setStorage('userId', dataUser['userid'], 30);
//        setStorage('userType', dataUser['userType'], 10);
//        var nameHome = $scope.lastName + ' ' + $scope.firstName;
//        setStorage('nameHome', nameHome, 30);
//        $('#header .log_out .current').text(nameHome);
//        location.reload();
//      });
//    });
//  };
//
//  function onLoadCallback() {
//    gapi.client.setApiKey('AIzaSyCRzAc2Jd3r3mhrhBgiWShvEIZNXRWCoRk'); //set your API KEY
//    gapi.client.load('plus', 'v1',function(){});//Load Google + API
//  }
//
//  function loginCallback(result) {
//    if(result['status']['signed_in']) {
//      gapi.client.load('plus','v1', function(){
//        var request = gapi.client.plus.people.get({
//          'userId': 'me'
//        });
//        request.execute(function(resp) {
//          $scope.userName = resp.emails[0].value;
//          $scope.firstName = resp.name.familyName;
//          $scope.lastName = resp.name.givenName;
//          $scope.googleid = resp.id;
//          $scope.image = resp.image.url;
//          var nameHome = resp.displayName;
//          StudentService.loginGoogle($scope.userName, 'S', $scope.firstName, $scope.lastName, $scope.image, $scope.googleid).then(function(data) {
//            var dataUser = data.data.request_data_result[0];
//              setStorage('userName', dataUser['userName'], 30);
//              setStorage('userId', dataUser['userid'], 30);
//              setStorage('userType', dataUser['userType'], 10);
//              setStorage('nameHome', nameHome, 30);
//              $('#header .log_out .current').text(nameHome);
//              location.reload();
//          });
//        });
//      });
//    }  
//  }
//
//  $scope.loginGoogle = function() {
//    var myParams = {
//      'clientid' : '667014101821-v1f9a583qgmt280gtci8n4a87k7uja2m.apps.googleusercontent.com', //You need to set client id
//      'cookiepolicy' : 'single_host_origin',
//      'callback' : loginCallback, //callback function
//      'approvalprompt':'force',
//      'scope' : 'https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/plus.profile.emails.read'
//    };
//    gapi.auth.signIn(myParams);
//  };
//
//}]);

brotControllers.controller('MentorProfileController', ['$scope', '$routeParams', '$http', function($scope, $routeParams, $http) {
  $scope.title = "Mentor profile";
  var mentorId = $routeParams.mentorId;
  $http({
    url : 'json/student_profile.json',
    method : 'GET'
  }).success(function(json) {
    if(json.status == 'SUCCESS') {
      if(json.response[0]) {
        json.response[0].date_of_birth = moment(json.response[0].date_of_birth, 'YYYY-MM-DD', true).format('MMMM DD, YYYY');
        $scope.user = json.response[0];

        var math = json.response[0].point.Maths;
        var physic = json.response[0].point.Physics;
        var biology = json.response[0].point.Biology;
        var chemistry = json.response[0].point.Chemistry;
        drawChart(math, physic, biology, chemistry);
      }
    }
  });

  //get list video by mentor
  $http({
    url : 'json/list_video_by_user.json',
    method : 'GET'
  }).success(function(json) {
    if(json.status == 'SUCCESS') {
      if(json.response) {
        $scope.listVideoMentor = json;
        //add amout of star for rateing and unrating;
        $.each(json.response, function(i, item) {
          //sumary of stars is: 5;
          //sumary of yellow stars is rate;
          //=> sumary of gray stars is unrate = 5-rate;
          var rate = item.video_rating;
          var unrate = 5 - rate;
          var arr_rate = [];
          for(var j = 0; j < rate; j++) {
            arr_rate.push('assets/img/yellow _star.png');
          }
          for(var k = 0; k < unrate; k++) {
            arr_rate.push('assets/img/grey_star.png');
          }
          $scope.listVideoMentor.response[i].arr_rate = arr_rate;
        });
      }
    }
  });

  // get list mentor review
  $http({
    url : 'json/list_mentor_review_student.json',
    method : 'GET'
  }).success(function(json) {
    if(json.status == 'SUCCESS') {
      if(json.response) {
        $scope.listMentorReview = json;

        //add amout of star for rateing and unrating;
        $.each(json.response, function(i, item) {
          //sumary of stars is: 5;
          //sumary of yellow stars is rate;
          //=> sumary of gray stars is unrate = 5-rate;
          var rate = item.video_rating;
          var unrate = 5 - rate;
          var arr_rate = [];
          for(var j = 0; j < rate; j++) {
            arr_rate.push('assets/img/yellow _star.png');
          }
          for(var k = 0; k < unrate; k++) {
            arr_rate.push('assets/img/grey_star.png');
          }
          $scope.listMentorReview.response[i].arr_rate = arr_rate;

          // format date
          item.review_date = moment(item.review_date, 'YYYY-MM-DD h:mm:ss', true).format('MM/DD/YY');
        });
      }
    }
  });
}]);

brotControllers.controller('StudentProfileController', ['$scope', '$rootScope', '$http', '$routeParams', '$location', 'StudentService', 'myCache',
    function($scope, $rootScope, $http, $routeParams, $location, StudentService, myCache) {

  $scope.title = "Student Profiles";
  $scope.page = $routeParams.page;
  $rootScope.page = 1;
  var userid = $routeParams.userid;
  var totalPage;
  var numberPage;

  var email = localStorage.getItem('userName');
  var studentId = localStorage.getItem('userId');
  var userType = localStorage.getItem('userType');
  $scope.userType = userType;

  if(userType == 'S') {
    $scope.title = 'Student Profiles';
    $scope.main = 'Students';
    $scope.sub = 'Students Name Profile';
  } else {
    $scope.title = 'Mentor Profiles';
    $scope.main = 'Mentors';
    $scope.sub = 'Mentors Name Profile';
  }
  $scope.username = email;

  init();

  function init() {
    StudentService.getListForumPost(userid, $rootScope.page).then(function(data) {
      $scope.countPost = data.data.count;
      if(data.data.status == 'true') {
        if(data.data.request_data_result.length === 0) {
          $scope.errorDataPost = DATA_ERROR.noDataFound;
        } else {
          $scope.listPostQuestion = data.data.request_data_result;
          numberPage = Math.ceil(data.data.count / 3);
          showPage(numberPage, $rootScope.page, function(response) {
            $scope.listPage = response;
          });
        }
      }
    });

    StudentService.getStudentProfile(userid).then(function(data) {
      var objUser = data.data.request_data_result;
      
      $rootScope.showClass = 0;

      var accomplishments = objUser.accomplishments;
      if(accomplishments != null) {
        accomplishments = accomplishments.substring(1, accomplishments.length - 1);
        var arrAccomplishments = accomplishments.split(',');
        objUser.accomplishments = arrAccomplishments;
      }

      var subjects = objUser.subjects;
      if(subjects != null) {
        subjects = subjects.substring(1, subjects.length - 1);
        var arrSubjects = subjects.split(',');
        objUser.subjects = arrSubjects;
      }

      var helpsubjects = objUser.helpsubjects;
      if(helpsubjects != null) {
        helpsubjects = helpsubjects.substring(1, helpsubjects.length - 1);
        var arrHelpsubjects = helpsubjects.split(',');
        objUser.helpsubjects = arrHelpsubjects;
      }

      if(objUser[0][0].idFacebook != null || objUser[0][0].idGoogle != null) {
        objUser[0][0].imageUrl = StudentService.getAvatar(objUser[0][0].userid);
      } else if(objUser[0][0].imageUrl === undefined || objUser[0][0].imageUrl == null) {
        objUser[0][0].imageUrl = "https://pbs.twimg.com/profile_images/345217437/man.jpg";
      } else {
        objUser[0][0].imageUrl = StudentService.getAvatar(objUser[0][0].userid);
      }
      $scope.user = objUser;
    });
    
    //get list current mentor of student
    StudentService.getListCurrentMentor(studentId).then(function(data) {
      if(data.data.status == 'true') {
        if(data.data.request_data_result) {
          $scope.listCurrentMentor = data.data.request_data_result;
          if($scope.listCurrentMentor.length === 0) {
            $scope.errorDataMentor = DATA_ERROR.noDataFound;
          }
        }
      }
    });

    StudentService.getMentorsReviewed(studentId, 4).then(function(data) {
      if(data.data.status == 'true') {
        if(data.data.request_data_result) {
          $scope.listMentorReview = data.data.request_data_result;
          if($scope.listMentorReview.length === 0) {
            $scope.errorData = DATA_ERROR.noDataFound;
          }
          $.each(data.data.request_data_result, function(i, item) {
            //sumary of stars is: 5;
            //sumary of yellow stars is rate;
            //=> sumary of gray stars is unrate = 5-rate;
            var rate = item.Rating;
            var unrate = 5 - rate;
            var arr_rate = [];
            for(var j = 0; j < rate; j++) {
              arr_rate.push('assets/img/yellow _star.png');
            }
            for(var k = 0; k < unrate; k++) {
              arr_rate.push('assets/img/grey_star.png');
            }
            // $scope.listMentorReview.response[i].arr_rate = arr_rate;
            $scope.listMentorReview[i].arr_rate = arr_rate;
            // format date
            item.review_date = moment(item.review_date, 'YYYY-MM-DD h:mm:ss', true).format('MM/DD/YY');
          });
        }
      }
    });

    drawChart(675, 675, 675, 675);
  }

  $scope.goToEditStudent = function(userName) {
    $location.path('/editStudent/basic');
  };
  

  $scope.addClassActive = function(page) {
    if(page == $rootScope.page) {
      return 'span_active';
    }
    return '';
  };

  $scope.showWithPage = function(page) {
    $rootScope.page = page;
    init();
  };

  $scope.questionDetail = function(subjectId, topicId, pid) {
    window.location.href = '/#/question_detail/?subject=' + subjectId + '&question_id=' + pid;
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

  $scope.uploadEssay = function() {
    var userId = localStorage.getItem('userId');
    if(userId == null) {
      $('#popSignIn').modal('show');
    } else {
      $location.path('/upload_essay');
    }
  };

  $scope.viewAllEssay = function() {
    var userId = localStorage.getItem('userId');
    if(userId == null) {
      $('#popSignIn').modal('show');
    } else {
      $location.path('/all_essay');
    }
  };

  $scope.loadMore = function(event, limit) {
    var ele = event.currentTarget;
    StudentService.getMentorsReviewed(studentId, limit).then(function(data) {
      if(data.data.status == 'true') {
        if(data.data.request_data_result) {
          $scope.listMentorReview = data.data.request_data_result;
          $.each(data.data.request_data_result, function(i, item) {
            //sumary of stars is: 5;
            //sumary of yellow stars is rate;
            //=> sumary of gray stars is unrate = 5-rate;
            var rate = item.Rating;
            var unrate = 5 - rate;
            var arr_rate = [];
            for(var j = 0; j < rate; j++) {
              arr_rate.push('assets/img/yellow _star.png');
            }
            for(var k = 0; k < unrate; k++) {
              arr_rate.push('assets/img/grey_star.png');
            }
            // $scope.listMentorReview.response[i].arr_rate = arr_rate;
            $scope.listMentorReview[i].arr_rate = arr_rate;
            // format date
            item.review_date = moment(item.review_date, 'YYYY-MM-DD h:mm:ss', true).format('MM/DD/YY');
          });
        }
      }
    });
    $(ele).parent().find('.divLoadMore').addClass('hidden');
  };
}]);

//brotControllers.controller('ForumCtrl', ['$sce', '$route','$scope', '$rootScope', '$timeout', '$http', '$routeParams', '$location', '$log', 'SubjectServices', 'QuestionsService', 'CategoriesService', 'MentorService', 'AnswerService', 'HomeService', 'CommentService', 'StudentService', 'myCache', function ($sce, $route, $scope, $rootScope, $timeout, $http, $routeParams, $location, $log, SubjectServices, QuestionsService, CategoriesService, MentorService, AnswerService, HomeService, CommentService, StudentService, myCache) {
//  var qid = $routeParams.question_id;
//  var lastRoute = $route.current;
//  $scope.clickS = 1;
//  $scope.clickC = 1;
//  $scope.countS = 0;
//  
//
//  var userId = localStorage.getItem('userId');
//  $scope.userId = userId;
//  var subject_id = $routeParams.subjectId;
//  var categorieId = $routeParams.categorieId;
//  $scope.subjectId = subject_id;
//  $scope.categorieId = categorieId;
//  var listLikeQuestion = [];
//  var idRemove;
//  var eventRemove;
//  var questionForAnswer;
//
//  init();
//
//  function init() {
//    if(myCache.get("subjects") !== undefined) {
//      $log.info("My cache already exists");
//      $scope.subjects = myCache.get("subjects");
//      for(var i = 0; i < $scope.subjects.length; i++) {
//        if($scope.subjectId == $scope.subjects[i][0].subject_id) {
//          $scope.image = $scope.subjects[i][0].image;
//          $scope.selectSubject = $scope.subjects[i][0].subject_name;
//          $scope.textSearch = $scope.selectSubject;
//          $scope.description = $scope.subjects[i][0].description;
//          break;
//        }
//      } 
//    } else {
//      HomeService.getSubjectsWithTag().then(function(data) {
//        if(data.data.status) {
//          $scope.subjects = data.data.request_data_result;
//          myCache.put("subjects", data.data.request_data_result);
//          for(var i = 0; i < $scope.subjects.length; i++) {
//            if($scope.subjectId == $scope.subjects[i][0].subject_id) {
//              $scope.image = $scope.subjects[i][0].image;
//              $scope.selectSubject = $scope.subjects[i][0].subject_name;
//              $scope.textSearch = $scope.selectSubject;
//              $scope.description = $scope.subjects[i][0].description;
//              break;
//            }
//          }
//        }
//      });
//    }
//    
//    $('.modal-backdrop').remove();
//
//    var questionIndex = ['How do I ask a friend out without making a fool of myself?', 'How do I maintain a balance between relationship and studies?'];
//    $scope.questionIndex = questionIndex[Math.floor(Math.random() * 2)];
//
//    $('#search-text').focus(function(){
//      $(this).attr('placeholder', '');
//    });
//    $('#search-text').focusout(function(){
//      $(this).attr('placeholder', $scope.questionIndex);
//    });
//
//    CategoriesService.getCategories($scope.subjectId).then(function(data) {
//      var listofCategeris = data.data.request_data_result;
//      if(listofCategeris == null) {
//        listofCategeris = [];
//      }
//      $scope.categories = listofCategeris;
//      if($scope.categorieId === undefined) {
//        QuestionsService.getQuestionSubjectWithPN(userId, $scope.subjectId, 1, 5).then(function(data) {
//          var questions = data.data.request_data_result;
//          $scope.countS = data.data.count;
//          if(questions == null) {
//            questions = [];
//          }
//          if($scope.countS == null) {
//            $scope.errorData = DATA_ERROR.noDataFound;
//          }
//          for(var i = 0; i < questions.length; i++) {
//            var qa = questions[i];
//            if(qa[0].idFacebook == null && qa[0].idGoogle == null) {
//              qa[0].imageUrl = StudentService.getAvatar(qa[0].authorID);  
//            } else {
//              qa[0].imageUrl = StudentService.getAvatar(qa[0].authorID);  
//            }
//            
//            qa[0].timeStamp = convertUnixTimeToTime(qa[0].timeStamp);
//            if(qa[1].answer !== undefined) {
//              for(var j = 0; j < qa[1].answer.length; j++) {
//                var answer_text = decodeURIComponent(qa[1].answer[j].content);
//                answer_text = $sce.trustAsHtml(answer_text);
//                qa[1].answer[j].answer_text = answer_text;
//                qa[1].answer[j].timeStamp = convertUnixTimeToTime(qa[1].answer[j].timeStamp);
//              }
//            }
//            getAnswer(qa[0].pid, 1, qa);
//          }
//          $scope.questions = questions;
//        });
//      } else {
//        QuestionsService.getQuestionWithPN(userId, $scope.subjectId, $scope.categorieId, 1, 4).then(function(data) {
//          var questions = data.data.request_data_result;
//          $scope.countC = data.data.count;
//          if($scope.countC == '0') {
//            $scope.errorData = DATA_ERROR.noDataFound;
//          }
//          if($scope.countC > 4) {
//            $scope.feature = 0;
//          }
//          if(questions == null) {
//            questions = [];
//          }
//          for (var i = 0; i < questions.length; i++) {
//            var qa = questions[i];
//            if(qa[0].idFacebook == null && qa[0].idGoogle == null) {
//              qa[0].imageUrl = StudentService.getAvatar(qa[0].authorID);  
//            } else {
//              qa[0].imageUrl = StudentService.getAvatar(qa[0].authorID);  
//            }
//            
//            qa[0].timeStamp = convertUnixTimeToTime(qa[0].timeStamp);
//            if(qa[1].answer !== undefined) {
//              for(var j = 0; j < qa[1].answer.length; j++) {
//                var answer_text = decodeURIComponent(qa[1].answer[j].content);  
//                answer_text = $sce.trustAsHtml(answer_text);
//                qa[1].answer[j].answer_text = answer_text;
//                qa[1].answer[j].timeStamp = convertUnixTimeToTime(qa[1].answer[j].timeStamp);
//              }
//            }
//            getAnswer(qa[0].pid, 1, qa);
//          }
//          $scope.questions = questions;
//        });
//      }
//    });
//
//    MentorService.getTopMentors($scope.subjectId).then(function(data) {
//      var listTopMentors = data.data.request_data_result;
//      if(listTopMentors == null) {
//        listTopMentors = [];
//      }
//      if(data.data.request_data_result.length === 0) {
//        $scope.errorData = DATA_ERROR.noDataFound;
//      }
//      $scope.mentors = listTopMentors;
//    });
//
//    QuestionsService.getPostLikeByUser(userId).then(function(data) {
//      var likePost = data.data.request_data_result;
//      for(var i = 0; i < likePost.length; i++) {
//        $('.wq').find('.q' + likePost[i].pid).html('Liked');
//      }
//    });
//
//    document.documentElement.style.overflow = 'auto';  // firefox, chrome
//    document.body.scroll = "yes"; // ie only
//
//    var editor = CKEDITOR.replace( 'answer');
//    editor.config.allowedContent = true;
//    editor.config.width = 615;
//    editor.config.height = 260;
//    editor.addCommand("mySimpleCommand", {
//      exec: function(edt) {
//        $('#uploadImage').modal('show');
//      }
//    });
//    editor.ui.addButton('SuperButton', {
//      label: "Upload Image",
//      command: 'mySimpleCommand',
//      toolbar: 'insert',
//      icon: '/assets/img/ico-hit.png'
//    });
//
//    $('#select-file').click(function() {
//      $('#file').click();
//    });
//
//    $('#file').change(function() {
//      $('.waitingUp').removeClass('hide');
//      CommentService.uploadImage(function(data) {
//        if(data.status == 'true') {
//          var local = data.request_data_result;
//          $('#uploadImage').modal('hide');
//          $('.waitingUp').addClass('hide');
//          var content = CKEDITOR.instances["answer"].getData();
//          content += "<img style='max-width: 570px;' src='"+ local +"'/>";
//          CKEDITOR.instances["answer"].setData(content);
//        } else {
//          $('.waitingUp').addClass('hide');
//          $scope.errorUpload = data.request_data_result;
//          alert($scope.errorUpload);
//          return;
//        }
//      });
//    });
//  }
//
//  function getAnswer (qid, page, qa) {
//    var listAnswer = [];
//    if(qa[1].answers !== undefined) {
//      listAnswer = qa[1].answers;
//    }
//    AnswerService.getAnswersByQuestion(qid, page, 5).then(function(data) {
//      var result = data.data.request_data_result;
//      if(data.data.status) {
//        for(i = 0; i < result.length; i++) {
//          var answer_text = decodeURIComponent(result[i][0].content);
//          answer_text = $sce.trustAsHtml(answer_text);
//          if(result[i][0].idFacebook == null && result[i][0].idGoogle == null) {
//            result[i][0].imageUrl = StudentService.getAvatar(result[i][0].authorID);  
//          } else {
//            result[i][0].imageUrl = StudentService.getAvatar(result[i][0].authorID);
//          }
//
//          result[i][0].answer_text = answer_text;
//          result[i][0].timeStamp = convertUnixTimeToTime(result[i][0].timeStamp);
//          listAnswer.push(result[i]);
//        }
//        qa[1].answers = listAnswer;
//        
//        AnswerService.getAnswersByQuestion(qid, page + 1, 5).then(function(data) {
//          if(data.data.request_data_result.length > 0) {
//            $('.forum-left .wq').find('#lma' + qid).removeClass('hide');
//          } else {
//            $('.forum-left .wq').find('#lma' + qid).addClass('hide');
//          }
//        }); 
//      }
//    });
//  }
//
//  $scope.loadMoreAnswer = function(qid, q) {
//    var page = parseInt($('.forum-left #lma' + qid).attr('page'), 10) + 1;
//    getAnswer(qid, page, q);
//    $('.forum-left #lma' + qid).attr('page', page);
//  };
//
//  $scope.like = function(qid, authorId, topicId) {
//    QuestionsService.likeQuestion(userId, qid).then(function(data) {
//      var ex = parseInt($('#q' + qid).html(), 10);
//      if(data.data.request_data_result[0].likePost == 1) {
//        $('#q' + qid).html('+' + (ex + 1));
//        $('.wq').find('.q' + qid).html('Liked');
//      } else {
//        $('#q' + qid).html('+' + (ex - 1));
//        $('.wq').find('.q' + qid).html('');
//      }
//    });
//  };
//
//  $scope.likeAnswer = function(aid, authorId, qid, topicId) {
//    AnswerService.likeAnswer(userId, aid).then(function(data) {
//      var ex = parseInt($('#a' + aid).html(), 10);
//      if(data.data.request_data_result[0].likeAnswer == 'Y') {
//        $('#a' + aid).html('+' + (ex + 1));
//        $('.a').find('.a' + aid).html('Liked');
//      } else {
//        $('#a' + aid).html('+' + (ex - 1));
//        $('.a').find('.a' + aid).html('');
//      }
//    });
//  };
//
//  $scope.loadMorePost = function() {
//    $('.wq').find('.q84').html('Liked');
//    if($scope.categorieId === undefined) {
//      $scope.clickS++;
//      QuestionsService.getQuestionSubjectWithPN(userId, $scope.subjectId, $scope.clickS, 5).then(function(data) {
//        var questions = data.data.request_data_result;
//        $scope.countS = data.data.count;
//        if(questions == null) {
//          questions = [];
//        }
//        for(var i = 0; i < questions.length; i++) {
//          var qa = questions[i];
//          if(qa[0].idFacebook == null && qa[0].idGoogle == null) {
//            qa[0].imageUrl = StudentService.getAvatar(qa[0].authorID);  
//          } else {
//            qa[0].imageUrl = StudentService.getAvatar(qa[0].authorID);
//          }
//          
//          qa[0].timeStamp = convertUnixTimeToTime(qa[0].timeStamp);
//          if(qa[1].answer[0] !== undefined) {
//            var answer_text = decodeURIComponent(qa[1].answer[0].content);
//            answer_text = $sce.trustAsHtml(answer_text);
//            qa[1].answer[0].answer_text = answer_text;
//            qa[1].answer[0].timeStamp = convertUnixTimeToTime(qa[1].answer[0].timeStamp);
//          }
//          getAnswer(qa[0].pid, 1, qa);
//          $scope.questions.push(qa);
//        }
//
//        QuestionsService.getQuestionSubjectWithPN(userId, $scope.subjectId, $scope.clickS + 1, 5).then(function(data) {
//          var feature = data.data.request_data_result;
//          if(feature.length === 0) {
//            $scope.feature = 1;
//          }
//        });
//      });
//    } else {
//      $scope.clickC++;
//      QuestionsService.getQuestionWithPN(userId, $scope.subjectId, $scope.categorieId, $scope.clickC, 4).then(function(data) {
//        var questions = data.data.request_data_result;
//        $scope.countC = data.data.count;
//        if(questions == null) {
//          questions = [];
//        }
//        for (var i = 0; i < questions.length; i++) {
//          var qa = questions[i];
//          if(qa[0].idFacebook == null && qa[0].idGoogle == null) {
//            qa[0].imageUrl = StudentService.getAvatar(qa[0].authorID);  
//          } else {
//            qa[0].imageUrl = StudentService.getAvatar(qa[0].authorID);
//          }
//
//          qa[0].timeStamp = convertUnixTimeToTime(qa[0].timeStamp);
//          if(qa[1].answer !== undefined) {
//            if(qa[1].answer[0] !== undefined) {
//              var answer_text = decodeURIComponent(qa[1].answer[0].content);
//              answer_text = $sce.trustAsHtml(answer_text);
//              qa[1].answer[0].answer_text = answer_text;
//              qa[1].answer[0].timeStamp = convertUnixTimeToTime(qa[1].answer[0].timeStamp);
//            }
//          }
//          getAnswer(qa[0].pid, 1, qa);
//          $scope.questions.push(qa);
//        }
//        QuestionsService.getQuestionWithPN(userId, $scope.subjectId, $scope.categorieId, $scope.clickC + 1, 4).then(function(data) {
//          var feature = data.data.request_data_result;
//          if(feature.length === 0) {
//            $scope.feature = 1;
//          }
//        });
//      });
//    }  
//  };
//
//  $scope.changeCategori = function(event) {
//    $scope.countC = 0;
//    $scope.feature = 1;
//    $scope.click = 1;
//    $scope.clickS = 1;
//    $scope.clickC = 1;
//    var ele = event.currentTarget;
//    var id = $(ele).attr('data-id');
//    $location.path('/forum').search({'subjectId': $scope.subjectId, 'categorieId': id});
//    var eleParent = angular.element(ele).parent().parent();
//    angular.element(eleParent).find('.active').removeClass('active');
//    angular.element(ele).addClass('active');
//    $scope.categorieId = id;
//    QuestionsService.getQuestionWithPN(userId, $scope.subjectId, id, 1, 4).then(function(data) {
//      var questions = data.data.request_data_result;
//      $scope.countC = data.data.count;
//      if($scope.countC == '0') {
//        $scope.errorData = DATA_ERROR.noDataFound;
//      }
//      if($scope.countC > 4) {
//        $scope.feature = 0;
//      }
//      if(questions == null) {
//        questions = [];
//      }
//      for(var i = 0; i < questions.length; i++) {
//        var qa = questions[i];
//        if(qa[0].idFacebook == null && qa[0].idGoogle == null) {
//          qa[0].imageUrl = StudentService.getAvatar(qa[0].authorID);
//        } else {
//          qa[0].imageUrl = StudentService.getAvatar(qa[0].authorID);
//        }
//        
//        qa[0].timeStamp = convertUnixTimeToTime(qa[0].timeStamp);
//        if(qa[1].answer !== undefined) {
//          for(var j = 0; j < qa[1].answer.length; j++) {
//            var answer_text = decodeURIComponent(qa[1].answer[j].content);  
//            answer_text = $sce.trustAsHtml(answer_text);
//            qa[1].answer[j].answer_text = answer_text;
//            qa[1].answer[j].timeStamp = convertUnixTimeToTime(qa[1].answer[j].timeStamp);
//          }
//        }
//        getAnswer(qa[0].pid, 1, qa);
//      }
//      $scope.questions = questions;
//    });
//  };
//
//  $scope.redirectForum = function() {
//      // get question of student
//    $scope.selectCategory = $('select option:selected').val();
//    var questions = $('.search-inner .search_question').val();
//    if (questions == null || (questions!=null && questions.trim() == '')) {
//      $rootScope.myVarQ = !$scope.myVarQ;
//      $timeout(function () {
//        $rootScope.myVarQ = false;
//      }, 2500);
//      return;
//    }
//
//    if(!$scope.selectCategory) {
//      $rootScope.myVarC = !$scope.myVarC;
//      $timeout(function () {
//        $rootScope.myVarC = false;
//      }, 2500);
//      return;
//    }
//
//    if(userId == null) {
//      $rootScope.myVarU = !$scope.myVarU;
//      $timeout(function () {
//        $rootScope.myVarU = false;
//      }, 2500);
//      return;
//    }
//    
//    HomeService.postQuestion(userId, $scope.selectCategory, questions).then(function(data) {
//      pid = data.data.request_data_result;
//      window.location.href = '/#/question_detail/?subject=' + $scope.selectCategory + '&question_id=' + pid;
//    });
//  };
//
//  $scope.editAnswer = function(aid, answer, q) {
//    questionForAnswer = q;
//    editAnswerId = aid;
//    $scope.answerOld = answer;
//    $('#editAnswer').modal('show');
//    CKEDITOR.instances['answer'].setData(decodeURIComponent($scope.answerOld));
//  };
//
//  $scope.updateAnswer = function() {
//    var content = CKEDITOR.instances['answer'].getData();
//    AnswerService.editAnswer(editAnswerId, content).then(function(data) {
//      if(data.data.request_data_result) {
//        // init();
//        var currentPageAnswer = parseInt($('.forum-left #lma' + questionForAnswer[0].pid).attr('page'), 10);
//        questionForAnswer[1].answers = [];
//        for(var i = 1; i <= currentPageAnswer; i++) {
//          getAnswer(questionForAnswer[0].pid, i, questionForAnswer);
//        }
//      }
//    });
//  };
//
//  $scope.deletePost = function(event, pid) {
//    $scope.typeItem = 'Post';
//    $('#deleteItem').modal('show');
//    idRemove = pid;
//    eventRemove = event;
//  };
//
//  $scope.deleteAnswer = function(aid, q) {
//    questionForAnswer = q;
//    $scope.typeItem = 'Answer';
//    $('#deleteItem').modal('show');
//    idRemove = aid;
//    eventRemove = event;
//  };
//
//  $scope.deleteItem = function() {
//    var eleParent = $(eventRemove.currentTarget).parent().parent();
//    if($scope.typeItem == 'Post') {
//      QuestionsService.removePost(idRemove).then(function(data) {
//        if(data.data.request_data_result) {
//          $(eleParent).remove();
//        }
//      });
//    } else {
//      QuestionsService.removeAnswer(idRemove).then(function(data) {
//        if(data.data.request_data_result) {
//          var currentPageAnswer = parseInt($('.forum-left #lma' + questionForAnswer[0].pid).attr('page'), 10);
//          questionForAnswer[1].answers = [];
//          for(var i = 1; i <= currentPageAnswer; i++) {
//            getAnswer(questionForAnswer[0].pid, i, questionForAnswer);
//          }
//        }
//      });
//    }
//  };
//
//  $('.btn-group').on('show.bs.dropdown', function(e){
//    var $dropdown = $(this).find('.dropdown-menu');
//    var orig_margin_top = parseInt($dropdown.css('margin-top'), 10);
//    $dropdown.css({'margin-top': (orig_margin_top + 10) + 'px', opacity: 0}).animate({'margin-top': orig_margin_top + 'px', opacity: 1}, 300, function(){
//      $(this).css({'margin-top':''});
//    });
//  });
//   // Add slidedown & fadeout animation to dropdown
//  $('.btn-group').on('hide.bs.dropdown', function(e){
//    var $dropdown = $(this).find('.dropdown-menu');
//    var orig_margin_top = parseInt($dropdown.css('margin-top'), 10);
//    $dropdown.css({'margin-top': orig_margin_top + 'px', opacity: 1, display: 'block'}).animate({'margin-top': (orig_margin_top + 10) + 'px', opacity: 0}, 300, function(){
//      $(this).css({'margin-top':'', display:''});
//    });
//  });
//
//  $('#forum-search .search-inner').find('.search-text').keypress(function(event) {
//    var searchText = $('#forum-search .search-inner').find('.search-text').val();
//
//    if(event.keyCode == 13) {
//      $scope.search = 1;
//      window.location.href = '#/searchForum/' + searchText;
//    }
//  });
//}]);

brotControllers.controller('SearchForumCtrl', ['$scope', '$routeParams', '$http', '$location', 'QuestionsService', 'MentorService', 'StudentService', function($scope, $routeParams, $http, $location, QuestionsService, MentorService, StudentService) {

  $scope.searchText = $routeParams.keyWord;
  var subject_id = $routeParams.subjectId;
  var userId = localStorage.getItem('userId');
  $scope.click = 1;
  $('#loading-dialog').css({display: 'block', width: '100%', height: '100%'});

  QuestionsService.searchPostWithTag(subject_id, $scope.searchText, $scope.click, 5).then(function(data) {
    var questions = data.data.request_data_result;
    $scope.countS = data.data.count;

    if($scope.countS > 5) {
      $scope.featureS = 0;
    } else {
      $scope.featureS = 1;
    }

    if(questions == null) {
      questions = [];
    }

    for(var i = 0; i < questions.length; i++) {
      var qa = questions[i];
      var date = moment(qa[0].updateTime, 'YYYY-MM-DD h:mm:ss').startOf('hour').fromNow();
      qa[0].timeStamp = convertUnixTimeToTime(qa[0].timeStamp);
      qa.question_creation_date = date;
      if(qa[0].idFacebook == null && qa[0].idGoogle == null) {
        qa[0].imageUrl = StudentService.getAvatar(qa[0].authorID);
      } else {
        qa[0].imageUrl = StudentService.getAvatar(qa[0].authorID);
      }
    }
    $scope.questions = questions;

    $('#loading-dialog').css({display: 'none', width: '100%', height: '100%'});
  });

  MentorService.getTopMentors(subject_id).then(function(data) {
    var listTopMentors = data.data.request_data_result;
    if(listTopMentors == null) {
      listTopMentors = [];
    }

    $scope.mentors = listTopMentors;
  });

  $scope.loadMorePost = function() {
    $scope.click++;
    QuestionsService.searchPostWithTag(subject_id, $scope.searchText, $scope.click, 5).then(function(data) {
      var questions = data.data.request_data_result;

      if(questions == null) {
        questions = [];
      }
      for(var i = 0; i < questions.length; i++) {
        var qa = questions[i];
        var date = moment(qa[0].updateTime, 'YYYY-MM-DD h:mm:ss').startOf('hour').fromNow();
        qa.question_creation_date = date;
        qa[0].timeStamp = convertUnixTimeToTime(qa[0].timeStamp);
        if(qa[0].idFacebook == null && qa[0].idGoogle == null) {
          qa[0].imageUrl = StudentService.getAvatar(qa[0].authorID);
        } else {
          qa[0].imageUrl = StudentService.getAvatar(qa[0].authorID);
        }
        $scope.questions.push(qa);
      }
    });

    QuestionsService.searchPostWithTag(subject_id, $scope.searchText, $scope.click + 1, 5).then(function(data) {
      var questions = data.data.request_data_result;
      if(questions.length === 0) {
        $scope.featureS = 1;
      }
    });
  };

  $scope.like = function(qid, authorId, topicId) {
    QuestionsService.likeQuestion(userId, qid).then(function(data) {
      var ex = parseInt($('#q' + qid).html(), 10);
      if(data.data.request_data_result[0].likePost == 1) {
        $('#q' + qid).html('+' + (ex + 1));
      } else {
        $('#q' + qid).html('+' + (ex - 1));
      }
    });
  };

  $('#forum-search .search-inner').find('.search-text').keypress(function(event) {
    var searchText = $('#forum-search .search-inner').find('.search-text').val();

    if(event.keyCode == 13) {
      $scope.search = 1;
      window.location.href = '#/searchForum/' + searchText;
    }
  });
}]);

brotControllers.controller('EditStudentController', ['$scope', '$rootScope', '$routeParams', '$http', '$location', '$log', 'StudentService', 'HomeService', '$window', 'myCache',
    function($scope, $rootScope, $routeParams, $http, $location, $log, StudentService, HomeService, $window, myCache) {

  var infor = $routeParams.type;
  var userId = localStorage.getItem('userId');
  var email = localStorage.getItem('userName');
  var userType = localStorage.getItem('userType');

  var listMajorId = [];
  var listActivityId = [];
  var listTopicId = [];
  var listSubjectId = [];

  if(userType == 'S') {
    $scope.title = 'Edit Student Profile';
  } else {
    $scope.title = 'Edit Mentor Profile';
  }

  $('#editStudent .nav-tabs').find('.active').removeClass('active');
  $('#editStudent .nav-tabs').find('.' + infor).addClass('active');

  $('#editStudent .tab-content').find('.active').removeClass('active');
  $('#' + infor).addClass('active');

  $('#editStudent .menu .basic a').click(function(event) {
    window.location.href = '#/editStudent/basic';
    $('#editStudent .menu .active').removeClass('active');
    $('#editStudent .menu .basic').addClass('active');

    $('#editStudent .student').find('.content').removeClass('active');
    $('#editStudent #basic').addClass('active');
  });

  $('#editStudent .menu .education a').click(function(event) {
    window.location.href = '#/editStudent/education';
    $('#editStudent .menu .active').removeClass('active');
    $('#editStudent .menu .education').addClass('active');

    $('#editStudent .student').find('.content').removeClass('active');
    $('#editStudent #education').addClass('active');
  });

  $('#editStudent .menu .setting a').click(function(event) {
    window.location.href = '#/editStudent/setting';
    $('#editStudent .menu .active').removeClass('active');
    $('#editStudent .menu .setting').addClass('active');

    $('#editStudent .student').find('.content').removeClass('active');
    $('#editStudent #setting').addClass('active');
  });

  if(myCache.get("subjects") !== undefined) {
    $log.info("My cache already exists");
    $scope.subjects = myCache.get("subjects");
  } else {
    HomeService.getSubjectsWithTag().then(function(data) {
      if(data.data.status) {
        $scope.subjects = data.data.request_data_result;
        myCache.put("subjects", data.data.request_data_result);
      }
    });
  }

  StudentService.getListMajors().then(function(data) {
    $scope.majors = data.data.request_data_result;
  });

  StudentService.getListActivities().then(function(data) {
    $scope.activities = data.data.request_data_result;
  });

  StudentService.getListCategory().then(function(data) {
    $scope.helps = data.data.request_data_result;
  });

  StudentService.getStudentProfile(userId).then(function(data) {
    var objUser = data.data.request_data_result[0][0];
    var objInfo = data.data.request_data_result[0][1];
    var accomplishments = objUser.accomplishments;
    if(accomplishments != null) {
      // accomplishments = accomplishments.substring(1, accomplishments.length - 1);
      objUser.accomplishments = accomplishments;
    }
    var subjects = objUser.subjects;
    if(subjects != null) {
      subjects = subjects.substring(1, subjects.length - 1);
      var arrSubjects = subjects.split(',');
      objUser.subjects = arrSubjects;
    } else {
      objUser.subjects = [];
    }

    var helpsubjects = objUser.helpsubjects;
    if(helpsubjects != null) {
      helpsubjects = helpsubjects.substring(1, helpsubjects.length - 1);
      var arrHelpsubjects = helpsubjects.split(',');
      objUser.helpsubjects = arrHelpsubjects;
    } else {
      objUser.helpsubjects = [];
    }
    $scope.user = objUser;

    if($scope.user !== undefined) {

      $('#editStudent .grade .class_selected').text($scope.user.currentClass);

      for(var i = 0; i < objInfo.majors.length; i++) {
        listMajorId.push(objInfo.majors[i].majorId);
        $('.boudary .check_item').find('#m' + objInfo.majors[i].majorId).removeClass('hide');
      }

      for(var j = 0; j < objInfo.activity.length; j++) {
        listActivityId.push(objInfo.activity[j].activityId);
        $('.activities .check_item').find('#a' + objInfo.activity[j].activityId).removeClass('hide');
      }

      for(var k = 0; k < objInfo.help.length; k++) {
        listSubjectId.push(objInfo.help[k].subjectId);
        $('.help .check_item').find('#h' + objInfo.help[k].subjectId).removeClass('hide');
      }
    }
  });

  $scope.chooseClass = function(evt) {
    var ele  = evt.currentTarget;
    var value = $(ele).text();
    $('#editStudent .grade .class_selected').text(value);
  };

  $scope.selectMajor = function(evt, majorId) {
    var ele = evt.currentTarget;
    var isClass = $(ele).find('.icon_check').hasClass('hide');
    if(isClass) {
      listMajorId.push(majorId);
      $(ele).find('.icon_check').removeClass('hide');
    }
    else {
      listMajorId.splice(listMajorId.indexOf(majorId), 1);
      $(ele).find('.icon_check').addClass('hide');
    }
  };

  $scope.selectHelp = function(evt, subjectId) {
    var ele = evt.currentTarget;
    var isClass = $(ele).find('.icon_check').hasClass('hide');
    if(isClass) {
      listSubjectId.push(subjectId);
      $(ele).find('.icon_check').removeClass('hide');
    }
    else {
      listSubjectId.splice(listSubjectId.indexOf(subjectId), 1);
      $(ele).find('.icon_check').addClass('hide');
    }
  };

  $scope.selectActivity = function(evt, activityId) {
    var ele = evt.currentTarget;
    var isClass = $(ele).find('.icon_check').hasClass('hide');
    if(isClass) {
      listActivityId.push(activityId);
      $(ele).find('.icon_check').removeClass('hide');
    }
    else {
      listActivityId.splice(listActivityId.indexOf(activityId), 1);
      $(ele).find('.icon_check').addClass('hide');
    }
  };

  $scope.saveProfileStudentBasic = function() {
    var bio = $('#basic .bio').val();
    var first_name = $('#basic .first_name').val();
    var last_name = $('#basic .last_name').val();
    if(first_name === ''){
      $('#basic .first_name').next().removeClass('hide');
      $('#basic .first_name').parent().next().removeClass('hide');
        return;
    }else{
      $('#basic .first_name').next().addClass('hide');
      $('#basic .first_name').parent().next().addClass('hide');
    }

    if(last_name === ''){
      $('#basic .last_name').next().removeClass('hide');
      $('#basic .last_name').parent().next().removeClass('hide');
      return;
    }else{
      $('#basic .last_name').next().addClass('hide');
      $('#basic .last_name').parent().next().addClass('hide');
    }
    if(bio === ''){
      $('#basic .bio').next().removeClass('hide');
      return;
    }else{
      $('#basic .bio').next().addClass('hide');
    }

    var objUser = $scope.user;
    StudentService.updateUserProfileBasic(objUser.username, first_name, last_name, bio).then(function(json) {
      removeStorage('nameHome');
      var nameHome = capitaliseFirstLetter(first_name) + ' ' + capitaliseFirstLetter(last_name);

      if(json.data.status == 'true') {

        if (first_name.length <= 50 && last_name.length <= 50) {
          setStorage('nameHome', nameHome, 30);
          $('#header .log_out .current').text(nameHome);
        }
        $('#basic .msg_success').removeClass('hide');
        setTimeout(function(){
          $('#basic .msg_success').addClass('hide');
        }, 3000);
      }
    });
  };

  $scope.saveProfileStudentEducation = function() {
    var school = $('#education .input_school').val();

    var cla = $('#education .class_selected').text();

    var accom = $('#education .accomplishments').val();

    var majors = '';
    var ele = $('#education .boudary').find('.check_item').find('.icon_check');

    $.each(ele, function(index, e) {
      if(!$(e).hasClass('hide')) {
        majors += $(e).parent().next().html() + ',';
      }
    });
    majors = majors.substring(0, majors.length - 1);

    var activities = '';
    var eleActivities = $('#education .activities').find('.check_item').find('.icon_check');
    $.each(eleActivities, function(index, e) {
      if(!$(e).hasClass('hide')) {
        activities += $(e).parent().next().html() + ',';
      }
    });
    activities = activities.substring(0, activities.length - 1);

    var  helpSubjects = '';
    var eleHelp = $('#education .help').find('.check_item').find('.icon_check');
    $.each(eleHelp, function(index, e) {
      if(!$(e).hasClass('hide')) {
        helpSubjects += $(e).parent().next().html() + ',';
      }
    });
    helpSubjects = helpSubjects.substring(0, helpSubjects.length - 1);

    var objUser = $scope.user;
    var majorId = listMajorId.toString();
    var activityId = listActivityId.toString();
    var helpSubjectId = listSubjectId.toString();

    StudentService.updateUserProfile(userId, objUser.username, cla, accom, majorId, activityId, helpSubjectId).then(function(json){
      if(json.data.status == 'true') {
        $window.scrollTo(0,0);
        $('#education .msg_success').removeClass('hide');
        setTimeout(function(){
          $('#education .msg_success').addClass('hide');
        }, 3000);
        listMajorId = [];
      }
    });
  };

  $scope.cancel = function() {
    window.location.href = '/';
  };

}]);

brotControllers.controller('PolicyCtrl', ['$sce','$scope', '$location', 'HomeService', function ($sce, $scope, $location, HomeService) {
  HomeService.getPolicy().then(function(data) {
    if(data.data.status) {
      $scope.policy = $sce.trustAsHtml(data.data.request_data_result[0].policy);
    }
  });
}]);

brotControllers.controller('TermsCtrl', ['$sce','$scope', '$location', 'HomeService', function ($sce, $scope, $location, HomeService) {
  HomeService.getTerms().then(function(data) {
    if(data.data.status) {
      $scope.terms = $sce.trustAsHtml(data.data.request_data_result[0].terms);
    }
  });
}]);

brotControllers.controller('NotificationCtrl', ['$sce','$scope', '$rootScope', '$location', '$log', '$routeParams', 'NotificationService', function ($sce, $scope, $rootScope, $location, $log, $routeParams, NotificationService) {

  var userId = localStorage.getItem('userId');
  var totalPageNotification;
  var listPage = [];
  var listReaded = [];
  $rootScope.page = 1;

  init();

  function init() {

    NotificationService.getNotificationReaded(userId).then(function(data) {
      if(data.data.request_data_result.length > 0) {
        $scope.listNotifications = data.data.request_data_result;
        for(var i = 0; i < $scope.listNotifications.length; i++) {
          if($scope.listNotifications[i].notification.length > 70) {
            $scope.listNotifications[i].notification = $scope.listNotifications[i].notification.substring(0, 70) + ' ...';
          }
        }
        $rootScope.notifications = $scope.listNotifications;
      } else {
        $scope.emptyNotification = 1;
        $scope.errorData = DATA_ERROR_NOTIFICATION.noNewNotification;
      }
    });

    NotificationService.getAllNotification(userId, $rootScope.page).then(function(data) {
      $scope.notificationsAll = data.data.request_data_result;
      $scope.countAll = data.data.count;
      if($scope.countAll == null) {
        $scope.errorData = DATA_ERROR_NOTIFICATION.noNewNotification;
      } else {
        totalPageNotification = Math.ceil($scope.countAll / 7);
        showPage(totalPageNotification, $rootScope.page, function(response) {
          $scope.listPage = response;
        });
      }

      $scope.showItem = true;
    });
  }

  $scope.notificationDetail = function(nid, subjectId, topicId, videoId, questionId, articleId, idSubAdmission, idTopicSubAdmission, idEssay) {
    NotificationService.getNotificationByUserId(userId).then(function(data) {
      $scope.listNotifications = data.data.request_data_result;
      $rootScope.countNotification = data.data.count;
      if($rootScope.countNotification == null) {
        NotificationService.getNotificationReaded(userId).then(function(data) {
          if(data.data.request_data_result.length > 0) {
            $scope.listNotifications = data.data.request_data_result;
            for(var i = 0; i < $scope.listNotifications.length; i++) {
              if($scope.listNotifications[i].notification.length > 70) {
                $scope.listNotifications[i].notification = $scope.listNotifications[i].notification.substring(0, 70) + ' ...';
              }
            }
            $rootScope.notifications = $scope.listNotifications;
          } else {
            $scope.emptyNotification = 1;
            $scope.errorData = DATA_ERROR_NOTIFICATION.noNewNotification;
          }
        });
      } else {
        for(var i = 0; i < $scope.listNotifications.length; i++) {
          if($scope.listNotifications[i].notification.length > 70) {
            $scope.listNotifications[i].notification = $scope.listNotifications[i].notification.substring(0, 70) + ' ...';
          }
        }
        $rootScope.notifications = $scope.listNotifications;  
      }

      if(subjectId == null) {
        if(articleId != null) {
          window.location.href = '#/admission/article/articledetail/' + articleId;
        } else if(idEssay != null) {
          window.location.href = '#/essay_detail/' + idEssay;
        } else {
          window.location.href = '#/admission/videoadmission/videodetail/' + idSubAdmission + '/' + idTopicSubAdmission + '/' + videoId;  
        }
      } else {
        if(videoId != null && questionId == null) {
          window.location.href = '#/detailVideo/' + subjectId + '/' + topicId + '/' + videoId;
        } else {
          window.location.href = '#/question_detail?subject=' + subjectId + '&question_id=' + questionId;
        }  
      }
    });
  };

  $scope.prevPage = function() {
    if($rootScope.page > 1) {
      $rootScope.page = parseInt($rootScope.page, 10) - 1;
      init();
    }  
  };

  $scope.nextPage = function() {
    if($rootScope.page < totalPageNotification) {
      $rootScope.page = parseInt($rootScope.page, 10) + 1;
      init();
    }  
  };

  $scope.showWithPage = function(page) {
    $rootScope.page = page;
    init();
  };

  $scope.addClassActive = function(page) {
    if(page == $rootScope.page) {
      return 'span_active';
    }
    return '';
  };

  // $scope.addClassHidden = function(status) {
  //   if(status == 'Y') {
  //     return 'hiddenNotification';
  //   }
  //   return '';
  // };
}]);

function drawChart(math, physic, biology, chemistry) {
  var doughnutData = [
    {
      value: parseInt(math, 10),
      color:"#FE8F09"
    },
    {
      value : parseInt(physic, 10),
      color : "#AF499C"
    },
    {
      value : parseInt(biology, 10),
      color : "#28AE61"
    },
    {
      value : parseInt(chemistry, 10),
      color : "#F1C433"
    }
  ];
  var option = {
    //Boolean - Whether we should show a stroke on each segment
    segmentShowStroke : true,

    //String - The colour of each segment stroke
    segmentStrokeColor : "#fff",

    //Number - The width of each segment stroke
    segmentStrokeWidth : 0,

    //The percentage of the chart that we cut out of the middle.
    percentageInnerCutout : 60,

    //Boolean - Whether we should animate the chart
    animation : true,

    //Number - Amount of animation steps
    animationSteps : 100,

    //String - Animation easing effect
    animationEasing : "easeOutBounce",

    //Boolean - Whether we animate the rotation of the Doughnut
    animateRotate : true,

    //Boolean - Whether we animate scaling the Doughnut from the centre
    animateScale : false,

    //Function - Will fire on animation completion.
    onAnimationComplete : null
  };

  var myDoughnut = new Chart(document.getElementById("chart").getContext("2d")).Doughnut(doughnutData, option);
}

brotControllers.controller('AllEssayCtrl', ['$scope', '$location', 'EssayService', function ($scope, $location, EssayService) {
  var userType = localStorage.getItem('userType');
  var userId = localStorage.getItem('userId');
  $scope.userId = userId;
  var clickE = 1;
  var listEssay = [];

  getAllEssay(clickE, userType, userId);

  function getAllEssay(page, userType, userId) {
    EssayService.getAllEssay(page, userType, userId).then(function(data) {
      $scope.showItem = true;
      var objs = data.data.request_data_result;

      if(objs.length !== 0) {
        $scope.countEssay = data.data.count;
        totalUpdateEssay = Math.ceil(data.data.count / 5);

        showPage(totalUpdateEssay, page, function(response) {
          $scope.listPage = response;
        });

        if(objs == null) {
          objs = [];
        }
        for(var i = 0; i < objs.length; i++) {
          var obj = objs[i];
          obj.reviewImage = EssayService.getImageReviewEssay(obj.uploadEssayId);
          var d = moment(obj.docSubmittedDate, 'YYYY-MM-DD hh:mm:ss').format('MM/DD/YYYY');
          obj.docSubmittedDate = d;
          listEssay.push(obj);
        }
        $scope.essays = listEssay;
      } else {
        $scope.showError = true;
        $scope.errorData = DATA_ERROR.noDataFound;
      }

      EssayService.getAllEssay(page++, userType, userId).then(function(data) {
        if(data.data.request_data_result.length < 5) {
          $scope.nextMore = 1;
        }
      });
    });
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