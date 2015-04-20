angular.module('hello', ['ngRoute']).config(function ($routeProvider) {

    $routeProvider.when('/', {
        templateUrl: 'home.html',
        controller: 'home'
    })
        .when('/genre/:genreMlId', {
            templateUrl: 'genre.html',
            controller: 'genres'
        })
        .when('/reviews', {
            templateUrl: 'reviews.html',
            controller: 'reviews'
        })
        .when('/movie/:mlId', {
            templateUrl: 'movie.html',
            controller: 'movie'
        })
        .when('/movie/:mlId/review', {
            templateUrl: 'createReview.html',
            controller: 'createReview'
        })
        .otherwise('/');

}).controller('navigation',

    function ($rootScope, $scope, $http, $location, $route, userStore) {

        $scope.tab = function (route) {
            return $route.current && route === $route.current.controller;
        };

        $http.get('user').success(function (data) {
            if (data.username) {
                $rootScope.authenticated = true;
                $rootScope.userName = data.username;
                userStore.userName = data.username;
            } else {
                $rootScope.authenticated = false;
                $rootScope.userName = null;
            }
        }).error(function () {
            $rootScope.authenticated = false;
            $rootScope.userName = null;
        });

        $scope.credentials = {};

        $scope.logout = function () {
            $http.post('logout', {}).success(function () {
                $rootScope.authenticated = false;
                $rootScope.userName = null;
                $location.path("/");
            }).error(function (data) {
                console.log("Logout failed");
                $rootScope.userName = null;
                $rootScope.authenticated = false;
            });
        }

    }).factory('userStore', function() {
        return {};
    })

    .controller('home', function ($rootScope, $scope, $http, userStore) {
        $http.get('catalog/genres').success(function (data) {
            $scope.genres = data;
        });

        $http.get('user').success(function (data) {
            if (data.username) {
                $http.get('recommendations/recommendations/forUser/' + data.username).success(function (recs) {
                    $scope.recommendations = recs;
                });
            }
        });


    }).controller('genres', function ($scope, $http, $routeParams) {
        $http.get('catalog/genres/' + $routeParams.genreMlId).success(function (data) {
            $scope.genre = data;
        });

        $http.get('catalog/movies/genre/' + $routeParams.genreMlId).success(function (data) {
            $scope.movies = data;
        });
    }).controller('movie', function ($rootScope, $scope, $http, $routeParams) {
        $http.get('/movie/' + $routeParams.mlId, {params:{"userName":$rootScope.userName}}).success(function (data) {
            $scope.movie = data;
        })

        $scope.like = function() {
            $http({
                method: 'POST',
                url: 'recommendations/recommendations/' + $rootScope.userName + '/likes/' + $scope.movie.mlId
            }).success(function (data) {
                $http.get('/movie/' + $routeParams.mlId).success(function (data) {
                    $scope.movie = data;
                })
            })
        }
    }).controller('createReview', function ($location, $rootScope, $scope, $http, $routeParams) {
        $http.get('/movie/' + $routeParams.mlId).success(function (data) {
            $scope.movie = data;

            $scope.formData = {};

            $scope.processForm = function () {
                $scope.formData.mlId = $scope.movie.mlId;
                $scope.formData.userName = $rootScope.userName;

                $http({
                    method: 'POST',
                    url: 'reviews/reviews/',
                    data: $scope.formData
                }).success(function (data) {
                    console.log(data);
                    $location.path('/movie/' + $scope.movie.mlId);
                });
            };
        })
    });
