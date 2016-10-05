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

    factory.deletePlaylist = function(id) {      

        var promise =  $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'playlist/deletePlaylist/',
            data: {
              "request_data_type": "playlist",
              "request_data_method": "deletePlaylist",
              "request_playlist":{
                "plid": id 
              }            
            }    
        });
        return promise;
    };

    factory.deleteMultiplePlaylist = function(plids){
      var promise = $http({
          method: 'POST',
          url: NEW_SERVICE_URL + 'playlist/deleteMultiplePlaylist',
          data:{
              "request_data_type": "playlist",
              "request_data_method": "deleteMultiplePlaylist",
              "request_playlist":{
                  "plids": plids
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

    factory.getAllPlaylist = function(){
      var promise = $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'playlist/getAllPlaylist'
      });
      return promise;
    }

    factory.searchPlaylist = function(uid, keyword, offset){
      var promise = $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'playlist/searchPlaylist?uid='+uid+'&keyword='+keyword+'&offset='+offset+''
      });
      return promise;
    }

    return factory;
} ]);