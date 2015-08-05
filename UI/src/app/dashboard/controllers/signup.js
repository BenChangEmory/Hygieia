/**
 * Controller for performing signup a new user */
(function () {
    'use strict';

    angular
        .module('devops-dashboard')
        .controller('SignupController', SignupController);

    SignupController.$inject = ['$scope','signupData', '$location', '$cookies'];
    function SignupController($scope, signupData, $location, $cookies) {
        var signup = this;

        // public variables
        signup.id = '';
        signup.passwd='';
        $scope.alerts = [];


        $scope.closeAlert = function(index) {
            $scope.alerts.splice(index, 1);
            $location.path("/");
        };

      //public methods
      signup.doSignup=doSignup;

      function doSignup()
      {
        signupData.signup(document.suf.myusername.value,document.suf.mypassword.value).then(processResponse);
      }

      function processResponse(data) {

          $scope.alerts.push({type: 'success', msg: data});
          console.log(data);



      }

    }
})();
