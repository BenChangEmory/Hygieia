/**
 * Controller for choosing or creating a new dashboard
 */
(function () {
    'use strict';

    angular
        .module('devops-dashboard')
        .controller('SiteController', SiteController);

    SiteController.$inject = ['$scope', '$modal', 'dashboardData', '$location', '$cookies','$cookieStore'];
    function SiteController($scope, $modal, dashboardData, $location, $cookies,$cookieStore) {
        var ctrl = this;

        // public variables
        ctrl.search = '';
        ctrl.username=$cookies.username;
        console.log("Setting navbar username as"+ctrl.username);



        // ctrl.dashboards = []; don't default since it's used to determine loading

        // public methods
        ctrl.createDashboard = createDashboard;
        ctrl.open = open;
        ctrl.logout= logout;
        ctrl.templateUrl = "app/dashboard/views/navheader.html";


        // request dashboards
        dashboardData.search().then(processResponse);

        //find dashboard I own
        dashboardData.mydashboard(ctrl.username).then(processDashResponse);


        function logout()
        {
$cookieStore.remove("username");
            $cookieStore.remove("authenticated");
            $location.path("/");
        }

        // method implementations
        function createDashboard() {
            // open modal for creating a new dashboard
            $modal.open({
                templateUrl: 'app/dashboard/views/createDashboard.html',
                controller: 'CreateDashboardController',
                controllerAs: 'ctrl'
            });
        }

        function open(dashboardId) {
            $location.path('/dashboard/' + dashboardId);
        }

        function processResponse(data) {
            // add dashboards to list
            ctrl.dashboards = [];
            for (var x = 0; x < data.length; x++) {
                ctrl.dashboards.push({
                    id: data[x].id,
                    name: data[x].title
                });
            }
        }

        function processDashResponse(mydata) {
            // add dashboards to list
            ctrl.mydash = [];
            for (var x = 0; x < mydata.length; x++) {

                ctrl.mydash.push({
                    id: mydata[x].id,
                    name: mydata[x].title
                });

            }

        }

    }
})();
