package group9.softwareengineering;

import android.location.Location;

public class Profile {

    private String name;
    private String phone_number;
    private String bio;
    private int fee_ph;
    private int rating;
    private Location location;

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

    public int getRating() {
        return rating;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


}