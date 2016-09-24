brot.signup = {

    init: function(){
        var main = brot.signup;
        main.signupEmail();
        //main.signupComplex();
    },

    signupEmail:function(){
        var url = 'http://ec2-54-200-200-106.us-west-2.compute.amazonaws.com:8080/brot/services';
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
                type: 'GET',
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