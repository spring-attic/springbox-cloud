var springbox = angular.module('springbox');

springbox.controller('home', function ($rootScope, $scope, $http) {
    $http.get('/catalog/genres').success(function (data) {
        $scope.genres = data;
    });

    $http.get('user').success(function (data) {
        if (data.username) {
            $http.get('recommendations/recommendations/forUser/' + data.username)
                .success(function (recs) {
                    $scope.recommendations = recs;
                });
        }
    });
});