package com.diary.apps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.diary.apps.databinding.ActivitySignInMasukBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInMasuk extends AppCompatActivity {

    ActivitySignInMasukBinding binding;
    FirebaseAuth auth;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignInMasukBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();

//      untuk keperluan menyimpan data sementara
        preferences = getSharedPreferences("kitasinau", MODE_PRIVATE);
        editor = preferences.edit();

//      Ketika di klik button Log In akan melakukan aksi
        binding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              inisialisasi
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                storeLoginuser(email,password);
            }

            //      Method storeLogInUser
            private void storeLoginuser(String email, String password) {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                          ketika button log in di klik lalu menuju ke dashbord gunakan startActivity
                            startActivity(new Intent(SignInMasuk.this,dashboard.class));
//                          menggunakan toast
                            Toast.makeText(SignInMasuk.this, "Selamat Datang", Toast.LENGTH_SHORT).show();
//                          agar ketika pencet kembali maka halaman tidak kembali ke halaman log in
                            finish();
                        }
                    }
                });
            }
        });

//      Ketika di klik button Create new account akan melakukan aksi
        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInMasuk.this, SignUp.class));

            }
        });

//      Method storeLogInUser
//        private void storeLoginuser(String email, String password){
//
//
//        }
    }
}
