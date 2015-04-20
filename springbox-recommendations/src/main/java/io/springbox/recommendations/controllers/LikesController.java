package io.springbox.recommendations.controllers;

import io.springbox.recommendations.domain.Likes;
import io.springbox.recommendations.repositories.LikesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LikesController {

    @Autowired
    LikesRepository likesRepository;

    @RequestMapping(value = "/likes", method = RequestMethod.GET)
    public Iterable<Likes> likes() {
        return likesRepository.findAll();
    }

    @RequestMapping(value = "/does/{userName}/like/{mlId}", method = RequestMethod.GET)
    public ResponseEntity<Boolean> likesFor(@PathVariable("mlId") String mlId, @PathVariable("userName") String userName) {
        int likes = likesRepository.likesFor(mlId, userName).size();
        if (likes > 0) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }
}
