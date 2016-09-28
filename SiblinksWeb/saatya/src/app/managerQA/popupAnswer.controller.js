brotControllers.controller('popupAnswerController', ['$scope', '$modalInstance',
    '$rootScope',
    '$routeParams',
    '$location',
    '$timeout',
    '$log',
    '$window',
    'QuestionsService',
    'AnswerService',
    'SubjectServices',
    'CategoriesService',
    'HomeService',
    'CommentService', 'StudentService',
    'myCache', 'q_id', function ($scope, $modalInstance, $rootScope, $routeParams,
                                 $location, $timeout, $log, $window, QuestionsService, AnswerService,
                                 SubjectServices, CategoriesService,
                                 HomeService, CommentService, StudentService, myCache, q_id) {
        //$scope.title1 = titlename2;
        init();
        var userId = localStorage.getItem('userId');
        $scope.userId = userId;
        function init() {
            QuestionsService.getQuestionById(q_id).then(function (data) {
                var obj = data.data.request_data_result;
                var viewNew = parseInt(obj[0].numViews, 10) + 1;
                authorId = obj[0].authorID;
                $scope.topicId = obj[0].topicId;
                if (obj[0].idFacebook == null && obj[0].idGoogle == null) {
                    obj[0].imageUrl = StudentService.getAvatar(obj[0].authorID);
                } else {
                    obj[0].imageUrl = StudentService.getAvatar(obj[0].authorID);
                }

                var question = {
                    "authorId": obj[0].authorID,
                    "question_id": obj[0].pid,
                    "question_heading": obj.question_heading,
                    "question_text": obj[0].content,
                    "views": viewNew,
                    "question_creation_date": moment(obj[0].updateTime, 'YYYY-MM-DD h:mm:ss').startOf('hour').fromNow(),
                    "timeStamp": convertUnixTimeToTime(obj[0].timeStamp),
                    "author": obj[0].userName,
                    "countLike": obj[0].countLike,
                    "firstName": obj[0].firstName,
                    "lastName": obj[0].lastName,
                    "topicId": obj[0].topicId,
                    "avatar": obj[0].imageUrl,
                    "subjectId":obj[0].subjectId
                };
                $scope.question = question;
            });


            $('#select-file').click(function () {
                $('#file').click();
            });
            $scope.onFileSelect = function ($files) {

                $('.waitingUp').removeClass('hide');
                $filex = $files;
                if ($filex != null) {
                    var fd = new FormData();
                    if ($filex != null) {
                        for (var i = 0; i < $filex.length; i++) {
                            file = $filex[i];
                            fd.append('file', file);
                        }
                    }
                    var url = NEW_SERVICE_URL + 'comments/uploadMultiFile';
                    $http({
                        method: 'POST',
                        url: url,
                        headers: {
                            'Content-Type': undefined
                        },
                        data: fd,
                        transformRequest: function (data, headersGetterFunction) {
                            return data;
                        }

                    }).success(function (data) {
                        var local = data.request_data_result;
                        $('#uploadImage').modal('hide');
                        $('.waitingUp').addClass('hide');
                        var content = CKEDITOR.instances["txtAnswer"].getData();
                        var imgPath = local.split(";;");
                        for (i = 0; i < imgPath.length; i++) {
                            content += "<img style='max-width: 570px;' src='" + imgPath[i] + "'/>";
                        }
                        CKEDITOR.instances["txtAnswer"].setData(content);
                    })
                        .error(function (data, status) {
                            $('.waitingUp').addClass('hide');
                            $scope.errorUpload = data.request_data_result;
                            return;
                        });
                }
            };
        }//end init()
        $scope.close = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.postAnswer = function(authorId,question_id,topicId,subjectId){

            var content = CKEDITOR.instances["txtAnswer"].getData() ;

            if(userId == null) {
                $('#popSignIn').modal('show');
            } else {
                if(content === '' || content == null) {
                    alert('Answer text is required.');
                } else {
                    AnswerService.postAnswer(userId,authorId, question_id, content,subjectId).then(function (data) {
                        var rs = data.data.request_data_result;
                        if(rs){
                            $modalInstance.dismiss('cancel');
                        }
                    });
                }

            }
        }

    }]);