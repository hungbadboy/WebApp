brotServices.factory('CommentService', ['$http', function($http) {
  var factory = {};

  factory.uploadImage = function(callback) {
    $.ajax({
      url: NEW_SERVICE_URL + 'comments/uploadFile',
      type: 'POST',
      data: new FormData($("#upload-file-form")[0]),
      enctype: 'multipart/form-data',
      processData: false,
      contentType: false,
      cache: false,
      success: function(data) {
        callback(data);
      }
    });
  };
  

  factory.uploadImageEdit = function(callback) {
    $.ajax({
      url: NEW_SERVICE_URL + 'comments/uploadFile',
      type: 'POST',
      data: new FormData($("#upload-file-form-edit")[0]),
      enctype: 'multipart/form-data',
      processData: false,
      contentType: false,
      cache: false,
      success: function(data) {
        callback(data);
      }
    });
  };

  factory.deleteComment = function(cid) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'comments/deleteComment',
      data: {
        "request_data_type": "comments",
        "request_data_method": "deleteComment",
        "request_data": {
          "cid": cid
        }
      }
    });
    return promise;
  };

  factory.editComment = function(cid, content) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'comments/update',
      data: {
        "request_data_type": "comments",
        "request_data_method": "update",
        "request_data": {
          "cid": cid,
          "content": encodeURIComponent(content)
        }
      }
    });
    return promise;
  };

  factory.addCommentEssay = function(userId, content, essayId) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'comments/addCommentEssay',
      data: {
        "request_data_type":"comments",
        "request_data_method":"addCommentEssay",
        "request_data": {
          "authorID": userId,
          "content": encodeURIComponent(content),
          "essayId": essayId
        }
      }
    });
    return promise;
  };

  return factory;
}]);