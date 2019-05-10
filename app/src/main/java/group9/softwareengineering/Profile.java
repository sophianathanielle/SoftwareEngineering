package group9.softwareengineering;

import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class Profile {

    private String name;
    private String phone_number;
    private String bio;
    private int fee_ph;
    private GeoPoint location;
    private double rating;
    private int ratingsOf1 , ratingsOf2 , ratingsOf3, ratingsOf4, ratingsOf5;

    public Profile(){

    }

    public Profile(String name, String phone_number) {
        this.name = name;
        this.phone_number = phone_number;
        this.fee_ph = 1;
        this.rating = 0.0;
        this.ratingsOf1 = 0;
        this.ratingsOf2 = 0;
        this.ratingsOf3 = 0;
        this.ratingsOf4 = 0;
        this.ratingsOf5 = 0;

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getFee_ph() {
        return fee_ph;
    }

    public void setFee_ph(int fee_ph) {
        this.fee_ph = fee_ph;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public double getRating() {
        return rating;
    }

    public int getRatingsOf1() {
        return ratingsOf1;
    }

    public int getRatingsOf2() {
        return ratingsOf2;
    }

    public int getRatingsOf3() {
        return ratingsOf3;
    }

    public int getRatingsOf4() {
        return ratingsOf4;
    }

    public int getRatingsOf5() {
        return ratingsOf5;
    }
}

