package com.example.rentflat.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Rate implements Parcelable {

    public static final Parcelable.Creator<Rate> CREATOR = new Parcelable.Creator<Rate>() {
        public Rate createFromParcel(Parcel in) {
            return new Rate(in);
        }

        public Rate[] newArray(int size) {
            return new Rate[size];
        }
    };
    private String rateId, userId, rateDescription, date;
    private Float contactRate, descriptionRate;

    private Rate(Parcel in) {
        this.rateId = in.readString();
        this.userId = in.readString();
        this.rateDescription = in.readString();
        this.date = in.readString();
        this.contactRate = in.readFloat();
        this.descriptionRate = in.readFloat();
    }

    public Rate(String rateId, String userId, String rateDescription, String date, float contactRate, float descriptionRate) {
        this.rateId = rateId;
        this.userId = userId;
        this.rateDescription = rateDescription;
        this.date = date;
        this.contactRate = contactRate;
        this.descriptionRate = descriptionRate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(rateId);
        out.writeString(userId);
        out.writeString(rateDescription);
        out.writeString(date);
        out.writeFloat(contactRate);
        out.writeFloat(descriptionRate);

    }

    public String getRateId() {
        return this.rateId;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getRateDescription() {
        return this.rateDescription;
    }

    public String getDate() {
        return this.date;
    }

    public float getContactRate() {
        return this.contactRate;
    }

    public float getDescriptionRate() {
        return this.descriptionRate;
    }


}
