package com.whyte.moviecatalogservice.model;

import java.util.List;

//Stores list of rated movies for a user
public class UserRating {
	
	private String userId;
	private List<Rating> userRatings;
	
	public UserRating() {
		
	}
	
	public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Rating> getRatings() {
        return userRatings;
    }

    public void setRatings(List<Rating> ratings) {
        this.userRatings = ratings;
    }
}
