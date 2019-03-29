package group9.softwareengineering;

import android.location.Location;

public class Profile {

    private String name,phone_number,bio,email,password;
    private int fee_ph,rating;
    private Location location;


    public Profile(String email, String password, String name, String phone_number) {
        this.name = name;
        this.phone_number = phone_number;
        this.email = email;
        this.password = password;
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

    public int getRating() {
        return rating;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
