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