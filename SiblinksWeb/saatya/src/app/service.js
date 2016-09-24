var brotServices = angular.module('brotServices', ['ngResource']);

function loginFBService(callback) {
    FB.init({
        appId: APPFBID,
        status: true, // check login status
        cookie: true, // enable cookies to allow the server to access the session
        xfbml: true  // parse XFBML
    });

    FB.login(function (response) {
        if (response.authResponse) {
            FB.api('/me', {fields: 'email, last_name, first_name, picture'}, function (response) {
                callback(response);
            });
        }
    }, {
        scope: 'public_profile, email'
    });
}

function showPage(numberPage, page, callback) {
    var listPage = [];
    numberPage = parseInt(numberPage, 10);
    page = parseInt(page, 10);
    var min = (page - 3 < 1) ? 1 : page - 3;
    var max = (page + 3 > numberPage) ? numberPage : page + 3;
    for (var i = min; i <= max; i++) {
        listPage.push(i);
    }
    callback(listPage);
}

function revertCKeditor(showCKEditor) {
    if (showCKEditor) {
        $('.content .discus .boxCkComment').toggle('slow');
        showCKEditor = false;
    }
    CKEDITOR.instances["txtDiscus"].setData('');
    $('.content .discus .ckComment').addClass('hide');
    $('.content .discus .go').removeClass('hide');
    $('.content .discus .btnHide').addClass('hide');
}

function revertCKeditorArticleDetail(showCKEditor) {
    if (showCKEditor) {
        $('.bottom_detailArticle .discussion .boxCkComment').toggle('slow');
        showCKEditor = false;
    }
    CKEDITOR.instances["txtDiscus"].setData('');
    $('.bottom_detailArticle .discussion .ckComment').addClass('hide');
    $('.bottom_detailArticle .discussion .go').removeClass('hide');
    $('.bottom_detailArticle .discussion .btnHide').addClass('hide');
}

function revertCKeditorEssayDetail(showCKEditor) {
    if (showCKEditor) {
        $('#essay_detail .discussion .boxCkComment').toggle('slow');
        showCKEditor = false;
    }
    CKEDITOR.instances["txtDiscus"].setData('');
    $('#essay_detail .discussion .ckComment').addClass('hide');
    $('#essay_detail .discussion .go').removeClass('hide');
    $('#essay_detail .discussion .btnHide').addClass('hide');
}

function showRating(rating, callback) {
    var rate = 0;
    if (rating != null && rating !== '') {
        rate = Math.floor(rating);
    }

    var unrate = 5 - rate;
    var arr_rate = [];
    for (var j = 0; j < rate; j++) {
        arr_rate.push('assets/img/yellow _star.png');
    }
    for (var k = 0; k < unrate; k++) {
        arr_rate.push('assets/img/grey_star.png');
    }
    callback(arr_rate);
}

function showClickRating(callback) {
    var arr_rate = [];
    for (var i = 0; i < 5; i++) {
        arr_rate.push('assets/img/grey_star.png');
    }
    callback(arr_rate);
}

brotServices.factory('HomeService', ['$http', function ($http) {
    var factory = {};

    factory.postQuestion = function (userId, subjectId, content) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'post/createPost',
            data: {
                "request_data_type": "post",
                "request_data_method": "createPost",
                "request_data": {
                    "authorID": userId,
                    "subjectId": subjectId,
                    "content": content
                }
            }
        });
        return promise;
    };

    factory.getSubjectIdWithTopicId = function (topicId) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'post/getSubjectIdWithTopicId',
            data: {
                "request_data_type": "post",
                "request_data_method": "getSubjectIdWithTopicId",
                "request_data": {
                    "topicId": topicId
                }
            }
        });
        return promise;
    };

    factory.getSubjectsWithTag = function () {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'subjects/listOfSubjectsWithTag',
            data: {
                "request_data_type": "subjects",
                "request_data_method": "listOfSubjectsWithTag",
                "request_data": {}
            }
        });
        return promise;
    };

    factory.getPolicy = function () {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/getPolicy',
            data: {
                "request_data_type": "user",
                "request_data_method": "getPolicy",
                "request_data": {}
            }
        });
        return promise;
    };

    factory.getTerms = function () {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/getTerms',
            data: {
                "request_data_type": "user",
                "request_data_method": "getTerms",
                "request_data": {}
            }
        });
        return promise;
    };

    return factory;
}]);

brotServices.factory('SubjectServices', ['$http', function ($http) {
    var factory = {};
    var subjects = [];
    factory.getSubjects = function () {
        return $http.post(NEW_SERVICE_URL + 'subjects/listOfSubjects', {
            "request_data_type": "subjects",
            "request_data_method": "listofSubjects",
            "request_data": {}
        }).success(function (data) {
            for (var i = 0; i < data.request_data_result.length; i++) {
                subjects.push(data.request_data_result[i]);
            }
        });
    };

    factory.getSubjectById = function (id) {
        var subject;

        var promise = factory.getSubjects().then(function (json) {
            var obj = json.data.request_data_result;
            for (var i = 0; i < obj.length; i++) {
                if (obj[i].subject_id == id) {
                    subject = obj[i];
                    return subject;
                }
            }
        });
        return promise;
    };
    return factory;
}]);

brotServices.factory('CategoriesService', ['$http', function ($http) {
    var factory = {};
    factory.getCategories = function (subjectId) {
        var categories;
        var pathWS = NEW_SERVICE_URL + 'subjects/listOfTopics?subject=' + subjectId;
        return $http.post(pathWS, {
            "request_data_type": "subjects",
            "request_data_method": "listofTopics",
            "request_data": {
                "subjectId": subjectId

            }
        }).success(function (data) {
            categories = data.request_data_result;
        });
    };

    factory.getCategoriById = function (categoryId) {
        var promise = $http({
            method: 'GET',
            url: 'json/category_id.json'
        }).success(function (json) {
            return json.response;
        });
        return promise;
    };

    return factory;
}]);

brotServices.factory('AnswerService', ['$http', '$log', function ($http) {
    var factory = {};

    factory.postAnswer = function (userId, questionId, content) {
        var rs;
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'post/createAnswer',
            data: {
                "request_data_type": "post",
                "request_data_method": "createAnswer",
                "request_data": {
                    "authorID": userId,
                    "pid": questionId,
                    "content": encodeURIComponent(content),
                    "tags": []
                }
            }
        });
        return promise;
    };

    factory.getAnswersByQuestion = function (question_id, page, limit) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'post/getAnswerListPN',
            data: {
                "request_data_type": "post",
                "request_data_method": "getAnswerListPN",
                "request_data": {
                    "pid": question_id,
                    "pageno": page,
                    "limit": limit
                }
            }
        });
        return promise;
    };

    factory.likeAnswer = function (userId, answer_id) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'like/likeAnswer',
            data: {
                "request_data_type": "like",
                "request_data_method": "likeAnswer",
                "request_data": {
                    "authorID": userId,
                    "aid": answer_id
                }
            }
        });
        return promise;
    };

    factory.editAnswer = function (aid, content) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'answer/editAnswer',
            data: {
                "request_data_type": "answer",
                "request_data_method": "editAnswer",
                "request_data": {
                    "aid": aid,
                    "content": encodeURIComponent(content)
                }
            }
        });
        return promise;
    };

    return factory;
}]);

//brotServices.factory('QuestionsService', ['$http','$log', function($http, $log) {
//  var factory = {};
//
//  factory.getQuestionSubjectWithPN = function(userId, subjectId, page, limit) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'post/getPostListSubjectPN',
//      data: {
//        "request_data_type": "post",
//        "request_data_method": "getPostListSubjectPN",
//        "request_data": {
//          "uid": userId,
//          "subjectId": subjectId,
//          "pageno": page,
//          "limit": limit
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.getQuestionWithPN = function(userId, subjectId, topicId, page, limit) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'post/getPostListPN',
//      async: false,
//      data: {
//        "request_data_type": "post",
//        "request_data_method": "getPostListPN",
//        "request_data": {
//          "uid": userId,
//          "subjectId": subjectId,
//          "topicId": topicId,
//          "pageno": page,
//          "limit": limit
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.getRelatedQuestionWithPN = function(userId, subjectId, topicId, pid, page, limit) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'post/getPostListRelatedPN',
//      data: {
//        "request_data_type": "post",
//        "request_data_method": "getPostListRelatedPN",
//        "request_data": {
//          "uid": userId,
//          "subjectId": subjectId,
//          "topicId": topicId,
//          "pid": pid,
//          "pageno": page,
//          "limit": limit
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.getQuestionById = function(question_id) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'post/getPostById',
//      data: {
//        "request_data_type": "post",
//        "request_data_method": "getPostById",
//        "request_data": {
//            "pid": question_id
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.updateViewQuestion = function(question_id, status) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'post/updateViewQuestion',
//      data: {
//        "request_data_type": "post",
//        "request_data_method": "updateViewQuestion",
//        "request_data": {
//          "pid": question_id,
//          "status": status
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.getAnswerById = function(question_id) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'post/getAnswerById',
//      data: {
//        "request_data_type": "post",
//        "request_data_method": "getAnswerById",
//        "request_data": {
//          "pid": question_id
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.likeQuestion = function(userId, questionId) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'like/likeQuestion',
//      data: {
//        "request_data_type": "like",
//        "request_data_method": "likeQuestion",
//        "request_data": {
//          "authorID": userId,
//          "pid": questionId
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.getQuestionRelated = function(subjectId, page, limit) {
//    var qa;
//    var promise = $http({
//      method: 'GET',
//      url: SERVICE_URL + '/qa/getRelatedQuestionsPn?subjectId=' + subjectId + '&qsDesc=multiply&pageno=' + page + '&limit=' + limit + '&totalCountFlag=true'
//    }).success(function(json) {
//      qa = json.response;
//      return qa;
//    });
//    return promise;
//  };
//
//  factory.followQuestion = function(user_id, question_id) {
//    var rs;
//    var promise = $http({
//      method: 'GET',
//      url: SERVICE_URL + '/qa/followQuestion?questionid=' + question_id + '&user_id=' + user_id
//    }).success(function(json) {
//      rs = json;
//      return rs;
//    });
//    return promise;
//  };
//
//  factory.searchPostWithTag = function(subjectId, content, page, limit) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'post/searchPostsWithTag',
//      data: {
//        "request_data_type":"post",
//        "request_data_method":"search_posts_with_tag",
//        "request_data": {
//          "subjectId": subjectId,
//          "tag":content,
//          "pageno": page,
//          "limit": limit
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.getPostLikeByUser = function(userId, pid) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'like/getPostLikeByUser',
//      async: false,
//      data: {
//        "request_data_type": "like",
//        "request_data_method": "getPostLikeByUser",
//        "request_data": {
//            "uid": userId
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.removePost = function(pid) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'post/removePost',
//      data: {
//        "request_data_type": "post",
//        "request_data_method": "removePost",
//        "request_data": {
//          "pid": pid
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.removeAnswer = function(aid) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'post/removeAnswer',
//      data: {
//        "request_data_type": "post",
//        "request_data_method": "removeAnswer",
//        "request_data": {
//          "aid": aid
//        }
//      }
//    });
//    return promise;
//  };
//
//  return factory;
//}]);

//brotServices.factory('NotificationService', ['$http', function($http) {
//  var factory = {};
//
//  factory.getNotificationByUserId = function(userId) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'notification/getNotificationNotReaded',
//      data: {
//        "request_data_type": "notification",
//        "request_data_method": "getNotificationNotReaded",
//        "request_data": {
//          "uid": userId,
//          "status": "N"
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.updateNotification = function(nid) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'notification/updateStatusNotification',
//      data: {
//        "request_data_type": "notification",
//        "request_data_method": "updateStatusNotification",
//        "request_data": {
//          "nid": nid,
//          "status": "Y"
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.getAllNotification = function(userId, pageno) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'notification/getAllNotification',
//      data: {
//        "request_data_type": "notification",
//        "request_data_method": "getAllNotification",
//        "request_data": {
//          "uid": userId,
//          "pageno": pageno,
//          "limit": 7
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.updateAllNotification = function(userid) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'notification/updateStatusAllNotification',
//      data: {
//        "request_data_type": "notification",
//        "request_data_method": "updateStatusAllNotification",
//        "request_data": {
//          "uid": userid
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.getNotificationReaded = function(userId) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'notification/getNotificationReaded',
//      data: {
//        "request_data_type": "notification",
//        "request_data_method": "getNotificationReaded",
//        "request_data": {
//          "uid": userId
//        }
//      }
//    });
//    return promise;
//  };
//
//  return factory;
//}]);

brotServices.factory('FaqsService', ['$http', function ($http) {
    var factory = {};

    factory.getFaqsAbout = function (limit, offest) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'faqs/fetchFaqs',
            data: {
                "request_data_type": "faqs",
                "request_data_method": "fetchFaqs",
                "request_data": {
                    "faqCategory": "About",
                    "limit": limit,
                    "page": offest
                }
            }
        });
        return promise;
    };

    factory.getFaqsHelp = function (limit, offest) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'faqs/fetchFaqs',
            data: {
                "request_data_type": "faqs",
                "request_data_method": "fetchFaqs",
                "request_data": {
                    "faqCategory": "Help",
                    "limit": limit,
                    "page": offest
                }
            }
        });
        return promise;
    };

    factory.getFaqsMentor = function (limit, offest) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'faqs/fetchFaqs',
            data: {
                "request_data_type": "faqs",
                "request_data_method": "fetchFaqs",
                "request_data": {
                    "faqCategory": "becomementor",
                    "limit": limit,
                    "page": offest
                }
            }
        });
        return promise;
    };

    factory.getTopQuestions = function (order, limit) {
        var faqs;
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'faqs/fetchFaqs/top',
            data: {
                "request_data_type": "faqs",
                "request_data_method": "TopFetchFaqs",
                "request_data": {
                    "order": order,
                    "limit": limit
                }
            }
            // method: 'GET',
            // url: '../json/top_questions_faqs.json',
            // headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function (data) {
            faqs = data.response;
            return faqs;
        });
        return promise;
    };
    return factory;
}]);

brotServices.factory('MentorService', ['$http', function ($http) {
    var factory = {};
    factory.getTopMentors = function (subjectId) {
        var mentors;
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'mentor/topMetorEachSubject',
            data: {
                "request_data_type": "mentor",
                "request_data_method": "topMetorEachSubject",
                "request_data": {
                    "subjectId": subjectId
                }
            }
        });
        return promise;
    };

    factory.getListMentors = function (name) {
        var mentors;
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + '/mentor/getList',
            data: {
                "request_data": {
                    "order": name,
                    "page": 1,
                    "limit": 30
                }
            }
        }).success(function (data) {
            mentors = data.request_data_result;
            return mentors;
        });
        return promise;
    };

    factory.searchMentors = function (key_search, a, order, page) {
        var mentors;
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'mentor/search',
            data: {
                "request_data": {
                    "fieldSearch": a,// ["firstName","lastName","school"],
                    "keySearch": key_search,
                    "pageno": page,
                    "limit": 9,
                    "order": order
                }
            }
        }).success(function (data) {
            mentors = data.request_data_result;
            return mentors;
        });
        return promise;
    };

    // factory.searchMentors = function(key) {

    //     var mentors;
    //     var promise = $http({
    //         method: 'POST',
    //         url: 'http://192.168.1.100:8181/siblinks/services' + '/mentor/search',
    //         data: {
    //             "request_data_type": "essay",
    //             "request_data_method": "searchMentors",
    //             "request_data": {
    //                 "keySearch": key_search,
    //                 "pageno": 1,
    //                 "limit": 5,
    //                 "totalCountFlag":true
    //             }
    //         }
    //     }).success(function(data) {
    //         mentors = data.request_data_result;
    //         return mentors;
    //     });
    //     return promise;
    // };
    return factory;
}]);

brotServices.factory('EssayService', ['$http', function ($http) {
    var factory = {};
    factory.getListUpdateEssay = function (userId, page) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'essay/getEssayByUserId',
            data: {
                "request_data_type": "essay",
                "request_data_method": "getEssayByUserId",
                "request_data": {
                    "uid": userId,
                    "pageno": page,
                    "limit": 5,
                    "totalCountFlag": true
                }
            }
        });
        return promise;
    };

    factory.getAllEssay = function (page, userType, userId) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'essay/getEssay',
            data: {
                "request_data_type": "essay",
                "request_data_method": "getEssay",
                "request_data": {
                    "uid": userId,
                    "pageno": page,
                    "limit": 5,
                    "usertype": userType,
                    "totalCountFlag": true
                }
            }
        });
        return promise;
    };

    factory.getEssayById = function (essayId, uid) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'essay/getEssayById',
            data: {
                "request_data_type": "essay",
                "request_data_method": "getEssayById",
                "request_data": {
                    "essayId": essayId,
                    "uid": uid
                }
            }
        });
        return promise;
    };

    factory.removeEssay = function (essayId) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'essay/removeEssay',
            data: {
                "request_data_type": "essay",
                "request_data_method": "removeEssay",
                "request_data": {
                    "essayId": essayId
                }
            }
        });
        return promise;
    };

    factory.postEssayDiscusion = function (userId, content) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'essay/postDiscussion',
            data: {
                "request_data_type": "essay",
                "request_data_method": "postDiscussion",
                "request_data": {
                    "uid": userId,
                    "message": content
                }
            }
        });
        return promise;
    };

    factory.getEssayDiscussion = function (userId, page, limit) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'essay/getDiscussion',
            data: {
                "request_data_type": "essay",
                "request_data_method": "getDiscussion",
                "request_data": {
                    "uid": userId,
                    "pageno": page,
                    "limit": limit,
                    "totalCountFlag": true
                }
            }
        });
        return promise;
    };

    factory.getDiscussesOnEssay = function (essayId, pageno, limit) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'essay/getEssayCommentsPN',
            data: {
                "request_data_type": "essay",
                "request_data_method": "getEssayCommentsPN",
                "request_data": {
                    "essayId": essayId,
                    "pageno": pageno,
                    "limit": limit
                }
            }
        });
        return promise;
    };

    factory.likeCommentEssay = function (userId, cid) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'like/likeCommentEssay',
            data: {
                "request_data_type": "like",
                "request_data_method": "likeCommentEssay",
                "request_data": {
                    "authorID": userId,
                    "cid": cid
                }
            }
        });
        return promise;
    };

    factory.getImageReviewEssay = function (eid) {
        return NEW_SERVICE_URL + 'essay/getImageUploadEssay/' + eid;
    };

    return factory;
}]);

brotServices.factory('SubCategoryVideoService', ['$http', function ($http) {
    var factory = {};
    factory.getSubCategoryVideo = function (subCategoryId) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/getVideoInfo',
            data: {
                "request_data_type": "video",
                "request_data_method": "getVideoInfo",
                "request_data": {
                    "vid": subCategoryId
                }
            }
        });
        return promise;
    };

    factory.getListSubCategoryVideo = function (subjectId, categoryId, pageno, limit, totalCountFlag) {
        var pathWS = NEW_SERVICE_URL + 'subjects/listOfSubTopicsPn';
        return $http.post(pathWS, {
            "request_data_type": "subjects",
            "request_data_method": "listofSubTopicsPn",
            "request_data": {
                "subjectId": subjectId,
                "cid": categoryId,
                "pageno": pageno,
                "limit": limit,
                "totalCountFlag": totalCountFlag
            }
        });
    };

    factory.getMentorOnVideo = function (subjectId) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/getMentorsOfVideo',
            data: {
                "request_data_type": "video",
                "request_data_method": "get_using_topic",
                "request_data": {
                    "subjectId": subjectId
                }
            }
        });
        return promise;
    };

    factory.getDiscussesOnVideo = function (video_id, pageno, limit, totalCountFlag) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/getVideoCommentsPN',
            data: {
                "request_data_type": "video",
                "request_data_method": "get_comments",
                "request_data": {
                    "vid": video_id,
                    "pageno": pageno,
                    "limit": limit,
                    "totalCountFlag": totalCountFlag
                }
            }
        });
        return promise;
    };

    factory.addComment = function (userName, userId, content, videoId) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'comments/addComment',
            data: {
                "request_data_type": "video",
                "request_data_method": "add_comment",
                "request_data": {
                    "author": userName,
                    "authorID": userId,
                    "content": encodeURIComponent(content),
                    "vid": videoId
                }
            }
        });
        return promise;
    };

    factory.likeCommentVideo = function (userId, cid) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'like/likeComment',
            data: {
                "request_data_type": "like",
                "request_data_method": "likeComment",
                "request_data": {
                    "authorID": userId,
                    "cid": cid
                }
            }
        });
        return promise;
    };

    factory.updateViewVideo = function (vid) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/updateViewVideo',
            data: {
                "request_data_type": "video",
                "request_data_method": "updateViewVideo",
                "request_data": {
                    "vid": vid
                }
            }
        });
        return promise;
    };

    factory.updateWatchedVideo = function (uid, vid) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/updateVideoWatched',
            data: {
                "request_data_type": "video",
                "request_data_method": "updateVideoWatched",
                "request_data": {
                    "uid": uid,
                    "vid": vid
                }
            }
        });
        return promise;
    };

    return factory;
}]);

brotServices.factory('StudentService', ['$http', function ($http) {
    var factory = {};

    factory.loginUser = function (userName, passWord, callback) {
        $.ajax({
            url: NEW_SERVICE_URL + 'user/loginUser',
            type: 'POST',
            dataType: 'json',
            data: {
                username: userName,
                password: passWord
            }, success: function (data) {
                callback(data);
            }
        });
    };

    factory.getListActivites = function () {
        return $http({
            method: 'GET',
            url: SERVICE_URL + '/user/extracurricularActivities',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
    };

    factory.getTutoringInterestes = function () {
        return $http({
            method: 'GET',
            url: SERVICE_URL + '/user/tutoringInterestes',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
    };

    factory.getListCurrentMentor = function (userId) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/getStudentMentors',
            data: {
                "request_data_type": "user",
                "request_data_method": "getMentorsOfVideo",
                "request_data": {
                    "uid": userId
                }
            }
        });
        return promise;
    };

    factory.getListForumPost = function (userId, page) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'post/getStudentPostsPN',
            data: {
                "request_data_type": "post",
                "request_data_method": "getStudentPosts",
                "request_data": {
                    "uid": userId,
                    "pageno": page,
                    "limit": 3,
                    "totalCountFlag": true
                }
            }
        });
        return promise;
    };

    factory.getMentorsReviewed = function (userId, limit) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/getMentorReviewsPN',
            data: {
                "request_data_type": "post",
                "request_data_method": "getMentorReviewsPN",
                "request_data": {
                    "uid": userId,
                    "pageno": 1,
                    "limit": limit,
                    "totalCountFlag": true
                }
            }
        });
        return promise;
    };

    factory.getStudentProfile = function (userid) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/getUserProfile',
            data: {
                "request_data_type": "user",
                "request_data_method": "getUserProfile",
                "request_data": {
                    "uid": userid
                }
            }
        });
        return promise;
    };

    // factory.updateUserProfile = function(email, type, firstName, lastName, imageUrl, school, currentClass, accomplishments,  subjects, helpSubjects,  bio) {
    //     if(currentClass == null) {
    //         currentClass = '';
    //     }
    //     if(accomplishments == null) {
    //         accomplishments = '';
    //     }
    //     if(school == null) {
    //         school = '';
    //     }
    //     return $http({
    //         method: 'POST',
    //         url: NEW_SERVICE_URL + 'user/updateUserProfile?username=' + email + '&type=' + type + '&firstname=' + firstName + '&lastname=' + lastName + '&imageurl=' + imageUrl + '&currentclass=' + currentClass + '&accomplishments=' + accomplishments + '&subjects=' + subjects + '&helpsubjects=' + helpSubjects + '&bio=' + bio + '&school=' + school,
    //         headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    //     });
    // };

    factory.updateUserProfileBasic = function (email, firstName, lastName, bio) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/updateUserProfileBasic',
            data: {
                "request_data_type": "user",
                "request_data_method": "updateUserProfileBasic",
                "request_data": {
                    "username": email,
                    "firstname": firstName,
                    "lastname": lastName,
                    "bio": bio
                }
            }
        });
        return promise;
    };

    factory.updateUserProfile = function (userId, email, currentClass, accomplishments, majorId, activityId, helpSubjectId) {
        if (currentClass == null) {
            currentClass = '';
        }
        if (accomplishments == null) {
            accomplishments = '';
        }
        return $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/updateUserProfile',
            data: {
                "request_data_type": "user",
                "request_data_method": "updateUserProfile",
                "request_data": {
                    "uid": userId,
                    "username": email,
                    "currentclass": currentClass,
                    "accomplishments": accomplishments,
                    "majorid": majorId,
                    "activityid": activityId,
                    "subjectId": helpSubjectId
                }
            }
        });
    };

    factory.getListMajors = function () {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/listOfMajors',
            data: {
                "request_data_type": "user",
                "request_data_method": "listOfMajors",
                "request_data": {}
            }
        });
        return promise;
    };

    factory.getListActivities = function () {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/listOfActivity',
            data: {
                "request_data_type": "user",
                "request_data_method": "listOfActivity",
                "request_data": {}
            }
        });
        return promise;
    };

    factory.getListCategory = function () {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/listCategory',
            data: {
                "request_data_type": "user",
                "request_data_method": "listCategory",
                "request_data": {}
            }
        });
        return promise;
    };

    factory.changePassword = function (email, password) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/forgotPassword',
            data: {
                "request_data_type": "user",
                "request_data_method": "forgotPassword",
                "request_data": {
                    'email': email,
                    'password': password
                }
            }
        });
        return promise;
    };

    factory.confirmToken = function (token) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/confirmToken',
            data: {
                "request_data_type": "user",
                "request_data_method": "confirmToken",
                "request_data": {
                    'email': token
                }
            }
        });
        return promise;
    };

    factory.loginFacebook = function (username, usertype, firstname, lastname, image, facebookid) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/loginFacebook',
            data: {
                "request_data_type": "user",
                "request_data_method": "loginFacebook",
                "request_data": {
                    'username': username,
                    'firstname': firstname,
                    'lastname': lastname,
                    'usertype': usertype,
                    'image': image,
                    'facebookid': facebookid
                }
            }
        });
        return promise;
    };

    factory.loginGoogle = function (username, usertype, firstname, lastname, image, googleid) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/loginGoogle',
            data: {
                "request_data_type": "user",
                "request_data_method": "loginGoogle",
                "request_data": {
                    'username': username,
                    'firstname': firstname,
                    'lastname': lastname,
                    'usertype': usertype,
                    'image': image,
                    'googleid': googleid
                }
            }
        });
        return promise;
    };

    factory.getAvatar = function (userId) {
        return NEW_SERVICE_URL + 'user/getAvatar/' + userId;
    };

    return factory;
}]);

brotServices.factory('VideoService', ['$http', function ($http) {
    var factory = {};

    factory.getVideosWithTopic = function (idTopic) {
        var pathWS = NEW_SERVICE_URL + 'video/getVideosWithTopic';
        return $http.post(pathWS, {
            "request_data_type": "video",
            "request_data_method": "get_using_topic",
            "request_data": {
                "topicId": idTopic
            }

        }).success(function (data) {
        });
    };

    factory.getVideoWithTopicPN = function (subjectId, topicId, page, order) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/getVideosWithTopicPN',
            data: {
                "request_data_type": "video",
                "request_data_method": "getVideosWithTopicPN",
                "request_data": {
                    "subjectId": subjectId,
                    "topicId": topicId,
                    "pageno": page,
                    "limit": 10,
                    "order": order,
                    "totalCountFlag": true
                }
            }
        });
        return promise;
    };

    // factory.getVideosWithTopic = function(vid, subject,topic){
    //      var promise = $http({
    //         // method: 'POST',
    //         // url: NEW_SERVICE_URL + 'video/getVideosWithTopic',
    //         // data: {
    //         //     "request_data_type": "video",
    //         //     "request_data_method": "get_using_topic",
    //         //     "request_data": {
    //         //         "vid": vid,
    //         //         "subject": subject,
    //         //         "topic": topic
    //         //     }
    //         // }


    //         method: 'POST',
    //         url: NEW_SERVICE_URL + 'video/searchVideosUsingTag',
    //         data: {
    //             "request_data_type": "video",
    //             "request_data_method": "search_videos",
    //             "request_data": {
    //                 "tag": "math",
    //                 "vid": 1
    //             }
    //         }
    //     }).success(function(data){
    //         return data.response;
    //     });
    //     return promise;
    // }

    // factory.searchVideos = function(title, vid){
    //      var promise = $http({

    //         method: 'POST',
    //         url: NEW_SERVICE_URL + 'video/searchVideos',
    //         data: {
    //             "request_data_type": "video",
    //             "request_data_method": "search_videos",
    //             "request_data": {
    //                 "title": title,
    //                 "vid": vid
    //             }
    //         }
    //     }).success(function(data){
    //         return data.response;
    //     });
    //     alert("server don't support");
    //     return promise;
    // }

    factory.searchAllVideos = function (title) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/searchVideos',
            data: {
                "request_data_type": "video",
                "request_data_method": "search_videos",
                "request_data": {
                    "title": title
                }
            }
        });
        return promise;
    };

    factory.searchVideos = function (title, page) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/searchVideosPN',
            data: {
                "request_data_type": "video",
                "request_data_method": "search_videos_with_PN",
                "request_data": {
                    "title": title,
                    "pageno": page,
                    "limit": 10,
                    "totalCountFlag": true
                }
            }
        });
        return promise;
    };

    factory.markVideoAsWatched = function (uid, vid) {
        alert("server don't support");
        return $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'markVideoAsWatched?uid=' + uid + '&vid=' + vid
        });
    };

    factory.searchAllVideo = function (keySearch, page, order) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/searchAllVideo',
            data: {
                "request_data_type": "video",
                "request_data_method": "searchAllVideo",
                "request_data": {
                    "title": keySearch,
                    "description": keySearch,
                    "pageno": page,
                    "order": order,
                    "limit": 10
                }
            }
        });
        return promise;
    };

    factory.getVideoUserWatched = function (uid) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/getIdVideoWatched',
            data: {
                "request_data_type": "video",
                "request_data_method": "getIdVideoWatched",
                "request_data": {
                    "uid": uid
                }
            }
        });
        return promise;
    };

    factory.rateVideo = function (uid, vid, rate) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/rateVideo',
            data: {
                "request_data_type": "video",
                "request_data_method": "rateVideo",
                "request_data": {
                    "uid": uid,
                    "vid": vid,
                    "rating": rate
                }
            }
        });
        return promise;
    };

    factory.checkUserRatingVideo = function (uid, vid) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/checkUserRatingVideo',
            data: {
                "request_data_type": "video",
                "request_data_method": "checkUserRatingVideo",
                "request_data": {
                    "uid": uid,
                    "vid": vid
                }
            }
        });
        return promise;
    };

    factory.getRatingVideo = function (vid) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/getRating',
            data: {
                "request_data_type": "video",
                "request_data_method": "getRating",
                "request_data": {
                    "vid": vid
                }
            }
        });
        return promise;
    };

    return factory;
}]);

brotServices.factory('MentorSignUpService', ['$http', function ($http) {
    var factory = {};

    factory.getUniversity = function () {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/collegesOrUniversities',
            data: {
                "request_data_type": "user",
                "request_data_method": "collegesOrUniversities",
                "request_data": {}
            }
        });
        return promise;
    };

    factory.signupMentor = function (email, password, firstname, lastname, usertype, dob, education, accomp, colmajor, activities, tutoringinterestes, familyincome, yourdream) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/signupcomplete',
            data: {
                "request_data_type": "user",
                "request_data_method": "signupMentor",
                "request_data": {
                    "email": email,
                    "password": password,
                    "firstname": firstname,
                    "lastname": lastname,
                    "usertype": usertype,
                    "dob": dob,
                    "education": education,
                    "accomp": accomp,
                    "colmajor": colmajor,
                    "activities": activities,
                    "helpin": tutoringinterestes,
                    "familyincome": familyincome,
                    "yourdream": yourdream
                }
            }
        });
        return promise;
    };

    return factory;
}]);

//brotServices.factory('AdmissionService', ['$http', function($http) {
//  var factory = {};
//
//  factory.getAdmission = function() {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'admission/getAdmission',
//      data: {
//        "request_data_type": "admission",
//        "request_data_method": "getAdmission",
//        "request_data": {
//
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.getSubAdmission = function(idAdmission) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'admission/getSubAdmission',
//      data: {
//        "request_data_type": "admission",
//        "request_data_method": "getSubAdmission",
//        "request_data_admission": {
//          "idAdmission": idAdmission
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.getTopicSubAdmission = function(idSubAdmission) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'admission/getTopicSubAdmission',
//      data: {
//        "request_data_type": "admission",
//        "request_data_method": "getTopicSubAdmission",
//        "request_data_admission": {
//          "idSubAdmission": idSubAdmission
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.getArticles = function(idAdmission, pageno, limit) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'article/getArticles',
//      data: {
//        "request_data_type": "article",
//        "request_data_method": "getArticles",
//        "request_data_article": {
//          "idAdmission": idAdmission,
//          "pageno": pageno,
//          "limit": limit
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.getArticleDetail = function(arId) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'article/getArticleDetail',
//      data: {
//        "request_data_type": "article",
//        "request_data_method": "getArticleDetail",
//        "request_data_article": {
//          "arId": arId
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.addCommentArticle = function(userId, content, arId) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'comments/addCommentArticle',
//      data: {
//        "request_data_type":"comments",
//        "request_data_method":"addCommentArticle",
//        "request_data_article": {
//          "authorId": userId,
//          "content": encodeURIComponent(content),
//          "arId": arId
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.getDiscussesOnArticle = function(arId, pageno, limit) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'article/getArticleCommentsPN',
//      data: {
//        "request_data_type": "article",
//        "request_data_method": "getArticleCommentsPN",
//        "request_data_article": {
//          "arId": arId,
//          "pageno": pageno,
//          "limit": limit
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.likeCommentArticle = function(userId, cid) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'like/likeCommentArticle',
//      data: {
//        "request_data_type": "like",
//        "request_data_method": "likeCommentArticle",
//        "request_data_article": {
//          "authorId": userId,
//          "cid": cid
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.getVideoTopicSubAdmissionPN = function(idTopicSubAdmission, pageno, limit) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'videoAdmission/getVideoTopicSubAdmissionPN',
//      data: {
//        "request_data_type": "videoAdmission",
//        "request_data_method": "getVideoTopicSubAdmissionPN",
//        "request_data_videoAdmission": {
//          "idTopicSubAdmission": idTopicSubAdmission,
//          "pageno": pageno,
//          "limit": limit
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.getVideoAdmissionDetail = function(idSubAdmission, idTopicSubAdmission, vId) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'videoAdmission/getVideoAdmissionDetail',
//      data: {
//        "request_data_type": "videoAdmission",
//        "request_data_method": "getVideoAdmissionDetail",
//        "request_data_videoAdmission": {
//          "idSubAdmission": idSubAdmission,
//          "idTopicSubAdmission": idTopicSubAdmission,
//          "vId": vId
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.getVideoTopicSubAdmission = function(idTopicSubAdmission) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'videoAdmission/getVideoTopicSubAdmission',
//      data: {
//        "request_data_type": "videoAdmission",
//        "request_data_method": "getVideoTopicSubAdmission",
//        "request_data_videoAdmission": {
//          "idTopicSubAdmission": idTopicSubAdmission
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.addCommentVideoAdmission = function(userId, content, vId) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'comments/addCommentVideoAdmission',
//      data: {
//        "request_data_type":"comments",
//        "request_data_method":"addCommentVideoAdmission",
//        "request_data_videoAdmission": {
//          "authorId": userId,
//          "content": encodeURIComponent(content),
//          "vId": vId
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.getVideoAdmissionComment = function(vId, pageno, limit) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'videoAdmission/getVideoAdmissionCommentsPN',
//      data: {
//        "request_data_type": "videoAdmission",
//        "request_data_method": "getVideoAdmissionCommentsPN",
//        "request_data_videoAdmission": {
//          "vId": vId,
//          "pageno": pageno,
//          "limit": limit
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.likeCommentVideoAdmission = function(userId, cid) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'like/likeCommentVideoAdmission',
//      data: {
//        "request_data_type": "like",
//        "request_data_method": "likeCommentVideoAdmission",
//        "request_data_videoAdmission": {
//          "authorId": userId,
//          "cid": cid
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.orderVideoAdmissionPN = function(idTopicSubAdmission, pageno, limit, order) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'videoAdmission/orderVideoAdmissionPN',
//      data: {
//        "request_data_type": "videoAdmission",
//        "request_data_method": "orderVideoAdmissionPN",
//        "request_data_videoAdmission": {
//          "idTopicSubAdmission": idTopicSubAdmission,
//          "pageno": pageno,
//          "limit": limit,
//          "order": order
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.updateViewVideoAdmission = function(vId) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'videoAdmission/updateViewVideoAdmission',
//      data: {
//        "request_data_type": "videoAdmission",
//        "request_data_method": "updateViewVideoAdmission",
//        "request_data_videoAdmission": {
//          "vId": vId
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.uploadImage = function(callback) {
//    $.ajax({
//      url: NEW_SERVICE_URL + 'article/uploadFile',
//      type: 'POST',
//      data: new FormData($("#upload-file-form")[0]),
//      enctype: 'multipart/form-data',
//      processData: false,
//      contentType: false,
//      cache: false,
//      success: function(data) {
//        callback(data);
//      }
//    });
//  };
//
//  factory.createArticle = function(title, image, description, content) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'article/createArticle',
//      data: {
//        "request_data_type": "article",
//        "request_data_method": "createArticle",
//        "request_data_article": {
//          "title": title,
//          "image": image,
//          "description": description,
//          "content": encodeURIComponent(content)
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.getImageArticle = function(arId) {
//    return NEW_SERVICE_URL + 'article/getImageArticle/' + arId;
//  };
//
//  factory.getImageVideoAdmission = function(vId) {
//    return NEW_SERVICE_URL + 'videoAdmission/getImageVideoAdmission/' + vId;
//  };
//
//  factory.updateVideoAdmissionWatched = function(uid, vid) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'videoAdmission/updateVideoAdmissionWatched',
//      data: {
//        "request_data_type": "videoAdmission",
//        "request_data_method": "updateVideoAdmissionWatched",
//        "request_data_videoAdmission": {
//          "uid": uid,
//          "vId": vid
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.getIdVideoAdmissionWatched = function(uid) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'videoAdmission/getIdVideoAdmissionWatched',
//      data: {
//        "request_data_type": "videoAdmission",
//        "request_data_method": "getIdVideoAdmissionWatched",
//        "request_data_videoAdmission": {
//          "uid": uid
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.rateVideoAdmission = function(uid, vid, rate) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'videoAdmission/rateVideoAdmission',
//      data: {
//        "request_data_type": "videoAdmission",
//        "request_data_method": "rateVideoAdmission",
//        "request_data_videoAdmission": {
//          "uid": uid,
//          "vId": vid,
//          "rating": rate
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.getRatingVideoAdmission = function(vid) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'videoAdmission/getRatingVideoAdmission',
//      data: {
//        "request_data_type": "videoAdmission",
//        "request_data_method": "getRatingVideoAdmission",
//        "request_data_videoAdmission": {
//          "vId": vid
//        }
//      }
//    });
//    return promise;
//  };
//
//  factory.checkUserRatingVideoAdmission = function(uid, vid) {
//    var promise = $http({
//      method: 'POST',
//      url: NEW_SERVICE_URL + 'videoAdmission/checkUserRatingVideoAdmission',
//      data: {
//        "request_data_type": "videoAdmission",
//        "request_data_method": "checkUserRatingVideoAdmission",
//        "request_data_videoAdmission": {
//          "uid": uid,
//          "vId": vid
//        }
//      }
//    });
//    return promise;
//  };
//
//  return factory;
//}]);

brotServices.factory('AboutMentorService', ['$http', function ($http) {
    var factory = {};

    factory.getAllAboutMentor = function () {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'mentor/getAllAboutMentor',
            data: {
                "request_data_type": "mentor",
                "request_data_method": "getAllAboutMentor",
                "request_data_aboutMentor": {}
            }
        });
        return promise;
    };

    factory.getImageAboutMentor = function (id) {
        return NEW_SERVICE_URL + 'mentor/getImageAboutMentor/' + id;
    };

    return factory;
}]);

brotServices.factory('CommentService', ['$http', function ($http) {
    var factory = {};

    factory.uploadImage = function (callback) {
        $.ajax({
            url: NEW_SERVICE_URL + 'comments/uploadFile',
            type: 'POST',
            data: new FormData($("#upload-file-form")[0]),
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            cache: false,
            success: function (data) {
                callback(data);
            }
        });
    };

    factory.uploadImageEdit = function (callback) {
        $.ajax({
            url: NEW_SERVICE_URL + 'comments/uploadFile',
            type: 'POST',
            data: new FormData($("#upload-file-form-edit")[0]),
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            cache: false,
            success: function (data) {
                callback(data);
            }
        });
    };

    factory.deleteComment = function (cid) {
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

    factory.editComment = function (cid, content) {
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

    factory.addCommentEssay = function (userId, content, essayId) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'comments/addCommentEssay',
            data: {
                "request_data_type": "comments",
                "request_data_method": "addCommentEssay",
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

brotServices.factory('myCache', function ($cacheFactory) {
    return $cacheFactory('myData');
});
