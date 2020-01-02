package com.example.rentflat.ui.findFlat;

import android.os.Parcel;
import android.os.Parcelable;

public class FindFlatSearch implements Parcelable {
    public static final Parcelable.Creator<FindFlatSearch> CREATOR = new Parcelable.Creator<FindFlatSearch>() {
        public FindFlatSearch createFromParcel(Parcel in) {
            return new FindFlatSearch(in);
        }

        public FindFlatSearch[] newArray(int size) {
            return new FindFlatSearch[size];
        }
    };
    String priceMin, priceMax, surfaceMin, surfaceMax, roomMin, roomMax, buildingType, province, locality, street, studentsCheckBox;

    private FindFlatSearch(Parcel in) {
        this.priceMin = in.readString();
        this.priceMax = in.readString();
        this.surfaceMin = in.readString();
        this.surfaceMax = in.readString();
        this.roomMin = in.readString();
        this.roomMax = in.readString();
        this.buildingType = in.readString();
        this.province = in.readString();
        this.locality = in.readString();
        this.street = in.readString();
        this.studentsCheckBox = in.readString();
    }

    public FindFlatSearch(String priceMin, String priceMax, String surfaceMin, String surfaceMax, String roomMin, String roomMax, String buildingType, String province, String locality, String street, String studentsCheckBox) {

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
        out.writeString(priceMin);
        out.writeString(priceMax);
        out.writeString(surfaceMin);
        out.writeString(surfaceMax);
        out.writeString(roomMin);
        out.writeString(roomMax);
        out.writeString(buildingType);
        out.writeString(province);
        out.writeString(locality);
        out.writeString(street);
        out.writeString(studentsCheckBox);

    }

    public String getPriceMin() {
        return this.priceMin;
    }

    public String getPriceMax() {
        return this.priceMax;
    }

    public String getSurfaceMin() {
        return this.surfaceMin;
    }

    public String getSurfaceMax() {
        return this.surfaceMax;
    }

    public String getRoomMin() {
        return this.roomMin;
    }

    public String getRoomMax() {
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
