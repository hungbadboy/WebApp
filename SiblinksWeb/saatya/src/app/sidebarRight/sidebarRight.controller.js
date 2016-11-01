/**
 * Created by Tavv on 28/09/2016.
 */
brotControllers.controller('SideBarRightController', ['$scope', '$http', 'MentorService', 'VideoService', 'myCache', 'SideBarRightService','$sce',
    function ($scope, $http, MentorService, VideoService, myCache, SideBarRightService, $sce) {

        var userId = localStorage.getItem('userId');
        //data test
        $scope.defaultLimit = 30;
        var defaultOffset = 0;

        var BASE_URL = {
            URL_QUESTION: '#/mentor/managerQA?pid=',
            URL_VIDEO: '#/mentor/video/detail/',
            URL_ESSAY: '#/mentor/essay?eid=',
            URL_PLAYLIST: '#/mentor/playlist/detail/',
            URL_PROFILE: '#/mentor/mentorProfile'
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
            Video : 'video',
            Playlist : 'playlist',
            Essay : 'essay',
            Profile : 'profile'
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
         * @param type in {@enum ACTIVITY_TYPE}
         * @param id
         * @returns {*} link
         */
        $scope.getLink = function (type, id) {
            if (isEmpty(id)) {
                if(type == ACTIVITY_TYPE.PROFILE){
                    return BASE_URL.URL_PROFILE;
                }
                return '';
            }
            switch (type) {
                case ACTIVITY_TYPE.Q_AND_A:
                    return BASE_URL.URL_QUESTION + id;
                case ACTIVITY_TYPE.VIDEO:
                    return BASE_URL.URL_VIDEO + id;
                case ACTIVITY_TYPE.ESSAY:
                    return BASE_URL.URL_ESSAY + id;
                case ACTIVITY_TYPE.PLAYLIST:
                    return BASE_URL.URL_PLAYLIST + id;
            }
        };


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
            if(action == ACTIVITY_ACTION.DELETE){
                return $sce.trustAsHtml(strLog);
            }
            var absHref = $scope.getLink(type, id);
            if(!isEmpty(strLog)) {
                if (strLog.indexOf(KEY_WORD_LINK.Question) != -1) {
                    return $sce.trustAsHtml(strLog.replace('question', '<a class = "text-under-line" href='+absHref+'>question</a>'));
                } else if (strLog.indexOf(KEY_WORD_LINK.Video) != -1) {
                    return $sce.trustAsHtml(strLog.replace('video', '<a class = "text-under-line" href='+absHref+'>video</a>'));
                }else if (strLog.indexOf(KEY_WORD_LINK.Essay) != -1) {
                    return $sce.trustAsHtml(strLog.replace('essay', '<a class = "text-under-line" href='+absHref+'>essay</a>'));
                }else if (strLog.indexOf(KEY_WORD_LINK.Playlist) != -1 ) {
                    return $sce.trustAsHtml(strLog.replace('playlist', '<a class = "text-under-line" href='+absHref+'>playlist</a>'));
                }else if (strLog.indexOf(KEY_WORD_LINK.Profile) != -1) {
                    return $sce.trustAsHtml(strLog.replace('profile', '<a class = "text-under-line" href=' + absHref + '>profile</a>'));
                }

            }
        }


    }]);