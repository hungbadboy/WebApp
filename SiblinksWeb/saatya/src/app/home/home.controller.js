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
             //
             // if (myCache.get("allQuestions") !== undefined) {
             //     $log.info("My cache already exists");
             //     $scope.allQuestions = myCache.get("allQuestions");
             // } else {
             //     QuestionsService.getAllQuestions().then(function (data) {
             //         if (data.data.status) {
             //             $log.info("Get All question");
             //             $scope.allQuestions = data.data.request_data_result;
             //             myCache.put("allQuestions", data.data.request_data_result);
             //         }
             //     });
             // }
            if (myCache.get("subjects") !== undefined) {
                $log.info("My cache already exists");
                $scope.subjects = myCache.get("subjects");
                setStorage('subjects',JSON.stringify($scope.subjects), 30);
            } else {
                HomeService.getAllCategory().then(function (data) {
                    if (data.data.status) {
                        $log.info("Get service subject with category");
                        $scope.subjects = data.data.request_data_result;
                        myCache.put("subjects", data.data.request_data_result);
                        setStorage('subjects',JSON.stringify(data.data.request_data_result), 30);
                    }
                });
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
        $scope.onFileSelect = function ($files) {
            $scope.askErrorMsg = "";
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
            // get question of student
        	if (selectCategory == null || selectCategory === undefined || selectCategory.originalObject == null) {
        		$scope.askErrorMsg='Please choose category';
        		$("#autocompleteCate_value").focus();
        		$rootScope.myVarC = !$scope.myVarC;
        		$timeout(function () {
        			$rootScope.myVarC = false;
        		}, 2500);
        		return;
        	}
            var questions = $('#autocompleteQuest_value').val();
            if (!questions) {
                $rootScope.myVarQ = !$scope.myVarQ;
                $timeout(function () {
                    $rootScope.myVarQ = false;
                }, 2500);
                $scope.askErrorMsg='Please input your question';
                $("#autocompleteQuest_value").focus();
                return;
            }

            if (isEmpty(userId) ||userId=='-1') {
            	$scope.askErrorMsg='Please login before you ask a question';
                $rootScope.myVarU = !$scope.myVarU;
                $timeout(function () {
                    $rootScope.myVarU = false;
                }, 2500);
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
                $scope.askErrorMsg='You only upload ' + MAX_IMAGE +' image';
                $rootScope.myVarU = !$scope.myVarU;
                $timeout(function () {
                    $rootScope.myVarU = false;
                }, 2500);
                return;
            }

            if(totalSize > MAX_SIZE_IMG_UPLOAD){
                $scope.askErrorMsg='Image over 5Mb';
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
                    window.location.reload();
                }
                else {
                    $scope.askErrorMsg = data.data.request_data_result;
                }
                $rootScope.$broadcast('close');
            });

          
        };

    }]);