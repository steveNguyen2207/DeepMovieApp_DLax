package com.vn.nguyenhoanganhtu.deepmovieapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Movie implements Parcelable {

    public Movie() {
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        urlImageMovie = in.readString();
        genre = in.readString();
        director = in.readString();
        releaseDate = in.readString();
        description = in.readString();
        urlMovie = in.readString();
        userId = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Movie(int id, String title, String urlImageMovie, String genre, String director, String releaseDate, String description, String urlMovie, String userId) {
        this.id = id;
        this.title = title;
        this.urlImageMovie = urlImageMovie;
        this.genre = genre;
        this.director = director;
        this.releaseDate = releaseDate;
        this.description = description;
        this.urlMovie = urlMovie;
        this.userId = userId;
    }

    private int id;
    private String title;
    private String urlImageMovie;
    private String genre;
    private String director;
    private String releaseDate;
    private String description;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUrlMovie() {
        return urlMovie;
    }

    public void setUrlMovie(String urlMovie) {
        this.urlMovie = urlMovie;
    }

    private String urlMovie;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(urlImageMovie);
        parcel.writeString(genre);
        parcel.writeString(director);
        parcel.writeString(releaseDate);
        parcel.writeString(description);
        parcel.writeString(urlMovie);
        parcel.writeString(userId);
    }
}
