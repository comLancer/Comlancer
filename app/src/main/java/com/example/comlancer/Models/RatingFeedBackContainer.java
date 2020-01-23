package com.example.comlancer.Models;

import java.util.ArrayList;

public class RatingFeedBackContainer {

    public RatingFeedBackContainer() {
    }

    ArrayList<RatingFeedback> feedbackList = new ArrayList<>();

    public ArrayList<RatingFeedback> getFeedbackList() {
        return feedbackList;
    }

    public void setFeedbackList(ArrayList<RatingFeedback> feedbackList) {
        this.feedbackList = feedbackList;
    }
}
