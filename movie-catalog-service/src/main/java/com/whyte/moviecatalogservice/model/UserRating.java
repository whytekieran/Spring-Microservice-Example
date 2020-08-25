package com.whyte.moviecatalogservice.model;

import java.util.List;

//Stores list of rated movies for a user
public class UserRating {
	
	private List<Rating> userRatings;
	
	public UserRating() {
		
	}

    public List<Rating> getRatings() {
        return userRatings;
    }

    public void setRatings(List<Rating> ratings) {
        this.userRatings = ratings;
    }
}
