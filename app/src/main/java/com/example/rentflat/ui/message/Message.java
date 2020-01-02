package com.example.rentflat.ui.message;

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

    private int messageId, senderId, recipientId;
    private String title, description,date;

    private Message(Parcel in) {
        this.messageId = in.readInt();
        this.senderId = in.readInt();
        this.recipientId = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.date = in.readString();
    }

    public Message(int messageId, int senderId, int recipientId, String title, String description, String date) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.recipientId = recipientId;
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
        out.writeInt(messageId);
        out.writeInt(senderId);
        out.writeInt(recipientId);
        out.writeString(title);
        out.writeString(description);
        out.writeString(date);

    }

    public int getMessageId() {
        return this.messageId;
    }

    public int getSenderId() {
        return this.senderId;
    }

    public int getRecipentId() {
        return this.recipientId;
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
