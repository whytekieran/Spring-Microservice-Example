package com.whyte.moviecatalogservice.resource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.whyte.moviecatalogservice.model.CatalogItem;
import com.whyte.moviecatalogservice.model.Movie;
import com.whyte.moviecatalogservice.model.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	
	@Autowired
    private RestTemplate restTemplate;
	
	/*Was put here as an example of using web client, uncomment to use and add commented code at bottom of this class instead of
	 * rest template request for movie also uncomment bean method inside MovieCatalogServiceApplication.java */
	//@Autowired
    //private WebClient.Builder webClientBuilder;
	
	/*The DiscoveryClient object is a more verbose and programmatic way of using Eureka discovery service. This way you can pass in the
	 * service ID and get a list of its related instances along with various useful metadata about each of them. You can loop
	 * though them, load balance between them, hit certain instances under certain conditions etc*/
	//@Autowired
	//private DiscoveryClient discoveryClient;
	
	@RequestMapping("/{userId}")
	/*Telling Hystrix this method should not cause everything to go down, apply the circuit breaker pattern to this method
	 * it can take many parameters, one of them sets the method to be called when the circuit breaks.*/
	@HystrixCommand(fallbackMethod = "getFallbackCatalog")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
		
		//get all movie id's and ratings for a particular use done without service discovery
		//UserRating userRating = restTemplate.getForObject("http://localhost:8082/ratingsdata/users/" + userId, UserRating.class);
		
		//After setting up eureka service discovery we can state the service name instead
		UserRating userRating = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId, UserRating.class);

		/*Use the list of returned movie Id's to get details of each movie (call movie info service for each movie id
		 * using the RestTemplate class)*/
        return userRating.getRatings().stream()
        		.map(rating -> {
        			//For each rating object make a call, set response as Movie.class because I know its returning movie objects. (no service discovery)
                    //Movie movie = restTemplate.getForObject("http://localhost:8081/movies/" + rating.getMovieId(), Movie.class);
                    //Done using Eureka service discovery
                    Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
                    //then put all this data together and return it.
                    return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
                })
        		.collect(Collectors.toList());	
	}
	
	//This is a fallback method for if the circuit breaker is active.
	public List<CatalogItem> getFallbackCatalog(@PathVariable("userId") String userId) {
		/*Just returns some hard coded list of items. Dont make another call just in case that fails, the most you
		 * want to do is get something from the cache*/
		return Arrays.asList(new CatalogItem("No Movie", "", 0));
	}
}

/*ALTERNATIVE - Or do it the web client way (webflux, async, event driven reactive programming)
mono is a reactive to saying "You will get an object back, but not right away"
technically this is still synchronous because block() says stop execution until 
the request returns a response. If you really wanted to to async you could return a mono object
in this method which basically says. "Im returning an empty container but an object is coming." */
/*Movie movie = webClientBuilder.build()
		.get()
		.uri("http://localhost:8081/movies/"+ rating.getMovieId())
		.retrieve()
		.bodyToMono(Movie.class)
		.block();		
*/

/*
 * When we use Hystrix like above it actually wraps this entire class in a proxy class. So when this class creates an "instance" we actually
 * get back an instance of this proxy class that Hystrix has created. It is this proxy class that actually contains the circuit breaker
 * logic. So when somebody makes a call Hystrix is constantly monitoring what its sending back and forth. It checks the call, examines the 
 * response etc. 
 * */
