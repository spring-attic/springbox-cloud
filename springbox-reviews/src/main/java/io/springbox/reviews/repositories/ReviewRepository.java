package io.springbox.reviews.repositories;

import io.springbox.reviews.domain.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<Review, String> {
    Iterable<Review> findByMlId(String mlId);
}
