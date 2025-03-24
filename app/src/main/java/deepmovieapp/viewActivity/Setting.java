package com.vn.nguyenhoanganhtu.deepmovieapp.viewActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.vn.nguyenhoanganhtu.deepmovieapp.R;
import com.vn.nguyenhoanganhtu.deepmovieapp.models.User;
import com.vn.nguyenhoanganhtu.deepmovieapp.viewFragment.ContentMovieFragment;
import com.vn.nguyenhoanganhtu.deepmovieapp.viewFragment.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

public class Setting extends AppCompatActivity {
    private Button btnChangePassword;
    private Button btnHelp;
    private ImageView imgBack;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore firestore;
    private ImageView imgProfile;
    private TextView txtName;

    void bindingView() {
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnHelp = findViewById(R.id.btnHelp);
        imgBack = findViewById(R.id.imgBack);
        imgProfile = findViewById(R.id.imgProfile);
        txtName = findViewById(R.id.txtName);
    }

    void bindingAction() {
        btnChangePassword.setOnClickListener(this::onClickChangPass);
        btnHelp.setOnClickListener(this::onClickBtnHelp);
        imgBack.setOnClickListener(this::onClickBtnBack);
    }

    private void onClickBtnBack(View view) {
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
        finish();
    }

    void setupProfile() {
        CollectionReference collectionRef = firestore.collection("user");
        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<User> userList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        userList.add(user);
                    }
                    for (User user: userList) {
                        Log.w("sy.nguyenvan", user.getUserId() + "---" + currentUser.getUid());
                        if (user.getUserId().contentEquals(currentUser.getUid())) {
                            String imageUrl = user.getProfile();
                            Picasso.get().load(imageUrl).into(imgProfile);
                            txtName.setText(user.getName());
                        }
                    }
                } else {
                    Exception e = task.getException();
                }
            }
        });
    }

    private void onClickBtnHelp(View view) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:0917076864"));
        startActivity(callIntent);
    }

    private void onClickChangPass(View view) {
        Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        bindingView();
        bindingAction();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        setupProfile();
    }
}