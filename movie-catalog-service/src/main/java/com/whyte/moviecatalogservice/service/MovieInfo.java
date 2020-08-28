package com.whyte.moviecatalogservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.whyte.moviecatalogservice.model.CatalogItem;
import com.whyte.moviecatalogservice.model.Movie;
import com.whyte.moviecatalogservice.model.Rating;

@Service
public class MovieInfo {
	
	@Autowired
    private RestTemplate restTemplate;
	
	//Bulkhead pattern, create a separate thread pool for this service
	@HystrixCommand(fallbackMethod = "getFallbackCatalogItem", 
			threadPoolKey = "movieInfoPool",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "20"),
                    @HystrixProperty(name = "maxQueueSize", value = "10")
            })
	public CatalogItem getCatalogItem(Rating rating) {
		Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
		return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
	}
		
	public CatalogItem getFallbackCatalogItem(Rating rating) {
		return new CatalogItem("Movie name not found", "", rating.getRating());
	}
}
