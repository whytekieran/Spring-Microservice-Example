package com.whyte.moviecatalogservice.resource;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whyte.moviecatalogservice.model.CatalogItem;
import com.whyte.moviecatalogservice.model.UserRating;
import com.whyte.moviecatalogservice.service.MovieInfo;
import com.whyte.moviecatalogservice.service.UserRatingInfo;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	
	//Much more granular level of fallback, 
	@Autowired
	MovieInfo movieInfo;
	
	@Autowired
	UserRatingInfo userRatingInfo;
	
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
		
		UserRating userRating = userRatingInfo.getUserRating(userId);

        return userRating.getRatings().stream()
        		.map(rating -> {
                    return movieInfo.getCatalogItem(rating);
                })
        		.collect(Collectors.toList());	
	}
}
