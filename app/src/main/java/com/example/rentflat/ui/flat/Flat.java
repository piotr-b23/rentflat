package com.example.rentflat.ui.flat;

import java.util.ArrayList;
import java.util.Scanner;

import static com.example.rentflat.MainActivity.serverIp;

public class Flat {
    private String flatId,flatUserId, price, surface, room,province, type, locality, street, description, students, photoAdress;
    private String photoLocation = serverIp;

    public Flat(String flatId,String flatUserId,String price,String surface,String room,String province,String type,String locality,String street,String description,String students, String photo){

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

    }

    public String generateTitle(){
        String title;

        title = this.price + "zł " + this.surface + "m2 ";

        return title;
    }

    public String generateDescription(){
        String recyclerDescription;

        recyclerDescription = this.type + "\nIlość pokoi: " + this.room + "\nMiejscowość: " +this.locality +"\nUlica: "+this.street;
        if(this.students.equals("1")) recyclerDescription +="\nMieszkanie studenckie: tak";
        else recyclerDescription +="\nMieszkanie studenckie: nie";


        return recyclerDescription;
    }

    public ArrayList<String> generatePhotos(){
        Scanner s = new Scanner(this.photoAdress);

        ArrayList<String> photos = new ArrayList<>();

        while (s.hasNext()){
            photos.add(photoLocation + s.next());
        }
        return photos;
    }
}
