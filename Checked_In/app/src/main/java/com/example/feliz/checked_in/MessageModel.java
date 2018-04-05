package com.example.feliz.checked_in;

/**
 * Created by Feliz on 2017/10/03.
 */

public class MessageModel
{

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String message;
    public Boolean send;

    public MessageModel(String message)
    {
        this.message = message;

    }

}
