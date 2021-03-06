package mx.iteso.escalaapp.beans;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mx.iteso.escalaapp.fragmentgym.AdapterGym;

/**
 * Created by aceve on 03/03/2018.
 */

public class Climber {
    private String firstname;
    private String lastname;
    private String category;
    private String gym;
    private String image;
    private String thumb;
    private String description;
    private String city;
    private String state;
    private String owner = "false";
    private String key;


    public Climber() {
        this.firstname = "";
        this.lastname = "";
        this.gym = "";
        this.description = "";
        this.city = "";
        this.state = "";
        this.image = "";
        this.thumb = "";
        this.category = "";
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Climber(String firstname, String lastname, String gym, String image) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.gym = gym;
        this.image = image;
    }

    public Climber(String firstname, String lastname, String gym, String image, String description, String city, String state) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.gym = gym;
        this.image = image;
        this.description = description;
        this.city = city;
        this.state = state;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return firstname + " " + lastname;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGym() {
        return gym;
    }

    public void setGym(String gym) {
        this.gym = gym;
    }


}
