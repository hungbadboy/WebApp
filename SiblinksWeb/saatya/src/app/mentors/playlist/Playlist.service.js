brotServices.factory('PlaylistService', [ '$http', function($http) {
    var factory = {};


    factory.loadPlaylist = function(userId, offset) {      

        var promise = $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'playlist/getPlaylist?userid='+ userId +'&offset='+offset+''
        });
        return promise;
    };

    factory.getPlaylistBySubject = function(userId, subjectId, offset) {      

        var promise = $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'playlist/getPlaylistBySubject?uid='+ userId +'&subjectId='+subjectId+'&offset='+offset+''
        });
        return promise;
    };

    factory.loadPlaylistById = function(plid) {      

        var promise = $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'playlist/getPlaylistById/'+ plid +''
        });
        return promise;
    };

    factory.loadSubject = function() {      

        var promise = $http({ 
          method: 'POST',
          url: NEW_SERVICE_URL + 'subjects/fetchSubjects/',
          data: {
            "request_data_type": "subjects",
            "request_data_method": "fetchSubjects"
            }
        });
        return promise;
    };

    // factory.insertPlaylist = function(playlist) {      

    //     var promise = $http({
    //       method: 'POST',
    //       url: NEW_SERVICE_URL + 'playlist/insertPlaylist',
    //       data: {
    //         "request_data_type": "playlist",
    //         "request_data_method": "insertPlaylist",
    //         "request_playlist":playlist
    //         }
    //     });
    //     return promise;
    // };

    factory.insertPlaylist = function(fd) {      
      var promise = $http({
          method: 'POST',
          url: NEW_SERVICE_URL + 'playlist/insertPlaylist',
          headers:{
            'Content-Type': undefined
          },
          data: fd,
          transformRequest: function (data, headersGetterFunction) {
            return data;
          }
        });
      return promise;
    };

    factory.updatePlaylist = function(fd) {
        var promise = $http({
          method: 'POST',
          url: NEW_SERVICE_URL + 'playlist/updatePlaylist',
          headers:{
            'Content-Type': undefined
          },
          data: fd,
          transformRequest: function (data, headersGetterFunction) {
            return data;
          }
        });
        return promise;
    };

    factory.deletePlaylist = function(id, uid) {      

        var promise =  $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'playlist/deletePlaylist/',
            data: {
              "request_data_type": "playlist",
              "request_data_method": "deletePlaylist",
              "request_playlist":{
                "plid": id,
                "createBy":uid
              }            
            }    
        });
        return promise;
    };

    factory.deleteMultiplePlaylist = function(plids, uid){
      var promise = $http({
          method: 'POST',
          url: NEW_SERVICE_URL + 'playlist/deleteMultiplePlaylist',
          data:{
              "request_data_type": "playlist",
              "request_data_method": "deleteMultiplePlaylist",
              "request_playlist":{
                  "plids": plids,
                  'createBy': uid
              }
          }
          
      });
      return promise;
    }

    factory.deleteVideoInPlaylist = function(vids){
      var promise = $http({
          method: 'POST',
          url: NEW_SERVICE_URL + 'playlist/deleteVideoInPlaylist',
          data:{
              "request_data_type": "playlist",
              "request_data_method": "deleteVideoInPlaylist",
              "request_data":{
                  "vids": vids
              }
          }
          
      });
      return promise;
    }

    factory.getAllPlaylist = function(uid){
      var promise = $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'playlist/getAllPlaylist?uid='+uid+''
      });
      return promise;
    }

    factory.searchPlaylist = function(uid, keyword, subjectId, offset){
      var promise = $http({
          method: 'POST',
          url: NEW_SERVICE_URL + 'playlist/searchPlaylist',
          data:{
              "request_data_type": "playlist",
              "request_data_method": "searchPlaylist",
              "request_data":{
                  "uid": uid,
                  "keySearch": keyword,
                  "subjectId": subjectId,
                  "offset": offset
              }
          }
      });
      return promise;
    }

    factory.getVideoInPlaylist = function(plid, offset){
      var promise = $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'playlist/getVideoInPlaylist?plid='+plid+'&offset='+offset+''
      });
      return promise;
    }

    return factory;
} ]);