package com.example.fhir_communication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static int SECRET_KEY = 99;

    private FirebaseFirestore firestore;
    private CollectionReference tasks;

    private FirebaseUser user;
    private FirebaseAuth auth;

    EditText adressedET;
    EditText subjectET;
    Spinner prioritySpinner;
    EditText contentET;
    EditText noteET;
    Spinner workStatusSpinner;

    Map<String, Object> data = new HashMap<>();

    private NotificationHandler notificationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        Bundle bundle = getIntent().getExtras();
        int secretKey = bundle.getInt("SECRET_KEY");

        if(secretKey != 99){
            finish();
        }

        adressedET = findViewById(R.id.adressedEditText);
        subjectET = findViewById(R.id.subjectEditText);
        prioritySpinner = findViewById(R.id.prioritySpinner);
        contentET = findViewById(R.id.contentEditText);
        noteET = findViewById(R.id.noteEditText);
        workStatusSpinner = findViewById(R.id.workStatusSpinner);

        prioritySpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.priorities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);

        workStatusSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.workstatuses, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workStatusSpinner.setAdapter(adapter2);

        user = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        notificationHandler = new NotificationHandler(this);
    }

    public void send(View view) {
        String adressed = adressedET.getText().toString();
        String subject = subjectET.getText().toString();
        String priority = prioritySpinner.getSelectedItem().toString();
        String content = contentET.getText().toString();
        String note = noteET.getText().toString();
        String workstatus = workStatusSpinner.getSelectedItem().toString();

        tasks = firestore.collection("Tasks");

        data.put("content", content);
        data.put("note", note);
        data.put("priority", priority);
        data.put("recipient", adressed);
        data.put("sender", user.getEmail());
        data.put("senderStatus", workstatus);
        data.put("subject", subject);
        data.put("status", "Nincs kész");

        tasks.add(data);

        //add notification

        notificationHandler.send("Feladat elküldve!");

        finish();


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}