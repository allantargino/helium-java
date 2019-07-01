package com.microsoft.azure.helium.app.actor;

import java.util.List;

import com.microsoft.azure.spring.data.cosmosdb.repository.DocumentDbRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface ActorsRepository extends DocumentDbRepository<Actor, String> {
     List<Actor> findByActorId(String actorId);
     List<Actor> findByTextSearchContaining(String actorName);
     //TODO: change to below when fixed: https://github.com/microsoft/spring-data-cosmosdb/issues/362
     //List<Actor> findByTextSearchContainingIgnoreCase(String actorName);
}