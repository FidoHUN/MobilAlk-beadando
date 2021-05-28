package com.example.fhir_communication;

public class TaskItem {
    private String id;
    private String subject;
    private String priority;
    private String status;
    private String content;
    private String note;
    private String sender;
    private String senderStatus;
    private String recipient;

    public TaskItem(){}

    public TaskItem(String subject, String priority, String status, String content, String note, String sender, String senderStatus, String recipient) {
        this.subject = subject;
        this.priority = priority;
        this.status = status;
        this.content = content;
        this.note = note;
        this.sender = sender;
        this.senderStatus = senderStatus;
        this.recipient = recipient;
    }

    public String getSubject(){ return subject; }
    public String getPriority(){ return priority; }
    public String getStatus(){ return status; }
    public String getContent(){ return content; }
    public String getNote(){ return note; }
    public String getSender(){ return sender; }
    public String getSenderStatus(){ return senderStatus; }
    public String getRecipient(){ return recipient; }

    public String _getId(){ return id; }
    public void setId(String id){ this.id = id; }
}
