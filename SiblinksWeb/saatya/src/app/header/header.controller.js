//=========================================== HEADER.CONTROLLER.JS==============
brotControllers.controller('UserHeaderController',
    ['$scope', '$modal', '$rootScope', '$http', '$location', '$log', 'NotificationService', 'LogoutService','myCache','HomeService',
        function ($scope, $modal, $rootScope, $http, $location, $log, NotificationService, LogoutService,myCache, HomeService) {
            // check login page
            brot.signin.statusStorageHtml();
            $scope.isHidden = false;
            $rootScope.notifications = [];
            // include
            $scope.headerByUser="src/app/header/CommonHeader.tpl.html";
            //$scope.menuHeader="src/app/header/menuHeader.tpl.html";
            
            $scope.footerUser="";
            var username = localStorage.getItem('userName');
            var userId = "";
            var userType = "";
            
            $scope.imageUrl = "";

            $scope.fullName = localStorage.getItem('nameHome');
            
            if (localStorage.getItem('imageUrl') !== undefined && localStorage.getItem('imageUrl') !='undefined') {
            	$scope.imageUrl = localStorage.getItem('imageUrl')
            };
            
            if (localStorage.getItem('userId') !== undefined || localStorage.getItem('userId') != 'undefined') {
                userId = localStorage.getItem('userId');
            }
            
            if (localStorage.getItem('userType') !== undefined || localStorage.getItem('userType') != 'undefined') {
            	userType = localStorage.getItem('userType');
            }
			//	  $('.icon_search').on('click', function () {
			//	    $('.form_search').removeClass('hide').find('input').focus();
			//	    hideNotification();
			//	    hideProfileSetting();
			//	  });
			//
			//	  $(document).on('click', function (e) {
			//	    if(e.target != $('.form_search') && e.target.id != 'img-search' && !$('.form_search').find(e.target).length) {
			//	      $('.form_search').addClass('hide');
			//	      $('#header .w975').find('.search-text').val('');
			//	      $('.form_search').find('.dropdown').removeClass('open');
			//	      $(".check-search").prop('checked', false); 
			//	    }
			//	  });

            $scope.profile = function () {
//                $('.user-setting-wrapper span.current').trigger('click');
                $location.path('/student/studentProfile' + userId);
            };

            $scope.goToEditStudent = function () {
                $location.path('/editStudent/basic');
            };

//            $scope.admission = function () {
//                if (userId == null) {
//                    // brot.signin.signin();
//                    $('#popSignIn').modal('show');
//                } else {
//                    $location.path('/admission');
//                }
//            };

//            $scope.popupNotification = function () {
//                if ($('#notification .wrap_img').hasClass('hide')) {
//                    hideProfileSetting();
//                    $('#notification').find('.active').addClass('hide');
//                    showNotification($(this));
//                } else {
//                    hideNotification();
//                }
//
//            };

            function init() {
            	// 
                // show or hide notification panel
                if(myCache.get("subjects") !== undefined) {
                    $log.info("My cache already exists");
                    $scope.subjects = myCache.get("subjects");
                } else {
                    HomeService.getAllCategory().then(function(data) {
                        if(data.data.status) {
                            $log.info("Get service subject with category");
                            $scope.subjects = data.data.request_data_result;
                            myCache.put("subjects", data.data.request_data_result);
                        }
                    });
                }
                if (userId != null) {
//                    NotificationService.getNotificationByUserId(userId).then(function (data) {
//                        $scope.listNotifications = data.data.request_data_result;
//                        $rootScope.countNotification = data.data.count;
//                        if ($rootScope.countNotification == null) {
//                            NotificationService.getNotificationReaded(userId).then(function (data) {
//                                if (data.data.request_data_result.length > 0) {
//                                    $scope.listNotifications = data.data.request_data_result;
//                                    for (var i = 0; i < $scope.listNotifications.length; i++) {
//                                        if ($scope.listNotifications[i].notification.length > 70) {
//                                            $scope.listNotifications[i].notification = $scope.listNotifications[i].notification.substring(0, 70) + ' ...';
//                                        }
//                                    }
//                                    $rootScope.notifications = $scope.listNotifications;
//                                } else {
//                                    $scope.emptyNotification = 1;
//                                    $scope.errorData = DATA_ERROR_NOTIFICATION.noNewNotification;
//                                }
//                            });
//                        } else {
//                            for (var i = 0; i < $scope.listNotifications.length; i++) {
//                                if ($scope.listNotifications[i].notification.length > 70) {
//                                    $scope.listNotifications[i].notification = $scope.listNotifications[i].notification.substring(0, 70) + ' ...';
//                                }
//                            }
//                            $rootScope.notifications = $scope.listNotifications;
//                        }
//                    });

                    //$('#notification').removeClass('inactive');
                    // call ws to get notification of user

//                    $('#notification .icon_notification_white').click(function (event) {
//                        hideProfileSetting();
//                        showNotification($(this));
//                    });
//
//                    $('#notification .wrap_img .icon_notification').click(function (event) {
//                        hideProfileSetting();
//                        hideNotification();
//                    });

//                    $('.user-setting-wrapper span.current').click(function () {
//                        hideNotification();
//
//                        if ($(this).hasClass('selected')) {
//                            $(this).removeClass('selected');
//                            $('.user-setting').addClass('hide');
//                        } else {
//                            $(this).addClass('selected');
//                            $('.user-setting').removeClass('hide');
//                        }
//                    });
                    // hide login
                    if (userType != null && userType != undefined && userType == 'S') {
                    	$scope.headerByUser="src/app/header/StudentHeader.tpl.html";
                    	$scope.footerUser="src/app/footer/footer.tpl.html";
                    } else if (userType != null && userType != undefined && userType == 'M') {
                    	$scope.headerByUser="src/app/header/MentorHeader.tpl.html";
                    }
                } else {
                	// User is not yet login
                	$scope.headerByUser="src/app/header/CommonHeader.tpl.html";
                	$scope.footerUser="src/app/footer/footer.tpl.html";
                    
                }
            }

            function showNotification(ele) {
                NotificationService.updateAllNotification(userId).then(function (data) {
                    if (data.data.request_data_result) {
                        $rootScope.countNotification = 0;
//                        $(ele).addClass('hide');
//                        $('#notification').find('.active').addClass('hide');
//                        $('#notification .wrap_img').removeClass('hide');
                    }
                });
            }

            function hideNotification() {
//                $('#notification').find('.active').removeClass('hide');
//                $('#notification .wrap_img').addClass('hide');
//                $('#notification .icon_notification_white').removeClass('hide');
            }

            function hideProfileSetting() {
//                $('.user-setting-wrapper span.current').removeClass('selected');
//                $('.user-setting').addClass('hide');
            }

            init();

            $scope.logout = function () {
                LogoutService.logout(username).then(function (data) {
                });
                window.localStorage.clear();
                window.location.href = '/';
            };

            $scope.viewAllNotification = function () {
                window.location.href = '#/notification';
            };

            $scope.detailNotification = function (nid, subjectId, topicId, videoId, questionId, articleId, idSubAdmission, idTopicSubAdmission, idEssay) {
                NotificationService.getNotificationByUserId(userId).then(function (data) {
                    $scope.listNotifications = data.data.request_data_result;
                    $rootScope.countNotification = data.data.count;
                    if ($rootScope.countNotification == null) {
                        NotificationService.getNotificationReaded(userId).then(function (data) {
                            if (data.data.request_data_result.length > 0) {
                                $scope.listNotifications = data.data.request_data_result;
                                for (var i = 0; i < $scope.listNotifications.length; i++) {
                                    if ($scope.listNotifications[i].notification.length > 70) {
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
                        for (var i = 0; i < $scope.listNotifications.length; i++) {
                            if ($scope.listNotifications[i].notification.length > 70) {
                                $scope.listNotifications[i].notification = $scope.listNotifications[i].notification.substring(0, 70) + ' ...';
                            }
                        }
                        $rootScope.notifications = $scope.listNotifications;
                    }

                    if (subjectId == null) {
                        if (articleId != null) {
                            window.location.href = '#/admission/article/articledetail/' + articleId;
                        } else if (idEssay != null) {
                            window.location.href = '#/essay_detail/' + idEssay;
                        } else {
                            window.location.href = '#/admission/videoadmission/videodetail/' + idSubAdmission + '/' + idTopicSubAdmission + '/' + videoId;
                        }
                    } else {
                        if (videoId != null && questionId == null) {
                            window.location.href = '#/detailVideo/' + subjectId + '/' + topicId + '/' + videoId;
                        } else {
                            window.location.href = '#/question_detail?subject=' + subjectId + '&question_id=' + questionId;
                        }
                    }
                });
            };

//            $('#header .w975').find('.search-text').keypress(function (event) {
//                var searchText = $('#header .w975').find('.search-text').val();
//
//                if (event.keyCode == 13) {
//                    // if($(".item-select input:checked" ).val() == 1) {
//                    window.location.href = '#/searchVideo/' + searchText + '/' + 1;
//                    // }
//                }
//            });

            $scope.showItemSearch = function () {
//                if ($('.form_search .dropdown').hasClass('open')) {
//                    $('.form_search').find('.dropdown').removeClass('open');
//                } else {
//                    $('.form_search').find('.dropdown').addClass('open');
//                }
            };
            
            
            // Toggle user information
            $scope.isShowHideUserInfo = true;
            $scope.toggleUserInfo = function() {
            	$scope.isShowHideUserInfo = $scope.isShowHideUserInfo ? false : true;
            	isToggleUserInfo = true;
      	  	}

            $scope.showUpload = function(){
                var modalInstance = $modal.open({
                templateUrl: 'src/app/mentors/video/upload_tutorial_popup.tpl.html',
                controller: 'UploadTutorialController',
                resolve: {
                    u_id: function () {
                        return userId;
                    },
                    v_id: function(){
                        return -1;
                    }
                }
              });
            }
        }]);
//=========================================== HEADER.CONTROLLER.JS==============