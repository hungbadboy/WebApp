//=========================================== HOME.CONTROLLER.JS==============
brotControllers.controller('HomeController', ['$scope', '$http', '$location', '$rootScope', '$timeout', '$log', 'HomeService', 'NotificationService', 'StudentService', 'VideoService', 'myCache', 'QuestionsService',
    function ($scope, $http, $location, $rootScope, $timeout, $log, HomeService, NotificationService, StudentService, VideoService, myCache, QuestionsService) {
        var subjects = [];
        // check login page
        brot.signin.statusStorageHtml();
        $scope.login = 0;
        $scope.filesArray = [];
        var selectCategory = null;
        $scope.askErrorMsg = "";
        var userId = localStorage.getItem('userId');

        $scope.title = "Home";
        $scope.questionIndex = "What do you want to ask...?";
        $scope.subjects = JSON.parse(localStorage.getItem('subjects'));

        init();

        function init() {
            if (isEmpty(userId)) {
                $scope.login = 1;
            }

            $('#autocompleteQuest_value').focus(function () {
                $(this).attr('placeholder', '');
            });
            $('#autocompleteQuest_value').focusout(function () {
                $(this).attr('placeholder', $scope.questionIndex);
            });
        }

        $scope.selectedQuestion = function (selected) {
            pid = selected.originalObject.pid;
            window.location.href = '/#/question_detail/?subject=' + 0 + '&question_id=' + pid;
        };

        $scope.selectedSubject = function (selected) {
            selectCategory = selected;
        };

        //fix in IE
        $timeout(function () {
            // $("#autocompleteCate_value_dropdown").addClass('ng-hide')
            $scope.$broadcast('angucomplete-alt:clearInput',"autocompleteCate_value" );
        }, 300);

        $scope.localSearchQuestion = function (str, questions) {
            var matches = [];

            questions.forEach(function (question) {
                if (selectCategory === undefined || $('#autocompleteCate_value').val() == "") {
                    if ((question.content.toLowerCase().indexOf(str.toString().toLowerCase()) >= 0)
                    ) {
                        matches.push(question);
                    }
                }
                else {
                    if ((question.content.toLowerCase().indexOf(str.toString().toLowerCase()) >= 0) &&
                        selectCategory.originalObject.id == question.subjectId) {
                        matches.push(question);
                    }

                }
            });
            return matches;
        };

        $scope.signupcomplete = function () {
            if (userId === null) {
                window.location.href = '#/signupcomplete';
            }
        };

        $scope.signupStudent = function () {
            if (userId === null) {
                window.location.href = '#/signup';
            }
        };
        
        $scope.onFileSelect = function ($files,errFiles) {
            $scope.askErrorMsg = "";
            var errFile = errFiles && errFiles[0];
            if(!isEmpty(errFile)) {
        		if(errFile.$error == 'pattern') {
	                $scope.askErrorMsg = 'File wrong format. Please select file image!';
	                return;
        		} else if (errFile.$error == 'maxSize') {
        			$scope.askErrorMsg = 'File size must be less than 5MB.';
        		} else {
        			// do nothing
        		}
            }
            if ($files!=null && $files.length > MAX_IMAGE){
                $scope.askErrorMsg = 'You cannot attach more than ' + MAX_IMAGE +' images for each question.';
                return ;
            }
            if ($files != null) {
                for (var i = 0; i < $files.length; i++) {
                    var file = $files[i];
                    $scope.filesArray.push(file);

                }
            }
        };
        $scope.removeImg = function (index) {
            $scope.filesArray.splice(index, 1);

        }

        $scope.redirectForum = function () {

            if (isEmpty(userId) ||userId=='-1') {
                $scope.askErrorMsg='Please login before you ask a question.';
                return;
            }
            // get question of student
        	if (selectCategory == null || selectCategory === undefined || selectCategory.originalObject == null) {
        		$scope.askErrorMsg='Please choose category.';
        		$("#autocompleteCate_value").focus();
        		return;
        	}
            var questions = $('#autocompleteQuest_value').val();
            if (questions == null || (questions !=null && questions.trim() == '')) {
                $scope.askErrorMsg='Please input your question.';
                $("#autocompleteQuest_value").focus();
                return;
            }


            fd = new FormData();
            var totalSize = 0;
            if ($scope.filesArray != null) {
                for (var i = 0; i < $scope.filesArray.length; i++) {
                    file = $scope.filesArray[i];
                    totalSize += file.size;
                    fd.append('file', file);
                }
            }
            if ($scope.filesArray.length > MAX_IMAGE) {
                $scope.askErrorMsg='You cannot attach more than  ' + MAX_IMAGE +' images for each question.';
                $rootScope.myVarU = !$scope.myVarU;
                $timeout(function () {
                    $rootScope.myVarU = false;
                }, 2500);
                return;
            }

            if(totalSize > MAX_SIZE_IMG_UPLOAD){
                $scope.askErrorMsg='File size must be less than 5MB.';
                $rootScope.myVarU = !$scope.myVarU;
                $timeout(function () {
                    $rootScope.myVarU = false;
                }, 2500);
                return;
            }

            fd.append('userId', userId);
            fd.append('content', questions);
            fd.append('subjectId', selectCategory.originalObject.subjectId);
            $rootScope.$broadcast('open');
            HomeService.addQuestion(fd).then(function (data) {
                if (data.data.status == "true") {
                    $(".popup-images, .form-ask-question").css({"left": "100%"});
                    window.location.href = '/#/ask_a_question/-1';
                }
                else {
                    $scope.askErrorMsg = data.data.request_data_result;
                }
                $rootScope.$broadcast('close');
            });
        };
    }]);