package com.vn.nguyenhoanganhtu.deepmovieapp.viewActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.vn.nguyenhoanganhtu.deepmovieapp.R;

public class ForgotPassword extends AppCompatActivity {
    private Button btnForgot;
    private TextInputEditText edtEmailForgotValue;
    private ProgressBar prgLoadingg;
    private ImageView imgBack;
    private FirebaseAuth mAuth;

    @SuppressLint("WrongViewCast")
    void bindingView() {
        btnForgot = findViewById(R.id.btnForgot);
        edtEmailForgotValue = findViewById(R.id.edtEmailForgotValue);
        imgBack = findViewById(R.id.imgBackLog);
        prgLoadingg = findViewById(R.id.prgLoadingg);
    }

    void bindingAction() {
        imgBack.setOnClickListener(this::onClickBtnBack);
        btnForgot.setOnClickListener(this::onClickBtnForgot);
    }

    private void onClickBtnForgot(View view) {
        prgLoadingg.setVisibility(View.VISIBLE);
        String email = String.valueOf(edtEmailForgotValue.getText());

        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Email IsEmpty", Toast.LENGTH_SHORT).show();
            prgLoadingg.setVisibility(View.GONE);
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),
                                "Send password to email " + email + "successfull!",
                                Toast.LENGTH_SHORT).show();
                        prgLoadingg.setVisibility(View.GONE);
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Send password fail! \n" + email + "don' exist!",
                                Toast.LENGTH_SHORT).show();
                        prgLoadingg.setVisibility(View.GONE);
                    }
                });
    }

    private void onClickBtnBack(View view) {
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), Setting.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mAuth = FirebaseAuth.getInstance();
        bindingView();
        bindingAction();
    }
}