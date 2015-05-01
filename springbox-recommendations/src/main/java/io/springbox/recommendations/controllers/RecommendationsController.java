package io.springbox.recommendations.controllers;

import io.springbox.recommendations.domain.Likes;
import io.springbox.recommendations.domain.Movie;
import io.springbox.recommendations.domain.Person;
import io.springbox.recommendations.repositories.LikesRepository;
import io.springbox.recommendations.repositories.MovieRepository;
import io.springbox.recommendations.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableOAuth2Resource
public class RecommendationsController {

    @Autowired
    MovieRepository movieRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    LikesRepository likesRepository;

    @RequestMapping(value = "/recommendations/{userName}/likes/{mlId}", method = RequestMethod.POST)
    public ResponseEntity<Likes> createPersonMovieLink(@PathVariable String userName,
                                                       @PathVariable String mlId) {
        Person person = personRepository.findByUserName(userName);
        Movie movie = movieRepository.findByMlId(mlId);

        Likes likes = new Likes();
        likes.setPerson(person);
        likes.setMovie(movie);
        likesRepository.save(likes);

        return new ResponseEntity<>(likes, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/recommendations/forUser/{userName}", method = RequestMethod.GET)
    @PreAuthorize("#userName == authentication.name")
    public Iterable<Movie> recommendedMoviesForUser(@PathVariable String userName) {
        return movieRepository.recommendedMoviesFor(userName);
    }

    @RequestMapping(value = "/recommendations/forMovie/{mlId}", method = RequestMethod.GET)
    public Iterable<Movie> recommendedMoviesForMovie(@PathVariable String mlId) {
        return movieRepository.moviesLikedByPeopleWhoLiked(mlId);
    }
}
