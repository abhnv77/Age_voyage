package com.example.agevoyage;

public class state_MODEL {
    int image;
    String name, state;

    public state_MODEL(int image, String state, String name) {
        this.image = image;
        this.state = state;
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String state() {
        return state;
    }

    public String name() {
        return name;
    }

    public interface OnStateClickListener {
    }
}
