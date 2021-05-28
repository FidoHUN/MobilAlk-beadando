package com.example.fhir_communication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.*;

public class TasksActivity extends AppCompatActivity {
    private static final String LOG_TAG = TasksActivity.class.getName();
    private static final int SECRET_KEY = 99;

    private FirebaseUser user;
    private FirebaseAuth auth;

    private RecyclerView recyclerView;
    private ArrayList<TaskItem> taskList;
    private TaskItemAdapter adapter;

    private FirebaseFirestore firestore;
    private CollectionReference tasks;

    private int collectionSize;

//    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
//    View inflatedView = inflater.inflate(R.layout.task_item, null);
//    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        Bundle bundle = getIntent().getExtras();
        int secretKey = bundle.getInt("SECRET_KEY");

        if(secretKey != 99){
            finish();
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        auth = FirebaseAuth.getInstance();

        if(user != null){
            Log.d(LOG_TAG, "Authenticated user " + user.getEmail());
        }else{
            Log.d(LOG_TAG, "Unauthenticated user");
            finish();
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        taskList = new ArrayList<>();

        adapter = new TaskItemAdapter(this, taskList);
        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        tasks = firestore.collection("Tasks");

//        deleteButton = (Button) inflatedView.findViewById(R.id.delete_button);
//        deleteButton.setVisibility(VISIBLE);

        tasks.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                collectionSize = queryDocumentSnapshots.size();
                Log.d(LOG_TAG, String.valueOf(collectionSize));
                if(collectionSize == 0){
                    initalizeData();
                    Log.d(LOG_TAG,"Új adatok kerültek be!");
                }

                queryData();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task_list_menu, menu);
        return true;
    }

    public void queryData(){
        taskList.clear();

        tasks.orderBy("status")
                .whereEqualTo("recipient", user.getEmail())
                .whereNotEqualTo("status", "Kész")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                            TaskItem item = document.toObject(TaskItem.class);
                            item.setId(document.getId());
                            taskList.add(item);
                        }

                        adapter.notifyDataSetChanged();
                    }
                });
    }

    public void initalizeData() {
        String[] tasksSubject = getResources().getStringArray(R.array.task_subjects);
        String[] tasksPriority = getResources().getStringArray(R.array.task_priorities);
        String[] tasksStatus = getResources().getStringArray(R.array.task_statuses);
        String[] tasksContent = getResources().getStringArray(R.array.task_contents);
        String[] tasksNote = getResources().getStringArray(R.array.task_notes);
        String[] tasksSender = getResources().getStringArray(R.array.task_senders);
        String[] tasksSenderStatus = getResources().getStringArray(R.array.task_sender_statuses);
        String[] tasksRecipient = getResources().getStringArray(R.array.task_recipients);

        for(int i = 0 ; i < tasksSubject.length ; i++){
            tasks.add(new TaskItem(tasksSubject[i], tasksPriority[i], tasksStatus[i], tasksContent[i], tasksNote[i], tasksSender[i], tasksSenderStatus[i], tasksRecipient[i]));
        }
    }

    public void updateTask(TaskItem item, String newStatus){
        tasks.document(item._getId()).update("status", newStatus).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(LOG_TAG, "Item cannot be changed");
            }
        });

        queryData();
    }

    public void makeTask(MenuItem item) {
        Intent intent = new Intent(this, CreateTaskActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    public void logout(MenuItem item) {
        auth.signOut();
        finish();
    }

    public void deleteTask(TaskItem currentItem) {
        DocumentReference ref = tasks.document(currentItem._getId());
        ref.delete();
        queryData();
    }
}