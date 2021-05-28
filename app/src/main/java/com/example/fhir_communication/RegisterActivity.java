package com.example.fhir_communication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static int SECRET_KEY = 99;

    EditText usernameET;
    EditText emailET;
    EditText passwordET;
    EditText passwordAgainET;
    Spinner spinner;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Bundle bundle = getIntent().getExtras();
        int secretKey = bundle.getInt("SECRET_KEY");

        if(secretKey != 99){
            finish();
        }

        usernameET = findViewById(R.id.userNameEditText);
        emailET = findViewById(R.id.emailAddressEditText);
        passwordET = findViewById(R.id.passwordEditText);
        passwordAgainET = findViewById(R.id.passwordAgainEditText);
        spinner = findViewById(R.id.typeSpinner);

        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.workstatuses, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
    }

    public void startTasksActivity(){
        Intent intent = new Intent(this, TasksActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    public void register(View view) {
        String username = usernameET.getText().toString();
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordAgain = passwordAgainET.getText().toString();
        String type = spinner.getSelectedItem().toString();

        if(!password.equals(passwordAgain)){
            return;
        }

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task != null){
                    Log.d(LOG_TAG, "User created succesfully");
                    startTasksActivity();
                }else{
                    Log.d(LOG_TAG, "User not created succesfully");
                }
            }
        });
    }

    public void cancel(View view) {
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}