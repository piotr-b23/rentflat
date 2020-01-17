package com.project.rentflat.models;

import android.os.Parcel;
import android.os.Parcelable;

public class FlatSearch implements Parcelable {
    public static final Parcelable.Creator<FlatSearch> CREATOR = new Parcelable.Creator<FlatSearch>() {
        public FlatSearch createFromParcel(Parcel in) {
            return new FlatSearch(in);
        }

        public FlatSearch[] newArray(int size) {
            return new FlatSearch[size];
        }
    };
    int priceMin, priceMax, surfaceMin, surfaceMax, roomMin, roomMax;
    String buildingType, province, locality, street, studentsCheckBox;

    private FlatSearch(Parcel in) {
        this.priceMin = in.readInt();
        this.priceMax = in.readInt();
        this.surfaceMin = in.readInt();
        this.surfaceMax = in.readInt();
        this.roomMin = in.readInt();
        this.roomMax = in.readInt();
        this.buildingType = in.readString();
        this.province = in.readString();
        this.locality = in.readString();
        this.street = in.readString();
        this.studentsCheckBox = in.readString();
    }

    public FlatSearch(int priceMin, int priceMax, int surfaceMin, int surfaceMax, int roomMin, int roomMax, String buildingType, String province, String locality, String street, String studentsCheckBox) {

        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.surfaceMin = surfaceMin;
        this.surfaceMax = surfaceMax;
        this.roomMin = roomMin;
        this.roomMax = roomMax;
        this.buildingType = buildingType;
        this.province = province;
        this.locality = locality;
        this.street = street;
        this.studentsCheckBox = studentsCheckBox;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(priceMin);
        out.writeInt(priceMax);
        out.writeInt(surfaceMin);
        out.writeInt(surfaceMax);
        out.writeInt(roomMin);
        out.writeInt(roomMax);
        out.writeString(buildingType);
        out.writeString(province);
        out.writeString(locality);
        out.writeString(street);
        out.writeString(studentsCheckBox);

    }

    public boolean checkPrice(){
        if (this.priceMin>this.priceMax) return false;
        else return true;
    }

    public boolean checkSurface(){
        if (this.surfaceMin>this.surfaceMax) return false;
        else return true;
    }

    public boolean checkRoom(){
        if (this.roomMin>this.roomMax) return false;
        else return true;
    }

    public int getPriceMin() {
        return this.priceMin;
    }

    public int getPriceMax() {
        return this.priceMax;
    }

    public int getSurfaceMin() {
        return this.surfaceMin;
    }

    public int getSurfaceMax() {
        return this.surfaceMax;
    }

    public int getRoomMin() {
        return this.roomMin;
    }

    public int getRoomMax() {
        return this.roomMax;
    }

    public String getBuildingType() {
        return this.buildingType;
    }

    public String getProvince() {
        return this.province;
    }

    public String getLocality() {
        return this.locality;
    }

    public String getStreet() {
        return this.street;
    }

    public String getStudentsCheckBox() {
        return this.studentsCheckBox;
    }
}
