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