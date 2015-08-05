/**
 * Controller for performing authentication or signingup a new user */
(function () {
    'use strict';

    angular
        .module('devops-dashboard')
        .controller('LoginController', LoginController)
        .run(function($cookies,$location) {
           console.log("I am in run scope");
            if($cookies.authenticated)
            {
                console.log("I am authenticated");
                $location.path('/site');

            }
            else
            {
                console.log("Not authenticated redirecting");
                $location.path('/');
            }
        });


    LoginController.$inject = ['$scope', 'loginData', '$location', '$cookies'];
    function LoginController($scope, loginData, $location, $cookies) {
        var login = this;

        // public variables
        login.id= '';
        login.passwd= '';
        login.submitted= false;
        $scope.alerts = [];


      //public methods
      login.doLogin=doLogin;
      login.doSignup=doSignup;


        //function for closing alerts
        $scope.closeAlert = function (index) {
            $scope.alerts.splice(index, 1);
        };


      function doLogin(valid)
      {
        login.submitted=true;
        if(valid)
        {

            loginData.login(document.lg.loginfield.value, document.lg.passwordfield.value).then(processResponse);
        }

      }




      function processResponse(data) {

          console.log("Authentication is:"+data);

          if(data)
          {
            $cookies.authenticated=true;
              $cookies.username=document.lg.loginfield.value;

            $location.path('/site');

          }
          else {
              $scope.alerts.push({type: 'danger', msg: 'Incorrect Username and Password please check'});
          }
      }

        function doSignup()
        {
            console.log("In signup");
            $location.path('/signup');
        }

    }
})();
