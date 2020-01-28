package com.example.comlancer.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class ImagesContainer implements Serializable {
    private ArrayList<ComlancerImages> ImageList = new ArrayList<>();

    public void ImagesContainer() {
    }

    public ArrayList<ComlancerImages> getImageList() {
        return ImageList;
    }

    public void setImageList(ArrayList<ComlancerImages> imageList) {
        ImageList = imageList;
    }
}
