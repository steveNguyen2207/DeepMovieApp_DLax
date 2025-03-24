package com.vn.nguyenhoanganhtu.deepmovieapp.utils;

import com.vn.nguyenhoanganhtu.deepmovieapp.models.Movie;

import java.util.List;

public class DataCenter {
    private static DataCenter instance;
    private List<Movie> movies;

    private DataCenter() {}

    public static synchronized DataCenter getInstance() {
        if (instance == null) {
            instance = new DataCenter();
        }
        return instance;
    }

    public List<Movie> getListMovie() {
        return movies;
    }

    public void setListMovie(List<Movie> movie) {
        this.movies = movie;
    }
}
