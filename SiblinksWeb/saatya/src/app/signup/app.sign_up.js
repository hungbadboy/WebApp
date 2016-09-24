brot.signup = {

    init: function(){
        var main = brot.signup;
        main.signupEmail();
        //main.signupComplex();
    },

    signupEmail:function(){
        var url = NEW_SERVICE_URL;
        $('#signUpEmail .button .btnSignUp').click(function(event) {

            var data = {
                firstname: $('#signUpEmail .field .firstname').val().trim(),
                lastname: $('#signUpEmail .field .lastname').val().trim(),
                email: $('#signUpEmail .field .email').val().trim(),
                password: $('#signUpEmail .field .password').val().trim(),
                usertype: 'S'
            };

            url += '/user/register';
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: url,
                data: data,
                success: function(json){
                    console.log(json);
                }

            });
        });
    }
};