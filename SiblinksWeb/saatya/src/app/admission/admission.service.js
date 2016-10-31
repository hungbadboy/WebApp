//=========================================== ADMINSSION.SERVICES.JS==============
brotServices.factory('AdmissionService', ['$http', function($http) {
  var factory = {};

  /* College Admission begin */
  factory.getAdmission = function () {
      var promise = $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'admission/getAdmission'
      }).success(function (data) {
          return data.response;
      });
      return promise;
  };
  
  factory.getVideoTuttorialAdmission = function (idAdmission) {
      var promise = $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'videoAdmission/getVideoTuttorialAdmission?idAdmission=' + idAdmission
      }).success(function (data) {
          return data.response;
      });
      return promise;
  };
  
  factory.getArticleAdmission = function (idAdmission) {
      var promise = $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'article/getArticleAdmission?idAdmission=' + idAdmission
      }).success(function (data) {
          return data.response;
      });
      return promise;
  };
  /* College Admission end */
  
  /*
  factory.getAdmission = function() {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'admission/getAdmission',
      data: {
        "request_data_type": "admission",
        "request_data_method": "getAdmission",
        "request_data": {

        }
      }
    });
    return promise;
  };

  factory.getSubAdmission = function(idAdmission) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'admission/getSubAdmission',
      data: {
        "request_data_type": "admission",
        "request_data_method": "getSubAdmission",
        "request_data_admission": {
          "idAdmission": idAdmission
        }
      }
    });
    return promise;
  };

  factory.getTopicSubAdmission = function(idSubAdmission) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'admission/getTopicSubAdmission',
      data: {
        "request_data_type": "admission",
        "request_data_method": "getTopicSubAdmission",
        "request_data_admission": {
          "idSubAdmission": idSubAdmission
        }
      }
    });
    return promise;
  };

  factory.getArticles = function(idAdmission, pageno, limit) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'article/getArticles',
      data: {
        "request_data_type": "article",
        "request_data_method": "getArticles",
        "request_data_article": {
          "idAdmission": idAdmission,
          "pageno": pageno,
          "limit": limit
        }
      }
    });
    return promise;
  };

  factory.getArticleDetail = function(arId) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'article/getArticleDetail',
      data: {
        "request_data_type": "article",
        "request_data_method": "getArticleDetail",
        "request_data_article": {
          "arId": arId
        }
      }
    });
    return promise;
  };

  factory.addCommentArticle = function(userId, content, arId) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'comments/addCommentArticle',
      data: {
        "request_data_type":"comments",
        "request_data_method":"addCommentArticle",
        "request_data_article": {
          "authorId": userId,
          "content": encodeURIComponent(content),
          "arId": arId
        }
      }
    });
    return promise;
  };

  factory.getDiscussesOnArticle = function(arId, pageno, limit) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'article/getArticleCommentsPN',
      data: {
        "request_data_type": "article",
        "request_data_method": "getArticleCommentsPN",
        "request_data_article": {
          "arId": arId,
          "pageno": pageno,
          "limit": limit
        }
      }
    });
    return promise;
  };

  factory.likeCommentArticle = function(userId, cid) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'like/likeCommentArticle',
      data: {
        "request_data_type": "like",
        "request_data_method": "likeCommentArticle",
        "request_data_article": {
          "authorId": userId,
          "cid": cid
        }
      }
    });
    return promise;
  };

  factory.getVideoTopicSubAdmissionPN = function(idTopicSubAdmission, pageno, limit) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'videoAdmission/getVideoTopicSubAdmissionPN',
      data: {
        "request_data_type": "videoAdmission",
        "request_data_method": "getVideoTopicSubAdmissionPN",
        "request_data_videoAdmission": {
          "idTopicSubAdmission": idTopicSubAdmission,
          "pageno": pageno,
          "limit": limit
        }
      }
    });
    return promise;
  };

  factory.getVideoAdmissionDetail = function(idSubAdmission, idTopicSubAdmission, vId) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'videoAdmission/getVideoAdmissionDetail',
      data: {
        "request_data_type": "videoAdmission",
        "request_data_method": "getVideoAdmissionDetail",
        "request_data_videoAdmission": {
          "idSubAdmission": idSubAdmission,
          "idTopicSubAdmission": idTopicSubAdmission,
          "vId": vId
        }
      }
    });
    return promise;
  };

  factory.getVideoTopicSubAdmission = function(idTopicSubAdmission) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'videoAdmission/getVideoTopicSubAdmission',
      data: {
        "request_data_type": "videoAdmission",
        "request_data_method": "getVideoTopicSubAdmission",
        "request_data_videoAdmission": {
          "idTopicSubAdmission": idTopicSubAdmission
        }
      }
    });
    return promise;
  };

  factory.addCommentVideoAdmission = function(userId, content, vId) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'comments/addCommentVideoAdmission',
      data: {
        "request_data_type":"comments",
        "request_data_method":"addCommentVideoAdmission",
        "request_data_videoAdmission": {
          "authorId": userId,
          "content": encodeURIComponent(content),
          "vId": vId
        }
      }
    });
    return promise;
  };

  factory.getVideoAdmissionComment = function(vId, pageno, limit) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'videoAdmission/getVideoAdmissionCommentsPN',
      data: {
        "request_data_type": "videoAdmission",
        "request_data_method": "getVideoAdmissionCommentsPN",
        "request_data_videoAdmission": {
          "vId": vId,
          "pageno": pageno,
          "limit": limit
        }
      }
    });
    return promise;
  };

  factory.likeCommentVideoAdmission = function(userId, cid) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'like/likeCommentVideoAdmission',
      data: {
        "request_data_type": "like",
        "request_data_method": "likeCommentVideoAdmission",
        "request_data_videoAdmission": {
          "authorId": userId,
          "cid": cid
        }
      }
    });
    return promise;
  };

  factory.orderVideoAdmissionPN = function(idTopicSubAdmission, pageno, limit, order) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'videoAdmission/orderVideoAdmissionPN',
      data: {
        "request_data_type": "videoAdmission",
        "request_data_method": "orderVideoAdmissionPN",
        "request_data_videoAdmission": {
          "idTopicSubAdmission": idTopicSubAdmission,
          "pageno": pageno,
          "limit": limit,
          "order": order
        }
      }
    });
    return promise;
  };

  factory.updateViewVideoAdmission = function(vId) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'videoAdmission/updateViewVideoAdmission',
      data: {
        "request_data_type": "videoAdmission",
        "request_data_method": "updateViewVideoAdmission",
        "request_data_videoAdmission": {
          "vId": vId
        }
      }
    });
    return promise;
  };

  factory.uploadImage = function(callback) {
    $.ajax({
      url: NEW_SERVICE_URL + 'article/uploadFile',
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

  factory.createArticle = function(title, image, description, content) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'article/createArticle',
      data: {
        "request_data_type": "article",
        "request_data_method": "createArticle",
        "request_data_article": {
          "title": title,
          "image": image,
          "description": description,
          "content": encodeURIComponent(content)
        }
      }
    });
    return promise;
  };

  factory.getImageArticle = function(arId) {
    return NEW_SERVICE_URL + 'article/getImageArticle/' + arId;
  };

  factory.getImageVideoAdmission = function(vId) {
    return NEW_SERVICE_URL + 'videoAdmission/getImageVideoAdmission/' + vId;
  };

  factory.updateVideoAdmissionWatched = function(uid, vid) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'videoAdmission/updateVideoAdmissionWatched',
      data: {
        "request_data_type": "videoAdmission",
        "request_data_method": "updateVideoAdmissionWatched",
        "request_data_videoAdmission": {
          "uid": uid,
          "vId": vid
        }
      }
    });
    return promise;
  };

  factory.getIdVideoAdmissionWatched = function(uid) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'videoAdmission/getIdVideoAdmissionWatched',
      data: {
        "request_data_type": "videoAdmission",
        "request_data_method": "getIdVideoAdmissionWatched",
        "request_data_videoAdmission": {
          "uid": uid
        }
      }
    });
    return promise;
  };

  factory.rateVideoAdmission = function(uid, vid, rate) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'videoAdmission/rateVideoAdmission',
      data: {
        "request_data_type": "videoAdmission",
        "request_data_method": "rateVideoAdmission",
        "request_data_videoAdmission": {
          "uid": uid,
          "vId": vid,
          "rating": rate
        }
      }
    });
    return promise;
  };

  factory.getRatingVideoAdmission = function(vid) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'videoAdmission/getRatingVideoAdmission',
      data: {
        "request_data_type": "videoAdmission",
        "request_data_method": "getRatingVideoAdmission",
        "request_data_videoAdmission": {
          "vId": vid
        }
      }
    });
    return promise;
  };

  factory.checkUserRatingVideoAdmission = function(uid, vid) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'videoAdmission/checkUserRatingVideoAdmission',
      data: {
        "request_data_type": "videoAdmission",
        "request_data_method": "checkUserRatingVideoAdmission",
        "request_data_videoAdmission": {
          "uid": uid,
          "vId": vid
        }
      }
    });
    return promise;
  };*/
  
  factory.updateViewArticalAdmission = function(articalId) {
	    var promise = $http({
	      method: 'POST',
	      url: NEW_SERVICE_URL + 'article/updateViewArticle',
	      data: {"arId": articalId}
	    });
	    return promise;
	  };
  return factory;
}]);
//===========================================END ADMINSSION.SERVICES.JS==============