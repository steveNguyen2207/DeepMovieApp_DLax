package com.vn.nguyenhoanganhtu.deepmovieapp.utils;

import com.vn.nguyenhoanganhtu.deepmovieapp.models.Movie;

import java.util.List;

import io.reactivex.rxjava3.subjects.PublishSubject;

public class DataRepository {
    private static DataRepository instance;
    private PublishSubject<List<Movie>> dataSubject;

    private DataRepository() {
        dataSubject = PublishSubject.create();
    }

    public static DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }

    public void setData(List<Movie> data) {
        dataSubject.onNext(data);
    }

    public PublishSubject<List<Movie>> getDataObservable() {
        return dataSubject;
    }
}

