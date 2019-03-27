package group9.softwareengineering;

import android.location.Location;

import java.util.ArrayList;
import java.util.Date;

public class Posting {

    private String id;
    private String poster;
    private String poster_id;
    private Date start_time;
    private Date end_time;
    private Location location;
    private String description;
    private int payment;
    private String[] petIDs;
    private ArrayList<Pet> pets = new ArrayList<>();

    public String getID() {
        return id;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPoster_id() {
        return poster_id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
