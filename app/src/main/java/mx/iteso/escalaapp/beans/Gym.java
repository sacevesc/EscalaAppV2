package mx.iteso.escalaapp.beans;

import android.widget.Button;

/**
 * Created by aceve on 03/03/2018.
 */

public class Gym {
    private String name;
    private String description;
    private String city;
    private int photo;
    private Button Competitions;

    public Gym(String name, String description, String city, int photo) {
        this.name = name;
        this.description = description;
        this.city= city;
       this.photo= photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public Button getCompetitions() {
        return Competitions;
    }

    public void setCompetitions(Button competitions) {
        Competitions = competitions;
    }
}