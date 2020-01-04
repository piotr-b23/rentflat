package com.example.rentflat.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Scanner;

import static com.example.rentflat.ui.MainActivity.serverIp;

public class Flat implements Parcelable {
    public static final Parcelable.Creator<Flat> CREATOR = new Parcelable.Creator<Flat>() {
        public Flat createFromParcel(Parcel in) {
            return new Flat(in);
        }

        public Flat[] newArray(int size) {
            return new Flat[size];
        }
    };
    private int flatId, flatUserId, price, surface, room;
    private String province, type, locality, street, description, students, photoAddress, date;
    private String photoLocation = serverIp;


    private Flat(Parcel in) {
        this.flatId = in.readInt();
        this.flatUserId = in.readInt();
        this.price = in.readInt();
        this.surface = in.readInt();
        this.room = in.readInt();
        this.province = in.readString();
        this.type = in.readString();
        this.locality = in.readString();
        this.street = in.readString();
        this.description = in.readString();
        this.students = in.readString();
        this.photoAddress = in.readString();
        this.date = in.readString();
    }

    public Flat(int flatId, int flatUserId, int price, int surface, int room, String province, String type, String locality, String street, String description, String students, String photo, String date) {

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
        this.photoAddress = photo;
        this.date = date;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(flatId);
        out.writeInt(flatUserId);
        out.writeInt(price);
        out.writeInt(surface);
        out.writeInt(room);
        out.writeString(province);
        out.writeString(type);
        out.writeString(locality);
        out.writeString(street);
        out.writeString(description);
        out.writeString(students);
        out.writeString(photoAddress);
        out.writeString(date);

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
        Scanner s = new Scanner(this.photoAddress);

        ArrayList<String> photos = new ArrayList<>();

        while (s.hasNext()) {
            photos.add(photoLocation + s.next());
        }
        return photos;
    }

    public ArrayList<String> generatePhotosToDisplay() {

        Scanner s = new Scanner(this.photoAddress);
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

    public int getFlatId() {
        return this.flatId;
    }

    public int getUserId() {
        return this.flatUserId;
    }

    public int getPrice() {
        return this.price;
    }

    public int getSurface() {
        return this.surface;
    }

    public int getRoom() {
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
