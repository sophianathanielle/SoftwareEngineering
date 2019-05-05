package group9.softwareengineering;

import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Posting {

    private String id;
    private String poster;
    private String poster_id;
    private Date start_time;
    private Date end_time;
    private GeoPoint location;
    private String description;
    private int payment;
    private ArrayList<String> petIDs = new ArrayList<>();
    private ArrayList<String> sitters_interested = new ArrayList<>();
    private Boolean completed;
    private String sitter_found;
    private String photoURL;
    private Boolean photo_request;
    private Boolean location_request;

    public Posting() {

    }

    public Boolean getPhoto_request() {
        return photo_request;
    }

    public void setPhoto_request(Boolean photo_request) {
        this.photo_request = photo_request;
    }

    public Boolean getLocation_request() {
        return location_request;
    }

    public void setLocation_request(Boolean location_request) {
        this.location_request = location_request;
    }

    public Posting(String poster_id, Date start_time, Date end_time, String description) {
        this.poster_id = poster_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.description = description;
        this.photo_request = false;
        this.location_request = false;
        // maybe create these values in Cloud Func on doc creation
        this.completed = false;
        this.sitter_found = null;
    }

    public Posting(String poster, String poster_id, Date start_time, Date end_time, GeoPoint location, String description, int payment, ArrayList<String> petIDs, boolean location_request , boolean photo_request) {
        this.poster = poster;
        this.poster_id = poster_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.location = location;
        this.description = description;
        this.payment = payment;
        this.petIDs = petIDs;
        this.location_request = location_request;
        this.photo_request = photo_request;
        // maybe create these values in Cloud Func on doc creation
        this.completed = false;
        this.sitter_found = null;
    }

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

    public ArrayList<String> getPetIDs() {
        return petIDs;
    }

    public String getDescription(){
        return description;
    }

    public ArrayList<String> getSitters_interested() {
        return sitters_interested;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getSitter_found() {
        return sitter_found;
    }
}