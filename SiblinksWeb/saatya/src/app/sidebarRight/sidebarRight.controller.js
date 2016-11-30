/**
 * Created by Tavv on 28/09/2016.
 */
brotControllers.controller('SideBarRightController', ['$scope', '$http', 'MentorService', 'VideoService', 'myCache', 'SideBarRightService', '$sce', '$rootScope',
    function ($scope, $http, MentorService, VideoService, myCache, SideBarRightService, $sce, $rootScope) {

        var userId = localStorage.getItem('userId');
        //data test
        $scope.defaultLimit = 30;
        var defaultOffset = 0;

        var BASE_URL = {
            QUESTION: '#/mentor/managerQA?pid=',
            VIDEO: '#/mentor/video/detail/',
            ESSAY: '#/mentor/essay?eid={0}&t={1}',
            PLAYLIST: '#/mentor/playlist/detail/',
            PROFILE: '#/mentor/mentorProfile'
        };

        var ACTIVITY_ACTION = {
            CREATE: 'C',
            UPDATE: 'U',
            DELETE: 'D',
            READ: 'R'
        };

        var ACTIVITY_TYPE = {
            Q_AND_A: '1',
            VIDEO: '2',
            ESSAY: '3',
            ADMISSION: '4',
            ARTICLE: '5',
            PLAYLIST: '6',
            PROFILE: '7'
        };

        var KEY_WORD_LINK = {
            Question: 'question',
            Video: 'video',
            Playlist: 'playlist',
            Essay: 'essay',
            Profile: 'profile',
            Avatar: 'avatar'
        };

        var KEY_WORD_ESSAY = {
            processing: 'processing',
            ignored: 'ignored',
            replied: 'replied'
        };

        var isHasLoadMore = false;


        var listActivityLogSize = 0;

        
        init();

        function init() {
            getActivityLogByUser(userId, $scope.defaultLimit, defaultOffset);
        }


        function getActivityLogByUser(userId, limit, offset) {
            if (!userId) {
                return;
            }
            SideBarRightService.getActivityLogById(userId, limit, offset).then(function (data) {
                if (data.data) {
                    $scope.listActivitiesLog = isHasLoadMore ? $scope.listActivitiesLog.concat(data.data) : data.data;
                    listActivityLogSize = $scope.listActivitiesLog.length;
                }
            });
        }


        /**
         * @param time is TIME_STAMP
         * @returns {*} time ago
         */
        $scope.convertUnixTimeToTime = function (time) {
            return convertUnixTimeToTime(time);
        };

        /**
         * @param action in {@enum ACTIVITY_ACTION}
         * @param type in {@enum ACTIVITY_TYPE}
         * @returns {boolean} true or false
         */
        $scope.isEmptyLink = function (action, type) {
            if (isEmpty(action) || isEmpty(type)) {
                return true;
            }
            return action == ACTIVITY_ACTION.DELETE || type == ACTIVITY_TYPE.ARTICLE || type == ACTIVITY_TYPE.ADMISSION;
        };


        /**
         * Prototype format String
         */
        if (!String.prototype.format) {
            String.prototype.format = function() {
                var args = arguments;
                return this.replace(/{(\d+)}/g, function(match, number) {
                    return typeof args[number] != 'undefined'
                        ? args[number]
                        : match
                        ;
                });
            };
        }

        /**
         * @param type in {@enum ACTIVITY_TYPE}
         * @param id
         * @returns {*} link
         */
        $scope.getLink = function (type, strLog, id) {
            if (isEmpty(id)) {
                if (type == ACTIVITY_TYPE.PROFILE) {
                    return BASE_URL.PROFILE;
                }
                return '';
            }
            switch (type) {
                case ACTIVITY_TYPE.Q_AND_A:
                    return BASE_URL.QUESTION + id;
                case ACTIVITY_TYPE.VIDEO:
                    return BASE_URL.VIDEO + id;
                case ACTIVITY_TYPE.ESSAY:
                    return validateLinkEssay(id, strLog);
                case ACTIVITY_TYPE.PLAYLIST:
                    return BASE_URL.PLAYLIST + id;
            }
        };

        function validateLinkEssay(id, strLog) {
            if(strLog.indexOf(KEY_WORD_ESSAY.processing) != -1){
                return BASE_URL.ESSAY.format(id, 2);
            }else if(strLog.indexOf(KEY_WORD_ESSAY.replied) != -1){
                return BASE_URL.ESSAY.format(id, 4);
            }else if(strLog.indexOf(KEY_WORD_ESSAY.ignored) != -1){
                return BASE_URL.ESSAY.format(id, 3);
            }else{
                return BASE_URL.ESSAY.format(id, 1);
            }
        }


        var currentPageLoad = 0;
        $scope.loadMoreActivity = function () {
            currentPageLoad++;
            var newOffset = $scope.defaultLimit * currentPageLoad;
            if (newOffset > listActivityLogSize) {
                return;
            }
            isHasLoadMore = true;
            if (userId) {
                getActivityLogByUser(userId, $scope.defaultLimit, newOffset);
            }
        };


        // $scope.validateLogContent = function (strLog) {
        //     if (strLog) {
        //         if(isLogContainsKeyWord(strLog)){
        //             var test = strLog.indexOfRegex(regexSearchKey);
        //             console.log(test);
        //         }
        //     }
        // };


        // function isLogContainsKeyWord(strLog){
        //     return strLog.contains(KEY_WORD_LINK.Question) || strLog.contains(KEY_WORD_LINK.Video)
        //         || strLog.contains(KEY_WORD_LINK.Playlist) || strLog.contains(KEY_WORD_LINK.Essay)
        //         || strLog.contains(KEY_WORD_LINK.Profile);
        // }


        $scope.validateLogContent = function (strLog, action, type, id) {
            if (action == ACTIVITY_ACTION.DELETE) {
                return $sce.trustAsHtml(strLog);
            }
            var absHref = $scope.getLink(type, strLog, id);
            if (!isEmpty(strLog)) {
                if (strLog.indexOf(KEY_WORD_LINK.Question) != -1) {
                    return $sce.trustAsHtml(strLog.replace(KEY_WORD_LINK.Question, '<a class = "text-under-line" href=' + absHref + '>question</a>'));
                } else if (strLog.indexOf(KEY_WORD_LINK.Video) != -1) {
                    return $sce.trustAsHtml(strLog.replace(KEY_WORD_LINK.Video, '<a class = "text-under-line" href=' + absHref + '>video</a>'));
                } else if (strLog.indexOf(KEY_WORD_LINK.Essay) != -1) {
                    return $sce.trustAsHtml(strLog.replace(KEY_WORD_LINK.Essay, '<a class = "text-under-line" href=' + absHref + '>essay</a>'));
                } else if (strLog.indexOf(KEY_WORD_LINK.Playlist) != -1) {
                    return $sce.trustAsHtml(strLog.replace(KEY_WORD_LINK.Playlist, '<a class = "text-under-line" href=' + absHref + '>playlist</a>'));
                } else if (strLog.indexOf(KEY_WORD_LINK.Profile) != -1) {
                    return $sce.trustAsHtml(strLog.replace(KEY_WORD_LINK.Profile, '<a class = "text-under-line" href=' + absHref + '>profile</a>'));
                } else if (strLog.indexOf(KEY_WORD_LINK.Avatar) != -1) {
                    return $sce.trustAsHtml(strLog.replace(KEY_WORD_LINK.Avatar, '<a class = "text-under-line" href=' + absHref + '>avatar</a>'));
                }

            }
        };
        
        
        $scope.toggleSideRightBar = function shotoggleSideRightBar() {
        	$rootScope.isMiniSideRightBar = !$rootScope.isMiniSideRightBar;
            var isShowMenuLeft = angular.element('#sidebar-menu').hasClass('in');

            if(isShowMenuLeft){
                angular.element('#sidebar-menu').removeClass('in');
                $rootScope.isMiniMenu = false;
            }

        	if($rootScope.isMiniSideRightBar) {
    			angular.element('#sidebar-right').removeClass('showsidebar');
        	} else {
        		angular.element('#sidebar-right').addClass('showsidebar');
        	}
        };


    }]);