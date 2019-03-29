package group9.softwareengineering;

import java.util.Date;

public class Review {

    private int rating;
    private Date review_date;
    private String review_text;

    public Review(int rating, Date review_date, String review_text){
        this.rating = rating;
        this.review_date = review_date;
        this.review_text = review_text;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getReview_date() {
        return review_date;
    }

    public void setReview_date(Date review_date) {
        this.review_date = review_date;
    }

    public String getReview_text() {
        return review_text;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }
}
