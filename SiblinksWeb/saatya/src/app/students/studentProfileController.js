brotControllers.controller('StudentProfileController', 
  ['$sce','$scope', '$modal', '$routeParams', '$http', '$location', 'StudentService', 'QuestionsService', 'MentorService', 'TeamMentorService', 'myCache',
                                       function ($sce, $scope, $modal, $routeParams, $http, $location, StudentService, QuestionsService, MentorService, TeamMentorService, myCache) {


    var userId = localStorage.getItem('userId'); 
    var userName = localStorage.getItem('userName'); 
    var userType = localStorage.getItem('userType');
    
    // Declare show message
    $scope.msgError="";
    $scope.msgSuccess="";
    
    $scope.currentPwd = "";
    $scope.newPwd  = "";
    $scope.confirmPwd = "";
    
    var isInit = true;
    $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';
    $scope.sections = [
      {name: 'My Questions'},
      {name: 'Essay'},
      {name: 'Setting'}
    ];
    $scope.selected = $scope.sections[0];


    init();

    function init(){
      getMentorInfo();
      getStudentProfile();
      getEssayProfile();
    }


    function getMentorInfo(){
      TeamMentorService.getTopMentorsByType(5, 0, 'subcribe', userId).then(function (data) {
          var data_result = data.data.request_data_result;
          var subjects = myCache.get("subjects");
          if (data_result) {
              var listTopMentors = [];
              for (var i = 0; i < data_result.length; i++) {
                  var mentor = {};
                  if(data_result[i].isSubs == 1){
                      mentor.userid = data_result[i].userid;
                      mentor.userName = data_result[i].userName;
                      mentor.imageUrl = data_result[i].imageUrl;
                      mentor.numsub = data_result[i].numsub;
                      mentor.numvideos = data_result[i].numvideos;
                      mentor.defaultSubjectId = data_result[i].defaultSubjectId;
                      mentor.listSubject= getSubjectNameById(data_result[i].defaultSubjectId, subjects);
                      mentor.numAnswers = data_result[i].numAnswers;
                      listTopMentors.push(mentor);
                  }
              }
              $scope.listMentorBySubs = listTopMentors;
          }            
      });
    }


    function getEssayProfile(){
      StudentService.getEssayProfile(userId, 0, 0).then(function(data){
          $scope.essays = data.data.request_data_result;
      });
    }

    function getStudentProfile(){
      StudentService.getUserProfile(userId).then(function(data){
        if (data.data.status) {
          $scope.student = data.data.request_data_result;
          $scope.student.fullName = (($scope.student.firstname == null)?'': $scope.student.firstname) + ' ' + (($scope.student.firstname == null)?'':$scope.student.lastName);
          //$scope.student.imageUrl =  //$scope.student.imageUrl.indexOf('http') == -1 ? $scope.baseIMAGEQ + $scope.student.imageUrl : $scope.student.imageUrl;
          $scope.student.registrationTime = moment($scope.student.registrationTime).format('MMM, YYYY ', 'en');
          if ($scope.student.gender == "M") {
            $('#male').prop('checked', true);
          }
          if ($scope.student.gender == "F") {
            $('#female').prop('checked', true);
          } else {
            $('#other').prop('checked', true);
          }
          displaySetting();
        }
      });
    }

    function displaySetting(){
      $("#firstName").val($scope.student.firstname);
      $("#lastName").val($scope.student.lastName);
      $("#about").val($scope.student.description);
      $("#email").val($scope.student.email);
      $("#description").val($scope.student.description);
      $("#bio").val($scope.student.bio);
      
      // Get favourite
      if($scope.student.favorite !=null && $scope.student.favorite !== undefined){
    	  var favorite = $scope.student.favorite.split(',');
    	  for (var i = 0; i < favorite.length; i++) {
    		  if (favorite[i] == 'Music') {
    			  $('#music').prop('checked', true);
    		  } 
    		  if (favorite[i] == 'Art') { 
    			  $('#art').prop('checked', true);
    		  }
    		  if (favorite[i] == 'Sport') { 
    			  $('#sport').prop('checked', true);
    		  }
    	  }
      }
    }



    function detectMultiImage(imagePath) {
        if(isEmpty(imagePath)){
            return null;
        }
        if (imagePath != null && imagePath.indexOf(";") != -1) {
            return imagePath.split(";");
        }
        var listImage = [];
        listImage.push(imagePath)
        return listImage;
    };

   $scope.setMaster = function(section) {
        $scope.selected = section;
        
        if ($scope.selected.name == "Setting") {
          $("#setting").addClass('active');
          $("#essay").removeClass('active');
          $("#my-questions").removeClass('active');
        }
        else if ($scope.selected.name == "Essay") {
          $("#essay").addClass('active');
          $("#setting").removeClass('active');
          $("#my-questions").removeClass('active');
        }
        else{
          $("#my-questions").addClass('active');
          $("#essay").removeClass('active');
          $("#setting").removeClass('active');
        }
    }

    $scope.isSelected = function(section) {
        return $scope.selected === section;
    }

    $scope.onFileSelect = function ($files) {
        var fd = new FormData();
        if ($files != null) {
        	fd.append('uploadfile', $files[0]);
            fd.append("userid",userId);
            fd.append("imageUrl",localStorage.getItem('imageUrl'));
            StudentService.uploadAvatar(fd).then(function (data) {
               if(data.data.status=="true") {
                   $scope.student.imageUrl = data.data.request_data_result;
                   setStorage('imageUrl', $scope.student.imageUrl, 10);
                   $scope.imageUrl=data.data.request_data_result;
                   window.location.href = '#/studentProfile';
				           window.location.reload();
               }
               else {
                   $scope.errorMessage = "Can't not upload avatar";
               }
            });
        }

    };

    $scope.changePassword = function(oldPwd, newPwd, confirmPwd){
      // Valid
      if(oldPwd == "" || oldPwd === undefined){
        $scope.msgError = "Password is required.";
        angular.element('#currentPwd').trigger('focus');
      } else if(newPwd == "" || newPwd === undefined){
        $scope.msgError = "New password is required.";
        angular.element('#newPwd').trigger('focus');
      } else if(confirmPwd == "" || confirmPwd === undefined){
    	  $scope.msgError = "Confirm password is required.";
    	  angular.element('#confirmPwd').trigger('focus');
      } else if (oldPwd == newPwd) {
    	  angular.element('#newPwd').trigger('focus');
          $scope.msgError = "New Password must difference New Password.";
      } else if (newPwd !== confirmPwd) {
        angular.element('#newPwd').trigger('focus');
        $scope.msgError = "Password is not matched.";
      } else {// Request change pwd
        var user = {
          'username':userName,
          'password':oldPwd,
          'newpassword':newPwd
        }
        
        StudentService.changePassword(user).then(function(data){
          console.log(data.data.request_data_result);
          if (data.data== "true") {
            $scope.msgSuccess="Change password successful.";
          } else {
        	  $scope.msgError = data.data.request_data_result;
          }
        });
      }
    };

    $scope.update = function(){
      var check = true;
      var error = '';

      var favorite = "";
      if ($('#music').is(':checked')) {
        favorite += $('#music').val();
      }
      if ($('#art').is(':checked')) {
        if (favorite.length > 0)
          favorite += ',' + $('#art').val();
        else
          favorite += $('#art').val();
      }
      if ($('#sport').is(':checked')) {
        if (favorite.length > 0)
          favorite += ',' + $('#sport').val();
        else
          favorite += $('#sport').val();
      }

      var gender = '';
      if ($('#male').is(':checked')) {
        gender = "M";
      }
      else if ($('#female').is(':checked')) {
        gender = "F";
      }
      else{
        gender = "O";
      }


      if ($('#email').val()) {
        if (!isValidEmailAddress($('#email').val())) {
          check = false;
          error += "Email is not valid";
        }
      }      

      if (check) {        
        var student = {
          'userid': userId,
          'firstName': $('#firstName').val(),
          'lastName': $('#lastName').val(),
          'email': $('#email').val(),
          'gender':gender,
          'school': $('#school').val(),
          'bio': $('#bio').val(),
          'description':$('#about').val(),
          'favorite':favorite
        };
        console.log(student);
        StudentService.updateStudentProfile(student).then(function(data){
          if (data.data.request_data_result == "Success") {
            window.location.href = '#/studentProfile';
            window.location.reload();
          }
          else{
              console.log(error);
           // alert("Something went wrong. Please try again later.");
          }
        });
      }
      else{
        console.log(error);
      }
    }

    function isValidEmailAddress(emailAddress) {
      var pattern = /^([a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+(\.[a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+)*|"((([ \t]*\r\n)?[ \t]+)?([\x01-\x08\x0b\x0c\x0e-\x1f\x7f\x21\x23-\x5b\x5d-\x7e\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|\\[\x01-\x09\x0b\x0c\x0d-\x7f\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))*(([ \t]*\r\n)?[ \t]+)?")@(([a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.)+([a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.?$/i;
      return pattern.test(emailAddress);
    };
}]);
