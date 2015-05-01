var springbox = angular.module('springbox');

springbox.controller('movie', function ($rootScope, $scope, $http, $routeParams) {
    $http.get('/movie/' + $routeParams.mlId, {params: {"userName": $rootScope.userName}})
        .success(function (data) {
            $scope.movie = data;
        });

    $scope.like = function () {
        $http({
            method: 'POST',
            url: 'recommendations/recommendations/' + $rootScope.userName + '/likes/' + $scope.movie.mlId
        }).success(function () {
            $http.get('/movie/' + $routeParams.mlId).success(function (data) {
                $scope.movie = data;
            })
        })
    }
});