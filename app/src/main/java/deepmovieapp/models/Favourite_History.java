package com.vn.nguyenhoanganhtu.deepmovieapp.models;

public class Favourite_History {

    private String userId;
    private int id;
    private String director;
    private String genre;
    private String title;
    private String urlImageMovie;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlImageMovie() {
        return urlImageMovie;
    }

    public void setUrlImageMovie(String urlImageMovie) {
        this.urlImageMovie = urlImageMovie;
    }

    public Favourite_History(String userId, int id, String director, String genre, String title, String urlImageMovie) {
        this.userId = userId;
        this.id = id;
        this.director = director;
        this.genre = genre;
        this.title = title;
        this.urlImageMovie = urlImageMovie;
    }

    public Favourite_History() {
    }
}
