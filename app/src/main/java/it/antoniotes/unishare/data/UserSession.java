package it.antoniotes.unishare.data;

import android.content.SharedPreferences;

public class UserSession {
    private static UserSession instance;
    private String loggedInUsername;
    private int loggedID;

    private UserSession() {
    }

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public String getLoggedInUsername() {
        return loggedInUsername;
    }

    public void setLoggedInUsername(String username) {
        loggedInUsername = username;
    }

    public int getLoggedID() {
        return loggedID;
    }

    public void setLoggedID(int loggedID) {
        this.loggedID = loggedID;
    }

    public void clearUserData() {
        loggedInUsername = null;
    }

}
