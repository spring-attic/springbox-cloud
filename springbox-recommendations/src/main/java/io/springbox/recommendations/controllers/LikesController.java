package io.springbox.recommendations.controllers;

import io.springbox.recommendations.domain.Likes;
import io.springbox.recommendations.repositories.LikesRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class LikesController {

    Log log = LogFactory.getLog(LikesController.class);

    @Autowired
    LikesRepository likesRepository;

    @RequestMapping(value = "/likes", method = RequestMethod.GET)
    @PreAuthorize("#userName == authentication.name")
    public Iterable<Likes> likes() {
        return likesRepository.findAll();
    }

    @RequestMapping(value = "/does/{userName}/like/{mlId}", method = RequestMethod.GET)
    public ResponseEntity<Boolean> likesFor(@PathVariable("mlId") String mlId, @PathVariable("userName") String userName) {
        log.debug(String.format("/does/%s/like/%s endpoint requested!", userName, mlId));
        int likes = likesRepository.likesFor(mlId, userName).size();
        log.debug(String.format("Result of %s like %s: %s", userName, mlId, likes));
        if (likes > 0) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }
}
