package com.whyte.movieinfoservice.model;

public class MovieSummary {
	//The json response would have more but we are only interested in these fields
	private String id;
    private String title;
    private String overview;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
