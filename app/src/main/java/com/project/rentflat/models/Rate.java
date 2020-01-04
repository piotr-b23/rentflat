package com.project.rentflat.models;

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
    private int rateId, userId;
    private String rateDescription, date;
    private Float contactRate, descriptionRate;

    private Rate(Parcel in) {
        this.rateId = in.readInt();
        this.userId = in.readInt();
        this.rateDescription = in.readString();
        this.date = in.readString();
        this.contactRate = in.readFloat();
        this.descriptionRate = in.readFloat();
    }

    public Rate(int rateId, int userId, String rateDescription, String date, float contactRate, float descriptionRate) {
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
        out.writeInt(rateId);
        out.writeInt(userId);
        out.writeString(rateDescription);
        out.writeString(date);
        out.writeFloat(contactRate);
        out.writeFloat(descriptionRate);

    }

    public int getRateId() {
        return this.rateId;
    }

    public int getUserId() {
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
