package com.whyte.ratingsdataservice.resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whyte.ratingsdataservice.model.Rating;
import com.whyte.ratingsdataservice.model.UserRating;

@RestController
@RequestMapping("/ratingsdata")
public class RatingsResource {
	
	@RequestMapping("/{movieId}")
    public Rating getRating(@PathVariable("movieId") String movieId) {
        return new Rating(movieId, 4);
    }
	
	@RequestMapping("users/{userId}")
	public UserRating getUserRating(@PathVariable("userId") String userId) {
		/*The ratings data is created by the init function. This data now contains real movie ids. Once the 
		 * movie catalog service gets back this data it will now use those real ids to call movie info service like usual
		 * however this service now makes a call to an external API called MovieDB and retrieve real movie data. Instead of it being hard
		 * coded*/
		UserRating userRating = new UserRating();
        userRating.initData(userId);
        return userRating;
	}
}
