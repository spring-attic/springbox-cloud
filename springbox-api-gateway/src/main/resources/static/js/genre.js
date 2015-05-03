var springbox = angular.module('springbox');

springbox.controller('genres', function ($scope, $http, $routeParams) {
    $http.get('/catalog/genres/' + $routeParams.genreMlId)
        .success(function (data) {
            $scope.genre = data;
        });

    $http.get('/catalog/movies/genre/' + $routeParams.genreMlId)
        .success(function (data) {
            $scope.movies = data;
        });
});
