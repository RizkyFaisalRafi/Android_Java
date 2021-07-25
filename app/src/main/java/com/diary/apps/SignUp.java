package com.diary.apps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.diary.apps.databinding.ActivitySignupBinding;
import com.diary.apps.model.UserDiary;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

//  Method library
//  Main binding
    ActivitySignupBinding binding;
//  Firebase authentication
    FirebaseAuth auth;
//  Loading sign up muter
    ProgressDialog dialog;
//  Realtime database
    DatabaseReference reference;
//  Akan menyimpan data sementara / lokal database. menyimpan variabel yang akan digunakan di hp
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//      inisialisasi Main Binding
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

//      inisialisasi method firebase authentication
        auth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(SignUp.this);

//        inisialisasi method progress dialog
        dialog = new ProgressDialog(SignUp.this);

//      inisialisasi ketika klik button create new account akan terkirim ke realtime firebase data user
        reference = FirebaseDatabase.getInstance().getReference("UserDiary");

//      Inisialisasi SharedPreferences
        preferences = getSharedPreferences("kitasinau", MODE_PRIVATE);
        editor = preferences.edit();

//      Ketika di klik button Log In akan melakukan aksi
        binding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, SignInMasuk.class));
            }
        });

//      Ketika di klik button Create new account akan melakukan aksi. setOnClickListener
//      Memanggil binding signup di xml
        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//              Untuk mendapatkan text yang di inputkan user
                String name = binding.name.getText().toString();
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                storeDataToFirebase(name,email,password);

            }
        });

//        binding.signup.setVisibility(View.GONE);

    }

    private void storeDataToFirebase(String name, String email, String password) {
//      mengatur text progress dialog please wait dan memunculkannya dengan show
        dialog.setMessage("Please Wait");
        dialog.show();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
//                    Log.d("Sukses", "Data Terkirim");

//                  user ini akan mengirimkan user id / unique id ke dalam database
                    FirebaseUser user = auth.getCurrentUser();
                    String unique = user.getUid();

                    UserDiary diary = new UserDiary(name,email,"");

                    reference.child(unique).setValue(diary).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
//                          memberhentikan progress dialog please wait ketika datanya sudah selesai
                            dialog.dismiss();

                            if (task.isSuccessful()){
                                Log.d("Sukses", "Data Terkirim");
                                editor.putString("unique", unique);
                                editor.commit();
                                Toast.makeText(SignUp.this, "Register Berhasil", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });

                }else {
                    Log.d("Gagal", "Gagal Bro");
                    Toast.makeText(SignUp.this, "Register Gagal", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}