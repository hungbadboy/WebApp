//=========================================== NOTIFICATION.SERVICE.JS==============
brotServices.factory('NotificationService', ['$http', function($http) {
  var factory = {};

  factory.getNotificationByUserId = function(userId) {
    var promise = $http({
      method: 'GET',
      url: NEW_SERVICE_URL + 'notification/getNotificationNotReaded?uid='+userId
    });
    return promise;
  };

  factory.updateNotification = function(nid) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'notification/updateStatusNotification',
      data: {
        "request_data_type": "notification",
        "request_data_method": "updateStatusNotification",
        "request_data": {
          "nid": nid,
          "status": "Y"
        }
      }
    });
    return promise;
  };

  factory.getAllNotification = function(userId, pageno, limit) {
    var promise = $http({
      method: 'GET',
      url: NEW_SERVICE_URL + 'notification/getAllNotification?uid='+userId+'&pageno='+pageno+'&limit='+limit
    });
    return promise;
  };

  factory.updateAllNotification = function(userid) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'notification/updateStatusAllNotification',
      data: {
        "request_data_type": "notification",
        "request_data_method": "updateStatusAllNotification",
        "request_data": {
          "uid": userid
        }
      }
    });
    return promise;
  };

  factory.getNotificationReaded = function(userId) {
    var promise = $http({
      method: 'GET',
      url: NEW_SERVICE_URL + 'notification/getNotificationReaded?uid='+userId
    });
    return promise;
  };

  return factory;
}]);
//=========================================== NOTIFICATION.SERVICE.JS==============