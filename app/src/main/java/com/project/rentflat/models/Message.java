package com.project.rentflat.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {

    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    private String senderId, userName, title, description,date;

    private Message(Parcel in) {
        this.senderId = in.readString();
        this.userName = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.date = in.readString();
    }

    public Message(String senderId, String userName, String title, String description, String date) {
        this.senderId = senderId;
        this.userName = userName;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(senderId);
        out.writeString(userName);
        out.writeString(title);
        out.writeString(description);
        out.writeString(date);

    }

    public String generateMessage(){
        String generatedMessage;
        generatedMessage = "\n\nOdpowiedź do wiadomości wysłanej\ndnia " + this.getDate() + "\nużytkownik " + this.getUserName() + " napisał:\n" + this.getDescription();

        return generatedMessage;
    }


    public String getSenderId() {
        return this.senderId;
    }

    public String getUserName(){
        return this.userName;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDate() {
        return this.date;
    }


}
