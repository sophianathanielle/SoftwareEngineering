package group9.softwareengineering;

import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Date;

public class Posting {

    private String id;
    private String poster;
    private String poster_id;
    private Date start_time;
    private Date end_time;
    private GeoPoint location;
    private String description;
    private int payment;
    private String petID;
    private int in_progress;
    private ArrayList<Pet> pets = new ArrayList<>();

    public Posting() { }

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

    public void setPetID(String petID) {
        this.petID = petID;
    }

    public int getIn_progress() {
        return in_progress;
    }

    public void setIn_progress(int in_progress) {
        this.in_progress = in_progress;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public String getPetID() {
        return petID;
    }

    public void addPet(Pet pet) { pets.add(pet); }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
