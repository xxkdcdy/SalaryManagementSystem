package com.ujs.salarymanagementsystem.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private int id_U;
    private String displayName;


    public LoggedInUser(int userId, String displayName) {
        this.id_U = userId;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getId_U(){return id_U;}
}