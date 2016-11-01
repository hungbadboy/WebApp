/**
 * Created by Tavv on 28/09/2016.
 */
brotServices.factory('SideBarRightService', ['$http', '$log', function($http) {
    var factory = {};
    factory.getStudentActivity = function (mentorId, limit, offset) {
        var rs;
        var promise = $http({
            method:'GET',
            url : NEW_SERVICE_URL+'mentor/getActivityStudent?mentorId='+mentorId+'&limit='+limit+'&offset='+offset
        }).success(function (response) {
            rs = response;
            return rs;
        });
        return promise;
    };

    factory.getActivityLogById = function (userId, limit, offset) {
        return $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'activityLogService/getActivityByUserId?userId=' + userId+'&limit='+limit+'&offset='+offset
        });
    };
    return factory;
}]);