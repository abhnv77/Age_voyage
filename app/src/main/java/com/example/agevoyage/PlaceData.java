package com.example.agevoyage;

public class PlaceData {
    private int id;
    private String name;
    private String bestTime;
    private String ageCategory;

    public PlaceData() {
        // Default constructor
    }

    public PlaceData(int id, String name, String bestTime, String ageCategory) {
        this.id = id;
        this.name = name;
        this.bestTime = bestTime;
        this.ageCategory = ageCategory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBestTime() {
        return bestTime;
    }

    public void setBestTime(String bestTime) {
        this.bestTime = bestTime;
    }

    public String getAgeCategory() {
        return ageCategory;
    }

    public void setAgeCategory(String ageCategory) {
        this.ageCategory = ageCategory;
    }
}
