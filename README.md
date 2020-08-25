# Spring-Microservice-Example
Spring Microservice Example that has three services movie-catalog-service, ratings-data-service and movie-info-service. 

Brief overview of how it works.

1. Call is made to the movie-catalog-service (GET) http://localhost:8080/catalog/<random_username>

2. movie-catalog-service calls ratings-data-service and passes username and returns a list of movie ids and ratings to the movie-catalog-service (note in production 
you would return list of movies rated by that user however username really doesnt do anything here. You will always get the same hardcoded list of movie ids and ratings 
this is just an example, note the ids returned are actually proper ids for a 3rd party API)

3.The movie-catalog-service calls movie-info-service for each movie id returned back from ratings-data-service. The movie-info-service then calls a 3rd party API called
MovieDB to get real movie data for the movies with those ids. (I needed to create an account on MovieDB to get an API key for this then add into application.properties).
Then passes it back to movie-catalog-service

4. The movie-catalog-service consolidates all the data then returns it back to the end user. 

This application also introduces the basic idea of discovery services (using Eureka) and fault tolerance/resilience through timeouts and circuit breaker pattern (using Hystrix)
