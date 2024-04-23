package com.example.itube;

public class Playlist {
    private String name;
    private int userID;
    private String videoURL;

    public Playlist(String name, int userID, String videoURL) {
        this.name = name;
        this.userID = userID;
        this.videoURL = videoURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }
}
