package group9.softwareengineering;

import android.location.Location;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class Posting {

    private String owner;
    private String owner_id;
    private Date start_time;
    private Date end_time;
    private Location location;
    private int payment;
    private String[] petIDs;
    private ArrayList<Pet> pets = new ArrayList<>();

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public String[] getPetIDs() {
        return petIDs;
    }

    public void addPet(Pet pet) { pets.add(pet); }

    public String getOwner_id() {
        return owner_id;
    }
}
