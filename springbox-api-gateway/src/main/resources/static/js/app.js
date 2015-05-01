var springbox = angular.module('springbox', ['ngRoute']).config(function ($routeProvider, $httpProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'views/home.html',
            controller: 'home'
        })
        .when('/genre/:genreMlId', {
            templateUrl: 'views/genre.html',
            controller: 'genres'
        })
        .when('/reviews', {
            templateUrl: 'views/reviews.html',
            controller: 'reviews'
        })
        .when('/movie/:mlId', {
            templateUrl: 'views/movie.html',
            controller: 'movie'
        })
        .when('/movie/:mlId/review', {
            templateUrl: 'views/createReview.html',
            controller: 'createReview'
        })
        .otherwise('/');

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
});

springbox.controller('navigation', function ($rootScope, $scope, $http, $location, $route, userStore) {
    $scope.tab = function (route) {
        return $route.current && route === $route.current.controller;
    };

    $http.get('user')
        .success(function (data) {
            if (data.username) {
                $rootScope.authenticated = true;
                $rootScope.userName = data.username;
                userStore.userName = data.username;
            } else {
                $rootScope.authenticated = false;
                $rootScope.userName = null;
            }
        })
        .error(function () {
            $rootScope.authenticated = false;
            $rootScope.userName = null;
        });

    $scope.credentials = {};

    $scope.logout = function () {
        $http.post('logout', {})
            .success(function () {
                $rootScope.authenticated = false;
                $rootScope.userName = null;
                $location.path("/");
            })
            .error(function (data) {
                console.log("Logout failed");
                $rootScope.userName = null;
                $rootScope.authenticated = false;
            });
    }
});

springbox.factory('userStore', function () {
    return {};
});
