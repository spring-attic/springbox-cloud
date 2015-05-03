package io.springbox.apigateway.controllers;

import io.springbox.apigateway.domain.MovieDetails;
import io.springbox.apigateway.services.catalog.CatalogIntegrationService;
import io.springbox.apigateway.services.recommendations.RecommendationsIntegrationService;
import io.springbox.apigateway.services.reviews.ReviewsIntegrationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;
import rx.Observer;

import java.security.Principal;

@RestController
public class ApiGatewayController {

    Log log = LogFactory.getLog(ApiGatewayController.class);

    @Autowired
    CatalogIntegrationService catalogIntegrationService;

    @Autowired
    ReviewsIntegrationService reviewsIntegrationService;

    @Autowired
    RecommendationsIntegrationService recommendationsIntegrationService;

    @RequestMapping("/movie/{mlId}")
    public DeferredResult<MovieDetails> movieDetails(@PathVariable String mlId,
                                                     @AuthenticationPrincipal Principal principal) {

        log.debug(String.format("Loading anonymous movie details for mlId: %s", mlId));

        Observable<MovieDetails> movieDetails = anonymousMovieDetails(mlId);

        if (principal != null) {
            String userName = principal.getName();

            log.debug(String.format("Loading details for mlId: %s for username: %s", mlId, userName));

            movieDetails = Observable.zip(
                    movieDetails,
                    recommendationsIntegrationService.likes(userName, mlId),
                    (details, likes) -> {
                        details.setLikes(likes);
                        return details;
                    }
            );
        }

        return toDeferredResult(movieDetails);
    }

    private Observable<MovieDetails> anonymousMovieDetails(String mlId) {
        return Observable.zip(
                catalogIntegrationService.getMovie(mlId),
                reviewsIntegrationService.reviewsFor(mlId),
                recommendationsIntegrationService.getRecommendations(mlId),
                (movie, reviews, recommendations) -> {
                    MovieDetails movieDetails = new MovieDetails();
                    movieDetails.setMlId(movie.getMlId());
                    movieDetails.setTitle(movie.getTitle());
                    movieDetails.setReviews(reviews);
                    movieDetails.setRecommendations(recommendations);
                    movieDetails.setGenres(movie.getGenres());
                    return movieDetails;
                }
        );
    }

    public DeferredResult<MovieDetails> toDeferredResult(Observable<MovieDetails> details) {
        DeferredResult<MovieDetails> result = new DeferredResult<>();
        details.subscribe(new Observer<MovieDetails>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onNext(MovieDetails movieDetails) {
                result.setResult(movieDetails);
            }
        });
        return result;
    }


}
