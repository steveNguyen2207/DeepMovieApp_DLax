package com.vn.nguyenhoanganhtu.deepmovieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vn.nguyenhoanganhtu.deepmovieapp.utils.DataCenter;
import com.vn.nguyenhoanganhtu.deepmovieapp.utils.FirebaseUtils;
import com.vn.nguyenhoanganhtu.deepmovieapp.viewActivity.GetStarted;
import com.vn.nguyenhoanganhtu.deepmovieapp.viewActivity.Login;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final long DELAY_TIME = 3000; // Thời gian chờ trước khi chuyển đổi (đơn vị: millisecond)

    void bindingDataFirebase() {
        FirebaseUtils firebaseUtils = FirebaseUtils.getInstance();
        firebaseUtils.handleListMovieFromFirestore();
    }
    void setUpTimeCallActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, DELAY_TIME);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindingDataFirebase();
        setUpTimeCallActivity();
    }
}