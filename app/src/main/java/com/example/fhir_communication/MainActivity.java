package com.example.fhir_communication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int SECRET_KEY = 99;

    EditText usernameET;
    EditText passwordET;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameET = findViewById(R.id.editTextUserName);
        passwordET = findViewById(R.id.editTextPassword);

        auth = FirebaseAuth.getInstance();
    }

    public void login(View view) {

        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();

        // Log.i(LOG_TAG, "Bejelentkezett: " + username + ", jelszó: " + password);

        auth.signInWithEmailAndPassword(username,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG, "User logged in succesfully");
                    startTasksActivity();
                }else{
                    Log.d(LOG_TAG, "User login failed");
                }
            }
        });
    }

    public void startTasksActivity(){
        Intent intent = new Intent(this, TasksActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }
}