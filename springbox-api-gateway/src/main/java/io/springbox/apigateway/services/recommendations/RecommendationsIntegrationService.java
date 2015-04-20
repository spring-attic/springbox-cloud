package io.springbox.apigateway.services.recommendations;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.ObservableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

import java.util.Arrays;
import java.util.List;

@Service
public class RecommendationsIntegrationService {
    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "stubRecommendations",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
            })
    public Observable<List<Movie>> getRecommendations(final String mlId) {
        return new ObservableResult<List<Movie>>() {
            @Override
            public List<Movie> invoke() {
                ParameterizedTypeReference<List<Movie>> responseType = new ParameterizedTypeReference<List<Movie>>() {
                };
                return restTemplate.exchange("http://springbox-api-gateway/recommendations/recommendations/forMovie/{mlId}", HttpMethod.GET, null, responseType, mlId).getBody();
            }
        };
    }

    @HystrixCommand(fallbackMethod = "stubLikes")
    public Observable<Boolean> likes(final String userName, final String mlId) {
        return new ObservableResult<Boolean>() {
            @Override
            public Boolean invoke() {
                return restTemplate.getForObject("http://springbox-api-gateway/recommendations/does/{userName}/like/{mlId}", Boolean.class, userName, mlId);
            }
        };
    }

    private List<Movie> stubRecommendations(final String mlId) {
        Movie one = new Movie();
        one.setMlId("25");
        one.setMlId("A movie which doesn't exist");
        Movie two = new Movie();
        two.setMlId("26");
        two.setMlId("A movie about nothing");
        return Arrays.asList(one, two);
    }

    private Boolean stubLikes(final String userName, final String mlId) {
        return false;
    }

}

