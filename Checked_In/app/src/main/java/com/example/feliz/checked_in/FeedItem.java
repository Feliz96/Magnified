package com.example.feliz.checked_in;

/**
 * Created by Feliz on 2017/09/03.
 */

public class FeedItem {
    private String id;
    private String name, status, image, profilePic, timeStamp;

    public FeedItem() {
    }

    public FeedItem(String id, String name, String status, String timeStamp,String image,String profilePic) {
        super();
        this.id = id;
        this.name = name;
        this.status = status;
        this.timeStamp = timeStamp;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImge() {
        return image;
    }

    public void setImge(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

}