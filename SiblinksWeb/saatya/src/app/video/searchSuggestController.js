/**
 * Created by PC on 01/12/2016.
 */
brotControllers.controller('SearchSuggestController', ['$scope', '$rootScope', '$http', '$timeout', '$window', 'VideoService', function ($scope, $rootScope, $http, $timeout, $window, VideoService) {

    init();

    function init(){
        getAllTitleVideoPlaylist();
    }

    function getAllTitleVideoPlaylist() {
        VideoService.getAllTitleVideoPlaylist().then(function (response) {
            if (response.data.status == "true" && response.data.request_data_result != StatusError.MSG_DATA_NOT_FOUND) {
                $scope.listTitlesVideo = response.data.request_data_result;
            } else {
                $scope.listTitlesVideo = null;
            }
        })
    }


    $scope.onSelectKeyword = function (keyword) {
        var searchValue = keyword.title;
        if(!isEmpty(searchValue)){
            searchValue = searchValue.trim();
        }
        $window.location.href = '#/videos?search='+encodeURIComponent(searchValue);
    };

}]);