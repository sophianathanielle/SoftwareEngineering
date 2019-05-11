package group9.softwareengineering;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class Profile implements Parcelable {

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

    protected Profile(Parcel in) {
        name = in.readString();
        phone_number = in.readString();
        bio = in.readString();
        fee_ph = in.readInt();
        rating = in.readDouble();
        ratingsOf1 = in.readInt();
        ratingsOf2 = in.readInt();
        ratingsOf3 = in.readInt();
        ratingsOf4 = in.readInt();
        ratingsOf5 = in.readInt();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(phone_number);
        parcel.writeString(bio);
        parcel.writeInt(fee_ph);
        parcel.writeDouble(rating);
        parcel.writeInt(ratingsOf1);
        parcel.writeInt(ratingsOf2);
        parcel.writeInt(ratingsOf3);
        parcel.writeInt(ratingsOf4);
        parcel.writeInt(ratingsOf5);
    }
}

