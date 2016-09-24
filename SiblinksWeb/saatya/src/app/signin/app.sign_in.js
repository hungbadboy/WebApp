brot = {};
brot.signin = {
  signin : function($rootScope) {
    var id = "popSignIn";
    dashboard.createModal(id);
    var ele = dashboard.get_ele(id);

    $(ele).load(dashboard.getUrlFolder() + 'template/popSignIn.html?r=' + Math.random(), function() {
      $(ele).modal({keyboard: false, backdrop: 'static'});
      $(ele).modal('show');
      $(ele).find('.signin_email').click(function(event) {

      });
      $(ele).find('.close').click(function(event) {
        $(ele).modal('hide');
      });
      $(ele).find('.signinComplex').click(function(event){
        var userName = $(ele).find('.userName').val();
        var passWord = $(ele).find('.passWord').val();
        var pathWS = NEW_SERVICE_URL + '/user/login';

        // if(userName != null && passWord != null) {
        //   $.ajax({
        //     url: pathWS,
        //     type: 'POST',
        //     dataType: 'json',
        //     data: {
        //         username: userName,
        //         password: passWord
        //     },
        //     success : function(data) {
        //       if(data.status == "true") {
        //           // Set Cache (30 seconds)
        //           var dataUser = data.request_data_result[0];
        //           setStorage('userName', dataUser['username'], 30);
        //           setStorage('userId', dataUser['userid'], 30);

        //           $('#header .login').addClass('hide');
        //           $('#header .log_out').removeClass('hide');

        //           var nameHome = '';
        //           if (dataUser['firstname'] == null || dataUser['lastname'] == null) {
        //               nameHome = capitaliseFirstLetter(dataUser['username'].substring(0, dataUser['username'].indexOf('@')));
        //           }
        //           else {
        //               nameHome = capitaliseFirstLetter(dataUser['firstname']) + ' ' + capitaliseFirstLetter(dataUser['lastname']);
        //           }
        //           setStorage('nameHome', nameHome, 30);
        //           $('#header .log_out .current').text(nameHome);
        //           $(ele).modal('hide');
        //           location.reload();
        //           // window.location.href = document.URL;
        //       } else {
        //           $('.loginMess').text(data.request_data_result[0]).removeClass('hide');
        //       }
        //     }
        //   });
        // }
      });

      //forgotpassword
      $('#popSignIn .lb_forgot').unbind('click').click(function() {
        $('.wrap_info .loadingdivForgot').find('#loading').addClass('hide');
        $('.wrap_info .wrap_event').find('.forgot').removeAttr('disabled');
        $('#popSignIn').modal('hide');
        var userName = $(ele).find('.userName').val();
        $('#forgotPassword').modal('show');
        $('#forgotPassword .userName').val(userName);

        $('#forgotPassword .wrap_info').removeClass('hide');
        $('#forgotPassword .wrap_alert').addClass('hide');
      });
    }); // end load form popup signin

    function setStorage(name, value, expire) {
      if(expire === undefined || expire == null) {
        expire = 3600; //default
      }
      var date = new Date();
      var schecule = Math.round((date.setSeconds(date.getSeconds()+expire))/1000);
      localStorage.setItem(name, value);
      localStorage.setItem(name + '_time', schecule);
    }

    function removeStorage(name) {
      localStorage.removeItem(name);
      localStorage.removeItem(name + '_time');
    }
  },

  statusStorageHtml: function() {
    var nameHome = localStorage.getItem('nameHome');
    if(nameHome !== undefined && nameHome != null) {
      $('#header .login').addClass('hide');
      $('#header .log_out').removeClass('hide');
      $('#header .log_out .current').text(nameHome);
    }
  },

  onSignInCallback : function(authResult) {
    gapi.client.load('plus', 'v1', function() {
      if(authResult.status.signed_in) {
        $('#popSignIn').modal('hide');
        brot.signin.getProfile();
      }
    });
  },

  getProfile : function() {
    var request = gapi.client.plus.people.get({
      'userId' : 'me'
    });

    request.execute(function(resp) {
      $('#login').addClass('hide');
      $('#header').find('.log_out').removeClass('hide');
      $('#header').find('.log_out').children().text(resp.displayName);
    });
  },

  checkLoginFaceStatus : function(res) {
    if(res.status == "connected") {
      brot.signin.testAPI();
    } else {
      FB.login(function(res){
        if(res.status == "connected"){
          brot.signin.testAPI();
        }
      });
    }
  },

  testAPI : function() {
    FB.api('/me', function(res) {
      $('#popSignIn').modal('hide') ;
      $('#login').addClass('hide');
      $('#header').find('.log_out').removeClass('hide');
      $('#header').find('.log_out').children().text(res.name);
    });
  },

  forgotPassword: function() {
    $('#popSignIn .lb_forgot').unbind('click').click(function() {

    });
  },
  signSuccess: function() {
    alert(username+ " " +passWord);
  }
};

function onSignInCallback(authResult) {
  brot.signin.onSignInCallback(authResult);
}


