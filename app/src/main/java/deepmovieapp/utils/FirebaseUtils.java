package com.vn.nguyenhoanganhtu.deepmovieapp.utils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vn.nguyenhoanganhtu.deepmovieapp.models.ListMovieGenre;
import com.vn.nguyenhoanganhtu.deepmovieapp.models.Movie;
import com.vn.nguyenhoanganhtu.deepmovieapp.models.User;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUtils {
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseUser currentUser = mAuth.getCurrentUser();
    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    List<Movie> listMovies = new ArrayList<>();

    private static FirebaseUtils instance;
    private FirebaseUtils() {}
    public static synchronized FirebaseUtils getInstance() {
        if (instance == null) {
            instance = new FirebaseUtils();
        }
        return instance;
    }

    public void handleListMovieFromFirestore() {
        CollectionReference movieCollectionRef = firestore.collection("movies");

        movieCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Xử lý dữ liệu khi lấy thành công
                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    // Lấy thông tin từ DocumentSnapshot và thêm vào movieList
                    Movie movie = document.toObject(Movie.class);
                    listMovies.add(movie);
                }
                DataCenter dataCenter = DataCenter.getInstance();
                dataCenter.setListMovie(listMovies);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Xử lý khi lấy dữ liệu thất bại
            }
        });
    }


}
