package io.springbox.reviews.controllers;

import io.springbox.reviews.domain.Review;
import io.springbox.reviews.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@EnableOAuth2Resource
public class ReviewController { //extends ResourceServerConfigurerAdapter {

    @Autowired
    ReviewRepository reviewRepository;

    @RequestMapping(value = "/reviews", method = RequestMethod.GET)
    public Iterable<Review> reviews() {
        return reviewRepository.findAll();
    }

    @RequestMapping(value = "/reviews/{mlId}", method = RequestMethod.GET)
    public Iterable<Review> reviews(@PathVariable String mlId) {
        return reviewRepository.findByMlId(mlId);
    }

    @RequestMapping(value = "/reviews", method = RequestMethod.POST)
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        reviewRepository.save(review);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    /*
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/reviews/**").authenticated()
                .and()
            .authorizeRequests()
                .anyRequest().permitAll();
    }
    */
}
