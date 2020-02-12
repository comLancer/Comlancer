package com.example.comlancer.Models;

import java.util.Comparator;

public class RatingCompare implements Comparator<User> {
    // Used for sorting in ascending order of
    // roll number
    public int compare(User a, User b) {
        return (int) (a.getAverageRating() - b.getAverageRating());
    }
}
