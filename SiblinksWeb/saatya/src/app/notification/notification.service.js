//=========================================== NOTIFICATION.SERVICE.JS==============
brotServices.factory('NotificationService', ['$http', function($http) {
  var factory = {};

  factory.getNotificationByUserId = function(userId) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'notification/getNotificationNotReaded',
      data: {
        "request_data_type": "notification",
        "request_data_method": "getNotificationNotReaded",
        "request_data": {
          "uid": userId,
          "status": "N"
        }
      }
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

  factory.getAllNotification = function(userId, pageno) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'notification/getAllNotification',
      data: {
        "request_data_type": "notification",
        "request_data_method": "getAllNotification",
        "request_data": {
          "uid": userId,
          "pageno": pageno,
          "limit": 7
        }
      }
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
      method: 'POST',
      url: NEW_SERVICE_URL + 'notification/getNotificationReaded',
      data: {
        "request_data_type": "notification",
        "request_data_method": "getNotificationReaded",
        "request_data": {
          "uid": userId
        }
      }
    });
    return promise;
  };

  return factory;
}]);
//=========================================== NOTIFICATION.SERVICE.JS==============