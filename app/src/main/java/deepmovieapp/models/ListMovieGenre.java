package com.vn.nguyenhoanganhtu.deepmovieapp.models;

import java.util.List;

public class ListMovieGenre {

    private String genre;
    private List<Movie> movies;

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public ListMovieGenre(String genre, List<Movie> movies) {
        this.genre = genre;
        this.movies = movies;
    }
}
