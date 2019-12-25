package com.example.rentflat.ui.flat;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import static com.example.rentflat.MainActivity.serverIp;

public class Flat implements Parcelable {
    private String flatId, flatUserId, price, surface, room, province, type, locality, street, description, students, photoAdress, date;
    private String photoLocation = serverIp;

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(flatId);
        out.writeString(flatUserId);
        out.writeString(price);
        out.writeString(surface);
        out.writeString(room);
        out.writeString(province);
        out.writeString(type);
        out.writeString(locality);
        out.writeString(street);
        out.writeString(description);
        out.writeString(students);
        out.writeString(photoAdress);
        out.writeString(date);

    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Flat> CREATOR = new Parcelable.Creator<Flat>() {
        public Flat createFromParcel(Parcel in) {
            return new Flat(in);
        }

        public Flat[] newArray(int size) {
            return new Flat[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Flat(Parcel in) {
        this.flatId = in.readString();
        this.flatUserId = in.readString();
        this.price = in.readString();
        this.surface = in.readString();
        this.room = in.readString();
        this.province = in.readString();
        this.type = in.readString();
        this.locality = in.readString();
        this.street = in.readString();
        this.description = in.readString();
        this.students = in.readString();
        this.photoAdress = in.readString();
        this.date = in.readString();
    }

    public Flat(String flatId, String flatUserId, String price, String surface, String room, String province, String type, String locality, String street, String description, String students, String photo,String date) {

        this.flatId = flatId;
        this.flatUserId = flatUserId;
        this.price = price;
        this.surface = surface;
        this.room = room;
        this.province = province;
        this.type = type;
        this.locality = locality;
        this.street = street;
        this.description = description;
        this.students = students;
        this.photoAdress = photo;
        this.date = date;

    }

    public String generateTitle() {
        String title;

        title = this.price + "zł " + this.surface + "m2 ";

        return title;
    }

    public String generateDescription() {
        String recyclerDescription;

        recyclerDescription = this.type + "\nIlość pokoi: " + this.room + "\nMiejscowość: " + this.locality + "\nUlica: " + this.street;
        if (this.students.equals("1")) recyclerDescription += "\nMieszkanie studenckie: tak";
        else recyclerDescription += "\nMieszkanie studenckie: nie";


        return recyclerDescription;
    }

    public ArrayList<String> generatePhotos() {
        Scanner s = new Scanner(this.photoAdress);

        ArrayList<String> photos = new ArrayList<>();

        while (s.hasNext()) {
            photos.add(photoLocation + s.next());
        }
        return photos;
    }

    public ArrayList<String> generatePhotosToDisplay() {

        Scanner s = new Scanner(this.photoAdress);
        String twoPhotos;

        ArrayList<String> photos = new ArrayList<>();

        while (s.hasNext()) {
            twoPhotos = photoLocation + s.next();
            if (s.hasNext()) {
                twoPhotos += " " + photoLocation + s.next();
                photos.add(twoPhotos);
            } else {
                photos.add(twoPhotos);
            }
        }
        return photos;
    }

    public String getFlatId() {
        return this.flatId;
    }

    public String getUserId() {
        return this.flatUserId;
    }

    public String getPrice() {
        return this.price;
    }

    public String getSurface() {
        return this.surface;
    }

    public String getRoom() {
        return this.room;
    }

    public String getType() {
        return this.type;
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

    public String getDescription() {
        return this.description;
    }

    public String getStudents() {
        return this.students;
    }

    public String getDate() {
        return this.date;
    }
}
