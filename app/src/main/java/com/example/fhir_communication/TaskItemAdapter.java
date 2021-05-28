package com.example.fhir_communication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TaskItemAdapter extends RecyclerView.Adapter<TaskItemAdapter.ViewHolder> {
    private static final String LOG_TAG = TaskItemAdapter.class.getName();

    private ArrayList<TaskItem> tasksItemsData;
    private Context context;
    private int lastPosition = -1;

    TaskItemAdapter(Context context, ArrayList<TaskItem> itemsData){
        this.tasksItemsData = itemsData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.task_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TaskItemAdapter.ViewHolder holder, int position) {
        TaskItem currentItem = tasksItemsData.get(position);
        holder.bindTo(currentItem);
    }

    @Override
    public int getItemCount() {
        return tasksItemsData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView subjectText;
        private TextView priorityText;
        private TextView statusText;
        private TextView contentText;
        private TextView noteText;
        private TextView senderText;
        private TextView senderStatusText;
        private TextView recipientText;

        public ViewHolder(View taskView){
            super(taskView);

            subjectText = taskView.findViewById(R.id.taskSubject);
            priorityText = taskView.findViewById(R.id.taskProirity);
            statusText = taskView.findViewById(R.id.taskStatus);
            contentText = taskView.findViewById(R.id.taskContent);
            noteText = taskView.findViewById(R.id.taskNote);
            senderText = taskView.findViewById(R.id.taskSender);
            senderStatusText = taskView.findViewById(R.id.taskSenderStatus);
            recipientText = taskView.findViewById(R.id.taskRecipient);
        }

        public void bindTo(TaskItem currentItem) {
            subjectText.setText(currentItem.getSubject());
            priorityText.setText(currentItem.getPriority());
            statusText.setText(currentItem.getStatus());
            contentText.setText(currentItem.getContent());
            noteText.setText(currentItem.getNote());
            senderText.setText(currentItem.getSender());
            senderStatusText.setText(currentItem.getSenderStatus());
            recipientText.setText(currentItem.getRecipient());

            itemView.findViewById(R.id.in_progress_button).setOnClickListener(view -> ((TasksActivity)context).updateTask(currentItem, "Folyamatban"));
            itemView.findViewById(R.id.completed_button).setOnClickListener(view -> ((TasksActivity)context).updateTask(currentItem, "Kész"));
            itemView.findViewById(R.id.delete_button).setOnClickListener(view -> ((TasksActivity)context).deleteTask(currentItem));
        }
    };

}
