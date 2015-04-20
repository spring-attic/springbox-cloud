package io.springbox.recommendations.repositories;

import io.springbox.recommendations.domain.Likes;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;

public interface LikesRepository extends GraphRepository<Likes> {

    @Query("MATCH (m:Movie {mlId:{0}})<-[likes:LIKES]-(p:Person {userName:{1}}) return likes")
    List<Likes> likesFor(String mlId, String userName);
}
