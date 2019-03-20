package io.springbox.apigateway.services.reviews;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.ObservableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

import java.util.Arrays;
import java.util.List;

@Service
public class ReviewsIntegrationService {

    @Autowired
    @Qualifier("loadBalancedRestTemplate")
    @LoadBalanced
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "stubReviews",
                    commandProperties = {
                            @HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE")
                    }
    )
    public Observable<List<Review>> reviewsFor(String mlId) {
        return new ObservableResult<List<Review>>() {
            @Override
            public List<Review> invoke() {
                ParameterizedTypeReference<List<Review>> responseType = new ParameterizedTypeReference<List<Review>>() {
                };
                return restTemplate.exchange("https://springbox-reviews/reviews/{mlId}", HttpMethod.GET, null, responseType, mlId).getBody();
            }
        };
    }

    private List<Review> stubReviews(String mlId) {
        Review review = new Review();
        review.setMlId(mlId);
        review.setRating(4);
        review.setTitle("Interesting...the wrong title. Sssshhhh!");
        review.setReview("Awesome sauce!");
        review.setUserName("joeblow");
        return Arrays.asList(review);
    }

}
