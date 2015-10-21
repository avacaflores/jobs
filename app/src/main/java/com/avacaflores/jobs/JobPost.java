package com.avacaflores.jobs;

import java.lang.String;public class JobPost {

    private int id;
    private String title;
    private String postDate;
    private String description;
    private String contacts;

    public JobPost() {
        id = 0;
        title = "";
        postDate = "";
        description = "";
        contacts = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }
}
