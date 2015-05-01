var springbox = angular.module('springbox');

springbox.controller('reviews', function ($rootScope, $scope, $http) {
    $http.get('/reviews')
        .success(function (data) {
            $scope.reviews = data;
        });
});
