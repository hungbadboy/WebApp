//=========================================== HEADER.CONTROLLER.JS==============
brotControllers.controller('UserHeaderController',
    ['$scope', '$modal', '$rootScope', '$http', '$location', '$document', '$log', 'NotificationService', 'LogoutService', 'myCache', 'HomeService', '$window',
        function ($scope, $modal, $rootScope, $http, $location, $document, $log, NotificationService, LogoutService, myCache, HomeService, $window) {
            // check login page
            brot.signin.statusStorageHtml();
            $scope.isHidden = false;
            $rootScope.notifications = [];
            // include
            $scope.headerByUser = "src/app/header/CommonHeader.tpl.html";
            //$scope.menuHeader="src/app/header/menuHeader.tpl.html";

            $scope.footerUser = "";
            var username = localStorage.getItem('userName');
            var userId = "";
            var userType = "";

            $rootScope.imageUrl = "";

            $scope.fullName = localStorage.getItem('nameHome');

            if (localStorage.getItem('imageUrl') !== undefined && localStorage.getItem('imageUrl') != 'undefined') {
                // $scope.imageUrl = localStorage.getItem('imageUrl');
                // if($rootScope.imageUrl != undefined || $rootScope.imageUrl != null){
                //    $scope.imageUrl = $rootScope.imageUrl;
                // }else{
                $rootScope.imageUrl = localStorage.getItem('imageUrl') != null ? localStorage.getItem('imageUrl') : 'assets/images/noavartar.jpg';
                // }
            }
            ;

            if (localStorage.getItem('userId') !== undefined || localStorage.getItem('userId') != 'undefined') {
                userId = localStorage.getItem('userId');
            }

            if (localStorage.getItem('userType') !== undefined || localStorage.getItem('userType') != 'undefined') {
                userType = localStorage.getItem('userType');
            }

            $scope.profile = function () {
                $location.path('/student/studentProfile' + userId);
            };

            /*$scope.goToEditStudent = function () {
             $location.path('/editStudent/basic');
             };*/

            $scope.goToProfile = function (url) {
                $('#user-info').hide();
                $location.path(url);

            }

            $scope.popupNotification = function () {
                showNotification($(this));
            };

            function init() {
                if (userId != null) {
                    // get Notification not read by user
                    NotificationService.getNotificationByUserId(userId).then(function (data) {
                        $rootScope.countNotification = data.data.count;
                    });
                    //$scope.listNotifications = data.data.request_data_result;
//                    	if ($rootScope.countNotification == null) {
//                    		NotificationService.getNotificationReaded(userId).then(function (data) {
//                              if (data.data.request_data_result.length > 0) {
//                                  $scope.listNotifications = data.data.request_data_result;
//                                  for (var i = 0; i < $scope.listNotifications.length; i++) {
//                                      if ($scope.listNotifications[i].notification.length > 70) {
//                                          $scope.listNotifications[i].notification = $scope.listNotifications[i].notification.substring(0, 70) + ' ...';
//                                      }
//                                  }
//                                  $rootScope.notifications = $scope.listNotifications;
//                              } else {
//                                  $scope.emptyNotification = 1;
//                                  $scope.errorData = DATA_ERROR_NOTIFICATION.noNewNotification;
//                              }
//                          });
//                    	} else {
//                    		for (var i = 0; i < $scope.listNotifications.length; i++) {
//                              if ($scope.listNotifications[i].notification.length > 70) {
//                                  $scope.listNotifications[i].notification = $scope.listNotifications[i].notification.substring(0, 70) + ' ...';
//                              }
//                          }
//                          $rootScope.notifications = $scope.listNotifications;
//                    	}
//                    }

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
                        $scope.headerByUser = "src/app/header/StudentHeader.tpl.html";
                        $scope.footerUser = "src/app/footer/footer.tpl.html";
                    } else if (userType != null && userType != undefined && userType == 'M') {
                        $scope.headerByUser = "src/app/header/MentorHeader.tpl.html";
                    }
                } else {
                    // User is not yet login
                    $scope.headerByUser = "src/app/header/CommonHeader.tpl.html";
                    $scope.footerUser = "src/app/footer/footer.tpl.html";

                }

                $rootScope.isMenuMobile = checkMenuType();
                $rootScope.isMiniSideRightBar = checkMenuType();
            }

            function showNotification(ele) {
                if ($rootScope.countNotification !== null && $rootScope.countNotification !== undefined && $rootScope.countNotification > 0) {
                    NotificationService.updateAllNotification(userId).then(function (data) {
                        if (data.data.request_data_result) {
                            $rootScope.countNotification = 0;
                        }
                    });
                }
            }

            init();

            $scope.logout = function () {
                LogoutService.logout(userId).then(function (data) {
                    if (data.data.status == true) {
                        window.localStorage.clear();
                        window.location.href = '/';
                    }
                });
            };

            $scope.viewAllNotification = function () {
                window.location.href = '#/notification';
            };

//            $scope.detailNotification = function (nid, subjectId, topicId, videoId, questionId, articleId, idSubAdmission, idTopicSubAdmission, idEssay) {
//                NotificationService.getNotificationByUserId(userId).then(function (data) {
//                    $scope.listNotifications = data.data.request_data_result;
//                    $rootScope.countNotification = data.data.count;
//                    if ($rootScope.countNotification == null) {
//                        NotificationService.getNotificationReaded(userId).then(function (data) {
//                            if (data.data.request_data_result.length > 0) {
//                                $scope.listNotifications = data.data.request_data_result;
//                                for (var i = 0; i < $scope.listNotifications.length; i++) {
//                                    if ($scope.listNotifications[i].notification.length > 70) {
//                                        $scope.listNotifications[i].notification = $scope.listNotifications[i].notification.substring(0, 70) + ' ...';
//                                    }
//                                }
//                                $rootScope.notifications = $scope.listNotifications;
//                            } else {
//                                $scope.emptyNotification = 1;
//                                $scope.errorData = DATA_ERROR_NOTIFICATION.noNewNotification;
//                            }
//                        });
//                    } else {
//                        for (var i = 0; i < $scope.listNotifications.length; i++) {
//                            if ($scope.listNotifications[i].notification.length > 70) {
//                                $scope.listNotifications[i].notification = $scope.listNotifications[i].notification.substring(0, 70) + ' ...';
//                            }
//                        }
//                        $rootScope.notifications = $scope.listNotifications;
//                    }
//
//                    if (subjectId == null) {
//                        if (articleId != null) {
//                            window.location.href = '#/admission/article/articledetail/' + articleId;
//                        } else if (idEssay != null) {
//                            window.location.href = '#/essay_detail/' + idEssay;
//                        } else {
//                            window.location.href = '#/admission/videoadmission/videodetail/' + idSubAdmission + '/' + idTopicSubAdmission + '/' + videoId;
//                        }
//                    } else {
//                        if (videoId != null && questionId == null) {
//                            window.location.href = '#/detailVideo/' + subjectId + '/' + topicId + '/' + videoId;
//                        } else {
//                            window.location.href = '#/question_detail?subject=' + subjectId + '&question_id=' + questionId;
//                        }
//                    }
//                });
//            };

//            $('#header .w975').find('.search-text').keypress(function (event) {
//                var searchText = $('#header .w975').find('.search-text').val();
//
//                if (event.keyCode == 13) {
//                    // if($(".item-select input:checked" ).val() == 1) {
//                    window.location.href = '#/searchVideo/' + searchText + '/' + 1;
//                    // }
//                }
//            });

            // Toggle user information
            $scope.isShowHideUserInfo = true;
            $scope.toggleUserInfo = function () {
                $scope.isShowHideUserInfo = $scope.isShowHideUserInfo ? false : true;
                isToggleUserInfo = true;
            }

            $scope.showUpload = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'src/app/mentors/video/upload_tutorial_popup.tpl.html',
                    controller: 'UploadTutorialController',
                    resolve: {
                        u_id: function () {
                            return userId;
                        },
                        v_id: function () {
                            return -1;
                        }
                    }
                });
            }
            /**
             * Show small left side bar
             */
            $scope.showSmallLeftSideBar = function showSmallLeftSideBar() {
                $rootScope.isMiniMenu = !$rootScope.isMiniMenu;
                if($rootScope.isMiniMenu && !$rootScope.isMiniSideRightBar){
                    angular.element('#sidebar-right').removeClass('showsidebar');
                    $rootScope.isMiniSideRightBar = true;
                }
                if ($rootScope.isMiniMenu) {
                    angular.element('.menuSubmenu').removeClass('show');
                } else {
                    angular.element('.menuSubmenu').addClass('show');
                }
            };

            /**
             *  Show hide when toggle element and hide all element when click any where
             */
            angular.element($document).on('click', function (el) {
                var elem = $(el.target).closest('.notification'),
                    userLogin = $(el.target).closest('.profile-user'),
                    boxUserinfo = $(el.target).closest('#user-info'),
                    box = $(el.target).closest('.notification-content'),
                    navbarToggle = $(el.target).closest('.navbar-toggle'),
                    mobimenu = $(el.target).closest('.mobimenu');

                if (elem.length) {
                    el.preventDefault();
                    $('.notification-content').toggle();
                } else if (!box.length) {
                    $('.notification-content').hide();
                }
                //
                if (userLogin.length) {
                    el.preventDefault();
                    $('#user-info').toggle();
                } else if (!boxUserinfo.length) {
                    $('#user-info').hide();
                }
                //
                if (navbarToggle.length) {
                    el.preventDefault();
                    if ($('.mobimenu').hasClass('in')) {
                        $('.mobimenu').addClass('in');
                    } else {
                        $('.mobimenu').removeClass('in');
                    }
                } else if (!boxUserinfo.length) {
                    $('.mobimenu').removeClass('in');
                }
            });

            function checkMenuType() {
                var width = $window.innerWidth;
                return width < 1281;
            }

        }]);

//=========================================== HEADER.CONTROLLER.JS==============