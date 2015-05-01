var springbox = angular.module('springbox');

springbox.controller('createReview', function ($location, $rootScope, $scope, $http, $routeParams) {
    $scope.formData = {};

    $http.get('/movie/' + $routeParams.mlId)
        .success(function (data) {
            $scope.formData.mlId = data.mlId;
        });

    $scope.processForm = function () {
        $scope.formData.userName = $rootScope.userName;

        $http({
            method: 'POST',
            url: 'reviews/reviews/',
            data: $scope.formData
        }).success(function (data) {
            console.log(data);
            $location.path('/movie/' + $scope.formData.mlId);
        });
    };
});
