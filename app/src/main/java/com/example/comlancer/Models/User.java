package com.example.comlancer.Models;

import java.io.Serializable;

public class User implements Serializable {
    private String firebaseUserId;
    private String email;
    private String Name;
    private String password;
    private String info;
    private String imageUrl;
    private String role;
private String Tag;



    private float ratingSum;
    private int numberOfFeedbacks;
    private float averageRating;
    private RatingFeedBackContainer myRatingFeedback;
    private String logoUrl;





    public String getPassword() {
        return password;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirebaseUserId() {
        return firebaseUserId;
    }

    public void setFirebaseUserId(String firebaseUserId) {
        this.firebaseUserId = firebaseUserId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String fullName) {
        this.Name = fullName;
    }

    public String getImageUrl() {
        return imageUrl;
    }


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public float getRatingSum() {
        return ratingSum;
    }

    public void setRatingSum(float ratingSum) {
        this.ratingSum = ratingSum;
    }

    public int getNumberOfFeedbacks() {
        return numberOfFeedbacks;
    }

    public void setNumberOfFeedbacks(int numberOfFeedbacks) {
        this.numberOfFeedbacks = numberOfFeedbacks;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public RatingFeedBackContainer getMyRatingFeedback() {
        return myRatingFeedback;
    }

    public void setMyRatingFeedback(RatingFeedBackContainer myRatingFeedback) {
        this.myRatingFeedback = myRatingFeedback;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }



}
